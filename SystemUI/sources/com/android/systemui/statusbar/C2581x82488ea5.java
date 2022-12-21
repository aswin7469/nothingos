package com.android.systemui.statusbar;

import android.animation.ValueAnimator;

/* renamed from: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2581x82488ea5 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ NotificationShadeDepthController f$0;

    public /* synthetic */ C2581x82488ea5(NotificationShadeDepthController notificationShadeDepthController) {
        this.f$0 = notificationShadeDepthController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        NotificationShadeDepthController$keyguardStateCallback$1.m3040onKeyguardFadingAwayChanged$lambda1$lambda0(this.f$0, valueAnimator);
    }
}
