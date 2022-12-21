package com.android.systemui.p012qs;

import com.android.systemui.p012qs.FgsManagerController;

/* renamed from: com.android.systemui.qs.FgsManagerController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FgsManagerController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ FgsManagerController.OnNumberOfPackagesChangedListener f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ FgsManagerController$$ExternalSyntheticLambda0(FgsManagerController.OnNumberOfPackagesChangedListener onNumberOfPackagesChangedListener, int i) {
        this.f$0 = onNumberOfPackagesChangedListener;
        this.f$1 = i;
    }

    public final void run() {
        FgsManagerController.m2892updateNumberOfVisibleRunningPackagesLocked$lambda13$lambda12(this.f$0, this.f$1);
    }
}
