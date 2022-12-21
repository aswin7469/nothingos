package com.android.systemui.util.service.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ObservableServiceModule_ProvidesMaxReconnectAttemptsFactory implements Factory<Integer> {
    private final Provider<Resources> resourcesProvider;

    public ObservableServiceModule_ProvidesMaxReconnectAttemptsFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesMaxReconnectAttempts(this.resourcesProvider.get()));
    }

    public static ObservableServiceModule_ProvidesMaxReconnectAttemptsFactory create(Provider<Resources> provider) {
        return new ObservableServiceModule_ProvidesMaxReconnectAttemptsFactory(provider);
    }

    public static int providesMaxReconnectAttempts(Resources resources) {
        return ObservableServiceModule.providesMaxReconnectAttempts(resources);
    }
}
