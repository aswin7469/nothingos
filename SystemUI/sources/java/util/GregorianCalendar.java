package java.util;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Locale;
import libcore.util.ZoneInfo;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.CalendarUtils;
import sun.util.calendar.Era;
import sun.util.calendar.Gregorian;
import sun.util.calendar.JulianCalendar;

public class GregorianCalendar extends Calendar {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: AD */
    public static final int f683AD = 1;

    /* renamed from: BC */
    public static final int f684BC = 0;
    static final int BCE = 0;

    /* renamed from: CE */
    static final int f685CE = 1;
    static final long DEFAULT_GREGORIAN_CUTOVER = -12219292800000L;
    private static final int EPOCH_OFFSET = 719163;
    private static final int EPOCH_YEAR = 1970;
    static final int[] LEAP_MONTH_LENGTH = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static final int[] LEAST_MAX_VALUES = {1, 292269054, 11, 52, 4, 28, 365, 7, 4, 1, 11, 23, 59, 59, 999, 50400000, 1200000};
    static final int[] MAX_VALUES = {1, 292278994, 11, 53, 6, 31, 366, 7, 6, 1, 11, 23, 59, 59, 999, 50400000, 7200000};
    static final int[] MIN_VALUES = {0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, -46800000, 0};
    static final int[] MONTH_LENGTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final long ONE_DAY = 86400000;
    private static final int ONE_HOUR = 3600000;
    private static final int ONE_MINUTE = 60000;
    private static final int ONE_SECOND = 1000;
    private static final long ONE_WEEK = 604800000;
    private static final Gregorian gcal = CalendarSystem.getGregorianCalendar();
    private static JulianCalendar jcal = null;
    private static Era[] jeras = null;
    static final long serialVersionUID = -8125100834729963327L;
    private transient long cachedFixedDate;
    private transient BaseCalendar calsys;
    private transient BaseCalendar.Date cdate;
    private transient BaseCalendar.Date gdate;
    private long gregorianCutover;
    private transient long gregorianCutoverDate;
    private transient int gregorianCutoverYear;
    private transient int gregorianCutoverYearJulian;
    private transient int[] originalFields;
    private transient int[] zoneOffsets;

    public String getCalendarType() {
        return "gregory";
    }

    public final boolean isWeekDateSupported() {
        return true;
    }

    public GregorianCalendar() {
        this(TimeZone.getDefaultRef(), Locale.getDefault(Locale.Category.FORMAT));
        setZoneShared(true);
    }

    public GregorianCalendar(TimeZone timeZone) {
        this(timeZone, Locale.getDefault(Locale.Category.FORMAT));
    }

    public GregorianCalendar(Locale locale) {
        this(TimeZone.getDefaultRef(), locale);
        setZoneShared(true);
    }

    public GregorianCalendar(TimeZone timeZone, Locale locale) {
        super(timeZone, locale);
        this.gregorianCutover = DEFAULT_GREGORIAN_CUTOVER;
        this.gregorianCutoverDate = 577736;
        this.gregorianCutoverYear = 1582;
        this.gregorianCutoverYearJulian = 1582;
        this.cachedFixedDate = Long.MIN_VALUE;
        this.gdate = gcal.newCalendarDate(timeZone);
        setTimeInMillis(System.currentTimeMillis());
    }

    public GregorianCalendar(int i, int i2, int i3) {
        this(i, i2, i3, 0, 0, 0, 0);
    }

    public GregorianCalendar(int i, int i2, int i3, int i4, int i5) {
        this(i, i2, i3, i4, i5, 0, 0);
    }

    public GregorianCalendar(int i, int i2, int i3, int i4, int i5, int i6) {
        this(i, i2, i3, i4, i5, i6, 0);
    }

    GregorianCalendar(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.gregorianCutover = DEFAULT_GREGORIAN_CUTOVER;
        this.gregorianCutoverDate = 577736;
        this.gregorianCutoverYear = 1582;
        this.gregorianCutoverYearJulian = 1582;
        this.cachedFixedDate = Long.MIN_VALUE;
        this.gdate = gcal.newCalendarDate(getZone());
        set(1, i);
        set(2, i2);
        set(5, i3);
        if (i4 < 12 || i4 > 23) {
            internalSet(10, i4);
        } else {
            internalSet(9, 1);
            internalSet(10, i4 - 12);
        }
        setFieldsComputed(1536);
        set(11, i4);
        set(12, i5);
        set(13, i6);
        internalSet(14, i7);
    }

    GregorianCalendar(TimeZone timeZone, Locale locale, boolean z) {
        super(timeZone, locale);
        this.gregorianCutover = DEFAULT_GREGORIAN_CUTOVER;
        this.gregorianCutoverDate = 577736;
        this.gregorianCutoverYear = 1582;
        this.gregorianCutoverYearJulian = 1582;
        this.cachedFixedDate = Long.MIN_VALUE;
        this.gdate = gcal.newCalendarDate(getZone());
    }

    GregorianCalendar(long j) {
        this();
        setTimeInMillis(j);
    }

    public void setGregorianChange(Date date) {
        long time = date.getTime();
        if (time != this.gregorianCutover) {
            complete();
            setGregorianChange(time);
        }
    }

    private void setGregorianChange(long j) {
        this.gregorianCutover = j;
        long floorDivide = CalendarUtils.floorDivide(j, (long) ONE_DAY) + 719163;
        this.gregorianCutoverDate = floorDivide;
        if (j == Long.MAX_VALUE) {
            this.gregorianCutoverDate = floorDivide + 1;
        }
        this.gregorianCutoverYear = getGregorianCutoverDate().getYear();
        BaseCalendar julianCalendarSystem = getJulianCalendarSystem();
        BaseCalendar.Date date = (BaseCalendar.Date) julianCalendarSystem.newCalendarDate(TimeZone.NO_TIMEZONE);
        julianCalendarSystem.getCalendarDateFromFixedDate(date, this.gregorianCutoverDate - 1);
        this.gregorianCutoverYearJulian = date.getNormalizedYear();
        if (this.time < this.gregorianCutover) {
            setUnnormalized();
        }
    }

    public final Date getGregorianChange() {
        return new Date(this.gregorianCutover);
    }

    public boolean isLeapYear(int i) {
        if ((i & 3) != 0) {
            return false;
        }
        int i2 = this.gregorianCutoverYear;
        if (i <= i2) {
            int i3 = this.gregorianCutoverYearJulian;
            if (i < i3) {
                return true;
            }
            if (!(i2 != i3 ? i == i2 : getCalendarDate(this.gregorianCutoverDate).getMonth() < 3) || i % 100 != 0 || i % 400 == 0) {
                return true;
            }
            return false;
        } else if (i % 100 != 0 || i % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equals(Object obj) {
        return (obj instanceof GregorianCalendar) && super.equals(obj) && this.gregorianCutover == ((GregorianCalendar) obj).gregorianCutover;
    }

    public int hashCode() {
        return ((int) this.gregorianCutoverDate) ^ super.hashCode();
    }

    public void add(int i, int i2) {
        long j;
        long j2;
        int i3;
        int i4 = i;
        int i5 = i2;
        if (i5 != 0) {
            if (i4 < 0 || i4 >= 15) {
                throw new IllegalArgumentException();
            }
            complete();
            int i6 = 1;
            if (i4 == 1) {
                int internalGet = internalGet(1);
                if (internalGetEra() == 1) {
                    int i7 = internalGet + i5;
                    if (i7 > 0) {
                        set(1, i7);
                    } else {
                        set(1, 1 - i7);
                        set(0, 0);
                    }
                } else {
                    int i8 = internalGet - i5;
                    if (i8 > 0) {
                        set(1, i8);
                    } else {
                        set(1, 1 - i8);
                        set(0, 1);
                    }
                }
                pinDayOfMonth();
            } else if (i4 == 2) {
                int internalGet2 = internalGet(2) + i5;
                int internalGet3 = internalGet(1);
                if (internalGet2 >= 0) {
                    i3 = internalGet2 / 12;
                } else {
                    i3 = ((internalGet2 + 1) / 12) - 1;
                }
                if (i3 != 0) {
                    if (internalGetEra() == 1) {
                        int i9 = internalGet3 + i3;
                        if (i9 > 0) {
                            set(1, i9);
                        } else {
                            set(1, 1 - i9);
                            set(0, 0);
                        }
                    } else {
                        int i10 = internalGet3 - i3;
                        if (i10 > 0) {
                            set(1, i10);
                        } else {
                            set(1, 1 - i10);
                            set(0, 1);
                        }
                    }
                }
                if (internalGet2 >= 0) {
                    set(2, internalGet2 % 12);
                } else {
                    int i11 = internalGet2 % 12;
                    if (i11 < 0) {
                        i11 += 12;
                    }
                    set(2, i11 + 0);
                }
                pinDayOfMonth();
            } else if (i4 == 0) {
                int internalGet4 = internalGet(0) + i5;
                if (internalGet4 < 0) {
                    internalGet4 = 0;
                }
                if (internalGet4 <= 1) {
                    i6 = internalGet4;
                }
                set(0, i6);
            } else {
                long j3 = (long) i5;
                if (!(i4 == 3 || i4 == 4)) {
                    switch (i4) {
                        case 8:
                            break;
                        case 9:
                            j3 = (long) (i5 / 2);
                            j = (long) ((i5 % 2) * 12);
                            break;
                        case 10:
                        case 11:
                            j2 = 3600000;
                            break;
                        case 12:
                            j2 = 60000;
                            break;
                        case 13:
                            j3 *= 1000;
                            break;
                    }
                }
                j2 = 7;
                j3 *= j2;
                j = 0;
                if (i4 >= 10) {
                    setTimeInMillis(this.time + j3);
                    return;
                }
                long currentFixedDate = getCurrentFixedDate();
                long internalGet5 = ((((((j + ((long) internalGet(11))) * 60) + ((long) internalGet(12))) * 60) + ((long) internalGet(13))) * 1000) + ((long) internalGet(14));
                if (internalGet5 >= ONE_DAY) {
                    currentFixedDate++;
                    internalGet5 -= ONE_DAY;
                } else if (internalGet5 < 0) {
                    currentFixedDate--;
                    internalGet5 += ONE_DAY;
                }
                setTimeInMillis(adjustForZoneAndDaylightSavingsTime(0, (((currentFixedDate + j3) - 719163) * ONE_DAY) + internalGet5, getZone()));
            }
        }
    }

    public void roll(int i, boolean z) {
        roll(i, z ? 1 : -1);
    }

    public void roll(int i, int i2) {
        BaseCalendar baseCalendar;
        int i3;
        boolean z;
        long j;
        int i4;
        int internalGet;
        int i5 = i;
        int i6 = i2;
        if (i6 != 0) {
            if (i5 < 0 || i5 >= 15) {
                throw new IllegalArgumentException();
            }
            complete();
            int minimum = getMinimum(i);
            int maximum = getMaximum(i);
            switch (i5) {
                case 2:
                    if (!isCutoverYear(this.cdate.getNormalizedYear())) {
                        int internalGet2 = (internalGet(2) + i6) % 12;
                        if (internalGet2 < 0) {
                            internalGet2 += 12;
                        }
                        set(2, internalGet2);
                        int monthLength = monthLength(internalGet2);
                        if (internalGet(5) > monthLength) {
                            set(5, monthLength);
                            return;
                        }
                        return;
                    }
                    int actualMaximum = getActualMaximum(2) + 1;
                    int internalGet3 = (internalGet(2) + i6) % actualMaximum;
                    if (internalGet3 < 0) {
                        internalGet3 += actualMaximum;
                    }
                    set(2, internalGet3);
                    int actualMaximum2 = getActualMaximum(5);
                    if (internalGet(5) > actualMaximum2) {
                        set(5, actualMaximum2);
                        return;
                    }
                    return;
                case 3:
                    int normalizedYear = this.cdate.getNormalizedYear();
                    int actualMaximum3 = getActualMaximum(3);
                    set(7, internalGet(7));
                    int internalGet4 = internalGet(3);
                    int i7 = internalGet4 + i6;
                    if (!isCutoverYear(normalizedYear)) {
                        int weekYear = getWeekYear();
                        if (weekYear == normalizedYear) {
                            if (i7 <= minimum || i7 >= actualMaximum3) {
                                long currentFixedDate = getCurrentFixedDate();
                                if (this.calsys.getYearFromFixedDate(currentFixedDate - ((long) ((internalGet4 - minimum) * 7))) != normalizedYear) {
                                    minimum++;
                                }
                                if (this.calsys.getYearFromFixedDate(currentFixedDate + ((long) ((actualMaximum3 - internalGet(3)) * 7))) != normalizedYear) {
                                    actualMaximum3--;
                                }
                                i3 = minimum;
                                minimum = internalGet4;
                            } else {
                                set(3, i7);
                                return;
                            }
                        } else if (weekYear > normalizedYear) {
                            if (i6 < 0) {
                                i6++;
                            }
                            i3 = minimum;
                            minimum = actualMaximum3;
                        } else {
                            if (i6 > 0) {
                                i6 -= internalGet4 - actualMaximum3;
                            }
                            i3 = minimum;
                        }
                        set(i5, getRolledValue(minimum, i6, i3, actualMaximum3));
                        return;
                    }
                    long currentFixedDate2 = getCurrentFixedDate();
                    int i8 = this.gregorianCutoverYear;
                    if (i8 == this.gregorianCutoverYearJulian) {
                        baseCalendar = getCutoverCalendarSystem();
                    } else if (normalizedYear == i8) {
                        baseCalendar = gcal;
                    } else {
                        baseCalendar = getJulianCalendarSystem();
                    }
                    long j2 = currentFixedDate2 - ((long) ((internalGet4 - minimum) * 7));
                    if (baseCalendar.getYearFromFixedDate(j2) != normalizedYear) {
                        minimum++;
                    }
                    long j3 = currentFixedDate2 + ((long) ((actualMaximum3 - internalGet4) * 7));
                    if ((j3 >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem()).getYearFromFixedDate(j3) != normalizedYear) {
                        actualMaximum3--;
                    }
                    BaseCalendar.Date calendarDate = getCalendarDate(j2 + ((long) ((getRolledValue(internalGet4, i6, minimum, actualMaximum3) - 1) * 7)));
                    set(2, calendarDate.getMonth() - 1);
                    set(5, calendarDate.getDayOfMonth());
                    return;
                case 4:
                    boolean isCutoverYear = isCutoverYear(this.cdate.getNormalizedYear());
                    int internalGet5 = internalGet(7) - getFirstDayOfWeek();
                    if (internalGet5 < 0) {
                        internalGet5 += 7;
                    }
                    long currentFixedDate3 = getCurrentFixedDate();
                    if (isCutoverYear) {
                        j = getFixedDateMonth1(this.cdate, currentFixedDate3);
                        i4 = actualMonthLength();
                        z = isCutoverYear;
                    } else {
                        z = isCutoverYear;
                        j = (currentFixedDate3 - ((long) internalGet(5))) + 1;
                        i4 = this.calsys.getMonthLength(this.cdate);
                    }
                    long dayOfWeekDateOnOrBefore = BaseCalendar.getDayOfWeekDateOnOrBefore(6 + j, getFirstDayOfWeek());
                    if (((int) (dayOfWeekDateOnOrBefore - j)) >= getMinimalDaysInFirstWeek()) {
                        dayOfWeekDateOnOrBefore -= 7;
                    }
                    long rolledValue = dayOfWeekDateOnOrBefore + ((long) ((getRolledValue(internalGet(i), i6, 1, getActualMaximum(i)) - 1) * 7)) + ((long) internalGet5);
                    if (rolledValue < j) {
                        rolledValue = j;
                    } else {
                        long j4 = ((long) i4) + j;
                        if (rolledValue >= j4) {
                            rolledValue = j4 - 1;
                        }
                    }
                    set(5, z ? getCalendarDate(rolledValue).getDayOfMonth() : ((int) (rolledValue - j)) + 1);
                    return;
                case 5:
                    if (!isCutoverYear(this.cdate.getNormalizedYear())) {
                        maximum = this.calsys.getMonthLength(this.cdate);
                        break;
                    } else {
                        long currentFixedDate4 = getCurrentFixedDate();
                        long fixedDateMonth1 = getFixedDateMonth1(this.cdate, currentFixedDate4);
                        set(5, getCalendarDate(fixedDateMonth1 + ((long) getRolledValue((int) (currentFixedDate4 - fixedDateMonth1), i6, 0, actualMonthLength() - 1))).getDayOfMonth());
                        return;
                    }
                case 6:
                    maximum = getActualMaximum(i);
                    if (isCutoverYear(this.cdate.getNormalizedYear())) {
                        long currentFixedDate5 = getCurrentFixedDate();
                        long internalGet6 = (currentFixedDate5 - ((long) internalGet(6))) + 1;
                        BaseCalendar.Date calendarDate2 = getCalendarDate((internalGet6 + ((long) getRolledValue(((int) (currentFixedDate5 - internalGet6)) + 1, i6, minimum, maximum))) - 1);
                        set(2, calendarDate2.getMonth() - 1);
                        set(5, calendarDate2.getDayOfMonth());
                        return;
                    }
                    break;
                case 7:
                    if (!isCutoverYear(this.cdate.getNormalizedYear()) && (internalGet = internalGet(3)) > 1 && internalGet < 52) {
                        set(3, internalGet);
                        maximum = 7;
                        break;
                    } else {
                        int i9 = i6 % 7;
                        if (i9 != 0) {
                            long currentFixedDate6 = getCurrentFixedDate();
                            long dayOfWeekDateOnOrBefore2 = BaseCalendar.getDayOfWeekDateOnOrBefore(currentFixedDate6, getFirstDayOfWeek());
                            long j5 = currentFixedDate6 + ((long) i9);
                            if (j5 < dayOfWeekDateOnOrBefore2) {
                                j5 += 7;
                            } else if (j5 >= dayOfWeekDateOnOrBefore2 + 7) {
                                j5 -= 7;
                            }
                            BaseCalendar.Date calendarDate3 = getCalendarDate(j5);
                            set(0, calendarDate3.getNormalizedYear() <= 0 ? 0 : 1);
                            set(calendarDate3.getYear(), calendarDate3.getMonth() - 1, calendarDate3.getDayOfMonth());
                            return;
                        }
                        return;
                    }
                    break;
                case 8:
                    if (!isCutoverYear(this.cdate.getNormalizedYear())) {
                        int internalGet7 = internalGet(5);
                        int monthLength2 = this.calsys.getMonthLength(this.cdate);
                        int i10 = monthLength2 % 7;
                        int i11 = monthLength2 / 7;
                        if ((internalGet7 - 1) % 7 < i10) {
                            i11++;
                        }
                        maximum = i11;
                        set(7, internalGet(7));
                        minimum = 1;
                        break;
                    } else {
                        long currentFixedDate7 = getCurrentFixedDate();
                        long fixedDateMonth12 = getFixedDateMonth1(this.cdate, currentFixedDate7);
                        int actualMonthLength = actualMonthLength();
                        int i12 = actualMonthLength % 7;
                        int i13 = actualMonthLength / 7;
                        int i14 = ((int) (currentFixedDate7 - fixedDateMonth12)) % 7;
                        if (i14 < i12) {
                            i13++;
                        }
                        long rolledValue2 = fixedDateMonth12 + ((long) ((getRolledValue(internalGet(i), i6, 1, i13) - 1) * 7)) + ((long) i14);
                        BaseCalendar julianCalendarSystem = rolledValue2 >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem();
                        BaseCalendar.Date date = (BaseCalendar.Date) julianCalendarSystem.newCalendarDate(TimeZone.NO_TIMEZONE);
                        julianCalendarSystem.getCalendarDateFromFixedDate(date, rolledValue2);
                        set(5, date.getDayOfMonth());
                        return;
                    }
                case 10:
                case 11:
                    int rolledValue3 = getRolledValue(internalGet(i), i6, minimum, maximum);
                    int i15 = (i5 == 10 && internalGet(9) == 1) ? rolledValue3 + 12 : rolledValue3;
                    CalendarDate calendarDate4 = this.calsys.getCalendarDate(this.time, getZone());
                    calendarDate4.setHours(i15);
                    this.time = this.calsys.getTime(calendarDate4);
                    if (internalGet(11) == calendarDate4.getHours()) {
                        int rolledValue4 = getRolledValue(rolledValue3, i6 > 0 ? 1 : -1, minimum, maximum);
                        if (i5 == 10 && internalGet(9) == 1) {
                            rolledValue4 += 12;
                        }
                        calendarDate4.setHours(rolledValue4);
                        this.time = this.calsys.getTime(calendarDate4);
                    }
                    int hours = calendarDate4.getHours();
                    internalSet(11, hours);
                    internalSet(9, hours / 12);
                    internalSet(10, hours % 12);
                    int zoneOffset = calendarDate4.getZoneOffset();
                    int daylightSaving = calendarDate4.getDaylightSaving();
                    internalSet(15, zoneOffset - daylightSaving);
                    internalSet(16, daylightSaving);
                    return;
            }
            set(i5, getRolledValue(internalGet(i), i6, minimum, maximum));
        }
    }

    public int getMinimum(int i) {
        return MIN_VALUES[i];
    }

    public int getMaximum(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
                if (this.gregorianCutoverYear <= 200) {
                    GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
                    gregorianCalendar.setLenient(true);
                    gregorianCalendar.setTimeInMillis(this.gregorianCutover);
                    int actualMaximum = gregorianCalendar.getActualMaximum(i);
                    gregorianCalendar.setTimeInMillis(this.gregorianCutover - 1);
                    return Math.max(MAX_VALUES[i], Math.max(actualMaximum, gregorianCalendar.getActualMaximum(i)));
                }
                break;
        }
        return MAX_VALUES[i];
    }

    public int getGreatestMinimum(int i) {
        if (i != 5) {
            return MIN_VALUES[i];
        }
        return Math.max(MIN_VALUES[i], getCalendarDate(getFixedDateMonth1(getGregorianCutoverDate(), this.gregorianCutoverDate)).getDayOfMonth());
    }

    public int getLeastMaximum(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
                GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
                gregorianCalendar.setLenient(true);
                gregorianCalendar.setTimeInMillis(this.gregorianCutover);
                int actualMaximum = gregorianCalendar.getActualMaximum(i);
                gregorianCalendar.setTimeInMillis(this.gregorianCutover - 1);
                return Math.min(LEAST_MAX_VALUES[i], Math.min(actualMaximum, gregorianCalendar.getActualMaximum(i)));
            default:
                return LEAST_MAX_VALUES[i];
        }
    }

    public int getActualMinimum(int i) {
        if (i == 5) {
            GregorianCalendar normalizedCalendar = getNormalizedCalendar();
            int normalizedYear = normalizedCalendar.cdate.getNormalizedYear();
            if (normalizedYear == this.gregorianCutoverYear || normalizedYear == this.gregorianCutoverYearJulian) {
                BaseCalendar.Date date = normalizedCalendar.cdate;
                return getCalendarDate(getFixedDateMonth1(date, normalizedCalendar.calsys.getFixedDate(date))).getDayOfMonth();
            }
        }
        return getMinimum(i);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v12, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.util.GregorianCalendar} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v19, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.util.GregorianCalendar} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v20, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v19, resolved type: java.util.GregorianCalendar} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v30, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v40, resolved type: java.util.GregorianCalendar} */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x0247, code lost:
        if (r4 < r0) goto L_0x0249;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x01f8, code lost:
        if (r4 > r1.getYearOffsetInMillis()) goto L_0x0249;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getActualMaximum(int r14) {
        /*
            r13 = this;
            r0 = 1
            int r1 = r0 << r14
            r2 = 130689(0x1fe81, float:1.83134E-40)
            r1 = r1 & r2
            if (r1 == 0) goto L_0x000e
            int r13 = r13.getMaximum(r14)
            return r13
        L_0x000e:
            java.util.GregorianCalendar r1 = r13.getNormalizedCalendar()
            sun.util.calendar.BaseCalendar$Date r2 = r1.cdate
            sun.util.calendar.BaseCalendar r3 = r1.calsys
            int r4 = r2.getNormalizedYear()
            r5 = 5
            r6 = 3
            r7 = 1
            r9 = 6
            r10 = 0
            r11 = 7
            switch(r14) {
                case 1: goto L_0x01d3;
                case 2: goto L_0x01aa;
                case 3: goto L_0x0141;
                case 4: goto L_0x00d9;
                case 5: goto L_0x00a3;
                case 6: goto L_0x006d;
                case 7: goto L_0x0024;
                case 8: goto L_0x002a;
                default: goto L_0x0024;
            }
        L_0x0024:
            java.lang.ArrayIndexOutOfBoundsException r13 = new java.lang.ArrayIndexOutOfBoundsException
            r13.<init>((int) r14)
            throw r13
        L_0x002a:
            int r14 = r2.getDayOfWeek()
            boolean r4 = r1.isCutoverYear(r4)
            if (r4 != 0) goto L_0x0049
            java.lang.Object r13 = r2.clone()
            sun.util.calendar.BaseCalendar$Date r13 = (sun.util.calendar.BaseCalendar.Date) r13
            int r1 = r3.getMonthLength(r13)
            r13.setDayOfMonth(r0)
            r3.normalize(r13)
            int r13 = r13.getDayOfWeek()
            goto L_0x0063
        L_0x0049:
            if (r1 != r13) goto L_0x0052
            java.lang.Object r13 = r13.clone()
            r1 = r13
            java.util.GregorianCalendar r1 = (java.util.GregorianCalendar) r1
        L_0x0052:
            int r13 = r1.actualMonthLength()
            int r0 = r1.getActualMinimum(r5)
            r1.set(r5, r0)
            int r0 = r1.get(r11)
            r1 = r13
            r13 = r0
        L_0x0063:
            int r14 = r14 - r13
            if (r14 >= 0) goto L_0x0068
            int r14 = r14 + 7
        L_0x0068:
            int r1 = r1 - r14
            int r1 = r1 + r9
            int r1 = r1 / r11
            goto L_0x024d
        L_0x006d:
            boolean r14 = r1.isCutoverYear(r4)
            if (r14 != 0) goto L_0x0079
            int r1 = r3.getYearLength(r2)
            goto L_0x024d
        L_0x0079:
            int r14 = r13.gregorianCutoverYear
            int r2 = r13.gregorianCutoverYearJulian
            if (r14 != r2) goto L_0x0088
            sun.util.calendar.BaseCalendar r14 = r1.getCutoverCalendarSystem()
            long r1 = r14.getFixedDate(r4, r0, r0, r10)
            goto L_0x0091
        L_0x0088:
            if (r4 != r2) goto L_0x008f
            long r1 = r3.getFixedDate(r4, r0, r0, r10)
            goto L_0x0091
        L_0x008f:
            long r1 = r13.gregorianCutoverDate
        L_0x0091:
            sun.util.calendar.Gregorian r14 = gcal
            int r4 = r4 + r0
            long r3 = r14.getFixedDate(r4, r0, r0, r10)
            long r13 = r13.gregorianCutoverDate
            int r0 = (r3 > r13 ? 1 : (r3 == r13 ? 0 : -1))
            if (r0 >= 0) goto L_0x009f
            r3 = r13
        L_0x009f:
            long r3 = r3 - r1
            int r1 = (int) r3
            goto L_0x024d
        L_0x00a3:
            int r14 = r3.getMonthLength(r2)
            boolean r0 = r1.isCutoverYear(r4)
            if (r0 == 0) goto L_0x00d6
            int r0 = r2.getDayOfMonth()
            if (r0 != r14) goto L_0x00b4
            goto L_0x00d6
        L_0x00b4:
            long r2 = r1.getCurrentFixedDate()
            long r4 = r13.gregorianCutoverDate
            int r13 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r13 < 0) goto L_0x00bf
            goto L_0x00d6
        L_0x00bf:
            int r13 = r1.actualMonthLength()
            sun.util.calendar.BaseCalendar$Date r14 = r1.cdate
            long r2 = r1.getFixedDateMonth1(r14, r2)
            long r13 = (long) r13
            long r2 = r2 + r13
            long r2 = r2 - r7
            sun.util.calendar.BaseCalendar$Date r13 = r1.getCalendarDate(r2)
            int r1 = r13.getDayOfMonth()
            goto L_0x024d
        L_0x00d6:
            r1 = r14
            goto L_0x024d
        L_0x00d9:
            boolean r14 = r1.isCutoverYear(r4)
            r4 = 4
            if (r14 != 0) goto L_0x0119
            sun.util.calendar.CalendarDate r14 = r3.newCalendarDate(r10)
            int r1 = r2.getYear()
            int r2 = r2.getMonth()
            r14.setDate(r1, r2, r0)
            int r0 = r3.getDayOfWeek(r14)
            int r14 = r3.getMonthLength(r14)
            int r1 = r13.getFirstDayOfWeek()
            int r0 = r0 - r1
            if (r0 >= 0) goto L_0x0100
            int r0 = r0 + 7
        L_0x0100:
            int r0 = 7 - r0
            int r13 = r13.getMinimalDaysInFirstWeek()
            if (r0 < r13) goto L_0x0109
            r6 = r4
        L_0x0109:
            int r0 = r0 + 21
            int r14 = r14 - r0
            if (r14 <= 0) goto L_0x0116
            int r1 = r6 + 1
            if (r14 <= r11) goto L_0x024d
            int r1 = r1 + 1
            goto L_0x024d
        L_0x0116:
            r1 = r6
            goto L_0x024d
        L_0x0119:
            if (r1 != r13) goto L_0x0122
            java.lang.Object r13 = r1.clone()
            r1 = r13
            java.util.GregorianCalendar r1 = (java.util.GregorianCalendar) r1
        L_0x0122:
            int r13 = r1.internalGet(r0)
            r14 = 2
            int r2 = r1.internalGet(r14)
        L_0x012b:
            int r3 = r1.get(r4)
            r1.add(r4, r0)
            int r5 = r1.get(r0)
            if (r5 != r13) goto L_0x013e
            int r5 = r1.get(r14)
            if (r5 == r2) goto L_0x012b
        L_0x013e:
            r1 = r3
            goto L_0x024d
        L_0x0141:
            boolean r14 = r1.isCutoverYear(r4)
            if (r14 != 0) goto L_0x017e
            java.util.TimeZone r14 = java.util.TimeZone.NO_TIMEZONE
            sun.util.calendar.CalendarDate r14 = r3.newCalendarDate(r14)
            int r1 = r2.getYear()
            r14.setDate(r1, r0, r0)
            int r14 = r3.getDayOfWeek(r14)
            int r1 = r13.getFirstDayOfWeek()
            int r14 = r14 - r1
            if (r14 >= 0) goto L_0x0161
            int r14 = r14 + 7
        L_0x0161:
            int r13 = r13.getMinimalDaysInFirstWeek()
            int r14 = r14 + r13
            int r14 = r14 - r0
            if (r14 == r9) goto L_0x017a
            boolean r13 = r2.isLeapYear()
            if (r13 == 0) goto L_0x0176
            if (r14 == r5) goto L_0x017a
            r13 = 12
            if (r14 != r13) goto L_0x0176
            goto L_0x017a
        L_0x0176:
            r1 = 52
            goto L_0x024d
        L_0x017a:
            r1 = 53
            goto L_0x024d
        L_0x017e:
            if (r1 != r13) goto L_0x0187
            java.lang.Object r14 = r1.clone()
            r1 = r14
            java.util.GregorianCalendar r1 = (java.util.GregorianCalendar) r1
        L_0x0187:
            int r14 = r13.getActualMaximum(r9)
            r1.set(r9, r14)
            int r2 = r1.get(r6)
            int r13 = r13.internalGet(r0)
            int r0 = r1.getWeekYear()
            if (r13 == r0) goto L_0x01a7
            int r14 = r14 - r11
            r1.set(r9, r14)
            int r13 = r1.get(r6)
        L_0x01a4:
            r1 = r13
            goto L_0x024d
        L_0x01a7:
            r1 = r2
            goto L_0x024d
        L_0x01aa:
            boolean r14 = r1.isCutoverYear(r4)
            if (r14 != 0) goto L_0x01b4
            r1 = 11
            goto L_0x024d
        L_0x01b4:
            sun.util.calendar.Gregorian r14 = gcal
            int r4 = r4 + r0
            long r5 = r14.getFixedDate(r4, r0, r0, r10)
            long r11 = r13.gregorianCutoverDate
            int r14 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1))
            if (r14 < 0) goto L_0x01b4
            java.lang.Object r13 = r2.clone()
            sun.util.calendar.BaseCalendar$Date r13 = (sun.util.calendar.BaseCalendar.Date) r13
            long r5 = r5 - r7
            r3.getCalendarDateFromFixedDate(r13, r5)
            int r13 = r13.getMonth()
            int r1 = r13 + -1
            goto L_0x024d
        L_0x01d3:
            if (r1 != r13) goto L_0x01dc
            java.lang.Object r14 = r13.clone()
            r1 = r14
            java.util.GregorianCalendar r1 = (java.util.GregorianCalendar) r1
        L_0x01dc:
            long r4 = r1.getYearOffsetInMillis()
            int r14 = r1.internalGetEra()
            if (r14 != r0) goto L_0x01fb
            r13 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r1.setTimeInMillis(r13)
            int r13 = r1.get(r0)
            long r0 = r1.getYearOffsetInMillis()
            int r14 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r14 <= 0) goto L_0x01a4
            goto L_0x0249
        L_0x01fb:
            long r0 = r1.getTimeInMillis()
            long r9 = r13.gregorianCutover
            int r14 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r14 < 0) goto L_0x0208
            sun.util.calendar.Gregorian r14 = gcal
            goto L_0x020c
        L_0x0208:
            sun.util.calendar.BaseCalendar r14 = getJulianCalendarSystem()
        L_0x020c:
            r0 = -9223372036854775808
            java.util.TimeZone r13 = r13.getZone()
            sun.util.calendar.CalendarDate r13 = r14.getCalendarDate((long) r0, (java.util.TimeZone) r13)
            long r0 = r3.getDayOfYear(r13)
            long r0 = r0 - r7
            r2 = 24
            long r0 = r0 * r2
            int r14 = r13.getHours()
            long r2 = (long) r14
            long r0 = r0 + r2
            r2 = 60
            long r0 = r0 * r2
            int r14 = r13.getMinutes()
            long r6 = (long) r14
            long r0 = r0 + r6
            long r0 = r0 * r2
            int r14 = r13.getSeconds()
            long r2 = (long) r14
            long r0 = r0 + r2
            r2 = 1000(0x3e8, double:4.94E-321)
            long r0 = r0 * r2
            int r14 = r13.getMillis()
            long r2 = (long) r14
            long r0 = r0 + r2
            int r13 = r13.getYear()
            if (r13 > 0) goto L_0x0245
            int r13 = 1 - r13
        L_0x0245:
            int r14 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r14 >= 0) goto L_0x01a4
        L_0x0249:
            int r13 = r13 + -1
            goto L_0x01a4
        L_0x024d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.GregorianCalendar.getActualMaximum(int):int");
    }

    private long getYearOffsetInMillis() {
        return (((((((((long) ((internalGet(6) - 1) * 24)) + ((long) internalGet(11))) * 60) + ((long) internalGet(12))) * 60) + ((long) internalGet(13))) * 1000) + ((long) internalGet(14))) - ((long) (internalGet(15) + internalGet(16)));
    }

    public Object clone() {
        GregorianCalendar gregorianCalendar = (GregorianCalendar) super.clone();
        BaseCalendar.Date date = (BaseCalendar.Date) this.gdate.clone();
        gregorianCalendar.gdate = date;
        BaseCalendar.Date date2 = this.cdate;
        if (date2 != null) {
            if (date2 != this.gdate) {
                gregorianCalendar.cdate = (BaseCalendar.Date) date2.clone();
            } else {
                gregorianCalendar.cdate = date;
            }
        }
        gregorianCalendar.originalFields = null;
        gregorianCalendar.zoneOffsets = null;
        return gregorianCalendar;
    }

    public TimeZone getTimeZone() {
        TimeZone timeZone = super.getTimeZone();
        this.gdate.setZone(timeZone);
        BaseCalendar.Date date = this.cdate;
        if (!(date == null || date == this.gdate)) {
            date.setZone(timeZone);
        }
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        super.setTimeZone(timeZone);
        this.gdate.setZone(timeZone);
        BaseCalendar.Date date = this.cdate;
        if (date != null && date != this.gdate) {
            date.setZone(timeZone);
        }
    }

    public int getWeekYear() {
        int i = get(1);
        if (internalGetEra() == 0) {
            i = 1 - i;
        }
        if (i > this.gregorianCutoverYear + 1) {
            int internalGet = internalGet(3);
            return internalGet(2) == 0 ? internalGet >= 52 ? i - 1 : i : internalGet == 1 ? i + 1 : i;
        }
        int internalGet2 = internalGet(6);
        int actualMaximum = getActualMaximum(6);
        int minimalDaysInFirstWeek = getMinimalDaysInFirstWeek();
        if (internalGet2 > minimalDaysInFirstWeek && internalGet2 < actualMaximum - 6) {
            return i;
        }
        GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
        gregorianCalendar.setLenient(true);
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        gregorianCalendar.set(6, 1);
        gregorianCalendar.complete();
        int firstDayOfWeek = getFirstDayOfWeek() - gregorianCalendar.get(7);
        if (firstDayOfWeek != 0) {
            if (firstDayOfWeek < 0) {
                firstDayOfWeek += 7;
            }
            gregorianCalendar.add(6, firstDayOfWeek);
        }
        int i2 = gregorianCalendar.get(6);
        if (internalGet2 >= i2) {
            int i3 = i + 1;
            gregorianCalendar.set(1, i3);
            gregorianCalendar.set(6, 1);
            gregorianCalendar.complete();
            int firstDayOfWeek2 = getFirstDayOfWeek() - gregorianCalendar.get(7);
            if (firstDayOfWeek2 != 0) {
                if (firstDayOfWeek2 < 0) {
                    firstDayOfWeek2 += 7;
                }
                gregorianCalendar.add(6, firstDayOfWeek2);
            }
            int i4 = gregorianCalendar.get(6) - 1;
            if (i4 == 0) {
                i4 = 7;
            }
            return (i4 < minimalDaysInFirstWeek || (actualMaximum - internalGet2) + 1 > 7 - i4) ? i : i3;
        } else if (i2 <= minimalDaysInFirstWeek) {
            return i - 1;
        } else {
            return i;
        }
    }

    public void setWeekDate(int i, int i2, int i3) {
        if (i3 < 1 || i3 > 7) {
            throw new IllegalArgumentException("invalid dayOfWeek: " + i3);
        }
        GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
        gregorianCalendar.setLenient(true);
        int i4 = gregorianCalendar.get(0);
        gregorianCalendar.clear();
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        gregorianCalendar.set(0, i4);
        gregorianCalendar.set(1, i);
        gregorianCalendar.set(3, 1);
        gregorianCalendar.set(7, getFirstDayOfWeek());
        int firstDayOfWeek = i3 - getFirstDayOfWeek();
        if (firstDayOfWeek < 0) {
            firstDayOfWeek += 7;
        }
        int i5 = firstDayOfWeek + ((i2 - 1) * 7);
        if (i5 != 0) {
            gregorianCalendar.add(6, i5);
        } else {
            gregorianCalendar.complete();
        }
        if (isLenient() || (gregorianCalendar.getWeekYear() == i && gregorianCalendar.internalGet(3) == i2 && gregorianCalendar.internalGet(7) == i3)) {
            set(0, gregorianCalendar.internalGet(0));
            set(1, gregorianCalendar.internalGet(1));
            set(2, gregorianCalendar.internalGet(2));
            set(5, gregorianCalendar.internalGet(5));
            internalSet(3, i2);
            complete();
            return;
        }
        throw new IllegalArgumentException();
    }

    public int getWeeksInWeekYear() {
        GregorianCalendar normalizedCalendar = getNormalizedCalendar();
        int weekYear = normalizedCalendar.getWeekYear();
        if (weekYear == normalizedCalendar.internalGet(1)) {
            return normalizedCalendar.getActualMaximum(3);
        }
        if (normalizedCalendar == this) {
            normalizedCalendar = (GregorianCalendar) normalizedCalendar.clone();
        }
        normalizedCalendar.setWeekDate(weekYear, 2, internalGet(7));
        return normalizedCalendar.getActualMaximum(3);
    }

    /* access modifiers changed from: protected */
    public void computeFields() {
        int i = 131071;
        if (isPartiallyNormalized()) {
            int setStateFields = getSetStateFields();
            int i2 = 131071 & (~setStateFields);
            if (i2 != 0 || this.calsys == null) {
                setStateFields |= computeFields(i2, 98304 & setStateFields);
            }
            i = setStateFields;
        } else {
            computeFields(131071, 0);
        }
        setFieldsComputed(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0276, code lost:
        if (r10 >= (r12 - 7)) goto L_0x0278;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0278, code lost:
        r6 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x02a5, code lost:
        if (r10 >= (r12 - 7)) goto L_0x0278;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x01ec, code lost:
        if (sun.util.calendar.CalendarUtils.isGregorianLeapYear(r1 - r5) != false) goto L_0x01ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01fa, code lost:
        if (sun.util.calendar.CalendarUtils.isJulianLeapYear(r1 - r5) != false) goto L_0x01ee;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int computeFields(int r22, int r23) {
        /*
            r21 = this;
            r0 = r21
            r1 = r22
            r2 = r23
            java.util.TimeZone r3 = r21.getZone()
            int[] r4 = r0.zoneOffsets
            r5 = 2
            if (r4 != 0) goto L_0x0013
            int[] r4 = new int[r5]
            r0.zoneOffsets = r4
        L_0x0013:
            r4 = 98304(0x18000, float:1.37753E-40)
            r6 = 1
            r7 = 0
            if (r2 == r4) goto L_0x0041
            boolean r8 = r3 instanceof libcore.util.ZoneInfo
            if (r8 == 0) goto L_0x0029
            libcore.util.ZoneInfo r3 = (libcore.util.ZoneInfo) r3
            long r8 = r0.time
            int[] r10 = r0.zoneOffsets
            int r3 = r3.getOffsetsByUtcTime(r8, r10)
            goto L_0x0042
        L_0x0029:
            long r8 = r0.time
            int r8 = r3.getOffset(r8)
            int[] r9 = r0.zoneOffsets
            int r3 = r3.getRawOffset()
            r9[r7] = r3
            int[] r3 = r0.zoneOffsets
            r9 = r3[r7]
            int r9 = r8 - r9
            r3[r6] = r9
            r3 = r8
            goto L_0x0042
        L_0x0041:
            r3 = r7
        L_0x0042:
            r8 = 16
            r9 = 15
            if (r2 == 0) goto L_0x006b
            boolean r3 = isFieldSet(r2, r9)
            if (r3 == 0) goto L_0x0056
            int[] r3 = r0.zoneOffsets
            int r10 = r0.internalGet(r9)
            r3[r7] = r10
        L_0x0056:
            boolean r2 = isFieldSet(r2, r8)
            if (r2 == 0) goto L_0x0064
            int[] r2 = r0.zoneOffsets
            int r3 = r0.internalGet(r8)
            r2[r6] = r3
        L_0x0064:
            int[] r2 = r0.zoneOffsets
            r3 = r2[r7]
            r2 = r2[r6]
            int r3 = r3 + r2
        L_0x006b:
            long r10 = (long) r3
            r12 = 86400000(0x5265c00, double:4.2687272E-316)
            long r10 = r10 / r12
            r2 = 86400000(0x5265c00, float:7.82218E-36)
            int r3 = r3 % r2
            long r14 = r0.time
            long r14 = r14 / r12
            long r10 = r10 + r14
            long r14 = r0.time
            long r14 = r14 % r12
            int r2 = (int) r14
            int r3 = r3 + r2
            long r14 = (long) r3
            int r2 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1))
            r16 = 1
            if (r2 < 0) goto L_0x0089
            long r14 = r14 - r12
            int r2 = (int) r14
            long r10 = r10 + r16
            goto L_0x0092
        L_0x0089:
            if (r3 >= 0) goto L_0x0091
            long r2 = (long) r3
            long r2 = r2 + r12
            int r3 = (int) r2
            long r10 = r10 - r16
            goto L_0x0089
        L_0x0091:
            r2 = r3
        L_0x0092:
            r12 = 719163(0xaf93b, double:3.553137E-318)
            long r10 = r10 + r12
            long r12 = r0.gregorianCutoverDate
            int r3 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r3 < 0) goto L_0x00c1
            long r12 = r0.cachedFixedDate
            int r3 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r3 == 0) goto L_0x00ab
            sun.util.calendar.Gregorian r3 = gcal
            sun.util.calendar.BaseCalendar$Date r12 = r0.gdate
            r3.getCalendarDateFromFixedDate(r12, r10)
            r0.cachedFixedDate = r10
        L_0x00ab:
            sun.util.calendar.BaseCalendar$Date r3 = r0.gdate
            int r3 = r3.getYear()
            if (r3 > 0) goto L_0x00b7
            int r3 = 1 - r3
            r12 = r7
            goto L_0x00b8
        L_0x00b7:
            r12 = r6
        L_0x00b8:
            sun.util.calendar.Gregorian r13 = gcal
            r0.calsys = r13
            sun.util.calendar.BaseCalendar$Date r13 = r0.gdate
            r0.cdate = r13
            goto L_0x00ed
        L_0x00c1:
            sun.util.calendar.BaseCalendar r3 = getJulianCalendarSystem()
            r0.calsys = r3
            sun.util.calendar.JulianCalendar r3 = jcal
            java.util.TimeZone r12 = r21.getZone()
            sun.util.calendar.JulianCalendar$Date r3 = r3.newCalendarDate((java.util.TimeZone) r12)
            r0.cdate = r3
            sun.util.calendar.JulianCalendar r12 = jcal
            r12.getCalendarDateFromFixedDate(r3, r10)
            sun.util.calendar.BaseCalendar$Date r3 = r0.cdate
            sun.util.calendar.Era r3 = r3.getEra()
            sun.util.calendar.Era[] r12 = jeras
            r12 = r12[r7]
            if (r3 != r12) goto L_0x00e6
            r12 = r7
            goto L_0x00e7
        L_0x00e6:
            r12 = r6
        L_0x00e7:
            sun.util.calendar.BaseCalendar$Date r3 = r0.cdate
            int r3 = r3.getYear()
        L_0x00ed:
            r0.internalSet(r7, r12)
            r0.internalSet(r6, r3)
            r3 = r1 | 3
            sun.util.calendar.BaseCalendar$Date r12 = r0.cdate
            int r12 = r12.getMonth()
            int r12 = r12 - r6
            sun.util.calendar.BaseCalendar$Date r13 = r0.cdate
            int r13 = r13.getDayOfMonth()
            r14 = r1 & 164(0xa4, float:2.3E-43)
            r15 = 7
            if (r14 == 0) goto L_0x0119
            r0.internalSet(r5, r12)
            r5 = 5
            r0.internalSet(r5, r13)
            sun.util.calendar.BaseCalendar$Date r5 = r0.cdate
            int r5 = r5.getDayOfWeek()
            r0.internalSet(r15, r5)
            r3 = r3 | 164(0xa4, float:2.3E-43)
        L_0x0119:
            r5 = r1 & 32256(0x7e00, float:4.52E-41)
            if (r5 == 0) goto L_0x0166
            r5 = 14
            r12 = 13
            r14 = 10
            r15 = 9
            r8 = 11
            r6 = 12
            if (r2 == 0) goto L_0x0152
            r20 = 3600000(0x36ee80, float:5.044674E-39)
            int r9 = r2 / r20
            r0.internalSet(r8, r9)
            int r8 = r9 / 12
            r0.internalSet(r15, r8)
            int r9 = r9 % r6
            r0.internalSet(r14, r9)
            int r2 = r2 % r20
            r8 = 60000(0xea60, float:8.4078E-41)
            int r9 = r2 / r8
            r0.internalSet(r6, r9)
            int r2 = r2 % r8
            int r6 = r2 / 1000
            r0.internalSet(r12, r6)
            int r2 = r2 % 1000
            r0.internalSet(r5, r2)
            goto L_0x0164
        L_0x0152:
            r0.internalSet(r8, r7)
            r0.internalSet(r15, r7)
            r0.internalSet(r14, r7)
            r0.internalSet(r6, r7)
            r0.internalSet(r12, r7)
            r0.internalSet(r5, r7)
        L_0x0164:
            r3 = r3 | 32256(0x7e00, float:4.52E-41)
        L_0x0166:
            r2 = r1 & r4
            if (r2 == 0) goto L_0x017e
            int[] r2 = r0.zoneOffsets
            r2 = r2[r7]
            r5 = 15
            r0.internalSet(r5, r2)
            int[] r2 = r0.zoneOffsets
            r5 = 1
            r2 = r2[r5]
            r5 = 16
            r0.internalSet(r5, r2)
            r3 = r3 | r4
        L_0x017e:
            r1 = r1 & 344(0x158, float:4.82E-43)
            if (r1 == 0) goto L_0x02b7
            sun.util.calendar.BaseCalendar$Date r1 = r0.cdate
            int r1 = r1.getNormalizedYear()
            sun.util.calendar.BaseCalendar r2 = r0.calsys
            sun.util.calendar.BaseCalendar$Date r4 = r0.cdate
            r5 = 1
            long r6 = r2.getFixedDate(r1, r5, r5, r4)
            long r8 = r10 - r6
            int r2 = (int) r8
            int r2 = r2 + r5
            long r8 = (long) r13
            long r8 = r10 - r8
            long r8 = r8 + r16
            sun.util.calendar.BaseCalendar r4 = r0.calsys
            sun.util.calendar.Gregorian r12 = gcal
            if (r4 != r12) goto L_0x01a3
            int r4 = r0.gregorianCutoverYear
            goto L_0x01a5
        L_0x01a3:
            int r4 = r0.gregorianCutoverYearJulian
        L_0x01a5:
            int r13 = r13 - r5
            if (r1 != r4) goto L_0x01ca
            int r2 = r0.gregorianCutoverYearJulian
            int r5 = r0.gregorianCutoverYear
            if (r2 > r5) goto L_0x01c1
            sun.util.calendar.BaseCalendar$Date r2 = r0.cdate
            long r5 = r0.getFixedDateJan1(r2, r10)
            long r12 = r0.gregorianCutoverDate
            int r2 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r2 < 0) goto L_0x01c0
            sun.util.calendar.BaseCalendar$Date r2 = r0.cdate
            long r8 = r0.getFixedDateMonth1(r2, r10)
        L_0x01c0:
            r6 = r5
        L_0x01c1:
            long r12 = r10 - r6
            int r2 = (int) r12
            r5 = 1
            int r2 = r2 + r5
            long r12 = r10 - r8
            int r13 = (int) r12
            goto L_0x01cb
        L_0x01ca:
            r5 = 1
        L_0x01cb:
            r12 = 6
            r0.internalSet(r12, r2)
            r2 = 7
            int r13 = r13 / r2
            int r13 = r13 + r5
            r2 = 8
            r0.internalSet(r2, r13)
            int r2 = r0.getWeekNumber(r6, r10)
            r12 = 0
            if (r2 != 0) goto L_0x022d
            long r13 = r6 - r16
            r18 = 365(0x16d, double:1.803E-321)
            long r6 = r6 - r18
            int r4 = r4 + r5
            if (r1 <= r4) goto L_0x01f1
            int r1 = r1 - r5
            boolean r1 = sun.util.calendar.CalendarUtils.isGregorianLeapYear(r1)
            if (r1 == 0) goto L_0x0227
        L_0x01ee:
            long r6 = r6 - r16
            goto L_0x0227
        L_0x01f1:
            int r2 = r0.gregorianCutoverYearJulian
            if (r1 > r2) goto L_0x01fd
            int r1 = r1 - r5
            boolean r1 = sun.util.calendar.CalendarUtils.isJulianLeapYear(r1)
            if (r1 == 0) goto L_0x0227
            goto L_0x01ee
        L_0x01fd:
            sun.util.calendar.BaseCalendar$Date r1 = r0.getCalendarDate(r13)
            int r1 = r1.getNormalizedYear()
            int r2 = r0.gregorianCutoverYear
            if (r1 != r2) goto L_0x021a
            sun.util.calendar.BaseCalendar r2 = r21.getCutoverCalendarSystem()
            sun.util.calendar.JulianCalendar r4 = jcal
            if (r2 != r4) goto L_0x0217
            r4 = 1
            long r6 = r2.getFixedDate(r1, r4, r4, r12)
            goto L_0x0227
        L_0x0217:
            long r6 = r0.gregorianCutoverDate
            goto L_0x0227
        L_0x021a:
            r4 = 1
            int r2 = r0.gregorianCutoverYearJulian
            if (r1 > r2) goto L_0x0227
            sun.util.calendar.BaseCalendar r2 = getJulianCalendarSystem()
            long r6 = r2.getFixedDate(r1, r4, r4, r12)
        L_0x0227:
            int r6 = r0.getWeekNumber(r6, r13)
            goto L_0x02a9
        L_0x022d:
            int r4 = r0.gregorianCutoverYear
            if (r1 > r4) goto L_0x027a
            int r5 = r0.gregorianCutoverYearJulian
            int r13 = r5 + -1
            if (r1 >= r13) goto L_0x0238
            goto L_0x027a
        L_0x0238:
            sun.util.calendar.BaseCalendar r6 = r0.calsys
            r7 = 1
            int r1 = r1 + r7
            int r5 = r5 + r7
            if (r1 != r5) goto L_0x0242
            if (r1 >= r4) goto L_0x0242
            r1 = r4
        L_0x0242:
            if (r1 != r4) goto L_0x0248
            sun.util.calendar.BaseCalendar r6 = r21.getCutoverCalendarSystem()
        L_0x0248:
            int r4 = r0.gregorianCutoverYear
            if (r1 > r4) goto L_0x0258
            int r5 = r0.gregorianCutoverYearJulian
            if (r5 == r4) goto L_0x0258
            if (r1 != r5) goto L_0x0253
            goto L_0x0258
        L_0x0253:
            long r4 = r0.gregorianCutoverDate
            r5 = r4
            r4 = 1
            goto L_0x025d
        L_0x0258:
            r4 = 1
            long r5 = r6.getFixedDate(r1, r4, r4, r12)
        L_0x025d:
            r12 = 6
            long r12 = r12 + r5
            int r1 = r21.getFirstDayOfWeek()
            long r12 = sun.util.calendar.BaseCalendar.getDayOfWeekDateOnOrBefore(r12, r1)
            long r5 = r12 - r5
            int r1 = (int) r5
            int r5 = r21.getMinimalDaysInFirstWeek()
            if (r1 < r5) goto L_0x02a8
            r5 = 7
            long r12 = r12 - r5
            int r1 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r1 < 0) goto L_0x02a8
        L_0x0278:
            r6 = r4
            goto L_0x02a9
        L_0x027a:
            r4 = 1
            r1 = 52
            if (r2 < r1) goto L_0x02a8
            r12 = 365(0x16d, double:1.803E-321)
            long r6 = r6 + r12
            sun.util.calendar.BaseCalendar$Date r1 = r0.cdate
            boolean r1 = r1.isLeapYear()
            if (r1 == 0) goto L_0x028c
            long r6 = r6 + r16
        L_0x028c:
            r12 = 6
            long r12 = r12 + r6
            int r1 = r21.getFirstDayOfWeek()
            long r12 = sun.util.calendar.BaseCalendar.getDayOfWeekDateOnOrBefore(r12, r1)
            long r5 = r12 - r6
            int r1 = (int) r5
            int r5 = r21.getMinimalDaysInFirstWeek()
            if (r1 < r5) goto L_0x02a8
            r5 = 7
            long r12 = r12 - r5
            int r1 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r1 < 0) goto L_0x02a8
            goto L_0x0278
        L_0x02a8:
            r6 = r2
        L_0x02a9:
            r1 = 3
            r0.internalSet(r1, r6)
            r1 = 4
            int r2 = r0.getWeekNumber(r8, r10)
            r0.internalSet(r1, r2)
            r3 = r3 | 344(0x158, float:4.82E-43)
        L_0x02b7:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.GregorianCalendar.computeFields(int, int):int");
    }

    private int getWeekNumber(long j, long j2) {
        int floorDivide;
        long dayOfWeekDateOnOrBefore = Gregorian.getDayOfWeekDateOnOrBefore(6 + j, getFirstDayOfWeek());
        if (((int) (dayOfWeekDateOnOrBefore - j)) >= getMinimalDaysInFirstWeek()) {
            dayOfWeekDateOnOrBefore -= 7;
        }
        int i = (int) (j2 - dayOfWeekDateOnOrBefore);
        if (i >= 0) {
            floorDivide = i / 7;
        } else {
            floorDivide = CalendarUtils.floorDivide(i, 7);
        }
        return floorDivide + 1;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0121, code lost:
        if (r5 == r4) goto L_0x0137;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0135, code lost:
        if (r4 == null) goto L_0x0137;
     */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0167  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void computeTime() {
        /*
            r18 = this;
            r0 = r18
            boolean r1 = r18.isLenient()
            r2 = 17
            r3 = 0
            if (r1 != 0) goto L_0x003e
            int[] r1 = r0.originalFields
            if (r1 != 0) goto L_0x0013
            int[] r1 = new int[r2]
            r0.originalFields = r1
        L_0x0013:
            r1 = r3
        L_0x0014:
            if (r1 >= r2) goto L_0x003e
            int r4 = r0.internalGet(r1)
            boolean r5 = r0.isExternallySet(r1)
            if (r5 == 0) goto L_0x0037
            int r5 = r0.getMinimum(r1)
            if (r4 < r5) goto L_0x002d
            int r5 = r0.getMaximum(r1)
            if (r4 > r5) goto L_0x002d
            goto L_0x0037
        L_0x002d:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = getFieldName(r1)
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0037:
            int[] r5 = r0.originalFields
            r5[r1] = r4
            int r1 = r1 + 1
            goto L_0x0014
        L_0x003e:
            int r1 = r18.selectFields()
            r4 = 1
            boolean r5 = r0.isSet(r4)
            if (r5 == 0) goto L_0x004e
            int r5 = r0.internalGet(r4)
            goto L_0x0050
        L_0x004e:
            r5 = 1970(0x7b2, float:2.76E-42)
        L_0x0050:
            int r6 = r18.internalGetEra()
            if (r6 != 0) goto L_0x0059
            int r5 = 1 - r5
            goto L_0x005b
        L_0x0059:
            if (r6 != r4) goto L_0x01ce
        L_0x005b:
            if (r5 > 0) goto L_0x0068
            boolean r6 = r0.isSet(r3)
            if (r6 != 0) goto L_0x0068
            r1 = r1 | 1
            r0.setFieldsComputed(r4)
        L_0x0068:
            r4 = 11
            boolean r6 = isFieldSet(r1, r4)
            r7 = 12
            r8 = 0
            if (r6 == 0) goto L_0x007b
            int r4 = r0.internalGet(r4)
            long r10 = (long) r4
            long r10 = r10 + r8
            goto L_0x0092
        L_0x007b:
            r4 = 10
            int r4 = r0.internalGet(r4)
            long r10 = (long) r4
            long r10 = r10 + r8
            r4 = 9
            boolean r6 = isFieldSet(r1, r4)
            if (r6 == 0) goto L_0x0092
            int r4 = r0.internalGet(r4)
            int r4 = r4 * r7
            long r12 = (long) r4
            long r10 = r10 + r12
        L_0x0092:
            r12 = 60
            long r10 = r10 * r12
            int r4 = r0.internalGet(r7)
            long r6 = (long) r4
            long r10 = r10 + r6
            long r10 = r10 * r12
            r4 = 13
            int r4 = r0.internalGet(r4)
            long r6 = (long) r4
            long r10 = r10 + r6
            r6 = 1000(0x3e8, double:4.94E-321)
            long r10 = r10 * r6
            r4 = 14
            int r4 = r0.internalGet(r4)
            long r6 = (long) r4
            long r10 = r10 + r6
            r6 = 86400000(0x5265c00, double:4.2687272E-316)
            long r12 = r10 / r6
            long r10 = r10 % r6
        L_0x00b5:
            int r4 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r4 >= 0) goto L_0x00be
            long r10 = r10 + r6
            r14 = 1
            long r12 = r12 - r14
            goto L_0x00b5
        L_0x00be:
            int r4 = r0.gregorianCutoverYear
            if (r5 <= r4) goto L_0x00e4
            int r8 = r0.gregorianCutoverYearJulian
            if (r5 <= r8) goto L_0x00e4
            sun.util.calendar.Gregorian r4 = gcal
            long r8 = r0.getFixedDate(r4, r5, r1)
            long r8 = r8 + r12
            long r14 = r0.gregorianCutoverDate
            int r4 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r4 < 0) goto L_0x00d5
            goto L_0x0144
        L_0x00d5:
            sun.util.calendar.BaseCalendar r4 = getJulianCalendarSystem()
            long r14 = r0.getFixedDate(r4, r5, r1)
            long r12 = r12 + r14
            r16 = r8
            r8 = r12
            r12 = r16
            goto L_0x010c
        L_0x00e4:
            if (r5 >= r4) goto L_0x00fc
            int r4 = r0.gregorianCutoverYearJulian
            if (r5 >= r4) goto L_0x00fc
            sun.util.calendar.BaseCalendar r4 = getJulianCalendarSystem()
            long r8 = r0.getFixedDate(r4, r5, r1)
            long r8 = r8 + r12
            long r12 = r0.gregorianCutoverDate
            int r4 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r4 >= 0) goto L_0x00fa
            goto L_0x0144
        L_0x00fa:
            r12 = r8
            goto L_0x010c
        L_0x00fc:
            sun.util.calendar.BaseCalendar r4 = getJulianCalendarSystem()
            long r8 = r0.getFixedDate(r4, r5, r1)
            long r8 = r8 + r12
            sun.util.calendar.Gregorian r4 = gcal
            long r14 = r0.getFixedDate(r4, r5, r1)
            long r12 = r12 + r14
        L_0x010c:
            r4 = 6
            boolean r4 = isFieldSet(r1, r4)
            if (r4 != 0) goto L_0x011a
            r4 = 3
            boolean r4 = isFieldSet(r1, r4)
            if (r4 == 0) goto L_0x0124
        L_0x011a:
            int r4 = r0.gregorianCutoverYear
            int r14 = r0.gregorianCutoverYearJulian
            if (r4 != r14) goto L_0x0121
            goto L_0x0144
        L_0x0121:
            if (r5 != r4) goto L_0x0124
            goto L_0x0137
        L_0x0124:
            long r4 = r0.gregorianCutoverDate
            int r14 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1))
            if (r14 < 0) goto L_0x0139
            int r4 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r4 < 0) goto L_0x012f
            goto L_0x0137
        L_0x012f:
            sun.util.calendar.BaseCalendar r4 = r0.calsys
            sun.util.calendar.Gregorian r5 = gcal
            if (r4 == r5) goto L_0x0137
            if (r4 != 0) goto L_0x0144
        L_0x0137:
            r8 = r12
            goto L_0x0144
        L_0x0139:
            int r4 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r4 >= 0) goto L_0x013e
            goto L_0x0144
        L_0x013e:
            boolean r4 = r18.isLenient()
            if (r4 == 0) goto L_0x01c6
        L_0x0144:
            r4 = 719163(0xaf93b, double:3.553137E-318)
            long r8 = r8 - r4
            long r8 = r8 * r6
            long r8 = r8 + r10
            java.util.TimeZone r4 = r18.getZone()
            r5 = 98304(0x18000, float:1.37753E-40)
            r5 = r5 & r1
            long r6 = r0.adjustForZoneAndDaylightSavingsTime(r5, r8, r4)
            r0.time = r6
            int r4 = r18.getSetStateFields()
            r1 = r1 | r4
            int r1 = r0.computeFields(r1, r5)
            boolean r4 = r18.isLenient()
            if (r4 != 0) goto L_0x01c2
            r4 = r3
        L_0x0168:
            if (r4 >= r2) goto L_0x01c2
            boolean r5 = r0.isExternallySet(r4)
            if (r5 != 0) goto L_0x0171
            goto L_0x017b
        L_0x0171:
            int[] r5 = r0.originalFields
            r5 = r5[r4]
            int r6 = r0.internalGet(r4)
            if (r5 != r6) goto L_0x017e
        L_0x017b:
            int r4 = r4 + 1
            goto L_0x0168
        L_0x017e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            int[] r2 = r0.originalFields
            r2 = r2[r4]
            r1.append((int) r2)
            java.lang.String r2 = " -> "
            r1.append((java.lang.String) r2)
            int r2 = r0.internalGet(r4)
            r1.append((int) r2)
            java.lang.String r1 = r1.toString()
            int[] r2 = r0.originalFields
            int[] r5 = r0.fields
            int[] r0 = r0.fields
            int r0 = r0.length
            java.lang.System.arraycopy((java.lang.Object) r2, (int) r3, (java.lang.Object) r5, (int) r3, (int) r0)
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = getFieldName(r4)
            r2.append((java.lang.String) r3)
            java.lang.String r3 = ": "
            r2.append((java.lang.String) r3)
            r2.append((java.lang.String) r1)
            java.lang.String r1 = r2.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x01c2:
            r0.setFieldsNormalized(r1)
            return
        L_0x01c6:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "the specified date doesn't exist"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x01ce:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Invalid era"
            r0.<init>((java.lang.String) r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.GregorianCalendar.computeTime():void");
    }

    private long adjustForZoneAndDaylightSavingsTime(int i, long j, TimeZone timeZone) {
        int i2;
        int i3;
        int i4 = 0;
        if (i != 98304) {
            if (this.zoneOffsets == null) {
                this.zoneOffsets = new int[2];
            }
            if (isFieldSet(i, 15)) {
                i3 = internalGet(15);
            } else {
                i3 = timeZone.getRawOffset();
            }
            long j2 = j - ((long) i3);
            if (timeZone instanceof ZoneInfo) {
                ((ZoneInfo) timeZone).getOffsetsByUtcTime(j2, this.zoneOffsets);
            } else {
                timeZone.getOffsets(j2, this.zoneOffsets);
            }
            int[] iArr = this.zoneOffsets;
            i4 = iArr[0];
            i2 = adjustDstOffsetForInvalidWallClock(j2, timeZone, iArr[1]);
        } else {
            i2 = 0;
        }
        if (i != 0) {
            if (isFieldSet(i, 15)) {
                i4 = internalGet(15);
            }
            if (isFieldSet(i, 16)) {
                i2 = internalGet(16);
            }
        }
        return (j - ((long) i4)) - ((long) i2);
    }

    private int adjustDstOffsetForInvalidWallClock(long j, TimeZone timeZone, int i) {
        if (i == 0 || timeZone.inDaylightTime(new Date(j - ((long) i)))) {
            return i;
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00d2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long getFixedDate(sun.util.calendar.BaseCalendar r19, int r20, int r21) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = r21
            r3 = 2
            boolean r4 = isFieldSet(r2, r3)
            r5 = 1
            r6 = 0
            if (r4 == 0) goto L_0x0033
            int r4 = r0.internalGet(r3)
            r7 = 11
            if (r4 <= r7) goto L_0x0023
            int r6 = r4 / 12
            int r6 = r20 + r6
            int r4 = r4 % 12
            r17 = r6
            r6 = r4
            r4 = r17
            goto L_0x0035
        L_0x0023:
            if (r4 >= 0) goto L_0x0032
            int[] r7 = new int[r5]
            r8 = 12
            int r4 = sun.util.calendar.CalendarUtils.floorDivide((int) r4, (int) r8, (int[]) r7)
            int r4 = r20 + r4
            r6 = r7[r6]
            goto L_0x0035
        L_0x0032:
            r6 = r4
        L_0x0033:
            r4 = r20
        L_0x0035:
            int r7 = r6 + 1
            sun.util.calendar.Gregorian r8 = gcal
            if (r1 != r8) goto L_0x003e
            sun.util.calendar.BaseCalendar$Date r9 = r0.gdate
            goto L_0x003f
        L_0x003e:
            r9 = 0
        L_0x003f:
            long r9 = r1.getFixedDate(r4, r7, r5, r9)
            boolean r3 = isFieldSet(r2, r3)
            r11 = 7
            r13 = 6
            r15 = 1
            r7 = 7
            if (r3 == 0) goto L_0x00d2
            r1 = 5
            boolean r3 = isFieldSet(r2, r1)
            if (r3 == 0) goto L_0x0063
            boolean r2 = r0.isSet(r1)
            if (r2 == 0) goto L_0x0127
            int r0 = r0.internalGet(r1)
            goto L_0x00ee
        L_0x0063:
            r1 = 4
            boolean r3 = isFieldSet(r2, r1)
            if (r3 == 0) goto L_0x0099
            long r3 = r9 + r13
            int r6 = r18.getFirstDayOfWeek()
            long r3 = sun.util.calendar.BaseCalendar.getDayOfWeekDateOnOrBefore(r3, r6)
            long r8 = r3 - r9
            int r6 = r18.getMinimalDaysInFirstWeek()
            long r5 = (long) r6
            int r5 = (r8 > r5 ? 1 : (r8 == r5 ? 0 : -1))
            if (r5 < 0) goto L_0x0080
            long r3 = r3 - r11
        L_0x0080:
            boolean r2 = isFieldSet(r2, r7)
            if (r2 == 0) goto L_0x008f
            long r3 = r3 + r13
            int r2 = r0.internalGet(r7)
            long r3 = sun.util.calendar.BaseCalendar.getDayOfWeekDateOnOrBefore(r3, r2)
        L_0x008f:
            int r0 = r0.internalGet(r1)
            r1 = 1
            int r0 = r0 - r1
            int r0 = r0 * r7
            long r0 = (long) r0
            goto L_0x0125
        L_0x0099:
            boolean r1 = isFieldSet(r2, r7)
            if (r1 == 0) goto L_0x00a4
            int r1 = r0.internalGet(r7)
            goto L_0x00a8
        L_0x00a4:
            int r1 = r18.getFirstDayOfWeek()
        L_0x00a8:
            r3 = 8
            boolean r2 = isFieldSet(r2, r3)
            if (r2 == 0) goto L_0x00b5
            int r2 = r0.internalGet(r3)
            goto L_0x00b6
        L_0x00b5:
            r2 = 1
        L_0x00b6:
            if (r2 < 0) goto L_0x00c1
            int r2 = r2 * r7
            long r2 = (long) r2
            long r9 = r9 + r2
            long r9 = r9 - r15
            long r0 = sun.util.calendar.BaseCalendar.getDayOfWeekDateOnOrBefore(r9, r1)
            goto L_0x00d0
        L_0x00c1:
            int r0 = r0.monthLength(r6, r4)
            r3 = 1
            int r2 = r2 + r3
            int r2 = r2 * r7
            int r0 = r0 + r2
            long r2 = (long) r0
            long r9 = r9 + r2
            long r9 = r9 - r15
            long r0 = sun.util.calendar.BaseCalendar.getDayOfWeekDateOnOrBefore(r9, r1)
        L_0x00d0:
            r9 = r0
            goto L_0x0127
        L_0x00d2:
            int r3 = r0.gregorianCutoverYear
            if (r4 != r3) goto L_0x00e3
            if (r1 != r8) goto L_0x00e3
            long r4 = r0.gregorianCutoverDate
            int r1 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            if (r1 >= 0) goto L_0x00e3
            int r1 = r0.gregorianCutoverYearJulian
            if (r3 == r1) goto L_0x00e3
            r9 = r4
        L_0x00e3:
            r1 = 6
            boolean r3 = isFieldSet(r2, r1)
            if (r3 == 0) goto L_0x00f2
            int r0 = r0.internalGet(r1)
        L_0x00ee:
            long r0 = (long) r0
            long r9 = r9 + r0
            long r9 = r9 - r15
            goto L_0x0127
        L_0x00f2:
            long r3 = r9 + r13
            int r1 = r18.getFirstDayOfWeek()
            long r3 = sun.util.calendar.BaseCalendar.getDayOfWeekDateOnOrBefore(r3, r1)
            long r5 = r3 - r9
            int r1 = r18.getMinimalDaysInFirstWeek()
            long r8 = (long) r1
            int r1 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1))
            if (r1 < 0) goto L_0x0108
            long r3 = r3 - r11
        L_0x0108:
            boolean r1 = isFieldSet(r2, r7)
            if (r1 == 0) goto L_0x011d
            int r1 = r0.internalGet(r7)
            int r2 = r18.getFirstDayOfWeek()
            if (r1 == r2) goto L_0x011d
            long r3 = r3 + r13
            long r3 = sun.util.calendar.BaseCalendar.getDayOfWeekDateOnOrBefore(r3, r1)
        L_0x011d:
            r1 = 3
            int r0 = r0.internalGet(r1)
            long r0 = (long) r0
            long r0 = r0 - r15
            long r0 = r0 * r11
        L_0x0125:
            long r9 = r3 + r0
        L_0x0127:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.GregorianCalendar.getFixedDate(sun.util.calendar.BaseCalendar, int, int):long");
    }

    private GregorianCalendar getNormalizedCalendar() {
        if (isFullyNormalized()) {
            return this;
        }
        GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
        gregorianCalendar.setLenient(true);
        gregorianCalendar.complete();
        return gregorianCalendar;
    }

    private static synchronized BaseCalendar getJulianCalendarSystem() {
        JulianCalendar julianCalendar;
        synchronized (GregorianCalendar.class) {
            if (jcal == null) {
                JulianCalendar julianCalendar2 = (JulianCalendar) CalendarSystem.forName("julian");
                jcal = julianCalendar2;
                jeras = julianCalendar2.getEras();
            }
            julianCalendar = jcal;
        }
        return julianCalendar;
    }

    private BaseCalendar getCutoverCalendarSystem() {
        if (this.gregorianCutoverYearJulian < this.gregorianCutoverYear) {
            return gcal;
        }
        return getJulianCalendarSystem();
    }

    private boolean isCutoverYear(int i) {
        return i == (this.calsys == gcal ? this.gregorianCutoverYear : this.gregorianCutoverYearJulian);
    }

    private long getFixedDateJan1(BaseCalendar.Date date, long j) {
        if (this.gregorianCutoverYear != this.gregorianCutoverYearJulian) {
            long j2 = this.gregorianCutoverDate;
            if (j >= j2) {
                return j2;
            }
        }
        return getJulianCalendarSystem().getFixedDate(date.getNormalizedYear(), 1, 1, (BaseCalendar.Date) null);
    }

    private long getFixedDateMonth1(BaseCalendar.Date date, long j) {
        BaseCalendar.Date gregorianCutoverDate2 = getGregorianCutoverDate();
        if (gregorianCutoverDate2.getMonth() == 1 && gregorianCutoverDate2.getDayOfMonth() == 1) {
            return (j - ((long) date.getDayOfMonth())) + 1;
        }
        if (date.getMonth() != gregorianCutoverDate2.getMonth()) {
            return (j - ((long) date.getDayOfMonth())) + 1;
        }
        BaseCalendar.Date lastJulianDate = getLastJulianDate();
        if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian && gregorianCutoverDate2.getMonth() == lastJulianDate.getMonth()) {
            return jcal.getFixedDate(date.getNormalizedYear(), date.getMonth(), 1, (BaseCalendar.Date) null);
        }
        return this.gregorianCutoverDate;
    }

    private BaseCalendar.Date getCalendarDate(long j) {
        BaseCalendar julianCalendarSystem = j >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem();
        BaseCalendar.Date date = (BaseCalendar.Date) julianCalendarSystem.newCalendarDate(TimeZone.NO_TIMEZONE);
        julianCalendarSystem.getCalendarDateFromFixedDate(date, j);
        return date;
    }

    private BaseCalendar.Date getGregorianCutoverDate() {
        return getCalendarDate(this.gregorianCutoverDate);
    }

    private BaseCalendar.Date getLastJulianDate() {
        return getCalendarDate(this.gregorianCutoverDate - 1);
    }

    private int monthLength(int i, int i2) {
        return isLeapYear(i2) ? LEAP_MONTH_LENGTH[i] : MONTH_LENGTH[i];
    }

    private int monthLength(int i) {
        int internalGet = internalGet(1);
        if (internalGetEra() == 0) {
            internalGet = 1 - internalGet;
        }
        return monthLength(i, internalGet);
    }

    private int actualMonthLength() {
        int normalizedYear = this.cdate.getNormalizedYear();
        if (normalizedYear != this.gregorianCutoverYear && normalizedYear != this.gregorianCutoverYearJulian) {
            return this.calsys.getMonthLength(this.cdate);
        }
        BaseCalendar.Date date = (BaseCalendar.Date) this.cdate.clone();
        long fixedDateMonth1 = getFixedDateMonth1(date, this.calsys.getFixedDate(date));
        long monthLength = ((long) this.calsys.getMonthLength(date)) + fixedDateMonth1;
        if (monthLength < this.gregorianCutoverDate) {
            return (int) (monthLength - fixedDateMonth1);
        }
        if (this.cdate != this.gdate) {
            date = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
        }
        gcal.getCalendarDateFromFixedDate(date, monthLength);
        return (int) (getFixedDateMonth1(date, monthLength) - fixedDateMonth1);
    }

    private int yearLength(int i) {
        return isLeapYear(i) ? 366 : 365;
    }

    private int yearLength() {
        int internalGet = internalGet(1);
        if (internalGetEra() == 0) {
            internalGet = 1 - internalGet;
        }
        return yearLength(internalGet);
    }

    private void pinDayOfMonth() {
        int i;
        int internalGet = internalGet(1);
        if (internalGet > this.gregorianCutoverYear || internalGet < this.gregorianCutoverYearJulian) {
            i = monthLength(internalGet(2));
        } else {
            i = getNormalizedCalendar().getActualMaximum(5);
        }
        if (internalGet(5) > i) {
            set(5, i);
        }
    }

    private long getCurrentFixedDate() {
        BaseCalendar baseCalendar = this.calsys;
        return baseCalendar == gcal ? this.cachedFixedDate : baseCalendar.getFixedDate(this.cdate);
    }

    private static int getRolledValue(int i, int i2, int i3, int i4) {
        int i5 = (i4 - i3) + 1;
        int i6 = i + (i2 % i5);
        if (i6 > i4) {
            return i6 - i5;
        }
        return i6 < i3 ? i6 + i5 : i6;
    }

    private int internalGetEra() {
        if (isSet(0)) {
            return internalGet(0);
        }
        return 1;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.gdate == null) {
            this.gdate = gcal.newCalendarDate(getZone());
            this.cachedFixedDate = Long.MIN_VALUE;
        }
        setGregorianChange(this.gregorianCutover);
    }

    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(getTimeInMillis()), getTimeZone().toZoneId());
    }

    public static GregorianCalendar from(ZonedDateTime zonedDateTime) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone(zonedDateTime.getZone()));
        gregorianCalendar.setGregorianChange(new Date(Long.MIN_VALUE));
        gregorianCalendar.setFirstDayOfWeek(2);
        gregorianCalendar.setMinimalDaysInFirstWeek(4);
        try {
            gregorianCalendar.setTimeInMillis(Math.addExact(Math.multiplyExact(zonedDateTime.toEpochSecond(), 1000), (long) zonedDateTime.get(ChronoField.MILLI_OF_SECOND)));
            return gregorianCalendar;
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException((Throwable) e);
        }
    }
}
