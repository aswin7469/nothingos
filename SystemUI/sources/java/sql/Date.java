package java.sql;

public class Date extends java.util.Date {
    static final long serialVersionUID = 1511598038487230103L;

    @Deprecated
    public Date(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    public Date(long j) {
        super(j);
    }

    public void setTime(long j) {
        super.setTime(j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x006c A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x006d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.sql.Date valueOf(java.lang.String r5) {
        /*
            if (r5 == 0) goto L_0x0073
            r0 = 45
            int r1 = r5.indexOf((int) r0)
            int r2 = r1 + 1
            int r0 = r5.indexOf((int) r0, (int) r2)
            if (r1 <= 0) goto L_0x0069
            if (r0 <= 0) goto L_0x0069
            int r3 = r5.length()
            r4 = 1
            int r3 = r3 - r4
            if (r0 >= r3) goto L_0x0069
            r3 = 0
            java.lang.String r1 = r5.substring(r3, r1)
            java.lang.String r2 = r5.substring(r2, r0)
            int r0 = r0 + r4
            java.lang.String r5 = r5.substring(r0)
            int r0 = r1.length()
            r3 = 4
            if (r0 != r3) goto L_0x0069
            int r0 = r2.length()
            if (r0 < r4) goto L_0x0069
            int r0 = r2.length()
            r3 = 2
            if (r0 > r3) goto L_0x0069
            int r0 = r5.length()
            if (r0 < r4) goto L_0x0069
            int r0 = r5.length()
            if (r0 > r3) goto L_0x0069
            int r0 = java.lang.Integer.parseInt(r1)
            int r1 = java.lang.Integer.parseInt(r2)
            int r5 = java.lang.Integer.parseInt(r5)
            if (r1 < r4) goto L_0x0069
            r2 = 12
            if (r1 > r2) goto L_0x0069
            if (r5 < r4) goto L_0x0069
            r2 = 31
            if (r5 > r2) goto L_0x0069
            java.sql.Date r2 = new java.sql.Date
            int r0 = r0 + -1900
            int r1 = r1 - r4
            r2.<init>(r0, r1, r5)
            goto L_0x006a
        L_0x0069:
            r2 = 0
        L_0x006a:
            if (r2 == 0) goto L_0x006d
            return r2
        L_0x006d:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            r5.<init>()
            throw r5
        L_0x0073:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            r5.<init>()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.sql.Date.valueOf(java.lang.String):java.sql.Date");
    }

    public String toString() {
        int year = super.getYear() + 1900;
        int month = super.getMonth() + 1;
        int date = super.getDate();
        char[] charArray = "2000-00-00".toCharArray();
        charArray[0] = Character.forDigit(year / 1000, 10);
        charArray[1] = Character.forDigit((year / 100) % 10, 10);
        charArray[2] = Character.forDigit((year / 10) % 10, 10);
        charArray[3] = Character.forDigit(year % 10, 10);
        charArray[5] = Character.forDigit(month / 10, 10);
        charArray[6] = Character.forDigit(month % 10, 10);
        charArray[8] = Character.forDigit(date / 10, 10);
        charArray[9] = Character.forDigit(date % 10, 10);
        return new String(charArray);
    }

    @Deprecated
    public int getHours() {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public int getMinutes() {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public int getSeconds() {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public void setHours(int i) {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public void setMinutes(int i) {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public void setSeconds(int i) {
        throw new IllegalArgumentException();
    }
}
