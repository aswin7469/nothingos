package java.time.chrono;

import com.android.settingslib.datetime.ZoneGetter;
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
import java.util.Comparator;
import java.util.Objects;

public interface ChronoLocalDateTime<D extends ChronoLocalDate> extends Temporal, TemporalAdjuster, Comparable<ChronoLocalDateTime<?>> {
    ChronoZonedDateTime<D> atZone(ZoneId zoneId);

    boolean equals(Object obj);

    int hashCode();

    boolean isSupported(TemporalField temporalField);

    ChronoLocalDateTime<D> plus(long j, TemporalUnit temporalUnit);

    D toLocalDate();

    LocalTime toLocalTime();

    String toString();

    ChronoLocalDateTime<D> with(TemporalField temporalField, long j);

    static Comparator<ChronoLocalDateTime<?>> timeLineOrder() {
        return new ChronoLocalDateTime$$ExternalSyntheticLambda0();
    }

    static /* synthetic */ int lambda$timeLineOrder$b9959cb5$1(ChronoLocalDateTime chronoLocalDateTime, ChronoLocalDateTime chronoLocalDateTime2) {
        int compare = Long.compare(chronoLocalDateTime.toLocalDate().toEpochDay(), chronoLocalDateTime2.toLocalDate().toEpochDay());
        return compare == 0 ? Long.compare(chronoLocalDateTime.toLocalTime().toNanoOfDay(), chronoLocalDateTime2.toLocalTime().toNanoOfDay()) : compare;
    }

    static ChronoLocalDateTime<?> from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoLocalDateTime) {
            return (ChronoLocalDateTime) temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null) {
            return chronology.localDateTime(temporalAccessor);
        }
        throw new DateTimeException("Unable to obtain ChronoLocalDateTime from TemporalAccessor: " + temporalAccessor.getClass());
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

    ChronoLocalDateTime<D> with(TemporalAdjuster temporalAdjuster) {
        return ChronoLocalDateTimeImpl.ensureValid(getChronology(), super.with(temporalAdjuster));
    }

    ChronoLocalDateTime<D> plus(TemporalAmount temporalAmount) {
        return ChronoLocalDateTimeImpl.ensureValid(getChronology(), super.plus(temporalAmount));
    }

    ChronoLocalDateTime<D> minus(TemporalAmount temporalAmount) {
        return ChronoLocalDateTimeImpl.ensureValid(getChronology(), super.minus(temporalAmount));
    }

    ChronoLocalDateTime<D> minus(long j, TemporalUnit temporalUnit) {
        return ChronoLocalDateTimeImpl.ensureValid(getChronology(), super.minus(j, temporalUnit));
    }

    <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset()) {
            return null;
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

    Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, toLocalDate().toEpochDay()).with(ChronoField.NANO_OF_DAY, toLocalTime().toNanoOfDay());
    }

    String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    Instant toInstant(ZoneOffset zoneOffset) {
        return Instant.ofEpochSecond(toEpochSecond(zoneOffset), (long) toLocalTime().getNano());
    }

    long toEpochSecond(ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        return ((toLocalDate().toEpochDay() * 86400) + ((long) toLocalTime().toSecondOfDay())) - ((long) zoneOffset.getTotalSeconds());
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [java.time.chrono.ChronoLocalDateTime<?>, java.time.chrono.ChronoLocalDateTime] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    int compareTo(java.time.chrono.ChronoLocalDateTime<?> r3) {
        /*
            r2 = this;
            java.time.chrono.ChronoLocalDate r0 = r2.toLocalDate()
            java.time.chrono.ChronoLocalDate r1 = r3.toLocalDate()
            int r0 = r0.compareTo((java.time.chrono.ChronoLocalDate) r1)
            if (r0 != 0) goto L_0x0028
            java.time.LocalTime r0 = r2.toLocalTime()
            java.time.LocalTime r1 = r3.toLocalTime()
            int r0 = r0.compareTo((java.time.LocalTime) r1)
            if (r0 != 0) goto L_0x0028
            java.time.chrono.Chronology r2 = r2.getChronology()
            java.time.chrono.Chronology r3 = r3.getChronology()
            int r0 = r2.compareTo((java.time.chrono.Chronology) r3)
        L_0x0028:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.ChronoLocalDateTime.compareTo(java.time.chrono.ChronoLocalDateTime):int");
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [java.time.chrono.ChronoLocalDateTime<?>, java.time.chrono.ChronoLocalDateTime] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean isAfter(java.time.chrono.ChronoLocalDateTime<?> r5) {
        /*
            r4 = this;
            java.time.chrono.ChronoLocalDate r0 = r4.toLocalDate()
            long r0 = r0.toEpochDay()
            java.time.chrono.ChronoLocalDate r2 = r5.toLocalDate()
            long r2 = r2.toEpochDay()
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x002d
            if (r0 != 0) goto L_0x002b
            java.time.LocalTime r4 = r4.toLocalTime()
            long r0 = r4.toNanoOfDay()
            java.time.LocalTime r4 = r5.toLocalTime()
            long r4 = r4.toNanoOfDay()
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x002b
            goto L_0x002d
        L_0x002b:
            r4 = 0
            goto L_0x002e
        L_0x002d:
            r4 = 1
        L_0x002e:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.ChronoLocalDateTime.isAfter(java.time.chrono.ChronoLocalDateTime):boolean");
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [java.time.chrono.ChronoLocalDateTime<?>, java.time.chrono.ChronoLocalDateTime] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean isBefore(java.time.chrono.ChronoLocalDateTime<?> r5) {
        /*
            r4 = this;
            java.time.chrono.ChronoLocalDate r0 = r4.toLocalDate()
            long r0 = r0.toEpochDay()
            java.time.chrono.ChronoLocalDate r2 = r5.toLocalDate()
            long r2 = r2.toEpochDay()
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x002d
            if (r0 != 0) goto L_0x002b
            java.time.LocalTime r4 = r4.toLocalTime()
            long r0 = r4.toNanoOfDay()
            java.time.LocalTime r4 = r5.toLocalTime()
            long r4 = r4.toNanoOfDay()
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 >= 0) goto L_0x002b
            goto L_0x002d
        L_0x002b:
            r4 = 0
            goto L_0x002e
        L_0x002d:
            r4 = 1
        L_0x002e:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.ChronoLocalDateTime.isBefore(java.time.chrono.ChronoLocalDateTime):boolean");
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [java.time.chrono.ChronoLocalDateTime<?>, java.time.chrono.ChronoLocalDateTime] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean isEqual(java.time.chrono.ChronoLocalDateTime<?> r5) {
        /*
            r4 = this;
            java.time.LocalTime r0 = r4.toLocalTime()
            long r0 = r0.toNanoOfDay()
            java.time.LocalTime r2 = r5.toLocalTime()
            long r2 = r2.toNanoOfDay()
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x002a
            java.time.chrono.ChronoLocalDate r4 = r4.toLocalDate()
            long r0 = r4.toEpochDay()
            java.time.chrono.ChronoLocalDate r4 = r5.toLocalDate()
            long r4 = r4.toEpochDay()
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 != 0) goto L_0x002a
            r4 = 1
            goto L_0x002b
        L_0x002a:
            r4 = 0
        L_0x002b:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.ChronoLocalDateTime.isEqual(java.time.chrono.ChronoLocalDateTime):boolean");
    }
}
