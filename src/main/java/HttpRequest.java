import java.util.Map;

public interface HttpRequest {

    HttpMethod getMethod ();

    String getPath();

    Map<String, String> getParams();

    default String getHostAndPort() {
        return getHeaders().get("Host");
    }

    Map<String, String> getHeaders();

    String getBody();
}
