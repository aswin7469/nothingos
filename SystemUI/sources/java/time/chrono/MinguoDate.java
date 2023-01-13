package java.time.chrono;

import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Objects;

public final class MinguoDate extends ChronoLocalDateImpl<MinguoDate> implements ChronoLocalDate, Serializable {
    private static final long serialVersionUID = 1300372329181994526L;
    private final transient LocalDate isoDate;

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public static MinguoDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static MinguoDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static MinguoDate now(Clock clock) {
        return new MinguoDate(LocalDate.now(clock));
    }

    /* renamed from: of */
    public static MinguoDate m947of(int i, int i2, int i3) {
        return new MinguoDate(LocalDate.m906of(i + 1911, i2, i3));
    }

    public static MinguoDate from(TemporalAccessor temporalAccessor) {
        return MinguoChronology.INSTANCE.date(temporalAccessor);
    }

    MinguoDate(LocalDate localDate) {
        Objects.requireNonNull(localDate, "isoDate");
        this.isoDate = localDate;
    }

    public MinguoChronology getChronology() {
        return MinguoChronology.INSTANCE;
    }

    public MinguoEra getEra() {
        return getProlepticYear() >= 1 ? MinguoEra.ROC : MinguoEra.BEFORE_ROC;
    }

    public int lengthOfMonth() {
        return this.isoDate.lengthOfMonth();
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (isSupported(temporalField)) {
            ChronoField chronoField = (ChronoField) temporalField;
            int i = C28781.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
            if (i == 1 || i == 2 || i == 3) {
                return this.isoDate.range(temporalField);
            }
            if (i != 4) {
                return getChronology().range(chronoField);
            }
            ValueRange range = ChronoField.YEAR.range();
            return ValueRange.m953of(1, getProlepticYear() <= 0 ? (-range.getMinimum()) + 1 + 1911 : range.getMaximum() - 1911);
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
    }

    /* renamed from: java.time.chrono.MinguoDate$1 */
    static /* synthetic */ class C28781 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoField;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                java.time.temporal.ChronoField[] r0 = java.time.temporal.ChronoField.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$temporal$ChronoField = r0
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_MONTH     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_YEAR     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR_OF_ERA     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x003e }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.PROLEPTIC_MONTH     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ERA     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.MinguoDate.C28781.<clinit>():void");
        }
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        int i = C28781.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()];
        int i2 = 1;
        if (i == 4) {
            int prolepticYear = getProlepticYear();
            if (prolepticYear < 1) {
                prolepticYear = 1 - prolepticYear;
            }
            return (long) prolepticYear;
        } else if (i == 5) {
            return getProlepticMonth();
        } else {
            if (i == 6) {
                return (long) getProlepticYear();
            }
            if (i != 7) {
                return this.isoDate.getLong(temporalField);
            }
            if (getProlepticYear() < 1) {
                i2 = 0;
            }
            return (long) i2;
        }
    }

    private long getProlepticMonth() {
        return ((((long) getProlepticYear()) * 12) + ((long) this.isoDate.getMonthValue())) - 1;
    }

    private int getProlepticYear() {
        return this.isoDate.getYear() - 1911;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0022, code lost:
        if (r1 != 7) goto L_0x0055;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.time.chrono.MinguoDate with(java.time.temporal.TemporalField r7, long r8) {
        /*
            r6 = this;
            boolean r0 = r7 instanceof java.time.temporal.ChronoField
            if (r0 == 0) goto L_0x0096
            r0 = r7
            java.time.temporal.ChronoField r0 = (java.time.temporal.ChronoField) r0
            long r1 = r6.getLong(r0)
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 != 0) goto L_0x0010
            return r6
        L_0x0010:
            int[] r1 = java.time.chrono.MinguoDate.C28781.$SwitchMap$java$time$temporal$ChronoField
            int r2 = r0.ordinal()
            r1 = r1[r2]
            r2 = 7
            r3 = 6
            r4 = 4
            if (r1 == r4) goto L_0x003a
            r5 = 5
            if (r1 == r5) goto L_0x0025
            if (r1 == r3) goto L_0x003a
            if (r1 == r2) goto L_0x003a
            goto L_0x0055
        L_0x0025:
            java.time.chrono.MinguoChronology r7 = r6.getChronology()
            java.time.temporal.ValueRange r7 = r7.range(r0)
            r7.checkValidValue(r8, r0)
            long r0 = r6.getProlepticMonth()
            long r8 = r8 - r0
            java.time.chrono.MinguoDate r6 = r6.plusMonths((long) r8)
            return r6
        L_0x003a:
            java.time.chrono.MinguoChronology r1 = r6.getChronology()
            java.time.temporal.ValueRange r1 = r1.range(r0)
            int r1 = r1.checkValidIntValue(r8, r0)
            int[] r5 = java.time.chrono.MinguoDate.C28781.$SwitchMap$java$time$temporal$ChronoField
            int r0 = r0.ordinal()
            r0 = r5[r0]
            r5 = 1
            if (r0 == r4) goto L_0x007f
            if (r0 == r3) goto L_0x0072
            if (r0 == r2) goto L_0x0060
        L_0x0055:
            java.time.LocalDate r0 = r6.isoDate
            java.time.LocalDate r7 = r0.with((java.time.temporal.TemporalField) r7, (long) r8)
            java.time.chrono.MinguoDate r6 = r6.with((java.time.LocalDate) r7)
            return r6
        L_0x0060:
            java.time.LocalDate r7 = r6.isoDate
            int r8 = r6.getProlepticYear()
            int r5 = r5 - r8
            int r5 = r5 + 1911
            java.time.LocalDate r7 = r7.withYear(r5)
            java.time.chrono.MinguoDate r6 = r6.with((java.time.LocalDate) r7)
            return r6
        L_0x0072:
            java.time.LocalDate r7 = r6.isoDate
            int r1 = r1 + 1911
            java.time.LocalDate r7 = r7.withYear(r1)
            java.time.chrono.MinguoDate r6 = r6.with((java.time.LocalDate) r7)
            return r6
        L_0x007f:
            java.time.LocalDate r7 = r6.isoDate
            int r8 = r6.getProlepticYear()
            if (r8 < r5) goto L_0x008a
            int r1 = r1 + 1911
            goto L_0x008d
        L_0x008a:
            int r5 = r5 - r1
            int r1 = r5 + 1911
        L_0x008d:
            java.time.LocalDate r7 = r7.withYear(r1)
            java.time.chrono.MinguoDate r6 = r6.with((java.time.LocalDate) r7)
            return r6
        L_0x0096:
            java.time.chrono.ChronoLocalDate r6 = super.with((java.time.temporal.TemporalField) r7, (long) r8)
            java.time.chrono.MinguoDate r6 = (java.time.chrono.MinguoDate) r6
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.MinguoDate.with(java.time.temporal.TemporalField, long):java.time.chrono.MinguoDate");
    }

    public MinguoDate with(TemporalAdjuster temporalAdjuster) {
        return (MinguoDate) super.with(temporalAdjuster);
    }

    public MinguoDate plus(TemporalAmount temporalAmount) {
        return (MinguoDate) super.plus(temporalAmount);
    }

    public MinguoDate minus(TemporalAmount temporalAmount) {
        return (MinguoDate) super.minus(temporalAmount);
    }

    /* access modifiers changed from: package-private */
    public MinguoDate plusYears(long j) {
        return with(this.isoDate.plusYears(j));
    }

    /* access modifiers changed from: package-private */
    public MinguoDate plusMonths(long j) {
        return with(this.isoDate.plusMonths(j));
    }

    /* access modifiers changed from: package-private */
    public MinguoDate plusWeeks(long j) {
        return (MinguoDate) super.plusWeeks(j);
    }

    /* access modifiers changed from: package-private */
    public MinguoDate plusDays(long j) {
        return with(this.isoDate.plusDays(j));
    }

    public MinguoDate plus(long j, TemporalUnit temporalUnit) {
        return (MinguoDate) super.plus(j, temporalUnit);
    }

    public MinguoDate minus(long j, TemporalUnit temporalUnit) {
        return (MinguoDate) super.minus(j, temporalUnit);
    }

    /* access modifiers changed from: package-private */
    public MinguoDate minusYears(long j) {
        return (MinguoDate) super.minusYears(j);
    }

    /* access modifiers changed from: package-private */
    public MinguoDate minusMonths(long j) {
        return (MinguoDate) super.minusMonths(j);
    }

    /* access modifiers changed from: package-private */
    public MinguoDate minusWeeks(long j) {
        return (MinguoDate) super.minusWeeks(j);
    }

    /* access modifiers changed from: package-private */
    public MinguoDate minusDays(long j) {
        return (MinguoDate) super.minusDays(j);
    }

    private MinguoDate with(LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new MinguoDate(localDate);
    }

    public final ChronoLocalDateTime<MinguoDate> atTime(LocalTime localTime) {
        return super.atTime(localTime);
    }

    public ChronoPeriod until(ChronoLocalDate chronoLocalDate) {
        Period until = this.isoDate.until(chronoLocalDate);
        return getChronology().period(until.getYears(), until.getMonths(), until.getDays());
    }

    public long toEpochDay() {
        return this.isoDate.toEpochDay();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MinguoDate) {
            return this.isoDate.equals(((MinguoDate) obj).isoDate);
        }
        return false;
    }

    public int hashCode() {
        return this.isoDate.hashCode() ^ getChronology().getId().hashCode();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new Ser((byte) 7, this);
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static MinguoDate readExternal(DataInput dataInput) throws IOException {
        return MinguoChronology.INSTANCE.date(dataInput.readInt(), (int) dataInput.readByte(), (int) dataInput.readByte());
    }
}
