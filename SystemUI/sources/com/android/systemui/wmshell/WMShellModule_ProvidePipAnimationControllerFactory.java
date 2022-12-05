package com.android.systemui.wmshell;

import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvidePipAnimationControllerFactory implements Factory<PipAnimationController> {
    private final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;

    public WMShellModule_ProvidePipAnimationControllerFactory(Provider<PipSurfaceTransactionHelper> provider) {
        this.pipSurfaceTransactionHelperProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PipAnimationController mo1933get() {
        return providePipAnimationController(this.pipSurfaceTransactionHelperProvider.mo1933get());
    }

    public static WMShellModule_ProvidePipAnimationControllerFactory create(Provider<PipSurfaceTransactionHelper> provider) {
        return new WMShellModule_ProvidePipAnimationControllerFactory(provider);
    }

    public static PipAnimationController providePipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        return (PipAnimationController) Preconditions.checkNotNullFromProvides(WMShellModule.providePipAnimationController(pipSurfaceTransactionHelper));
    }
}
