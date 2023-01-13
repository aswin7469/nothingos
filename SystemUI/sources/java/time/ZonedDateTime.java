package java.time;

import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.demomode.DemoMode;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInput;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.chrono.ChronoZonedDateTime;
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
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.List;
import java.util.Objects;

public final class ZonedDateTime implements Temporal, ChronoZonedDateTime<LocalDate>, Serializable {
    private static final long serialVersionUID = -6260982410461394882L;
    private final LocalDateTime dateTime;
    private final ZoneOffset offset;
    private final ZoneId zone;

    public static ZonedDateTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static ZonedDateTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static ZonedDateTime now(Clock clock) {
        Objects.requireNonNull(clock, DemoMode.COMMAND_CLOCK);
        return ofInstant(clock.instant(), clock.getZone());
    }

    /* renamed from: of */
    public static ZonedDateTime m935of(LocalDate localDate, LocalTime localTime, ZoneId zoneId) {
        return m936of(LocalDateTime.m914of(localDate, localTime), zoneId);
    }

    /* renamed from: of */
    public static ZonedDateTime m936of(LocalDateTime localDateTime, ZoneId zoneId) {
        return ofLocal(localDateTime, zoneId, (ZoneOffset) null);
    }

    /* renamed from: of */
    public static ZonedDateTime m934of(int i, int i2, int i3, int i4, int i5, int i6, int i7, ZoneId zoneId) {
        return ofLocal(LocalDateTime.m910of(i, i2, i3, i4, i5, i6, i7), zoneId, (ZoneOffset) null);
    }

    public static ZonedDateTime ofLocal(LocalDateTime localDateTime, ZoneId zoneId, ZoneOffset zoneOffset) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset) {
            return new ZonedDateTime(localDateTime, (ZoneOffset) zoneId, zoneId);
        }
        ZoneRules rules = zoneId.getRules();
        List<ZoneOffset> validOffsets = rules.getValidOffsets(localDateTime);
        if (validOffsets.size() == 1) {
            zoneOffset = validOffsets.get(0);
        } else if (validOffsets.size() == 0) {
            ZoneOffsetTransition transition = rules.getTransition(localDateTime);
            localDateTime = localDateTime.plusSeconds(transition.getDuration().getSeconds());
            zoneOffset = transition.getOffsetAfter();
        } else if (zoneOffset == null || !validOffsets.contains(zoneOffset)) {
            zoneOffset = (ZoneOffset) Objects.requireNonNull(validOffsets.get(0), ZoneGetter.KEY_OFFSET);
        }
        return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
    }

    public static ZonedDateTime ofInstant(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        return create(instant.getEpochSecond(), instant.getNano(), zoneId);
    }

    public static ZonedDateTime ofInstant(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId.getRules().isValidOffset(localDateTime, zoneOffset)) {
            return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
        }
        return create(localDateTime.toEpochSecond(zoneOffset), localDateTime.getNano(), zoneId);
    }

    private static ZonedDateTime create(long j, int i, ZoneId zoneId) {
        ZoneOffset offset2 = zoneId.getRules().getOffset(Instant.ofEpochSecond(j, (long) i));
        return new ZonedDateTime(LocalDateTime.ofEpochSecond(j, i, offset2), offset2, zoneId);
    }

    public static ZonedDateTime ofStrict(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        Objects.requireNonNull(zoneId, "zone");
        ZoneRules rules = zoneId.getRules();
        if (rules.isValidOffset(localDateTime, zoneOffset)) {
            return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
        }
        ZoneOffsetTransition transition = rules.getTransition(localDateTime);
        if (transition == null || !transition.isGap()) {
            throw new DateTimeException("ZoneOffset '" + zoneOffset + "' is not valid for LocalDateTime '" + localDateTime + "' in zone '" + zoneId + "'");
        }
        throw new DateTimeException("LocalDateTime '" + localDateTime + "' does not exist in zone '" + zoneId + "' due to a gap in the local time-line, typically caused by daylight savings");
    }

    private static ZonedDateTime ofLenient(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        Objects.requireNonNull(zoneId, "zone");
        if (!(zoneId instanceof ZoneOffset) || zoneOffset.equals(zoneId)) {
            return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
        }
        throw new IllegalArgumentException("ZoneId must match ZoneOffset");
    }

    public static ZonedDateTime from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ZonedDateTime) {
            return (ZonedDateTime) temporalAccessor;
        }
        try {
            ZoneId from = ZoneId.from(temporalAccessor);
            if (temporalAccessor.isSupported(ChronoField.INSTANT_SECONDS)) {
                return create(temporalAccessor.getLong(ChronoField.INSTANT_SECONDS), temporalAccessor.get(ChronoField.NANO_OF_SECOND), from);
            }
            return m935of(LocalDate.from(temporalAccessor), LocalTime.from(temporalAccessor), from);
        } catch (DateTimeException e) {
            throw new DateTimeException("Unable to obtain ZonedDateTime from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName(), e);
        }
    }

    public static ZonedDateTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static ZonedDateTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (ZonedDateTime) dateTimeFormatter.parse(charSequence, new ZonedDateTime$$ExternalSyntheticLambda0());
    }

    private ZonedDateTime(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        this.dateTime = localDateTime;
        this.offset = zoneOffset;
        this.zone = zoneId;
    }

    private ZonedDateTime resolveLocal(LocalDateTime localDateTime) {
        return ofLocal(localDateTime, this.zone, this.offset);
    }

    private ZonedDateTime resolveInstant(LocalDateTime localDateTime) {
        return ofInstant(localDateTime, this.offset, this.zone);
    }

    private ZonedDateTime resolveOffset(ZoneOffset zoneOffset) {
        return (zoneOffset.equals(this.offset) || !this.zone.getRules().isValidOffset(this.dateTime, zoneOffset)) ? this : new ZonedDateTime(this.dateTime, zoneOffset, this.zone);
    }

    public boolean isSupported(TemporalField temporalField) {
        return (temporalField instanceof ChronoField) || (temporalField != null && temporalField.isSupportedBy(this));
    }

    public boolean isSupported(TemporalUnit temporalUnit) {
        return super.isSupported(temporalUnit);
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.OFFSET_SECONDS) {
            return temporalField.range();
        }
        return this.dateTime.range(temporalField);
    }

    /* renamed from: java.time.ZonedDateTime$1 */
    static /* synthetic */ class C28671 {
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
            throw new UnsupportedOperationException("Method not decompiled: java.time.ZonedDateTime.C28671.<clinit>():void");
        }
    }

    public int get(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return super.get(temporalField);
        }
        int i = C28671.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()];
        if (i == 1) {
            throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
        } else if (i != 2) {
            return this.dateTime.get(temporalField);
        } else {
            return getOffset().getTotalSeconds();
        }
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        int i = C28671.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()];
        if (i == 1) {
            return toEpochSecond();
        }
        if (i != 2) {
            return this.dateTime.getLong(temporalField);
        }
        return (long) getOffset().getTotalSeconds();
    }

    public ZoneOffset getOffset() {
        return this.offset;
    }

    public ZonedDateTime withEarlierOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(this.dateTime);
        if (transition != null && transition.isOverlap()) {
            ZoneOffset offsetBefore = transition.getOffsetBefore();
            if (!offsetBefore.equals(this.offset)) {
                return new ZonedDateTime(this.dateTime, offsetBefore, this.zone);
            }
        }
        return this;
    }

    public ZonedDateTime withLaterOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(toLocalDateTime());
        if (transition != null) {
            ZoneOffset offsetAfter = transition.getOffsetAfter();
            if (!offsetAfter.equals(this.offset)) {
                return new ZonedDateTime(this.dateTime, offsetAfter, this.zone);
            }
        }
        return this;
    }

    public ZoneId getZone() {
        return this.zone;
    }

    public ZonedDateTime withZoneSameLocal(ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : ofLocal(this.dateTime, zoneId, this.offset);
    }

    public ZonedDateTime withZoneSameInstant(ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : create(this.dateTime.toEpochSecond(this.offset), this.dateTime.getNano(), zoneId);
    }

    public ZonedDateTime withFixedOffsetZone() {
        if (this.zone.equals(this.offset)) {
            return this;
        }
        LocalDateTime localDateTime = this.dateTime;
        ZoneOffset zoneOffset = this.offset;
        return new ZonedDateTime(localDateTime, zoneOffset, zoneOffset);
    }

    public LocalDateTime toLocalDateTime() {
        return this.dateTime;
    }

    public LocalDate toLocalDate() {
        return this.dateTime.toLocalDate();
    }

    public int getYear() {
        return this.dateTime.getYear();
    }

    public int getMonthValue() {
        return this.dateTime.getMonthValue();
    }

    public Month getMonth() {
        return this.dateTime.getMonth();
    }

    public int getDayOfMonth() {
        return this.dateTime.getDayOfMonth();
    }

    public int getDayOfYear() {
        return this.dateTime.getDayOfYear();
    }

    public DayOfWeek getDayOfWeek() {
        return this.dateTime.getDayOfWeek();
    }

    public LocalTime toLocalTime() {
        return this.dateTime.toLocalTime();
    }

    public int getHour() {
        return this.dateTime.getHour();
    }

    public int getMinute() {
        return this.dateTime.getMinute();
    }

    public int getSecond() {
        return this.dateTime.getSecond();
    }

    public int getNano() {
        return this.dateTime.getNano();
    }

    public ZonedDateTime with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return resolveLocal(LocalDateTime.m914of((LocalDate) temporalAdjuster, this.dateTime.toLocalTime()));
        }
        if (temporalAdjuster instanceof LocalTime) {
            return resolveLocal(LocalDateTime.m914of(this.dateTime.toLocalDate(), (LocalTime) temporalAdjuster));
        }
        if (temporalAdjuster instanceof LocalDateTime) {
            return resolveLocal((LocalDateTime) temporalAdjuster);
        }
        if (temporalAdjuster instanceof OffsetDateTime) {
            OffsetDateTime offsetDateTime = (OffsetDateTime) temporalAdjuster;
            return ofLocal(offsetDateTime.toLocalDateTime(), this.zone, offsetDateTime.getOffset());
        } else if (temporalAdjuster instanceof Instant) {
            Instant instant = (Instant) temporalAdjuster;
            return create(instant.getEpochSecond(), instant.getNano(), this.zone);
        } else if (temporalAdjuster instanceof ZoneOffset) {
            return resolveOffset((ZoneOffset) temporalAdjuster);
        } else {
            return (ZonedDateTime) temporalAdjuster.adjustInto(this);
        }
    }

    public ZonedDateTime with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (ZonedDateTime) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        int i = C28671.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
        if (i == 1) {
            return create(j, getNano(), this.zone);
        }
        if (i != 2) {
            return resolveLocal(this.dateTime.with(temporalField, j));
        }
        return resolveOffset(ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(j)));
    }

    public ZonedDateTime withYear(int i) {
        return resolveLocal(this.dateTime.withYear(i));
    }

    public ZonedDateTime withMonth(int i) {
        return resolveLocal(this.dateTime.withMonth(i));
    }

    public ZonedDateTime withDayOfMonth(int i) {
        return resolveLocal(this.dateTime.withDayOfMonth(i));
    }

    public ZonedDateTime withDayOfYear(int i) {
        return resolveLocal(this.dateTime.withDayOfYear(i));
    }

    public ZonedDateTime withHour(int i) {
        return resolveLocal(this.dateTime.withHour(i));
    }

    public ZonedDateTime withMinute(int i) {
        return resolveLocal(this.dateTime.withMinute(i));
    }

    public ZonedDateTime withSecond(int i) {
        return resolveLocal(this.dateTime.withSecond(i));
    }

    public ZonedDateTime withNano(int i) {
        return resolveLocal(this.dateTime.withNano(i));
    }

    public ZonedDateTime truncatedTo(TemporalUnit temporalUnit) {
        return resolveLocal(this.dateTime.truncatedTo(temporalUnit));
    }

    public ZonedDateTime plus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return resolveLocal(this.dateTime.plus((TemporalAmount) (Period) temporalAmount));
        }
        Objects.requireNonNull(temporalAmount, "amountToAdd");
        return (ZonedDateTime) temporalAmount.addTo(this);
    }

    public ZonedDateTime plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (ZonedDateTime) temporalUnit.addTo(this, j);
        }
        if (temporalUnit.isDateBased()) {
            return resolveLocal(this.dateTime.plus(j, temporalUnit));
        }
        return resolveInstant(this.dateTime.plus(j, temporalUnit));
    }

    public ZonedDateTime plusYears(long j) {
        return resolveLocal(this.dateTime.plusYears(j));
    }

    public ZonedDateTime plusMonths(long j) {
        return resolveLocal(this.dateTime.plusMonths(j));
    }

    public ZonedDateTime plusWeeks(long j) {
        return resolveLocal(this.dateTime.plusWeeks(j));
    }

    public ZonedDateTime plusDays(long j) {
        return resolveLocal(this.dateTime.plusDays(j));
    }

    public ZonedDateTime plusHours(long j) {
        return resolveInstant(this.dateTime.plusHours(j));
    }

    public ZonedDateTime plusMinutes(long j) {
        return resolveInstant(this.dateTime.plusMinutes(j));
    }

    public ZonedDateTime plusSeconds(long j) {
        return resolveInstant(this.dateTime.plusSeconds(j));
    }

    public ZonedDateTime plusNanos(long j) {
        return resolveInstant(this.dateTime.plusNanos(j));
    }

    public ZonedDateTime minus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return resolveLocal(this.dateTime.minus((TemporalAmount) (Period) temporalAmount));
        }
        Objects.requireNonNull(temporalAmount, "amountToSubtract");
        return (ZonedDateTime) temporalAmount.subtractFrom(this);
    }

    public ZonedDateTime minus(long j, TemporalUnit temporalUnit) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plus(Long.MAX_VALUE, temporalUnit);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plus(j2, temporalUnit);
    }

    public ZonedDateTime minusYears(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusYears(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusYears(j2);
    }

    public ZonedDateTime minusMonths(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusMonths(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusMonths(j2);
    }

    public ZonedDateTime minusWeeks(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusWeeks(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusWeeks(j2);
    }

    public ZonedDateTime minusDays(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusDays(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusDays(j2);
    }

    public ZonedDateTime minusHours(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusHours(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusHours(j2);
    }

    public ZonedDateTime minusMinutes(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusMinutes(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusMinutes(j2);
    }

    public ZonedDateTime minusSeconds(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusSeconds(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusSeconds(j2);
    }

    public ZonedDateTime minusNanos(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusNanos(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusNanos(j2);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return toLocalDate();
        }
        return super.query(temporalQuery);
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        ZonedDateTime from = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        ZonedDateTime withZoneSameInstant = from.withZoneSameInstant(this.zone);
        if (temporalUnit.isDateBased()) {
            return this.dateTime.until(withZoneSameInstant.dateTime, temporalUnit);
        }
        return toOffsetDateTime().until(withZoneSameInstant.toOffsetDateTime(), temporalUnit);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public OffsetDateTime toOffsetDateTime() {
        return OffsetDateTime.m923of(this.dateTime, this.offset);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ZonedDateTime)) {
            return false;
        }
        ZonedDateTime zonedDateTime = (ZonedDateTime) obj;
        if (!this.dateTime.equals(zonedDateTime.dateTime) || !this.offset.equals(zonedDateTime.offset) || !this.zone.equals(zonedDateTime.zone)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Integer.rotateLeft(this.zone.hashCode(), 3) ^ (this.dateTime.hashCode() ^ this.offset.hashCode());
    }

    public String toString() {
        String str = this.dateTime.toString() + this.offset.toString();
        if (this.offset == this.zone) {
            return str;
        }
        return str + '[' + this.zone.toString() + ']';
    }

    private Object writeReplace() {
        return new Ser((byte) 6, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        this.dateTime.writeExternal(dataOutput);
        this.offset.writeExternal(dataOutput);
        this.zone.write(dataOutput);
    }

    static ZonedDateTime readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ofLenient(LocalDateTime.readExternal(objectInput), ZoneOffset.readExternal(objectInput), (ZoneId) Ser.read(objectInput));
    }
}
