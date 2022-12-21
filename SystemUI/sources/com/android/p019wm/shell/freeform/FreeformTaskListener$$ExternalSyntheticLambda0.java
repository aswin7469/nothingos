package com.android.p019wm.shell.freeform;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.freeform.FreeformTaskListener$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FreeformTaskListener$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$0;
    public final /* synthetic */ SurfaceControl f$1;
    public final /* synthetic */ Rect f$2;

    public /* synthetic */ FreeformTaskListener$$ExternalSyntheticLambda0(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, Rect rect) {
        this.f$0 = runningTaskInfo;
        this.f$1 = surfaceControl;
        this.f$2 = rect;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        FreeformTaskListener.lambda$onTaskInfoChanged$1(this.f$0, this.f$1, this.f$2, transaction);
    }
}
