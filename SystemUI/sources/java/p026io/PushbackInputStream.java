package java.p026io;

/* renamed from: java.io.PushbackInputStream */
public class PushbackInputStream extends FilterInputStream {
    protected byte[] buf;
    protected int pos;

    public boolean markSupported() {
        return false;
    }

    private void ensureOpen() throws IOException {
        if (this.f521in == null) {
            throw new IOException("Stream closed");
        }
    }

    public PushbackInputStream(InputStream inputStream, int i) {
        super(inputStream);
        if (i > 0) {
            this.buf = new byte[i];
            this.pos = i;
            return;
        }
        throw new IllegalArgumentException("size <= 0");
    }

    public PushbackInputStream(InputStream inputStream) {
        this(inputStream, 1);
    }

    public int read() throws IOException {
        ensureOpen();
        int i = this.pos;
        byte[] bArr = this.buf;
        if (i >= bArr.length) {
            return super.read();
        }
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        ensureOpen();
        bArr.getClass();
        if (i < 0 || i2 < 0 || i2 > bArr.length - i) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            byte[] bArr2 = this.buf;
            int length = bArr2.length;
            int i3 = this.pos;
            int i4 = length - i3;
            if (i4 > 0) {
                if (i2 < i4) {
                    i4 = i2;
                }
                System.arraycopy((Object) bArr2, i3, (Object) bArr, i, i4);
                this.pos += i4;
                i += i4;
                i2 -= i4;
            }
            if (i2 <= 0) {
                return i4;
            }
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                return i4 + read;
            }
            if (i4 == 0) {
                return -1;
            }
            return i4;
        }
    }

    public void unread(int i) throws IOException {
        ensureOpen();
        int i2 = this.pos;
        if (i2 != 0) {
            byte[] bArr = this.buf;
            int i3 = i2 - 1;
            this.pos = i3;
            bArr[i3] = (byte) i;
            return;
        }
        throw new IOException("Push back buffer is full");
    }

    public void unread(byte[] bArr, int i, int i2) throws IOException {
        ensureOpen();
        int i3 = this.pos;
        if (i2 <= i3) {
            int i4 = i3 - i2;
            this.pos = i4;
            System.arraycopy((Object) bArr, i, (Object) this.buf, i4, i2);
            return;
        }
        throw new IOException("Push back buffer is full");
    }

    public void unread(byte[] bArr) throws IOException {
        unread(bArr, 0, bArr.length);
    }

    public int available() throws IOException {
        ensureOpen();
        int length = this.buf.length - this.pos;
        int available = super.available();
        if (length > Integer.MAX_VALUE - available) {
            return Integer.MAX_VALUE;
        }
        return length + available;
    }

    public long skip(long j) throws IOException {
        ensureOpen();
        if (j <= 0) {
            return 0;
        }
        int length = this.buf.length;
        int i = this.pos;
        long j2 = (long) (length - i);
        if (j2 > 0) {
            if (j < j2) {
                j2 = j;
            }
            this.pos = (int) (((long) i) + j2);
            j -= j2;
        }
        return j > 0 ? j2 + super.skip(j) : j2;
    }

    public synchronized void mark(int i) {
    }

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public synchronized void close() throws IOException {
        if (this.f521in != null) {
            this.f521in.close();
            this.f521in = null;
            this.buf = null;
        }
    }
}
