package com.android.systemui.dump;

import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogBufferFreezer_Factory implements Factory<LogBufferFreezer> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<DelayableExecutor> executorProvider;

    public LogBufferFreezer_Factory(Provider<DumpManager> provider, Provider<DelayableExecutor> provider2) {
        this.dumpManagerProvider = provider;
        this.executorProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LogBufferFreezer mo1933get() {
        return newInstance(this.dumpManagerProvider.mo1933get(), this.executorProvider.mo1933get());
    }

    public static LogBufferFreezer_Factory create(Provider<DumpManager> provider, Provider<DelayableExecutor> provider2) {
        return new LogBufferFreezer_Factory(provider, provider2);
    }

    public static LogBufferFreezer newInstance(DumpManager dumpManager, DelayableExecutor delayableExecutor) {
        return new LogBufferFreezer(dumpManager, delayableExecutor);
    }
}
