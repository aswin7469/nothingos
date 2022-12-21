package java.util.zip;

import java.p026io.FilterOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;

public class InflaterOutputStream extends FilterOutputStream {
    protected final byte[] buf;
    private boolean closed;
    protected final Inflater inf;
    private boolean usesDefaultInflater;
    private final byte[] wbuf;

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public InflaterOutputStream(OutputStream outputStream) {
        this(outputStream, new Inflater());
        this.usesDefaultInflater = true;
    }

    public InflaterOutputStream(OutputStream outputStream, Inflater inflater) {
        this(outputStream, inflater, 512);
    }

    public InflaterOutputStream(OutputStream outputStream, Inflater inflater, int i) {
        super(outputStream);
        this.wbuf = new byte[1];
        this.usesDefaultInflater = false;
        this.closed = false;
        if (outputStream == null) {
            throw new NullPointerException("Null output");
        } else if (inflater == null) {
            throw new NullPointerException("Null inflater");
        } else if (i > 0) {
            this.inf = inflater;
            this.buf = new byte[i];
        } else {
            throw new IllegalArgumentException("Buffer size < 1");
        }
    }

    public void close() throws IOException {
        if (!this.closed) {
            try {
                finish();
            } finally {
                this.out.close();
                this.closed = true;
            }
        }
    }

    public void flush() throws IOException {
        ensureOpen();
        if (!this.inf.finished()) {
            while (true) {
                try {
                    if (this.inf.finished() || this.inf.needsInput()) {
                        break;
                    }
                    Inflater inflater = this.inf;
                    byte[] bArr = this.buf;
                    int inflate = inflater.inflate(bArr, 0, bArr.length);
                    if (inflate < 1) {
                        break;
                    }
                    this.out.write(this.buf, 0, inflate);
                } catch (DataFormatException e) {
                    String message = e.getMessage();
                    if (message == null) {
                        message = "Invalid ZLIB data format";
                    }
                    throw new ZipException(message);
                }
            }
            super.flush();
        }
    }

    public void finish() throws IOException {
        ensureOpen();
        flush();
        if (this.usesDefaultInflater) {
            this.inf.end();
        }
    }

    public void write(int i) throws IOException {
        byte[] bArr = this.wbuf;
        bArr[0] = (byte) i;
        write(bArr, 0, 1);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        int inflate;
        ensureOpen();
        if (bArr == null) {
            throw new NullPointerException("Null buffer for read");
        } else if (i < 0 || i2 < 0 || i2 > bArr.length - i) {
            throw new IndexOutOfBoundsException();
        } else if (i2 != 0) {
            while (true) {
                try {
                    if (this.inf.needsInput()) {
                        if (i2 >= 1) {
                            int i3 = 512;
                            if (i2 < 512) {
                                i3 = i2;
                            }
                            this.inf.setInput(bArr, i, i3);
                            i += i3;
                            i2 -= i3;
                        } else {
                            return;
                        }
                    }
                    do {
                        Inflater inflater = this.inf;
                        byte[] bArr2 = this.buf;
                        inflate = inflater.inflate(bArr2, 0, bArr2.length);
                        if (inflate > 0) {
                            this.out.write(this.buf, 0, inflate);
                            continue;
                        }
                    } while (inflate > 0);
                    if (!this.inf.finished()) {
                        if (this.inf.needsDictionary()) {
                            throw new ZipException("ZLIB dictionary missing");
                        }
                    } else {
                        return;
                    }
                } catch (DataFormatException e) {
                    String message = e.getMessage();
                    if (message == null) {
                        message = "Invalid ZLIB data format";
                    }
                    throw new ZipException(message);
                }
            }
        }
    }
}
