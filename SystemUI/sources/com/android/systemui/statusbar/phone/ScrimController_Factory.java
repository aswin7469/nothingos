package com.android.systemui.statusbar.phone;

import android.app.AlarmManager;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScrimController_Factory implements Factory<ScrimController> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<DelayedWakeLock.Builder> delayedWakeLockBuilderProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LightBarController> lightBarControllerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;

    public ScrimController_Factory(Provider<LightBarController> provider, Provider<DozeParameters> provider2, Provider<AlarmManager> provider3, Provider<KeyguardStateController> provider4, Provider<DelayedWakeLock.Builder> provider5, Provider<Handler> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DockManager> provider8, Provider<ConfigurationController> provider9, Provider<Executor> provider10, Provider<UnlockedScreenOffAnimationController> provider11) {
        this.lightBarControllerProvider = provider;
        this.dozeParametersProvider = provider2;
        this.alarmManagerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.delayedWakeLockBuilderProvider = provider5;
        this.handlerProvider = provider6;
        this.keyguardUpdateMonitorProvider = provider7;
        this.dockManagerProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.mainExecutorProvider = provider10;
        this.unlockedScreenOffAnimationControllerProvider = provider11;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ScrimController mo1933get() {
        return newInstance(this.lightBarControllerProvider.mo1933get(), this.dozeParametersProvider.mo1933get(), this.alarmManagerProvider.mo1933get(), this.keyguardStateControllerProvider.mo1933get(), this.delayedWakeLockBuilderProvider.mo1933get(), this.handlerProvider.mo1933get(), this.keyguardUpdateMonitorProvider.mo1933get(), this.dockManagerProvider.mo1933get(), this.configurationControllerProvider.mo1933get(), this.mainExecutorProvider.mo1933get(), this.unlockedScreenOffAnimationControllerProvider.mo1933get());
    }

    public static ScrimController_Factory create(Provider<LightBarController> provider, Provider<DozeParameters> provider2, Provider<AlarmManager> provider3, Provider<KeyguardStateController> provider4, Provider<DelayedWakeLock.Builder> provider5, Provider<Handler> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DockManager> provider8, Provider<ConfigurationController> provider9, Provider<Executor> provider10, Provider<UnlockedScreenOffAnimationController> provider11) {
        return new ScrimController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static ScrimController newInstance(LightBarController lightBarController, DozeParameters dozeParameters, AlarmManager alarmManager, KeyguardStateController keyguardStateController, DelayedWakeLock.Builder builder, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, ConfigurationController configurationController, Executor executor, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        return new ScrimController(lightBarController, dozeParameters, alarmManager, keyguardStateController, builder, handler, keyguardUpdateMonitor, dockManager, configurationController, executor, unlockedScreenOffAnimationController);
    }
}
