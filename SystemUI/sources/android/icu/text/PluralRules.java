package android.icu.text;

import android.icu.number.FormattedNumber;
import android.icu.number.FormattedNumberRange;
import android.icu.util.ULocale;
import java.p026io.Serializable;
import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class PluralRules implements Serializable {
    public static final PluralRules DEFAULT = null;
    public static final String KEYWORD_FEW = "few";
    public static final String KEYWORD_MANY = "many";
    public static final String KEYWORD_ONE = "one";
    public static final String KEYWORD_OTHER = "other";
    public static final String KEYWORD_TWO = "two";
    public static final String KEYWORD_ZERO = "zero";
    public static final double NO_UNIQUE_VALUE = -0.00123456777d;

    public enum PluralType {
        CARDINAL,
        ORDINAL
    }

    private PluralRules() {
        throw new RuntimeException("Stub!");
    }

    public static PluralRules parseDescription(String str) throws ParseException {
        throw new RuntimeException("Stub!");
    }

    public static PluralRules createRules(String str) {
        throw new RuntimeException("Stub!");
    }

    public static PluralRules forLocale(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static PluralRules forLocale(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static PluralRules forLocale(ULocale uLocale, PluralType pluralType) {
        throw new RuntimeException("Stub!");
    }

    public static PluralRules forLocale(Locale locale, PluralType pluralType) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String select(double d) {
        throw new RuntimeException("Stub!");
    }

    public String select(FormattedNumber formattedNumber) {
        throw new RuntimeException("Stub!");
    }

    public String select(FormattedNumberRange formattedNumberRange) {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getKeywords() {
        throw new RuntimeException("Stub!");
    }

    public double getUniqueKeywordValue(String str) {
        throw new RuntimeException("Stub!");
    }

    public Collection<Double> getAllKeywordValues(String str) {
        throw new RuntimeException("Stub!");
    }

    public Collection<Double> getSamples(String str) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(PluralRules pluralRules) {
        throw new RuntimeException("Stub!");
    }
}
