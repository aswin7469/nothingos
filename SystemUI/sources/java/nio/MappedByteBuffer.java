package java.nio;

import java.p026io.FileDescriptor;
import sun.misc.Unsafe;

public abstract class MappedByteBuffer extends ByteBuffer {
    private static byte unused;

    /* renamed from: fd */
    private final FileDescriptor f570fd;

    private native void force0(FileDescriptor fileDescriptor, long j, long j2);

    private native boolean isLoaded0(long j, long j2, int i);

    private native void load0(long j, long j2);

    MappedByteBuffer(int i, int i2, int i3, int i4, FileDescriptor fileDescriptor) {
        super(i, i2, i3, i4);
        this.f570fd = fileDescriptor;
    }

    MappedByteBuffer(int i, int i2, int i3, int i4, byte[] bArr, int i5) {
        super(i, i2, i3, i4, bArr, i5);
        this.f570fd = null;
    }

    MappedByteBuffer(int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
        this.f570fd = null;
    }

    private void checkMapped() {
        if (this.f570fd == null) {
            throw new UnsupportedOperationException();
        }
    }

    private long mappingOffset() {
        long pageSize = (long) Bits.pageSize();
        long j = this.address % pageSize;
        return j >= 0 ? j : j + pageSize;
    }

    private long mappingAddress(long j) {
        return this.address - j;
    }

    private long mappingLength(long j) {
        return ((long) capacity()) + j;
    }

    public final boolean isLoaded() {
        checkMapped();
        if (this.address == 0 || capacity() == 0) {
            return true;
        }
        long mappingOffset = mappingOffset();
        long mappingLength = mappingLength(mappingOffset);
        return isLoaded0(mappingAddress(mappingOffset), mappingLength, Bits.pageCount(mappingLength));
    }

    public final MappedByteBuffer load() {
        checkMapped();
        if (!(this.address == 0 || capacity() == 0)) {
            long mappingOffset = mappingOffset();
            long mappingLength = mappingLength(mappingOffset);
            load0(mappingAddress(mappingOffset), mappingLength);
            Unsafe unsafe = Unsafe.getUnsafe();
            int pageSize = Bits.pageSize();
            int pageCount = Bits.pageCount(mappingLength);
            long mappingAddress = mappingAddress(mappingOffset);
            byte b = 0;
            for (int i = 0; i < pageCount; i++) {
                b = (byte) (b ^ unsafe.getByte(mappingAddress));
                mappingAddress += (long) pageSize;
            }
            if (unused != 0) {
                unused = b;
            }
        }
        return this;
    }

    public final MappedByteBuffer force() {
        checkMapped();
        if (!(this.address == 0 || capacity() == 0)) {
            long mappingOffset = mappingOffset();
            force0(this.f570fd, mappingAddress(mappingOffset), mappingLength(mappingOffset));
        }
        return this;
    }
}
