package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipAnimationControllerFactory */
public final class TvPipModule_ProvidePipAnimationControllerFactory implements Factory<PipAnimationController> {
    private final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;

    public TvPipModule_ProvidePipAnimationControllerFactory(Provider<PipSurfaceTransactionHelper> provider) {
        this.pipSurfaceTransactionHelperProvider = provider;
    }

    public PipAnimationController get() {
        return providePipAnimationController(this.pipSurfaceTransactionHelperProvider.get());
    }

    public static TvPipModule_ProvidePipAnimationControllerFactory create(Provider<PipSurfaceTransactionHelper> provider) {
        return new TvPipModule_ProvidePipAnimationControllerFactory(provider);
    }

    public static PipAnimationController providePipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        return (PipAnimationController) Preconditions.checkNotNullFromProvides(TvPipModule.providePipAnimationController(pipSurfaceTransactionHelper));
    }
}
