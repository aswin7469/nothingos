package com.android.p019wm.shell.apppairs;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda7 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AppPair$$ExternalSyntheticLambda7 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ AppPair f$0;

    public /* synthetic */ AppPair$$ExternalSyntheticLambda7(AppPair appPair) {
        this.f$0 = appPair;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.m3404lambda$onTaskVanished$4$comandroidwmshellapppairsAppPair(transaction);
    }
}
