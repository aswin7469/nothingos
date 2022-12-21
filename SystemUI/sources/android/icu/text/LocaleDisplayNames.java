package android.icu.text;

import android.icu.text.DisplayContext;
import android.icu.util.ULocale;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public abstract class LocaleDisplayNames {

    public enum DialectHandling {
        STANDARD_NAMES,
        DIALECT_NAMES
    }

    public abstract DisplayContext getContext(DisplayContext.Type type);

    public abstract DialectHandling getDialectHandling();

    public abstract ULocale getLocale();

    public abstract List<UiListItem> getUiListCompareWholeItems(Set<ULocale> set, Comparator<UiListItem> comparator);

    public abstract String keyDisplayName(String str);

    public abstract String keyValueDisplayName(String str, String str2);

    public abstract String languageDisplayName(String str);

    public abstract String localeDisplayName(ULocale uLocale);

    public abstract String localeDisplayName(String str);

    public abstract String localeDisplayName(Locale locale);

    public abstract String regionDisplayName(String str);

    public abstract String scriptDisplayName(int i);

    public abstract String scriptDisplayName(String str);

    public abstract String variantDisplayName(String str);

    LocaleDisplayNames() {
        throw new RuntimeException("Stub!");
    }

    public static LocaleDisplayNames getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static LocaleDisplayNames getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static LocaleDisplayNames getInstance(ULocale uLocale, DialectHandling dialectHandling) {
        throw new RuntimeException("Stub!");
    }

    public static LocaleDisplayNames getInstance(ULocale uLocale, DisplayContext... displayContextArr) {
        throw new RuntimeException("Stub!");
    }

    public static LocaleDisplayNames getInstance(Locale locale, DisplayContext... displayContextArr) {
        throw new RuntimeException("Stub!");
    }

    public List<UiListItem> getUiList(Set<ULocale> set, boolean z, Comparator<Object> comparator) {
        throw new RuntimeException("Stub!");
    }

    public static class UiListItem {
        public final ULocale minimized = null;
        public final ULocale modified = null;
        public final String nameInDisplayLocale = null;
        public final String nameInSelf = null;

        public UiListItem(ULocale uLocale, ULocale uLocale2, String str, String str2) {
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

        public static Comparator<UiListItem> getComparator(Comparator<Object> comparator, boolean z) {
            throw new RuntimeException("Stub!");
        }
    }
}
