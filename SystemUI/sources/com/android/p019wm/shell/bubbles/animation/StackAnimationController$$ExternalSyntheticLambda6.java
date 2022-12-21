package com.android.p019wm.shell.bubbles.animation;

import com.android.p019wm.shell.bubbles.animation.PhysicsAnimationLayout;

/* renamed from: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda6 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StackAnimationController$$ExternalSyntheticLambda6 implements PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator {
    public final /* synthetic */ StackAnimationController f$0;
    public final /* synthetic */ float f$1;

    public /* synthetic */ StackAnimationController$$ExternalSyntheticLambda6(StackAnimationController stackAnimationController, float f) {
        this.f$0 = stackAnimationController;
        this.f$1 = f;
    }

    public final void configureAnimationForChildAtIndex(int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
        this.f$0.mo48952xc7b6588b(this.f$1, i, physicsPropertyAnimator);
    }
}
