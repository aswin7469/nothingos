package com.android.p019wm.shell.stagesplit;

import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda5(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).removeFromSideStage(this.f$0);
    }
}
