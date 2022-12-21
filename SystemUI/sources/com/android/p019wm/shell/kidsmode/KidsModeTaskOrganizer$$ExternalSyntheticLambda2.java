package com.android.p019wm.shell.kidsmode;

import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.kidsmode.KidsModeTaskOrganizer$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KidsModeTaskOrganizer$$ExternalSyntheticLambda2 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;
    public final /* synthetic */ Rect f$1;

    public /* synthetic */ KidsModeTaskOrganizer$$ExternalSyntheticLambda2(SurfaceControl surfaceControl, Rect rect) {
        this.f$0 = surfaceControl;
        this.f$1 = rect;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        KidsModeTaskOrganizer.lambda$updateBounds$3(this.f$0, this.f$1, transaction);
    }
}
