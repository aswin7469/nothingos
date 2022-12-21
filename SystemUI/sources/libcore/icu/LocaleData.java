package libcore.icu;

import android.icu.text.DateFormatSymbols;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import dalvik.system.VMRuntime;
import java.util.HashMap;
import java.util.Locale;
import libcore.util.Objects;
import sun.util.locale.LanguageTag;

public final class LocaleData {
    private static final Locale LOCALE_EN_US_POSIX = new Locale("en", "US", "POSIX");
    public static final long USE_REAL_ROOT_LOCALE = 159047832;
    private static final HashMap<String, LocaleData> localeDataCache = new HashMap<>();
    public String[] amPm;
    public String[] eras;
    public Integer firstDayOfWeek;
    public String[] longMonthNames;
    public String[] longStandAloneMonthNames;
    public String[] longStandAloneWeekdayNames;
    public String[] longWeekdayNames;
    public Integer minimalDaysInFirstWeek;
    public String[] shortMonthNames;
    public String[] shortStandAloneMonthNames;
    public String[] shortStandAloneWeekdayNames;
    public String[] shortWeekdayNames;
    public String timeFormat_Hm = "HH:mm";
    public String timeFormat_hm = "h:mm a";
    public String[] tinyMonthNames;
    public String[] tinyStandAloneMonthNames;
    public String[] tinyStandAloneWeekdayNames;
    public String[] tinyWeekdayNames;
    public String today = "Today";
    public String tomorrow = "Tomorrow";
    public char zeroDigit = '0';

    static {
        get(Locale.ROOT);
        get(Locale.f700US);
        get(Locale.getDefault());
    }

    private LocaleData() {
    }

    public static Locale mapInvalidAndNullLocales(Locale locale) {
        if (locale == null) {
            return Locale.getDefault();
        }
        return LanguageTag.UNDETERMINED.equals(locale.toLanguageTag()) ? Locale.ROOT : locale;
    }

    public static Locale getCompatibleLocaleForBug159514442(Locale locale) {
        return (!Locale.ROOT.equals(locale) || VMRuntime.getRuntime().getTargetSdkVersion() > 29) ? locale : LOCALE_EN_US_POSIX;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        monitor-enter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r2 = r1.get(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0023, code lost:
        if (r2 == null) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0026, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0027, code lost:
        r1.put(r0, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002a, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002b, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0018, code lost:
        r3 = initLocaleData(r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static libcore.icu.LocaleData get(java.util.Locale r3) {
        /*
            if (r3 == 0) goto L_0x0032
            java.util.Locale r3 = getCompatibleLocaleForBug159514442(r3)
            java.lang.String r0 = r3.toLanguageTag()
            java.util.HashMap<java.lang.String, libcore.icu.LocaleData> r1 = localeDataCache
            monitor-enter(r1)
            java.lang.Object r2 = r1.get(r0)     // Catch:{ all -> 0x002f }
            libcore.icu.LocaleData r2 = (libcore.icu.LocaleData) r2     // Catch:{ all -> 0x002f }
            if (r2 == 0) goto L_0x0017
            monitor-exit(r1)     // Catch:{ all -> 0x002f }
            return r2
        L_0x0017:
            monitor-exit(r1)     // Catch:{ all -> 0x002f }
            libcore.icu.LocaleData r3 = initLocaleData(r3)
            monitor-enter(r1)
            java.lang.Object r2 = r1.get(r0)     // Catch:{ all -> 0x002c }
            libcore.icu.LocaleData r2 = (libcore.icu.LocaleData) r2     // Catch:{ all -> 0x002c }
            if (r2 == 0) goto L_0x0027
            monitor-exit(r1)     // Catch:{ all -> 0x002c }
            return r2
        L_0x0027:
            r1.put(r0, r3)     // Catch:{ all -> 0x002c }
            monitor-exit(r1)     // Catch:{ all -> 0x002c }
            return r3
        L_0x002c:
            r3 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x002c }
            throw r3
        L_0x002f:
            r3 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x002f }
            throw r3
        L_0x0032:
            java.lang.NullPointerException r3 = new java.lang.NullPointerException
            java.lang.String r0 = "locale == null"
            r3.<init>(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: libcore.icu.LocaleData.get(java.util.Locale):libcore.icu.LocaleData");
    }

    public String toString() {
        return Objects.toString(this);
    }

    public static LocaleData initLocaleData(Locale locale) {
        LocaleData localeData = new LocaleData();
        localeData.initializeDateFormatData(locale);
        localeData.initializeCalendarData(locale);
        return localeData;
    }

    private void initializeDateFormatData(Locale locale) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols((Class<? extends Calendar>) GregorianCalendar.class, locale);
        this.longMonthNames = dateFormatSymbols.getMonths(0, 1);
        this.shortMonthNames = dateFormatSymbols.getMonths(0, 0);
        this.tinyMonthNames = dateFormatSymbols.getMonths(0, 2);
        this.longWeekdayNames = dateFormatSymbols.getWeekdays(0, 1);
        this.shortWeekdayNames = dateFormatSymbols.getWeekdays(0, 0);
        this.tinyWeekdayNames = dateFormatSymbols.getWeekdays(0, 2);
        this.longStandAloneMonthNames = dateFormatSymbols.getMonths(1, 1);
        this.shortStandAloneMonthNames = dateFormatSymbols.getMonths(1, 0);
        this.tinyStandAloneMonthNames = dateFormatSymbols.getMonths(1, 2);
        this.longStandAloneWeekdayNames = dateFormatSymbols.getWeekdays(1, 1);
        this.shortStandAloneWeekdayNames = dateFormatSymbols.getWeekdays(1, 0);
        this.tinyStandAloneWeekdayNames = dateFormatSymbols.getWeekdays(1, 2);
        this.amPm = dateFormatSymbols.getAmPmStrings();
        this.eras = dateFormatSymbols.getEras();
    }

    private void initializeCalendarData(Locale locale) {
        Calendar instance = Calendar.getInstance(locale);
        this.firstDayOfWeek = Integer.valueOf(instance.getFirstDayOfWeek());
        this.minimalDaysInFirstWeek = Integer.valueOf(instance.getMinimalDaysInFirstWeek());
    }
}
