package com.android.p019wm.shell.splitscreen;

import android.os.Bundle;
import android.view.RemoteAnimationAdapter;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda11 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3555xeaa4ee02 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ Bundle f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ float f$5;
    public final /* synthetic */ RemoteAnimationAdapter f$6;

    public /* synthetic */ C3555xeaa4ee02(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) {
        this.f$0 = i;
        this.f$1 = bundle;
        this.f$2 = i2;
        this.f$3 = bundle2;
        this.f$4 = i3;
        this.f$5 = f;
        this.f$6 = remoteAnimationAdapter;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).mStageCoordinator.startTasksWithLegacyTransition(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
