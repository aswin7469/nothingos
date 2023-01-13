package com.android.p019wm.shell.common;

import android.view.InsetsState;
import com.android.p019wm.shell.common.DisplayInsetsController;

/* renamed from: com.android.wm.shell.common.DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3438x9b4a9e25 implements Runnable {
    public final /* synthetic */ DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ InsetsState f$1;

    public /* synthetic */ C3438x9b4a9e25(DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, InsetsState insetsState) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = insetsState;
    }

    public final void run() {
        this.f$0.mo49083x57bb9b23(this.f$1);
    }
}
