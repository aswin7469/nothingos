package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dagger.FrameworkServicesModule_ProvideAmbientDisplayConfigurationFactory */
public final class C2053x19afde28 implements Factory<AmbientDisplayConfiguration> {
    private final Provider<Context> contextProvider;
    private final FrameworkServicesModule module;

    public C2053x19afde28(FrameworkServicesModule frameworkServicesModule, Provider<Context> provider) {
        this.module = frameworkServicesModule;
        this.contextProvider = provider;
    }

    public AmbientDisplayConfiguration get() {
        return provideAmbientDisplayConfiguration(this.module, this.contextProvider.get());
    }

    public static C2053x19afde28 create(FrameworkServicesModule frameworkServicesModule, Provider<Context> provider) {
        return new C2053x19afde28(frameworkServicesModule, provider);
    }

    public static AmbientDisplayConfiguration provideAmbientDisplayConfiguration(FrameworkServicesModule frameworkServicesModule, Context context) {
        return (AmbientDisplayConfiguration) Preconditions.checkNotNullFromProvides(frameworkServicesModule.provideAmbientDisplayConfiguration(context));
    }
}
