package java.net;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.p026io.CharArrayWriter;
import java.p026io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.Objects;
import sun.security.action.GetPropertyAction;

public class URLEncoder {
    static final int caseDiff = 32;
    static String dfltEncName = GetPropertyAction.privilegedGetProperty("file.encoding");
    static BitSet dontNeedEncoding = new BitSet(256);

    static {
        for (int i = 97; i <= 122; i++) {
            dontNeedEncoding.set(i);
        }
        for (int i2 = 65; i2 <= 90; i2++) {
            dontNeedEncoding.set(i2);
        }
        for (int i3 = 48; i3 <= 57; i3++) {
            dontNeedEncoding.set(i3);
        }
        dontNeedEncoding.set(32);
        dontNeedEncoding.set(45);
        dontNeedEncoding.set(95);
        dontNeedEncoding.set(46);
        dontNeedEncoding.set(42);
    }

    private URLEncoder() {
    }

    @Deprecated
    public static String encode(String str) {
        try {
            return encode(str, dfltEncName);
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String encode(String str, String str2) throws UnsupportedEncodingException {
        if (str2 != null) {
            try {
                return encode(str, Charset.forName(str2));
            } catch (IllegalCharsetNameException | UnsupportedCharsetException unused) {
                throw new UnsupportedEncodingException(str2);
            }
        } else {
            throw new NullPointerException("charsetName");
        }
    }

    public static String encode(String str, Charset charset) {
        BitSet bitSet;
        int i;
        char charAt;
        Objects.requireNonNull(charset, "charset");
        StringBuilder sb = new StringBuilder(str.length());
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        int i2 = 0;
        boolean z = false;
        while (i2 < str.length()) {
            char charAt2 = str.charAt(i2);
            if (dontNeedEncoding.get(charAt2)) {
                if (charAt2 == ' ') {
                    charAt2 = '+';
                    z = true;
                }
                sb.append((char) charAt2);
                i2++;
            } else {
                do {
                    charArrayWriter.write(charAt2);
                    if (charAt2 >= 55296 && charAt2 <= 56319 && (i = i2 + 1) < str.length() && (charAt = str.charAt(i)) >= 56320 && charAt <= 57343) {
                        charArrayWriter.write(charAt);
                        i2 = i;
                    }
                    i2++;
                    if (i2 >= str.length()) {
                        break;
                    }
                    bitSet = dontNeedEncoding;
                    charAt2 = str.charAt(i2);
                } while (!bitSet.get(charAt2));
                charArrayWriter.flush();
                byte[] bytes = new String(charArrayWriter.toCharArray()).getBytes(charset);
                for (int i3 = 0; i3 < bytes.length; i3++) {
                    sb.append('%');
                    char forDigit = Character.forDigit((bytes[i3] >> 4) & 15, 16);
                    if (Character.isLetter(forDigit)) {
                        forDigit = (char) (forDigit - ' ');
                    }
                    sb.append(forDigit);
                    char forDigit2 = Character.forDigit(bytes[i3] & 15, 16);
                    if (Character.isLetter(forDigit2)) {
                        forDigit2 = (char) (forDigit2 - ' ');
                    }
                    sb.append(forDigit2);
                }
                charArrayWriter.reset();
                z = true;
            }
        }
        return z ? sb.toString() : str;
    }
}
