package sun.security.x509;

import java.p026io.IOException;
import java.util.Locale;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class DNSName implements GeneralNameInterface {
    private static final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String alphaDigitsAndHyphen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-";
    private static final String digitsAndHyphen = "0123456789-";
    private String name;

    public int getType() {
        return 2;
    }

    public DNSName(DerValue derValue) throws IOException {
        this.name = derValue.getIA5String();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0062, code lost:
        r0 = r1 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public DNSName(java.lang.String r7) throws java.p026io.IOException {
        /*
            r6 = this;
            r6.<init>()
            if (r7 == 0) goto L_0x0088
            int r0 = r7.length()
            if (r0 == 0) goto L_0x0088
            r0 = 32
            int r0 = r7.indexOf((int) r0)
            r1 = -1
            if (r0 != r1) goto L_0x0080
            r0 = 0
            char r1 = r7.charAt(r0)
            r2 = 46
            if (r1 == r2) goto L_0x0078
            int r1 = r7.length()
            r3 = 1
            int r1 = r1 - r3
            char r1 = r7.charAt(r1)
            if (r1 == r2) goto L_0x0078
        L_0x0029:
            int r1 = r7.length()
            if (r0 >= r1) goto L_0x0075
            int r1 = r7.indexOf((int) r2, (int) r0)
            if (r1 >= 0) goto L_0x0039
            int r1 = r7.length()
        L_0x0039:
            int r4 = r1 - r0
            if (r4 < r3) goto L_0x006d
            java.lang.String r4 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            char r5 = r7.charAt(r0)
            int r4 = r4.indexOf((int) r5)
            if (r4 < 0) goto L_0x0065
        L_0x0049:
            int r0 = r0 + 1
            if (r0 >= r1) goto L_0x0062
            char r4 = r7.charAt(r0)
            java.lang.String r5 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-"
            int r4 = r5.indexOf((int) r4)
            if (r4 < 0) goto L_0x005a
            goto L_0x0049
        L_0x005a:
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r7 = "DNSName components must consist of letters, digits, and hyphens"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x0062:
            int r0 = r1 + 1
            goto L_0x0029
        L_0x0065:
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r7 = "DNSName components must begin with a letter"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x006d:
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r7 = "DNSName SubjectAltNames with empty components are not permitted"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x0075:
            r6.name = r7
            return
        L_0x0078:
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r7 = "DNS names or NameConstraints may not begin or end with a ."
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x0080:
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r7 = "DNS names or NameConstraints with blank components are not permitted"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x0088:
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r7 = "DNS name must not be null"
            r6.<init>((java.lang.String) r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.DNSName.<init>(java.lang.String):void");
    }

    public String getName() {
        return this.name;
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putIA5String(this.name);
    }

    public String toString() {
        return "DNSName: " + this.name;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DNSName)) {
            return false;
        }
        return this.name.equalsIgnoreCase(((DNSName) obj).name);
    }

    public int hashCode() {
        return this.name.toUpperCase(Locale.ENGLISH).hashCode();
    }

    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        if (generalNameInterface == null || generalNameInterface.getType() != 2) {
            return -1;
        }
        String lowerCase = ((DNSName) generalNameInterface).getName().toLowerCase(Locale.ENGLISH);
        String lowerCase2 = this.name.toLowerCase(Locale.ENGLISH);
        if (lowerCase.equals(lowerCase2)) {
            return 0;
        }
        if (lowerCase2.endsWith(lowerCase)) {
            if (lowerCase2.charAt(lowerCase2.lastIndexOf(lowerCase) - 1) == '.') {
                return 2;
            }
        } else if (lowerCase.endsWith(lowerCase2) && lowerCase.charAt(lowerCase.lastIndexOf(lowerCase2) - 1) == '.') {
            return 1;
        }
        return 3;
    }

    public int subtreeDepth() throws UnsupportedOperationException {
        int indexOf = this.name.indexOf(46);
        int i = 1;
        while (indexOf >= 0) {
            i++;
            indexOf = this.name.indexOf(46, indexOf + 1);
        }
        return i;
    }
}
