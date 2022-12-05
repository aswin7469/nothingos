package com.android.systemui.wmshell;

import com.android.wm.shell.ShellInit;
import com.android.wm.shell.ShellInitImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideShellInitFactory implements Factory<ShellInit> {
    private final Provider<ShellInitImpl> implProvider;

    public WMShellBaseModule_ProvideShellInitFactory(Provider<ShellInitImpl> provider) {
        this.implProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ShellInit mo1933get() {
        return provideShellInit(this.implProvider.mo1933get());
    }

    public static WMShellBaseModule_ProvideShellInitFactory create(Provider<ShellInitImpl> provider) {
        return new WMShellBaseModule_ProvideShellInitFactory(provider);
    }

    public static ShellInit provideShellInit(ShellInitImpl shellInitImpl) {
        return (ShellInit) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellInit(shellInitImpl));
    }
}
