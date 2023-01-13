package java.util;

import java.p026io.Serializable;
import java.security.AccessControlContext;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import libcore.icu.LocaleData;
import sun.util.locale.provider.CalendarDataUtility;

public abstract class Calendar implements Serializable, Cloneable, Comparable<Calendar> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int ALL_FIELDS = 131071;
    public static final int ALL_STYLES = 0;

    /* renamed from: AM */
    public static final int f637AM = 0;
    public static final int AM_PM = 9;
    static final int AM_PM_MASK = 512;
    public static final int APRIL = 3;
    public static final int AUGUST = 7;
    private static final int COMPUTED = 1;
    public static final int DATE = 5;
    static final int DATE_MASK = 32;
    public static final int DAY_OF_MONTH = 5;
    static final int DAY_OF_MONTH_MASK = 32;
    public static final int DAY_OF_WEEK = 7;
    public static final int DAY_OF_WEEK_IN_MONTH = 8;
    static final int DAY_OF_WEEK_IN_MONTH_MASK = 256;
    static final int DAY_OF_WEEK_MASK = 128;
    public static final int DAY_OF_YEAR = 6;
    static final int DAY_OF_YEAR_MASK = 64;
    public static final int DECEMBER = 11;
    public static final int DST_OFFSET = 16;
    static final int DST_OFFSET_MASK = 65536;
    public static final int ERA = 0;
    static final int ERA_MASK = 1;
    public static final int FEBRUARY = 1;
    public static final int FIELD_COUNT = 17;
    private static final String[] FIELD_NAME = {"ERA", "YEAR", "MONTH", "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR", "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET"};
    public static final int FRIDAY = 6;
    public static final int HOUR = 10;
    static final int HOUR_MASK = 1024;
    public static final int HOUR_OF_DAY = 11;
    static final int HOUR_OF_DAY_MASK = 2048;
    public static final int JANUARY = 0;
    public static final int JULY = 6;
    public static final int JUNE = 5;
    public static final int LONG = 2;
    public static final int LONG_FORMAT = 2;
    public static final int LONG_STANDALONE = 32770;
    public static final int MARCH = 2;
    public static final int MAY = 4;
    public static final int MILLISECOND = 14;
    static final int MILLISECOND_MASK = 16384;
    private static final int MINIMUM_USER_STAMP = 2;
    public static final int MINUTE = 12;
    static final int MINUTE_MASK = 4096;
    public static final int MONDAY = 2;
    public static final int MONTH = 2;
    static final int MONTH_MASK = 4;
    public static final int NARROW_FORMAT = 4;
    public static final int NARROW_STANDALONE = 32772;
    public static final int NOVEMBER = 10;
    public static final int OCTOBER = 9;

    /* renamed from: PM */
    public static final int f638PM = 1;
    public static final int SATURDAY = 7;
    public static final int SECOND = 13;
    static final int SECOND_MASK = 8192;
    public static final int SEPTEMBER = 8;
    public static final int SHORT = 1;
    public static final int SHORT_FORMAT = 1;
    public static final int SHORT_STANDALONE = 32769;
    static final int STANDALONE_MASK = 32768;
    public static final int SUNDAY = 1;
    public static final int THURSDAY = 5;
    public static final int TUESDAY = 3;
    public static final int UNDECIMBER = 12;
    private static final int UNSET = 0;
    public static final int WEDNESDAY = 4;
    public static final int WEEK_OF_MONTH = 4;
    static final int WEEK_OF_MONTH_MASK = 16;
    public static final int WEEK_OF_YEAR = 3;
    static final int WEEK_OF_YEAR_MASK = 8;
    public static final int YEAR = 1;
    static final int YEAR_MASK = 2;
    public static final int ZONE_OFFSET = 15;
    static final int ZONE_OFFSET_MASK = 32768;
    private static final ConcurrentMap<Locale, int[]> cachedLocaleData = new ConcurrentHashMap(3);
    static final int currentSerialVersion = 1;
    static final long serialVersionUID = -1807547505821590642L;
    transient boolean areAllFieldsSet;
    protected boolean areFieldsSet;
    protected int[] fields;
    private int firstDayOfWeek;
    protected boolean[] isSet;
    protected boolean isTimeSet;
    private boolean lenient;
    private int minimalDaysInFirstWeek;
    private int nextStamp;
    private int serialVersionOnStream;
    private transient boolean sharedZone;
    private transient int[] stamp;
    protected long time;
    private TimeZone zone;

    private static int aggregateStamp(int i, int i2) {
        if (i == 0 || i2 == 0) {
            return 0;
        }
        return i > i2 ? i : i2;
    }

    static boolean isFieldSet(int i, int i2) {
        return (i & (1 << i2)) != 0;
    }

    private boolean isNarrowFormatStyle(int i) {
        return i == 4;
    }

    private boolean isNarrowStyle(int i) {
        return i == 4 || i == 32772;
    }

    private boolean isStandaloneStyle(int i) {
        return (32768 & i) != 0;
    }

    public static int toStandaloneStyle(int i) {
        return i | 32768;
    }

    public abstract void add(int i, int i2);

    /* access modifiers changed from: protected */
    public abstract void computeFields();

    /* access modifiers changed from: protected */
    public abstract void computeTime();

    /* access modifiers changed from: package-private */
    public int getBaseStyle(int i) {
        return -32769 & i;
    }

    public abstract int getGreatestMinimum(int i);

    public abstract int getLeastMaximum(int i);

    public abstract int getMaximum(int i);

    public abstract int getMinimum(int i);

    public boolean isWeekDateSupported() {
        return false;
    }

    public abstract void roll(int i, boolean z);

    public static class Builder {
        private static final int NFIELDS = 18;
        private static final int WEEK_YEAR = 17;
        private int[] fields;
        private int firstDayOfWeek;
        private long instant;
        private boolean lenient = true;
        private Locale locale;
        private int maxFieldIndex;
        private int minimalDaysInFirstWeek;
        private int nextStamp;
        private String type;
        private TimeZone zone;

        private boolean isValidWeekParameter(int i) {
            return i > 0 && i <= 7;
        }

        public Builder setInstant(long j) {
            if (this.fields == null) {
                this.instant = j;
                this.nextStamp = 1;
                return this;
            }
            throw new IllegalStateException();
        }

        public Builder setInstant(Date date) {
            return setInstant(date.getTime());
        }

        public Builder set(int i, int i2) {
            if (i < 0 || i >= 17) {
                throw new IllegalArgumentException("field is invalid");
            } else if (!isInstantSet()) {
                allocateFields();
                internalSet(i, i2);
                return this;
            } else {
                throw new IllegalStateException("instant has been set");
            }
        }

        public Builder setFields(int... iArr) {
            int length = iArr.length;
            if (length % 2 != 0) {
                throw new IllegalArgumentException();
            } else if (isInstantSet()) {
                throw new IllegalStateException("instant has been set");
            } else if (this.nextStamp + (length / 2) >= 0) {
                allocateFields();
                int i = 0;
                while (i < length) {
                    int i2 = i + 1;
                    int i3 = iArr[i];
                    if (i3 < 0 || i3 >= 17) {
                        throw new IllegalArgumentException("field is invalid");
                    }
                    internalSet(i3, iArr[i2]);
                    i = i2 + 1;
                }
                return this;
            } else {
                throw new IllegalStateException("stamp counter overflow");
            }
        }

        public Builder setDate(int i, int i2, int i3) {
            return setFields(1, i, 2, i2, 5, i3);
        }

        public Builder setTimeOfDay(int i, int i2, int i3) {
            return setTimeOfDay(i, i2, i3, 0);
        }

        public Builder setTimeOfDay(int i, int i2, int i3, int i4) {
            return setFields(11, i, 12, i2, 13, i3, 14, i4);
        }

        public Builder setWeekDate(int i, int i2, int i3) {
            allocateFields();
            internalSet(17, i);
            internalSet(3, i2);
            internalSet(7, i3);
            return this;
        }

        public Builder setTimeZone(TimeZone timeZone) {
            timeZone.getClass();
            this.zone = timeZone;
            return this;
        }

        public Builder setLenient(boolean z) {
            this.lenient = z;
            return this;
        }

        public Builder setCalendarType(String str) {
            if (str.equals("gregorian")) {
                str = "gregory";
            }
            if (Calendar.getAvailableCalendarTypes().contains(str) || str.equals("iso8601")) {
                String str2 = this.type;
                if (str2 == null) {
                    this.type = str;
                } else if (!str2.equals(str)) {
                    throw new IllegalStateException("calendar type override");
                }
                return this;
            }
            throw new IllegalArgumentException("unknown calendar type: " + str);
        }

        public Builder setLocale(Locale locale2) {
            locale2.getClass();
            this.locale = locale2;
            return this;
        }

        public Builder setWeekDefinition(int i, int i2) {
            if (!isValidWeekParameter(i) || !isValidWeekParameter(i2)) {
                throw new IllegalArgumentException();
            }
            this.firstDayOfWeek = i;
            this.minimalDaysInFirstWeek = i2;
            return this;
        }

        /* JADX WARNING: Removed duplicated region for block: B:38:0x00b7  */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x00cd A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.Calendar build() {
            /*
                r8 = this;
                java.util.Locale r0 = r8.locale
                if (r0 != 0) goto L_0x000a
                java.util.Locale r0 = java.util.Locale.getDefault()
                r8.locale = r0
            L_0x000a:
                java.util.TimeZone r0 = r8.zone
                if (r0 != 0) goto L_0x0014
                java.util.TimeZone r0 = java.util.TimeZone.getDefault()
                r8.zone = r0
            L_0x0014:
                java.lang.String r0 = r8.type
                if (r0 != 0) goto L_0x0022
                java.util.Locale r0 = r8.locale
                java.lang.String r1 = "ca"
                java.lang.String r0 = r0.getUnicodeLocaleType(r1)
                r8.type = r0
            L_0x0022:
                java.lang.String r0 = r8.type
                java.lang.String r1 = "gregory"
                if (r0 != 0) goto L_0x002a
                r8.type = r1
            L_0x002a:
                java.lang.String r0 = r8.type
                r0.hashCode()
                boolean r1 = r0.equals(r1)
                r2 = 2
                r3 = 1
                if (r1 != 0) goto L_0x006d
                java.lang.String r1 = "iso8601"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L_0x0057
                java.util.GregorianCalendar r0 = new java.util.GregorianCalendar
                java.util.TimeZone r1 = r8.zone
                java.util.Locale r4 = r8.locale
                r0.<init>((java.util.TimeZone) r1, (java.util.Locale) r4, (boolean) r3)
                java.util.Date r1 = new java.util.Date
                r4 = -9223372036854775808
                r1.<init>((long) r4)
                r0.setGregorianChange((java.util.Date) r1)
                r1 = 4
                r8.setWeekDefinition(r2, r1)
                goto L_0x0076
            L_0x0057:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "unknown calendar type: "
                r1.<init>((java.lang.String) r2)
                java.lang.String r8 = r8.type
                r1.append((java.lang.String) r8)
                java.lang.String r8 = r1.toString()
                r0.<init>((java.lang.String) r8)
                throw r0
            L_0x006d:
                java.util.GregorianCalendar r0 = new java.util.GregorianCalendar
                java.util.TimeZone r1 = r8.zone
                java.util.Locale r4 = r8.locale
                r0.<init>((java.util.TimeZone) r1, (java.util.Locale) r4, (boolean) r3)
            L_0x0076:
                boolean r1 = r8.lenient
                r0.setLenient(r1)
                int r1 = r8.firstDayOfWeek
                if (r1 == 0) goto L_0x0087
                r0.setFirstDayOfWeek(r1)
                int r1 = r8.minimalDaysInFirstWeek
                r0.setMinimalDaysInFirstWeek(r1)
            L_0x0087:
                boolean r1 = r8.isInstantSet()
                if (r1 == 0) goto L_0x0096
                long r1 = r8.instant
                r0.setTimeInMillis(r1)
                r0.complete()
                return r0
            L_0x0096:
                int[] r1 = r8.fields
                if (r1 == 0) goto L_0x0117
                r1 = 17
                boolean r4 = r8.isSet(r1)
                r5 = 0
                if (r4 == 0) goto L_0x00ad
                int[] r4 = r8.fields
                r1 = r4[r1]
                r4 = r4[r3]
                if (r1 <= r4) goto L_0x00ad
                r1 = r3
                goto L_0x00ae
            L_0x00ad:
                r1 = r5
            L_0x00ae:
                if (r1 == 0) goto L_0x00cd
                boolean r4 = r0.isWeekDateSupported()
                if (r4 == 0) goto L_0x00b7
                goto L_0x00cd
            L_0x00b7:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "week date is unsupported by "
                r1.<init>((java.lang.String) r2)
                java.lang.String r8 = r8.type
                r1.append((java.lang.String) r8)
                java.lang.String r8 = r1.toString()
                r0.<init>((java.lang.String) r8)
                throw r0
            L_0x00cd:
                int r4 = r8.nextStamp
                if (r2 >= r4) goto L_0x00ea
                r4 = r5
            L_0x00d2:
                int r6 = r8.maxFieldIndex
                if (r4 > r6) goto L_0x00e7
                int[] r6 = r8.fields
                r7 = r6[r4]
                if (r7 != r2) goto L_0x00e4
                int r7 = r4 + 18
                r6 = r6[r7]
                r0.set(r4, r6)
                goto L_0x00e7
            L_0x00e4:
                int r4 = r4 + 1
                goto L_0x00d2
            L_0x00e7:
                int r2 = r2 + 1
                goto L_0x00cd
            L_0x00ea:
                if (r1 == 0) goto L_0x0114
                r1 = 3
                boolean r1 = r8.isSet(r1)
                if (r1 == 0) goto L_0x00f9
                int[] r1 = r8.fields
                r2 = 21
                r3 = r1[r2]
            L_0x00f9:
                r1 = 7
                boolean r1 = r8.isSet(r1)
                if (r1 == 0) goto L_0x0107
                int[] r1 = r8.fields
                r2 = 25
                r1 = r1[r2]
                goto L_0x010b
            L_0x0107:
                int r1 = r0.getFirstDayOfWeek()
            L_0x010b:
                int[] r8 = r8.fields
                r2 = 35
                r8 = r8[r2]
                r0.setWeekDate(r8, r3, r1)
            L_0x0114:
                r0.complete()
            L_0x0117:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Calendar.Builder.build():java.util.Calendar");
        }

        private void allocateFields() {
            if (this.fields == null) {
                this.fields = new int[36];
                this.nextStamp = 2;
                this.maxFieldIndex = -1;
            }
        }

        private void internalSet(int i, int i2) {
            int[] iArr = this.fields;
            int i3 = this.nextStamp;
            int i4 = i3 + 1;
            this.nextStamp = i4;
            iArr[i] = i3;
            if (i4 >= 0) {
                iArr[i + 18] = i2;
                if (i > this.maxFieldIndex && i < 17) {
                    this.maxFieldIndex = i;
                    return;
                }
                return;
            }
            throw new IllegalStateException("stamp counter overflow");
        }

        private boolean isInstantSet() {
            return this.nextStamp == 1;
        }

        private boolean isSet(int i) {
            int[] iArr = this.fields;
            return iArr != null && iArr[i] > 0;
        }
    }

    protected Calendar() {
        this(TimeZone.getDefaultRef(), Locale.getDefault(Locale.Category.FORMAT));
        this.sharedZone = true;
    }

    protected Calendar(TimeZone timeZone, Locale locale) {
        this.lenient = true;
        this.sharedZone = false;
        this.nextStamp = 2;
        this.serialVersionOnStream = 1;
        locale = locale == null ? Locale.getDefault() : locale;
        this.fields = new int[17];
        this.isSet = new boolean[17];
        this.stamp = new int[17];
        this.zone = timeZone;
        setWeekCountData(locale);
    }

    public static Calendar getInstance() {
        return createCalendar(TimeZone.getDefault(), Locale.getDefault(Locale.Category.FORMAT));
    }

    public static Calendar getInstance(TimeZone timeZone) {
        return createCalendar(timeZone, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static Calendar getInstance(Locale locale) {
        return createCalendar(TimeZone.getDefault(), locale);
    }

    public static Calendar getInstance(TimeZone timeZone, Locale locale) {
        return createCalendar(timeZone, locale);
    }

    public static Calendar getJapaneseImperialInstance(TimeZone timeZone, Locale locale) {
        return new JapaneseImperialCalendar(timeZone, locale);
    }

    private static Calendar createCalendar(TimeZone timeZone, Locale locale) {
        return new GregorianCalendar(timeZone, locale);
    }

    public static synchronized Locale[] getAvailableLocales() {
        Locale[] availableLocales;
        synchronized (Calendar.class) {
            availableLocales = DateFormat.getAvailableLocales();
        }
        return availableLocales;
    }

    public final Date getTime() {
        return new Date(getTimeInMillis());
    }

    public final void setTime(Date date) {
        setTimeInMillis(date.getTime());
    }

    public long getTimeInMillis() {
        if (!this.isTimeSet) {
            updateTime();
        }
        return this.time;
    }

    public void setTimeInMillis(long j) {
        if (this.time != j || !this.isTimeSet || !this.areFieldsSet || !this.areAllFieldsSet) {
            this.time = j;
            this.isTimeSet = true;
            this.areFieldsSet = false;
            computeFields();
            this.areFieldsSet = true;
            this.areAllFieldsSet = true;
        }
    }

    public int get(int i) {
        complete();
        return internalGet(i);
    }

    /* access modifiers changed from: protected */
    public final int internalGet(int i) {
        return this.fields[i];
    }

    /* access modifiers changed from: package-private */
    public final void internalSet(int i, int i2) {
        this.fields[i] = i2;
    }

    public void set(int i, int i2) {
        if (this.areFieldsSet && !this.areAllFieldsSet) {
            computeFields();
        }
        internalSet(i, i2);
        this.isTimeSet = false;
        this.areFieldsSet = false;
        this.isSet[i] = true;
        int[] iArr = this.stamp;
        int i3 = this.nextStamp;
        int i4 = i3 + 1;
        this.nextStamp = i4;
        iArr[i] = i3;
        if (i4 == Integer.MAX_VALUE) {
            adjustStamp();
        }
    }

    public final void set(int i, int i2, int i3) {
        set(1, i);
        set(2, i2);
        set(5, i3);
    }

    public final void set(int i, int i2, int i3, int i4, int i5) {
        set(1, i);
        set(2, i2);
        set(5, i3);
        set(11, i4);
        set(12, i5);
    }

    public final void set(int i, int i2, int i3, int i4, int i5, int i6) {
        set(1, i);
        set(2, i2);
        set(5, i3);
        set(11, i4);
        set(12, i5);
        set(13, i6);
    }

    public final void clear() {
        int i = 0;
        while (true) {
            int[] iArr = this.fields;
            if (i < iArr.length) {
                int[] iArr2 = this.stamp;
                iArr[i] = 0;
                iArr2[i] = 0;
                this.isSet[i] = false;
                i++;
            } else {
                this.areFieldsSet = false;
                this.areAllFieldsSet = false;
                this.isTimeSet = false;
                return;
            }
        }
    }

    public final void clear(int i) {
        this.fields[i] = 0;
        this.stamp[i] = 0;
        this.isSet[i] = false;
        this.areFieldsSet = false;
        this.areAllFieldsSet = false;
        this.isTimeSet = false;
    }

    public final boolean isSet(int i) {
        return this.stamp[i] != 0;
    }

    public String getDisplayName(int i, int i2, Locale locale) {
        if (i2 == 0) {
            i2 = 1;
        }
        if (!checkDisplayNameParams(i, i2, 1, 4, locale, 645)) {
            return null;
        }
        String calendarType = getCalendarType();
        int i3 = get(i);
        if (isStandaloneStyle(i2) || isNarrowFormatStyle(i2)) {
            String retrieveFieldValueName = CalendarDataUtility.retrieveFieldValueName(calendarType, i, i3, i2, locale);
            if (retrieveFieldValueName != null) {
                return retrieveFieldValueName;
            }
            if (isNarrowFormatStyle(i2)) {
                return CalendarDataUtility.retrieveFieldValueName(calendarType, i, i3, toStandaloneStyle(i2), locale);
            }
            return isStandaloneStyle(i2) ? CalendarDataUtility.retrieveFieldValueName(calendarType, i, i3, getBaseStyle(i2), locale) : retrieveFieldValueName;
        }
        String[] fieldStrings = getFieldStrings(i, i2, DateFormatSymbols.getInstance(locale));
        if (fieldStrings == null || i3 >= fieldStrings.length) {
            return null;
        }
        return fieldStrings[i3];
    }

    public Map<String, Integer> getDisplayNames(int i, int i2, Locale locale) {
        if (!checkDisplayNameParams(i, i2, 0, 4, locale, 645)) {
            return null;
        }
        complete();
        String calendarType = getCalendarType();
        if (i2 != 0 && !isStandaloneStyle(i2) && !isNarrowFormatStyle(i2)) {
            return getDisplayNamesImpl(i, i2, locale);
        }
        Map<String, Integer> retrieveFieldValueNames = CalendarDataUtility.retrieveFieldValueNames(calendarType, i, i2, locale);
        if (retrieveFieldValueNames != null) {
            return retrieveFieldValueNames;
        }
        if (isNarrowFormatStyle(i2)) {
            return CalendarDataUtility.retrieveFieldValueNames(calendarType, i, toStandaloneStyle(i2), locale);
        }
        return i2 != 0 ? CalendarDataUtility.retrieveFieldValueNames(calendarType, i, getBaseStyle(i2), locale) : retrieveFieldValueNames;
    }

    private Map<String, Integer> getDisplayNamesImpl(int i, int i2, Locale locale) {
        String[] fieldStrings = getFieldStrings(i, i2, DateFormatSymbols.getInstance(locale));
        if (fieldStrings == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (int i3 = 0; i3 < fieldStrings.length; i3++) {
            if (fieldStrings[i3].length() != 0) {
                hashMap.put(fieldStrings[i3], Integer.valueOf(i3));
            }
        }
        return hashMap;
    }

    /* access modifiers changed from: package-private */
    public boolean checkDisplayNameParams(int i, int i2, int i3, int i4, Locale locale, int i5) {
        int baseStyle = getBaseStyle(i2);
        if (i < 0 || i >= this.fields.length || baseStyle < i3 || baseStyle > i4) {
            throw new IllegalArgumentException();
        } else if (baseStyle != 3) {
            locale.getClass();
            return isFieldSet(i5, i);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String[] getFieldStrings(int i, int i2, DateFormatSymbols dateFormatSymbols) {
        String[] months;
        int baseStyle = getBaseStyle(i2);
        if (baseStyle == 4) {
            return null;
        }
        if (i == 0) {
            return dateFormatSymbols.getEras();
        }
        if (i == 2) {
            months = baseStyle == 2 ? dateFormatSymbols.getMonths() : dateFormatSymbols.getShortMonths();
        } else if (i == 7) {
            months = baseStyle == 2 ? dateFormatSymbols.getWeekdays() : dateFormatSymbols.getShortWeekdays();
        } else if (i != 9) {
            return null;
        } else {
            return dateFormatSymbols.getAmPmStrings();
        }
        return months;
    }

    /* access modifiers changed from: protected */
    public void complete() {
        if (!this.isTimeSet) {
            updateTime();
        }
        if (!this.areFieldsSet || !this.areAllFieldsSet) {
            computeFields();
            this.areFieldsSet = true;
            this.areAllFieldsSet = true;
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean isExternallySet(int i) {
        return this.stamp[i] >= 2;
    }

    /* access modifiers changed from: package-private */
    public final int getSetStateFields() {
        int i = 0;
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            if (this.stamp[i2] != 0) {
                i |= 1 << i2;
            }
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public final void setFieldsComputed(int i) {
        if (i == ALL_FIELDS) {
            for (int i2 = 0; i2 < this.fields.length; i2++) {
                this.stamp[i2] = 1;
                this.isSet[i2] = true;
            }
            this.areAllFieldsSet = true;
            this.areFieldsSet = true;
            return;
        }
        for (int i3 = 0; i3 < this.fields.length; i3++) {
            if ((i & 1) == 1) {
                this.stamp[i3] = 1;
                this.isSet[i3] = true;
            } else if (this.areAllFieldsSet && !this.isSet[i3]) {
                this.areAllFieldsSet = false;
            }
            i >>>= 1;
        }
    }

    /* access modifiers changed from: package-private */
    public final void setFieldsNormalized(int i) {
        if (i != ALL_FIELDS) {
            int i2 = 0;
            while (true) {
                int[] iArr = this.fields;
                if (i2 >= iArr.length) {
                    break;
                }
                if ((i & 1) == 0) {
                    int[] iArr2 = this.stamp;
                    iArr[i2] = 0;
                    iArr2[i2] = 0;
                    this.isSet[i2] = false;
                }
                i >>= 1;
                i2++;
            }
        }
        this.areFieldsSet = true;
        this.areAllFieldsSet = false;
    }

    /* access modifiers changed from: package-private */
    public final boolean isPartiallyNormalized() {
        return this.areFieldsSet && !this.areAllFieldsSet;
    }

    /* access modifiers changed from: package-private */
    public final boolean isFullyNormalized() {
        return this.areFieldsSet && this.areAllFieldsSet;
    }

    /* access modifiers changed from: package-private */
    public final void setUnnormalized() {
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006b, code lost:
        if (r10[4] < r10[3]) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0075, code lost:
        if (r10[8] >= r10[3]) goto L_0x0084;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0096, code lost:
        if (r0[4] >= r0[8]) goto L_0x009e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00be  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00cd  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f4  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00fc  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:74:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int selectFields() {
        /*
            r14 = this;
            int[] r0 = r14.stamp
            r1 = 0
            r1 = r0[r1]
            r2 = 3
            r3 = 2
            if (r1 == 0) goto L_0x000b
            r1 = r2
            goto L_0x000c
        L_0x000b:
            r1 = r3
        L_0x000c:
            r4 = 7
            r4 = r0[r4]
            r5 = r0[r3]
            r6 = 5
            r6 = r0[r6]
            r7 = 4
            r0 = r0[r7]
            int r0 = aggregateStamp(r0, r4)
            int[] r8 = r14.stamp
            r9 = 8
            r8 = r8[r9]
            int r8 = aggregateStamp(r8, r4)
            int[] r10 = r14.stamp
            r11 = 6
            r11 = r10[r11]
            r10 = r10[r2]
            int r10 = aggregateStamp(r10, r4)
            if (r0 <= r6) goto L_0x0034
            r12 = r0
            goto L_0x0035
        L_0x0034:
            r12 = r6
        L_0x0035:
            if (r8 <= r12) goto L_0x0038
            r12 = r8
        L_0x0038:
            if (r11 <= r12) goto L_0x003b
            r12 = r11
        L_0x003b:
            if (r10 <= r12) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r10 = r12
        L_0x003f:
            if (r10 != 0) goto L_0x0060
            int[] r0 = r14.stamp
            r8 = r0[r7]
            r0 = r0[r9]
            int r0 = java.lang.Math.max((int) r0, (int) r4)
            int[] r10 = r14.stamp
            r10 = r10[r2]
            int r12 = java.lang.Math.max((int) r8, (int) r0)
            int r10 = java.lang.Math.max((int) r12, (int) r10)
            if (r10 != 0) goto L_0x005b
            r6 = r5
            goto L_0x005c
        L_0x005b:
            r5 = r10
        L_0x005c:
            r13 = r8
            r8 = r0
            r0 = r13
            goto L_0x0061
        L_0x0060:
            r5 = r10
        L_0x0061:
            if (r5 == r6) goto L_0x0084
            if (r5 != r0) goto L_0x006d
            int[] r10 = r14.stamp
            r12 = r10[r7]
            r10 = r10[r2]
            if (r12 >= r10) goto L_0x0084
        L_0x006d:
            if (r5 != r8) goto L_0x0078
            int[] r10 = r14.stamp
            r12 = r10[r9]
            r2 = r10[r2]
            if (r12 < r2) goto L_0x0078
            goto L_0x0084
        L_0x0078:
            if (r5 != r11) goto L_0x007d
            r0 = r1 | 64
            goto L_0x00a9
        L_0x007d:
            if (r4 == 0) goto L_0x0081
            r1 = r1 | 128(0x80, float:1.794E-43)
        L_0x0081:
            r0 = r1 | 8
            goto L_0x00a9
        L_0x0084:
            r1 = r1 | r7
            if (r5 != r6) goto L_0x008a
            r0 = r1 | 32
            goto L_0x00a9
        L_0x008a:
            if (r4 == 0) goto L_0x008e
            r1 = r1 | 128(0x80, float:1.794E-43)
        L_0x008e:
            if (r0 != r8) goto L_0x009c
            int[] r0 = r14.stamp
            r2 = r0[r7]
            r0 = r0[r9]
            if (r2 < r0) goto L_0x0099
            goto L_0x009e
        L_0x0099:
            r0 = r1 | 256(0x100, float:3.59E-43)
            goto L_0x00a9
        L_0x009c:
            if (r5 != r0) goto L_0x00a1
        L_0x009e:
            r0 = r1 | 16
            goto L_0x00a9
        L_0x00a1:
            int[] r0 = r14.stamp
            r0 = r0[r9]
            if (r0 == 0) goto L_0x00a8
            goto L_0x0099
        L_0x00a8:
            r0 = r1
        L_0x00a9:
            int[] r1 = r14.stamp
            r2 = 11
            r2 = r1[r2]
            r4 = 10
            r5 = r1[r4]
            r6 = 9
            r1 = r1[r6]
            int r1 = aggregateStamp(r5, r1)
            if (r1 <= r2) goto L_0x00be
            goto L_0x00bf
        L_0x00be:
            r1 = r2
        L_0x00bf:
            if (r1 != 0) goto L_0x00cb
            int[] r1 = r14.stamp
            r4 = r1[r4]
            r1 = r1[r6]
            int r1 = java.lang.Math.max((int) r4, (int) r1)
        L_0x00cb:
            if (r1 == 0) goto L_0x00dc
            if (r1 != r2) goto L_0x00d2
            r0 = r0 | 2048(0x800, float:2.87E-42)
            goto L_0x00dc
        L_0x00d2:
            r0 = r0 | 1024(0x400, float:1.435E-42)
            int[] r1 = r14.stamp
            r1 = r1[r6]
            if (r1 == 0) goto L_0x00dc
            r0 = r0 | 512(0x200, float:7.175E-43)
        L_0x00dc:
            int[] r14 = r14.stamp
            r1 = 12
            r1 = r14[r1]
            if (r1 == 0) goto L_0x00e6
            r0 = r0 | 4096(0x1000, float:5.74E-42)
        L_0x00e6:
            r1 = 13
            r1 = r14[r1]
            if (r1 == 0) goto L_0x00ee
            r0 = r0 | 8192(0x2000, float:1.14794E-41)
        L_0x00ee:
            r1 = 14
            r1 = r14[r1]
            if (r1 == 0) goto L_0x00f6
            r0 = r0 | 16384(0x4000, float:2.2959E-41)
        L_0x00f6:
            r1 = 15
            r1 = r14[r1]
            if (r1 < r3) goto L_0x0100
            r1 = 32768(0x8000, float:4.5918E-41)
            r0 = r0 | r1
        L_0x0100:
            r1 = 16
            r14 = r14[r1]
            if (r14 < r3) goto L_0x0109
            r14 = 65536(0x10000, float:9.18355E-41)
            r0 = r0 | r14
        L_0x0109:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Calendar.selectFields():int");
    }

    public static Set<String> getAvailableCalendarTypes() {
        return AvailableCalendarTypes.SET;
    }

    private static class AvailableCalendarTypes {
        /* access modifiers changed from: private */
        public static final Set<String> SET;

        static {
            HashSet hashSet = new HashSet(3);
            hashSet.add("gregory");
            SET = Collections.unmodifiableSet(hashSet);
        }

        private AvailableCalendarTypes() {
        }
    }

    public String getCalendarType() {
        return getClass().getName();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        try {
            Calendar calendar = (Calendar) obj;
            if (compareTo(getMillisOf(calendar)) == 0 && this.lenient == calendar.lenient && this.firstDayOfWeek == calendar.firstDayOfWeek && this.minimalDaysInFirstWeek == calendar.minimalDaysInFirstWeek && this.zone.equals(calendar.zone)) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public int hashCode() {
        boolean hashCode = this.lenient | (this.firstDayOfWeek << 1) | (this.minimalDaysInFirstWeek << 4) | (this.zone.hashCode() << 7);
        long millisOf = getMillisOf(this);
        return (((int) millisOf) ^ ((int) (millisOf >> 32))) ^ hashCode ? 1 : 0;
    }

    public boolean before(Object obj) {
        return (obj instanceof Calendar) && compareTo((Calendar) obj) < 0;
    }

    public boolean after(Object obj) {
        return (obj instanceof Calendar) && compareTo((Calendar) obj) > 0;
    }

    public int compareTo(Calendar calendar) {
        return compareTo(getMillisOf(calendar));
    }

    public void roll(int i, int i2) {
        while (i2 > 0) {
            roll(i, true);
            i2--;
        }
        while (i2 < 0) {
            roll(i, false);
            i2++;
        }
    }

    public void setTimeZone(TimeZone timeZone) {
        this.zone = timeZone;
        this.sharedZone = false;
        this.areFieldsSet = false;
        this.areAllFieldsSet = false;
    }

    public TimeZone getTimeZone() {
        if (this.sharedZone) {
            this.zone = (TimeZone) this.zone.clone();
            this.sharedZone = false;
        }
        return this.zone;
    }

    /* access modifiers changed from: package-private */
    public TimeZone getZone() {
        return this.zone;
    }

    /* access modifiers changed from: package-private */
    public void setZoneShared(boolean z) {
        this.sharedZone = z;
    }

    public void setLenient(boolean z) {
        this.lenient = z;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public void setFirstDayOfWeek(int i) {
        if (this.firstDayOfWeek != i) {
            this.firstDayOfWeek = i;
            invalidateWeekFields();
        }
    }

    public int getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public void setMinimalDaysInFirstWeek(int i) {
        if (this.minimalDaysInFirstWeek != i) {
            this.minimalDaysInFirstWeek = i;
            invalidateWeekFields();
        }
    }

    public int getMinimalDaysInFirstWeek() {
        return this.minimalDaysInFirstWeek;
    }

    public int getWeekYear() {
        throw new UnsupportedOperationException();
    }

    public void setWeekDate(int i, int i2, int i3) {
        throw new UnsupportedOperationException();
    }

    public int getWeeksInWeekYear() {
        throw new UnsupportedOperationException();
    }

    public int getActualMinimum(int i) {
        int greatestMinimum = getGreatestMinimum(i);
        int minimum = getMinimum(i);
        if (greatestMinimum == minimum) {
            return greatestMinimum;
        }
        Calendar calendar = (Calendar) clone();
        calendar.setLenient(true);
        int i2 = greatestMinimum;
        while (true) {
            calendar.set(i, greatestMinimum);
            if (calendar.get(i) != greatestMinimum) {
                return i2;
            }
            int i3 = greatestMinimum - 1;
            if (i3 < minimum) {
                return greatestMinimum;
            }
            int i4 = i3;
            i2 = greatestMinimum;
            greatestMinimum = i4;
        }
    }

    public int getActualMaximum(int i) {
        int leastMaximum = getLeastMaximum(i);
        int maximum = getMaximum(i);
        if (leastMaximum == maximum) {
            return leastMaximum;
        }
        Calendar calendar = (Calendar) clone();
        calendar.setLenient(true);
        if (i == 3 || i == 4) {
            calendar.set(7, this.firstDayOfWeek);
        }
        int i2 = leastMaximum;
        while (true) {
            calendar.set(i, leastMaximum);
            if (calendar.get(i) != leastMaximum) {
                return i2;
            }
            int i3 = leastMaximum + 1;
            if (i3 > maximum) {
                return leastMaximum;
            }
            int i4 = leastMaximum;
            leastMaximum = i3;
            i2 = i4;
        }
    }

    public Object clone() {
        try {
            Calendar calendar = (Calendar) super.clone();
            calendar.fields = new int[17];
            calendar.isSet = new boolean[17];
            calendar.stamp = new int[17];
            for (int i = 0; i < 17; i++) {
                calendar.fields[i] = this.fields[i];
                calendar.stamp[i] = this.stamp[i];
                calendar.isSet[i] = this.isSet[i];
            }
            calendar.zone = (TimeZone) this.zone.clone();
            return calendar;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    static String getFieldName(int i) {
        return FIELD_NAME[i];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(800);
        sb.append(getClass().getName());
        sb.append('[');
        appendValue(sb, "time", this.isTimeSet, this.time);
        sb.append(",areFieldsSet=");
        sb.append(this.areFieldsSet);
        sb.append(",areAllFieldsSet=");
        sb.append(this.areAllFieldsSet);
        sb.append(",lenient=");
        sb.append(this.lenient);
        sb.append(",zone=");
        sb.append((Object) this.zone);
        appendValue(sb, ",firstDayOfWeek", true, (long) this.firstDayOfWeek);
        appendValue(sb, ",minimalDaysInFirstWeek", true, (long) this.minimalDaysInFirstWeek);
        for (int i = 0; i < 17; i++) {
            sb.append(',');
            appendValue(sb, FIELD_NAME[i], isSet(i), (long) this.fields[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    private static void appendValue(StringBuilder sb, String str, boolean z, long j) {
        sb.append(str);
        sb.append('=');
        if (z) {
            sb.append(j);
        } else {
            sb.append('?');
        }
    }

    private void setWeekCountData(Locale locale) {
        Locale compatibleLocaleForBug159514442 = LocaleData.getCompatibleLocaleForBug159514442(locale);
        ConcurrentMap<Locale, int[]> concurrentMap = cachedLocaleData;
        int[] iArr = concurrentMap.get(compatibleLocaleForBug159514442);
        if (iArr == null) {
            LocaleData localeData = LocaleData.get(compatibleLocaleForBug159514442);
            iArr = new int[]{localeData.firstDayOfWeek.intValue(), localeData.minimalDaysInFirstWeek.intValue()};
            concurrentMap.putIfAbsent(compatibleLocaleForBug159514442, iArr);
        }
        this.firstDayOfWeek = iArr[0];
        this.minimalDaysInFirstWeek = iArr[1];
    }

    private void updateTime() {
        computeTime();
        this.isTimeSet = true;
    }

    private int compareTo(long j) {
        int i = (getMillisOf(this) > j ? 1 : (getMillisOf(this) == j ? 0 : -1));
        if (i > 0) {
            return 1;
        }
        return i == 0 ? 0 : -1;
    }

    private static long getMillisOf(Calendar calendar) {
        if (calendar.isTimeSet) {
            return calendar.time;
        }
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.setLenient(true);
        return calendar2.getTimeInMillis();
    }

    private void adjustStamp() {
        int i;
        int i2 = 2;
        int i3 = 2;
        do {
            int i4 = 0;
            int i5 = 0;
            i = Integer.MAX_VALUE;
            while (true) {
                int[] iArr = this.stamp;
                if (i5 >= iArr.length) {
                    break;
                }
                int i6 = iArr[i5];
                if (i6 >= i3 && i > i6) {
                    i = i6;
                }
                if (i2 < i6) {
                    i2 = i6;
                }
                i5++;
            }
            if (i2 != i && i == Integer.MAX_VALUE) {
                break;
            }
            while (true) {
                int[] iArr2 = this.stamp;
                if (i4 >= iArr2.length) {
                    break;
                }
                if (iArr2[i4] == i) {
                    iArr2[i4] = i3;
                }
                i4++;
            }
            i3++;
        } while (i != i2);
        this.nextStamp = i3;
    }

    private void invalidateWeekFields() {
        int[] iArr = this.stamp;
        if (iArr[4] == 1 || iArr[3] == 1) {
            Calendar calendar = (Calendar) clone();
            calendar.setLenient(true);
            calendar.clear(4);
            calendar.clear(3);
            if (this.stamp[4] == 1) {
                int i = calendar.get(4);
                int[] iArr2 = this.fields;
                if (iArr2[4] != i) {
                    iArr2[4] = i;
                }
            }
            if (this.stamp[3] == 1) {
                int i2 = calendar.get(3);
                int[] iArr3 = this.fields;
                if (iArr3[3] != i2) {
                    iArr3[3] = i2;
                }
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:1|2|(2:4|5)|6|7|8) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0008 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void writeObject(java.p026io.ObjectOutputStream r2) throws java.p026io.IOException {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.isTimeSet     // Catch:{ all -> 0x000d }
            if (r0 != 0) goto L_0x0008
            r1.updateTime()     // Catch:{ IllegalArgumentException -> 0x0008 }
        L_0x0008:
            r2.defaultWriteObject()     // Catch:{ all -> 0x000d }
            monitor-exit(r1)
            return
        L_0x000d:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Calendar.writeObject(java.io.ObjectOutputStream):void");
    }

    private static class CalendarAccessControlContext {
        private static final AccessControlContext INSTANCE;

        static {
            RuntimePermission runtimePermission = new RuntimePermission("accessClassInPackage.sun.util.calendar");
            PermissionCollection newPermissionCollection = runtimePermission.newPermissionCollection();
            newPermissionCollection.add(runtimePermission);
            INSTANCE = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain((CodeSource) null, newPermissionCollection)});
        }

        private CalendarAccessControlContext() {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003a, code lost:
        r5 = r5.getID();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.p026io.ObjectInputStream r5) throws java.p026io.IOException, java.lang.ClassNotFoundException {
        /*
            r4 = this;
            r5.defaultReadObject()
            r5 = 17
            int[] r0 = new int[r5]
            r4.stamp = r0
            int r0 = r4.serialVersionOnStream
            r1 = 2
            r2 = 1
            if (r0 < r1) goto L_0x0022
            r4.isTimeSet = r2
            int[] r0 = r4.fields
            if (r0 != 0) goto L_0x0019
            int[] r0 = new int[r5]
            r4.fields = r0
        L_0x0019:
            boolean[] r0 = r4.isSet
            if (r0 != 0) goto L_0x0032
            boolean[] r5 = new boolean[r5]
            r4.isSet = r5
            goto L_0x0032
        L_0x0022:
            if (r0 < 0) goto L_0x0032
            r0 = 0
        L_0x0025:
            if (r0 >= r5) goto L_0x0032
            int[] r1 = r4.stamp
            boolean[] r3 = r4.isSet
            boolean r3 = r3[r0]
            r1[r0] = r3
            int r0 = r0 + 1
            goto L_0x0025
        L_0x0032:
            r4.serialVersionOnStream = r2
            java.util.TimeZone r5 = r4.zone
            boolean r0 = r5 instanceof java.util.SimpleTimeZone
            if (r0 == 0) goto L_0x0058
            java.lang.String r5 = r5.getID()
            java.util.TimeZone r0 = java.util.TimeZone.getTimeZone((java.lang.String) r5)
            if (r0 == 0) goto L_0x0058
            java.util.TimeZone r1 = r4.zone
            boolean r1 = r0.hasSameRules(r1)
            if (r1 == 0) goto L_0x0058
            java.lang.String r1 = r0.getID()
            boolean r5 = r1.equals(r5)
            if (r5 == 0) goto L_0x0058
            r4.zone = r0
        L_0x0058:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Calendar.readObject(java.io.ObjectInputStream):void");
    }

    public final Instant toInstant() {
        return Instant.ofEpochMilli(getTimeInMillis());
    }
}
