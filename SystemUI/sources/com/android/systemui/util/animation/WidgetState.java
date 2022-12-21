package com.android.systemui.util.animation;

import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b'\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B_\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\u0006\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003J\t\u0010)\u001a\u00020\u0006HÆ\u0003J\t\u0010*\u001a\u00020\u0006HÆ\u0003J\t\u0010+\u001a\u00020\u0006HÆ\u0003J\t\u0010,\u001a\u00020\u0006HÆ\u0003J\t\u0010-\u001a\u00020\u0003HÆ\u0003J\t\u0010.\u001a\u00020\u0003HÆ\u0003J\t\u0010/\u001a\u00020\rHÆ\u0003Jc\u00100\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\rHÆ\u0001J\u0013\u00101\u001a\u00020\r2\b\u00102\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00103\u001a\u00020\u0006HÖ\u0001J\u000e\u00104\u001a\u0002052\u0006\u00106\u001a\u000207J\t\u00108\u001a\u000209HÖ\u0001R\u001a\u0010\n\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\f\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0007\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\t\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0018\"\u0004\b\u001c\u0010\u001aR\u001a\u0010\b\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0018\"\u0004\b\u001e\u0010\u001aR\u001a\u0010\u000b\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0010\"\u0004\b \u0010\u0012R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0018\"\u0004\b\"\u0010\u001aR\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0010\"\u0004\b$\u0010\u0012R\u001a\u0010\u0004\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0010\"\u0004\b&\u0010\u0012¨\u0006:"}, mo64987d2 = {"Lcom/android/systemui/util/animation/WidgetState;", "", "x", "", "y", "width", "", "height", "measureWidth", "measureHeight", "alpha", "scale", "gone", "", "(FFIIIIFFZ)V", "getAlpha", "()F", "setAlpha", "(F)V", "getGone", "()Z", "setGone", "(Z)V", "getHeight", "()I", "setHeight", "(I)V", "getMeasureHeight", "setMeasureHeight", "getMeasureWidth", "setMeasureWidth", "getScale", "setScale", "getWidth", "setWidth", "getX", "setX", "getY", "setY", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "initFromLayout", "", "view", "Landroid/view/View;", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TransitionLayout.kt */
public final class WidgetState {
    private float alpha;
    private boolean gone;
    private int height;
    private int measureHeight;
    private int measureWidth;
    private float scale;
    private int width;

    /* renamed from: x */
    private float f398x;

    /* renamed from: y */
    private float f399y;

    public WidgetState() {
        this(0.0f, 0.0f, 0, 0, 0, 0, 0.0f, 0.0f, false, 511, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ WidgetState copy$default(WidgetState widgetState, float f, float f2, int i, int i2, int i3, int i4, float f3, float f4, boolean z, int i5, Object obj) {
        WidgetState widgetState2 = widgetState;
        int i6 = i5;
        return widgetState.copy((i6 & 1) != 0 ? widgetState2.f398x : f, (i6 & 2) != 0 ? widgetState2.f399y : f2, (i6 & 4) != 0 ? widgetState2.width : i, (i6 & 8) != 0 ? widgetState2.height : i2, (i6 & 16) != 0 ? widgetState2.measureWidth : i3, (i6 & 32) != 0 ? widgetState2.measureHeight : i4, (i6 & 64) != 0 ? widgetState2.alpha : f3, (i6 & 128) != 0 ? widgetState2.scale : f4, (i6 & 256) != 0 ? widgetState2.gone : z);
    }

    public final float component1() {
        return this.f398x;
    }

    public final float component2() {
        return this.f399y;
    }

    public final int component3() {
        return this.width;
    }

    public final int component4() {
        return this.height;
    }

    public final int component5() {
        return this.measureWidth;
    }

    public final int component6() {
        return this.measureHeight;
    }

    public final float component7() {
        return this.alpha;
    }

    public final float component8() {
        return this.scale;
    }

    public final boolean component9() {
        return this.gone;
    }

    public final WidgetState copy(float f, float f2, int i, int i2, int i3, int i4, float f3, float f4, boolean z) {
        return new WidgetState(f, f2, i, i2, i3, i4, f3, f4, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WidgetState)) {
            return false;
        }
        WidgetState widgetState = (WidgetState) obj;
        return Intrinsics.areEqual((Object) Float.valueOf(this.f398x), (Object) Float.valueOf(widgetState.f398x)) && Intrinsics.areEqual((Object) Float.valueOf(this.f399y), (Object) Float.valueOf(widgetState.f399y)) && this.width == widgetState.width && this.height == widgetState.height && this.measureWidth == widgetState.measureWidth && this.measureHeight == widgetState.measureHeight && Intrinsics.areEqual((Object) Float.valueOf(this.alpha), (Object) Float.valueOf(widgetState.alpha)) && Intrinsics.areEqual((Object) Float.valueOf(this.scale), (Object) Float.valueOf(widgetState.scale)) && this.gone == widgetState.gone;
    }

    public int hashCode() {
        int hashCode = ((((((((((((((Float.hashCode(this.f398x) * 31) + Float.hashCode(this.f399y)) * 31) + Integer.hashCode(this.width)) * 31) + Integer.hashCode(this.height)) * 31) + Integer.hashCode(this.measureWidth)) * 31) + Integer.hashCode(this.measureHeight)) * 31) + Float.hashCode(this.alpha)) * 31) + Float.hashCode(this.scale)) * 31;
        boolean z = this.gone;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    public String toString() {
        return "WidgetState(x=" + this.f398x + ", y=" + this.f399y + ", width=" + this.width + ", height=" + this.height + ", measureWidth=" + this.measureWidth + ", measureHeight=" + this.measureHeight + ", alpha=" + this.alpha + ", scale=" + this.scale + ", gone=" + this.gone + ')';
    }

    public WidgetState(float f, float f2, int i, int i2, int i3, int i4, float f3, float f4, boolean z) {
        this.f398x = f;
        this.f399y = f2;
        this.width = i;
        this.height = i2;
        this.measureWidth = i3;
        this.measureHeight = i4;
        this.alpha = f3;
        this.scale = f4;
        this.gone = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ WidgetState(float f, float f2, int i, int i2, int i3, int i4, float f3, float f4, boolean z, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this((i5 & 1) != 0 ? 0.0f : f, (i5 & 2) != 0 ? 0.0f : f2, (i5 & 4) != 0 ? 0 : i, (i5 & 8) != 0 ? 0 : i2, (i5 & 16) != 0 ? 0 : i3, (i5 & 32) != 0 ? 0 : i4, (i5 & 64) != 0 ? 1.0f : f3, (i5 & 128) != 0 ? 1.0f : f4, (i5 & 256) != 0 ? false : z);
    }

    public final float getX() {
        return this.f398x;
    }

    public final void setX(float f) {
        this.f398x = f;
    }

    public final float getY() {
        return this.f399y;
    }

    public final void setY(float f) {
        this.f399y = f;
    }

    public final int getWidth() {
        return this.width;
    }

    public final void setWidth(int i) {
        this.width = i;
    }

    public final int getHeight() {
        return this.height;
    }

    public final void setHeight(int i) {
        this.height = i;
    }

    public final int getMeasureWidth() {
        return this.measureWidth;
    }

    public final void setMeasureWidth(int i) {
        this.measureWidth = i;
    }

    public final int getMeasureHeight() {
        return this.measureHeight;
    }

    public final void setMeasureHeight(int i) {
        this.measureHeight = i;
    }

    public final float getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(float f) {
        this.alpha = f;
    }

    public final float getScale() {
        return this.scale;
    }

    public final void setScale(float f) {
        this.scale = f;
    }

    public final boolean getGone() {
        return this.gone;
    }

    public final void setGone(boolean z) {
        this.gone = z;
    }

    public final void initFromLayout(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        boolean z = true;
        boolean z2 = view.getVisibility() == 8;
        this.gone = z2;
        if (z2) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null) {
                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
                this.f398x = (float) layoutParams2.getConstraintWidget().getLeft();
                this.f399y = (float) layoutParams2.getConstraintWidget().getTop();
                this.width = layoutParams2.getConstraintWidget().getWidth();
                int height2 = layoutParams2.getConstraintWidget().getHeight();
                this.height = height2;
                this.measureHeight = height2;
                this.measureWidth = this.width;
                this.alpha = 0.0f;
                this.scale = 0.0f;
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
        }
        this.f398x = (float) view.getLeft();
        this.f399y = (float) view.getTop();
        this.width = view.getWidth();
        int height3 = view.getHeight();
        this.height = height3;
        this.measureWidth = this.width;
        this.measureHeight = height3;
        if (view.getVisibility() != 8) {
            z = false;
        }
        this.gone = z;
        this.alpha = view.getAlpha();
        this.scale = 1.0f;
    }
}
