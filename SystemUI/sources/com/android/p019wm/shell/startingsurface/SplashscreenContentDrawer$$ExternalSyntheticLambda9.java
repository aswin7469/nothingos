package com.android.p019wm.shell.startingsurface;

import android.graphics.Rect;
import android.view.SurfaceControl;
import android.window.SplashScreenView;

/* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda9 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplashscreenContentDrawer$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ SplashscreenContentDrawer f$0;
    public final /* synthetic */ SplashScreenView f$1;
    public final /* synthetic */ SurfaceControl f$2;
    public final /* synthetic */ Rect f$3;
    public final /* synthetic */ Runnable f$4;

    public /* synthetic */ SplashscreenContentDrawer$$ExternalSyntheticLambda9(SplashscreenContentDrawer splashscreenContentDrawer, SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, Runnable runnable) {
        this.f$0 = splashscreenContentDrawer;
        this.f$1 = splashScreenView;
        this.f$2 = surfaceControl;
        this.f$3 = rect;
        this.f$4 = runnable;
    }

    public final void run() {
        this.f$0.mo51106xe85b2b62(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
