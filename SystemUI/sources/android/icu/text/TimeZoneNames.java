package android.icu.text;

import android.icu.util.ULocale;
import java.p026io.Serializable;
import java.util.Locale;
import java.util.Set;

public abstract class TimeZoneNames implements Serializable {

    public enum NameType {
        LONG_GENERIC,
        LONG_STANDARD,
        LONG_DAYLIGHT,
        SHORT_GENERIC,
        SHORT_STANDARD,
        SHORT_DAYLIGHT,
        EXEMPLAR_LOCATION
    }

    public abstract Set<String> getAvailableMetaZoneIDs();

    public abstract Set<String> getAvailableMetaZoneIDs(String str);

    public abstract String getMetaZoneDisplayName(String str, NameType nameType);

    public abstract String getMetaZoneID(String str, long j);

    public abstract String getReferenceZoneID(String str, String str2);

    public abstract String getTimeZoneDisplayName(String str, NameType nameType);

    TimeZoneNames() {
        throw new RuntimeException("Stub!");
    }

    public static TimeZoneNames getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static TimeZoneNames getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static TimeZoneNames getTZDBInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public final String getDisplayName(String str, NameType nameType, long j) {
        throw new RuntimeException("Stub!");
    }

    public String getExemplarLocationName(String str) {
        throw new RuntimeException("Stub!");
    }
}
