package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.ShellCommandHandler;
import com.android.p019wm.shell.ShellCommandHandlerImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellCommandHandlerFactory */
public final class WMShellBaseModule_ProvideShellCommandHandlerFactory implements Factory<Optional<ShellCommandHandler>> {
    private final Provider<ShellCommandHandlerImpl> implProvider;

    public WMShellBaseModule_ProvideShellCommandHandlerFactory(Provider<ShellCommandHandlerImpl> provider) {
        this.implProvider = provider;
    }

    public Optional<ShellCommandHandler> get() {
        return provideShellCommandHandler(this.implProvider.get());
    }

    public static WMShellBaseModule_ProvideShellCommandHandlerFactory create(Provider<ShellCommandHandlerImpl> provider) {
        return new WMShellBaseModule_ProvideShellCommandHandlerFactory(provider);
    }

    public static Optional<ShellCommandHandler> provideShellCommandHandler(ShellCommandHandlerImpl shellCommandHandlerImpl) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellCommandHandler(shellCommandHandlerImpl));
    }
}