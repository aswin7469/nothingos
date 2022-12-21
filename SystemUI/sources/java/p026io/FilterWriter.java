package java.p026io;

/* renamed from: java.io.FilterWriter */
public abstract class FilterWriter extends Writer {
    protected Writer out;

    protected FilterWriter(Writer writer) {
        super(writer);
        this.out = writer;
    }

    public void write(int i) throws IOException {
        this.out.write(i);
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        this.out.write(cArr, i, i2);
    }

    public void write(String str, int i, int i2) throws IOException {
        this.out.write(str, i, i2);
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void close() throws IOException {
        this.out.close();
    }
}
