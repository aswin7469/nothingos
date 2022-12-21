package java.util.prefs;

import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.Arrays;
import java.util.Random;
import kotlin.text.Typography;

class Base64 {
    private static final byte[] altBase64ToInt = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, NetworkStackConstants.TCPHDR_URG, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25};
    private static final byte[] base64ToInt = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, NetworkStackConstants.TCPHDR_URG, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    private static final char[] intToAltBase64 = {'!', Typography.quote, '#', '$', '%', Typography.amp, '\'', '(', ')', ',', '-', '.', AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR, ';', Typography.less, Typography.greater, '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?'};
    private static final char[] intToBase64 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    Base64() {
    }

    static String byteArrayToBase64(byte[] bArr) {
        return byteArrayToBase64(bArr, false);
    }

    static String byteArrayToAltBase64(byte[] bArr) {
        return byteArrayToBase64(bArr, true);
    }

    private static String byteArrayToBase64(byte[] bArr, boolean z) {
        int length = bArr.length;
        int i = length / 3;
        int i2 = length - (i * 3);
        StringBuffer stringBuffer = new StringBuffer(((length + 2) / 3) * 4);
        char[] cArr = z ? intToAltBase64 : intToBase64;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            int i5 = i4 + 1;
            byte b = bArr[i4] & 255;
            int i6 = i5 + 1;
            byte b2 = bArr[i5] & 255;
            int i7 = i6 + 1;
            byte b3 = bArr[i6] & 255;
            stringBuffer.append(cArr[b >> 2]);
            stringBuffer.append(cArr[((b << 4) & 63) | (b2 >> 4)]);
            stringBuffer.append(cArr[((b2 << 2) & 63) | (b3 >> 6)]);
            stringBuffer.append(cArr[b3 & 63]);
            i3++;
            i4 = i7;
        }
        if (i2 != 0) {
            int i8 = i4 + 1;
            byte b4 = bArr[i4] & 255;
            stringBuffer.append(cArr[b4 >> 2]);
            if (i2 == 1) {
                stringBuffer.append(cArr[(b4 << 4) & 63]);
                stringBuffer.append("==");
            } else {
                byte b5 = bArr[i8] & 255;
                stringBuffer.append(cArr[((b4 << 4) & 63) | (b5 >> 4)]);
                stringBuffer.append(cArr[(b5 << 2) & 63]);
                stringBuffer.append('=');
            }
        }
        return stringBuffer.toString();
    }

    static byte[] base64ToByteArray(String str) {
        return base64ToByteArray(str, false);
    }

    static byte[] altBase64ToByteArray(String str) {
        return base64ToByteArray(str, true);
    }

    private static byte[] base64ToByteArray(String str, boolean z) {
        int i;
        int i2;
        byte[] bArr = z ? altBase64ToInt : base64ToInt;
        int length = str.length();
        int i3 = length / 4;
        if (i3 * 4 == length) {
            int i4 = 0;
            if (length != 0) {
                if (str.charAt(length - 1) == '=') {
                    i2 = i3 - 1;
                    i = 1;
                } else {
                    i2 = i3;
                    i = 0;
                }
                if (str.charAt(length - 2) == '=') {
                    i++;
                }
            } else {
                i2 = i3;
                i = 0;
            }
            byte[] bArr2 = new byte[((i3 * 3) - i)];
            int i5 = 0;
            int i6 = 0;
            while (i4 < i2) {
                int i7 = i5 + 1;
                int base64toInt = base64toInt(str.charAt(i5), bArr);
                int i8 = i7 + 1;
                int base64toInt2 = base64toInt(str.charAt(i7), bArr);
                int i9 = i8 + 1;
                int base64toInt3 = base64toInt(str.charAt(i8), bArr);
                int i10 = i9 + 1;
                int base64toInt4 = base64toInt(str.charAt(i9), bArr);
                int i11 = i6 + 1;
                bArr2[i6] = (byte) ((base64toInt << 2) | (base64toInt2 >> 4));
                int i12 = i11 + 1;
                bArr2[i11] = (byte) ((base64toInt2 << 4) | (base64toInt3 >> 2));
                i6 = i12 + 1;
                bArr2[i12] = (byte) ((base64toInt3 << 6) | base64toInt4);
                i4++;
                i5 = i10;
            }
            if (i != 0) {
                int i13 = i5 + 1;
                int base64toInt5 = base64toInt(str.charAt(i5), bArr);
                int i14 = i13 + 1;
                int base64toInt6 = base64toInt(str.charAt(i13), bArr);
                int i15 = i6 + 1;
                bArr2[i6] = (byte) ((base64toInt5 << 2) | (base64toInt6 >> 4));
                if (i == 1) {
                    bArr2[i15] = (byte) ((base64toInt(str.charAt(i14), bArr) >> 2) | (base64toInt6 << 4));
                }
            }
            return bArr2;
        }
        throw new IllegalArgumentException("String length must be a multiple of four.");
    }

    private static int base64toInt(char c, byte[] bArr) {
        byte b = bArr[c];
        if (b >= 0) {
            return b;
        }
        throw new IllegalArgumentException("Illegal character " + c);
    }

    public static void main(String[] strArr) {
        int parseInt = Integer.parseInt(strArr[0]);
        int parseInt2 = Integer.parseInt(strArr[1]);
        Random random = new Random();
        for (int i = 0; i < parseInt; i++) {
            for (int i2 = 0; i2 < parseInt2; i2++) {
                byte[] bArr = new byte[i2];
                for (int i3 = 0; i3 < i2; i3++) {
                    bArr[i3] = (byte) random.nextInt();
                }
                if (!Arrays.equals(bArr, base64ToByteArray(byteArrayToBase64(bArr)))) {
                    System.out.println("Dismal failure!");
                }
                if (!Arrays.equals(bArr, altBase64ToByteArray(byteArrayToAltBase64(bArr)))) {
                    System.out.println("Alternate dismal failure!");
                }
            }
        }
    }
}
