package com.android.p019wm.shell;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.TaskView$$ExternalSyntheticLambda6 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda6 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ TaskView f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda6(TaskView taskView, int i) {
        this.f$0 = taskView;
        this.f$1 = i;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.m3396lambda$updateTaskVisibility$6$comandroidwmshellTaskView(this.f$1, transaction);
    }
}
