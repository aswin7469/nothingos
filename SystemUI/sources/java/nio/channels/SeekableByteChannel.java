package java.nio.channels;

import java.nio.ByteBuffer;
import java.p026io.IOException;

public interface SeekableByteChannel extends ByteChannel {
    long position() throws IOException;

    SeekableByteChannel position(long j) throws IOException;

    int read(ByteBuffer byteBuffer) throws IOException;

    long size() throws IOException;

    SeekableByteChannel truncate(long j) throws IOException;

    int write(ByteBuffer byteBuffer) throws IOException;
}
