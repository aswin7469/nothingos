package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory implements Factory<DelayableExecutor> {
    private final Provider<Looper> looperProvider;

    public SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory(Provider<Looper> provider) {
        this.looperProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DelayableExecutor mo1933get() {
        return provideBackgroundDelayableExecutor(this.looperProvider.mo1933get());
    }

    public static SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory create(Provider<Looper> provider) {
        return new SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory(provider);
    }

    public static DelayableExecutor provideBackgroundDelayableExecutor(Looper looper) {
        return (DelayableExecutor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideBackgroundDelayableExecutor(looper));
    }
}
