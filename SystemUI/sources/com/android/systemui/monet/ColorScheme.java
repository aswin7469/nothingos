package com.android.systemui.monet;

import android.app.WallpaperColors;
import android.icu.text.DateFormat;
import androidx.exifinterface.media.ExifInterface;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.graphics.cam.Cam;
import com.android.internal.graphics.cam.CamUtils;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.text.StringsKt;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0019\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 )2\u00020\u0001:\u0001)B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006B!\b\u0017\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bB\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\fJ\b\u0010'\u001a\u00020(H\u0016R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\u0015\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e8F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0010R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e8F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0010R\u0011\u0010\u001c\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u0017R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00030\u000e¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0010R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0017R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b%\u0010&¨\u0006*"}, mo65043d2 = {"Lcom/android/systemui/monet/ColorScheme;", "", "seed", "", "darkTheme", "", "(IZ)V", "wallpaperColors", "Landroid/app/WallpaperColors;", "style", "Lcom/android/systemui/monet/Style;", "(Landroid/app/WallpaperColors;ZLcom/android/systemui/monet/Style;)V", "(IZLcom/android/systemui/monet/Style;)V", "accent1", "", "getAccent1", "()Ljava/util/List;", "accent2", "getAccent2", "accent3", "getAccent3", "accentColor", "getAccentColor", "()I", "allAccentColors", "getAllAccentColors", "allNeutralColors", "getAllNeutralColors", "backgroundColor", "getBackgroundColor", "getDarkTheme", "()Z", "neutral1", "getNeutral1", "neutral2", "getNeutral2", "getSeed", "getStyle", "()Lcom/android/systemui/monet/Style;", "toString", "", "Companion", "monet_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorScheme.kt */
public final class ColorScheme {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final List<Integer> accent1;
    private final List<Integer> accent2;
    private final List<Integer> accent3;
    private final boolean darkTheme;
    private final List<Integer> neutral1;
    private final List<Integer> neutral2;
    private final int seed;
    private final Style style;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public ColorScheme(WallpaperColors wallpaperColors, boolean z) {
        this(wallpaperColors, z, (Style) null, 4, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
    }

    @JvmStatic
    public static final int getSeedColor(WallpaperColors wallpaperColors) {
        return Companion.getSeedColor(wallpaperColors);
    }

    @JvmStatic
    public static final int getSeedColor(WallpaperColors wallpaperColors, boolean z) {
        return Companion.getSeedColor(wallpaperColors, z);
    }

    @JvmStatic
    public static final List<Integer> getSeedColors(WallpaperColors wallpaperColors) {
        return Companion.getSeedColors(wallpaperColors);
    }

    @JvmStatic
    public static final List<Integer> getSeedColors(WallpaperColors wallpaperColors, boolean z) {
        return Companion.getSeedColors(wallpaperColors, z);
    }

    public ColorScheme(int i, boolean z, Style style2) {
        Intrinsics.checkNotNullParameter(style2, "style");
        this.seed = i;
        this.darkTheme = z;
        this.style = style2;
        Cam fromInt = Cam.fromInt(i);
        if (i == 0 || (style2 != Style.CONTENT && fromInt.getChroma() < 5.0f)) {
            i = -14979341;
        }
        Cam fromInt2 = Cam.fromInt(i);
        TonalSpec a1 = style2.getCoreSpec$monet_release().getA1();
        Intrinsics.checkNotNullExpressionValue(fromInt2, "camSeed");
        this.accent1 = a1.shades(fromInt2);
        this.accent2 = style2.getCoreSpec$monet_release().getA2().shades(fromInt2);
        this.accent3 = style2.getCoreSpec$monet_release().getA3().shades(fromInt2);
        this.neutral1 = style2.getCoreSpec$monet_release().getN1().shades(fromInt2);
        this.neutral2 = style2.getCoreSpec$monet_release().getN2().shades(fromInt2);
    }

    public final int getSeed() {
        return this.seed;
    }

    public final boolean getDarkTheme() {
        return this.darkTheme;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ColorScheme(int i, boolean z, Style style2, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, z, (i2 & 4) != 0 ? Style.TONAL_SPOT : style2);
    }

    public final Style getStyle() {
        return this.style;
    }

    public final List<Integer> getAccent1() {
        return this.accent1;
    }

    public final List<Integer> getAccent2() {
        return this.accent2;
    }

    public final List<Integer> getAccent3() {
        return this.accent3;
    }

    public final List<Integer> getNeutral1() {
        return this.neutral1;
    }

    public final List<Integer> getNeutral2() {
        return this.neutral2;
    }

    public ColorScheme(int i, boolean z) {
        this(i, z, Style.TONAL_SPOT);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ColorScheme(WallpaperColors wallpaperColors, boolean z, Style style2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(wallpaperColors, z, (i & 4) != 0 ? Style.TONAL_SPOT : style2);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public ColorScheme(WallpaperColors wallpaperColors, boolean z, Style style2) {
        this(Companion.getSeedColor(wallpaperColors, style2 != Style.CONTENT), z, style2);
        Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
        Intrinsics.checkNotNullParameter(style2, "style");
    }

    public final List<Integer> getAllAccentColors() {
        List<Integer> arrayList = new ArrayList<>();
        arrayList.addAll(this.accent1);
        arrayList.addAll(this.accent2);
        arrayList.addAll(this.accent3);
        return arrayList;
    }

    public final List<Integer> getAllNeutralColors() {
        List<Integer> arrayList = new ArrayList<>();
        arrayList.addAll(this.neutral1);
        arrayList.addAll(this.neutral2);
        return arrayList;
    }

    public final int getBackgroundColor() {
        return ColorUtils.setAlphaComponent(this.neutral1.get(this.darkTheme ? 8 : 0).intValue(), 255);
    }

    public final int getAccentColor() {
        return ColorUtils.setAlphaComponent(this.accent1.get(this.darkTheme ? 2 : 6).intValue(), 255);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ColorScheme {\n  seed color: ");
        Companion companion = Companion;
        return sb.append(companion.stringForColor(this.seed)).append("\n  style: ").append((Object) this.style).append("\n  palettes: \n  ").append(companion.humanReadable("PRIMARY", this.accent1)).append("\n  ").append(companion.humanReadable("SECONDARY", this.accent2)).append("\n  ").append(companion.humanReadable("TERTIARY", this.accent3)).append("\n  ").append(companion.humanReadable("NEUTRAL", this.neutral1)).append("\n  ").append(companion.humanReadable("NEUTRAL VARIANT", this.neutral2)).append("\n}").toString();
    }

    @Metadata(mo65042d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007J \u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\n2\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\fH\u0002J@\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\n2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00130\u00122\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00100\u00122\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0002J\u001e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00040\nH\u0002J\u0018\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0010H\u0002J\u0010\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u0004H\u0002J\u0010\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u0004H\u0002J\u000e\u0010 \u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0010¨\u0006!"}, mo65043d2 = {"Lcom/android/systemui/monet/ColorScheme$Companion;", "", "()V", "getSeedColor", "", "wallpaperColors", "Landroid/app/WallpaperColors;", "filter", "", "getSeedColors", "", "hueDiff", "", "a", "b", "huePopulations", "", "camByColor", "", "Lcom/android/internal/graphics/cam/Cam;", "populationByColor", "humanReadable", "", "paletteName", "colors", "score", "cam", "proportion", "stringForColor", "color", "wrapDegrees", "degrees", "wrapDegreesDouble", "monet_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ColorScheme.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final int getSeedColor(WallpaperColors wallpaperColors) {
            Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
            return getSeedColor$default(this, wallpaperColors, false, 2, (Object) null);
        }

        @JvmStatic
        public final List<Integer> getSeedColors(WallpaperColors wallpaperColors) {
            Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
            return getSeedColors$default(this, wallpaperColors, false, 2, (Object) null);
        }

        public final double wrapDegreesDouble(double d) {
            if (d >= 0.0d) {
                return d >= 360.0d ? d % ((double) StackStateAnimator.ANIMATION_DURATION_STANDARD) : d;
            }
            double d2 = (double) StackStateAnimator.ANIMATION_DURATION_STANDARD;
            return (d % d2) + d2;
        }

        private Companion() {
        }

        public static /* synthetic */ int getSeedColor$default(Companion companion, WallpaperColors wallpaperColors, boolean z, int i, Object obj) {
            if ((i & 2) != 0) {
                z = true;
            }
            return companion.getSeedColor(wallpaperColors, z);
        }

        @JvmStatic
        public final int getSeedColor(WallpaperColors wallpaperColors, boolean z) {
            Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
            return ((Number) CollectionsKt.first(getSeedColors(wallpaperColors, z))).intValue();
        }

        public static /* synthetic */ List getSeedColors$default(Companion companion, WallpaperColors wallpaperColors, boolean z, int i, Object obj) {
            if ((i & 2) != 0) {
                z = true;
            }
            return companion.getSeedColors(wallpaperColors, z);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:87:0x0332, code lost:
            if (r2 == 15) goto L_0x0337;
         */
        @kotlin.jvm.JvmStatic
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final java.util.List<java.lang.Integer> getSeedColors(android.app.WallpaperColors r19, boolean r20) {
            /*
                r18 = this;
                r0 = r20
                java.lang.String r1 = "wallpaperColors"
                r2 = r19
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r1)
                java.util.Map r1 = r19.getAllColors()
                java.util.Collection r1 = r1.values()
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                java.util.Iterator r1 = r1.iterator()
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x0345
                java.lang.Object r3 = r1.next()
            L_0x0022:
                boolean r4 = r1.hasNext()
                if (r4 == 0) goto L_0x0043
                java.lang.Object r4 = r1.next()
                java.lang.Integer r4 = (java.lang.Integer) r4
                java.lang.Integer r3 = (java.lang.Integer) r3
                int r3 = r3.intValue()
                java.lang.String r5 = "b"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r4, r5)
                int r4 = r4.intValue()
                int r3 = r3 + r4
                java.lang.Integer r3 = java.lang.Integer.valueOf((int) r3)
                goto L_0x0022
            L_0x0043:
                java.lang.Number r3 = (java.lang.Number) r3
                int r1 = r3.intValue()
                double r3 = (double) r1
                r5 = 0
                int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                r7 = 0
                r8 = 1
                if (r1 != 0) goto L_0x0054
                r1 = r8
                goto L_0x0055
            L_0x0054:
                r1 = r7
            L_0x0055:
                r9 = 1084227584(0x40a00000, float:5.0)
                r10 = -14979341(0xffffffffff1b6ef3, float:-2.0660642E38)
                if (r1 == 0) goto L_0x00e6
                java.util.List r1 = r19.getMainColors()
                java.lang.String r2 = "wallpaperColors.mainColors"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r2)
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                java.util.ArrayList r2 = new java.util.ArrayList
                r3 = 10
                int r3 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r1, r3)
                r2.<init>((int) r3)
                java.util.Collection r2 = (java.util.Collection) r2
                java.util.Iterator r1 = r1.iterator()
            L_0x0079:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x0091
                java.lang.Object r3 = r1.next()
                android.graphics.Color r3 = (android.graphics.Color) r3
                int r3 = r3.toArgb()
                java.lang.Integer r3 = java.lang.Integer.valueOf((int) r3)
                r2.add(r3)
                goto L_0x0079
            L_0x0091:
                java.util.List r2 = (java.util.List) r2
                java.lang.Iterable r2 = (java.lang.Iterable) r2
                java.util.List r1 = kotlin.collections.CollectionsKt.distinct(r2)
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                java.util.ArrayList r2 = new java.util.ArrayList
                r2.<init>()
                java.util.Collection r2 = (java.util.Collection) r2
                java.util.Iterator r1 = r1.iterator()
            L_0x00a6:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x00cf
                java.lang.Object r3 = r1.next()
                r4 = r3
                java.lang.Number r4 = (java.lang.Number) r4
                int r4 = r4.intValue()
                if (r0 != 0) goto L_0x00bb
            L_0x00b9:
                r4 = r8
                goto L_0x00c9
            L_0x00bb:
                com.android.internal.graphics.cam.Cam r4 = com.android.internal.graphics.cam.Cam.fromInt(r4)
                float r4 = r4.getChroma()
                int r4 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1))
                if (r4 < 0) goto L_0x00c8
                goto L_0x00b9
            L_0x00c8:
                r4 = r7
            L_0x00c9:
                if (r4 == 0) goto L_0x00a6
                r2.add(r3)
                goto L_0x00a6
            L_0x00cf:
                java.util.List r2 = (java.util.List) r2
                java.lang.Iterable r2 = (java.lang.Iterable) r2
                java.util.List r0 = kotlin.collections.CollectionsKt.toList(r2)
                boolean r1 = r0.isEmpty()
                if (r1 == 0) goto L_0x00e5
                java.lang.Integer r0 = java.lang.Integer.valueOf((int) r10)
                java.util.List r0 = kotlin.collections.CollectionsKt.listOf(r0)
            L_0x00e5:
                return r0
            L_0x00e6:
                java.util.Map r11 = r19.getAllColors()
                java.lang.String r12 = "wallpaperColors.allColors"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r11, r12)
                java.util.LinkedHashMap r13 = new java.util.LinkedHashMap
                int r14 = r11.size()
                int r14 = kotlin.collections.MapsKt.mapCapacity(r14)
                r13.<init>((int) r14)
                java.util.Map r13 = (java.util.Map) r13
                java.util.Set r11 = r11.entrySet()
                java.lang.Iterable r11 = (java.lang.Iterable) r11
                java.util.Iterator r11 = r11.iterator()
            L_0x0109:
                boolean r14 = r11.hasNext()
                if (r14 == 0) goto L_0x012f
                java.lang.Object r14 = r11.next()
                java.util.Map$Entry r14 = (java.util.Map.Entry) r14
                java.lang.Object r15 = r14.getKey()
                java.lang.Object r14 = r14.getValue()
                java.lang.Number r14 = (java.lang.Number) r14
                int r14 = r14.intValue()
                double r5 = (double) r14
                double r5 = r5 / r3
                java.lang.Double r5 = java.lang.Double.valueOf((double) r5)
                r13.put(r15, r5)
                r5 = 0
                goto L_0x0109
            L_0x012f:
                java.util.Map r3 = r19.getAllColors()
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, r12)
                java.util.LinkedHashMap r4 = new java.util.LinkedHashMap
                int r5 = r3.size()
                int r5 = kotlin.collections.MapsKt.mapCapacity(r5)
                r4.<init>((int) r5)
                java.util.Map r4 = (java.util.Map) r4
                java.util.Set r3 = r3.entrySet()
                java.lang.Iterable r3 = (java.lang.Iterable) r3
                java.util.Iterator r3 = r3.iterator()
            L_0x014f:
                boolean r5 = r3.hasNext()
                if (r5 == 0) goto L_0x0176
                java.lang.Object r5 = r3.next()
                java.util.Map$Entry r5 = (java.util.Map.Entry) r5
                java.lang.Object r6 = r5.getKey()
                java.lang.Object r5 = r5.getKey()
                java.lang.String r11 = "it.key"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r11)
                java.lang.Number r5 = (java.lang.Number) r5
                int r5 = r5.intValue()
                com.android.internal.graphics.cam.Cam r5 = com.android.internal.graphics.cam.Cam.fromInt(r5)
                r4.put(r6, r5)
                goto L_0x014f
            L_0x0176:
                r5 = r18
                java.util.List r3 = r5.huePopulations(r4, r13, r0)
                java.util.Map r2 = r19.getAllColors()
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r12)
                java.util.LinkedHashMap r5 = new java.util.LinkedHashMap
                int r6 = r2.size()
                int r6 = kotlin.collections.MapsKt.mapCapacity(r6)
                r5.<init>((int) r6)
                java.util.Map r5 = (java.util.Map) r5
                java.util.Set r2 = r2.entrySet()
                java.lang.Iterable r2 = (java.lang.Iterable) r2
                java.util.Iterator r2 = r2.iterator()
            L_0x019c:
                boolean r6 = r2.hasNext()
                r11 = 15
                if (r6 == 0) goto L_0x01e9
                java.lang.Object r6 = r2.next()
                java.util.Map$Entry r6 = (java.util.Map.Entry) r6
                java.lang.Object r12 = r6.getKey()
                java.lang.Object r6 = r6.getKey()
                java.lang.Object r6 = r4.get(r6)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r6)
                com.android.internal.graphics.cam.Cam r6 = (com.android.internal.graphics.cam.Cam) r6
                float r6 = r6.getHue()
                int r6 = kotlin.math.MathKt.roundToInt((float) r6)
                int r13 = r6 + -15
                int r6 = r6 + r11
                r14 = 0
                if (r13 > r6) goto L_0x01e1
            L_0x01ca:
                com.android.systemui.monet.ColorScheme$Companion r11 = com.android.systemui.monet.ColorScheme.Companion
                int r11 = r11.wrapDegrees(r13)
                java.lang.Object r11 = r3.get(r11)
                java.lang.Number r11 = (java.lang.Number) r11
                double r16 = r11.doubleValue()
                double r14 = r14 + r16
                if (r13 == r6) goto L_0x01e1
                int r13 = r13 + 1
                goto L_0x01ca
            L_0x01e1:
                java.lang.Double r6 = java.lang.Double.valueOf((double) r14)
                r5.put(r12, r6)
                goto L_0x019c
            L_0x01e9:
                if (r0 != 0) goto L_0x01ed
                r0 = r4
                goto L_0x0243
            L_0x01ed:
                java.util.LinkedHashMap r0 = new java.util.LinkedHashMap
                r0.<init>()
                java.util.Map r0 = (java.util.Map) r0
                java.util.Set r2 = r4.entrySet()
                java.util.Iterator r2 = r2.iterator()
            L_0x01fc:
                boolean r3 = r2.hasNext()
                if (r3 == 0) goto L_0x0243
                java.lang.Object r3 = r2.next()
                java.util.Map$Entry r3 = (java.util.Map.Entry) r3
                java.lang.Object r6 = r3.getValue()
                com.android.internal.graphics.cam.Cam r6 = (com.android.internal.graphics.cam.Cam) r6
                java.lang.Object r12 = r3.getKey()
                java.lang.Object r12 = r5.get(r12)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r12)
                java.lang.Number r12 = (java.lang.Number) r12
                double r12 = r12.doubleValue()
                float r6 = r6.getChroma()
                int r6 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
                if (r6 < 0) goto L_0x0234
                if (r1 != 0) goto L_0x0232
                r14 = 4576918229304087675(0x3f847ae147ae147b, double:0.01)
                int r6 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
                if (r6 <= 0) goto L_0x0234
            L_0x0232:
                r6 = r8
                goto L_0x0235
            L_0x0234:
                r6 = r7
            L_0x0235:
                if (r6 == 0) goto L_0x01fc
                java.lang.Object r6 = r3.getKey()
                java.lang.Object r3 = r3.getValue()
                r0.put(r6, r3)
                goto L_0x01fc
            L_0x0243:
                java.util.LinkedHashMap r1 = new java.util.LinkedHashMap
                int r2 = r0.size()
                int r2 = kotlin.collections.MapsKt.mapCapacity(r2)
                r1.<init>((int) r2)
                java.util.Map r1 = (java.util.Map) r1
                java.util.Set r0 = r0.entrySet()
                java.lang.Iterable r0 = (java.lang.Iterable) r0
                java.util.Iterator r0 = r0.iterator()
            L_0x025c:
                boolean r2 = r0.hasNext()
                if (r2 == 0) goto L_0x0296
                java.lang.Object r2 = r0.next()
                java.util.Map$Entry r2 = (java.util.Map.Entry) r2
                java.lang.Object r3 = r2.getKey()
                com.android.systemui.monet.ColorScheme$Companion r6 = com.android.systemui.monet.ColorScheme.Companion
                java.lang.Object r9 = r2.getValue()
                java.lang.String r12 = "it.value"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r9, r12)
                com.android.internal.graphics.cam.Cam r9 = (com.android.internal.graphics.cam.Cam) r9
                java.lang.Object r2 = r2.getKey()
                java.lang.Object r2 = r5.get(r2)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
                java.lang.Number r2 = (java.lang.Number) r2
                double r12 = r2.doubleValue()
                double r12 = r6.score(r9, r12)
                java.lang.Double r2 = java.lang.Double.valueOf((double) r12)
                r1.put(r3, r2)
                goto L_0x025c
            L_0x0296:
                java.util.Set r0 = r1.entrySet()
                java.util.Collection r0 = (java.util.Collection) r0
                java.util.List r0 = kotlin.collections.CollectionsKt.toMutableList(r0)
                int r1 = r0.size()
                if (r1 <= r8) goto L_0x02b0
                com.android.systemui.monet.ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1 r1 = new com.android.systemui.monet.ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1
                r1.<init>()
                java.util.Comparator r1 = (java.util.Comparator) r1
                kotlin.collections.CollectionsKt.sortWith(r0, r1)
            L_0x02b0:
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                java.util.List r1 = (java.util.List) r1
                r2 = 90
            L_0x02b9:
                r1.clear()
                java.util.Iterator r3 = r0.iterator()
            L_0x02c0:
                boolean r5 = r3.hasNext()
                if (r5 == 0) goto L_0x0332
                java.lang.Object r5 = r3.next()
                java.util.Map$Entry r5 = (java.util.Map.Entry) r5
                java.lang.Object r5 = r5.getKey()
                java.lang.Integer r5 = (java.lang.Integer) r5
                r6 = r1
                java.lang.Iterable r6 = (java.lang.Iterable) r6
                java.util.Iterator r6 = r6.iterator()
            L_0x02d9:
                boolean r9 = r6.hasNext()
                if (r9 == 0) goto L_0x0319
                java.lang.Object r9 = r6.next()
                r12 = r9
                java.lang.Number r12 = (java.lang.Number) r12
                int r12 = r12.intValue()
                java.lang.Object r13 = r4.get(r5)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r13)
                com.android.internal.graphics.cam.Cam r13 = (com.android.internal.graphics.cam.Cam) r13
                float r13 = r13.getHue()
                java.lang.Integer r12 = java.lang.Integer.valueOf((int) r12)
                java.lang.Object r12 = r4.get(r12)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r12)
                com.android.internal.graphics.cam.Cam r12 = (com.android.internal.graphics.cam.Cam) r12
                float r12 = r12.getHue()
                com.android.systemui.monet.ColorScheme$Companion r14 = com.android.systemui.monet.ColorScheme.Companion
                float r12 = r14.hueDiff(r13, r12)
                float r13 = (float) r2
                int r12 = (r12 > r13 ? 1 : (r12 == r13 ? 0 : -1))
                if (r12 >= 0) goto L_0x0315
                r12 = r8
                goto L_0x0316
            L_0x0315:
                r12 = r7
            L_0x0316:
                if (r12 == 0) goto L_0x02d9
                goto L_0x031a
            L_0x0319:
                r9 = 0
            L_0x031a:
                if (r9 == 0) goto L_0x031e
                r6 = r8
                goto L_0x031f
            L_0x031e:
                r6 = r7
            L_0x031f:
                if (r6 == 0) goto L_0x0322
                goto L_0x02c0
            L_0x0322:
                java.lang.String r6 = "int"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r6)
                r1.add(r5)
                int r5 = r1.size()
                r6 = 4
                if (r5 < r6) goto L_0x02c0
                goto L_0x0337
            L_0x0332:
                if (r2 == r11) goto L_0x0337
                int r2 = r2 + -1
                goto L_0x02b9
            L_0x0337:
                boolean r0 = r1.isEmpty()
                if (r0 == 0) goto L_0x0344
                java.lang.Integer r0 = java.lang.Integer.valueOf((int) r10)
                r1.add(r0)
            L_0x0344:
                return r1
            L_0x0345:
                java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
                java.lang.String r1 = "Empty collection can't be reduced."
                r0.<init>((java.lang.String) r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.monet.ColorScheme.Companion.getSeedColors(android.app.WallpaperColors, boolean):java.util.List");
        }

        private final int wrapDegrees(int i) {
            if (i < 0) {
                return (i % StackStateAnimator.ANIMATION_DURATION_STANDARD) + StackStateAnimator.ANIMATION_DURATION_STANDARD;
            }
            return i >= 360 ? i % StackStateAnimator.ANIMATION_DURATION_STANDARD : i;
        }

        private final float hueDiff(float f, float f2) {
            return 180.0f - Math.abs(Math.abs(f - f2) - 180.0f);
        }

        /* access modifiers changed from: private */
        public final String stringForColor(int i) {
            Cam fromInt = Cam.fromInt(i);
            String str = DateFormat.HOUR24 + StringsKt.padEnd$default(String.valueOf(MathKt.roundToInt(fromInt.getHue())), 4, 0, 2, (Object) null);
            String str2 = "C" + StringsKt.padEnd$default(String.valueOf(MathKt.roundToInt(fromInt.getChroma())), 4, 0, 2, (Object) null);
            String str3 = ExifInterface.GPS_DIRECTION_TRUE + StringsKt.padEnd$default(String.valueOf(MathKt.roundToInt(CamUtils.lstarFromInt(i))), 4, 0, 2, (Object) null);
            String hexString = Integer.toHexString(i & 16777215);
            Intrinsics.checkNotNullExpressionValue(hexString, "toHexString(color and 0xffffff)");
            String upperCase = StringsKt.padStart(hexString, 6, '0').toUpperCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(upperCase, "this as java.lang.String).toUpperCase(Locale.ROOT)");
            return str + str2 + str3 + " = #" + upperCase;
        }

        /* access modifiers changed from: private */
        public final String humanReadable(String str, List<Integer> list) {
            StringBuilder append = new StringBuilder().append(str).append(10);
            Iterable<Number> iterable = list;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (Number intValue : iterable) {
                arrayList.add(ColorScheme.Companion.stringForColor(intValue.intValue()));
            }
            return append.append(CollectionsKt.joinToString$default((List) arrayList, "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, ColorScheme$Companion$humanReadable$2.INSTANCE, 30, (Object) null)).toString();
        }

        private final double score(Cam cam, double d) {
            double d2;
            double d3;
            double d4 = d * 70.0d;
            if (cam.getChroma() < 48.0f) {
                d2 = (double) (cam.getChroma() - 48.0f);
                d3 = 0.1d;
            } else {
                d2 = (double) (cam.getChroma() - 48.0f);
                d3 = 0.3d;
            }
            return (d2 * d3) + d4;
        }

        static /* synthetic */ List huePopulations$default(Companion companion, Map map, Map map2, boolean z, int i, Object obj) {
            if ((i & 4) != 0) {
                z = true;
            }
            return companion.huePopulations(map, map2, z);
        }

        private final List<Double> huePopulations(Map<Integer, ? extends Cam> map, Map<Integer, Double> map2, boolean z) {
            ArrayList arrayList = new ArrayList((int) StackStateAnimator.ANIMATION_DURATION_STANDARD);
            for (int i = 0; i < 360; i++) {
                arrayList.add(Double.valueOf(0.0d));
            }
            List<Double> mutableList = CollectionsKt.toMutableList(arrayList);
            for (Map.Entry next : map2.entrySet()) {
                Double d = map2.get(next.getKey());
                Intrinsics.checkNotNull(d);
                double doubleValue = d.doubleValue();
                Cam cam = map.get(next.getKey());
                Intrinsics.checkNotNull(cam);
                Cam cam2 = cam;
                int roundToInt = MathKt.roundToInt(cam2.getHue()) % StackStateAnimator.ANIMATION_DURATION_STANDARD;
                if (!z || cam2.getChroma() > 5.0f) {
                    mutableList.set(roundToInt, Double.valueOf(mutableList.get(roundToInt).doubleValue() + doubleValue));
                }
            }
            return mutableList;
        }
    }
}
