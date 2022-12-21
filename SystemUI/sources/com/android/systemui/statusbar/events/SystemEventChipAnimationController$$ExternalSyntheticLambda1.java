package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SystemEventChipAnimationController$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SystemEventChipAnimationController f$0;
    public final /* synthetic */ ValueAnimator f$1;

    public /* synthetic */ SystemEventChipAnimationController$$ExternalSyntheticLambda1(SystemEventChipAnimationController systemEventChipAnimationController, ValueAnimator valueAnimator) {
        this.f$0 = systemEventChipAnimationController;
        this.f$1 = valueAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SystemEventChipAnimationController.m3072onSystemEventAnimationBegin$lambda4$lambda3(this.f$0, this.f$1, valueAnimator);
    }
}
