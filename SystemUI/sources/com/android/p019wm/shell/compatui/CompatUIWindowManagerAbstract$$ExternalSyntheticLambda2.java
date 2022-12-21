package com.android.p019wm.shell.compatui;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.compatui.CompatUIWindowManagerAbstract$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CompatUIWindowManagerAbstract$$ExternalSyntheticLambda2 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;

    public /* synthetic */ CompatUIWindowManagerAbstract$$ExternalSyntheticLambda2(SurfaceControl surfaceControl) {
        this.f$0 = surfaceControl;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        transaction.remove(this.f$0);
    }
}
