package com.android.systemui.statusbar.phone.fragment;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBarSystemEventAnimator$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StatusBarSystemEventAnimator f$0;

    public /* synthetic */ StatusBarSystemEventAnimator$$ExternalSyntheticLambda2(StatusBarSystemEventAnimator statusBarSystemEventAnimator) {
        this.f$0 = statusBarSystemEventAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        StatusBarSystemEventAnimator.m3200onSystemEventAnimationFinish$lambda2(this.f$0, valueAnimator);
    }
}
