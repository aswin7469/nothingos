package java.util.zip;

import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;

public class CheckedInputStream extends FilterInputStream {
    private Checksum cksum;

    public CheckedInputStream(InputStream inputStream, Checksum checksum) {
        super(inputStream);
        this.cksum = checksum;
    }

    public int read() throws IOException {
        int read = this.f521in.read();
        if (read != -1) {
            this.cksum.update(read);
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.f521in.read(bArr, i, i2);
        if (read != -1) {
            this.cksum.update(bArr, i, read);
        }
        return read;
    }

    public long skip(long j) throws IOException {
        byte[] bArr = new byte[512];
        long j2 = 0;
        while (j2 < j) {
            long j3 = j - j2;
            long read = (long) read(bArr, 0, j3 < ((long) 512) ? (int) j3 : 512);
            if (read == -1) {
                return j2;
            }
            j2 += read;
        }
        return j2;
    }

    public Checksum getChecksum() {
        return this.cksum;
    }
}
