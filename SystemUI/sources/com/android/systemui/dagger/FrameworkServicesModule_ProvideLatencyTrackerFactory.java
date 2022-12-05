package com.android.systemui.dagger;

import android.content.Context;
import com.android.internal.util.LatencyTracker;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideLatencyTrackerFactory implements Factory<LatencyTracker> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideLatencyTrackerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LatencyTracker mo1933get() {
        return provideLatencyTracker(this.contextProvider.mo1933get());
    }

    public static FrameworkServicesModule_ProvideLatencyTrackerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideLatencyTrackerFactory(provider);
    }

    public static LatencyTracker provideLatencyTracker(Context context) {
        return (LatencyTracker) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideLatencyTracker(context));
    }
}
