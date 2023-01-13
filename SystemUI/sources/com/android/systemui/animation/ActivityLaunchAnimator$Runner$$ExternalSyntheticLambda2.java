package com.android.systemui.animation;

import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;
import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ActivityLaunchAnimator$Runner$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ActivityLaunchAnimator.Runner f$0;
    public final /* synthetic */ RemoteAnimationTarget[] f$1;
    public final /* synthetic */ RemoteAnimationTarget[] f$2;
    public final /* synthetic */ IRemoteAnimationFinishedCallback f$3;

    public /* synthetic */ ActivityLaunchAnimator$Runner$$ExternalSyntheticLambda2(ActivityLaunchAnimator.Runner runner, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        this.f$0 = runner;
        this.f$1 = remoteAnimationTargetArr;
        this.f$2 = remoteAnimationTargetArr2;
        this.f$3 = iRemoteAnimationFinishedCallback;
    }

    public final void run() {
        ActivityLaunchAnimator.Runner.m2541onAnimationStart$lambda1(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
