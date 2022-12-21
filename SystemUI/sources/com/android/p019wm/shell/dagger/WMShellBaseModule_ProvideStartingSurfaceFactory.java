package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.android.p019wm.shell.startingsurface.StartingWindowController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideStartingSurfaceFactory */
public final class WMShellBaseModule_ProvideStartingSurfaceFactory implements Factory<Optional<StartingSurface>> {
    private final Provider<StartingWindowController> startingWindowControllerProvider;

    public WMShellBaseModule_ProvideStartingSurfaceFactory(Provider<StartingWindowController> provider) {
        this.startingWindowControllerProvider = provider;
    }

    public Optional<StartingSurface> get() {
        return provideStartingSurface(this.startingWindowControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideStartingSurfaceFactory create(Provider<StartingWindowController> provider) {
        return new WMShellBaseModule_ProvideStartingSurfaceFactory(provider);
    }

    public static Optional<StartingSurface> provideStartingSurface(StartingWindowController startingWindowController) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideStartingSurface(startingWindowController));
    }
}
