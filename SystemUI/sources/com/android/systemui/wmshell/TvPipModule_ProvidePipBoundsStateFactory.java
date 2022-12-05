package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.pip.PipBoundsState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvPipModule_ProvidePipBoundsStateFactory implements Factory<PipBoundsState> {
    private final Provider<Context> contextProvider;

    public TvPipModule_ProvidePipBoundsStateFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public PipBoundsState mo1933get() {
        return providePipBoundsState(this.contextProvider.mo1933get());
    }

    public static TvPipModule_ProvidePipBoundsStateFactory create(Provider<Context> provider) {
        return new TvPipModule_ProvidePipBoundsStateFactory(provider);
    }

    public static PipBoundsState providePipBoundsState(Context context) {
        return (PipBoundsState) Preconditions.checkNotNullFromProvides(TvPipModule.providePipBoundsState(context));
    }
}
