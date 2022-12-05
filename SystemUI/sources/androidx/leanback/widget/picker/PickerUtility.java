package androidx.leanback.widget.picker;

import android.content.res.Resources;
import android.os.Build;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
/* loaded from: classes.dex */
class PickerUtility {
    static final boolean SUPPORTS_BEST_DATE_TIME_PATTERN;

    static {
        SUPPORTS_BEST_DATE_TIME_PATTERN = Build.VERSION.SDK_INT >= 18;
    }

    /* loaded from: classes.dex */
    public static class DateConstant {
        public final String[] days;
        public final Locale locale;
        public final String[] months;

        DateConstant(Locale locale, Resources resources) {
            this.locale = locale;
            this.months = DateFormatSymbols.getInstance(locale).getShortMonths();
            Calendar calendar = Calendar.getInstance(locale);
            this.days = PickerUtility.createStringIntArrays(calendar.getMinimum(5), calendar.getMaximum(5), "%02d");
        }
    }

    /* loaded from: classes.dex */
    public static class TimeConstant {
        public final String[] ampm;
        public final Locale locale;
        public final String[] hours12 = PickerUtility.createStringIntArrays(1, 12, "%02d");
        public final String[] hours24 = PickerUtility.createStringIntArrays(0, 23, "%02d");
        public final String[] minutes = PickerUtility.createStringIntArrays(0, 59, "%02d");

        TimeConstant(Locale locale, Resources resources) {
            this.locale = locale;
            DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale);
            this.ampm = dateFormatSymbols.getAmPmStrings();
        }
    }

    public static DateConstant getDateConstantInstance(Locale locale, Resources resources) {
        return new DateConstant(locale, resources);
    }

    public static TimeConstant getTimeConstantInstance(Locale locale, Resources resources) {
        return new TimeConstant(locale, resources);
    }

    public static String[] createStringIntArrays(int firstNumber, int lastNumber, String format) {
        String[] strArr = new String[(lastNumber - firstNumber) + 1];
        for (int i = firstNumber; i <= lastNumber; i++) {
            if (format != null) {
                strArr[i - firstNumber] = String.format(format, Integer.valueOf(i));
            } else {
                strArr[i - firstNumber] = String.valueOf(i);
            }
        }
        return strArr;
    }

    public static Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Calendar.getInstance(locale);
        }
        long timeInMillis = oldCalendar.getTimeInMillis();
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTimeInMillis(timeInMillis);
        return calendar;
    }
}
