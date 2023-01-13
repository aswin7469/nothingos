package com.android.p019wm.shell.common;

import android.view.InsetsVisibilities;
import com.android.p019wm.shell.common.DisplayInsetsController;

/* renamed from: com.android.wm.shell.common.DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3439x9b4a9e26 implements Runnable {
    public final /* synthetic */ DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ InsetsVisibilities f$2;

    public /* synthetic */ C3439x9b4a9e26(DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, String str, InsetsVisibilities insetsVisibilities) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = str;
        this.f$2 = insetsVisibilities;
    }

    public final void run() {
        this.f$0.mo49086x2b1167c0(this.f$1, this.f$2);
    }
}
