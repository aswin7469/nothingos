package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SystemEventChipAnimationController$$ExternalSyntheticLambda5 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SystemEventChipAnimationController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ SystemEventChipAnimationController$$ExternalSyntheticLambda5(SystemEventChipAnimationController systemEventChipAnimationController, int i) {
        this.f$0 = systemEventChipAnimationController;
        this.f$1 = i;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SystemEventChipAnimationController.m3071createMoveOutAnimationForDot$lambda12$lambda11(this.f$0, this.f$1, valueAnimator);
    }
}
