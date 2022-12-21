package com.android.p019wm.shell.transition;

import android.os.IBinder;
import com.android.p019wm.shell.transition.Transitions;
import java.util.ArrayList;

/* renamed from: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DefaultTransitionHandler$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ DefaultTransitionHandler f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ IBinder f$2;
    public final /* synthetic */ Transitions.TransitionFinishCallback f$3;

    public /* synthetic */ DefaultTransitionHandler$$ExternalSyntheticLambda3(DefaultTransitionHandler defaultTransitionHandler, ArrayList arrayList, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        this.f$0 = defaultTransitionHandler;
        this.f$1 = arrayList;
        this.f$2 = iBinder;
        this.f$3 = transitionFinishCallback;
    }

    public final void run() {
        this.f$0.mo51231x47d8deba(this.f$1, this.f$2, this.f$3);
    }
}
