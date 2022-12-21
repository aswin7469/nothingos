package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipAppOpsListenerFactory */
public final class TvPipModule_ProvidePipAppOpsListenerFactory implements Factory<PipAppOpsListener> {
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;

    public TvPipModule_ProvidePipAppOpsListenerFactory(Provider<Context> provider, Provider<PipTaskOrganizer> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.pipTaskOrganizerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    public PipAppOpsListener get() {
        return providePipAppOpsListener(this.contextProvider.get(), this.pipTaskOrganizerProvider.get(), this.mainExecutorProvider.get());
    }

    public static TvPipModule_ProvidePipAppOpsListenerFactory create(Provider<Context> provider, Provider<PipTaskOrganizer> provider2, Provider<ShellExecutor> provider3) {
        return new TvPipModule_ProvidePipAppOpsListenerFactory(provider, provider2, provider3);
    }

    public static PipAppOpsListener providePipAppOpsListener(Context context, PipTaskOrganizer pipTaskOrganizer, ShellExecutor shellExecutor) {
        return (PipAppOpsListener) Preconditions.checkNotNullFromProvides(TvPipModule.providePipAppOpsListener(context, pipTaskOrganizer, shellExecutor));
    }
}
