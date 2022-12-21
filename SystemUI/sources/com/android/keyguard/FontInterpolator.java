package com.android.keyguard;

import android.graphics.fonts.Font;
import android.graphics.fonts.FontVariationAxis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

@Metadata(mo64986d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u000b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 %2\u00020\u0001:\u0003%&'B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0002J\u0010\u0010\u000f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0002J(\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\rH\u0002J\u001e\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\rJ\u0001\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u001a0\u001b2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u001a0\u001b2O\u0010\u001c\u001aK\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\u001f\u0012\b\b \u0012\u0004\b\b(!\u0012\u0015\u0012\u0013\u0018\u00010\r¢\u0006\f\b\u001f\u0012\b\b \u0012\u0004\b\b(\"\u0012\u0015\u0012\u0013\u0018\u00010\r¢\u0006\f\b\u001f\u0012\b\b \u0012\u0004\b\b(#\u0012\u0004\u0012\u00020\r0\u001dH\u0002¢\u0006\u0002\u0010$R*\u0010\u0003\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006`\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R*\u0010\u000b\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00060\u0004j\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u0006`\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006("}, mo64987d2 = {"Lcom/android/keyguard/FontInterpolator;", "", "()V", "interpCache", "Ljava/util/HashMap;", "Lcom/android/keyguard/FontInterpolator$InterpKey;", "Landroid/graphics/fonts/Font;", "Lkotlin/collections/HashMap;", "tmpInterpKey", "tmpVarFontKey", "Lcom/android/keyguard/FontInterpolator$VarFontKey;", "verFontCache", "adjustItalic", "", "value", "adjustWeight", "coerceInWithStep", "v", "min", "max", "step", "lerp", "start", "end", "progress", "", "Landroid/graphics/fonts/FontVariationAxis;", "", "filter", "Lkotlin/Function3;", "", "Lkotlin/ParameterName;", "name", "tag", "left", "right", "([Landroid/graphics/fonts/FontVariationAxis;[Landroid/graphics/fonts/FontVariationAxis;Lkotlin/jvm/functions/Function3;)Ljava/util/List;", "Companion", "InterpKey", "VarFontKey", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FontInterpolator.kt */
public final class FontInterpolator {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final FontVariationAxis[] EMPTY_AXES = new FontVariationAxis[0];
    private final HashMap<InterpKey, Font> interpCache = new HashMap<>();
    private final InterpKey tmpInterpKey = new InterpKey((Font) null, (Font) null, 0.0f);
    private final VarFontKey tmpVarFontKey = new VarFontKey(0, 0, new ArrayList());
    private final HashMap<VarFontKey, Font> verFontCache = new HashMap<>();

    @Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B!\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0006HÆ\u0003J+\u0010\u0015\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001J\u001e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000b¨\u0006\u001f"}, mo64987d2 = {"Lcom/android/keyguard/FontInterpolator$InterpKey;", "", "l", "Landroid/graphics/fonts/Font;", "r", "progress", "", "(Landroid/graphics/fonts/Font;Landroid/graphics/fonts/Font;F)V", "getL", "()Landroid/graphics/fonts/Font;", "setL", "(Landroid/graphics/fonts/Font;)V", "getProgress", "()F", "setProgress", "(F)V", "getR", "setR", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "set", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: FontInterpolator.kt */
    private static final class InterpKey {

        /* renamed from: l */
        private Font f229l;
        private float progress;

        /* renamed from: r */
        private Font f230r;

        public static /* synthetic */ InterpKey copy$default(InterpKey interpKey, Font font, Font font2, float f, int i, Object obj) {
            if ((i & 1) != 0) {
                font = interpKey.f229l;
            }
            if ((i & 2) != 0) {
                font2 = interpKey.f230r;
            }
            if ((i & 4) != 0) {
                f = interpKey.progress;
            }
            return interpKey.copy(font, font2, f);
        }

        public final Font component1() {
            return this.f229l;
        }

        public final Font component2() {
            return this.f230r;
        }

        public final float component3() {
            return this.progress;
        }

        public final InterpKey copy(Font font, Font font2, float f) {
            return new InterpKey(font, font2, f);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof InterpKey)) {
                return false;
            }
            InterpKey interpKey = (InterpKey) obj;
            return Intrinsics.areEqual((Object) this.f229l, (Object) interpKey.f229l) && Intrinsics.areEqual((Object) this.f230r, (Object) interpKey.f230r) && Intrinsics.areEqual((Object) Float.valueOf(this.progress), (Object) Float.valueOf(interpKey.progress));
        }

        public int hashCode() {
            Font font = this.f229l;
            int i = 0;
            int hashCode = (font == null ? 0 : font.hashCode()) * 31;
            Font font2 = this.f230r;
            if (font2 != null) {
                i = font2.hashCode();
            }
            return ((hashCode + i) * 31) + Float.hashCode(this.progress);
        }

        public String toString() {
            return "InterpKey(l=" + this.f229l + ", r=" + this.f230r + ", progress=" + this.progress + ')';
        }

        public InterpKey(Font font, Font font2, float f) {
            this.f229l = font;
            this.f230r = font2;
            this.progress = f;
        }

        public final Font getL() {
            return this.f229l;
        }

        public final float getProgress() {
            return this.progress;
        }

        public final Font getR() {
            return this.f230r;
        }

        public final void setL(Font font) {
            this.f229l = font;
        }

        public final void setProgress(float f) {
            this.progress = f;
        }

        public final void setR(Font font) {
            this.f230r = font;
        }

        public final void set(Font font, Font font2, float f) {
            Intrinsics.checkNotNullParameter(font, "l");
            Intrinsics.checkNotNullParameter(font2, "r");
            this.f229l = font;
            this.f230r = font2;
            this.progress = f;
        }
    }

    @Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001d\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007B#\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\f¢\u0006\u0002\u0010\rJ\t\u0010\u0016\u001a\u00020\tHÆ\u0003J\t\u0010\u0017\u001a\u00020\tHÆ\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00060\fHÆ\u0003J-\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\fHÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\tHÖ\u0001J\u001c\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005J\t\u0010 \u001a\u00020!HÖ\u0001R\u001a\u0010\n\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\f¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\b\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000f\"\u0004\b\u0015\u0010\u0011¨\u0006\""}, mo64987d2 = {"Lcom/android/keyguard/FontInterpolator$VarFontKey;", "", "font", "Landroid/graphics/fonts/Font;", "axes", "", "Landroid/graphics/fonts/FontVariationAxis;", "(Landroid/graphics/fonts/Font;Ljava/util/List;)V", "sourceId", "", "index", "sortedAxes", "", "(IILjava/util/List;)V", "getIndex", "()I", "setIndex", "(I)V", "getSortedAxes", "()Ljava/util/List;", "getSourceId", "setSourceId", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "set", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: FontInterpolator.kt */
    private static final class VarFontKey {
        private int index;
        private final List<FontVariationAxis> sortedAxes;
        private int sourceId;

        public static /* synthetic */ VarFontKey copy$default(VarFontKey varFontKey, int i, int i2, List<FontVariationAxis> list, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                i = varFontKey.sourceId;
            }
            if ((i3 & 2) != 0) {
                i2 = varFontKey.index;
            }
            if ((i3 & 4) != 0) {
                list = varFontKey.sortedAxes;
            }
            return varFontKey.copy(i, i2, list);
        }

        public final int component1() {
            return this.sourceId;
        }

        public final int component2() {
            return this.index;
        }

        public final List<FontVariationAxis> component3() {
            return this.sortedAxes;
        }

        public final VarFontKey copy(int i, int i2, List<FontVariationAxis> list) {
            Intrinsics.checkNotNullParameter(list, "sortedAxes");
            return new VarFontKey(i, i2, list);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof VarFontKey)) {
                return false;
            }
            VarFontKey varFontKey = (VarFontKey) obj;
            return this.sourceId == varFontKey.sourceId && this.index == varFontKey.index && Intrinsics.areEqual((Object) this.sortedAxes, (Object) varFontKey.sortedAxes);
        }

        public int hashCode() {
            return (((Integer.hashCode(this.sourceId) * 31) + Integer.hashCode(this.index)) * 31) + this.sortedAxes.hashCode();
        }

        public String toString() {
            return "VarFontKey(sourceId=" + this.sourceId + ", index=" + this.index + ", sortedAxes=" + this.sortedAxes + ')';
        }

        public VarFontKey(int i, int i2, List<FontVariationAxis> list) {
            Intrinsics.checkNotNullParameter(list, "sortedAxes");
            this.sourceId = i;
            this.index = i2;
            this.sortedAxes = list;
        }

        public final int getSourceId() {
            return this.sourceId;
        }

        public final void setSourceId(int i) {
            this.sourceId = i;
        }

        public final int getIndex() {
            return this.index;
        }

        public final void setIndex(int i) {
            this.index = i;
        }

        public final List<FontVariationAxis> getSortedAxes() {
            return this.sortedAxes;
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public VarFontKey(android.graphics.fonts.Font r4, java.util.List<android.graphics.fonts.FontVariationAxis> r5) {
            /*
                r3 = this;
                java.lang.String r0 = "font"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
                java.lang.String r0 = "axes"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
                int r0 = r4.getSourceIdentifier()
                int r4 = r4.getTtcIndex()
                java.util.Collection r5 = (java.util.Collection) r5
                java.util.List r5 = kotlin.collections.CollectionsKt.toMutableList(r5)
                int r1 = r5.size()
                r2 = 1
                if (r1 <= r2) goto L_0x0029
                com.android.keyguard.FontInterpolator$VarFontKey$_init_$lambda-1$$inlined$sortBy$1 r1 = new com.android.keyguard.FontInterpolator$VarFontKey$_init_$lambda-1$$inlined$sortBy$1
                r1.<init>()
                java.util.Comparator r1 = (java.util.Comparator) r1
                kotlin.collections.CollectionsKt.sortWith(r5, r1)
            L_0x0029:
                kotlin.Unit r1 = kotlin.Unit.INSTANCE
                r3.<init>(r0, r4, r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.FontInterpolator.VarFontKey.<init>(android.graphics.fonts.Font, java.util.List):void");
        }

        public final void set(Font font, List<FontVariationAxis> list) {
            Intrinsics.checkNotNullParameter(font, "font");
            Intrinsics.checkNotNullParameter(list, "axes");
            this.sourceId = font.getSourceIdentifier();
            this.index = font.getTtcIndex();
            this.sortedAxes.clear();
            this.sortedAxes.addAll(list);
            List<FontVariationAxis> list2 = this.sortedAxes;
            if (list2.size() > 1) {
                CollectionsKt.sortWith(list2, new FontInterpolator$VarFontKey$set$$inlined$sortBy$1());
            }
        }
    }

    public final Font lerp(Font font, Font font2, float f) {
        Intrinsics.checkNotNullParameter(font, "start");
        Intrinsics.checkNotNullParameter(font2, "end");
        boolean z = true;
        if (f == 0.0f) {
            return font;
        }
        if (f == 1.0f) {
            return font2;
        }
        FontVariationAxis[] axes = font.getAxes();
        if (axes == null) {
            axes = EMPTY_AXES;
        }
        FontVariationAxis[] axes2 = font2.getAxes();
        if (axes2 == null) {
            axes2 = EMPTY_AXES;
        }
        if (axes.length == 0) {
            if (axes2.length != 0) {
                z = false;
            }
            if (z) {
                return font;
            }
        }
        this.tmpInterpKey.set(font, font2, f);
        Font font3 = this.interpCache.get(this.tmpInterpKey);
        if (font3 != null) {
            return font3;
        }
        List<FontVariationAxis> lerp = lerp(axes, axes2, (Function3<? super String, ? super Float, ? super Float, Float>) new FontInterpolator$lerp$newAxes$1(this, f));
        this.tmpVarFontKey.set(font, lerp);
        Font font4 = this.verFontCache.get(this.tmpVarFontKey);
        if (font4 != null) {
            this.interpCache.put(new InterpKey(font, font2, f), font4);
            return font4;
        }
        Font.Builder builder = new Font.Builder(font);
        Object[] array = lerp.toArray((T[]) new FontVariationAxis[0]);
        Intrinsics.checkNotNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        Font build = builder.setFontVariationSettings((FontVariationAxis[]) array).build();
        InterpKey interpKey = new InterpKey(font, font2, f);
        Intrinsics.checkNotNullExpressionValue(build, "newFont");
        this.interpCache.put(interpKey, build);
        this.verFontCache.put(new VarFontKey(font, lerp), build);
        return build;
    }

    /* access modifiers changed from: private */
    public final float adjustWeight(float f) {
        return coerceInWithStep(f, 0.0f, 1000.0f, 10.0f);
    }

    /* access modifiers changed from: private */
    public final float adjustItalic(float f) {
        return coerceInWithStep(f, 0.0f, 1.0f, 0.1f);
    }

    private final float coerceInWithStep(float f, float f2, float f3, float f4) {
        return ((float) ((int) (RangesKt.coerceIn(f, f2, f3) / f4))) * f4;
    }

    @Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nR\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\u0004\n\u0002\u0010\u0006¨\u0006\f"}, mo64987d2 = {"Lcom/android/keyguard/FontInterpolator$Companion;", "", "()V", "EMPTY_AXES", "", "Landroid/graphics/fonts/FontVariationAxis;", "[Landroid/graphics/fonts/FontVariationAxis;", "canInterpolate", "", "start", "Landroid/graphics/fonts/Font;", "end", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: FontInterpolator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean canInterpolate(Font font, Font font2) {
            Intrinsics.checkNotNullParameter(font, "start");
            Intrinsics.checkNotNullParameter(font2, "end");
            return font.getTtcIndex() == font2.getTtcIndex() && font.getSourceIdentifier() == font2.getSourceIdentifier();
        }
    }

    private final List<FontVariationAxis> lerp(FontVariationAxis[] fontVariationAxisArr, FontVariationAxis[] fontVariationAxisArr2, Function3<? super String, ? super Float, ? super Float, Float> function3) {
        int i;
        FontVariationAxis fontVariationAxis;
        if (fontVariationAxisArr.length > 1) {
            ArraysKt.sortWith(fontVariationAxisArr, new FontInterpolator$lerp$$inlined$sortBy$1());
        }
        if (fontVariationAxisArr2.length > 1) {
            ArraysKt.sortWith(fontVariationAxisArr2, new FontInterpolator$lerp$$inlined$sortBy$2());
        }
        List<FontVariationAxis> arrayList = new ArrayList<>();
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= fontVariationAxisArr.length && i3 >= fontVariationAxisArr2.length) {
                return arrayList;
            }
            String tag = i2 < fontVariationAxisArr.length ? fontVariationAxisArr[i2].getTag() : null;
            String tag2 = i3 < fontVariationAxisArr2.length ? fontVariationAxisArr2[i3].getTag() : null;
            int compareTo = tag == null ? 1 : tag2 == null ? -1 : tag.compareTo(tag2);
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
        }
    }
}
