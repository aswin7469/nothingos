package java.text;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.StringJoiner;

class CalendarBuilder {
    private static final int COMPUTED = 1;
    public static final int ISO_DAY_OF_WEEK = 1000;
    private static final int MAX_FIELD = 18;
    private static final int MINIMUM_USER_STAMP = 2;
    private static final int UNSET = 0;
    public static final int WEEK_YEAR = 17;
    private final int[] field = new int[36];
    private int maxFieldIndex = -1;
    private int nextStamp = 2;

    static boolean isValidDayOfWeek(int i) {
        return i > 0 && i <= 7;
    }

    static int toISODayOfWeek(int i) {
        if (i == 1) {
            return 7;
        }
        return i - 1;
    }

    CalendarBuilder() {
    }

    /* access modifiers changed from: package-private */
    public CalendarBuilder set(int i, int i2) {
        if (i == 1000) {
            i2 = toCalendarDayOfWeek(i2);
            i = 7;
        }
        int[] iArr = this.field;
        int i3 = this.nextStamp;
        this.nextStamp = i3 + 1;
        iArr[i] = i3;
        iArr[i + 18] = i2;
        if (i > this.maxFieldIndex && i < 17) {
            this.maxFieldIndex = i;
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public CalendarBuilder addYear(int i) {
        int[] iArr = this.field;
        iArr[19] = iArr[19] + i;
        iArr[35] = iArr[35] + i;
        return this;
    }

    /* access modifiers changed from: package-private */
    public boolean isSet(int i) {
        if (i == 1000) {
            i = 7;
        }
        return this.field[i] > 0;
    }

    /* access modifiers changed from: package-private */
    public CalendarBuilder clear(int i) {
        if (i == 1000) {
            i = 7;
        }
        int[] iArr = this.field;
        iArr[i] = 0;
        iArr[i + 18] = 0;
        return this;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0050  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Calendar establish(java.util.Calendar r9) {
        /*
            r8 = this;
            r0 = 17
            boolean r1 = r8.isSet(r0)
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x0014
            int[] r1 = r8.field
            r0 = r1[r0]
            r1 = r1[r3]
            if (r0 <= r1) goto L_0x0014
            r0 = r3
            goto L_0x0015
        L_0x0014:
            r0 = r2
        L_0x0015:
            r1 = 35
            if (r0 == 0) goto L_0x002d
            boolean r4 = r9.isWeekDateSupported()
            if (r4 != 0) goto L_0x002d
            boolean r0 = r8.isSet(r3)
            if (r0 != 0) goto L_0x002c
            int[] r0 = r8.field
            r0 = r0[r1]
            r8.set(r3, r0)
        L_0x002c:
            r0 = r2
        L_0x002d:
            r9.clear()
            r4 = 2
        L_0x0031:
            int r5 = r8.nextStamp
            if (r4 >= r5) goto L_0x004e
            r5 = r2
        L_0x0036:
            int r6 = r8.maxFieldIndex
            if (r5 > r6) goto L_0x004b
            int[] r6 = r8.field
            r7 = r6[r5]
            if (r7 != r4) goto L_0x0048
            int r7 = r5 + 18
            r6 = r6[r7]
            r9.set(r5, r6)
            goto L_0x004b
        L_0x0048:
            int r5 = r5 + 1
            goto L_0x0036
        L_0x004b:
            int r4 = r4 + 1
            goto L_0x0031
        L_0x004e:
            if (r0 == 0) goto L_0x009b
            r0 = 3
            boolean r0 = r8.isSet(r0)
            if (r0 == 0) goto L_0x005e
            int[] r0 = r8.field
            r2 = 21
            r0 = r0[r2]
            goto L_0x005f
        L_0x005e:
            r0 = r3
        L_0x005f:
            r2 = 7
            boolean r4 = r8.isSet(r2)
            if (r4 == 0) goto L_0x006d
            int[] r4 = r8.field
            r5 = 25
            r4 = r4[r5]
            goto L_0x0071
        L_0x006d:
            int r4 = r9.getFirstDayOfWeek()
        L_0x0071:
            boolean r5 = isValidDayOfWeek(r4)
            if (r5 != 0) goto L_0x0094
            boolean r5 = r9.isLenient()
            if (r5 == 0) goto L_0x0094
            r5 = 8
            if (r4 < r5) goto L_0x0089
            int r4 = r4 + -1
            int r5 = r4 / 7
            int r0 = r0 + r5
            int r4 = r4 % r2
            int r4 = r4 + r3
            goto L_0x0090
        L_0x0089:
            if (r4 > 0) goto L_0x0090
            int r4 = r4 + 7
            int r0 = r0 + -1
            goto L_0x0089
        L_0x0090:
            int r4 = toCalendarDayOfWeek(r4)
        L_0x0094:
            int[] r8 = r8.field
            r8 = r8[r1]
            r9.setWeekDate(r8, r0, r4)
        L_0x009b:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.CalendarBuilder.establish(java.util.Calendar):java.util.Calendar");
    }

    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR, "CalendarBuilder:[", NavigationBarInflaterView.SIZE_MOD_END);
        for (int i = 0; i < this.field.length; i++) {
            if (isSet(i)) {
                stringJoiner.add(i + "=" + this.field[i + 18]);
            }
        }
        return stringJoiner.toString();
    }

    static int toCalendarDayOfWeek(int i) {
        if (!isValidDayOfWeek(i)) {
            return i;
        }
        if (i == 7) {
            return 1;
        }
        return 1 + i;
    }
}
