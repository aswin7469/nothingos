package java.nio.channels;

import java.nio.ByteBuffer;
import java.p026io.IOException;

public interface ReadableByteChannel extends Channel {
    int read(ByteBuffer byteBuffer) throws IOException;
}
