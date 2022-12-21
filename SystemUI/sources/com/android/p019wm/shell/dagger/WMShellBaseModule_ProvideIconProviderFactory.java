package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.launcher3.icons.IconProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideIconProviderFactory */
public final class WMShellBaseModule_ProvideIconProviderFactory implements Factory<IconProvider> {
    private final Provider<Context> contextProvider;

    public WMShellBaseModule_ProvideIconProviderFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public IconProvider get() {
        return provideIconProvider(this.contextProvider.get());
    }

    public static WMShellBaseModule_ProvideIconProviderFactory create(Provider<Context> provider) {
        return new WMShellBaseModule_ProvideIconProviderFactory(provider);
    }

    public static IconProvider provideIconProvider(Context context) {
        return (IconProvider) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideIconProvider(context));
    }
}
