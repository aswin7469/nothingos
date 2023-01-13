package com.android.systemui.shared.system;

import android.window.IRemoteTransitionFinishedCallback;
import com.android.systemui.shared.system.RemoteTransitionCompat;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RemoteTransitionCompat$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ IRemoteTransitionFinishedCallback f$0;

    public /* synthetic */ RemoteTransitionCompat$1$$ExternalSyntheticLambda0(IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
        this.f$0 = iRemoteTransitionFinishedCallback;
    }

    public final void run() {
        RemoteTransitionCompat.C25201.lambda$mergeAnimation$2(this.f$0);
    }
}
