package com.android.systemui.dagger;

import android.media.IAudioService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIAudioServiceFactory implements Factory<IAudioService> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public IAudioService mo1933get() {
        return provideIAudioService();
    }

    public static FrameworkServicesModule_ProvideIAudioServiceFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IAudioService provideIAudioService() {
        return (IAudioService) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIAudioService());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIAudioServiceFactory INSTANCE = new FrameworkServicesModule_ProvideIAudioServiceFactory();
    }
}
