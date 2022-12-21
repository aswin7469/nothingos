package java.p026io;

@Deprecated
/* renamed from: java.io.LineNumberInputStream */
public class LineNumberInputStream extends FilterInputStream {
    int lineNumber;
    int markLineNumber;
    int markPushBack = -1;
    int pushBack = -1;

    public LineNumberInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public int read() throws IOException {
        int i = this.pushBack;
        if (i != -1) {
            this.pushBack = -1;
        } else {
            i = this.f521in.read();
        }
        if (i != 10) {
            if (i != 13) {
                return i;
            }
            int read = this.f521in.read();
            this.pushBack = read;
            if (read == 10) {
                this.pushBack = -1;
            }
        }
        this.lineNumber++;
        return 10;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3;
        bArr.getClass();
        if (i < 0 || i > bArr.length || i2 < 0 || (i3 = i + i2) > bArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            int read = read();
            if (read == -1) {
                return -1;
            }
            bArr[i] = (byte) read;
            int i4 = 1;
            while (true) {
                if (i4 >= i2) {
                    break;
                }
                try {
                    int read2 = read();
                    if (read2 == -1) {
                        break;
                    }
                    if (bArr != null) {
                        bArr[i + i4] = (byte) read2;
                    }
                    i4++;
                } catch (IOException unused) {
                }
            }
            return i4;
        }
    }

    public long skip(long j) throws IOException {
        int read;
        if (j <= 0) {
            return 0;
        }
        byte[] bArr = new byte[2048];
        long j2 = j;
        while (j2 > 0 && (read = read(bArr, 0, (int) Math.min((long) 2048, j2))) >= 0) {
            j2 -= (long) read;
        }
        return j - j2;
    }

    public void setLineNumber(int i) {
        this.lineNumber = i;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int available() throws IOException {
        int i = this.pushBack;
        int available = super.available() / 2;
        return i == -1 ? available : available + 1;
    }

    public void mark(int i) {
        this.markLineNumber = this.lineNumber;
        this.markPushBack = this.pushBack;
        this.f521in.mark(i);
    }

    public void reset() throws IOException {
        this.lineNumber = this.markLineNumber;
        this.pushBack = this.markPushBack;
        this.f521in.reset();
    }
}
