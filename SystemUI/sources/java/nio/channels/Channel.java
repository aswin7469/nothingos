package java.nio.channels;

import java.p026io.Closeable;
import java.p026io.IOException;

public interface Channel extends Closeable {
    void close() throws IOException;

    boolean isOpen();
}
