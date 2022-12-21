package android.icu.text;

import android.icu.util.Currency;
import android.icu.util.ULocale;
import java.p026io.Serializable;
import java.util.Locale;

public class DecimalFormatSymbols implements Cloneable, Serializable {
    public static final int CURRENCY_SPC_CURRENCY_MATCH = 0;
    public static final int CURRENCY_SPC_INSERT = 2;
    public static final int CURRENCY_SPC_SURROUNDING_MATCH = 1;

    public DecimalFormatSymbols() {
        throw new RuntimeException("Stub!");
    }

    public DecimalFormatSymbols(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public DecimalFormatSymbols(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static DecimalFormatSymbols getInstance() {
        throw new RuntimeException("Stub!");
    }

    public static DecimalFormatSymbols getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static DecimalFormatSymbols getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static DecimalFormatSymbols forNumberingSystem(Locale locale, NumberingSystem numberingSystem) {
        throw new RuntimeException("Stub!");
    }

    public static DecimalFormatSymbols forNumberingSystem(ULocale uLocale, NumberingSystem numberingSystem) {
        throw new RuntimeException("Stub!");
    }

    public static Locale[] getAvailableLocales() {
        throw new RuntimeException("Stub!");
    }

    public static ULocale[] getAvailableULocales() {
        throw new RuntimeException("Stub!");
    }

    public char getZeroDigit() {
        throw new RuntimeException("Stub!");
    }

    public char[] getDigits() {
        throw new RuntimeException("Stub!");
    }

    public void setZeroDigit(char c) {
        throw new RuntimeException("Stub!");
    }

    public String[] getDigitStrings() {
        throw new RuntimeException("Stub!");
    }

    public void setDigitStrings(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public char getSignificantDigit() {
        throw new RuntimeException("Stub!");
    }

    public void setSignificantDigit(char c) {
        throw new RuntimeException("Stub!");
    }

    public char getGroupingSeparator() {
        throw new RuntimeException("Stub!");
    }

    public void setGroupingSeparator(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getGroupingSeparatorString() {
        throw new RuntimeException("Stub!");
    }

    public void setGroupingSeparatorString(String str) {
        throw new RuntimeException("Stub!");
    }

    public char getDecimalSeparator() {
        throw new RuntimeException("Stub!");
    }

    public void setDecimalSeparator(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getDecimalSeparatorString() {
        throw new RuntimeException("Stub!");
    }

    public void setDecimalSeparatorString(String str) {
        throw new RuntimeException("Stub!");
    }

    public char getPerMill() {
        throw new RuntimeException("Stub!");
    }

    public void setPerMill(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getPerMillString() {
        throw new RuntimeException("Stub!");
    }

    public void setPerMillString(String str) {
        throw new RuntimeException("Stub!");
    }

    public char getPercent() {
        throw new RuntimeException("Stub!");
    }

    public void setPercent(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getPercentString() {
        throw new RuntimeException("Stub!");
    }

    public void setPercentString(String str) {
        throw new RuntimeException("Stub!");
    }

    public char getDigit() {
        throw new RuntimeException("Stub!");
    }

    public void setDigit(char c) {
        throw new RuntimeException("Stub!");
    }

    public char getPatternSeparator() {
        throw new RuntimeException("Stub!");
    }

    public void setPatternSeparator(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getInfinity() {
        throw new RuntimeException("Stub!");
    }

    public void setInfinity(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getNaN() {
        throw new RuntimeException("Stub!");
    }

    public void setNaN(String str) {
        throw new RuntimeException("Stub!");
    }

    public char getMinusSign() {
        throw new RuntimeException("Stub!");
    }

    public void setMinusSign(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getMinusSignString() {
        throw new RuntimeException("Stub!");
    }

    public void setMinusSignString(String str) {
        throw new RuntimeException("Stub!");
    }

    public char getPlusSign() {
        throw new RuntimeException("Stub!");
    }

    public void setPlusSign(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getPlusSignString() {
        throw new RuntimeException("Stub!");
    }

    public void setPlusSignString(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getCurrencySymbol() {
        throw new RuntimeException("Stub!");
    }

    public void setCurrencySymbol(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getInternationalCurrencySymbol() {
        throw new RuntimeException("Stub!");
    }

    public void setInternationalCurrencySymbol(String str) {
        throw new RuntimeException("Stub!");
    }

    public Currency getCurrency() {
        throw new RuntimeException("Stub!");
    }

    public void setCurrency(Currency currency) {
        throw new RuntimeException("Stub!");
    }

    public char getMonetaryDecimalSeparator() {
        throw new RuntimeException("Stub!");
    }

    public void setMonetaryDecimalSeparator(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getMonetaryDecimalSeparatorString() {
        throw new RuntimeException("Stub!");
    }

    public void setMonetaryDecimalSeparatorString(String str) {
        throw new RuntimeException("Stub!");
    }

    public char getMonetaryGroupingSeparator() {
        throw new RuntimeException("Stub!");
    }

    public void setMonetaryGroupingSeparator(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getMonetaryGroupingSeparatorString() {
        throw new RuntimeException("Stub!");
    }

    public void setMonetaryGroupingSeparatorString(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getExponentMultiplicationSign() {
        throw new RuntimeException("Stub!");
    }

    public void setExponentMultiplicationSign(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getExponentSeparator() {
        throw new RuntimeException("Stub!");
    }

    public void setExponentSeparator(String str) {
        throw new RuntimeException("Stub!");
    }

    public char getPadEscape() {
        throw new RuntimeException("Stub!");
    }

    public void setPadEscape(char c) {
        throw new RuntimeException("Stub!");
    }

    public String getPatternForCurrencySpacing(int i, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public void setPatternForCurrencySpacing(int i, boolean z, String str) {
        throw new RuntimeException("Stub!");
    }

    public Locale getLocale() {
        throw new RuntimeException("Stub!");
    }

    public ULocale getULocale() {
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
}
