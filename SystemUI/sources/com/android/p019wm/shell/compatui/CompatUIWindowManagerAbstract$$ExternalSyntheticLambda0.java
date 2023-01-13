package com.android.p019wm.shell.compatui;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.compatui.CompatUIWindowManagerAbstract$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CompatUIWindowManagerAbstract$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ CompatUIWindowManagerAbstract f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ CompatUIWindowManagerAbstract$$ExternalSyntheticLambda0(CompatUIWindowManagerAbstract compatUIWindowManagerAbstract, int i, int i2) {
        this.f$0 = compatUIWindowManagerAbstract;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.mo49432xb3cca3b6(this.f$1, this.f$2, transaction);
    }
}
