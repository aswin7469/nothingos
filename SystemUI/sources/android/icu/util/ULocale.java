package android.icu.util;

import java.p026io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public final class ULocale implements Serializable, Comparable<ULocale> {
    public static final ULocale CANADA = null;
    public static final ULocale CANADA_FRENCH = null;
    public static final ULocale CHINA = null;
    public static final ULocale CHINESE = null;
    public static final ULocale ENGLISH = null;
    public static final ULocale FRANCE = null;
    public static final ULocale FRENCH = null;
    public static final ULocale GERMAN = null;
    public static final ULocale GERMANY = null;
    public static final ULocale ITALIAN = null;
    public static final ULocale ITALY = null;
    public static final ULocale JAPAN = null;
    public static final ULocale JAPANESE = null;
    public static final ULocale KOREA = null;
    public static final ULocale KOREAN = null;
    public static final ULocale PRC = null;
    public static final char PRIVATE_USE_EXTENSION = 'x';
    public static final ULocale ROOT = null;
    public static final ULocale SIMPLIFIED_CHINESE = null;
    public static final ULocale TAIWAN = null;
    public static final ULocale TRADITIONAL_CHINESE = null;

    /* renamed from: UK */
    public static final ULocale f36UK = null;
    public static final char UNICODE_LOCALE_EXTENSION = 'u';

    /* renamed from: US */
    public static final ULocale f37US = null;

    public enum AvailableType {
        DEFAULT,
        ONLY_LEGACY_ALIASES,
        WITH_LEGACY_ALIASES
    }

    public enum Category {
        DISPLAY,
        FORMAT
    }

    public ULocale(String str) {
        throw new RuntimeException("Stub!");
    }

    public ULocale(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public ULocale(String str, String str2, String str3) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale forLocale(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale createCanonical(String str) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale createCanonical(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public Locale toLocale() {
        throw new RuntimeException("Stub!");
    }

    public static ULocale getDefault() {
        throw new RuntimeException("Stub!");
    }

    public static ULocale getDefault(Category category) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int compareTo(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale[] getAvailableLocales() {
        throw new RuntimeException("Stub!");
    }

    public static Collection<ULocale> getAvailableLocalesByType(AvailableType availableType) {
        throw new RuntimeException("Stub!");
    }

    public static String[] getISOCountries() {
        throw new RuntimeException("Stub!");
    }

    public static String[] getISOLanguages() {
        throw new RuntimeException("Stub!");
    }

    public String getLanguage() {
        throw new RuntimeException("Stub!");
    }

    public static String getLanguage(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getScript() {
        throw new RuntimeException("Stub!");
    }

    public static String getScript(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getCountry() {
        throw new RuntimeException("Stub!");
    }

    public static String getCountry(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getVariant() {
        throw new RuntimeException("Stub!");
    }

    public static String getVariant(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String getFallback(String str) {
        throw new RuntimeException("Stub!");
    }

    public ULocale getFallback() {
        throw new RuntimeException("Stub!");
    }

    public String getBaseName() {
        throw new RuntimeException("Stub!");
    }

    public static String getBaseName(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getName() {
        throw new RuntimeException("Stub!");
    }

    public static String getName(String str) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public Iterator<String> getKeywords() {
        throw new RuntimeException("Stub!");
    }

    public static Iterator<String> getKeywords(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getKeywordValue(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String getKeywordValue(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String canonicalize(String str) {
        throw new RuntimeException("Stub!");
    }

    public ULocale setKeywordValue(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String setKeywordValue(String str, String str2, String str3) {
        throw new RuntimeException("Stub!");
    }

    public String getISO3Language() {
        throw new RuntimeException("Stub!");
    }

    public static String getISO3Language(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getISO3Country() {
        throw new RuntimeException("Stub!");
    }

    public static String getISO3Country(String str) {
        throw new RuntimeException("Stub!");
    }

    public boolean isRightToLeft() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayLanguage() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayLanguage(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayLanguage(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayLanguage(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayLanguageWithDialect() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayLanguageWithDialect(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayLanguageWithDialect(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayLanguageWithDialect(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayScript() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayScript(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayScript(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayScript(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayCountry() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayCountry(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayCountry(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayCountry(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayVariant() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayVariant(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayVariant(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayVariant(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayKeyword(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayKeyword(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayKeyword(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayKeywordValue(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayKeywordValue(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayKeywordValue(String str, String str2, String str3) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayKeywordValue(String str, String str2, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayName(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayName(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayNameWithDialect() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayNameWithDialect(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayNameWithDialect(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String getDisplayNameWithDialect(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getCharacterOrientation() {
        throw new RuntimeException("Stub!");
    }

    public String getLineOrientation() {
        throw new RuntimeException("Stub!");
    }

    public static ULocale acceptLanguage(String str, ULocale[] uLocaleArr, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale acceptLanguage(ULocale[] uLocaleArr, ULocale[] uLocaleArr2, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale acceptLanguage(String str, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale acceptLanguage(ULocale[] uLocaleArr, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale addLikelySubtags(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static ULocale minimizeSubtags(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getExtension(char c) {
        throw new RuntimeException("Stub!");
    }

    public Set<Character> getExtensionKeys() {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getUnicodeLocaleAttributes() {
        throw new RuntimeException("Stub!");
    }

    public String getUnicodeLocaleType(String str) {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getUnicodeLocaleKeys() {
        throw new RuntimeException("Stub!");
    }

    public String toLanguageTag() {
        throw new RuntimeException("Stub!");
    }

    public static ULocale forLanguageTag(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String toUnicodeLocaleKey(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String toUnicodeLocaleType(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static String toLegacyKey(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String toLegacyType(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static final class Builder {
        public Builder() {
            throw new RuntimeException("Stub!");
        }

        public Builder setLocale(ULocale uLocale) {
            throw new RuntimeException("Stub!");
        }

        public Builder setLanguageTag(String str) {
            throw new RuntimeException("Stub!");
        }

        public Builder setLanguage(String str) {
            throw new RuntimeException("Stub!");
        }

        public Builder setScript(String str) {
            throw new RuntimeException("Stub!");
        }

        public Builder setRegion(String str) {
            throw new RuntimeException("Stub!");
        }

        public Builder setVariant(String str) {
            throw new RuntimeException("Stub!");
        }

        public Builder setExtension(char c, String str) {
            throw new RuntimeException("Stub!");
        }

        public Builder setUnicodeLocaleKeyword(String str, String str2) {
            throw new RuntimeException("Stub!");
        }

        public Builder addUnicodeLocaleAttribute(String str) {
            throw new RuntimeException("Stub!");
        }

        public Builder removeUnicodeLocaleAttribute(String str) {
            throw new RuntimeException("Stub!");
        }

        public Builder clear() {
            throw new RuntimeException("Stub!");
        }

        public Builder clearExtensions() {
            throw new RuntimeException("Stub!");
        }

        public ULocale build() {
            throw new RuntimeException("Stub!");
        }
    }
}
