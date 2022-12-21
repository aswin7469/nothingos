package java.p026io;

/* renamed from: java.io.StringWriter */
public class StringWriter extends Writer {
    private StringBuffer buf;

    public void close() throws IOException {
    }

    public void flush() {
    }

    public StringWriter() {
        StringBuffer stringBuffer = new StringBuffer();
        this.buf = stringBuffer;
        this.lock = stringBuffer;
    }

    public StringWriter(int i) {
        if (i >= 0) {
            StringBuffer stringBuffer = new StringBuffer(i);
            this.buf = stringBuffer;
            this.lock = stringBuffer;
            return;
        }
        throw new IllegalArgumentException("Negative buffer size");
    }

    public void write(int i) {
        this.buf.append((char) i);
    }

    public void write(char[] cArr, int i, int i2) {
        int i3;
        if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 != 0) {
            this.buf.append(cArr, i, i2);
        }
    }

    public void write(String str) {
        this.buf.append(str);
    }

    public void write(String str, int i, int i2) {
        this.buf.append((CharSequence) str, i, i2 + i);
    }

    public StringWriter append(CharSequence charSequence) {
        write(String.valueOf((Object) charSequence));
        return this;
    }

    public StringWriter append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        return append(charSequence.subSequence(i, i2));
    }

    public StringWriter append(char c) {
        write((int) c);
        return this;
    }

    public String toString() {
        return this.buf.toString();
    }

    public StringBuffer getBuffer() {
        return this.buf;
    }
}
