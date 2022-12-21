package java.time.chrono;

import android.net.wifi.WifiEnterpriseConfig;
import androidx.exifinterface.media.ExifInterface;
import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectStreamException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import sun.util.logging.PlatformLogger;

public abstract class AbstractChronology implements Chronology {
    private static final ConcurrentHashMap<String, Chronology> CHRONOS_BY_ID = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Chronology> CHRONOS_BY_TYPE = new ConcurrentHashMap<>();

    static Chronology registerChrono(Chronology chronology) {
        return registerChrono(chronology, chronology.getId());
    }

    static Chronology registerChrono(Chronology chronology, String str) {
        String calendarType;
        Chronology putIfAbsent = CHRONOS_BY_ID.putIfAbsent(str, chronology);
        if (putIfAbsent == null && (calendarType = chronology.getCalendarType()) != null) {
            CHRONOS_BY_TYPE.putIfAbsent(calendarType, chronology);
        }
        return putIfAbsent;
    }

    private static boolean initCache() {
        if (CHRONOS_BY_ID.get(ExifInterface.TAG_RW2_ISO) != null) {
            return false;
        }
        registerChrono(HijrahChronology.INSTANCE);
        registerChrono(JapaneseChronology.INSTANCE);
        registerChrono(MinguoChronology.INSTANCE);
        registerChrono(ThaiBuddhistChronology.INSTANCE);
        Iterator<S> it = ServiceLoader.load(AbstractChronology.class, (ClassLoader) null).iterator();
        while (it.hasNext()) {
            AbstractChronology abstractChronology = (AbstractChronology) it.next();
            String id = abstractChronology.getId();
            if (id.equals(ExifInterface.TAG_RW2_ISO) || registerChrono(abstractChronology) != null) {
                PlatformLogger logger = PlatformLogger.getLogger("java.time.chrono");
                logger.warning("Ignoring duplicate Chronology, from ServiceLoader configuration " + id);
            }
        }
        registerChrono(IsoChronology.INSTANCE);
        return true;
    }

    static Chronology ofLocale(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        String unicodeLocaleType = locale.getUnicodeLocaleType("ca");
        if (unicodeLocaleType == null || "iso".equals(unicodeLocaleType) || "iso8601".equals(unicodeLocaleType)) {
            return IsoChronology.INSTANCE;
        }
        do {
            Chronology chronology = CHRONOS_BY_TYPE.get(unicodeLocaleType);
            if (chronology != null) {
                return chronology;
            }
        } while (initCache());
        Iterator<S> it = ServiceLoader.load(Chronology.class).iterator();
        while (it.hasNext()) {
            Chronology chronology2 = (Chronology) it.next();
            if (unicodeLocaleType.equals(chronology2.getCalendarType())) {
                return chronology2;
            }
        }
        throw new DateTimeException("Unknown calendar system: " + unicodeLocaleType);
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0022  */
    /* renamed from: of */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.time.chrono.Chronology m939of(java.lang.String r3) {
        /*
            java.lang.String r0 = "id"
            java.util.Objects.requireNonNull(r3, (java.lang.String) r0)
        L_0x0005:
            java.time.chrono.Chronology r0 = of0(r3)
            if (r0 == 0) goto L_0x000c
            return r0
        L_0x000c:
            boolean r0 = initCache()
            if (r0 != 0) goto L_0x0005
            java.lang.Class<java.time.chrono.Chronology> r0 = java.time.chrono.Chronology.class
            java.util.ServiceLoader r0 = java.util.ServiceLoader.load(r0)
            java.util.Iterator r0 = r0.iterator()
        L_0x001c:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x003d
            java.lang.Object r1 = r0.next()
            java.time.chrono.Chronology r1 = (java.time.chrono.Chronology) r1
            java.lang.String r2 = r1.getId()
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L_0x003c
            java.lang.String r2 = r1.getCalendarType()
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L_0x001c
        L_0x003c:
            return r1
        L_0x003d:
            java.time.DateTimeException r0 = new java.time.DateTimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Unknown chronology: "
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r3)
            java.lang.String r3 = r1.toString()
            r0.<init>(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.AbstractChronology.m939of(java.lang.String):java.time.chrono.Chronology");
    }

    private static Chronology of0(String str) {
        Chronology chronology = CHRONOS_BY_ID.get(str);
        return chronology == null ? CHRONOS_BY_TYPE.get(str) : chronology;
    }

    static Set<Chronology> getAvailableChronologies() {
        initCache();
        HashSet hashSet = new HashSet(CHRONOS_BY_ID.values());
        Iterator<S> it = ServiceLoader.load(Chronology.class).iterator();
        while (it.hasNext()) {
            hashSet.add((Chronology) it.next());
        }
        return hashSet;
    }

    protected AbstractChronology() {
    }

    public ChronoLocalDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        if (map.containsKey(ChronoField.EPOCH_DAY)) {
            return dateEpochDay(map.remove(ChronoField.EPOCH_DAY).longValue());
        }
        resolveProlepticMonth(map, resolverStyle);
        ChronoLocalDate resolveYearOfEra = resolveYearOfEra(map, resolverStyle);
        if (resolveYearOfEra != null) {
            return resolveYearOfEra;
        }
        if (!map.containsKey(ChronoField.YEAR)) {
            return null;
        }
        if (map.containsKey(ChronoField.MONTH_OF_YEAR)) {
            if (map.containsKey(ChronoField.DAY_OF_MONTH)) {
                return resolveYMD(map, resolverStyle);
            }
            if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_MONTH)) {
                if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)) {
                    return resolveYMAA(map, resolverStyle);
                }
                if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                    return resolveYMAD(map, resolverStyle);
                }
            }
        }
        if (map.containsKey(ChronoField.DAY_OF_YEAR)) {
            return resolveYD(map, resolverStyle);
        }
        if (!map.containsKey(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
            return null;
        }
        if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)) {
            return resolveYAA(map, resolverStyle);
        }
        if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
            return resolveYAD(map, resolverStyle);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void resolveProlepticMonth(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        Long remove = map.remove(ChronoField.PROLEPTIC_MONTH);
        if (remove != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.PROLEPTIC_MONTH.checkValidValue(remove.longValue());
            }
            ChronoLocalDate with = dateNow().with((TemporalField) ChronoField.DAY_OF_MONTH, 1).with((TemporalField) ChronoField.PROLEPTIC_MONTH, remove.longValue());
            addFieldValue(map, ChronoField.MONTH_OF_YEAR, (long) with.get(ChronoField.MONTH_OF_YEAR));
            addFieldValue(map, ChronoField.YEAR, (long) with.get(ChronoField.YEAR));
        }
    }

    /* access modifiers changed from: package-private */
    public ChronoLocalDate resolveYearOfEra(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int i;
        Long remove = map.remove(ChronoField.YEAR_OF_ERA);
        if (remove != null) {
            Long remove2 = map.remove(ChronoField.ERA);
            if (resolverStyle != ResolverStyle.LENIENT) {
                i = range(ChronoField.YEAR_OF_ERA).checkValidIntValue(remove.longValue(), ChronoField.YEAR_OF_ERA);
            } else {
                i = Math.toIntExact(remove.longValue());
            }
            if (remove2 != null) {
                addFieldValue(map, ChronoField.YEAR, (long) prolepticYear(eraOf(range(ChronoField.ERA).checkValidIntValue(remove2.longValue(), ChronoField.ERA)), i));
                return null;
            } else if (map.containsKey(ChronoField.YEAR)) {
                addFieldValue(map, ChronoField.YEAR, (long) prolepticYear(dateYearDay(range(ChronoField.YEAR).checkValidIntValue(map.get(ChronoField.YEAR).longValue(), ChronoField.YEAR), 1).getEra(), i));
                return null;
            } else if (resolverStyle == ResolverStyle.STRICT) {
                map.put(ChronoField.YEAR_OF_ERA, remove);
                return null;
            } else {
                List<Era> eras = eras();
                if (eras.isEmpty()) {
                    addFieldValue(map, ChronoField.YEAR, (long) i);
                    return null;
                }
                addFieldValue(map, ChronoField.YEAR, (long) prolepticYear(eras.get(eras.size() - 1), i));
                return null;
            }
        } else if (!map.containsKey(ChronoField.ERA)) {
            return null;
        } else {
            range(ChronoField.ERA).checkValidValue(map.get(ChronoField.ERA).longValue(), ChronoField.ERA);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public ChronoLocalDate resolveYMD(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int checkValidIntValue = range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR).longValue(), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            long subtractExact = Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), 1);
            return date(checkValidIntValue, 1, 1).plus(subtractExact, (TemporalUnit) ChronoUnit.MONTHS).plus(Math.subtractExact(map.remove(ChronoField.DAY_OF_MONTH).longValue(), 1), (TemporalUnit) ChronoUnit.DAYS);
        }
        int checkValidIntValue2 = range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), ChronoField.MONTH_OF_YEAR);
        int checkValidIntValue3 = range(ChronoField.DAY_OF_MONTH).checkValidIntValue(map.remove(ChronoField.DAY_OF_MONTH).longValue(), ChronoField.DAY_OF_MONTH);
        if (resolverStyle != ResolverStyle.SMART) {
            return date(checkValidIntValue, checkValidIntValue2, checkValidIntValue3);
        }
        try {
            return date(checkValidIntValue, checkValidIntValue2, checkValidIntValue3);
        } catch (DateTimeException unused) {
            return date(checkValidIntValue, checkValidIntValue2, 1).with(TemporalAdjusters.lastDayOfMonth());
        }
    }

    /* access modifiers changed from: package-private */
    public ChronoLocalDate resolveYD(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int checkValidIntValue = range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR).longValue(), ChronoField.YEAR);
        if (resolverStyle != ResolverStyle.LENIENT) {
            return dateYearDay(checkValidIntValue, range(ChronoField.DAY_OF_YEAR).checkValidIntValue(map.remove(ChronoField.DAY_OF_YEAR).longValue(), ChronoField.DAY_OF_YEAR));
        }
        return dateYearDay(checkValidIntValue, 1).plus(Math.subtractExact(map.remove(ChronoField.DAY_OF_YEAR).longValue(), 1), (TemporalUnit) ChronoUnit.DAYS);
    }

    /* access modifiers changed from: package-private */
    public ChronoLocalDate resolveYMAA(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int checkValidIntValue = range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR).longValue(), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            long subtractExact = Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), 1);
            long subtractExact2 = Math.subtractExact(map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH).longValue(), 1);
            return date(checkValidIntValue, 1, 1).plus(subtractExact, (TemporalUnit) ChronoUnit.MONTHS).plus(subtractExact2, (TemporalUnit) ChronoUnit.WEEKS).plus(Math.subtractExact(map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH).longValue(), 1), (TemporalUnit) ChronoUnit.DAYS);
        }
        int checkValidIntValue2 = range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), ChronoField.MONTH_OF_YEAR);
        ChronoLocalDate plus = date(checkValidIntValue, checkValidIntValue2, 1).plus((long) (((range(ChronoField.ALIGNED_WEEK_OF_MONTH).checkValidIntValue(map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH).longValue(), ChronoField.ALIGNED_WEEK_OF_MONTH) - 1) * 7) + (range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH).checkValidIntValue(map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH).longValue(), ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH) - 1)), (TemporalUnit) ChronoUnit.DAYS);
        if (resolverStyle != ResolverStyle.STRICT || plus.get(ChronoField.MONTH_OF_YEAR) == checkValidIntValue2) {
            return plus;
        }
        throw new DateTimeException("Strict mode rejected resolved date as it is in a different month");
    }

    /* access modifiers changed from: package-private */
    public ChronoLocalDate resolveYMAD(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int checkValidIntValue = range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR).longValue(), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return resolveAligned(date(checkValidIntValue, 1, 1), Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), 1), Math.subtractExact(map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH).longValue(), 1), Math.subtractExact(map.remove(ChronoField.DAY_OF_WEEK).longValue(), 1));
        }
        int checkValidIntValue2 = range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), ChronoField.MONTH_OF_YEAR);
        ChronoLocalDate with = date(checkValidIntValue, checkValidIntValue2, 1).plus((long) ((range(ChronoField.ALIGNED_WEEK_OF_MONTH).checkValidIntValue(map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH).longValue(), ChronoField.ALIGNED_WEEK_OF_MONTH) - 1) * 7), (TemporalUnit) ChronoUnit.DAYS).with(TemporalAdjusters.nextOrSame(DayOfWeek.m906of(range(ChronoField.DAY_OF_WEEK).checkValidIntValue(map.remove(ChronoField.DAY_OF_WEEK).longValue(), ChronoField.DAY_OF_WEEK))));
        if (resolverStyle != ResolverStyle.STRICT || with.get(ChronoField.MONTH_OF_YEAR) == checkValidIntValue2) {
            return with;
        }
        throw new DateTimeException("Strict mode rejected resolved date as it is in a different month");
    }

    /* access modifiers changed from: package-private */
    public ChronoLocalDate resolveYAA(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int checkValidIntValue = range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR).longValue(), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            long subtractExact = Math.subtractExact(map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR).longValue(), 1);
            return dateYearDay(checkValidIntValue, 1).plus(subtractExact, (TemporalUnit) ChronoUnit.WEEKS).plus(Math.subtractExact(map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR).longValue(), 1), (TemporalUnit) ChronoUnit.DAYS);
        }
        ChronoLocalDate plus = dateYearDay(checkValidIntValue, 1).plus((long) (((range(ChronoField.ALIGNED_WEEK_OF_YEAR).checkValidIntValue(map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR).longValue(), ChronoField.ALIGNED_WEEK_OF_YEAR) - 1) * 7) + (range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR).checkValidIntValue(map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR).longValue(), ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR) - 1)), (TemporalUnit) ChronoUnit.DAYS);
        if (resolverStyle != ResolverStyle.STRICT || plus.get(ChronoField.YEAR) == checkValidIntValue) {
            return plus;
        }
        throw new DateTimeException("Strict mode rejected resolved date as it is in a different year");
    }

    /* access modifiers changed from: package-private */
    public ChronoLocalDate resolveYAD(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int checkValidIntValue = range(ChronoField.YEAR).checkValidIntValue(map.remove(ChronoField.YEAR).longValue(), ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return resolveAligned(dateYearDay(checkValidIntValue, 1), 0, Math.subtractExact(map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR).longValue(), 1), Math.subtractExact(map.remove(ChronoField.DAY_OF_WEEK).longValue(), 1));
        }
        ChronoLocalDate with = dateYearDay(checkValidIntValue, 1).plus((long) ((range(ChronoField.ALIGNED_WEEK_OF_YEAR).checkValidIntValue(map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR).longValue(), ChronoField.ALIGNED_WEEK_OF_YEAR) - 1) * 7), (TemporalUnit) ChronoUnit.DAYS).with(TemporalAdjusters.nextOrSame(DayOfWeek.m906of(range(ChronoField.DAY_OF_WEEK).checkValidIntValue(map.remove(ChronoField.DAY_OF_WEEK).longValue(), ChronoField.DAY_OF_WEEK))));
        if (resolverStyle != ResolverStyle.STRICT || with.get(ChronoField.YEAR) == checkValidIntValue) {
            return with;
        }
        throw new DateTimeException("Strict mode rejected resolved date as it is in a different year");
    }

    /* access modifiers changed from: package-private */
    public ChronoLocalDate resolveAligned(ChronoLocalDate chronoLocalDate, long j, long j2, long j3) {
        long j4;
        ChronoLocalDate plus = chronoLocalDate.plus(j, (TemporalUnit) ChronoUnit.MONTHS).plus(j2, (TemporalUnit) ChronoUnit.WEEKS);
        if (j3 > 7) {
            long j5 = j3 - 1;
            plus = plus.plus(j5 / 7, (TemporalUnit) ChronoUnit.WEEKS);
            j4 = j5 % 7;
        } else {
            if (j3 < 1) {
                plus = plus.plus(Math.subtractExact(j3, 7) / 7, (TemporalUnit) ChronoUnit.WEEKS);
                j4 = (j3 + 6) % 7;
            }
            return plus.with(TemporalAdjusters.nextOrSame(DayOfWeek.m906of((int) j3)));
        }
        j3 = j4 + 1;
        return plus.with(TemporalAdjusters.nextOrSame(DayOfWeek.m906of((int) j3)));
    }

    /* access modifiers changed from: package-private */
    public void addFieldValue(Map<TemporalField, Long> map, ChronoField chronoField, long j) {
        Long l = map.get(chronoField);
        if (l == null || l.longValue() == j) {
            map.put(chronoField, Long.valueOf(j));
            return;
        }
        throw new DateTimeException("Conflict found: " + chronoField + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + l + " differs from " + chronoField + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + j);
    }

    public int compareTo(Chronology chronology) {
        return getId().compareTo(chronology.getId());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractChronology)) {
            return false;
        }
        if (compareTo((Chronology) (AbstractChronology) obj) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getId().hashCode() ^ getClass().hashCode();
    }

    public String toString() {
        return getId();
    }

    /* access modifiers changed from: package-private */
    public Object writeReplace() {
        return new Ser((byte) 1, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(getId());
    }

    static Chronology readExternal(DataInput dataInput) throws IOException {
        return Chronology.m941of(dataInput.readUTF());
    }
}
