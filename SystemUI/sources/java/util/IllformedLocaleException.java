package java.util;

public class IllformedLocaleException extends RuntimeException {
    private static final long serialVersionUID = -5245986824925681401L;
    private int _errIdx;

    public IllformedLocaleException() {
        this._errIdx = -1;
    }

    public IllformedLocaleException(String str) {
        super(str);
        this._errIdx = -1;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public IllformedLocaleException(java.lang.String r3, int r4) {
        /*
            r2 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append((java.lang.String) r3)
            if (r4 >= 0) goto L_0x000d
            java.lang.String r3 = ""
            goto L_0x0020
        L_0x000d:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r1 = " [at index "
            r3.<init>((java.lang.String) r1)
            r3.append((int) r4)
            java.lang.String r1 = "]"
            r3.append((java.lang.String) r1)
            java.lang.String r3 = r3.toString()
        L_0x0020:
            r0.append((java.lang.String) r3)
            java.lang.String r3 = r0.toString()
            r2.<init>((java.lang.String) r3)
            r2._errIdx = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.IllformedLocaleException.<init>(java.lang.String, int):void");
    }

    public int getErrorIndex() {
        return this._errIdx;
    }
}
