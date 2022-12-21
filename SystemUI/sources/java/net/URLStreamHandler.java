package java.net;

import java.p026io.IOException;
import java.util.Objects;

public abstract class URLStreamHandler {
    /* access modifiers changed from: protected */
    public int getDefaultPort() {
        return -1;
    }

    /* access modifiers changed from: protected */
    public abstract URLConnection openConnection(URL url) throws IOException;

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0163  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0168  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x016e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parseURL(java.net.URL r18, java.lang.String r19, int r20, int r21) {
        /*
            r17 = this;
            r0 = r19
            r1 = r20
            r2 = r21
            java.lang.String r3 = r18.getProtocol()
            java.lang.String r4 = r18.getAuthority()
            java.lang.String r5 = r18.getUserInfo()
            java.lang.String r6 = r18.getHost()
            int r7 = r18.getPort()
            java.lang.String r8 = r18.getPath()
            java.lang.String r9 = r18.getQuery()
            java.lang.String r10 = r18.getRef()
            r11 = 63
            r12 = -1
            r14 = 0
            if (r1 >= r2) goto L_0x0043
            int r15 = r0.indexOf((int) r11)
            if (r15 == r12) goto L_0x0043
            if (r15 >= r2) goto L_0x0043
            int r9 = r15 + 1
            java.lang.String r9 = r0.substring(r9, r2)
            if (r2 <= r15) goto L_0x003d
            r2 = r15
        L_0x003d:
            java.lang.String r0 = r0.substring(r14, r15)
            r15 = 1
            goto L_0x0044
        L_0x0043:
            r15 = r14
        L_0x0044:
            int r13 = r2 + -2
            r14 = 92
            java.lang.String r16 = ""
            r12 = 47
            if (r1 > r13) goto L_0x0182
            char r13 = r0.charAt(r1)
            if (r13 != r12) goto L_0x0182
            int r13 = r1 + 1
            char r13 = r0.charAt(r13)
            if (r13 != r12) goto L_0x0182
            int r1 = r1 + 2
            r4 = r1
        L_0x005f:
            if (r4 >= r2) goto L_0x0072
            char r5 = r0.charAt(r4)
            r6 = 35
            if (r5 == r6) goto L_0x0072
            if (r5 == r12) goto L_0x0072
            if (r5 == r11) goto L_0x0072
            if (r5 == r14) goto L_0x0072
            int r4 = r4 + 1
            goto L_0x005f
        L_0x0072:
            java.lang.String r1 = r0.substring(r1, r4)
            r5 = 64
            int r6 = r1.indexOf((int) r5)
            r11 = -1
            if (r6 == r11) goto L_0x0094
            int r5 = r1.lastIndexOf((int) r5)
            if (r6 == r5) goto L_0x0088
            r5 = 0
            r6 = 0
            goto L_0x0096
        L_0x0088:
            r5 = 0
            java.lang.String r11 = r1.substring(r5, r6)
            r13 = 1
            int r6 = r6 + r13
            java.lang.String r6 = r1.substring(r6)
            goto L_0x0097
        L_0x0094:
            r5 = 0
            r6 = r1
        L_0x0096:
            r11 = 0
        L_0x0097:
            if (r6 == 0) goto L_0x0163
            int r7 = r6.length()
            r13 = 58
            if (r7 <= 0) goto L_0x011d
            char r7 = r6.charAt(r5)
            r8 = 91
            if (r7 != r8) goto L_0x011d
            r7 = 93
            int r7 = r6.indexOf((int) r7)
            java.lang.String r8 = "Invalid authority field: "
            r14 = 2
            if (r7 <= r14) goto L_0x010b
            int r14 = r7 + 1
            java.lang.String r12 = r6.substring(r5, r14)
            r5 = 1
            java.lang.String r7 = r12.substring(r5, r7)
            boolean r7 = sun.net.util.IPAddressUtil.isIPv6LiteralAddress(r7)
            if (r7 == 0) goto L_0x00f7
            int r7 = r6.length()
            if (r7 <= r14) goto L_0x00f4
            char r7 = r6.charAt(r14)
            if (r7 != r13) goto L_0x00e2
            int r7 = r6.length()
            int r14 = r14 + r5
            if (r7 <= r14) goto L_0x00f4
            java.lang.String r5 = r6.substring(r14)
            int r5 = java.lang.Integer.parseInt(r5)
            r7 = r5
            goto L_0x00f5
        L_0x00e2:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>((java.lang.String) r8)
            r2.append((java.lang.String) r1)
            java.lang.String r1 = r2.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x00f4:
            r7 = -1
        L_0x00f5:
            r6 = r12
            goto L_0x0165
        L_0x00f7:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Invalid host: "
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r12)
            java.lang.String r1 = r1.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x010b:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>((java.lang.String) r8)
            r2.append((java.lang.String) r1)
            java.lang.String r1 = r2.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x011d:
            int r5 = r6.indexOf((int) r13)
            if (r5 < 0) goto L_0x0160
            int r7 = r6.length()
            int r8 = r5 + 1
            if (r7 <= r8) goto L_0x0158
            char r7 = r6.charAt(r8)
            r12 = 48
            if (r7 < r12) goto L_0x0140
            r12 = 57
            if (r7 > r12) goto L_0x0140
            java.lang.String r7 = r6.substring(r8)
            int r7 = java.lang.Integer.parseInt(r7)
            goto L_0x0159
        L_0x0140:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "invalid port: "
            r1.<init>((java.lang.String) r2)
            java.lang.String r2 = r6.substring(r8)
            r1.append((java.lang.String) r2)
            java.lang.String r1 = r1.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0158:
            r7 = -1
        L_0x0159:
            r8 = 0
            java.lang.String r5 = r6.substring(r8, r5)
            r6 = r5
            goto L_0x0165
        L_0x0160:
            r5 = -1
            r7 = -1
            goto L_0x0166
        L_0x0163:
            r6 = r16
        L_0x0165:
            r5 = -1
        L_0x0166:
            if (r7 < r5) goto L_0x016e
            r5 = r1
            r8 = 0
            if (r15 != 0) goto L_0x0185
            r9 = 0
            goto L_0x0185
        L_0x016e:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Invalid port number :"
            r1.<init>((java.lang.String) r2)
            r1.append((int) r7)
            java.lang.String r1 = r1.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0182:
            r11 = r5
            r5 = r4
            r4 = r1
        L_0x0185:
            if (r6 != 0) goto L_0x0189
            r6 = r16
        L_0x0189:
            if (r4 >= r2) goto L_0x01ed
            char r1 = r0.charAt(r4)
            r12 = 47
            if (r1 == r12) goto L_0x01e9
            char r1 = r0.charAt(r4)
            r13 = 92
            if (r1 != r13) goto L_0x019c
            goto L_0x01e9
        L_0x019c:
            java.lang.String r1 = "/"
            if (r8 == 0) goto L_0x01d0
            int r13 = r8.length()
            if (r13 <= 0) goto L_0x01d0
            int r13 = r8.lastIndexOf((int) r12)
            r12 = -1
            if (r13 != r12) goto L_0x01b0
            if (r5 == 0) goto L_0x01b0
            goto L_0x01b2
        L_0x01b0:
            r1 = r16
        L_0x01b2:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r14 = 1
            int r13 = r13 + r14
            r14 = 0
            java.lang.String r8 = r8.substring(r14, r13)
            r12.append((java.lang.String) r8)
            r12.append((java.lang.String) r1)
            java.lang.String r0 = r0.substring(r4, r2)
            r12.append((java.lang.String) r0)
            java.lang.String r8 = r12.toString()
            goto L_0x01ed
        L_0x01d0:
            if (r5 == 0) goto L_0x01d3
            goto L_0x01d5
        L_0x01d3:
            r1 = r16
        L_0x01d5:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append((java.lang.String) r1)
            java.lang.String r0 = r0.substring(r4, r2)
            r8.append((java.lang.String) r0)
            java.lang.String r8 = r8.toString()
            goto L_0x01ed
        L_0x01e9:
            java.lang.String r8 = r0.substring(r4, r2)
        L_0x01ed:
            if (r8 != 0) goto L_0x01f1
            r8 = r16
        L_0x01f1:
            java.lang.String r0 = "/./"
            int r0 = r8.indexOf((java.lang.String) r0)
            if (r0 < 0) goto L_0x0214
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r2 = 0
            java.lang.String r4 = r8.substring(r2, r0)
            r1.append((java.lang.String) r4)
            int r0 = r0 + 2
            java.lang.String r0 = r8.substring(r0)
            r1.append((java.lang.String) r0)
            java.lang.String r8 = r1.toString()
            goto L_0x01f1
        L_0x0214:
            r0 = 0
        L_0x0215:
            java.lang.String r1 = "/../"
            int r0 = r8.indexOf((java.lang.String) r1, (int) r0)
            if (r0 < 0) goto L_0x0256
            if (r0 != 0) goto L_0x0226
            int r0 = r0 + 3
            java.lang.String r8 = r8.substring(r0)
            goto L_0x0214
        L_0x0226:
            if (r0 <= 0) goto L_0x0253
            int r2 = r0 + -1
            r4 = 47
            int r2 = r8.lastIndexOf((int) r4, (int) r2)
            if (r2 < 0) goto L_0x0253
            int r1 = r8.indexOf((java.lang.String) r1, (int) r2)
            if (r1 == 0) goto L_0x0253
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r4 = 0
            java.lang.String r2 = r8.substring(r4, r2)
            r1.append((java.lang.String) r2)
            int r0 = r0 + 3
            java.lang.String r0 = r8.substring(r0)
            r1.append((java.lang.String) r0)
            java.lang.String r8 = r1.toString()
            goto L_0x0214
        L_0x0253:
            int r0 = r0 + 3
            goto L_0x0215
        L_0x0256:
            java.lang.String r0 = "/.."
            boolean r1 = r8.endsWith(r0)
            if (r1 == 0) goto L_0x0274
            int r0 = r8.indexOf((java.lang.String) r0)
            r1 = 1
            int r0 = r0 - r1
            r1 = 47
            int r0 = r8.lastIndexOf((int) r1, (int) r0)
            if (r0 < 0) goto L_0x0274
            int r0 = r0 + 1
            r2 = 0
            java.lang.String r8 = r8.substring(r2, r0)
            goto L_0x0256
        L_0x0274:
            java.lang.String r0 = "./"
            boolean r0 = r8.startsWith(r0)
            if (r0 == 0) goto L_0x0287
            int r0 = r8.length()
            r1 = 2
            if (r0 <= r1) goto L_0x0287
            java.lang.String r8 = r8.substring(r1)
        L_0x0287:
            java.lang.String r0 = "/."
            boolean r0 = r8.endsWith(r0)
            if (r0 == 0) goto L_0x029b
            int r0 = r8.length()
            r1 = 1
            int r0 = r0 - r1
            r2 = 0
            java.lang.String r8 = r8.substring(r2, r0)
            goto L_0x029d
        L_0x029b:
            r1 = 1
            r2 = 0
        L_0x029d:
            java.lang.String r0 = "?"
            boolean r0 = r8.endsWith(r0)
            if (r0 == 0) goto L_0x02af
            int r0 = r8.length()
            int r0 = r0 - r1
            java.lang.String r0 = r8.substring(r2, r0)
            r8 = r0
        L_0x02af:
            r0 = r17
            r1 = r18
            r2 = r3
            r3 = r6
            r4 = r7
            r6 = r11
            r7 = r8
            r8 = r9
            r9 = r10
            r0.setURL(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URLStreamHandler.parseURL(java.net.URL, java.lang.String, int, int):void");
    }

    /* access modifiers changed from: protected */
    public boolean equals(URL url, URL url2) {
        return Objects.equals(url.getRef(), url2.getRef()) && Objects.equals(url.getQuery(), url2.getQuery()) && sameFile(url, url2);
    }

    /* access modifiers changed from: protected */
    public int hashCode(URL url) {
        return Objects.hash(url.getRef(), url.getQuery(), url.getProtocol(), url.getFile(), url.getHost(), Integer.valueOf(url.getPort()));
    }

    /* access modifiers changed from: protected */
    public boolean sameFile(URL url, URL url2) {
        if (url.getProtocol() != url2.getProtocol() && (url.getProtocol() == null || !url.getProtocol().equalsIgnoreCase(url2.getProtocol()))) {
            return false;
        }
        if (url.getFile() != url2.getFile() && (url.getFile() == null || !url.getFile().equals(url2.getFile()))) {
            return false;
        }
        if ((url.getPort() != -1 ? url.getPort() : url.handler.getDefaultPort()) == (url2.getPort() != -1 ? url2.getPort() : url2.handler.getDefaultPort()) && hostsEqual(url, url2)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0028, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.net.InetAddress getHostAddress(java.net.URL r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.net.InetAddress r0 = r4.hostAddress     // Catch:{ all -> 0x0029 }
            if (r0 == 0) goto L_0x0009
            java.net.InetAddress r4 = r4.hostAddress     // Catch:{ all -> 0x0029 }
            monitor-exit(r3)
            return r4
        L_0x0009:
            java.lang.String r0 = r4.getHost()     // Catch:{ all -> 0x0029 }
            r1 = 0
            if (r0 == 0) goto L_0x0027
            java.lang.String r2 = ""
            boolean r2 = r0.equals(r2)     // Catch:{ all -> 0x0029 }
            if (r2 == 0) goto L_0x0019
            goto L_0x0027
        L_0x0019:
            java.net.InetAddress r0 = java.net.InetAddress.getByName(r0)     // Catch:{ UnknownHostException -> 0x0025, SecurityException -> 0x0023 }
            r4.hostAddress = r0     // Catch:{ UnknownHostException -> 0x0025, SecurityException -> 0x0023 }
            java.net.InetAddress r4 = r4.hostAddress     // Catch:{ all -> 0x0029 }
            monitor-exit(r3)
            return r4
        L_0x0023:
            monitor-exit(r3)
            return r1
        L_0x0025:
            monitor-exit(r3)
            return r1
        L_0x0027:
            monitor-exit(r3)
            return r1
        L_0x0029:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URLStreamHandler.getHostAddress(java.net.URL):java.net.InetAddress");
    }

    /* access modifiers changed from: protected */
    public boolean hostsEqual(URL url, URL url2) {
        if (url.getHost() == null || url2.getHost() == null) {
            return url.getHost() == null && url2.getHost() == null;
        }
        return url.getHost().equalsIgnoreCase(url2.getHost());
    }

    /* access modifiers changed from: protected */
    public String toExternalForm(URL url) {
        int length = url.getProtocol().length() + 1;
        if (url.getAuthority() != null && url.getAuthority().length() > 0) {
            length += url.getAuthority().length() + 2;
        }
        if (url.getPath() != null) {
            length += url.getPath().length();
        }
        if (url.getQuery() != null) {
            length += url.getQuery().length() + 1;
        }
        if (url.getRef() != null) {
            length += url.getRef().length() + 1;
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(url.getProtocol());
        sb.append(":");
        if (url.getAuthority() != null) {
            sb.append("//");
            sb.append(url.getAuthority());
        }
        String file = url.getFile();
        if (file != null) {
            sb.append(file);
        }
        if (url.getRef() != null) {
            sb.append("#");
            sb.append(url.getRef());
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void setURL(URL url, String str, String str2, int i, String str3, String str4, String str5, String str6, String str7) {
        if (this == url.handler) {
            url.set(url.getProtocol(), str2, i, str3, str4, str5, str6, str7);
            return;
        }
        throw new SecurityException("handler for url different from this handler");
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void setURL(URL url, String str, String str2, int i, String str3, String str4) {
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11 = str2;
        int i2 = i;
        String str12 = str3;
        if (str11 == null || str2.length() == 0) {
            str7 = str11;
            str6 = null;
            str5 = null;
        } else {
            if (i2 == -1) {
                str10 = str11;
            } else {
                str10 = str2 + ":" + i;
            }
            int lastIndexOf = str2.lastIndexOf(64);
            if (lastIndexOf != -1) {
                str5 = str2.substring(0, lastIndexOf);
                str11 = str2.substring(lastIndexOf + 1);
            } else {
                str5 = null;
            }
            str6 = str10;
            str7 = str11;
        }
        if (str12 != null) {
            int lastIndexOf2 = str12.lastIndexOf(63);
            if (lastIndexOf2 != -1) {
                String substring = str12.substring(lastIndexOf2 + 1);
                str9 = str12.substring(0, lastIndexOf2);
                str8 = substring;
            } else {
                str9 = str12;
                str8 = null;
            }
        } else {
            str9 = null;
            str8 = null;
        }
        setURL(url, str, str7, i, str6, str5, str9, str8, str4);
    }
}
