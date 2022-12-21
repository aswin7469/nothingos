package sun.security.util;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.nio.ByteBuffer;
import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.PrintStream;

public class HexDumpEncoder {
    private int currentByte;
    private int offset;
    protected PrintStream pStream;
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
        this.pStream = new PrintStream(outputStream);
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
                this.pStream.write((int) b);
            }
        }
        this.pStream.println();
        this.offset += this.thisLineLength;
    }

    /* access modifiers changed from: protected */
    public int readFully(InputStream inputStream, byte[] bArr) throws IOException {
        for (int i = 0; i < bArr.length; i++) {
            int read = inputStream.read();
            if (read == -1) {
                return i;
            }
            bArr[i] = (byte) read;
        }
        return bArr.length;
    }

    public void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[bytesPerLine()];
        encodeBufferPrefix(outputStream);
        while (true) {
            int readFully = readFully(inputStream, bArr);
            if (readFully != 0) {
                encodeLinePrefix(outputStream, readFully);
                int i = 0;
                while (i < readFully) {
                    if (bytesPerAtom() + i <= readFully) {
                        encodeAtom(outputStream, bArr, i, bytesPerAtom());
                    } else {
                        encodeAtom(outputStream, bArr, i, readFully - i);
                    }
                    i += bytesPerAtom();
                }
                if (readFully >= bytesPerLine()) {
                    encodeLineSuffix(outputStream);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public String encode(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encode(new ByteArrayInputStream(bArr), byteArrayOutputStream);
            return byteArrayOutputStream.toString("ISO-8859-1");
        } catch (Exception unused) {
            throw new Error("CharacterEncoder.encode internal error");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] getBytes(java.nio.ByteBuffer r3) {
        /*
            r2 = this;
            boolean r2 = r3.hasArray()
            if (r2 == 0) goto L_0x0020
            byte[] r2 = r3.array()
            int r0 = r2.length
            int r1 = r3.capacity()
            if (r0 != r1) goto L_0x0020
            int r0 = r2.length
            int r1 = r3.remaining()
            if (r0 != r1) goto L_0x0020
            int r0 = r3.limit()
            r3.position((int) r0)
            goto L_0x0021
        L_0x0020:
            r2 = 0
        L_0x0021:
            if (r2 != 0) goto L_0x002c
            int r2 = r3.remaining()
            byte[] r2 = new byte[r2]
            r3.get((byte[]) r2)
        L_0x002c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.HexDumpEncoder.getBytes(java.nio.ByteBuffer):byte[]");
    }

    public String encode(ByteBuffer byteBuffer) {
        return encode(getBytes(byteBuffer));
    }

    public void encodeBuffer(InputStream inputStream, OutputStream outputStream) throws IOException {
        int readFully;
        byte[] bArr = new byte[bytesPerLine()];
        encodeBufferPrefix(outputStream);
        do {
            readFully = readFully(inputStream, bArr);
            if (readFully != 0) {
                encodeLinePrefix(outputStream, readFully);
                int i = 0;
                while (i < readFully) {
                    if (bytesPerAtom() + i <= readFully) {
                        encodeAtom(outputStream, bArr, i, bytesPerAtom());
                    } else {
                        encodeAtom(outputStream, bArr, i, readFully - i);
                    }
                    i += bytesPerAtom();
                }
                encodeLineSuffix(outputStream);
            } else {
                return;
            }
        } while (readFully >= bytesPerLine());
    }

    public void encodeBuffer(byte[] bArr, OutputStream outputStream) throws IOException {
        encodeBuffer((InputStream) new ByteArrayInputStream(bArr), outputStream);
    }

    public String encodeBuffer(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encodeBuffer((InputStream) new ByteArrayInputStream(bArr), (OutputStream) byteArrayOutputStream);
            return byteArrayOutputStream.toString();
        } catch (Exception unused) {
            throw new Error("CharacterEncoder.encodeBuffer internal error");
        }
    }

    public void encodeBuffer(ByteBuffer byteBuffer, OutputStream outputStream) throws IOException {
        encodeBuffer(getBytes(byteBuffer), outputStream);
    }
}
