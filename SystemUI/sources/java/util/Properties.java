package java.util;

import java.p026io.BufferedWriter;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.OutputStreamWriter;
import java.p026io.PrintStream;
import java.p026io.PrintWriter;
import java.p026io.Reader;
import java.p026io.Writer;

public class Properties extends Hashtable<Object, Object> {
    private static final char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final long serialVersionUID = 4112578634029874840L;
    protected Properties defaults;

    public Properties() {
        this((Properties) null);
    }

    public Properties(Properties properties) {
        this.defaults = properties;
    }

    public synchronized Object setProperty(String str, String str2) {
        return put(str, str2);
    }

    public synchronized void load(Reader reader) throws IOException {
        load0(new LineReader(reader));
    }

    public synchronized void load(InputStream inputStream) throws IOException {
        load0(new LineReader(inputStream));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0039, code lost:
        r8 = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void load0(java.util.Properties.LineReader r12) throws java.p026io.IOException {
        /*
            r11 = this;
            r0 = 1024(0x400, float:1.435E-42)
            char[] r0 = new char[r0]
        L_0x0004:
            int r1 = r12.readLine()
            if (r1 < 0) goto L_0x0061
            r2 = 0
            r3 = r2
            r4 = r3
        L_0x000d:
            r5 = 58
            r6 = 61
            r7 = 1
            if (r3 >= r1) goto L_0x0038
            char[] r8 = r12.lineBuf
            char r8 = r8[r3]
            if (r8 == r6) goto L_0x001c
            if (r8 != r5) goto L_0x0022
        L_0x001c:
            if (r4 != 0) goto L_0x0022
            int r4 = r3 + 1
            r8 = r7
            goto L_0x003a
        L_0x0022:
            boolean r9 = java.lang.Character.isWhitespace((char) r8)
            if (r9 == 0) goto L_0x002d
            if (r4 != 0) goto L_0x002d
            int r4 = r3 + 1
            goto L_0x0039
        L_0x002d:
            r5 = 92
            if (r8 != r5) goto L_0x0034
            r4 = r4 ^ 1
            goto L_0x0035
        L_0x0034:
            r4 = r2
        L_0x0035:
            int r3 = r3 + 1
            goto L_0x000d
        L_0x0038:
            r4 = r1
        L_0x0039:
            r8 = r2
        L_0x003a:
            if (r4 >= r1) goto L_0x0050
            char[] r9 = r12.lineBuf
            char r9 = r9[r4]
            boolean r10 = java.lang.Character.isWhitespace((char) r9)
            if (r10 != 0) goto L_0x004d
            if (r8 != 0) goto L_0x0050
            if (r9 == r6) goto L_0x004c
            if (r9 != r5) goto L_0x0050
        L_0x004c:
            r8 = r7
        L_0x004d:
            int r4 = r4 + 1
            goto L_0x003a
        L_0x0050:
            char[] r5 = r12.lineBuf
            java.lang.String r2 = r11.loadConvert(r5, r2, r3, r0)
            char[] r3 = r12.lineBuf
            int r1 = r1 - r4
            java.lang.String r1 = r11.loadConvert(r3, r4, r1, r0)
            r11.put(r2, r1)
            goto L_0x0004
        L_0x0061:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Properties.load0(java.util.Properties$LineReader):void");
    }

    class LineReader {
        byte[] inByteBuf;
        char[] inCharBuf;
        int inLimit = 0;
        int inOff = 0;
        InputStream inStream;
        char[] lineBuf = new char[1024];
        Reader reader;

        public LineReader(InputStream inputStream) {
            this.inStream = inputStream;
            this.inByteBuf = new byte[8192];
        }

        public LineReader(Reader reader2) {
            this.reader = reader2;
            this.inCharBuf = new char[8192];
        }

        /* access modifiers changed from: package-private */
        public int readLine() throws IOException {
            char c;
            int i;
            int i2;
            boolean z = false;
            int i3 = 0;
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            while (true) {
                boolean z5 = true;
                boolean z6 = true;
                while (true) {
                    if (this.inOff >= this.inLimit) {
                        InputStream inputStream = this.inStream;
                        if (inputStream == null) {
                            i2 = this.reader.read(this.inCharBuf);
                        } else {
                            i2 = inputStream.read(this.inByteBuf);
                        }
                        this.inLimit = i2;
                        this.inOff = 0;
                        if (i2 <= 0) {
                            if (i3 == 0 || z2) {
                                return -1;
                            }
                            return z4 ? i3 - 1 : i3;
                        }
                    }
                    if (this.inStream != null) {
                        byte[] bArr = this.inByteBuf;
                        int i4 = this.inOff;
                        this.inOff = i4 + 1;
                        c = (char) (bArr[i4] & 255);
                    } else {
                        char[] cArr = this.inCharBuf;
                        int i5 = this.inOff;
                        this.inOff = i5 + 1;
                        c = cArr[i5];
                    }
                    if (z) {
                        z = false;
                        if (c == 10) {
                            continue;
                        }
                    }
                    if (z5) {
                        if (!Character.isWhitespace(c) && (z3 || !(c == 13 || c == 10))) {
                            z5 = false;
                            z3 = false;
                        }
                    }
                    if (z6) {
                        if (c == '#' || c == '!') {
                            z6 = false;
                            z2 = true;
                        } else {
                            z6 = false;
                        }
                    }
                    if (c != 10 && c != 13) {
                        char[] cArr2 = this.lineBuf;
                        int i6 = i3 + 1;
                        cArr2[i3] = c;
                        if (i6 == cArr2.length) {
                            int length = cArr2.length * 2;
                            if (length < 0) {
                                length = Integer.MAX_VALUE;
                            }
                            char[] cArr3 = new char[length];
                            System.arraycopy((Object) cArr2, 0, (Object) cArr3, 0, cArr2.length);
                            this.lineBuf = cArr3;
                        }
                        z4 = c == '\\' ? !z4 : false;
                        i3 = i6;
                    } else if (z2 || i3 == 0) {
                        i3 = 0;
                        z2 = false;
                    } else {
                        if (this.inOff >= this.inLimit) {
                            InputStream inputStream2 = this.inStream;
                            if (inputStream2 == null) {
                                i = this.reader.read(this.inCharBuf);
                            } else {
                                i = inputStream2.read(this.inByteBuf);
                            }
                            this.inLimit = i;
                            this.inOff = 0;
                            if (i <= 0) {
                                return z4 ? i3 - 1 : i3;
                            }
                        }
                        if (!z4) {
                            return i3;
                        }
                        i3--;
                        z4 = false;
                        if (c == 13) {
                            z = true;
                            z5 = true;
                        } else {
                            z5 = true;
                        }
                        z3 = z5;
                    }
                }
                i3 = 0;
                z2 = false;
            }
        }
    }

    private String loadConvert(char[] cArr, int i, int i2, char[] cArr2) {
        int i3;
        if (cArr2.length < i2) {
            int i4 = i2 * 2;
            if (i4 < 0) {
                i4 = Integer.MAX_VALUE;
            }
            cArr2 = new char[i4];
        }
        int i5 = i2 + i;
        int i6 = 0;
        while (i < i5) {
            int i7 = i + 1;
            char c = cArr[i];
            if (c == '\\') {
                i = i7 + 1;
                char c2 = cArr[i7];
                if (c2 == 'u') {
                    int i8 = 0;
                    int i9 = 0;
                    while (i8 < 4) {
                        int i10 = i + 1;
                        char c3 = cArr[i];
                        switch (c3) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                i9 = ((i9 << 4) + c3) - 48;
                                break;
                            default:
                                switch (c3) {
                                    case 'A':
                                    case 'B':
                                    case 'C':
                                    case 'D':
                                    case 'E':
                                    case 'F':
                                        i9 = (((i9 << 4) + 10) + c3) - 65;
                                        break;
                                    default:
                                        switch (c3) {
                                            case 'a':
                                            case 'b':
                                            case 'c':
                                            case 'd':
                                            case 'e':
                                            case 'f':
                                                i9 = (((i9 << 4) + 10) + c3) - 97;
                                                break;
                                            default:
                                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                                        }
                                }
                        }
                        i8++;
                        i = i10;
                    }
                    cArr2[i6] = (char) i9;
                    i6++;
                } else {
                    if (c2 == 't') {
                        c2 = 9;
                    } else if (c2 == 'r') {
                        c2 = 13;
                    } else if (c2 == 'n') {
                        c2 = 10;
                    } else if (c2 == 'f') {
                        c2 = 12;
                    }
                    i3 = i6 + 1;
                    cArr2[i6] = c2;
                }
            } else {
                i3 = i6 + 1;
                cArr2[i6] = c;
                i = i7;
            }
            i6 = i3;
        }
        return new String(cArr2, 0, i6);
    }

    private String saveConvert(String str, boolean z, boolean z2) {
        int length = str.length();
        int i = length * 2;
        if (i < 0) {
            i = Integer.MAX_VALUE;
        }
        StringBuffer stringBuffer = new StringBuffer(i);
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt <= '=' || charAt >= 127) {
                if (charAt == 9) {
                    stringBuffer.append("\\t");
                } else if (charAt == 10) {
                    stringBuffer.append("\\n");
                } else if (charAt == 12) {
                    stringBuffer.append("\\f");
                } else if (charAt == 13) {
                    stringBuffer.append("\\r");
                } else if (charAt == ' ') {
                    if (i2 == 0 || z) {
                        stringBuffer.append('\\');
                    }
                    stringBuffer.append(' ');
                } else if (charAt == '!' || charAt == '#' || charAt == ':' || charAt == '=') {
                    stringBuffer.append('\\');
                    stringBuffer.append(charAt);
                } else {
                    if ((charAt < ' ' || charAt > '~') && z2) {
                        stringBuffer.append("\\u");
                        stringBuffer.append(toHex((charAt >> 12) & 15));
                        stringBuffer.append(toHex((charAt >> 8) & 15));
                        stringBuffer.append(toHex((charAt >> 4) & 15));
                        stringBuffer.append(toHex(charAt & 15));
                    } else {
                        stringBuffer.append(charAt);
                    }
                }
            } else if (charAt == '\\') {
                stringBuffer.append("\\\\");
            } else {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x008d, code lost:
        if (r11.charAt(r3) != '!') goto L_0x008f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void writeComments(java.p026io.BufferedWriter r10, java.lang.String r11) throws java.p026io.IOException {
        /*
            java.lang.String r0 = "#"
            r10.write((java.lang.String) r0)
            int r1 = r11.length()
            r2 = 6
            char[] r2 = new char[r2]
            r3 = 92
            r4 = 0
            r2[r4] = r3
            r3 = 117(0x75, float:1.64E-43)
            r5 = 1
            r2[r5] = r3
            r3 = r4
        L_0x0017:
            if (r4 >= r1) goto L_0x0096
            char r6 = r11.charAt(r4)
            r7 = 13
            r8 = 10
            r9 = 255(0xff, float:3.57E-43)
            if (r6 > r9) goto L_0x0029
            if (r6 == r8) goto L_0x0029
            if (r6 != r7) goto L_0x0094
        L_0x0029:
            if (r3 == r4) goto L_0x0032
            java.lang.String r3 = r11.substring(r3, r4)
            r10.write((java.lang.String) r3)
        L_0x0032:
            if (r6 <= r9) goto L_0x0067
            int r3 = r6 >> 12
            r3 = r3 & 15
            char r3 = toHex(r3)
            r7 = 2
            r2[r7] = r3
            int r3 = r6 >> 8
            r3 = r3 & 15
            char r3 = toHex(r3)
            r7 = 3
            r2[r7] = r3
            int r3 = r6 >> 4
            r3 = r3 & 15
            char r3 = toHex(r3)
            r7 = 4
            r2[r7] = r3
            r3 = r6 & 15
            char r3 = toHex(r3)
            r6 = 5
            r2[r6] = r3
            java.lang.String r3 = new java.lang.String
            r3.<init>((char[]) r2)
            r10.write((java.lang.String) r3)
            goto L_0x0092
        L_0x0067:
            r10.newLine()
            if (r6 != r7) goto L_0x0079
            int r3 = r1 + -1
            if (r4 == r3) goto L_0x0079
            int r3 = r4 + 1
            char r6 = r11.charAt(r3)
            if (r6 != r8) goto L_0x0079
            r4 = r3
        L_0x0079:
            int r3 = r1 + -1
            if (r4 == r3) goto L_0x008f
            int r3 = r4 + 1
            char r6 = r11.charAt(r3)
            r7 = 35
            if (r6 == r7) goto L_0x0092
            char r3 = r11.charAt(r3)
            r6 = 33
            if (r3 == r6) goto L_0x0092
        L_0x008f:
            r10.write((java.lang.String) r0)
        L_0x0092:
            int r3 = r4 + 1
        L_0x0094:
            int r4 = r4 + r5
            goto L_0x0017
        L_0x0096:
            if (r3 == r4) goto L_0x009f
            java.lang.String r11 = r11.substring(r3, r4)
            r10.write((java.lang.String) r11)
        L_0x009f:
            r10.newLine()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Properties.writeComments(java.io.BufferedWriter, java.lang.String):void");
    }

    @Deprecated
    public void save(OutputStream outputStream, String str) {
        try {
            store(outputStream, str);
        } catch (IOException unused) {
        }
    }

    public void store(Writer writer, String str) throws IOException {
        BufferedWriter bufferedWriter;
        if (writer instanceof BufferedWriter) {
            bufferedWriter = (BufferedWriter) writer;
        } else {
            bufferedWriter = new BufferedWriter(writer);
        }
        store0(bufferedWriter, str, false);
    }

    public void store(OutputStream outputStream, String str) throws IOException {
        store0(new BufferedWriter(new OutputStreamWriter(outputStream, "8859_1")), str, true);
    }

    private void store0(BufferedWriter bufferedWriter, String str, boolean z) throws IOException {
        if (str != null) {
            writeComments(bufferedWriter, str);
        }
        bufferedWriter.write("#" + new Date().toString());
        bufferedWriter.newLine();
        synchronized (this) {
            Enumeration keys = keys();
            while (keys.hasMoreElements()) {
                String str2 = (String) keys.nextElement();
                String saveConvert = saveConvert(str2, true, z);
                String saveConvert2 = saveConvert((String) get(str2), false, z);
                bufferedWriter.write(saveConvert + "=" + saveConvert2);
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.flush();
    }

    public synchronized void loadFromXML(InputStream inputStream) throws IOException, InvalidPropertiesFormatException {
        XMLUtils.load(this, (InputStream) Objects.requireNonNull(inputStream));
        inputStream.close();
    }

    public void storeToXML(OutputStream outputStream, String str) throws IOException {
        storeToXML(outputStream, str, "UTF-8");
    }

    public void storeToXML(OutputStream outputStream, String str, String str2) throws IOException {
        XMLUtils.save(this, (OutputStream) Objects.requireNonNull(outputStream), str, (String) Objects.requireNonNull(str2));
    }

    public String getProperty(String str) {
        Properties properties;
        Object obj = super.get(str);
        String str2 = obj instanceof String ? (String) obj : null;
        return (str2 != null || (properties = this.defaults) == null) ? str2 : properties.getProperty(str);
    }

    public String getProperty(String str, String str2) {
        String property = getProperty(str);
        return property == null ? str2 : property;
    }

    public Enumeration<?> propertyNames() {
        Hashtable hashtable = new Hashtable();
        enumerate(hashtable);
        return hashtable.keys();
    }

    public Set<String> stringPropertyNames() {
        Hashtable hashtable = new Hashtable();
        enumerateStringProperties(hashtable);
        return hashtable.keySet();
    }

    public void list(PrintStream printStream) {
        printStream.println("-- listing properties --");
        Hashtable hashtable = new Hashtable();
        enumerate(hashtable);
        Enumeration keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            String str = (String) keys.nextElement();
            String str2 = (String) hashtable.get(str);
            if (str2.length() > 40) {
                str2 = str2.substring(0, 37) + "...";
            }
            printStream.println(str + "=" + str2);
        }
    }

    public void list(PrintWriter printWriter) {
        printWriter.println("-- listing properties --");
        Hashtable hashtable = new Hashtable();
        enumerate(hashtable);
        Enumeration keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            String str = (String) keys.nextElement();
            String str2 = (String) hashtable.get(str);
            if (str2.length() > 40) {
                str2 = str2.substring(0, 37) + "...";
            }
            printWriter.println(str + "=" + str2);
        }
    }

    private synchronized void enumerate(Hashtable<String, Object> hashtable) {
        Properties properties = this.defaults;
        if (properties != null) {
            properties.enumerate(hashtable);
        }
        Enumeration keys = keys();
        while (keys.hasMoreElements()) {
            String str = (String) keys.nextElement();
            hashtable.put(str, get(str));
        }
    }

    private synchronized void enumerateStringProperties(Hashtable<String, String> hashtable) {
        Properties properties = this.defaults;
        if (properties != null) {
            properties.enumerateStringProperties(hashtable);
        }
        Enumeration keys = keys();
        while (keys.hasMoreElements()) {
            Object nextElement = keys.nextElement();
            Object obj = get(nextElement);
            if ((nextElement instanceof String) && (obj instanceof String)) {
                hashtable.put((String) nextElement, (String) obj);
            }
        }
    }

    private static char toHex(int i) {
        return hexDigit[i & 15];
    }
}
