package android.icu.text;

import android.icu.util.Currency;
import android.icu.util.CurrencyAmount;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParsePosition;

public class DecimalFormat extends NumberFormat {
    public static final int MINIMUM_GROUPING_DIGITS_AUTO = -2;
    public static final int MINIMUM_GROUPING_DIGITS_MIN2 = -3;
    public static final int PAD_AFTER_PREFIX = 1;
    public static final int PAD_AFTER_SUFFIX = 3;
    public static final int PAD_BEFORE_PREFIX = 0;
    public static final int PAD_BEFORE_SUFFIX = 2;

    public DecimalFormat() {
        throw new RuntimeException("Stub!");
    }

    public DecimalFormat(String str) {
        throw new RuntimeException("Stub!");
    }

    public DecimalFormat(String str, DecimalFormatSymbols decimalFormatSymbols) {
        throw new RuntimeException("Stub!");
    }

    public DecimalFormat(String str, DecimalFormatSymbols decimalFormatSymbols, CurrencyPluralInfo currencyPluralInfo, int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void applyPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void applyLocalizedPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(double d, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(long j, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(BigInteger bigInteger, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(android.icu.math.BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(CurrencyAmount currencyAmount, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public Number parse(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public CurrencyAmount parseCurrency(CharSequence charSequence, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public synchronized DecimalFormatSymbols getDecimalFormatSymbols() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDecimalFormatSymbols(DecimalFormatSymbols decimalFormatSymbols) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getPositivePrefix() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setPositivePrefix(String str) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getNegativePrefix() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setNegativePrefix(String str) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getPositiveSuffix() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setPositiveSuffix(String str) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getNegativeSuffix() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setNegativeSuffix(String str) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isSignAlwaysShown() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setSignAlwaysShown(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMultiplier() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMultiplier(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized BigDecimal getRoundingIncrement() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setRoundingIncrement(BigDecimal bigDecimal) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setRoundingIncrement(android.icu.math.BigDecimal bigDecimal) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setRoundingIncrement(double d) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getRoundingMode() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setRoundingMode(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized MathContext getMathContext() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMathContext(MathContext mathContext) {
        throw new RuntimeException("Stub!");
    }

    public synchronized android.icu.math.MathContext getMathContextICU() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMathContextICU(android.icu.math.MathContext mathContext) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMinimumIntegerDigits() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMinimumIntegerDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMaximumIntegerDigits() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMaximumIntegerDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMinimumFractionDigits() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMinimumFractionDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMaximumFractionDigits() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMaximumFractionDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean areSignificantDigitsUsed() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setSignificantDigitsUsed(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMinimumSignificantDigits() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMinimumSignificantDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMaximumSignificantDigits() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMaximumSignificantDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getFormatWidth() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setFormatWidth(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized char getPadCharacter() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setPadCharacter(char c) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getPadPosition() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setPadPosition(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isScientificNotation() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setScientificNotation(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized byte getMinimumExponentDigits() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMinimumExponentDigits(byte b) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isExponentSignAlwaysShown() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setExponentSignAlwaysShown(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isGroupingUsed() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setGroupingUsed(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getGroupingSize() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setGroupingSize(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getSecondaryGroupingSize() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setSecondaryGroupingSize(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMinimumGroupingDigits() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMinimumGroupingDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isDecimalSeparatorAlwaysShown() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDecimalSeparatorAlwaysShown(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized Currency getCurrency() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setCurrency(Currency currency) {
        throw new RuntimeException("Stub!");
    }

    public synchronized Currency.CurrencyUsage getCurrencyUsage() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setCurrencyUsage(Currency.CurrencyUsage currencyUsage) {
        throw new RuntimeException("Stub!");
    }

    public synchronized CurrencyPluralInfo getCurrencyPluralInfo() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setCurrencyPluralInfo(CurrencyPluralInfo currencyPluralInfo) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isParseBigDecimal() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setParseBigDecimal(boolean z) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getParseMaxDigits() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setParseMaxDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isParseStrict() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setParseStrict(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isParseIntegerOnly() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setParseIntegerOnly(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isDecimalPatternMatchRequired() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDecimalPatternMatchRequired(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isParseNoExponent() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setParseNoExponent(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isParseCaseSensitive() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setParseCaseSensitive(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public synchronized String toPattern() {
        throw new RuntimeException("Stub!");
    }

    public synchronized String toLocalizedPattern() {
        throw new RuntimeException("Stub!");
    }
}
