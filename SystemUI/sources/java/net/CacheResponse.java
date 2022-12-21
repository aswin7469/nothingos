package java.net;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.util.List;
import java.util.Map;

public abstract class CacheResponse {
    public abstract InputStream getBody() throws IOException;

    public abstract Map<String, List<String>> getHeaders() throws IOException;
}
