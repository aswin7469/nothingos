package java.nio.channels;

import java.p026io.IOException;

public interface AsynchronousChannel extends Channel {
    void close() throws IOException;
}
