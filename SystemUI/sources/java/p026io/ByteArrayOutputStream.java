package java.p026io;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

/* renamed from: java.io.ByteArrayOutputStream */
public class ByteArrayOutputStream extends OutputStream {
    private static final int MAX_ARRAY_SIZE = 2147483639;
    protected byte[] buf;
    protected int count;

    public void close() throws IOException {
    }

    public ByteArrayOutputStream() {
        this(32);
    }

    public ByteArrayOutputStream(int i) {
        if (i >= 0) {
            this.buf = new byte[i];
            return;
        }
        throw new IllegalArgumentException("Negative initial size: " + i);
    }

    private void ensureCapacity(int i) {
        if (i - this.buf.length > 0) {
            grow(i);
        }
    }

    private void grow(int i) {
        int length = this.buf.length << 1;
        if (length - i < 0) {
            length = i;
        }
        if (length - MAX_ARRAY_SIZE > 0) {
            length = hugeCapacity(i);
        }
        this.buf = Arrays.copyOf(this.buf, length);
    }

    private static int hugeCapacity(int i) {
        if (i < 0) {
            throw new OutOfMemoryError();
        } else if (i > MAX_ARRAY_SIZE) {
            return Integer.MAX_VALUE;
        } else {
            return MAX_ARRAY_SIZE;
        }
    }

    public synchronized void write(int i) {
        ensureCapacity(this.count + 1);
        byte[] bArr = this.buf;
        int i2 = this.count;
        bArr[i2] = (byte) i;
        this.count = i2 + 1;
    }

    public synchronized void write(byte[] bArr, int i, int i2) {
        Objects.checkFromIndexSize(i, i2, bArr.length);
        ensureCapacity(this.count + i2);
        System.arraycopy((Object) bArr, i, (Object) this.buf, this.count, i2);
        this.count += i2;
    }

    public void writeBytes(byte[] bArr) {
        write(bArr, 0, bArr.length);
    }

    public synchronized void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.buf, 0, this.count);
    }

    public synchronized void reset() {
        this.count = 0;
    }

    public synchronized byte[] toByteArray() {
        return Arrays.copyOf(this.buf, this.count);
    }

    public synchronized int size() {
        return this.count;
    }

    public synchronized String toString() {
        return new String(this.buf, 0, this.count);
    }

    public synchronized String toString(String str) throws UnsupportedEncodingException {
        return new String(this.buf, 0, this.count, str);
    }

    public synchronized String toString(Charset charset) {
        return new String(this.buf, 0, this.count, charset);
    }

    @Deprecated
    public synchronized String toString(int i) {
        return new String(this.buf, i, 0, this.count);
    }
}
