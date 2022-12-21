package com.android.p019wm.shell.stagesplit;

import com.android.p019wm.shell.stagesplit.SplitScreenController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ SplitScreenController.ISplitScreenImpl f$0;
    public final /* synthetic */ ISplitScreenListener f$1;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3(SplitScreenController.ISplitScreenImpl iSplitScreenImpl, ISplitScreenListener iSplitScreenListener) {
        this.f$0 = iSplitScreenImpl;
        this.f$1 = iSplitScreenListener;
    }

    public final void accept(Object obj) {
        this.f$0.mo50978xbf677a04(this.f$1, (SplitScreenController) obj);
    }
}
