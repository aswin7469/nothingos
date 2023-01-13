package java.time.chrono;

import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Map;

public final class MinguoChronology extends AbstractChronology implements Serializable {
    public static final MinguoChronology INSTANCE = new MinguoChronology();
    static final int YEARS_DIFFERENCE = 1911;
    private static final long serialVersionUID = 1039765215346859963L;

    public String getCalendarType() {
        return "roc";
    }

    public String getId() {
        return "Minguo";
    }

    private MinguoChronology() {
    }

    public MinguoDate date(Era era, int i, int i2, int i3) {
        return date(prolepticYear(era, i), i2, i3);
    }

    public MinguoDate date(int i, int i2, int i3) {
        return new MinguoDate(LocalDate.m906of(i + YEARS_DIFFERENCE, i2, i3));
    }

    public MinguoDate dateYearDay(Era era, int i, int i2) {
        return dateYearDay(prolepticYear(era, i), i2);
    }

    public MinguoDate dateYearDay(int i, int i2) {
        return new MinguoDate(LocalDate.ofYearDay(i + YEARS_DIFFERENCE, i2));
    }

    public MinguoDate dateEpochDay(long j) {
        return new MinguoDate(LocalDate.ofEpochDay(j));
    }

    public MinguoDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    public MinguoDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    public MinguoDate dateNow(Clock clock) {
        return date((TemporalAccessor) LocalDate.now(clock));
    }

    public MinguoDate date(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof MinguoDate) {
            return (MinguoDate) temporalAccessor;
        }
        return new MinguoDate(LocalDate.from(temporalAccessor));
    }

    public ChronoLocalDateTime<MinguoDate> localDateTime(TemporalAccessor temporalAccessor) {
        return super.localDateTime(temporalAccessor);
    }

    public ChronoZonedDateTime<MinguoDate> zonedDateTime(TemporalAccessor temporalAccessor) {
        return super.zonedDateTime(temporalAccessor);
    }

    public ChronoZonedDateTime<MinguoDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return super.zonedDateTime(instant, zoneId);
    }

    public boolean isLeapYear(long j) {
        return IsoChronology.INSTANCE.isLeapYear(j + 1911);
    }

    public int prolepticYear(Era era, int i) {
        if (era instanceof MinguoEra) {
            return era == MinguoEra.ROC ? i : 1 - i;
        }
        throw new ClassCastException("Era must be MinguoEra");
    }

    public MinguoEra eraOf(int i) {
        return MinguoEra.m948of(i);
    }

    public List<Era> eras() {
        return List.m1739of((E[]) MinguoEra.values());
    }

    /* renamed from: java.time.chrono.MinguoChronology$1 */
    static /* synthetic */ class C28771 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoField;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                java.time.temporal.ChronoField[] r0 = java.time.temporal.ChronoField.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$temporal$ChronoField = r0
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.PROLEPTIC_MONTH     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR_OF_ERA     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.MinguoChronology.C28771.<clinit>():void");
        }
    }

    public ValueRange range(ChronoField chronoField) {
        int i = C28771.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
        if (i == 1) {
            ValueRange range = ChronoField.PROLEPTIC_MONTH.range();
            return ValueRange.m953of(range.getMinimum() - 22932, range.getMaximum() - 22932);
        } else if (i == 2) {
            ValueRange range2 = ChronoField.YEAR.range();
            return ValueRange.m954of(1, range2.getMaximum() - 1911, (-range2.getMinimum()) + 1 + 1911);
        } else if (i != 3) {
            return chronoField.range();
        } else {
            ValueRange range3 = ChronoField.YEAR.range();
            return ValueRange.m953of(range3.getMinimum() - 1911, range3.getMaximum() - 1911);
        }
    }

    public MinguoDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        return (MinguoDate) super.resolveDate(map, resolverStyle);
    }

    /* access modifiers changed from: package-private */
    public Object writeReplace() {
        return super.writeReplace();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
