package com.nothing.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BatteryShareControllerImpl_Factory implements Factory<BatteryShareControllerImpl> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<Context> contextProvider;

    public BatteryShareControllerImpl_Factory(Provider<Context> provider, Provider<BatteryController> provider2) {
        this.contextProvider = provider;
        this.batteryControllerProvider = provider2;
    }

    public BatteryShareControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.batteryControllerProvider.get());
    }

    public static BatteryShareControllerImpl_Factory create(Provider<Context> provider, Provider<BatteryController> provider2) {
        return new BatteryShareControllerImpl_Factory(provider, provider2);
    }

    public static BatteryShareControllerImpl newInstance(Context context, BatteryController batteryController) {
        return new BatteryShareControllerImpl(context, batteryController);
    }
}
