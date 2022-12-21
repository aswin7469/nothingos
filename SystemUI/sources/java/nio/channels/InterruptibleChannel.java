package java.nio.channels;

import java.p026io.IOException;

public interface InterruptibleChannel extends Channel {
    void close() throws IOException;
}
