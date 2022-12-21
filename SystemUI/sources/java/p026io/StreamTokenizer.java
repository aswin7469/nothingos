package java.p026io;

/* renamed from: java.io.StreamTokenizer */
public class StreamTokenizer {
    private static final byte CT_ALPHA = 4;
    private static final byte CT_COMMENT = 16;
    private static final byte CT_DIGIT = 2;
    private static final byte CT_QUOTE = 8;
    private static final byte CT_WHITESPACE = 1;
    private static final int NEED_CHAR = Integer.MAX_VALUE;
    private static final int SKIP_LF = 2147483646;
    public static final int TT_EOF = -1;
    public static final int TT_EOL = 10;
    private static final int TT_NOTHING = -4;
    public static final int TT_NUMBER = -2;
    public static final int TT_WORD = -3;
    private int LINENO;
    private char[] buf;
    private byte[] ctype;
    private boolean eolIsSignificantP;
    private boolean forceLower;
    private InputStream input;
    public double nval;
    private int peekc;
    private boolean pushedBack;
    private Reader reader;
    private boolean slashSlashCommentsP;
    private boolean slashStarCommentsP;
    public String sval;
    public int ttype;

    private StreamTokenizer() {
        this.reader = null;
        this.input = null;
        this.buf = new char[20];
        this.peekc = Integer.MAX_VALUE;
        this.LINENO = 1;
        this.eolIsSignificantP = false;
        this.slashSlashCommentsP = false;
        this.slashStarCommentsP = false;
        this.ctype = new byte[256];
        this.ttype = -4;
        wordChars(97, 122);
        wordChars(65, 90);
        wordChars(160, 255);
        whitespaceChars(0, 32);
        commentChar(47);
        quoteChar(34);
        quoteChar(39);
        parseNumbers();
    }

    @Deprecated
    public StreamTokenizer(InputStream inputStream) {
        this();
        inputStream.getClass();
        this.input = inputStream;
    }

    public StreamTokenizer(Reader reader2) {
        this();
        reader2.getClass();
        this.reader = reader2;
    }

    public void resetSyntax() {
        int length = this.ctype.length;
        while (true) {
            length--;
            if (length >= 0) {
                this.ctype[length] = 0;
            } else {
                return;
            }
        }
    }

    public void wordChars(int i, int i2) {
        if (i < 0) {
            i = 0;
        }
        byte[] bArr = this.ctype;
        if (i2 >= bArr.length) {
            i2 = bArr.length - 1;
        }
        while (i <= i2) {
            byte[] bArr2 = this.ctype;
            bArr2[i] = (byte) (bArr2[i] | 4);
            i++;
        }
    }

    public void whitespaceChars(int i, int i2) {
        if (i < 0) {
            i = 0;
        }
        byte[] bArr = this.ctype;
        if (i2 >= bArr.length) {
            i2 = bArr.length - 1;
        }
        while (i <= i2) {
            this.ctype[i] = 1;
            i++;
        }
    }

    public void ordinaryChars(int i, int i2) {
        if (i < 0) {
            i = 0;
        }
        byte[] bArr = this.ctype;
        if (i2 >= bArr.length) {
            i2 = bArr.length - 1;
        }
        while (i <= i2) {
            this.ctype[i] = 0;
            i++;
        }
    }

    public void ordinaryChar(int i) {
        if (i >= 0) {
            byte[] bArr = this.ctype;
            if (i < bArr.length) {
                bArr[i] = 0;
            }
        }
    }

    public void commentChar(int i) {
        if (i >= 0) {
            byte[] bArr = this.ctype;
            if (i < bArr.length) {
                bArr[i] = 16;
            }
        }
    }

    public void quoteChar(int i) {
        if (i >= 0) {
            byte[] bArr = this.ctype;
            if (i < bArr.length) {
                bArr[i] = 8;
            }
        }
    }

    public void parseNumbers() {
        for (int i = 48; i <= 57; i++) {
            byte[] bArr = this.ctype;
            bArr[i] = (byte) (bArr[i] | 2);
        }
        byte[] bArr2 = this.ctype;
        bArr2[46] = (byte) (bArr2[46] | 2);
        bArr2[45] = (byte) (bArr2[45] | 2);
    }

    public void eolIsSignificant(boolean z) {
        this.eolIsSignificantP = z;
    }

    public void slashStarComments(boolean z) {
        this.slashStarCommentsP = z;
    }

    public void slashSlashComments(boolean z) {
        this.slashSlashCommentsP = z;
    }

    public void lowerCaseMode(boolean z) {
        this.forceLower = z;
    }

    private int read() throws IOException {
        Reader reader2 = this.reader;
        if (reader2 != null) {
            return reader2.read();
        }
        InputStream inputStream = this.input;
        if (inputStream != null) {
            return inputStream.read();
        }
        throw new IllegalStateException();
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00d3  */
    public int nextToken() throws java.p026io.IOException {
        /*
            r15 = this;
            boolean r0 = r15.pushedBack
            r1 = 0
            if (r0 == 0) goto L_0x000a
            r15.pushedBack = r1
            int r15 = r15.ttype
            return r15
        L_0x000a:
            byte[] r0 = r15.ctype
            r2 = 0
            r15.sval = r2
            int r2 = r15.peekc
            r3 = 2147483647(0x7fffffff, float:NaN)
            if (r2 >= 0) goto L_0x0017
            r2 = r3
        L_0x0017:
            r4 = 2147483646(0x7ffffffe, float:NaN)
            r5 = -1
            r6 = 10
            if (r2 != r4) goto L_0x002b
            int r2 = r15.read()
            if (r2 >= 0) goto L_0x0028
            r15.ttype = r5
            return r5
        L_0x0028:
            if (r2 != r6) goto L_0x002b
            r2 = r3
        L_0x002b:
            if (r2 != r3) goto L_0x0036
            int r2 = r15.read()
            if (r2 >= 0) goto L_0x0036
            r15.ttype = r5
            return r5
        L_0x0036:
            r15.ttype = r2
            r15.peekc = r3
            r7 = 256(0x100, float:3.59E-43)
            r8 = 4
            if (r2 >= r7) goto L_0x0042
            byte r9 = r0[r2]
            goto L_0x0043
        L_0x0042:
            r9 = r8
        L_0x0043:
            r10 = r9 & 1
            r11 = 13
            r12 = 1
            if (r10 == 0) goto L_0x0081
            if (r2 != r11) goto L_0x0065
            int r2 = r15.LINENO
            int r2 = r2 + r12
            r15.LINENO = r2
            boolean r2 = r15.eolIsSignificantP
            if (r2 == 0) goto L_0x005a
            r15.peekc = r4
            r15.ttype = r6
            return r6
        L_0x005a:
            int r2 = r15.read()
            if (r2 != r6) goto L_0x0077
            int r2 = r15.read()
            goto L_0x0077
        L_0x0065:
            if (r2 != r6) goto L_0x0073
            int r2 = r15.LINENO
            int r2 = r2 + r12
            r15.LINENO = r2
            boolean r2 = r15.eolIsSignificantP
            if (r2 == 0) goto L_0x0073
            r15.ttype = r6
            return r6
        L_0x0073:
            int r2 = r15.read()
        L_0x0077:
            if (r2 >= 0) goto L_0x007c
            r15.ttype = r5
            return r5
        L_0x007c:
            if (r2 >= r7) goto L_0x0042
            byte r9 = r0[r2]
            goto L_0x0043
        L_0x0081:
            r4 = r9 & 2
            r10 = 48
            if (r4 == 0) goto L_0x00d3
            r0 = 57
            r3 = 46
            r4 = 45
            if (r2 != r4) goto L_0x00a0
            int r2 = r15.read()
            if (r2 == r3) goto L_0x009e
            if (r2 < r10) goto L_0x0099
            if (r2 <= r0) goto L_0x009e
        L_0x0099:
            r15.peekc = r2
            r15.ttype = r4
            return r4
        L_0x009e:
            r4 = r12
            goto L_0x00a1
        L_0x00a0:
            r4 = r1
        L_0x00a1:
            r6 = 0
            r7 = r6
            r6 = r2
            r2 = r1
        L_0x00a6:
            if (r6 != r3) goto L_0x00ac
            if (r1 != 0) goto L_0x00ac
            r1 = r12
            goto L_0x00b8
        L_0x00ac:
            r13 = 4621819117588971520(0x4024000000000000, double:10.0)
            if (r10 > r6) goto L_0x00bd
            if (r6 > r0) goto L_0x00bd
            double r7 = r7 * r13
            int r6 = r6 + -48
            double r13 = (double) r6
            double r7 = r7 + r13
            int r2 = r2 + r1
        L_0x00b8:
            int r6 = r15.read()
            goto L_0x00a6
        L_0x00bd:
            r15.peekc = r6
            if (r2 == 0) goto L_0x00ca
            int r2 = r2 + r5
            r0 = r13
        L_0x00c3:
            if (r2 <= 0) goto L_0x00c9
            double r0 = r0 * r13
            int r2 = r2 + -1
            goto L_0x00c3
        L_0x00c9:
            double r7 = r7 / r0
        L_0x00ca:
            if (r4 == 0) goto L_0x00cd
            double r7 = -r7
        L_0x00cd:
            r15.nval = r7
            r0 = -2
            r15.ttype = r0
            return r0
        L_0x00d3:
            r4 = r9 & 4
            if (r4 == 0) goto L_0x0119
            r3 = r1
        L_0x00d8:
            char[] r4 = r15.buf
            int r5 = r4.length
            if (r3 < r5) goto L_0x00e6
            int r5 = r4.length
            int r5 = r5 * 2
            char[] r4 = java.util.Arrays.copyOf((char[]) r4, (int) r5)
            r15.buf = r4
        L_0x00e6:
            char[] r4 = r15.buf
            int r5 = r3 + 1
            char r2 = (char) r2
            r4[r3] = r2
            int r2 = r15.read()
            if (r2 >= 0) goto L_0x00f5
            r3 = r12
            goto L_0x00fb
        L_0x00f5:
            if (r2 >= r7) goto L_0x00fa
            byte r3 = r0[r2]
            goto L_0x00fb
        L_0x00fa:
            r3 = r8
        L_0x00fb:
            r3 = r3 & 6
            if (r3 != 0) goto L_0x0117
            r15.peekc = r2
            char[] r0 = r15.buf
            java.lang.String r0 = java.lang.String.copyValueOf(r0, r1, r5)
            r15.sval = r0
            boolean r1 = r15.forceLower
            if (r1 == 0) goto L_0x0113
            java.lang.String r0 = r0.toLowerCase()
            r15.sval = r0
        L_0x0113:
            r0 = -3
            r15.ttype = r0
            return r0
        L_0x0117:
            r3 = r5
            goto L_0x00d8
        L_0x0119:
            r4 = r9 & 8
            if (r4 == 0) goto L_0x01c7
            r15.ttype = r2
            int r0 = r15.read()
            r2 = r1
        L_0x0124:
            if (r0 < 0) goto L_0x01b4
            int r4 = r15.ttype
            if (r0 == r4) goto L_0x01b4
            if (r0 == r6) goto L_0x01b4
            if (r0 == r11) goto L_0x01b4
            r4 = 92
            if (r0 != r4) goto L_0x0197
            int r0 = r15.read()
            if (r0 < r10) goto L_0x0163
            r4 = 55
            if (r0 > r4) goto L_0x0163
            int r5 = r0 + -48
            int r7 = r15.read()
            if (r10 > r7) goto L_0x0161
            if (r7 > r4) goto L_0x0161
            int r5 = r5 << 3
            int r7 = r7 + -48
            int r5 = r5 + r7
            int r7 = r15.read()
            if (r10 > r7) goto L_0x0161
            if (r7 > r4) goto L_0x0161
            r4 = 51
            if (r0 > r4) goto L_0x0161
            int r0 = r5 << 3
            int r7 = r7 + -48
            int r5 = r0 + r7
            int r7 = r15.read()
        L_0x0161:
            r0 = r5
            goto L_0x0195
        L_0x0163:
            r4 = 97
            if (r0 == r4) goto L_0x0190
            r4 = 98
            if (r0 == r4) goto L_0x018d
            r4 = 102(0x66, float:1.43E-43)
            if (r0 == r4) goto L_0x018a
            r4 = 110(0x6e, float:1.54E-43)
            if (r0 == r4) goto L_0x0188
            r4 = 114(0x72, float:1.6E-43)
            if (r0 == r4) goto L_0x0186
            r4 = 116(0x74, float:1.63E-43)
            if (r0 == r4) goto L_0x0183
            r4 = 118(0x76, float:1.65E-43)
            if (r0 == r4) goto L_0x0180
            goto L_0x0191
        L_0x0180:
            r0 = 11
            goto L_0x0191
        L_0x0183:
            r0 = 9
            goto L_0x0191
        L_0x0186:
            r0 = r11
            goto L_0x0191
        L_0x0188:
            r0 = r6
            goto L_0x0191
        L_0x018a:
            r0 = 12
            goto L_0x0191
        L_0x018d:
            r0 = 8
            goto L_0x0191
        L_0x0190:
            r0 = 7
        L_0x0191:
            int r7 = r15.read()
        L_0x0195:
            r4 = r7
            goto L_0x019b
        L_0x0197:
            int r4 = r15.read()
        L_0x019b:
            char[] r5 = r15.buf
            int r7 = r5.length
            if (r2 < r7) goto L_0x01a9
            int r7 = r5.length
            int r7 = r7 * 2
            char[] r5 = java.util.Arrays.copyOf((char[]) r5, (int) r7)
            r15.buf = r5
        L_0x01a9:
            char[] r5 = r15.buf
            int r7 = r2 + 1
            char r0 = (char) r0
            r5[r2] = r0
            r0 = r4
            r2 = r7
            goto L_0x0124
        L_0x01b4:
            int r4 = r15.ttype
            if (r0 != r4) goto L_0x01b9
            goto L_0x01ba
        L_0x01b9:
            r3 = r0
        L_0x01ba:
            r15.peekc = r3
            char[] r0 = r15.buf
            java.lang.String r0 = java.lang.String.copyValueOf(r0, r1, r2)
            r15.sval = r0
            int r15 = r15.ttype
            return r15
        L_0x01c7:
            r3 = 47
            if (r2 != r3) goto L_0x0245
            boolean r4 = r15.slashSlashCommentsP
            if (r4 != 0) goto L_0x01d3
            boolean r4 = r15.slashStarCommentsP
            if (r4 == 0) goto L_0x0245
        L_0x01d3:
            int r2 = r15.read()
            r4 = 42
            if (r2 != r4) goto L_0x0210
            boolean r7 = r15.slashStarCommentsP
            if (r7 == 0) goto L_0x0210
        L_0x01df:
            int r0 = r15.read()
            if (r0 != r3) goto L_0x01ed
            if (r1 == r4) goto L_0x01e8
            goto L_0x01ed
        L_0x01e8:
            int r15 = r15.nextToken()
            return r15
        L_0x01ed:
            if (r0 != r11) goto L_0x01ff
            int r0 = r15.LINENO
            int r0 = r0 + r12
            r15.LINENO = r0
            int r0 = r15.read()
            if (r0 != r6) goto L_0x020a
            int r0 = r15.read()
            goto L_0x020a
        L_0x01ff:
            if (r0 != r6) goto L_0x020a
            int r0 = r15.LINENO
            int r0 = r0 + r12
            r15.LINENO = r0
            int r0 = r15.read()
        L_0x020a:
            r1 = r0
            if (r1 >= 0) goto L_0x01df
            r15.ttype = r5
            return r5
        L_0x0210:
            if (r2 != r3) goto L_0x0228
            boolean r1 = r15.slashSlashCommentsP
            if (r1 == 0) goto L_0x0228
        L_0x0216:
            int r0 = r15.read()
            if (r0 == r6) goto L_0x0221
            if (r0 == r11) goto L_0x0221
            if (r0 < 0) goto L_0x0221
            goto L_0x0216
        L_0x0221:
            r15.peekc = r0
            int r15 = r15.nextToken()
            return r15
        L_0x0228:
            byte r0 = r0[r3]
            r0 = r0 & 16
            if (r0 == 0) goto L_0x0240
        L_0x022e:
            int r0 = r15.read()
            if (r0 == r6) goto L_0x0239
            if (r0 == r11) goto L_0x0239
            if (r0 < 0) goto L_0x0239
            goto L_0x022e
        L_0x0239:
            r15.peekc = r0
            int r15 = r15.nextToken()
            return r15
        L_0x0240:
            r15.peekc = r2
            r15.ttype = r3
            return r3
        L_0x0245:
            r0 = r9 & 16
            if (r0 == 0) goto L_0x025b
        L_0x0249:
            int r0 = r15.read()
            if (r0 == r6) goto L_0x0254
            if (r0 == r11) goto L_0x0254
            if (r0 < 0) goto L_0x0254
            goto L_0x0249
        L_0x0254:
            r15.peekc = r0
            int r15 = r15.nextToken()
            return r15
        L_0x025b:
            r15.ttype = r2
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.StreamTokenizer.nextToken():int");
    }

    public void pushBack() {
        if (this.ttype != -4) {
            this.pushedBack = true;
        }
    }

    public int lineno() {
        return this.LINENO;
    }

    public String toString() {
        String str;
        int i = this.ttype;
        if (i == -4) {
            str = "NOTHING";
        } else if (i == -3) {
            str = this.sval;
        } else if (i == -2) {
            str = "n=" + this.nval;
        } else if (i == -1) {
            str = "EOF";
        } else if (i == 10) {
            str = "EOL";
        } else if (i >= 256 || (this.ctype[i] & 8) == 0) {
            char[] cArr = new char[3];
            cArr[2] = '\'';
            cArr[0] = '\'';
            cArr[1] = (char) i;
            str = new String(cArr);
        } else {
            str = this.sval;
        }
        return "Token[" + str + "], line " + this.LINENO;
    }
}
