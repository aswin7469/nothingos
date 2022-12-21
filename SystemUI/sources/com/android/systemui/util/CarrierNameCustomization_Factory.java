package com.android.systemui.util;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class CarrierNameCustomization_Factory implements Factory<CarrierNameCustomization> {
    private final Provider<Context> contextProvider;

    public CarrierNameCustomization_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public CarrierNameCustomization get() {
        return newInstance(this.contextProvider.get());
    }

    public static CarrierNameCustomization_Factory create(Provider<Context> provider) {
        return new CarrierNameCustomization_Factory(provider);
    }

    public static CarrierNameCustomization newInstance(Context context) {
        return new CarrierNameCustomization(context);
    }
}
