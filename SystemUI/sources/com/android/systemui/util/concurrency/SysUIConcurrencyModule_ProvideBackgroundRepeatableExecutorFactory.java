package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory implements Factory<RepeatableExecutor> {
    private final Provider<DelayableExecutor> execProvider;

    public SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory(Provider<DelayableExecutor> provider) {
        this.execProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public RepeatableExecutor mo1933get() {
        return provideBackgroundRepeatableExecutor(this.execProvider.mo1933get());
    }

    public static SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory create(Provider<DelayableExecutor> provider) {
        return new SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory(provider);
    }

    public static RepeatableExecutor provideBackgroundRepeatableExecutor(DelayableExecutor delayableExecutor) {
        return (RepeatableExecutor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideBackgroundRepeatableExecutor(delayableExecutor));
    }
}
