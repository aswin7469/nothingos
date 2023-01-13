package com.android.p019wm.shell.transition;

import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.transition.RemoteTransitionHandler;
import com.android.p019wm.shell.transition.Transitions;

/* renamed from: com.android.wm.shell.transition.RemoteTransitionHandler$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RemoteTransitionHandler$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RemoteTransitionHandler.C36231 f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;
    public final /* synthetic */ SurfaceControl.Transaction f$2;
    public final /* synthetic */ IBinder f$3;
    public final /* synthetic */ Transitions.TransitionFinishCallback f$4;
    public final /* synthetic */ WindowContainerTransaction f$5;

    public /* synthetic */ RemoteTransitionHandler$1$$ExternalSyntheticLambda0(RemoteTransitionHandler.C36231 r1, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
        this.f$0 = r1;
        this.f$1 = transaction;
        this.f$2 = transaction2;
        this.f$3 = iBinder;
        this.f$4 = transitionFinishCallback;
        this.f$5 = windowContainerTransaction;
    }

    public final void run() {
        this.f$0.mo51271x67d3e2b9(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
