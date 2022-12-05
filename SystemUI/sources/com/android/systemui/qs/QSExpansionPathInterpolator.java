package com.android.systemui.qs;

import android.view.animation.Interpolator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: QSExpansionPathInterpolator.kt */
/* loaded from: classes.dex */
public final class QSExpansionPathInterpolator {
    @NotNull
    private PathInterpolatorBuilder pathInterpolatorBuilder = new PathInterpolatorBuilder(0.0f, 0.0f, 0.0f, 1.0f);
    @NotNull
    private PathInterpolatorBuilder expandCircleInterpolatorBuilder = new PathInterpolatorBuilder(0.4f, 0.0f, 0.2f, 1.0f);
    @NotNull
    private PathInterpolatorBuilder collapseCircleInterpolatorBuilder = new PathInterpolatorBuilder(0.4f, 0.0f, 1.0f, 1.0f);

    @NotNull
    public final Interpolator getXInterpolator() {
        Interpolator xInterpolator = this.pathInterpolatorBuilder.getXInterpolator();
        Intrinsics.checkNotNullExpressionValue(xInterpolator, "pathInterpolatorBuilder.xInterpolator");
        return xInterpolator;
    }

    @NotNull
    public final Interpolator getYInterpolator() {
        Interpolator yInterpolator = this.pathInterpolatorBuilder.getYInterpolator();
        Intrinsics.checkNotNullExpressionValue(yInterpolator, "pathInterpolatorBuilder.yInterpolator");
        return yInterpolator;
    }
}
