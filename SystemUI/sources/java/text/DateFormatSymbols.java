package java.text;

import java.lang.ref.SoftReference;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import libcore.icu.ICU;
import libcore.icu.LocaleData;
import libcore.icu.TimeZoneNames;

public class DateFormatSymbols implements Serializable, Cloneable {
    static final int PATTERN_AM_PM = 14;
    static final int PATTERN_DAY_OF_MONTH = 3;
    static final int PATTERN_DAY_OF_WEEK = 9;
    static final int PATTERN_DAY_OF_WEEK_IN_MONTH = 11;
    static final int PATTERN_DAY_OF_YEAR = 10;
    static final int PATTERN_DAY_PERIOD = 24;
    static final int PATTERN_ERA = 0;
    static final int PATTERN_FLEXIBLE_DAY_PERIOD = 25;
    static final int PATTERN_HOUR0 = 16;
    static final int PATTERN_HOUR1 = 15;
    static final int PATTERN_HOUR_OF_DAY0 = 5;
    static final int PATTERN_HOUR_OF_DAY1 = 4;
    static final int PATTERN_ISO_DAY_OF_WEEK = 20;
    static final int PATTERN_ISO_ZONE = 21;
    static final int PATTERN_MILLISECOND = 8;
    static final int PATTERN_MINUTE = 6;
    static final int PATTERN_MONTH = 2;
    static final int PATTERN_MONTH_STANDALONE = 22;
    static final int PATTERN_SECOND = 7;
    static final int PATTERN_STANDALONE_DAY_OF_WEEK = 23;
    static final int PATTERN_WEEK_OF_MONTH = 13;
    static final int PATTERN_WEEK_OF_YEAR = 12;
    static final int PATTERN_WEEK_YEAR = 19;
    static final int PATTERN_YEAR = 1;
    static final int PATTERN_ZONE_NAME = 17;
    static final int PATTERN_ZONE_VALUE = 18;
    private static final ConcurrentMap<Locale, SoftReference<DateFormatSymbols>> cachedInstances = new ConcurrentHashMap(3);
    static final int currentSerialVersion = 1;
    static final int millisPerHour = 3600000;
    static final String patternChars = "GyMdkHmsSEDFwWahKzZYuXLcbB";
    static final long serialVersionUID = -5987973545549424702L;
    String[] ampms = null;
    volatile transient int cachedHashCode;
    String[] eras = null;
    transient boolean isZoneStringsSet = false;
    private transient int lastZoneIndex;
    String localPatternChars = null;
    Locale locale = null;
    String[] months = null;
    private int serialVersionOnStream = 1;
    String[] shortMonths = null;
    private String[] shortStandAloneMonths;
    private String[] shortStandAloneWeekdays;
    String[] shortWeekdays = null;
    private String[] standAloneMonths;
    private String[] standAloneWeekdays;
    private String[] tinyMonths;
    private String[] tinyStandAloneMonths;
    private String[] tinyStandAloneWeekdays;
    private String[] tinyWeekdays;
    String[] weekdays = null;
    String[][] zoneStrings = null;

    public DateFormatSymbols() {
        initializeData(Locale.getDefault(Locale.Category.FORMAT));
    }

    public DateFormatSymbols(Locale locale2) {
        initializeData(locale2);
    }

    public static Locale[] getAvailableLocales() {
        return ICU.getAvailableLocales();
    }

    public static final DateFormatSymbols getInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormatSymbols getInstance(Locale locale2) {
        return (DateFormatSymbols) getCachedInstance(locale2).clone();
    }

    static final DateFormatSymbols getInstanceRef(Locale locale2) {
        return getCachedInstance(locale2);
    }

    private static DateFormatSymbols getCachedInstance(Locale locale2) {
        DateFormatSymbols dateFormatSymbols;
        Locale compatibleLocaleForBug159514442 = LocaleData.getCompatibleLocaleForBug159514442(locale2);
        ConcurrentMap<Locale, SoftReference<DateFormatSymbols>> concurrentMap = cachedInstances;
        SoftReference softReference = concurrentMap.get(compatibleLocaleForBug159514442);
        if (softReference != null && (dateFormatSymbols = (DateFormatSymbols) softReference.get()) != null) {
            return dateFormatSymbols;
        }
        DateFormatSymbols dateFormatSymbols2 = new DateFormatSymbols(locale2);
        SoftReference softReference2 = new SoftReference(dateFormatSymbols2);
        SoftReference putIfAbsent = concurrentMap.putIfAbsent(compatibleLocaleForBug159514442, softReference2);
        if (putIfAbsent == null) {
            return dateFormatSymbols2;
        }
        DateFormatSymbols dateFormatSymbols3 = (DateFormatSymbols) putIfAbsent.get();
        if (dateFormatSymbols3 != null) {
            return dateFormatSymbols3;
        }
        concurrentMap.put(compatibleLocaleForBug159514442, softReference2);
        return dateFormatSymbols2;
    }

    public String[] getEras() {
        String[] strArr = this.eras;
        return (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
    }

    public void setEras(String[] strArr) {
        this.eras = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getMonths() {
        String[] strArr = this.months;
        return (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
    }

    public void setMonths(String[] strArr) {
        this.months = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getShortMonths() {
        String[] strArr = this.shortMonths;
        return (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
    }

    public void setShortMonths(String[] strArr) {
        this.shortMonths = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getWeekdays() {
        String[] strArr = this.weekdays;
        return (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
    }

    public void setWeekdays(String[] strArr) {
        this.weekdays = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getShortWeekdays() {
        String[] strArr = this.shortWeekdays;
        return (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
    }

    public void setShortWeekdays(String[] strArr) {
        this.shortWeekdays = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getAmPmStrings() {
        String[] strArr = this.ampms;
        return (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
    }

    public void setAmPmStrings(String[] strArr) {
        this.ampms = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[][] getZoneStrings() {
        return getZoneStringsImpl(true);
    }

    public void setZoneStrings(String[][] strArr) {
        String[][] strArr2 = new String[strArr.length][];
        int i = 0;
        while (i < strArr.length) {
            String[] strArr3 = strArr[i];
            int length = strArr3.length;
            if (length >= 5) {
                strArr2[i] = (String[]) Arrays.copyOf((T[]) strArr3, length);
                i++;
            } else {
                throw new IllegalArgumentException();
            }
        }
        this.zoneStrings = strArr2;
        this.isZoneStringsSet = true;
    }

    public String getLocalPatternChars() {
        return this.localPatternChars;
    }

    public void setLocalPatternChars(String str) {
        this.localPatternChars = str.toString();
        this.cachedHashCode = 0;
    }

    /* access modifiers changed from: package-private */
    public String[] getTinyMonths() {
        return this.tinyMonths;
    }

    /* access modifiers changed from: package-private */
    public String[] getStandAloneMonths() {
        return this.standAloneMonths;
    }

    /* access modifiers changed from: package-private */
    public String[] getShortStandAloneMonths() {
        return this.shortStandAloneMonths;
    }

    /* access modifiers changed from: package-private */
    public String[] getTinyStandAloneMonths() {
        return this.tinyStandAloneMonths;
    }

    /* access modifiers changed from: package-private */
    public String[] getTinyWeekdays() {
        return this.tinyWeekdays;
    }

    /* access modifiers changed from: package-private */
    public String[] getStandAloneWeekdays() {
        return this.standAloneWeekdays;
    }

    /* access modifiers changed from: package-private */
    public String[] getShortStandAloneWeekdays() {
        return this.shortStandAloneWeekdays;
    }

    /* access modifiers changed from: package-private */
    public String[] getTinyStandAloneWeekdays() {
        return this.tinyStandAloneWeekdays;
    }

    public Object clone() {
        try {
            DateFormatSymbols dateFormatSymbols = (DateFormatSymbols) super.clone();
            copyMembers(this, dateFormatSymbols);
            return dateFormatSymbols;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    public int hashCode() {
        int i = this.cachedHashCode;
        if (i == 0 && (i = Objects.hashCode(this.localPatternChars) + ((((((((((((55 + Arrays.hashCode((Object[]) this.eras)) * 11) + Arrays.hashCode((Object[]) this.months)) * 11) + Arrays.hashCode((Object[]) this.shortMonths)) * 11) + Arrays.hashCode((Object[]) this.weekdays)) * 11) + Arrays.hashCode((Object[]) this.shortWeekdays)) * 11) + Arrays.hashCode((Object[]) this.ampms)) * 11)) != 0) {
            this.cachedHashCode = i;
        }
        return i;
    }

    public boolean equals(Object obj) {
        String str;
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            DateFormatSymbols dateFormatSymbols = (DateFormatSymbols) obj;
            if (Arrays.equals((Object[]) this.eras, (Object[]) dateFormatSymbols.eras) && Arrays.equals((Object[]) this.months, (Object[]) dateFormatSymbols.months) && Arrays.equals((Object[]) this.shortMonths, (Object[]) dateFormatSymbols.shortMonths) && Arrays.equals((Object[]) this.tinyMonths, (Object[]) dateFormatSymbols.tinyMonths) && Arrays.equals((Object[]) this.weekdays, (Object[]) dateFormatSymbols.weekdays) && Arrays.equals((Object[]) this.shortWeekdays, (Object[]) dateFormatSymbols.shortWeekdays) && Arrays.equals((Object[]) this.tinyWeekdays, (Object[]) dateFormatSymbols.tinyWeekdays) && Arrays.equals((Object[]) this.standAloneMonths, (Object[]) dateFormatSymbols.standAloneMonths) && Arrays.equals((Object[]) this.shortStandAloneMonths, (Object[]) dateFormatSymbols.shortStandAloneMonths) && Arrays.equals((Object[]) this.tinyStandAloneMonths, (Object[]) dateFormatSymbols.tinyStandAloneMonths) && Arrays.equals((Object[]) this.standAloneWeekdays, (Object[]) dateFormatSymbols.standAloneWeekdays) && Arrays.equals((Object[]) this.shortStandAloneWeekdays, (Object[]) dateFormatSymbols.shortStandAloneWeekdays) && Arrays.equals((Object[]) this.tinyStandAloneWeekdays, (Object[]) dateFormatSymbols.tinyStandAloneWeekdays) && Arrays.equals((Object[]) this.ampms, (Object[]) dateFormatSymbols.ampms) && (((str = this.localPatternChars) != null && str.equals(dateFormatSymbols.localPatternChars)) || (this.localPatternChars == null && dateFormatSymbols.localPatternChars == null))) {
                if (this.isZoneStringsSet || dateFormatSymbols.isZoneStringsSet || !Objects.equals(this.locale, dateFormatSymbols.locale)) {
                    return Arrays.deepEquals(getZoneStringsWrapper(), dateFormatSymbols.getZoneStringsWrapper());
                }
                return true;
            }
        }
        return false;
    }

    private void initializeData(Locale locale2) {
        DateFormatSymbols dateFormatSymbols;
        SoftReference softReference = cachedInstances.get(LocaleData.getCompatibleLocaleForBug159514442(locale2));
        if (softReference == null || (dateFormatSymbols = (DateFormatSymbols) softReference.get()) == null) {
            Locale mapInvalidAndNullLocales = LocaleData.mapInvalidAndNullLocales(locale2);
            LocaleData localeData = LocaleData.get(mapInvalidAndNullLocales);
            this.locale = mapInvalidAndNullLocales;
            this.eras = localeData.eras;
            this.months = localeData.longMonthNames;
            this.shortMonths = localeData.shortMonthNames;
            this.ampms = localeData.amPm;
            this.localPatternChars = patternChars;
            this.weekdays = localeData.longWeekdayNames;
            this.shortWeekdays = localeData.shortWeekdayNames;
            initializeSupplementaryData(localeData);
            return;
        }
        copyMembers(dateFormatSymbols, this);
    }

    private void initializeSupplementaryData(LocaleData localeData) {
        this.tinyMonths = localeData.tinyMonthNames;
        this.tinyWeekdays = localeData.tinyWeekdayNames;
        this.standAloneMonths = localeData.longStandAloneMonthNames;
        this.shortStandAloneMonths = localeData.shortStandAloneMonthNames;
        this.tinyStandAloneMonths = localeData.tinyStandAloneMonthNames;
        this.standAloneWeekdays = localeData.longStandAloneWeekdayNames;
        this.shortStandAloneWeekdays = localeData.shortStandAloneWeekdayNames;
        this.tinyStandAloneWeekdays = localeData.tinyStandAloneWeekdayNames;
    }

    /* access modifiers changed from: package-private */
    public final int getZoneIndex(String str) {
        String[][] zoneStringsWrapper = getZoneStringsWrapper();
        int i = this.lastZoneIndex;
        if (i < zoneStringsWrapper.length && str.equals(zoneStringsWrapper[i][0])) {
            return this.lastZoneIndex;
        }
        for (int i2 = 0; i2 < zoneStringsWrapper.length; i2++) {
            if (str.equals(zoneStringsWrapper[i2][0])) {
                this.lastZoneIndex = i2;
                return i2;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public final String[][] getZoneStringsWrapper() {
        if (isSubclassObject()) {
            return getZoneStrings();
        }
        return getZoneStringsImpl(false);
    }

    private synchronized String[][] internalZoneStrings() {
        if (this.zoneStrings == null) {
            this.zoneStrings = TimeZoneNames.getZoneStrings(this.locale);
        }
        return this.zoneStrings;
    }

    private String[][] getZoneStringsImpl(boolean z) {
        String[][] internalZoneStrings = internalZoneStrings();
        if (!z) {
            return internalZoneStrings;
        }
        int length = internalZoneStrings.length;
        String[][] strArr = new String[length][];
        for (int i = 0; i < length; i++) {
            String[] strArr2 = internalZoneStrings[i];
            strArr[i] = (String[]) Arrays.copyOf((T[]) strArr2, strArr2.length);
        }
        return strArr;
    }

    private boolean isSubclassObject() {
        return !getClass().getName().equals("java.text.DateFormatSymbols");
    }

    private void copyMembers(DateFormatSymbols dateFormatSymbols, DateFormatSymbols dateFormatSymbols2) {
        dateFormatSymbols2.locale = dateFormatSymbols.locale;
        String[] strArr = dateFormatSymbols.eras;
        dateFormatSymbols2.eras = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
        String[] strArr2 = dateFormatSymbols.months;
        dateFormatSymbols2.months = (String[]) Arrays.copyOf((T[]) strArr2, strArr2.length);
        String[] strArr3 = dateFormatSymbols.shortMonths;
        dateFormatSymbols2.shortMonths = (String[]) Arrays.copyOf((T[]) strArr3, strArr3.length);
        String[] strArr4 = dateFormatSymbols.weekdays;
        dateFormatSymbols2.weekdays = (String[]) Arrays.copyOf((T[]) strArr4, strArr4.length);
        String[] strArr5 = dateFormatSymbols.shortWeekdays;
        dateFormatSymbols2.shortWeekdays = (String[]) Arrays.copyOf((T[]) strArr5, strArr5.length);
        String[] strArr6 = dateFormatSymbols.ampms;
        dateFormatSymbols2.ampms = (String[]) Arrays.copyOf((T[]) strArr6, strArr6.length);
        if (dateFormatSymbols.zoneStrings != null) {
            dateFormatSymbols2.zoneStrings = dateFormatSymbols.getZoneStringsImpl(true);
        } else {
            dateFormatSymbols2.zoneStrings = null;
        }
        dateFormatSymbols2.localPatternChars = dateFormatSymbols.localPatternChars;
        dateFormatSymbols2.cachedHashCode = 0;
        dateFormatSymbols2.tinyMonths = dateFormatSymbols.tinyMonths;
        dateFormatSymbols2.tinyWeekdays = dateFormatSymbols.tinyWeekdays;
        dateFormatSymbols2.standAloneMonths = dateFormatSymbols.standAloneMonths;
        dateFormatSymbols2.shortStandAloneMonths = dateFormatSymbols.shortStandAloneMonths;
        dateFormatSymbols2.tinyStandAloneMonths = dateFormatSymbols.tinyStandAloneMonths;
        dateFormatSymbols2.standAloneWeekdays = dateFormatSymbols.standAloneWeekdays;
        dateFormatSymbols2.shortStandAloneWeekdays = dateFormatSymbols.shortStandAloneWeekdays;
        dateFormatSymbols2.tinyStandAloneWeekdays = dateFormatSymbols.tinyStandAloneWeekdays;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            initializeSupplementaryData(LocaleData.get(this.locale));
        }
        this.serialVersionOnStream = 1;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        internalZoneStrings();
        objectOutputStream.defaultWriteObject();
    }
}
