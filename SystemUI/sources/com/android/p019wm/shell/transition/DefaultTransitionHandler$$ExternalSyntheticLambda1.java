package com.android.p019wm.shell.transition;

import android.view.SurfaceControl;

/* renamed from: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DefaultTransitionHandler$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DefaultTransitionHandler f$0;
    public final /* synthetic */ WindowThumbnail f$1;
    public final /* synthetic */ SurfaceControl.Transaction f$2;
    public final /* synthetic */ Runnable f$3;

    public /* synthetic */ DefaultTransitionHandler$$ExternalSyntheticLambda1(DefaultTransitionHandler defaultTransitionHandler, WindowThumbnail windowThumbnail, SurfaceControl.Transaction transaction, Runnable runnable) {
        this.f$0 = defaultTransitionHandler;
        this.f$1 = windowThumbnail;
        this.f$2 = transaction;
        this.f$3 = runnable;
    }

    public final void run() {
        this.f$0.mo51230x59e8ca18(this.f$1, this.f$2, this.f$3);
    }
}
