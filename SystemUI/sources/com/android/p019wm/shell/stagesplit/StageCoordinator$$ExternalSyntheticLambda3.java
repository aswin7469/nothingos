package com.android.p019wm.shell.stagesplit;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.split.SplitLayout;

/* renamed from: com.android.wm.shell.stagesplit.StageCoordinator$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda3 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ SplitLayout f$1;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda3(StageCoordinator stageCoordinator, SplitLayout splitLayout) {
        this.f$0 = stageCoordinator;
        this.f$1 = splitLayout;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.mo51030x500a14b8(this.f$1, transaction);
    }
}
