package java.nio.channels;

import java.nio.ByteBuffer;
import java.p026io.IOException;

public interface GatheringByteChannel extends WritableByteChannel {
    long write(ByteBuffer[] byteBufferArr) throws IOException;

    long write(ByteBuffer[] byteBufferArr, int i, int i2) throws IOException;
}
