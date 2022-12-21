package com.android.keyguard;

import android.animation.TimeInterpolator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AnimatableClockView$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AnimatableClockView f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ Integer f$3;
    public final /* synthetic */ long f$4;
    public final /* synthetic */ TimeInterpolator f$5;
    public final /* synthetic */ long f$6;
    public final /* synthetic */ Runnable f$7;

    public /* synthetic */ AnimatableClockView$$ExternalSyntheticLambda1(AnimatableClockView animatableClockView, int i, float f, Integer num, long j, TimeInterpolator timeInterpolator, long j2, Runnable runnable) {
        this.f$0 = animatableClockView;
        this.f$1 = i;
        this.f$2 = f;
        this.f$3 = num;
        this.f$4 = j;
        this.f$5 = timeInterpolator;
        this.f$6 = j2;
        this.f$7 = runnable;
    }

    public final void run() {
        AnimatableClockView.m2278setTextStyle$lambda1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}
