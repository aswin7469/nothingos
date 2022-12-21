package com.android.systemui.unfold;

import android.view.SurfaceControl;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda4 implements SurfaceControl.TransactionCommittedListener {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda4(Runnable runnable) {
        this.f$0 = runnable;
    }

    public final void onTransactionCommitted() {
        UnfoldLightRevealOverlayAnimation.m3273addView$lambda6$lambda5$lambda4(this.f$0);
    }
}
