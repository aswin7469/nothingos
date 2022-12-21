package com.nothing.systemui.statusbar.policy;

import android.content.Context;
import android.os.Looper;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class TeslaInfoControllerImpl_Factory implements Factory<TeslaInfoControllerImpl> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Looper> mainLooperProvider;

    public TeslaInfoControllerImpl_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Looper> provider3, Provider<DumpManager> provider4) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.mainLooperProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    public TeslaInfoControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.mainLooperProvider.get(), this.dumpManagerProvider.get());
    }

    public static TeslaInfoControllerImpl_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Looper> provider3, Provider<DumpManager> provider4) {
        return new TeslaInfoControllerImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static TeslaInfoControllerImpl newInstance(Context context, BroadcastDispatcher broadcastDispatcher, Looper looper, DumpManager dumpManager) {
        return new TeslaInfoControllerImpl(context, broadcastDispatcher, looper, dumpManager);
    }
}
