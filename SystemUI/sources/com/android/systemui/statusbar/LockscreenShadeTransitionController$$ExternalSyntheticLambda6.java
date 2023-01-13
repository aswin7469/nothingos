package com.android.systemui.statusbar;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LockscreenShadeTransitionController$$ExternalSyntheticLambda6 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LockscreenShadeTransitionController f$0;

    public /* synthetic */ LockscreenShadeTransitionController$$ExternalSyntheticLambda6(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        this.f$0 = lockscreenShadeTransitionController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        LockscreenShadeTransitionController.m3036setDragDownAmountAnimated$lambda4(this.f$0, valueAnimator);
    }
}
