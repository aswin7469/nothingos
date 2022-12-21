package javax.crypto;

import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;

public class CipherInputStream extends FilterInputStream {
    private Cipher cipher;
    private boolean closed = false;
    private boolean done = false;
    private byte[] ibuffer = new byte[512];
    private InputStream input;
    private byte[] obuffer;
    private int ofinish = 0;
    private int ostart = 0;

    public boolean markSupported() {
        return false;
    }

    private int getMoreData() throws IOException {
        if (this.done) {
            return -1;
        }
        this.ofinish = 0;
        this.ostart = 0;
        int outputSize = this.cipher.getOutputSize(this.ibuffer.length);
        byte[] bArr = this.obuffer;
        if (bArr == null || outputSize > bArr.length) {
            this.obuffer = new byte[outputSize];
        }
        int read = this.input.read(this.ibuffer);
        if (read == -1) {
            this.done = true;
            try {
                this.ofinish = this.cipher.doFinal(this.obuffer, 0);
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                this.obuffer = null;
                throw new IOException(e);
            } catch (ShortBufferException e2) {
                this.obuffer = null;
                throw new IllegalStateException("ShortBufferException is not expected", e2);
            }
        } else {
            try {
                this.ofinish = this.cipher.update(this.ibuffer, 0, read, this.obuffer, 0);
            } catch (IllegalStateException e3) {
                this.obuffer = null;
                throw e3;
            } catch (ShortBufferException e4) {
                this.obuffer = null;
                throw new IllegalStateException("ShortBufferException is not expected", e4);
            }
        }
        return this.ofinish;
    }

    public CipherInputStream(InputStream inputStream, Cipher cipher2) {
        super(inputStream);
        this.input = inputStream;
        this.cipher = cipher2;
    }

    protected CipherInputStream(InputStream inputStream) {
        super(inputStream);
        this.input = inputStream;
        this.cipher = new NullCipher();
    }

    public int read() throws IOException {
        if (this.ostart >= this.ofinish) {
            int i = 0;
            while (i == 0) {
                i = getMoreData();
            }
            if (i == -1) {
                return -1;
            }
        }
        byte[] bArr = this.obuffer;
        int i2 = this.ostart;
        this.ostart = i2 + 1;
        return bArr[i2] & 255;
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.ostart >= this.ofinish) {
            int i3 = 0;
            while (i3 == 0) {
                i3 = getMoreData();
            }
            if (i3 == -1) {
                return -1;
            }
        }
        if (i2 <= 0) {
            return 0;
        }
        int i4 = this.ofinish;
        int i5 = this.ostart;
        int i6 = i4 - i5;
        if (i2 >= i6) {
            i2 = i6;
        }
        if (bArr != null) {
            System.arraycopy((Object) this.obuffer, i5, (Object) bArr, i, i2);
        }
        this.ostart += i2;
        return i2;
    }

    public long skip(long j) throws IOException {
        int i = this.ofinish;
        int i2 = this.ostart;
        long j2 = (long) (i - i2);
        if (j > j2) {
            j = j2;
        }
        if (j < 0) {
            return 0;
        }
        this.ostart = (int) (((long) i2) + j);
        return j;
    }

    public int available() throws IOException {
        return this.ofinish - this.ostart;
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.input.close();
            if (!this.done) {
                try {
                    this.cipher.doFinal();
                } catch (BadPaddingException | IllegalBlockSizeException e) {
                    if (e instanceof AEADBadTagException) {
                        throw new IOException(e);
                    }
                }
            }
            this.ostart = 0;
            this.ofinish = 0;
        }
    }
}
