package com.android.systemui.statusbar;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LockscreenShadeTransitionController$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LockscreenShadeTransitionController f$0;

    public /* synthetic */ LockscreenShadeTransitionController$$ExternalSyntheticLambda3(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        this.f$0 = lockscreenShadeTransitionController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        LockscreenShadeTransitionController.m3037setPulseHeight$lambda9(this.f$0, valueAnimator);
    }
}
