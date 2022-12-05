package com.nothingos.keyguard.weather;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardWeatherControllerImpl_Factory implements Factory<KeyguardWeatherControllerImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;

    public KeyguardWeatherControllerImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardWeatherControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.handlerProvider.mo1933get(), this.keyguardUpdateMonitorProvider.mo1933get());
    }

    public static KeyguardWeatherControllerImpl_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        return new KeyguardWeatherControllerImpl_Factory(provider, provider2, provider3);
    }

    public static KeyguardWeatherControllerImpl newInstance(Context context, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new KeyguardWeatherControllerImpl(context, handler, keyguardUpdateMonitor);
    }
}
