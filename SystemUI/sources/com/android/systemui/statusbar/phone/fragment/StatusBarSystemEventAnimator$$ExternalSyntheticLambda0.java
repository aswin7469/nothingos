package com.android.systemui.statusbar.phone.fragment;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBarSystemEventAnimator$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StatusBarSystemEventAnimator f$0;

    public /* synthetic */ StatusBarSystemEventAnimator$$ExternalSyntheticLambda0(StatusBarSystemEventAnimator statusBarSystemEventAnimator) {
        this.f$0 = statusBarSystemEventAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        StatusBarSystemEventAnimator.m3203onSystemEventAnimationBegin$lambda0(this.f$0, valueAnimator);
    }
}
