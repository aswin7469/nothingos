package com.android.okhttp;

import java.util.concurrent.TimeUnit;

public final class CacheControl {
    public static final CacheControl FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    public static final CacheControl FORCE_NETWORK = new Builder().noCache().build();
    String headerValue;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;

    private CacheControl(boolean z, boolean z2, int i, int i2, boolean z3, boolean z4, boolean z5, int i3, int i4, boolean z6, boolean z7, String str) {
        this.noCache = z;
        this.noStore = z2;
        this.maxAgeSeconds = i;
        this.sMaxAgeSeconds = i2;
        this.isPrivate = z3;
        this.isPublic = z4;
        this.mustRevalidate = z5;
        this.maxStaleSeconds = i3;
        this.minFreshSeconds = i4;
        this.onlyIfCached = z6;
        this.noTransform = z7;
        this.headerValue = str;
    }

    private CacheControl(Builder builder) {
        this.noCache = builder.noCache;
        this.noStore = builder.noStore;
        this.maxAgeSeconds = builder.maxAgeSeconds;
        this.sMaxAgeSeconds = -1;
        this.isPrivate = false;
        this.isPublic = false;
        this.mustRevalidate = false;
        this.maxStaleSeconds = builder.maxStaleSeconds;
        this.minFreshSeconds = builder.minFreshSeconds;
        this.onlyIfCached = builder.onlyIfCached;
        this.noTransform = builder.noTransform;
    }

    public boolean noCache() {
        return this.noCache;
    }

    public boolean noStore() {
        return this.noStore;
    }

    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }

    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }

    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }

    public boolean noTransform() {
        return this.noTransform;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.android.okhttp.CacheControl parse(com.android.okhttp.Headers r21) {
        /*
            r0 = r21
            int r1 = r21.size()
            r6 = 0
            r7 = 1
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = -1
            r12 = -1
            r13 = 0
            r14 = 0
            r15 = 0
            r16 = -1
            r17 = -1
            r18 = 0
            r19 = 0
        L_0x0018:
            if (r6 >= r1) goto L_0x0139
            java.lang.String r2 = r0.name(r6)
            java.lang.String r5 = r0.value(r6)
            java.lang.String r3 = "Cache-Control"
            boolean r3 = r2.equalsIgnoreCase(r3)
            if (r3 == 0) goto L_0x002f
            if (r8 == 0) goto L_0x002d
            goto L_0x0037
        L_0x002d:
            r8 = r5
            goto L_0x0038
        L_0x002f:
            java.lang.String r3 = "Pragma"
            boolean r2 = r2.equalsIgnoreCase(r3)
            if (r2 == 0) goto L_0x0132
        L_0x0037:
            r7 = 0
        L_0x0038:
            r2 = 0
        L_0x0039:
            int r3 = r5.length()
            if (r2 >= r3) goto L_0x0132
            java.lang.String r3 = "=,;"
            int r3 = com.android.okhttp.internal.http.HeaderParser.skipUntil(r5, r2, r3)
            java.lang.String r2 = r5.substring(r2, r3)
            java.lang.String r2 = r2.trim()
            int r4 = r5.length()
            if (r3 == r4) goto L_0x0097
            char r4 = r5.charAt(r3)
            r0 = 44
            if (r4 == r0) goto L_0x0097
            char r0 = r5.charAt(r3)
            r4 = 59
            if (r0 != r4) goto L_0x0064
            goto L_0x0097
        L_0x0064:
            int r3 = r3 + 1
            int r0 = com.android.okhttp.internal.http.HeaderParser.skipWhitespace(r5, r3)
            int r3 = r5.length()
            if (r0 >= r3) goto L_0x0087
            char r3 = r5.charAt(r0)
            r4 = 34
            if (r3 != r4) goto L_0x0087
            int r0 = r0 + 1
            java.lang.String r3 = "\""
            int r3 = com.android.okhttp.internal.http.HeaderParser.skipUntil(r5, r0, r3)
            java.lang.String r0 = r5.substring(r0, r3)
            r4 = 1
            int r3 = r3 + r4
            goto L_0x009b
        L_0x0087:
            r4 = 1
            java.lang.String r3 = ",;"
            int r3 = com.android.okhttp.internal.http.HeaderParser.skipUntil(r5, r0, r3)
            java.lang.String r0 = r5.substring(r0, r3)
            java.lang.String r0 = r0.trim()
            goto L_0x009b
        L_0x0097:
            r4 = 1
            int r3 = r3 + 1
            r0 = 0
        L_0x009b:
            java.lang.String r4 = "no-cache"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x00a8
            r4 = -1
            r9 = 1
            goto L_0x012d
        L_0x00a8:
            java.lang.String r4 = "no-store"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x00b5
            r4 = -1
            r10 = 1
            goto L_0x012d
        L_0x00b5:
            java.lang.String r4 = "max-age"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x00c3
            r4 = -1
            int r11 = com.android.okhttp.internal.http.HeaderParser.parseSeconds(r0, r4)
            goto L_0x012d
        L_0x00c3:
            java.lang.String r4 = "s-maxage"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x00d2
            r4 = -1
            int r12 = com.android.okhttp.internal.http.HeaderParser.parseSeconds(r0, r4)
            goto L_0x012d
        L_0x00d2:
            java.lang.String r4 = "private"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x00de
            r4 = -1
            r13 = 1
            goto L_0x012d
        L_0x00de:
            java.lang.String r4 = "public"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x00ea
            r4 = -1
            r14 = 1
            goto L_0x012d
        L_0x00ea:
            java.lang.String r4 = "must-revalidate"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x00f6
            r4 = -1
            r15 = 1
            goto L_0x012d
        L_0x00f6:
            java.lang.String r4 = "max-stale"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x0107
            r2 = 2147483647(0x7fffffff, float:NaN)
            int r16 = com.android.okhttp.internal.http.HeaderParser.parseSeconds(r0, r2)
            r4 = -1
            goto L_0x012d
        L_0x0107:
            java.lang.String r4 = "min-fresh"
            boolean r4 = r4.equalsIgnoreCase(r2)
            if (r4 == 0) goto L_0x0115
            r4 = -1
            int r17 = com.android.okhttp.internal.http.HeaderParser.parseSeconds(r0, r4)
            goto L_0x012d
        L_0x0115:
            r4 = -1
            java.lang.String r0 = "only-if-cached"
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x0122
            r18 = 1
            goto L_0x012d
        L_0x0122:
            java.lang.String r0 = "no-transform"
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x012d
            r19 = 1
        L_0x012d:
            r0 = r21
            r2 = r3
            goto L_0x0039
        L_0x0132:
            r4 = -1
            int r6 = r6 + 1
            r0 = r21
            goto L_0x0018
        L_0x0139:
            if (r7 != 0) goto L_0x013e
            r20 = 0
            goto L_0x0140
        L_0x013e:
            r20 = r8
        L_0x0140:
            com.android.okhttp.CacheControl r0 = new com.android.okhttp.CacheControl
            r8 = r0
            r8.<init>(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.CacheControl.parse(com.android.okhttp.Headers):com.android.okhttp.CacheControl");
    }

    public String toString() {
        String str = this.headerValue;
        if (str != null) {
            return str;
        }
        String headerValue2 = headerValue();
        this.headerValue = headerValue2;
        return headerValue2;
    }

    private String headerValue() {
        StringBuilder sb = new StringBuilder();
        if (this.noCache) {
            sb.append("no-cache, ");
        }
        if (this.noStore) {
            sb.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            sb.append("max-age=");
            sb.append(this.maxAgeSeconds);
            sb.append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            sb.append("s-maxage=");
            sb.append(this.sMaxAgeSeconds);
            sb.append(", ");
        }
        if (this.isPrivate) {
            sb.append("private, ");
        }
        if (this.isPublic) {
            sb.append("public, ");
        }
        if (this.mustRevalidate) {
            sb.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            sb.append("max-stale=");
            sb.append(this.maxStaleSeconds);
            sb.append(", ");
        }
        if (this.minFreshSeconds != -1) {
            sb.append("min-fresh=");
            sb.append(this.minFreshSeconds);
            sb.append(", ");
        }
        if (this.onlyIfCached) {
            sb.append("only-if-cached, ");
        }
        if (this.noTransform) {
            sb.append("no-transform, ");
        }
        if (sb.length() == 0) {
            return "";
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public static final class Builder {
        int maxAgeSeconds = -1;
        int maxStaleSeconds = -1;
        int minFreshSeconds = -1;
        boolean noCache;
        boolean noStore;
        boolean noTransform;
        boolean onlyIfCached;

        public Builder noCache() {
            this.noCache = true;
            return this;
        }

        public Builder noStore() {
            this.noStore = true;
            return this;
        }

        public Builder maxAge(int i, TimeUnit timeUnit) {
            if (i >= 0) {
                long seconds = timeUnit.toSeconds((long) i);
                this.maxAgeSeconds = seconds > 2147483647L ? Integer.MAX_VALUE : (int) seconds;
                return this;
            }
            throw new IllegalArgumentException("maxAge < 0: " + i);
        }

        public Builder maxStale(int i, TimeUnit timeUnit) {
            if (i >= 0) {
                long seconds = timeUnit.toSeconds((long) i);
                this.maxStaleSeconds = seconds > 2147483647L ? Integer.MAX_VALUE : (int) seconds;
                return this;
            }
            throw new IllegalArgumentException("maxStale < 0: " + i);
        }

        public Builder minFresh(int i, TimeUnit timeUnit) {
            if (i >= 0) {
                long seconds = timeUnit.toSeconds((long) i);
                this.minFreshSeconds = seconds > 2147483647L ? Integer.MAX_VALUE : (int) seconds;
                return this;
            }
            throw new IllegalArgumentException("minFresh < 0: " + i);
        }

        public Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }

        public Builder noTransform() {
            this.noTransform = true;
            return this;
        }

        public CacheControl build() {
            return new CacheControl(this);
        }
    }
}
