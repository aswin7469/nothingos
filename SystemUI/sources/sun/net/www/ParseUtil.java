package sun.net.www;

import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.p026io.File;
import java.util.BitSet;
import sun.nio.p034cs.ThreadLocalCoders;
import sun.util.locale.LanguageTag;

public class ParseUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long H_ALPHA;
    private static final long H_ALPHANUM;
    private static final long H_DASH;
    private static final long H_DIGIT = 0;
    private static final long H_ESCAPED = 0;
    private static final long H_HEX = (highMask('A', 'F') | highMask('a', 'f'));
    private static final long H_LOWALPHA;
    private static final long H_MARK;
    private static final long H_PATH;
    private static final long H_PCHAR;
    private static final long H_REG_NAME;
    private static final long H_RESERVED;
    private static final long H_SERVER;
    private static final long H_UNRESERVED;
    private static final long H_UPALPHA;
    private static final long H_URIC;
    private static final long H_USERINFO;
    private static final long L_ALPHA = 0;
    private static final long L_ALPHANUM;
    private static final long L_DASH;
    private static final long L_DIGIT;
    private static final long L_ESCAPED = 1;
    private static final long L_HEX;
    private static final long L_LOWALPHA = 0;
    private static final long L_MARK;
    private static final long L_PATH;
    private static final long L_PCHAR;
    private static final long L_REG_NAME;
    private static final long L_RESERVED;
    private static final long L_SERVER;
    private static final long L_UNRESERVED;
    private static final long L_UPALPHA = 0;
    private static final long L_URIC;
    private static final long L_USERINFO;
    static BitSet encodedInPath;
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static boolean match(char c, long j, long j2) {
        if (c < '@') {
            return ((1 << c) & j) != 0;
        }
        if (c < 128) {
            return ((1 << (c - '@')) & j2) != 0;
        }
        return false;
    }

    static {
        BitSet bitSet = new BitSet(256);
        encodedInPath = bitSet;
        bitSet.set(61);
        encodedInPath.set(59);
        encodedInPath.set(63);
        encodedInPath.set(47);
        encodedInPath.set(35);
        encodedInPath.set(32);
        encodedInPath.set(60);
        encodedInPath.set(62);
        encodedInPath.set(37);
        encodedInPath.set(34);
        encodedInPath.set(123);
        encodedInPath.set(125);
        encodedInPath.set(124);
        encodedInPath.set(92);
        encodedInPath.set(94);
        encodedInPath.set(91);
        encodedInPath.set(93);
        encodedInPath.set(96);
        for (int i = 0; i < 32; i++) {
            encodedInPath.set(i);
        }
        encodedInPath.set(127);
        long lowMask = lowMask('0', '9');
        L_DIGIT = lowMask;
        L_HEX = lowMask;
        long highMask = highMask('A', 'Z');
        H_UPALPHA = highMask;
        long highMask2 = highMask('a', 'z');
        H_LOWALPHA = highMask2;
        long j = highMask | highMask2;
        H_ALPHA = j;
        long j2 = lowMask | 0;
        L_ALPHANUM = j2;
        long j3 = j | 0;
        H_ALPHANUM = j3;
        long lowMask2 = lowMask("-_.!~*'()");
        L_MARK = lowMask2;
        long highMask3 = highMask("-_.!~*'()");
        H_MARK = highMask3;
        long j4 = j2 | lowMask2;
        L_UNRESERVED = j4;
        long j5 = j3 | highMask3;
        H_UNRESERVED = j5;
        long lowMask3 = lowMask(";/?:@&=+$,[]");
        L_RESERVED = lowMask3;
        long highMask4 = highMask(";/?:@&=+$,[]");
        H_RESERVED = highMask4;
        long lowMask4 = lowMask(LanguageTag.SEP);
        L_DASH = lowMask4;
        long highMask5 = highMask(LanguageTag.SEP);
        H_DASH = highMask5;
        L_URIC = lowMask3 | j4 | 1;
        H_URIC = highMask4 | j5 | 0;
        long lowMask5 = j4 | 1 | lowMask(":@&=+$,");
        L_PCHAR = lowMask5;
        long highMask6 = j5 | 0 | highMask(":@&=+$,");
        H_PCHAR = highMask6;
        L_PATH = lowMask5 | lowMask(";/");
        H_PATH = highMask(";/") | highMask6;
        long lowMask6 = j4 | 1 | lowMask(";:&=+$,");
        L_USERINFO = lowMask6;
        long highMask7 = j5 | 0 | highMask(";:&=+$,");
        H_USERINFO = highMask7;
        L_REG_NAME = j4 | 1 | lowMask("$,;:@&=+");
        H_REG_NAME = 0 | j5 | highMask("$,;:@&=+");
        L_SERVER = j2 | lowMask6 | lowMask4 | lowMask(".:@[]");
        H_SERVER = highMask7 | j3 | highMask5 | highMask(".:@[]");
    }

    public static String encodePath(String str) {
        return encodePath(str, true);
    }

    public static String encodePath(String str, boolean z) {
        int i;
        char[] cArr = new char[((str.length() * 2) + 16)];
        char[] charArray = str.toCharArray();
        int length = str.length();
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            char c = charArray[i3];
            if ((!z && c == '/') || (z && c == File.separatorChar)) {
                cArr[i2] = '/';
                i2++;
            } else if (c <= 127) {
                if ((c >= 'a' && c <= 'z') || ((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))) {
                    i = i2 + 1;
                    cArr[i2] = c;
                } else if (encodedInPath.get(c)) {
                    i2 = escape(cArr, c, i2);
                } else {
                    i = i2 + 1;
                    cArr[i2] = c;
                }
                i2 = i;
            } else if (c > 2047) {
                i2 = escape(cArr, (char) (((c >> 0) & 63) | 128), escape(cArr, (char) (((c >> 6) & 63) | 128), escape(cArr, (char) (((c >> 12) & 15) | 224), i2)));
            } else {
                i2 = escape(cArr, (char) (((c >> 0) & 63) | 128), escape(cArr, (char) (((c >> 6) & 31) | 192), i2));
            }
            if (i2 + 9 > cArr.length) {
                int length2 = (cArr.length * 2) + 16;
                if (length2 < 0) {
                    length2 = Integer.MAX_VALUE;
                }
                char[] cArr2 = new char[length2];
                System.arraycopy((Object) cArr, 0, (Object) cArr2, 0, i2);
                cArr = cArr2;
            }
        }
        return new String(cArr, 0, i2);
    }

    private static int escape(char[] cArr, char c, int i) {
        int i2 = i + 1;
        cArr[i] = '%';
        int i3 = i2 + 1;
        cArr[i2] = Character.forDigit((c >> 4) & 15, 16);
        int i4 = i3 + 1;
        cArr[i3] = Character.forDigit(c & 15, 16);
        return i4;
    }

    private static byte unescape(String str, int i) {
        return (byte) Integer.parseInt(str.substring(i + 1, i + 3), 16);
    }

    public static String decode(String str) {
        int length = str.length();
        if (length == 0 || str.indexOf(37) < 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(length);
        ByteBuffer allocate = ByteBuffer.allocate(length);
        CharBuffer allocate2 = CharBuffer.allocate(length);
        CharsetDecoder onUnmappableCharacter = ThreadLocalCoders.decoderFor("UTF-8").onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        int i = 0;
        char charAt = str.charAt(0);
        while (i < length) {
            if (charAt != '%') {
                sb.append(charAt);
                i++;
                if (i >= length) {
                    break;
                }
                charAt = str.charAt(i);
            } else {
                allocate.clear();
                do {
                    try {
                        allocate.put(unescape(str, i));
                        i += 3;
                        if (i >= length || (charAt = str.charAt(i)) != '%') {
                            allocate.flip();
                            allocate2.clear();
                            onUnmappableCharacter.reset();
                        }
                        allocate.put(unescape(str, i));
                        i += 3;
                        break;
                    } catch (NumberFormatException unused) {
                        throw new IllegalArgumentException();
                    }
                } while ((charAt = str.charAt(i)) != '%');
                allocate.flip();
                allocate2.clear();
                onUnmappableCharacter.reset();
                if (onUnmappableCharacter.decode(allocate, allocate2, true).isError()) {
                    throw new IllegalArgumentException("Error decoding percent encoded characters");
                } else if (!onUnmappableCharacter.flush(allocate2).isError()) {
                    sb.append(allocate2.flip().toString());
                } else {
                    throw new IllegalArgumentException("Error decoding percent encoded characters");
                }
            }
        }
        return sb.toString();
    }

    public String canonizeString(String str) {
        str.length();
        while (true) {
            int indexOf = str.indexOf("/../");
            if (indexOf < 0) {
                break;
            }
            int lastIndexOf = str.lastIndexOf(47, indexOf - 1);
            if (lastIndexOf >= 0) {
                str = str.substring(0, lastIndexOf) + str.substring(indexOf + 3);
            } else {
                str = str.substring(indexOf + 3);
            }
        }
        while (true) {
            int indexOf2 = str.indexOf("/./");
            if (indexOf2 < 0) {
                break;
            }
            str = str.substring(0, indexOf2) + str.substring(indexOf2 + 2);
        }
        while (str.endsWith("/..")) {
            int indexOf3 = str.indexOf("/..");
            int lastIndexOf2 = str.lastIndexOf(47, indexOf3 - 1);
            if (lastIndexOf2 >= 0) {
                r5 = str.substring(0, lastIndexOf2 + 1);
            } else {
                r5 = str.substring(0, indexOf3);
            }
        }
        return str.endsWith("/.") ? str.substring(0, str.length() - 1) : str;
    }

    public static URL fileToEncodedURL(File file) throws MalformedURLException {
        String encodePath = encodePath(file.getAbsolutePath());
        if (!encodePath.startsWith("/")) {
            encodePath = "/" + encodePath;
        }
        if (!encodePath.endsWith("/") && file.isDirectory()) {
            encodePath = encodePath + "/";
        }
        return new URL("file", "", encodePath);
    }

    public static URI toURI(URL url) {
        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        String path = url.getPath();
        String query = url.getQuery();
        String ref = url.getRef();
        if (path != null && !path.startsWith("/")) {
            path = "/" + path;
        }
        if (authority != null && authority.endsWith(":-1")) {
            authority = authority.substring(0, authority.length() - 3);
        }
        try {
            return createURI(protocol, authority, path, query, ref);
        } catch (URISyntaxException unused) {
            return null;
        }
    }

    private static URI createURI(String str, String str2, String str3, String str4, String str5) throws URISyntaxException {
        String parseUtil = toString(str, (String) null, str2, (String) null, (String) null, -1, str3, str4, str5);
        checkPath(parseUtil, str, str3);
        return new URI(parseUtil);
    }

    private static String toString(String str, String str2, String str3, String str4, String str5, int i, String str6, String str7, String str8) {
        String str9 = str;
        StringBuffer stringBuffer = new StringBuffer();
        if (str9 != null) {
            stringBuffer.append(str);
            stringBuffer.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        }
        appendSchemeSpecificPart(stringBuffer, str2, str3, str4, str5, i, str6, str7);
        appendFragment(stringBuffer, str8);
        return stringBuffer.toString();
    }

    private static void appendSchemeSpecificPart(StringBuffer stringBuffer, String str, String str2, String str3, String str4, int i, String str5, String str6) {
        String str7;
        if (str == null) {
            appendAuthority(stringBuffer, str2, str3, str4, i);
            if (str5 != null) {
                stringBuffer.append(quote(str5, L_PATH, H_PATH));
            }
            if (str6 != null) {
                stringBuffer.append('?');
                stringBuffer.append(quote(str6, L_URIC, H_URIC));
            }
        } else if (str.startsWith("//[")) {
            int indexOf = str.indexOf(NavigationBarInflaterView.SIZE_MOD_END);
            if (indexOf != -1 && str.indexOf(":") != -1) {
                if (indexOf == str.length()) {
                    str7 = "";
                } else {
                    int i2 = indexOf + 1;
                    String substring = str.substring(0, i2);
                    str7 = str.substring(i2);
                    str = substring;
                }
                stringBuffer.append(str);
                stringBuffer.append(quote(str7, L_URIC, H_URIC));
            }
        } else {
            stringBuffer.append(quote(str, L_URIC, H_URIC));
        }
    }

    private static void appendAuthority(StringBuffer stringBuffer, String str, String str2, String str3, int i) {
        String str4;
        boolean z = true;
        if (str3 != null) {
            stringBuffer.append("//");
            if (str2 != null) {
                stringBuffer.append(quote(str2, L_USERINFO, H_USERINFO));
                stringBuffer.append('@');
            }
            if (str3.indexOf(58) < 0 || str3.startsWith(NavigationBarInflaterView.SIZE_MOD_START) || str3.endsWith(NavigationBarInflaterView.SIZE_MOD_END)) {
                z = false;
            }
            if (z) {
                stringBuffer.append('[');
            }
            stringBuffer.append(str3);
            if (z) {
                stringBuffer.append(']');
            }
            if (i != -1) {
                stringBuffer.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                stringBuffer.append(i);
            }
        } else if (str != null) {
            stringBuffer.append("//");
            if (str.startsWith(NavigationBarInflaterView.SIZE_MOD_START)) {
                int indexOf = str.indexOf(NavigationBarInflaterView.SIZE_MOD_END);
                if (indexOf != -1 && str.indexOf(":") != -1) {
                    if (indexOf == str.length()) {
                        str4 = "";
                    } else {
                        int i2 = indexOf + 1;
                        String substring = str.substring(0, i2);
                        str4 = str.substring(i2);
                        str = substring;
                    }
                    stringBuffer.append(str);
                    stringBuffer.append(quote(str4, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
                    return;
                }
                return;
            }
            stringBuffer.append(quote(str, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
        }
    }

    private static void appendFragment(StringBuffer stringBuffer, String str) {
        if (str != null) {
            stringBuffer.append('#');
            stringBuffer.append(quote(str, L_URIC, H_URIC));
        }
    }

    private static String quote(String str, long j, long j2) {
        str.length();
        boolean z = (1 & j) != 0;
        StringBuffer stringBuffer = null;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt < 128) {
                if (!match(charAt, j, j2) && !isEscaped(str, i)) {
                    if (stringBuffer == null) {
                        stringBuffer = new StringBuffer();
                        stringBuffer.append(str.substring(0, i));
                    }
                    appendEscape(stringBuffer, (byte) charAt);
                } else if (stringBuffer != null) {
                    stringBuffer.append(charAt);
                }
            } else if (z && (Character.isSpaceChar(charAt) || Character.isISOControl(charAt))) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer();
                    stringBuffer.append(str.substring(0, i));
                }
                appendEncoded(stringBuffer, charAt);
            } else if (stringBuffer != null) {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer == null ? str : stringBuffer.toString();
    }

    private static boolean isEscaped(String str, int i) {
        int i2;
        if (str == null || str.length() <= (i2 = i + 2) || str.charAt(i) != '%') {
            return false;
        }
        char charAt = str.charAt(i + 1);
        long j = L_HEX;
        long j2 = H_HEX;
        if (!match(charAt, j, j2) || !match(str.charAt(i2), j, j2)) {
            return false;
        }
        return true;
    }

    private static void appendEncoded(StringBuffer stringBuffer, char c) {
        ByteBuffer byteBuffer;
        try {
            CharsetEncoder encoderFor = ThreadLocalCoders.encoderFor("UTF-8");
            byteBuffer = encoderFor.encode(CharBuffer.wrap((CharSequence) "" + c));
        } catch (CharacterCodingException unused) {
            byteBuffer = null;
        }
        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get() & 255;
            if (b >= 128) {
                appendEscape(stringBuffer, (byte) b);
            } else {
                stringBuffer.append((char) b);
            }
        }
    }

    private static void appendEscape(StringBuffer stringBuffer, byte b) {
        stringBuffer.append('%');
        char[] cArr = hexDigits;
        stringBuffer.append(cArr[(b >> 4) & 15]);
        stringBuffer.append(cArr[(b >> 0) & 15]);
    }

    private static void checkPath(String str, String str2, String str3) throws URISyntaxException {
        if (str2 != null && str3 != null && str3.length() > 0 && str3.charAt(0) != '/') {
            throw new URISyntaxException(str, "Relative path in absolute URI");
        }
    }

    private static long lowMask(char c, char c2) {
        long j = 0;
        for (int max = Math.max(Math.min((int) c, 63), 0); max <= Math.max(Math.min((int) c2, 63), 0); max++) {
            j |= 1 << max;
        }
        return j;
    }

    private static long lowMask(String str) {
        int length = str.length();
        long j = 0;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt < '@') {
                j |= 1 << charAt;
            }
        }
        return j;
    }

    private static long highMask(char c, char c2) {
        long j = 0;
        for (int max = Math.max(Math.min((int) c, 127), 64) - 64; max <= Math.max(Math.min((int) c2, 127), 64) - 64; max++) {
            j |= 1 << max;
        }
        return j;
    }

    private static long highMask(String str) {
        int length = str.length();
        long j = 0;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt >= '@' && charAt < 128) {
                j |= 1 << (charAt - '@');
            }
        }
        return j;
    }
}
