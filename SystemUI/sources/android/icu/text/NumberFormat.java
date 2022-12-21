package android.icu.text;

import android.icu.math.BigDecimal;
import android.icu.text.DisplayContext;
import android.icu.util.Currency;
import android.icu.util.CurrencyAmount;
import android.icu.util.ULocale;
import java.math.BigInteger;
import java.p026io.InvalidObjectException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

public abstract class NumberFormat extends UFormat {
    public static final int ACCOUNTINGCURRENCYSTYLE = 7;
    public static final int CASHCURRENCYSTYLE = 8;
    public static final int CURRENCYSTYLE = 1;
    public static final int FRACTION_FIELD = 1;
    public static final int INTEGERSTYLE = 4;
    public static final int INTEGER_FIELD = 0;
    public static final int ISOCURRENCYSTYLE = 5;
    public static final int NUMBERSTYLE = 0;
    public static final int PERCENTSTYLE = 2;
    public static final int PLURALCURRENCYSTYLE = 6;
    public static final int SCIENTIFICSTYLE = 3;
    public static final int STANDARDCURRENCYSTYLE = 9;

    public abstract StringBuffer format(double d, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract StringBuffer format(long j, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract StringBuffer format(BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract StringBuffer format(java.math.BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract StringBuffer format(BigInteger bigInteger, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Number parse(String str, ParsePosition parsePosition);

    public NumberFormat() {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public final Object parseObject(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public final String format(double d) {
        throw new RuntimeException("Stub!");
    }

    public final String format(long j) {
        throw new RuntimeException("Stub!");
    }

    public final String format(BigInteger bigInteger) {
        throw new RuntimeException("Stub!");
    }

    public final String format(java.math.BigDecimal bigDecimal) {
        throw new RuntimeException("Stub!");
    }

    public final String format(BigDecimal bigDecimal) {
        throw new RuntimeException("Stub!");
    }

    public final String format(CurrencyAmount currencyAmount) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(CurrencyAmount currencyAmount, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public Number parse(String str) throws ParseException {
        throw new RuntimeException("Stub!");
    }

    public CurrencyAmount parseCurrency(CharSequence charSequence, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public boolean isParseIntegerOnly() {
        throw new RuntimeException("Stub!");
    }

    public void setParseIntegerOnly(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public void setParseStrict(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public boolean isParseStrict() {
        throw new RuntimeException("Stub!");
    }

    public void setContext(DisplayContext displayContext) {
        throw new RuntimeException("Stub!");
    }

    public DisplayContext getContext(DisplayContext.Type type) {
        throw new RuntimeException("Stub!");
    }

    public static final NumberFormat getInstance() {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static final NumberFormat getInstance(int i) {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getInstance(Locale locale, int i) {
        throw new RuntimeException("Stub!");
    }

    public static final NumberFormat getNumberInstance() {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getNumberInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getNumberInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static final NumberFormat getIntegerInstance() {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getIntegerInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getIntegerInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static final NumberFormat getCurrencyInstance() {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getCurrencyInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getCurrencyInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static final NumberFormat getPercentInstance() {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getPercentInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getPercentInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static final NumberFormat getScientificInstance() {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getScientificInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getScientificInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static Locale[] getAvailableLocales() {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public boolean isGroupingUsed() {
        throw new RuntimeException("Stub!");
    }

    public void setGroupingUsed(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public int getMaximumIntegerDigits() {
        throw new RuntimeException("Stub!");
    }

    public void setMaximumIntegerDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getMinimumIntegerDigits() {
        throw new RuntimeException("Stub!");
    }

    public void setMinimumIntegerDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getMaximumFractionDigits() {
        throw new RuntimeException("Stub!");
    }

    public void setMaximumFractionDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getMinimumFractionDigits() {
        throw new RuntimeException("Stub!");
    }

    public void setMinimumFractionDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public void setCurrency(Currency currency) {
        throw new RuntimeException("Stub!");
    }

    public Currency getCurrency() {
        throw new RuntimeException("Stub!");
    }

    public int getRoundingMode() {
        throw new RuntimeException("Stub!");
    }

    public void setRoundingMode(int i) {
        throw new RuntimeException("Stub!");
    }

    public static NumberFormat getInstance(ULocale uLocale, int i) {
        throw new RuntimeException("Stub!");
    }

    protected static String getPattern(ULocale uLocale, int i) {
        throw new RuntimeException("Stub!");
    }

    public static class Field extends Format.Field {
        public static final Field COMPACT = null;
        public static final Field CURRENCY = null;
        public static final Field DECIMAL_SEPARATOR = null;
        public static final Field EXPONENT = null;
        public static final Field EXPONENT_SIGN = null;
        public static final Field EXPONENT_SYMBOL = null;
        public static final Field FRACTION = null;
        public static final Field GROUPING_SEPARATOR = null;
        public static final Field INTEGER = null;
        public static final Field MEASURE_UNIT = null;
        public static final Field PERCENT = null;
        public static final Field PERMILLE = null;
        public static final Field SIGN = null;

        protected Field(String str) {
            super((String) null);
            throw new RuntimeException("Stub!");
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws InvalidObjectException {
            throw new RuntimeException("Stub!");
        }
    }
}
