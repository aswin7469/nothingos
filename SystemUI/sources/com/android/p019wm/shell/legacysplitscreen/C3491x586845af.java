package com.android.p019wm.shell.legacysplitscreen;

import android.window.WindowContainerToken;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;

/* renamed from: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3491x586845af implements Runnable {
    public final /* synthetic */ LegacySplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ WindowContainerToken[] f$1;

    public /* synthetic */ C3491x586845af(LegacySplitScreenController.SplitScreenImpl splitScreenImpl, WindowContainerToken[] windowContainerTokenArr) {
        this.f$0 = splitScreenImpl;
        this.f$1 = windowContainerTokenArr;
    }

    public final void run() {
        this.f$0.mo49740x52055634(this.f$1);
    }
}
