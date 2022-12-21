package java.p026io;

/* renamed from: java.io.FilterInputStream */
public class FilterInputStream extends InputStream {

    /* renamed from: in */
    protected volatile InputStream f521in;

    protected FilterInputStream(InputStream inputStream) {
        this.f521in = inputStream;
    }

    public int read() throws IOException {
        return this.f521in.read();
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        return this.f521in.read(bArr, i, i2);
    }

    public long skip(long j) throws IOException {
        return this.f521in.skip(j);
    }

    public int available() throws IOException {
        return this.f521in.available();
    }

    public void close() throws IOException {
        this.f521in.close();
    }

    public synchronized void mark(int i) {
        this.f521in.mark(i);
    }

    public synchronized void reset() throws IOException {
        this.f521in.reset();
    }

    public boolean markSupported() {
        return this.f521in.markSupported();
    }
}
