package com.android.systemui.statusbar;

import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.systemui.statusbar.NotificationShadeDepthController;

/* renamed from: com.android.systemui.statusbar.NotificationShadeDepthController$DepthAnimation$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2586xc13cfbf7 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ NotificationShadeDepthController.DepthAnimation f$0;

    public /* synthetic */ C2586xc13cfbf7(NotificationShadeDepthController.DepthAnimation depthAnimation) {
        this.f$0 = depthAnimation;
    }

    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        NotificationShadeDepthController.DepthAnimation.m3043_init_$lambda0(this.f$0, dynamicAnimation, z, f, f2);
    }
}
