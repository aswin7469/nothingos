package com.android.p019wm.shell.startingsurface;

import android.os.IBinder;
import android.widget.FrameLayout;
import com.android.p019wm.shell.startingsurface.StartingSurfaceDrawer;

/* renamed from: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StartingSurfaceDrawer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StartingSurfaceDrawer f$0;
    public final /* synthetic */ StartingSurfaceDrawer.SplashScreenViewSupplier f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ IBinder f$3;
    public final /* synthetic */ FrameLayout f$4;

    public /* synthetic */ StartingSurfaceDrawer$$ExternalSyntheticLambda0(StartingSurfaceDrawer startingSurfaceDrawer, StartingSurfaceDrawer.SplashScreenViewSupplier splashScreenViewSupplier, int i, IBinder iBinder, FrameLayout frameLayout) {
        this.f$0 = startingSurfaceDrawer;
        this.f$1 = splashScreenViewSupplier;
        this.f$2 = i;
        this.f$3 = iBinder;
        this.f$4 = frameLayout;
    }

    public final void run() {
        this.f$0.mo51162xac683a44(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}