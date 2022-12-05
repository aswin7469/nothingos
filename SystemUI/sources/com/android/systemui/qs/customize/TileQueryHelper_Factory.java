package com.android.systemui.qs.customize;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.FeatureFlags;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TileQueryHelper_Factory implements Factory<TileQueryHelper> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public TileQueryHelper_Factory(Provider<Context> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<Executor> provider4, Provider<FeatureFlags> provider5) {
        this.contextProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.bgExecutorProvider = provider4;
        this.featureFlagsProvider = provider5;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public TileQueryHelper mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.userTrackerProvider.mo1933get(), this.mainExecutorProvider.mo1933get(), this.bgExecutorProvider.mo1933get(), this.featureFlagsProvider.mo1933get());
    }

    public static TileQueryHelper_Factory create(Provider<Context> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<Executor> provider4, Provider<FeatureFlags> provider5) {
        return new TileQueryHelper_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static TileQueryHelper newInstance(Context context, UserTracker userTracker, Executor executor, Executor executor2, FeatureFlags featureFlags) {
        return new TileQueryHelper(context, userTracker, executor, executor2, featureFlags);
    }
}
