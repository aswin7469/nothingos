package com.android.keyguard;

import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardStatusViewController_Factory implements Factory<KeyguardStatusViewController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<KeyguardClockSwitchController> keyguardClockSwitchControllerProvider;
    private final Provider<KeyguardSliceViewController> keyguardSliceViewControllerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardStatusView> keyguardStatusViewProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;

    public KeyguardStatusViewController_Factory(Provider<KeyguardStatusView> provider, Provider<KeyguardSliceViewController> provider2, Provider<KeyguardClockSwitchController> provider3, Provider<KeyguardStateController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<ConfigurationController> provider6, Provider<DozeParameters> provider7, Provider<ScreenOffAnimationController> provider8) {
        this.keyguardStatusViewProvider = provider;
        this.keyguardSliceViewControllerProvider = provider2;
        this.keyguardClockSwitchControllerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.keyguardUpdateMonitorProvider = provider5;
        this.configurationControllerProvider = provider6;
        this.dozeParametersProvider = provider7;
        this.screenOffAnimationControllerProvider = provider8;
    }

    public KeyguardStatusViewController get() {
        return newInstance(this.keyguardStatusViewProvider.get(), this.keyguardSliceViewControllerProvider.get(), this.keyguardClockSwitchControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.configurationControllerProvider.get(), this.dozeParametersProvider.get(), this.screenOffAnimationControllerProvider.get());
    }

    public static KeyguardStatusViewController_Factory create(Provider<KeyguardStatusView> provider, Provider<KeyguardSliceViewController> provider2, Provider<KeyguardClockSwitchController> provider3, Provider<KeyguardStateController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<ConfigurationController> provider6, Provider<DozeParameters> provider7, Provider<ScreenOffAnimationController> provider8) {
        return new KeyguardStatusViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static KeyguardStatusViewController newInstance(KeyguardStatusView keyguardStatusView, KeyguardSliceViewController keyguardSliceViewController, KeyguardClockSwitchController keyguardClockSwitchController, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController) {
        return new KeyguardStatusViewController(keyguardStatusView, keyguardSliceViewController, keyguardClockSwitchController, keyguardStateController, keyguardUpdateMonitor, configurationController, dozeParameters, screenOffAnimationController);
    }
}
