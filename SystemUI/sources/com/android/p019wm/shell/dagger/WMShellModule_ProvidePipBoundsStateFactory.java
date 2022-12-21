package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.pip.PipBoundsState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipBoundsStateFactory */
public final class WMShellModule_ProvidePipBoundsStateFactory implements Factory<PipBoundsState> {
    private final Provider<Context> contextProvider;

    public WMShellModule_ProvidePipBoundsStateFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public PipBoundsState get() {
        return providePipBoundsState(this.contextProvider.get());
    }

    public static WMShellModule_ProvidePipBoundsStateFactory create(Provider<Context> provider) {
        return new WMShellModule_ProvidePipBoundsStateFactory(provider);
    }

    public static PipBoundsState providePipBoundsState(Context context) {
        return (PipBoundsState) Preconditions.checkNotNullFromProvides(WMShellModule.providePipBoundsState(context));
    }
}
