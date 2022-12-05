package com.android.systemui.wmshell;

import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellCommandHandlerImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideShellCommandHandlerFactory implements Factory<Optional<ShellCommandHandler>> {
    private final Provider<ShellCommandHandlerImpl> implProvider;

    public WMShellBaseModule_ProvideShellCommandHandlerFactory(Provider<ShellCommandHandlerImpl> provider) {
        this.implProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Optional<ShellCommandHandler> mo1933get() {
        return provideShellCommandHandler(this.implProvider.mo1933get());
    }

    public static WMShellBaseModule_ProvideShellCommandHandlerFactory create(Provider<ShellCommandHandlerImpl> provider) {
        return new WMShellBaseModule_ProvideShellCommandHandlerFactory(provider);
    }

    public static Optional<ShellCommandHandler> provideShellCommandHandler(ShellCommandHandlerImpl shellCommandHandlerImpl) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellCommandHandler(shellCommandHandlerImpl));
    }
}
