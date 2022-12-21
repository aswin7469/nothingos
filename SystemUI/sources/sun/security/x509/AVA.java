package sun.security.x509;

import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.p026io.Reader;
import java.security.AccessController;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import sun.security.action.GetBooleanAction;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class AVA implements DerEncoder {
    static final int DEFAULT = 1;
    private static final boolean PRESERVE_OLD_DC_ENCODING = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("com.sun.security.preserveOldDCEncoding"))).booleanValue();
    static final int RFC1779 = 2;
    static final int RFC2253 = 3;
    private static final Debug debug = Debug.getInstance(X509CertImpl.NAME, "\t[AVA]");
    private static final String escapedDefault = ",+<>;\"";
    private static final String hexDigits = "0123456789ABCDEF";
    private static final String specialChars1779 = ",=\n+<>#;\\\"";
    private static final String specialChars2253 = ",=+<>#;\\\"";
    private static final String specialCharsDefault = ",=\n+<>#;\\\" ";
    final ObjectIdentifier oid;
    final DerValue value;

    private static boolean isTerminator(int i, int i2) {
        if (i != -1) {
            return i != 59 ? i == 43 || i == 44 : i2 != 3;
        }
        return true;
    }

    public AVA(ObjectIdentifier objectIdentifier, DerValue derValue) {
        if (objectIdentifier == null || derValue == null) {
            throw null;
        }
        this.oid = objectIdentifier;
        this.value = derValue;
    }

    AVA(Reader reader) throws IOException {
        this(reader, 1);
    }

    AVA(Reader reader, Map<String, String> map) throws IOException {
        this(reader, 1, map);
    }

    AVA(Reader reader, int i) throws IOException {
        this(reader, i, Collections.emptyMap());
    }

    AVA(Reader reader, int i, Map<String, String> map) throws IOException {
        int i2;
        StringBuilder sb = new StringBuilder();
        while (true) {
            int readChar = readChar(reader, "Incorrect AVA format");
            if (readChar == 61) {
                break;
            }
            sb.append((char) readChar);
        }
        this.oid = AVAKeyword.getOID(sb.toString(), i, map);
        sb.setLength(0);
        if (i != 3) {
            while (true) {
                i2 = reader.read();
                if (i2 != 32 && i2 != 10) {
                    break;
                }
            }
        } else {
            i2 = reader.read();
            if (i2 == 32) {
                throw new IOException("Incorrect AVA RFC2253 format - leading space must be escaped");
            }
        }
        if (i2 == -1) {
            this.value = new DerValue("");
        } else if (i2 == 35) {
            this.value = parseHexString(reader, i);
        } else if (i2 != 34 || i == 3) {
            this.value = parseString(reader, i2, i, sb);
        } else {
            this.value = parseQuotedString(reader, sb);
        }
    }

    public ObjectIdentifier getObjectIdentifier() {
        return this.oid;
    }

    public DerValue getDerValue() {
        return this.value;
    }

    public String getValueString() {
        try {
            String asString = this.value.getAsString();
            if (asString != null) {
                return asString;
            }
            throw new RuntimeException("AVA string is null");
        } catch (IOException e) {
            throw new RuntimeException("AVA error: " + e, e);
        }
    }

    private static DerValue parseHexString(Reader reader, int i) throws IOException {
        int read;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i2 = 0;
        byte b = 0;
        while (true) {
            read = reader.read();
            if (isTerminator(read, i)) {
                break;
            } else if (read != 32 && read != 10) {
                char c = (char) read;
                int indexOf = hexDigits.indexOf((int) Character.toUpperCase(c));
                if (indexOf != -1) {
                    if (i2 % 2 == 1) {
                        b = (byte) ((b * 16) + ((byte) indexOf));
                        byteArrayOutputStream.write(b);
                    } else {
                        b = (byte) indexOf;
                    }
                    i2++;
                } else {
                    throw new IOException("AVA parse, invalid hex digit: " + c);
                }
            }
        }
        do {
            if (read == 32 || read == 10) {
                read = reader.read();
            } else {
                throw new IOException("AVA parse, invalid hex digit: " + ((char) read));
            }
        } while (!isTerminator(read, i));
        if (i2 == 0) {
            throw new IOException("AVA parse, zero hex digits");
        } else if (i2 % 2 != 1) {
            return new DerValue(byteArrayOutputStream.toByteArray());
        } else {
            throw new IOException("AVA parse, odd number of hex digits");
        }
    }

    private DerValue parseQuotedString(Reader reader, StringBuilder sb) throws IOException {
        int read;
        int readChar = readChar(reader, "Quoted string did not end in quote");
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        while (readChar != 34) {
            if (readChar == 92) {
                readChar = readChar(reader, "Quoted string did not end in quote");
                Byte embeddedHexPair = getEmbeddedHexPair(readChar, reader);
                if (embeddedHexPair != null) {
                    arrayList.add(embeddedHexPair);
                    readChar = reader.read();
                    z = false;
                } else {
                    char c = (char) readChar;
                    if (specialChars1779.indexOf((int) c) < 0) {
                        throw new IOException("Invalid escaped character in AVA: " + c);
                    }
                }
            }
            if (arrayList.size() > 0) {
                sb.append(getEmbeddedHexString(arrayList));
                arrayList.clear();
            }
            char c2 = (char) readChar;
            z &= DerValue.isPrintableStringChar(c2);
            sb.append(c2);
            readChar = readChar(reader, "Quoted string did not end in quote");
        }
        if (arrayList.size() > 0) {
            sb.append(getEmbeddedHexString(arrayList));
            arrayList.clear();
        }
        while (true) {
            read = reader.read();
            if (read != 10 && read != 32) {
                break;
            }
        }
        if (read != -1) {
            throw new IOException("AVA had characters other than whitespace after terminating quote");
        } else if (this.oid.equals((Object) PKCS9Attribute.EMAIL_ADDRESS_OID) || (this.oid.equals((Object) X500Name.DOMAIN_COMPONENT_OID) && !PRESERVE_OLD_DC_ENCODING)) {
            return new DerValue((byte) 22, sb.toString());
        } else {
            if (z) {
                return new DerValue(sb.toString());
            }
            return new DerValue((byte) 12, sb.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:73:0x015c A[LOOP:0: B:1:0x0013->B:73:0x015c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0102 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private sun.security.util.DerValue parseString(java.p026io.Reader r17, int r18, int r19, java.lang.StringBuilder r20) throws java.p026io.IOException {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r19
            r3 = r20
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r5 = 1
            r7 = r18
            r8 = r5
            r10 = r8
            r9 = 0
        L_0x0013:
            r11 = 92
            java.lang.String r13 = ",=+<>#;\\\""
            r14 = -1
            r15 = 3
            if (r7 != r11) goto L_0x009b
            java.lang.String r7 = "Invalid trailing backslash"
            int r7 = readChar(r1, r7)
            java.lang.Byte r11 = getEmbeddedHexPair(r7, r1)
            if (r11 == 0) goto L_0x0031
            r4.add(r11)
            int r7 = r17.read()
            r8 = 0
            goto L_0x00fc
        L_0x0031:
            java.lang.String r11 = "'"
            java.lang.String r6 = "Invalid escaped character in AVA: '"
            if (r2 != r5) goto L_0x0056
            char r5 = (char) r7
            java.lang.String r12 = ",=\n+<>#;\\\" "
            int r12 = r12.indexOf((int) r5)
            if (r12 == r14) goto L_0x0041
            goto L_0x0056
        L_0x0041:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((java.lang.String) r6)
            r1.append((char) r5)
            r1.append((java.lang.String) r11)
            java.lang.String r1 = r1.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0056:
            if (r2 != r15) goto L_0x0099
            r5 = 32
            if (r7 != r5) goto L_0x006d
            if (r10 != 0) goto L_0x0099
            boolean r5 = trailingSpace(r17)
            if (r5 == 0) goto L_0x0065
            goto L_0x0099
        L_0x0065:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Invalid escaped space character in AVA.  Only a leading or trailing space character can be escaped."
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x006d:
            r5 = 35
            if (r7 != r5) goto L_0x007c
            if (r10 == 0) goto L_0x0074
            goto L_0x0099
        L_0x0074:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Invalid escaped '#' character in AVA.  Only a leading '#' can be escaped."
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x007c:
            char r5 = (char) r7
            int r10 = r13.indexOf((int) r5)
            if (r10 == r14) goto L_0x0084
            goto L_0x0099
        L_0x0084:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((java.lang.String) r6)
            r1.append((char) r5)
            r1.append((java.lang.String) r11)
            java.lang.String r1 = r1.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0099:
            r5 = 1
            goto L_0x00bf
        L_0x009b:
            if (r2 != r15) goto L_0x00be
            char r5 = (char) r7
            int r6 = r13.indexOf((int) r5)
            if (r6 != r14) goto L_0x00a5
            goto L_0x00be
        L_0x00a5:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Character '"
            r1.<init>((java.lang.String) r2)
            r1.append((char) r5)
            java.lang.String r2 = "' in AVA appears without escape"
            r1.append((java.lang.String) r2)
            java.lang.String r1 = r1.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x00be:
            r5 = 0
        L_0x00bf:
            int r6 = r4.size()
            java.lang.String r10 = " "
            if (r6 <= 0) goto L_0x00db
            r6 = 0
        L_0x00c8:
            if (r6 >= r9) goto L_0x00d0
            r3.append((java.lang.String) r10)
            int r6 = r6 + 1
            goto L_0x00c8
        L_0x00d0:
            java.lang.String r6 = getEmbeddedHexString(r4)
            r3.append((java.lang.String) r6)
            r4.clear()
            r9 = 0
        L_0x00db:
            char r6 = (char) r7
            boolean r11 = sun.security.util.DerValue.isPrintableStringChar(r6)
            r8 = r8 & r11
            r11 = 32
            if (r7 != r11) goto L_0x00ea
            if (r5 != 0) goto L_0x00ea
            int r9 = r9 + 1
            goto L_0x00f7
        L_0x00ea:
            r5 = 0
        L_0x00eb:
            if (r5 >= r9) goto L_0x00f3
            r3.append((java.lang.String) r10)
            int r5 = r5 + 1
            goto L_0x00eb
        L_0x00f3:
            r3.append((char) r6)
            r9 = 0
        L_0x00f7:
            int r5 = r17.read()
            r7 = r5
        L_0x00fc:
            boolean r5 = isTerminator(r7, r2)
            if (r5 == 0) goto L_0x015c
            if (r2 != r15) goto L_0x010f
            if (r9 > 0) goto L_0x0107
            goto L_0x010f
        L_0x0107:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "Incorrect AVA RFC2253 format - trailing space must be escaped"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x010f:
            int r1 = r4.size()
            if (r1 <= 0) goto L_0x011f
            java.lang.String r1 = getEmbeddedHexString(r4)
            r3.append((java.lang.String) r1)
            r4.clear()
        L_0x011f:
            sun.security.util.ObjectIdentifier r1 = r0.oid
            sun.security.util.ObjectIdentifier r2 = sun.security.pkcs.PKCS9Attribute.EMAIL_ADDRESS_OID
            boolean r1 = r1.equals((java.lang.Object) r2)
            if (r1 != 0) goto L_0x0150
            sun.security.util.ObjectIdentifier r0 = r0.oid
            sun.security.util.ObjectIdentifier r1 = sun.security.x509.X500Name.DOMAIN_COMPONENT_OID
            boolean r0 = r0.equals((java.lang.Object) r1)
            if (r0 == 0) goto L_0x0138
            boolean r0 = PRESERVE_OLD_DC_ENCODING
            if (r0 != 0) goto L_0x0138
            goto L_0x0150
        L_0x0138:
            if (r8 == 0) goto L_0x0144
            sun.security.util.DerValue r0 = new sun.security.util.DerValue
            java.lang.String r1 = r20.toString()
            r0.<init>((java.lang.String) r1)
            return r0
        L_0x0144:
            sun.security.util.DerValue r0 = new sun.security.util.DerValue
            r1 = 12
            java.lang.String r2 = r20.toString()
            r0.<init>((byte) r1, (java.lang.String) r2)
            return r0
        L_0x0150:
            sun.security.util.DerValue r0 = new sun.security.util.DerValue
            r1 = 22
            java.lang.String r2 = r20.toString()
            r0.<init>((byte) r1, (java.lang.String) r2)
            return r0
        L_0x015c:
            r5 = 1
            r10 = 0
            goto L_0x0013
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.AVA.parseString(java.io.Reader, int, int, java.lang.StringBuilder):sun.security.util.DerValue");
    }

    private static Byte getEmbeddedHexPair(int i, Reader reader) throws IOException {
        char c = (char) i;
        if (hexDigits.indexOf((int) Character.toUpperCase(c)) < 0) {
            return null;
        }
        char readChar = (char) readChar(reader, "unexpected EOF - escaped hex value must include two valid digits");
        if (hexDigits.indexOf((int) Character.toUpperCase(readChar)) >= 0) {
            return new Byte((byte) ((Character.digit(c, 16) << 4) + Character.digit(readChar, 16)));
        }
        throw new IOException("escaped hex value must include two valid digits");
    }

    private static String getEmbeddedHexString(List<Byte> list) throws IOException {
        int size = list.size();
        byte[] bArr = new byte[size];
        for (int i = 0; i < size; i++) {
            bArr[i] = list.get(i).byteValue();
        }
        return new String(bArr, "UTF8");
    }

    private static int readChar(Reader reader, String str) throws IOException {
        int read = reader.read();
        if (read != -1) {
            return read;
        }
        throw new IOException(str);
    }

    private static boolean trailingSpace(Reader reader) throws IOException {
        boolean z = true;
        if (!reader.markSupported()) {
            return true;
        }
        reader.mark(9999);
        while (true) {
            int read = reader.read();
            if (read == -1) {
                break;
            } else if (read != 32 && (read != 92 || reader.read() != 32)) {
                z = false;
            }
        }
        z = false;
        reader.reset();
        return z;
    }

    AVA(DerValue derValue) throws IOException {
        if (derValue.tag == 48) {
            this.oid = X500Name.intern(derValue.data.getOID());
            this.value = derValue.data.getDerValue();
            if (derValue.data.available() != 0) {
                throw new IOException("AVA, extra bytes = " + derValue.data.available());
            }
            return;
        }
        throw new IOException("AVA not a sequence");
    }

    AVA(DerInputStream derInputStream) throws IOException {
        this(derInputStream.getDerValue());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AVA)) {
            return false;
        }
        return toRFC2253CanonicalString().equals(((AVA) obj).toRFC2253CanonicalString());
    }

    public int hashCode() {
        return toRFC2253CanonicalString().hashCode();
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derEncode(derOutputStream);
    }

    public void derEncode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.putOID(this.oid);
        this.value.encode(derOutputStream);
        derOutputStream2.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream2.toByteArray());
    }

    private String toKeyword(int i, Map<String, String> map) {
        return AVAKeyword.getKeyword(this.oid, i, map);
    }

    public String toString() {
        return toKeywordValueString(toKeyword(1, Collections.emptyMap()));
    }

    public String toRFC1779String() {
        return toRFC1779String(Collections.emptyMap());
    }

    public String toRFC1779String(Map<String, String> map) {
        return toKeywordValueString(toKeyword(2, map));
    }

    public String toRFC2253String() {
        return toRFC2253String(Collections.emptyMap());
    }

    public String toRFC2253String(Map<String, String> map) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(toKeyword(3, map));
        sb.append('=');
        int i = 0;
        if ((sb.charAt(0) < '0' || sb.charAt(0) > '9') && isDerString(this.value, false)) {
            try {
                String str = new String(this.value.getDataBytes(), "UTF8");
                StringBuilder sb2 = new StringBuilder();
                for (int i2 = 0; i2 < str.length(); i2++) {
                    char charAt = str.charAt(i2);
                    if (DerValue.isPrintableStringChar(charAt) || ",=+<>#;\"\\".indexOf((int) charAt) >= 0) {
                        if (",=+<>#;\"\\".indexOf((int) charAt) >= 0) {
                            sb2.append('\\');
                        }
                        sb2.append(charAt);
                    } else if (charAt == 0) {
                        sb2.append("\\00");
                    } else if (debug == null || !Debug.isOn("ava")) {
                        sb2.append(charAt);
                    } else {
                        try {
                            byte[] bytes = Character.toString(charAt).getBytes("UTF8");
                            for (int i3 = 0; i3 < bytes.length; i3++) {
                                sb2.append('\\');
                                sb2.append(Character.toUpperCase(Character.forDigit((bytes[i3] >>> 4) & 15, 16)));
                                sb2.append(Character.toUpperCase(Character.forDigit(bytes[i3] & 15, 16)));
                            }
                        } catch (IOException unused) {
                            throw new IllegalArgumentException("DER Value conversion");
                        }
                    }
                }
                char[] charArray = sb2.toString().toCharArray();
                StringBuilder sb3 = new StringBuilder();
                int i4 = 0;
                while (i4 < charArray.length && ((r3 = charArray[i4]) == ' ' || r3 == 13)) {
                    i4++;
                }
                int length = charArray.length - 1;
                while (length >= 0) {
                    char c = charArray[length];
                    if (c != ' ' && c != 13) {
                        break;
                    }
                    length--;
                }
                while (i < charArray.length) {
                    char c2 = charArray[i];
                    if (i < i4 || i > length) {
                        sb3.append('\\');
                    }
                    sb3.append(c2);
                    i++;
                }
                sb.append(sb3.toString());
            } catch (IOException unused2) {
                throw new IllegalArgumentException("DER Value conversion");
            }
        } else {
            try {
                byte[] byteArray = this.value.toByteArray();
                sb.append('#');
                while (i < byteArray.length) {
                    byte b = byteArray[i];
                    sb.append(Character.forDigit((b >>> 4) & 15, 16));
                    sb.append(Character.forDigit(b & 15, 16));
                    i++;
                }
            } catch (IOException unused3) {
                throw new IllegalArgumentException("DER Value conversion");
            }
        }
        return sb.toString();
    }

    public String toRFC2253CanonicalString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append(toKeyword(3, Collections.emptyMap()));
        sb.append('=');
        if ((sb.charAt(0) < '0' || sb.charAt(0) > '9') && (isDerString(this.value, true) || this.value.tag == 20)) {
            try {
                String str = new String(this.value.getDataBytes(), "UTF8");
                StringBuilder sb2 = new StringBuilder();
                boolean z = false;
                for (int i = 0; i < str.length(); i++) {
                    char charAt = str.charAt(i);
                    if (DerValue.isPrintableStringChar(charAt) || ",+<>;\"\\".indexOf((int) charAt) >= 0 || (i == 0 && charAt == '#')) {
                        if ((i == 0 && charAt == '#') || ",+<>;\"\\".indexOf((int) charAt) >= 0) {
                            sb2.append('\\');
                        }
                        if (!Character.isWhitespace(charAt)) {
                            sb2.append(charAt);
                        } else {
                            if (!z) {
                                sb2.append(charAt);
                                z = true;
                            }
                        }
                    } else if (debug == null || !Debug.isOn("ava")) {
                        sb2.append(charAt);
                    } else {
                        try {
                            byte[] bytes = Character.toString(charAt).getBytes("UTF8");
                            for (int i2 = 0; i2 < bytes.length; i2++) {
                                sb2.append('\\');
                                sb2.append(Character.forDigit((bytes[i2] >>> 4) & 15, 16));
                                sb2.append(Character.forDigit(bytes[i2] & 15, 16));
                            }
                        } catch (IOException unused) {
                            throw new IllegalArgumentException("DER Value conversion");
                        }
                    }
                    z = false;
                }
                sb.append(sb2.toString().trim());
            } catch (IOException unused2) {
                throw new IllegalArgumentException("DER Value conversion");
            }
        } else {
            try {
                byte[] byteArray = this.value.toByteArray();
                sb.append('#');
                for (byte b : byteArray) {
                    sb.append(Character.forDigit((b >>> 4) & 15, 16));
                    sb.append(Character.forDigit(b & 15, 16));
                }
            } catch (IOException unused3) {
                throw new IllegalArgumentException("DER Value conversion");
            }
        }
        return Normalizer.normalize(sb.toString().toUpperCase(Locale.f700US).toLowerCase(Locale.f700US), Normalizer.Form.NFKD);
    }

    private static boolean isDerString(DerValue derValue, boolean z) {
        if (z) {
            byte b = derValue.tag;
            return b == 12 || b == 19;
        }
        byte b2 = derValue.tag;
        return b2 == 12 || b2 == 22 || b2 == 27 || b2 == 30 || b2 == 19 || b2 == 20;
    }

    /* access modifiers changed from: package-private */
    public boolean hasRFC2253Keyword() {
        return AVAKeyword.hasKeyword(this.oid, 3);
    }

    private String toKeywordValueString(String str) {
        char charAt;
        StringBuilder sb = new StringBuilder(40);
        sb.append(str);
        sb.append("=");
        try {
            String asString = this.value.getAsString();
            if (asString == null) {
                byte[] byteArray = this.value.toByteArray();
                sb.append('#');
                for (int i = 0; i < byteArray.length; i++) {
                    sb.append(hexDigits.charAt((byteArray[i] >> 4) & 15));
                    sb.append(hexDigits.charAt(byteArray[i] & 15));
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                int length = asString.length();
                boolean z = length > 1 && asString.charAt(0) == '\"' && asString.charAt(length + -1) == '\"';
                boolean z2 = false;
                boolean z3 = false;
                for (int i2 = 0; i2 < length; i2++) {
                    char charAt2 = asString.charAt(i2);
                    if (!z || !(i2 == 0 || i2 == length - 1)) {
                        if (!DerValue.isPrintableStringChar(charAt2)) {
                            if (",+=\n<>#;\\\"".indexOf((int) charAt2) < 0) {
                                if (debug == null || !Debug.isOn("ava")) {
                                    sb2.append(charAt2);
                                } else {
                                    byte[] bytes = Character.toString(charAt2).getBytes("UTF8");
                                    for (int i3 = 0; i3 < bytes.length; i3++) {
                                        sb2.append('\\');
                                        sb2.append(Character.toUpperCase(Character.forDigit((bytes[i3] >>> 4) & 15, 16)));
                                        sb2.append(Character.toUpperCase(Character.forDigit(bytes[i3] & 15, 16)));
                                    }
                                }
                                z3 = false;
                            }
                        }
                        if (!z2 && ((i2 == 0 && (charAt2 == ' ' || charAt2 == 10)) || ",+=\n<>#;\\\"".indexOf((int) charAt2) >= 0)) {
                            z2 = true;
                        }
                        if (charAt2 == ' ' || charAt2 == 10) {
                            if (!z2 && z3) {
                                z2 = true;
                            }
                            z3 = true;
                        } else {
                            if (charAt2 == '\"' || charAt2 == '\\') {
                                sb2.append('\\');
                            }
                            z3 = false;
                        }
                        sb2.append(charAt2);
                    } else {
                        sb2.append(charAt2);
                    }
                }
                boolean z4 = (sb2.length() <= 0 || !((charAt = sb2.charAt(sb2.length() - 1)) == ' ' || charAt == 10)) ? z2 : true;
                if (z || !z4) {
                    sb.append(sb2.toString());
                } else {
                    sb.append("\"" + sb2.toString() + "\"");
                }
            }
            return sb.toString();
        } catch (IOException unused) {
            throw new IllegalArgumentException("DER Value conversion");
        }
    }
}
