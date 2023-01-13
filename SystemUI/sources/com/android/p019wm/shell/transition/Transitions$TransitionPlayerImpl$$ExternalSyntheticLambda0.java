package com.android.p019wm.shell.transition;

import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import com.android.p019wm.shell.transition.Transitions;

/* renamed from: com.android.wm.shell.transition.Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Transitions.TransitionPlayerImpl f$0;
    public final /* synthetic */ IBinder f$1;
    public final /* synthetic */ TransitionInfo f$2;
    public final /* synthetic */ SurfaceControl.Transaction f$3;
    public final /* synthetic */ SurfaceControl.Transaction f$4;

    public /* synthetic */ Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda0(Transitions.TransitionPlayerImpl transitionPlayerImpl, IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        this.f$0 = transitionPlayerImpl;
        this.f$1 = iBinder;
        this.f$2 = transitionInfo;
        this.f$3 = transaction;
        this.f$4 = transaction2;
    }

    public final void run() {
        this.f$0.mo51311x7172b0d0(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
