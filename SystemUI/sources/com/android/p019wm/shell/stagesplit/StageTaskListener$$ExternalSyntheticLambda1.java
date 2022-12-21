package com.android.p019wm.shell.stagesplit;

import android.graphics.Point;
import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.stagesplit.StageTaskListener$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageTaskListener$$ExternalSyntheticLambda1 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;
    public final /* synthetic */ Point f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ StageTaskListener$$ExternalSyntheticLambda1(SurfaceControl surfaceControl, Point point, boolean z) {
        this.f$0 = surfaceControl;
        this.f$1 = point;
        this.f$2 = z;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        StageTaskListener.lambda$updateChildTaskSurface$2(this.f$0, this.f$1, this.f$2, transaction);
    }
}
