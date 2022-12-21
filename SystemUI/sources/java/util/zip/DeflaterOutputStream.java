package java.util.zip;

import java.p026io.FilterOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;

public class DeflaterOutputStream extends FilterOutputStream {
    protected byte[] buf;
    private boolean closed;
    protected Deflater def;
    private final boolean syncFlush;
    boolean usesDefaultDeflater;

    public DeflaterOutputStream(OutputStream outputStream, Deflater deflater, int i, boolean z) {
        super(outputStream);
        this.closed = false;
        this.usesDefaultDeflater = false;
        if (outputStream == null || deflater == null) {
            throw null;
        } else if (i > 0) {
            this.def = deflater;
            this.buf = new byte[i];
            this.syncFlush = z;
        } else {
            throw new IllegalArgumentException("buffer size <= 0");
        }
    }

    public DeflaterOutputStream(OutputStream outputStream, Deflater deflater, int i) {
        this(outputStream, deflater, i, false);
    }

    public DeflaterOutputStream(OutputStream outputStream, Deflater deflater, boolean z) {
        this(outputStream, deflater, 512, z);
    }

    public DeflaterOutputStream(OutputStream outputStream, Deflater deflater) {
        this(outputStream, deflater, 512, false);
    }

    public DeflaterOutputStream(OutputStream outputStream, boolean z) {
        this(outputStream, new Deflater(), 512, z);
        this.usesDefaultDeflater = true;
    }

    public DeflaterOutputStream(OutputStream outputStream) {
        this(outputStream, false);
        this.usesDefaultDeflater = true;
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) (i & 255)}, 0, 1);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (!this.def.finished()) {
            int i3 = i + i2;
            if ((i | i2 | i3 | (bArr.length - i3)) < 0) {
                throw new IndexOutOfBoundsException();
            } else if (i2 != 0 && !this.def.finished()) {
                this.def.setInput(bArr, i, i2);
                while (!this.def.needsInput()) {
                    deflate();
                }
            }
        } else {
            throw new IOException("write beyond end of stream");
        }
    }

    public void finish() throws IOException {
        if (!this.def.finished()) {
            this.def.finish();
            while (!this.def.finished()) {
                deflate();
            }
        }
    }

    public void close() throws IOException {
        if (!this.closed) {
            finish();
            if (this.usesDefaultDeflater) {
                this.def.end();
            }
            this.out.close();
            this.closed = true;
        }
    }

    /* access modifiers changed from: protected */
    public void deflate() throws IOException {
        while (true) {
            Deflater deflater = this.def;
            byte[] bArr = this.buf;
            int deflate = deflater.deflate(bArr, 0, bArr.length);
            if (deflate > 0) {
                this.out.write(this.buf, 0, deflate);
            } else {
                return;
            }
        }
    }

    public void flush() throws IOException {
        int deflate;
        if (this.syncFlush && !this.def.finished()) {
            do {
                Deflater deflater = this.def;
                byte[] bArr = this.buf;
                deflate = deflater.deflate(bArr, 0, bArr.length, 2);
                if (deflate <= 0) {
                    break;
                }
                this.out.write(this.buf, 0, deflate);
            } while (deflate >= this.buf.length);
        }
        this.out.flush();
    }
}
