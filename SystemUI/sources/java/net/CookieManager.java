package java.net;

import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CookieManager extends CookieHandler {
    private CookieStore cookieJar;
    private CookiePolicy policyCallback;

    public CookieManager() {
        this((CookieStore) null, (CookiePolicy) null);
    }

    public CookieManager(CookieStore cookieStore, CookiePolicy cookiePolicy) {
        this.cookieJar = null;
        this.policyCallback = cookiePolicy == null ? CookiePolicy.ACCEPT_ORIGINAL_SERVER : cookiePolicy;
        if (cookieStore == null) {
            this.cookieJar = new InMemoryCookieStore();
        } else {
            this.cookieJar = cookieStore;
        }
    }

    public void setCookiePolicy(CookiePolicy cookiePolicy) {
        if (cookiePolicy != null) {
            this.policyCallback = cookiePolicy;
        }
    }

    public CookieStore getCookieStore() {
        return this.cookieJar;
    }

    public Map<String, List<String>> get(URI uri, Map<String, List<String>> map) throws IOException {
        if (uri == null || map == null) {
            throw new IllegalArgumentException("Argument is null");
        } else if (this.cookieJar == null) {
            return Map.m1734of();
        } else {
            boolean equalsIgnoreCase = "https".equalsIgnoreCase(uri.getScheme());
            ArrayList arrayList = new ArrayList();
            for (HttpCookie next : this.cookieJar.get(uri)) {
                if (pathMatches(uri, next) && (equalsIgnoreCase || !next.getSecure())) {
                    String portlist = next.getPortlist();
                    if (portlist == null || portlist.isEmpty()) {
                        arrayList.add(next);
                    } else {
                        int port = uri.getPort();
                        if (port == -1) {
                            port = "https".equals(uri.getScheme()) ? 443 : 80;
                        }
                        if (isInPortList(portlist, port)) {
                            arrayList.add(next);
                        }
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return Collections.emptyMap();
            }
            return Map.m1735of("Cookie", sortByPath(arrayList));
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:22|23|(1:25)|26) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r5 = java.util.Collections.emptyList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005d, code lost:
        if (r1.isLoggable(sun.util.logging.PlatformLogger.Level.SEVERE) != false) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005f, code lost:
        r1.severe("Invalid cookie for " + r10 + ": " + r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007b, code lost:
        r4 = r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0053 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void put(java.net.URI r10, java.util.Map<java.lang.String, java.util.List<java.lang.String>> r11) throws java.p026io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = "/"
            if (r10 == 0) goto L_0x0146
            if (r11 == 0) goto L_0x0146
            java.net.CookieStore r1 = r9.cookieJar
            if (r1 != 0) goto L_0x000b
            return
        L_0x000b:
            java.lang.String r1 = "java.net.CookieManager"
            sun.util.logging.PlatformLogger r1 = sun.util.logging.PlatformLogger.getLogger(r1)
            java.util.Set r2 = r11.keySet()
            java.util.Iterator r2 = r2.iterator()
        L_0x0019:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0145
            java.lang.Object r3 = r2.next()
            java.lang.String r3 = (java.lang.String) r3
            if (r3 == 0) goto L_0x0019
            java.lang.String r4 = "Set-Cookie2"
            boolean r4 = r3.equalsIgnoreCase(r4)
            if (r4 != 0) goto L_0x0038
            java.lang.String r4 = "Set-Cookie"
            boolean r4 = r3.equalsIgnoreCase(r4)
            if (r4 != 0) goto L_0x0038
            goto L_0x0019
        L_0x0038:
            java.lang.Object r3 = r11.get(r3)
            java.util.List r3 = (java.util.List) r3
            java.util.Iterator r3 = r3.iterator()
        L_0x0042:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0019
            java.lang.Object r4 = r3.next()
            java.lang.String r4 = (java.lang.String) r4
            java.util.List r4 = java.net.HttpCookie.parse(r4)     // Catch:{ IllegalArgumentException -> 0x0053 }
            goto L_0x007c
        L_0x0053:
            java.util.List r5 = java.util.Collections.emptyList()     // Catch:{ IllegalArgumentException -> 0x0042 }
            sun.util.logging.PlatformLogger$Level r6 = sun.util.logging.PlatformLogger.Level.SEVERE     // Catch:{ IllegalArgumentException -> 0x0042 }
            boolean r6 = r1.isLoggable(r6)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 == 0) goto L_0x007b
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IllegalArgumentException -> 0x0042 }
            r6.<init>()     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.String r7 = "Invalid cookie for "
            r6.append((java.lang.String) r7)     // Catch:{ IllegalArgumentException -> 0x0042 }
            r6.append((java.lang.Object) r10)     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.String r7 = ": "
            r6.append((java.lang.String) r7)     // Catch:{ IllegalArgumentException -> 0x0042 }
            r6.append((java.lang.String) r4)     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.String r4 = r6.toString()     // Catch:{ IllegalArgumentException -> 0x0042 }
            r1.severe(r4)     // Catch:{ IllegalArgumentException -> 0x0042 }
        L_0x007b:
            r4 = r5
        L_0x007c:
            java.util.Iterator r4 = r4.iterator()     // Catch:{ IllegalArgumentException -> 0x0042 }
        L_0x0080:
            boolean r5 = r4.hasNext()     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r5 == 0) goto L_0x0042
            java.lang.Object r5 = r4.next()     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.net.HttpCookie r5 = (java.net.HttpCookie) r5     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.String r6 = r5.getPath()     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 != 0) goto L_0x00b1
            java.lang.String r6 = r10.getPath()     // Catch:{ IllegalArgumentException -> 0x0042 }
            boolean r7 = r6.endsWith(r0)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r7 != 0) goto L_0x00ad
            r7 = 47
            int r7 = r6.lastIndexOf((int) r7)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r7 <= 0) goto L_0x00ac
            int r7 = r7 + 1
            r8 = 0
            java.lang.String r6 = r6.substring(r8, r7)     // Catch:{ IllegalArgumentException -> 0x0042 }
            goto L_0x00ad
        L_0x00ac:
            r6 = r0
        L_0x00ad:
            r5.setPath(r6)     // Catch:{ IllegalArgumentException -> 0x0042 }
            goto L_0x00b8
        L_0x00b1:
            boolean r6 = pathMatches(r10, r5)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 != 0) goto L_0x00b8
            goto L_0x0080
        L_0x00b8:
            java.lang.String r6 = r5.getDomain()     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 != 0) goto L_0x00e0
            java.lang.String r6 = r10.getHost()     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 == 0) goto L_0x00dd
            java.lang.String r7 = "."
            boolean r7 = r6.contains(r7)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r7 != 0) goto L_0x00dd
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IllegalArgumentException -> 0x0042 }
            r7.<init>()     // Catch:{ IllegalArgumentException -> 0x0042 }
            r7.append((java.lang.String) r6)     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.String r6 = ".local"
            r7.append((java.lang.String) r6)     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.String r6 = r7.toString()     // Catch:{ IllegalArgumentException -> 0x0042 }
        L_0x00dd:
            r5.setDomain(r6)     // Catch:{ IllegalArgumentException -> 0x0042 }
        L_0x00e0:
            java.lang.String r6 = r5.getPortlist()     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 == 0) goto L_0x0138
            int r7 = r10.getPort()     // Catch:{ IllegalArgumentException -> 0x0042 }
            r8 = -1
            if (r7 != r8) goto L_0x00fe
            java.lang.String r7 = "https"
            java.lang.String r8 = r10.getScheme()     // Catch:{ IllegalArgumentException -> 0x0042 }
            boolean r7 = r7.equals(r8)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r7 == 0) goto L_0x00fc
            r7 = 443(0x1bb, float:6.21E-43)
            goto L_0x00fe
        L_0x00fc:
            r7 = 80
        L_0x00fe:
            boolean r8 = r6.isEmpty()     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r8 == 0) goto L_0x0125
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IllegalArgumentException -> 0x0042 }
            r6.<init>()     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.String r8 = ""
            r6.append((java.lang.String) r8)     // Catch:{ IllegalArgumentException -> 0x0042 }
            r6.append((int) r7)     // Catch:{ IllegalArgumentException -> 0x0042 }
            java.lang.String r6 = r6.toString()     // Catch:{ IllegalArgumentException -> 0x0042 }
            r5.setPortlist(r6)     // Catch:{ IllegalArgumentException -> 0x0042 }
            boolean r6 = r9.shouldAcceptInternal(r10, r5)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 == 0) goto L_0x0080
            java.net.CookieStore r6 = r9.cookieJar     // Catch:{ IllegalArgumentException -> 0x0042 }
            r6.add(r10, r5)     // Catch:{ IllegalArgumentException -> 0x0042 }
            goto L_0x0080
        L_0x0125:
            boolean r6 = isInPortList(r6, r7)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 == 0) goto L_0x0080
            boolean r6 = r9.shouldAcceptInternal(r10, r5)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 == 0) goto L_0x0080
            java.net.CookieStore r6 = r9.cookieJar     // Catch:{ IllegalArgumentException -> 0x0042 }
            r6.add(r10, r5)     // Catch:{ IllegalArgumentException -> 0x0042 }
            goto L_0x0080
        L_0x0138:
            boolean r6 = r9.shouldAcceptInternal(r10, r5)     // Catch:{ IllegalArgumentException -> 0x0042 }
            if (r6 == 0) goto L_0x0080
            java.net.CookieStore r6 = r9.cookieJar     // Catch:{ IllegalArgumentException -> 0x0042 }
            r6.add(r10, r5)     // Catch:{ IllegalArgumentException -> 0x0042 }
            goto L_0x0080
        L_0x0145:
            return
        L_0x0146:
            java.lang.IllegalArgumentException r9 = new java.lang.IllegalArgumentException
            java.lang.String r10 = "Argument is null"
            r9.<init>((java.lang.String) r10)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.CookieManager.put(java.net.URI, java.util.Map):void");
    }

    private boolean shouldAcceptInternal(URI uri, HttpCookie httpCookie) {
        try {
            return this.policyCallback.shouldAccept(uri, httpCookie);
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean isInPortList(String str, int i) {
        int indexOf = str.indexOf(44);
        while (indexOf > 0) {
            try {
                if (Integer.parseInt(str.substring(0, indexOf)) == i) {
                    return true;
                }
                str = str.substring(indexOf + 1);
                indexOf = str.indexOf(44);
            } catch (NumberFormatException unused) {
            }
        }
        if (!str.isEmpty()) {
            try {
                if (Integer.parseInt(str) == i) {
                    return true;
                }
            } catch (NumberFormatException unused2) {
            }
        }
        return false;
    }

    private static boolean pathMatches(URI uri, HttpCookie httpCookie) {
        return normalizePath(uri.getPath()).startsWith(normalizePath(httpCookie.getPath()));
    }

    /* access modifiers changed from: private */
    public static String normalizePath(String str) {
        if (str == null) {
            str = "";
        }
        if (str.endsWith("/")) {
            return str;
        }
        return str + "/";
    }

    private List<String> sortByPath(List<HttpCookie> list) {
        Collections.sort(list, new CookiePathComparator());
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (HttpCookie next : list) {
            if (next.getVersion() < i) {
                i = next.getVersion();
            }
        }
        if (i == 1) {
            sb.append("$Version=\"1\"; ");
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (i2 != 0) {
                sb.append("; ");
            }
            sb.append(list.get(i2).toString());
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(sb.toString());
        return arrayList;
    }

    static class CookiePathComparator implements Comparator<HttpCookie> {
        CookiePathComparator() {
        }

        public int compare(HttpCookie httpCookie, HttpCookie httpCookie2) {
            if (httpCookie == httpCookie2) {
                return 0;
            }
            if (httpCookie == null) {
                return -1;
            }
            if (httpCookie2 == null) {
                return 1;
            }
            if (!httpCookie.getName().equals(httpCookie2.getName())) {
                return 0;
            }
            String r5 = CookieManager.normalizePath(httpCookie.getPath());
            String r6 = CookieManager.normalizePath(httpCookie2.getPath());
            if (r5.startsWith(r6)) {
                return -1;
            }
            return r6.startsWith(r5) ? 1 : 0;
        }
    }
}
