package com.android.p019wm.shell.splitscreen;

import android.view.RemoteAnimationTarget;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ RemoteAnimationTarget[][] f$0;
    public final /* synthetic */ RemoteAnimationTarget[] f$1;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7(RemoteAnimationTarget[][] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2) {
        this.f$0 = remoteAnimationTargetArr;
        this.f$1 = remoteAnimationTargetArr2;
    }

    public final void accept(Object obj) {
        SplitScreenController.ISplitScreenImpl.lambda$onStartingSplitLegacy$14(this.f$0, this.f$1, (SplitScreenController) obj);
    }
}
