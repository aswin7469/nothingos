package java.p026io;

@Deprecated
/* renamed from: java.io.StringBufferInputStream */
public class StringBufferInputStream extends InputStream {
    protected String buffer;
    protected int count;
    protected int pos;

    public StringBufferInputStream(String str) {
        this.buffer = str;
        this.count = str.length();
    }

    public synchronized int read() {
        char c;
        int i = this.pos;
        if (i < this.count) {
            String str = this.buffer;
            this.pos = i + 1;
            c = str.charAt(i) & 255;
        } else {
            c = 65535;
        }
        return c;
    }

    public synchronized int read(byte[] bArr, int i, int i2) {
        int i3;
        if (bArr != null) {
            if (i >= 0) {
                if (i <= bArr.length && i2 >= 0 && (i3 = i + i2) <= bArr.length && i3 >= 0) {
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
                    this.buffer.getBytes(i4, i4 + i2, bArr, i);
                    this.pos += i2;
                    return i2;
                }
            }
            throw new IndexOutOfBoundsException();
        }
        throw new NullPointerException();
    }

    public synchronized long skip(long j) {
        if (j < 0) {
            return 0;
        }
        int i = this.count;
        int i2 = this.pos;
        if (j > ((long) (i - i2))) {
            j = (long) (i - i2);
        }
        this.pos = (int) (((long) i2) + j);
        return j;
    }

    public synchronized int available() {
        return this.count - this.pos;
    }

    public synchronized void reset() {
        this.pos = 0;
    }
}
