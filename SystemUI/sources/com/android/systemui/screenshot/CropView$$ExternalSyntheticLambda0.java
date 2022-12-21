package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import com.android.systemui.screenshot.CropView;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CropView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ CropView f$0;
    public final /* synthetic */ CropView.CropBoundary f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;

    public /* synthetic */ CropView$$ExternalSyntheticLambda0(CropView cropView, CropView.CropBoundary cropBoundary, float f, float f2) {
        this.f$0 = cropView;
        this.f$1 = cropBoundary;
        this.f$2 = f;
        this.f$3 = f2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo37287x648c44ab(this.f$1, this.f$2, this.f$3, valueAnimator);
    }
}
