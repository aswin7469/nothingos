package com.android.systemui.doze;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeTriggers_Factory implements Factory<DozeTriggers> {
    private final Provider<AuthController> authControllerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<AmbientDisplayConfiguration> configProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<DozeHost> dozeHostProvider;
    private final Provider<DozeLog> dozeLogProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<ProximitySensor.ProximityCheck> proxCheckProvider;
    private final Provider<ProximitySensor> proximitySensorProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<AsyncSensorManager> sensorManagerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<WakeLock> wakeLockProvider;

    public DozeTriggers_Factory(Provider<Context> provider, Provider<DozeHost> provider2, Provider<AmbientDisplayConfiguration> provider3, Provider<DozeParameters> provider4, Provider<AsyncSensorManager> provider5, Provider<WakeLock> provider6, Provider<DockManager> provider7, Provider<ProximitySensor> provider8, Provider<ProximitySensor.ProximityCheck> provider9, Provider<DozeLog> provider10, Provider<BroadcastDispatcher> provider11, Provider<SecureSettings> provider12, Provider<AuthController> provider13, Provider<DelayableExecutor> provider14, Provider<UiEventLogger> provider15, Provider<KeyguardStateController> provider16) {
        this.contextProvider = provider;
        this.dozeHostProvider = provider2;
        this.configProvider = provider3;
        this.dozeParametersProvider = provider4;
        this.sensorManagerProvider = provider5;
        this.wakeLockProvider = provider6;
        this.dockManagerProvider = provider7;
        this.proximitySensorProvider = provider8;
        this.proxCheckProvider = provider9;
        this.dozeLogProvider = provider10;
        this.broadcastDispatcherProvider = provider11;
        this.secureSettingsProvider = provider12;
        this.authControllerProvider = provider13;
        this.mainExecutorProvider = provider14;
        this.uiEventLoggerProvider = provider15;
        this.keyguardStateControllerProvider = provider16;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DozeTriggers mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.dozeHostProvider.mo1933get(), this.configProvider.mo1933get(), this.dozeParametersProvider.mo1933get(), this.sensorManagerProvider.mo1933get(), this.wakeLockProvider.mo1933get(), this.dockManagerProvider.mo1933get(), this.proximitySensorProvider.mo1933get(), this.proxCheckProvider.mo1933get(), this.dozeLogProvider.mo1933get(), this.broadcastDispatcherProvider.mo1933get(), this.secureSettingsProvider.mo1933get(), this.authControllerProvider.mo1933get(), this.mainExecutorProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get(), this.keyguardStateControllerProvider.mo1933get());
    }

    public static DozeTriggers_Factory create(Provider<Context> provider, Provider<DozeHost> provider2, Provider<AmbientDisplayConfiguration> provider3, Provider<DozeParameters> provider4, Provider<AsyncSensorManager> provider5, Provider<WakeLock> provider6, Provider<DockManager> provider7, Provider<ProximitySensor> provider8, Provider<ProximitySensor.ProximityCheck> provider9, Provider<DozeLog> provider10, Provider<BroadcastDispatcher> provider11, Provider<SecureSettings> provider12, Provider<AuthController> provider13, Provider<DelayableExecutor> provider14, Provider<UiEventLogger> provider15, Provider<KeyguardStateController> provider16) {
        return new DozeTriggers_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static DozeTriggers newInstance(Context context, DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeParameters dozeParameters, AsyncSensorManager asyncSensorManager, WakeLock wakeLock, DockManager dockManager, ProximitySensor proximitySensor, ProximitySensor.ProximityCheck proximityCheck, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, AuthController authController, DelayableExecutor delayableExecutor, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController) {
        return new DozeTriggers(context, dozeHost, ambientDisplayConfiguration, dozeParameters, asyncSensorManager, wakeLock, dockManager, proximitySensor, proximityCheck, dozeLog, broadcastDispatcher, secureSettings, authController, delayableExecutor, uiEventLogger, keyguardStateController);
    }
}
