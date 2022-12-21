package com.android.systemui.media;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0018\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001b\u001a\u00020\bHÆ\u0003J1\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010\u001d\u001a\u00020\b2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÖ\u0001J\t\u0010 \u001a\u00020!HÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0004\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0013\"\u0004\b\u0017\u0010\u0015¨\u0006\""}, mo64987d2 = {"Lcom/android/systemui/media/CacheKey;", "", "widthMeasureSpec", "", "heightMeasureSpec", "expansion", "", "gutsVisible", "", "(IIFZ)V", "getExpansion", "()F", "setExpansion", "(F)V", "getGutsVisible", "()Z", "setGutsVisible", "(Z)V", "getHeightMeasureSpec", "()I", "setHeightMeasureSpec", "(I)V", "getWidthMeasureSpec", "setWidthMeasureSpec", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaViewController.kt */
final class CacheKey {
    private float expansion;
    private boolean gutsVisible;
    private int heightMeasureSpec;
    private int widthMeasureSpec;

    public CacheKey() {
        this(0, 0, 0.0f, false, 15, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ CacheKey copy$default(CacheKey cacheKey, int i, int i2, float f, boolean z, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = cacheKey.widthMeasureSpec;
        }
        if ((i3 & 2) != 0) {
            i2 = cacheKey.heightMeasureSpec;
        }
        if ((i3 & 4) != 0) {
            f = cacheKey.expansion;
        }
        if ((i3 & 8) != 0) {
            z = cacheKey.gutsVisible;
        }
        return cacheKey.copy(i, i2, f, z);
    }

    public final int component1() {
        return this.widthMeasureSpec;
    }

    public final int component2() {
        return this.heightMeasureSpec;
    }

    public final float component3() {
        return this.expansion;
    }

    public final boolean component4() {
        return this.gutsVisible;
    }

    public final CacheKey copy(int i, int i2, float f, boolean z) {
        return new CacheKey(i, i2, f, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheKey)) {
            return false;
        }
        CacheKey cacheKey = (CacheKey) obj;
        return this.widthMeasureSpec == cacheKey.widthMeasureSpec && this.heightMeasureSpec == cacheKey.heightMeasureSpec && Intrinsics.areEqual((Object) Float.valueOf(this.expansion), (Object) Float.valueOf(cacheKey.expansion)) && this.gutsVisible == cacheKey.gutsVisible;
    }

    public int hashCode() {
        int hashCode = ((((Integer.hashCode(this.widthMeasureSpec) * 31) + Integer.hashCode(this.heightMeasureSpec)) * 31) + Float.hashCode(this.expansion)) * 31;
        boolean z = this.gutsVisible;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    public String toString() {
        return "CacheKey(widthMeasureSpec=" + this.widthMeasureSpec + ", heightMeasureSpec=" + this.heightMeasureSpec + ", expansion=" + this.expansion + ", gutsVisible=" + this.gutsVisible + ')';
    }

    public CacheKey(int i, int i2, float f, boolean z) {
        this.widthMeasureSpec = i;
        this.heightMeasureSpec = i2;
        this.expansion = f;
        this.gutsVisible = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ CacheKey(int i, int i2, float f, boolean z, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? -1 : i, (i3 & 2) != 0 ? -1 : i2, (i3 & 4) != 0 ? 0.0f : f, (i3 & 8) != 0 ? false : z);
    }

    public final int getWidthMeasureSpec() {
        return this.widthMeasureSpec;
    }

    public final void setWidthMeasureSpec(int i) {
        this.widthMeasureSpec = i;
    }

    public final int getHeightMeasureSpec() {
        return this.heightMeasureSpec;
    }

    public final void setHeightMeasureSpec(int i) {
        this.heightMeasureSpec = i;
    }

    public final float getExpansion() {
        return this.expansion;
    }

    public final void setExpansion(float f) {
        this.expansion = f;
    }

    public final boolean getGutsVisible() {
        return this.gutsVisible;
    }

    public final void setGutsVisible(boolean z) {
        this.gutsVisible = z;
    }
}
