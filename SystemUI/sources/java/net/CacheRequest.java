package java.net;

import java.p026io.IOException;
import java.p026io.OutputStream;

public abstract class CacheRequest {
    public abstract void abort();

    public abstract OutputStream getBody() throws IOException;
}
