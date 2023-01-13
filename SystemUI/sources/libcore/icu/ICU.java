package libcore.icu;

import android.icu.text.DateFormat;
import android.icu.text.DateTimePatternGenerator;
import android.icu.util.Currency;
import android.icu.util.IllformedLocaleException;
import android.icu.util.ULocale;
import com.android.icu.util.ExtendedCalendar;
import com.android.icu.util.LocaleNative;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import libcore.util.BasicLruCache;
import sun.util.locale.LanguageTag;

public final class ICU {
    private static final BasicLruCache<String, String> CACHED_PATTERNS = new BasicLruCache<>(8);
    private static final int IDX_LANGUAGE = 0;
    private static final int IDX_REGION = 2;
    private static final int IDX_SCRIPT = 1;
    private static final int IDX_VARIANT = 3;
    private static Locale[] availableLocalesCache;
    private static String[] isoCountries;
    private static Set<String> isoCountriesSet;
    private static String[] isoLanguages;

    private static native String[] getAvailableLocalesNative();

    public static native String getCldrVersion();

    public static native String getDefaultLocale();

    public static native String getISO3Country(String str);

    public static native String getISO3Language(String str);

    private static native String[] getISOCountriesNative();

    private static native String[] getISOLanguagesNative();

    public static native String getIcuVersion();

    @Deprecated
    public static native String getScript(String str);

    public static native String getUnicodeVersion();

    private ICU() {
    }

    public static void initializeCacheInZygote() {
        Locale[] localeArr = {Locale.f698US, Locale.getDefault()};
        for (int i = 0; i < 2; i++) {
            Locale locale = localeArr[i];
            getTimePattern(locale, false, false);
            getTimePattern(locale, false, true);
            getTimePattern(locale, true, false);
            getTimePattern(locale, true, true);
        }
    }

    public static String[] getISOLanguages() {
        if (isoLanguages == null) {
            isoLanguages = getISOLanguagesNative();
        }
        return (String[]) isoLanguages.clone();
    }

    public static String[] getISOCountries() {
        return (String[]) getISOCountriesInternal().clone();
    }

    public static boolean isIsoCountry(String str) {
        if (isoCountriesSet == null) {
            String[] iSOCountriesInternal = getISOCountriesInternal();
            HashSet hashSet = new HashSet(iSOCountriesInternal.length);
            for (String add : iSOCountriesInternal) {
                hashSet.add(add);
            }
            isoCountriesSet = hashSet;
        }
        if (str == null || !isoCountriesSet.contains(str)) {
            return false;
        }
        return true;
    }

    private static String[] getISOCountriesInternal() {
        if (isoCountries == null) {
            isoCountries = getISOCountriesNative();
        }
        return isoCountries;
    }

    private static void parseLangScriptRegionAndVariants(String str, String[] strArr) {
        int indexOf = str.indexOf(95);
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(95, i);
        int i2 = indexOf2 + 1;
        int indexOf3 = str.indexOf(95, i2);
        if (indexOf == -1) {
            strArr[0] = str;
        } else if (indexOf2 == -1) {
            strArr[0] = str.substring(0, indexOf);
            String substring = str.substring(i);
            if (substring.length() == 4) {
                strArr[1] = substring;
            } else if (substring.length() == 2 || substring.length() == 3) {
                strArr[2] = substring;
            } else {
                strArr[3] = substring;
            }
        } else if (indexOf3 == -1) {
            strArr[0] = str.substring(0, indexOf);
            String substring2 = str.substring(i, indexOf2);
            String substring3 = str.substring(i2);
            if (substring2.length() == 4) {
                strArr[1] = substring2;
                if (substring3.length() == 2 || substring3.length() == 3 || substring3.isEmpty()) {
                    strArr[2] = substring3;
                } else {
                    strArr[3] = substring3;
                }
            } else if (substring2.isEmpty() || substring2.length() == 2 || substring2.length() == 3) {
                strArr[2] = substring2;
                strArr[3] = substring3;
            } else {
                strArr[3] = str.substring(i);
            }
        } else {
            strArr[0] = str.substring(0, indexOf);
            String substring4 = str.substring(i, indexOf2);
            if (substring4.length() == 4) {
                strArr[1] = substring4;
                strArr[2] = str.substring(i2, indexOf3);
                strArr[3] = str.substring(indexOf3 + 1);
                return;
            }
            strArr[2] = substring4;
            strArr[3] = str.substring(i2);
        }
    }

    public static Locale localeFromIcuLocaleId(String str) {
        int indexOf = str.indexOf(64);
        Map map = Collections.EMPTY_MAP;
        Map map2 = Collections.EMPTY_MAP;
        Set<String> set = Collections.EMPTY_SET;
        if (indexOf != -1) {
            map = new HashMap();
            map2 = new HashMap();
            set = new HashSet<>();
            for (String str2 : str.substring(indexOf + 1).split(NavigationBarInflaterView.GRAVITY_SEPARATOR)) {
                if (str2.startsWith("attribute=")) {
                    for (String add : str2.substring(10).split(LanguageTag.SEP)) {
                        set.add(add);
                    }
                } else {
                    int indexOf2 = str2.indexOf(61);
                    if (indexOf2 == 1) {
                        map.put(Character.valueOf(str2.charAt(0)), str2.substring(2));
                    } else {
                        map2.put(str2.substring(0, indexOf2), str2.substring(indexOf2 + 1));
                    }
                }
            }
        }
        String[] strArr = {"", "", "", ""};
        if (indexOf == -1) {
            parseLangScriptRegionAndVariants(str, strArr);
        } else {
            parseLangScriptRegionAndVariants(str.substring(0, indexOf), strArr);
        }
        Locale.Builder builder = new Locale.Builder();
        builder.setLanguage(strArr[0]);
        builder.setRegion(strArr[2]);
        builder.setVariant(strArr[3]);
        builder.setScript(strArr[1]);
        for (String addUnicodeLocaleAttribute : set) {
            builder.addUnicodeLocaleAttribute(addUnicodeLocaleAttribute);
        }
        for (Map.Entry entry : map2.entrySet()) {
            builder.setUnicodeLocaleKeyword((String) entry.getKey(), (String) entry.getValue());
        }
        for (Map.Entry entry2 : map.entrySet()) {
            builder.setExtension(((Character) entry2.getKey()).charValue(), (String) entry2.getValue());
        }
        return builder.build();
    }

    public static Locale[] localesFromStrings(String[] strArr) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (String localeFromIcuLocaleId : strArr) {
            linkedHashSet.add(localeFromIcuLocaleId(localeFromIcuLocaleId));
        }
        return (Locale[]) linkedHashSet.toArray(new Locale[linkedHashSet.size()]);
    }

    public static Locale[] getAvailableLocales() {
        if (availableLocalesCache == null) {
            availableLocalesCache = localesFromStrings(getAvailableLocalesNative());
        }
        return (Locale[]) availableLocalesCache.clone();
    }

    static String getTimePattern(Locale locale, boolean z, boolean z2) {
        return getBestDateTimePattern(z2 ? z ? DateFormat.HOUR24_MINUTE_SECOND : "hms" : z ? DateFormat.HOUR24_MINUTE : "hm", locale);
    }

    public static String getBestDateTimePattern(String str, Locale locale) {
        String str2;
        String str3 = str + "\t" + locale.toLanguageTag();
        BasicLruCache<String, String> basicLruCache = CACHED_PATTERNS;
        synchronized (basicLruCache) {
            str2 = basicLruCache.get(str3);
            if (str2 == null) {
                str2 = getBestDateTimePattern0(str, locale);
                basicLruCache.put(str3, str2);
            }
        }
        return str2;
    }

    private static String getBestDateTimePattern0(String str, Locale locale) {
        return DateTimePatternGenerator.getInstance(locale).getBestPattern(str);
    }

    private static String getBestDateTimePatternNative(String str, String str2) {
        return getBestDateTimePattern0(str, Locale.forLanguageTag(str2));
    }

    public static char[] getDateFormatOrder(String str) {
        char[] cArr = new char[3];
        int i = 0;
        int i2 = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt == 'd' || charAt == 'L' || charAt == 'M' || charAt == 'y') {
                if (charAt == 'd' && !z) {
                    cArr[i2] = 'd';
                    i2++;
                    z = true;
                } else if ((charAt == 'L' || charAt == 'M') && !z2) {
                    cArr[i2] = 'M';
                    i2++;
                    z2 = true;
                } else if (charAt == 'y' && !z3) {
                    cArr[i2] = 'y';
                    i2++;
                    z3 = true;
                }
            } else if (charAt == 'G') {
                continue;
            } else if ((charAt >= 'a' && charAt <= 'z') || (charAt >= 'A' && charAt <= 'Z')) {
                throw new IllegalArgumentException("Bad pattern character '" + charAt + "' in " + str);
            } else if (charAt != '\'') {
                continue;
            } else {
                if (i < str.length() - 1) {
                    int i3 = i + 1;
                    if (str.charAt(i3) == '\'') {
                        i = i3;
                    }
                }
                int indexOf = str.indexOf(39, i + 1);
                if (indexOf != -1) {
                    i = indexOf + 1;
                } else {
                    throw new IllegalArgumentException("Bad quoting in " + str);
                }
            }
            i++;
        }
        return cArr;
    }

    public static String transformIcuDateTimePattern_forJavaTime(String str) {
        return transformIcuDateTimePattern(str);
    }

    public static String transformIcuDateTimePattern_forJavaText(String str) {
        return transformIcuDateTimePattern(str);
    }

    private static String transformIcuDateTimePattern(String str) {
        if (str == null) {
            return null;
        }
        boolean z = true;
        boolean z2 = str.indexOf(66) != -1;
        boolean z3 = str.indexOf(98) != -1;
        if ((!z2 && !z3) || str.indexOf(72) == -1) {
            z = false;
        }
        if (z) {
            return removeBFromDateTimePattern(str);
        }
        if (str.indexOf(104) == -1) {
            return str;
        }
        if (z3) {
            str = str.replace('b', 'a');
        }
        return z2 ? str.replace('B', 'a') : str;
    }

    private static String removeBFromDateTimePattern(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        int i = 0;
        char c = ' ';
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt != ' ') {
                if (!(charAt == 'B' || charAt == 'b')) {
                    sb.append(charAt);
                }
            } else if (i == 0 || !(c == 'B' || c == 'b')) {
                sb.append(charAt);
            }
            i++;
            c = charAt;
        }
        int length = sb.length() - 1;
        if (length >= 0 && sb.charAt(length) == ' ') {
            sb.deleteCharAt(length);
        }
        return sb.toString();
    }

    public static String getCurrencyCode(String str) {
        if (!(str == null || str.length() == 0)) {
            try {
                String[] availableCurrencyCodes = Currency.getAvailableCurrencyCodes(new ULocale.Builder().setRegion(str).build(), new Date());
                if (!(availableCurrencyCodes == null || availableCurrencyCodes.length == 0)) {
                    return availableCurrencyCodes[0];
                }
            } catch (IllformedLocaleException unused) {
            }
        }
        return null;
    }

    @Deprecated
    public static Locale addLikelySubtags(Locale locale) {
        return ULocale.addLikelySubtags(ULocale.forLocale(locale)).toLocale();
    }

    @Deprecated
    public static String addLikelySubtags(String str) {
        return ULocale.addLikelySubtags(new ULocale(str)).getName();
    }

    public static void setDefaultLocale(String str) {
        LocaleNative.setDefault(str);
    }

    public static ExtendedCalendar getExtendedCalendar(Locale locale, String str) {
        return ExtendedCalendar.getInstance(ULocale.forLocale(locale).setKeywordValue("calendar", str));
    }

    public static String convertToTzId(String str) {
        String legacyType;
        if (str == null || (legacyType = ULocale.toLegacyType("tz", str)) == null || legacyType.equals(str.toLowerCase(Locale.ROOT))) {
            return null;
        }
        return legacyType;
    }
}
