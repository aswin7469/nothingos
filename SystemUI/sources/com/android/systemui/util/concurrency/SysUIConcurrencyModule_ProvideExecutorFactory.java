package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideExecutorFactory implements Factory<Executor> {
    private final Provider<Looper> looperProvider;

    public SysUIConcurrencyModule_ProvideExecutorFactory(Provider<Looper> provider) {
        this.looperProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Executor mo1933get() {
        return provideExecutor(this.looperProvider.mo1933get());
    }

    public static SysUIConcurrencyModule_ProvideExecutorFactory create(Provider<Looper> provider) {
        return new SysUIConcurrencyModule_ProvideExecutorFactory(provider);
    }

    public static Executor provideExecutor(Looper looper) {
        return (Executor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideExecutor(looper));
    }
}
