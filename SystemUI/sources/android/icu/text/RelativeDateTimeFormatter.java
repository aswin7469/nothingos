package android.icu.text;

import android.icu.util.ULocale;
import java.text.AttributedCharacterIterator;
import java.util.Locale;

public final class RelativeDateTimeFormatter {

    public enum AbsoluteUnit {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        DAY,
        WEEK,
        MONTH,
        YEAR,
        NOW,
        QUARTER,
        HOUR,
        MINUTE
    }

    public enum Direction {
        LAST_2,
        LAST,
        THIS,
        NEXT,
        NEXT_2,
        PLAIN
    }

    public enum RelativeDateTimeUnit {
        YEAR,
        QUARTER,
        MONTH,
        WEEK,
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }

    public enum RelativeUnit {
        SECONDS,
        MINUTES,
        HOURS,
        DAYS,
        WEEKS,
        MONTHS,
        YEARS
    }

    public enum Style {
        LONG,
        SHORT,
        NARROW
    }

    private RelativeDateTimeFormatter() {
        throw new RuntimeException("Stub!");
    }

    public static RelativeDateTimeFormatter getInstance() {
        throw new RuntimeException("Stub!");
    }

    public static RelativeDateTimeFormatter getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static RelativeDateTimeFormatter getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static RelativeDateTimeFormatter getInstance(ULocale uLocale, NumberFormat numberFormat) {
        throw new RuntimeException("Stub!");
    }

    public static RelativeDateTimeFormatter getInstance(ULocale uLocale, NumberFormat numberFormat, Style style, DisplayContext displayContext) {
        throw new RuntimeException("Stub!");
    }

    public static RelativeDateTimeFormatter getInstance(Locale locale, NumberFormat numberFormat) {
        throw new RuntimeException("Stub!");
    }

    public String format(double d, Direction direction, RelativeUnit relativeUnit) {
        throw new RuntimeException("Stub!");
    }

    public FormattedRelativeDateTime formatToValue(double d, Direction direction, RelativeUnit relativeUnit) {
        throw new RuntimeException("Stub!");
    }

    public String formatNumeric(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        throw new RuntimeException("Stub!");
    }

    public FormattedRelativeDateTime formatNumericToValue(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        throw new RuntimeException("Stub!");
    }

    public String format(Direction direction, AbsoluteUnit absoluteUnit) {
        throw new RuntimeException("Stub!");
    }

    public FormattedRelativeDateTime formatToValue(Direction direction, AbsoluteUnit absoluteUnit) {
        throw new RuntimeException("Stub!");
    }

    public String format(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        throw new RuntimeException("Stub!");
    }

    public FormattedRelativeDateTime formatToValue(double d, RelativeDateTimeUnit relativeDateTimeUnit) {
        throw new RuntimeException("Stub!");
    }

    public String combineDateAndTime(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public NumberFormat getNumberFormat() {
        throw new RuntimeException("Stub!");
    }

    public DisplayContext getCapitalizationContext() {
        throw new RuntimeException("Stub!");
    }

    public Style getFormatStyle() {
        throw new RuntimeException("Stub!");
    }

    public static class FormattedRelativeDateTime implements FormattedValue {
        private FormattedRelativeDateTime() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }

        public int length() {
            throw new RuntimeException("Stub!");
        }

        public char charAt(int i) {
            throw new RuntimeException("Stub!");
        }

        public CharSequence subSequence(int i, int i2) {
            throw new RuntimeException("Stub!");
        }

        public <A extends Appendable> A appendTo(A a) {
            throw new RuntimeException("Stub!");
        }

        public boolean nextPosition(ConstrainedFieldPosition constrainedFieldPosition) {
            throw new RuntimeException("Stub!");
        }

        public AttributedCharacterIterator toCharacterIterator() {
            throw new RuntimeException("Stub!");
        }
    }
}
