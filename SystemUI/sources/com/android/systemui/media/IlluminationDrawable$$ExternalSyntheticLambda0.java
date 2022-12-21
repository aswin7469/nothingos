package com.android.systemui.media;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class IlluminationDrawable$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ IlluminationDrawable f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ IlluminationDrawable$$ExternalSyntheticLambda0(IlluminationDrawable illuminationDrawable, int i, int i2, int i3) {
        this.f$0 = illuminationDrawable;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        IlluminationDrawable.m2762animateBackground$lambda4$lambda3(this.f$0, this.f$1, this.f$2, this.f$3, valueAnimator);
    }
}
