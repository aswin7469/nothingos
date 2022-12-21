package java.lang;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.p026io.UnsupportedEncodingException;
import libcore.util.CharsetUtils;
import libcore.util.EmptyArray;

public final class StringFactory {
    private static final char REPLACEMENT_CHAR = '�';

    public static native String newStringFromBytes(byte[] bArr, int i, int i2, int i3);

    static native String newStringFromChars(int i, int i2, char[] cArr);

    public static native String newStringFromString(String str);

    public static native String newStringFromUtf8Bytes(byte[] bArr, int i, int i2);

    public static String newEmptyString() {
        return newStringFromChars(EmptyArray.CHAR, 0, 0);
    }

    public static String newStringFromBytes(byte[] bArr) {
        return newStringFromBytes(bArr, 0, bArr.length);
    }

    public static String newStringFromBytes(byte[] bArr, int i) {
        return newStringFromBytes(bArr, i, 0, bArr.length);
    }

    public static String newStringFromBytes(byte[] bArr, int i, int i2) {
        return newStringFromBytes(bArr, i, i2, Charset.defaultCharset());
    }

    public static String newStringFromBytes(byte[] bArr, int i, int i2, String str) throws UnsupportedEncodingException {
        return newStringFromBytes(bArr, i, i2, Charset.forNameUEE(str));
    }

    public static String newStringFromBytes(byte[] bArr, String str) throws UnsupportedEncodingException {
        return newStringFromBytes(bArr, 0, bArr.length, Charset.forNameUEE(str));
    }

    public static String newStringFromBytes(byte[] bArr, int i, int i2, Charset charset) {
        char[] cArr;
        if ((i | i2) < 0 || i2 > bArr.length - i) {
            throw new StringIndexOutOfBoundsException(bArr.length, i, i2);
        }
        String name = charset.name();
        if (name.equals("UTF-8")) {
            return newStringFromUtf8Bytes(bArr, i, i2);
        }
        if (name.equals("ISO-8859-1")) {
            cArr = new char[i2];
            CharsetUtils.isoLatin1BytesToChars(bArr, i, i2, cArr);
        } else if (name.equals("US-ASCII")) {
            cArr = new char[i2];
            CharsetUtils.asciiBytesToChars(bArr, i, i2, cArr);
        } else {
            CharBuffer decode = charset.decode(ByteBuffer.wrap(bArr, i, i2));
            i2 = decode.length();
            cArr = decode.array();
        }
        return newStringFromChars(cArr, 0, i2);
    }

    public static String newStringFromBytes(byte[] bArr, Charset charset) {
        return newStringFromBytes(bArr, 0, bArr.length, charset);
    }

    public static String newStringFromChars(char[] cArr) {
        return newStringFromChars(cArr, 0, cArr.length);
    }

    public static String newStringFromChars(char[] cArr, int i, int i2) {
        if ((i | i2) >= 0 && i2 <= cArr.length - i) {
            return newStringFromChars(i, i2, cArr);
        }
        throw new StringIndexOutOfBoundsException(cArr.length, i, i2);
    }

    public static String newStringFromStringBuffer(StringBuffer stringBuffer) {
        String newStringFromChars;
        synchronized (stringBuffer) {
            newStringFromChars = newStringFromChars(stringBuffer.getValue(), 0, stringBuffer.length());
        }
        return newStringFromChars;
    }

    public static String newStringFromCodePoints(int[] iArr, int i, int i2) {
        if (iArr == null) {
            throw new NullPointerException("codePoints == null");
        } else if ((i | i2) < 0 || i2 > iArr.length - i) {
            throw new StringIndexOutOfBoundsException(iArr.length, i, i2);
        } else {
            char[] cArr = new char[(i2 * 2)];
            int i3 = i2 + i;
            int i4 = 0;
            while (i < i3) {
                i4 += Character.toChars(iArr[i], cArr, i4);
                i++;
            }
            return newStringFromChars(cArr, 0, i4);
        }
    }

    public static String newStringFromStringBuilder(StringBuilder sb) {
        return newStringFromChars(sb.getValue(), 0, sb.length());
    }
}
