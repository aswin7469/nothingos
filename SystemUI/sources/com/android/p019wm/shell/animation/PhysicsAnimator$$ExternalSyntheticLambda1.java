package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;

/* renamed from: com.android.wm.shell.animation.PhysicsAnimator$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PhysicsAnimator$$ExternalSyntheticLambda1 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ PhysicsAnimator f$0;
    public final /* synthetic */ FloatPropertyCompat f$1;
    public final /* synthetic */ DynamicAnimation f$2;

    public /* synthetic */ PhysicsAnimator$$ExternalSyntheticLambda1(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, DynamicAnimation dynamicAnimation) {
        this.f$0 = physicsAnimator;
        this.f$1 = floatPropertyCompat;
        this.f$2 = dynamicAnimation;
    }

    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        PhysicsAnimator.m3395configureDynamicAnimation$lambda5(this.f$0, this.f$1, this.f$2, dynamicAnimation, z, f, f2);
    }
}
