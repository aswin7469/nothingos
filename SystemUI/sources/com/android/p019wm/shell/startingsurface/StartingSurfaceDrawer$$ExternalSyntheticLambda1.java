package com.android.p019wm.shell.startingsurface;

import android.window.SplashScreenView;
import com.android.p019wm.shell.startingsurface.StartingSurfaceDrawer;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StartingSurfaceDrawer$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ StartingSurfaceDrawer.SplashScreenViewSupplier f$0;

    public /* synthetic */ StartingSurfaceDrawer$$ExternalSyntheticLambda1(StartingSurfaceDrawer.SplashScreenViewSupplier splashScreenViewSupplier) {
        this.f$0 = splashScreenViewSupplier;
    }

    public final void accept(Object obj) {
        this.f$0.setView((SplashScreenView) obj);
    }
}
