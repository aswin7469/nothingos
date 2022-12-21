package com.android.systemui.p012qs;

import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.p012qs.AutoAddTracker;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.AutoAddTracker_Builder_Factory */
public final class AutoAddTracker_Builder_Factory implements Factory<AutoAddTracker.Builder> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<QSHost> qsHostProvider;
    private final Provider<SecureSettings> secureSettingsProvider;

    public AutoAddTracker_Builder_Factory(Provider<SecureSettings> provider, Provider<BroadcastDispatcher> provider2, Provider<QSHost> provider3, Provider<DumpManager> provider4, Provider<Handler> provider5, Provider<Executor> provider6) {
        this.secureSettingsProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.qsHostProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.handlerProvider = provider5;
        this.executorProvider = provider6;
    }

    public AutoAddTracker.Builder get() {
        return newInstance(this.secureSettingsProvider.get(), this.broadcastDispatcherProvider.get(), this.qsHostProvider.get(), this.dumpManagerProvider.get(), this.handlerProvider.get(), this.executorProvider.get());
    }

    public static AutoAddTracker_Builder_Factory create(Provider<SecureSettings> provider, Provider<BroadcastDispatcher> provider2, Provider<QSHost> provider3, Provider<DumpManager> provider4, Provider<Handler> provider5, Provider<Executor> provider6) {
        return new AutoAddTracker_Builder_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static AutoAddTracker.Builder newInstance(SecureSettings secureSettings, BroadcastDispatcher broadcastDispatcher, QSHost qSHost, DumpManager dumpManager, Handler handler, Executor executor) {
        return new AutoAddTracker.Builder(secureSettings, broadcastDispatcher, qSHost, dumpManager, handler, executor);
    }
}
