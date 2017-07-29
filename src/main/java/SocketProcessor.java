import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class SocketProcessor implements Runnable {

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
            readInputHeaders();
            writeResponse("<html><body><h1>Hello from Habrahabr</h1></body></html>");
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

    private void writeResponse(String s) throws Throwable {
        os.write(String.format(RESPONSE, s.length(), s).getBytes());
        os.flush();
    }

    private void readInputHeaders() throws Throwable {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s;
        while((s = br.readLine()) != null && !s.trim().isEmpty()) {
            // TODO: 28.07.17 save headers
            System.out.println(s);
        }
    }
}