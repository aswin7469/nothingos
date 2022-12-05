package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideAmbientDisplayConfigurationFactory implements Factory<AmbientDisplayConfiguration> {
    private final Provider<Context> contextProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProvideAmbientDisplayConfigurationFactory(DependencyProvider dependencyProvider, Provider<Context> provider) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public AmbientDisplayConfiguration mo1933get() {
        return provideAmbientDisplayConfiguration(this.module, this.contextProvider.mo1933get());
    }

    public static DependencyProvider_ProvideAmbientDisplayConfigurationFactory create(DependencyProvider dependencyProvider, Provider<Context> provider) {
        return new DependencyProvider_ProvideAmbientDisplayConfigurationFactory(dependencyProvider, provider);
    }

    public static AmbientDisplayConfiguration provideAmbientDisplayConfiguration(DependencyProvider dependencyProvider, Context context) {
        return (AmbientDisplayConfiguration) Preconditions.checkNotNullFromProvides(dependencyProvider.provideAmbientDisplayConfiguration(context));
    }
}
