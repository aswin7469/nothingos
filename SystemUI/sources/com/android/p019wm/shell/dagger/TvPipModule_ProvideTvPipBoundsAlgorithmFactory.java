package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsAlgorithm;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvideTvPipBoundsAlgorithmFactory */
public final class TvPipModule_ProvideTvPipBoundsAlgorithmFactory implements Factory<TvPipBoundsAlgorithm> {
    private final Provider<Context> contextProvider;
    private final Provider<PipSnapAlgorithm> pipSnapAlgorithmProvider;
    private final Provider<TvPipBoundsState> tvPipBoundsStateProvider;

    public TvPipModule_ProvideTvPipBoundsAlgorithmFactory(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<PipSnapAlgorithm> provider3) {
        this.contextProvider = provider;
        this.tvPipBoundsStateProvider = provider2;
        this.pipSnapAlgorithmProvider = provider3;
    }

    public TvPipBoundsAlgorithm get() {
        return provideTvPipBoundsAlgorithm(this.contextProvider.get(), this.tvPipBoundsStateProvider.get(), this.pipSnapAlgorithmProvider.get());
    }

    public static TvPipModule_ProvideTvPipBoundsAlgorithmFactory create(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<PipSnapAlgorithm> provider3) {
        return new TvPipModule_ProvideTvPipBoundsAlgorithmFactory(provider, provider2, provider3);
    }

    public static TvPipBoundsAlgorithm provideTvPipBoundsAlgorithm(Context context, TvPipBoundsState tvPipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        return (TvPipBoundsAlgorithm) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipBoundsAlgorithm(context, tvPipBoundsState, pipSnapAlgorithm));
    }
}
