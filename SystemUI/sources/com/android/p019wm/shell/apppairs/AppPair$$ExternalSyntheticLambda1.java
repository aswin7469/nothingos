package com.android.p019wm.shell.apppairs;

import android.app.ActivityManager;
import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AppPair$$ExternalSyntheticLambda1 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ AppPair f$0;
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$1;

    public /* synthetic */ AppPair$$ExternalSyntheticLambda1(AppPair appPair, ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.f$0 = appPair;
        this.f$1 = runningTaskInfo;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.m3407lambda$onTaskInfoChanged$3$comandroidwmshellapppairsAppPair(this.f$1, transaction);
    }
}
