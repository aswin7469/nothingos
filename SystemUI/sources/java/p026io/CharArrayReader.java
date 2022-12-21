package java.p026io;

/* renamed from: java.io.CharArrayReader */
public class CharArrayReader extends Reader {
    protected char[] buf;
    protected int count;
    protected int markedPos = 0;
    protected int pos;

    public boolean markSupported() {
        return true;
    }

    public CharArrayReader(char[] cArr) {
        this.buf = cArr;
        this.pos = 0;
        this.count = cArr.length;
    }

    public CharArrayReader(char[] cArr, int i, int i2) {
        int i3;
        if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i2 + i) < 0) {
            throw new IllegalArgumentException();
        }
        this.buf = cArr;
        this.pos = i;
        this.count = Math.min(i3, cArr.length);
        this.markedPos = i;
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
            if (i >= this.count) {
                return -1;
            }
            char[] cArr = this.buf;
            this.pos = i + 1;
            char c = cArr[i];
            return c;
        }
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        int i3;
        synchronized (this.lock) {
            ensureOpen();
            if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
                throw new IndexOutOfBoundsException();
            } else if (i2 == 0) {
                return 0;
            } else {
                int i4 = this.pos;
                int i5 = this.count;
                if (i4 >= i5) {
                    return -1;
                }
                int i6 = i5 - i4;
                if (i2 > i6) {
                    i2 = i6;
                }
                if (i2 <= 0) {
                    return 0;
                }
                System.arraycopy((Object) this.buf, i4, (Object) cArr, i, i2);
                this.pos += i2;
                return i2;
            }
        }
    }

    public long skip(long j) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i = this.count;
            int i2 = this.pos;
            long j2 = (long) (i - i2);
            if (j > j2) {
                j = j2;
            }
            if (j < 0) {
                return 0;
            }
            this.pos = (int) (((long) i2) + j);
            return j;
        }
    }

    public boolean ready() throws IOException {
        boolean z;
        synchronized (this.lock) {
            ensureOpen();
            z = this.count - this.pos > 0;
        }
        return z;
    }

    public void mark(int i) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            this.markedPos = this.pos;
        }
    }

    public void reset() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            this.pos = this.markedPos;
        }
    }

    public void close() {
        synchronized (this.lock) {
            this.buf = null;
        }
    }
}
