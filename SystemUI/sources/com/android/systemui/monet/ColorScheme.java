package com.android.systemui.monet;

import android.app.WallpaperColors;
import android.graphics.Color;
import com.android.internal.graphics.cam.Cam;
import com.android.internal.graphics.cam.CamUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt__MathJVMKt;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: ColorScheme.kt */
/* loaded from: classes.dex */
public final class ColorScheme {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final List<Integer> accent1;
    @NotNull
    private final List<Integer> accent2;
    @NotNull
    private final List<Integer> accent3;
    private final boolean darkTheme;
    @NotNull
    private final List<Integer> neutral1;
    @NotNull
    private final List<Integer> neutral2;

    public static final int getSeedColor(@NotNull WallpaperColors wallpaperColors) {
        return Companion.getSeedColor(wallpaperColors);
    }

    public ColorScheme(int i, boolean z) {
        float coerceAtLeast;
        List<Integer> list;
        List<Integer> list2;
        List<Integer> list3;
        List<Integer> list4;
        List<Integer> list5;
        this.darkTheme = z;
        Cam fromInt = Cam.fromInt((i == 0 || Cam.fromInt(i).getChroma() < 5.0f) ? -14979341 : i);
        float hue = fromInt.getHue();
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(fromInt.getChroma(), 48.0f);
        int[] of = Shades.of(hue, coerceAtLeast);
        Intrinsics.checkNotNullExpressionValue(of, "of(hue, chroma)");
        list = ArraysKt___ArraysKt.toList(of);
        this.accent1 = list;
        int[] of2 = Shades.of(hue, 16.0f);
        Intrinsics.checkNotNullExpressionValue(of2, "of(hue, ACCENT2_CHROMA)");
        list2 = ArraysKt___ArraysKt.toList(of2);
        this.accent2 = list2;
        int[] of3 = Shades.of(60.0f + hue, 32.0f);
        Intrinsics.checkNotNullExpressionValue(of3, "of(hue + ACCENT3_HUE_SHIFT, ACCENT3_CHROMA)");
        list3 = ArraysKt___ArraysKt.toList(of3);
        this.accent3 = list3;
        int[] of4 = Shades.of(hue, 4.0f);
        Intrinsics.checkNotNullExpressionValue(of4, "of(hue, NEUTRAL1_CHROMA)");
        list4 = ArraysKt___ArraysKt.toList(of4);
        this.neutral1 = list4;
        int[] of5 = Shades.of(hue, 8.0f);
        Intrinsics.checkNotNullExpressionValue(of5, "of(hue, NEUTRAL2_CHROMA)");
        list5 = ArraysKt___ArraysKt.toList(of5);
        this.neutral2 = list5;
    }

    @NotNull
    public final List<Integer> getAccent1() {
        return this.accent1;
    }

    @NotNull
    public final List<Integer> getAllAccentColors() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.accent1);
        arrayList.addAll(this.accent2);
        arrayList.addAll(this.accent3);
        return arrayList;
    }

    @NotNull
    public final List<Integer> getAllNeutralColors() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.neutral1);
        arrayList.addAll(this.neutral2);
        return arrayList;
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ColorScheme {\n  neutral1: ");
        Companion companion = Companion;
        sb.append(companion.humanReadable(this.neutral1));
        sb.append("\n  neutral2: ");
        sb.append(companion.humanReadable(this.neutral2));
        sb.append("\n  accent1: ");
        sb.append(companion.humanReadable(this.accent1));
        sb.append("\n  accent2: ");
        sb.append(companion.humanReadable(this.accent2));
        sb.append("\n  accent3: ");
        sb.append(companion.humanReadable(this.accent3));
        sb.append("\n}");
        return sb.toString();
    }

    /* compiled from: ColorScheme.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int getSeedColor(@NotNull WallpaperColors wallpaperColors) {
            Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
            return ((Number) CollectionsKt.first((List<? extends Object>) getSeedColors(wallpaperColors))).intValue();
        }

        @NotNull
        public final List<Integer> getSeedColors(@NotNull WallpaperColors wallpaperColors) {
            int mapCapacity;
            int mapCapacity2;
            int mapCapacity3;
            int mapCapacity4;
            List<Map.Entry> mutableList;
            Object obj;
            boolean z;
            int roundToInt;
            int collectionSizeOrDefault;
            List distinct;
            List<Integer> list;
            List<Integer> listOf;
            Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
            Iterator it = wallpaperColors.getAllColors().values().iterator();
            if (it.hasNext()) {
                Object next = it.next();
                while (it.hasNext()) {
                    Integer b = (Integer) it.next();
                    int intValue = ((Integer) next).intValue();
                    Intrinsics.checkNotNullExpressionValue(b, "b");
                    next = Integer.valueOf(intValue + b.intValue());
                }
                double intValue2 = ((Number) next).intValue();
                boolean z2 = intValue2 == 0.0d;
                if (z2) {
                    List<Color> mainColors = wallpaperColors.getMainColors();
                    Intrinsics.checkNotNullExpressionValue(mainColors, "wallpaperColors.mainColors");
                    collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(mainColors, 10);
                    ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
                    for (Color color : mainColors) {
                        arrayList.add(Integer.valueOf(color.toArgb()));
                    }
                    distinct = CollectionsKt___CollectionsKt.distinct(arrayList);
                    ArrayList arrayList2 = new ArrayList();
                    for (Object obj2 : distinct) {
                        if (Cam.fromInt(((Number) obj2).intValue()).getChroma() >= 5.0f) {
                            arrayList2.add(obj2);
                        }
                    }
                    list = CollectionsKt___CollectionsKt.toList(arrayList2);
                    if (!list.isEmpty()) {
                        return list;
                    }
                    listOf = CollectionsKt__CollectionsJVMKt.listOf(-14979341);
                    return listOf;
                }
                Map allColors = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors, "wallpaperColors.allColors");
                mapCapacity = MapsKt__MapsJVMKt.mapCapacity(allColors.size());
                Map<Integer, Double> linkedHashMap = new LinkedHashMap<>(mapCapacity);
                for (Map.Entry entry : allColors.entrySet()) {
                    linkedHashMap.put(entry.getKey(), Double.valueOf(((Number) entry.getValue()).intValue() / intValue2));
                }
                Map allColors2 = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors2, "wallpaperColors.allColors");
                mapCapacity2 = MapsKt__MapsJVMKt.mapCapacity(allColors2.size());
                Map<Integer, ? extends Cam> linkedHashMap2 = new LinkedHashMap<>(mapCapacity2);
                for (Map.Entry entry2 : allColors2.entrySet()) {
                    Object key = entry2.getKey();
                    Object key2 = entry2.getKey();
                    Intrinsics.checkNotNullExpressionValue(key2, "it.key");
                    linkedHashMap2.put(key, Cam.fromInt(((Number) key2).intValue()));
                }
                List<Double> huePopulations = huePopulations(linkedHashMap2, linkedHashMap);
                Map allColors3 = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors3, "wallpaperColors.allColors");
                mapCapacity3 = MapsKt__MapsJVMKt.mapCapacity(allColors3.size());
                LinkedHashMap linkedHashMap3 = new LinkedHashMap(mapCapacity3);
                for (Map.Entry entry3 : allColors3.entrySet()) {
                    Object key3 = entry3.getKey();
                    Cam cam = (Cam) linkedHashMap2.get(entry3.getKey());
                    Intrinsics.checkNotNull(cam);
                    roundToInt = MathKt__MathJVMKt.roundToInt(cam.getHue());
                    int i = roundToInt - 15;
                    int i2 = roundToInt + 15;
                    double d = 0.0d;
                    if (i <= i2) {
                        while (true) {
                            int i3 = i + 1;
                            d += huePopulations.get(ColorScheme.Companion.wrapDegrees(i)).doubleValue();
                            if (i == i2) {
                                break;
                            }
                            i = i3;
                        }
                    }
                    linkedHashMap3.put(key3, Double.valueOf(d));
                }
                LinkedHashMap linkedHashMap4 = new LinkedHashMap();
                for (Map.Entry<Integer, ? extends Cam> entry4 : linkedHashMap2.entrySet()) {
                    Integer key4 = entry4.getKey();
                    Intrinsics.checkNotNullExpressionValue(key4, "it.key");
                    CamUtils.lstarFromInt(key4.intValue());
                    Double d2 = (Double) linkedHashMap3.get(entry4.getKey());
                    Intrinsics.checkNotNull(d2);
                    if (((Cam) entry4.getValue()).getChroma() >= 5.0f && (z2 || d2.doubleValue() > 0.01d)) {
                        linkedHashMap4.put(entry4.getKey(), entry4.getValue());
                    }
                }
                ArrayList arrayList3 = new ArrayList();
                mapCapacity4 = MapsKt__MapsJVMKt.mapCapacity(linkedHashMap4.size());
                LinkedHashMap linkedHashMap5 = new LinkedHashMap(mapCapacity4);
                for (Map.Entry entry5 : linkedHashMap4.entrySet()) {
                    Object key5 = entry5.getKey();
                    Companion companion = ColorScheme.Companion;
                    Object value = entry5.getValue();
                    Intrinsics.checkNotNullExpressionValue(value, "it.value");
                    Double d3 = (Double) linkedHashMap3.get(entry5.getKey());
                    Intrinsics.checkNotNull(d3);
                    linkedHashMap5.put(key5, Double.valueOf(companion.score((Cam) value, d3.doubleValue())));
                }
                mutableList = CollectionsKt___CollectionsKt.toMutableList((Collection) linkedHashMap5.entrySet());
                if (mutableList.size() > 1) {
                    CollectionsKt__MutableCollectionsJVMKt.sortWith(mutableList, new Comparator<T>() { // from class: com.android.systemui.monet.ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1
                        @Override // java.util.Comparator
                        public final int compare(T t, T t2) {
                            int compareValues;
                            compareValues = ComparisonsKt__ComparisonsKt.compareValues((Double) ((Map.Entry) t2).getValue(), (Double) ((Map.Entry) t).getValue());
                            return compareValues;
                        }
                    });
                }
                for (Map.Entry entry6 : mutableList) {
                    Integer num = (Integer) entry6.getKey();
                    Iterator it2 = arrayList3.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            obj = null;
                            break;
                        }
                        obj = it2.next();
                        int intValue3 = ((Number) obj).intValue();
                        Cam cam2 = (Cam) linkedHashMap2.get(num);
                        Intrinsics.checkNotNull(cam2);
                        float hue = cam2.getHue();
                        Cam cam3 = (Cam) linkedHashMap2.get(Integer.valueOf(intValue3));
                        Intrinsics.checkNotNull(cam3);
                        if (ColorScheme.Companion.hueDiff(hue, cam3.getHue()) < 15.0f) {
                            z = true;
                            continue;
                        } else {
                            z = false;
                            continue;
                        }
                        if (z) {
                            break;
                        }
                    }
                    if (!(obj != null)) {
                        Intrinsics.checkNotNullExpressionValue(num, "int");
                        arrayList3.add(num);
                    }
                }
                if (arrayList3.isEmpty()) {
                    arrayList3.add(-14979341);
                }
                return arrayList3;
            }
            throw new UnsupportedOperationException("Empty collection can't be reduced.");
        }

        private final int wrapDegrees(int i) {
            if (i < 0) {
                return (i % 360) + 360;
            }
            return i >= 360 ? i % 360 : i;
        }

        private final float hueDiff(float f, float f2) {
            return 180.0f - Math.abs(Math.abs(f - f2) - 180.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String humanReadable(List<Integer> list) {
            String joinToString$default;
            joinToString$default = CollectionsKt___CollectionsKt.joinToString$default(list, null, null, null, 0, null, ColorScheme$Companion$humanReadable$1.INSTANCE, 31, null);
            return joinToString$default;
        }

        private final double score(Cam cam, double d) {
            double d2;
            float chroma;
            double d3 = d * 70.0d;
            if (cam.getChroma() < 48.0f) {
                d2 = 0.1d;
                chroma = cam.getChroma();
            } else {
                d2 = 0.3d;
                chroma = cam.getChroma();
            }
            return ((chroma - 48.0f) * d2) + d3;
        }

        private final List<Double> huePopulations(Map<Integer, ? extends Cam> map, Map<Integer, Double> map2) {
            List<Double> mutableList;
            int roundToInt;
            ArrayList arrayList = new ArrayList(360);
            for (int i = 0; i < 360; i++) {
                arrayList.add(Double.valueOf(0.0d));
            }
            mutableList = CollectionsKt___CollectionsKt.toMutableList((Collection) arrayList);
            for (Map.Entry<Integer, Double> entry : map2.entrySet()) {
                Double d = map2.get(entry.getKey());
                Intrinsics.checkNotNull(d);
                double doubleValue = d.doubleValue();
                Cam cam = map.get(entry.getKey());
                Intrinsics.checkNotNull(cam);
                roundToInt = MathKt__MathJVMKt.roundToInt(cam.getHue());
                int i2 = roundToInt % 360;
                mutableList.set(i2, Double.valueOf(mutableList.get(i2).doubleValue() + doubleValue));
            }
            return mutableList;
        }
    }
}
