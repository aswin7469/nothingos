package java.text;

import kotlin.text.Typography;

class PatternEntry {
    static final int RESET = -2;
    static final int UNSET = -1;
    String chars;
    String extension;
    int strength;

    static boolean isSpecialChar(char c) {
        return c == ' ' || (c <= '/' && c >= '\"') || ((c <= '?' && c >= ':') || ((c <= '`' && c >= '[') || (c <= '~' && c >= '{')));
    }

    public void appendQuotedExtension(StringBuffer stringBuffer) {
        appendQuoted(this.extension, stringBuffer);
    }

    public void appendQuotedChars(StringBuffer stringBuffer) {
        appendQuoted(this.chars, stringBuffer);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.chars.equals(((PatternEntry) obj).chars);
    }

    public int hashCode() {
        return this.chars.hashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        addToBuffer(stringBuffer, true, false, (PatternEntry) null);
        return stringBuffer.toString();
    }

    /* access modifiers changed from: package-private */
    public final int getStrength() {
        return this.strength;
    }

    /* access modifiers changed from: package-private */
    public final String getExtension() {
        return this.extension;
    }

    /* access modifiers changed from: package-private */
    public final String getChars() {
        return this.chars;
    }

    /* access modifiers changed from: package-private */
    public void addToBuffer(StringBuffer stringBuffer, boolean z, boolean z2, PatternEntry patternEntry) {
        if (z2 && stringBuffer.length() > 0) {
            if (this.strength == 0 || patternEntry != null) {
                stringBuffer.append(10);
            } else {
                stringBuffer.append(' ');
            }
        }
        if (patternEntry != null) {
            stringBuffer.append((char) Typography.amp);
            if (z2) {
                stringBuffer.append(' ');
            }
            patternEntry.appendQuotedChars(stringBuffer);
            appendQuotedExtension(stringBuffer);
            if (z2) {
                stringBuffer.append(' ');
            }
        }
        int i = this.strength;
        if (i == -2) {
            stringBuffer.append((char) Typography.amp);
        } else if (i == -1) {
            stringBuffer.append('?');
        } else if (i == 0) {
            stringBuffer.append((char) Typography.less);
        } else if (i == 1) {
            stringBuffer.append(';');
        } else if (i == 2) {
            stringBuffer.append(',');
        } else if (i == 3) {
            stringBuffer.append('=');
        }
        if (z2) {
            stringBuffer.append(' ');
        }
        appendQuoted(this.chars, stringBuffer);
        if (z && !this.extension.isEmpty()) {
            stringBuffer.append('/');
            appendQuoted(this.extension, stringBuffer);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0035, code lost:
        if (r1 != '@') goto L_0x0040;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void appendQuoted(java.lang.String r5, java.lang.StringBuffer r6) {
        /*
            r0 = 0
            char r1 = r5.charAt(r0)
            boolean r2 = java.lang.Character.isSpaceChar((char) r1)
            r3 = 1
            r4 = 39
            if (r2 == 0) goto L_0x0013
            r6.append((char) r4)
        L_0x0011:
            r0 = r3
            goto L_0x0040
        L_0x0013:
            boolean r2 = isSpecialChar(r1)
            if (r2 == 0) goto L_0x001d
            r6.append((char) r4)
            goto L_0x0011
        L_0x001d:
            r2 = 9
            if (r1 == r2) goto L_0x003c
            r2 = 10
            if (r1 == r2) goto L_0x003c
            r2 = 12
            if (r1 == r2) goto L_0x003c
            r2 = 13
            if (r1 == r2) goto L_0x003c
            r2 = 16
            if (r1 == r2) goto L_0x003c
            if (r1 == r4) goto L_0x0038
            r2 = 64
            if (r1 == r2) goto L_0x003c
            goto L_0x0040
        L_0x0038:
            r6.append((char) r4)
            goto L_0x0011
        L_0x003c:
            r6.append((char) r4)
            goto L_0x0011
        L_0x0040:
            r6.append((java.lang.String) r5)
            if (r0 == 0) goto L_0x0048
            r6.append((char) r4)
        L_0x0048:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.PatternEntry.appendQuoted(java.lang.String, java.lang.StringBuffer):void");
    }

    PatternEntry(int i, StringBuffer stringBuffer, StringBuffer stringBuffer2) {
        String str = "";
        this.chars = str;
        this.extension = str;
        this.strength = i;
        this.chars = stringBuffer.toString();
        this.extension = stringBuffer2.length() > 0 ? stringBuffer2.toString() : str;
    }

    static class Parser {

        /* renamed from: i */
        private int f377i;
        private StringBuffer newChars = new StringBuffer();
        private StringBuffer newExtension = new StringBuffer();
        private String pattern;

        public Parser(String str) {
            this.pattern = str;
            this.f377i = 0;
        }

        /* JADX WARNING: Removed duplicated region for block: B:65:0x011f A[RETURN] */
        /* JADX WARNING: Removed duplicated region for block: B:66:0x0121  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.text.PatternEntry next() throws java.text.ParseException {
            /*
                r10 = this;
                java.lang.StringBuffer r0 = r10.newChars
                r1 = 0
                r0.setLength(r1)
                java.lang.StringBuffer r0 = r10.newExtension
                r0.setLength(r1)
                r0 = -1
                r2 = 1
                r4 = r0
                r3 = r1
                r5 = r2
            L_0x0010:
                int r6 = r10.f377i
                java.lang.String r7 = r10.pattern
                int r7 = r7.length()
                r8 = 10
                if (r6 >= r7) goto L_0x011d
                java.lang.String r6 = r10.pattern
                int r7 = r10.f377i
                char r6 = r6.charAt(r7)
                r7 = 39
                if (r3 == 0) goto L_0x004c
                if (r6 != r7) goto L_0x002d
                r3 = r1
                goto L_0x0116
            L_0x002d:
                java.lang.StringBuffer r7 = r10.newChars
                int r7 = r7.length()
                if (r7 != 0) goto L_0x003c
                java.lang.StringBuffer r7 = r10.newChars
                r7.append((char) r6)
                goto L_0x0116
            L_0x003c:
                if (r5 == 0) goto L_0x0045
                java.lang.StringBuffer r7 = r10.newChars
                r7.append((char) r6)
                goto L_0x0116
            L_0x0045:
                java.lang.StringBuffer r7 = r10.newExtension
                r7.append((char) r6)
                goto L_0x0116
            L_0x004c:
                r9 = 9
                if (r6 == r9) goto L_0x0116
                if (r6 == r8) goto L_0x0116
                r9 = 12
                if (r6 == r9) goto L_0x0116
                r9 = 13
                if (r6 == r9) goto L_0x0116
                r9 = 32
                if (r6 == r9) goto L_0x0116
                r9 = 44
                if (r6 == r9) goto L_0x0112
                r9 = 47
                if (r6 == r9) goto L_0x0110
                r9 = 38
                if (r6 == r9) goto L_0x010b
                if (r6 == r7) goto L_0x00e3
                switch(r6) {
                    case 59: goto L_0x00de;
                    case 60: goto L_0x00d9;
                    case 61: goto L_0x00d4;
                    default: goto L_0x006f;
                }
            L_0x006f:
                if (r4 != r0) goto L_0x00a1
                java.text.ParseException r0 = new java.text.ParseException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "missing char (=,;<&) : "
                r1.<init>((java.lang.String) r2)
                java.lang.String r2 = r10.pattern
                int r3 = r10.f377i
                int r4 = r3 + 10
                int r5 = r2.length()
                if (r4 >= r5) goto L_0x008a
                int r4 = r10.f377i
                int r4 = r4 + r8
                goto L_0x0090
            L_0x008a:
                java.lang.String r4 = r10.pattern
                int r4 = r4.length()
            L_0x0090:
                java.lang.String r2 = r2.substring(r3, r4)
                r1.append((java.lang.String) r2)
                java.lang.String r1 = r1.toString()
                int r10 = r10.f377i
                r0.<init>(r1, r10)
                throw r0
            L_0x00a1:
                boolean r7 = java.text.PatternEntry.isSpecialChar(r6)
                if (r7 == 0) goto L_0x00c6
                if (r3 == 0) goto L_0x00aa
                goto L_0x00c6
            L_0x00aa:
                java.text.ParseException r0 = new java.text.ParseException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "Unquoted punctuation character : "
                r1.<init>((java.lang.String) r2)
                r2 = 16
                java.lang.String r2 = java.lang.Integer.toString(r6, r2)
                r1.append((java.lang.String) r2)
                java.lang.String r1 = r1.toString()
                int r10 = r10.f377i
                r0.<init>(r1, r10)
                throw r0
            L_0x00c6:
                if (r5 == 0) goto L_0x00ce
                java.lang.StringBuffer r7 = r10.newChars
                r7.append((char) r6)
                goto L_0x0116
            L_0x00ce:
                java.lang.StringBuffer r7 = r10.newExtension
                r7.append((char) r6)
                goto L_0x0116
            L_0x00d4:
                if (r4 == r0) goto L_0x00d7
                goto L_0x011d
            L_0x00d7:
                r4 = 3
                goto L_0x0116
            L_0x00d9:
                if (r4 == r0) goto L_0x00dc
                goto L_0x011d
            L_0x00dc:
                r4 = r1
                goto L_0x0116
            L_0x00de:
                if (r4 == r0) goto L_0x00e1
                goto L_0x011d
            L_0x00e1:
                r4 = r2
                goto L_0x0116
            L_0x00e3:
                java.lang.String r3 = r10.pattern
                int r6 = r10.f377i
                int r6 = r6 + r2
                r10.f377i = r6
                char r3 = r3.charAt(r6)
                java.lang.StringBuffer r6 = r10.newChars
                int r6 = r6.length()
                if (r6 != 0) goto L_0x00fc
                java.lang.StringBuffer r6 = r10.newChars
                r6.append((char) r3)
                goto L_0x0109
            L_0x00fc:
                if (r5 == 0) goto L_0x0104
                java.lang.StringBuffer r6 = r10.newChars
                r6.append((char) r3)
                goto L_0x0109
            L_0x0104:
                java.lang.StringBuffer r6 = r10.newExtension
                r6.append((char) r3)
            L_0x0109:
                r3 = r2
                goto L_0x0116
            L_0x010b:
                if (r4 == r0) goto L_0x010e
                goto L_0x011d
            L_0x010e:
                r4 = -2
                goto L_0x0116
            L_0x0110:
                r5 = r1
                goto L_0x0116
            L_0x0112:
                if (r4 == r0) goto L_0x0115
                goto L_0x011d
            L_0x0115:
                r4 = 2
            L_0x0116:
                int r6 = r10.f377i
                int r6 = r6 + r2
                r10.f377i = r6
                goto L_0x0010
            L_0x011d:
                if (r4 != r0) goto L_0x0121
                r10 = 0
                return r10
            L_0x0121:
                java.lang.StringBuffer r0 = r10.newChars
                int r0 = r0.length()
                if (r0 != 0) goto L_0x0159
                java.text.ParseException r0 = new java.text.ParseException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "missing chars (=,;<&): "
                r1.<init>((java.lang.String) r2)
                java.lang.String r2 = r10.pattern
                int r3 = r10.f377i
                int r4 = r3 + 10
                int r5 = r2.length()
                if (r4 >= r5) goto L_0x0142
                int r4 = r10.f377i
                int r4 = r4 + r8
                goto L_0x0148
            L_0x0142:
                java.lang.String r4 = r10.pattern
                int r4 = r4.length()
            L_0x0148:
                java.lang.String r2 = r2.substring(r3, r4)
                r1.append((java.lang.String) r2)
                java.lang.String r1 = r1.toString()
                int r10 = r10.f377i
                r0.<init>(r1, r10)
                throw r0
            L_0x0159:
                java.text.PatternEntry r0 = new java.text.PatternEntry
                java.lang.StringBuffer r1 = r10.newChars
                java.lang.StringBuffer r10 = r10.newExtension
                r0.<init>(r4, r1, r10)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.text.PatternEntry.Parser.next():java.text.PatternEntry");
        }
    }
}
