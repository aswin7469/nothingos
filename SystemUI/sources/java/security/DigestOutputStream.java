package java.security;

import java.p026io.FilterOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;

public class DigestOutputStream extends FilterOutputStream {
    protected MessageDigest digest;

    /* renamed from: on */
    private boolean f198on = true;

    public DigestOutputStream(OutputStream outputStream, MessageDigest messageDigest) {
        super(outputStream);
        setMessageDigest(messageDigest);
    }

    public MessageDigest getMessageDigest() {
        return this.digest;
    }

    public void setMessageDigest(MessageDigest messageDigest) {
        this.digest = messageDigest;
    }

    public void write(int i) throws IOException {
        this.out.write(i);
        if (this.f198on) {
            this.digest.update((byte) i);
        }
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (bArr == null || i + i2 > bArr.length) {
            throw new IllegalArgumentException("wrong parameters for write");
        } else if (i < 0 || i2 < 0) {
            throw new IndexOutOfBoundsException("wrong index for write");
        } else {
            this.out.write(bArr, i, i2);
            if (this.f198on) {
                this.digest.update(bArr, i, i2);
            }
        }
    }

    /* renamed from: on */
    public void mo24039on(boolean z) {
        this.f198on = z;
    }

    public String toString() {
        return "[Digest Output Stream] " + this.digest.toString();
    }
}
