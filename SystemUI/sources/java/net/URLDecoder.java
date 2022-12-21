package java.net;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.p026io.UnsupportedEncodingException;

public class URLDecoder {
    static String dfltEncName = URLEncoder.dfltEncName;

    private static boolean isValidHexChar(char c) {
        return ('0' <= c && c <= '9') || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
    }

    @Deprecated
    public static String decode(String str) {
        try {
            return decode(str, dfltEncName);
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String decode(String str, String str2) throws UnsupportedEncodingException {
        if (!str2.isEmpty()) {
            try {
                return decode(str, Charset.forName(str2));
            } catch (IllegalCharsetNameException | UnsupportedCharsetException unused) {
                throw new UnsupportedEncodingException(str2);
            }
        } else {
            throw new UnsupportedEncodingException("URLDecoder: empty string enc parameter");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00c3, code lost:
        r1.append(new java.lang.String(r3, 0, r5, r13));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String decode(java.lang.String r12, java.nio.charset.Charset r13) {
        /*
            java.lang.String r0 = "Charset"
            java.util.Objects.requireNonNull(r13, (java.lang.String) r0)
            int r0 = r12.length()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r2 = 500(0x1f4, float:7.0E-43)
            if (r0 <= r2) goto L_0x0012
            int r2 = r0 / 2
            goto L_0x0013
        L_0x0012:
            r2 = r0
        L_0x0013:
            r1.<init>((int) r2)
            r2 = 0
            r3 = 0
            r4 = r2
            r5 = r4
        L_0x001a:
            if (r4 >= r0) goto L_0x00e5
            char r6 = r12.charAt(r4)
            r7 = 37
            r8 = 1
            if (r6 == r7) goto L_0x0038
            r7 = 43
            if (r6 == r7) goto L_0x002f
            r1.append((char) r6)
            int r4 = r4 + 1
            goto L_0x001a
        L_0x002f:
            r5 = 32
            r1.append((char) r5)
            int r4 = r4 + 1
        L_0x0036:
            r5 = r8
            goto L_0x001a
        L_0x0038:
            if (r3 != 0) goto L_0x0044
            int r3 = r0 - r4
            int r3 = r3 / 3
            byte[] r3 = new byte[r3]     // Catch:{ NumberFormatException -> 0x0041 }
            goto L_0x0044
        L_0x0041:
            r12 = move-exception
            goto L_0x00cd
        L_0x0044:
            r5 = r2
        L_0x0045:
            int r9 = r4 + 2
            if (r9 >= r0) goto L_0x00b6
            if (r6 != r7) goto L_0x00b6
            int r10 = r4 + 1
            char r11 = r12.charAt(r10)     // Catch:{ NumberFormatException -> 0x0041 }
            boolean r11 = isValidHexChar(r11)     // Catch:{ NumberFormatException -> 0x0041 }
            if (r11 == 0) goto L_0x0099
            char r9 = r12.charAt(r9)     // Catch:{ NumberFormatException -> 0x0041 }
            boolean r9 = isValidHexChar(r9)     // Catch:{ NumberFormatException -> 0x0041 }
            if (r9 == 0) goto L_0x0099
            int r9 = r4 + 3
            java.lang.String r10 = r12.substring(r10, r9)     // Catch:{ NumberFormatException -> 0x0041 }
            r11 = 16
            int r10 = java.lang.Integer.parseInt(r10, r11)     // Catch:{ NumberFormatException -> 0x0041 }
            if (r10 < 0) goto L_0x007e
            int r4 = r5 + 1
            byte r10 = (byte) r10     // Catch:{ NumberFormatException -> 0x0041 }
            r3[r5] = r10     // Catch:{ NumberFormatException -> 0x0041 }
            if (r9 >= r0) goto L_0x007b
            char r5 = r12.charAt(r9)     // Catch:{ NumberFormatException -> 0x0041 }
            r6 = r5
        L_0x007b:
            r5 = r4
            r4 = r9
            goto L_0x0045
        L_0x007e:
            java.lang.IllegalArgumentException r13 = new java.lang.IllegalArgumentException     // Catch:{ NumberFormatException -> 0x0041 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ NumberFormatException -> 0x0041 }
            r0.<init>()     // Catch:{ NumberFormatException -> 0x0041 }
            java.lang.String r1 = "URLDecoder: Illegal hex characters in escape (%) pattern - negative value : "
            r0.append((java.lang.String) r1)     // Catch:{ NumberFormatException -> 0x0041 }
            java.lang.String r12 = r12.substring(r4, r9)     // Catch:{ NumberFormatException -> 0x0041 }
            r0.append((java.lang.String) r12)     // Catch:{ NumberFormatException -> 0x0041 }
            java.lang.String r12 = r0.toString()     // Catch:{ NumberFormatException -> 0x0041 }
            r13.<init>((java.lang.String) r12)     // Catch:{ NumberFormatException -> 0x0041 }
            throw r13     // Catch:{ NumberFormatException -> 0x0041 }
        L_0x0099:
            java.lang.IllegalArgumentException r13 = new java.lang.IllegalArgumentException     // Catch:{ NumberFormatException -> 0x0041 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ NumberFormatException -> 0x0041 }
            r0.<init>()     // Catch:{ NumberFormatException -> 0x0041 }
            java.lang.String r1 = "URLDecoder: Illegal hex characters in escape (%) pattern : "
            r0.append((java.lang.String) r1)     // Catch:{ NumberFormatException -> 0x0041 }
            int r1 = r4 + 3
            java.lang.String r12 = r12.substring(r4, r1)     // Catch:{ NumberFormatException -> 0x0041 }
            r0.append((java.lang.String) r12)     // Catch:{ NumberFormatException -> 0x0041 }
            java.lang.String r12 = r0.toString()     // Catch:{ NumberFormatException -> 0x0041 }
            r13.<init>((java.lang.String) r12)     // Catch:{ NumberFormatException -> 0x0041 }
            throw r13     // Catch:{ NumberFormatException -> 0x0041 }
        L_0x00b6:
            if (r4 >= r0) goto L_0x00c3
            if (r6 == r7) goto L_0x00bb
            goto L_0x00c3
        L_0x00bb:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException     // Catch:{ NumberFormatException -> 0x0041 }
            java.lang.String r13 = "URLDecoder: Incomplete trailing escape (%) pattern"
            r12.<init>((java.lang.String) r13)     // Catch:{ NumberFormatException -> 0x0041 }
            throw r12     // Catch:{ NumberFormatException -> 0x0041 }
        L_0x00c3:
            java.lang.String r6 = new java.lang.String     // Catch:{ NumberFormatException -> 0x0041 }
            r6.<init>((byte[]) r3, (int) r2, (int) r5, (java.nio.charset.Charset) r13)     // Catch:{ NumberFormatException -> 0x0041 }
            r1.append((java.lang.String) r6)     // Catch:{ NumberFormatException -> 0x0041 }
            goto L_0x0036
        L_0x00cd:
            java.lang.IllegalArgumentException r13 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "URLDecoder: Illegal hex characters in escape (%) pattern - "
            r0.<init>((java.lang.String) r1)
            java.lang.String r12 = r12.getMessage()
            r0.append((java.lang.String) r12)
            java.lang.String r12 = r0.toString()
            r13.<init>((java.lang.String) r12)
            throw r13
        L_0x00e5:
            if (r5 == 0) goto L_0x00eb
            java.lang.String r12 = r1.toString()
        L_0x00eb:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URLDecoder.decode(java.lang.String, java.nio.charset.Charset):java.lang.String");
    }
}
