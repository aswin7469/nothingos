package sun.misc;

import java.p026io.IOException;
import java.p026io.OutputStream;

public class BASE64Encoder extends CharacterEncoder {
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    /* access modifiers changed from: protected */
    public int bytesPerAtom() {
        return 3;
    }

    /* access modifiers changed from: protected */
    public int bytesPerLine() {
        return 57;
    }

    /* access modifiers changed from: protected */
    public void encodeAtom(OutputStream outputStream, byte[] bArr, int i, int i2) throws IOException {
        if (i2 == 1) {
            byte b = bArr[i];
            char[] cArr = pem_array;
            outputStream.write((int) cArr[(b >>> 2) & 63]);
            outputStream.write((int) cArr[((b << 4) & 48) + 0]);
            outputStream.write(61);
            outputStream.write(61);
        } else if (i2 == 2) {
            byte b2 = bArr[i];
            byte b3 = bArr[i + 1];
            char[] cArr2 = pem_array;
            outputStream.write((int) cArr2[(b2 >>> 2) & 63]);
            outputStream.write((int) cArr2[((b2 << 4) & 48) + ((b3 >>> 4) & 15)]);
            outputStream.write((int) cArr2[((b3 << 2) & 60) + 0]);
            outputStream.write(61);
        } else {
            byte b4 = bArr[i];
            byte b5 = bArr[i + 1];
            byte b6 = bArr[i + 2];
            char[] cArr3 = pem_array;
            outputStream.write((int) cArr3[(b4 >>> 2) & 63]);
            outputStream.write((int) cArr3[((b4 << 4) & 48) + ((b5 >>> 4) & 15)]);
            outputStream.write((int) cArr3[((b5 << 2) & 60) + ((b6 >>> 6) & 3)]);
            outputStream.write((int) cArr3[b6 & 63]);
        }
    }
}
