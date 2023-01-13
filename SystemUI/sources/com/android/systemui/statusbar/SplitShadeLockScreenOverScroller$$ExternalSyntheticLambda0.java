package com.android.systemui.statusbar;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitShadeLockScreenOverScroller$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SplitShadeLockScreenOverScroller f$0;

    public /* synthetic */ SplitShadeLockScreenOverScroller$$ExternalSyntheticLambda0(SplitShadeLockScreenOverScroller splitShadeLockScreenOverScroller) {
        this.f$0 = splitShadeLockScreenOverScroller;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        SplitShadeLockScreenOverScroller.m3045releaseOverScroll$lambda0(this.f$0, valueAnimator);
    }
}
