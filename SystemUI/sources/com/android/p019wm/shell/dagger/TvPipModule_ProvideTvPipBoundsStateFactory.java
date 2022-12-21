package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvideTvPipBoundsStateFactory */
public final class TvPipModule_ProvideTvPipBoundsStateFactory implements Factory<TvPipBoundsState> {
    private final Provider<Context> contextProvider;

    public TvPipModule_ProvideTvPipBoundsStateFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public TvPipBoundsState get() {
        return provideTvPipBoundsState(this.contextProvider.get());
    }

    public static TvPipModule_ProvideTvPipBoundsStateFactory create(Provider<Context> provider) {
        return new TvPipModule_ProvideTvPipBoundsStateFactory(provider);
    }

    public static TvPipBoundsState provideTvPipBoundsState(Context context) {
        return (TvPipBoundsState) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipBoundsState(context));
    }
}
