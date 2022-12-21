package android.icu.util;

import java.p026io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public abstract class TimeZone implements Serializable, Cloneable, Freezable<TimeZone> {
    public static final int GENERIC_LOCATION = 7;
    public static final TimeZone GMT_ZONE = null;
    public static final int LONG = 1;
    public static final int LONG_GENERIC = 3;
    public static final int LONG_GMT = 5;
    public static final int SHORT = 0;
    public static final int SHORT_COMMONLY_USED = 6;
    public static final int SHORT_GENERIC = 2;
    public static final int SHORT_GMT = 4;
    public static final int TIMEZONE_ICU = 0;
    public static final int TIMEZONE_JDK = 1;
    public static final TimeZone UNKNOWN_ZONE = null;
    public static final String UNKNOWN_ZONE_ID = "Etc/Unknown";

    public enum SystemTimeZoneType {
        ANY,
        CANONICAL,
        CANONICAL_LOCATION
    }

    public abstract int getOffset(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract int getRawOffset();

    public abstract boolean inDaylightTime(Date date);

    public abstract void setRawOffset(int i);

    public abstract boolean useDaylightTime();

    public TimeZone() {
        throw new RuntimeException("Stub!");
    }

    public int getOffset(long j) {
        throw new RuntimeException("Stub!");
    }

    public void getOffset(long j, boolean z, int[] iArr) {
        throw new RuntimeException("Stub!");
    }

    public String getID() {
        throw new RuntimeException("Stub!");
    }

    public void setID(String str) {
        throw new RuntimeException("Stub!");
    }

    public final String getDisplayName() {
        throw new RuntimeException("Stub!");
    }

    public final String getDisplayName(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public final String getDisplayName(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public final String getDisplayName(boolean z, int i) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName(boolean z, int i, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName(boolean z, int i, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public int getDSTSavings() {
        throw new RuntimeException("Stub!");
    }

    public boolean observesDaylightTime() {
        throw new RuntimeException("Stub!");
    }

    public static TimeZone getTimeZone(String str) {
        throw new RuntimeException("Stub!");
    }

    public static TimeZone getFrozenTimeZone(String str) {
        throw new RuntimeException("Stub!");
    }

    public static TimeZone getTimeZone(String str, int i) {
        throw new RuntimeException("Stub!");
    }

    public static Set<String> getAvailableIDs(SystemTimeZoneType systemTimeZoneType, String str, Integer num) {
        throw new RuntimeException("Stub!");
    }

    public static String[] getAvailableIDs(int i) {
        throw new RuntimeException("Stub!");
    }

    public static String[] getAvailableIDs(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String[] getAvailableIDs() {
        throw new RuntimeException("Stub!");
    }

    public static int countEquivalentIDs(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String getEquivalentID(String str, int i) {
        throw new RuntimeException("Stub!");
    }

    public static TimeZone getDefault() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasSameRules(TimeZone timeZone) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public static String getTZDataVersion() {
        throw new RuntimeException("Stub!");
    }

    public static String getCanonicalID(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String getCanonicalID(String str, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public static String getRegion(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String getWindowsID(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String getIDForWindowsID(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFrozen() {
        throw new RuntimeException("Stub!");
    }

    public TimeZone freeze() {
        throw new RuntimeException("Stub!");
    }

    public TimeZone cloneAsThawed() {
        throw new RuntimeException("Stub!");
    }
}
