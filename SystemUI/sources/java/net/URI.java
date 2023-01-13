package java.net;

import android.net.connectivity.com.android.net.module.util.ConnectivitySettingsUtils;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.text.Normalizer;
import java.util.SeempLog;
import sun.nio.p034cs.ThreadLocalCoders;
import sun.util.locale.BaseLocale;
import sun.util.locale.LanguageTag;

public final class URI implements Comparable<URI>, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    /* access modifiers changed from: private */
    public static final long H_ALPHA;
    /* access modifiers changed from: private */
    public static final long H_ALPHANUM;
    /* access modifiers changed from: private */
    public static final long H_DASH;
    private static final long H_DIGIT = 0;
    /* access modifiers changed from: private */
    public static final long H_DOT = highMask(BaseIconCache.EMPTY_CLASS_NAME);
    private static final long H_ESCAPED = 0;
    /* access modifiers changed from: private */
    public static final long H_HEX = (highMask('A', 'F') | highMask('a', 'f'));
    private static final long H_LEFT_BRACKET = highMask(NavigationBarInflaterView.SIZE_MOD_START);
    private static final long H_LOWALPHA;
    private static final long H_MARK;
    /* access modifiers changed from: private */
    public static final long H_PATH;
    private static final long H_PCHAR;
    /* access modifiers changed from: private */
    public static final long H_REG_NAME;
    private static final long H_RESERVED;
    /* access modifiers changed from: private */
    public static final long H_SCHEME;
    /* access modifiers changed from: private */
    public static final long H_SERVER;
    /* access modifiers changed from: private */
    public static final long H_SERVER_PERCENT;
    /* access modifiers changed from: private */
    public static final long H_UNDERSCORE = highMask(BaseLocale.SEP);
    private static final long H_UNRESERVED;
    private static final long H_UPALPHA;
    /* access modifiers changed from: private */
    public static final long H_URIC;
    private static final long H_URIC_NO_SLASH;
    /* access modifiers changed from: private */
    public static final long H_USERINFO;
    private static final long L_ALPHA = 0;
    /* access modifiers changed from: private */
    public static final long L_ALPHANUM;
    /* access modifiers changed from: private */
    public static final long L_DASH;
    /* access modifiers changed from: private */
    public static final long L_DIGIT;
    /* access modifiers changed from: private */
    public static final long L_DOT = lowMask(BaseIconCache.EMPTY_CLASS_NAME);
    private static final long L_ESCAPED = 1;
    /* access modifiers changed from: private */
    public static final long L_HEX;
    private static final long L_LEFT_BRACKET = lowMask(NavigationBarInflaterView.SIZE_MOD_START);
    private static final long L_LOWALPHA = 0;
    private static final long L_MARK;
    /* access modifiers changed from: private */
    public static final long L_PATH;
    private static final long L_PCHAR;
    /* access modifiers changed from: private */
    public static final long L_REG_NAME;
    private static final long L_RESERVED;
    /* access modifiers changed from: private */
    public static final long L_SCHEME;
    /* access modifiers changed from: private */
    public static final long L_SERVER;
    /* access modifiers changed from: private */
    public static final long L_SERVER_PERCENT;
    /* access modifiers changed from: private */
    public static final long L_UNDERSCORE = lowMask(BaseLocale.SEP);
    private static final long L_UNRESERVED;
    private static final long L_UPALPHA = 0;
    /* access modifiers changed from: private */
    public static final long L_URIC;
    private static final long L_URIC_NO_SLASH;
    /* access modifiers changed from: private */
    public static final long L_USERINFO;
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final long serialVersionUID = -6052424284110960213L;
    /* access modifiers changed from: private */
    public transient String authority;
    private volatile transient String decodedAuthority;
    private volatile transient String decodedFragment;
    private volatile transient String decodedPath;
    private volatile transient String decodedQuery;
    private volatile transient String decodedSchemeSpecificPart;
    private volatile transient String decodedUserInfo;
    /* access modifiers changed from: private */
    public transient String fragment;
    private volatile transient int hash;
    /* access modifiers changed from: private */
    public transient String host;
    /* access modifiers changed from: private */
    public transient String path;
    /* access modifiers changed from: private */
    public transient int port;
    /* access modifiers changed from: private */
    public transient String query;
    /* access modifiers changed from: private */
    public transient String scheme;
    /* access modifiers changed from: private */
    public volatile transient String schemeSpecificPart;
    /* access modifiers changed from: private */
    public volatile String string;
    /* access modifiers changed from: private */
    public transient String userInfo;

    private static int decode(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        char c2 = 'a';
        if (c < 'a' || c > 'f') {
            c2 = 'A';
            if (c < 'A' || c > 'F') {
                return -1;
            }
        }
        return (c - c2) + 10;
    }

    /* access modifiers changed from: private */
    public static boolean match(char c, long j, long j2) {
        if (c == 0) {
            return false;
        }
        return c < '@' ? ((1 << c) & j) != 0 : c < 128 && ((1 << (c - '@')) & j2) != 0;
    }

    private static int toLower(char c) {
        return (c < 'A' || c > 'Z') ? c : c + ' ';
    }

    private static int toUpper(char c) {
        return (c < 'a' || c > 'z') ? c : c - ' ';
    }

    private URI() {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
    }

    public URI(String str) throws URISyntaxException {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
        SeempLog.record_str(92, "s:" + str);
        new Parser(str).parse(false);
    }

    public URI(String str, String str2, String str3, int i, String str4, String str5, String str6) throws URISyntaxException {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
        String uri = toString(str, (String) null, (String) null, str2, str3, i, str4, str5, str6);
        checkPath(uri, str, str4);
        new Parser(uri).parse(true);
    }

    public URI(String str, String str2, String str3, String str4, String str5) throws URISyntaxException {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
        String uri = toString(str, (String) null, str2, (String) null, (String) null, -1, str3, str4, str5);
        String str6 = str3;
        checkPath(uri, str, str3);
        new Parser(uri).parse(false);
    }

    public URI(String str, String str2, String str3, String str4) throws URISyntaxException {
        this(str, (String) null, str2, -1, str3, (String) null, str4);
    }

    public URI(String str, String str2, String str3) throws URISyntaxException {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
        new Parser(toString(str, str2, (String) null, (String) null, (String) null, -1, (String) null, (String) null, str3)).parse(false);
    }

    public static URI create(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public URI parseServerAuthority() throws URISyntaxException {
        if (this.host == null && this.authority != null) {
            defineString();
            new Parser(this.string).parse(true);
        }
        return this;
    }

    public URI normalize() {
        return normalize(this);
    }

    public URI resolve(URI uri) {
        return resolve(this, uri);
    }

    public URI resolve(String str) {
        return resolve(create(str));
    }

    public URI relativize(URI uri) {
        return relativize(this, uri);
    }

    public URL toURL() throws MalformedURLException {
        if (isAbsolute()) {
            return new URL(toString());
        }
        throw new IllegalArgumentException("URI is not absolute");
    }

    public String getScheme() {
        return this.scheme;
    }

    public boolean isAbsolute() {
        return this.scheme != null;
    }

    public boolean isOpaque() {
        return this.path == null;
    }

    public String getRawSchemeSpecificPart() {
        defineSchemeSpecificPart();
        return this.schemeSpecificPart;
    }

    public String getSchemeSpecificPart() {
        if (this.decodedSchemeSpecificPart == null) {
            this.decodedSchemeSpecificPart = decode(getRawSchemeSpecificPart());
        }
        return this.decodedSchemeSpecificPart;
    }

    public String getRawAuthority() {
        return this.authority;
    }

    public String getAuthority() {
        if (this.decodedAuthority == null) {
            this.decodedAuthority = decode(this.authority);
        }
        return this.decodedAuthority;
    }

    public String getRawUserInfo() {
        return this.userInfo;
    }

    public String getUserInfo() {
        String str;
        if (this.decodedUserInfo == null && (str = this.userInfo) != null) {
            this.decodedUserInfo = decode(str);
        }
        return this.decodedUserInfo;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getRawPath() {
        return this.path;
    }

    public String getPath() {
        String str;
        if (this.decodedPath == null && (str = this.path) != null) {
            this.decodedPath = decode(str);
        }
        return this.decodedPath;
    }

    public String getRawQuery() {
        return this.query;
    }

    public String getQuery() {
        String str;
        if (this.decodedQuery == null && (str = this.query) != null) {
            this.decodedQuery = decode(str);
        }
        return this.decodedQuery;
    }

    public String getRawFragment() {
        return this.fragment;
    }

    public String getFragment() {
        String str;
        if (this.decodedFragment == null && (str = this.fragment) != null) {
            this.decodedFragment = decode(str);
        }
        return this.decodedFragment;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof URI)) {
            return false;
        }
        URI uri = (URI) obj;
        if (isOpaque() != uri.isOpaque() || !equalIgnoringCase(this.scheme, uri.scheme) || !equal(this.fragment, uri.fragment)) {
            return false;
        }
        if (isOpaque()) {
            return equal(this.schemeSpecificPart, uri.schemeSpecificPart);
        }
        if (!equal(this.path, uri.path) || !equal(this.query, uri.query)) {
            return false;
        }
        String str = this.authority;
        String str2 = uri.authority;
        if (str == str2) {
            return true;
        }
        if (this.host != null) {
            return equal(this.userInfo, uri.userInfo) && equalIgnoringCase(this.host, uri.host) && this.port == uri.port;
        }
        if (str != null) {
            if (!equal(str, str2)) {
                return false;
            }
        } else if (str != str2) {
            return false;
        }
    }

    public int hashCode() {
        int i;
        if (this.hash != 0) {
            return this.hash;
        }
        int hash2 = hash(hashIgnoringCase(0, this.scheme), this.fragment);
        if (isOpaque()) {
            i = hash(hash2, this.schemeSpecificPart);
        } else {
            int hash3 = hash(hash(hash2, this.path), this.query);
            if (this.host != null) {
                i = hashIgnoringCase(hash(hash3, this.userInfo), this.host) + (this.port * 1949);
            } else {
                i = hash(hash3, this.authority);
            }
        }
        this.hash = i;
        return i;
    }

    public int compareTo(URI uri) {
        int compareIgnoringCase = compareIgnoringCase(this.scheme, uri.scheme);
        if (compareIgnoringCase != 0) {
            return compareIgnoringCase;
        }
        if (isOpaque()) {
            if (!uri.isOpaque()) {
                return 1;
            }
            int compare = compare(this.schemeSpecificPart, uri.schemeSpecificPart);
            if (compare != 0) {
                return compare;
            }
            return compare(this.fragment, uri.fragment);
        } else if (uri.isOpaque()) {
            return -1;
        } else {
            if (this.host == null || uri.host == null) {
                int compare2 = compare(this.authority, uri.authority);
                if (compare2 != 0) {
                    return compare2;
                }
            } else {
                int compare3 = compare(this.userInfo, uri.userInfo);
                if (compare3 != 0) {
                    return compare3;
                }
                int compareIgnoringCase2 = compareIgnoringCase(this.host, uri.host);
                if (compareIgnoringCase2 != 0) {
                    return compareIgnoringCase2;
                }
                int i = this.port - uri.port;
                if (i != 0) {
                    return i;
                }
            }
            int compare4 = compare(this.path, uri.path);
            if (compare4 != 0) {
                return compare4;
            }
            int compare5 = compare(this.query, uri.query);
            if (compare5 != 0) {
                return compare5;
            }
            return compare(this.fragment, uri.fragment);
        }
    }

    public String toString() {
        defineString();
        return this.string;
    }

    public String toASCIIString() {
        defineString();
        return encode(this.string);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        defineString();
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        this.port = -1;
        objectInputStream.defaultReadObject();
        try {
            new Parser(this.string).parse(false);
        } catch (URISyntaxException e) {
            InvalidObjectException invalidObjectException = new InvalidObjectException("Invalid URI");
            invalidObjectException.initCause(e);
            throw invalidObjectException;
        }
    }

    private static boolean equal(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str == null || str2 == null || str.length() != str2.length()) {
            return false;
        }
        if (str.indexOf(37) < 0) {
            return str.equals(str2);
        }
        int length = str.length();
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            char charAt2 = str2.charAt(i);
            if (charAt != '%') {
                if (charAt != charAt2) {
                    return false;
                }
            } else if (charAt2 != '%') {
                return false;
            } else {
                int i2 = i + 1;
                if (toLower(str.charAt(i2)) != toLower(str2.charAt(i2))) {
                    return false;
                }
                i = i2 + 1;
                if (toLower(str.charAt(i)) != toLower(str2.charAt(i))) {
                    return false;
                }
            }
            i++;
        }
        return true;
    }

    private static boolean equalIgnoringCase(String str, String str2) {
        int length;
        if (str == str2) {
            return true;
        }
        if (str == null || str2 == null || str2.length() != (length = str.length())) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (toLower(str.charAt(i)) != toLower(str2.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static int hash(int i, String str) {
        if (str == null) {
            return i;
        }
        if (str.indexOf(37) < 0) {
            return (i * 127) + str.hashCode();
        }
        return normalizedHash(i, str);
    }

    private static int normalizedHash(int i, String str) {
        int i2 = 0;
        int i3 = 0;
        while (i2 < str.length()) {
            char charAt = str.charAt(i2);
            i3 = (i3 * 31) + charAt;
            if (charAt == '%') {
                for (int i4 = i2 + 1; i4 < i2 + 3; i4++) {
                    i3 = (i3 * 31) + toUpper(str.charAt(i4));
                }
                i2 += 2;
            }
            i2++;
        }
        return (i * 127) + i3;
    }

    private static int hashIgnoringCase(int i, String str) {
        if (str == null) {
            return i;
        }
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            i = (i * 31) + toLower(str.charAt(i2));
        }
        return i;
    }

    private static int compare(String str, String str2) {
        if (str == str2) {
            return 0;
        }
        if (str == null) {
            return -1;
        }
        if (str2 != null) {
            return str.compareTo(str2);
        }
        return 1;
    }

    private static int compareIgnoringCase(String str, String str2) {
        if (str == str2) {
            return 0;
        }
        if (str == null) {
            return -1;
        }
        if (str2 == null) {
            return 1;
        }
        int length = str.length();
        int length2 = str2.length();
        int i = length < length2 ? length : length2;
        for (int i2 = 0; i2 < i; i2++) {
            int lower = toLower(str.charAt(i2)) - toLower(str2.charAt(i2));
            if (lower != 0) {
                return lower;
            }
        }
        return length - length2;
    }

    private static void checkPath(String str, String str2, String str3) throws URISyntaxException {
        if (str2 != null && str3 != null && str3.length() > 0 && str3.charAt(0) != '/') {
            throw new URISyntaxException(str, "Relative path in absolute URI");
        }
    }

    private void appendAuthority(StringBuffer stringBuffer, String str, String str2, String str3, int i) {
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
                String str4 = "";
                if (indexOf == -1 || str.indexOf(":") == -1) {
                    String str5 = str4;
                    str4 = str;
                    str = str5;
                } else if (indexOf != str.length()) {
                    int i2 = indexOf + 1;
                    String substring = str.substring(0, i2);
                    str4 = str.substring(i2);
                    str = substring;
                }
                stringBuffer.append(str);
                stringBuffer.append(quote(str4, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
                return;
            }
            stringBuffer.append(quote(str, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
        }
    }

    private void appendSchemeSpecificPart(StringBuffer stringBuffer, String str, String str2, String str3, String str4, int i, String str5, String str6) {
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

    private void appendFragment(StringBuffer stringBuffer, String str) {
        if (str != null) {
            stringBuffer.append('#');
            stringBuffer.append(quote(str, L_URIC, H_URIC));
        }
    }

    private String toString(String str, String str2, String str3, String str4, String str5, int i, String str6, String str7, String str8) {
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

    private void defineSchemeSpecificPart() {
        if (this.schemeSpecificPart == null) {
            StringBuffer stringBuffer = new StringBuffer();
            appendSchemeSpecificPart(stringBuffer, (String) null, getAuthority(), getUserInfo(), this.host, this.port, getPath(), getQuery());
            if (stringBuffer.length() != 0) {
                this.schemeSpecificPart = stringBuffer.toString();
            }
        }
    }

    private void defineString() {
        if (this.string == null) {
            StringBuffer stringBuffer = new StringBuffer();
            String str = this.scheme;
            if (str != null) {
                stringBuffer.append(str);
                stringBuffer.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            }
            if (isOpaque()) {
                stringBuffer.append(this.schemeSpecificPart);
            } else {
                if (this.host != null) {
                    stringBuffer.append("//");
                    String str2 = this.userInfo;
                    if (str2 != null) {
                        stringBuffer.append(str2);
                        stringBuffer.append('@');
                    }
                    boolean z = this.host.indexOf(58) >= 0 && !this.host.startsWith(NavigationBarInflaterView.SIZE_MOD_START) && !this.host.endsWith(NavigationBarInflaterView.SIZE_MOD_END);
                    if (z) {
                        stringBuffer.append('[');
                    }
                    stringBuffer.append(this.host);
                    if (z) {
                        stringBuffer.append(']');
                    }
                    if (this.port != -1) {
                        stringBuffer.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                        stringBuffer.append(this.port);
                    }
                } else if (this.authority != null) {
                    stringBuffer.append("//");
                    stringBuffer.append(this.authority);
                }
                String str3 = this.path;
                if (str3 != null) {
                    stringBuffer.append(str3);
                }
                if (this.query != null) {
                    stringBuffer.append('?');
                    stringBuffer.append(this.query);
                }
            }
            if (this.fragment != null) {
                stringBuffer.append('#');
                stringBuffer.append(this.fragment);
            }
            this.string = stringBuffer.toString();
        }
    }

    private static String resolvePath(String str, String str2, boolean z) {
        String str3;
        int lastIndexOf = str.lastIndexOf(47);
        int length = str2.length();
        if (length == 0) {
            str3 = lastIndexOf >= 0 ? str.substring(0, lastIndexOf + 1) : "";
        } else {
            StringBuffer stringBuffer = new StringBuffer(str.length() + length);
            if (lastIndexOf >= 0) {
                stringBuffer.append(str.substring(0, lastIndexOf + 1));
            }
            stringBuffer.append(str2);
            str3 = stringBuffer.toString();
        }
        return normalize(str3, true);
    }

    private static URI resolve(URI uri, URI uri2) {
        String str;
        if (uri2.isOpaque() || uri.isOpaque()) {
            return uri2;
        }
        if (uri2.scheme == null && uri2.authority == null && uri2.path.equals("") && (str = uri2.fragment) != null && uri2.query == null) {
            String str2 = uri.fragment;
            if (str2 != null && str.equals(str2)) {
                return uri;
            }
            URI uri3 = new URI();
            uri3.scheme = uri.scheme;
            uri3.authority = uri.authority;
            uri3.userInfo = uri.userInfo;
            uri3.host = uri.host;
            uri3.port = uri.port;
            uri3.path = uri.path;
            uri3.fragment = uri2.fragment;
            uri3.query = uri.query;
            return uri3;
        } else if (uri2.scheme != null) {
            return uri2;
        } else {
            URI uri4 = new URI();
            uri4.scheme = uri.scheme;
            uri4.query = uri2.query;
            uri4.fragment = uri2.fragment;
            String str3 = uri2.authority;
            if (str3 == null) {
                uri4.authority = uri.authority;
                uri4.host = uri.host;
                uri4.userInfo = uri.userInfo;
                uri4.port = uri.port;
                String str4 = uri2.path;
                if (str4 == null || str4.isEmpty()) {
                    uri4.path = uri.path;
                    String str5 = uri2.query;
                    if (str5 == null) {
                        str5 = uri.query;
                    }
                    uri4.query = str5;
                } else if (uri2.path.length() <= 0 || uri2.path.charAt(0) != '/') {
                    uri4.path = resolvePath(uri.path, uri2.path, uri.isAbsolute());
                } else {
                    uri4.path = normalize(uri2.path, true);
                }
            } else {
                uri4.authority = str3;
                uri4.host = uri2.host;
                uri4.userInfo = uri2.userInfo;
                uri4.host = uri2.host;
                uri4.port = uri2.port;
                uri4.path = uri2.path;
            }
            return uri4;
        }
    }

    private static URI normalize(URI uri) {
        String str;
        String normalize;
        if (uri.isOpaque() || (str = uri.path) == null || str.length() == 0 || (normalize = normalize(uri.path)) == uri.path) {
            return uri;
        }
        URI uri2 = new URI();
        uri2.scheme = uri.scheme;
        uri2.fragment = uri.fragment;
        uri2.authority = uri.authority;
        uri2.userInfo = uri.userInfo;
        uri2.host = uri.host;
        uri2.port = uri.port;
        uri2.path = normalize;
        uri2.query = uri.query;
        return uri2;
    }

    private static URI relativize(URI uri, URI uri2) {
        if (uri2.isOpaque() || uri.isOpaque() || !equalIgnoringCase(uri.scheme, uri2.scheme) || !equal(uri.authority, uri2.authority)) {
            return uri2;
        }
        String normalize = normalize(uri.path);
        String normalize2 = normalize(uri2.path);
        if (!normalize.equals(normalize2)) {
            if (normalize.indexOf(47) != -1) {
                normalize = normalize.substring(0, normalize.lastIndexOf(47) + 1);
            }
            if (!normalize2.startsWith(normalize)) {
                return uri2;
            }
        }
        URI uri3 = new URI();
        uri3.path = normalize2.substring(normalize.length());
        uri3.query = uri2.query;
        uri3.fragment = uri2.fragment;
        return uri3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003e, code lost:
        if (r9.charAt(r3 + 2) != '/') goto L_0x0041;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int needsNormalization(java.lang.String r9) {
        /*
            int r0 = r9.length()
            r1 = 1
            int r0 = r0 - r1
            r2 = 0
            r3 = r2
        L_0x0008:
            r4 = 47
            if (r3 > r0) goto L_0x0016
            char r5 = r9.charAt(r3)
            if (r5 == r4) goto L_0x0013
            goto L_0x0016
        L_0x0013:
            int r3 = r3 + 1
            goto L_0x0008
        L_0x0016:
            if (r3 <= r1) goto L_0x001b
            r1 = r2
            r5 = r1
            goto L_0x001c
        L_0x001b:
            r5 = r2
        L_0x001c:
            if (r3 > r0) goto L_0x005d
            char r6 = r9.charAt(r3)
            r7 = 46
            if (r6 != r7) goto L_0x0041
            if (r3 == r0) goto L_0x0040
            int r6 = r3 + 1
            char r8 = r9.charAt(r6)
            if (r8 == r4) goto L_0x0040
            char r8 = r9.charAt(r6)
            if (r8 != r7) goto L_0x0041
            if (r6 == r0) goto L_0x0040
            int r6 = r3 + 2
            char r6 = r9.charAt(r6)
            if (r6 != r4) goto L_0x0041
        L_0x0040:
            r1 = r2
        L_0x0041:
            int r5 = r5 + 1
        L_0x0043:
            if (r3 > r0) goto L_0x001c
            int r6 = r3 + 1
            char r3 = r9.charAt(r3)
            if (r3 == r4) goto L_0x004f
            r3 = r6
            goto L_0x0043
        L_0x004f:
            r3 = r6
        L_0x0050:
            if (r3 > r0) goto L_0x001c
            char r6 = r9.charAt(r3)
            if (r6 == r4) goto L_0x0059
            goto L_0x001c
        L_0x0059:
            int r3 = r3 + 1
            r1 = r2
            goto L_0x0050
        L_0x005d:
            if (r1 == 0) goto L_0x0060
            r5 = -1
        L_0x0060:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URI.needsNormalization(java.lang.String):int");
    }

    private static void split(char[] cArr, int[] iArr) {
        int i;
        int length = cArr.length - 1;
        int i2 = 0;
        while (i <= length && cArr[i] == '/') {
            cArr[i] = 0;
            i2 = i + 1;
        }
        int i3 = 0;
        while (i <= length) {
            int i4 = i3 + 1;
            iArr[i3] = i;
            i++;
            while (true) {
                if (i > length) {
                    break;
                }
                int i5 = i + 1;
                if (cArr[i] == '/') {
                    cArr[i5 - 1] = 0;
                    while (true) {
                        i = i5;
                        if (i > length || cArr[i] != '/') {
                            break;
                        }
                        i5 = i + 1;
                        cArr[i] = 0;
                    }
                } else {
                    i = i5;
                }
            }
            i3 = i4;
        }
        if (i3 != iArr.length) {
            throw new InternalError();
        }
    }

    private static int join(char[] cArr, int[] iArr) {
        int i;
        int i2 = 1;
        int length = cArr.length - 1;
        if (cArr[0] == 0) {
            cArr[0] = '/';
        } else {
            i2 = 0;
        }
        for (int i3 : iArr) {
            if (i3 != -1) {
                if (i2 == i3) {
                    while (i2 <= length && cArr[i2] != 0) {
                        i2++;
                    }
                    if (i2 <= length) {
                        i = i2 + 1;
                        cArr[i2] = '/';
                    }
                } else if (i2 < i3) {
                    while (i3 <= length) {
                        char c = cArr[i3];
                        if (c == 0) {
                            break;
                        }
                        i3++;
                        cArr[i2] = c;
                        i2++;
                    }
                    if (i3 <= length) {
                        i = i2 + 1;
                        cArr[i2] = '/';
                    }
                } else {
                    throw new InternalError();
                }
                i2 = i;
            }
        }
        return i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0012, code lost:
        r5 = 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void removeDots(char[] r10, int[] r11, boolean r12) {
        /*
            int r0 = r11.length
            int r1 = r10.length
            r2 = 1
            int r1 = r1 - r2
            r3 = 0
            r4 = r3
        L_0x0006:
            if (r4 >= r0) goto L_0x0063
        L_0x0008:
            r5 = r11[r4]
            char r6 = r10[r5]
            r7 = 46
            if (r6 != r7) goto L_0x0027
            if (r5 != r1) goto L_0x0014
        L_0x0012:
            r5 = r2
            goto L_0x002c
        L_0x0014:
            int r6 = r5 + 1
            char r8 = r10[r6]
            if (r8 != 0) goto L_0x001b
            goto L_0x0012
        L_0x001b:
            if (r8 != r7) goto L_0x0027
            if (r6 == r1) goto L_0x0025
            int r5 = r5 + 2
            char r5 = r10[r5]
            if (r5 != 0) goto L_0x0027
        L_0x0025:
            r5 = 2
            goto L_0x002c
        L_0x0027:
            int r4 = r4 + 1
            if (r4 < r0) goto L_0x0008
            r5 = r3
        L_0x002c:
            if (r4 > r0) goto L_0x0063
            if (r5 != 0) goto L_0x0031
            goto L_0x0063
        L_0x0031:
            r6 = -1
            if (r5 != r2) goto L_0x0037
            r11[r4] = r6
            goto L_0x0060
        L_0x0037:
            int r5 = r4 + -1
        L_0x0039:
            if (r5 < 0) goto L_0x0043
            r8 = r11[r5]
            if (r8 == r6) goto L_0x0040
            goto L_0x0043
        L_0x0040:
            int r5 = r5 + -1
            goto L_0x0039
        L_0x0043:
            if (r5 < 0) goto L_0x005c
            r8 = r11[r5]
            char r9 = r10[r8]
            if (r9 != r7) goto L_0x0057
            int r9 = r8 + 1
            char r9 = r10[r9]
            if (r9 != r7) goto L_0x0057
            int r8 = r8 + 2
            char r7 = r10[r8]
            if (r7 == 0) goto L_0x0060
        L_0x0057:
            r11[r4] = r6
            r11[r5] = r6
            goto L_0x0060
        L_0x005c:
            if (r12 == 0) goto L_0x0060
            r11[r4] = r6
        L_0x0060:
            int r4 = r4 + 1
            goto L_0x0006
        L_0x0063:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URI.removeDots(char[], int[], boolean):void");
    }

    private static void maybeAddLeadingDot(char[] cArr, int[] iArr) {
        if (cArr[0] != 0) {
            int length = iArr.length;
            int i = 0;
            while (i < length && iArr[i] < 0) {
                i++;
            }
            if (i < length && i != 0) {
                int i2 = iArr[i];
                while (i2 < cArr.length && (r2 = cArr[i2]) != ':' && r2 != 0) {
                    i2++;
                }
                if (i2 < cArr.length && cArr[i2] != 0) {
                    cArr[0] = '.';
                    cArr[1] = 0;
                    iArr[0] = 0;
                }
            }
        }
    }

    private static String normalize(String str) {
        return normalize(str, false);
    }

    private static String normalize(String str, boolean z) {
        int needsNormalization = needsNormalization(str);
        if (needsNormalization < 0) {
            return str;
        }
        char[] charArray = str.toCharArray();
        int[] iArr = new int[needsNormalization];
        split(charArray, iArr);
        removeDots(charArray, iArr, z);
        maybeAddLeadingDot(charArray, iArr);
        String str2 = new String(charArray, 0, join(charArray, iArr));
        return str2.equals(str) ? str : str2;
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

    private static long lowMask(char c, char c2) {
        long j = 0;
        for (int max = Math.max(Math.min((int) c, 63), 0); max <= Math.max(Math.min((int) c2, 63), 0); max++) {
            j |= 1 << max;
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

    static {
        long lowMask = lowMask('0', '9');
        L_DIGIT = lowMask;
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
        L_HEX = lowMask;
        long lowMask2 = lowMask("-_.!~*'()");
        L_MARK = lowMask2;
        long highMask3 = highMask("-_.!~*'()");
        H_MARK = highMask3;
        long j4 = lowMask2 | j2;
        L_UNRESERVED = j4;
        long j5 = highMask3 | j3;
        H_UNRESERVED = j5;
        long lowMask3 = lowMask(";/?:@&=+$,[]");
        L_RESERVED = lowMask3;
        long highMask4 = highMask(";/?:@&=+$,[]");
        H_RESERVED = highMask4;
        L_URIC = lowMask3 | j4 | 1;
        H_URIC = highMask4 | j5 | 0;
        long lowMask4 = j4 | 1 | lowMask(":@&=+$,");
        L_PCHAR = lowMask4;
        long highMask5 = j5 | 0 | highMask(":@&=+$,");
        H_PCHAR = highMask5;
        L_PATH = lowMask4 | lowMask(";/");
        H_PATH = highMask5 | highMask(";/");
        long lowMask5 = lowMask(LanguageTag.SEP);
        L_DASH = lowMask5;
        long highMask6 = highMask(LanguageTag.SEP);
        H_DASH = highMask6;
        long lowMask6 = j4 | 1 | lowMask(";:&=+$,");
        L_USERINFO = lowMask6;
        long highMask7 = j5 | 0 | highMask(";:&=+$,");
        H_USERINFO = highMask7;
        L_REG_NAME = j4 | 1 | lowMask("$,;:@&=+");
        H_REG_NAME = j5 | 0 | highMask("$,;:@&=+");
        long lowMask7 = lowMask6 | j2 | lowMask5 | lowMask(".:@[]");
        L_SERVER = lowMask7;
        long highMask8 = highMask7 | j3 | highMask6 | highMask(".:@[]");
        H_SERVER = highMask8;
        L_SERVER_PERCENT = lowMask7 | lowMask("%");
        H_SERVER_PERCENT = highMask("%") | highMask8;
        L_SCHEME = lowMask | 0 | lowMask("+-.");
        H_SCHEME = j | 0 | highMask("+-.");
        L_URIC_NO_SLASH = j4 | 1 | lowMask(";?:@&=+$,");
        H_URIC_NO_SLASH = j5 | 0 | highMask(";?:@&=+$,");
    }

    private static void appendEscape(StringBuffer stringBuffer, byte b) {
        stringBuffer.append('%');
        char[] cArr = hexDigits;
        stringBuffer.append(cArr[(b >> 4) & 15]);
        stringBuffer.append(cArr[(b >> 0) & 15]);
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

    private static String quote(String str, long j, long j2) {
        str.length();
        boolean z = (1 & j) != 0;
        StringBuffer stringBuffer = null;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt < 128) {
                if (!match(charAt, j, j2)) {
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

    private static String encode(String str) {
        ByteBuffer byteBuffer;
        int length = str.length();
        if (length == 0) {
            return str;
        }
        int i = 0;
        while (str.charAt(i) < 128) {
            i++;
            if (i >= length) {
                return str;
            }
        }
        try {
            byteBuffer = ThreadLocalCoders.encoderFor("UTF-8").encode(CharBuffer.wrap((CharSequence) Normalizer.normalize(str, Normalizer.Form.NFC)));
        } catch (CharacterCodingException unused) {
            byteBuffer = null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get() & 255;
            if (b >= 128) {
                appendEscape(stringBuffer, (byte) b);
            } else {
                stringBuffer.append((char) b);
            }
        }
        return stringBuffer.toString();
    }

    private static byte decode(char c, char c2) {
        return (byte) (((decode(c) & 15) << 4) | ((decode(c2) & 15) << 0));
    }

    private static String decode(String str) {
        int length;
        if (str == null || (length = str.length()) == 0 || str.indexOf(37) < 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(length);
        ByteBuffer allocate = ByteBuffer.allocate(length);
        CharBuffer allocate2 = CharBuffer.allocate(length);
        CharsetDecoder onUnmappableCharacter = ThreadLocalCoders.decoderFor("UTF-8").onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        char charAt = str.charAt(0);
        int i = 0;
        boolean z = false;
        while (i < length) {
            if (charAt == '[') {
                z = true;
            } else if (z && charAt == ']') {
                z = false;
            }
            if (charAt != '%' || z) {
                stringBuffer.append(charAt);
                i++;
                if (i >= length) {
                    break;
                }
                charAt = str.charAt(i);
            } else {
                allocate.clear();
                do {
                    int i2 = i + 1;
                    char charAt2 = str.charAt(i2);
                    int i3 = i2 + 1;
                    allocate.put(decode(charAt2, str.charAt(i3)));
                    i = i3 + 1;
                    if (i >= length || (charAt = str.charAt(i)) != '%') {
                        allocate.flip();
                        allocate2.clear();
                        onUnmappableCharacter.reset();
                        onUnmappableCharacter.decode(allocate, allocate2, true);
                        onUnmappableCharacter.flush(allocate2);
                        stringBuffer.append(allocate2.flip().toString());
                    }
                    int i22 = i + 1;
                    char charAt22 = str.charAt(i22);
                    int i32 = i22 + 1;
                    allocate.put(decode(charAt22, str.charAt(i32)));
                    i = i32 + 1;
                    break;
                } while ((charAt = str.charAt(i)) != '%');
                allocate.flip();
                allocate2.clear();
                onUnmappableCharacter.reset();
                onUnmappableCharacter.decode(allocate, allocate2, true);
                onUnmappableCharacter.flush(allocate2);
                stringBuffer.append(allocate2.flip().toString());
            }
        }
        return stringBuffer.toString();
    }

    private class Parser {
        private String input;
        private int ipv6byteCount = 0;
        private boolean requireServerAuthority = false;

        Parser(String str) {
            this.input = str;
            URI.this.string = str;
        }

        private void fail(String str) throws URISyntaxException {
            throw new URISyntaxException(this.input, str);
        }

        private void fail(String str, int i) throws URISyntaxException {
            throw new URISyntaxException(this.input, str, i);
        }

        private void failExpecting(String str, int i) throws URISyntaxException {
            fail("Expected " + str, i);
        }

        private void failExpecting(String str, String str2, int i) throws URISyntaxException {
            fail("Expected " + str + " following " + str2, i);
        }

        private String substring(int i, int i2) {
            return this.input.substring(i, i2);
        }

        private char charAt(int i) {
            return this.input.charAt(i);
        }

        /* renamed from: at */
        private boolean m1702at(int i, int i2, char c) {
            return i < i2 && charAt(i) == c;
        }

        /* renamed from: at */
        private boolean m1703at(int i, int i2, String str) {
            int length = str.length();
            if (length > i2 - i) {
                return false;
            }
            int i3 = 0;
            while (i3 < length) {
                int i4 = i + 1;
                if (charAt(i) != str.charAt(i3)) {
                    break;
                }
                i3++;
                i = i4;
            }
            if (i3 == length) {
                return true;
            }
            return false;
        }

        private int scan(int i, int i2, char c) {
            return (i >= i2 || charAt(i) != c) ? i : i + 1;
        }

        private int scan(int i, int i2, String str, String str2) {
            while (i < i2) {
                char charAt = charAt(i);
                if (str.indexOf((int) charAt) >= 0) {
                    return -1;
                }
                if (str2.indexOf((int) charAt) >= 0) {
                    break;
                }
                i++;
            }
            return i;
        }

        private int scanEscape(int i, int i2, char c) throws URISyntaxException {
            if (c != '%') {
                return (c <= 128 || Character.isSpaceChar(c) || Character.isISOControl(c)) ? i : i + 1;
            }
            int i3 = i + 3;
            if (i3 <= i2 && URI.match(charAt(i + 1), URI.L_HEX, URI.H_HEX) && URI.match(charAt(i + 2), URI.L_HEX, URI.H_HEX)) {
                return i3;
            }
            fail("Malformed escape pair", i);
            return i;
        }

        private int scan(int i, int i2, long j, long j2) throws URISyntaxException {
            int scanEscape;
            while (i < i2) {
                char charAt = charAt(i);
                if (!URI.match(charAt, j, j2)) {
                    if ((1 & j) == 0 || (scanEscape = scanEscape(i, i2, charAt)) <= i) {
                        break;
                    }
                    i = scanEscape;
                } else {
                    i++;
                }
            }
            return i;
        }

        private void checkChars(int i, int i2, long j, long j2, String str) throws URISyntaxException {
            int scan = scan(i, i2, j, j2);
            if (scan < i2) {
                fail("Illegal character in " + str, scan);
            }
        }

        private void checkChar(int i, long j, long j2, String str) throws URISyntaxException {
            checkChars(i, i + 1, j, j2, str);
        }

        /* access modifiers changed from: package-private */
        public void parse(boolean z) throws URISyntaxException {
            int i;
            this.requireServerAuthority = z;
            int length = this.input.length();
            int i2 = 0;
            int scan = scan(0, length, "/?#", ":");
            if (scan < 0 || !m1702at(scan, length, (char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR)) {
                i = parseHierarchical(0, length);
            } else {
                if (scan == 0) {
                    failExpecting("scheme name", 0);
                }
                checkChar(0, 0, URI.H_ALPHA, "scheme name");
                checkChars(1, scan, URI.L_SCHEME, URI.H_SCHEME, "scheme name");
                URI.this.scheme = substring(0, scan);
                i2 = scan + 1;
                if (m1702at(i2, length, '/')) {
                    i = parseHierarchical(i2, length);
                } else {
                    i = scan(i2, length, "", "#");
                    if (i <= i2) {
                        failExpecting("scheme-specific part", i2);
                    }
                    checkChars(i2, i, URI.L_URIC, URI.H_URIC, "opaque part");
                }
            }
            URI.this.schemeSpecificPart = substring(i2, i);
            if (m1702at(i, length, '#')) {
                int i3 = i + 1;
                checkChars(i3, length, URI.L_URIC, URI.H_URIC, "fragment");
                URI.this.fragment = substring(i3, length);
                i = length;
            }
            if (i < length) {
                fail("end of URI", i);
            }
        }

        private int parseHierarchical(int i, int i2) throws URISyntaxException {
            if (m1702at(i, i2, '/') && m1702at(i + 1, i2, '/')) {
                i += 2;
                int scan = scan(i, i2, "", "/?#");
                if (scan > i) {
                    i = parseAuthority(i, scan);
                } else if (scan >= i2) {
                    failExpecting("authority", i);
                }
            }
            int scan2 = scan(i, i2, "", "?#");
            checkChars(i, scan2, URI.L_PATH, URI.H_PATH, "path");
            URI.this.path = substring(i, scan2);
            if (!m1702at(scan2, i2, '?')) {
                return scan2;
            }
            int i3 = scan2 + 1;
            int scan3 = scan(i3, i2, "", "#");
            checkChars(i3, scan3, URI.L_URIC, URI.H_URIC, "query");
            URI.this.query = substring(i3, scan3);
            return scan3;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:3:0x001b, code lost:
            if (scan(r11, r12, java.net.URI.m3714$$Nest$sfgetL_SERVER_PERCENT(), java.net.URI.m3701$$Nest$sfgetH_SERVER_PERCENT()) == r12) goto L_0x001d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x001f, code lost:
            r0 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x0030, code lost:
            if (scan(r11, r12, java.net.URI.m3713$$Nest$sfgetL_SERVER(), java.net.URI.m3700$$Nest$sfgetH_SERVER()) == r12) goto L_0x001d;
         */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x0087  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int parseAuthority(int r11, int r12) throws java.net.URISyntaxException {
            /*
                r10 = this;
                java.lang.String r0 = ""
                java.lang.String r1 = "]"
                int r0 = r10.scan((int) r11, (int) r12, (java.lang.String) r0, (java.lang.String) r1)
                r1 = 1
                r2 = 0
                if (r0 <= r11) goto L_0x0021
                long r6 = java.net.URI.L_SERVER_PERCENT
                long r8 = java.net.URI.H_SERVER_PERCENT
                r3 = r10
                r4 = r11
                r5 = r12
                int r0 = r3.scan((int) r4, (int) r5, (long) r6, (long) r8)
                if (r0 != r12) goto L_0x001f
            L_0x001d:
                r0 = r1
                goto L_0x0033
            L_0x001f:
                r0 = r2
                goto L_0x0033
            L_0x0021:
                long r6 = java.net.URI.L_SERVER
                long r8 = java.net.URI.H_SERVER
                r3 = r10
                r4 = r11
                r5 = r12
                int r0 = r3.scan((int) r4, (int) r5, (long) r6, (long) r8)
                if (r0 != r12) goto L_0x001f
                goto L_0x001d
            L_0x0033:
                long r6 = java.net.URI.L_REG_NAME
                long r8 = java.net.URI.H_REG_NAME
                r3 = r10
                r4 = r11
                r5 = r12
                int r3 = r3.scan((int) r4, (int) r5, (long) r6, (long) r8)
                if (r3 != r12) goto L_0x0045
                goto L_0x0046
            L_0x0045:
                r1 = r2
            L_0x0046:
                if (r1 == 0) goto L_0x0054
                if (r0 != 0) goto L_0x0054
                java.net.URI r0 = java.net.URI.this
                java.lang.String r10 = r10.substring(r11, r12)
                r0.authority = r10
                return r12
            L_0x0054:
                r2 = 0
                if (r0 == 0) goto L_0x0084
                int r0 = r10.parseServer(r11, r12)     // Catch:{ URISyntaxException -> 0x006c }
                if (r0 >= r12) goto L_0x0062
                java.lang.String r3 = "end of authority"
                r10.failExpecting(r3, r0)     // Catch:{ URISyntaxException -> 0x006c }
            L_0x0062:
                java.net.URI r3 = java.net.URI.this     // Catch:{ URISyntaxException -> 0x006c }
                java.lang.String r4 = r10.substring(r11, r12)     // Catch:{ URISyntaxException -> 0x006c }
                r3.authority = r4     // Catch:{ URISyntaxException -> 0x006c }
                goto L_0x0085
            L_0x006c:
                r0 = move-exception
                java.net.URI r3 = java.net.URI.this
                r3.userInfo = r2
                java.net.URI r3 = java.net.URI.this
                r3.host = r2
                java.net.URI r2 = java.net.URI.this
                r3 = -1
                r2.port = r3
                boolean r2 = r10.requireServerAuthority
                if (r2 != 0) goto L_0x0083
                r2 = r0
                goto L_0x0084
            L_0x0083:
                throw r0
            L_0x0084:
                r0 = r11
            L_0x0085:
                if (r0 >= r12) goto L_0x009c
                if (r1 == 0) goto L_0x0093
                java.net.URI r0 = java.net.URI.this
                java.lang.String r10 = r10.substring(r11, r12)
                r0.authority = r10
                goto L_0x009c
            L_0x0093:
                if (r2 != 0) goto L_0x009b
                java.lang.String r11 = "Illegal character in authority"
                r10.fail(r11, r0)
                goto L_0x009c
            L_0x009b:
                throw r2
            L_0x009c:
                return r12
            */
            throw new UnsupportedOperationException("Method not decompiled: java.net.URI.Parser.parseAuthority(int, int):int");
        }

        /* JADX WARNING: Removed duplicated region for block: B:35:0x00c6  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int parseServer(int r12, int r13) throws java.net.URISyntaxException {
            /*
                r11 = this;
                java.lang.String r0 = "@"
                java.lang.String r1 = "/?#"
                int r0 = r11.scan((int) r12, (int) r13, (java.lang.String) r1, (java.lang.String) r0)
                if (r0 < r12) goto L_0x002e
                r2 = 64
                boolean r2 = r11.m1702at((int) r0, (int) r13, (char) r2)
                if (r2 == 0) goto L_0x002e
                long r5 = java.net.URI.L_USERINFO
                long r7 = java.net.URI.H_USERINFO
                java.lang.String r9 = "user info"
                r2 = r11
                r3 = r12
                r4 = r0
                r2.checkChars(r3, r4, r5, r7, r9)
                java.net.URI r2 = java.net.URI.this
                java.lang.String r12 = r11.substring(r12, r0)
                r2.userInfo = r12
                int r12 = r0 + 1
            L_0x002e:
                r0 = 91
                boolean r0 = r11.m1702at((int) r12, (int) r13, (char) r0)
                java.lang.String r2 = ""
                if (r0 == 0) goto L_0x0086
                int r12 = r12 + 1
                java.lang.String r0 = "]"
                int r0 = r11.scan((int) r12, (int) r13, (java.lang.String) r1, (java.lang.String) r0)
                if (r0 <= r12) goto L_0x0080
                r1 = 93
                boolean r1 = r11.m1702at((int) r0, (int) r13, (char) r1)
                if (r1 == 0) goto L_0x0080
                java.lang.String r1 = "%"
                int r1 = r11.scan((int) r12, (int) r0, (java.lang.String) r2, (java.lang.String) r1)
                if (r1 <= r12) goto L_0x006e
                r11.parseIPv6Reference(r12, r1)
                int r4 = r1 + 1
                if (r4 != r0) goto L_0x005e
                java.lang.String r1 = "scope id expected"
                r11.fail(r1)
            L_0x005e:
                long r6 = java.net.URI.L_ALPHANUM
                long r8 = java.net.URI.H_ALPHANUM
                java.lang.String r10 = "scope id"
                r3 = r11
                r5 = r0
                r3.checkChars(r4, r5, r6, r8, r10)
                goto L_0x0071
            L_0x006e:
                r11.parseIPv6Reference(r12, r0)
            L_0x0071:
                java.net.URI r1 = java.net.URI.this
                int r12 = r12 + -1
                int r0 = r0 + 1
                java.lang.String r12 = r11.substring(r12, r0)
                r1.host = r12
            L_0x007e:
                r12 = r0
                goto L_0x0090
            L_0x0080:
                java.lang.String r1 = "closing bracket for IPv6 address"
                r11.failExpecting(r1, r0)
                goto L_0x0090
            L_0x0086:
                int r0 = r11.parseIPv4Address(r12, r13)
                if (r0 > r12) goto L_0x007e
                int r12 = r11.parseHostname(r12, r13)
            L_0x0090:
                r0 = 58
                boolean r0 = r11.m1702at((int) r12, (int) r13, (char) r0)
                if (r0 == 0) goto L_0x00c4
                int r12 = r12 + 1
                java.lang.String r0 = "/"
                int r0 = r11.scan((int) r12, (int) r13, (java.lang.String) r2, (java.lang.String) r0)
                if (r0 <= r12) goto L_0x00c4
                long r6 = java.net.URI.L_DIGIT
                r8 = 0
                java.lang.String r10 = "port number"
                r3 = r11
                r4 = r12
                r5 = r0
                r3.checkChars(r4, r5, r6, r8, r10)
                java.net.URI r1 = java.net.URI.this     // Catch:{ NumberFormatException -> 0x00be }
                java.lang.String r2 = r11.substring(r12, r0)     // Catch:{ NumberFormatException -> 0x00be }
                int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ NumberFormatException -> 0x00be }
                r1.port = r2     // Catch:{ NumberFormatException -> 0x00be }
                goto L_0x00c3
            L_0x00be:
                java.lang.String r1 = "Malformed port number"
                r11.fail(r1, r12)
            L_0x00c3:
                r12 = r0
            L_0x00c4:
                if (r12 >= r13) goto L_0x00cb
                java.lang.String r13 = "port number"
                r11.failExpecting(r13, r12)
            L_0x00cb:
                return r12
            */
            throw new UnsupportedOperationException("Method not decompiled: java.net.URI.Parser.parseServer(int, int):int");
        }

        private int scanByte(int i, int i2) throws URISyntaxException {
            int scan = scan(i, i2, URI.L_DIGIT, 0);
            return (scan > i && Integer.parseInt(substring(i, scan)) > 255) ? i : scan;
        }

        private int scanIPv4Address(int i, int i2, boolean z) throws URISyntaxException {
            int scan = scan(i, i2, URI.L_DIGIT | URI.L_DOT, URI.H_DOT | 0);
            if (scan <= i || (z && scan != i2)) {
                return -1;
            }
            int scanByte = scanByte(i, scan);
            if (scanByte > i) {
                int scan2 = scan(scanByte, scan, '.');
                if (scan2 > scanByte) {
                    scanByte = scanByte(scan2, scan);
                    if (scanByte > scan2) {
                        scan2 = scan(scanByte, scan, '.');
                        if (scan2 > scanByte) {
                            scanByte = scanByte(scan2, scan);
                            if (scanByte > scan2) {
                                int scan3 = scan(scanByte, scan, '.');
                                if (scan3 <= scanByte) {
                                    scanByte = scan3;
                                } else {
                                    scanByte = scanByte(scan3, scan);
                                    if (scanByte > scan3 && scanByte >= scan) {
                                        return scanByte;
                                    }
                                }
                            }
                        }
                    }
                }
                scanByte = scan2;
            }
            fail("Malformed IPv4 address", scanByte);
            return -1;
        }

        private int takeIPv4Address(int i, int i2, String str) throws URISyntaxException {
            int scanIPv4Address = scanIPv4Address(i, i2, true);
            if (scanIPv4Address <= i) {
                failExpecting(str, i);
            }
            return scanIPv4Address;
        }

        private int parseIPv4Address(int i, int i2) {
            int i3 = -1;
            try {
                int scanIPv4Address = scanIPv4Address(i, i2, false);
                if (scanIPv4Address <= i || scanIPv4Address >= i2 || charAt(scanIPv4Address) == ':') {
                    i3 = scanIPv4Address;
                }
                if (i3 > i) {
                    URI.this.host = substring(i, i3);
                }
            } catch (NumberFormatException | URISyntaxException unused) {
            }
            return i3;
        }

        private int parseHostname(int i, int i2) throws URISyntaxException {
            int i3 = i;
            int i4 = i2;
            int i5 = -1;
            int i6 = i3;
            while (true) {
                int scan = scan(i6, i2, URI.L_ALPHANUM, URI.H_ALPHANUM);
                if (scan <= i6) {
                    break;
                }
                if (scan > i6) {
                    int scan2 = scan(scan, i2, URI.L_ALPHANUM | URI.L_DASH | URI.L_UNDERSCORE, URI.H_UNDERSCORE | URI.H_ALPHANUM | URI.H_DASH);
                    if (scan2 > scan) {
                        int i7 = scan2 - 1;
                        if (charAt(i7) == '-') {
                            fail("Illegal character in hostname", i7);
                        }
                        scan = scan2;
                    }
                } else {
                    scan = i6;
                }
                int scan3 = scan(scan, i4, '.');
                if (scan3 > scan) {
                    i5 = i6;
                    i6 = scan3;
                    if (scan3 >= i4) {
                        break;
                    }
                } else {
                    i5 = i6;
                    i6 = scan;
                    break;
                }
            }
            if (i6 < i4 && !m1702at(i6, i4, (char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR)) {
                fail("Illegal character in hostname", i6);
            }
            if (i5 < 0) {
                failExpecting(ConnectivitySettingsUtils.PRIVATE_DNS_MODE_PROVIDER_HOSTNAME_STRING, i);
            }
            if (i5 > i3 && !URI.match(charAt(i5), 0, URI.H_ALPHA)) {
                fail("Illegal character in hostname", i5);
            }
            URI.this.host = substring(i, i6);
            return i6;
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x0041  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x004a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int parseIPv6Reference(int r5, int r6) throws java.net.URISyntaxException {
            /*
                r4 = this;
                int r0 = r4.scanHexSeq(r5, r6)
                java.lang.String r1 = "::"
                r2 = 1
                r3 = 0
                if (r0 <= r5) goto L_0x002e
                boolean r1 = r4.m1703at((int) r0, (int) r6, (java.lang.String) r1)
                if (r1 == 0) goto L_0x0017
                int r0 = r0 + 2
                int r0 = r4.scanHexPost(r0, r6)
                goto L_0x003d
            L_0x0017:
                r1 = 58
                boolean r1 = r4.m1702at((int) r0, (int) r6, (char) r1)
                if (r1 == 0) goto L_0x003c
                int r0 = r0 + 1
                java.lang.String r1 = "IPv4 address"
                int r0 = r4.takeIPv4Address(r0, r6, r1)
                int r1 = r4.ipv6byteCount
                int r1 = r1 + 4
                r4.ipv6byteCount = r1
                goto L_0x003c
            L_0x002e:
                boolean r0 = r4.m1703at((int) r5, (int) r6, (java.lang.String) r1)
                if (r0 == 0) goto L_0x003b
                int r0 = r5 + 2
                int r0 = r4.scanHexPost(r0, r6)
                goto L_0x003d
            L_0x003b:
                r0 = r5
            L_0x003c:
                r2 = r3
            L_0x003d:
                java.lang.String r1 = "Malformed IPv6 address"
                if (r0 >= r6) goto L_0x0044
                r4.fail(r1, r5)
            L_0x0044:
                int r6 = r4.ipv6byteCount
                r3 = 16
                if (r6 <= r3) goto L_0x004f
                java.lang.String r6 = "IPv6 address too long"
                r4.fail(r6, r5)
            L_0x004f:
                if (r2 != 0) goto L_0x005a
                int r6 = r4.ipv6byteCount
                if (r6 >= r3) goto L_0x005a
                java.lang.String r6 = "IPv6 address too short"
                r4.fail(r6, r5)
            L_0x005a:
                if (r2 == 0) goto L_0x0063
                int r6 = r4.ipv6byteCount
                if (r6 != r3) goto L_0x0063
                r4.fail(r1, r5)
            L_0x0063:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.net.URI.Parser.parseIPv6Reference(int, int):int");
        }

        private int scanHexPost(int i, int i2) throws URISyntaxException {
            if (i == i2) {
                return i;
            }
            int scanHexSeq = scanHexSeq(i, i2);
            if (scanHexSeq <= i) {
                int takeIPv4Address = takeIPv4Address(i, i2, "hex digits or IPv4 address");
                this.ipv6byteCount += 4;
                return takeIPv4Address;
            } else if (!m1702at(scanHexSeq, i2, (char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR)) {
                return scanHexSeq;
            } else {
                int takeIPv4Address2 = takeIPv4Address(scanHexSeq + 1, i2, "hex digits or IPv4 address");
                this.ipv6byteCount += 4;
                return takeIPv4Address2;
            }
        }

        private int scanHexSeq(int i, int i2) throws URISyntaxException {
            int scan = scan(i, i2, URI.L_HEX, URI.H_HEX);
            if (scan <= i || m1702at(scan, i2, '.')) {
                return -1;
            }
            if (scan > i + 4) {
                fail("IPv6 hexadecimal digit sequence too long", i);
            }
            this.ipv6byteCount += 2;
            while (scan < i2 && m1702at(scan, i2, (char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR)) {
                int i3 = scan + 1;
                if (m1702at(i3, i2, (char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR)) {
                    return scan;
                }
                scan = scan(i3, i2, URI.L_HEX, URI.H_HEX);
                if (scan <= i3) {
                    failExpecting("digits for an IPv6 address", i3);
                }
                if (m1702at(scan, i2, '.')) {
                    return i3 - 1;
                }
                if (scan > i3 + 4) {
                    fail("IPv6 hexadecimal digit sequence too long", i3);
                }
                this.ipv6byteCount += 2;
            }
            return scan;
        }
    }
}
