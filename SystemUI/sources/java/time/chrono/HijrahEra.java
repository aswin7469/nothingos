package java.time.chrono;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.Locale;

public enum HijrahEra implements Era {
    AH;

    public int getValue() {
        return 1;
    }

    /* renamed from: of */
    public static HijrahEra m944of(int i) {
        if (i == 1) {
            return AH;
        }
        throw new DateTimeException("Invalid era: " + i);
    }

    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return ValueRange.m955of(1, 1);
        }
        return super.range(temporalField);
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendText((TemporalField) ChronoField.ERA, textStyle).toFormatter(locale).withChronology(HijrahChronology.INSTANCE).format(HijrahDate.now());
    }
}
