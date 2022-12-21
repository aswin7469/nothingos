package com.android.systemui.statusbar.phone.shade.transition;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitShadeOverScroller$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SplitShadeOverScroller f$0;

    public /* synthetic */ SplitShadeOverScroller$$ExternalSyntheticLambda0(SplitShadeOverScroller splitShadeOverScroller) {
        this.f$0 = splitShadeOverScroller;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SplitShadeOverScroller.m3216releaseOverScroll$lambda0(this.f$0, valueAnimator);
    }
}
