package com.android.p019wm.shell.apppairs;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.split.SplitLayout;

/* renamed from: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AppPair$$ExternalSyntheticLambda2 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ AppPair f$0;
    public final /* synthetic */ SplitLayout f$1;

    public /* synthetic */ AppPair$$ExternalSyntheticLambda2(AppPair appPair, SplitLayout splitLayout) {
        this.f$0 = appPair;
        this.f$1 = splitLayout;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.mo48168x50fd130d(this.f$1, transaction);
    }
}
