package java.p026io;

/* renamed from: java.io.BufferedOutputStream */
public class BufferedOutputStream extends FilterOutputStream {
    protected byte[] buf;
    protected int count;

    public BufferedOutputStream(OutputStream outputStream) {
        this(outputStream, 8192);
    }

    public BufferedOutputStream(OutputStream outputStream, int i) {
        super(outputStream);
        if (i > 0) {
            this.buf = new byte[i];
            return;
        }
        throw new IllegalArgumentException("Buffer size <= 0");
    }

    private void flushBuffer() throws IOException {
        if (this.count > 0) {
            this.out.write(this.buf, 0, this.count);
            this.count = 0;
        }
    }

    public synchronized void write(int i) throws IOException {
        if (this.count >= this.buf.length) {
            flushBuffer();
        }
        byte[] bArr = this.buf;
        int i2 = this.count;
        this.count = i2 + 1;
        bArr[i2] = (byte) i;
    }

    public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
        byte[] bArr2 = this.buf;
        if (i2 >= bArr2.length) {
            flushBuffer();
            this.out.write(bArr, i, i2);
            return;
        }
        if (i2 > bArr2.length - this.count) {
            flushBuffer();
        }
        System.arraycopy((Object) bArr, i, (Object) this.buf, this.count, i2);
        this.count += i2;
    }

    public synchronized void flush() throws IOException {
        flushBuffer();
        this.out.flush();
    }
}
