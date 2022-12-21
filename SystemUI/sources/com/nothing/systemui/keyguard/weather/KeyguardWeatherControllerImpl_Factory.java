package com.nothing.systemui.keyguard.weather;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardWeatherControllerImpl_Factory implements Factory<KeyguardWeatherControllerImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;

    public KeyguardWeatherControllerImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    public KeyguardWeatherControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.keyguardUpdateMonitorProvider.get());
    }

    public static KeyguardWeatherControllerImpl_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        return new KeyguardWeatherControllerImpl_Factory(provider, provider2, provider3);
    }

    public static KeyguardWeatherControllerImpl newInstance(Context context, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new KeyguardWeatherControllerImpl(context, handler, keyguardUpdateMonitor);
    }
}
