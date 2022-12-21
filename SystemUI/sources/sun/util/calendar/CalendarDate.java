package sun.util.calendar;

import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.wifitrackerlib.WifiEntry;
import java.util.Locale;
import java.util.TimeZone;

public abstract class CalendarDate implements Cloneable {
    public static final int FIELD_UNDEFINED = Integer.MIN_VALUE;
    public static final long TIME_UNDEFINED = Long.MIN_VALUE;
    private int dayOfMonth;
    private int dayOfWeek;
    private int daylightSaving;
    private Era era;
    private boolean forceStandardTime;
    private long fraction;
    private int hours;
    private boolean leapYear;
    private Locale locale;
    private int millis;
    private int minutes;
    private int month;
    private boolean normalized;
    private int seconds;
    private int year;
    private int zoneOffset;
    private TimeZone zoneinfo;

    protected CalendarDate() {
        this(TimeZone.getDefault());
    }

    protected CalendarDate(TimeZone timeZone) {
        this.dayOfWeek = Integer.MIN_VALUE;
        this.zoneinfo = timeZone;
    }

    public Era getEra() {
        return this.era;
    }

    public CalendarDate setEra(Era era2) {
        if (this.era == era2) {
            return this;
        }
        this.era = era2;
        this.normalized = false;
        return this;
    }

    public int getYear() {
        return this.year;
    }

    public CalendarDate setYear(int i) {
        if (this.year != i) {
            this.year = i;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addYear(int i) {
        if (i != 0) {
            this.year += i;
            this.normalized = false;
        }
        return this;
    }

    public boolean isLeapYear() {
        return this.leapYear;
    }

    /* access modifiers changed from: package-private */
    public void setLeapYear(boolean z) {
        this.leapYear = z;
    }

    public int getMonth() {
        return this.month;
    }

    public CalendarDate setMonth(int i) {
        if (this.month != i) {
            this.month = i;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addMonth(int i) {
        if (i != 0) {
            this.month += i;
            this.normalized = false;
        }
        return this;
    }

    public int getDayOfMonth() {
        return this.dayOfMonth;
    }

    public CalendarDate setDayOfMonth(int i) {
        if (this.dayOfMonth != i) {
            this.dayOfMonth = i;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addDayOfMonth(int i) {
        if (i != 0) {
            this.dayOfMonth += i;
            this.normalized = false;
        }
        return this;
    }

    public int getDayOfWeek() {
        if (!isNormalized()) {
            this.dayOfWeek = Integer.MIN_VALUE;
        }
        return this.dayOfWeek;
    }

    public int getHours() {
        return this.hours;
    }

    public CalendarDate setHours(int i) {
        if (this.hours != i) {
            this.hours = i;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addHours(int i) {
        if (i != 0) {
            this.hours += i;
            this.normalized = false;
        }
        return this;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public CalendarDate setMinutes(int i) {
        if (this.minutes != i) {
            this.minutes = i;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addMinutes(int i) {
        if (i != 0) {
            this.minutes += i;
            this.normalized = false;
        }
        return this;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public CalendarDate setSeconds(int i) {
        if (this.seconds != i) {
            this.seconds = i;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addSeconds(int i) {
        if (i != 0) {
            this.seconds += i;
            this.normalized = false;
        }
        return this;
    }

    public int getMillis() {
        return this.millis;
    }

    public CalendarDate setMillis(int i) {
        if (this.millis != i) {
            this.millis = i;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addMillis(int i) {
        if (i != 0) {
            this.millis += i;
            this.normalized = false;
        }
        return this;
    }

    public long getTimeOfDay() {
        if (isNormalized()) {
            return this.fraction;
        }
        this.fraction = Long.MIN_VALUE;
        return Long.MIN_VALUE;
    }

    public CalendarDate setDate(int i, int i2, int i3) {
        setYear(i);
        setMonth(i2);
        setDayOfMonth(i3);
        return this;
    }

    public CalendarDate addDate(int i, int i2, int i3) {
        addYear(i);
        addMonth(i2);
        addDayOfMonth(i3);
        return this;
    }

    public CalendarDate setTimeOfDay(int i, int i2, int i3, int i4) {
        setHours(i);
        setMinutes(i2);
        setSeconds(i3);
        setMillis(i4);
        return this;
    }

    public CalendarDate addTimeOfDay(int i, int i2, int i3, int i4) {
        addHours(i);
        addMinutes(i2);
        addSeconds(i3);
        addMillis(i4);
        return this;
    }

    /* access modifiers changed from: protected */
    public void setTimeOfDay(long j) {
        this.fraction = j;
    }

    public boolean isNormalized() {
        return this.normalized;
    }

    public boolean isStandardTime() {
        return this.forceStandardTime;
    }

    public void setStandardTime(boolean z) {
        this.forceStandardTime = z;
    }

    public boolean isDaylightTime() {
        if (!isStandardTime() && this.daylightSaving != 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void setLocale(Locale locale2) {
        this.locale = locale2;
    }

    public TimeZone getZone() {
        return this.zoneinfo;
    }

    public CalendarDate setZone(TimeZone timeZone) {
        this.zoneinfo = timeZone;
        return this;
    }

    public boolean isSameDate(CalendarDate calendarDate) {
        return getDayOfWeek() == calendarDate.getDayOfWeek() && getMonth() == calendarDate.getMonth() && getYear() == calendarDate.getYear() && getEra() == calendarDate.getEra();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CalendarDate)) {
            return false;
        }
        CalendarDate calendarDate = (CalendarDate) obj;
        if (isNormalized() != calendarDate.isNormalized()) {
            return false;
        }
        TimeZone timeZone = this.zoneinfo;
        boolean z = timeZone != null;
        TimeZone timeZone2 = calendarDate.zoneinfo;
        if (z != (timeZone2 != null)) {
            return false;
        }
        if ((!z || timeZone.equals(timeZone2)) && getEra() == calendarDate.getEra() && this.year == calendarDate.year && this.month == calendarDate.month && this.dayOfMonth == calendarDate.dayOfMonth && this.hours == calendarDate.hours && this.minutes == calendarDate.minutes && this.seconds == calendarDate.seconds && this.millis == calendarDate.millis && this.zoneOffset == calendarDate.zoneOffset) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long j = (((((((((((((((long) this.year) - 1970) * 12) + ((long) (this.month - 1))) * 30) + ((long) this.dayOfMonth)) * 24) + ((long) this.hours)) * 60) + ((long) this.minutes)) * 60) + ((long) this.seconds)) * 1000) + ((long) this.millis)) - ((long) this.zoneOffset);
        boolean isNormalized = isNormalized();
        Era era2 = getEra();
        int i = 0;
        int hashCode = era2 != null ? era2.hashCode() : 0;
        TimeZone timeZone = this.zoneinfo;
        if (timeZone != null) {
            i = timeZone.hashCode();
        }
        return (((((int) j) * ((int) (j >> 32))) ^ hashCode) ^ isNormalized) ^ i ? 1 : 0;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        char c = '-';
        CalendarUtils.sprintf0d(sb, this.year, 4).append('-');
        CalendarUtils.sprintf0d(sb, this.month, 2).append('-');
        CalendarUtils.sprintf0d(sb, this.dayOfMonth, 2).append('T');
        CalendarUtils.sprintf0d(sb, this.hours, 2).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        CalendarUtils.sprintf0d(sb, this.minutes, 2).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        CalendarUtils.sprintf0d(sb, this.seconds, 2).append('.');
        CalendarUtils.sprintf0d(sb, this.millis, 3);
        int i = this.zoneOffset;
        if (i == 0) {
            sb.append('Z');
        } else if (i != Integer.MIN_VALUE) {
            if (i > 0) {
                c = '+';
            } else {
                i = -i;
            }
            int i2 = i / WifiEntry.FREQUENCY_60_GHZ;
            sb.append(c);
            CalendarUtils.sprintf0d(sb, i2 / 60, 2);
            CalendarUtils.sprintf0d(sb, i2 % 60, 2);
        } else {
            sb.append(" local time");
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void setDayOfWeek(int i) {
        this.dayOfWeek = i;
    }

    /* access modifiers changed from: protected */
    public void setNormalized(boolean z) {
        this.normalized = z;
    }

    public int getZoneOffset() {
        return this.zoneOffset;
    }

    /* access modifiers changed from: protected */
    public void setZoneOffset(int i) {
        this.zoneOffset = i;
    }

    public int getDaylightSaving() {
        return this.daylightSaving;
    }

    /* access modifiers changed from: protected */
    public void setDaylightSaving(int i) {
        this.daylightSaving = i;
    }
}
