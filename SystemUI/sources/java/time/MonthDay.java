package java.time;

import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Objects;
import sun.util.locale.LanguageTag;

public final class MonthDay implements TemporalAccessor, TemporalAdjuster, Comparable<MonthDay>, Serializable {
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendLiteral("--").appendValue(ChronoField.MONTH_OF_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter();
    private static final long serialVersionUID = -939150713474957432L;
    private final int day;
    private final int month;

    public static MonthDay now() {
        return now(Clock.systemDefaultZone());
    }

    public static MonthDay now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static MonthDay now(Clock clock) {
        LocalDate now = LocalDate.now(clock);
        return m920of(now.getMonth(), now.getDayOfMonth());
    }

    /* renamed from: of */
    public static MonthDay m920of(Month month2, int i) {
        Objects.requireNonNull(month2, "month");
        ChronoField.DAY_OF_MONTH.checkValidValue((long) i);
        if (i <= month2.maxLength()) {
            return new MonthDay(month2.getValue(), i);
        }
        throw new DateTimeException("Illegal value for DayOfMonth field, value " + i + " is not valid for month " + month2.name());
    }

    /* renamed from: of */
    public static MonthDay m919of(int i, int i2) {
        return m920of(Month.m918of(i), i2);
    }

    public static MonthDay from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof MonthDay) {
            return (MonthDay) temporalAccessor;
        }
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(temporalAccessor))) {
                temporalAccessor = LocalDate.from(temporalAccessor);
            }
            return m919of(temporalAccessor.get(ChronoField.MONTH_OF_YEAR), temporalAccessor.get(ChronoField.DAY_OF_MONTH));
        } catch (DateTimeException e) {
            throw new DateTimeException("Unable to obtain MonthDay from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName(), e);
        }
    }

    public static MonthDay parse(CharSequence charSequence) {
        return parse(charSequence, PARSER);
    }

    public static MonthDay parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (MonthDay) dateTimeFormatter.parse(charSequence, new MonthDay$$ExternalSyntheticLambda0());
    }

    private MonthDay(int i, int i2) {
        this.month = i;
        this.day = i2;
    }

    public boolean isSupported(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.MONTH_OF_YEAR || temporalField == ChronoField.DAY_OF_MONTH) {
                return true;
            }
            return false;
        } else if (temporalField == null || !temporalField.isSupportedBy(this)) {
            return false;
        } else {
            return true;
        }
    }

    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return temporalField.range();
        }
        if (temporalField == ChronoField.DAY_OF_MONTH) {
            return ValueRange.m954of(1, (long) getMonth().minLength(), (long) getMonth().maxLength());
        }
        return super.range(temporalField);
    }

    public int get(TemporalField temporalField) {
        return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
    }

    /* renamed from: java.time.MonthDay$1 */
    static /* synthetic */ class C28611 {
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
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_MONTH     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.MONTH_OF_YEAR     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.MonthDay.C28611.<clinit>():void");
        }
    }

    public long getLong(TemporalField temporalField) {
        int i;
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        int i2 = C28611.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()];
        if (i2 == 1) {
            i = this.day;
        } else if (i2 == 2) {
            i = this.month;
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        return (long) i;
    }

    public int getMonthValue() {
        return this.month;
    }

    public Month getMonth() {
        return Month.m918of(this.month);
    }

    public int getDayOfMonth() {
        return this.day;
    }

    public boolean isValidYear(int i) {
        return !(this.day == 29 && this.month == 2 && !Year.isLeap((long) i));
    }

    public MonthDay withMonth(int i) {
        return with(Month.m918of(i));
    }

    public MonthDay with(Month month2) {
        Objects.requireNonNull(month2, "month");
        if (month2.getValue() == this.month) {
            return this;
        }
        return new MonthDay(month2.getValue(), Math.min(this.day, month2.maxLength()));
    }

    public MonthDay withDayOfMonth(int i) {
        if (i == this.day) {
            return this;
        }
        return m919of(this.month, i);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return IsoChronology.INSTANCE;
        }
        return super.query(temporalQuery);
    }

    public Temporal adjustInto(Temporal temporal) {
        if (Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            Temporal with = temporal.with(ChronoField.MONTH_OF_YEAR, (long) this.month);
            return with.with(ChronoField.DAY_OF_MONTH, Math.min(with.range(ChronoField.DAY_OF_MONTH).getMaximum(), (long) this.day));
        }
        throw new DateTimeException("Adjustment only supported on ISO date-time");
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public LocalDate atYear(int i) {
        return LocalDate.m906of(i, this.month, isValidYear(i) ? this.day : 28);
    }

    public int compareTo(MonthDay monthDay) {
        int i = this.month - monthDay.month;
        return i == 0 ? this.day - monthDay.day : i;
    }

    public boolean isAfter(MonthDay monthDay) {
        return compareTo(monthDay) > 0;
    }

    public boolean isBefore(MonthDay monthDay) {
        return compareTo(monthDay) < 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MonthDay)) {
            return false;
        }
        MonthDay monthDay = (MonthDay) obj;
        if (this.month == monthDay.month && this.day == monthDay.day) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.month << 6) + this.day;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(10);
        sb.append("--");
        sb.append(this.month < 10 ? "0" : "");
        sb.append(this.month);
        sb.append(this.day < 10 ? "-0" : LanguageTag.SEP);
        sb.append(this.day);
        return sb.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 13, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(this.month);
        dataOutput.writeByte(this.day);
    }

    static MonthDay readExternal(DataInput dataInput) throws IOException {
        return m919of((int) dataInput.readByte(), (int) dataInput.readByte());
    }
}
