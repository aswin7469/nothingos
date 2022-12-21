package com.android.keyguard;

import com.android.keyguard.CarrierTextManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class CarrierTextController_Factory implements Factory<CarrierTextController> {
    private final Provider<CarrierTextManager.Builder> carrierTextManagerBuilderProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<CarrierText> viewProvider;

    public CarrierTextController_Factory(Provider<CarrierText> provider, Provider<CarrierTextManager.Builder> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        this.viewProvider = provider;
        this.carrierTextManagerBuilderProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    public CarrierTextController get() {
        return newInstance(this.viewProvider.get(), this.carrierTextManagerBuilderProvider.get(), this.keyguardUpdateMonitorProvider.get());
    }

    public static CarrierTextController_Factory create(Provider<CarrierText> provider, Provider<CarrierTextManager.Builder> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        return new CarrierTextController_Factory(provider, provider2, provider3);
    }

    public static CarrierTextController newInstance(CarrierText carrierText, CarrierTextManager.Builder builder, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new CarrierTextController(carrierText, builder, keyguardUpdateMonitor);
    }
}
