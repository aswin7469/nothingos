package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;

/* renamed from: com.android.wm.shell.animation.PhysicsAnimator$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PhysicsAnimator$$ExternalSyntheticLambda0 implements DynamicAnimation.OnAnimationUpdateListener {
    public final /* synthetic */ PhysicsAnimator f$0;
    public final /* synthetic */ FloatPropertyCompat f$1;

    public /* synthetic */ PhysicsAnimator$$ExternalSyntheticLambda0(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat) {
        this.f$0 = physicsAnimator;
        this.f$1 = floatPropertyCompat;
    }

    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
        PhysicsAnimator.m3394configureDynamicAnimation$lambda4(this.f$0, this.f$1, dynamicAnimation, f, f2);
    }
}
