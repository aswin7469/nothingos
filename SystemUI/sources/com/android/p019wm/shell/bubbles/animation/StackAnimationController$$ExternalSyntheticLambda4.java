package com.android.p019wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;

/* renamed from: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StackAnimationController$$ExternalSyntheticLambda4 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ StackAnimationController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ Runnable[] f$2;

    public /* synthetic */ StackAnimationController$$ExternalSyntheticLambda4(StackAnimationController stackAnimationController, boolean z, Runnable[] runnableArr) {
        this.f$0 = stackAnimationController;
        this.f$1 = z;
        this.f$2 = runnableArr;
    }

    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.mo48965xe9c3843c(this.f$1, this.f$2, dynamicAnimation, z, f, f2);
    }
}
