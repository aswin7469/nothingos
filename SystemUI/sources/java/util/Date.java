package java.util;

import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.wifitrackerlib.WifiEntry;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.text.DateFormat;
import java.time.Instant;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.CalendarUtils;

public class Date implements Serializable, Cloneable, Comparable<Date> {
    private static int defaultCenturyStart = 0;
    private static final BaseCalendar gcal = CalendarSystem.getGregorianCalendar();
    private static BaseCalendar jcal = null;
    private static final long serialVersionUID = 7523967970034938905L;
    private static final int[] ttb = {14, 1, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 10000, 10000, 10000, 10300, 10240, 10360, 10300, 10420, 10360, 10480, 10420};
    private static final String[] wtb = {"am", "pm", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", ZoneGetter.KEY_GMT, "ut", "utc", "est", "edt", "cst", "cdt", "mst", "mdt", "pst", "pdt"};
    private transient BaseCalendar.Date cdate;
    private transient long fastTime;

    public Date() {
        this(System.currentTimeMillis());
    }

    public Date(long j) {
        this.fastTime = j;
    }

    @Deprecated
    public Date(int i, int i2, int i3) {
        this(i, i2, i3, 0, 0, 0);
    }

    @Deprecated
    public Date(int i, int i2, int i3, int i4, int i5) {
        this(i, i2, i3, i4, i5, 0);
    }

    @Deprecated
    public Date(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i + 1900;
        if (i2 >= 12) {
            i7 += i2 / 12;
            i2 %= 12;
        } else if (i2 < 0) {
            i7 += CalendarUtils.floorDivide(i2, 12);
            i2 = CalendarUtils.mod(i2, 12);
        }
        BaseCalendar.Date date = (BaseCalendar.Date) getCalendarSystem(i7).newCalendarDate(TimeZone.getDefaultRef());
        this.cdate = date;
        date.setNormalizedDate(i7, i2 + 1, i3).setTimeOfDay(i4, i5, i6, 0);
        getTimeImpl();
        this.cdate = null;
    }

    @Deprecated
    public Date(String str) {
        this(parse(str));
    }

    public Object clone() {
        Date date = null;
        try {
            Date date2 = (Date) super.clone();
            try {
                BaseCalendar.Date date3 = this.cdate;
                if (date3 == null) {
                    return date2;
                }
                date2.cdate = (BaseCalendar.Date) date3.clone();
                return date2;
            } catch (CloneNotSupportedException unused) {
                date = date2;
                return date;
            }
        } catch (CloneNotSupportedException unused2) {
            return date;
        }
    }

    @Deprecated
    public static long UTC(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i + 1900;
        if (i2 >= 12) {
            i7 += i2 / 12;
            i2 %= 12;
        } else if (i2 < 0) {
            i7 += CalendarUtils.floorDivide(i2, 12);
            i2 = CalendarUtils.mod(i2, 12);
        }
        BaseCalendar.Date date = (BaseCalendar.Date) getCalendarSystem(i7).newCalendarDate((TimeZone) null);
        date.setNormalizedDate(i7, i2 + 1, i3).setTimeOfDay(i4, i5, i6, 0);
        Date date2 = new Date(0);
        date2.normalize(date);
        return date2.fastTime;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:140:0x0189, code lost:
        r6 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x018a, code lost:
        if (r10 < 0) goto L_0x0213;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x018c, code lost:
        r2 = 0;
        r0 = r19;
        r1 = r9;
     */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long parse(java.lang.String r19) {
        /*
            r0 = r19
            if (r0 == 0) goto L_0x0213
            int r7 = r19.length()
            r1 = 0
            r2 = 0
            r6 = -1
            r11 = -2147483648(0xffffffff80000000, float:-0.0)
            r12 = -1
            r13 = -1
            r14 = -1
            r15 = -1
            r16 = -1
        L_0x0013:
            if (r1 >= r7) goto L_0x019c
            char r3 = r0.charAt(r1)
            int r1 = r1 + 1
            r4 = 32
            if (r3 <= r4) goto L_0x0196
            r5 = 44
            if (r3 != r5) goto L_0x0025
            goto L_0x0196
        L_0x0025:
            r10 = 40
            if (r3 != r10) goto L_0x0040
            r5 = 1
        L_0x002a:
            if (r1 >= r7) goto L_0x0013
            char r3 = r0.charAt(r1)
            int r1 = r1 + 1
            if (r3 != r10) goto L_0x0037
            int r5 = r5 + 1
            goto L_0x002a
        L_0x0037:
            r4 = 41
            if (r3 != r4) goto L_0x002a
            int r5 = r5 + -1
            if (r5 > 0) goto L_0x002a
            goto L_0x0013
        L_0x0040:
            r10 = 45
            r9 = 43
            r5 = 48
            if (r5 > r3) goto L_0x0105
            r4 = 57
            if (r3 > r4) goto L_0x0105
            int r17 = r3 + -48
            r8 = r17
        L_0x0050:
            if (r1 >= r7) goto L_0x0061
            char r3 = r0.charAt(r1)
            if (r5 > r3) goto L_0x0061
            if (r3 > r4) goto L_0x0061
            int r8 = r8 * 10
            int r8 = r8 + r3
            int r8 = r8 - r5
            int r1 = r1 + 1
            goto L_0x0050
        L_0x0061:
            if (r2 == r9) goto L_0x00c8
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r2 != r10) goto L_0x006b
            if (r11 == r9) goto L_0x006b
            goto L_0x00c8
        L_0x006b:
            r2 = 70
            if (r8 < r2) goto L_0x0080
            if (r11 != r9) goto L_0x0213
            r2 = 32
            if (r3 <= r2) goto L_0x00c6
            r2 = 44
            if (r3 == r2) goto L_0x00c6
            r2 = 47
            if (r3 == r2) goto L_0x00c6
            if (r1 < r7) goto L_0x0213
            goto L_0x00c6
        L_0x0080:
            r2 = 58
            if (r3 != r2) goto L_0x0090
            if (r14 >= 0) goto L_0x008a
            byte r2 = (byte) r8
            r14 = r2
            goto L_0x0102
        L_0x008a:
            if (r15 >= 0) goto L_0x0213
        L_0x008c:
            byte r2 = (byte) r8
            r15 = r2
            goto L_0x0102
        L_0x0090:
            r2 = 47
            if (r3 != r2) goto L_0x00a2
            if (r12 >= 0) goto L_0x009c
            int r8 = r8 + -1
            byte r2 = (byte) r8
            r12 = r2
            goto L_0x0102
        L_0x009c:
            if (r13 >= 0) goto L_0x0213
        L_0x009e:
            byte r2 = (byte) r8
            r13 = r2
            goto L_0x0102
        L_0x00a2:
            if (r1 >= r7) goto L_0x00ae
            r2 = 44
            if (r3 == r2) goto L_0x00ae
            r2 = 32
            if (r3 <= r2) goto L_0x00ae
            if (r3 != r10) goto L_0x0213
        L_0x00ae:
            if (r14 < 0) goto L_0x00b3
            if (r15 >= 0) goto L_0x00b3
            goto L_0x008c
        L_0x00b3:
            if (r15 < 0) goto L_0x00bb
            if (r16 >= 0) goto L_0x00bb
            byte r2 = (byte) r8
            r16 = r2
            goto L_0x0102
        L_0x00bb:
            if (r13 >= 0) goto L_0x00be
            goto L_0x009e
        L_0x00be:
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r11 != r2) goto L_0x0213
            if (r12 < 0) goto L_0x0213
            if (r13 < 0) goto L_0x0213
        L_0x00c6:
            r11 = r8
            goto L_0x0102
        L_0x00c8:
            if (r6 == 0) goto L_0x00cd
            r3 = -1
            if (r6 != r3) goto L_0x0213
        L_0x00cd:
            r3 = 24
            if (r8 >= r3) goto L_0x00f5
            int r8 = r8 * 60
            if (r1 >= r7) goto L_0x00f2
            char r3 = r0.charAt(r1)
            r6 = 58
            if (r3 != r6) goto L_0x00f2
            int r1 = r1 + 1
            r3 = 0
        L_0x00e0:
            if (r1 >= r7) goto L_0x00f3
            char r6 = r0.charAt(r1)
            if (r5 > r6) goto L_0x00f3
            if (r6 > r4) goto L_0x00f3
            int r3 = r3 * 10
            int r6 = r6 + -48
            int r3 = r3 + r6
            int r1 = r1 + 1
            goto L_0x00e0
        L_0x00f2:
            r3 = 0
        L_0x00f3:
            int r8 = r8 + r3
            goto L_0x00fc
        L_0x00f5:
            int r3 = r8 % 100
            int r8 = r8 / 100
            int r8 = r8 * 60
            int r8 = r8 + r3
        L_0x00fc:
            r4 = 43
            if (r2 != r4) goto L_0x0101
            int r8 = -r8
        L_0x0101:
            r6 = r8
        L_0x0102:
            r2 = 0
            goto L_0x0013
        L_0x0105:
            r4 = r9
            r2 = 47
            if (r3 == r2) goto L_0x0192
            r2 = 58
            if (r3 == r2) goto L_0x0192
            if (r3 == r4) goto L_0x0192
            if (r3 != r10) goto L_0x0114
            goto L_0x0192
        L_0x0114:
            int r8 = r1 + -1
            r9 = r1
        L_0x0117:
            if (r9 >= r7) goto L_0x0131
            char r1 = r0.charAt(r9)
            r2 = 65
            if (r2 > r1) goto L_0x0125
            r2 = 90
            if (r1 <= r2) goto L_0x012e
        L_0x0125:
            r2 = 97
            if (r2 > r1) goto L_0x0131
            r2 = 122(0x7a, float:1.71E-43)
            if (r1 <= r2) goto L_0x012e
            goto L_0x0131
        L_0x012e:
            int r9 = r9 + 1
            goto L_0x0117
        L_0x0131:
            int r1 = r8 + 1
            if (r9 <= r1) goto L_0x0213
            java.lang.String[] r1 = wtb
            int r1 = r1.length
            r2 = -1
        L_0x0139:
            int r10 = r1 + -1
            if (r10 < 0) goto L_0x0188
            java.lang.String[] r1 = wtb
            r1 = r1[r10]
            r2 = 1
            r3 = 0
            int r18 = r9 - r8
            r4 = r19
            r5 = r8
            r0 = r6
            r6 = r18
            boolean r1 = r1.regionMatches(r2, r3, r4, r5, r6)
            if (r1 == 0) goto L_0x0182
            int[] r1 = ttb
            r1 = r1[r10]
            if (r1 == 0) goto L_0x0189
            r2 = 12
            r3 = 1
            if (r1 != r3) goto L_0x0165
            if (r14 > r2) goto L_0x0213
            if (r14 < r3) goto L_0x0213
            if (r14 >= r2) goto L_0x0189
            int r14 = r14 + 12
            goto L_0x0189
        L_0x0165:
            r4 = 14
            if (r1 != r4) goto L_0x0172
            if (r14 > r2) goto L_0x0213
            if (r14 < r3) goto L_0x0213
            if (r14 != r2) goto L_0x0189
            r6 = r0
            r14 = 0
            goto L_0x018a
        L_0x0172:
            r2 = 13
            if (r1 > r2) goto L_0x017e
            if (r12 >= 0) goto L_0x0213
            int r1 = r1 + -2
            byte r1 = (byte) r1
            r6 = r0
            r12 = r1
            goto L_0x018a
        L_0x017e:
            int r1 = r1 + -10000
            r6 = r1
            goto L_0x018a
        L_0x0182:
            r6 = r0
            r1 = r10
            r2 = -1
            r0 = r19
            goto L_0x0139
        L_0x0188:
            r0 = r6
        L_0x0189:
            r6 = r0
        L_0x018a:
            if (r10 < 0) goto L_0x0213
            r2 = 0
            r0 = r19
            r1 = r9
            goto L_0x0013
        L_0x0192:
            r0 = r6
            r6 = r0
            r2 = r3
            goto L_0x0198
        L_0x0196:
            r0 = r6
            r6 = r0
        L_0x0198:
            r0 = r19
            goto L_0x0013
        L_0x019c:
            r0 = r6
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = 1
            if (r11 == r1) goto L_0x0213
            if (r12 < 0) goto L_0x0213
            if (r13 < 0) goto L_0x0213
            r1 = 100
            if (r11 >= r1) goto L_0x01ce
            java.lang.Class<java.util.Date> r2 = java.util.Date.class
            monitor-enter(r2)
            int r4 = defaultCenturyStart     // Catch:{ all -> 0x01cb }
            if (r4 != 0) goto L_0x01bf
            sun.util.calendar.BaseCalendar r4 = gcal     // Catch:{ all -> 0x01cb }
            sun.util.calendar.CalendarDate r4 = r4.getCalendarDate()     // Catch:{ all -> 0x01cb }
            int r4 = r4.getYear()     // Catch:{ all -> 0x01cb }
            int r4 = r4 + -80
            defaultCenturyStart = r4     // Catch:{ all -> 0x01cb }
        L_0x01bf:
            monitor-exit(r2)     // Catch:{ all -> 0x01cb }
            int r2 = defaultCenturyStart
            int r4 = r2 / 100
            int r4 = r4 * r1
            int r11 = r11 + r4
            if (r11 >= r2) goto L_0x01ce
            int r11 = r11 + 100
            goto L_0x01ce
        L_0x01cb:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x01cb }
            throw r0
        L_0x01ce:
            if (r16 >= 0) goto L_0x01d2
            r1 = 0
            goto L_0x01d4
        L_0x01d2:
            r1 = r16
        L_0x01d4:
            if (r15 >= 0) goto L_0x01d7
            r15 = 0
        L_0x01d7:
            if (r14 >= 0) goto L_0x01da
            r14 = 0
        L_0x01da:
            sun.util.calendar.BaseCalendar r2 = getCalendarSystem((int) r11)
            r4 = -1
            if (r0 != r4) goto L_0x01f8
            java.util.TimeZone r0 = java.util.TimeZone.getDefaultRef()
            sun.util.calendar.CalendarDate r0 = r2.newCalendarDate(r0)
            sun.util.calendar.BaseCalendar$Date r0 = (sun.util.calendar.BaseCalendar.Date) r0
            int r12 = r12 + r3
            r0.setDate(r11, r12, r13)
            r4 = 0
            r0.setTimeOfDay(r14, r15, r1, r4)
            long r0 = r2.getTime(r0)
            return r0
        L_0x01f8:
            r4 = 0
            r5 = 0
            sun.util.calendar.CalendarDate r5 = r2.newCalendarDate(r5)
            sun.util.calendar.BaseCalendar$Date r5 = (sun.util.calendar.BaseCalendar.Date) r5
            int r12 = r12 + r3
            r5.setDate(r11, r12, r13)
            r5.setTimeOfDay(r14, r15, r1, r4)
            long r1 = r2.getTime(r5)
            r3 = 60000(0xea60, float:8.4078E-41)
            int r6 = r0 * r3
            long r3 = (long) r6
            long r1 = r1 + r3
            return r1
        L_0x0213:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Date.parse(java.lang.String):long");
    }

    @Deprecated
    public int getYear() {
        return normalize().getYear() - 1900;
    }

    @Deprecated
    public void setYear(int i) {
        getCalendarDate().setNormalizedYear(i + 1900);
    }

    @Deprecated
    public int getMonth() {
        return normalize().getMonth() - 1;
    }

    @Deprecated
    public void setMonth(int i) {
        int i2;
        if (i >= 12) {
            i2 = i / 12;
            i %= 12;
        } else if (i < 0) {
            int floorDivide = CalendarUtils.floorDivide(i, 12);
            i = CalendarUtils.mod(i, 12);
            i2 = floorDivide;
        } else {
            i2 = 0;
        }
        BaseCalendar.Date calendarDate = getCalendarDate();
        if (i2 != 0) {
            calendarDate.setNormalizedYear(calendarDate.getNormalizedYear() + i2);
        }
        calendarDate.setMonth(i + 1);
    }

    @Deprecated
    public int getDate() {
        return normalize().getDayOfMonth();
    }

    @Deprecated
    public void setDate(int i) {
        getCalendarDate().setDayOfMonth(i);
    }

    @Deprecated
    public int getDay() {
        return normalize().getDayOfWeek() - 1;
    }

    @Deprecated
    public int getHours() {
        return normalize().getHours();
    }

    @Deprecated
    public void setHours(int i) {
        getCalendarDate().setHours(i);
    }

    @Deprecated
    public int getMinutes() {
        return normalize().getMinutes();
    }

    @Deprecated
    public void setMinutes(int i) {
        getCalendarDate().setMinutes(i);
    }

    @Deprecated
    public int getSeconds() {
        return normalize().getSeconds();
    }

    @Deprecated
    public void setSeconds(int i) {
        getCalendarDate().setSeconds(i);
    }

    public long getTime() {
        return getTimeImpl();
    }

    private final long getTimeImpl() {
        BaseCalendar.Date date = this.cdate;
        if (date != null && !date.isNormalized()) {
            normalize();
        }
        return this.fastTime;
    }

    public void setTime(long j) {
        this.fastTime = j;
        this.cdate = null;
    }

    public boolean before(Date date) {
        return getMillisOf(this) < getMillisOf(date);
    }

    public boolean after(Date date) {
        return getMillisOf(this) > getMillisOf(date);
    }

    public boolean equals(Object obj) {
        return (obj instanceof Date) && getTime() == ((Date) obj).getTime();
    }

    static final long getMillisOf(Date date) {
        if (date.getClass() != Date.class) {
            return date.getTime();
        }
        BaseCalendar.Date date2 = date.cdate;
        if (date2 == null || date2.isNormalized()) {
            return date.fastTime;
        }
        return gcal.getTime((BaseCalendar.Date) date.cdate.clone());
    }

    public int compareTo(Date date) {
        int i = (getMillisOf(this) > getMillisOf(date) ? 1 : (getMillisOf(this) == getMillisOf(date) ? 0 : -1));
        if (i < 0) {
            return -1;
        }
        return i == 0 ? 0 : 1;
    }

    public int hashCode() {
        long time = getTime();
        return ((int) time) ^ ((int) (time >> 32));
    }

    public String toString() {
        BaseCalendar.Date normalize = normalize();
        StringBuilder sb = new StringBuilder(28);
        int dayOfWeek = normalize.getDayOfWeek();
        if (dayOfWeek == 1) {
            dayOfWeek = 8;
        }
        String[] strArr = wtb;
        convertToAbbr(sb, strArr[dayOfWeek]).append(' ');
        convertToAbbr(sb, strArr[(normalize.getMonth() - 1) + 2 + 7]).append(' ');
        CalendarUtils.sprintf0d(sb, normalize.getDayOfMonth(), 2).append(' ');
        CalendarUtils.sprintf0d(sb, normalize.getHours(), 2).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        CalendarUtils.sprintf0d(sb, normalize.getMinutes(), 2).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        CalendarUtils.sprintf0d(sb, normalize.getSeconds(), 2).append(' ');
        TimeZone zone = normalize.getZone();
        if (zone != null) {
            sb.append(zone.getDisplayName(normalize.isDaylightTime(), 0, Locale.f700US));
        } else {
            sb.append("GMT");
        }
        sb.append(' ');
        sb.append(normalize.getYear());
        return sb.toString();
    }

    private static final StringBuilder convertToAbbr(StringBuilder sb, String str) {
        sb.append(Character.toUpperCase(str.charAt(0)));
        sb.append(str.charAt(1));
        sb.append(str.charAt(2));
        return sb;
    }

    @Deprecated
    public String toLocaleString() {
        return DateFormat.getDateTimeInstance().format(this);
    }

    @Deprecated
    public String toGMTString() {
        TimeZone timeZone = null;
        BaseCalendar.Date date = (BaseCalendar.Date) getCalendarSystem(getTime()).getCalendarDate(getTime(), (TimeZone) null);
        StringBuilder sb = new StringBuilder(32);
        CalendarUtils.sprintf0d(sb, date.getDayOfMonth(), 1).append(' ');
        convertToAbbr(sb, wtb[(date.getMonth() - 1) + 2 + 7]).append(' ');
        sb.append(date.getYear());
        sb.append(' ');
        CalendarUtils.sprintf0d(sb, date.getHours(), 2).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        CalendarUtils.sprintf0d(sb, date.getMinutes(), 2).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        CalendarUtils.sprintf0d(sb, date.getSeconds(), 2);
        sb.append(" GMT");
        return sb.toString();
    }

    @Deprecated
    public int getTimezoneOffset() {
        int i;
        if (this.cdate == null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(this.fastTime);
            i = gregorianCalendar.get(15) + gregorianCalendar.get(16);
        } else {
            normalize();
            i = this.cdate.getZoneOffset();
        }
        return (-i) / WifiEntry.FREQUENCY_60_GHZ;
    }

    private final BaseCalendar.Date getCalendarDate() {
        if (this.cdate == null) {
            this.cdate = (BaseCalendar.Date) getCalendarSystem(this.fastTime).getCalendarDate(this.fastTime, TimeZone.getDefaultRef());
        }
        return this.cdate;
    }

    private final BaseCalendar.Date normalize() {
        BaseCalendar.Date date = this.cdate;
        if (date == null) {
            BaseCalendar.Date date2 = (BaseCalendar.Date) getCalendarSystem(this.fastTime).getCalendarDate(this.fastTime, TimeZone.getDefaultRef());
            this.cdate = date2;
            return date2;
        }
        if (!date.isNormalized()) {
            this.cdate = normalize(this.cdate);
        }
        TimeZone defaultRef = TimeZone.getDefaultRef();
        if (defaultRef != this.cdate.getZone()) {
            this.cdate.setZone(defaultRef);
            getCalendarSystem(this.cdate).getCalendarDate(this.fastTime, (CalendarDate) this.cdate);
        }
        return this.cdate;
    }

    private final BaseCalendar.Date normalize(BaseCalendar.Date date) {
        int normalizedYear = date.getNormalizedYear();
        int month = date.getMonth();
        int dayOfMonth = date.getDayOfMonth();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        int millis = date.getMillis();
        TimeZone zone = date.getZone();
        if (normalizedYear == 1582 || normalizedYear > 280000000 || normalizedYear < -280000000) {
            if (zone == null) {
                zone = TimeZone.getTimeZone("GMT");
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar(zone);
            gregorianCalendar.clear();
            gregorianCalendar.set(14, millis);
            gregorianCalendar.set(normalizedYear, month - 1, dayOfMonth, hours, minutes, seconds);
            long timeInMillis = gregorianCalendar.getTimeInMillis();
            this.fastTime = timeInMillis;
            return (BaseCalendar.Date) getCalendarSystem(timeInMillis).getCalendarDate(this.fastTime, zone);
        }
        BaseCalendar calendarSystem = getCalendarSystem(normalizedYear);
        if (calendarSystem != getCalendarSystem(date)) {
            date = (BaseCalendar.Date) calendarSystem.newCalendarDate(zone);
            date.setNormalizedDate(normalizedYear, month, dayOfMonth).setTimeOfDay(hours, minutes, seconds, millis);
        }
        long time = calendarSystem.getTime(date);
        this.fastTime = time;
        BaseCalendar calendarSystem2 = getCalendarSystem(time);
        if (calendarSystem2 == calendarSystem) {
            return date;
        }
        BaseCalendar.Date date2 = (BaseCalendar.Date) calendarSystem2.newCalendarDate(zone);
        date2.setNormalizedDate(normalizedYear, month, dayOfMonth).setTimeOfDay(hours, minutes, seconds, millis);
        this.fastTime = calendarSystem2.getTime(date2);
        return date2;
    }

    private static final BaseCalendar getCalendarSystem(int i) {
        if (i >= 1582) {
            return gcal;
        }
        return getJulianCalendar();
    }

    private static final BaseCalendar getCalendarSystem(long j) {
        if (j >= 0 || j >= -12219292800000L - ((long) TimeZone.getDefaultRef().getOffset(j))) {
            return gcal;
        }
        return getJulianCalendar();
    }

    private static final BaseCalendar getCalendarSystem(BaseCalendar.Date date) {
        if (jcal == null) {
            return gcal;
        }
        if (date.getEra() != null) {
            return jcal;
        }
        return gcal;
    }

    private static final synchronized BaseCalendar getJulianCalendar() {
        BaseCalendar baseCalendar;
        synchronized (Date.class) {
            if (jcal == null) {
                jcal = (BaseCalendar) CalendarSystem.forName("julian");
            }
            baseCalendar = jcal;
        }
        return baseCalendar;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeLong(getTimeImpl());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.fastTime = objectInputStream.readLong();
    }

    public static Date from(Instant instant) {
        try {
            return new Date(instant.toEpochMilli());
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException((Throwable) e);
        }
    }

    public Instant toInstant() {
        return Instant.ofEpochMilli(getTime());
    }
}
