package com.android.p019wm.shell.bubbles.animation;

import android.animation.ValueAnimator;

/* renamed from: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3420x4b8fea76 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ C3420x4b8fea76(Runnable runnable) {
        this.f$0 = runnable;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.run();
    }
}
