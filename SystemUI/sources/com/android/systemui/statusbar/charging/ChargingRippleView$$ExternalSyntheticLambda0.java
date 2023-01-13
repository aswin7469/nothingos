package com.android.systemui.statusbar.charging;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ChargingRippleView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChargingRippleView f$0;

    public /* synthetic */ ChargingRippleView$$ExternalSyntheticLambda0(ChargingRippleView chargingRippleView) {
        this.f$0 = chargingRippleView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChargingRippleView.m3052startRipple$lambda0(this.f$0, valueAnimator);
    }
}
