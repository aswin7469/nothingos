package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipAnimationControllerFactory */
public final class WMShellModule_ProvidePipAnimationControllerFactory implements Factory<PipAnimationController> {
    private final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;

    public WMShellModule_ProvidePipAnimationControllerFactory(Provider<PipSurfaceTransactionHelper> provider) {
        this.pipSurfaceTransactionHelperProvider = provider;
    }

    public PipAnimationController get() {
        return providePipAnimationController(this.pipSurfaceTransactionHelperProvider.get());
    }

    public static WMShellModule_ProvidePipAnimationControllerFactory create(Provider<PipSurfaceTransactionHelper> provider) {
        return new WMShellModule_ProvidePipAnimationControllerFactory(provider);
    }

    public static PipAnimationController providePipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        return (PipAnimationController) Preconditions.checkNotNullFromProvides(WMShellModule.providePipAnimationController(pipSurfaceTransactionHelper));
    }
}
