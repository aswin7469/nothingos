package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AuthRippleView$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthRippleView f$0;

    public /* synthetic */ AuthRippleView$$ExternalSyntheticLambda3(AuthRippleView authRippleView) {
        this.f$0 = authRippleView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        AuthRippleView.m2568startDwellRipple$lambda10$lambda9(this.f$0, valueAnimator);
    }
}
