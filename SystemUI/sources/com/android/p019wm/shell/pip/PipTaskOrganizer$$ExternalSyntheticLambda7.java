package com.android.p019wm.shell.pip;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda7 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda7 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda7(Runnable runnable) {
        this.f$0 = runnable;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.run();
    }
}
