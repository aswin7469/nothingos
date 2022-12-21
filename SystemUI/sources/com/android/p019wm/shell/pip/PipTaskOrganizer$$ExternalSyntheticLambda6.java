package com.android.p019wm.shell.pip;

import com.android.p019wm.shell.pip.PipAnimationController;

/* renamed from: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda6 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ PipAnimationController.PipTransitionAnimator f$0;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda6(PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
        this.f$0 = pipTransitionAnimator;
    }

    public final void run() {
        this.f$0.clearContentOverlay();
    }
}
