package httpserver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import java.io.*;
import java.net.Socket;
import java.util.stream.Collectors;

class HttpServerTest {

    public static final int PORT = 8080;
    public static final String REQUEST = "GET / HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Cache-Control: max-age=0\r\n" +
            "Upgrade-Insecure-Requests: 1\r\n" +
             "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36\r\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n" +
            "Accept-Encoding: gzip, deflate, br\r\n" +
            "Accept-Language: en-US,en;q=0.8\r\n\r\n";

    static Thread serverThread;

    @BeforeAll
        static void setUp() {
        serverThread =
                new Thread(() -> HttpServer.main(String.valueOf(PORT)), "server"); // TODO: 28.07.17 refactor
        serverThread.start();
    }

    @AfterAll
    static void tearDown() {
        serverThread.interrupt();
    }

    @Test
    void ping() throws Throwable { // TODO: 28.07.17 SneakyThrows 

        try (Socket socket = new Socket("localhost", PORT);
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(
                 new InputStreamReader(socket.getInputStream()))) {
            outputStream.write(REQUEST.getBytes());
            //outputStream.close();

            String response = bufferedReader.lines()
                    //.filter(s -> s.trim().length() > 0)
                    .collect(Collectors.joining("\r\n"));


            String s = HelloWorldServer.HTML;
            assertThat(response, is(String.format(SocketProcessor.RESPONSE, s.length(), s)));

            String line;
            while((line = bufferedReader.readLine()) != null && !line.trim().isEmpty()) {
                System.out.println(line);
            }
        }
    }

}