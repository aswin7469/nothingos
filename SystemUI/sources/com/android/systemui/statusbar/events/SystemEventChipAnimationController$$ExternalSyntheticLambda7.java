package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SystemEventChipAnimationController$$ExternalSyntheticLambda7 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SystemEventChipAnimationController f$0;

    public /* synthetic */ SystemEventChipAnimationController$$ExternalSyntheticLambda7(SystemEventChipAnimationController systemEventChipAnimationController) {
        this.f$0 = systemEventChipAnimationController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SystemEventChipAnimationController.m3065createMoveOutAnimationDefault$lambda17$lambda16(this.f$0, valueAnimator);
    }
}