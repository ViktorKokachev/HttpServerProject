package httpserver;

import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    @SneakyThrows
    public static void main(String... args) {
        try (ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]))) {
            System.out.println("Server started, please visit: http://localhost:" + args[0]);
            while (!Thread.currentThread().isInterrupted()) {
                Socket s = ss.accept();
                new Thread(new HelloWorldServer(s)).start();
            }
        }
    }
}
