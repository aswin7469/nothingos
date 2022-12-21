package android.icu.util;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class Currency extends MeasureUnit {
    public static final int FORMAL_SYMBOL_NAME = 4;
    public static final int LONG_NAME = 1;
    public static final int NARROW_SYMBOL_NAME = 3;
    public static final int PLURAL_LONG_NAME = 2;
    public static final int SYMBOL_NAME = 0;
    public static final int VARIANT_SYMBOL_NAME = 5;

    public enum CurrencyUsage {
        STANDARD,
        CASH
    }

    protected Currency(String str) {
        throw new RuntimeException("Stub!");
    }

    public static Currency getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static Currency getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static String[] getAvailableCurrencyCodes(ULocale uLocale, Date date) {
        throw new RuntimeException("Stub!");
    }

    public static String[] getAvailableCurrencyCodes(Locale locale, Date date) {
        throw new RuntimeException("Stub!");
    }

    public static Set<Currency> getAvailableCurrencies() {
        throw new RuntimeException("Stub!");
    }

    public static Currency getInstance(String str) {
        throw new RuntimeException("Stub!");
    }

    public static Currency fromJavaCurrency(java.util.Currency currency) {
        throw new RuntimeException("Stub!");
    }

    public java.util.Currency toJavaCurrency() {
        throw new RuntimeException("Stub!");
    }

    public static Locale[] getAvailableLocales() {
        throw new RuntimeException("Stub!");
    }

    public static ULocale[] getAvailableULocales() {
        throw new RuntimeException("Stub!");
    }

    public static final String[] getKeywordValuesForLocale(String str, ULocale uLocale, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public String getCurrencyCode() {
        throw new RuntimeException("Stub!");
    }

    public int getNumericCode() {
        throw new RuntimeException("Stub!");
    }

    public String getSymbol() {
        throw new RuntimeException("Stub!");
    }

    public String getSymbol(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public String getSymbol(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public String getName(Locale locale, int i, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public String getName(ULocale uLocale, int i, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public String getName(Locale locale, int i, String str, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public String getName(ULocale uLocale, int i, String str, boolean[] zArr) {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public int getDefaultFractionDigits() {
        throw new RuntimeException("Stub!");
    }

    public int getDefaultFractionDigits(CurrencyUsage currencyUsage) {
        throw new RuntimeException("Stub!");
    }

    public double getRoundingIncrement() {
        throw new RuntimeException("Stub!");
    }

    public double getRoundingIncrement(CurrencyUsage currencyUsage) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public static boolean isAvailable(String str, Date date, Date date2) {
        throw new RuntimeException("Stub!");
    }
}
