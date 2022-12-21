package sun.util.calendar;

import java.util.TimeZone;
import sun.util.calendar.BaseCalendar;

public class Gregorian extends BaseCalendar {
    public String getName() {
        return "gregorian";
    }

    static class Date extends BaseCalendar.Date {
        protected Date() {
        }

        protected Date(TimeZone timeZone) {
            super(timeZone);
        }

        public int getNormalizedYear() {
            return getYear();
        }

        public void setNormalizedYear(int i) {
            setYear(i);
        }
    }

    Gregorian() {
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
}
