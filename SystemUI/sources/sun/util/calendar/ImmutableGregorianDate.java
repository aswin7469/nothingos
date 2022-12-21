package sun.util.calendar;

import java.util.Locale;
import java.util.TimeZone;
import sun.util.calendar.BaseCalendar;

class ImmutableGregorianDate extends BaseCalendar.Date {
    private final BaseCalendar.Date date;

    ImmutableGregorianDate(BaseCalendar.Date date2) {
        date2.getClass();
        this.date = date2;
    }

    public Era getEra() {
        return this.date.getEra();
    }

    public CalendarDate setEra(Era era) {
        unsupported();
        return this;
    }

    public int getYear() {
        return this.date.getYear();
    }

    public CalendarDate setYear(int i) {
        unsupported();
        return this;
    }

    public CalendarDate addYear(int i) {
        unsupported();
        return this;
    }

    public boolean isLeapYear() {
        return this.date.isLeapYear();
    }

    /* access modifiers changed from: package-private */
    public void setLeapYear(boolean z) {
        unsupported();
    }

    public int getMonth() {
        return this.date.getMonth();
    }

    public CalendarDate setMonth(int i) {
        unsupported();
        return this;
    }

    public CalendarDate addMonth(int i) {
        unsupported();
        return this;
    }

    public int getDayOfMonth() {
        return this.date.getDayOfMonth();
    }

    public CalendarDate setDayOfMonth(int i) {
        unsupported();
        return this;
    }

    public CalendarDate addDayOfMonth(int i) {
        unsupported();
        return this;
    }

    public int getDayOfWeek() {
        return this.date.getDayOfWeek();
    }

    public int getHours() {
        return this.date.getHours();
    }

    public CalendarDate setHours(int i) {
        unsupported();
        return this;
    }

    public CalendarDate addHours(int i) {
        unsupported();
        return this;
    }

    public int getMinutes() {
        return this.date.getMinutes();
    }

    public CalendarDate setMinutes(int i) {
        unsupported();
        return this;
    }

    public CalendarDate addMinutes(int i) {
        unsupported();
        return this;
    }

    public int getSeconds() {
        return this.date.getSeconds();
    }

    public CalendarDate setSeconds(int i) {
        unsupported();
        return this;
    }

    public CalendarDate addSeconds(int i) {
        unsupported();
        return this;
    }

    public int getMillis() {
        return this.date.getMillis();
    }

    public CalendarDate setMillis(int i) {
        unsupported();
        return this;
    }

    public CalendarDate addMillis(int i) {
        unsupported();
        return this;
    }

    public long getTimeOfDay() {
        return this.date.getTimeOfDay();
    }

    public CalendarDate setDate(int i, int i2, int i3) {
        unsupported();
        return this;
    }

    public CalendarDate addDate(int i, int i2, int i3) {
        unsupported();
        return this;
    }

    public CalendarDate setTimeOfDay(int i, int i2, int i3, int i4) {
        unsupported();
        return this;
    }

    public CalendarDate addTimeOfDay(int i, int i2, int i3, int i4) {
        unsupported();
        return this;
    }

    /* access modifiers changed from: protected */
    public void setTimeOfDay(long j) {
        unsupported();
    }

    public boolean isNormalized() {
        return this.date.isNormalized();
    }

    public boolean isStandardTime() {
        return this.date.isStandardTime();
    }

    public void setStandardTime(boolean z) {
        unsupported();
    }

    public boolean isDaylightTime() {
        return this.date.isDaylightTime();
    }

    /* access modifiers changed from: protected */
    public void setLocale(Locale locale) {
        unsupported();
    }

    public TimeZone getZone() {
        return this.date.getZone();
    }

    public CalendarDate setZone(TimeZone timeZone) {
        unsupported();
        return this;
    }

    public boolean isSameDate(CalendarDate calendarDate) {
        return calendarDate.isSameDate(calendarDate);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ImmutableGregorianDate)) {
            return false;
        }
        return this.date.equals(((ImmutableGregorianDate) obj).date);
    }

    public int hashCode() {
        return this.date.hashCode();
    }

    public Object clone() {
        return super.clone();
    }

    public String toString() {
        return this.date.toString();
    }

    /* access modifiers changed from: protected */
    public void setDayOfWeek(int i) {
        unsupported();
    }

    /* access modifiers changed from: protected */
    public void setNormalized(boolean z) {
        unsupported();
    }

    public int getZoneOffset() {
        return this.date.getZoneOffset();
    }

    /* access modifiers changed from: protected */
    public void setZoneOffset(int i) {
        unsupported();
    }

    public int getDaylightSaving() {
        return this.date.getDaylightSaving();
    }

    /* access modifiers changed from: protected */
    public void setDaylightSaving(int i) {
        unsupported();
    }

    public int getNormalizedYear() {
        return this.date.getNormalizedYear();
    }

    public void setNormalizedYear(int i) {
        unsupported();
    }

    private void unsupported() {
        throw new UnsupportedOperationException();
    }
}
