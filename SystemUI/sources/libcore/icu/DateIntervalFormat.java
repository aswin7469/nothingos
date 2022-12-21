package libcore.icu;

import android.icu.util.Calendar;
import android.icu.util.ULocale;
import java.text.FieldPosition;
import java.util.TimeZone;

public final class DateIntervalFormat {
    private DateIntervalFormat() {
    }

    public static String formatDateRange(long j, long j2, int i, String str) {
        if ((i & 8192) != 0) {
            str = "UTC";
        }
        return formatDateRange(ULocale.getDefault(), DateUtilsBridge.icuTimeZone(str != null ? TimeZone.getTimeZone(str) : TimeZone.getDefault()), j, j2, i);
    }

    public static String formatDateRange(ULocale uLocale, android.icu.util.TimeZone timeZone, long j, long j2, int i) {
        Calendar calendar;
        Calendar createIcuCalendar = DateUtilsBridge.createIcuCalendar(timeZone, uLocale, j);
        int i2 = (j > j2 ? 1 : (j == j2 ? 0 : -1));
        if (i2 == 0) {
            calendar = createIcuCalendar;
        } else {
            calendar = DateUtilsBridge.createIcuCalendar(timeZone, uLocale, j2);
        }
        if (isExactlyMidnight(calendar)) {
            boolean z = true;
            boolean z2 = (i & 1) == 1;
            if (DateUtilsBridge.dayDistance(createIcuCalendar, calendar) != 1) {
                z = false;
            }
            if ((!z2 && i2 != 0) || (z && !DateUtilsBridge.isDisplayMidnightUsingSkeleton(createIcuCalendar))) {
                calendar.add(5, -1);
            }
        }
        android.icu.text.DateIntervalFormat instance = android.icu.text.DateIntervalFormat.getInstance(DateUtilsBridge.toSkeleton(createIcuCalendar, calendar, i), uLocale);
        instance.setTimeZone(timeZone);
        return instance.format(createIcuCalendar, calendar, new StringBuffer(), new FieldPosition(0)).toString();
    }

    private static boolean isExactlyMidnight(Calendar calendar) {
        return calendar.get(11) == 0 && calendar.get(12) == 0 && calendar.get(13) == 0 && calendar.get(14) == 0;
    }
}
