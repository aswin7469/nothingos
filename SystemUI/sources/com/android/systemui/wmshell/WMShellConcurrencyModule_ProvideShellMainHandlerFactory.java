package com.android.systemui.wmshell;

import android.content.Context;
import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellConcurrencyModule_ProvideShellMainHandlerFactory implements Factory<Handler> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> sysuiMainHandlerProvider;

    public WMShellConcurrencyModule_ProvideShellMainHandlerFactory(Provider<Context> provider, Provider<Handler> provider2) {
        this.contextProvider = provider;
        this.sysuiMainHandlerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public Handler mo1933get() {
        return provideShellMainHandler(this.contextProvider.mo1933get(), this.sysuiMainHandlerProvider.mo1933get());
    }

    public static WMShellConcurrencyModule_ProvideShellMainHandlerFactory create(Provider<Context> provider, Provider<Handler> provider2) {
        return new WMShellConcurrencyModule_ProvideShellMainHandlerFactory(provider, provider2);
    }

    public static Handler provideShellMainHandler(Context context, Handler handler) {
        return (Handler) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideShellMainHandler(context, handler));
    }
}
