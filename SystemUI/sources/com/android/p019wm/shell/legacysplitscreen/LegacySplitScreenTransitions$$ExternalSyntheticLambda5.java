package com.android.p019wm.shell.legacysplitscreen;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* renamed from: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LegacySplitScreenTransitions$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ LegacySplitScreenTransitions f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;
    public final /* synthetic */ SurfaceControl f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ ValueAnimator f$4;

    public /* synthetic */ LegacySplitScreenTransitions$$ExternalSyntheticLambda5(LegacySplitScreenTransitions legacySplitScreenTransitions, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, ValueAnimator valueAnimator) {
        this.f$0 = legacySplitScreenTransitions;
        this.f$1 = transaction;
        this.f$2 = surfaceControl;
        this.f$3 = f;
        this.f$4 = valueAnimator;
    }

    public final void run() {
        this.f$0.mo49750x59db86a4(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
