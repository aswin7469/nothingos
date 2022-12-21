package libcore.p030io;

import android.system.ErrnoException;
import android.system.OsConstants;
import java.nio.ByteOrder;
import java.p026io.FileDescriptor;

/* renamed from: libcore.io.MemoryMappedFile */
public final class MemoryMappedFile implements AutoCloseable {
    private final long address;
    private boolean closed;
    private final int size;

    public MemoryMappedFile(long j, long j2) {
        this.address = j;
        if (j2 < 0 || j2 > 2147483647L) {
            throw new IllegalArgumentException("Unsupported file size=" + j2);
        }
        this.size = (int) j2;
    }

    public static MemoryMappedFile mmapRO(String str) throws ErrnoException {
        FileDescriptor open = Libcore.f857os.open(str, OsConstants.O_RDONLY, 0);
        try {
            long j = Libcore.f857os.fstat(open).st_size;
            return new MemoryMappedFile(Libcore.f857os.mmap(0, j, OsConstants.PROT_READ, OsConstants.MAP_SHARED, open, 0), j);
        } finally {
            Libcore.f857os.close(open);
        }
    }

    public void close() throws ErrnoException {
        if (!this.closed) {
            this.closed = true;
            Libcore.f857os.munmap(this.address, (long) this.size);
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    public BufferIterator bigEndianIterator() {
        return new NioBufferIterator(this, this.address, this.size, ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN);
    }

    public BufferIterator littleEndianIterator() {
        return new NioBufferIterator(this, this.address, this.size, ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN);
    }

    /* access modifiers changed from: package-private */
    public void checkNotClosed() {
        if (this.closed) {
            throw new IllegalStateException("MemoryMappedFile is closed");
        }
    }

    public int size() {
        checkNotClosed();
        return this.size;
    }
}
