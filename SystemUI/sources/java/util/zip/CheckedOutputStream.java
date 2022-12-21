package java.util.zip;

import java.p026io.FilterOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;

public class CheckedOutputStream extends FilterOutputStream {
    private Checksum cksum;

    public CheckedOutputStream(OutputStream outputStream, Checksum checksum) {
        super(outputStream);
        this.cksum = checksum;
    }

    public void write(int i) throws IOException {
        this.out.write(i);
        this.cksum.update(i);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
        this.cksum.update(bArr, i, i2);
    }

    public Checksum getChecksum() {
        return this.cksum;
    }
}
