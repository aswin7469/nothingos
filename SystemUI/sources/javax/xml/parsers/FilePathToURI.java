package javax.xml.parsers;

import java.p026io.File;
import java.p026io.UnsupportedEncodingException;
import kotlin.text.Typography;

class FilePathToURI {
    private static char[] gAfterEscaping1 = new char[128];
    private static char[] gAfterEscaping2 = new char[128];
    private static char[] gHexChs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static boolean[] gNeedEscaping = new boolean[128];

    FilePathToURI() {
    }

    static {
        for (int i = 0; i <= 31; i++) {
            gNeedEscaping[i] = true;
            char[] cArr = gAfterEscaping1;
            char[] cArr2 = gHexChs;
            cArr[i] = cArr2[i >> 4];
            gAfterEscaping2[i] = cArr2[i & 15];
        }
        gNeedEscaping[127] = true;
        gAfterEscaping1[127] = '7';
        gAfterEscaping2[127] = 'F';
        char[] cArr3 = {' ', Typography.less, Typography.greater, '#', '%', Typography.quote, '{', '}', '|', '\\', '^', '~', '[', ']', '`'};
        for (int i2 = 0; i2 < 15; i2++) {
            char c = cArr3[i2];
            gNeedEscaping[c] = true;
            char[] cArr4 = gAfterEscaping1;
            char[] cArr5 = gHexChs;
            cArr4[c] = cArr5[c >> 4];
            gAfterEscaping2[c] = cArr5[c & 15];
        }
    }

    public static String filepath2URI(String str) {
        char charAt;
        char upperCase;
        if (str == null) {
            return null;
        }
        String replace = str.replace(File.separatorChar, '/');
        int length = replace.length();
        StringBuilder sb = new StringBuilder(length * 3);
        sb.append("file://");
        if (length >= 2 && replace.charAt(1) == ':' && (upperCase = Character.toUpperCase(replace.charAt(0))) >= 'A' && upperCase <= 'Z') {
            sb.append('/');
        }
        int i = 0;
        while (i < length && (charAt = replace.charAt(i)) < 128) {
            if (gNeedEscaping[charAt]) {
                sb.append('%');
                sb.append(gAfterEscaping1[charAt]);
                sb.append(gAfterEscaping2[charAt]);
            } else {
                sb.append((char) charAt);
            }
            i++;
        }
        if (i < length) {
            try {
                for (byte b : replace.substring(i).getBytes("UTF-8")) {
                    if (b < 0) {
                        int i2 = b + 256;
                        sb.append('%');
                        sb.append(gHexChs[i2 >> 4]);
                        sb.append(gHexChs[i2 & 15]);
                    } else if (gNeedEscaping[b]) {
                        sb.append('%');
                        sb.append(gAfterEscaping1[b]);
                        sb.append(gAfterEscaping2[b]);
                    } else {
                        sb.append((char) b);
                    }
                }
            } catch (UnsupportedEncodingException unused) {
                return replace;
            }
        }
        return sb.toString();
    }
}
