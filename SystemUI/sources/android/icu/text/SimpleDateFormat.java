package android.icu.text;

import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.icu.util.ULocale;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

public class SimpleDateFormat extends DateFormat {
    public SimpleDateFormat() {
        throw new RuntimeException("Stub!");
    }

    public SimpleDateFormat(String str) {
        throw new RuntimeException("Stub!");
    }

    public SimpleDateFormat(String str, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public SimpleDateFormat(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public SimpleDateFormat(String str, String str2, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public SimpleDateFormat(String str, DateFormatSymbols dateFormatSymbols) {
        throw new RuntimeException("Stub!");
    }

    public void set2DigitYearStart(Date date) {
        throw new RuntimeException("Stub!");
    }

    public Date get2DigitYearStart() {
        throw new RuntimeException("Stub!");
    }

    public void setContext(DisplayContext displayContext) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(Calendar calendar, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public DateFormat.Field patternCharToDateFormatField(char c) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public String subFormat(char c, int i, int i2, FieldPosition fieldPosition, DateFormatSymbols dateFormatSymbols, Calendar calendar) throws IllegalArgumentException {
        throw new RuntimeException("Stub!");
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public String zeroPaddingNumber(long j, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void parse(String str, Calendar calendar, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int matchString(String str, int i, int i2, String[] strArr, Calendar calendar) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int matchQuarterString(String str, int i, int i2, String[] strArr, Calendar calendar) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int subParse(String str, int i, char c, int i2, boolean z, boolean z2, boolean[] zArr, Calendar calendar) {
        throw new RuntimeException("Stub!");
    }

    public String toPattern() {
        throw new RuntimeException("Stub!");
    }

    public String toLocalizedPattern() {
        throw new RuntimeException("Stub!");
    }

    public void applyPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public void applyLocalizedPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols getDateFormatSymbols() {
        throw new RuntimeException("Stub!");
    }

    public void setDateFormatSymbols(DateFormatSymbols dateFormatSymbols) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public DateFormatSymbols getSymbols() {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat getTimeZoneFormat() {
        throw new RuntimeException("Stub!");
    }

    public void setTimeZoneFormat(TimeZoneFormat timeZoneFormat) {
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

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public void setNumberFormat(String str, NumberFormat numberFormat) {
        throw new RuntimeException("Stub!");
    }

    public NumberFormat getNumberFormat(char c) {
        throw new RuntimeException("Stub!");
    }
}
