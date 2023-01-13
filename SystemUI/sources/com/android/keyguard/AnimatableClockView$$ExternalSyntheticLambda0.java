package com.android.keyguard;

import com.android.keyguard.AnimatableClockView;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AnimatableClockView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AnimatableClockView f$0;
    public final /* synthetic */ AnimatableClockView.DozeStateGetter f$1;

    public /* synthetic */ AnimatableClockView$$ExternalSyntheticLambda0(AnimatableClockView animatableClockView, AnimatableClockView.DozeStateGetter dozeStateGetter) {
        this.f$0 = animatableClockView;
        this.f$1 = dozeStateGetter;
    }

    public final void run() {
        AnimatableClockView.m2283animateCharge$lambda0(this.f$0, this.f$1);
    }
}
