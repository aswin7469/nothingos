package java.time.chrono;

import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectStreamException;
import java.p026io.Serializable;
import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Objects;

final class ChronoPeriodImpl implements ChronoPeriod, Serializable {
    private static final List<TemporalUnit> SUPPORTED_UNITS = List.m1731of(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS);
    private static final long serialVersionUID = 57387258289L;
    private final Chronology chrono;
    final int days;
    final int months;
    final int years;

    ChronoPeriodImpl(Chronology chronology, int i, int i2, int i3) {
        Objects.requireNonNull(chronology, "chrono");
        this.chrono = chronology;
        this.years = i;
        this.months = i2;
        this.days = i3;
    }

    public long get(TemporalUnit temporalUnit) {
        int i;
        if (temporalUnit == ChronoUnit.YEARS) {
            i = this.years;
        } else if (temporalUnit == ChronoUnit.MONTHS) {
            i = this.months;
        } else if (temporalUnit == ChronoUnit.DAYS) {
            i = this.days;
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
        }
        return (long) i;
    }

    public List<TemporalUnit> getUnits() {
        return SUPPORTED_UNITS;
    }

    public Chronology getChronology() {
        return this.chrono;
    }

    public boolean isZero() {
        return this.years == 0 && this.months == 0 && this.days == 0;
    }

    public boolean isNegative() {
        return this.years < 0 || this.months < 0 || this.days < 0;
    }

    public ChronoPeriod plus(TemporalAmount temporalAmount) {
        ChronoPeriodImpl validateAmount = validateAmount(temporalAmount);
        return new ChronoPeriodImpl(this.chrono, Math.addExact(this.years, validateAmount.years), Math.addExact(this.months, validateAmount.months), Math.addExact(this.days, validateAmount.days));
    }

    public ChronoPeriod minus(TemporalAmount temporalAmount) {
        ChronoPeriodImpl validateAmount = validateAmount(temporalAmount);
        return new ChronoPeriodImpl(this.chrono, Math.subtractExact(this.years, validateAmount.years), Math.subtractExact(this.months, validateAmount.months), Math.subtractExact(this.days, validateAmount.days));
    }

    private ChronoPeriodImpl validateAmount(TemporalAmount temporalAmount) {
        Objects.requireNonNull(temporalAmount, "amount");
        if (temporalAmount instanceof ChronoPeriodImpl) {
            ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl) temporalAmount;
            if (this.chrono.equals(chronoPeriodImpl.getChronology())) {
                return chronoPeriodImpl;
            }
            throw new ClassCastException("Chronology mismatch, expected: " + this.chrono.getId() + ", actual: " + chronoPeriodImpl.getChronology().getId());
        }
        throw new DateTimeException("Unable to obtain ChronoPeriod from TemporalAmount: " + temporalAmount.getClass());
    }

    public ChronoPeriod multipliedBy(int i) {
        return (isZero() || i == 1) ? this : new ChronoPeriodImpl(this.chrono, Math.multiplyExact(this.years, i), Math.multiplyExact(this.months, i), Math.multiplyExact(this.days, i));
    }

    public ChronoPeriod normalized() {
        long monthRange = monthRange();
        if (monthRange <= 0) {
            return this;
        }
        int i = this.years;
        int i2 = this.months;
        long j = (((long) i) * monthRange) + ((long) i2);
        long j2 = j / monthRange;
        int i3 = (int) (j % monthRange);
        if (j2 == ((long) i) && i3 == i2) {
            return this;
        }
        return new ChronoPeriodImpl(this.chrono, Math.toIntExact(j2), i3, this.days);
    }

    private long monthRange() {
        ValueRange range = this.chrono.range(ChronoField.MONTH_OF_YEAR);
        if (!range.isFixed() || !range.isIntValue()) {
            return -1;
        }
        return (range.getMaximum() - range.getMinimum()) + 1;
    }

    public Temporal addTo(Temporal temporal) {
        validateChrono(temporal);
        if (this.months == 0) {
            int i = this.years;
            if (i != 0) {
                temporal = temporal.plus((long) i, ChronoUnit.YEARS);
            }
        } else {
            long monthRange = monthRange();
            if (monthRange > 0) {
                temporal = temporal.plus((((long) this.years) * monthRange) + ((long) this.months), ChronoUnit.MONTHS);
            } else {
                int i2 = this.years;
                if (i2 != 0) {
                    temporal = temporal.plus((long) i2, ChronoUnit.YEARS);
                }
                temporal = temporal.plus((long) this.months, ChronoUnit.MONTHS);
            }
        }
        int i3 = this.days;
        return i3 != 0 ? temporal.plus((long) i3, ChronoUnit.DAYS) : temporal;
    }

    public Temporal subtractFrom(Temporal temporal) {
        validateChrono(temporal);
        if (this.months == 0) {
            int i = this.years;
            if (i != 0) {
                temporal = temporal.minus((long) i, ChronoUnit.YEARS);
            }
        } else {
            long monthRange = monthRange();
            if (monthRange > 0) {
                temporal = temporal.minus((((long) this.years) * monthRange) + ((long) this.months), ChronoUnit.MONTHS);
            } else {
                int i2 = this.years;
                if (i2 != 0) {
                    temporal = temporal.minus((long) i2, ChronoUnit.YEARS);
                }
                temporal = temporal.minus((long) this.months, ChronoUnit.MONTHS);
            }
        }
        int i3 = this.days;
        return i3 != 0 ? temporal.minus((long) i3, ChronoUnit.DAYS) : temporal;
    }

    private void validateChrono(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null && !this.chrono.equals(chronology)) {
            throw new DateTimeException("Chronology mismatch, expected: " + this.chrono.getId() + ", actual: " + chronology.getId());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChronoPeriodImpl)) {
            return false;
        }
        ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl) obj;
        if (this.years == chronoPeriodImpl.years && this.months == chronoPeriodImpl.months && this.days == chronoPeriodImpl.days && this.chrono.equals(chronoPeriodImpl.chrono)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.chrono.hashCode() ^ ((this.years + Integer.rotateLeft(this.months, 8)) + Integer.rotateLeft(this.days, 16));
    }

    public String toString() {
        if (isZero()) {
            return getChronology().toString() + " P0D";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getChronology().toString());
        sb.append(" P");
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

    /* access modifiers changed from: protected */
    public Object writeReplace() {
        return new Ser((byte) 9, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.chrono.getId());
        dataOutput.writeInt(this.years);
        dataOutput.writeInt(this.months);
        dataOutput.writeInt(this.days);
    }

    static ChronoPeriodImpl readExternal(DataInput dataInput) throws IOException {
        return new ChronoPeriodImpl(Chronology.m939of(dataInput.readUTF()), dataInput.readInt(), dataInput.readInt(), dataInput.readInt());
    }
}
