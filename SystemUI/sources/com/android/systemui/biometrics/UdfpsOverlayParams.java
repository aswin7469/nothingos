package com.android.systemui.biometrics;

import android.graphics.Rect;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B7\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005¢\u0006\u0002\u0010\nJ\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001a\u001a\u00020\bHÆ\u0003J\t\u0010\u001b\u001a\u00020\u0005HÆ\u0003J;\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010 \u001a\u00020\u0005HÖ\u0001J\t\u0010!\u001a\u00020\"HÖ\u0001R\u0011\u0010\u000b\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\t\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006#"}, mo64987d2 = {"Lcom/android/systemui/biometrics/UdfpsOverlayParams;", "", "sensorBounds", "Landroid/graphics/Rect;", "naturalDisplayWidth", "", "naturalDisplayHeight", "scaleFactor", "", "rotation", "(Landroid/graphics/Rect;IIFI)V", "logicalDisplayHeight", "getLogicalDisplayHeight", "()I", "logicalDisplayWidth", "getLogicalDisplayWidth", "getNaturalDisplayHeight", "getNaturalDisplayWidth", "getRotation", "getScaleFactor", "()F", "getSensorBounds", "()Landroid/graphics/Rect;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UdfpsOverlayParams.kt */
public final class UdfpsOverlayParams {
    private final int naturalDisplayHeight;
    private final int naturalDisplayWidth;
    private final int rotation;
    private final float scaleFactor;
    private final Rect sensorBounds;

    public UdfpsOverlayParams() {
        this((Rect) null, 0, 0, 0.0f, 0, 31, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ UdfpsOverlayParams copy$default(UdfpsOverlayParams udfpsOverlayParams, Rect rect, int i, int i2, float f, int i3, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            rect = udfpsOverlayParams.sensorBounds;
        }
        if ((i4 & 2) != 0) {
            i = udfpsOverlayParams.naturalDisplayWidth;
        }
        int i5 = i;
        if ((i4 & 4) != 0) {
            i2 = udfpsOverlayParams.naturalDisplayHeight;
        }
        int i6 = i2;
        if ((i4 & 8) != 0) {
            f = udfpsOverlayParams.scaleFactor;
        }
        float f2 = f;
        if ((i4 & 16) != 0) {
            i3 = udfpsOverlayParams.rotation;
        }
        return udfpsOverlayParams.copy(rect, i5, i6, f2, i3);
    }

    public final Rect component1() {
        return this.sensorBounds;
    }

    public final int component2() {
        return this.naturalDisplayWidth;
    }

    public final int component3() {
        return this.naturalDisplayHeight;
    }

    public final float component4() {
        return this.scaleFactor;
    }

    public final int component5() {
        return this.rotation;
    }

    public final UdfpsOverlayParams copy(Rect rect, int i, int i2, float f, int i3) {
        Intrinsics.checkNotNullParameter(rect, "sensorBounds");
        return new UdfpsOverlayParams(rect, i, i2, f, i3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UdfpsOverlayParams)) {
            return false;
        }
        UdfpsOverlayParams udfpsOverlayParams = (UdfpsOverlayParams) obj;
        return Intrinsics.areEqual((Object) this.sensorBounds, (Object) udfpsOverlayParams.sensorBounds) && this.naturalDisplayWidth == udfpsOverlayParams.naturalDisplayWidth && this.naturalDisplayHeight == udfpsOverlayParams.naturalDisplayHeight && Intrinsics.areEqual((Object) Float.valueOf(this.scaleFactor), (Object) Float.valueOf(udfpsOverlayParams.scaleFactor)) && this.rotation == udfpsOverlayParams.rotation;
    }

    public int hashCode() {
        return (((((((this.sensorBounds.hashCode() * 31) + Integer.hashCode(this.naturalDisplayWidth)) * 31) + Integer.hashCode(this.naturalDisplayHeight)) * 31) + Float.hashCode(this.scaleFactor)) * 31) + Integer.hashCode(this.rotation);
    }

    public String toString() {
        return "UdfpsOverlayParams(sensorBounds=" + this.sensorBounds + ", naturalDisplayWidth=" + this.naturalDisplayWidth + ", naturalDisplayHeight=" + this.naturalDisplayHeight + ", scaleFactor=" + this.scaleFactor + ", rotation=" + this.rotation + ')';
    }

    public UdfpsOverlayParams(Rect rect, int i, int i2, float f, int i3) {
        Intrinsics.checkNotNullParameter(rect, "sensorBounds");
        this.sensorBounds = rect;
        this.naturalDisplayWidth = i;
        this.naturalDisplayHeight = i2;
        this.scaleFactor = f;
        this.rotation = i3;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ UdfpsOverlayParams(android.graphics.Rect r4, int r5, int r6, float r7, int r8, int r9, kotlin.jvm.internal.DefaultConstructorMarker r10) {
        /*
            r3 = this;
            r10 = r9 & 1
            if (r10 == 0) goto L_0x0009
            android.graphics.Rect r4 = new android.graphics.Rect
            r4.<init>()
        L_0x0009:
            r10 = r9 & 2
            r0 = 0
            if (r10 == 0) goto L_0x0010
            r10 = r0
            goto L_0x0011
        L_0x0010:
            r10 = r5
        L_0x0011:
            r5 = r9 & 4
            if (r5 == 0) goto L_0x0017
            r1 = r0
            goto L_0x0018
        L_0x0017:
            r1 = r6
        L_0x0018:
            r5 = r9 & 8
            if (r5 == 0) goto L_0x001e
            r7 = 1065353216(0x3f800000, float:1.0)
        L_0x001e:
            r2 = r7
            r5 = r9 & 16
            if (r5 == 0) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r0 = r8
        L_0x0025:
            r5 = r3
            r6 = r4
            r7 = r10
            r8 = r1
            r9 = r2
            r10 = r0
            r5.<init>(r6, r7, r8, r9, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsOverlayParams.<init>(android.graphics.Rect, int, int, float, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final Rect getSensorBounds() {
        return this.sensorBounds;
    }

    public final int getNaturalDisplayWidth() {
        return this.naturalDisplayWidth;
    }

    public final int getNaturalDisplayHeight() {
        return this.naturalDisplayHeight;
    }

    public final float getScaleFactor() {
        return this.scaleFactor;
    }

    public final int getRotation() {
        return this.rotation;
    }

    public final int getLogicalDisplayWidth() {
        int i = this.rotation;
        if (i == 1 || i == 3) {
            return this.naturalDisplayHeight;
        }
        return this.naturalDisplayWidth;
    }

    public final int getLogicalDisplayHeight() {
        int i = this.rotation;
        if (i == 1 || i == 3) {
            return this.naturalDisplayWidth;
        }
        return this.naturalDisplayHeight;
    }
}
