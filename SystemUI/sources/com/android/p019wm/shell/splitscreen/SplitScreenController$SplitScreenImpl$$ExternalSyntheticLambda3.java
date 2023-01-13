package com.android.p019wm.shell.splitscreen;

import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.splitscreen.SplitScreenController;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ SplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ SplitScreen.SplitScreenListener f$1;

    public /* synthetic */ SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda3(SplitScreenController.SplitScreenImpl splitScreenImpl, SplitScreen.SplitScreenListener splitScreenListener) {
        this.f$0 = splitScreenImpl;
        this.f$1 = splitScreenListener;
    }

    public final void run() {
        this.f$0.mo50801xe58688e8(this.f$1);
    }
}
