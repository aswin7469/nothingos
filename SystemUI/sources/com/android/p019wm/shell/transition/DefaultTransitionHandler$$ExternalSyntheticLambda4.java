package com.android.p019wm.shell.transition;

import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.window.TransitionInfo;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DefaultTransitionHandler$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ DefaultTransitionHandler f$0;
    public final /* synthetic */ TransitionInfo.Change f$1;
    public final /* synthetic */ Animation f$2;
    public final /* synthetic */ SurfaceControl.Transaction f$3;

    public /* synthetic */ DefaultTransitionHandler$$ExternalSyntheticLambda4(DefaultTransitionHandler defaultTransitionHandler, TransitionInfo.Change change, Animation animation, SurfaceControl.Transaction transaction) {
        this.f$0 = defaultTransitionHandler;
        this.f$1 = change;
        this.f$2 = animation;
        this.f$3 = transaction;
    }

    public final void accept(Object obj) {
        this.f$0.mo51232xd4c5f5d9(this.f$1, this.f$2, this.f$3, (SurfaceControl.Transaction) obj);
    }
}
