package android.icu.util;

import android.icu.text.DateFormat;
import java.p026io.Serializable;
import java.util.Date;
import java.util.Locale;

public abstract class Calendar implements Serializable, Cloneable, Comparable<Calendar> {

    /* renamed from: AM */
    public static final int f26AM = 0;
    public static final int AM_PM = 9;
    public static final int APRIL = 3;
    public static final int AUGUST = 7;
    @Deprecated
    protected static final int BASE_FIELD_COUNT = 23;
    public static final int DATE = 5;
    public static final int DAY_OF_MONTH = 5;
    public static final int DAY_OF_WEEK = 7;
    public static final int DAY_OF_WEEK_IN_MONTH = 8;
    public static final int DAY_OF_YEAR = 6;
    public static final int DECEMBER = 11;
    public static final int DOW_LOCAL = 18;
    public static final int DST_OFFSET = 16;
    protected static final int EPOCH_JULIAN_DAY = 2440588;
    public static final int ERA = 0;
    public static final int EXTENDED_YEAR = 19;
    public static final int FEBRUARY = 1;
    public static final int FRIDAY = 6;
    protected static final int GREATEST_MINIMUM = 1;
    public static final int HOUR = 10;
    public static final int HOUR_OF_DAY = 11;
    protected static final int INTERNALLY_SET = 1;
    public static final int IS_LEAP_MONTH = 22;
    public static final int JANUARY = 0;
    protected static final int JAN_1_1_JULIAN_DAY = 1721426;
    public static final int JULIAN_DAY = 20;
    public static final int JULY = 6;
    public static final int JUNE = 5;
    protected static final int LEAST_MAXIMUM = 2;
    public static final int MARCH = 2;
    protected static final int MAXIMUM = 3;
    protected static final Date MAX_DATE = null;
    @Deprecated
    protected static final int MAX_FIELD_COUNT = 32;
    protected static final int MAX_JULIAN = 2130706432;
    protected static final long MAX_MILLIS = 183882168921600000L;
    public static final int MAY = 4;
    public static final int MILLISECOND = 14;
    public static final int MILLISECONDS_IN_DAY = 21;
    protected static final int MINIMUM = 0;
    protected static final int MINIMUM_USER_STAMP = 2;
    public static final int MINUTE = 12;
    protected static final Date MIN_DATE = null;
    protected static final int MIN_JULIAN = -2130706432;
    protected static final long MIN_MILLIS = -184303902528000000L;
    public static final int MONDAY = 2;
    public static final int MONTH = 2;
    public static final int NOVEMBER = 10;
    public static final int OCTOBER = 9;
    protected static final long ONE_DAY = 86400000;
    protected static final int ONE_HOUR = 3600000;
    protected static final int ONE_MINUTE = 60000;
    protected static final int ONE_SECOND = 1000;
    protected static final long ONE_WEEK = 604800000;

    /* renamed from: PM */
    public static final int f27PM = 1;
    protected static final int RESOLVE_REMAP = 32;
    public static final int SATURDAY = 7;
    public static final int SECOND = 13;
    public static final int SEPTEMBER = 8;
    public static final int SUNDAY = 1;
    public static final int THURSDAY = 5;
    public static final int TUESDAY = 3;
    public static final int UNDECIMBER = 12;
    protected static final int UNSET = 0;
    public static final int WALLTIME_FIRST = 1;
    public static final int WALLTIME_LAST = 0;
    public static final int WALLTIME_NEXT_VALID = 2;
    public static final int WEDNESDAY = 4;
    public static final int WEEK_OF_MONTH = 4;
    public static final int WEEK_OF_YEAR = 3;
    public static final int YEAR = 1;
    public static final int YEAR_WOY = 17;
    public static final int ZONE_OFFSET = 15;

    /* access modifiers changed from: protected */
    public abstract int handleComputeMonthStart(int i, int i2, boolean z);

    /* access modifiers changed from: protected */
    public abstract int handleGetExtendedYear();

    /* access modifiers changed from: protected */
    public abstract int handleGetLimit(int i, int i2);

    protected Calendar() {
        throw new RuntimeException("Stub!");
    }

    protected Calendar(TimeZone timeZone, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    protected Calendar(TimeZone timeZone, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static Calendar getInstance() {
        throw new RuntimeException("Stub!");
    }

    public static Calendar getInstance(TimeZone timeZone) {
        throw new RuntimeException("Stub!");
    }

    public static Calendar getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static Calendar getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static Calendar getInstance(TimeZone timeZone, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static Calendar getInstance(TimeZone timeZone, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static Locale[] getAvailableLocales() {
        throw new RuntimeException("Stub!");
    }

    public static final String[] getKeywordValuesForLocale(String str, ULocale uLocale, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public final Date getTime() {
        throw new RuntimeException("Stub!");
    }

    public final void setTime(Date date) {
        throw new RuntimeException("Stub!");
    }

    public long getTimeInMillis() {
        throw new RuntimeException("Stub!");
    }

    public void setTimeInMillis(long j) {
        throw new RuntimeException("Stub!");
    }

    public final int get(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final int internalGet(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final int internalGet(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final void set(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final void set(int i, int i2, int i3) {
        throw new RuntimeException("Stub!");
    }

    public final void set(int i, int i2, int i3, int i4, int i5) {
        throw new RuntimeException("Stub!");
    }

    public final void set(int i, int i2, int i3, int i4, int i5, int i6) {
        throw new RuntimeException("Stub!");
    }

    public final void clear() {
        throw new RuntimeException("Stub!");
    }

    public final void clear(int i) {
        throw new RuntimeException("Stub!");
    }

    public final boolean isSet(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void complete() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public boolean isEquivalentTo(Calendar calendar) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean before(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public boolean after(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int getActualMaximum(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getActualMinimum(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void prepareGetActual(int i, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public final void roll(int i, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public void roll(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void add(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public int compareTo(Calendar calendar) {
        throw new RuntimeException("Stub!");
    }

    public DateFormat getDateTimeFormat(int i, int i2, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public DateFormat getDateTimeFormat(int i, int i2, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public DateFormat handleGetDateFormat(String str, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public DateFormat handleGetDateFormat(String str, String str2, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public DateFormat handleGetDateFormat(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void pinField(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int weekNumber(int i, int i2, int i3) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final int weekNumber(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public int fieldDifference(Date date, int i) {
        throw new RuntimeException("Stub!");
    }

    public void setTimeZone(TimeZone timeZone) {
        throw new RuntimeException("Stub!");
    }

    public TimeZone getTimeZone() {
        throw new RuntimeException("Stub!");
    }

    public void setLenient(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public boolean isLenient() {
        throw new RuntimeException("Stub!");
    }

    public void setRepeatedWallTimeOption(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getRepeatedWallTimeOption() {
        throw new RuntimeException("Stub!");
    }

    public void setSkippedWallTimeOption(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getSkippedWallTimeOption() {
        throw new RuntimeException("Stub!");
    }

    public void setFirstDayOfWeek(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getFirstDayOfWeek() {
        throw new RuntimeException("Stub!");
    }

    public void setMinimalDaysInFirstWeek(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getMinimalDaysInFirstWeek() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int getLimit(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final int getMinimum(int i) {
        throw new RuntimeException("Stub!");
    }

    public final int getMaximum(int i) {
        throw new RuntimeException("Stub!");
    }

    public final int getGreatestMinimum(int i) {
        throw new RuntimeException("Stub!");
    }

    public final int getLeastMaximum(int i) {
        throw new RuntimeException("Stub!");
    }

    public boolean isWeekend(Date date) {
        throw new RuntimeException("Stub!");
    }

    public boolean isWeekend() {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public static WeekData getWeekDataForRegion(String str) {
        throw new RuntimeException("Stub!");
    }

    public WeekData getWeekData() {
        throw new RuntimeException("Stub!");
    }

    public Calendar setWeekData(WeekData weekData) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void computeFields() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final void computeGregorianFields(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int resolveFields(int[][][] iArr) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int newestStamp(int i, int i2, int i3) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final int getStamp(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int newerField(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void validateFields() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void validateField(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final void validateField(int i, int i2, int i3) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void computeTime() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public int computeMillisInDay() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public int computeZoneOffset(long j, int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int computeJulianDay() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int[][][] getFieldResolutionTable() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleGetMonthLength(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleGetYearLength(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int[] handleCreateFields() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleComputeJulianDay(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int computeGregorianMonthStart(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void handleComputeFields(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final int getGregorianYear() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final int getGregorianMonth() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final int getGregorianDayOfYear() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final int getGregorianDayOfMonth() {
        throw new RuntimeException("Stub!");
    }

    public final int getFieldCount() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final void internalSet(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    protected static final boolean isGregorianLeapYear(int i) {
        throw new RuntimeException("Stub!");
    }

    protected static final int gregorianMonthLength(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    protected static final int gregorianPreviousMonthLength(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    protected static final long floorDivide(long j, long j2) {
        throw new RuntimeException("Stub!");
    }

    protected static final int floorDivide(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    protected static final int floorDivide(int i, int i2, int[] iArr) {
        throw new RuntimeException("Stub!");
    }

    protected static final int floorDivide(long j, int i, int[] iArr) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public String fieldName(int i) {
        throw new RuntimeException("Stub!");
    }

    protected static final int millisToJulianDay(long j) {
        throw new RuntimeException("Stub!");
    }

    protected static final long julianDayToMillis(int i) {
        throw new RuntimeException("Stub!");
    }

    protected static final int julianDayToDayOfWeek(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public final long internalGetTimeInMillis() {
        throw new RuntimeException("Stub!");
    }

    public String getType() {
        throw new RuntimeException("Stub!");
    }

    public static final class WeekData {
        public final int firstDayOfWeek = 0;
        public final int minimalDaysInFirstWeek = 0;
        public final int weekendCease = 0;
        public final int weekendCeaseMillis = 0;
        public final int weekendOnset = 0;
        public final int weekendOnsetMillis = 0;

        public WeekData(int i, int i2, int i3, int i4, int i5, int i6) {
            throw new RuntimeException("Stub!");
        }

        public int hashCode() {
            throw new RuntimeException("Stub!");
        }

        public boolean equals(Object obj) {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }
}
