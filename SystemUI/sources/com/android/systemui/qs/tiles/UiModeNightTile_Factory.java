package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.LocationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UiModeNightTile_Factory implements Factory<UiModeNightTile> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Looper> backgroundLooperProvider;
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSHost> hostProvider;
    private final Provider<LocationController> locationControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public UiModeNightTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<ConfigurationController> provider9, Provider<BatteryController> provider10, Provider<LocationController> provider11) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.batteryControllerProvider = provider10;
        this.locationControllerProvider = provider11;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public UiModeNightTile mo1933get() {
        return newInstance(this.hostProvider.mo1933get(), this.backgroundLooperProvider.mo1933get(), this.mainHandlerProvider.mo1933get(), this.falsingManagerProvider.mo1933get(), this.metricsLoggerProvider.mo1933get(), this.statusBarStateControllerProvider.mo1933get(), this.activityStarterProvider.mo1933get(), this.qsLoggerProvider.mo1933get(), this.configurationControllerProvider.mo1933get(), this.batteryControllerProvider.mo1933get(), this.locationControllerProvider.mo1933get());
    }

    public static UiModeNightTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<ConfigurationController> provider9, Provider<BatteryController> provider10, Provider<LocationController> provider11) {
        return new UiModeNightTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static UiModeNightTile newInstance(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ConfigurationController configurationController, BatteryController batteryController, LocationController locationController) {
        return new UiModeNightTile(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, configurationController, batteryController, locationController);
    }
}
