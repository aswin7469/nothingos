package com.android.p019wm.shell.transition;

import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.window.TransitionInfo;
import java.util.ArrayList;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DefaultTransitionHandler$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ DefaultTransitionHandler f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ Animation f$2;
    public final /* synthetic */ TransitionInfo.Change f$3;
    public final /* synthetic */ Runnable f$4;
    public final /* synthetic */ float f$5;
    public final /* synthetic */ Rect f$6;

    public /* synthetic */ DefaultTransitionHandler$$ExternalSyntheticLambda5(DefaultTransitionHandler defaultTransitionHandler, ArrayList arrayList, Animation animation, TransitionInfo.Change change, Runnable runnable, float f, Rect rect) {
        this.f$0 = defaultTransitionHandler;
        this.f$1 = arrayList;
        this.f$2 = animation;
        this.f$3 = change;
        this.f$4 = runnable;
        this.f$5 = f;
        this.f$6 = rect;
    }

    public final void accept(Object obj) {
        this.f$0.mo51233x61b30cf8(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, (SurfaceControl.Transaction) obj);
    }
}
