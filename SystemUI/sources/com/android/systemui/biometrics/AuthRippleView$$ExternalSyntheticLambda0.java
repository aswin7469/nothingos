package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AuthRippleView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthRippleView f$0;

    public /* synthetic */ AuthRippleView$$ExternalSyntheticLambda0(AuthRippleView authRippleView) {
        this.f$0 = authRippleView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        AuthRippleView.m2566retractDwellRipple$lambda1$lambda0(this.f$0, valueAnimator);
    }
}
