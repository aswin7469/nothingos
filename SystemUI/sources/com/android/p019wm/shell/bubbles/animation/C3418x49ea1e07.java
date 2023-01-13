package com.android.p019wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.p019wm.shell.bubbles.animation.PhysicsAnimationLayout;

/* renamed from: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3418x49ea1e07 implements Runnable {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsAnimationController f$0;
    public final /* synthetic */ DynamicAnimation.ViewProperty[] f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ C3418x49ea1e07(PhysicsAnimationLayout.PhysicsAnimationController physicsAnimationController, DynamicAnimation.ViewProperty[] viewPropertyArr, Runnable runnable) {
        this.f$0 = physicsAnimationController;
        this.f$1 = viewPropertyArr;
        this.f$2 = runnable;
    }

    public final void run() {
        this.f$0.mo48905x612ec9b3(this.f$1, this.f$2);
    }
}
