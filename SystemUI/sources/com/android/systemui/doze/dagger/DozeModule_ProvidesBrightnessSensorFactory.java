package com.android.systemui.doze.dagger;

import android.content.Context;
import android.hardware.Sensor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeModule_ProvidesBrightnessSensorFactory implements Factory<Optional<Sensor>> {
    private final Provider<Context> contextProvider;
    private final Provider<AsyncSensorManager> sensorManagerProvider;

    public DozeModule_ProvidesBrightnessSensorFactory(Provider<AsyncSensorManager> provider, Provider<Context> provider2) {
        this.sensorManagerProvider = provider;
        this.contextProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Optional<Sensor> mo1933get() {
        return providesBrightnessSensor(this.sensorManagerProvider.mo1933get(), this.contextProvider.mo1933get());
    }

    public static DozeModule_ProvidesBrightnessSensorFactory create(Provider<AsyncSensorManager> provider, Provider<Context> provider2) {
        return new DozeModule_ProvidesBrightnessSensorFactory(provider, provider2);
    }

    public static Optional<Sensor> providesBrightnessSensor(AsyncSensorManager asyncSensorManager, Context context) {
        return (Optional) Preconditions.checkNotNullFromProvides(DozeModule.providesBrightnessSensor(asyncSensorManager, context));
    }
}
