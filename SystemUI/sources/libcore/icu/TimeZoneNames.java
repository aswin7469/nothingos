package libcore.icu;

import android.icu.text.TimeZoneNames;
import com.android.icu.text.TimeZoneNamesNative;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import libcore.util.BasicLruCache;

public final class TimeZoneNames {
    private static final int LONG_NAME = 1;
    private static final int LONG_NAME_DST = 3;
    private static final int NAME_COUNT = 5;
    private static final int OLSON_NAME = 0;
    private static final int SHORT_NAME = 2;
    private static final int SHORT_NAME_DST = 4;
    private static final Comparator<String[]> ZONE_STRINGS_COMPARATOR = new Comparator<String[]>() {
        public int compare(String[] strArr, String[] strArr2) {
            return strArr[0].compareTo(strArr2[0]);
        }
    };
    /* access modifiers changed from: private */
    public static final String[] availableTimeZoneIds = TimeZone.getAvailableIDs();
    private static final ZoneStringsCache cachedZoneStrings = new ZoneStringsCache();

    private static class ZoneStringsCache extends BasicLruCache<Locale, String[][]> {
        public ZoneStringsCache() {
            super(5);
        }

        /* access modifiers changed from: protected */
        public String[][] create(Locale locale) {
            long nanoTime = System.nanoTime();
            long nanoTime2 = System.nanoTime();
            String[][] filledZoneStrings = TimeZoneNamesNative.getFilledZoneStrings(locale, TimeZoneNames.availableTimeZoneIds);
            long nanoTime3 = System.nanoTime();
            addOffsetStrings(filledZoneStrings);
            internStrings(filledZoneStrings);
            long nanoTime4 = System.nanoTime();
            long millis = TimeUnit.NANOSECONDS.toMillis(nanoTime3 - nanoTime2);
            long millis2 = TimeUnit.NANOSECONDS.toMillis(nanoTime4 - nanoTime);
            System.logI("Loaded time zone names for \"" + locale + "\" in " + millis2 + "ms (" + millis + "ms in ICU)");
            return filledZoneStrings;
        }

        private void addOffsetStrings(String[][] strArr) {
            for (int i = 0; i < strArr.length; i++) {
                TimeZone timeZone = null;
                for (int i2 = 1; i2 < 5; i2++) {
                    String[] strArr2 = strArr[i];
                    if (strArr2[i2] == null) {
                        if (timeZone == null) {
                            timeZone = TimeZone.getTimeZone(strArr2[0]);
                        }
                        int rawOffset = timeZone.getRawOffset();
                        if (i2 == 3 || i2 == 4) {
                            rawOffset += timeZone.getDSTSavings();
                        }
                        strArr[i][i2] = TimeZone.createGmtOffsetString(true, true, rawOffset);
                    }
                }
            }
        }

        private void internStrings(String[][] strArr) {
            HashMap hashMap = new HashMap();
            for (int i = 0; i < strArr.length; i++) {
                for (int i2 = 1; i2 < 5; i2++) {
                    String str = strArr[i][i2];
                    String str2 = (String) hashMap.get(str);
                    if (str2 == null) {
                        hashMap.put(str, str);
                    } else {
                        strArr[i][i2] = str2;
                    }
                }
            }
        }
    }

    private TimeZoneNames() {
    }

    public static String getDisplayName(String[][] strArr, String str, boolean z, int i) {
        int binarySearch = Arrays.binarySearch(strArr, new String[]{str}, ZONE_STRINGS_COMPARATOR);
        if (binarySearch < 0) {
            return null;
        }
        String[] strArr2 = strArr[binarySearch];
        return z ? i == 1 ? strArr2[3] : strArr2[4] : i == 1 ? strArr2[1] : strArr2[2];
    }

    public static String[][] getZoneStrings(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return (String[][]) cachedZoneStrings.get(locale);
    }

    public static void getDisplayNames(android.icu.text.TimeZoneNames timeZoneNames, String str, TimeZoneNames.NameType[] nameTypeArr, long j, String[] strArr, int i) {
        for (int i2 = 0; i2 < nameTypeArr.length; i2++) {
            strArr[i + i2] = timeZoneNames.getDisplayName(str, nameTypeArr[i2], j);
        }
    }
}
