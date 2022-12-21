package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AuthRippleView$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthRippleView f$0;

    public /* synthetic */ AuthRippleView$$ExternalSyntheticLambda1(AuthRippleView authRippleView) {
        this.f$0 = authRippleView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        AuthRippleView.m2567retractDwellRipple$lambda3$lambda2(this.f$0, valueAnimator);
    }
}
