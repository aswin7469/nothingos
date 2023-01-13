package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import com.android.systemui.statusbar.LightRevealScrim;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AuthRippleController$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LightRevealScrim f$0;
    public final /* synthetic */ AuthRippleController f$1;
    public final /* synthetic */ ValueAnimator f$2;

    public /* synthetic */ AuthRippleController$$ExternalSyntheticLambda0(LightRevealScrim lightRevealScrim, AuthRippleController authRippleController, ValueAnimator valueAnimator) {
        this.f$0 = lightRevealScrim;
        this.f$1 = authRippleController;
        this.f$2 = valueAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        AuthRippleController.m2566onKeyguardFadingAwayChanged$lambda3$lambda2(this.f$0, this.f$1, this.f$2, valueAnimator);
    }
}
