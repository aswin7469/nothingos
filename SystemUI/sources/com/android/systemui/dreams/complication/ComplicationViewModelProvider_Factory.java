package com.android.systemui.dreams.complication;

import androidx.lifecycle.ViewModelStore;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationViewModelProvider_Factory implements Factory<ComplicationViewModelProvider> {
    private final Provider<ViewModelStore> storeProvider;
    private final Provider<ComplicationViewModel> viewModelProvider;

    public ComplicationViewModelProvider_Factory(Provider<ViewModelStore> provider, Provider<ComplicationViewModel> provider2) {
        this.storeProvider = provider;
        this.viewModelProvider = provider2;
    }

    public ComplicationViewModelProvider get() {
        return newInstance(this.storeProvider.get(), this.viewModelProvider.get());
    }

    public static ComplicationViewModelProvider_Factory create(Provider<ViewModelStore> provider, Provider<ComplicationViewModel> provider2) {
        return new ComplicationViewModelProvider_Factory(provider, provider2);
    }

    public static ComplicationViewModelProvider newInstance(ViewModelStore viewModelStore, ComplicationViewModel complicationViewModel) {
        return new ComplicationViewModelProvider(viewModelStore, complicationViewModel);
    }
}
