package java.time.chrono;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Locale;

public enum ThaiBuddhistEra implements Era {
    BEFORE_BE,
    BE;

    /* renamed from: of */
    public static ThaiBuddhistEra m952of(int i) {
        if (i == 0) {
            return BEFORE_BE;
        }
        if (i == 1) {
            return BE;
        }
        throw new DateTimeException("Invalid era: " + i);
    }

    public int getValue() {
        return ordinal();
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendText((TemporalField) ChronoField.ERA, textStyle).toFormatter(locale).withChronology(ThaiBuddhistChronology.INSTANCE).format(this == BE ? ThaiBuddhistDate.m951of(1, 1, 1) : ThaiBuddhistDate.m951of(0, 1, 1));
    }
}
