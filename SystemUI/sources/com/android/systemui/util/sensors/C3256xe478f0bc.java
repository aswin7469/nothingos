package com.android.systemui.util.sensors;

import android.content.res.Resources;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.util.sensors.SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory */
public final class C3256xe478f0bc implements Factory<ThresholdSensor[]> {
    private final Provider<Resources> resourcesProvider;
    private final Provider<ThresholdSensorImpl.BuilderFactory> thresholdSensorImplBuilderFactoryProvider;

    public C3256xe478f0bc(Provider<ThresholdSensorImpl.BuilderFactory> provider, Provider<Resources> provider2) {
        this.thresholdSensorImplBuilderFactoryProvider = provider;
        this.resourcesProvider = provider2;
    }

    public ThresholdSensor[] get() {
        return providePostureToSecondaryProximitySensorMapping(this.thresholdSensorImplBuilderFactoryProvider.get(), this.resourcesProvider.get());
    }

    public static C3256xe478f0bc create(Provider<ThresholdSensorImpl.BuilderFactory> provider, Provider<Resources> provider2) {
        return new C3256xe478f0bc(provider, provider2);
    }

    public static ThresholdSensor[] providePostureToSecondaryProximitySensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, Resources resources) {
        return (ThresholdSensor[]) Preconditions.checkNotNullFromProvides(SensorModule.providePostureToSecondaryProximitySensorMapping(builderFactory, resources));
    }
}
