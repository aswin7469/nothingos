package java.p026io;

/* renamed from: java.io.BufferedWriter */
public class BufferedWriter extends Writer {
    private static int defaultCharBufferSize = 8192;

    /* renamed from: cb */
    private char[] f512cb;
    private int nChars;
    private int nextChar;
    private Writer out;

    private int min(int i, int i2) {
        return i < i2 ? i : i2;
    }

    public BufferedWriter(Writer writer) {
        this(writer, defaultCharBufferSize);
    }

    public BufferedWriter(Writer writer, int i) {
        super(writer);
        if (i > 0) {
            this.out = writer;
            this.f512cb = new char[i];
            this.nChars = i;
            this.nextChar = 0;
            return;
        }
        throw new IllegalArgumentException("Buffer size <= 0");
    }

    private void ensureOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }

    /* access modifiers changed from: package-private */
    public void flushBuffer() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i = this.nextChar;
            if (i != 0) {
                this.out.write(this.f512cb, 0, i);
                this.nextChar = 0;
            }
        }
    }

    public void write(int i) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.nextChar >= this.nChars) {
                flushBuffer();
            }
            char[] cArr = this.f512cb;
            int i2 = this.nextChar;
            this.nextChar = i2 + 1;
            cArr[i2] = (char) i;
        }
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        int i3;
        synchronized (this.lock) {
            ensureOpen();
            if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
                throw new IndexOutOfBoundsException();
            } else if (i2 != 0) {
                if (i2 >= this.nChars) {
                    flushBuffer();
                    this.out.write(cArr, i, i2);
                    return;
                }
                while (i < i3) {
                    int min = min(this.nChars - this.nextChar, i3 - i);
                    System.arraycopy((Object) cArr, i, (Object) this.f512cb, this.nextChar, min);
                    i += min;
                    int i4 = this.nextChar + min;
                    this.nextChar = i4;
                    if (i4 >= this.nChars) {
                        flushBuffer();
                    }
                }
            }
        }
    }

    public void write(String str, int i, int i2) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i3 = i2 + i;
            while (i < i3) {
                int min = min(this.nChars - this.nextChar, i3 - i);
                int i4 = i + min;
                str.getChars(i, i4, this.f512cb, this.nextChar);
                int i5 = this.nextChar + min;
                this.nextChar = i5;
                if (i5 >= this.nChars) {
                    flushBuffer();
                }
                i = i4;
            }
        }
    }

    public void newLine() throws IOException {
        write(System.lineSeparator());
    }

    public void flush() throws IOException {
        synchronized (this.lock) {
            flushBuffer();
            this.out.flush();
        }
    }

    public void close() throws IOException {
        synchronized (this.lock) {
            Writer writer = this.out;
            if (writer != null) {
                try {
                    flushBuffer();
                    if (writer != null) {
                        writer.close();
                    }
                    this.out = null;
                    this.f512cb = null;
                    return;
                } catch (Throwable th) {
                    this.out = null;
                    this.f512cb = null;
                    throw th;
                }
            } else {
                return;
            }
        }
        throw th;
    }
}
