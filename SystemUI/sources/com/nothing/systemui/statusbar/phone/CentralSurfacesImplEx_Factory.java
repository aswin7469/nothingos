package com.nothing.systemui.statusbar.phone;

import android.content.Context;
import android.os.PowerManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.nothing.systemui.doze.AODController;
import com.nothing.systemui.doze.LiftWakeGestureController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class CentralSurfacesImplEx_Factory implements Factory<CentralSurfacesImplEx> {
    private final Provider<AODController> aodControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LiftWakeGestureController> liftWakeGestureControllerProvider;
    private final Provider<PowerManager> powerManagerProvider;

    public CentralSurfacesImplEx_Factory(Provider<Context> provider, Provider<PowerManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<AODController> provider4, Provider<LiftWakeGestureController> provider5) {
        this.contextProvider = provider;
        this.powerManagerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.aodControllerProvider = provider4;
        this.liftWakeGestureControllerProvider = provider5;
    }

    public CentralSurfacesImplEx get() {
        return newInstance(this.contextProvider.get(), this.powerManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.aodControllerProvider.get(), this.liftWakeGestureControllerProvider.get());
    }

    public static CentralSurfacesImplEx_Factory create(Provider<Context> provider, Provider<PowerManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<AODController> provider4, Provider<LiftWakeGestureController> provider5) {
        return new CentralSurfacesImplEx_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static CentralSurfacesImplEx newInstance(Context context, PowerManager powerManager, KeyguardUpdateMonitor keyguardUpdateMonitor, AODController aODController, LiftWakeGestureController liftWakeGestureController) {
        return new CentralSurfacesImplEx(context, powerManager, keyguardUpdateMonitor, aODController, liftWakeGestureController);
    }
}
