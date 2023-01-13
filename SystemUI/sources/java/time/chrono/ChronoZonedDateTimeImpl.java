package java.time.chrono;

import com.android.settingslib.datetime.ZoneGetter;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInput;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutput;
import java.p026io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.List;
import java.util.Objects;

final class ChronoZonedDateTimeImpl<D extends ChronoLocalDate> implements ChronoZonedDateTime<D>, Serializable {
    private static final long serialVersionUID = -5261813987200935591L;
    private final transient ChronoLocalDateTimeImpl<D> dateTime;
    private final transient ZoneOffset offset;
    private final transient ZoneId zone;

    static <R extends ChronoLocalDate> ChronoZonedDateTime<R> ofBest(ChronoLocalDateTimeImpl<R> chronoLocalDateTimeImpl, ZoneId zoneId, ZoneOffset zoneOffset) {
        Objects.requireNonNull(chronoLocalDateTimeImpl, "localDateTime");
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset) {
            return new ChronoZonedDateTimeImpl(chronoLocalDateTimeImpl, (ZoneOffset) zoneId, zoneId);
        }
        ZoneRules rules = zoneId.getRules();
        LocalDateTime from = LocalDateTime.from(chronoLocalDateTimeImpl);
        List<ZoneOffset> validOffsets = rules.getValidOffsets(from);
        if (validOffsets.size() == 1) {
            zoneOffset = validOffsets.get(0);
        } else if (validOffsets.size() == 0) {
            ZoneOffsetTransition transition = rules.getTransition(from);
            chronoLocalDateTimeImpl = chronoLocalDateTimeImpl.plusSeconds(transition.getDuration().getSeconds());
            zoneOffset = transition.getOffsetAfter();
        } else if (zoneOffset == null || !validOffsets.contains(zoneOffset)) {
            zoneOffset = validOffsets.get(0);
        }
        Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        return new ChronoZonedDateTimeImpl(chronoLocalDateTimeImpl, zoneOffset, zoneId);
    }

    static ChronoZonedDateTimeImpl<?> ofInstant(Chronology chronology, Instant instant, ZoneId zoneId) {
        ZoneOffset offset2 = zoneId.getRules().getOffset(instant);
        Objects.requireNonNull(offset2, ZoneGetter.KEY_OFFSET);
        return new ChronoZonedDateTimeImpl<>((ChronoLocalDateTimeImpl) chronology.localDateTime(LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), offset2)), offset2, zoneId);
    }

    private ChronoZonedDateTimeImpl<D> create(Instant instant, ZoneId zoneId) {
        return ofInstant(getChronology(), instant, zoneId);
    }

    static <R extends ChronoLocalDate> ChronoZonedDateTimeImpl<R> ensureValid(Chronology chronology, Temporal temporal) {
        ChronoZonedDateTimeImpl<R> chronoZonedDateTimeImpl = (ChronoZonedDateTimeImpl) temporal;
        if (chronology.equals(chronoZonedDateTimeImpl.getChronology())) {
            return chronoZonedDateTimeImpl;
        }
        throw new ClassCastException("Chronology mismatch, required: " + chronology.getId() + ", actual: " + chronoZonedDateTimeImpl.getChronology().getId());
    }

    private ChronoZonedDateTimeImpl(ChronoLocalDateTimeImpl<D> chronoLocalDateTimeImpl, ZoneOffset zoneOffset, ZoneId zoneId) {
        this.dateTime = (ChronoLocalDateTimeImpl) Objects.requireNonNull(chronoLocalDateTimeImpl, "dateTime");
        this.offset = (ZoneOffset) Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        this.zone = (ZoneId) Objects.requireNonNull(zoneId, "zone");
    }

    public ZoneOffset getOffset() {
        return this.offset;
    }

    public ChronoZonedDateTime<D> withEarlierOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(LocalDateTime.from(this));
        if (transition != null && transition.isOverlap()) {
            ZoneOffset offsetBefore = transition.getOffsetBefore();
            if (!offsetBefore.equals(this.offset)) {
                return new ChronoZonedDateTimeImpl(this.dateTime, offsetBefore, this.zone);
            }
        }
        return this;
    }

    public ChronoZonedDateTime<D> withLaterOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(LocalDateTime.from(this));
        if (transition != null) {
            ZoneOffset offsetAfter = transition.getOffsetAfter();
            if (!offsetAfter.equals(getOffset())) {
                return new ChronoZonedDateTimeImpl(this.dateTime, offsetAfter, this.zone);
            }
        }
        return this;
    }

    public ChronoLocalDateTime<D> toLocalDateTime() {
        return this.dateTime;
    }

    public ZoneId getZone() {
        return this.zone;
    }

    public ChronoZonedDateTime<D> withZoneSameLocal(ZoneId zoneId) {
        return ofBest(this.dateTime, zoneId, this.offset);
    }

    public ChronoZonedDateTime<D> withZoneSameInstant(ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : create(this.dateTime.toInstant(this.offset), zoneId);
    }

    public boolean isSupported(TemporalField temporalField) {
        return (temporalField instanceof ChronoField) || (temporalField != null && temporalField.isSupportedBy(this));
    }

    public ChronoZonedDateTime<D> with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return ensureValid(getChronology(), temporalField.adjustInto(this, j));
        }
        ChronoField chronoField = (ChronoField) temporalField;
        int i = C28711.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
        if (i == 1) {
            return plus(j - toEpochSecond(), (TemporalUnit) ChronoUnit.SECONDS);
        }
        if (i != 2) {
            return ofBest(this.dateTime.with(temporalField, j), this.zone, this.offset);
        }
        return create(this.dateTime.toInstant(ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(j))), this.zone);
    }

    /* renamed from: java.time.chrono.ChronoZonedDateTimeImpl$1 */
    static /* synthetic */ class C28711 {
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
            throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.ChronoZonedDateTimeImpl.C28711.<clinit>():void");
        }
    }

    public ChronoZonedDateTime<D> plus(long j, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return with((TemporalAdjuster) this.dateTime.plus(j, temporalUnit));
        }
        return ensureValid(getChronology(), temporalUnit.addTo(this, j));
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporal, "endExclusive");
        ChronoZonedDateTime<? extends ChronoLocalDate> zonedDateTime = getChronology().zonedDateTime(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            return this.dateTime.until(zonedDateTime.withZoneSameInstant(this.offset).toLocalDateTime(), temporalUnit);
        }
        Objects.requireNonNull(temporalUnit, "unit");
        return temporalUnit.between(this, zonedDateTime);
    }

    private Object writeReplace() {
        return new Ser((byte) 3, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.dateTime);
        objectOutput.writeObject(this.offset);
        objectOutput.writeObject(this.zone);
    }

    static ChronoZonedDateTime<?> readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ((ChronoLocalDateTime) objectInput.readObject()).atZone((ZoneOffset) objectInput.readObject()).withZoneSameLocal((ZoneId) objectInput.readObject());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChronoZonedDateTime)) {
            return false;
        }
        if (compareTo((ChronoZonedDateTime<?>) (ChronoZonedDateTime) obj) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Integer.rotateLeft(getZone().hashCode(), 3) ^ (toLocalDateTime().hashCode() ^ getOffset().hashCode());
    }

    public String toString() {
        String str = toLocalDateTime().toString() + getOffset().toString();
        if (getOffset() == getZone()) {
            return str;
        }
        return str + '[' + getZone().toString() + ']';
    }
}
