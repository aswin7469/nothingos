package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamWeatherComplication_Factory implements Factory<DreamWeatherComplication> {
    private final Provider<DreamWeatherComplicationComponent.Factory> componentFactoryProvider;

    public DreamWeatherComplication_Factory(Provider<DreamWeatherComplicationComponent.Factory> provider) {
        this.componentFactoryProvider = provider;
    }

    public DreamWeatherComplication get() {
        return newInstance(this.componentFactoryProvider.get());
    }

    public static DreamWeatherComplication_Factory create(Provider<DreamWeatherComplicationComponent.Factory> provider) {
        return new DreamWeatherComplication_Factory(provider);
    }

    public static DreamWeatherComplication newInstance(DreamWeatherComplicationComponent.Factory factory) {
        return new DreamWeatherComplication(factory);
    }
}
