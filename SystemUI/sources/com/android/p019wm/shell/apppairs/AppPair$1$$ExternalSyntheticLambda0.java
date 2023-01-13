package com.android.p019wm.shell.apppairs;

import android.view.SurfaceControl;
import com.android.p019wm.shell.apppairs.AppPair;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.apppairs.AppPair$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AppPair$1$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ AppPair.C33681 f$0;
    public final /* synthetic */ SurfaceControl f$1;

    public /* synthetic */ AppPair$1$$ExternalSyntheticLambda0(AppPair.C33681 r1, SurfaceControl surfaceControl) {
        this.f$0 = r1;
        this.f$1 = surfaceControl;
    }

    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        this.f$0.m3410lambda$onLeashReady$0$comandroidwmshellapppairsAppPair$1(this.f$1, transaction);
    }
}
