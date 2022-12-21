package java.p026io;

/* renamed from: java.io.PushbackReader */
public class PushbackReader extends FilterReader {
    private char[] buf;
    private int pos;

    public boolean markSupported() {
        return false;
    }

    public PushbackReader(Reader reader, int i) {
        super(reader);
        if (i > 0) {
            this.buf = new char[i];
            this.pos = i;
            return;
        }
        throw new IllegalArgumentException("size <= 0");
    }

    public PushbackReader(Reader reader) {
        this(reader, 1);
    }

    private void ensureOpen() throws IOException {
        if (this.buf == null) {
            throw new IOException("Stream closed");
        }
    }

    public int read() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i = this.pos;
            char[] cArr = this.buf;
            if (i < cArr.length) {
                this.pos = i + 1;
                char c = cArr[i];
                return c;
            }
            int read = super.read();
            return read;
        }
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (i2 > 0) {
                char[] cArr2 = this.buf;
                int length = cArr2.length;
                int i3 = this.pos;
                int i4 = length - i3;
                if (i4 > 0) {
                    if (i2 < i4) {
                        i4 = i2;
                    }
                    System.arraycopy((Object) cArr2, i3, (Object) cArr, i, i4);
                    this.pos += i4;
                    i += i4;
                    i2 -= i4;
                }
                if (i2 <= 0) {
                    return i4;
                }
                int read = super.read(cArr, i, i2);
                if (read == -1) {
                    if (i4 == 0) {
                        i4 = -1;
                    }
                    return i4;
                }
                int i5 = i4 + read;
                return i5;
            } else if (i2 >= 0) {
                if (i >= 0) {
                    try {
                        if (i <= cArr.length) {
                            return 0;
                        }
                    } catch (ArrayIndexOutOfBoundsException unused) {
                        throw new IndexOutOfBoundsException();
                    }
                }
                throw new IndexOutOfBoundsException();
            } else {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    public void unread(int i) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i2 = this.pos;
            if (i2 != 0) {
                char[] cArr = this.buf;
                int i3 = i2 - 1;
                this.pos = i3;
                cArr[i3] = (char) i;
            } else {
                throw new IOException("Pushback buffer overflow");
            }
        }
    }

    public void unread(char[] cArr, int i, int i2) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i3 = this.pos;
            if (i2 <= i3) {
                int i4 = i3 - i2;
                this.pos = i4;
                System.arraycopy((Object) cArr, i, (Object) this.buf, i4, i2);
            } else {
                throw new IOException("Pushback buffer overflow");
            }
        }
    }

    public void unread(char[] cArr) throws IOException {
        unread(cArr, 0, cArr.length);
    }

    public boolean ready() throws IOException {
        boolean z;
        synchronized (this.lock) {
            ensureOpen();
            if (this.pos >= this.buf.length) {
                if (!super.ready()) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public void mark(int i) throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public void close() throws IOException {
        synchronized (this.lock) {
            super.close();
            this.buf = null;
        }
    }

    public long skip(long j) throws IOException {
        if (j >= 0) {
            synchronized (this.lock) {
                ensureOpen();
                char[] cArr = this.buf;
                int length = cArr.length;
                int i = this.pos;
                int i2 = length - i;
                if (i2 > 0) {
                    long j2 = (long) i2;
                    if (j <= j2) {
                        this.pos = (int) (((long) i) + j);
                        return j;
                    }
                    this.pos = cArr.length;
                    j -= j2;
                }
                long skip = ((long) i2) + super.skip(j);
                return skip;
            }
        }
        throw new IllegalArgumentException("skip value is negative");
    }
}
