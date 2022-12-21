package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SysUIConcurrencyModule_ProvideRepeatableExecutorFactory implements Factory<RepeatableExecutor> {
    private final Provider<DelayableExecutor> execProvider;

    public SysUIConcurrencyModule_ProvideRepeatableExecutorFactory(Provider<DelayableExecutor> provider) {
        this.execProvider = provider;
    }

    public RepeatableExecutor get() {
        return provideRepeatableExecutor(this.execProvider.get());
    }

    public static SysUIConcurrencyModule_ProvideRepeatableExecutorFactory create(Provider<DelayableExecutor> provider) {
        return new SysUIConcurrencyModule_ProvideRepeatableExecutorFactory(provider);
    }

    public static RepeatableExecutor provideRepeatableExecutor(DelayableExecutor delayableExecutor) {
        return (RepeatableExecutor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideRepeatableExecutor(delayableExecutor));
    }
}
