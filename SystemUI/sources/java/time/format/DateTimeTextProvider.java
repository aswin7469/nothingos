package java.time.format;

import android.icu.text.DateFormatSymbols;
import android.icu.util.ULocale;
import com.android.icu.text.ExtendedDateFormatSymbols;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalField;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.locale.provider.CalendarDataUtility;

class DateTimeTextProvider {
    private static final ConcurrentMap<Map.Entry<TemporalField, Locale>, Object> CACHE = new ConcurrentHashMap(16, 0.75f, 2);
    /* access modifiers changed from: private */
    public static final Comparator<Map.Entry<String, Long>> COMPARATOR = new Comparator<Map.Entry<String, Long>>() {
        public int compare(Map.Entry<String, Long> entry, Map.Entry<String, Long> entry2) {
            return entry2.getKey().length() - entry.getKey().length();
        }
    };
    private static final DateTimeTextProvider INSTANCE = new DateTimeTextProvider();

    private static int toWeekDay(int i) {
        if (i == 1) {
            return 7;
        }
        return i - 1;
    }

    DateTimeTextProvider() {
    }

    static DateTimeTextProvider getInstance() {
        return INSTANCE;
    }

    public String getText(TemporalField temporalField, long j, TextStyle textStyle, Locale locale) {
        Object findStore = findStore(temporalField, locale);
        if (findStore instanceof LocaleStore) {
            return ((LocaleStore) findStore).getText(j, textStyle);
        }
        return null;
    }

    public String getText(Chronology chronology, TemporalField temporalField, long j, TextStyle textStyle, Locale locale) {
        int i;
        int i2;
        if (chronology == IsoChronology.INSTANCE || !(temporalField instanceof ChronoField)) {
            return getText(temporalField, j, textStyle, locale);
        }
        int i3 = 2;
        if (temporalField == ChronoField.ERA) {
            i = 0;
            if (chronology != JapaneseChronology.INSTANCE) {
                i2 = (int) j;
            } else if (j == -999) {
                i3 = 0;
            } else {
                i2 = ((int) j) + 2;
            }
            i3 = 0;
            i = i2;
        } else if (temporalField == ChronoField.MONTH_OF_YEAR) {
            i = ((int) j) - 1;
        } else if (temporalField == ChronoField.DAY_OF_WEEK) {
            i = ((int) j) + 1;
            i3 = 7;
            if (i > 7) {
                i = 1;
            }
        } else if (temporalField != ChronoField.AMPM_OF_DAY) {
            return null;
        } else {
            i = (int) j;
            i3 = 9;
        }
        return CalendarDataUtility.retrieveJavaTimeFieldValueName(chronology.getCalendarType(), i3, i, textStyle.toCalendarStyle(), locale);
    }

    public Iterator<Map.Entry<String, Long>> getTextIterator(TemporalField temporalField, TextStyle textStyle, Locale locale) {
        Object findStore = findStore(temporalField, locale);
        if (findStore instanceof LocaleStore) {
            return ((LocaleStore) findStore).getTextIterator(textStyle);
        }
        return null;
    }

    public Iterator<Map.Entry<String, Long>> getTextIterator(Chronology chronology, TemporalField temporalField, TextStyle textStyle, Locale locale) {
        int i;
        if (chronology == IsoChronology.INSTANCE || !(temporalField instanceof ChronoField)) {
            return getTextIterator(temporalField, textStyle, locale);
        }
        int i2 = C28892.$SwitchMap$java$time$temporal$ChronoField[((ChronoField) temporalField).ordinal()];
        int i3 = 0;
        if (i2 == 1) {
            i = 0;
        } else if (i2 == 2) {
            i = 2;
        } else if (i2 == 3) {
            i = 7;
        } else if (i2 != 4) {
            return null;
        } else {
            i = 9;
        }
        if (textStyle != null) {
            i3 = textStyle.toCalendarStyle();
        }
        Map<String, Integer> retrieveJavaTimeFieldValueNames = CalendarDataUtility.retrieveJavaTimeFieldValueNames(chronology.getCalendarType(), i, i3, locale);
        if (retrieveJavaTimeFieldValueNames == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(retrieveJavaTimeFieldValueNames.size());
        if (i == 0) {
            for (Map.Entry next : retrieveJavaTimeFieldValueNames.entrySet()) {
                int intValue = ((Integer) next.getValue()).intValue();
                if (chronology == JapaneseChronology.INSTANCE) {
                    intValue = intValue == 0 ? -999 : intValue - 2;
                }
                arrayList.add(createEntry((String) next.getKey(), Long.valueOf((long) intValue)));
            }
        } else if (i == 2) {
            for (Map.Entry next2 : retrieveJavaTimeFieldValueNames.entrySet()) {
                arrayList.add(createEntry((String) next2.getKey(), Long.valueOf((long) (((Integer) next2.getValue()).intValue() + 1))));
            }
        } else if (i != 7) {
            for (Map.Entry next3 : retrieveJavaTimeFieldValueNames.entrySet()) {
                arrayList.add(createEntry((String) next3.getKey(), Long.valueOf((long) ((Integer) next3.getValue()).intValue())));
            }
        } else {
            for (Map.Entry next4 : retrieveJavaTimeFieldValueNames.entrySet()) {
                arrayList.add(createEntry((String) next4.getKey(), Long.valueOf((long) toWeekDay(((Integer) next4.getValue()).intValue()))));
            }
        }
        return arrayList.iterator();
    }

    /* renamed from: java.time.format.DateTimeTextProvider$2 */
    static /* synthetic */ class C28892 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoField;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                java.time.temporal.ChronoField[] r0 = java.time.temporal.ChronoField.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$temporal$ChronoField = r0
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.ERA     // Catch:{ NoSuchFieldError -> 0x0012 }
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
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_WEEK     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoField     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.AMPM_OF_DAY     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeTextProvider.C28892.<clinit>():void");
        }
    }

    private Object findStore(TemporalField temporalField, Locale locale) {
        Map.Entry createEntry = createEntry(temporalField, locale);
        ConcurrentMap<Map.Entry<TemporalField, Locale>, Object> concurrentMap = CACHE;
        Object obj = concurrentMap.get(createEntry);
        if (obj != null) {
            return obj;
        }
        concurrentMap.putIfAbsent(createEntry, createStore(temporalField, locale));
        return concurrentMap.get(createEntry);
    }

    private Object createStore(TemporalField temporalField, Locale locale) {
        Map<String, Integer> retrieveJavaTimeFieldValueNames;
        Map<String, Integer> retrieveJavaTimeFieldValueNames2;
        TemporalField temporalField2 = temporalField;
        Locale locale2 = locale;
        HashMap hashMap = new HashMap();
        int i = 0;
        if (temporalField2 == ChronoField.ERA) {
            for (TextStyle textStyle : TextStyle.values()) {
                if (!textStyle.isStandalone() && (retrieveJavaTimeFieldValueNames2 = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", 0, textStyle.toCalendarStyle(), locale2)) != null) {
                    HashMap hashMap2 = new HashMap();
                    for (Map.Entry next : retrieveJavaTimeFieldValueNames2.entrySet()) {
                        hashMap2.put(Long.valueOf((long) ((Integer) next.getValue()).intValue()), (String) next.getKey());
                    }
                    if (!hashMap2.isEmpty()) {
                        hashMap.put(textStyle, hashMap2);
                    }
                }
            }
            return new LocaleStore(hashMap);
        }
        int i2 = 1;
        if (temporalField2 == ChronoField.MONTH_OF_YEAR) {
            TextStyle[] values = TextStyle.values();
            int length = values.length;
            int i3 = 0;
            while (i3 < length) {
                TextStyle textStyle2 = values[i3];
                HashMap hashMap3 = new HashMap();
                int i4 = 11;
                int i5 = 2;
                if (textStyle2.equals(TextStyle.NARROW) || textStyle2.equals(TextStyle.NARROW_STANDALONE)) {
                    int i6 = 0;
                    while (i6 <= i4) {
                        String retrieveJavaTimeFieldValueName = CalendarDataUtility.retrieveJavaTimeFieldValueName("gregory", i5, i6, textStyle2.toCalendarStyle(), locale2);
                        if (retrieveJavaTimeFieldValueName == null) {
                            break;
                        }
                        hashMap3.put(Long.valueOf(((long) i6) + 1), retrieveJavaTimeFieldValueName);
                        i6++;
                        i4 = 11;
                        i5 = 2;
                    }
                } else {
                    Map<String, Integer> retrieveJavaTimeFieldValueNames3 = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", 2, textStyle2.toCalendarStyle(), locale2);
                    if (retrieveJavaTimeFieldValueNames3 != null) {
                        for (Map.Entry next2 : retrieveJavaTimeFieldValueNames3.entrySet()) {
                            hashMap3.put(Long.valueOf((long) (((Integer) next2.getValue()).intValue() + i2)), (String) next2.getKey());
                        }
                    } else {
                        for (int i7 = i; i7 <= 11; i7++) {
                            String retrieveJavaTimeFieldValueName2 = CalendarDataUtility.retrieveJavaTimeFieldValueName("gregory", 2, i7, textStyle2.toCalendarStyle(), locale2);
                            if (retrieveJavaTimeFieldValueName2 == null) {
                                break;
                            }
                            hashMap3.put(Long.valueOf(((long) i7) + 1), retrieveJavaTimeFieldValueName2);
                        }
                    }
                }
                if (!hashMap3.isEmpty()) {
                    hashMap.put(textStyle2, hashMap3);
                }
                i3++;
                i = 0;
                i2 = 1;
            }
            return new LocaleStore(hashMap);
        } else if (temporalField2 == ChronoField.DAY_OF_WEEK) {
            for (TextStyle textStyle3 : TextStyle.values()) {
                HashMap hashMap4 = new HashMap();
                if (textStyle3.equals(TextStyle.NARROW) || textStyle3.equals(TextStyle.NARROW_STANDALONE)) {
                    for (int i8 = 1; i8 <= 7; i8++) {
                        String retrieveJavaTimeFieldValueName3 = CalendarDataUtility.retrieveJavaTimeFieldValueName("gregory", 7, i8, textStyle3.toCalendarStyle(), locale2);
                        if (retrieveJavaTimeFieldValueName3 == null) {
                            break;
                        }
                        hashMap4.put(Long.valueOf((long) toWeekDay(i8)), retrieveJavaTimeFieldValueName3);
                    }
                } else {
                    Map<String, Integer> retrieveJavaTimeFieldValueNames4 = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", 7, textStyle3.toCalendarStyle(), locale2);
                    if (retrieveJavaTimeFieldValueNames4 != null) {
                        for (Map.Entry next3 : retrieveJavaTimeFieldValueNames4.entrySet()) {
                            hashMap4.put(Long.valueOf((long) toWeekDay(((Integer) next3.getValue()).intValue())), (String) next3.getKey());
                        }
                    } else {
                        for (int i9 = 1; i9 <= 7; i9++) {
                            String retrieveJavaTimeFieldValueName4 = CalendarDataUtility.retrieveJavaTimeFieldValueName("gregory", 7, i9, textStyle3.toCalendarStyle(), locale2);
                            if (retrieveJavaTimeFieldValueName4 == null) {
                                break;
                            }
                            hashMap4.put(Long.valueOf((long) toWeekDay(i9)), retrieveJavaTimeFieldValueName4);
                        }
                    }
                }
                if (!hashMap4.isEmpty()) {
                    hashMap.put(textStyle3, hashMap4);
                }
            }
            return new LocaleStore(hashMap);
        } else if (temporalField2 == ChronoField.AMPM_OF_DAY) {
            for (TextStyle textStyle4 : TextStyle.values()) {
                if (!textStyle4.isStandalone() && (retrieveJavaTimeFieldValueNames = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", 9, textStyle4.toCalendarStyle(), locale2)) != null) {
                    HashMap hashMap5 = new HashMap();
                    for (Map.Entry next4 : retrieveJavaTimeFieldValueNames.entrySet()) {
                        hashMap5.put(Long.valueOf((long) ((Integer) next4.getValue()).intValue()), (String) next4.getKey());
                    }
                    if (!hashMap5.isEmpty()) {
                        hashMap.put(textStyle4, hashMap5);
                    }
                }
            }
            return new LocaleStore(hashMap);
        } else if (temporalField2 != IsoFields.QUARTER_OF_YEAR) {
            return "";
        } else {
            ULocale forLocale = ULocale.forLocale(locale);
            forLocale.setKeywordValue("calendar", "gregorian");
            ExtendedDateFormatSymbols instance = ExtendedDateFormatSymbols.getInstance(forLocale);
            DateFormatSymbols dateFormatSymbols = instance.getDateFormatSymbols();
            hashMap.put(TextStyle.FULL, extractQuarters(dateFormatSymbols.getQuarters(0, 1)));
            hashMap.put(TextStyle.FULL_STANDALONE, extractQuarters(dateFormatSymbols.getQuarters(1, 1)));
            hashMap.put(TextStyle.SHORT, extractQuarters(dateFormatSymbols.getQuarters(0, 0)));
            hashMap.put(TextStyle.SHORT_STANDALONE, extractQuarters(dateFormatSymbols.getQuarters(1, 0)));
            hashMap.put(TextStyle.NARROW, extractQuarters(instance.getNarrowQuarters(0)));
            hashMap.put(TextStyle.NARROW_STANDALONE, extractQuarters(instance.getNarrowQuarters(1)));
            return new LocaleStore(hashMap);
        }
    }

    private static Map<Long, String> extractQuarters(String[] strArr) {
        HashMap hashMap = new HashMap();
        int i = 0;
        while (i < strArr.length) {
            int i2 = i + 1;
            hashMap.put(Long.valueOf((long) i2), strArr[i]);
            i = i2;
        }
        return hashMap;
    }

    /* access modifiers changed from: private */
    public static <A, B> Map.Entry<A, B> createEntry(A a, B b) {
        return new AbstractMap.SimpleImmutableEntry(a, b);
    }

    static final class LocaleStore {
        private final Map<TextStyle, List<Map.Entry<String, Long>>> parsable;
        private final Map<TextStyle, Map<Long, String>> valueTextMap;

        LocaleStore(Map<TextStyle, Map<Long, String>> map) {
            this.valueTextMap = map;
            HashMap hashMap = new HashMap();
            ArrayList arrayList = new ArrayList();
            for (Map.Entry next : map.entrySet()) {
                HashMap hashMap2 = new HashMap();
                for (Map.Entry entry : ((Map) next.getValue()).entrySet()) {
                    hashMap2.put((String) entry.getValue(), DateTimeTextProvider.createEntry((String) entry.getValue(), (Long) entry.getKey()));
                }
                ArrayList arrayList2 = new ArrayList(hashMap2.values());
                Collections.sort(arrayList2, DateTimeTextProvider.COMPARATOR);
                hashMap.put((TextStyle) next.getKey(), arrayList2);
                arrayList.addAll(arrayList2);
                hashMap.put(null, arrayList);
            }
            Collections.sort(arrayList, DateTimeTextProvider.COMPARATOR);
            this.parsable = hashMap;
        }

        /* access modifiers changed from: package-private */
        public String getText(long j, TextStyle textStyle) {
            Map map = this.valueTextMap.get(textStyle);
            if (map != null) {
                return (String) map.get(Long.valueOf(j));
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public Iterator<Map.Entry<String, Long>> getTextIterator(TextStyle textStyle) {
            List list = this.parsable.get(textStyle);
            if (list != null) {
                return list.iterator();
            }
            return null;
        }
    }
}
