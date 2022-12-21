package com.android.p019wm.shell.pip;

import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda9 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda9 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ Rect f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda9(PipTaskOrganizer pipTaskOrganizer, Rect rect, int i, int i2) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = rect;
        this.f$2 = i;
        this.f$3 = i2;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.m3455lambda$exitPip$1$comandroidwmshellpipPipTaskOrganizer(this.f$1, this.f$2, this.f$3, transaction);
    }
}
