package com.android.systemui.keyguard;

import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import com.android.systemui.keyguard.KeyguardViewMediator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KeyguardViewMediator$6$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ KeyguardViewMediator.C21726 f$0;
    public final /* synthetic */ RemoteAnimationTarget f$1;
    public final /* synthetic */ SyncRtSurfaceTransactionApplier f$2;
    public final /* synthetic */ IRemoteAnimationFinishedCallback f$3;

    public /* synthetic */ KeyguardViewMediator$6$$ExternalSyntheticLambda1(KeyguardViewMediator.C21726 r1, RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        this.f$0 = r1;
        this.f$1 = remoteAnimationTarget;
        this.f$2 = syncRtSurfaceTransactionApplier;
        this.f$3 = iRemoteAnimationFinishedCallback;
    }

    public final void run() {
        this.f$0.mo33301x31a5aa94(this.f$1, this.f$2, this.f$3);
    }
}
