package com.android.systemui.doze;

import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeScreenBrightness_Factory implements Factory<DozeScreenBrightness> {
    private final Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<DozeHost> hostProvider;
    private final Provider<Optional<Sensor>> lightSensorOptionalProvider;
    private final Provider<AsyncSensorManager> sensorManagerProvider;
    private final Provider<DozeMachine.Service> serviceProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public DozeScreenBrightness_Factory(Provider<Context> provider, Provider<DozeMachine.Service> provider2, Provider<AsyncSensorManager> provider3, Provider<Optional<Sensor>> provider4, Provider<DozeHost> provider5, Provider<Handler> provider6, Provider<AlwaysOnDisplayPolicy> provider7, Provider<WakefulnessLifecycle> provider8, Provider<DozeParameters> provider9, Provider<DockManager> provider10, Provider<UnlockedScreenOffAnimationController> provider11) {
        this.contextProvider = provider;
        this.serviceProvider = provider2;
        this.sensorManagerProvider = provider3;
        this.lightSensorOptionalProvider = provider4;
        this.hostProvider = provider5;
        this.handlerProvider = provider6;
        this.alwaysOnDisplayPolicyProvider = provider7;
        this.wakefulnessLifecycleProvider = provider8;
        this.dozeParametersProvider = provider9;
        this.dockManagerProvider = provider10;
        this.unlockedScreenOffAnimationControllerProvider = provider11;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DozeScreenBrightness mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.serviceProvider.mo1933get(), this.sensorManagerProvider.mo1933get(), this.lightSensorOptionalProvider.mo1933get(), this.hostProvider.mo1933get(), this.handlerProvider.mo1933get(), this.alwaysOnDisplayPolicyProvider.mo1933get(), this.wakefulnessLifecycleProvider.mo1933get(), this.dozeParametersProvider.mo1933get(), this.dockManagerProvider.mo1933get(), this.unlockedScreenOffAnimationControllerProvider.mo1933get());
    }

    public static DozeScreenBrightness_Factory create(Provider<Context> provider, Provider<DozeMachine.Service> provider2, Provider<AsyncSensorManager> provider3, Provider<Optional<Sensor>> provider4, Provider<DozeHost> provider5, Provider<Handler> provider6, Provider<AlwaysOnDisplayPolicy> provider7, Provider<WakefulnessLifecycle> provider8, Provider<DozeParameters> provider9, Provider<DockManager> provider10, Provider<UnlockedScreenOffAnimationController> provider11) {
        return new DozeScreenBrightness_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static DozeScreenBrightness newInstance(Context context, DozeMachine.Service service, AsyncSensorManager asyncSensorManager, Optional<Sensor> optional, DozeHost dozeHost, Handler handler, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, WakefulnessLifecycle wakefulnessLifecycle, DozeParameters dozeParameters, DockManager dockManager, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        return new DozeScreenBrightness(context, service, asyncSensorManager, optional, dozeHost, handler, alwaysOnDisplayPolicy, wakefulnessLifecycle, dozeParameters, dockManager, unlockedScreenOffAnimationController);
    }
}
