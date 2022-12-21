package java.time.chrono;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
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
import java.time.temporal.ValueRange;
import java.util.Comparator;
import java.util.Objects;

public interface ChronoZonedDateTime<D extends ChronoLocalDate> extends Temporal, Comparable<ChronoZonedDateTime<?>> {
    boolean equals(Object obj);

    ZoneOffset getOffset();

    ZoneId getZone();

    int hashCode();

    boolean isSupported(TemporalField temporalField);

    ChronoZonedDateTime<D> plus(long j, TemporalUnit temporalUnit);

    ChronoLocalDateTime<D> toLocalDateTime();

    String toString();

    ChronoZonedDateTime<D> with(TemporalField temporalField, long j);

    ChronoZonedDateTime<D> withEarlierOffsetAtOverlap();

    ChronoZonedDateTime<D> withLaterOffsetAtOverlap();

    ChronoZonedDateTime<D> withZoneSameInstant(ZoneId zoneId);

    ChronoZonedDateTime<D> withZoneSameLocal(ZoneId zoneId);

    static Comparator<ChronoZonedDateTime<?>> timeLineOrder() {
        return new ChronoZonedDateTime$$ExternalSyntheticLambda0();
    }

    static /* synthetic */ int lambda$timeLineOrder$f56e6d02$1(ChronoZonedDateTime chronoZonedDateTime, ChronoZonedDateTime chronoZonedDateTime2) {
        int compare = Long.compare(chronoZonedDateTime.toEpochSecond(), chronoZonedDateTime2.toEpochSecond());
        return compare == 0 ? Long.compare((long) chronoZonedDateTime.toLocalTime().getNano(), (long) chronoZonedDateTime2.toLocalTime().getNano()) : compare;
    }

    static ChronoZonedDateTime<?> from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoZonedDateTime) {
            return (ChronoZonedDateTime) temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null) {
            return chronology.zonedDateTime(temporalAccessor);
        }
        throw new DateTimeException("Unable to obtain ChronoZonedDateTime from TemporalAccessor: " + temporalAccessor.getClass());
    }

    ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.OFFSET_SECONDS) {
            return temporalField.range();
        }
        return toLocalDateTime().range(temporalField);
    }

    /* renamed from: java.time.chrono.ChronoZonedDateTime$1 */
    static /* synthetic */ class C28641 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoField;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                java.time.temporal.ChronoField[] r0 = java.time.temporal.ChronoField.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$temporal$ChronoField = r0
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.INSTANT_SECONDS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.OFFSET_SECONDS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.ChronoZonedDateTime.C28641.<clinit>():void");
        }
    }

    int get(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return super.get(temporalField);
        }
        int i = C28641.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()];
        if (i == 1) {
            throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
        } else if (i != 2) {
            return toLocalDateTime().get(temporalField);
        } else {
            return getOffset().getTotalSeconds();
        }
    }

    long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        int i = C28641.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()];
        if (i == 1) {
            return toEpochSecond();
        }
        if (i != 2) {
            return toLocalDateTime().getLong(temporalField);
        }
        return (long) getOffset().getTotalSeconds();
    }

    D toLocalDate() {
        return toLocalDateTime().toLocalDate();
    }

    LocalTime toLocalTime() {
        return toLocalDateTime().toLocalTime();
    }

    Chronology getChronology() {
        return toLocalDate().getChronology();
    }

    boolean isSupported(TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            if (temporalUnit != ChronoUnit.FOREVER) {
                return true;
            }
            return false;
        } else if (temporalUnit == null || !temporalUnit.isSupportedBy(this)) {
            return false;
        } else {
            return true;
        }
    }

    ChronoZonedDateTime<D> with(TemporalAdjuster temporalAdjuster) {
        return ChronoZonedDateTimeImpl.ensureValid(getChronology(), super.with(temporalAdjuster));
    }

    ChronoZonedDateTime<D> plus(TemporalAmount temporalAmount) {
        return ChronoZonedDateTimeImpl.ensureValid(getChronology(), super.plus(temporalAmount));
    }

    ChronoZonedDateTime<D> minus(TemporalAmount temporalAmount) {
        return ChronoZonedDateTimeImpl.ensureValid(getChronology(), super.minus(temporalAmount));
    }

    ChronoZonedDateTime<D> minus(long j, TemporalUnit temporalUnit) {
        return ChronoZonedDateTimeImpl.ensureValid(getChronology(), super.minus(j, temporalUnit));
    }

    <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.zoneId()) {
            return getZone();
        }
        if (temporalQuery == TemporalQueries.offset()) {
            return getOffset();
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return toLocalTime();
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.NANOS;
        }
        return temporalQuery.queryFrom(this);
    }

    String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    Instant toInstant() {
        return Instant.ofEpochSecond(toEpochSecond(), (long) toLocalTime().getNano());
    }

    long toEpochSecond() {
        return ((toLocalDate().toEpochDay() * 86400) + ((long) toLocalTime().toSecondOfDay())) - ((long) getOffset().getTotalSeconds());
    }

    int compareTo(ChronoZonedDateTime<?> chronoZonedDateTime) {
        int compare = Long.compare(toEpochSecond(), chronoZonedDateTime.toEpochSecond());
        if (compare != 0) {
            return compare;
        }
        int nano = toLocalTime().getNano() - chronoZonedDateTime.toLocalTime().getNano();
        if (nano != 0) {
            return nano;
        }
        int compareTo = toLocalDateTime().compareTo(chronoZonedDateTime.toLocalDateTime());
        if (compareTo != 0) {
            return compareTo;
        }
        int compareTo2 = getZone().getId().compareTo(chronoZonedDateTime.getZone().getId());
        return compareTo2 == 0 ? getChronology().compareTo(chronoZonedDateTime.getChronology()) : compareTo2;
    }

    boolean isBefore(ChronoZonedDateTime<?> chronoZonedDateTime) {
        int i = (toEpochSecond() > chronoZonedDateTime.toEpochSecond() ? 1 : (toEpochSecond() == chronoZonedDateTime.toEpochSecond() ? 0 : -1));
        return i < 0 || (i == 0 && toLocalTime().getNano() < chronoZonedDateTime.toLocalTime().getNano());
    }

    boolean isAfter(ChronoZonedDateTime<?> chronoZonedDateTime) {
        int i = (toEpochSecond() > chronoZonedDateTime.toEpochSecond() ? 1 : (toEpochSecond() == chronoZonedDateTime.toEpochSecond() ? 0 : -1));
        return i > 0 || (i == 0 && toLocalTime().getNano() > chronoZonedDateTime.toLocalTime().getNano());
    }

    boolean isEqual(ChronoZonedDateTime<?> chronoZonedDateTime) {
        return toEpochSecond() == chronoZonedDateTime.toEpochSecond() && toLocalTime().getNano() == chronoZonedDateTime.toLocalTime().getNano();
    }
}
