package java.time.chrono;

import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInput;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutput;
import java.p026io.Serializable;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
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

public final class HijrahDate extends ChronoLocalDateImpl<HijrahDate> implements ChronoLocalDate, Serializable {
    private static final long serialVersionUID = -5207853542612002020L;
    private final transient HijrahChronology chrono;
    private final transient int dayOfMonth;
    private final transient int monthOfYear;
    private final transient int prolepticYear;

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    /* renamed from: of */
    static HijrahDate m943of(HijrahChronology hijrahChronology, int i, int i2, int i3) {
        return new HijrahDate(hijrahChronology, i, i2, i3);
    }

    static HijrahDate ofEpochDay(HijrahChronology hijrahChronology, long j) {
        return new HijrahDate(hijrahChronology, j);
    }

    public static HijrahDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static HijrahDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static HijrahDate now(Clock clock) {
        return ofEpochDay(HijrahChronology.INSTANCE, LocalDate.now(clock).toEpochDay());
    }

    /* renamed from: of */
    public static HijrahDate m942of(int i, int i2, int i3) {
        return HijrahChronology.INSTANCE.date(i, i2, i3);
    }

    public static HijrahDate from(TemporalAccessor temporalAccessor) {
        return HijrahChronology.INSTANCE.date(temporalAccessor);
    }

    private HijrahDate(HijrahChronology hijrahChronology, int i, int i2, int i3) {
        hijrahChronology.getEpochDay(i, i2, i3);
        this.chrono = hijrahChronology;
        this.prolepticYear = i;
        this.monthOfYear = i2;
        this.dayOfMonth = i3;
    }

    private HijrahDate(HijrahChronology hijrahChronology, long j) {
        int[] hijrahDateInfo = hijrahChronology.getHijrahDateInfo((int) j);
        this.chrono = hijrahChronology;
        this.prolepticYear = hijrahDateInfo[0];
        this.monthOfYear = hijrahDateInfo[1];
        this.dayOfMonth = hijrahDateInfo[2];
    }

    public HijrahChronology getChronology() {
        return this.chrono;
    }

    public HijrahEra getEra() {
        return HijrahEra.AH;
    }

    public int lengthOfMonth() {
        return this.chrono.getMonthLength(this.prolepticYear, this.monthOfYear);
    }

    public int lengthOfYear() {
        return this.chrono.getYearLength(this.prolepticYear);
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (isSupported(temporalField)) {
            ChronoField chronoField = (ChronoField) temporalField;
            int i = C28681.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
            if (i == 1) {
                return ValueRange.m955of(1, (long) lengthOfMonth());
            }
            if (i == 2) {
                return ValueRange.m955of(1, (long) lengthOfYear());
            }
            if (i != 3) {
                return getChronology().range(chronoField);
            }
            return ValueRange.m955of(1, 5);
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
    }

    /* renamed from: java.time.chrono.HijrahDate$1 */
    static /* synthetic */ class C28681 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoField;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|(3:25|26|28)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0090 */
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
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_WEEK     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x003e }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.EPOCH_DAY     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0060 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x006c }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.MONTH_OF_YEAR     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0078 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.PROLEPTIC_MONTH     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0084 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR_OF_ERA     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0090 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x009c }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ERA     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.HijrahDate.C28681.<clinit>():void");
        }
    }

    public long getLong(TemporalField temporalField) {
        int i;
        int i2;
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch (C28681.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()]) {
            case 1:
                i = this.dayOfMonth;
                break;
            case 2:
                i = getDayOfYear();
                break;
            case 3:
                i2 = (this.dayOfMonth - 1) / 7;
                break;
            case 4:
                i = getDayOfWeek();
                break;
            case 5:
                i2 = (this.dayOfMonth - 1) % 7;
                break;
            case 6:
                i2 = (getDayOfYear() - 1) % 7;
                break;
            case 7:
                return toEpochDay();
            case 8:
                i2 = (getDayOfYear() - 1) / 7;
                break;
            case 9:
                i = this.monthOfYear;
                break;
            case 10:
                return getProlepticMonth();
            case 11:
                i = this.prolepticYear;
                break;
            case 12:
                i = this.prolepticYear;
                break;
            case 13:
                i = getEraValue();
                break;
            default:
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        i = i2 + 1;
        return (long) i;
    }

    private long getProlepticMonth() {
        return ((((long) this.prolepticYear) * 12) + ((long) this.monthOfYear)) - 1;
    }

    public HijrahDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (HijrahDate) super.with(temporalField, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        this.chrono.range(chronoField).checkValidValue(j, chronoField);
        int i = (int) j;
        switch (C28681.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()]) {
            case 1:
                return resolvePreviousValid(this.prolepticYear, this.monthOfYear, i);
            case 2:
                return plusDays((long) (Math.min(i, lengthOfYear()) - getDayOfYear()));
            case 3:
                return plusDays((j - getLong(ChronoField.ALIGNED_WEEK_OF_MONTH)) * 7);
            case 4:
                return plusDays(j - ((long) getDayOfWeek()));
            case 5:
                return plusDays(j - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
            case 6:
                return plusDays(j - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
            case 7:
                return new HijrahDate(this.chrono, j);
            case 8:
                return plusDays((j - getLong(ChronoField.ALIGNED_WEEK_OF_YEAR)) * 7);
            case 9:
                return resolvePreviousValid(this.prolepticYear, i, this.dayOfMonth);
            case 10:
                return plusMonths(j - getProlepticMonth());
            case 11:
                if (this.prolepticYear < 1) {
                    i = 1 - i;
                }
                return resolvePreviousValid(i, this.monthOfYear, this.dayOfMonth);
            case 12:
                return resolvePreviousValid(i, this.monthOfYear, this.dayOfMonth);
            case 13:
                return resolvePreviousValid(1 - this.prolepticYear, this.monthOfYear, this.dayOfMonth);
            default:
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
    }

    private HijrahDate resolvePreviousValid(int i, int i2, int i3) {
        int monthLength = this.chrono.getMonthLength(i, i2);
        if (i3 > monthLength) {
            i3 = monthLength;
        }
        return m943of(this.chrono, i, i2, i3);
    }

    public HijrahDate with(TemporalAdjuster temporalAdjuster) {
        return (HijrahDate) super.with(temporalAdjuster);
    }

    public HijrahDate withVariant(HijrahChronology hijrahChronology) {
        if (this.chrono == hijrahChronology) {
            return this;
        }
        int dayOfYear = hijrahChronology.getDayOfYear(this.prolepticYear, this.monthOfYear);
        int i = this.prolepticYear;
        int i2 = this.monthOfYear;
        int i3 = this.dayOfMonth;
        if (i3 <= dayOfYear) {
            dayOfYear = i3;
        }
        return m943of(hijrahChronology, i, i2, dayOfYear);
    }

    public HijrahDate plus(TemporalAmount temporalAmount) {
        return (HijrahDate) super.plus(temporalAmount);
    }

    public HijrahDate minus(TemporalAmount temporalAmount) {
        return (HijrahDate) super.minus(temporalAmount);
    }

    public long toEpochDay() {
        return this.chrono.getEpochDay(this.prolepticYear, this.monthOfYear, this.dayOfMonth);
    }

    private int getDayOfYear() {
        return this.chrono.getDayOfYear(this.prolepticYear, this.monthOfYear) + this.dayOfMonth;
    }

    private int getDayOfWeek() {
        return Math.floorMod(toEpochDay() + 3, 7) + 1;
    }

    private int getEraValue() {
        return this.prolepticYear > 1 ? 1 : 0;
    }

    public boolean isLeapYear() {
        return this.chrono.isLeapYear((long) this.prolepticYear);
    }

    /* access modifiers changed from: package-private */
    public HijrahDate plusYears(long j) {
        if (j == 0) {
            return this;
        }
        return resolvePreviousValid(Math.addExact(this.prolepticYear, (int) j), this.monthOfYear, this.dayOfMonth);
    }

    /* access modifiers changed from: package-private */
    public HijrahDate plusMonths(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = (((long) this.prolepticYear) * 12) + ((long) (this.monthOfYear - 1)) + j;
        return resolvePreviousValid(this.chrono.checkValidYear(Math.floorDiv(j2, 12)), ((int) Math.floorMod(j2, 12)) + 1, this.dayOfMonth);
    }

    /* access modifiers changed from: package-private */
    public HijrahDate plusWeeks(long j) {
        return (HijrahDate) super.plusWeeks(j);
    }

    /* access modifiers changed from: package-private */
    public HijrahDate plusDays(long j) {
        return new HijrahDate(this.chrono, toEpochDay() + j);
    }

    public HijrahDate plus(long j, TemporalUnit temporalUnit) {
        return (HijrahDate) super.plus(j, temporalUnit);
    }

    public HijrahDate minus(long j, TemporalUnit temporalUnit) {
        return (HijrahDate) super.minus(j, temporalUnit);
    }

    /* access modifiers changed from: package-private */
    public HijrahDate minusYears(long j) {
        return (HijrahDate) super.minusYears(j);
    }

    /* access modifiers changed from: package-private */
    public HijrahDate minusMonths(long j) {
        return (HijrahDate) super.minusMonths(j);
    }

    /* access modifiers changed from: package-private */
    public HijrahDate minusWeeks(long j) {
        return (HijrahDate) super.minusWeeks(j);
    }

    /* access modifiers changed from: package-private */
    public HijrahDate minusDays(long j) {
        return (HijrahDate) super.minusDays(j);
    }

    public final ChronoLocalDateTime<HijrahDate> atTime(LocalTime localTime) {
        return super.atTime(localTime);
    }

    public ChronoPeriod until(ChronoLocalDate chronoLocalDate) {
        HijrahDate date = getChronology().date((TemporalAccessor) chronoLocalDate);
        long j = (long) (((date.prolepticYear - this.prolepticYear) * 12) + (date.monthOfYear - this.monthOfYear));
        int i = date.dayOfMonth - this.dayOfMonth;
        int i2 = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i2 > 0 && i < 0) {
            j--;
            i = (int) (date.toEpochDay() - plusMonths(j).toEpochDay());
        } else if (i2 < 0 && i > 0) {
            j++;
            i -= date.lengthOfMonth();
        }
        return getChronology().period(Math.toIntExact(j / 12), (int) (j % 12), i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HijrahDate)) {
            return false;
        }
        HijrahDate hijrahDate = (HijrahDate) obj;
        if (this.prolepticYear == hijrahDate.prolepticYear && this.monthOfYear == hijrahDate.monthOfYear && this.dayOfMonth == hijrahDate.dayOfMonth && getChronology().equals(hijrahDate.getChronology())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = this.prolepticYear;
        int i2 = this.monthOfYear;
        return (getChronology().getId().hashCode() ^ (i & -2048)) ^ (((i << 11) + (i2 << 6)) + this.dayOfMonth);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new Ser((byte) 6, this);
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(getChronology());
        objectOutput.writeInt(get(ChronoField.YEAR));
        objectOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        objectOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static HijrahDate readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ((HijrahChronology) objectInput.readObject()).date(objectInput.readInt(), (int) objectInput.readByte(), (int) objectInput.readByte());
    }
}
