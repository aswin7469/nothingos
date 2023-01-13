package com.android.systemui.p012qs.tileimpl;

import android.animation.ValueAnimator;
import android.widget.ImageView;

/* renamed from: com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QSIconViewImpl$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ QSIconViewImpl f$0;
    public final /* synthetic */ ImageView f$1;

    public /* synthetic */ QSIconViewImpl$$ExternalSyntheticLambda0(QSIconViewImpl qSIconViewImpl, ImageView imageView) {
        this.f$0 = qSIconViewImpl;
        this.f$1 = imageView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo36734xff879a00(this.f$1, valueAnimator);
    }
}
