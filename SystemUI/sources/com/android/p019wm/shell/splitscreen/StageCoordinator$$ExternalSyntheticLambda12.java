package com.android.p019wm.shell.splitscreen;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda12 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda12 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda12(StageCoordinator stageCoordinator, boolean z, boolean z2) {
        this.f$0 = stageCoordinator;
        this.f$1 = z;
        this.f$2 = z2;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.mo50841x3d500690(this.f$1, this.f$2, transaction);
    }
}
