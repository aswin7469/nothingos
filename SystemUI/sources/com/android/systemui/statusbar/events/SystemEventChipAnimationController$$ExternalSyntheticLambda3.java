package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SystemEventChipAnimationController$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SystemEventChipAnimationController f$0;

    public /* synthetic */ SystemEventChipAnimationController$$ExternalSyntheticLambda3(SystemEventChipAnimationController systemEventChipAnimationController) {
        this.f$0 = systemEventChipAnimationController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SystemEventChipAnimationController.m3070createMoveOutAnimationForDot$lambda8$lambda7(this.f$0, valueAnimator);
    }
}
