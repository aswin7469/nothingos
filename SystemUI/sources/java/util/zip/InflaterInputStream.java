package java.util.zip;

import java.p026io.EOFException;
import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;

public class InflaterInputStream extends FilterInputStream {

    /* renamed from: b */
    private byte[] f806b;
    protected byte[] buf;
    @Deprecated
    protected boolean closed;
    protected Inflater inf;
    protected int len;
    private boolean reachEOF;
    private byte[] singleByteBuf;

    public boolean markSupported() {
        return false;
    }

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public InflaterInputStream(InputStream inputStream, Inflater inflater, int i) {
        super(inputStream);
        this.closed = false;
        this.reachEOF = false;
        this.singleByteBuf = new byte[1];
        this.f806b = new byte[512];
        if (inputStream == null || inflater == null) {
            throw null;
        } else if (i > 0) {
            this.inf = inflater;
            this.buf = new byte[i];
        } else {
            throw new IllegalArgumentException("buffer size <= 0");
        }
    }

    public InflaterInputStream(InputStream inputStream, Inflater inflater) {
        this(inputStream, inflater, 512);
    }

    public InflaterInputStream(InputStream inputStream) {
        this(inputStream, new Inflater());
    }

    public int read() throws IOException {
        ensureOpen();
        if (read(this.singleByteBuf, 0, 1) == -1) {
            return -1;
        }
        return Byte.toUnsignedInt(this.singleByteBuf[0]);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        ensureOpen();
        bArr.getClass();
        if (i < 0 || i2 < 0 || i2 > bArr.length - i) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            while (true) {
                try {
                    int inflate = this.inf.inflate(bArr, i, i2);
                    if (inflate != 0) {
                        return inflate;
                    }
                    if (this.inf.finished()) {
                        break;
                    } else if (this.inf.needsDictionary()) {
                        break;
                    } else if (this.inf.needsInput()) {
                        fill();
                    }
                } catch (DataFormatException e) {
                    String message = e.getMessage();
                    if (message == null) {
                        message = "Invalid ZLIB data format";
                    }
                    throw new ZipException(message);
                }
            }
            this.reachEOF = true;
            return -1;
        }
    }

    public int available() throws IOException {
        ensureOpen();
        if (this.reachEOF) {
            return 0;
        }
        if (!this.inf.finished()) {
            return 1;
        }
        this.reachEOF = true;
        return 0;
    }

    public long skip(long j) throws IOException {
        if (j >= 0) {
            ensureOpen();
            int min = (int) Math.min(j, 2147483647L);
            int i = 0;
            while (true) {
                if (i >= min) {
                    break;
                }
                int i2 = min - i;
                byte[] bArr = this.f806b;
                if (i2 > bArr.length) {
                    i2 = bArr.length;
                }
                int read = read(bArr, 0, i2);
                if (read == -1) {
                    this.reachEOF = true;
                    break;
                }
                i += read;
            }
            return (long) i;
        }
        throw new IllegalArgumentException("negative skip length");
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.inf.end();
            this.f521in.close();
            this.closed = true;
        }
    }

    /* access modifiers changed from: protected */
    public void fill() throws IOException {
        ensureOpen();
        InputStream inputStream = this.f521in;
        byte[] bArr = this.buf;
        int read = inputStream.read(bArr, 0, bArr.length);
        this.len = read;
        if (read != -1) {
            this.inf.setInput(this.buf, 0, read);
            return;
        }
        throw new EOFException("Unexpected end of ZLIB input stream");
    }

    public synchronized void mark(int i) {
    }

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
}
