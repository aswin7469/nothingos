package com.nothing.systemui.doze;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.util.sensors.ProximitySensor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LiftWakeGestureController_Factory implements Factory<LiftWakeGestureController> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<ProximitySensor> proximitySensorProvider;

    public LiftWakeGestureController_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<ProximitySensor> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.proximitySensorProvider = provider3;
    }

    public LiftWakeGestureController get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.proximitySensorProvider.get());
    }

    public static LiftWakeGestureController_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<ProximitySensor> provider3) {
        return new LiftWakeGestureController_Factory(provider, provider2, provider3);
    }

    public static LiftWakeGestureController newInstance(Context context, Handler handler, ProximitySensor proximitySensor) {
        return new LiftWakeGestureController(context, handler, proximitySensor);
    }
}
