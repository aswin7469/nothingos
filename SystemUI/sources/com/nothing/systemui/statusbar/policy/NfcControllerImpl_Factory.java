package com.nothing.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NfcControllerImpl_Factory implements Factory<NfcControllerImpl> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<Context> contextProvider;

    public NfcControllerImpl_Factory(Provider<Context> provider, Provider<BatteryController> provider2) {
        this.contextProvider = provider;
        this.batteryControllerProvider = provider2;
    }

    public NfcControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.batteryControllerProvider.get());
    }

    public static NfcControllerImpl_Factory create(Provider<Context> provider, Provider<BatteryController> provider2) {
        return new NfcControllerImpl_Factory(provider, provider2);
    }

    public static NfcControllerImpl newInstance(Context context, BatteryController batteryController) {
        return new NfcControllerImpl(context, batteryController);
    }
}
