package com.android.keyguard;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardSecurityContainerController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardSecurityContainerController_Factory_Factory implements Factory<KeyguardSecurityContainerController.Factory> {
    private final Provider<AdminSecondaryLockScreenController.Factory> adminSecondaryLockScreenControllerFactoryProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<KeyguardSecurityViewFlipperController> securityViewFlipperControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<KeyguardSecurityContainer> viewProvider;

    public KeyguardSecurityContainerController_Factory_Factory(Provider<KeyguardSecurityContainer> provider, Provider<AdminSecondaryLockScreenController.Factory> provider2, Provider<LockPatternUtils> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<KeyguardSecurityModel> provider5, Provider<MetricsLogger> provider6, Provider<UiEventLogger> provider7, Provider<KeyguardStateController> provider8, Provider<KeyguardSecurityViewFlipperController> provider9, Provider<ConfigurationController> provider10) {
        this.viewProvider = provider;
        this.adminSecondaryLockScreenControllerFactoryProvider = provider2;
        this.lockPatternUtilsProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
        this.keyguardSecurityModelProvider = provider5;
        this.metricsLoggerProvider = provider6;
        this.uiEventLoggerProvider = provider7;
        this.keyguardStateControllerProvider = provider8;
        this.securityViewFlipperControllerProvider = provider9;
        this.configurationControllerProvider = provider10;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardSecurityContainerController.Factory mo1933get() {
        return newInstance(this.viewProvider.mo1933get(), this.adminSecondaryLockScreenControllerFactoryProvider.mo1933get(), this.lockPatternUtilsProvider.mo1933get(), this.keyguardUpdateMonitorProvider.mo1933get(), this.keyguardSecurityModelProvider.mo1933get(), this.metricsLoggerProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get(), this.keyguardStateControllerProvider.mo1933get(), this.securityViewFlipperControllerProvider.mo1933get(), this.configurationControllerProvider.mo1933get());
    }

    public static KeyguardSecurityContainerController_Factory_Factory create(Provider<KeyguardSecurityContainer> provider, Provider<AdminSecondaryLockScreenController.Factory> provider2, Provider<LockPatternUtils> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<KeyguardSecurityModel> provider5, Provider<MetricsLogger> provider6, Provider<UiEventLogger> provider7, Provider<KeyguardStateController> provider8, Provider<KeyguardSecurityViewFlipperController> provider9, Provider<ConfigurationController> provider10) {
        return new KeyguardSecurityContainerController_Factory_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static KeyguardSecurityContainerController.Factory newInstance(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController) {
        return new KeyguardSecurityContainerController.Factory(keyguardSecurityContainer, factory, lockPatternUtils, keyguardUpdateMonitor, keyguardSecurityModel, metricsLogger, uiEventLogger, keyguardStateController, keyguardSecurityViewFlipperController, configurationController);
    }
}
