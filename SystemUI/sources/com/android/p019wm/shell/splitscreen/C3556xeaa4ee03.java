package com.android.p019wm.shell.splitscreen;

import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda12 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3556xeaa4ee03 implements Consumer {
    public final /* synthetic */ SplitScreenController.ISplitScreenImpl f$0;
    public final /* synthetic */ ISplitScreenListener f$1;

    public /* synthetic */ C3556xeaa4ee03(SplitScreenController.ISplitScreenImpl iSplitScreenImpl, ISplitScreenListener iSplitScreenListener) {
        this.f$0 = iSplitScreenImpl;
        this.f$1 = iSplitScreenListener;
    }

    public final void accept(Object obj) {
        this.f$0.mo50786xca1e3634(this.f$1, (SplitScreenController) obj);
    }
}
