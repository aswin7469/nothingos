package com.android.p019wm.shell.apppairs;

import android.app.ActivityManager;
import com.android.p019wm.shell.apppairs.AppPairsController;

/* renamed from: com.android.wm.shell.apppairs.AppPairsController$AppPairsImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AppPairsController$AppPairsImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AppPairsController.AppPairsImpl f$0;
    public final /* synthetic */ boolean[] f$1;
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$2;
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$3;

    public /* synthetic */ AppPairsController$AppPairsImpl$$ExternalSyntheticLambda1(AppPairsController.AppPairsImpl appPairsImpl, boolean[] zArr, ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        this.f$0 = appPairsImpl;
        this.f$1 = zArr;
        this.f$2 = runningTaskInfo;
        this.f$3 = runningTaskInfo2;
    }

    public final void run() {
        this.f$0.mo48204xd594b21d(this.f$1, this.f$2, this.f$3);
    }
}
