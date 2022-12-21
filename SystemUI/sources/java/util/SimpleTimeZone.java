package java.util;

import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.CalendarUtils;
import sun.util.calendar.Gregorian;

public class SimpleTimeZone extends TimeZone {
    private static final int DOM_MODE = 1;
    private static final int DOW_GE_DOM_MODE = 3;
    private static final int DOW_IN_MONTH_MODE = 2;
    private static final int DOW_LE_DOM_MODE = 4;
    private static final int MAX_RULE_NUM = 6;
    public static final int STANDARD_TIME = 1;
    public static final int UTC_TIME = 2;
    public static final int WALL_TIME = 0;
    static final int currentSerialVersion = 2;
    private static final Gregorian gcal = CalendarSystem.getGregorianCalendar();
    private static final int millisPerDay = 86400000;
    private static final int millisPerHour = 3600000;
    static final long serialVersionUID = -403250971215465050L;
    private static final byte[] staticLeapMonthLength = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final byte[] staticMonthLength = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private volatile transient Cache cache;
    private int dstSavings;
    private int endDay;
    private int endDayOfWeek;
    private int endMode;
    private int endMonth;
    private int endTime;
    private int endTimeMode;
    private final byte[] monthLength;
    private int rawOffset;
    private int serialVersionOnStream;
    private int startDay;
    private int startDayOfWeek;
    private int startMode;
    private int startMonth;
    private int startTime;
    private int startTimeMode;
    private int startYear;
    private boolean useDaylight;

    public SimpleTimeZone(int i, String str) {
        this.useDaylight = false;
        this.monthLength = staticMonthLength;
        this.serialVersionOnStream = 2;
        this.rawOffset = i;
        setID(str);
        this.dstSavings = millisPerHour;
    }

    public SimpleTimeZone(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this(i, str, i2, i3, i4, i5, 0, i6, i7, i8, i9, 0, millisPerHour);
    }

    public SimpleTimeZone(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        this(i, str, i2, i3, i4, i5, 0, i6, i7, i8, i9, 0, i10);
    }

    public SimpleTimeZone(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12) {
        this.useDaylight = false;
        this.monthLength = staticMonthLength;
        this.serialVersionOnStream = 2;
        setID(str);
        this.rawOffset = i;
        this.startMonth = i2;
        this.startDay = i3;
        this.startDayOfWeek = i4;
        this.startTime = i5;
        this.startTimeMode = i6;
        this.endMonth = i7;
        this.endDay = i8;
        this.endDayOfWeek = i9;
        this.endTime = i10;
        this.endTimeMode = i11;
        this.dstSavings = i12;
        decodeRules();
        if (i12 <= 0) {
            throw new IllegalArgumentException("Illegal daylight saving value: " + i12);
        }
    }

    public void setStartYear(int i) {
        this.startYear = i;
        invalidateCache();
    }

    public void setStartRule(int i, int i2, int i3, int i4) {
        this.startMonth = i;
        this.startDay = i2;
        this.startDayOfWeek = i3;
        this.startTime = i4;
        this.startTimeMode = 0;
        decodeStartRule();
        invalidateCache();
    }

    public void setStartRule(int i, int i2, int i3) {
        setStartRule(i, i2, 0, i3);
    }

    public void setStartRule(int i, int i2, int i3, int i4, boolean z) {
        if (z) {
            setStartRule(i, i2, -i3, i4);
        } else {
            setStartRule(i, -i2, -i3, i4);
        }
    }

    public void setEndRule(int i, int i2, int i3, int i4) {
        this.endMonth = i;
        this.endDay = i2;
        this.endDayOfWeek = i3;
        this.endTime = i4;
        this.endTimeMode = 0;
        decodeEndRule();
        invalidateCache();
    }

    public void setEndRule(int i, int i2, int i3) {
        setEndRule(i, i2, 0, i3);
    }

    public void setEndRule(int i, int i2, int i3, int i4, boolean z) {
        if (z) {
            setEndRule(i, i2, -i3, i4);
        } else {
            setEndRule(i, -i2, -i3, i4);
        }
    }

    public int getOffset(long j) {
        return getOffsets(j, (int[]) null);
    }

    /* access modifiers changed from: package-private */
    public int getOffsets(long j, int[] iArr) {
        int i = this.rawOffset;
        if (this.useDaylight) {
            Cache cache2 = this.cache;
            if (cache2 == null || j < cache2.start || j >= cache2.end) {
                BaseCalendar baseCalendar = j >= -12219292800000L ? gcal : (BaseCalendar) CalendarSystem.forName("julian");
                BaseCalendar.Date date = (BaseCalendar.Date) baseCalendar.newCalendarDate(TimeZone.NO_TIMEZONE);
                baseCalendar.getCalendarDate(((long) this.rawOffset) + j, (CalendarDate) date);
                int normalizedYear = date.getNormalizedYear();
                if (normalizedYear >= this.startYear) {
                    date.setTimeOfDay(0, 0, 0, 0);
                    i = getOffset(baseCalendar, date, normalizedYear, j);
                }
            } else {
                i += this.dstSavings;
            }
        }
        if (iArr != null) {
            int i2 = this.rawOffset;
            iArr[0] = i2;
            iArr[1] = i - i2;
        }
        return i;
    }

    public int getOffset(int i, int i2, int i3, int i4, int i5, int i6) {
        if (i == 1 || i == 0) {
            int i7 = i == 0 ? 1 - i2 : i2;
            if (i7 >= 292278994) {
                i7 = (i7 % 2800) + 2800;
            } else if (i7 <= -292269054) {
                i7 = (int) CalendarUtils.mod((long) i7, 28);
            }
            int i8 = i3 + 1;
            BaseCalendar baseCalendar = gcal;
            BaseCalendar.Date date = (BaseCalendar.Date) baseCalendar.newCalendarDate(TimeZone.NO_TIMEZONE);
            date.setDate(i7, i8, i4);
            long time = baseCalendar.getTime(date) + ((long) (i6 - this.rawOffset));
            if (time < -12219292800000L) {
                baseCalendar = (BaseCalendar) CalendarSystem.forName("julian");
                date = (BaseCalendar.Date) baseCalendar.newCalendarDate(TimeZone.NO_TIMEZONE);
                date.setNormalizedDate(i7, i8, i4);
                time = (baseCalendar.getTime(date) + ((long) i6)) - ((long) this.rawOffset);
            }
            if (date.getNormalizedYear() != i7 || date.getMonth() != i8 || date.getDayOfMonth() != i4 || i5 < 1 || i5 > 7 || i6 < 0 || i6 >= millisPerDay) {
                throw new IllegalArgumentException();
            } else if (!this.useDaylight || i2 < this.startYear || i != 1) {
                return this.rawOffset;
            } else {
                return getOffset(baseCalendar, date, i7, time);
            }
        } else {
            throw new IllegalArgumentException("Illegal era " + i);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x006d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getOffset(sun.util.calendar.BaseCalendar r9, sun.util.calendar.BaseCalendar.Date r10, int r11, long r12) {
        /*
            r8 = this;
            java.util.SimpleTimeZone$Cache r0 = r8.cache
            if (r0 == 0) goto L_0x0020
            long r1 = r0.start
            int r1 = (r12 > r1 ? 1 : (r12 == r1 ? 0 : -1))
            if (r1 < 0) goto L_0x0016
            long r1 = r0.end
            int r1 = (r12 > r1 ? 1 : (r12 == r1 ? 0 : -1))
            if (r1 >= 0) goto L_0x0016
            int r9 = r8.rawOffset
            int r8 = r8.dstSavings
            int r9 = r9 + r8
            return r9
        L_0x0016:
            long r1 = (long) r11
            long r3 = r0.year
            int r0 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x0020
            int r8 = r8.rawOffset
            return r8
        L_0x0020:
            long r3 = r8.getStart(r9, r10, r11)
            long r5 = r8.getEnd(r9, r10, r11)
            int r0 = r8.rawOffset
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 > 0) goto L_0x0044
            int r9 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1))
            if (r9 < 0) goto L_0x0039
            int r9 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r9 >= 0) goto L_0x0039
            int r9 = r8.dstSavings
            int r0 = r0 + r9
        L_0x0039:
            r9 = r0
            java.util.SimpleTimeZone$Cache r10 = new java.util.SimpleTimeZone$Cache
            long r1 = (long) r11
            r0 = r10
            r0.<init>(r1, r3, r5)
            r8.cache = r10
            goto L_0x007d
        L_0x0044:
            int r1 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r1 >= 0) goto L_0x0056
            int r11 = r11 + -1
            long r3 = r8.getStart(r9, r10, r11)
            int r9 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1))
            if (r9 < 0) goto L_0x0067
            int r9 = r8.dstSavings
        L_0x0054:
            int r0 = r0 + r9
            goto L_0x0067
        L_0x0056:
            int r1 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1))
            if (r1 < 0) goto L_0x0067
            int r11 = r11 + 1
            long r5 = r8.getEnd(r9, r10, r11)
            int r9 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r9 >= 0) goto L_0x0067
            int r9 = r8.dstSavings
            goto L_0x0054
        L_0x0067:
            r6 = r5
            r4 = r3
            int r9 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r9 > 0) goto L_0x007c
            java.util.SimpleTimeZone$Cache r9 = new java.util.SimpleTimeZone$Cache
            int r10 = r8.startYear
            long r10 = (long) r10
            r12 = 1
            long r2 = r10 - r12
            r1 = r9
            r1.<init>(r2, r4, r6)
            r8.cache = r9
        L_0x007c:
            r9 = r0
        L_0x007d:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.SimpleTimeZone.getOffset(sun.util.calendar.BaseCalendar, sun.util.calendar.BaseCalendar$Date, int, long):int");
    }

    private long getStart(BaseCalendar baseCalendar, BaseCalendar.Date date, int i) {
        int i2 = this.startTime;
        if (this.startTimeMode != 2) {
            i2 -= this.rawOffset;
        }
        return getTransition(baseCalendar, date, this.startMode, i, this.startMonth, this.startDay, this.startDayOfWeek, i2);
    }

    private long getEnd(BaseCalendar baseCalendar, BaseCalendar.Date date, int i) {
        int i2 = this.endTime;
        int i3 = this.endTimeMode;
        if (i3 != 2) {
            i2 -= this.rawOffset;
        }
        if (i3 == 0) {
            i2 -= this.dstSavings;
        }
        return getTransition(baseCalendar, date, this.endMode, i, this.endMonth, this.endDay, this.endDayOfWeek, i2);
    }

    /* JADX WARNING: type inference failed for: r0v4, types: [sun.util.calendar.CalendarDate] */
    /* JADX WARNING: type inference failed for: r0v6, types: [sun.util.calendar.CalendarDate] */
    /* JADX WARNING: type inference failed for: r0v9, types: [sun.util.calendar.CalendarDate] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long getTransition(sun.util.calendar.BaseCalendar r1, sun.util.calendar.BaseCalendar.Date r2, int r3, int r4, int r5, int r6, int r7, int r8) {
        /*
            r0 = this;
            r2.setNormalizedYear(r4)
            r0 = 1
            int r5 = r5 + r0
            r2.setMonth(r5)
            if (r3 == r0) goto L_0x003f
            r4 = 2
            if (r3 == r4) goto L_0x002b
            r4 = 3
            if (r3 == r4) goto L_0x0020
            r0 = 4
            if (r3 == r0) goto L_0x0014
            goto L_0x0042
        L_0x0014:
            r2.setDayOfMonth(r6)
            r0 = -1
            sun.util.calendar.CalendarDate r0 = r1.getNthDayOfWeek(r0, r7, r2)
            r2 = r0
            sun.util.calendar.BaseCalendar$Date r2 = (sun.util.calendar.BaseCalendar.Date) r2
            goto L_0x0042
        L_0x0020:
            r2.setDayOfMonth(r6)
            sun.util.calendar.CalendarDate r0 = r1.getNthDayOfWeek(r0, r7, r2)
            r2 = r0
            sun.util.calendar.BaseCalendar$Date r2 = (sun.util.calendar.BaseCalendar.Date) r2
            goto L_0x0042
        L_0x002b:
            r2.setDayOfMonth(r0)
            if (r6 >= 0) goto L_0x0037
            int r0 = r1.getMonthLength(r2)
            r2.setDayOfMonth(r0)
        L_0x0037:
            sun.util.calendar.CalendarDate r0 = r1.getNthDayOfWeek(r6, r7, r2)
            r2 = r0
            sun.util.calendar.BaseCalendar$Date r2 = (sun.util.calendar.BaseCalendar.Date) r2
            goto L_0x0042
        L_0x003f:
            r2.setDayOfMonth(r6)
        L_0x0042:
            long r0 = r1.getTime(r2)
            long r2 = (long) r8
            long r0 = r0 + r2
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.SimpleTimeZone.getTransition(sun.util.calendar.BaseCalendar, sun.util.calendar.BaseCalendar$Date, int, int, int, int, int, int):long");
    }

    public int getRawOffset() {
        return this.rawOffset;
    }

    public void setRawOffset(int i) {
        this.rawOffset = i;
    }

    public void setDSTSavings(int i) {
        if (i > 0) {
            this.dstSavings = i;
            return;
        }
        throw new IllegalArgumentException("Illegal daylight saving value: " + i);
    }

    public int getDSTSavings() {
        if (this.useDaylight) {
            return this.dstSavings;
        }
        return 0;
    }

    public boolean useDaylightTime() {
        return this.useDaylight;
    }

    public boolean observesDaylightTime() {
        return useDaylightTime();
    }

    public boolean inDaylightTime(Date date) {
        return getOffset(date.getTime()) != this.rawOffset;
    }

    public Object clone() {
        return super.clone();
    }

    public int hashCode() {
        return this.rawOffset ^ (((((((this.startMonth ^ this.startDay) ^ this.startDayOfWeek) ^ this.startTime) ^ this.endMonth) ^ this.endDay) ^ this.endDayOfWeek) ^ this.endTime);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SimpleTimeZone)) {
            return false;
        }
        SimpleTimeZone simpleTimeZone = (SimpleTimeZone) obj;
        if (!getID().equals(simpleTimeZone.getID()) || !hasSameRules(simpleTimeZone)) {
            return false;
        }
        return true;
    }

    public boolean hasSameRules(TimeZone timeZone) {
        boolean z;
        if (this == timeZone) {
            return true;
        }
        if (!(timeZone instanceof SimpleTimeZone)) {
            return false;
        }
        SimpleTimeZone simpleTimeZone = (SimpleTimeZone) timeZone;
        if (this.rawOffset == simpleTimeZone.rawOffset && (z = this.useDaylight) == simpleTimeZone.useDaylight) {
            if (!z) {
                return true;
            }
            if (this.dstSavings == simpleTimeZone.dstSavings && this.startMode == simpleTimeZone.startMode && this.startMonth == simpleTimeZone.startMonth && this.startDay == simpleTimeZone.startDay && this.startDayOfWeek == simpleTimeZone.startDayOfWeek && this.startTime == simpleTimeZone.startTime && this.startTimeMode == simpleTimeZone.startTimeMode && this.endMode == simpleTimeZone.endMode && this.endMonth == simpleTimeZone.endMonth && this.endDay == simpleTimeZone.endDay && this.endDayOfWeek == simpleTimeZone.endDayOfWeek && this.endTime == simpleTimeZone.endTime && this.endTimeMode == simpleTimeZone.endTimeMode && this.startYear == simpleTimeZone.startYear) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return getClass().getName() + "[id=" + getID() + ",offset=" + this.rawOffset + ",dstSavings=" + this.dstSavings + ",useDaylight=" + this.useDaylight + ",startYear=" + this.startYear + ",startMode=" + this.startMode + ",startMonth=" + this.startMonth + ",startDay=" + this.startDay + ",startDayOfWeek=" + this.startDayOfWeek + ",startTime=" + this.startTime + ",startTimeMode=" + this.startTimeMode + ",endMode=" + this.endMode + ",endMonth=" + this.endMonth + ",endDay=" + this.endDay + ",endDayOfWeek=" + this.endDayOfWeek + ",endTime=" + this.endTime + ",endTimeMode=" + this.endTimeMode + ']';
    }

    private static final class Cache {
        final long end;
        final long start;
        final long year;

        Cache(long j, long j2, long j3) {
            this.year = j;
            this.start = j2;
            this.end = j3;
        }
    }

    private void invalidateCache() {
        this.cache = null;
    }

    private void decodeRules() {
        decodeStartRule();
        decodeEndRule();
    }

    private void decodeStartRule() {
        int i = this.startDay;
        this.useDaylight = (i == 0 || this.endDay == 0) ? false : true;
        if (i != 0) {
            int i2 = this.startMonth;
            if (i2 < 0 || i2 > 11) {
                throw new IllegalArgumentException("Illegal start month " + this.startMonth);
            }
            int i3 = this.startTime;
            if (i3 < 0 || i3 > millisPerDay) {
                throw new IllegalArgumentException("Illegal start time " + this.startTime);
            }
            int i4 = this.startDayOfWeek;
            if (i4 == 0) {
                this.startMode = 1;
            } else {
                if (i4 > 0) {
                    this.startMode = 2;
                } else {
                    this.startDayOfWeek = -i4;
                    if (i > 0) {
                        this.startMode = 3;
                    } else {
                        this.startDay = -i;
                        this.startMode = 4;
                    }
                }
                if (this.startDayOfWeek > 7) {
                    throw new IllegalArgumentException("Illegal start day of week " + this.startDayOfWeek);
                }
            }
            if (this.startMode == 2) {
                int i5 = this.startDay;
                if (i5 < -5 || i5 > 5) {
                    throw new IllegalArgumentException("Illegal start day of week in month " + this.startDay);
                }
                return;
            }
            int i6 = this.startDay;
            if (i6 < 1 || i6 > staticMonthLength[i2]) {
                throw new IllegalArgumentException("Illegal start day " + this.startDay);
            }
        }
    }

    private void decodeEndRule() {
        this.useDaylight = (this.startDay == 0 || this.endDay == 0) ? false : true;
        int i = this.endDay;
        if (i != 0) {
            int i2 = this.endMonth;
            if (i2 < 0 || i2 > 11) {
                throw new IllegalArgumentException("Illegal end month " + this.endMonth);
            }
            int i3 = this.endTime;
            if (i3 < 0 || i3 > millisPerDay) {
                throw new IllegalArgumentException("Illegal end time " + this.endTime);
            }
            int i4 = this.endDayOfWeek;
            if (i4 == 0) {
                this.endMode = 1;
            } else {
                if (i4 > 0) {
                    this.endMode = 2;
                } else {
                    this.endDayOfWeek = -i4;
                    if (i > 0) {
                        this.endMode = 3;
                    } else {
                        this.endDay = -i;
                        this.endMode = 4;
                    }
                }
                if (this.endDayOfWeek > 7) {
                    throw new IllegalArgumentException("Illegal end day of week " + this.endDayOfWeek);
                }
            }
            if (this.endMode == 2) {
                int i5 = this.endDay;
                if (i5 < -5 || i5 > 5) {
                    throw new IllegalArgumentException("Illegal end day of week in month " + this.endDay);
                }
                return;
            }
            int i6 = this.endDay;
            if (i6 < 1 || i6 > staticMonthLength[i2]) {
                throw new IllegalArgumentException("Illegal end day " + this.endDay);
            }
        }
    }

    private void makeRulesCompatible() {
        int i = this.startMode;
        if (i == 1) {
            this.startDay = (this.startDay / 7) + 1;
            this.startDayOfWeek = 1;
        } else if (i == 3) {
            int i2 = this.startDay;
            if (i2 != 1) {
                this.startDay = (i2 / 7) + 1;
            }
        } else if (i == 4) {
            int i3 = this.startDay;
            if (i3 >= 30) {
                this.startDay = -1;
            } else {
                this.startDay = (i3 / 7) + 1;
            }
        }
        int i4 = this.endMode;
        if (i4 == 1) {
            this.endDay = (this.endDay / 7) + 1;
            this.endDayOfWeek = 1;
        } else if (i4 == 3) {
            int i5 = this.endDay;
            if (i5 != 1) {
                this.endDay = (i5 / 7) + 1;
            }
        } else if (i4 == 4) {
            int i6 = this.endDay;
            if (i6 >= 30) {
                this.endDay = -1;
            } else {
                this.endDay = (i6 / 7) + 1;
            }
        }
        if (this.startTimeMode == 2) {
            this.startTime += this.rawOffset;
        }
        while (true) {
            int i7 = this.startTime;
            if (i7 >= 0) {
                break;
            }
            this.startTime = i7 + millisPerDay;
            this.startDayOfWeek = ((this.startDayOfWeek + 5) % 7) + 1;
        }
        while (true) {
            int i8 = this.startTime;
            if (i8 < millisPerDay) {
                break;
            }
            this.startTime = i8 - millisPerDay;
            this.startDayOfWeek = (this.startDayOfWeek % 7) + 1;
        }
        int i9 = this.endTimeMode;
        if (i9 == 1) {
            this.endTime += this.dstSavings;
        } else if (i9 == 2) {
            this.endTime += this.rawOffset + this.dstSavings;
        }
        while (true) {
            int i10 = this.endTime;
            if (i10 >= 0) {
                break;
            }
            this.endTime = i10 + millisPerDay;
            this.endDayOfWeek = ((this.endDayOfWeek + 5) % 7) + 1;
        }
        while (true) {
            int i11 = this.endTime;
            if (i11 >= millisPerDay) {
                this.endTime = i11 - millisPerDay;
                this.endDayOfWeek = (this.endDayOfWeek % 7) + 1;
            } else {
                return;
            }
        }
    }

    private byte[] packRules() {
        return new byte[]{(byte) this.startDay, (byte) this.startDayOfWeek, (byte) this.endDay, (byte) this.endDayOfWeek, (byte) this.startTimeMode, (byte) this.endTimeMode};
    }

    private void unpackRules(byte[] bArr) {
        this.startDay = bArr[0];
        this.startDayOfWeek = bArr[1];
        this.endDay = bArr[2];
        this.endDayOfWeek = bArr[3];
        if (bArr.length >= 6) {
            this.startTimeMode = bArr[4];
            this.endTimeMode = bArr[5];
        }
    }

    private int[] packTimes() {
        return new int[]{this.startTime, this.endTime};
    }

    private void unpackTimes(int[] iArr) {
        this.startTime = iArr[0];
        this.endTime = iArr[1];
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        byte[] packRules = packRules();
        int[] packTimes = packTimes();
        makeRulesCompatible();
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(packRules.length);
        objectOutputStream.write(packRules);
        objectOutputStream.writeObject(packTimes);
        unpackRules(packRules);
        unpackTimes(packTimes);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            if (this.startDayOfWeek == 0) {
                this.startDayOfWeek = 1;
            }
            if (this.endDayOfWeek == 0) {
                this.endDayOfWeek = 1;
            }
            this.endMode = 2;
            this.startMode = 2;
            this.dstSavings = millisPerHour;
        } else {
            int readInt = objectInputStream.readInt();
            if (readInt <= 6) {
                byte[] bArr = new byte[readInt];
                objectInputStream.readFully(bArr);
                unpackRules(bArr);
            } else {
                throw new InvalidObjectException("Too many rules: " + readInt);
            }
        }
        if (this.serialVersionOnStream >= 2) {
            unpackTimes((int[]) objectInputStream.readObject());
        }
        this.serialVersionOnStream = 2;
    }
}
