package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import java.util.ArrayList;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScreenshotView$$ExternalSyntheticLambda14 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ScreenshotView f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ ScreenshotView$$ExternalSyntheticLambda14(ScreenshotView screenshotView, float f, ArrayList arrayList) {
        this.f$0 = screenshotView;
        this.f$1 = f;
        this.f$2 = arrayList;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo37499x6fe5c40c(this.f$1, this.f$2, valueAnimator);
    }
}
