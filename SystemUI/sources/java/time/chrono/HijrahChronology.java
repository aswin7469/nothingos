package java.time.chrono;

import java.p026io.InputStream;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class HijrahChronology extends AbstractChronology implements Serializable {
    public static final HijrahChronology INSTANCE;
    private static final String KEY_ID = "id";
    private static final String KEY_ISO_START = "iso-start";
    private static final String KEY_TYPE = "type";
    private static final String KEY_VERSION = "version";
    private static final String RESOURCE_PREFIX = "hijrah-config-";
    private static final String RESOURCE_SUFFIX = ".properties";
    private static final long serialVersionUID = 3127340209035924785L;
    private final transient String calendarType;
    private transient int[] hijrahEpochMonthStartDays;
    private transient int hijrahStartEpochMonth;
    private volatile transient boolean initComplete;
    private transient int maxEpochDay;
    private transient int maxMonthLength;
    private transient int maxYearLength;
    private transient int minEpochDay;
    private transient int minMonthLength;
    private transient int minYearLength;
    private final transient String typeId;

    static {
        HijrahChronology hijrahChronology = new HijrahChronology("Hijrah-umalqura", "islamic-umalqura");
        INSTANCE = hijrahChronology;
        AbstractChronology.registerChrono(hijrahChronology, "Hijrah");
        AbstractChronology.registerChrono(hijrahChronology, "islamic");
    }

    private HijrahChronology(String str, String str2) {
        if (str.isEmpty()) {
            throw new IllegalArgumentException("calendar id is empty");
        } else if (!str2.isEmpty()) {
            this.typeId = str;
            this.calendarType = str2;
        } else {
            throw new IllegalArgumentException("calendar typeId is empty");
        }
    }

    private void checkCalendarInit() {
        if (!this.initComplete) {
            loadCalendarData();
            this.initComplete = true;
        }
    }

    public String getId() {
        return this.typeId;
    }

    public String getCalendarType() {
        return this.calendarType;
    }

    public HijrahDate date(Era era, int i, int i2, int i3) {
        return date(prolepticYear(era, i), i2, i3);
    }

    public HijrahDate date(int i, int i2, int i3) {
        return HijrahDate.m943of(this, i, i2, i3);
    }

    public HijrahDate dateYearDay(Era era, int i, int i2) {
        return dateYearDay(prolepticYear(era, i), i2);
    }

    public HijrahDate dateYearDay(int i, int i2) {
        HijrahDate of = HijrahDate.m943of(this, i, 1, 1);
        if (i2 <= of.lengthOfYear()) {
            return of.plusDays((long) (i2 - 1));
        }
        throw new DateTimeException("Invalid dayOfYear: " + i2);
    }

    public HijrahDate dateEpochDay(long j) {
        return HijrahDate.ofEpochDay(this, j);
    }

    public HijrahDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    public HijrahDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    public HijrahDate dateNow(Clock clock) {
        return date((TemporalAccessor) LocalDate.now(clock));
    }

    public HijrahDate date(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof HijrahDate) {
            return (HijrahDate) temporalAccessor;
        }
        return HijrahDate.ofEpochDay(this, temporalAccessor.getLong(ChronoField.EPOCH_DAY));
    }

    public ChronoLocalDateTime<HijrahDate> localDateTime(TemporalAccessor temporalAccessor) {
        return super.localDateTime(temporalAccessor);
    }

    public ChronoZonedDateTime<HijrahDate> zonedDateTime(TemporalAccessor temporalAccessor) {
        return super.zonedDateTime(temporalAccessor);
    }

    public ChronoZonedDateTime<HijrahDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return super.zonedDateTime(instant, zoneId);
    }

    public boolean isLeapYear(long j) {
        checkCalendarInit();
        if (j < ((long) getMinimumYear()) || j > ((long) getMaximumYear()) || getYearLength((int) j) <= 354) {
            return false;
        }
        return true;
    }

    public int prolepticYear(Era era, int i) {
        if (era instanceof HijrahEra) {
            return i;
        }
        throw new ClassCastException("Era must be HijrahEra");
    }

    public HijrahEra eraOf(int i) {
        if (i == 1) {
            return HijrahEra.AH;
        }
        throw new DateTimeException("invalid Hijrah era");
    }

    public List<Era> eras() {
        return List.m1733of((E[]) HijrahEra.values());
    }

    public ValueRange range(ChronoField chronoField) {
        checkCalendarInit();
        if (!(chronoField instanceof ChronoField)) {
            return chronoField.range();
        }
        switch (C28671.$SwitchMap$java$time$temporal$ChronoField[chronoField.ordinal()]) {
            case 1:
                return ValueRange.m957of(1, 1, (long) getMinimumMonthLength(), (long) getMaximumMonthLength());
            case 2:
                return ValueRange.m955of(1, (long) getMaximumDayOfYear());
            case 3:
                return ValueRange.m955of(1, 5);
            case 4:
            case 5:
                return ValueRange.m955of((long) getMinimumYear(), (long) getMaximumYear());
            case 6:
                return ValueRange.m955of(1, 1);
            default:
                return chronoField.range();
        }
    }

    /* renamed from: java.time.chrono.HijrahChronology$1 */
    static /* synthetic */ class C28671 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoField;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
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
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x003e }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR_OF_ERA     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ERA     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.HijrahChronology.C28671.<clinit>():void");
        }
    }

    public HijrahDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        return (HijrahDate) super.resolveDate(map, resolverStyle);
    }

    /* access modifiers changed from: package-private */
    public int checkValidYear(long j) {
        if (j >= ((long) getMinimumYear()) && j <= ((long) getMaximumYear())) {
            return (int) j;
        }
        throw new DateTimeException("Invalid Hijrah year: " + j);
    }

    /* access modifiers changed from: package-private */
    public void checkValidDayOfYear(int i) {
        if (i < 1 || i > getMaximumDayOfYear()) {
            throw new DateTimeException("Invalid Hijrah day of year: " + i);
        }
    }

    /* access modifiers changed from: package-private */
    public void checkValidMonth(int i) {
        if (i < 1 || i > 12) {
            throw new DateTimeException("Invalid Hijrah month: " + i);
        }
    }

    /* access modifiers changed from: package-private */
    public int[] getHijrahDateInfo(int i) {
        checkCalendarInit();
        if (i < this.minEpochDay || i >= this.maxEpochDay) {
            throw new DateTimeException("Hijrah date out of range");
        }
        int epochDayToEpochMonth = epochDayToEpochMonth(i);
        return new int[]{epochMonthToYear(epochDayToEpochMonth), epochMonthToMonth(epochDayToEpochMonth) + 1, (i - epochMonthToEpochDay(epochDayToEpochMonth)) + 1};
    }

    /* access modifiers changed from: package-private */
    public long getEpochDay(int i, int i2, int i3) {
        checkCalendarInit();
        checkValidMonth(i2);
        int yearToEpochMonth = yearToEpochMonth(i) + (i2 - 1);
        if (yearToEpochMonth < 0 || yearToEpochMonth >= this.hijrahEpochMonthStartDays.length) {
            throw new DateTimeException("Invalid Hijrah date, year: " + i + ", month: " + i2);
        } else if (i3 >= 1 && i3 <= getMonthLength(i, i2)) {
            return (long) (epochMonthToEpochDay(yearToEpochMonth) + (i3 - 1));
        } else {
            throw new DateTimeException("Invalid Hijrah day of month: " + i3);
        }
    }

    /* access modifiers changed from: package-private */
    public int getDayOfYear(int i, int i2) {
        return yearMonthToDayOfYear(i, i2 - 1);
    }

    /* access modifiers changed from: package-private */
    public int getMonthLength(int i, int i2) {
        int yearToEpochMonth = yearToEpochMonth(i) + (i2 - 1);
        if (yearToEpochMonth >= 0 && yearToEpochMonth < this.hijrahEpochMonthStartDays.length) {
            return epochMonthLength(yearToEpochMonth);
        }
        throw new DateTimeException("Invalid Hijrah date, year: " + i + ", month: " + i2);
    }

    /* access modifiers changed from: package-private */
    public int getYearLength(int i) {
        return yearMonthToDayOfYear(i, 12);
    }

    /* access modifiers changed from: package-private */
    public int getMinimumYear() {
        return epochMonthToYear(0);
    }

    /* access modifiers changed from: package-private */
    public int getMaximumYear() {
        return epochMonthToYear(this.hijrahEpochMonthStartDays.length - 1) - 1;
    }

    /* access modifiers changed from: package-private */
    public int getMaximumMonthLength() {
        return this.maxMonthLength;
    }

    /* access modifiers changed from: package-private */
    public int getMinimumMonthLength() {
        return this.minMonthLength;
    }

    /* access modifiers changed from: package-private */
    public int getMaximumDayOfYear() {
        return this.maxYearLength;
    }

    /* access modifiers changed from: package-private */
    public int getSmallestMaximumDayOfYear() {
        return this.minYearLength;
    }

    private int epochDayToEpochMonth(int i) {
        int binarySearch = Arrays.binarySearch(this.hijrahEpochMonthStartDays, i);
        return binarySearch < 0 ? (-binarySearch) - 2 : binarySearch;
    }

    private int epochMonthToYear(int i) {
        return (i + this.hijrahStartEpochMonth) / 12;
    }

    private int yearToEpochMonth(int i) {
        return (i * 12) - this.hijrahStartEpochMonth;
    }

    private int epochMonthToMonth(int i) {
        return (i + this.hijrahStartEpochMonth) % 12;
    }

    private int epochMonthToEpochDay(int i) {
        return this.hijrahEpochMonthStartDays[i];
    }

    private int yearMonthToDayOfYear(int i, int i2) {
        int yearToEpochMonth = yearToEpochMonth(i);
        return epochMonthToEpochDay(i2 + yearToEpochMonth) - epochMonthToEpochDay(yearToEpochMonth);
    }

    private int epochMonthLength(int i) {
        int[] iArr = this.hijrahEpochMonthStartDays;
        return iArr[i + 1] - iArr[i];
    }

    private Properties readConfigProperties(String str) throws Exception {
        String str2 = RESOURCE_PREFIX + str + RESOURCE_SUFFIX;
        InputStream resourceAsStream = HijrahChronology.class.getResourceAsStream(str2);
        if (resourceAsStream != null) {
            try {
                Properties properties = new Properties();
                properties.load(resourceAsStream);
                if (resourceAsStream != null) {
                    resourceAsStream.close();
                }
                return properties;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        } else {
            throw new RuntimeException("Hijrah calendar resource not found: /java/time/chrono/" + str2);
        }
        throw th;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:34|35|36) */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b6, code lost:
        throw new java.lang.IllegalArgumentException("bad key: " + r12);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:34:0x00a0 */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00e7 A[Catch:{ Exception -> 0x0179 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadCalendarData() {
        /*
            r16 = this;
            r1 = r16
            java.lang.String r0 = r1.calendarType     // Catch:{ Exception -> 0x0179 }
            java.util.Properties r0 = r1.readConfigProperties(r0)     // Catch:{ Exception -> 0x0179 }
            java.util.HashMap r2 = new java.util.HashMap     // Catch:{ Exception -> 0x0179 }
            r2.<init>()     // Catch:{ Exception -> 0x0179 }
            java.util.Set r0 = r0.entrySet()     // Catch:{ Exception -> 0x0179 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x0179 }
            r4 = 0
            r5 = 2147483647(0x7fffffff, float:NaN)
            r6 = -2147483648(0xffffffff80000000, float:-0.0)
            r7 = r5
            r8 = r6
            r9 = 0
            r5 = r4
            r6 = r5
        L_0x0020:
            boolean r10 = r0.hasNext()     // Catch:{ Exception -> 0x0179 }
            r11 = 1
            if (r10 == 0) goto L_0x00f1
            java.lang.Object r10 = r0.next()     // Catch:{ Exception -> 0x0179 }
            java.util.Map$Entry r10 = (java.util.Map.Entry) r10     // Catch:{ Exception -> 0x0179 }
            java.lang.Object r12 = r10.getKey()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ Exception -> 0x0179 }
            int r13 = r12.hashCode()     // Catch:{ Exception -> 0x0179 }
            r14 = -1117701862(0xffffffffbd61391a, float:-0.054986097)
            r15 = 3
            r3 = 2
            if (r13 == r14) goto L_0x006d
            r14 = 3355(0xd1b, float:4.701E-42)
            if (r13 == r14) goto L_0x0063
            r14 = 3575610(0x368f3a, float:5.010497E-39)
            if (r13 == r14) goto L_0x0058
            r14 = 351608024(0x14f51cd8, float:2.4750055E-26)
            if (r13 == r14) goto L_0x004d
            goto L_0x0077
        L_0x004d:
            java.lang.String r13 = "version"
            boolean r13 = r12.equals(r13)     // Catch:{ Exception -> 0x0179 }
            if (r13 == 0) goto L_0x0077
            r13 = r3
            goto L_0x0078
        L_0x0058:
            java.lang.String r13 = "type"
            boolean r13 = r12.equals(r13)     // Catch:{ Exception -> 0x0179 }
            if (r13 == 0) goto L_0x0077
            r13 = r11
            goto L_0x0078
        L_0x0063:
            java.lang.String r13 = "id"
            boolean r13 = r12.equals(r13)     // Catch:{ Exception -> 0x0179 }
            if (r13 == 0) goto L_0x0077
            r13 = 0
            goto L_0x0078
        L_0x006d:
            java.lang.String r13 = "iso-start"
            boolean r13 = r12.equals(r13)     // Catch:{ Exception -> 0x0179 }
            if (r13 == 0) goto L_0x0077
            r13 = r15
            goto L_0x0078
        L_0x0077:
            r13 = -1
        L_0x0078:
            if (r13 == 0) goto L_0x00e7
            if (r13 == r11) goto L_0x00dd
            if (r13 == r3) goto L_0x00d3
            if (r13 == r15) goto L_0x00b7
            int r3 = java.lang.Integer.parseInt(r12)     // Catch:{ NumberFormatException -> 0x00a0 }
            java.lang.Object r10 = r10.getValue()     // Catch:{ NumberFormatException -> 0x00a0 }
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ NumberFormatException -> 0x00a0 }
            int[] r10 = r1.parseMonths(r10)     // Catch:{ NumberFormatException -> 0x00a0 }
            java.lang.Integer r11 = java.lang.Integer.valueOf((int) r3)     // Catch:{ NumberFormatException -> 0x00a0 }
            r2.put(r11, r10)     // Catch:{ NumberFormatException -> 0x00a0 }
            int r8 = java.lang.Math.max((int) r8, (int) r3)     // Catch:{ NumberFormatException -> 0x00a0 }
            int r3 = java.lang.Math.min((int) r7, (int) r3)     // Catch:{ NumberFormatException -> 0x00a0 }
            r7 = r3
            r12 = 0
            goto L_0x0020
        L_0x00a0:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ Exception -> 0x0179 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0179 }
            r2.<init>()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = "bad key: "
            r2.append((java.lang.String) r3)     // Catch:{ Exception -> 0x0179 }
            r2.append((java.lang.String) r12)     // Catch:{ Exception -> 0x0179 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0179 }
            r0.<init>((java.lang.String) r2)     // Catch:{ Exception -> 0x0179 }
            throw r0     // Catch:{ Exception -> 0x0179 }
        L_0x00b7:
            java.lang.Object r9 = r10.getValue()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Exception -> 0x0179 }
            int[] r9 = r1.parseYMD(r9)     // Catch:{ Exception -> 0x0179 }
            r12 = 0
            r10 = r9[r12]     // Catch:{ Exception -> 0x0179 }
            r11 = r9[r11]     // Catch:{ Exception -> 0x0179 }
            r3 = r9[r3]     // Catch:{ Exception -> 0x0179 }
            java.time.LocalDate r3 = java.time.LocalDate.m908of((int) r10, (int) r11, (int) r3)     // Catch:{ Exception -> 0x0179 }
            long r9 = r3.toEpochDay()     // Catch:{ Exception -> 0x0179 }
            int r9 = (int) r9     // Catch:{ Exception -> 0x0179 }
            goto L_0x0020
        L_0x00d3:
            r12 = 0
            java.lang.Object r3 = r10.getValue()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x0179 }
            r6 = r3
            goto L_0x0020
        L_0x00dd:
            r12 = 0
            java.lang.Object r3 = r10.getValue()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x0179 }
            r5 = r3
            goto L_0x0020
        L_0x00e7:
            r12 = 0
            java.lang.Object r3 = r10.getValue()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x0179 }
            r4 = r3
            goto L_0x0020
        L_0x00f1:
            java.lang.String r0 = r16.getId()     // Catch:{ Exception -> 0x0179 }
            boolean r0 = r0.equals(r4)     // Catch:{ Exception -> 0x0179 }
            if (r0 == 0) goto L_0x0162
            java.lang.String r0 = r16.getCalendarType()     // Catch:{ Exception -> 0x0179 }
            boolean r0 = r0.equals(r5)     // Catch:{ Exception -> 0x0179 }
            if (r0 == 0) goto L_0x014b
            if (r6 == 0) goto L_0x0143
            boolean r0 = r6.isEmpty()     // Catch:{ Exception -> 0x0179 }
            if (r0 != 0) goto L_0x0143
            if (r9 == 0) goto L_0x013b
            int r0 = r7 * 12
            r1.hijrahStartEpochMonth = r0     // Catch:{ Exception -> 0x0179 }
            r1.minEpochDay = r9     // Catch:{ Exception -> 0x0179 }
            int[] r0 = r1.createEpochMonths(r9, r7, r8, r2)     // Catch:{ Exception -> 0x0179 }
            r1.hijrahEpochMonthStartDays = r0     // Catch:{ Exception -> 0x0179 }
            int r2 = r0.length     // Catch:{ Exception -> 0x0179 }
            int r2 = r2 - r11
            r0 = r0[r2]     // Catch:{ Exception -> 0x0179 }
            r1.maxEpochDay = r0     // Catch:{ Exception -> 0x0179 }
        L_0x0121:
            if (r7 >= r8) goto L_0x013a
            int r0 = r1.getYearLength(r7)     // Catch:{ Exception -> 0x0179 }
            int r2 = r1.minYearLength     // Catch:{ Exception -> 0x0179 }
            int r2 = java.lang.Math.min((int) r2, (int) r0)     // Catch:{ Exception -> 0x0179 }
            r1.minYearLength = r2     // Catch:{ Exception -> 0x0179 }
            int r2 = r1.maxYearLength     // Catch:{ Exception -> 0x0179 }
            int r0 = java.lang.Math.max((int) r2, (int) r0)     // Catch:{ Exception -> 0x0179 }
            r1.maxYearLength = r0     // Catch:{ Exception -> 0x0179 }
            int r7 = r7 + 1
            goto L_0x0121
        L_0x013a:
            return
        L_0x013b:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ Exception -> 0x0179 }
            java.lang.String r2 = "Configuration does not contain a ISO start date"
            r0.<init>((java.lang.String) r2)     // Catch:{ Exception -> 0x0179 }
            throw r0     // Catch:{ Exception -> 0x0179 }
        L_0x0143:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ Exception -> 0x0179 }
            java.lang.String r2 = "Configuration does not contain a version"
            r0.<init>((java.lang.String) r2)     // Catch:{ Exception -> 0x0179 }
            throw r0     // Catch:{ Exception -> 0x0179 }
        L_0x014b:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ Exception -> 0x0179 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0179 }
            r2.<init>()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = "Configuration is for a different calendar type: "
            r2.append((java.lang.String) r3)     // Catch:{ Exception -> 0x0179 }
            r2.append((java.lang.String) r5)     // Catch:{ Exception -> 0x0179 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0179 }
            r0.<init>((java.lang.String) r2)     // Catch:{ Exception -> 0x0179 }
            throw r0     // Catch:{ Exception -> 0x0179 }
        L_0x0162:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ Exception -> 0x0179 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0179 }
            r2.<init>()     // Catch:{ Exception -> 0x0179 }
            java.lang.String r3 = "Configuration is for a different calendar: "
            r2.append((java.lang.String) r3)     // Catch:{ Exception -> 0x0179 }
            r2.append((java.lang.String) r4)     // Catch:{ Exception -> 0x0179 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0179 }
            r0.<init>((java.lang.String) r2)     // Catch:{ Exception -> 0x0179 }
            throw r0     // Catch:{ Exception -> 0x0179 }
        L_0x0179:
            r0 = move-exception
            java.lang.String r2 = "java.time.chrono"
            sun.util.logging.PlatformLogger r2 = sun.util.logging.PlatformLogger.getLogger(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Unable to initialize Hijrah calendar proxy: "
            r3.<init>((java.lang.String) r4)
            java.lang.String r4 = r1.typeId
            r3.append((java.lang.String) r4)
            java.lang.String r3 = r3.toString()
            r2.severe((java.lang.String) r3, (java.lang.Throwable) r0)
            java.time.DateTimeException r2 = new java.time.DateTimeException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Unable to initialize HijrahCalendar: "
            r3.<init>((java.lang.String) r4)
            java.lang.String r1 = r1.typeId
            r3.append((java.lang.String) r1)
            java.lang.String r1 = r3.toString()
            r2.<init>(r1, r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.chrono.HijrahChronology.loadCalendarData():void");
    }

    private int[] createEpochMonths(int i, int i2, int i3, Map<Integer, int[]> map) {
        int i4 = (((i3 - i2) + 1) * 12) + 1;
        int[] iArr = new int[i4];
        this.minMonthLength = Integer.MAX_VALUE;
        this.maxMonthLength = Integer.MIN_VALUE;
        int i5 = 0;
        for (int i6 = i2; i6 <= i3; i6++) {
            int[] iArr2 = map.get(Integer.valueOf(i6));
            int i7 = 0;
            while (i7 < 12) {
                int i8 = iArr2[i7];
                int i9 = i5 + 1;
                iArr[i5] = i;
                if (i8 < 29 || i8 > 32) {
                    throw new IllegalArgumentException("Invalid month length in year: " + i2);
                }
                i += i8;
                this.minMonthLength = Math.min(this.minMonthLength, i8);
                this.maxMonthLength = Math.max(this.maxMonthLength, i8);
                i7++;
                i5 = i9;
            }
        }
        int i10 = i5 + 1;
        iArr[i5] = i;
        if (i10 == i4) {
            return iArr;
        }
        throw new IllegalStateException("Did not fill epochMonths exactly: ndx = " + i10 + " should be " + i4);
    }

    private int[] parseMonths(String str) {
        int[] iArr = new int[12];
        String[] split = str.split("\\s");
        if (split.length == 12) {
            int i = 0;
            while (i < 12) {
                try {
                    iArr[i] = Integer.parseInt(split[i]);
                    i++;
                } catch (NumberFormatException unused) {
                    throw new IllegalArgumentException("bad key: " + split[i]);
                }
            }
            return iArr;
        }
        throw new IllegalArgumentException("wrong number of months on line: " + Arrays.toString((Object[]) split) + "; count: " + split.length);
    }

    private int[] parseYMD(String str) {
        String trim = str.trim();
        try {
            if (trim.charAt(4) == '-' && trim.charAt(7) == '-') {
                return new int[]{Integer.parseInt(trim, 0, 4, 10), Integer.parseInt(trim, 5, 7, 10), Integer.parseInt(trim, 8, 10, 10)};
            }
            throw new IllegalArgumentException("date must be yyyy-MM-dd");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("date must be yyyy-MM-dd", e);
        }
    }

    /* access modifiers changed from: package-private */
    public Object writeReplace() {
        return super.writeReplace();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
