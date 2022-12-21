package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SysUIConcurrencyModule_ProvideMainRepeatableExecutorFactory implements Factory<RepeatableExecutor> {
    private final Provider<DelayableExecutor> execProvider;

    public SysUIConcurrencyModule_ProvideMainRepeatableExecutorFactory(Provider<DelayableExecutor> provider) {
        this.execProvider = provider;
    }

    public RepeatableExecutor get() {
        return provideMainRepeatableExecutor(this.execProvider.get());
    }

    public static SysUIConcurrencyModule_ProvideMainRepeatableExecutorFactory create(Provider<DelayableExecutor> provider) {
        return new SysUIConcurrencyModule_ProvideMainRepeatableExecutorFactory(provider);
    }

    public static RepeatableExecutor provideMainRepeatableExecutor(DelayableExecutor delayableExecutor) {
        return (RepeatableExecutor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideMainRepeatableExecutor(delayableExecutor));
    }
}
