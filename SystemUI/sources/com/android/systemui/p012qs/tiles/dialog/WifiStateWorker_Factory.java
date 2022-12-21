package com.android.systemui.p012qs.tiles.dialog;

import android.net.wifi.WifiManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.tiles.dialog.WifiStateWorker_Factory */
public final class WifiStateWorker_Factory implements Factory<WifiStateWorker> {
    private final Provider<DelayableExecutor> backgroundExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<WifiManager> wifiManagerProvider;

    public WifiStateWorker_Factory(Provider<BroadcastDispatcher> provider, Provider<DelayableExecutor> provider2, Provider<WifiManager> provider3) {
        this.broadcastDispatcherProvider = provider;
        this.backgroundExecutorProvider = provider2;
        this.wifiManagerProvider = provider3;
    }

    public WifiStateWorker get() {
        return newInstance(this.broadcastDispatcherProvider.get(), this.backgroundExecutorProvider.get(), this.wifiManagerProvider.get());
    }

    public static WifiStateWorker_Factory create(Provider<BroadcastDispatcher> provider, Provider<DelayableExecutor> provider2, Provider<WifiManager> provider3) {
        return new WifiStateWorker_Factory(provider, provider2, provider3);
    }

    public static WifiStateWorker newInstance(BroadcastDispatcher broadcastDispatcher, DelayableExecutor delayableExecutor, WifiManager wifiManager) {
        return new WifiStateWorker(broadcastDispatcher, delayableExecutor, wifiManager);
    }
}
