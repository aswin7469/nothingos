package java.time.chrono;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Comparator;
import java.util.Objects;

public interface ChronoLocalDate extends Temporal, TemporalAdjuster, Comparable<ChronoLocalDate> {
    boolean equals(Object obj);

    Chronology getChronology();

    int hashCode();

    int lengthOfMonth();

    String toString();

    long until(Temporal temporal, TemporalUnit temporalUnit);

    ChronoPeriod until(ChronoLocalDate chronoLocalDate);

    static Comparator<ChronoLocalDate> timeLineOrder() {
        return new ChronoLocalDate$$ExternalSyntheticLambda0();
    }

    static ChronoLocalDate from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoLocalDate) {
            return (ChronoLocalDate) temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null) {
            return chronology.date(temporalAccessor);
        }
        throw new DateTimeException("Unable to obtain ChronoLocalDate from TemporalAccessor: " + temporalAccessor.getClass());
    }

    Era getEra() {
        return getChronology().eraOf(get(ChronoField.ERA));
    }

    boolean isLeapYear() {
        return getChronology().isLeapYear(getLong(ChronoField.YEAR));
    }

    int lengthOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    boolean isSupported(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField.isDateBased();
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }

    boolean isSupported(TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isDateBased();
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }

    ChronoLocalDate with(TemporalAdjuster temporalAdjuster) {
        return ChronoLocalDateImpl.ensureValid(getChronology(), super.with(temporalAdjuster));
    }

    ChronoLocalDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return ChronoLocalDateImpl.ensureValid(getChronology(), temporalField.adjustInto(this, j));
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
    }

    ChronoLocalDate plus(TemporalAmount temporalAmount) {
        return ChronoLocalDateImpl.ensureValid(getChronology(), super.plus(temporalAmount));
    }

    ChronoLocalDate plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return ChronoLocalDateImpl.ensureValid(getChronology(), temporalUnit.addTo(this, j));
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
    }

    ChronoLocalDate minus(TemporalAmount temporalAmount) {
        return ChronoLocalDateImpl.ensureValid(getChronology(), super.minus(temporalAmount));
    }

    ChronoLocalDate minus(long j, TemporalUnit temporalUnit) {
        return ChronoLocalDateImpl.ensureValid(getChronology(), super.minus(j, temporalUnit));
    }

    <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.localTime()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.DAYS;
        }
        return temporalQuery.queryFrom(this);
    }

    Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, toEpochDay());
    }

    String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    ChronoLocalDateTime<?> atTime(LocalTime localTime) {
        return ChronoLocalDateTimeImpl.m940of(this, localTime);
    }

    long toEpochDay() {
        return getLong(ChronoField.EPOCH_DAY);
    }

    int compareTo(ChronoLocalDate chronoLocalDate) {
        int compare = Long.compare(toEpochDay(), chronoLocalDate.toEpochDay());
        return compare == 0 ? getChronology().compareTo(chronoLocalDate.getChronology()) : compare;
    }

    boolean isAfter(ChronoLocalDate chronoLocalDate) {
        return toEpochDay() > chronoLocalDate.toEpochDay();
    }

    boolean isBefore(ChronoLocalDate chronoLocalDate) {
        return toEpochDay() < chronoLocalDate.toEpochDay();
    }

    boolean isEqual(ChronoLocalDate chronoLocalDate) {
        return toEpochDay() == chronoLocalDate.toEpochDay();
    }
}
