package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UdfpsAnimationViewController$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ UdfpsAnimationViewController f$0;

    public /* synthetic */ UdfpsAnimationViewController$$ExternalSyntheticLambda2(UdfpsAnimationViewController udfpsAnimationViewController) {
        this.f$0 = udfpsAnimationViewController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        UdfpsAnimationViewController.m2581runDialogAlphaAnimator$lambda3$lambda2(this.f$0, valueAnimator);
    }
}
