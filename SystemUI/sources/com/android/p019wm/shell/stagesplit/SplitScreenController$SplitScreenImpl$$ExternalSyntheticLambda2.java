package com.android.p019wm.shell.stagesplit;

import com.android.p019wm.shell.stagesplit.SplitScreen;
import com.android.p019wm.shell.stagesplit.SplitScreenController;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ SplitScreen.SplitScreenListener f$1;

    public /* synthetic */ SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda2(SplitScreenController.SplitScreenImpl splitScreenImpl, SplitScreen.SplitScreenListener splitScreenListener) {
        this.f$0 = splitScreenImpl;
        this.f$1 = splitScreenListener;
    }

    public final void run() {
        this.f$0.mo50985xd0f17ec1(this.f$1);
    }
}