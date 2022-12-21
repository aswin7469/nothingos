package java.util.zip;

import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;

public class DeflaterInputStream extends FilterInputStream {
    protected final byte[] buf;
    protected final Deflater def;
    private byte[] rbuf;
    private boolean reachEOF;
    private boolean usesDefaultDeflater;

    public void mark(int i) {
    }

    public boolean markSupported() {
        return false;
    }

    private void ensureOpen() throws IOException {
        if (this.f521in == null) {
            throw new IOException("Stream closed");
        }
    }

    public DeflaterInputStream(InputStream inputStream) {
        this(inputStream, new Deflater());
        this.usesDefaultDeflater = true;
    }

    public DeflaterInputStream(InputStream inputStream, Deflater deflater) {
        this(inputStream, deflater, 512);
    }

    public DeflaterInputStream(InputStream inputStream, Deflater deflater, int i) {
        super(inputStream);
        this.rbuf = new byte[1];
        this.usesDefaultDeflater = false;
        this.reachEOF = false;
        if (inputStream == null) {
            throw new NullPointerException("Null input");
        } else if (deflater == null) {
            throw new NullPointerException("Null deflater");
        } else if (i >= 1) {
            this.def = deflater;
            this.buf = new byte[i];
        } else {
            throw new IllegalArgumentException("Buffer size < 1");
        }
    }

    public void close() throws IOException {
        if (this.f521in != null) {
            try {
                if (this.usesDefaultDeflater) {
                    this.def.end();
                }
                this.f521in.close();
            } finally {
                this.f521in = null;
            }
        }
    }

    public int read() throws IOException {
        if (read(this.rbuf, 0, 1) <= 0) {
            return -1;
        }
        return this.rbuf[0] & 255;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        ensureOpen();
        if (bArr == null) {
            throw new NullPointerException("Null buffer for read");
        } else if (i < 0 || i2 < 0 || i2 > bArr.length - i) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            int i3 = 0;
            while (i2 > 0 && !this.def.finished()) {
                if (this.def.needsInput()) {
                    InputStream inputStream = this.f521in;
                    byte[] bArr2 = this.buf;
                    int read = inputStream.read(bArr2, 0, bArr2.length);
                    if (read < 0) {
                        this.def.finish();
                    } else if (read > 0) {
                        this.def.setInput(this.buf, 0, read);
                    }
                }
                int deflate = this.def.deflate(bArr, i, i2);
                i3 += deflate;
                i += deflate;
                i2 -= deflate;
            }
            if (!this.def.finished()) {
                return i3;
            }
            this.reachEOF = true;
            if (i3 == 0) {
                return -1;
            }
            return i3;
        }
    }

    public long skip(long j) throws IOException {
        long j2 = 0;
        if (j >= 0) {
            ensureOpen();
            if (this.rbuf.length < 512) {
                this.rbuf = new byte[512];
            }
            int min = (int) Math.min(j, 2147483647L);
            while (min > 0) {
                byte[] bArr = this.rbuf;
                int read = read(bArr, 0, min <= bArr.length ? min : bArr.length);
                if (read < 0) {
                    break;
                }
                j2 += (long) read;
                min -= read;
            }
            return j2;
        }
        throw new IllegalArgumentException("negative skip length");
    }

    public int available() throws IOException {
        ensureOpen();
        return this.reachEOF ? 0 : 1;
    }

    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
}
