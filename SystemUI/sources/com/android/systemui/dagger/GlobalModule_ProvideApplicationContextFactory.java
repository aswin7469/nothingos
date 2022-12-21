package com.android.systemui.dagger;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class GlobalModule_ProvideApplicationContextFactory implements Factory<Context> {
    private final Provider<Context> contextProvider;
    private final GlobalModule module;

    public GlobalModule_ProvideApplicationContextFactory(GlobalModule globalModule, Provider<Context> provider) {
        this.module = globalModule;
        this.contextProvider = provider;
    }

    public Context get() {
        return provideApplicationContext(this.module, this.contextProvider.get());
    }

    public static GlobalModule_ProvideApplicationContextFactory create(GlobalModule globalModule, Provider<Context> provider) {
        return new GlobalModule_ProvideApplicationContextFactory(globalModule, provider);
    }

    public static Context provideApplicationContext(GlobalModule globalModule, Context context) {
        return (Context) Preconditions.checkNotNullFromProvides(globalModule.provideApplicationContext(context));
    }
}
