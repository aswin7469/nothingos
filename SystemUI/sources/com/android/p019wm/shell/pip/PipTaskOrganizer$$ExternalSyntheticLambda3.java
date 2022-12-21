package com.android.p019wm.shell.pip;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* renamed from: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ SurfaceControl f$1;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda3(PipTaskOrganizer pipTaskOrganizer, SurfaceControl surfaceControl) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = surfaceControl;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo50214x5530480d(this.f$1, valueAnimator);
    }
}
