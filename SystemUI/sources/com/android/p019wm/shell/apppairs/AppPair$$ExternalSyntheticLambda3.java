package com.android.p019wm.shell.apppairs;

import android.view.SurfaceControl;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.split.SplitLayout;

/* renamed from: com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AppPair$$ExternalSyntheticLambda3 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ AppPair f$0;
    public final /* synthetic */ SplitLayout f$1;

    public /* synthetic */ AppPair$$ExternalSyntheticLambda3(AppPair appPair, SplitLayout splitLayout) {
        this.f$0 = appPair;
        this.f$1 = splitLayout;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.mo48167xc90fc31a(this.f$1, transaction);
    }
}
