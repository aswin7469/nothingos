package android.icu.text;

import android.icu.util.Freezable;
import android.icu.util.ULocale;
import java.p026io.Serializable;
import java.util.Locale;

public class DateIntervalInfo implements Cloneable, Freezable<DateIntervalInfo>, Serializable {
    public DateIntervalInfo(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public DateIntervalInfo(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public void setIntervalPattern(String str, int i, String str2) {
        throw new RuntimeException("Stub!");
    }

    public PatternInfo getIntervalPattern(String str, int i) {
        throw new RuntimeException("Stub!");
    }

    public String getFallbackIntervalPattern() {
        throw new RuntimeException("Stub!");
    }

    public void setFallbackIntervalPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public boolean getDefaultOrder() {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public boolean isFrozen() {
        throw new RuntimeException("Stub!");
    }

    public DateIntervalInfo freeze() {
        throw new RuntimeException("Stub!");
    }

    public DateIntervalInfo cloneAsThawed() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public static final class PatternInfo implements Cloneable, Serializable {
        public PatternInfo(String str, String str2, boolean z) {
            throw new RuntimeException("Stub!");
        }

        public String getFirstPart() {
            throw new RuntimeException("Stub!");
        }

        public String getSecondPart() {
            throw new RuntimeException("Stub!");
        }

        public boolean firstDateInPtnIsLaterDate() {
            throw new RuntimeException("Stub!");
        }

        public boolean equals(Object obj) {
            throw new RuntimeException("Stub!");
        }

        public int hashCode() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }
}
