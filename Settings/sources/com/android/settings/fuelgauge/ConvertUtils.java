package com.android.settings.fuelgauge;

import android.content.ContentValues;
import android.content.Context;
import android.os.LocaleList;
import android.text.format.DateFormat;
import com.android.settings.overlay.FeatureFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes.dex */
public final class ConvertUtils {
    private static final Map<String, BatteryHistEntry> EMPTY_BATTERY_MAP = new HashMap();
    private static final BatteryHistEntry EMPTY_BATTERY_HIST_ENTRY = new BatteryHistEntry(new ContentValues());
    static double PERCENTAGE_OF_TOTAL_THRESHOLD = 1.0d;

    private static double getDiffValue(double d, double d2, double d3) {
        double d4 = 0.0d;
        double d5 = d2 > d ? d2 - d : 0.0d;
        if (d3 > d2) {
            d4 = d3 - d2;
        }
        return d5 + d4;
    }

    private static long getDiffValue(long j, long j2, long j3) {
        long j4 = 0;
        long j5 = j2 > j ? j2 - j : 0L;
        if (j3 > j2) {
            j4 = j3 - j2;
        }
        return j5 + j4;
    }

    public static String utcToLocalTime(Context context, long j) {
        return DateFormat.format(DateFormat.getBestDateTimePattern(getLocale(context), "MMM dd,yyyy HH:mm:ss"), j).toString();
    }

    public static String utcToLocalTimeHour(Context context, long j, boolean z) {
        Locale locale = getLocale(context);
        return DateFormat.format(DateFormat.getBestDateTimePattern(locale, z ? "HHm" : "ha"), j).toString().toLowerCase(locale);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x00ee, code lost:
        if (r5 == 0.0d) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Map<Integer, List<BatteryDiffEntry>> getIndexedUsageMap(Context context, int i, long[] jArr, Map<Long, Map<String, BatteryHistEntry>> map, boolean z) {
        HashMap hashMap;
        Map<Long, Map<String, BatteryHistEntry>> map2 = map;
        if (map2 == null || map.isEmpty()) {
            return new HashMap();
        }
        HashMap hashMap2 = new HashMap();
        int i2 = 0;
        while (i2 < i) {
            int i3 = i2 * 2;
            Long valueOf = Long.valueOf(jArr[i3]);
            Long valueOf2 = Long.valueOf(jArr[i3 + 1]);
            Long valueOf3 = Long.valueOf(jArr[i3 + 2]);
            Map<String, BatteryHistEntry> map3 = EMPTY_BATTERY_MAP;
            Map<String, BatteryHistEntry> orDefault = map2.getOrDefault(valueOf, map3);
            Map<String, BatteryHistEntry> orDefault2 = map2.getOrDefault(valueOf2, map3);
            Map<String, BatteryHistEntry> orDefault3 = map2.getOrDefault(valueOf3, map3);
            if (orDefault.isEmpty() || orDefault2.isEmpty() || orDefault3.isEmpty()) {
                hashMap = hashMap2;
                hashMap.put(Integer.valueOf(i2), new ArrayList());
            } else {
                HashSet hashSet = new HashSet();
                hashSet.addAll(orDefault.keySet());
                hashSet.addAll(orDefault2.keySet());
                hashSet.addAll(orDefault3.keySet());
                ArrayList<BatteryDiffEntry> arrayList = new ArrayList();
                hashMap2.put(Integer.valueOf(i2), arrayList);
                Iterator it = hashSet.iterator();
                double d = 0.0d;
                while (it.hasNext()) {
                    String str = (String) it.next();
                    BatteryHistEntry batteryHistEntry = EMPTY_BATTERY_HIST_ENTRY;
                    BatteryHistEntry orDefault4 = orDefault.getOrDefault(str, batteryHistEntry);
                    BatteryHistEntry orDefault5 = orDefault2.getOrDefault(str, batteryHistEntry);
                    BatteryHistEntry orDefault6 = orDefault3.getOrDefault(str, batteryHistEntry);
                    Map<String, BatteryHistEntry> map4 = orDefault3;
                    Map<String, BatteryHistEntry> map5 = orDefault;
                    Map<String, BatteryHistEntry> map6 = orDefault2;
                    long diffValue = getDiffValue(orDefault4.mForegroundUsageTimeInMs, orDefault5.mForegroundUsageTimeInMs, orDefault6.mForegroundUsageTimeInMs);
                    HashMap hashMap3 = hashMap2;
                    long diffValue2 = getDiffValue(orDefault4.mBackgroundUsageTimeInMs, orDefault5.mBackgroundUsageTimeInMs, orDefault6.mBackgroundUsageTimeInMs);
                    Iterator it2 = it;
                    ArrayList arrayList2 = arrayList;
                    double diffValue3 = getDiffValue(orDefault4.mConsumePower, orDefault5.mConsumePower, orDefault6.mConsumePower);
                    if (diffValue != 0 || diffValue2 != 0) {
                    }
                    BatteryHistEntry selectBatteryHistEntry = selectBatteryHistEntry(orDefault4, orDefault5, orDefault6);
                    if (selectBatteryHistEntry != null) {
                        float f = (float) (diffValue + diffValue2);
                        if (f > 7200000.0f) {
                            float f2 = 7200000.0f / f;
                            diffValue = Math.round(((float) diffValue) * f2);
                            diffValue2 = Math.round(((float) diffValue2) * f2);
                            diffValue3 *= f2;
                        }
                        double d2 = diffValue3;
                        d += d2;
                        arrayList2.add(new BatteryDiffEntry(context, diffValue, diffValue2, d2, selectBatteryHistEntry));
                        arrayList = arrayList2;
                        orDefault3 = map4;
                        orDefault = map5;
                        orDefault2 = map6;
                        hashMap2 = hashMap3;
                        it = it2;
                    }
                    orDefault3 = map4;
                    orDefault = map5;
                    orDefault2 = map6;
                    hashMap2 = hashMap3;
                    arrayList = arrayList2;
                    it = it2;
                }
                HashMap hashMap4 = hashMap2;
                for (BatteryDiffEntry batteryDiffEntry : arrayList) {
                    batteryDiffEntry.setTotalConsumePower(d);
                }
                hashMap = hashMap4;
            }
            i2++;
            map2 = map;
            hashMap2 = hashMap;
        }
        HashMap hashMap5 = hashMap2;
        insert24HoursData(-1, hashMap5);
        if (z) {
            purgeLowPercentageAndFakeData(context, hashMap5);
        }
        return hashMap5;
    }

    private static void insert24HoursData(int i, Map<Integer, List<BatteryDiffEntry>> map) {
        HashMap hashMap = new HashMap();
        double d = 0.0d;
        for (List<BatteryDiffEntry> list : map.values()) {
            for (BatteryDiffEntry batteryDiffEntry : list) {
                String key = batteryDiffEntry.mBatteryHistEntry.getKey();
                BatteryDiffEntry batteryDiffEntry2 = (BatteryDiffEntry) hashMap.get(key);
                if (batteryDiffEntry2 == null) {
                    hashMap.put(key, batteryDiffEntry.m341clone());
                } else {
                    batteryDiffEntry2.mForegroundUsageTimeInMs += batteryDiffEntry.mForegroundUsageTimeInMs;
                    batteryDiffEntry2.mBackgroundUsageTimeInMs += batteryDiffEntry.mBackgroundUsageTimeInMs;
                    batteryDiffEntry2.mConsumePower += batteryDiffEntry.mConsumePower;
                }
                d += batteryDiffEntry.mConsumePower;
            }
        }
        ArrayList<BatteryDiffEntry> arrayList = new ArrayList(hashMap.values());
        for (BatteryDiffEntry batteryDiffEntry3 : arrayList) {
            batteryDiffEntry3.setTotalConsumePower(d);
        }
        map.put(Integer.valueOf(i), arrayList);
    }

    private static void purgeLowPercentageAndFakeData(Context context, Map<Integer, List<BatteryDiffEntry>> map) {
        List<CharSequence> hideBackgroundUsageTimeList = FeatureFactory.getFactory(context).getPowerUsageFeatureProvider(context).getHideBackgroundUsageTimeList(context);
        for (List<BatteryDiffEntry> list : map.values()) {
            Iterator<BatteryDiffEntry> it = list.iterator();
            while (it.hasNext()) {
                BatteryDiffEntry next = it.next();
                if (next.getPercentOfTotal() < PERCENTAGE_OF_TOTAL_THRESHOLD || "fake_package".equals(next.getPackageName())) {
                    it.remove();
                }
                String packageName = next.getPackageName();
                if (packageName != null && !hideBackgroundUsageTimeList.isEmpty() && hideBackgroundUsageTimeList.contains(packageName)) {
                    next.mBackgroundUsageTimeInMs = 0L;
                }
            }
        }
    }

    private static BatteryHistEntry selectBatteryHistEntry(BatteryHistEntry batteryHistEntry, BatteryHistEntry batteryHistEntry2, BatteryHistEntry batteryHistEntry3) {
        if (batteryHistEntry == null || batteryHistEntry == EMPTY_BATTERY_HIST_ENTRY) {
            if (batteryHistEntry2 != null && batteryHistEntry2 != EMPTY_BATTERY_HIST_ENTRY) {
                return batteryHistEntry2;
            }
            if (batteryHistEntry3 != null && batteryHistEntry3 != EMPTY_BATTERY_HIST_ENTRY) {
                return batteryHistEntry3;
            }
            return null;
        }
        return batteryHistEntry;
    }

    static Locale getLocale(Context context) {
        if (context == null) {
            return Locale.getDefault();
        }
        LocaleList locales = context.getResources().getConfiguration().getLocales();
        return (locales == null || locales.isEmpty()) ? Locale.getDefault() : locales.get(0);
    }
}
