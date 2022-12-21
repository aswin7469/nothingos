package java.p026io;

import java.util.Objects;

/* renamed from: java.io.OutputStream */
public abstract class OutputStream implements Closeable, Flushable {
    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }

    public abstract void write(int i) throws IOException;

    public static OutputStream nullOutputStream() {
        return new OutputStream() {
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (this.closed) {
                    throw new IOException("Stream closed");
                }
            }

            public void write(int i) throws IOException {
                ensureOpen();
            }

            public void write(byte[] bArr, int i, int i2) throws IOException {
                Objects.checkFromIndexSize(i, i2, bArr.length);
                ensureOpen();
            }

            public void close() {
                this.closed = true;
            }
        };
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        Objects.checkFromIndexSize(i, i2, bArr.length);
        for (int i3 = 0; i3 < i2; i3++) {
            write((int) bArr[i + i3]);
        }
    }
}
