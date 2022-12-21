package android.icu.text;

import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import android.icu.util.ULocale;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;

public class MeasureFormat extends UFormat {

    public enum FormatWidth {
        WIDE,
        SHORT,
        NARROW,
        NUMERIC
    }

    MeasureFormat() {
        throw new RuntimeException("Stub!");
    }

    public static MeasureFormat getInstance(ULocale uLocale, FormatWidth formatWidth) {
        throw new RuntimeException("Stub!");
    }

    public static MeasureFormat getInstance(Locale locale, FormatWidth formatWidth) {
        throw new RuntimeException("Stub!");
    }

    public static MeasureFormat getInstance(ULocale uLocale, FormatWidth formatWidth, NumberFormat numberFormat) {
        throw new RuntimeException("Stub!");
    }

    public static MeasureFormat getInstance(Locale locale, FormatWidth formatWidth, NumberFormat numberFormat) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public Measure parseObject(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public final String formatMeasures(Measure... measureArr) {
        throw new RuntimeException("Stub!");
    }

    public StringBuilder formatMeasurePerUnit(Measure measure, MeasureUnit measureUnit, StringBuilder sb, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public StringBuilder formatMeasures(StringBuilder sb, FieldPosition fieldPosition, Measure... measureArr) {
        throw new RuntimeException("Stub!");
    }

    public String getUnitDisplayName(MeasureUnit measureUnit) {
        throw new RuntimeException("Stub!");
    }

    public final boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public final int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public FormatWidth getWidth() {
        throw new RuntimeException("Stub!");
    }

    public final ULocale getLocale() {
        throw new RuntimeException("Stub!");
    }

    public NumberFormat getNumberFormat() {
        throw new RuntimeException("Stub!");
    }

    public static MeasureFormat getCurrencyFormat(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static MeasureFormat getCurrencyFormat(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static MeasureFormat getCurrencyFormat() {
        throw new RuntimeException("Stub!");
    }
}
