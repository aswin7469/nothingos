package com.android.systemui.util.sensors;

import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ProximityCheck_Factory implements Factory<ProximityCheck> {
    private final Provider<DelayableExecutor> delayableExecutorProvider;
    private final Provider<ProximitySensor> sensorProvider;

    public ProximityCheck_Factory(Provider<ProximitySensor> provider, Provider<DelayableExecutor> provider2) {
        this.sensorProvider = provider;
        this.delayableExecutorProvider = provider2;
    }

    public ProximityCheck get() {
        return newInstance(this.sensorProvider.get(), this.delayableExecutorProvider.get());
    }

    public static ProximityCheck_Factory create(Provider<ProximitySensor> provider, Provider<DelayableExecutor> provider2) {
        return new ProximityCheck_Factory(provider, provider2);
    }

    public static ProximityCheck newInstance(ProximitySensor proximitySensor, DelayableExecutor delayableExecutor) {
        return new ProximityCheck(proximitySensor, delayableExecutor);
    }
}
