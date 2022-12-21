package sun.nio.p035fs;

/* renamed from: sun.nio.fs.Globs */
public class Globs {
    private static char EOL = '\u0000';
    private static final String globMetaChars = "\\*?[{";
    private static final String regexMetaChars = ".^$+{[]|()";

    private Globs() {
    }

    private static boolean isRegexMeta(char c) {
        return regexMetaChars.indexOf((int) c) != -1;
    }

    private static boolean isGlobMeta(char c) {
        return globMetaChars.indexOf((int) c) != -1;
    }

    private static char next(String str, int i) {
        if (i < str.length()) {
            return str.charAt(i);
        }
        return EOL;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0111, code lost:
        if (r3 != ']') goto L_0x0119;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0113, code lost:
        r1.append("]]");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0121, code lost:
        throw new java.util.regex.PatternSyntaxException("Missing ']", r0, r5 - 1);
     */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0111 A[EDGE_INSN: B:125:0x0111->B:78:0x0111 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00af  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String toRegexPattern(java.lang.String r17, boolean r18) {
        /*
            r0 = r17
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "^"
            r1.<init>((java.lang.String) r2)
            r2 = 0
            r3 = r2
            r4 = r3
        L_0x000c:
            int r5 = r17.length()
            r6 = 1
            if (r3 >= r5) goto L_0x0187
            int r5 = r3 + 1
            char r3 = r0.charAt(r3)
            r7 = 42
            if (r3 == r7) goto L_0x0169
            r7 = 44
            if (r3 == r7) goto L_0x015d
            r7 = 47
            if (r3 == r7) goto L_0x0151
            r8 = 63
            if (r3 == r8) goto L_0x0143
            r8 = 123(0x7b, float:1.72E-43)
            if (r3 == r8) goto L_0x012f
            r8 = 125(0x7d, float:1.75E-43)
            if (r3 == r8) goto L_0x0122
            r8 = 91
            r9 = 92
            if (r3 == r8) goto L_0x006f
            if (r3 == r9) goto L_0x0047
            boolean r6 = isRegexMeta(r3)
            if (r6 == 0) goto L_0x0042
            r1.append((char) r9)
        L_0x0042:
            r1.append((char) r3)
            goto L_0x0176
        L_0x0047:
            int r3 = r17.length()
            if (r5 == r3) goto L_0x0066
            int r3 = r5 + 1
            char r5 = r0.charAt(r5)
            boolean r6 = isGlobMeta(r5)
            if (r6 != 0) goto L_0x005f
            boolean r6 = isRegexMeta(r5)
            if (r6 == 0) goto L_0x0062
        L_0x005f:
            r1.append((char) r9)
        L_0x0062:
            r1.append((char) r5)
            goto L_0x000c
        L_0x0066:
            java.util.regex.PatternSyntaxException r1 = new java.util.regex.PatternSyntaxException
            java.lang.String r2 = "No character to escape"
            int r5 = r5 - r6
            r1.<init>(r2, r0, r5)
            throw r1
        L_0x006f:
            if (r18 == 0) goto L_0x0077
            java.lang.String r10 = "[[^\\\\]&&["
            r1.append((java.lang.String) r10)
            goto L_0x007c
        L_0x0077:
            java.lang.String r10 = "[[^/]&&["
            r1.append((java.lang.String) r10)
        L_0x007c:
            char r10 = next(r0, r5)
            r11 = 94
            r12 = 45
            if (r10 != r11) goto L_0x008e
            java.lang.String r10 = "\\^"
            r1.append((java.lang.String) r10)
        L_0x008b:
            int r5 = r5 + 1
            goto L_0x00a5
        L_0x008e:
            char r10 = next(r0, r5)
            r13 = 33
            if (r10 != r13) goto L_0x009b
            r1.append((char) r11)
            int r5 = r5 + 1
        L_0x009b:
            char r10 = next(r0, r5)
            if (r10 != r12) goto L_0x00a5
            r1.append((char) r12)
            goto L_0x008b
        L_0x00a5:
            r10 = r2
            r11 = r10
        L_0x00a7:
            int r13 = r17.length()
            r14 = 93
            if (r5 >= r13) goto L_0x0111
            int r3 = r5 + 1
            char r5 = r0.charAt(r5)
            if (r5 != r14) goto L_0x00bd
            r16 = r5
            r5 = r3
            r3 = r16
            goto L_0x0111
        L_0x00bd:
            if (r5 == r7) goto L_0x0108
            if (r18 == 0) goto L_0x00c3
            if (r5 == r9) goto L_0x0108
        L_0x00c3:
            if (r5 == r9) goto L_0x00d1
            if (r5 == r8) goto L_0x00d1
            r13 = 38
            if (r5 != r13) goto L_0x00d4
            char r15 = next(r0, r3)
            if (r15 != r13) goto L_0x00d4
        L_0x00d1:
            r1.append((char) r9)
        L_0x00d4:
            r1.append((char) r5)
            if (r5 != r12) goto L_0x0103
            java.lang.String r5 = "Invalid range"
            if (r10 == 0) goto L_0x00fc
            int r10 = r3 + 1
            char r3 = next(r0, r3)
            char r13 = EOL
            if (r3 == r13) goto L_0x00fa
            if (r3 != r14) goto L_0x00ea
            goto L_0x00fa
        L_0x00ea:
            if (r3 < r11) goto L_0x00f2
            r1.append((char) r3)
            r5 = r10
            r10 = r2
            goto L_0x00a7
        L_0x00f2:
            java.util.regex.PatternSyntaxException r1 = new java.util.regex.PatternSyntaxException
            int r10 = r10 + -3
            r1.<init>(r5, r0, r10)
            throw r1
        L_0x00fa:
            r5 = r10
            goto L_0x0111
        L_0x00fc:
            java.util.regex.PatternSyntaxException r1 = new java.util.regex.PatternSyntaxException
            int r3 = r3 - r6
            r1.<init>(r5, r0, r3)
            throw r1
        L_0x0103:
            r11 = r5
            r10 = r6
            r5 = r3
            r3 = r11
            goto L_0x00a7
        L_0x0108:
            java.util.regex.PatternSyntaxException r1 = new java.util.regex.PatternSyntaxException
            java.lang.String r2 = "Explicit 'name separator' in class"
            int r3 = r3 - r6
            r1.<init>(r2, r0, r3)
            throw r1
        L_0x0111:
            if (r3 != r14) goto L_0x0119
            java.lang.String r3 = "]]"
            r1.append((java.lang.String) r3)
            goto L_0x0176
        L_0x0119:
            java.util.regex.PatternSyntaxException r1 = new java.util.regex.PatternSyntaxException
            java.lang.String r2 = "Missing ']"
            int r5 = r5 - r6
            r1.<init>(r2, r0, r5)
            throw r1
        L_0x0122:
            if (r4 == 0) goto L_0x012b
            java.lang.String r3 = "))"
            r1.append((java.lang.String) r3)
            r4 = r2
            goto L_0x0176
        L_0x012b:
            r1.append((char) r8)
            goto L_0x0176
        L_0x012f:
            if (r4 != 0) goto L_0x013a
            java.lang.String r3 = "(?:(?:"
            r1.append((java.lang.String) r3)
            r3 = r5
            r4 = r6
            goto L_0x000c
        L_0x013a:
            java.util.regex.PatternSyntaxException r1 = new java.util.regex.PatternSyntaxException
            java.lang.String r2 = "Cannot nest groups"
            int r5 = r5 - r6
            r1.<init>(r2, r0, r5)
            throw r1
        L_0x0143:
            if (r18 == 0) goto L_0x014b
            java.lang.String r3 = "[^\\\\]"
            r1.append((java.lang.String) r3)
            goto L_0x0176
        L_0x014b:
            java.lang.String r3 = "[^/]"
            r1.append((java.lang.String) r3)
            goto L_0x0176
        L_0x0151:
            if (r18 == 0) goto L_0x0159
            java.lang.String r3 = "\\\\"
            r1.append((java.lang.String) r3)
            goto L_0x0176
        L_0x0159:
            r1.append((char) r3)
            goto L_0x0176
        L_0x015d:
            if (r4 == 0) goto L_0x0165
            java.lang.String r3 = ")|(?:"
            r1.append((java.lang.String) r3)
            goto L_0x0176
        L_0x0165:
            r1.append((char) r7)
            goto L_0x0176
        L_0x0169:
            char r3 = next(r0, r5)
            if (r3 != r7) goto L_0x0179
            java.lang.String r3 = ".*"
            r1.append((java.lang.String) r3)
            int r5 = r5 + 1
        L_0x0176:
            r3 = r5
            goto L_0x000c
        L_0x0179:
            if (r18 == 0) goto L_0x0181
            java.lang.String r3 = "[^\\\\]*"
            r1.append((java.lang.String) r3)
            goto L_0x0176
        L_0x0181:
            java.lang.String r3 = "[^/]*"
            r1.append((java.lang.String) r3)
            goto L_0x0176
        L_0x0187:
            if (r4 != 0) goto L_0x0193
            r0 = 36
            r1.append((char) r0)
            java.lang.String r0 = r1.toString()
            return r0
        L_0x0193:
            java.util.regex.PatternSyntaxException r1 = new java.util.regex.PatternSyntaxException
            java.lang.String r2 = "Missing '}"
            int r3 = r3 - r6
            r1.<init>(r2, r0, r3)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.Globs.toRegexPattern(java.lang.String, boolean):java.lang.String");
    }

    static String toUnixRegexPattern(String str) {
        return toRegexPattern(str, false);
    }

    static String toWindowsRegexPattern(String str) {
        return toRegexPattern(str, true);
    }
}
