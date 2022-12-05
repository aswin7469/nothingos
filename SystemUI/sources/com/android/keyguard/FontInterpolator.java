package com.android.keyguard;

import android.graphics.fonts.Font;
import android.graphics.fonts.FontVariationAxis;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: FontInterpolator.kt */
/* loaded from: classes.dex */
public final class FontInterpolator {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final FontVariationAxis[] EMPTY_AXES = new FontVariationAxis[0];
    @NotNull
    private final HashMap<InterpKey, Font> interpCache = new HashMap<>();
    @NotNull
    private final HashMap<VarFontKey, Font> verFontCache = new HashMap<>();
    @NotNull
    private final InterpKey tmpInterpKey = new InterpKey(null, null, 0.0f);
    @NotNull
    private final VarFontKey tmpVarFontKey = new VarFontKey(0, 0, new ArrayList());

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: FontInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class InterpKey {
        @Nullable
        private Font l;
        private float progress;
        @Nullable
        private Font r;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof InterpKey)) {
                return false;
            }
            InterpKey interpKey = (InterpKey) obj;
            return Intrinsics.areEqual(this.l, interpKey.l) && Intrinsics.areEqual(this.r, interpKey.r) && Intrinsics.areEqual(Float.valueOf(this.progress), Float.valueOf(interpKey.progress));
        }

        public int hashCode() {
            Font font = this.l;
            int i = 0;
            int hashCode = (font == null ? 0 : font.hashCode()) * 31;
            Font font2 = this.r;
            if (font2 != null) {
                i = font2.hashCode();
            }
            return ((hashCode + i) * 31) + Float.hashCode(this.progress);
        }

        @NotNull
        public String toString() {
            return "InterpKey(l=" + this.l + ", r=" + this.r + ", progress=" + this.progress + ')';
        }

        public InterpKey(@Nullable Font font, @Nullable Font font2, float f) {
            this.l = font;
            this.r = font2;
            this.progress = f;
        }

        public final void set(@NotNull Font l, @NotNull Font r, float f) {
            Intrinsics.checkNotNullParameter(l, "l");
            Intrinsics.checkNotNullParameter(r, "r");
            this.l = l;
            this.r = r;
            this.progress = f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: FontInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class VarFontKey {
        private int index;
        @NotNull
        private final List<FontVariationAxis> sortedAxes;
        private int sourceId;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof VarFontKey)) {
                return false;
            }
            VarFontKey varFontKey = (VarFontKey) obj;
            return this.sourceId == varFontKey.sourceId && this.index == varFontKey.index && Intrinsics.areEqual(this.sortedAxes, varFontKey.sortedAxes);
        }

        public int hashCode() {
            return (((Integer.hashCode(this.sourceId) * 31) + Integer.hashCode(this.index)) * 31) + this.sortedAxes.hashCode();
        }

        @NotNull
        public String toString() {
            return "VarFontKey(sourceId=" + this.sourceId + ", index=" + this.index + ", sortedAxes=" + this.sortedAxes + ')';
        }

        public VarFontKey(int i, int i2, @NotNull List<FontVariationAxis> sortedAxes) {
            Intrinsics.checkNotNullParameter(sortedAxes, "sortedAxes");
            this.sourceId = i;
            this.index = i2;
            this.sortedAxes = sortedAxes;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public VarFontKey(@NotNull Font font, @NotNull List<FontVariationAxis> axes) {
            this(r0, r4, r5);
            List mutableList;
            Intrinsics.checkNotNullParameter(font, "font");
            Intrinsics.checkNotNullParameter(axes, "axes");
            int sourceIdentifier = font.getSourceIdentifier();
            int ttcIndex = font.getTtcIndex();
            mutableList = CollectionsKt___CollectionsKt.toMutableList((Collection) axes);
            if (mutableList.size() > 1) {
                CollectionsKt__MutableCollectionsJVMKt.sortWith(mutableList, new Comparator<T>() { // from class: com.android.keyguard.FontInterpolator$VarFontKey$_init_$lambda-1$$inlined$sortBy$1
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        int compareValues;
                        compareValues = ComparisonsKt__ComparisonsKt.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                        return compareValues;
                    }
                });
            }
            Unit unit = Unit.INSTANCE;
        }

        public final void set(@NotNull Font font, @NotNull List<FontVariationAxis> axes) {
            Intrinsics.checkNotNullParameter(font, "font");
            Intrinsics.checkNotNullParameter(axes, "axes");
            this.sourceId = font.getSourceIdentifier();
            this.index = font.getTtcIndex();
            this.sortedAxes.clear();
            this.sortedAxes.addAll(axes);
            List<FontVariationAxis> list = this.sortedAxes;
            if (list.size() > 1) {
                CollectionsKt__MutableCollectionsJVMKt.sortWith(list, new Comparator<T>() { // from class: com.android.keyguard.FontInterpolator$VarFontKey$set$$inlined$sortBy$1
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        int compareValues;
                        compareValues = ComparisonsKt__ComparisonsKt.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                        return compareValues;
                    }
                });
            }
        }
    }

    @NotNull
    public final Font lerp(@NotNull Font start, @NotNull Font end, float f) {
        Intrinsics.checkNotNullParameter(start, "start");
        Intrinsics.checkNotNullParameter(end, "end");
        boolean z = true;
        if (f == 0.0f) {
            return start;
        }
        if (f == 1.0f) {
            return end;
        }
        FontVariationAxis[] axes = start.getAxes();
        if (axes == null) {
            axes = EMPTY_AXES;
        }
        FontVariationAxis[] axes2 = end.getAxes();
        if (axes2 == null) {
            axes2 = EMPTY_AXES;
        }
        if (axes.length == 0) {
            if (axes2.length != 0) {
                z = false;
            }
            if (z) {
                return start;
            }
        }
        this.tmpInterpKey.set(start, end, f);
        Font font = this.interpCache.get(this.tmpInterpKey);
        if (font != null) {
            return font;
        }
        List<FontVariationAxis> lerp = lerp(axes, axes2, new FontInterpolator$lerp$newAxes$1(this, f));
        this.tmpVarFontKey.set(start, lerp);
        Font font2 = this.verFontCache.get(this.tmpVarFontKey);
        if (font2 != null) {
            this.interpCache.put(new InterpKey(start, end, f), font2);
            return font2;
        }
        Font.Builder builder = new Font.Builder(start);
        Object[] array = lerp.toArray(new FontVariationAxis[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
        Font newFont = builder.setFontVariationSettings((FontVariationAxis[]) array).build();
        HashMap<InterpKey, Font> hashMap = this.interpCache;
        InterpKey interpKey = new InterpKey(start, end, f);
        Intrinsics.checkNotNullExpressionValue(newFont, "newFont");
        hashMap.put(interpKey, newFont);
        this.verFontCache.put(new VarFontKey(start, lerp), newFont);
        return newFont;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final float adjustWeight(float f) {
        return coerceInWithStep(f, 0.0f, 1000.0f, 10.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final float adjustItalic(float f) {
        return coerceInWithStep(f, 0.0f, 1.0f, 0.1f);
    }

    private final float coerceInWithStep(float f, float f2, float f3, float f4) {
        float coerceIn;
        coerceIn = RangesKt___RangesKt.coerceIn(f, f2, f3);
        return ((int) (coerceIn / f4)) * f4;
    }

    /* compiled from: FontInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean canInterpolate(@NotNull Font start, @NotNull Font end) {
            Intrinsics.checkNotNullParameter(start, "start");
            Intrinsics.checkNotNullParameter(end, "end");
            return start.getTtcIndex() == end.getTtcIndex() && start.getSourceIdentifier() == end.getSourceIdentifier();
        }
    }

    private final List<FontVariationAxis> lerp(FontVariationAxis[] fontVariationAxisArr, FontVariationAxis[] fontVariationAxisArr2, Function3<? super String, ? super Float, ? super Float, Float> function3) {
        int compareTo;
        int i;
        FontVariationAxis fontVariationAxis;
        if (fontVariationAxisArr.length > 1) {
            ArraysKt___ArraysJvmKt.sortWith(fontVariationAxisArr, new Comparator<T>() { // from class: com.android.keyguard.FontInterpolator$lerp$$inlined$sortBy$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    int compareValues;
                    compareValues = ComparisonsKt__ComparisonsKt.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                    return compareValues;
                }
            });
        }
        if (fontVariationAxisArr2.length > 1) {
            ArraysKt___ArraysJvmKt.sortWith(fontVariationAxisArr2, new Comparator<T>() { // from class: com.android.keyguard.FontInterpolator$lerp$$inlined$sortBy$2
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    int compareValues;
                    compareValues = ComparisonsKt__ComparisonsKt.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                    return compareValues;
                }
            });
        }
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 < fontVariationAxisArr.length || i3 < fontVariationAxisArr2.length) {
                String tag = i2 < fontVariationAxisArr.length ? fontVariationAxisArr[i2].getTag() : null;
                String tag2 = i3 < fontVariationAxisArr2.length ? fontVariationAxisArr2[i3].getTag() : null;
                if (tag == null) {
                    compareTo = 1;
                } else {
                    compareTo = tag2 == null ? -1 : tag.compareTo(tag2);
                }
                if (compareTo == 0) {
                    Intrinsics.checkNotNull(tag);
                    int i4 = i3 + 1;
                    fontVariationAxis = new FontVariationAxis(tag, function3.invoke(tag, Float.valueOf(fontVariationAxisArr[i2].getStyleValue()), Float.valueOf(fontVariationAxisArr2[i3].getStyleValue())).floatValue());
                    i2++;
                    i = i4;
                } else if (compareTo < 0) {
                    Intrinsics.checkNotNull(tag);
                    FontVariationAxis fontVariationAxis2 = new FontVariationAxis(tag, function3.invoke(tag, Float.valueOf(fontVariationAxisArr[i2].getStyleValue()), null).floatValue());
                    i = i3;
                    fontVariationAxis = fontVariationAxis2;
                    i2++;
                } else {
                    Intrinsics.checkNotNull(tag2);
                    i = i3 + 1;
                    fontVariationAxis = new FontVariationAxis(tag2, function3.invoke(tag2, null, Float.valueOf(fontVariationAxisArr2[i3].getStyleValue())).floatValue());
                }
                arrayList.add(fontVariationAxis);
                i3 = i;
            } else {
                return arrayList;
            }
        }
    }
}
