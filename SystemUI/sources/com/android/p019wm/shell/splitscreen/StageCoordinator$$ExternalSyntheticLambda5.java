package com.android.p019wm.shell.splitscreen;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.split.SplitLayout;

/* renamed from: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda5 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ SplitLayout f$1;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda5(StageCoordinator stageCoordinator, SplitLayout splitLayout) {
        this.f$0 = stageCoordinator;
        this.f$1 = splitLayout;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.mo50838x9c0aa3a1(this.f$1, transaction);
    }
}