package com.android.systemui.biometrics;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AuthBiometricView$$ExternalSyntheticLambda8 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthBiometricView f$0;

    public /* synthetic */ AuthBiometricView$$ExternalSyntheticLambda8(AuthBiometricView authBiometricView) {
        this.f$0 = authBiometricView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }
}
