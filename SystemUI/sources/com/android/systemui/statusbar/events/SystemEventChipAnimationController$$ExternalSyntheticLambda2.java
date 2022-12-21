package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SystemEventChipAnimationController$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SystemEventChipAnimationController f$0;

    public /* synthetic */ SystemEventChipAnimationController$$ExternalSyntheticLambda2(SystemEventChipAnimationController systemEventChipAnimationController) {
        this.f$0 = systemEventChipAnimationController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SystemEventChipAnimationController.m3069createMoveOutAnimationForDot$lambda6$lambda5(this.f$0, valueAnimator);
    }
}
