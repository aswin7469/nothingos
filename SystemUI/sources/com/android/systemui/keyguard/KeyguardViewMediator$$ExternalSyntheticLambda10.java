package com.android.systemui.keyguard;

import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KeyguardViewMediator$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ KeyguardViewMediator f$0;
    public final /* synthetic */ IRemoteAnimationFinishedCallback f$1;
    public final /* synthetic */ RemoteAnimationTarget[] f$2;

    public /* synthetic */ KeyguardViewMediator$$ExternalSyntheticLambda10(KeyguardViewMediator keyguardViewMediator, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, RemoteAnimationTarget[] remoteAnimationTargetArr) {
        this.f$0 = keyguardViewMediator;
        this.f$1 = iRemoteAnimationFinishedCallback;
        this.f$2 = remoteAnimationTargetArr;
    }

    public final void run() {
        this.f$0.mo33257x5fb004b2(this.f$1, this.f$2);
    }
}
