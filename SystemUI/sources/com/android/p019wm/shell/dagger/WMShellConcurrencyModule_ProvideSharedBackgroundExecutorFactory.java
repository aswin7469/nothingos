package com.android.p019wm.shell.dagger;

import android.os.Handler;
import com.android.p019wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory */
public final class WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory implements Factory<ShellExecutor> {
    private final Provider<Handler> handlerProvider;

    public WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory(Provider<Handler> provider) {
        this.handlerProvider = provider;
    }

    public ShellExecutor get() {
        return provideSharedBackgroundExecutor(this.handlerProvider.get());
    }

    public static WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory create(Provider<Handler> provider) {
        return new WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory(provider);
    }

    public static ShellExecutor provideSharedBackgroundExecutor(Handler handler) {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideSharedBackgroundExecutor(handler));
    }
}
