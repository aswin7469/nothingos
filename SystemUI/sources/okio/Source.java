package okio;

import java.p026io.Closeable;
import java.p026io.IOException;

public interface Source extends Closeable {
    void close() throws IOException;

    long read(Buffer buffer, long j) throws IOException;

    Timeout timeout();
}
