package java.p026io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/* renamed from: java.io.InputStream */
public abstract class InputStream implements Closeable {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int MAX_BUFFER_SIZE = 2147483639;
    private static final int MAX_SKIP_BUFFER_SIZE = 2048;

    public int available() throws IOException {
        return 0;
    }

    public void close() throws IOException {
    }

    public boolean markSupported() {
        return false;
    }

    public abstract int read() throws IOException;

    public static InputStream nullInputStream() {
        return new InputStream() {
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (this.closed) {
                    throw new IOException("Stream closed");
                }
            }

            public int available() throws IOException {
                ensureOpen();
                return 0;
            }

            public int read() throws IOException {
                ensureOpen();
                return -1;
            }

            public int read(byte[] bArr, int i, int i2) throws IOException {
                Objects.checkFromIndexSize(i, i2, bArr.length);
                if (i2 == 0) {
                    return 0;
                }
                ensureOpen();
                return -1;
            }

            public byte[] readAllBytes() throws IOException {
                ensureOpen();
                return new byte[0];
            }

            public int readNBytes(byte[] bArr, int i, int i2) throws IOException {
                Objects.checkFromIndexSize(i, i2, bArr.length);
                ensureOpen();
                return 0;
            }

            public byte[] readNBytes(int i) throws IOException {
                if (i >= 0) {
                    ensureOpen();
                    return new byte[0];
                }
                throw new IllegalArgumentException("len < 0");
            }

            public long skip(long j) throws IOException {
                ensureOpen();
                return 0;
            }

            public long transferTo(OutputStream outputStream) throws IOException {
                Objects.requireNonNull(outputStream);
                ensureOpen();
                return 0;
            }

            public void close() throws IOException {
                this.closed = true;
            }
        };
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        Objects.checkFromIndexSize(i, i2, bArr.length);
        if (i2 == 0) {
            return 0;
        }
        int read = read();
        if (read == -1) {
            return -1;
        }
        bArr[i] = (byte) read;
        int i3 = 1;
        while (true) {
            if (i3 >= i2) {
                break;
            }
            try {
                int read2 = read();
                if (read2 == -1) {
                    break;
                }
                bArr[i + i3] = (byte) read2;
                i3++;
            } catch (IOException unused) {
            }
        }
        return i3;
    }

    public byte[] readAllBytes() throws IOException {
        return readNBytes(Integer.MAX_VALUE);
    }

    public byte[] readNBytes(int i) throws IOException {
        int read;
        if (i >= 0) {
            byte[] bArr = null;
            ArrayList<byte[]> arrayList = null;
            int i2 = 0;
            do {
                int min = Math.min(i, 8192);
                byte[] bArr2 = new byte[min];
                int i3 = 0;
                while (true) {
                    read = read(bArr2, i3, Math.min(min - i3, i));
                    if (read <= 0) {
                        break;
                    }
                    i3 += read;
                    i -= read;
                }
                if (i3 > 0) {
                    if (MAX_BUFFER_SIZE - i2 >= i3) {
                        i2 += i3;
                        if (bArr == null) {
                            bArr = bArr2;
                        } else {
                            if (arrayList == null) {
                                arrayList = new ArrayList<>();
                                arrayList.add(bArr);
                            }
                            arrayList.add(bArr2);
                        }
                    } else {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                }
                if (read < 0) {
                    break;
                }
            } while (i > 0);
            if (arrayList != null) {
                byte[] bArr3 = new byte[i2];
                int i4 = 0;
                for (byte[] bArr4 : arrayList) {
                    int min2 = Math.min(bArr4.length, i2);
                    System.arraycopy((Object) bArr4, 0, (Object) bArr3, i4, min2);
                    i4 += min2;
                    i2 -= min2;
                }
                return bArr3;
            } else if (bArr == null) {
                return new byte[0];
            } else {
                return bArr.length == i2 ? bArr : Arrays.copyOf(bArr, i2);
            }
        } else {
            throw new IllegalArgumentException("len < 0");
        }
    }

    public int readNBytes(byte[] bArr, int i, int i2) throws IOException {
        Objects.checkFromIndexSize(i, i2, bArr.length);
        int i3 = 0;
        while (i3 < i2) {
            int read = read(bArr, i + i3, i2 - i3);
            if (read < 0) {
                break;
            }
            i3 += read;
        }
        return i3;
    }

    public long skip(long j) throws IOException {
        int read;
        if (j <= 0) {
            return 0;
        }
        int min = (int) Math.min(2048, j);
        byte[] bArr = new byte[min];
        long j2 = j;
        while (j2 > 0 && (read = read(bArr, 0, (int) Math.min((long) min, j2))) >= 0) {
            j2 -= (long) read;
        }
        return j - j2;
    }

    public synchronized void mark(int i) {
    }

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public long transferTo(OutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream, "out");
        byte[] bArr = new byte[8192];
        long j = 0;
        while (true) {
            int read = read(bArr, 0, 8192);
            if (read < 0) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }
}
