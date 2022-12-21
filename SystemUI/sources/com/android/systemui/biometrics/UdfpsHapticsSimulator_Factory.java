package com.android.systemui.biometrics;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UdfpsHapticsSimulator_Factory implements Factory<UdfpsHapticsSimulator> {
    private final Provider<CommandRegistry> commandRegistryProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<VibratorHelper> vibratorProvider;

    public UdfpsHapticsSimulator_Factory(Provider<CommandRegistry> provider, Provider<VibratorHelper> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        this.commandRegistryProvider = provider;
        this.vibratorProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    public UdfpsHapticsSimulator get() {
        return newInstance(this.commandRegistryProvider.get(), this.vibratorProvider.get(), this.keyguardUpdateMonitorProvider.get());
    }

    public static UdfpsHapticsSimulator_Factory create(Provider<CommandRegistry> provider, Provider<VibratorHelper> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        return new UdfpsHapticsSimulator_Factory(provider, provider2, provider3);
    }

    public static UdfpsHapticsSimulator newInstance(CommandRegistry commandRegistry, VibratorHelper vibratorHelper, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new UdfpsHapticsSimulator(commandRegistry, vibratorHelper, keyguardUpdateMonitor);
    }
}
