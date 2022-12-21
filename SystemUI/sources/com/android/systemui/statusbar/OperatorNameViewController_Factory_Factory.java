package com.android.systemui.statusbar;

import android.telephony.TelephonyManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class OperatorNameViewController_Factory_Factory implements Factory<OperatorNameViewController.Factory> {
    private final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    private final Provider<DarkIconDispatcher> darkIconDispatcherProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NetworkController> networkControllerProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public OperatorNameViewController_Factory_Factory(Provider<DarkIconDispatcher> provider, Provider<NetworkController> provider2, Provider<TunerService> provider3, Provider<TelephonyManager> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<CarrierConfigTracker> provider6) {
        this.darkIconDispatcherProvider = provider;
        this.networkControllerProvider = provider2;
        this.tunerServiceProvider = provider3;
        this.telephonyManagerProvider = provider4;
        this.keyguardUpdateMonitorProvider = provider5;
        this.carrierConfigTrackerProvider = provider6;
    }

    public OperatorNameViewController.Factory get() {
        return newInstance(this.darkIconDispatcherProvider.get(), this.networkControllerProvider.get(), this.tunerServiceProvider.get(), this.telephonyManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.carrierConfigTrackerProvider.get());
    }

    public static OperatorNameViewController_Factory_Factory create(Provider<DarkIconDispatcher> provider, Provider<NetworkController> provider2, Provider<TunerService> provider3, Provider<TelephonyManager> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<CarrierConfigTracker> provider6) {
        return new OperatorNameViewController_Factory_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static OperatorNameViewController.Factory newInstance(DarkIconDispatcher darkIconDispatcher, NetworkController networkController, TunerService tunerService, TelephonyManager telephonyManager, KeyguardUpdateMonitor keyguardUpdateMonitor, CarrierConfigTracker carrierConfigTracker) {
        return new OperatorNameViewController.Factory(darkIconDispatcher, networkController, tunerService, telephonyManager, keyguardUpdateMonitor, carrierConfigTracker);
    }
}
