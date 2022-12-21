package com.android.p019wm.shell.transition;

import android.animation.ValueAnimator;
import java.util.ArrayList;

/* renamed from: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda9 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DefaultTransitionHandler$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ ArrayList f$0;
    public final /* synthetic */ ValueAnimator f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ DefaultTransitionHandler$$ExternalSyntheticLambda9(ArrayList arrayList, ValueAnimator valueAnimator, Runnable runnable) {
        this.f$0 = arrayList;
        this.f$1 = valueAnimator;
        this.f$2 = runnable;
    }

    public final void run() {
        DefaultTransitionHandler.lambda$startSurfaceAnimation$5(this.f$0, this.f$1, this.f$2);
    }
}
