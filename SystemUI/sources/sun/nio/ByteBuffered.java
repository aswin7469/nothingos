package sun.nio;

import java.nio.ByteBuffer;
import java.p026io.IOException;

public interface ByteBuffered {
    ByteBuffer getByteBuffer() throws IOException;
}
