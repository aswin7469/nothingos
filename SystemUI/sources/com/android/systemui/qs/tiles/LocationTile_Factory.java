package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LocationTile_Factory implements Factory<LocationTile> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Looper> backgroundLooperProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSHost> hostProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<LocationController> locationControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public LocationTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<LocationController> provider9, Provider<KeyguardStateController> provider10) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.locationControllerProvider = provider9;
        this.keyguardStateControllerProvider = provider10;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LocationTile mo1933get() {
        return newInstance(this.hostProvider.mo1933get(), this.backgroundLooperProvider.mo1933get(), this.mainHandlerProvider.mo1933get(), this.falsingManagerProvider.mo1933get(), this.metricsLoggerProvider.mo1933get(), this.statusBarStateControllerProvider.mo1933get(), this.activityStarterProvider.mo1933get(), this.qsLoggerProvider.mo1933get(), this.locationControllerProvider.mo1933get(), this.keyguardStateControllerProvider.mo1933get());
    }

    public static LocationTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<LocationController> provider9, Provider<KeyguardStateController> provider10) {
        return new LocationTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static LocationTile newInstance(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, LocationController locationController, KeyguardStateController keyguardStateController) {
        return new LocationTile(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, locationController, keyguardStateController);
    }
}
