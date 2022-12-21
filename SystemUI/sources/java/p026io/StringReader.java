package java.p026io;

/* renamed from: java.io.StringReader */
public class StringReader extends Reader {
    private int length;
    private int mark = 0;
    private int next = 0;
    private String str;

    public boolean markSupported() {
        return true;
    }

    public StringReader(String str2) {
        this.str = str2;
        this.length = str2.length();
    }

    private void ensureOpen() throws IOException {
        if (this.str == null) {
            throw new IOException("Stream closed");
        }
    }

    public int read() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i = this.next;
            if (i >= this.length) {
                return -1;
            }
            String str2 = this.str;
            this.next = i + 1;
            char charAt = str2.charAt(i);
            return charAt;
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
                int i4 = this.next;
                int i5 = this.length;
                if (i4 >= i5) {
                    return -1;
                }
                int min = Math.min(i5 - i4, i2);
                String str2 = this.str;
                int i6 = this.next;
                str2.getChars(i6, i6 + min, cArr, i);
                this.next += min;
                return min;
            }
        }
    }

    public long skip(long j) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i = this.next;
            int i2 = this.length;
            if (i >= i2) {
                return 0;
            }
            long max = Math.max((long) (-this.next), Math.min((long) (i2 - i), j));
            this.next = (int) (((long) this.next) + max);
            return max;
        }
    }

    public boolean ready() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
        }
        return true;
    }

    public void mark(int i) throws IOException {
        if (i >= 0) {
            synchronized (this.lock) {
                ensureOpen();
                this.mark = this.next;
            }
            return;
        }
        throw new IllegalArgumentException("Read-ahead limit < 0");
    }

    public void reset() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            this.next = this.mark;
        }
    }

    public void close() {
        synchronized (this.lock) {
            this.str = null;
        }
    }
}
