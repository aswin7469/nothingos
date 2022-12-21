package com.android.p019wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.SpringAnimation;
import com.android.p019wm.shell.bubbles.animation.PhysicsAnimationLayout;

/* renamed from: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3413x4b8fea79 implements Runnable {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsPropertyAnimator f$0;
    public final /* synthetic */ SpringAnimation f$1;
    public final /* synthetic */ SpringAnimation f$2;

    public /* synthetic */ C3413x4b8fea79(PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator, SpringAnimation springAnimation, SpringAnimation springAnimation2) {
        this.f$0 = physicsPropertyAnimator;
        this.f$1 = springAnimation;
        this.f$2 = springAnimation2;
    }

    public final void run() {
        this.f$0.mo48907x30d28d4d(this.f$1, this.f$2);
    }
}
