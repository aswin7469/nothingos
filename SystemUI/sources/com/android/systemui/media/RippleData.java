package com.android.systemui.media;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b \n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003¢\u0006\u0002\u0010\nJ\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003JO\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u0003HÆ\u0001J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010&\u001a\u00020'HÖ\u0001J\t\u0010(\u001a\u00020)HÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\t\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u001a\u0010\b\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\f\"\u0004\b\u0012\u0010\u000eR\u001a\u0010\u0007\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001a\u0010\u0006\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\f\"\u0004\b\u0016\u0010\u000eR\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\f\"\u0004\b\u0018\u0010\u000eR\u001a\u0010\u0004\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000e¨\u0006*"}, mo65043d2 = {"Lcom/android/systemui/media/RippleData;", "", "x", "", "y", "alpha", "progress", "minSize", "maxSize", "highlight", "(FFFFFFF)V", "getAlpha", "()F", "setAlpha", "(F)V", "getHighlight", "setHighlight", "getMaxSize", "setMaxSize", "getMinSize", "setMinSize", "getProgress", "setProgress", "getX", "setX", "getY", "setY", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LightSourceDrawable.kt */
final class RippleData {
    private float alpha;
    private float highlight;
    private float maxSize;
    private float minSize;
    private float progress;

    /* renamed from: x */
    private float f316x;

    /* renamed from: y */
    private float f317y;

    public static /* synthetic */ RippleData copy$default(RippleData rippleData, float f, float f2, float f3, float f4, float f5, float f6, float f7, int i, Object obj) {
        if ((i & 1) != 0) {
            f = rippleData.f316x;
        }
        if ((i & 2) != 0) {
            f2 = rippleData.f317y;
        }
        float f8 = f2;
        if ((i & 4) != 0) {
            f3 = rippleData.alpha;
        }
        float f9 = f3;
        if ((i & 8) != 0) {
            f4 = rippleData.progress;
        }
        float f10 = f4;
        if ((i & 16) != 0) {
            f5 = rippleData.minSize;
        }
        float f11 = f5;
        if ((i & 32) != 0) {
            f6 = rippleData.maxSize;
        }
        float f12 = f6;
        if ((i & 64) != 0) {
            f7 = rippleData.highlight;
        }
        return rippleData.copy(f, f8, f9, f10, f11, f12, f7);
    }

    public final float component1() {
        return this.f316x;
    }

    public final float component2() {
        return this.f317y;
    }

    public final float component3() {
        return this.alpha;
    }

    public final float component4() {
        return this.progress;
    }

    public final float component5() {
        return this.minSize;
    }

    public final float component6() {
        return this.maxSize;
    }

    public final float component7() {
        return this.highlight;
    }

    public final RippleData copy(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        return new RippleData(f, f2, f3, f4, f5, f6, f7);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RippleData)) {
            return false;
        }
        RippleData rippleData = (RippleData) obj;
        return Intrinsics.areEqual((Object) Float.valueOf(this.f316x), (Object) Float.valueOf(rippleData.f316x)) && Intrinsics.areEqual((Object) Float.valueOf(this.f317y), (Object) Float.valueOf(rippleData.f317y)) && Intrinsics.areEqual((Object) Float.valueOf(this.alpha), (Object) Float.valueOf(rippleData.alpha)) && Intrinsics.areEqual((Object) Float.valueOf(this.progress), (Object) Float.valueOf(rippleData.progress)) && Intrinsics.areEqual((Object) Float.valueOf(this.minSize), (Object) Float.valueOf(rippleData.minSize)) && Intrinsics.areEqual((Object) Float.valueOf(this.maxSize), (Object) Float.valueOf(rippleData.maxSize)) && Intrinsics.areEqual((Object) Float.valueOf(this.highlight), (Object) Float.valueOf(rippleData.highlight));
    }

    public int hashCode() {
        return (((((((((((Float.hashCode(this.f316x) * 31) + Float.hashCode(this.f317y)) * 31) + Float.hashCode(this.alpha)) * 31) + Float.hashCode(this.progress)) * 31) + Float.hashCode(this.minSize)) * 31) + Float.hashCode(this.maxSize)) * 31) + Float.hashCode(this.highlight);
    }

    public String toString() {
        return "RippleData(x=" + this.f316x + ", y=" + this.f317y + ", alpha=" + this.alpha + ", progress=" + this.progress + ", minSize=" + this.minSize + ", maxSize=" + this.maxSize + ", highlight=" + this.highlight + ')';
    }

    public RippleData(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.f316x = f;
        this.f317y = f2;
        this.alpha = f3;
        this.progress = f4;
        this.minSize = f5;
        this.maxSize = f6;
        this.highlight = f7;
    }

    public final float getX() {
        return this.f316x;
    }

    public final void setX(float f) {
        this.f316x = f;
    }

    public final float getY() {
        return this.f317y;
    }

    public final void setY(float f) {
        this.f317y = f;
    }

    public final float getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(float f) {
        this.alpha = f;
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
    }

    public final float getMinSize() {
        return this.minSize;
    }

    public final void setMinSize(float f) {
        this.minSize = f;
    }

    public final float getMaxSize() {
        return this.maxSize;
    }

    public final void setMaxSize(float f) {
        this.maxSize = f;
    }

    public final float getHighlight() {
        return this.highlight;
    }

    public final void setHighlight(float f) {
        this.highlight = f;
    }
}
