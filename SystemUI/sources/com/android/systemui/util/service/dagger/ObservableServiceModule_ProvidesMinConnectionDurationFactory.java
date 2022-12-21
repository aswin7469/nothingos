package com.android.systemui.util.service.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ObservableServiceModule_ProvidesMinConnectionDurationFactory implements Factory<Integer> {
    private final Provider<Resources> resourcesProvider;

    public ObservableServiceModule_ProvidesMinConnectionDurationFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesMinConnectionDuration(this.resourcesProvider.get()));
    }

    public static ObservableServiceModule_ProvidesMinConnectionDurationFactory create(Provider<Resources> provider) {
        return new ObservableServiceModule_ProvidesMinConnectionDurationFactory(provider);
    }

    public static int providesMinConnectionDuration(Resources resources) {
        return ObservableServiceModule.providesMinConnectionDuration(resources);
    }
}
