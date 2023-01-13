package java.time.chrono;

import com.android.settingslib.utils.StringUtil;
import com.android.systemui.demomode.DemoMode;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public interface Chronology extends Comparable<Chronology> {
    int compareTo(Chronology chronology);

    ChronoLocalDate date(int i, int i2, int i3);

    ChronoLocalDate date(TemporalAccessor temporalAccessor);

    ChronoLocalDate dateEpochDay(long j);

    ChronoLocalDate dateYearDay(int i, int i2);

    boolean equals(Object obj);

    Era eraOf(int i);

    List<Era> eras();

    String getCalendarType();

    String getId();

    int hashCode();

    boolean isLeapYear(long j);

    int prolepticYear(Era era, int i);

    ValueRange range(ChronoField chronoField);

    ChronoLocalDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle);

    String toString();

    static Chronology from(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        return (Chronology) Objects.requireNonNullElse((Chronology) temporalAccessor.query(TemporalQueries.chronology()), IsoChronology.INSTANCE);
    }

    static Chronology ofLocale(Locale locale) {
        return AbstractChronology.ofLocale(locale);
    }

    /* renamed from: of */
    static Chronology m939of(String str) {
        return AbstractChronology.m937of(str);
    }

    static Set<Chronology> getAvailableChronologies() {
        return AbstractChronology.getAvailableChronologies();
    }

    ChronoLocalDate date(Era era, int i, int i2, int i3) {
        return date(prolepticYear(era, i), i2, i3);
    }

    ChronoLocalDate dateYearDay(Era era, int i, int i2) {
        return dateYearDay(prolepticYear(era, i), i2);
    }

    ChronoLocalDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    ChronoLocalDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    ChronoLocalDate dateNow(Clock clock) {
        Objects.requireNonNull(clock, DemoMode.COMMAND_CLOCK);
        return date(LocalDate.now(clock));
    }

    ChronoLocalDateTime<? extends ChronoLocalDate> localDateTime(TemporalAccessor temporalAccessor) {
        try {
            return date(temporalAccessor).atTime(LocalTime.from(temporalAccessor));
        } catch (DateTimeException e) {
            throw new DateTimeException("Unable to obtain ChronoLocalDateTime from TemporalAccessor: " + temporalAccessor.getClass(), e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001a, code lost:
        return java.time.chrono.ChronoZonedDateTimeImpl.ofBest(java.time.chrono.ChronoLocalDateTimeImpl.ensureValid(r3, localDateTime(r4)), r0, (java.time.ZoneOffset) null);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x000d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    java.time.chrono.ChronoZonedDateTime<? extends java.time.chrono.ChronoLocalDate> zonedDateTime(java.time.temporal.TemporalAccessor r4) {
        /*
            r3 = this;
            java.time.ZoneId r0 = java.time.ZoneId.from(r4)     // Catch:{ DateTimeException -> 0x001b }
            java.time.Instant r1 = java.time.Instant.from(r4)     // Catch:{ DateTimeException -> 0x000d }
            java.time.chrono.ChronoZonedDateTime r3 = r3.zonedDateTime(r1, r0)     // Catch:{ DateTimeException -> 0x000d }
            return r3
        L_0x000d:
            java.time.chrono.ChronoLocalDateTime r1 = r3.localDateTime(r4)     // Catch:{ DateTimeException -> 0x001b }
            java.time.chrono.ChronoLocalDateTimeImpl r3 = java.time.chrono.ChronoLocalDateTimeImpl.ensureValid(r3, r1)     // Catch:{ DateTimeException -> 0x001b }
            r1 = 0
            java.time.chrono.ChronoZonedDateTime r3 = java.time.chrono.ChronoZonedDateTimeImpl.ofBest(r3, r0, r1)     // Catch:{ DateTimeException -> 0x001b }
            return r3
        L_0x001b:
            r3 = move-exception
            java.time.DateTimeException r0 = new java.time.DateTimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Unable to obtain ChronoZonedDateTime from TemporalAccessor: "
            r1.<init>((java.lang.String) r2)
            java.lang.Class r4 = r4.getClass()
            r1.append((java.lang.Object) r4)
            java.lang.String r4 = r1.toString()
            r0.<init>(r4, r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.Chronology.zonedDateTime(java.time.temporal.TemporalAccessor):java.time.chrono.ChronoZonedDateTime");
    }

    ChronoZonedDateTime<? extends ChronoLocalDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return ChronoZonedDateTimeImpl.ofInstant(this, instant, zoneId);
    }

    String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendChronologyText(textStyle).toFormatter(locale).format(new TemporalAccessor() {
            public boolean isSupported(TemporalField temporalField) {
                return false;
            }

            public long getLong(TemporalField temporalField) {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }

            public <R> R query(TemporalQuery<R> temporalQuery) {
                if (temporalQuery == TemporalQueries.chronology()) {
                    return Chronology.this;
                }
                return super.query(temporalQuery);
            }
        });
    }

    ChronoPeriod period(int i, int i2, int i3) {
        return new ChronoPeriodImpl(this, i, i2, i3);
    }

    long epochSecond(int i, int i2, int i3, int i4, int i5, int i6, ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "zoneOffset");
        ChronoField.HOUR_OF_DAY.checkValidValue((long) i4);
        ChronoField.MINUTE_OF_HOUR.checkValidValue((long) i5);
        ChronoField.SECOND_OF_MINUTE.checkValidValue((long) i6);
        return Math.addExact(Math.multiplyExact(date(i, i2, i3).toEpochDay(), (int) StringUtil.SECONDS_PER_DAY), ((long) ((((i4 * 60) + i5) * 60) + i6)) - ((long) zoneOffset.getTotalSeconds()));
    }

    long epochSecond(Era era, int i, int i2, int i3, int i4, int i5, int i6, ZoneOffset zoneOffset) {
        Era era2 = era;
        Objects.requireNonNull(era, "era");
        return epochSecond(prolepticYear(era, i), i2, i3, i4, i5, i6, zoneOffset);
    }
}
