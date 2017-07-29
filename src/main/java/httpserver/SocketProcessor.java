package httpserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class SocketProcessor implements Runnable {

    public static final String RESPONSE =
            "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: %d\r\n" +
                "Connection: close\r\n\r\n%s";

    private Socket s;
    private InputStream is;
    private OutputStream os;

    public SocketProcessor(Socket s) throws Throwable {
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
    }

    public void run() {
        try {
            writeResponse(
                    mapRequest(getHttpRequest())
            );
        } catch (Throwable t) {
                /*do nothing*/
        } finally {
            try {
                s.close();
            } catch (Throwable t) {
                    /*do nothing*/
            }
        }
        System.err.println("Client processing finished");
    }

    abstract protected String mapRequest(HttpRequest httpRequest);


    private void writeResponse(String s) throws Throwable {
        os.write(String.format(RESPONSE, s.length(), s).getBytes());
        os.flush();
    }

    private HttpRequest getHttpRequest() throws Throwable {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = br.readLine().trim();

        String[] contentWithTail = s.split("\\s", 2);
        HttpMethod httpMethod = HttpMethod.valueOf(contentWithTail[0]);

        contentWithTail = contentWithTail[1].split("[?\\s]", 2);
        String path = contentWithTail[0];

        Map<String,String> params = contentWithTail[1].startsWith("HTTP") ?
                Collections.emptyMap():
                getParams(contentWithTail[1].split("\\s", 2)[1]);

        Map<String, String> headers = new HashMap<String, String>();
        while((s = br.readLine()) != null && !s.trim().isEmpty()) {
            String[] header = s.split(":\\s", 2);
            headers.put(header[0], header[1]);
            //System.out.println(s);
        }

        StringBuilder body = new StringBuilder();

        //if (httpMethod == httpserver.HttpMethod.PUT || httpMethod == httpserver.HttpMethod.POST)
        while (br.ready() && (s = br.readLine()) != null)
            body.append(s);

        return HttpRequest.from(httpMethod, path, params, headers, body.toString());
    }

    private Map<String, String> getParams(String string) {
        Map<String, String> params = new HashMap<String, String>();
        for (String param : string.split("&")) {
            String[] split = param.split("=", 2);
            params.put(split[0], split[1]);
        }
        return params;
    }
}