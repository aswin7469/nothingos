package java.time.temporal;

import android.icu.text.DateTimePatternGenerator;
import android.icu.util.Calendar;
import android.icu.util.ULocale;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class WeekFields implements Serializable {
    private static final ConcurrentMap<String, WeekFields> CACHE = new ConcurrentHashMap(4, 0.75f, 2);
    public static final WeekFields ISO = new WeekFields(DayOfWeek.MONDAY, 4);
    public static final WeekFields SUNDAY_START = m956of(DayOfWeek.SUNDAY, 1);
    public static final TemporalUnit WEEK_BASED_YEARS = IsoFields.WEEK_BASED_YEARS;
    private static final long serialVersionUID = -1177360819670808121L;
    /* access modifiers changed from: private */
    public final transient TemporalField dayOfWeek = ComputedDayOfField.ofDayOfWeekField(this);
    private final DayOfWeek firstDayOfWeek;
    private final int minimalDays;
    /* access modifiers changed from: private */
    public final transient TemporalField weekBasedYear = ComputedDayOfField.ofWeekBasedYearField(this);
    private final transient TemporalField weekOfMonth = ComputedDayOfField.ofWeekOfMonthField(this);
    /* access modifiers changed from: private */
    public final transient TemporalField weekOfWeekBasedYear = ComputedDayOfField.ofWeekOfWeekBasedYearField(this);
    private final transient TemporalField weekOfYear = ComputedDayOfField.ofWeekOfYearField(this);

    /* renamed from: of */
    public static WeekFields m957of(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        Calendar.WeekData weekData = Calendar.getInstance(locale).getWeekData();
        return m956of(DayOfWeek.SUNDAY.plus((long) (retrieveFirstDayOfWeek(locale, weekData) - 1)), weekData.minimalDaysInFirstWeek);
    }

    private static int retrieveFirstDayOfWeek(Locale locale, Calendar.WeekData weekData) {
        String unicodeLocaleType;
        if (locale.hasExtensions() && (unicodeLocaleType = locale.getUnicodeLocaleType("fw")) != null) {
            String lowerCase = unicodeLocaleType.toLowerCase(Locale.ROOT);
            lowerCase.hashCode();
            char c = 65535;
            switch (lowerCase.hashCode()) {
                case 101661:
                    if (lowerCase.equals("fri")) {
                        c = 0;
                        break;
                    }
                    break;
                case 108300:
                    if (lowerCase.equals("mon")) {
                        c = 1;
                        break;
                    }
                    break;
                case 113638:
                    if (lowerCase.equals("sat")) {
                        c = 2;
                        break;
                    }
                    break;
                case 114252:
                    if (lowerCase.equals("sun")) {
                        c = 3;
                        break;
                    }
                    break;
                case 114817:
                    if (lowerCase.equals("thu")) {
                        c = 4;
                        break;
                    }
                    break;
                case 115204:
                    if (lowerCase.equals("tue")) {
                        c = 5;
                        break;
                    }
                    break;
                case 117590:
                    if (lowerCase.equals("wed")) {
                        c = 6;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return 6;
                case 1:
                    return 2;
                case 2:
                    return 7;
                case 3:
                    return 1;
                case 4:
                    return 5;
                case 5:
                    return 3;
                case 6:
                    return 4;
            }
        }
        return weekData.firstDayOfWeek;
    }

    /* renamed from: of */
    public static WeekFields m956of(DayOfWeek dayOfWeek2, int i) {
        String str = dayOfWeek2.toString() + i;
        ConcurrentMap<String, WeekFields> concurrentMap = CACHE;
        WeekFields weekFields = concurrentMap.get(str);
        if (weekFields != null) {
            return weekFields;
        }
        concurrentMap.putIfAbsent(str, new WeekFields(dayOfWeek2, i));
        return concurrentMap.get(str);
    }

    private WeekFields(DayOfWeek dayOfWeek2, int i) {
        Objects.requireNonNull(dayOfWeek2, "firstDayOfWeek");
        if (i < 1 || i > 7) {
            throw new IllegalArgumentException("Minimal number of days is invalid");
        }
        this.firstDayOfWeek = dayOfWeek2;
        this.minimalDays = i;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, InvalidObjectException {
        objectInputStream.defaultReadObject();
        if (this.firstDayOfWeek != null) {
            int i = this.minimalDays;
            if (i < 1 || i > 7) {
                throw new InvalidObjectException("Minimal number of days is invalid");
            }
            return;
        }
        throw new InvalidObjectException("firstDayOfWeek is null");
    }

    private Object readResolve() throws InvalidObjectException {
        try {
            return m956of(this.firstDayOfWeek, this.minimalDays);
        } catch (IllegalArgumentException e) {
            throw new InvalidObjectException("Invalid serialized WeekFields: " + e.getMessage());
        }
    }

    public DayOfWeek getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public int getMinimalDaysInFirstWeek() {
        return this.minimalDays;
    }

    public TemporalField dayOfWeek() {
        return this.dayOfWeek;
    }

    public TemporalField weekOfMonth() {
        return this.weekOfMonth;
    }

    public TemporalField weekOfYear() {
        return this.weekOfYear;
    }

    public TemporalField weekOfWeekBasedYear() {
        return this.weekOfWeekBasedYear;
    }

    public TemporalField weekBasedYear() {
        return this.weekBasedYear;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WeekFields)) {
            return false;
        }
        if (hashCode() == obj.hashCode()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.firstDayOfWeek.ordinal() * 7) + this.minimalDays;
    }

    public String toString() {
        return "WeekFields[" + this.firstDayOfWeek + ',' + this.minimalDays + ']';
    }

    static class ComputedDayOfField implements TemporalField {
        private static final ValueRange DAY_OF_WEEK_RANGE = ValueRange.m953of(1, 7);
        private static final ValueRange WEEK_OF_MONTH_RANGE = ValueRange.m955of(0, 1, 4, 6);
        private static final ValueRange WEEK_OF_WEEK_BASED_YEAR_RANGE = ValueRange.m954of(1, 52, 53);
        private static final ValueRange WEEK_OF_YEAR_RANGE = ValueRange.m955of(0, 1, 52, 54);
        private final TemporalUnit baseUnit;
        private final String name;
        private final ValueRange range;
        private final TemporalUnit rangeUnit;
        private final WeekFields weekDef;

        public boolean isDateBased() {
            return true;
        }

        public boolean isTimeBased() {
            return false;
        }

        static ComputedDayOfField ofDayOfWeekField(WeekFields weekFields) {
            return new ComputedDayOfField("DayOfWeek", weekFields, ChronoUnit.DAYS, ChronoUnit.WEEKS, DAY_OF_WEEK_RANGE);
        }

        static ComputedDayOfField ofWeekOfMonthField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfMonth", weekFields, ChronoUnit.WEEKS, ChronoUnit.MONTHS, WEEK_OF_MONTH_RANGE);
        }

        static ComputedDayOfField ofWeekOfYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfYear", weekFields, ChronoUnit.WEEKS, ChronoUnit.YEARS, WEEK_OF_YEAR_RANGE);
        }

        static ComputedDayOfField ofWeekOfWeekBasedYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfWeekBasedYear", weekFields, ChronoUnit.WEEKS, IsoFields.WEEK_BASED_YEARS, WEEK_OF_WEEK_BASED_YEAR_RANGE);
        }

        static ComputedDayOfField ofWeekBasedYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekBasedYear", weekFields, IsoFields.WEEK_BASED_YEARS, ChronoUnit.FOREVER, ChronoField.YEAR.range());
        }

        private ChronoLocalDate ofWeekBasedYear(Chronology chronology, int i, int i2, int i3) {
            ChronoLocalDate date = chronology.date(i, 1, 1);
            int startOfWeekOffset = startOfWeekOffset(1, localizedDayOfWeek((TemporalAccessor) date));
            return date.plus((long) ((-startOfWeekOffset) + (i3 - 1) + ((Math.min(i2, computeWeek(startOfWeekOffset, date.lengthOfYear() + this.weekDef.getMinimalDaysInFirstWeek()) - 1) - 1) * 7)), (TemporalUnit) ChronoUnit.DAYS);
        }

        private ComputedDayOfField(String str, WeekFields weekFields, TemporalUnit temporalUnit, TemporalUnit temporalUnit2, ValueRange valueRange) {
            this.name = str;
            this.weekDef = weekFields;
            this.baseUnit = temporalUnit;
            this.rangeUnit = temporalUnit2;
            this.range = valueRange;
        }

        public long getFrom(TemporalAccessor temporalAccessor) {
            int localizedWeekBasedYear;
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                localizedWeekBasedYear = localizedDayOfWeek(temporalAccessor);
            } else if (this.rangeUnit == ChronoUnit.MONTHS) {
                return localizedWeekOfMonth(temporalAccessor);
            } else {
                if (this.rangeUnit == ChronoUnit.YEARS) {
                    return localizedWeekOfYear(temporalAccessor);
                }
                if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                    localizedWeekBasedYear = localizedWeekOfWeekBasedYear(temporalAccessor);
                } else if (this.rangeUnit == ChronoUnit.FOREVER) {
                    localizedWeekBasedYear = localizedWeekBasedYear(temporalAccessor);
                } else {
                    throw new IllegalStateException("unreachable, rangeUnit: " + this.rangeUnit + ", this: " + this);
                }
            }
            return (long) localizedWeekBasedYear;
        }

        private int localizedDayOfWeek(TemporalAccessor temporalAccessor) {
            return Math.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
        }

        private int localizedDayOfWeek(int i) {
            return Math.floorMod(i - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
        }

        private long localizedWeekOfMonth(TemporalAccessor temporalAccessor) {
            int localizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i = temporalAccessor.get(ChronoField.DAY_OF_MONTH);
            return (long) computeWeek(startOfWeekOffset(i, localizedDayOfWeek), i);
        }

        private long localizedWeekOfYear(TemporalAccessor temporalAccessor) {
            int localizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            return (long) computeWeek(startOfWeekOffset(i, localizedDayOfWeek), i);
        }

        private int localizedWeekBasedYear(TemporalAccessor temporalAccessor) {
            int localizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i = temporalAccessor.get(ChronoField.YEAR);
            int i2 = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            int startOfWeekOffset = startOfWeekOffset(i2, localizedDayOfWeek);
            int computeWeek = computeWeek(startOfWeekOffset, i2);
            if (computeWeek == 0) {
                return i - 1;
            }
            return computeWeek >= computeWeek(startOfWeekOffset, ((int) temporalAccessor.range(ChronoField.DAY_OF_YEAR).getMaximum()) + this.weekDef.getMinimalDaysInFirstWeek()) ? i + 1 : i;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:6:0x002c, code lost:
            r5 = computeWeek(r0, ((int) r6.range(java.time.temporal.ChronoField.DAY_OF_YEAR).getMaximum()) + r5.weekDef.getMinimalDaysInFirstWeek());
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int localizedWeekOfWeekBasedYear(java.time.temporal.TemporalAccessor r6) {
            /*
                r5 = this;
                int r0 = r5.localizedDayOfWeek((java.time.temporal.TemporalAccessor) r6)
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_YEAR
                int r1 = r6.get(r1)
                int r0 = r5.startOfWeekOffset(r1, r0)
                int r2 = r5.computeWeek(r0, r1)
                if (r2 != 0) goto L_0x0028
                java.time.chrono.Chronology r0 = java.time.chrono.Chronology.from(r6)
                java.time.chrono.ChronoLocalDate r6 = r0.date(r6)
                long r0 = (long) r1
                java.time.temporal.ChronoUnit r2 = java.time.temporal.ChronoUnit.DAYS
                java.time.chrono.ChronoLocalDate r6 = r6.minus((long) r0, (java.time.temporal.TemporalUnit) r2)
                int r5 = r5.localizedWeekOfWeekBasedYear(r6)
                return r5
            L_0x0028:
                r1 = 50
                if (r2 <= r1) goto L_0x0047
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_YEAR
                java.time.temporal.ValueRange r6 = r6.range(r1)
                long r3 = r6.getMaximum()
                int r6 = (int) r3
                java.time.temporal.WeekFields r1 = r5.weekDef
                int r1 = r1.getMinimalDaysInFirstWeek()
                int r6 = r6 + r1
                int r5 = r5.computeWeek(r0, r6)
                if (r2 < r5) goto L_0x0047
                int r2 = r2 - r5
                int r2 = r2 + 1
            L_0x0047:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.temporal.WeekFields.ComputedDayOfField.localizedWeekOfWeekBasedYear(java.time.temporal.TemporalAccessor):int");
        }

        private int startOfWeekOffset(int i, int i2) {
            int floorMod = Math.floorMod(i - i2, 7);
            return floorMod + 1 > this.weekDef.getMinimalDaysInFirstWeek() ? 7 - floorMod : -floorMod;
        }

        private int computeWeek(int i, int i2) {
            return ((i + 7) + (i2 - 1)) / 7;
        }

        public <R extends Temporal> R adjustInto(R r, long j) {
            int checkValidIntValue = this.range.checkValidIntValue(j, this);
            int i = r.get(this);
            if (checkValidIntValue == i) {
                return r;
            }
            if (this.rangeUnit != ChronoUnit.FOREVER) {
                return r.plus((long) (checkValidIntValue - i), this.baseUnit);
            }
            int i2 = r.get(this.weekDef.dayOfWeek);
            return ofWeekBasedYear(Chronology.from(r), (int) j, r.get(this.weekDef.weekOfWeekBasedYear), i2);
        }

        public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            long longValue = map.get(this).longValue();
            int intExact = Math.toIntExact(longValue);
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                int checkValidIntValue = this.range.checkValidIntValue(longValue, this);
                map.remove(this);
                map.put(ChronoField.DAY_OF_WEEK, Long.valueOf((long) (Math.floorMod((this.weekDef.getFirstDayOfWeek().getValue() - 1) + (checkValidIntValue - 1), 7) + 1)));
                return null;
            } else if (!map.containsKey(ChronoField.DAY_OF_WEEK)) {
                return null;
            } else {
                int localizedDayOfWeek = localizedDayOfWeek(ChronoField.DAY_OF_WEEK.checkValidIntValue(map.get(ChronoField.DAY_OF_WEEK).longValue()));
                Chronology from = Chronology.from(temporalAccessor);
                if (map.containsKey(ChronoField.YEAR)) {
                    int checkValidIntValue2 = ChronoField.YEAR.checkValidIntValue(map.get(ChronoField.YEAR).longValue());
                    if (this.rangeUnit == ChronoUnit.MONTHS && map.containsKey(ChronoField.MONTH_OF_YEAR)) {
                        return resolveWoM(map, from, checkValidIntValue2, map.get(ChronoField.MONTH_OF_YEAR).longValue(), (long) intExact, localizedDayOfWeek, resolverStyle);
                    } else if (this.rangeUnit == ChronoUnit.YEARS) {
                        return resolveWoY(map, from, checkValidIntValue2, (long) intExact, localizedDayOfWeek, resolverStyle);
                    }
                } else if ((this.rangeUnit == WeekFields.WEEK_BASED_YEARS || this.rangeUnit == ChronoUnit.FOREVER) && map.containsKey(this.weekDef.weekBasedYear) && map.containsKey(this.weekDef.weekOfWeekBasedYear)) {
                    return resolveWBY(map, from, localizedDayOfWeek, resolverStyle);
                }
                return null;
            }
        }

        private ChronoLocalDate resolveWoM(Map<TemporalField, Long> map, Chronology chronology, int i, long j, long j2, int i2, ResolverStyle resolverStyle) {
            ChronoLocalDate chronoLocalDate;
            if (resolverStyle == ResolverStyle.LENIENT) {
                ChronoLocalDate plus = chronology.date(i, 1, 1).plus(Math.subtractExact(j, 1), (TemporalUnit) ChronoUnit.MONTHS);
                chronoLocalDate = plus.plus(Math.addExact(Math.multiplyExact(Math.subtractExact(j2, localizedWeekOfMonth(plus)), 7), (long) (i2 - localizedDayOfWeek((TemporalAccessor) plus))), (TemporalUnit) ChronoUnit.DAYS);
            } else {
                ChronoLocalDate date = chronology.date(i, ChronoField.MONTH_OF_YEAR.checkValidIntValue(j), 1);
                chronoLocalDate = date.plus((long) ((((int) (((long) this.range.checkValidIntValue(j2, this)) - localizedWeekOfMonth(date))) * 7) + (i2 - localizedDayOfWeek((TemporalAccessor) date))), (TemporalUnit) ChronoUnit.DAYS);
                if (resolverStyle == ResolverStyle.STRICT && chronoLocalDate.getLong(ChronoField.MONTH_OF_YEAR) != j) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different month");
                }
            }
            map.remove(this);
            map.remove(ChronoField.YEAR);
            map.remove(ChronoField.MONTH_OF_YEAR);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDate;
        }

        private ChronoLocalDate resolveWoY(Map<TemporalField, Long> map, Chronology chronology, int i, long j, int i2, ResolverStyle resolverStyle) {
            ChronoLocalDate chronoLocalDate;
            ChronoLocalDate date = chronology.date(i, 1, 1);
            if (resolverStyle == ResolverStyle.LENIENT) {
                chronoLocalDate = date.plus(Math.addExact(Math.multiplyExact(Math.subtractExact(j, localizedWeekOfYear(date)), 7), (long) (i2 - localizedDayOfWeek((TemporalAccessor) date))), (TemporalUnit) ChronoUnit.DAYS);
            } else {
                chronoLocalDate = date.plus((long) ((((int) (((long) this.range.checkValidIntValue(j, this)) - localizedWeekOfYear(date))) * 7) + (i2 - localizedDayOfWeek((TemporalAccessor) date))), (TemporalUnit) ChronoUnit.DAYS);
                if (resolverStyle == ResolverStyle.STRICT && chronoLocalDate.getLong(ChronoField.YEAR) != ((long) i)) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different year");
                }
            }
            map.remove(this);
            map.remove(ChronoField.YEAR);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDate;
        }

        private ChronoLocalDate resolveWBY(Map<TemporalField, Long> map, Chronology chronology, int i, ResolverStyle resolverStyle) {
            ChronoLocalDate chronoLocalDate;
            int checkValidIntValue = this.weekDef.weekBasedYear.range().checkValidIntValue(map.get(this.weekDef.weekBasedYear).longValue(), this.weekDef.weekBasedYear);
            if (resolverStyle == ResolverStyle.LENIENT) {
                chronoLocalDate = ofWeekBasedYear(chronology, checkValidIntValue, 1, i).plus(Math.subtractExact(map.get(this.weekDef.weekOfWeekBasedYear).longValue(), 1), (TemporalUnit) ChronoUnit.WEEKS);
            } else {
                chronoLocalDate = ofWeekBasedYear(chronology, checkValidIntValue, this.weekDef.weekOfWeekBasedYear.range().checkValidIntValue(map.get(this.weekDef.weekOfWeekBasedYear).longValue(), this.weekDef.weekOfWeekBasedYear), i);
                if (resolverStyle == ResolverStyle.STRICT && localizedWeekBasedYear(chronoLocalDate) != checkValidIntValue) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different week-based-year");
                }
            }
            map.remove(this);
            map.remove(this.weekDef.weekBasedYear);
            map.remove(this.weekDef.weekOfWeekBasedYear);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDate;
        }

        public String getDisplayName(Locale locale) {
            Objects.requireNonNull(locale, "locale");
            if (this.rangeUnit != ChronoUnit.YEARS) {
                return this.name;
            }
            String appendItemName = DateTimePatternGenerator.getInstance(ULocale.forLocale(locale)).getAppendItemName(4);
            return (appendItemName == null || appendItemName.isEmpty()) ? this.name : appendItemName;
        }

        public TemporalUnit getBaseUnit() {
            return this.baseUnit;
        }

        public TemporalUnit getRangeUnit() {
            return this.rangeUnit;
        }

        public ValueRange range() {
            return this.range;
        }

        public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
            if (!temporalAccessor.isSupported(ChronoField.DAY_OF_WEEK)) {
                return false;
            }
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                return true;
            }
            if (this.rangeUnit == ChronoUnit.MONTHS) {
                return temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH);
            }
            if (this.rangeUnit == ChronoUnit.YEARS) {
                return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR);
            }
            if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR);
            }
            if (this.rangeUnit == ChronoUnit.FOREVER) {
                return temporalAccessor.isSupported(ChronoField.YEAR);
            }
            return false;
        }

        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                return this.range;
            }
            if (this.rangeUnit == ChronoUnit.MONTHS) {
                return rangeByWeek(temporalAccessor, ChronoField.DAY_OF_MONTH);
            }
            if (this.rangeUnit == ChronoUnit.YEARS) {
                return rangeByWeek(temporalAccessor, ChronoField.DAY_OF_YEAR);
            }
            if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                return rangeWeekOfWeekBasedYear(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.FOREVER) {
                return ChronoField.YEAR.range();
            }
            throw new IllegalStateException("unreachable, rangeUnit: " + this.rangeUnit + ", this: " + this);
        }

        private ValueRange rangeByWeek(TemporalAccessor temporalAccessor, TemporalField temporalField) {
            int startOfWeekOffset = startOfWeekOffset(temporalAccessor.get(temporalField), localizedDayOfWeek(temporalAccessor));
            ValueRange range2 = temporalAccessor.range(temporalField);
            return ValueRange.m953of((long) computeWeek(startOfWeekOffset, (int) range2.getMinimum()), (long) computeWeek(startOfWeekOffset, (int) range2.getMaximum()));
        }

        private ValueRange rangeWeekOfWeekBasedYear(TemporalAccessor temporalAccessor) {
            if (!temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR)) {
                return WEEK_OF_YEAR_RANGE;
            }
            int localizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            int startOfWeekOffset = startOfWeekOffset(i, localizedDayOfWeek);
            int computeWeek = computeWeek(startOfWeekOffset, i);
            if (computeWeek == 0) {
                return rangeWeekOfWeekBasedYear(Chronology.from(temporalAccessor).date(temporalAccessor).minus((long) (i + 7), (TemporalUnit) ChronoUnit.DAYS));
            }
            int maximum = (int) temporalAccessor.range(ChronoField.DAY_OF_YEAR).getMaximum();
            int computeWeek2 = computeWeek(startOfWeekOffset, this.weekDef.getMinimalDaysInFirstWeek() + maximum);
            if (computeWeek >= computeWeek2) {
                return rangeWeekOfWeekBasedYear(Chronology.from(temporalAccessor).date(temporalAccessor).plus((long) ((maximum - i) + 1 + 7), (TemporalUnit) ChronoUnit.DAYS));
            }
            return ValueRange.m953of(1, (long) (computeWeek2 - 1));
        }

        public String toString() {
            return this.name + NavigationBarInflaterView.SIZE_MOD_START + this.weekDef.toString() + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }
}
