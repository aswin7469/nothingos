package com.android.systemui.wmshell;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellConcurrencyModule_ProvideShellMainExecutorFactory implements Factory<ShellExecutor> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<ShellExecutor> sysuiMainExecutorProvider;

    public WMShellConcurrencyModule_ProvideShellMainExecutorFactory(Provider<Context> provider, Provider<Handler> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.sysuiMainExecutorProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ShellExecutor mo1933get() {
        return provideShellMainExecutor(this.contextProvider.mo1933get(), this.mainHandlerProvider.mo1933get(), this.sysuiMainExecutorProvider.mo1933get());
    }

    public static WMShellConcurrencyModule_ProvideShellMainExecutorFactory create(Provider<Context> provider, Provider<Handler> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellConcurrencyModule_ProvideShellMainExecutorFactory(provider, provider2, provider3);
    }

    public static ShellExecutor provideShellMainExecutor(Context context, Handler handler, ShellExecutor shellExecutor) {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideShellMainExecutor(context, handler, shellExecutor));
    }
}
