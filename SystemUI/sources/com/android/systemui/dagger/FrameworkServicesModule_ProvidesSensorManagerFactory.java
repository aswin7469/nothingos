package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.SensorManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvidesSensorManagerFactory implements Factory<SensorManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvidesSensorManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public SensorManager mo1933get() {
        return providesSensorManager(this.contextProvider.mo1933get());
    }

    public static FrameworkServicesModule_ProvidesSensorManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvidesSensorManagerFactory(provider);
    }

    public static SensorManager providesSensorManager(Context context) {
        return (SensorManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.providesSensorManager(context));
    }
}
