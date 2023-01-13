package java.net;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import kotlin.text.Typography;
import libcore.net.http.HttpDate;

public final class HttpCookie implements Cloneable {
    static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final long MAX_AGE_UNSPECIFIED = -1;
    private static final Set<String> RESERVED_NAMES;
    private static final String SET_COOKIE = "set-cookie:";
    private static final String SET_COOKIE2 = "set-cookie2:";
    static final Map<String, CookieAttributeAssignor> assignors;
    private static final String tspecials = ",;= \t";
    private String comment;
    private String commentURL;
    private String domain;
    private final String header;
    private boolean httpOnly;
    private long maxAge;
    private final String name;
    private String path;
    private String portlist;
    private boolean secure;
    private boolean toDiscard;
    private String value;
    private int version;
    /* access modifiers changed from: private */
    public final long whenCreated;

    interface CookieAttributeAssignor {
        void assign(HttpCookie httpCookie, String str, String str2);
    }

    static {
        HashSet hashSet = new HashSet();
        RESERVED_NAMES = hashSet;
        hashSet.add("comment");
        hashSet.add("commenturl");
        hashSet.add("discard");
        hashSet.add("domain");
        hashSet.add("expires");
        hashSet.add("httponly");
        hashSet.add("max-age");
        hashSet.add("path");
        hashSet.add("port");
        hashSet.add("secure");
        hashSet.add("version");
        HashMap hashMap = new HashMap();
        assignors = hashMap;
        hashMap.put("comment", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getComment() == null) {
                    httpCookie.setComment(str2);
                }
            }
        });
        hashMap.put("commenturl", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getCommentURL() == null) {
                    httpCookie.setCommentURL(str2);
                }
            }
        });
        hashMap.put("discard", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                httpCookie.setDiscard(true);
            }
        });
        hashMap.put("domain", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getDomain() == null) {
                    httpCookie.setDomain(str2);
                }
            }
        });
        hashMap.put("max-age", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                try {
                    long parseLong = Long.parseLong(str2);
                    if (httpCookie.getMaxAge() == -1) {
                        httpCookie.setMaxAge(parseLong);
                    }
                } catch (NumberFormatException unused) {
                    throw new IllegalArgumentException("Illegal cookie max-age attribute");
                }
            }
        });
        hashMap.put("path", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getPath() == null) {
                    httpCookie.setPath(str2);
                }
            }
        });
        hashMap.put("port", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getPortlist() == null) {
                    if (str2 == null) {
                        str2 = "";
                    }
                    httpCookie.setPortlist(str2);
                }
            }
        });
        hashMap.put("secure", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                httpCookie.setSecure(true);
            }
        });
        hashMap.put("httponly", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                httpCookie.setHttpOnly(true);
            }
        });
        hashMap.put("version", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                try {
                    httpCookie.setVersion(Integer.parseInt(str2));
                } catch (NumberFormatException unused) {
                }
            }
        });
        hashMap.put("expires", new CookieAttributeAssignor() {
            public void assign(HttpCookie httpCookie, String str, String str2) {
                if (httpCookie.getMaxAge() == -1) {
                    Date parse = HttpDate.parse(str2);
                    long j = 0;
                    if (parse != null) {
                        long time = (parse.getTime() - httpCookie.whenCreated) / 1000;
                        if (time != -1) {
                            j = time;
                        }
                    }
                    httpCookie.setMaxAge(j);
                }
            }
        });
    }

    public HttpCookie(String str, String str2) {
        this(str, str2, (String) null);
    }

    private HttpCookie(String str, String str2, String str3) {
        this.maxAge = -1;
        this.version = 1;
        String trim = str.trim();
        if (trim.length() == 0 || !isToken(trim) || trim.charAt(0) == '$') {
            throw new IllegalArgumentException("Illegal cookie name");
        }
        this.name = trim;
        this.value = str2;
        this.toDiscard = false;
        this.secure = false;
        this.whenCreated = System.currentTimeMillis();
        this.portlist = null;
        this.header = str3;
    }

    public static List<HttpCookie> parse(String str) {
        return parse(str, false);
    }

    private static List<HttpCookie> parse(String str, boolean z) {
        int guessCookieVersion = guessCookieVersion(str);
        if (startsWithIgnoreCase(str, SET_COOKIE2)) {
            str = str.substring(12);
        } else if (startsWithIgnoreCase(str, SET_COOKIE)) {
            str = str.substring(11);
        }
        ArrayList arrayList = new ArrayList();
        if (guessCookieVersion == 0) {
            HttpCookie parseInternal = parseInternal(str, z);
            parseInternal.setVersion(0);
            arrayList.add(parseInternal);
        } else {
            for (String parseInternal2 : splitMultiCookies(str)) {
                HttpCookie parseInternal3 = parseInternal(parseInternal2, z);
                parseInternal3.setVersion(1);
                arrayList.add(parseInternal3);
            }
        }
        return arrayList;
    }

    public boolean hasExpired() {
        long j = this.maxAge;
        if (j == 0) {
            return true;
        }
        return j != -1 && (System.currentTimeMillis() - this.whenCreated) / 1000 > this.maxAge;
    }

    public void setComment(String str) {
        this.comment = str;
    }

    public String getComment() {
        return this.comment;
    }

    public void setCommentURL(String str) {
        this.commentURL = str;
    }

    public String getCommentURL() {
        return this.commentURL;
    }

    public void setDiscard(boolean z) {
        this.toDiscard = z;
    }

    public boolean getDiscard() {
        return this.toDiscard;
    }

    public void setPortlist(String str) {
        this.portlist = str;
    }

    public String getPortlist() {
        return this.portlist;
    }

    public void setDomain(String str) {
        if (str != null) {
            this.domain = str.toLowerCase();
        } else {
            this.domain = str;
        }
    }

    public String getDomain() {
        return this.domain;
    }

    public void setMaxAge(long j) {
        this.maxAge = j;
    }

    public long getMaxAge() {
        return this.maxAge;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setSecure(boolean z) {
        this.secure = z;
    }

    public boolean getSecure() {
        return this.secure;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i) {
        if (i == 0 || i == 1) {
            this.version = i;
            return;
        }
        throw new IllegalArgumentException("cookie version should be 0 or 1");
    }

    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    public void setHttpOnly(boolean z) {
        this.httpOnly = z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0042, code lost:
        if (r8.equalsIgnoreCase(r9 + ".local") != false) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean domainMatches(java.lang.String r8, java.lang.String r9) {
        /*
            r0 = 0
            if (r8 == 0) goto L_0x0089
            if (r9 != 0) goto L_0x0007
            goto L_0x0089
        L_0x0007:
            java.lang.String r1 = ".local"
            boolean r2 = r1.equalsIgnoreCase(r8)
            r3 = 46
            int r4 = r8.indexOf((int) r3)
            r5 = 1
            if (r4 != 0) goto L_0x001a
            int r4 = r8.indexOf((int) r3, (int) r5)
        L_0x001a:
            r6 = -1
            if (r2 != 0) goto L_0x0027
            if (r4 == r6) goto L_0x0026
            int r7 = r8.length()
            int r7 = r7 - r5
            if (r4 != r7) goto L_0x0027
        L_0x0026:
            return r0
        L_0x0027:
            int r4 = r9.indexOf((int) r3)
            if (r4 != r6) goto L_0x0045
            if (r2 != 0) goto L_0x0044
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append((java.lang.String) r9)
            r4.append((java.lang.String) r1)
            java.lang.String r1 = r4.toString()
            boolean r1 = r8.equalsIgnoreCase(r1)
            if (r1 == 0) goto L_0x0045
        L_0x0044:
            return r5
        L_0x0045:
            int r1 = r8.length()
            int r4 = r9.length()
            int r4 = r4 - r1
            if (r4 != 0) goto L_0x0055
            boolean r8 = r9.equalsIgnoreCase(r8)
            return r8
        L_0x0055:
            if (r4 <= 0) goto L_0x0076
            r9.substring(r0, r4)
            java.lang.String r9 = r9.substring(r4)
            boolean r9 = r9.equalsIgnoreCase(r8)
            if (r9 == 0) goto L_0x0075
            java.lang.String r9 = "."
            boolean r9 = r8.startsWith(r9)
            if (r9 == 0) goto L_0x0072
            boolean r8 = isFullyQualifiedDomainName(r8, r5)
            if (r8 != 0) goto L_0x0074
        L_0x0072:
            if (r2 == 0) goto L_0x0075
        L_0x0074:
            r0 = r5
        L_0x0075:
            return r0
        L_0x0076:
            if (r4 != r6) goto L_0x0089
            char r1 = r8.charAt(r0)
            if (r1 != r3) goto L_0x0089
            java.lang.String r8 = r8.substring(r5)
            boolean r8 = r9.equalsIgnoreCase(r8)
            if (r8 == 0) goto L_0x0089
            r0 = r5
        L_0x0089:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.HttpCookie.domainMatches(java.lang.String, java.lang.String):boolean");
    }

    private static boolean isFullyQualifiedDomainName(String str, int i) {
        int indexOf = str.indexOf(46, i + 1);
        if (indexOf == -1 || indexOf >= str.length() - 1) {
            return false;
        }
        return true;
    }

    public String toString() {
        if (getVersion() > 0) {
            return toRFC2965HeaderString();
        }
        return toNetscapeHeaderString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HttpCookie)) {
            return false;
        }
        HttpCookie httpCookie = (HttpCookie) obj;
        if (!equalsIgnoreCase(getName(), httpCookie.getName()) || !equalsIgnoreCase(getDomain(), httpCookie.getDomain()) || !Objects.equals(getPath(), httpCookie.getPath())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hashCode = this.name.toLowerCase().hashCode();
        String str = this.domain;
        int i = 0;
        int hashCode2 = str != null ? str.toLowerCase().hashCode() : 0;
        String str2 = this.path;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + hashCode2 + i;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static boolean isToken(String str) {
        if (RESERVED_NAMES.contains(str.toLowerCase(Locale.f698US))) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt < ' ' || charAt >= 127 || tspecials.indexOf((int) charAt) != -1) {
                return false;
            }
        }
        return true;
    }

    private static HttpCookie parseInternal(String str, boolean z) {
        HttpCookie httpCookie;
        String str2;
        String str3;
        StringTokenizer stringTokenizer = new StringTokenizer(str, NavigationBarInflaterView.GRAVITY_SEPARATOR);
        try {
            String nextToken = stringTokenizer.nextToken();
            int indexOf = nextToken.indexOf(61);
            if (indexOf != -1) {
                String trim = nextToken.substring(0, indexOf).trim();
                String trim2 = nextToken.substring(indexOf + 1).trim();
                if (z) {
                    httpCookie = new HttpCookie(trim, stripOffSurroundingQuote(trim2), str);
                } else {
                    httpCookie = new HttpCookie(trim, stripOffSurroundingQuote(trim2));
                }
                while (stringTokenizer.hasMoreTokens()) {
                    String nextToken2 = stringTokenizer.nextToken();
                    int indexOf2 = nextToken2.indexOf(61);
                    if (indexOf2 != -1) {
                        str3 = nextToken2.substring(0, indexOf2).trim();
                        str2 = nextToken2.substring(indexOf2 + 1).trim();
                    } else {
                        str3 = nextToken2.trim();
                        str2 = null;
                    }
                    assignAttribute(httpCookie, str3, str2);
                }
                return httpCookie;
            }
            throw new IllegalArgumentException("Invalid cookie name-value pair");
        } catch (NoSuchElementException unused) {
            throw new IllegalArgumentException("Empty cookie header string");
        }
    }

    private static void assignAttribute(HttpCookie httpCookie, String str, String str2) {
        String stripOffSurroundingQuote = stripOffSurroundingQuote(str2);
        CookieAttributeAssignor cookieAttributeAssignor = assignors.get(str.toLowerCase());
        if (cookieAttributeAssignor != null) {
            cookieAttributeAssignor.assign(httpCookie, str, stripOffSurroundingQuote);
        }
    }

    private String header() {
        return this.header;
    }

    private String toNetscapeHeaderString() {
        return getName() + "=" + getValue();
    }

    private String toRFC2965HeaderString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("=\"");
        sb.append(getValue());
        sb.append((char) Typography.quote);
        if (getPath() != null) {
            sb.append(";$Path=\"");
            sb.append(getPath());
            sb.append((char) Typography.quote);
        }
        if (getDomain() != null) {
            sb.append(";$Domain=\"");
            sb.append(getDomain());
            sb.append((char) Typography.quote);
        }
        if (getPortlist() != null) {
            sb.append(";$Port=\"");
            sb.append(getPortlist());
            sb.append((char) Typography.quote);
        }
        return sb.toString();
    }

    private static int guessCookieVersion(String str) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.indexOf("expires=") != -1) {
            return 0;
        }
        if (lowerCase.indexOf("version=") == -1 && lowerCase.indexOf("max-age") == -1 && !startsWithIgnoreCase(lowerCase, SET_COOKIE2)) {
            return 0;
        }
        return 1;
    }

    private static String stripOffSurroundingQuote(String str) {
        if (str == null || str.length() <= 2 || str.charAt(0) != '\"' || str.charAt(str.length() - 1) != '\"') {
            return (str == null || str.length() <= 2 || str.charAt(0) != '\'' || str.charAt(str.length() - 1) != '\'') ? str : str.substring(1, str.length() - 1);
        }
        return str.substring(1, str.length() - 1);
    }

    private static boolean equalsIgnoreCase(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str == null || str2 == null) {
            return false;
        }
        return str.equalsIgnoreCase(str2);
    }

    private static boolean startsWithIgnoreCase(String str, String str2) {
        if (str == null || str2 == null || str.length() < str2.length() || !str2.equalsIgnoreCase(str.substring(0, str2.length()))) {
            return false;
        }
        return true;
    }

    private static List<String> splitMultiCookies(String str) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < str.length(); i3++) {
            char charAt = str.charAt(i3);
            if (charAt == '\"') {
                i2++;
            }
            if (charAt == ',' && i2 % 2 == 0) {
                arrayList.add(str.substring(i, i3));
                i = i3 + 1;
            }
        }
        arrayList.add(str.substring(i));
        return arrayList;
    }
}
