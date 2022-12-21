package com.android.p019wm.shell.splitscreen;

import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.util.concurrent.Executor;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ SplitScreen.SplitScreenListener f$1;
    public final /* synthetic */ Executor f$2;

    public /* synthetic */ SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda1(SplitScreenController.SplitScreenImpl splitScreenImpl, SplitScreen.SplitScreenListener splitScreenListener, Executor executor) {
        this.f$0 = splitScreenImpl;
        this.f$1 = splitScreenListener;
        this.f$2 = executor;
    }

    public final void run() {
        this.f$0.mo50790x486dac51(this.f$1, this.f$2);
    }
}
