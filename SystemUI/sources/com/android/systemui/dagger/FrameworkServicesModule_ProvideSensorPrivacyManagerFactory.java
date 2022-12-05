package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.SensorPrivacyManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideSensorPrivacyManagerFactory implements Factory<SensorPrivacyManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideSensorPrivacyManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public SensorPrivacyManager mo1933get() {
        return provideSensorPrivacyManager(this.contextProvider.mo1933get());
    }

    public static FrameworkServicesModule_ProvideSensorPrivacyManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideSensorPrivacyManagerFactory(provider);
    }

    public static SensorPrivacyManager provideSensorPrivacyManager(Context context) {
        return (SensorPrivacyManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideSensorPrivacyManager(context));
    }
}
