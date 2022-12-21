package java.time.chrono;

import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
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
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.Era;
import sun.util.calendar.LocalGregorianCalendar;

public final class JapaneseDate extends ChronoLocalDateImpl<JapaneseDate> implements ChronoLocalDate, Serializable {
    static final LocalDate MEIJI_6_ISODATE = LocalDate.m908of(1873, 1, 1);
    private static final long serialVersionUID = -305327627230580483L;
    private transient JapaneseEra era;
    private final transient LocalDate isoDate;
    private transient int yearOfEra;

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public static JapaneseDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static JapaneseDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static JapaneseDate now(Clock clock) {
        return new JapaneseDate(LocalDate.now(clock));
    }

    /* renamed from: of */
    public static JapaneseDate m947of(JapaneseEra japaneseEra, int i, int i2, int i3) {
        Objects.requireNonNull(japaneseEra, "era");
        LocalGregorianCalendar.Date newCalendarDate = JapaneseChronology.JCAL.newCalendarDate((TimeZone) null);
        newCalendarDate.setEra(japaneseEra.getPrivateEra()).setDate(i, i2, i3);
        if (JapaneseChronology.JCAL.validate(newCalendarDate)) {
            return new JapaneseDate(japaneseEra, i, LocalDate.m908of(newCalendarDate.getNormalizedYear(), i2, i3));
        }
        throw new DateTimeException("year, month, and day not valid for Era");
    }

    /* renamed from: of */
    public static JapaneseDate m946of(int i, int i2, int i3) {
        return new JapaneseDate(LocalDate.m908of(i, i2, i3));
    }

    static JapaneseDate ofYearDay(JapaneseEra japaneseEra, int i, int i2) {
        Objects.requireNonNull(japaneseEra, "era");
        CalendarDate sinceDate = japaneseEra.getPrivateEra().getSinceDate();
        LocalGregorianCalendar.Date newCalendarDate = JapaneseChronology.JCAL.newCalendarDate((TimeZone) null);
        newCalendarDate.setEra(japaneseEra.getPrivateEra());
        if (i == 1) {
            newCalendarDate.setDate(i, sinceDate.getMonth(), (sinceDate.getDayOfMonth() + i2) - 1);
        } else {
            newCalendarDate.setDate(i, 1, i2);
        }
        JapaneseChronology.JCAL.normalize(newCalendarDate);
        if (japaneseEra.getPrivateEra() == newCalendarDate.getEra() && i == newCalendarDate.getYear()) {
            return new JapaneseDate(japaneseEra, i, LocalDate.m908of(newCalendarDate.getNormalizedYear(), newCalendarDate.getMonth(), newCalendarDate.getDayOfMonth()));
        }
        throw new DateTimeException("Invalid parameters");
    }

    public static JapaneseDate from(TemporalAccessor temporalAccessor) {
        return JapaneseChronology.INSTANCE.date(temporalAccessor);
    }

    JapaneseDate(LocalDate localDate) {
        if (!localDate.isBefore(MEIJI_6_ISODATE)) {
            LocalGregorianCalendar.Date privateJapaneseDate = toPrivateJapaneseDate(localDate);
            this.era = JapaneseEra.toJapaneseEra(privateJapaneseDate.getEra());
            this.yearOfEra = privateJapaneseDate.getYear();
            this.isoDate = localDate;
            return;
        }
        throw new DateTimeException("JapaneseDate before Meiji 6 is not supported");
    }

    JapaneseDate(JapaneseEra japaneseEra, int i, LocalDate localDate) {
        if (!localDate.isBefore(MEIJI_6_ISODATE)) {
            this.era = japaneseEra;
            this.yearOfEra = i;
            this.isoDate = localDate;
            return;
        }
        throw new DateTimeException("JapaneseDate before Meiji 6 is not supported");
    }

    public JapaneseChronology getChronology() {
        return JapaneseChronology.INSTANCE;
    }

    public JapaneseEra getEra() {
        return this.era;
    }

    public int lengthOfMonth() {
        return this.isoDate.lengthOfMonth();
    }

    public int lengthOfYear() {
        Calendar createCalendar = JapaneseChronology.createCalendar();
        createCalendar.set(0, this.era.getValue() + 2);
        createCalendar.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
        return createCalendar.getActualMaximum(6);
    }

    public boolean isSupported(TemporalField temporalField) {
        if (temporalField == ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH || temporalField == ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR || temporalField == ChronoField.ALIGNED_WEEK_OF_MONTH || temporalField == ChronoField.ALIGNED_WEEK_OF_YEAR) {
            return false;
        }
        return super.isSupported(temporalField);
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (isSupported(temporalField)) {
            ChronoField chronoField = (ChronoField) temporalField;
            int i = C28701.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
            if (i == 1) {
                return ValueRange.m955of(1, (long) lengthOfMonth());
            }
            if (i == 2) {
                return ValueRange.m955of(1, (long) lengthOfYear());
            }
            if (i != 3) {
                return getChronology().range(chronoField);
            }
            Calendar createCalendar = JapaneseChronology.createCalendar();
            createCalendar.set(0, this.era.getValue() + 2);
            createCalendar.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
            return ValueRange.m955of(1, (long) createCalendar.getActualMaximum(1));
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField) temporalField) {
            case DAY_OF_YEAR:
                Calendar createCalendar = JapaneseChronology.createCalendar();
                createCalendar.set(0, this.era.getValue() + 2);
                createCalendar.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
                return (long) createCalendar.get(6);
            case YEAR_OF_ERA:
                return (long) this.yearOfEra;
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
            case ALIGNED_WEEK_OF_MONTH:
            case ALIGNED_WEEK_OF_YEAR:
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            case ERA:
                return (long) this.era.getValue();
            default:
                return this.isoDate.getLong(temporalField);
        }
    }

    private static LocalGregorianCalendar.Date toPrivateJapaneseDate(LocalDate localDate) {
        LocalGregorianCalendar.Date newCalendarDate = JapaneseChronology.JCAL.newCalendarDate((TimeZone) null);
        Era privateEraFrom = JapaneseEra.privateEraFrom(localDate);
        int year = localDate.getYear();
        if (privateEraFrom != null) {
            year -= privateEraFrom.getSinceDate().getYear() - 1;
        }
        newCalendarDate.setEra(privateEraFrom).setYear(year).setMonth(localDate.getMonthValue()).setDayOfMonth(localDate.getDayOfMonth());
        JapaneseChronology.JCAL.normalize(newCalendarDate);
        return newCalendarDate;
    }

    public JapaneseDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (JapaneseDate) super.with(temporalField, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        if (getLong(chronoField) == j) {
            return this;
        }
        int i = C28701.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
        if (i == 3 || i == 8 || i == 9) {
            int checkValidIntValue = getChronology().range(chronoField).checkValidIntValue(j, chronoField);
            int i2 = C28701.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
            if (i2 == 3) {
                return withYear(checkValidIntValue);
            }
            if (i2 == 8) {
                return withYear(JapaneseEra.m948of(checkValidIntValue), this.yearOfEra);
            }
            if (i2 == 9) {
                return with(this.isoDate.withYear(checkValidIntValue));
            }
        }
        return with(this.isoDate.with(temporalField, j));
    }

    public JapaneseDate with(TemporalAdjuster temporalAdjuster) {
        return (JapaneseDate) super.with(temporalAdjuster);
    }

    public JapaneseDate plus(TemporalAmount temporalAmount) {
        return (JapaneseDate) super.plus(temporalAmount);
    }

    public JapaneseDate minus(TemporalAmount temporalAmount) {
        return (JapaneseDate) super.minus(temporalAmount);
    }

    private JapaneseDate withYear(JapaneseEra japaneseEra, int i) {
        return with(this.isoDate.withYear(JapaneseChronology.INSTANCE.prolepticYear(japaneseEra, i)));
    }

    private JapaneseDate withYear(int i) {
        return withYear(getEra(), i);
    }

    /* access modifiers changed from: package-private */
    public JapaneseDate plusYears(long j) {
        return with(this.isoDate.plusYears(j));
    }

    /* access modifiers changed from: package-private */
    public JapaneseDate plusMonths(long j) {
        return with(this.isoDate.plusMonths(j));
    }

    /* access modifiers changed from: package-private */
    public JapaneseDate plusWeeks(long j) {
        return with(this.isoDate.plusWeeks(j));
    }

    /* access modifiers changed from: package-private */
    public JapaneseDate plusDays(long j) {
        return with(this.isoDate.plusDays(j));
    }

    public JapaneseDate plus(long j, TemporalUnit temporalUnit) {
        return (JapaneseDate) super.plus(j, temporalUnit);
    }

    public JapaneseDate minus(long j, TemporalUnit temporalUnit) {
        return (JapaneseDate) super.minus(j, temporalUnit);
    }

    /* access modifiers changed from: package-private */
    public JapaneseDate minusYears(long j) {
        return (JapaneseDate) super.minusYears(j);
    }

    /* access modifiers changed from: package-private */
    public JapaneseDate minusMonths(long j) {
        return (JapaneseDate) super.minusMonths(j);
    }

    /* access modifiers changed from: package-private */
    public JapaneseDate minusWeeks(long j) {
        return (JapaneseDate) super.minusWeeks(j);
    }

    /* access modifiers changed from: package-private */
    public JapaneseDate minusDays(long j) {
        return (JapaneseDate) super.minusDays(j);
    }

    private JapaneseDate with(LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new JapaneseDate(localDate);
    }

    public final ChronoLocalDateTime<JapaneseDate> atTime(LocalTime localTime) {
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
        if (obj instanceof JapaneseDate) {
            return this.isoDate.equals(((JapaneseDate) obj).isoDate);
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
        return new Ser((byte) 4, this);
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static JapaneseDate readExternal(DataInput dataInput) throws IOException {
        return JapaneseChronology.INSTANCE.date(dataInput.readInt(), (int) dataInput.readByte(), (int) dataInput.readByte());
    }
}
