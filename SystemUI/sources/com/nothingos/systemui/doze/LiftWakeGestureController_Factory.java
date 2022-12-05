package com.nothingos.systemui.doze;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.util.sensors.ProximitySensor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class LiftWakeGestureController_Factory implements Factory<LiftWakeGestureController> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<ProximitySensor> proximitySensorProvider;

    public LiftWakeGestureController_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<ProximitySensor> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.proximitySensorProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LiftWakeGestureController mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.handlerProvider.mo1933get(), this.proximitySensorProvider.mo1933get());
    }

    public static LiftWakeGestureController_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<ProximitySensor> provider3) {
        return new LiftWakeGestureController_Factory(provider, provider2, provider3);
    }

    public static LiftWakeGestureController newInstance(Context context, Handler handler, ProximitySensor proximitySensor) {
        return new LiftWakeGestureController(context, handler, proximitySensor);
    }
}
