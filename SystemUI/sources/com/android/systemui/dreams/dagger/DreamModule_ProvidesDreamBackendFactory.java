package com.android.systemui.dreams.dagger;

import android.content.Context;
import com.android.settingslib.dream.DreamBackend;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamModule_ProvidesDreamBackendFactory implements Factory<DreamBackend> {
    private final Provider<Context> contextProvider;

    public DreamModule_ProvidesDreamBackendFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public DreamBackend get() {
        return providesDreamBackend(this.contextProvider.get());
    }

    public static DreamModule_ProvidesDreamBackendFactory create(Provider<Context> provider) {
        return new DreamModule_ProvidesDreamBackendFactory(provider);
    }

    public static DreamBackend providesDreamBackend(Context context) {
        return (DreamBackend) Preconditions.checkNotNullFromProvides(DreamModule.providesDreamBackend(context));
    }
}
