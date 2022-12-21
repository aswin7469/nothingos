package libcore.util;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class HexEncoding {
    private static final char[] LOWER_CASE_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] UPPER_CASE_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private HexEncoding() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String encodeToString(byte b, boolean z) {
        char[] cArr = z ? UPPER_CASE_DIGITS : LOWER_CASE_DIGITS;
        return new String(new char[]{cArr[(b >> 4) & 15], cArr[b & 15]}, 0, 2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static char[] encode(byte[] bArr) {
        return encode(bArr, 0, bArr.length, true);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static char[] encode(byte[] bArr, boolean z) {
        return encode(bArr, 0, bArr.length, z);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static char[] encode(byte[] bArr, int i, int i2) {
        return encode(bArr, i, i2, true);
    }

    private static char[] encode(byte[] bArr, int i, int i2, boolean z) {
        char[] cArr = z ? UPPER_CASE_DIGITS : LOWER_CASE_DIGITS;
        char[] cArr2 = new char[(i2 * 2)];
        for (int i3 = 0; i3 < i2; i3++) {
            byte b = bArr[i + i3];
            int i4 = i3 * 2;
            cArr2[i4] = cArr[(b >> 4) & 15];
            cArr2[i4 + 1] = cArr[b & 15];
        }
        return cArr2;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String encodeToString(byte[] bArr) {
        return encodeToString(bArr, true);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String encodeToString(byte[] bArr, boolean z) {
        return new String(encode(bArr, z));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static byte[] decode(String str) throws IllegalArgumentException {
        return decode(str.toCharArray());
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static byte[] decode(String str, boolean z) throws IllegalArgumentException {
        return decode(str.toCharArray(), z);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static byte[] decode(char[] cArr) throws IllegalArgumentException {
        return decode(cArr, false);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static byte[] decode(char[] cArr, boolean z) throws IllegalArgumentException {
        int length = cArr.length;
        byte[] bArr = new byte[((length + 1) / 2)];
        int i = 0;
        if (z) {
            if (length % 2 != 0) {
                bArr[0] = (byte) toDigit(cArr, 0);
                i = 1;
            }
        } else if (length % 2 != 0) {
            throw new IllegalArgumentException("Invalid input length: " + length);
        }
        int i2 = i;
        while (i < length) {
            bArr[i2] = (byte) ((toDigit(cArr, i) << 4) | toDigit(cArr, i + 1));
            i += 2;
            i2++;
        }
        return bArr;
    }

    private static int toDigit(char[] cArr, int i) throws IllegalArgumentException {
        char c = cArr[i];
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        char c2 = 'a';
        if ('a' > c || c > 'f') {
            c2 = 'A';
            if ('A' > c || c > 'F') {
                throw new IllegalArgumentException("Illegal char: " + cArr[i] + " at offset " + i);
            }
        }
        return (c - c2) + 10;
    }
}
