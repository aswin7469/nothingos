package com.android.p019wm.shell.splitscreen;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* renamed from: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda10 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda10 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ SurfaceControl f$1;
    public final /* synthetic */ SurfaceControl.Transaction f$2;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda10(StageCoordinator stageCoordinator, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        this.f$0 = stageCoordinator;
        this.f$1 = surfaceControl;
        this.f$2 = transaction;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo50844x9f9bec6e(this.f$1, this.f$2, valueAnimator);
    }
}
