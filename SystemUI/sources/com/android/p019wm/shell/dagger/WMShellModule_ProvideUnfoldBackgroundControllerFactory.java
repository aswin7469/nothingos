package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.p019wm.shell.unfold.UnfoldBackgroundController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvideUnfoldBackgroundControllerFactory */
public final class WMShellModule_ProvideUnfoldBackgroundControllerFactory implements Factory<UnfoldBackgroundController> {
    private final Provider<Context> contextProvider;
    private final Provider<RootTaskDisplayAreaOrganizer> rootTaskDisplayAreaOrganizerProvider;

    public WMShellModule_ProvideUnfoldBackgroundControllerFactory(Provider<RootTaskDisplayAreaOrganizer> provider, Provider<Context> provider2) {
        this.rootTaskDisplayAreaOrganizerProvider = provider;
        this.contextProvider = provider2;
    }

    public UnfoldBackgroundController get() {
        return provideUnfoldBackgroundController(this.rootTaskDisplayAreaOrganizerProvider.get(), this.contextProvider.get());
    }

    public static WMShellModule_ProvideUnfoldBackgroundControllerFactory create(Provider<RootTaskDisplayAreaOrganizer> provider, Provider<Context> provider2) {
        return new WMShellModule_ProvideUnfoldBackgroundControllerFactory(provider, provider2);
    }

    public static UnfoldBackgroundController provideUnfoldBackgroundController(RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, Context context) {
        return (UnfoldBackgroundController) Preconditions.checkNotNullFromProvides(WMShellModule.provideUnfoldBackgroundController(rootTaskDisplayAreaOrganizer, context));
    }
}
