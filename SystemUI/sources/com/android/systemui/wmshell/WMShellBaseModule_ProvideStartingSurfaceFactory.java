package com.android.systemui.wmshell;

import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.startingsurface.StartingWindowController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideStartingSurfaceFactory implements Factory<Optional<StartingSurface>> {
    private final Provider<StartingWindowController> startingWindowControllerProvider;

    public WMShellBaseModule_ProvideStartingSurfaceFactory(Provider<StartingWindowController> provider) {
        this.startingWindowControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Optional<StartingSurface> mo1933get() {
        return provideStartingSurface(this.startingWindowControllerProvider.mo1933get());
    }

    public static WMShellBaseModule_ProvideStartingSurfaceFactory create(Provider<StartingWindowController> provider) {
        return new WMShellBaseModule_ProvideStartingSurfaceFactory(provider);
    }

    public static Optional<StartingSurface> provideStartingSurface(StartingWindowController startingWindowController) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideStartingSurface(startingWindowController));
    }
}
