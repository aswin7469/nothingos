package com.android.p019wm.shell.fullscreen;

import android.graphics.Point;
import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.fullscreen.FullscreenTaskListener$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FullscreenTaskListener$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ SurfaceControl f$0;
    public final /* synthetic */ Point f$1;

    public /* synthetic */ FullscreenTaskListener$$ExternalSyntheticLambda0(SurfaceControl surfaceControl, Point point) {
        this.f$0 = surfaceControl;
        this.f$1 = point;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        FullscreenTaskListener.lambda$onTaskAppeared$0(this.f$0, this.f$1, transaction);
    }
}
