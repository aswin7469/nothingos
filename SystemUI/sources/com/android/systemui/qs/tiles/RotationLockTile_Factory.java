package com.android.systemui.qs.tiles;

import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RotationLockTile_Factory implements Factory<RotationLockTile> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Looper> backgroundLooperProvider;
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSHost> hostProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<SensorPrivacyManager> privacyManagerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<RotationLockController> rotationLockControllerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public RotationLockTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<RotationLockController> provider9, Provider<SensorPrivacyManager> provider10, Provider<BatteryController> provider11, Provider<SecureSettings> provider12) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.rotationLockControllerProvider = provider9;
        this.privacyManagerProvider = provider10;
        this.batteryControllerProvider = provider11;
        this.secureSettingsProvider = provider12;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public RotationLockTile mo1933get() {
        return newInstance(this.hostProvider.mo1933get(), this.backgroundLooperProvider.mo1933get(), this.mainHandlerProvider.mo1933get(), this.falsingManagerProvider.mo1933get(), this.metricsLoggerProvider.mo1933get(), this.statusBarStateControllerProvider.mo1933get(), this.activityStarterProvider.mo1933get(), this.qsLoggerProvider.mo1933get(), this.rotationLockControllerProvider.mo1933get(), this.privacyManagerProvider.mo1933get(), this.batteryControllerProvider.mo1933get(), this.secureSettingsProvider.mo1933get());
    }

    public static RotationLockTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<RotationLockController> provider9, Provider<SensorPrivacyManager> provider10, Provider<BatteryController> provider11, Provider<SecureSettings> provider12) {
        return new RotationLockTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static RotationLockTile newInstance(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, RotationLockController rotationLockController, SensorPrivacyManager sensorPrivacyManager, BatteryController batteryController, SecureSettings secureSettings) {
        return new RotationLockTile(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, rotationLockController, sensorPrivacyManager, batteryController, secureSettings);
    }
}
