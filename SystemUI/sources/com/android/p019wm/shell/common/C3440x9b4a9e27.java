package com.android.p019wm.shell.common;

import android.view.InsetsSourceControl;
import android.view.InsetsState;
import com.android.p019wm.shell.common.DisplayInsetsController;

/* renamed from: com.android.wm.shell.common.DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3440x9b4a9e27 implements Runnable {
    public final /* synthetic */ DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ InsetsState f$1;
    public final /* synthetic */ InsetsSourceControl[] f$2;

    public /* synthetic */ C3440x9b4a9e27(DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = insetsState;
        this.f$2 = insetsSourceControlArr;
    }

    public final void run() {
        this.f$0.mo49084xea796379(this.f$1, this.f$2);
    }
}
