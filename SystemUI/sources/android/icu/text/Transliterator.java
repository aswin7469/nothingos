package android.icu.text;

import android.icu.util.ULocale;
import java.util.Enumeration;
import java.util.Locale;

public abstract class Transliterator {
    public static final int FORWARD = 0;
    public static final int REVERSE = 1;

    Transliterator() {
        throw new RuntimeException("Stub!");
    }

    public final int transliterate(Replaceable replaceable, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final void transliterate(Replaceable replaceable) {
        throw new RuntimeException("Stub!");
    }

    public final String transliterate(String str) {
        throw new RuntimeException("Stub!");
    }

    public final void transliterate(Replaceable replaceable, Position position, String str) {
        throw new RuntimeException("Stub!");
    }

    public final void transliterate(Replaceable replaceable, Position position, int i) {
        throw new RuntimeException("Stub!");
    }

    public final void transliterate(Replaceable replaceable, Position position) {
        throw new RuntimeException("Stub!");
    }

    public final void finishTransliteration(Replaceable replaceable, Position position) {
        throw new RuntimeException("Stub!");
    }

    public void filteredTransliterate(Replaceable replaceable, Position position, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public final int getMaximumContextLength() {
        throw new RuntimeException("Stub!");
    }

    public final String getID() {
        throw new RuntimeException("Stub!");
    }

    public static final String getDisplayName(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayName(String str, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayName(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeFilter getFilter() {
        throw new RuntimeException("Stub!");
    }

    public void setFilter(UnicodeFilter unicodeFilter) {
        throw new RuntimeException("Stub!");
    }

    public static final Transliterator getInstance(String str) {
        throw new RuntimeException("Stub!");
    }

    public static Transliterator getInstance(String str, int i) {
        throw new RuntimeException("Stub!");
    }

    public static final Transliterator createFromRules(String str, String str2, int i) {
        throw new RuntimeException("Stub!");
    }

    public String toRules(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public Transliterator[] getElements() {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet getSourceSet() {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet getTargetSet() {
        throw new RuntimeException("Stub!");
    }

    public final Transliterator getInverse() {
        throw new RuntimeException("Stub!");
    }

    public static final Enumeration<String> getAvailableIDs() {
        throw new RuntimeException("Stub!");
    }

    public static final Enumeration<String> getAvailableSources() {
        throw new RuntimeException("Stub!");
    }

    public static final Enumeration<String> getAvailableTargets(String str) {
        throw new RuntimeException("Stub!");
    }

    public static final Enumeration<String> getAvailableVariants(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static class Position {
        public int contextLimit;
        public int contextStart;
        public int limit;
        public int start;

        public Position() {
            throw new RuntimeException("Stub!");
        }

        public Position(int i, int i2, int i3) {
            throw new RuntimeException("Stub!");
        }

        public Position(int i, int i2, int i3, int i4) {
            throw new RuntimeException("Stub!");
        }

        public Position(Position position) {
            throw new RuntimeException("Stub!");
        }

        public void set(Position position) {
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

        public final void validate(int i) {
            throw new RuntimeException("Stub!");
        }
    }
}
