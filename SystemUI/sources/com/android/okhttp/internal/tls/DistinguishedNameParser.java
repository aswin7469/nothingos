package com.android.okhttp.internal.tls;

import javax.security.auth.x500.X500Principal;

final class DistinguishedNameParser {
    private int beg;
    private char[] chars;
    private int cur;

    /* renamed from: dn */
    private final String f236dn;
    private int end;
    private final int length;
    private int pos;

    public DistinguishedNameParser(X500Principal x500Principal) {
        String name = x500Principal.getName(X500Principal.RFC2253);
        this.f236dn = name;
        this.length = name.length();
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0015 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0017  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String nextAT() {
        /*
            r6 = this;
        L_0x0000:
            int r0 = r6.pos
            int r1 = r6.length
            r2 = 32
            if (r0 >= r1) goto L_0x0013
            char[] r3 = r6.chars
            char r3 = r3[r0]
            if (r3 != r2) goto L_0x0013
            int r0 = r0 + 1
            r6.pos = r0
            goto L_0x0000
        L_0x0013:
            if (r0 != r1) goto L_0x0017
            r6 = 0
            return r6
        L_0x0017:
            r6.beg = r0
            int r0 = r0 + 1
            r6.pos = r0
        L_0x001d:
            int r0 = r6.pos
            int r1 = r6.length
            r3 = 61
            if (r0 >= r1) goto L_0x0032
            char[] r4 = r6.chars
            char r4 = r4[r0]
            if (r4 == r3) goto L_0x0032
            if (r4 == r2) goto L_0x0032
            int r0 = r0 + 1
            r6.pos = r0
            goto L_0x001d
        L_0x0032:
            java.lang.String r4 = "Unexpected end of DN: "
            if (r0 >= r1) goto L_0x00d1
            r6.end = r0
            char[] r1 = r6.chars
            char r0 = r1[r0]
            if (r0 != r2) goto L_0x006e
        L_0x003e:
            int r0 = r6.pos
            int r1 = r6.length
            if (r0 >= r1) goto L_0x0051
            char[] r5 = r6.chars
            char r5 = r5[r0]
            if (r5 == r3) goto L_0x0051
            if (r5 != r2) goto L_0x0051
            int r0 = r0 + 1
            r6.pos = r0
            goto L_0x003e
        L_0x0051:
            char[] r5 = r6.chars
            char r5 = r5[r0]
            if (r5 != r3) goto L_0x005a
            if (r0 == r1) goto L_0x005a
            goto L_0x006e
        L_0x005a:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((java.lang.String) r4)
            java.lang.String r6 = r6.f236dn
            r1.append((java.lang.String) r6)
            java.lang.String r6 = r1.toString()
            r0.<init>((java.lang.String) r6)
            throw r0
        L_0x006e:
            int r0 = r6.pos
            int r0 = r0 + 1
            r6.pos = r0
        L_0x0074:
            int r0 = r6.pos
            int r1 = r6.length
            if (r0 >= r1) goto L_0x0085
            char[] r1 = r6.chars
            char r1 = r1[r0]
            if (r1 != r2) goto L_0x0085
            int r0 = r0 + 1
            r6.pos = r0
            goto L_0x0074
        L_0x0085:
            int r0 = r6.end
            int r1 = r6.beg
            int r0 = r0 - r1
            r2 = 4
            if (r0 <= r2) goto L_0x00c4
            char[] r0 = r6.chars
            int r3 = r1 + 3
            char r3 = r0[r3]
            r4 = 46
            if (r3 != r4) goto L_0x00c4
            char r3 = r0[r1]
            r4 = 79
            if (r3 == r4) goto L_0x00a1
            r4 = 111(0x6f, float:1.56E-43)
            if (r3 != r4) goto L_0x00c4
        L_0x00a1:
            int r3 = r1 + 1
            char r3 = r0[r3]
            r4 = 73
            if (r3 == r4) goto L_0x00b1
            int r3 = r1 + 1
            char r3 = r0[r3]
            r4 = 105(0x69, float:1.47E-43)
            if (r3 != r4) goto L_0x00c4
        L_0x00b1:
            int r3 = r1 + 2
            char r3 = r0[r3]
            r4 = 68
            if (r3 == r4) goto L_0x00c1
            int r3 = r1 + 2
            char r0 = r0[r3]
            r3 = 100
            if (r0 != r3) goto L_0x00c4
        L_0x00c1:
            int r1 = r1 + r2
            r6.beg = r1
        L_0x00c4:
            java.lang.String r0 = new java.lang.String
            char[] r1 = r6.chars
            int r2 = r6.beg
            int r6 = r6.end
            int r6 = r6 - r2
            r0.<init>((char[]) r1, (int) r2, (int) r6)
            return r0
        L_0x00d1:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((java.lang.String) r4)
            java.lang.String r6 = r6.f236dn
            r1.append((java.lang.String) r6)
            java.lang.String r6 = r1.toString()
            r0.<init>((java.lang.String) r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.tls.DistinguishedNameParser.nextAT():java.lang.String");
    }

    private String quotedAV() {
        int i = this.pos + 1;
        this.pos = i;
        this.beg = i;
        this.end = i;
        while (true) {
            int i2 = this.pos;
            if (i2 != this.length) {
                char[] cArr = this.chars;
                char c = cArr[i2];
                if (c == '\"') {
                    this.pos = i2 + 1;
                    while (true) {
                        int i3 = this.pos;
                        if (i3 >= this.length || this.chars[i3] != ' ') {
                            char[] cArr2 = this.chars;
                            int i4 = this.beg;
                        } else {
                            this.pos = i3 + 1;
                        }
                    }
                    char[] cArr22 = this.chars;
                    int i42 = this.beg;
                    return new String(cArr22, i42, this.end - i42);
                }
                if (c == '\\') {
                    cArr[this.end] = getEscaped();
                } else {
                    cArr[this.end] = c;
                }
                this.pos++;
                this.end++;
            } else {
                throw new IllegalStateException("Unexpected end of DN: " + this.f236dn);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0016, code lost:
        r1 = r6.chars;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String hexAV() {
        /*
            r6 = this;
            int r0 = r6.pos
            int r1 = r0 + 4
            int r2 = r6.length
            java.lang.String r3 = "Unexpected end of DN: "
            if (r1 >= r2) goto L_0x0095
            r6.beg = r0
            int r0 = r0 + 1
            r6.pos = r0
        L_0x0010:
            int r0 = r6.pos
            int r1 = r6.length
            if (r0 == r1) goto L_0x0054
            char[] r1 = r6.chars
            char r2 = r1[r0]
            r4 = 43
            if (r2 == r4) goto L_0x0054
            r4 = 44
            if (r2 == r4) goto L_0x0054
            r4 = 59
            if (r2 != r4) goto L_0x0027
            goto L_0x0054
        L_0x0027:
            r4 = 32
            if (r2 != r4) goto L_0x0042
            r6.end = r0
            int r0 = r0 + 1
            r6.pos = r0
        L_0x0031:
            int r0 = r6.pos
            int r1 = r6.length
            if (r0 >= r1) goto L_0x0056
            char[] r1 = r6.chars
            char r1 = r1[r0]
            if (r1 != r4) goto L_0x0056
            int r0 = r0 + 1
            r6.pos = r0
            goto L_0x0031
        L_0x0042:
            r4 = 65
            if (r2 < r4) goto L_0x004f
            r4 = 70
            if (r2 > r4) goto L_0x004f
            int r2 = r2 + 32
            char r2 = (char) r2
            r1[r0] = r2
        L_0x004f:
            int r0 = r0 + 1
            r6.pos = r0
            goto L_0x0010
        L_0x0054:
            r6.end = r0
        L_0x0056:
            int r0 = r6.end
            int r1 = r6.beg
            int r0 = r0 - r1
            r2 = 5
            if (r0 < r2) goto L_0x0081
            r2 = r0 & 1
            if (r2 == 0) goto L_0x0081
            int r2 = r0 / 2
            byte[] r3 = new byte[r2]
            int r1 = r1 + 1
            r4 = 0
        L_0x0069:
            if (r4 >= r2) goto L_0x0077
            int r5 = r6.getByte(r1)
            byte r5 = (byte) r5
            r3[r4] = r5
            int r1 = r1 + 2
            int r4 = r4 + 1
            goto L_0x0069
        L_0x0077:
            java.lang.String r1 = new java.lang.String
            char[] r2 = r6.chars
            int r6 = r6.beg
            r1.<init>((char[]) r2, (int) r6, (int) r0)
            return r1
        L_0x0081:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((java.lang.String) r3)
            java.lang.String r6 = r6.f236dn
            r1.append((java.lang.String) r6)
            java.lang.String r6 = r1.toString()
            r0.<init>((java.lang.String) r6)
            throw r0
        L_0x0095:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((java.lang.String) r3)
            java.lang.String r6 = r6.f236dn
            r1.append((java.lang.String) r6)
            java.lang.String r6 = r1.toString()
            r0.<init>((java.lang.String) r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.tls.DistinguishedNameParser.hexAV():java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x009d, code lost:
        return new java.lang.String(r1, r2, r8.cur - r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String escapedAV() {
        /*
            r8 = this;
            int r0 = r8.pos
            r8.beg = r0
            r8.end = r0
        L_0x0006:
            int r0 = r8.pos
            int r1 = r8.length
            if (r0 < r1) goto L_0x0019
            java.lang.String r0 = new java.lang.String
            char[] r1 = r8.chars
            int r2 = r8.beg
            int r8 = r8.end
            int r8 = r8 - r2
            r0.<init>((char[]) r1, (int) r2, (int) r8)
            return r0
        L_0x0019:
            char[] r1 = r8.chars
            char r2 = r1[r0]
            r3 = 44
            r4 = 43
            r5 = 59
            r6 = 32
            if (r2 == r6) goto L_0x005e
            if (r2 == r5) goto L_0x0051
            r5 = 92
            if (r2 == r5) goto L_0x003e
            if (r2 == r4) goto L_0x0051
            if (r2 == r3) goto L_0x0051
            int r3 = r8.end
            int r4 = r3 + 1
            r8.end = r4
            r1[r3] = r2
            int r0 = r0 + 1
            r8.pos = r0
            goto L_0x0006
        L_0x003e:
            int r0 = r8.end
            int r2 = r0 + 1
            r8.end = r2
            char r2 = r8.getEscaped()
            r1[r0] = r2
            int r0 = r8.pos
            int r0 = r0 + 1
            r8.pos = r0
            goto L_0x0006
        L_0x0051:
            java.lang.String r0 = new java.lang.String
            char[] r1 = r8.chars
            int r2 = r8.beg
            int r8 = r8.end
            int r8 = r8 - r2
            r0.<init>((char[]) r1, (int) r2, (int) r8)
            return r0
        L_0x005e:
            int r2 = r8.end
            r8.cur = r2
            int r0 = r0 + 1
            r8.pos = r0
            int r0 = r2 + 1
            r8.end = r0
            r1[r2] = r6
        L_0x006c:
            int r0 = r8.pos
            int r1 = r8.length
            if (r0 >= r1) goto L_0x0085
            char[] r2 = r8.chars
            char r7 = r2[r0]
            if (r7 != r6) goto L_0x0085
            int r1 = r8.end
            int r7 = r1 + 1
            r8.end = r7
            r2[r1] = r6
            int r0 = r0 + 1
            r8.pos = r0
            goto L_0x006c
        L_0x0085:
            if (r0 == r1) goto L_0x0091
            char[] r1 = r8.chars
            char r0 = r1[r0]
            if (r0 == r3) goto L_0x0091
            if (r0 == r4) goto L_0x0091
            if (r0 != r5) goto L_0x0006
        L_0x0091:
            java.lang.String r0 = new java.lang.String
            char[] r1 = r8.chars
            int r2 = r8.beg
            int r8 = r8.cur
            int r8 = r8 - r2
            r0.<init>((char[]) r1, (int) r2, (int) r8)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.tls.DistinguishedNameParser.escapedAV():java.lang.String");
    }

    private char getEscaped() {
        int i = this.pos + 1;
        this.pos = i;
        if (i != this.length) {
            char c = this.chars[i];
            if (!(c == ' ' || c == '%' || c == '\\' || c == '_' || c == '\"' || c == '#')) {
                switch (c) {
                    case '*':
                    case '+':
                    case ',':
                        break;
                    default:
                        switch (c) {
                            case ';':
                            case '<':
                            case '=':
                            case '>':
                                break;
                            default:
                                return getUTF8();
                        }
                }
            }
            return c;
        }
        throw new IllegalStateException("Unexpected end of DN: " + this.f236dn);
    }

    private char getUTF8() {
        int i;
        int i2;
        int i3 = getByte(this.pos);
        this.pos++;
        if (i3 < 128) {
            return (char) i3;
        }
        if (i3 < 192 || i3 > 247) {
            return '?';
        }
        if (i3 <= 223) {
            i2 = i3 & 31;
            i = 1;
        } else if (i3 <= 239) {
            i2 = i3 & 15;
            i = 2;
        } else {
            i2 = i3 & 7;
            i = 3;
        }
        for (int i4 = 0; i4 < i; i4++) {
            int i5 = this.pos + 1;
            this.pos = i5;
            if (i5 == this.length || this.chars[i5] != '\\') {
                return '?';
            }
            int i6 = i5 + 1;
            this.pos = i6;
            int i7 = getByte(i6);
            this.pos++;
            if ((i7 & 192) != 128) {
                return '?';
            }
            i2 = (i2 << 6) + (i7 & 63);
        }
        return (char) i2;
    }

    private int getByte(int i) {
        int i2;
        int i3;
        int i4 = i + 1;
        if (i4 < this.length) {
            char[] cArr = this.chars;
            char c = cArr[i];
            if (c >= '0' && c <= '9') {
                i2 = c - '0';
            } else if (c >= 'a' && c <= 'f') {
                i2 = c - 'W';
            } else if (c < 'A' || c > 'F') {
                throw new IllegalStateException("Malformed DN: " + this.f236dn);
            } else {
                i2 = c - '7';
            }
            char c2 = cArr[i4];
            if (c2 >= '0' && c2 <= '9') {
                i3 = c2 - '0';
            } else if (c2 >= 'a' && c2 <= 'f') {
                i3 = c2 - 'W';
            } else if (c2 < 'A' || c2 > 'F') {
                throw new IllegalStateException("Malformed DN: " + this.f236dn);
            } else {
                i3 = c2 - '7';
            }
            return (i2 << 4) + i3;
        }
        throw new IllegalStateException("Malformed DN: " + this.f236dn);
    }

    public String findMostSpecific(String str) {
        String str2;
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.f236dn.toCharArray();
        String nextAT = nextAT();
        if (nextAT == null) {
            return null;
        }
        do {
            int i = this.pos;
            if (i == this.length) {
                return null;
            }
            char c = this.chars[i];
            if (c != '\"') {
                str2 = c != '#' ? (c == '+' || c == ',' || c == ';') ? "" : escapedAV() : hexAV();
            } else {
                str2 = quotedAV();
            }
            if (str.equalsIgnoreCase(nextAT)) {
                return str2;
            }
            int i2 = this.pos;
            if (i2 >= this.length) {
                return null;
            }
            char c2 = this.chars[i2];
            if (c2 == ',' || c2 == ';' || c2 == '+') {
                this.pos = i2 + 1;
                nextAT = nextAT();
            } else {
                throw new IllegalStateException("Malformed DN: " + this.f236dn);
            }
        } while (nextAT != null);
        throw new IllegalStateException("Malformed DN: " + this.f236dn);
    }
}
