package com.android.systemui;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DisplayCutoutBaseView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DisplayCutoutBaseView f$0;

    public /* synthetic */ DisplayCutoutBaseView$$ExternalSyntheticLambda0(DisplayCutoutBaseView displayCutoutBaseView) {
        this.f$0 = displayCutoutBaseView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        DisplayCutoutBaseView.m2520enableShowProtection$lambda1(this.f$0, valueAnimator);
    }
}
