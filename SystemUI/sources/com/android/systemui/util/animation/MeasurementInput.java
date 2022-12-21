package com.android.systemui.util.animation;

import android.view.View;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0006\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\u0004\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\b\"\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\r\u0010\bR\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\b\"\u0004\b\u000f\u0010\u000b¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/util/animation/MeasurementInput;", "", "widthMeasureSpec", "", "heightMeasureSpec", "(II)V", "height", "getHeight", "()I", "getHeightMeasureSpec", "setHeightMeasureSpec", "(I)V", "width", "getWidth", "getWidthMeasureSpec", "setWidthMeasureSpec", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MeasurementInput.kt */
public final class MeasurementInput {
    private int heightMeasureSpec;
    private int widthMeasureSpec;

    public static /* synthetic */ MeasurementInput copy$default(MeasurementInput measurementInput, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = measurementInput.widthMeasureSpec;
        }
        if ((i3 & 2) != 0) {
            i2 = measurementInput.heightMeasureSpec;
        }
        return measurementInput.copy(i, i2);
    }

    public final int component1() {
        return this.widthMeasureSpec;
    }

    public final int component2() {
        return this.heightMeasureSpec;
    }

    public final MeasurementInput copy(int i, int i2) {
        return new MeasurementInput(i, i2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MeasurementInput)) {
            return false;
        }
        MeasurementInput measurementInput = (MeasurementInput) obj;
        return this.widthMeasureSpec == measurementInput.widthMeasureSpec && this.heightMeasureSpec == measurementInput.heightMeasureSpec;
    }

    public int hashCode() {
        return (Integer.hashCode(this.widthMeasureSpec) * 31) + Integer.hashCode(this.heightMeasureSpec);
    }

    public String toString() {
        return "MeasurementInput(widthMeasureSpec=" + this.widthMeasureSpec + ", heightMeasureSpec=" + this.heightMeasureSpec + ')';
    }

    public MeasurementInput(int i, int i2) {
        this.widthMeasureSpec = i;
        this.heightMeasureSpec = i2;
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

    public final int getWidth() {
        return View.MeasureSpec.getSize(this.widthMeasureSpec);
    }

    public final int getHeight() {
        return View.MeasureSpec.getSize(this.heightMeasureSpec);
    }
}
