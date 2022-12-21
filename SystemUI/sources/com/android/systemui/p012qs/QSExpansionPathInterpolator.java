package com.android.systemui.p012qs;

import android.view.animation.Interpolator;
import javax.inject.Singleton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Singleton
@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\f\u0010\n¨\u0006\u0010"}, mo64987d2 = {"Lcom/android/systemui/qs/QSExpansionPathInterpolator;", "", "()V", "lastX", "", "pathInterpolatorBuilder", "Lcom/android/systemui/qs/PathInterpolatorBuilder;", "xInterpolator", "Landroid/view/animation/Interpolator;", "getXInterpolator", "()Landroid/view/animation/Interpolator;", "yInterpolator", "getYInterpolator", "setControlX2", "", "value", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.QSExpansionPathInterpolator */
/* compiled from: QSExpansionPathInterpolator.kt */
public final class QSExpansionPathInterpolator {
    private float lastX;
    private PathInterpolatorBuilder pathInterpolatorBuilder = new PathInterpolatorBuilder(0.0f, 0.0f, 0.0f, 1.0f);

    public final Interpolator getXInterpolator() {
        Interpolator xInterpolator = this.pathInterpolatorBuilder.getXInterpolator();
        Intrinsics.checkNotNullExpressionValue(xInterpolator, "pathInterpolatorBuilder.xInterpolator");
        return xInterpolator;
    }

    public final Interpolator getYInterpolator() {
        Interpolator yInterpolator = this.pathInterpolatorBuilder.getYInterpolator();
        Intrinsics.checkNotNullExpressionValue(yInterpolator, "pathInterpolatorBuilder.yInterpolator");
        return yInterpolator;
    }

    public final void setControlX2(float f) {
        if (!(f == this.lastX)) {
            this.lastX = f;
            this.pathInterpolatorBuilder = new PathInterpolatorBuilder(0.0f, 0.0f, this.lastX, 1.0f);
        }
    }
}
