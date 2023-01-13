package java.time.chrono;

import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.Era;

public final class JapaneseEra implements Era, Serializable {
    static final Era[] ERA_CONFIG;
    static final int ERA_OFFSET = 2;
    public static final JapaneseEra HEISEI;
    private static final JapaneseEra[] KNOWN_ERAS;
    public static final JapaneseEra MEIJI;
    private static final int N_ERA_CONSTANTS;
    public static final JapaneseEra REIWA;
    public static final JapaneseEra SHOWA;
    public static final JapaneseEra TAISHO;
    private static final long serialVersionUID = 1466499369062886794L;
    private final transient int eraValue;
    private final transient LocalDate since;

    private static int ordinal(int i) {
        return (i + 2) - 1;
    }

    static {
        JapaneseEra japaneseEra = new JapaneseEra(-1, LocalDate.m906of(1868, 1, 1));
        MEIJI = japaneseEra;
        JapaneseEra japaneseEra2 = new JapaneseEra(0, LocalDate.m906of(1912, 7, 30));
        TAISHO = japaneseEra2;
        JapaneseEra japaneseEra3 = new JapaneseEra(1, LocalDate.m906of(1926, 12, 25));
        SHOWA = japaneseEra3;
        JapaneseEra japaneseEra4 = new JapaneseEra(2, LocalDate.m906of(1989, 1, 8));
        HEISEI = japaneseEra4;
        JapaneseEra japaneseEra5 = new JapaneseEra(3, LocalDate.m906of(2019, 5, 1));
        REIWA = japaneseEra5;
        int value = japaneseEra5.getValue() + 2;
        N_ERA_CONSTANTS = value;
        Era[] eras = JapaneseChronology.JCAL.getEras();
        ERA_CONFIG = eras;
        JapaneseEra[] japaneseEraArr = new JapaneseEra[eras.length];
        KNOWN_ERAS = japaneseEraArr;
        japaneseEraArr[0] = japaneseEra;
        japaneseEraArr[1] = japaneseEra2;
        japaneseEraArr[2] = japaneseEra3;
        japaneseEraArr[3] = japaneseEra4;
        japaneseEraArr[4] = japaneseEra5;
        while (true) {
            Era[] eraArr = ERA_CONFIG;
            if (value < eraArr.length) {
                CalendarDate sinceDate = eraArr[value].getSinceDate();
                KNOWN_ERAS[value] = new JapaneseEra((value - 2) + 1, LocalDate.m906of(sinceDate.getYear(), sinceDate.getMonth(), sinceDate.getDayOfMonth()));
                value++;
            } else {
                return;
            }
        }
    }

    private JapaneseEra(int i, LocalDate localDate) {
        this.eraValue = i;
        this.since = localDate;
    }

    /* access modifiers changed from: package-private */
    public Era getPrivateEra() {
        return ERA_CONFIG[ordinal(this.eraValue)];
    }

    /* renamed from: of */
    public static JapaneseEra m946of(int i) {
        int ordinal = ordinal(i);
        if (ordinal >= 0) {
            JapaneseEra[] japaneseEraArr = KNOWN_ERAS;
            if (ordinal < japaneseEraArr.length) {
                return japaneseEraArr[ordinal];
            }
        }
        throw new DateTimeException("Invalid era: " + i);
    }

    public static JapaneseEra valueOf(String str) {
        Objects.requireNonNull(str, "japaneseEra");
        for (JapaneseEra japaneseEra : KNOWN_ERAS) {
            if (japaneseEra.getName().equals(str)) {
                return japaneseEra;
            }
        }
        throw new IllegalArgumentException("japaneseEra is invalid");
    }

    public static JapaneseEra[] values() {
        JapaneseEra[] japaneseEraArr = KNOWN_ERAS;
        return (JapaneseEra[]) Arrays.copyOf((T[]) japaneseEraArr, japaneseEraArr.length);
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        if (getValue() > N_ERA_CONSTANTS - 2) {
            Objects.requireNonNull(locale, "locale");
            return textStyle.asNormal() == TextStyle.NARROW ? getAbbreviation() : getName();
        }
        return new DateTimeFormatterBuilder().appendText((TemporalField) ChronoField.ERA, textStyle).toFormatter(locale).withChronology(JapaneseChronology.INSTANCE).format(this == MEIJI ? JapaneseDate.MEIJI_6_ISODATE : this.since);
    }

    static JapaneseEra from(LocalDate localDate) {
        if (!localDate.isBefore(JapaneseDate.MEIJI_6_ISODATE)) {
            for (int length = KNOWN_ERAS.length - 1; length > 0; length--) {
                JapaneseEra japaneseEra = KNOWN_ERAS[length];
                if (localDate.compareTo((ChronoLocalDate) japaneseEra.since) >= 0) {
                    return japaneseEra;
                }
            }
            return null;
        }
        throw new DateTimeException("JapaneseDate before Meiji 6 are not supported");
    }

    static JapaneseEra toJapaneseEra(Era era) {
        for (int length = ERA_CONFIG.length - 1; length >= 0; length--) {
            if (ERA_CONFIG[length].equals(era)) {
                return KNOWN_ERAS[length];
            }
        }
        return null;
    }

    static Era privateEraFrom(LocalDate localDate) {
        for (int length = KNOWN_ERAS.length - 1; length > 0; length--) {
            if (localDate.compareTo((ChronoLocalDate) KNOWN_ERAS[length].since) >= 0) {
                return ERA_CONFIG[length];
            }
        }
        return null;
    }

    public int getValue() {
        return this.eraValue;
    }

    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return JapaneseChronology.INSTANCE.range(ChronoField.ERA);
        }
        return super.range(temporalField);
    }

    /* access modifiers changed from: package-private */
    public String getAbbreviation() {
        return ERA_CONFIG[ordinal(getValue())].getAbbreviation();
    }

    /* access modifiers changed from: package-private */
    public String getName() {
        return ERA_CONFIG[ordinal(getValue())].getName();
    }

    public String toString() {
        return getName();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new Ser((byte) 5, this);
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(getValue());
    }

    static JapaneseEra readExternal(DataInput dataInput) throws IOException {
        return m946of(dataInput.readByte());
    }
}
