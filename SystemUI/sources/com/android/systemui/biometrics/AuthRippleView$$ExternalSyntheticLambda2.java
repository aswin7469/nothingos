package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AuthRippleView$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthRippleView f$0;

    public /* synthetic */ AuthRippleView$$ExternalSyntheticLambda2(AuthRippleView authRippleView) {
        this.f$0 = authRippleView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        AuthRippleView.m2569startDwellRipple$lambda8$lambda7(this.f$0, valueAnimator);
    }
}
