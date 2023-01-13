package com.android.p019wm.shell.unfold;

import android.window.TransitionInfo;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.unfold.UnfoldTransitionHandler$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UnfoldTransitionHandler$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ UnfoldTransitionHandler f$0;
    public final /* synthetic */ float f$1;

    public /* synthetic */ UnfoldTransitionHandler$$ExternalSyntheticLambda1(UnfoldTransitionHandler unfoldTransitionHandler, float f) {
        this.f$0 = unfoldTransitionHandler;
        this.f$1 = f;
    }

    public final void accept(Object obj) {
        this.f$0.mo51321x2f816bed(this.f$1, (TransitionInfo.Change) obj);
    }
}
