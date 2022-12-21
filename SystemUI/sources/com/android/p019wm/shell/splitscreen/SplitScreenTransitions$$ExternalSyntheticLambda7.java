package com.android.p019wm.shell.splitscreen;

import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.p019wm.shell.transition.Transitions;

/* renamed from: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda7 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplitScreenTransitions$$ExternalSyntheticLambda7 implements Transitions.TransitionFinishCallback {
    public final /* synthetic */ SplitScreenTransitions f$0;

    public /* synthetic */ SplitScreenTransitions$$ExternalSyntheticLambda7(SplitScreenTransitions splitScreenTransitions) {
        this.f$0 = splitScreenTransitions;
    }

    public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        this.f$0.onFinish(windowContainerTransaction, windowContainerTransactionCallback);
    }
}
