package com.nothing.systemui.keyguard;

import android.os.PowerManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardViewMediatorEx_Factory implements Factory<KeyguardViewMediatorEx> {
    private final Provider<KeyguardUpdateMonitorEx> keyguardUpdateMonitorExProvider;
    private final Provider<PowerManager> pmProvider;

    public KeyguardViewMediatorEx_Factory(Provider<PowerManager> provider, Provider<KeyguardUpdateMonitorEx> provider2) {
        this.pmProvider = provider;
        this.keyguardUpdateMonitorExProvider = provider2;
    }

    public KeyguardViewMediatorEx get() {
        return newInstance(this.pmProvider.get(), this.keyguardUpdateMonitorExProvider.get());
    }

    public static KeyguardViewMediatorEx_Factory create(Provider<PowerManager> provider, Provider<KeyguardUpdateMonitorEx> provider2) {
        return new KeyguardViewMediatorEx_Factory(provider, provider2);
    }

    public static KeyguardViewMediatorEx newInstance(PowerManager powerManager, KeyguardUpdateMonitorEx keyguardUpdateMonitorEx) {
        return new KeyguardViewMediatorEx(powerManager, keyguardUpdateMonitorEx);
    }
}
