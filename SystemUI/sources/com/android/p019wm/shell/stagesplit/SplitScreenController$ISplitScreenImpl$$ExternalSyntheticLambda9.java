package com.android.p019wm.shell.stagesplit;

import android.view.RemoteAnimationTarget;
import com.android.p019wm.shell.stagesplit.SplitScreenController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda9 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ RemoteAnimationTarget[][] f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ RemoteAnimationTarget[] f$2;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda9(RemoteAnimationTarget[][] remoteAnimationTargetArr, boolean z, RemoteAnimationTarget[] remoteAnimationTargetArr2) {
        this.f$0 = remoteAnimationTargetArr;
        this.f$1 = z;
        this.f$2 = remoteAnimationTargetArr2;
    }

    public final void accept(Object obj) {
        SplitScreenController.ISplitScreenImpl.lambda$onGoingToRecentsLegacy$11(this.f$0, this.f$1, this.f$2, (SplitScreenController) obj);
    }
}
