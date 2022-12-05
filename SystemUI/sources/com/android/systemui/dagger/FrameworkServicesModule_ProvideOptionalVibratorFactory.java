package com.android.systemui.dagger;

import android.content.Context;
import android.os.Vibrator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideOptionalVibratorFactory implements Factory<Optional<Vibrator>> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideOptionalVibratorFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Optional<Vibrator> mo1933get() {
        return provideOptionalVibrator(this.contextProvider.mo1933get());
    }

    public static FrameworkServicesModule_ProvideOptionalVibratorFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideOptionalVibratorFactory(provider);
    }

    public static Optional<Vibrator> provideOptionalVibrator(Context context) {
        return (Optional) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideOptionalVibrator(context));
    }
}
