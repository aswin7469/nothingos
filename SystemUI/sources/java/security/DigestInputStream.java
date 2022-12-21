package java.security;

import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;

public class DigestInputStream extends FilterInputStream {
    protected MessageDigest digest;

    /* renamed from: on */
    private boolean f197on = true;

    public DigestInputStream(InputStream inputStream, MessageDigest messageDigest) {
        super(inputStream);
        setMessageDigest(messageDigest);
    }

    public MessageDigest getMessageDigest() {
        return this.digest;
    }

    public void setMessageDigest(MessageDigest messageDigest) {
        this.digest = messageDigest;
    }

    public int read() throws IOException {
        int read = this.f521in.read();
        if (this.f197on && read != -1) {
            this.digest.update((byte) read);
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.f521in.read(bArr, i, i2);
        if (this.f197on && read != -1) {
            this.digest.update(bArr, i, read);
        }
        return read;
    }

    /* renamed from: on */
    public void mo24036on(boolean z) {
        this.f197on = z;
    }

    public String toString() {
        return "[Digest Input Stream] " + this.digest.toString();
    }
}
