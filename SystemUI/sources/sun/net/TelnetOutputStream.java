package sun.net;

import java.p026io.BufferedOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;

public class TelnetOutputStream extends BufferedOutputStream {
    public boolean binaryMode;
    boolean seenCR = false;
    boolean stickyCRLF = false;

    public TelnetOutputStream(OutputStream outputStream, boolean z) {
        super(outputStream);
        this.binaryMode = z;
    }

    public void setStickyCRLF(boolean z) {
        this.stickyCRLF = z;
    }

    public void write(int i) throws IOException {
        if (this.binaryMode) {
            super.write(i);
        } else if (this.seenCR) {
            if (i != 10) {
                super.write(0);
            }
            super.write(i);
            if (i != 13) {
                this.seenCR = false;
            }
        } else if (i == 10) {
            super.write(13);
            super.write(10);
        } else {
            if (i == 13) {
                if (this.stickyCRLF) {
                    this.seenCR = true;
                } else {
                    super.write(13);
                    i = 0;
                }
            }
            super.write(i);
        }
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (this.binaryMode) {
            super.write(bArr, i, i2);
            return;
        }
        while (true) {
            i2--;
            if (i2 >= 0) {
                write(bArr[i]);
                i++;
            } else {
                return;
            }
        }
    }
}
