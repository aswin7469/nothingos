package sun.misc;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.p026io.PrintStream;

public class HexDumpEncoder extends CharacterEncoder {
    private int currentByte;
    private int offset;
    private byte[] thisLine = new byte[16];
    private int thisLineLength;

    /* access modifiers changed from: protected */
    public int bytesPerAtom() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public int bytesPerLine() {
        return 16;
    }

    static void hexDigit(PrintStream printStream, byte b) {
        char c = (char) ((b >> 4) & 15);
        printStream.write((int) (char) (c > 9 ? (c - 10) + 65 : c + '0'));
        char c2 = (char) (b & 15);
        printStream.write((int) (char) (c2 > 9 ? (c2 - 10) + 65 : c2 + '0'));
    }

    /* access modifiers changed from: protected */
    public void encodeBufferPrefix(OutputStream outputStream) throws IOException {
        this.offset = 0;
        super.encodeBufferPrefix(outputStream);
    }

    /* access modifiers changed from: protected */
    public void encodeLinePrefix(OutputStream outputStream, int i) throws IOException {
        hexDigit(this.pStream, (byte) ((this.offset >>> 8) & 255));
        hexDigit(this.pStream, (byte) (this.offset & 255));
        this.pStream.print(": ");
        this.currentByte = 0;
        this.thisLineLength = i;
    }

    /* access modifiers changed from: protected */
    public void encodeAtom(OutputStream outputStream, byte[] bArr, int i, int i2) throws IOException {
        this.thisLine[this.currentByte] = bArr[i];
        hexDigit(this.pStream, bArr[i]);
        this.pStream.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        int i3 = this.currentByte + 1;
        this.currentByte = i3;
        if (i3 == 8) {
            this.pStream.print("  ");
        }
    }

    /* access modifiers changed from: protected */
    public void encodeLineSuffix(OutputStream outputStream) throws IOException {
        int i = this.thisLineLength;
        if (i < 16) {
            while (i < 16) {
                this.pStream.print("   ");
                if (i == 7) {
                    this.pStream.print("  ");
                }
                i++;
            }
        }
        this.pStream.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        for (int i2 = 0; i2 < this.thisLineLength; i2++) {
            byte b = this.thisLine[i2];
            if (b < 32 || b > 122) {
                this.pStream.print(BaseIconCache.EMPTY_CLASS_NAME);
            } else {
                this.pStream.write((int) this.thisLine[i2]);
            }
        }
        this.pStream.println();
        this.offset += this.thisLineLength;
    }
}
