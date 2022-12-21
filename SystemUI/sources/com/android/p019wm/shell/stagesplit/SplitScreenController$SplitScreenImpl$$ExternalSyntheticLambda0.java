package com.android.p019wm.shell.stagesplit;

import com.android.p019wm.shell.stagesplit.SplitScreen;
import com.android.p019wm.shell.stagesplit.SplitScreenController;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ SplitScreen.SplitScreenListener f$1;

    public /* synthetic */ SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0(SplitScreenController.SplitScreenImpl splitScreenImpl, SplitScreen.SplitScreenListener splitScreenListener) {
        this.f$0 = splitScreenImpl;
        this.f$1 = splitScreenListener;
    }

    public final void run() {
        this.f$0.mo50986x24638009(this.f$1);
    }
}
