package libcore.icu;

import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.icu.util.TimeZone;
import android.icu.util.ULocale;

public final class DateUtilsBridge {
    public static final int FORMAT_12HOUR = 64;
    public static final int FORMAT_24HOUR = 128;
    public static final int FORMAT_ABBREV_ALL = 524288;
    public static final int FORMAT_ABBREV_MONTH = 65536;
    public static final int FORMAT_ABBREV_RELATIVE = 262144;
    public static final int FORMAT_ABBREV_TIME = 16384;
    public static final int FORMAT_ABBREV_WEEKDAY = 32768;
    public static final int FORMAT_NO_MONTH_DAY = 32;
    public static final int FORMAT_NO_YEAR = 8;
    public static final int FORMAT_NUMERIC_DATE = 131072;
    public static final int FORMAT_SHOW_DATE = 16;
    public static final int FORMAT_SHOW_TIME = 1;
    public static final int FORMAT_SHOW_WEEKDAY = 2;
    public static final int FORMAT_SHOW_YEAR = 4;
    public static final int FORMAT_UTC = 8192;

    static TimeZone icuTimeZone(java.util.TimeZone timeZone) {
        TimeZone timeZone2 = TimeZone.getTimeZone(timeZone.getID());
        timeZone2.freeze();
        return timeZone2;
    }

    static Calendar createIcuCalendar(TimeZone timeZone, ULocale uLocale, long j) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, uLocale);
        gregorianCalendar.setTimeInMillis(j);
        return gregorianCalendar;
    }

    static String toSkeleton(Calendar calendar, int i) {
        return toSkeleton(calendar, calendar, i);
    }

    static String toSkeleton(Calendar calendar, Calendar calendar2, int i) {
        if ((524288 & i) != 0) {
            i |= 114688;
        }
        String str = (131072 & i) != 0 ? "M" : (65536 & i) != 0 ? DateFormat.ABBR_MONTH : DateFormat.MONTH;
        String str2 = (32768 & i) != 0 ? "EEE" : DateFormat.WEEKDAY;
        int i2 = i & 128;
        String str3 = i2 != 0 ? DateFormat.HOUR24 : (i & 64) != 0 ? "h" : DateFormat.HOUR;
        if ((i & 16384) == 0 || i2 != 0) {
            str3 = str3 + DateFormat.MINUTE;
        } else if (!onTheHour(calendar) || !onTheHour(calendar2)) {
            str3 = str3 + DateFormat.MINUTE;
        }
        if (fallOnDifferentDates(calendar, calendar2)) {
            i |= 16;
        }
        if (fallInSameMonth(calendar, calendar2) && (i & 32) != 0) {
            i = i & -3 & -2;
        }
        if ((i & 19) == 0) {
            i |= 16;
        }
        if ((i & 16) != 0 && (i & 4) == 0 && (i & 8) == 0 && (!fallInSameYear(calendar, calendar2) || !isThisYear(calendar))) {
            i |= 4;
        }
        StringBuilder sb = new StringBuilder();
        if ((i & 48) != 0) {
            if ((i & 4) != 0) {
                sb.append(DateFormat.YEAR);
            }
            sb.append(str);
            if ((i & 32) == 0) {
                sb.append(DateFormat.DAY);
            }
        }
        if ((i & 2) != 0) {
            sb.append(str2);
        }
        if ((i & 1) != 0) {
            sb.append(str3);
        }
        return sb.toString();
    }

    static int dayDistance(Calendar calendar, Calendar calendar2) {
        return calendar2.get(20) - calendar.get(20);
    }

    static boolean isDisplayMidnightUsingSkeleton(Calendar calendar) {
        return calendar.get(11) == 0 && calendar.get(12) == 0;
    }

    private static boolean onTheHour(Calendar calendar) {
        return calendar.get(12) == 0 && calendar.get(13) == 0;
    }

    private static boolean fallOnDifferentDates(Calendar calendar, Calendar calendar2) {
        if (calendar.get(1) == calendar2.get(1) && calendar.get(2) == calendar2.get(2) && calendar.get(5) == calendar2.get(5)) {
            return false;
        }
        return true;
    }

    private static boolean fallInSameMonth(Calendar calendar, Calendar calendar2) {
        return calendar.get(2) == calendar2.get(2);
    }

    private static boolean fallInSameYear(Calendar calendar, Calendar calendar2) {
        return calendar.get(1) == calendar2.get(1);
    }

    private static boolean isThisYear(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        if (calendar.get(1) == calendar2.get(1)) {
            return true;
        }
        return false;
    }
}
