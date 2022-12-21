package com.android.p019wm.shell.stagesplit;

import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).setSideStageVisibility(this.f$0);
    }
}
