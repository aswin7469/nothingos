package com.android.p019wm.shell.startingsurface;

import android.view.SurfaceControl;

/* renamed from: com.android.wm.shell.startingsurface.SplashScreenExitAnimation$ShiftUpAnimation$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3596x713783c7 implements Runnable {
    public final /* synthetic */ SurfaceControl f$0;

    public /* synthetic */ C3596x713783c7(SurfaceControl surfaceControl) {
        this.f$0 = surfaceControl;
    }

    public final void run() {
        this.f$0.release();
    }
}
