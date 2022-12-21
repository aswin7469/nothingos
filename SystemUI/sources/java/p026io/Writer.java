package java.p026io;

import java.util.Objects;

/* renamed from: java.io.Writer */
public abstract class Writer implements Appendable, Closeable, Flushable {
    private static final int WRITE_BUFFER_SIZE = 1024;
    protected Object lock;
    private char[] writeBuffer;

    public abstract void close() throws IOException;

    public abstract void flush() throws IOException;

    public abstract void write(char[] cArr, int i, int i2) throws IOException;

    public static Writer nullWriter() {
        return new Writer() {
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (this.closed) {
                    throw new IOException("Stream closed");
                }
            }

            public Writer append(char c) throws IOException {
                ensureOpen();
                return this;
            }

            public Writer append(CharSequence charSequence) throws IOException {
                ensureOpen();
                return this;
            }

            public Writer append(CharSequence charSequence, int i, int i2) throws IOException {
                ensureOpen();
                if (charSequence != null) {
                    Objects.checkFromToIndex(i, i2, charSequence.length());
                }
                return this;
            }

            public void write(int i) throws IOException {
                ensureOpen();
            }

            public void write(char[] cArr, int i, int i2) throws IOException {
                Objects.checkFromIndexSize(i, i2, cArr.length);
                ensureOpen();
            }

            public void write(String str) throws IOException {
                Objects.requireNonNull(str);
                ensureOpen();
            }

            public void write(String str, int i, int i2) throws IOException {
                Objects.checkFromIndexSize(i, i2, str.length());
                ensureOpen();
            }

            public void flush() throws IOException {
                ensureOpen();
            }

            public void close() throws IOException {
                this.closed = true;
            }
        };
    }

    protected Writer() {
        this.lock = this;
    }

    protected Writer(Object obj) {
        obj.getClass();
        this.lock = obj;
    }

    public void write(int i) throws IOException {
        synchronized (this.lock) {
            if (this.writeBuffer == null) {
                this.writeBuffer = new char[1024];
            }
            char[] cArr = this.writeBuffer;
            cArr[0] = (char) i;
            write(cArr, 0, 1);
        }
    }

    public void write(char[] cArr) throws IOException {
        write(cArr, 0, cArr.length);
    }

    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    public void write(String str, int i, int i2) throws IOException {
        char[] cArr;
        synchronized (this.lock) {
            if (i2 <= 1024) {
                if (this.writeBuffer == null) {
                    this.writeBuffer = new char[1024];
                }
                cArr = this.writeBuffer;
            } else {
                cArr = new char[i2];
            }
            str.getChars(i, i + i2, cArr, 0);
            write(cArr, 0, i2);
        }
    }

    public Writer append(CharSequence charSequence) throws IOException {
        write(String.valueOf((Object) charSequence));
        return this;
    }

    public Writer append(CharSequence charSequence, int i, int i2) throws IOException {
        if (charSequence == null) {
            charSequence = "null";
        }
        return append(charSequence.subSequence(i, i2));
    }

    public Writer append(char c) throws IOException {
        write((int) c);
        return this;
    }
}
