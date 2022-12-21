package java.p026io;

import java.nio.CharBuffer;
import java.util.Objects;

/* renamed from: java.io.Reader */
public abstract class Reader implements Readable, Closeable {
    private static final int TRANSFER_BUFFER_SIZE = 8192;
    private static final int maxSkipBufferSize = 8192;
    protected Object lock;
    private char[] skipBuffer;

    public abstract void close() throws IOException;

    public boolean markSupported() {
        return false;
    }

    public abstract int read(char[] cArr, int i, int i2) throws IOException;

    public boolean ready() throws IOException {
        return false;
    }

    public static Reader nullReader() {
        return new Reader() {
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (this.closed) {
                    throw new IOException("Stream closed");
                }
            }

            public int read() throws IOException {
                ensureOpen();
                return -1;
            }

            public int read(char[] cArr, int i, int i2) throws IOException {
                Objects.checkFromIndexSize(i, i2, cArr.length);
                ensureOpen();
                return i2 == 0 ? 0 : -1;
            }

            public int read(CharBuffer charBuffer) throws IOException {
                Objects.requireNonNull(charBuffer);
                ensureOpen();
                return charBuffer.hasRemaining() ? -1 : 0;
            }

            public boolean ready() throws IOException {
                ensureOpen();
                return false;
            }

            public long skip(long j) throws IOException {
                ensureOpen();
                return 0;
            }

            public long transferTo(Writer writer) throws IOException {
                Objects.requireNonNull(writer);
                ensureOpen();
                return 0;
            }

            public void close() {
                this.closed = true;
            }
        };
    }

    protected Reader() {
        this.skipBuffer = null;
        this.lock = this;
    }

    protected Reader(Object obj) {
        this.skipBuffer = null;
        obj.getClass();
        this.lock = obj;
    }

    public int read(CharBuffer charBuffer) throws IOException {
        int remaining = charBuffer.remaining();
        char[] cArr = new char[remaining];
        int read = read(cArr, 0, remaining);
        if (read > 0) {
            charBuffer.put(cArr, 0, read);
        }
        return read;
    }

    public int read() throws IOException {
        char[] cArr = new char[1];
        if (read(cArr, 0, 1) == -1) {
            return -1;
        }
        return cArr[0];
    }

    public int read(char[] cArr) throws IOException {
        return read(cArr, 0, cArr.length);
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
        throw new IllegalArgumentException("skip value is negative");
    }

    public void mark(int i) throws IOException {
        throw new IOException("mark() not supported");
    }

    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }

    public long transferTo(Writer writer) throws IOException {
        Objects.requireNonNull(writer, "out");
        char[] cArr = new char[8192];
        long j = 0;
        while (true) {
            int read = read(cArr, 0, 8192);
            if (read < 0) {
                return j;
            }
            writer.write(cArr, 0, read);
            j += (long) read;
        }
    }
}
