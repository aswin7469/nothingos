package com.android.p019wm.shell.transition;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* renamed from: com.android.wm.shell.transition.ScreenRotationAnimation$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScreenRotationAnimation$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ScreenRotationAnimation f$0;
    public final /* synthetic */ ValueAnimator f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ float[] f$4;
    public final /* synthetic */ SurfaceControl.Transaction f$5;

    public /* synthetic */ ScreenRotationAnimation$$ExternalSyntheticLambda0(ScreenRotationAnimation screenRotationAnimation, ValueAnimator valueAnimator, int i, int i2, float[] fArr, SurfaceControl.Transaction transaction) {
        this.f$0 = screenRotationAnimation;
        this.f$1 = valueAnimator;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = fArr;
        this.f$5 = transaction;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo51270x9a10279a(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, valueAnimator);
    }
}
