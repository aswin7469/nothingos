package java.p026io;

/* renamed from: java.io.LineNumberReader */
public class LineNumberReader extends BufferedReader {
    private static final int maxSkipBufferSize = 8192;
    private int lineNumber = 0;
    private int markedLineNumber;
    private boolean markedSkipLF;
    private char[] skipBuffer = null;
    private boolean skipLF;

    public LineNumberReader(Reader reader) {
        super(reader);
    }

    public LineNumberReader(Reader reader, int i) {
        super(reader, i);
    }

    public void setLineNumber(int i) {
        this.lineNumber = i;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int read() throws IOException {
        synchronized (this.lock) {
            int read = super.read();
            if (this.skipLF) {
                if (read == 10) {
                    read = super.read();
                }
                this.skipLF = false;
            }
            if (read != 10) {
                if (read != 13) {
                    return read;
                }
                this.skipLF = true;
            }
            this.lineNumber++;
            return 10;
        }
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        int read;
        synchronized (this.lock) {
            read = super.read(cArr, i, i2);
            for (int i3 = i; i3 < i + read; i3++) {
                char c = cArr[i3];
                if (this.skipLF) {
                    this.skipLF = false;
                    if (c == 10) {
                    }
                }
                if (c != 10) {
                    if (c != 13) {
                    } else {
                        this.skipLF = true;
                    }
                }
                this.lineNumber++;
            }
        }
        return read;
    }

    public String readLine() throws IOException {
        String readLine;
        synchronized (this.lock) {
            readLine = super.readLine(this.skipLF);
            this.skipLF = false;
            if (readLine != null) {
                this.lineNumber++;
            }
        }
        return readLine;
    }

    public long skip(long j) throws IOException {
        long j2;
        if (j >= 0) {
            int min = (int) Math.min(j, 8192);
            synchronized (this.lock) {
                char[] cArr = this.skipBuffer;
                if (cArr == null || cArr.length < min) {
                    this.skipBuffer = new char[min];
                }
                long j3 = j;
                while (true) {
                    if (j3 <= 0) {
                        break;
                    }
                    int read = read(this.skipBuffer, 0, (int) Math.min(j3, (long) min));
                    if (read == -1) {
                        break;
                    }
                    j3 -= (long) read;
                }
                j2 = j - j3;
            }
            return j2;
        }
        throw new IllegalArgumentException("skip() value is negative");
    }

    public void mark(int i) throws IOException {
        synchronized (this.lock) {
            if (this.skipLF) {
                i++;
            }
            super.mark(i);
            this.markedLineNumber = this.lineNumber;
            this.markedSkipLF = this.skipLF;
        }
    }

    public void reset() throws IOException {
        synchronized (this.lock) {
            super.reset();
            this.lineNumber = this.markedLineNumber;
            this.skipLF = this.markedSkipLF;
        }
    }
}
