package sun.nio.p035fs;

import java.net.URI;
import java.nio.file.Path;
import java.p026io.File;
import java.util.Arrays;

/* renamed from: sun.nio.fs.UnixUriUtils */
class UnixUriUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long H_ALPHA;
    private static final long H_ALPHANUM;
    private static final long H_DIGIT = 0;
    private static final long H_LOWALPHA;
    private static final long H_MARK;
    private static final long H_PATH;
    private static final long H_PCHAR;
    private static final long H_UNRESERVED;
    private static final long H_UPALPHA;
    private static final long L_ALPHA = 0;
    private static final long L_ALPHANUM;
    private static final long L_DIGIT;
    private static final long L_LOWALPHA = 0;
    private static final long L_MARK;
    private static final long L_PATH;
    private static final long L_PCHAR;
    private static final long L_UNRESERVED;
    private static final long L_UPALPHA = 0;
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

    private UnixUriUtils() {
    }

    static Path fromUri(UnixFileSystem unixFileSystem, URI uri) {
        byte b;
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("URI is not absolute");
        } else if (!uri.isOpaque()) {
            String scheme = uri.getScheme();
            if (scheme == null || !scheme.equalsIgnoreCase("file")) {
                throw new IllegalArgumentException("URI scheme is not \"file\"");
            } else if (uri.getAuthority() != null) {
                throw new IllegalArgumentException("URI has an authority component");
            } else if (uri.getFragment() != null) {
                throw new IllegalArgumentException("URI has a fragment component");
            } else if (uri.getQuery() != null) {
                throw new IllegalArgumentException("URI has a query component");
            } else if (!uri.toString().startsWith("file:///")) {
                return new File(uri).toPath();
            } else {
                String rawPath = uri.getRawPath();
                int length = rawPath.length();
                if (length != 0) {
                    if (rawPath.endsWith("/") && length > 1) {
                        length--;
                    }
                    byte[] bArr = new byte[length];
                    int i = 0;
                    int i2 = 0;
                    while (i < length) {
                        int i3 = i + 1;
                        char charAt = rawPath.charAt(i);
                        if (charAt == '%') {
                            int i4 = i3 + 1;
                            int i5 = i4 + 1;
                            b = (byte) (decode(rawPath.charAt(i4)) | (decode(rawPath.charAt(i3)) << 4));
                            if (b != 0) {
                                i3 = i5;
                            } else {
                                throw new IllegalArgumentException("Nul character not allowed");
                            }
                        } else {
                            b = (byte) charAt;
                        }
                        bArr[i2] = b;
                        i = i3;
                        i2++;
                    }
                    if (i2 != length) {
                        bArr = Arrays.copyOf(bArr, i2);
                    }
                    return new UnixPath(unixFileSystem, bArr);
                }
                throw new IllegalArgumentException("URI path component is empty");
            }
        } else {
            throw new IllegalArgumentException("URI is not hierarchical");
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x005b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.net.URI toUri(sun.nio.p035fs.UnixPath r9) {
        /*
            sun.nio.fs.UnixPath r0 = r9.toAbsolutePath()
            byte[] r0 = r0.asByteArray()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "file:///"
            r1.<init>((java.lang.String) r2)
            r2 = 1
            r3 = r2
        L_0x0011:
            int r4 = r0.length
            if (r3 >= r4) goto L_0x0041
            byte r4 = r0[r3]
            r4 = r4 & 255(0xff, float:3.57E-43)
            char r4 = (char) r4
            long r5 = L_PATH
            long r7 = H_PATH
            boolean r5 = match(r4, r5, r7)
            if (r5 == 0) goto L_0x0027
            r1.append((char) r4)
            goto L_0x003e
        L_0x0027:
            r5 = 37
            r1.append((char) r5)
            char[] r5 = hexDigits
            int r6 = r4 >> 4
            r6 = r6 & 15
            char r6 = r5[r6]
            r1.append((char) r6)
            r4 = r4 & 15
            char r4 = r5[r4]
            r1.append((char) r4)
        L_0x003e:
            int r3 = r3 + 1
            goto L_0x0011
        L_0x0041:
            int r0 = r1.length()
            int r0 = r0 - r2
            char r0 = r1.charAt(r0)
            r3 = 47
            if (r0 == r3) goto L_0x005b
            sun.nio.fs.UnixFileAttributes r9 = sun.nio.p035fs.UnixFileAttributes.get(r9, r2)     // Catch:{ UnixException -> 0x005b }
            boolean r9 = r9.isDirectory()     // Catch:{ UnixException -> 0x005b }
            if (r9 == 0) goto L_0x005b
            r1.append((char) r3)     // Catch:{ UnixException -> 0x005b }
        L_0x005b:
            java.net.URI r9 = new java.net.URI     // Catch:{ URISyntaxException -> 0x0065 }
            java.lang.String r0 = r1.toString()     // Catch:{ URISyntaxException -> 0x0065 }
            r9.<init>(r0)     // Catch:{ URISyntaxException -> 0x0065 }
            return r9
        L_0x0065:
            r9 = move-exception
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>((java.lang.Object) r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixUriUtils.toUri(sun.nio.fs.UnixPath):java.net.URI");
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

    private static int decode(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        char c2 = 'a';
        if (c < 'a' || c > 'f') {
            c2 = 'A';
            if (c < 'A' || c > 'F') {
                throw new AssertionError();
            }
        }
        return (c - c2) + 10;
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
        long lowMask2 = lowMask("-_.!~*'()");
        L_MARK = lowMask2;
        long highMask3 = highMask("-_.!~*'()");
        H_MARK = highMask3;
        long j4 = j2 | lowMask2;
        L_UNRESERVED = j4;
        long j5 = j3 | highMask3;
        H_UNRESERVED = j5;
        long lowMask3 = j4 | lowMask(":@&=+$,");
        L_PCHAR = lowMask3;
        long highMask4 = j5 | highMask(":@&=+$,");
        H_PCHAR = highMask4;
        L_PATH = lowMask3 | lowMask(";/");
        H_PATH = highMask(";/") | highMask4;
    }
}
