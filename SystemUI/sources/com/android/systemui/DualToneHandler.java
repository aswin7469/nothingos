package com.android.systemui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.view.ContextThemeWrapper;
import com.android.settingslib.Utils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0012B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ \u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\tH\u0002J\u000e\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\u000f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0002\u001a\u00020\u0003R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/DualToneHandler;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "darkColor", "Lcom/android/systemui/DualToneHandler$Color;", "lightColor", "getBackgroundColor", "", "intensity", "", "getColorForDarkIntensity", "darkIntensity", "getFillColor", "getSingleColor", "setColorsFromContext", "", "Color", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DualToneHandler.kt */
public final class DualToneHandler {
    private Color darkColor;
    private Color lightColor;

    public DualToneHandler(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        setColorsFromContext(context);
    }

    public final void setColorsFromContext(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, C1894R.attr.darkIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, Utils.getThemeAttr(context, C1894R.attr.lightIconTheme));
        Context context2 = contextThemeWrapper;
        this.darkColor = new Color(Utils.getColorAttrDefaultColor(context2, C1894R.attr.singleToneColor), Utils.getColorAttrDefaultColor(context2, C1894R.attr.iconBackgroundColor), Utils.getColorAttrDefaultColor(context2, C1894R.attr.fillColor));
        Context context3 = contextThemeWrapper2;
        this.lightColor = new Color(Utils.getColorAttrDefaultColor(context3, C1894R.attr.singleToneColor), Utils.getColorAttrDefaultColor(context3, C1894R.attr.iconBackgroundColor), Utils.getColorAttrDefaultColor(context3, C1894R.attr.fillColor));
    }

    private final int getColorForDarkIntensity(float f, int i, int i2) {
        Object evaluate = ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(i), Integer.valueOf(i2));
        if (evaluate != null) {
            return ((Integer) evaluate).intValue();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public final int getSingleColor(float f) {
        Color color = this.lightColor;
        Color color2 = null;
        if (color == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lightColor");
            color = null;
        }
        int single = color.getSingle();
        Color color3 = this.darkColor;
        if (color3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("darkColor");
        } else {
            color2 = color3;
        }
        return getColorForDarkIntensity(f, single, color2.getSingle());
    }

    public final int getBackgroundColor(float f) {
        Color color = this.lightColor;
        Color color2 = null;
        if (color == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lightColor");
            color = null;
        }
        int background = color.getBackground();
        Color color3 = this.darkColor;
        if (color3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("darkColor");
        } else {
            color2 = color3;
        }
        return getColorForDarkIntensity(f, background, color2.getBackground());
    }

    public final int getFillColor(float f) {
        Color color = this.lightColor;
        Color color2 = null;
        if (color == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lightColor");
            color = null;
        }
        int fill = color.getFill();
        Color color3 = this.darkColor;
        if (color3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("darkColor");
        } else {
            color2 = color3;
        }
        return getColorForDarkIntensity(f, fill, color2.getFill());
    }

    @Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J'\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b¨\u0006\u0015"}, mo65043d2 = {"Lcom/android/systemui/DualToneHandler$Color;", "", "single", "", "background", "fill", "(III)V", "getBackground", "()I", "getFill", "getSingle", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DualToneHandler.kt */
    private static final class Color {
        private final int background;
        private final int fill;
        private final int single;

        public static /* synthetic */ Color copy$default(Color color, int i, int i2, int i3, int i4, Object obj) {
            if ((i4 & 1) != 0) {
                i = color.single;
            }
            if ((i4 & 2) != 0) {
                i2 = color.background;
            }
            if ((i4 & 4) != 0) {
                i3 = color.fill;
            }
            return color.copy(i, i2, i3);
        }

        public final int component1() {
            return this.single;
        }

        public final int component2() {
            return this.background;
        }

        public final int component3() {
            return this.fill;
        }

        public final Color copy(int i, int i2, int i3) {
            return new Color(i, i2, i3);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Color)) {
                return false;
            }
            Color color = (Color) obj;
            return this.single == color.single && this.background == color.background && this.fill == color.fill;
        }

        public int hashCode() {
            return (((Integer.hashCode(this.single) * 31) + Integer.hashCode(this.background)) * 31) + Integer.hashCode(this.fill);
        }

        public String toString() {
            return "Color(single=" + this.single + ", background=" + this.background + ", fill=" + this.fill + ')';
        }

        public Color(int i, int i2, int i3) {
            this.single = i;
            this.background = i2;
            this.fill = i3;
        }

        public final int getBackground() {
            return this.background;
        }

        public final int getFill() {
            return this.fill;
        }

        public final int getSingle() {
            return this.single;
        }
    }
}
