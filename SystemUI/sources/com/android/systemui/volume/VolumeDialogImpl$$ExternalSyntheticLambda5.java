package com.android.systemui.volume;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class VolumeDialogImpl$$ExternalSyntheticLambda5 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ VolumeDialogImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ VolumeDialogImpl$$ExternalSyntheticLambda5(VolumeDialogImpl volumeDialogImpl, int i, int i2) {
        this.f$0 = volumeDialogImpl;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo47352xd07f896(this.f$1, this.f$2, valueAnimator);
    }
}
