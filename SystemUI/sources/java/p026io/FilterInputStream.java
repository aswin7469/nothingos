package java.p026io;

/* renamed from: java.io.FilterInputStream */
public class FilterInputStream extends InputStream {

    /* renamed from: in */
    protected volatile InputStream f519in;

    protected FilterInputStream(InputStream inputStream) {
        this.f519in = inputStream;
    }

    public int read() throws IOException {
        return this.f519in.read();
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        return this.f519in.read(bArr, i, i2);
    }

    public long skip(long j) throws IOException {
        return this.f519in.skip(j);
    }

    public int available() throws IOException {
        return this.f519in.available();
    }

    public void close() throws IOException {
        this.f519in.close();
    }

    public synchronized void mark(int i) {
        this.f519in.mark(i);
    }

    public synchronized void reset() throws IOException {
        this.f519in.reset();
    }

    public boolean markSupported() {
        return this.f519in.markSupported();
    }
}
