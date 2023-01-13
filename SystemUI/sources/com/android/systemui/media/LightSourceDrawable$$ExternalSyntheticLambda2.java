package com.android.systemui.media;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LightSourceDrawable$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LightSourceDrawable f$0;

    public /* synthetic */ LightSourceDrawable$$ExternalSyntheticLambda2(LightSourceDrawable lightSourceDrawable) {
        this.f$0 = lightSourceDrawable;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        LightSourceDrawable.m2770_set_active_$lambda1$lambda0(this.f$0, valueAnimator);
    }
}
