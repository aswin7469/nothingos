package com.android.p019wm.shell.startingsurface;

import com.android.p019wm.shell.startingsurface.StartingSurfaceDrawer;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StartingSurfaceDrawer$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ StartingSurfaceDrawer.SplashScreenViewSupplier f$0;

    public /* synthetic */ StartingSurfaceDrawer$$ExternalSyntheticLambda2(StartingSurfaceDrawer.SplashScreenViewSupplier splashScreenViewSupplier) {
        this.f$0 = splashScreenViewSupplier;
    }

    public final void accept(Object obj) {
        this.f$0.setUiThreadInitTask((Runnable) obj);
    }
}
