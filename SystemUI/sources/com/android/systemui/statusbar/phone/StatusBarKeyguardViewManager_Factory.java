package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class StatusBarKeyguardViewManager_Factory implements Factory<StatusBarKeyguardViewManager> {
    private final Provider<ViewMediatorCallback> callbackProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    private final Provider<KeyguardBouncer.Factory> keyguardBouncerFactoryProvider;
    private final Provider<KeyguardMessageAreaController.Factory> keyguardMessageAreaFactoryProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LatencyTracker> latencyTrackerProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<NotificationMediaManager> notificationMediaManagerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<Optional<SysUIUnfoldComponent>> sysUIUnfoldComponentProvider;
    private final Provider<SysuiStatusBarStateController> sysuiStatusBarStateControllerProvider;

    public StatusBarKeyguardViewManager_Factory(Provider<Context> provider, Provider<ViewMediatorCallback> provider2, Provider<LockPatternUtils> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<ConfigurationController> provider5, Provider<KeyguardUpdateMonitor> provider6, Provider<DreamOverlayStateController> provider7, Provider<NavigationModeController> provider8, Provider<DockManager> provider9, Provider<NotificationShadeWindowController> provider10, Provider<KeyguardStateController> provider11, Provider<NotificationMediaManager> provider12, Provider<KeyguardBouncer.Factory> provider13, Provider<KeyguardMessageAreaController.Factory> provider14, Provider<Optional<SysUIUnfoldComponent>> provider15, Provider<ShadeController> provider16, Provider<LatencyTracker> provider17) {
        this.contextProvider = provider;
        this.callbackProvider = provider2;
        this.lockPatternUtilsProvider = provider3;
        this.sysuiStatusBarStateControllerProvider = provider4;
        this.configurationControllerProvider = provider5;
        this.keyguardUpdateMonitorProvider = provider6;
        this.dreamOverlayStateControllerProvider = provider7;
        this.navigationModeControllerProvider = provider8;
        this.dockManagerProvider = provider9;
        this.notificationShadeWindowControllerProvider = provider10;
        this.keyguardStateControllerProvider = provider11;
        this.notificationMediaManagerProvider = provider12;
        this.keyguardBouncerFactoryProvider = provider13;
        this.keyguardMessageAreaFactoryProvider = provider14;
        this.sysUIUnfoldComponentProvider = provider15;
        this.shadeControllerProvider = provider16;
        this.latencyTrackerProvider = provider17;
    }

    public StatusBarKeyguardViewManager get() {
        return newInstance(this.contextProvider.get(), this.callbackProvider.get(), this.lockPatternUtilsProvider.get(), this.sysuiStatusBarStateControllerProvider.get(), this.configurationControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dreamOverlayStateControllerProvider.get(), this.navigationModeControllerProvider.get(), this.dockManagerProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.notificationMediaManagerProvider.get(), this.keyguardBouncerFactoryProvider.get(), this.keyguardMessageAreaFactoryProvider.get(), this.sysUIUnfoldComponentProvider.get(), DoubleCheck.lazy(this.shadeControllerProvider), this.latencyTrackerProvider.get());
    }

    public static StatusBarKeyguardViewManager_Factory create(Provider<Context> provider, Provider<ViewMediatorCallback> provider2, Provider<LockPatternUtils> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<ConfigurationController> provider5, Provider<KeyguardUpdateMonitor> provider6, Provider<DreamOverlayStateController> provider7, Provider<NavigationModeController> provider8, Provider<DockManager> provider9, Provider<NotificationShadeWindowController> provider10, Provider<KeyguardStateController> provider11, Provider<NotificationMediaManager> provider12, Provider<KeyguardBouncer.Factory> provider13, Provider<KeyguardMessageAreaController.Factory> provider14, Provider<Optional<SysUIUnfoldComponent>> provider15, Provider<ShadeController> provider16, Provider<LatencyTracker> provider17) {
        return new StatusBarKeyguardViewManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17);
    }

    public static StatusBarKeyguardViewManager newInstance(Context context, ViewMediatorCallback viewMediatorCallback, LockPatternUtils lockPatternUtils, SysuiStatusBarStateController sysuiStatusBarStateController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, DreamOverlayStateController dreamOverlayStateController, NavigationModeController navigationModeController, DockManager dockManager, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, NotificationMediaManager notificationMediaManager, KeyguardBouncer.Factory factory, KeyguardMessageAreaController.Factory factory2, Optional<SysUIUnfoldComponent> optional, Lazy<ShadeController> lazy, LatencyTracker latencyTracker) {
        return new StatusBarKeyguardViewManager(context, viewMediatorCallback, lockPatternUtils, sysuiStatusBarStateController, configurationController, keyguardUpdateMonitor, dreamOverlayStateController, navigationModeController, dockManager, notificationShadeWindowController, keyguardStateController, notificationMediaManager, factory, factory2, optional, lazy, latencyTracker);
    }
}
