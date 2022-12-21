package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import android.graphics.Rect;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScreenshotView$$ExternalSyntheticLambda19 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ScreenshotView f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ Rect f$3;
    public final /* synthetic */ float f$4;

    public /* synthetic */ ScreenshotView$$ExternalSyntheticLambda19(ScreenshotView screenshotView, float f, float f2, Rect rect, float f3) {
        this.f$0 = screenshotView;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = rect;
        this.f$4 = f3;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo37513xad33ca28(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}
