package libcore.p030io;

/* renamed from: libcore.io.BufferIterator */
public abstract class BufferIterator {
    public abstract int pos();

    public abstract byte readByte();

    public abstract void readByteArray(byte[] bArr, int i, int i2);

    public abstract int readInt();

    public abstract void readIntArray(int[] iArr, int i, int i2);

    public abstract void readLongArray(long[] jArr, int i, int i2);

    public abstract short readShort();

    public abstract void seek(int i);

    public abstract void skip(int i);
}
