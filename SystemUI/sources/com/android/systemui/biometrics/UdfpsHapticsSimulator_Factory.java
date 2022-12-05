package com.android.systemui.biometrics;

import android.os.Vibrator;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UdfpsHapticsSimulator_Factory implements Factory<UdfpsHapticsSimulator> {
    private final Provider<CommandRegistry> commandRegistryProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<Vibrator> vibratorProvider;

    public UdfpsHapticsSimulator_Factory(Provider<CommandRegistry> provider, Provider<Vibrator> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        this.commandRegistryProvider = provider;
        this.vibratorProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public UdfpsHapticsSimulator mo1933get() {
        return newInstance(this.commandRegistryProvider.mo1933get(), this.vibratorProvider.mo1933get(), this.keyguardUpdateMonitorProvider.mo1933get());
    }

    public static UdfpsHapticsSimulator_Factory create(Provider<CommandRegistry> provider, Provider<Vibrator> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        return new UdfpsHapticsSimulator_Factory(provider, provider2, provider3);
    }

    public static UdfpsHapticsSimulator newInstance(CommandRegistry commandRegistry, Vibrator vibrator, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new UdfpsHapticsSimulator(commandRegistry, vibrator, keyguardUpdateMonitor);
    }
}
