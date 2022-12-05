package com.nothingos.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class BatteryShareControllerImpl_Factory implements Factory<BatteryShareControllerImpl> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<Context> contextProvider;

    public BatteryShareControllerImpl_Factory(Provider<Context> provider, Provider<BatteryController> provider2) {
        this.contextProvider = provider;
        this.batteryControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public BatteryShareControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.batteryControllerProvider.mo1933get());
    }

    public static BatteryShareControllerImpl_Factory create(Provider<Context> provider, Provider<BatteryController> provider2) {
        return new BatteryShareControllerImpl_Factory(provider, provider2);
    }

    public static BatteryShareControllerImpl newInstance(Context context, BatteryController batteryController) {
        return new BatteryShareControllerImpl(context, batteryController);
    }
}
