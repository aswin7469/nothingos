package com.android.p019wm.shell.stagesplit;

import android.os.Bundle;
import android.view.RemoteAnimationAdapter;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ Bundle f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ RemoteAnimationAdapter f$5;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteAnimationAdapter remoteAnimationAdapter) {
        this.f$0 = i;
        this.f$1 = bundle;
        this.f$2 = i2;
        this.f$3 = bundle2;
        this.f$4 = i3;
        this.f$5 = remoteAnimationAdapter;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).mStageCoordinator.startTasksWithLegacyTransition(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
