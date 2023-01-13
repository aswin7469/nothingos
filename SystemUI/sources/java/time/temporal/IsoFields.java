package java.time.temporal;

import android.icu.lang.UCharacter;
import android.icu.text.DateTimePatternGenerator;
import android.icu.util.ULocale;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class IsoFields {
    public static final TemporalField DAY_OF_QUARTER = Field.DAY_OF_QUARTER;
    public static final TemporalField QUARTER_OF_YEAR = Field.QUARTER_OF_YEAR;
    public static final TemporalUnit QUARTER_YEARS = Unit.QUARTER_YEARS;
    public static final TemporalField WEEK_BASED_YEAR = Field.WEEK_BASED_YEAR;
    public static final TemporalUnit WEEK_BASED_YEARS = Unit.WEEK_BASED_YEARS;
    public static final TemporalField WEEK_OF_WEEK_BASED_YEAR = Field.WEEK_OF_WEEK_BASED_YEAR;

    private IsoFields() {
        throw new AssertionError((Object) "Not instantiable");
    }

    private enum Field implements TemporalField {
        DAY_OF_QUARTER((String) null) {
            public String toString() {
                return "DayOfQuarter";
            }

            public TemporalUnit getBaseUnit() {
                return ChronoUnit.DAYS;
            }

            public TemporalUnit getRangeUnit() {
                return IsoFields.QUARTER_YEARS;
            }

            public ValueRange range() {
                return ValueRange.m954of(1, 90, 92);
            }

            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR) && temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) && temporalAccessor.isSupported(ChronoField.YEAR) && IsoFields.isIso(temporalAccessor);
            }

            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    long j = temporalAccessor.getLong(QUARTER_OF_YEAR);
                    if (j == 1) {
                        return IsoChronology.INSTANCE.isLeapYear(temporalAccessor.getLong(ChronoField.YEAR)) ? ValueRange.m953of(1, 91) : ValueRange.m953of(1, 90);
                    } else if (j == 2) {
                        return ValueRange.m953of(1, 91);
                    } else {
                        if (j == 3 || j == 4) {
                            return ValueRange.m953of(1, 92);
                        }
                        return range();
                    }
                } else {
                    throw new UnsupportedTemporalTypeException("Unsupported field: DayOfQuarter");
                }
            }

            public long getFrom(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return (long) (temporalAccessor.get(ChronoField.DAY_OF_YEAR) - Field.QUARTER_DAYS[((temporalAccessor.get(ChronoField.MONTH_OF_YEAR) - 1) / 3) + (IsoChronology.INSTANCE.isLeapYear(temporalAccessor.getLong(ChronoField.YEAR)) ? 4 : 0)]);
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: DayOfQuarter");
            }

            public <R extends Temporal> R adjustInto(R r, long j) {
                long from = getFrom(r);
                range().checkValidValue(j, this);
                return r.with(ChronoField.DAY_OF_YEAR, r.getLong(ChronoField.DAY_OF_YEAR) + (j - from));
            }

            public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
                LocalDate localDate;
                long j;
                Long l = map.get(ChronoField.YEAR);
                Long l2 = map.get(QUARTER_OF_YEAR);
                if (l == null || l2 == null) {
                    return null;
                }
                int checkValidIntValue = ChronoField.YEAR.checkValidIntValue(l.longValue());
                long longValue = map.get(DAY_OF_QUARTER).longValue();
                Field.ensureIso(temporalAccessor);
                if (resolverStyle == ResolverStyle.LENIENT) {
                    localDate = LocalDate.m906of(checkValidIntValue, 1, 1).plusMonths(Math.multiplyExact(Math.subtractExact(l2.longValue(), 1), 3));
                    j = Math.subtractExact(longValue, 1);
                } else {
                    localDate = LocalDate.m906of(checkValidIntValue, ((QUARTER_OF_YEAR.range().checkValidIntValue(l2.longValue(), QUARTER_OF_YEAR) - 1) * 3) + 1, 1);
                    if (longValue < 1 || longValue > 90) {
                        if (resolverStyle == ResolverStyle.STRICT) {
                            rangeRefinedBy(localDate).checkValidValue(longValue, this);
                        } else {
                            range().checkValidValue(longValue, this);
                        }
                    }
                    j = longValue - 1;
                }
                map.remove(this);
                map.remove(ChronoField.YEAR);
                map.remove(QUARTER_OF_YEAR);
                return localDate.plusDays(j);
            }
        },
        QUARTER_OF_YEAR((String) null) {
            public String toString() {
                return "QuarterOfYear";
            }

            public TemporalUnit getBaseUnit() {
                return IsoFields.QUARTER_YEARS;
            }

            public TemporalUnit getRangeUnit() {
                return ChronoUnit.YEARS;
            }

            public ValueRange range() {
                return ValueRange.m953of(1, 4);
            }

            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) && IsoFields.isIso(temporalAccessor);
            }

            public long getFrom(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return (temporalAccessor.getLong(ChronoField.MONTH_OF_YEAR) + 2) / 3;
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: QuarterOfYear");
            }

            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return super.rangeRefinedBy(temporalAccessor);
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: QuarterOfYear");
            }

            public <R extends Temporal> R adjustInto(R r, long j) {
                long from = getFrom(r);
                range().checkValidValue(j, this);
                return r.with(ChronoField.MONTH_OF_YEAR, r.getLong(ChronoField.MONTH_OF_YEAR) + ((j - from) * 3));
            }
        },
        WEEK_OF_WEEK_BASED_YEAR((String) null) {
            public String toString() {
                return "WeekOfWeekBasedYear";
            }

            public String getDisplayName(Locale locale) {
                Objects.requireNonNull(locale, "locale");
                String appendItemName = DateTimePatternGenerator.getInstance(ULocale.forLocale(locale)).getAppendItemName(4);
                return (appendItemName == null || appendItemName.isEmpty()) ? toString() : appendItemName;
            }

            public TemporalUnit getBaseUnit() {
                return ChronoUnit.WEEKS;
            }

            public TemporalUnit getRangeUnit() {
                return IsoFields.WEEK_BASED_YEARS;
            }

            public ValueRange range() {
                return ValueRange.m954of(1, 52, 53);
            }

            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.EPOCH_DAY) && IsoFields.isIso(temporalAccessor);
            }

            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return Field.getWeekRange(LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekOfWeekBasedYear");
            }

            public long getFrom(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return (long) Field.getWeek(LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekOfWeekBasedYear");
            }

            public <R extends Temporal> R adjustInto(R r, long j) {
                range().checkValidValue(j, this);
                return r.plus(Math.subtractExact(j, getFrom(r)), ChronoUnit.WEEKS);
            }

            public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
                LocalDate localDate;
                long j;
                Long l = map.get(WEEK_BASED_YEAR);
                Long l2 = map.get(ChronoField.DAY_OF_WEEK);
                if (l == null || l2 == null) {
                    return null;
                }
                int checkValidIntValue = WEEK_BASED_YEAR.range().checkValidIntValue(l.longValue(), WEEK_BASED_YEAR);
                long longValue = map.get(WEEK_OF_WEEK_BASED_YEAR).longValue();
                Field.ensureIso(temporalAccessor);
                LocalDate of = LocalDate.m906of(checkValidIntValue, 1, 4);
                if (resolverStyle == ResolverStyle.LENIENT) {
                    long longValue2 = l2.longValue();
                    if (longValue2 > 7) {
                        long j2 = longValue2 - 1;
                        of = of.plusWeeks(j2 / 7);
                        j = j2 % 7;
                    } else {
                        if (longValue2 < 1) {
                            of = of.plusWeeks(Math.subtractExact(longValue2, 7) / 7);
                            j = (longValue2 + 6) % 7;
                        }
                        localDate = of.plusWeeks(Math.subtractExact(longValue, 1)).with((TemporalField) ChronoField.DAY_OF_WEEK, longValue2);
                    }
                    longValue2 = j + 1;
                    localDate = of.plusWeeks(Math.subtractExact(longValue, 1)).with((TemporalField) ChronoField.DAY_OF_WEEK, longValue2);
                } else {
                    int checkValidIntValue2 = ChronoField.DAY_OF_WEEK.checkValidIntValue(l2.longValue());
                    if (longValue < 1 || longValue > 52) {
                        if (resolverStyle == ResolverStyle.STRICT) {
                            Field.getWeekRange(of).checkValidValue(longValue, this);
                        } else {
                            range().checkValidValue(longValue, this);
                        }
                    }
                    localDate = of.plusWeeks(longValue - 1).with((TemporalField) ChronoField.DAY_OF_WEEK, (long) checkValidIntValue2);
                }
                map.remove(this);
                map.remove(WEEK_BASED_YEAR);
                map.remove(ChronoField.DAY_OF_WEEK);
                return localDate;
            }
        },
        WEEK_BASED_YEAR((String) null) {
            public String toString() {
                return "WeekBasedYear";
            }

            public TemporalUnit getBaseUnit() {
                return IsoFields.WEEK_BASED_YEARS;
            }

            public TemporalUnit getRangeUnit() {
                return ChronoUnit.FOREVER;
            }

            public ValueRange range() {
                return ChronoField.YEAR.range();
            }

            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.EPOCH_DAY) && IsoFields.isIso(temporalAccessor);
            }

            public long getFrom(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return (long) Field.getWeekBasedYear(LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
            }

            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return super.rangeRefinedBy(temporalAccessor);
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
            }

            public <R extends Temporal> R adjustInto(R r, long j) {
                if (isSupportedBy(r)) {
                    int checkValidIntValue = range().checkValidIntValue(j, WEEK_BASED_YEAR);
                    LocalDate from = LocalDate.from(r);
                    int i = from.get(ChronoField.DAY_OF_WEEK);
                    int r4 = Field.getWeek(from);
                    if (r4 == 53 && Field.getWeekRange(checkValidIntValue) == 52) {
                        r4 = 52;
                    }
                    LocalDate of = LocalDate.m906of(checkValidIntValue, 1, 4);
                    return r.with(of.plusDays((long) ((i - of.get(ChronoField.DAY_OF_WEEK)) + ((r4 - 1) * 7))));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
            }
        };
        
        /* access modifiers changed from: private */
        public static final int[] QUARTER_DAYS = null;

        public boolean isDateBased() {
            return true;
        }

        public boolean isTimeBased() {
            return false;
        }

        static {
            QUARTER_DAYS = new int[]{0, 90, 181, UCharacter.UnicodeBlock.TANGUT_COMPONENTS_ID, 0, 91, 182, UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F_ID};
        }

        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            return range();
        }

        /* access modifiers changed from: private */
        public static void ensureIso(TemporalAccessor temporalAccessor) {
            if (!IsoFields.isIso(temporalAccessor)) {
                throw new DateTimeException("Resolve requires IsoChronology");
            }
        }

        /* access modifiers changed from: private */
        public static ValueRange getWeekRange(LocalDate localDate) {
            return ValueRange.m953of(1, (long) getWeekRange(getWeekBasedYear(localDate)));
        }

        /* access modifiers changed from: private */
        public static int getWeekRange(int i) {
            LocalDate of = LocalDate.m906of(i, 1, 1);
            if (of.getDayOfWeek() != DayOfWeek.THURSDAY) {
                return (of.getDayOfWeek() != DayOfWeek.WEDNESDAY || !of.isLeapYear()) ? 52 : 53;
            }
            return 53;
        }

        /* access modifiers changed from: private */
        public static int getWeek(LocalDate localDate) {
            int ordinal = localDate.getDayOfWeek().ordinal();
            int dayOfYear = localDate.getDayOfYear() - 1;
            int i = (3 - ordinal) + dayOfYear;
            int i2 = (i - ((i / 7) * 7)) - 3;
            if (i2 < -3) {
                i2 += 7;
            }
            if (dayOfYear < i2) {
                return (int) getWeekRange(localDate.withDayOfYear(180).minusYears(1)).getMaximum();
            }
            int i3 = ((dayOfYear - i2) / 7) + 1;
            if (i3 == 53) {
                if (!(i2 == -3 || (i2 == -2 && localDate.isLeapYear()))) {
                    return 1;
                }
            }
            return i3;
        }

        /* access modifiers changed from: private */
        public static int getWeekBasedYear(LocalDate localDate) {
            int year = localDate.getYear();
            int dayOfYear = localDate.getDayOfYear();
            if (dayOfYear <= 3) {
                if (dayOfYear - localDate.getDayOfWeek().ordinal() < -2) {
                    return year - 1;
                }
                return year;
            } else if (dayOfYear < 363) {
                return year;
            } else {
                return ((dayOfYear - 363) - (localDate.isLeapYear() ? 1 : 0)) - localDate.getDayOfWeek().ordinal() >= 0 ? year + 1 : year;
            }
        }
    }

    private enum Unit implements TemporalUnit {
        WEEK_BASED_YEARS("WeekBasedYears", Duration.ofSeconds(31556952)),
        QUARTER_YEARS("QuarterYears", Duration.ofSeconds(7889238));
        
        private final Duration duration;
        private final String name;

        public boolean isDateBased() {
            return true;
        }

        public boolean isDurationEstimated() {
            return true;
        }

        public boolean isTimeBased() {
            return false;
        }

        private Unit(String str, Duration duration2) {
            this.name = str;
            this.duration = duration2;
        }

        public Duration getDuration() {
            return this.duration;
        }

        public boolean isSupportedBy(Temporal temporal) {
            return temporal.isSupported(ChronoField.EPOCH_DAY) && IsoFields.isIso(temporal);
        }

        public <R extends Temporal> R addTo(R r, long j) {
            int i = C28911.$SwitchMap$java$time$temporal$IsoFields$Unit[ordinal()];
            if (i == 1) {
                return r.with(IsoFields.WEEK_BASED_YEAR, Math.addExact((long) r.get(IsoFields.WEEK_BASED_YEAR), j));
            }
            if (i == 2) {
                return r.plus(j / 4, ChronoUnit.YEARS).plus((j % 4) * 3, ChronoUnit.MONTHS);
            }
            throw new IllegalStateException("Unreachable");
        }

        public long between(Temporal temporal, Temporal temporal2) {
            if (temporal.getClass() != temporal2.getClass()) {
                return temporal.until(temporal2, this);
            }
            int i = C28911.$SwitchMap$java$time$temporal$IsoFields$Unit[ordinal()];
            if (i == 1) {
                return Math.subtractExact(temporal2.getLong(IsoFields.WEEK_BASED_YEAR), temporal.getLong(IsoFields.WEEK_BASED_YEAR));
            }
            if (i == 2) {
                return temporal.until(temporal2, ChronoUnit.MONTHS) / 3;
            }
            throw new IllegalStateException("Unreachable");
        }

        public String toString() {
            return this.name;
        }
    }

    /* renamed from: java.time.temporal.IsoFields$1 */
    static /* synthetic */ class C28911 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$IsoFields$Unit;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                java.time.temporal.IsoFields$Unit[] r0 = java.time.temporal.IsoFields.Unit.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$temporal$IsoFields$Unit = r0
                java.time.temporal.IsoFields$Unit r1 = java.time.temporal.IsoFields.Unit.WEEK_BASED_YEARS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$time$temporal$IsoFields$Unit     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.temporal.IsoFields$Unit r1 = java.time.temporal.IsoFields.Unit.QUARTER_YEARS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.temporal.IsoFields.C28911.<clinit>():void");
        }
    }

    static boolean isIso(TemporalAccessor temporalAccessor) {
        return Chronology.from(temporalAccessor).equals(IsoChronology.INSTANCE);
    }
}
