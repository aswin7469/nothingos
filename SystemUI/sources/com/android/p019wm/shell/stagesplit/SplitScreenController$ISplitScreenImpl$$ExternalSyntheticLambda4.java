package com.android.p019wm.shell.stagesplit;

import android.os.Bundle;
import android.window.RemoteTransition;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ Bundle f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ RemoteTransition f$5;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda4(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteTransition remoteTransition) {
        this.f$0 = i;
        this.f$1 = bundle;
        this.f$2 = i2;
        this.f$3 = bundle2;
        this.f$4 = i3;
        this.f$5 = remoteTransition;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).mStageCoordinator.startTasks(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
