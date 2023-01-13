package com.android.p019wm.shell.bubbles.animation;

import com.android.p019wm.shell.bubbles.animation.PhysicsAnimationLayout;
import java.util.List;
import java.util.Set;

/* renamed from: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3416x49ea1e05 implements PhysicsAnimationLayout.PhysicsAnimationController.MultiAnimationStarter {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsAnimationController f$0;
    public final /* synthetic */ Set f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ C3416x49ea1e05(PhysicsAnimationLayout.PhysicsAnimationController physicsAnimationController, Set set, List list) {
        this.f$0 = physicsAnimationController;
        this.f$1 = set;
        this.f$2 = list;
    }

    public final void startAll(Runnable[] runnableArr) {
        this.f$0.mo48904x6a525548(this.f$1, this.f$2, runnableArr);
    }
}
