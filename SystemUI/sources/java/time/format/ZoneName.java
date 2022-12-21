package java.time.format;

import android.icu.text.TimeZoneNames;
import android.icu.util.TimeZone;
import android.icu.util.ULocale;
import java.util.Locale;

class ZoneName {
    ZoneName() {
    }

    public static String toZid(String str, Locale locale) {
        TimeZoneNames instance = TimeZoneNames.getInstance(locale);
        if (instance.getAvailableMetaZoneIDs().contains(str)) {
            ULocale forLocale = ULocale.forLocale(locale);
            String country = forLocale.getCountry();
            if (country.length() == 0) {
                country = ULocale.addLikelySubtags(forLocale).getCountry();
            }
            str = instance.getReferenceZoneID(str, country);
        }
        return toZid(str);
    }

    public static String toZid(String str) {
        String systemCanonicalID = getSystemCanonicalID(str);
        return systemCanonicalID != null ? systemCanonicalID : str;
    }

    public static String getSystemCanonicalID(String str) {
        if (TimeZone.UNKNOWN_ZONE_ID.equals(str)) {
            return str;
        }
        boolean[] zArr = {false};
        String canonicalID = TimeZone.getCanonicalID(str, zArr);
        if (canonicalID == null || !zArr[0]) {
            return null;
        }
        return canonicalID;
    }
}
