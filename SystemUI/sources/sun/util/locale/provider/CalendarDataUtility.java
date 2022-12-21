package sun.util.locale.provider;

import android.icu.lang.UCharacter;
import android.icu.text.DateFormatSymbols;
import android.icu.util.ULocale;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarDataUtility {
    private static final String BUDDHIST_CALENDAR = "buddhist";
    private static final String GREGORIAN_CALENDAR = "gregorian";
    private static final String ISLAMIC_CALENDAR = "islamic";
    private static final String JAPANESE_CALENDAR = "japanese";
    private static int[] REST_OF_STYLES = {Calendar.SHORT_STANDALONE, 2, Calendar.LONG_STANDALONE, 4, Calendar.NARROW_STANDALONE};

    private CalendarDataUtility() {
    }

    public static String retrieveFieldValueName(String str, int i, int i2, int i3, Locale locale) {
        if (i == 0) {
            String normalizeCalendarType = normalizeCalendarType(str);
            normalizeCalendarType.hashCode();
            char c = 65535;
            switch (normalizeCalendarType.hashCode()) {
                case -1581060683:
                    if (normalizeCalendarType.equals(BUDDHIST_CALENDAR)) {
                        c = 0;
                        break;
                    }
                    break;
                case -752730191:
                    if (normalizeCalendarType.equals(JAPANESE_CALENDAR)) {
                        c = 1;
                        break;
                    }
                    break;
                case 2093696456:
                    if (normalizeCalendarType.equals(ISLAMIC_CALENDAR)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 2:
                    i2--;
                    break;
                case 1:
                    i2 += UCharacter.UnicodeBlock.LATIN_EXTENDED_E_ID;
                    break;
            }
        }
        if (i2 < 0) {
            return null;
        }
        String[] names = getNames(str, i, i3, locale);
        if (i2 >= names.length) {
            return null;
        }
        return names[i2];
    }

    public static String retrieveJavaTimeFieldValueName(String str, int i, int i2, int i3, Locale locale) {
        return retrieveFieldValueName(str, i, i2, i3, locale);
    }

    public static Map<String, Integer> retrieveFieldValueNames(String str, int i, int i2, Locale locale) {
        Map<String, Integer> map;
        if (i2 == 0) {
            map = retrieveFieldValueNamesImpl(str, i, 1, locale);
            for (int retrieveFieldValueNamesImpl : REST_OF_STYLES) {
                map.putAll(retrieveFieldValueNamesImpl(str, i, retrieveFieldValueNamesImpl, locale));
            }
        } else {
            map = retrieveFieldValueNamesImpl(str, i, i2, locale);
        }
        if (map.isEmpty()) {
            return null;
        }
        return map;
    }

    public static Map<String, Integer> retrieveJavaTimeFieldValueNames(String str, int i, int i2, Locale locale) {
        return retrieveFieldValueNames(str, i, i2, locale);
    }

    private static String normalizeCalendarType(String str) {
        if (str.equals("gregory") || str.equals("iso8601")) {
            return GREGORIAN_CALENDAR;
        }
        return str.startsWith(ISLAMIC_CALENDAR) ? ISLAMIC_CALENDAR : str;
    }

    private static Map<String, Integer> retrieveFieldValueNamesImpl(String str, int i, int i2, Locale locale) {
        int i3;
        String[] names = getNames(str, i, i2, locale);
        int i4 = 0;
        if (i == 0) {
            String normalizeCalendarType = normalizeCalendarType(str);
            normalizeCalendarType.hashCode();
            char c = 65535;
            switch (normalizeCalendarType.hashCode()) {
                case -1581060683:
                    if (normalizeCalendarType.equals(BUDDHIST_CALENDAR)) {
                        c = 0;
                        break;
                    }
                    break;
                case -752730191:
                    if (normalizeCalendarType.equals(JAPANESE_CALENDAR)) {
                        c = 1;
                        break;
                    }
                    break;
                case 2093696456:
                    if (normalizeCalendarType.equals(ISLAMIC_CALENDAR)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 2:
                    i3 = 1;
                    break;
                case 1:
                    i4 = 232;
                    i3 = -231;
                    break;
            }
        }
        i3 = 0;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        while (i4 < names.length) {
            if (!names[i4].isEmpty() && linkedHashMap.put(names[i4], Integer.valueOf(i4 + i3)) != null) {
                return new LinkedHashMap();
            }
            i4++;
        }
        return linkedHashMap;
    }

    private static String[] getNames(String str, int i, int i2, Locale locale) {
        int context = toContext(i2);
        int width = toWidth(i2);
        DateFormatSymbols dateFormatSymbols = getDateFormatSymbols(str, locale);
        if (i != 0) {
            if (i == 2) {
                return dateFormatSymbols.getMonths(context, width);
            }
            if (i == 7) {
                return dateFormatSymbols.getWeekdays(context, width);
            }
            if (i == 9) {
                return dateFormatSymbols.getAmPmStrings();
            }
            throw new UnsupportedOperationException("Unknown field: " + i);
        } else if (width == 0) {
            return dateFormatSymbols.getEras();
        } else {
            if (width == 1) {
                return dateFormatSymbols.getEraNames();
            }
            if (width == 2) {
                return dateFormatSymbols.getNarrowEras();
            }
            throw new UnsupportedOperationException("Unknown width: " + width);
        }
    }

    private static DateFormatSymbols getDateFormatSymbols(String str, Locale locale) {
        return new DateFormatSymbols(ULocale.forLocale(locale).setKeywordValue("calendar", normalizeCalendarType(str)));
    }

    private static int toWidth(int i) {
        if (i == 1) {
            return 0;
        }
        if (i != 2) {
            if (i == 4 || i == 32772) {
                return 2;
            }
            switch (i) {
                case Calendar.SHORT_STANDALONE:
                    return 0;
                case Calendar.LONG_STANDALONE:
                    break;
                default:
                    throw new IllegalArgumentException("Invalid style: " + i);
            }
        }
        return 1;
    }

    private static int toContext(int i) {
        if (i == 1 || i == 2 || i == 4) {
            return 0;
        }
        if (i != 32772) {
            switch (i) {
                case Calendar.SHORT_STANDALONE:
                case Calendar.LONG_STANDALONE:
                    break;
                default:
                    throw new IllegalArgumentException("Invalid style: " + i);
            }
        }
        return 1;
    }
}
