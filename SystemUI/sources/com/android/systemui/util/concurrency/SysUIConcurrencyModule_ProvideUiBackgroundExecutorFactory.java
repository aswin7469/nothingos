package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory implements Factory<Executor> {
    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Executor mo1933get() {
        return provideUiBackgroundExecutor();
    }

    public static SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Executor provideUiBackgroundExecutor() {
        return (Executor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideUiBackgroundExecutor());
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory INSTANCE = new SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory();
    }
}
