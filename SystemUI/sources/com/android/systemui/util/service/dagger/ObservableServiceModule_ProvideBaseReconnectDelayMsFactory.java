package com.android.systemui.util.service.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ObservableServiceModule_ProvideBaseReconnectDelayMsFactory implements Factory<Integer> {
    private final Provider<Resources> resourcesProvider;

    public ObservableServiceModule_ProvideBaseReconnectDelayMsFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(provideBaseReconnectDelayMs(this.resourcesProvider.get()));
    }

    public static ObservableServiceModule_ProvideBaseReconnectDelayMsFactory create(Provider<Resources> provider) {
        return new ObservableServiceModule_ProvideBaseReconnectDelayMsFactory(provider);
    }

    public static int provideBaseReconnectDelayMs(Resources resources) {
        return ObservableServiceModule.provideBaseReconnectDelayMs(resources);
    }
}
