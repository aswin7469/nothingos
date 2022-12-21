package sun.util.calendar;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class CalendarSystem {
    private static final Gregorian GREGORIAN_INSTANCE = new Gregorian();
    private static final ConcurrentMap<String, CalendarSystem> calendars = new ConcurrentHashMap();
    private static final Map<String, Class<?>> names;

    public abstract CalendarDate getCalendarDate();

    public abstract CalendarDate getCalendarDate(long j);

    public abstract CalendarDate getCalendarDate(long j, TimeZone timeZone);

    public abstract CalendarDate getCalendarDate(long j, CalendarDate calendarDate);

    public abstract Era getEra(String str);

    public abstract Era[] getEras();

    public abstract int getMonthLength(CalendarDate calendarDate);

    public abstract String getName();

    public abstract CalendarDate getNthDayOfWeek(int i, int i2, CalendarDate calendarDate);

    public abstract long getTime(CalendarDate calendarDate);

    public abstract int getWeekLength();

    public abstract int getYearLength(CalendarDate calendarDate);

    public abstract int getYearLengthInMonths(CalendarDate calendarDate);

    public abstract CalendarDate newCalendarDate();

    public abstract CalendarDate newCalendarDate(TimeZone timeZone);

    public abstract boolean normalize(CalendarDate calendarDate);

    public abstract void setEra(CalendarDate calendarDate, String str);

    public abstract CalendarDate setTimeOfDay(CalendarDate calendarDate, int i);

    public abstract boolean validate(CalendarDate calendarDate);

    static {
        HashMap hashMap = new HashMap();
        names = hashMap;
        hashMap.put("gregorian", Gregorian.class);
        hashMap.put("japanese", LocalGregorianCalendar.class);
        hashMap.put("julian", JulianCalendar.class);
    }

    public static Gregorian getGregorianCalendar() {
        return GREGORIAN_INSTANCE;
    }

    public static CalendarSystem forName(String str) {
        CalendarSystem calendarSystem;
        if ("gregorian".equals(str)) {
            return GREGORIAN_INSTANCE;
        }
        ConcurrentMap<String, CalendarSystem> concurrentMap = calendars;
        CalendarSystem calendarSystem2 = concurrentMap.get(str);
        if (calendarSystem2 != null) {
            return calendarSystem2;
        }
        Class cls = names.get(str);
        if (cls == null) {
            return null;
        }
        if (cls.isAssignableFrom(LocalGregorianCalendar.class)) {
            calendarSystem = LocalGregorianCalendar.getLocalGregorianCalendar(str);
        } else {
            try {
                calendarSystem = (CalendarSystem) cls.newInstance();
            } catch (Exception e) {
                throw new InternalError((Throwable) e);
            }
        }
        if (calendarSystem == null) {
            return null;
        }
        CalendarSystem putIfAbsent = concurrentMap.putIfAbsent(str, calendarSystem);
        return putIfAbsent == null ? calendarSystem : putIfAbsent;
    }

    public static Properties getCalendarProperties() throws IOException {
        Properties properties = new Properties();
        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("calendars.properties");
        try {
            properties.load(systemResourceAsStream);
            if (systemResourceAsStream != null) {
                systemResourceAsStream.close();
            }
            return properties;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }
}
