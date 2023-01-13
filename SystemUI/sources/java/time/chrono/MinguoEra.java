package java.time.chrono;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Locale;

public enum MinguoEra implements Era {
    BEFORE_ROC,
    ROC;

    /* renamed from: of */
    public static MinguoEra m948of(int i) {
        if (i == 0) {
            return BEFORE_ROC;
        }
        if (i == 1) {
            return ROC;
        }
        throw new DateTimeException("Invalid era: " + i);
    }

    public int getValue() {
        return ordinal();
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendText((TemporalField) ChronoField.ERA, textStyle).toFormatter(locale).withChronology(MinguoChronology.INSTANCE).format(this == ROC ? MinguoDate.m947of(1, 1, 1) : MinguoDate.m947of(0, 1, 1));
    }
}
