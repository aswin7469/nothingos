package com.android.p019wm.shell.transition;

import android.window.RemoteTransition;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.transition.Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ RemoteTransition f$0;

    public /* synthetic */ Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1(RemoteTransition remoteTransition) {
        this.f$0 = remoteTransition;
    }

    public final void accept(Object obj) {
        ((Transitions) obj).mRemoteTransitionHandler.removeFiltered(this.f$0);
    }
}
