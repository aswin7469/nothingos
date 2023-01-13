package com.android.systemui.controls.p010ui;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;

/* renamed from: com.android.systemui.controls.ui.ControlViewHolder$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlViewHolder$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ float f$4;
    public final /* synthetic */ ControlViewHolder f$5;
    public final /* synthetic */ Drawable f$6;

    public /* synthetic */ ControlViewHolder$$ExternalSyntheticLambda0(int i, int i2, int i3, int i4, float f, ControlViewHolder controlViewHolder, Drawable drawable) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = i3;
        this.f$3 = i4;
        this.f$4 = f;
        this.f$5 = controlViewHolder;
        this.f$6 = drawable;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ControlViewHolder.m2691startBackgroundAnimation$lambda10$lambda9(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, valueAnimator);
    }
}
