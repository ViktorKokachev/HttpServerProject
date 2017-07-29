package httpserver;

import javax.swing.text.html.HTML;
import java.net.Socket;

public class HelloWorldServer extends SocketProcessor{

    public static final String HTML = "<html><body><head><meta charset=\"utf-8\"><head><h1>АЛЛО ДЯДЯ</h1></body></html>";

    public HelloWorldServer(Socket s) throws Throwable {
        super(s);
    }

    @Override
    protected String mapRequest(HttpRequest httpRequest) {
        return HTML;
    }
}
