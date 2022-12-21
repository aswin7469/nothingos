package android.icu.text;

import android.icu.util.Freezable;
import android.icu.util.Output;
import android.icu.util.TimeZone;
import android.icu.util.ULocale;
import java.p026io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.EnumSet;
import java.util.Locale;

public class TimeZoneFormat extends UFormat implements Freezable<TimeZoneFormat>, Serializable {

    public enum GMTOffsetPatternType {
        POSITIVE_HM,
        POSITIVE_HMS,
        NEGATIVE_HM,
        NEGATIVE_HMS,
        POSITIVE_H,
        NEGATIVE_H
    }

    public enum ParseOption {
        ALL_STYLES,
        TZ_DATABASE_ABBREVIATIONS
    }

    public enum Style {
        GENERIC_LOCATION,
        GENERIC_LONG,
        GENERIC_SHORT,
        SPECIFIC_LONG,
        SPECIFIC_SHORT,
        LOCALIZED_GMT,
        LOCALIZED_GMT_SHORT,
        ISO_BASIC_SHORT,
        ISO_BASIC_LOCAL_SHORT,
        ISO_BASIC_FIXED,
        ISO_BASIC_LOCAL_FIXED,
        ISO_BASIC_FULL,
        ISO_BASIC_LOCAL_FULL,
        ISO_EXTENDED_FIXED,
        ISO_EXTENDED_LOCAL_FIXED,
        ISO_EXTENDED_FULL,
        ISO_EXTENDED_LOCAL_FULL,
        ZONE_ID,
        ZONE_ID_SHORT,
        EXEMPLAR_LOCATION
    }

    public enum TimeType {
        UNKNOWN,
        STANDARD,
        DAYLIGHT
    }

    protected TimeZoneFormat(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static TimeZoneFormat getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static TimeZoneFormat getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneNames getTimeZoneNames() {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat setTimeZoneNames(TimeZoneNames timeZoneNames) {
        throw new RuntimeException("Stub!");
    }

    public String getGMTPattern() {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat setGMTPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getGMTOffsetPattern(GMTOffsetPatternType gMTOffsetPatternType) {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat setGMTOffsetPattern(GMTOffsetPatternType gMTOffsetPatternType, String str) {
        throw new RuntimeException("Stub!");
    }

    public String getGMTOffsetDigits() {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat setGMTOffsetDigits(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getGMTZeroFormat() {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat setGMTZeroFormat(String str) {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat setDefaultParseOptions(EnumSet<ParseOption> enumSet) {
        throw new RuntimeException("Stub!");
    }

    public EnumSet<ParseOption> getDefaultParseOptions() {
        throw new RuntimeException("Stub!");
    }

    public final String formatOffsetISO8601Basic(int i, boolean z, boolean z2, boolean z3) {
        throw new RuntimeException("Stub!");
    }

    public final String formatOffsetISO8601Extended(int i, boolean z, boolean z2, boolean z3) {
        throw new RuntimeException("Stub!");
    }

    public String formatOffsetLocalizedGMT(int i) {
        throw new RuntimeException("Stub!");
    }

    public String formatOffsetShortLocalizedGMT(int i) {
        throw new RuntimeException("Stub!");
    }

    public final String format(Style style, TimeZone timeZone, long j) {
        throw new RuntimeException("Stub!");
    }

    public String format(Style style, TimeZone timeZone, long j, Output<TimeType> output) {
        throw new RuntimeException("Stub!");
    }

    public final int parseOffsetISO8601(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public int parseOffsetLocalizedGMT(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public int parseOffsetShortLocalizedGMT(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public TimeZone parse(Style style, String str, ParsePosition parsePosition, EnumSet<ParseOption> enumSet, Output<TimeType> output) {
        throw new RuntimeException("Stub!");
    }

    public TimeZone parse(Style style, String str, ParsePosition parsePosition, Output<TimeType> output) {
        throw new RuntimeException("Stub!");
    }

    public final TimeZone parse(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public final TimeZone parse(String str) throws ParseException {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public Object parseObject(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFrozen() {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat freeze() {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneFormat cloneAsThawed() {
        throw new RuntimeException("Stub!");
    }
}
