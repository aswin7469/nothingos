package org.json;

public class JSONTokener {

    /* renamed from: in */
    private final String f856in;
    private int pos;

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        char c2 = 'A';
        if (c < 'A' || c > 'F') {
            c2 = 'a';
            if (c < 'a' || c > 'f') {
                return -1;
            }
        }
        return (c - c2) + 10;
    }

    public JSONTokener(String str) {
        if (str != null && str.startsWith("﻿")) {
            str = str.substring(1);
        }
        this.f856in = str;
    }

    public Object nextValue() throws JSONException {
        int nextCleanInternal = nextCleanInternal();
        if (nextCleanInternal == -1) {
            throw syntaxError("End of input");
        } else if (nextCleanInternal == 34 || nextCleanInternal == 39) {
            return nextString((char) nextCleanInternal);
        } else {
            if (nextCleanInternal == 91) {
                return readArray();
            }
            if (nextCleanInternal == 123) {
                return readObject();
            }
            this.pos--;
            return readLiteral();
        }
    }

    private int nextCleanInternal() throws JSONException {
        while (this.pos < this.f856in.length()) {
            String str = this.f856in;
            int i = this.pos;
            this.pos = i + 1;
            char charAt = str.charAt(i);
            if (!(charAt == 9 || charAt == 10 || charAt == 13 || charAt == ' ')) {
                if (charAt == '#') {
                    skipToEndOfLine();
                } else if (charAt != '/' || this.pos == this.f856in.length()) {
                    return charAt;
                } else {
                    char charAt2 = this.f856in.charAt(this.pos);
                    if (charAt2 == '*') {
                        int i2 = this.pos + 1;
                        this.pos = i2;
                        int indexOf = this.f856in.indexOf("*/", i2);
                        if (indexOf != -1) {
                            this.pos = indexOf + 2;
                        } else {
                            throw syntaxError("Unterminated comment");
                        }
                    } else if (charAt2 != '/') {
                        return charAt;
                    } else {
                        this.pos++;
                        skipToEndOfLine();
                    }
                }
            }
        }
        return -1;
    }

    private void skipToEndOfLine() {
        while (this.pos < this.f856in.length()) {
            char charAt = this.f856in.charAt(this.pos);
            if (charAt == 13 || charAt == 10) {
                this.pos++;
                return;
            }
            this.pos++;
        }
    }

    public String nextString(char c) throws JSONException {
        int i = this.pos;
        StringBuilder sb = null;
        while (this.pos < this.f856in.length()) {
            String str = this.f856in;
            int i2 = this.pos;
            this.pos = i2 + 1;
            char charAt = str.charAt(i2);
            if (charAt == c) {
                if (sb == null) {
                    return new String(this.f856in.substring(i, this.pos - 1));
                }
                sb.append((CharSequence) this.f856in, i, this.pos - 1);
                return sb.toString();
            } else if (charAt == '\\') {
                if (this.pos != this.f856in.length()) {
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    sb.append((CharSequence) this.f856in, i, this.pos - 1);
                    sb.append(readEscapeCharacter());
                    i = this.pos;
                } else {
                    throw syntaxError("Unterminated escape sequence");
                }
            }
        }
        throw syntaxError("Unterminated string");
    }

    private char readEscapeCharacter() throws JSONException {
        String str = this.f856in;
        int i = this.pos;
        this.pos = i + 1;
        char charAt = str.charAt(i);
        if (charAt == 'b') {
            return 8;
        }
        if (charAt == 'f') {
            return 12;
        }
        if (charAt == 'n') {
            return 10;
        }
        if (charAt == 'r') {
            return 13;
        }
        if (charAt == 't') {
            return 9;
        }
        if (charAt != 'u') {
            return charAt;
        }
        if (this.pos + 4 <= this.f856in.length()) {
            String str2 = this.f856in;
            int i2 = this.pos;
            String substring = str2.substring(i2, i2 + 4);
            this.pos += 4;
            try {
                return (char) Integer.parseInt(substring, 16);
            } catch (NumberFormatException unused) {
                throw syntaxError("Invalid escape sequence: " + substring);
            }
        } else {
            throw syntaxError("Unterminated escape sequence");
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:14|(5:16|(1:26)(2:20|(1:25)(1:24))|27|28|(2:34|35)(2:32|33))|36|37|38) */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x008f, code lost:
        return new java.lang.String(r0);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:36:0x0085 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.Object readLiteral() throws org.json.JSONException {
        /*
            r5 = this;
            java.lang.String r0 = "{}[]/\\:,=;# \t\f"
            java.lang.String r0 = r5.nextToInternal(r0)
            int r1 = r0.length()
            if (r1 == 0) goto L_0x0090
            java.lang.String r5 = "null"
            boolean r5 = r5.equalsIgnoreCase(r0)
            if (r5 == 0) goto L_0x0017
            java.lang.Object r5 = org.json.JSONObject.NULL
            return r5
        L_0x0017:
            java.lang.String r5 = "true"
            boolean r5 = r5.equalsIgnoreCase(r0)
            if (r5 == 0) goto L_0x0022
            java.lang.Boolean r5 = java.lang.Boolean.TRUE
            return r5
        L_0x0022:
            java.lang.String r5 = "false"
            boolean r5 = r5.equalsIgnoreCase(r0)
            if (r5 == 0) goto L_0x002d
            java.lang.Boolean r5 = java.lang.Boolean.FALSE
            return r5
        L_0x002d:
            r5 = 46
            int r5 = r0.indexOf((int) r5)
            r1 = -1
            if (r5 != r1) goto L_0x0085
            java.lang.String r5 = "0x"
            boolean r5 = r0.startsWith(r5)
            if (r5 != 0) goto L_0x0061
            java.lang.String r5 = "0X"
            boolean r5 = r0.startsWith(r5)
            if (r5 == 0) goto L_0x0047
            goto L_0x0061
        L_0x0047:
            java.lang.String r5 = "0"
            boolean r5 = r0.startsWith(r5)
            if (r5 == 0) goto L_0x005d
            int r5 = r0.length()
            r1 = 1
            if (r5 <= r1) goto L_0x005d
            java.lang.String r5 = r0.substring(r1)
            r1 = 8
            goto L_0x0068
        L_0x005d:
            r1 = 10
            r5 = r0
            goto L_0x0068
        L_0x0061:
            r5 = 2
            java.lang.String r5 = r0.substring(r5)
            r1 = 16
        L_0x0068:
            long r1 = java.lang.Long.parseLong(r5, r1)     // Catch:{ NumberFormatException -> 0x0085 }
            r3 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 > 0) goto L_0x0080
            r3 = -2147483648(0xffffffff80000000, double:NaN)
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 < 0) goto L_0x0080
            int r5 = (int) r1     // Catch:{ NumberFormatException -> 0x0085 }
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r5)     // Catch:{ NumberFormatException -> 0x0085 }
            return r5
        L_0x0080:
            java.lang.Long r5 = java.lang.Long.valueOf((long) r1)     // Catch:{ NumberFormatException -> 0x0085 }
            return r5
        L_0x0085:
            java.lang.Double r5 = java.lang.Double.valueOf((java.lang.String) r0)     // Catch:{ NumberFormatException -> 0x008a }
            return r5
        L_0x008a:
            java.lang.String r5 = new java.lang.String
            r5.<init>((java.lang.String) r0)
            return r5
        L_0x0090:
            java.lang.String r0 = "Expected literal value"
            org.json.JSONException r5 = r5.syntaxError(r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.JSONTokener.readLiteral():java.lang.Object");
    }

    private String nextToInternal(String str) {
        int i = this.pos;
        while (this.pos < this.f856in.length()) {
            char charAt = this.f856in.charAt(this.pos);
            if (charAt == 13 || charAt == 10 || str.indexOf((int) charAt) != -1) {
                return this.f856in.substring(i, this.pos);
            }
            this.pos++;
        }
        return this.f856in.substring(i);
    }

    private JSONObject readObject() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        int nextCleanInternal = nextCleanInternal();
        if (nextCleanInternal == 125) {
            return jSONObject;
        }
        if (nextCleanInternal != -1) {
            this.pos--;
        }
        while (true) {
            Object nextValue = nextValue();
            if (nextValue instanceof String) {
                int nextCleanInternal2 = nextCleanInternal();
                if (nextCleanInternal2 == 58 || nextCleanInternal2 == 61) {
                    if (this.pos < this.f856in.length() && this.f856in.charAt(this.pos) == '>') {
                        this.pos++;
                    }
                    jSONObject.put((String) nextValue, nextValue());
                    int nextCleanInternal3 = nextCleanInternal();
                    if (nextCleanInternal3 != 44 && nextCleanInternal3 != 59) {
                        if (nextCleanInternal3 == 125) {
                            return jSONObject;
                        }
                        throw syntaxError("Unterminated object");
                    }
                } else {
                    throw syntaxError("Expected ':' after " + nextValue);
                }
            } else if (nextValue == null) {
                throw syntaxError("Names cannot be null");
            } else {
                throw syntaxError("Names must be strings, but " + nextValue + " is of type " + nextValue.getClass().getName());
            }
        }
    }

    private JSONArray readArray() throws JSONException {
        JSONArray jSONArray = new JSONArray();
        boolean z = false;
        while (true) {
            int nextCleanInternal = nextCleanInternal();
            if (nextCleanInternal != -1) {
                if (nextCleanInternal == 44 || nextCleanInternal == 59) {
                    jSONArray.put((Object) null);
                } else if (nextCleanInternal != 93) {
                    this.pos--;
                    jSONArray.put(nextValue());
                    int nextCleanInternal2 = nextCleanInternal();
                    if (!(nextCleanInternal2 == 44 || nextCleanInternal2 == 59)) {
                        if (nextCleanInternal2 == 93) {
                            return jSONArray;
                        }
                        throw syntaxError("Unterminated array");
                    }
                } else {
                    if (z) {
                        jSONArray.put((Object) null);
                    }
                    return jSONArray;
                }
                z = true;
            } else {
                throw syntaxError("Unterminated array");
            }
        }
    }

    public JSONException syntaxError(String str) {
        return new JSONException(str + this);
    }

    public String toString() {
        return " at character " + this.pos + " of " + this.f856in;
    }

    public boolean more() {
        return this.pos < this.f856in.length();
    }

    public char next() {
        if (this.pos >= this.f856in.length()) {
            return 0;
        }
        String str = this.f856in;
        int i = this.pos;
        this.pos = i + 1;
        return str.charAt(i);
    }

    public char next(char c) throws JSONException {
        char next = next();
        if (next == c) {
            return next;
        }
        throw syntaxError("Expected " + c + " but was " + next);
    }

    public char nextClean() throws JSONException {
        int nextCleanInternal = nextCleanInternal();
        if (nextCleanInternal == -1) {
            return 0;
        }
        return (char) nextCleanInternal;
    }

    public String next(int i) throws JSONException {
        if (this.pos + i <= this.f856in.length()) {
            String str = this.f856in;
            int i2 = this.pos;
            String substring = str.substring(i2, i2 + i);
            this.pos += i;
            return substring;
        }
        throw syntaxError(i + " is out of bounds");
    }

    public String nextTo(String str) {
        if (str != null) {
            return nextToInternal(str).trim();
        }
        throw new NullPointerException("excluded == null");
    }

    public String nextTo(char c) {
        return nextToInternal(String.valueOf(c)).trim();
    }

    public void skipPast(String str) {
        int indexOf = this.f856in.indexOf(str, this.pos);
        this.pos = indexOf == -1 ? this.f856in.length() : str.length() + indexOf;
    }

    public char skipTo(char c) {
        int indexOf = this.f856in.indexOf((int) c, this.pos);
        if (indexOf == -1) {
            return 0;
        }
        this.pos = indexOf;
        return c;
    }

    public void back() {
        int i = this.pos - 1;
        this.pos = i;
        if (i == -1) {
            this.pos = 0;
        }
    }
}
