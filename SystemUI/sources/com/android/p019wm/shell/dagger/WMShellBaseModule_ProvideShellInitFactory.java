package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.ShellInit;
import com.android.p019wm.shell.ShellInitImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellInitFactory */
public final class WMShellBaseModule_ProvideShellInitFactory implements Factory<ShellInit> {
    private final Provider<ShellInitImpl> implProvider;

    public WMShellBaseModule_ProvideShellInitFactory(Provider<ShellInitImpl> provider) {
        this.implProvider = provider;
    }

    public ShellInit get() {
        return provideShellInit(this.implProvider.get());
    }

    public static WMShellBaseModule_ProvideShellInitFactory create(Provider<ShellInitImpl> provider) {
        return new WMShellBaseModule_ProvideShellInitFactory(provider);
    }

    public static ShellInit provideShellInit(ShellInitImpl shellInitImpl) {
        return (ShellInit) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellInit(shellInitImpl));
    }
}
