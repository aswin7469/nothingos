package com.android.systemui.statusbar.phone;

import android.os.UserManager;
import com.android.keyguard.CarrierTextController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class KeyguardStatusBarViewController_Factory implements Factory<KeyguardStatusBarViewController> {
    private final Provider<SystemStatusAnimationScheduler> animationSchedulerProvider;
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<BatteryMeterViewController> batteryMeterViewControllerProvider;
    private final Provider<BiometricUnlockController> biometricUnlockControllerProvider;
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<CarrierTextController> carrierTextControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<StatusBarUserSwitcherFeatureController> featureControllerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<NotificationPanelViewController.NotificationPanelViewStateProvider> notificationPanelViewStateProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
    private final Provider<StatusBarIconController> statusBarIconControllerProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    private final Provider<StatusBarUserInfoTracker> statusBarUserInfoTrackerProvider;
    private final Provider<StatusBarIconController.TintedIconManager.Factory> tintedIconManagerFactoryProvider;
    private final Provider<UserInfoController> userInfoControllerProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<StatusBarUserSwitcherController> userSwitcherControllerProvider;
    private final Provider<KeyguardStatusBarView> viewProvider;

    public KeyguardStatusBarViewController_Factory(Provider<KeyguardStatusBarView> provider, Provider<CarrierTextController> provider2, Provider<ConfigurationController> provider3, Provider<SystemStatusAnimationScheduler> provider4, Provider<BatteryController> provider5, Provider<UserInfoController> provider6, Provider<StatusBarIconController> provider7, Provider<StatusBarIconController.TintedIconManager.Factory> provider8, Provider<BatteryMeterViewController> provider9, Provider<NotificationPanelViewController.NotificationPanelViewStateProvider> provider10, Provider<KeyguardStateController> provider11, Provider<KeyguardBypassController> provider12, Provider<KeyguardUpdateMonitor> provider13, Provider<BiometricUnlockController> provider14, Provider<SysuiStatusBarStateController> provider15, Provider<StatusBarContentInsetsProvider> provider16, Provider<UserManager> provider17, Provider<StatusBarUserSwitcherFeatureController> provider18, Provider<StatusBarUserSwitcherController> provider19, Provider<StatusBarUserInfoTracker> provider20, Provider<SecureSettings> provider21, Provider<Executor> provider22) {
        this.viewProvider = provider;
        this.carrierTextControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.animationSchedulerProvider = provider4;
        this.batteryControllerProvider = provider5;
        this.userInfoControllerProvider = provider6;
        this.statusBarIconControllerProvider = provider7;
        this.tintedIconManagerFactoryProvider = provider8;
        this.batteryMeterViewControllerProvider = provider9;
        this.notificationPanelViewStateProvider = provider10;
        this.keyguardStateControllerProvider = provider11;
        this.bypassControllerProvider = provider12;
        this.keyguardUpdateMonitorProvider = provider13;
        this.biometricUnlockControllerProvider = provider14;
        this.statusBarStateControllerProvider = provider15;
        this.statusBarContentInsetsProvider = provider16;
        this.userManagerProvider = provider17;
        this.featureControllerProvider = provider18;
        this.userSwitcherControllerProvider = provider19;
        this.statusBarUserInfoTrackerProvider = provider20;
        this.secureSettingsProvider = provider21;
        this.mainExecutorProvider = provider22;
    }

    public KeyguardStatusBarViewController get() {
        return newInstance(this.viewProvider.get(), this.carrierTextControllerProvider.get(), this.configurationControllerProvider.get(), this.animationSchedulerProvider.get(), this.batteryControllerProvider.get(), this.userInfoControllerProvider.get(), this.statusBarIconControllerProvider.get(), this.tintedIconManagerFactoryProvider.get(), this.batteryMeterViewControllerProvider.get(), this.notificationPanelViewStateProvider.get(), this.keyguardStateControllerProvider.get(), this.bypassControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.biometricUnlockControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.statusBarContentInsetsProvider.get(), this.userManagerProvider.get(), this.featureControllerProvider.get(), this.userSwitcherControllerProvider.get(), this.statusBarUserInfoTrackerProvider.get(), this.secureSettingsProvider.get(), this.mainExecutorProvider.get());
    }

    public static KeyguardStatusBarViewController_Factory create(Provider<KeyguardStatusBarView> provider, Provider<CarrierTextController> provider2, Provider<ConfigurationController> provider3, Provider<SystemStatusAnimationScheduler> provider4, Provider<BatteryController> provider5, Provider<UserInfoController> provider6, Provider<StatusBarIconController> provider7, Provider<StatusBarIconController.TintedIconManager.Factory> provider8, Provider<BatteryMeterViewController> provider9, Provider<NotificationPanelViewController.NotificationPanelViewStateProvider> provider10, Provider<KeyguardStateController> provider11, Provider<KeyguardBypassController> provider12, Provider<KeyguardUpdateMonitor> provider13, Provider<BiometricUnlockController> provider14, Provider<SysuiStatusBarStateController> provider15, Provider<StatusBarContentInsetsProvider> provider16, Provider<UserManager> provider17, Provider<StatusBarUserSwitcherFeatureController> provider18, Provider<StatusBarUserSwitcherController> provider19, Provider<StatusBarUserInfoTracker> provider20, Provider<SecureSettings> provider21, Provider<Executor> provider22) {
        return new KeyguardStatusBarViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22);
    }

    public static KeyguardStatusBarViewController newInstance(KeyguardStatusBarView keyguardStatusBarView, CarrierTextController carrierTextController, ConfigurationController configurationController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, BatteryController batteryController, UserInfoController userInfoController, StatusBarIconController statusBarIconController, StatusBarIconController.TintedIconManager.Factory factory, BatteryMeterViewController batteryMeterViewController, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider2, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, KeyguardUpdateMonitor keyguardUpdateMonitor, BiometricUnlockController biometricUnlockController, SysuiStatusBarStateController sysuiStatusBarStateController, StatusBarContentInsetsProvider statusBarContentInsetsProvider2, UserManager userManager, StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, StatusBarUserSwitcherController statusBarUserSwitcherController, StatusBarUserInfoTracker statusBarUserInfoTracker, SecureSettings secureSettings, Executor executor) {
        return new KeyguardStatusBarViewController(keyguardStatusBarView, carrierTextController, configurationController, systemStatusAnimationScheduler, batteryController, userInfoController, statusBarIconController, factory, batteryMeterViewController, notificationPanelViewStateProvider2, keyguardStateController, keyguardBypassController, keyguardUpdateMonitor, biometricUnlockController, sysuiStatusBarStateController, statusBarContentInsetsProvider2, userManager, statusBarUserSwitcherFeatureController, statusBarUserSwitcherController, statusBarUserInfoTracker, secureSettings, executor);
    }
}
