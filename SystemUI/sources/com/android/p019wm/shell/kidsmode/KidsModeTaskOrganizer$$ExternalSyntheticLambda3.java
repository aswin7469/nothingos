package com.android.p019wm.shell.kidsmode;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.kidsmode.KidsModeTaskOrganizer$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KidsModeTaskOrganizer$$ExternalSyntheticLambda3 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;

    public /* synthetic */ KidsModeTaskOrganizer$$ExternalSyntheticLambda3(SurfaceControl surfaceControl) {
        this.f$0 = surfaceControl;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        KidsModeTaskOrganizer.lambda$onTaskAppeared$1(this.f$0, transaction);
    }
}
