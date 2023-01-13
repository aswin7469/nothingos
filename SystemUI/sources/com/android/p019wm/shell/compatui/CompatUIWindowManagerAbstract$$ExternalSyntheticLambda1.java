package com.android.p019wm.shell.compatui;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.compatui.CompatUIWindowManagerAbstract$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CompatUIWindowManagerAbstract$$ExternalSyntheticLambda1 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ CompatUIWindowManagerAbstract f$0;
    public final /* synthetic */ SurfaceControl f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ CompatUIWindowManagerAbstract$$ExternalSyntheticLambda1(CompatUIWindowManagerAbstract compatUIWindowManagerAbstract, SurfaceControl surfaceControl, int i) {
        this.f$0 = compatUIWindowManagerAbstract;
        this.f$1 = surfaceControl;
        this.f$2 = i;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.mo49431xe1721304(this.f$1, this.f$2, transaction);
    }
}
