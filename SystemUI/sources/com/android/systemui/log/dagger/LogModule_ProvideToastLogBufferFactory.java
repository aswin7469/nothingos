package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogModule_ProvideToastLogBufferFactory implements Factory<LogBuffer> {
    private final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideToastLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LogBuffer mo1933get() {
        return provideToastLogBuffer(this.factoryProvider.mo1933get());
    }

    public static LogModule_ProvideToastLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideToastLogBufferFactory(provider);
    }

    public static LogBuffer provideToastLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideToastLogBuffer(logBufferFactory));
    }
}
