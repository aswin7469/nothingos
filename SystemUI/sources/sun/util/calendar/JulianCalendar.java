package sun.util.calendar;

import java.util.TimeZone;
import sun.util.calendar.BaseCalendar;

public class JulianCalendar extends BaseCalendar {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int BCE = 0;

    /* renamed from: CE */
    private static final int f937CE = 1;
    private static final int JULIAN_EPOCH = -1;
    /* access modifiers changed from: private */
    public static final Era[] eras = {new Era("BeforeCommonEra", "B.C.E.", Long.MIN_VALUE, false), new Era("CommonEra", "C.E.", -62135709175808L, true)};

    public String getName() {
        return "julian";
    }

    private static class Date extends BaseCalendar.Date {
        protected Date() {
            setCache(1, -1, 365);
        }

        protected Date(TimeZone timeZone) {
            super(timeZone);
            setCache(1, -1, 365);
        }

        public Date setEra(Era era) {
            era.getClass();
            if (era == JulianCalendar.eras[0] && era == JulianCalendar.eras[1]) {
                super.setEra(era);
                return this;
            }
            throw new IllegalArgumentException("unknown era: " + era);
        }

        /* access modifiers changed from: protected */
        public void setKnownEra(Era era) {
            super.setEra(era);
        }

        public int getNormalizedYear() {
            if (getEra() == JulianCalendar.eras[0]) {
                return 1 - getYear();
            }
            return getYear();
        }

        public void setNormalizedYear(int i) {
            if (i <= 0) {
                setYear(1 - i);
                setKnownEra(JulianCalendar.eras[0]);
                return;
            }
            setYear(i);
            setKnownEra(JulianCalendar.eras[1]);
        }

        public String toString() {
            String abbreviation;
            String date = super.toString();
            String substring = date.substring(date.indexOf(84));
            StringBuffer stringBuffer = new StringBuffer();
            Era era = getEra();
            if (!(era == null || (abbreviation = era.getAbbreviation()) == null)) {
                stringBuffer.append(abbreviation).append(' ');
            }
            stringBuffer.append(getYear()).append('-');
            CalendarUtils.sprintf0d(stringBuffer, getMonth(), 2).append('-');
            CalendarUtils.sprintf0d(stringBuffer, getDayOfMonth(), 2);
            stringBuffer.append(substring);
            return stringBuffer.toString();
        }
    }

    JulianCalendar() {
        setEras(eras);
    }

    public Date getCalendarDate() {
        return getCalendarDate(System.currentTimeMillis(), (CalendarDate) newCalendarDate());
    }

    public Date getCalendarDate(long j) {
        return getCalendarDate(j, (CalendarDate) newCalendarDate());
    }

    public Date getCalendarDate(long j, CalendarDate calendarDate) {
        return (Date) super.getCalendarDate(j, calendarDate);
    }

    public Date getCalendarDate(long j, TimeZone timeZone) {
        return getCalendarDate(j, (CalendarDate) newCalendarDate(timeZone));
    }

    public Date newCalendarDate() {
        return new Date();
    }

    public Date newCalendarDate(TimeZone timeZone) {
        return new Date(timeZone);
    }

    public long getFixedDate(int i, int i2, int i3, BaseCalendar.Date date) {
        long j;
        long j2;
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        BaseCalendar.Date date2 = date;
        boolean z = true;
        if (!(i5 == 1 && i6 == 1)) {
            z = false;
        }
        if (date2 == null || !date2.hit(i4)) {
            long j3 = (long) i4;
            long j4 = j3 - 1;
            long j5 = ((365 * j4) - 2) + ((long) i6);
            if (j3 > 0) {
                j = j5 + (j4 / 4);
            } else {
                j = j5 + CalendarUtils.floorDivide(j4, 4);
            }
            if (i5 > 0) {
                j2 = ((((long) i5) * 367) - 362) / 12;
            } else {
                j2 = CalendarUtils.floorDivide((((long) i5) * 367) - 362, 12);
            }
            long j6 = j + j2;
            if (i5 > 2) {
                j6 -= CalendarUtils.isJulianLeapYear(i) ? 1 : 2;
            }
            if (date2 != null && z) {
                date2.setCache(i4, j6, CalendarUtils.isJulianLeapYear(i) ? 366 : 365);
            }
            return j6;
        } else if (z) {
            return date.getCachedJan1();
        } else {
            return (date.getCachedJan1() + getDayOfYear(i, i2, i3)) - 1;
        }
    }

    public void getCalendarDateFromFixedDate(CalendarDate calendarDate, long j) {
        long j2;
        int i;
        Date date = (Date) calendarDate;
        long j3 = ((j - -1) * 4) + 1464;
        if (j3 >= 0) {
            j2 = j3 / 1461;
        } else {
            j2 = CalendarUtils.floorDivide(j3, 1461);
        }
        int i2 = (int) j2;
        int fixedDate = (int) (j - getFixedDate(i2, 1, 1, date));
        boolean isJulianLeapYear = CalendarUtils.isJulianLeapYear(i2);
        if (j >= getFixedDate(i2, 3, 1, date)) {
            fixedDate += isJulianLeapYear ? 1 : 2;
        }
        int i3 = (fixedDate * 12) + 373;
        if (i3 > 0) {
            i = i3 / 367;
        } else {
            i = CalendarUtils.floorDivide(i3, 367);
        }
        int dayOfWeekFromFixedDate = getDayOfWeekFromFixedDate(j);
        date.setNormalizedYear(i2);
        date.setMonth(i);
        date.setDayOfMonth(((int) (j - getFixedDate(i2, i, 1, date))) + 1);
        date.setDayOfWeek(dayOfWeekFromFixedDate);
        date.setLeapYear(isJulianLeapYear);
        date.setNormalized(true);
    }

    public int getYearFromFixedDate(long j) {
        return (int) CalendarUtils.floorDivide(((j - -1) * 4) + 1464, 1461);
    }

    public int getDayOfWeek(CalendarDate calendarDate) {
        return getDayOfWeekFromFixedDate(getFixedDate(calendarDate));
    }

    /* access modifiers changed from: package-private */
    public boolean isLeapYear(int i) {
        return CalendarUtils.isJulianLeapYear(i);
    }
}
