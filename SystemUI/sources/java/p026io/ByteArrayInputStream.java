package java.p026io;

import java.util.Arrays;
import java.util.Objects;

/* renamed from: java.io.ByteArrayInputStream */
public class ByteArrayInputStream extends InputStream {
    protected byte[] buf;
    protected int count;
    protected int mark = 0;
    protected int pos;

    public void close() throws IOException {
    }

    public boolean markSupported() {
        return true;
    }

    public ByteArrayInputStream(byte[] bArr) {
        this.buf = bArr;
        this.pos = 0;
        this.count = bArr.length;
    }

    public ByteArrayInputStream(byte[] bArr, int i, int i2) {
        this.buf = bArr;
        this.pos = i;
        this.count = Math.min(i2 + i, bArr.length);
        this.mark = i;
    }

    public synchronized int read() {
        byte b;
        int i = this.pos;
        if (i < this.count) {
            byte[] bArr = this.buf;
            this.pos = i + 1;
            b = bArr[i] & 255;
        } else {
            b = -1;
        }
        return b;
    }

    public synchronized int read(byte[] bArr, int i, int i2) {
        Objects.checkFromIndexSize(i, i2, bArr.length);
        int i3 = this.pos;
        int i4 = this.count;
        if (i3 >= i4) {
            return -1;
        }
        int i5 = i4 - i3;
        if (i2 > i5) {
            i2 = i5;
        }
        if (i2 <= 0) {
            return 0;
        }
        System.arraycopy((Object) this.buf, i3, (Object) bArr, i, i2);
        this.pos += i2;
        return i2;
    }

    public synchronized byte[] readAllBytes() {
        byte[] copyOfRange;
        copyOfRange = Arrays.copyOfRange(this.buf, this.pos, this.count);
        this.pos = this.count;
        return copyOfRange;
    }

    public int readNBytes(byte[] bArr, int i, int i2) {
        int read = read(bArr, i, i2);
        if (read == -1) {
            return 0;
        }
        return read;
    }

    public synchronized long transferTo(OutputStream outputStream) throws IOException {
        int i;
        int i2 = this.count;
        int i3 = this.pos;
        i = i2 - i3;
        outputStream.write(this.buf, i3, i);
        this.pos = this.count;
        return (long) i;
    }

    public synchronized long skip(long j) {
        long j2;
        int i = this.count;
        int i2 = this.pos;
        j2 = (long) (i - i2);
        if (j < j2) {
            if (j < 0) {
                j = 0;
            }
            j2 = j;
        }
        this.pos = (int) (((long) i2) + j2);
        return j2;
    }

    public synchronized int available() {
        return this.count - this.pos;
    }

    public void mark(int i) {
        this.mark = this.pos;
    }

    public synchronized void reset() {
        this.pos = this.mark;
    }
}
