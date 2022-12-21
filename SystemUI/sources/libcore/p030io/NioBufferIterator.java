package libcore.p030io;

/* renamed from: libcore.io.NioBufferIterator */
public final class NioBufferIterator extends BufferIterator {
    private final long address;
    private final MemoryMappedFile file;
    private final int length;
    private int position;
    private final boolean swap;

    NioBufferIterator(MemoryMappedFile memoryMappedFile, long j, int i, boolean z) {
        memoryMappedFile.checkNotClosed();
        this.file = memoryMappedFile;
        this.address = j;
        if (i < 0) {
            throw new IllegalArgumentException("length < 0");
        } else if (Long.compareUnsigned(j, -1 - ((long) i)) <= 0) {
            this.length = i;
            this.swap = z;
        } else {
            throw new IllegalArgumentException("length " + i + " would overflow 64-bit address space");
        }
    }

    public void seek(int i) {
        this.position = i;
    }

    public void skip(int i) {
        this.position += i;
    }

    public int pos() {
        return this.position;
    }

    public void readByteArray(byte[] bArr, int i, int i2) {
        checkArrayBounds(i, bArr.length, i2);
        this.file.checkNotClosed();
        checkReadBounds(this.position, this.length, i2);
        Memory.peekByteArray(this.address + ((long) this.position), bArr, i, i2);
        this.position += i2;
    }

    public byte readByte() {
        this.file.checkNotClosed();
        checkReadBounds(this.position, this.length, 1);
        byte peekByte = Memory.peekByte(this.address + ((long) this.position));
        this.position++;
        return peekByte;
    }

    public int readInt() {
        this.file.checkNotClosed();
        checkReadBounds(this.position, this.length, 4);
        int peekInt = Memory.peekInt(this.address + ((long) this.position), this.swap);
        this.position += 4;
        return peekInt;
    }

    public void readIntArray(int[] iArr, int i, int i2) {
        checkArrayBounds(i, iArr.length, i2);
        this.file.checkNotClosed();
        int i3 = i2 * 4;
        checkReadBounds(this.position, this.length, i3);
        Memory.peekIntArray(this.address + ((long) this.position), iArr, i, i2, this.swap);
        this.position += i3;
    }

    public void readLongArray(long[] jArr, int i, int i2) {
        checkArrayBounds(i, jArr.length, i2);
        this.file.checkNotClosed();
        int i3 = i2 * 8;
        checkReadBounds(this.position, this.length, i3);
        Memory.peekLongArray(this.address + ((long) this.position), jArr, i, i2, this.swap);
        this.position += i3;
    }

    public short readShort() {
        this.file.checkNotClosed();
        checkReadBounds(this.position, this.length, 2);
        short peekShort = Memory.peekShort(this.address + ((long) this.position), this.swap);
        this.position += 2;
        return peekShort;
    }

    private static void checkReadBounds(int i, int i2, int i3) {
        if (i < 0 || i3 < 0) {
            throw new IndexOutOfBoundsException("Invalid read args: position=" + i + ", byteCount=" + i3);
        }
        int i4 = i + i3;
        if (i4 < 0 || i4 > i2) {
            throw new IndexOutOfBoundsException("Read outside range: position=" + i + ", byteCount=" + i3 + ", length=" + i2);
        }
    }

    private static void checkArrayBounds(int i, int i2, int i3) {
        if (i < 0 || i3 < 0) {
            throw new IndexOutOfBoundsException("Invalid args: arrayOffset=" + i + ", count=" + i3);
        }
        int i4 = i + i3;
        if (i4 < 0 || i4 > i2) {
            throw new IndexOutOfBoundsException("Write outside range: arrayLength=" + i2 + ", arrayOffset=" + i + ", count=" + i3);
        }
    }
}
