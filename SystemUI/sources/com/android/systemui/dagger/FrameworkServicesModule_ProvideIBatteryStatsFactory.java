package com.android.systemui.dagger;

import com.android.internal.app.IBatteryStats;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIBatteryStatsFactory implements Factory<IBatteryStats> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public IBatteryStats mo1933get() {
        return provideIBatteryStats();
    }

    public static FrameworkServicesModule_ProvideIBatteryStatsFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IBatteryStats provideIBatteryStats() {
        return (IBatteryStats) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIBatteryStats());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIBatteryStatsFactory INSTANCE = new FrameworkServicesModule_ProvideIBatteryStatsFactory();
    }
}
