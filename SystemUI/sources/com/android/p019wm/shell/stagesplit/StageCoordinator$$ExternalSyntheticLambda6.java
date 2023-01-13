package com.android.p019wm.shell.stagesplit;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.stagesplit.StageCoordinator$$ExternalSyntheticLambda6 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda6 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda6(StageCoordinator stageCoordinator, boolean z, boolean z2) {
        this.f$0 = stageCoordinator;
        this.f$1 = z;
        this.f$2 = z2;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.mo51042xba06c2ee(this.f$1, this.f$2, transaction);
    }
}
