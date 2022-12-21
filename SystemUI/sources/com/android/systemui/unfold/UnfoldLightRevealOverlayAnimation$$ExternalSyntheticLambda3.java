package com.android.systemui.unfold;

import android.view.SurfaceControl;
import android.view.WindowlessWindowManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda3 implements WindowlessWindowManager.ResizeCompleteCallback {
    public final /* synthetic */ UnfoldLightRevealOverlayAnimation f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda3(UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation, Runnable runnable) {
        this.f$0 = unfoldLightRevealOverlayAnimation;
        this.f$1 = runnable;
    }

    public final void finished(SurfaceControl.Transaction transaction) {
        UnfoldLightRevealOverlayAnimation.m3272addView$lambda6$lambda5(this.f$0, this.f$1, transaction);
    }
}
