package com.android.p019wm.shell.dagger;

import android.animation.AnimationHandler;
import com.android.p019wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory */
public final class C3453x374a97b implements Factory<AnimationHandler> {
    private final Provider<ShellExecutor> mainExecutorProvider;

    public C3453x374a97b(Provider<ShellExecutor> provider) {
        this.mainExecutorProvider = provider;
    }

    public AnimationHandler get() {
        return provideShellMainExecutorSfVsyncAnimationHandler(this.mainExecutorProvider.get());
    }

    public static C3453x374a97b create(Provider<ShellExecutor> provider) {
        return new C3453x374a97b(provider);
    }

    public static AnimationHandler provideShellMainExecutorSfVsyncAnimationHandler(ShellExecutor shellExecutor) {
        return (AnimationHandler) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideShellMainExecutorSfVsyncAnimationHandler(shellExecutor));
    }
}
