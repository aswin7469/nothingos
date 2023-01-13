package java.time;

import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Period implements ChronoPeriod, Serializable {
    private static final Pattern PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)Y)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)W)?(?:([-+]?[0-9]+)D)?", 2);
    private static final List<TemporalUnit> SUPPORTED_UNITS = List.m1731of(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS);
    public static final Period ZERO = new Period(0, 0, 0);
    private static final long serialVersionUID = -3587258372562876L;
    private final int days;
    private final int months;
    private final int years;

    public static Period ofYears(int i) {
        return create(i, 0, 0);
    }

    public static Period ofMonths(int i) {
        return create(0, i, 0);
    }

    public static Period ofWeeks(int i) {
        return create(0, 0, Math.multiplyExact(i, 7));
    }

    public static Period ofDays(int i) {
        return create(0, 0, i);
    }

    /* renamed from: of */
    public static Period m926of(int i, int i2, int i3) {
        return create(i, i2, i3);
    }

    public static Period from(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return (Period) temporalAmount;
        }
        if (!(temporalAmount instanceof ChronoPeriod) || IsoChronology.INSTANCE.equals(((ChronoPeriod) temporalAmount).getChronology())) {
            Objects.requireNonNull(temporalAmount, "amount");
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            for (TemporalUnit next : temporalAmount.getUnits()) {
                long j = temporalAmount.get(next);
                if (next == ChronoUnit.YEARS) {
                    i = Math.toIntExact(j);
                } else if (next == ChronoUnit.MONTHS) {
                    i2 = Math.toIntExact(j);
                } else if (next == ChronoUnit.DAYS) {
                    i3 = Math.toIntExact(j);
                } else {
                    throw new DateTimeException("Unit must be Years, Months or Days, but was " + next);
                }
            }
            return create(i, i2, i3);
        }
        throw new DateTimeException("Period requires ISO chronology: " + temporalAmount);
    }

    public static Period parse(CharSequence charSequence) {
        Objects.requireNonNull(charSequence, "text");
        Matcher matcher = PATTERN.matcher(charSequence);
        if (matcher.matches()) {
            int i = 1;
            if (charMatch(charSequence, matcher.start(1), matcher.end(1), '-')) {
                i = -1;
            }
            int start = matcher.start(2);
            int end = matcher.end(2);
            int start2 = matcher.start(3);
            int end2 = matcher.end(3);
            int start3 = matcher.start(4);
            int end3 = matcher.end(4);
            int start4 = matcher.start(5);
            int end4 = matcher.end(5);
            if (start >= 0 || start2 >= 0 || start3 >= 0 || start4 >= 0) {
                try {
                    return create(parseNumber(charSequence, start, end, i), parseNumber(charSequence, start2, end2, i), Math.addExact(parseNumber(charSequence, start4, end4, i), Math.multiplyExact(parseNumber(charSequence, start3, end3, i), 7)));
                } catch (NumberFormatException e) {
                    throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0, e);
                }
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0);
    }

    private static boolean charMatch(CharSequence charSequence, int i, int i2, char c) {
        return i >= 0 && i2 == i + 1 && charSequence.charAt(i) == c;
    }

    private static int parseNumber(CharSequence charSequence, int i, int i2, int i3) {
        if (i < 0 || i2 < 0) {
            return 0;
        }
        try {
            return Math.multiplyExact(Integer.parseInt(charSequence, i, i2, 10), i3);
        } catch (ArithmeticException e) {
            throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0, e);
        }
    }

    public static Period between(LocalDate localDate, LocalDate localDate2) {
        return localDate.until((ChronoLocalDate) localDate2);
    }

    private static Period create(int i, int i2, int i3) {
        if ((i | i2 | i3) == 0) {
            return ZERO;
        }
        return new Period(i, i2, i3);
    }

    private Period(int i, int i2, int i3) {
        this.years = i;
        this.months = i2;
        this.days = i3;
    }

    public long get(TemporalUnit temporalUnit) {
        int days2;
        if (temporalUnit == ChronoUnit.YEARS) {
            days2 = getYears();
        } else if (temporalUnit == ChronoUnit.MONTHS) {
            days2 = getMonths();
        } else if (temporalUnit == ChronoUnit.DAYS) {
            days2 = getDays();
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
        }
        return (long) days2;
    }

    public List<TemporalUnit> getUnits() {
        return SUPPORTED_UNITS;
    }

    public IsoChronology getChronology() {
        return IsoChronology.INSTANCE;
    }

    public boolean isZero() {
        return this == ZERO;
    }

    public boolean isNegative() {
        return this.years < 0 || this.months < 0 || this.days < 0;
    }

    public int getYears() {
        return this.years;
    }

    public int getMonths() {
        return this.months;
    }

    public int getDays() {
        return this.days;
    }

    public Period withYears(int i) {
        if (i == this.years) {
            return this;
        }
        return create(i, this.months, this.days);
    }

    public Period withMonths(int i) {
        if (i == this.months) {
            return this;
        }
        return create(this.years, i, this.days);
    }

    public Period withDays(int i) {
        if (i == this.days) {
            return this;
        }
        return create(this.years, this.months, i);
    }

    public Period plus(TemporalAmount temporalAmount) {
        Period from = from(temporalAmount);
        return create(Math.addExact(this.years, from.years), Math.addExact(this.months, from.months), Math.addExact(this.days, from.days));
    }

    public Period plusYears(long j) {
        return j == 0 ? this : create(Math.toIntExact(Math.addExact((long) this.years, j)), this.months, this.days);
    }

    public Period plusMonths(long j) {
        return j == 0 ? this : create(this.years, Math.toIntExact(Math.addExact((long) this.months, j)), this.days);
    }

    public Period plusDays(long j) {
        return j == 0 ? this : create(this.years, this.months, Math.toIntExact(Math.addExact((long) this.days, j)));
    }

    public Period minus(TemporalAmount temporalAmount) {
        Period from = from(temporalAmount);
        return create(Math.subtractExact(this.years, from.years), Math.subtractExact(this.months, from.months), Math.subtractExact(this.days, from.days));
    }

    public Period minusYears(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusYears(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusYears(j2);
    }

    public Period minusMonths(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusMonths(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusMonths(j2);
    }

    public Period minusDays(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusDays(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusDays(j2);
    }

    public Period multipliedBy(int i) {
        return (this == ZERO || i == 1) ? this : create(Math.multiplyExact(this.years, i), Math.multiplyExact(this.months, i), Math.multiplyExact(this.days, i));
    }

    public Period negated() {
        return multipliedBy(-1);
    }

    public Period normalized() {
        long totalMonths = toTotalMonths();
        long j = totalMonths / 12;
        int i = (int) (totalMonths % 12);
        if (j == ((long) this.years) && i == this.months) {
            return this;
        }
        return create(Math.toIntExact(j), i, this.days);
    }

    public long toTotalMonths() {
        return (((long) this.years) * 12) + ((long) this.months);
    }

    public Temporal addTo(Temporal temporal) {
        validateChrono(temporal);
        if (this.months == 0) {
            int i = this.years;
            if (i != 0) {
                temporal = temporal.plus((long) i, ChronoUnit.YEARS);
            }
        } else {
            long totalMonths = toTotalMonths();
            if (totalMonths != 0) {
                temporal = temporal.plus(totalMonths, ChronoUnit.MONTHS);
            }
        }
        int i2 = this.days;
        return i2 != 0 ? temporal.plus((long) i2, ChronoUnit.DAYS) : temporal;
    }

    public Temporal subtractFrom(Temporal temporal) {
        validateChrono(temporal);
        if (this.months == 0) {
            int i = this.years;
            if (i != 0) {
                temporal = temporal.minus((long) i, ChronoUnit.YEARS);
            }
        } else {
            long totalMonths = toTotalMonths();
            if (totalMonths != 0) {
                temporal = temporal.minus(totalMonths, ChronoUnit.MONTHS);
            }
        }
        int i2 = this.days;
        return i2 != 0 ? temporal.minus((long) i2, ChronoUnit.DAYS) : temporal;
    }

    private void validateChrono(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null && !IsoChronology.INSTANCE.equals(chronology)) {
            throw new DateTimeException("Chronology mismatch, expected: ISO, actual: " + chronology.getId());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Period)) {
            return false;
        }
        Period period = (Period) obj;
        if (this.years == period.years && this.months == period.months && this.days == period.days) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.years + Integer.rotateLeft(this.months, 8) + Integer.rotateLeft(this.days, 16);
    }

    public String toString() {
        if (this == ZERO) {
            return "P0D";
        }
        StringBuilder sb = new StringBuilder("P");
        int i = this.years;
        if (i != 0) {
            sb.append(i);
            sb.append('Y');
        }
        int i2 = this.months;
        if (i2 != 0) {
            sb.append(i2);
            sb.append('M');
        }
        int i3 = this.days;
        if (i3 != 0) {
            sb.append(i3);
            sb.append('D');
        }
        return sb.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 14, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.years);
        dataOutput.writeInt(this.months);
        dataOutput.writeInt(this.days);
    }

    static Period readExternal(DataInput dataInput) throws IOException {
        return m926of(dataInput.readInt(), dataInput.readInt(), dataInput.readInt());
    }
}
