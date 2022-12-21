package javax.crypto;

import java.p026io.FilterOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;

public class CipherOutputStream extends FilterOutputStream {
    private Cipher cipher;
    private boolean closed = false;
    private byte[] ibuffer = new byte[1];
    private byte[] obuffer;
    private OutputStream output;

    public CipherOutputStream(OutputStream outputStream, Cipher cipher2) {
        super(outputStream);
        this.output = outputStream;
        this.cipher = cipher2;
    }

    protected CipherOutputStream(OutputStream outputStream) {
        super(outputStream);
        this.output = outputStream;
        this.cipher = new NullCipher();
    }

    public void write(int i) throws IOException {
        byte[] bArr = this.ibuffer;
        bArr[0] = (byte) i;
        byte[] update = this.cipher.update(bArr, 0, 1);
        this.obuffer = update;
        if (update != null) {
            this.output.write(update);
            this.obuffer = null;
        }
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        byte[] update = this.cipher.update(bArr, i, i2);
        this.obuffer = update;
        if (update != null) {
            this.output.write(update);
            this.obuffer = null;
        }
    }

    public void flush() throws IOException {
        byte[] bArr = this.obuffer;
        if (bArr != null) {
            this.output.write(bArr);
            this.obuffer = null;
        }
        this.output.flush();
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            try {
                this.obuffer = this.cipher.doFinal();
                try {
                    flush();
                } catch (IOException unused) {
                }
                this.out.close();
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                this.obuffer = null;
                throw new IOException(e);
            }
        }
    }
}
