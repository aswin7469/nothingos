package com.android.systemui.p012qs.external;

import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.external.TileServices_Factory */
public final class TileServices_Factory implements Factory<TileServices> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<QSTileHost> hostProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public TileServices_Factory(Provider<QSTileHost> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<UserTracker> provider4, Provider<KeyguardStateController> provider5, Provider<CommandQueue> provider6) {
        this.hostProvider = provider;
        this.handlerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.userTrackerProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.commandQueueProvider = provider6;
    }

    public TileServices get() {
        return newInstance(this.hostProvider.get(), this.handlerProvider, this.broadcastDispatcherProvider.get(), this.userTrackerProvider.get(), this.keyguardStateControllerProvider.get(), this.commandQueueProvider.get());
    }

    public static TileServices_Factory create(Provider<QSTileHost> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<UserTracker> provider4, Provider<KeyguardStateController> provider5, Provider<CommandQueue> provider6) {
        return new TileServices_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static TileServices newInstance(QSTileHost qSTileHost, Provider<Handler> provider, BroadcastDispatcher broadcastDispatcher, UserTracker userTracker, KeyguardStateController keyguardStateController, CommandQueue commandQueue) {
        return new TileServices(qSTileHost, provider, broadcastDispatcher, userTracker, keyguardStateController, commandQueue);
    }
}
