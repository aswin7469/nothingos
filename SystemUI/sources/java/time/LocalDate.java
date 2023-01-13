package java.time;

import android.icu.lang.UCharacter;
import android.net.wifi.WifiEnterpriseConfig;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.settingslib.utils.StringUtil;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Era;
import java.time.chrono.IsoChronology;
import java.time.chrono.IsoEra;
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
import java.util.Objects;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import sun.util.locale.LanguageTag;

public final class LocalDate implements Temporal, TemporalAdjuster, ChronoLocalDate, Serializable {
    static final long DAYS_0000_TO_1970 = 719528;
    private static final int DAYS_PER_CYCLE = 146097;
    public static final LocalDate EPOCH = m906of(1970, 1, 1);
    public static final LocalDate MAX = m906of((int) Year.MAX_VALUE, 12, 31);
    public static final LocalDate MIN = m906of((int) Year.MIN_VALUE, 1, 1);
    private static final long serialVersionUID = 2942565459149668126L;
    private final short day;
    private final short month;
    private final int year;

    public static LocalDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static LocalDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static LocalDate now(Clock clock) {
        Objects.requireNonNull(clock, DemoMode.COMMAND_CLOCK);
        return ofInstant(clock.instant(), clock.getZone());
    }

    /* renamed from: of */
    public static LocalDate m907of(int i, Month month2, int i2) {
        ChronoField.YEAR.checkValidValue((long) i);
        Objects.requireNonNull(month2, "month");
        ChronoField.DAY_OF_MONTH.checkValidValue((long) i2);
        return create(i, month2.getValue(), i2);
    }

    /* renamed from: of */
    public static LocalDate m906of(int i, int i2, int i3) {
        ChronoField.YEAR.checkValidValue((long) i);
        ChronoField.MONTH_OF_YEAR.checkValidValue((long) i2);
        ChronoField.DAY_OF_MONTH.checkValidValue((long) i3);
        return create(i, i2, i3);
    }

    public static LocalDate ofYearDay(int i, int i2) {
        long j = (long) i;
        ChronoField.YEAR.checkValidValue(j);
        ChronoField.DAY_OF_YEAR.checkValidValue((long) i2);
        boolean isLeapYear = IsoChronology.INSTANCE.isLeapYear(j);
        if (i2 != 366 || isLeapYear) {
            Month of = Month.m918of(((i2 - 1) / 31) + 1);
            if (i2 > (of.firstDayOfYear(isLeapYear) + of.length(isLeapYear)) - 1) {
                of = of.plus(1);
            }
            return new LocalDate(i, of.getValue(), (i2 - of.firstDayOfYear(isLeapYear)) + 1);
        }
        throw new DateTimeException("Invalid date 'DayOfYear 366' as '" + i + "' is not a leap year");
    }

    public static LocalDate ofInstant(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        return ofEpochDay(Math.floorDiv(instant.getEpochSecond() + ((long) zoneId.getRules().getOffset(instant).getTotalSeconds()), (int) StringUtil.SECONDS_PER_DAY));
    }

    public static LocalDate ofEpochDay(long j) {
        long j2;
        long j3 = j;
        ChronoField.EPOCH_DAY.checkValidValue(j3);
        long j4 = (j3 + DAYS_0000_TO_1970) - 60;
        if (j4 < 0) {
            long j5 = ((j4 + 1) / 146097) - 1;
            j2 = j5 * 400;
            j4 += (-j5) * 146097;
        } else {
            j2 = 0;
        }
        long j6 = ((j4 * 400) + 591) / 146097;
        long j7 = j4 - ((((j6 * 365) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
        if (j7 < 0) {
            j6--;
            j7 = j4 - ((((365 * j6) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
        }
        int i = (int) j7;
        int i2 = ((i * 5) + 2) / 153;
        return new LocalDate(ChronoField.YEAR.checkValidIntValue(j6 + j2 + ((long) (i2 / 10))), ((i2 + 2) % 12) + 1, (i - (((i2 * UCharacter.UnicodeBlock.SYMBOLS_FOR_LEGACY_COMPUTING_ID) + 5) / 10)) + 1);
    }

    public static LocalDate from(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        LocalDate localDate = (LocalDate) temporalAccessor.query(TemporalQueries.localDate());
        if (localDate != null) {
            return localDate;
        }
        throw new DateTimeException("Unable to obtain LocalDate from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName());
    }

    public static LocalDate parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static LocalDate parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (LocalDate) dateTimeFormatter.parse(charSequence, new LocalDate$$ExternalSyntheticLambda3());
    }

    private static LocalDate create(int i, int i2, int i3) {
        int i4 = 28;
        if (i3 > 28) {
            if (i2 != 2) {
                i4 = (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) ? 30 : 31;
            } else if (IsoChronology.INSTANCE.isLeapYear((long) i)) {
                i4 = 29;
            }
            if (i3 > i4) {
                if (i3 == 29) {
                    throw new DateTimeException("Invalid date 'February 29' as '" + i + "' is not a leap year");
                }
                throw new DateTimeException("Invalid date '" + Month.m918of(i2).name() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + i3 + "'");
            }
        }
        return new LocalDate(i, i2, i3);
    }

    private static LocalDate resolvePreviousValid(int i, int i2, int i3) {
        if (i2 == 2) {
            i3 = Math.min(i3, IsoChronology.INSTANCE.isLeapYear((long) i) ? 29 : 28);
        } else if (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) {
            i3 = Math.min(i3, 30);
        }
        return new LocalDate(i, i2, i3);
    }

    private LocalDate(int i, int i2, int i3) {
        this.year = i;
        this.month = (short) i2;
        this.day = (short) i3;
    }

    public boolean isSupported(TemporalField temporalField) {
        return super.isSupported(temporalField);
    }

    public boolean isSupported(TemporalUnit temporalUnit) {
        return super.isSupported(temporalUnit);
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        if (chronoField.isDateBased()) {
            int i = C28571.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()];
            if (i == 1) {
                return ValueRange.m953of(1, (long) lengthOfMonth());
            }
            if (i == 2) {
                return ValueRange.m953of(1, (long) lengthOfYear());
            }
            if (i == 3) {
                return ValueRange.m953of(1, (getMonth() != Month.FEBRUARY || isLeapYear()) ? 5 : 4);
            } else if (i != 4) {
                return temporalField.range();
            } else {
                return ValueRange.m953of(1, getYear() <= 0 ? 1000000000 : 999999999);
            }
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
    }

    public int get(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return get0(temporalField);
        }
        return super.get(temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        if (temporalField == ChronoField.EPOCH_DAY) {
            return toEpochDay();
        }
        if (temporalField == ChronoField.PROLEPTIC_MONTH) {
            return getProlepticMonth();
        }
        return (long) get0(temporalField);
    }

    private int get0(TemporalField temporalField) {
        switch (C28571.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()]) {
            case 1:
                return this.day;
            case 2:
                return getDayOfYear();
            case 3:
                return ((this.day - 1) / 7) + 1;
            case 4:
                int i = this.year;
                return i >= 1 ? i : 1 - i;
            case 5:
                return getDayOfWeek().getValue();
            case 6:
                return ((this.day - 1) % 7) + 1;
            case 7:
                return ((getDayOfYear() - 1) % 7) + 1;
            case 8:
                throw new UnsupportedTemporalTypeException("Invalid field 'EpochDay' for get() method, use getLong() instead");
            case 9:
                return ((getDayOfYear() - 1) / 7) + 1;
            case 10:
                return this.month;
            case 11:
                throw new UnsupportedTemporalTypeException("Invalid field 'ProlepticMonth' for get() method, use getLong() instead");
            case 12:
                return this.year;
            case 13:
                if (this.year >= 1) {
                    return 1;
                }
                return 0;
            default:
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
    }

    private long getProlepticMonth() {
        return ((((long) this.year) * 12) + ((long) this.month)) - 1;
    }

    public IsoChronology getChronology() {
        return IsoChronology.INSTANCE;
    }

    public Era getEra() {
        return getYear() >= 1 ? IsoEra.CE : IsoEra.BCE;
    }

    public int getYear() {
        return this.year;
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

    public int getDayOfYear() {
        return (getMonth().firstDayOfYear(isLeapYear()) + this.day) - 1;
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.m904of(Math.floorMod(toEpochDay() + 3, 7) + 1);
    }

    public boolean isLeapYear() {
        return IsoChronology.INSTANCE.isLeapYear((long) this.year);
    }

    public int lengthOfMonth() {
        short s = this.month;
        return s != 2 ? (s == 4 || s == 6 || s == 9 || s == 11) ? 30 : 31 : isLeapYear() ? 29 : 28;
    }

    public int lengthOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    public LocalDate with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return (LocalDate) temporalAdjuster;
        }
        return (LocalDate) temporalAdjuster.adjustInto(this);
    }

    public LocalDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (LocalDate) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        chronoField.checkValidValue(j);
        switch (C28571.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()]) {
            case 1:
                return withDayOfMonth((int) j);
            case 2:
                return withDayOfYear((int) j);
            case 3:
                return plusWeeks(j - getLong(ChronoField.ALIGNED_WEEK_OF_MONTH));
            case 4:
                if (this.year < 1) {
                    j = 1 - j;
                }
                return withYear((int) j);
            case 5:
                return plusDays(j - ((long) getDayOfWeek().getValue()));
            case 6:
                return plusDays(j - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
            case 7:
                return plusDays(j - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
            case 8:
                return ofEpochDay(j);
            case 9:
                return plusWeeks(j - getLong(ChronoField.ALIGNED_WEEK_OF_YEAR));
            case 10:
                return withMonth((int) j);
            case 11:
                return plusMonths(j - getProlepticMonth());
            case 12:
                return withYear((int) j);
            case 13:
                return getLong(ChronoField.ERA) == j ? this : withYear(1 - this.year);
            default:
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
    }

    public LocalDate withYear(int i) {
        if (this.year == i) {
            return this;
        }
        ChronoField.YEAR.checkValidValue((long) i);
        return resolvePreviousValid(i, this.month, this.day);
    }

    public LocalDate withMonth(int i) {
        if (this.month == i) {
            return this;
        }
        ChronoField.MONTH_OF_YEAR.checkValidValue((long) i);
        return resolvePreviousValid(this.year, i, this.day);
    }

    public LocalDate withDayOfMonth(int i) {
        if (this.day == i) {
            return this;
        }
        return m906of(this.year, (int) this.month, i);
    }

    public LocalDate withDayOfYear(int i) {
        if (getDayOfYear() == i) {
            return this;
        }
        return ofYearDay(this.year, i);
    }

    public LocalDate plus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            Period period = (Period) temporalAmount;
            return plusMonths(period.toTotalMonths()).plusDays((long) period.getDays());
        }
        Objects.requireNonNull(temporalAmount, "amountToAdd");
        return (LocalDate) temporalAmount.addTo(this);
    }

    public LocalDate plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (LocalDate) temporalUnit.addTo(this, j);
        }
        switch (C28571.$SwitchMap$java$time$temporal$ChronoUnit[((ChronoUnit) temporalUnit).ordinal()]) {
            case 1:
                return plusDays(j);
            case 2:
                return plusWeeks(j);
            case 3:
                return plusMonths(j);
            case 4:
                return plusYears(j);
            case 5:
                return plusYears(Math.multiplyExact(j, 10));
            case 6:
                return plusYears(Math.multiplyExact(j, 100));
            case 7:
                return plusYears(Math.multiplyExact(j, 1000));
            case 8:
                return with((TemporalField) ChronoField.ERA, Math.addExact(getLong(ChronoField.ERA), j));
            default:
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
        }
    }

    /* renamed from: java.time.LocalDate$1 */
    static /* synthetic */ class C28571 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoField;
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoUnit;

        /* JADX WARNING: Can't wrap try/catch for region: R(46:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|(2:17|18)|19|21|22|23|(2:25|26)|27|(2:29|30)|31|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|60) */
        /* JADX WARNING: Can't wrap try/catch for region: R(49:0|(2:1|2)|3|5|6|7|9|10|11|(2:13|14)|15|17|18|19|21|22|23|(2:25|26)|27|29|30|31|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|60) */
        /* JADX WARNING: Can't wrap try/catch for region: R(52:0|1|2|3|5|6|7|9|10|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|60) */
        /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0071 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x007b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x0085 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x008f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x0099 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x00a3 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x00ad */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x00b7 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x00c3 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x00cf */
        /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x00db */
        /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x00e7 */
        static {
            /*
                java.time.temporal.ChronoUnit[] r0 = java.time.temporal.ChronoUnit.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$temporal$ChronoUnit = r0
                r1 = 1
                java.time.temporal.ChronoUnit r2 = java.time.temporal.ChronoUnit.DAYS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.temporal.ChronoUnit r3 = java.time.temporal.ChronoUnit.WEEKS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.time.temporal.ChronoUnit r4 = java.time.temporal.ChronoUnit.MONTHS     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.time.temporal.ChronoUnit r5 = java.time.temporal.ChronoUnit.YEARS     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                r4 = 5
                int[] r5 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x003e }
                java.time.temporal.ChronoUnit r6 = java.time.temporal.ChronoUnit.DECADES     // Catch:{ NoSuchFieldError -> 0x003e }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r5[r6] = r4     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                r5 = 6
                int[] r6 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.time.temporal.ChronoUnit r7 = java.time.temporal.ChronoUnit.CENTURIES     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r7 = r7.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r6[r7] = r5     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                r6 = 7
                int[] r7 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.time.temporal.ChronoUnit r8 = java.time.temporal.ChronoUnit.MILLENNIA     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r8 = r8.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r7[r8] = r6     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                r7 = 8
                int[] r8 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0060 }
                java.time.temporal.ChronoUnit r9 = java.time.temporal.ChronoUnit.ERAS     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r9 = r9.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r8[r9] = r7     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                java.time.temporal.ChronoField[] r8 = java.time.temporal.ChronoField.values()
                int r8 = r8.length
                int[] r8 = new int[r8]
                $SwitchMap$java$time$temporal$ChronoField = r8
                java.time.temporal.ChronoField r9 = java.time.temporal.ChronoField.DAY_OF_MONTH     // Catch:{ NoSuchFieldError -> 0x0071 }
                int r9 = r9.ordinal()     // Catch:{ NoSuchFieldError -> 0x0071 }
                r8[r9] = r1     // Catch:{ NoSuchFieldError -> 0x0071 }
            L_0x0071:
                int[] r1 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x007b }
                java.time.temporal.ChronoField r8 = java.time.temporal.ChronoField.DAY_OF_YEAR     // Catch:{ NoSuchFieldError -> 0x007b }
                int r8 = r8.ordinal()     // Catch:{ NoSuchFieldError -> 0x007b }
                r1[r8] = r0     // Catch:{ NoSuchFieldError -> 0x007b }
            L_0x007b:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0085 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH     // Catch:{ NoSuchFieldError -> 0x0085 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0085 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0085 }
            L_0x0085:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x008f }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR_OF_ERA     // Catch:{ NoSuchFieldError -> 0x008f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x008f }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x008f }
            L_0x008f:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0099 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_WEEK     // Catch:{ NoSuchFieldError -> 0x0099 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0099 }
                r0[r1] = r4     // Catch:{ NoSuchFieldError -> 0x0099 }
            L_0x0099:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x00a3 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH     // Catch:{ NoSuchFieldError -> 0x00a3 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a3 }
                r0[r1] = r5     // Catch:{ NoSuchFieldError -> 0x00a3 }
            L_0x00a3:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x00ad }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR     // Catch:{ NoSuchFieldError -> 0x00ad }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ad }
                r0[r1] = r6     // Catch:{ NoSuchFieldError -> 0x00ad }
            L_0x00ad:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x00b7 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.EPOCH_DAY     // Catch:{ NoSuchFieldError -> 0x00b7 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b7 }
                r0[r1] = r7     // Catch:{ NoSuchFieldError -> 0x00b7 }
            L_0x00b7:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x00c3 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR     // Catch:{ NoSuchFieldError -> 0x00c3 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c3 }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00c3 }
            L_0x00c3:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x00cf }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.MONTH_OF_YEAR     // Catch:{ NoSuchFieldError -> 0x00cf }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00cf }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00cf }
            L_0x00cf:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x00db }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.PROLEPTIC_MONTH     // Catch:{ NoSuchFieldError -> 0x00db }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00db }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00db }
            L_0x00db:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x00e7 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR     // Catch:{ NoSuchFieldError -> 0x00e7 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00e7 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00e7 }
            L_0x00e7:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x00f3 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ERA     // Catch:{ NoSuchFieldError -> 0x00f3 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00f3 }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00f3 }
            L_0x00f3:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.LocalDate.C28571.<clinit>():void");
        }
    }

    public LocalDate plusYears(long j) {
        if (j == 0) {
            return this;
        }
        return resolvePreviousValid(ChronoField.YEAR.checkValidIntValue(((long) this.year) + j), this.month, this.day);
    }

    public LocalDate plusMonths(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = (((long) this.year) * 12) + ((long) (this.month - 1)) + j;
        return resolvePreviousValid(ChronoField.YEAR.checkValidIntValue(Math.floorDiv(j2, 12)), Math.floorMod(j2, 12) + 1, this.day);
    }

    public LocalDate plusWeeks(long j) {
        return plusDays(Math.multiplyExact(j, 7));
    }

    public LocalDate plusDays(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = ((long) this.day) + j;
        if (j2 > 0) {
            if (j2 <= 28) {
                return new LocalDate(this.year, this.month, (int) j2);
            }
            if (j2 <= 59) {
                long lengthOfMonth = (long) lengthOfMonth();
                if (j2 <= lengthOfMonth) {
                    return new LocalDate(this.year, this.month, (int) j2);
                }
                short s = this.month;
                if (s < 12) {
                    return new LocalDate(this.year, s + 1, (int) (j2 - lengthOfMonth));
                }
                ChronoField.YEAR.checkValidValue((long) (this.year + 1));
                return new LocalDate(this.year + 1, 1, (int) (j2 - lengthOfMonth));
            }
        }
        return ofEpochDay(Math.addExact(toEpochDay(), j));
    }

    public LocalDate minus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            Period period = (Period) temporalAmount;
            return minusMonths(period.toTotalMonths()).minusDays((long) period.getDays());
        }
        Objects.requireNonNull(temporalAmount, "amountToSubtract");
        return (LocalDate) temporalAmount.subtractFrom(this);
    }

    public LocalDate minus(long j, TemporalUnit temporalUnit) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plus(Long.MAX_VALUE, temporalUnit);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plus(j2, temporalUnit);
    }

    public LocalDate minusYears(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusYears(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusYears(j2);
    }

    public LocalDate minusMonths(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusMonths(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusMonths(j2);
    }

    public LocalDate minusWeeks(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusWeeks(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusWeeks(j2);
    }

    public LocalDate minusDays(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusDays(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusDays(j2);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return this;
        }
        return super.query(temporalQuery);
    }

    public Temporal adjustInto(Temporal temporal) {
        return super.adjustInto(temporal);
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        LocalDate from = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        switch (C28571.$SwitchMap$java$time$temporal$ChronoUnit[((ChronoUnit) temporalUnit).ordinal()]) {
            case 1:
                return daysUntil(from);
            case 2:
                return daysUntil(from) / 7;
            case 3:
                return monthsUntil(from);
            case 4:
                return monthsUntil(from) / 12;
            case 5:
                return monthsUntil(from) / 120;
            case 6:
                return monthsUntil(from) / NotificationTapHelper.DOUBLE_TAP_TIMEOUT_MS;
            case 7:
                return monthsUntil(from) / 12000;
            case 8:
                return from.getLong(ChronoField.ERA) - getLong(ChronoField.ERA);
            default:
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
        }
    }

    /* access modifiers changed from: package-private */
    public long daysUntil(LocalDate localDate) {
        return localDate.toEpochDay() - toEpochDay();
    }

    private long monthsUntil(LocalDate localDate) {
        return (((localDate.getProlepticMonth() * 32) + ((long) localDate.getDayOfMonth())) - ((getProlepticMonth() * 32) + ((long) getDayOfMonth()))) / 32;
    }

    public Period until(ChronoLocalDate chronoLocalDate) {
        LocalDate from = from(chronoLocalDate);
        long prolepticMonth = from.getProlepticMonth() - getProlepticMonth();
        int i = from.day - this.day;
        int i2 = (prolepticMonth > 0 ? 1 : (prolepticMonth == 0 ? 0 : -1));
        if (i2 > 0 && i < 0) {
            prolepticMonth--;
            i = (int) (from.toEpochDay() - plusMonths(prolepticMonth).toEpochDay());
        } else if (i2 < 0 && i > 0) {
            prolepticMonth++;
            i -= from.lengthOfMonth();
        }
        return Period.m926of(Math.toIntExact(prolepticMonth / 12), (int) (prolepticMonth % 12), i);
    }

    public Stream<LocalDate> datesUntil(LocalDate localDate) {
        long epochDay = localDate.toEpochDay();
        long epochDay2 = toEpochDay();
        if (epochDay >= epochDay2) {
            return LongStream.range(epochDay2, epochDay).mapToObj(new LocalDate$$ExternalSyntheticLambda0());
        }
        throw new IllegalArgumentException(localDate + " < " + this);
    }

    public Stream<LocalDate> datesUntil(LocalDate localDate, Period period) {
        long j;
        long j2;
        if (!period.isZero()) {
            long epochDay = localDate.toEpochDay();
            long epochDay2 = toEpochDay();
            long j3 = epochDay - epochDay2;
            long totalMonths = period.toTotalMonths();
            long days = (long) period.getDays();
            int i = (totalMonths > 0 ? 1 : (totalMonths == 0 ? 0 : -1));
            if ((i >= 0 || days <= 0) && (i <= 0 || days >= 0)) {
                int i2 = (j3 > 0 ? 1 : (j3 == 0 ? 0 : -1));
                if (i2 == 0) {
                    return Stream.empty();
                }
                int i3 = (i > 0 || days > 0) ? 1 : -1;
                boolean z = false;
                boolean z2 = i3 < 0;
                if (i2 < 0) {
                    z = true;
                }
                if (z2 ^ z) {
                    StringBuilder sb = new StringBuilder();
                    sb.append((Object) localDate);
                    sb.append(i3 < 0 ? " > " : " < ");
                    sb.append((Object) this);
                    throw new IllegalArgumentException(sb.toString());
                } else if (i == 0) {
                    return LongStream.rangeClosed(0, (j3 - ((long) i3)) / days).mapToObj(new LocalDate$$ExternalSyntheticLambda1(epochDay2, days));
                } else {
                    long j4 = ((j3 * 1600) / ((48699 * totalMonths) + (1600 * days))) + 1;
                    long j5 = totalMonths * j4;
                    long j6 = days * j4;
                    if (i > 0) {
                        j2 = MAX.getProlepticMonth();
                        j = getProlepticMonth();
                    } else {
                        j2 = getProlepticMonth();
                        j = MIN.getProlepticMonth();
                    }
                    long j7 = j2 - j;
                    long j8 = (long) i3;
                    if (j5 * j8 > j7 || (plusMonths(j5).toEpochDay() + j6) * j8 >= epochDay * j8) {
                        j4--;
                        long j9 = j5 - totalMonths;
                        long j10 = j6 - days;
                        if (j9 * j8 > j7 || (plusMonths(j9).toEpochDay() + j10) * j8 >= epochDay * j8) {
                            j4--;
                        }
                    }
                    return LongStream.rangeClosed(0, j4).mapToObj(new LocalDate$$ExternalSyntheticLambda2(this, totalMonths, days));
                }
            } else {
                throw new IllegalArgumentException("period months and days are of opposite sign");
            }
        } else {
            throw new IllegalArgumentException("step is zero");
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$datesUntil$1$java-time-LocalDate  reason: not valid java name */
    public /* synthetic */ LocalDate m3164lambda$datesUntil$1$javatimeLocalDate(long j, long j2, long j3) {
        return plusMonths(j * j3).plusDays(j2 * j3);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public LocalDateTime atTime(LocalTime localTime) {
        return LocalDateTime.m914of(this, localTime);
    }

    public LocalDateTime atTime(int i, int i2) {
        return atTime(LocalTime.m915of(i, i2));
    }

    public LocalDateTime atTime(int i, int i2, int i3) {
        return atTime(LocalTime.m916of(i, i2, i3));
    }

    public LocalDateTime atTime(int i, int i2, int i3, int i4) {
        return atTime(LocalTime.m917of(i, i2, i3, i4));
    }

    public OffsetDateTime atTime(OffsetTime offsetTime) {
        return OffsetDateTime.m923of(LocalDateTime.m914of(this, offsetTime.toLocalTime()), offsetTime.getOffset());
    }

    public LocalDateTime atStartOfDay() {
        return LocalDateTime.m914of(this, LocalTime.MIDNIGHT);
    }

    public ZonedDateTime atStartOfDay(ZoneId zoneId) {
        ZoneOffsetTransition transition;
        Objects.requireNonNull(zoneId, "zone");
        LocalDateTime atTime = atTime(LocalTime.MIDNIGHT);
        if (!(zoneId instanceof ZoneOffset) && (transition = zoneId.getRules().getTransition(atTime)) != null && transition.isGap()) {
            atTime = transition.getDateTimeAfter();
        }
        return ZonedDateTime.m936of(atTime, zoneId);
    }

    public long toEpochDay() {
        long j;
        long j2 = (long) this.year;
        long j3 = (long) this.month;
        long j4 = (365 * j2) + 0;
        if (j2 >= 0) {
            j = j4 + (((3 + j2) / 4) - ((99 + j2) / 100)) + ((j2 + 399) / 400);
        } else {
            j = j4 - (((j2 / -4) - (j2 / -100)) + (j2 / -400));
        }
        long j5 = j + (((367 * j3) - 362) / 12) + ((long) (this.day - 1));
        if (j3 > 2) {
            j5--;
            if (!isLeapYear()) {
                j5--;
            }
        }
        return j5 - DAYS_0000_TO_1970;
    }

    public long toEpochSecond(LocalTime localTime, ZoneOffset zoneOffset) {
        Objects.requireNonNull(localTime, "time");
        Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        return ((toEpochDay() * 86400) + ((long) localTime.toSecondOfDay())) - ((long) zoneOffset.getTotalSeconds());
    }

    public int compareTo(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate);
        }
        return super.compareTo(chronoLocalDate);
    }

    /* access modifiers changed from: package-private */
    public int compareTo0(LocalDate localDate) {
        int i = this.year - localDate.year;
        if (i != 0) {
            return i;
        }
        int i2 = this.month - localDate.month;
        return i2 == 0 ? this.day - localDate.day : i2;
    }

    public boolean isAfter(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate) > 0;
        }
        return super.isAfter(chronoLocalDate);
    }

    public boolean isBefore(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate) < 0;
        }
        return super.isBefore(chronoLocalDate);
    }

    public boolean isEqual(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate) == 0;
        }
        return super.isEqual(chronoLocalDate);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocalDate)) {
            return false;
        }
        if (compareTo0((LocalDate) obj) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = this.year;
        return (i & -2048) ^ (((i << 11) + (this.month << 6)) + this.day);
    }

    public String toString() {
        int i = this.year;
        short s = this.month;
        short s2 = this.day;
        int abs = Math.abs(i);
        StringBuilder sb = new StringBuilder(10);
        if (abs >= 1000) {
            if (i > 9999) {
                sb.append('+');
            }
            sb.append(i);
        } else if (i < 0) {
            sb.append(i - 10000);
            sb.deleteCharAt(1);
        } else {
            sb.append(i + 10000);
            sb.deleteCharAt(0);
        }
        String str = "-0";
        sb.append(s < 10 ? str : LanguageTag.SEP);
        sb.append((int) s);
        if (s2 >= 10) {
            str = LanguageTag.SEP;
        }
        sb.append(str);
        sb.append((int) s2);
        return sb.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 3, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
        dataOutput.writeByte(this.month);
        dataOutput.writeByte(this.day);
    }

    static LocalDate readExternal(DataInput dataInput) throws IOException {
        return m906of(dataInput.readInt(), (int) dataInput.readByte(), (int) dataInput.readByte());
    }
}
