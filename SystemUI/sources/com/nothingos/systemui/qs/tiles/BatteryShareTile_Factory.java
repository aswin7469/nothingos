package com.nothingos.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.nothingos.systemui.statusbar.policy.BatteryShareController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class BatteryShareTile_Factory implements Factory<BatteryShareTile> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Looper> backgroundLooperProvider;
    private final Provider<BatteryShareController> batteryShareControllerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSHost> hostProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public BatteryShareTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<BatteryShareController> provider9) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.batteryShareControllerProvider = provider9;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public BatteryShareTile mo1933get() {
        return newInstance(this.hostProvider.mo1933get(), this.backgroundLooperProvider.mo1933get(), this.mainHandlerProvider.mo1933get(), this.falsingManagerProvider.mo1933get(), this.metricsLoggerProvider.mo1933get(), this.statusBarStateControllerProvider.mo1933get(), this.activityStarterProvider.mo1933get(), this.qsLoggerProvider.mo1933get(), this.batteryShareControllerProvider.mo1933get());
    }

    public static BatteryShareTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<BatteryShareController> provider9) {
        return new BatteryShareTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static BatteryShareTile newInstance(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BatteryShareController batteryShareController) {
        return new BatteryShareTile(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, batteryShareController);
    }
}
