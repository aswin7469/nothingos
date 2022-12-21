package java.nio.channels;

import java.nio.ByteBuffer;
import java.p026io.IOException;

public interface WritableByteChannel extends Channel {
    int write(ByteBuffer byteBuffer) throws IOException;
}
