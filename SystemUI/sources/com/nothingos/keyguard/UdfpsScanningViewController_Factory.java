package com.nothingos.keyguard;

import android.content.Context;
import com.airbnb.lottie.LottieAnimationView;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class UdfpsScanningViewController_Factory implements Factory<UdfpsScanningViewController> {
    private final Provider<AuthController> authControllerProvider;
    private final Provider<BiometricUnlockController> biometricUnlockControllerProvider;
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<CommandRegistry> commandRegistryProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<LottieAnimationView> scanningViewProvider;
    private final Provider<StatusBar> statusBarProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<Context> sysuiContextProvider;
    private final Provider<UdfpsController> udfpsControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public UdfpsScanningViewController_Factory(Provider<StatusBar> provider, Provider<Context> provider2, Provider<AuthController> provider3, Provider<ConfigurationController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<KeyguardStateController> provider6, Provider<WakefulnessLifecycle> provider7, Provider<CommandRegistry> provider8, Provider<NotificationShadeWindowController> provider9, Provider<KeyguardBypassController> provider10, Provider<BiometricUnlockController> provider11, Provider<UdfpsController> provider12, Provider<StatusBarStateController> provider13, Provider<LottieAnimationView> provider14) {
        this.statusBarProvider = provider;
        this.sysuiContextProvider = provider2;
        this.authControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
        this.keyguardUpdateMonitorProvider = provider5;
        this.keyguardStateControllerProvider = provider6;
        this.wakefulnessLifecycleProvider = provider7;
        this.commandRegistryProvider = provider8;
        this.notificationShadeWindowControllerProvider = provider9;
        this.bypassControllerProvider = provider10;
        this.biometricUnlockControllerProvider = provider11;
        this.udfpsControllerProvider = provider12;
        this.statusBarStateControllerProvider = provider13;
        this.scanningViewProvider = provider14;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public UdfpsScanningViewController mo1933get() {
        return newInstance(this.statusBarProvider.mo1933get(), this.sysuiContextProvider.mo1933get(), this.authControllerProvider.mo1933get(), this.configurationControllerProvider.mo1933get(), this.keyguardUpdateMonitorProvider.mo1933get(), this.keyguardStateControllerProvider.mo1933get(), this.wakefulnessLifecycleProvider.mo1933get(), this.commandRegistryProvider.mo1933get(), this.notificationShadeWindowControllerProvider.mo1933get(), this.bypassControllerProvider.mo1933get(), this.biometricUnlockControllerProvider.mo1933get(), this.udfpsControllerProvider, this.statusBarStateControllerProvider.mo1933get(), this.scanningViewProvider.mo1933get());
    }

    public static UdfpsScanningViewController_Factory create(Provider<StatusBar> provider, Provider<Context> provider2, Provider<AuthController> provider3, Provider<ConfigurationController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<KeyguardStateController> provider6, Provider<WakefulnessLifecycle> provider7, Provider<CommandRegistry> provider8, Provider<NotificationShadeWindowController> provider9, Provider<KeyguardBypassController> provider10, Provider<BiometricUnlockController> provider11, Provider<UdfpsController> provider12, Provider<StatusBarStateController> provider13, Provider<LottieAnimationView> provider14) {
        return new UdfpsScanningViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static UdfpsScanningViewController newInstance(StatusBar statusBar, Context context, AuthController authController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, WakefulnessLifecycle wakefulnessLifecycle, CommandRegistry commandRegistry, NotificationShadeWindowController notificationShadeWindowController, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController, Provider<UdfpsController> provider, StatusBarStateController statusBarStateController, LottieAnimationView lottieAnimationView) {
        return new UdfpsScanningViewController(statusBar, context, authController, configurationController, keyguardUpdateMonitor, keyguardStateController, wakefulnessLifecycle, commandRegistry, notificationShadeWindowController, keyguardBypassController, biometricUnlockController, provider, statusBarStateController, lottieAnimationView);
    }
}
