package com.android.systemui.util.animation;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u001a\u0010\u0004\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t¨\u0006\u0015"}, mo64987d2 = {"Lcom/android/systemui/util/animation/MeasurementOutput;", "", "measuredWidth", "", "measuredHeight", "(II)V", "getMeasuredHeight", "()I", "setMeasuredHeight", "(I)V", "getMeasuredWidth", "setMeasuredWidth", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MeasurementInput.kt */
public final class MeasurementOutput {
    private int measuredHeight;
    private int measuredWidth;

    public static /* synthetic */ MeasurementOutput copy$default(MeasurementOutput measurementOutput, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = measurementOutput.measuredWidth;
        }
        if ((i3 & 2) != 0) {
            i2 = measurementOutput.measuredHeight;
        }
        return measurementOutput.copy(i, i2);
    }

    public final int component1() {
        return this.measuredWidth;
    }

    public final int component2() {
        return this.measuredHeight;
    }

    public final MeasurementOutput copy(int i, int i2) {
        return new MeasurementOutput(i, i2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MeasurementOutput)) {
            return false;
        }
        MeasurementOutput measurementOutput = (MeasurementOutput) obj;
        return this.measuredWidth == measurementOutput.measuredWidth && this.measuredHeight == measurementOutput.measuredHeight;
    }

    public int hashCode() {
        return (Integer.hashCode(this.measuredWidth) * 31) + Integer.hashCode(this.measuredHeight);
    }

    public String toString() {
        return "MeasurementOutput(measuredWidth=" + this.measuredWidth + ", measuredHeight=" + this.measuredHeight + ')';
    }

    public MeasurementOutput(int i, int i2) {
        this.measuredWidth = i;
        this.measuredHeight = i2;
    }

    public final int getMeasuredWidth() {
        return this.measuredWidth;
    }

    public final void setMeasuredWidth(int i) {
        this.measuredWidth = i;
    }

    public final int getMeasuredHeight() {
        return this.measuredHeight;
    }

    public final void setMeasuredHeight(int i) {
        this.measuredHeight = i;
    }
}
