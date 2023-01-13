package com.android.p019wm.shell.transition;

import android.os.IBinder;
import com.android.p019wm.shell.transition.Transitions;

/* renamed from: com.android.wm.shell.transition.OneShotRemoteHandler$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class OneShotRemoteHandler$$ExternalSyntheticLambda0 implements IBinder.DeathRecipient {
    public final /* synthetic */ OneShotRemoteHandler f$0;
    public final /* synthetic */ Transitions.TransitionFinishCallback f$1;

    public /* synthetic */ OneShotRemoteHandler$$ExternalSyntheticLambda0(OneShotRemoteHandler oneShotRemoteHandler, Transitions.TransitionFinishCallback transitionFinishCallback) {
        this.f$0 = oneShotRemoteHandler;
        this.f$1 = transitionFinishCallback;
    }

    public final void binderDied() {
        this.f$0.mo51264x2986f74a(this.f$1);
    }
}
