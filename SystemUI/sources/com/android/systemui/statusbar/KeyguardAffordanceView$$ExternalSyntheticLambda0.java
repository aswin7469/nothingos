package com.android.systemui.statusbar;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KeyguardAffordanceView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ KeyguardAffordanceView f$0;
    public final /* synthetic */ Drawable f$1;

    public /* synthetic */ KeyguardAffordanceView$$ExternalSyntheticLambda0(KeyguardAffordanceView keyguardAffordanceView, Drawable drawable) {
        this.f$0 = keyguardAffordanceView;
        this.f$1 = drawable;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo38552xe0befb6(this.f$1, valueAnimator);
    }
}
