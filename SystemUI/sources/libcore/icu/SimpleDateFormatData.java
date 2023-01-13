package libcore.icu;

import com.android.icu.util.ExtendedCalendar;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleDateFormatData {
    private static final ConcurrentHashMap<String, SimpleDateFormatData> CACHE = new ConcurrentHashMap<>(3);
    private final String fullDateFormat;
    private final String fullTimeFormat;
    private final Locale locale;
    private final String longDateFormat;
    private final String longTimeFormat;
    private final String mediumDateFormat;
    private final String mediumTimeFormat;
    private final String shortDateFormat;
    private final String shortTimeFormat;

    private SimpleDateFormatData(Locale locale2) {
        this.locale = locale2;
        ExtendedCalendar extendedCalendar = ICU.getExtendedCalendar(locale2, "gregorian");
        String dateTimeFormatString = getDateTimeFormatString(extendedCalendar, -1, 0);
        this.fullTimeFormat = dateTimeFormatString != null ? dateTimeFormatString.replace('v', 'z') : dateTimeFormatString;
        this.longTimeFormat = getDateTimeFormatString(extendedCalendar, -1, 1);
        this.mediumTimeFormat = getDateTimeFormatString(extendedCalendar, -1, 2);
        this.shortTimeFormat = getDateTimeFormatString(extendedCalendar, -1, 3);
        this.fullDateFormat = getDateTimeFormatString(extendedCalendar, 0, -1);
        this.longDateFormat = getDateTimeFormatString(extendedCalendar, 1, -1);
        this.mediumDateFormat = getDateTimeFormatString(extendedCalendar, 2, -1);
        this.shortDateFormat = getDateTimeFormatString(extendedCalendar, 3, -1);
    }

    public static SimpleDateFormatData getInstance(Locale locale2) {
        Objects.requireNonNull(locale2, "locale can't be null");
        Locale compatibleLocaleForBug159514442 = LocaleData.getCompatibleLocaleForBug159514442(locale2);
        String languageTag = compatibleLocaleForBug159514442.toLanguageTag();
        ConcurrentHashMap<String, SimpleDateFormatData> concurrentHashMap = CACHE;
        SimpleDateFormatData simpleDateFormatData = concurrentHashMap.get(languageTag);
        if (simpleDateFormatData != null) {
            return simpleDateFormatData;
        }
        SimpleDateFormatData simpleDateFormatData2 = new SimpleDateFormatData(compatibleLocaleForBug159514442);
        SimpleDateFormatData putIfAbsent = concurrentHashMap.putIfAbsent(languageTag, simpleDateFormatData2);
        return putIfAbsent != null ? putIfAbsent : simpleDateFormatData2;
    }

    public static void initializeCacheInZygote() {
        getInstance(Locale.ROOT);
        getInstance(Locale.f698US);
        getInstance(Locale.getDefault());
    }

    public String getDateFormat(int i) {
        if (i == 0) {
            return this.fullDateFormat;
        }
        if (i == 1) {
            return this.longDateFormat;
        }
        if (i == 2) {
            return this.mediumDateFormat;
        }
        if (i == 3) {
            return this.shortDateFormat;
        }
        throw new AssertionError();
    }

    public String getTimeFormat(int i) {
        if (i == 0) {
            return this.fullTimeFormat;
        }
        if (i == 1) {
            return this.longTimeFormat;
        }
        if (i != 2) {
            if (i != 3) {
                throw new AssertionError();
            } else if (DateFormat.is24Hour == null) {
                return this.shortTimeFormat;
            } else {
                return ICU.getTimePattern(this.locale, DateFormat.is24Hour.booleanValue(), false);
            }
        } else if (DateFormat.is24Hour == null) {
            return this.mediumTimeFormat;
        } else {
            return ICU.getTimePattern(this.locale, DateFormat.is24Hour.booleanValue(), true);
        }
    }

    private static String getDateTimeFormatString(ExtendedCalendar extendedCalendar, int i, int i2) {
        return ICU.transformIcuDateTimePattern_forJavaText(extendedCalendar.getDateTimePattern(i, i2));
    }
}
