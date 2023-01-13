package java.p026io;

/* renamed from: java.io.FilterReader */
public abstract class FilterReader extends Reader {

    /* renamed from: in */
    protected Reader f520in;

    protected FilterReader(Reader reader) {
        super(reader);
        this.f520in = reader;
    }

    public int read() throws IOException {
        return this.f520in.read();
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        return this.f520in.read(cArr, i, i2);
    }

    public long skip(long j) throws IOException {
        return this.f520in.skip(j);
    }

    public boolean ready() throws IOException {
        return this.f520in.ready();
    }

    public boolean markSupported() {
        return this.f520in.markSupported();
    }

    public void mark(int i) throws IOException {
        this.f520in.mark(i);
    }

    public void reset() throws IOException {
        this.f520in.reset();
    }

    public void close() throws IOException {
        this.f520in.close();
    }
}
