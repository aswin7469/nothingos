package com.android.p019wm.shell.fullscreen;

import android.graphics.Point;
import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.fullscreen.FullscreenTaskListener;

/* renamed from: com.android.wm.shell.fullscreen.FullscreenTaskListener$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FullscreenTaskListener$$ExternalSyntheticLambda2 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ FullscreenTaskListener.TaskData f$0;
    public final /* synthetic */ Point f$1;

    public /* synthetic */ FullscreenTaskListener$$ExternalSyntheticLambda2(FullscreenTaskListener.TaskData taskData, Point point) {
        this.f$0 = taskData;
        this.f$1 = point;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        transaction.setPosition(this.f$0.surface, (float) this.f$1.x, (float) this.f$1.y);
    }
}
