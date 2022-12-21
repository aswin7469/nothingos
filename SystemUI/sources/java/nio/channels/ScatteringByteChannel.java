package java.nio.channels;

import java.nio.ByteBuffer;
import java.p026io.IOException;

public interface ScatteringByteChannel extends ReadableByteChannel {
    long read(ByteBuffer[] byteBufferArr) throws IOException;

    long read(ByteBuffer[] byteBufferArr, int i, int i2) throws IOException;
}
