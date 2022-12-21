package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.RootDisplayAreaOrganizer;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.displayareahelper.DisplayAreaHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayAreaHelperFactory */
public final class WMShellBaseModule_ProvideDisplayAreaHelperFactory implements Factory<Optional<DisplayAreaHelper>> {
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<RootDisplayAreaOrganizer> rootDisplayAreaOrganizerProvider;

    public WMShellBaseModule_ProvideDisplayAreaHelperFactory(Provider<ShellExecutor> provider, Provider<RootDisplayAreaOrganizer> provider2) {
        this.mainExecutorProvider = provider;
        this.rootDisplayAreaOrganizerProvider = provider2;
    }

    public Optional<DisplayAreaHelper> get() {
        return provideDisplayAreaHelper(this.mainExecutorProvider.get(), this.rootDisplayAreaOrganizerProvider.get());
    }

    public static WMShellBaseModule_ProvideDisplayAreaHelperFactory create(Provider<ShellExecutor> provider, Provider<RootDisplayAreaOrganizer> provider2) {
        return new WMShellBaseModule_ProvideDisplayAreaHelperFactory(provider, provider2);
    }

    public static Optional<DisplayAreaHelper> provideDisplayAreaHelper(ShellExecutor shellExecutor, RootDisplayAreaOrganizer rootDisplayAreaOrganizer) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDisplayAreaHelper(shellExecutor, rootDisplayAreaOrganizer));
    }
}
