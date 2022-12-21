package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.RootDisplayAreaOrganizer;
import com.android.p019wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory */
public final class WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory implements Factory<RootDisplayAreaOrganizer> {
    private final Provider<ShellExecutor> mainExecutorProvider;

    public WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory(Provider<ShellExecutor> provider) {
        this.mainExecutorProvider = provider;
    }

    public RootDisplayAreaOrganizer get() {
        return provideRootDisplayAreaOrganizer(this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory create(Provider<ShellExecutor> provider) {
        return new WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory(provider);
    }

    public static RootDisplayAreaOrganizer provideRootDisplayAreaOrganizer(ShellExecutor shellExecutor) {
        return (RootDisplayAreaOrganizer) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideRootDisplayAreaOrganizer(shellExecutor));
    }
}
