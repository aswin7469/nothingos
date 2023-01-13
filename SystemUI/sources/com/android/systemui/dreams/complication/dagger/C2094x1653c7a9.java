package com.android.systemui.dreams.complication.dagger;

import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dreams.complication.dagger.ComplicationModule_ProvidesComplicationCollectionViewModelFactory */
public final class C2094x1653c7a9 implements Factory<ComplicationCollectionViewModel> {
    private final Provider<ViewModelStore> storeProvider;
    private final Provider<ComplicationCollectionViewModel> viewModelProvider;

    public C2094x1653c7a9(Provider<ViewModelStore> provider, Provider<ComplicationCollectionViewModel> provider2) {
        this.storeProvider = provider;
        this.viewModelProvider = provider2;
    }

    public ComplicationCollectionViewModel get() {
        return providesComplicationCollectionViewModel(this.storeProvider.get(), this.viewModelProvider.get());
    }

    public static C2094x1653c7a9 create(Provider<ViewModelStore> provider, Provider<ComplicationCollectionViewModel> provider2) {
        return new C2094x1653c7a9(provider, provider2);
    }

    public static ComplicationCollectionViewModel providesComplicationCollectionViewModel(ViewModelStore viewModelStore, ComplicationCollectionViewModel complicationCollectionViewModel) {
        return (ComplicationCollectionViewModel) Preconditions.checkNotNullFromProvides(ComplicationModule.providesComplicationCollectionViewModel(viewModelStore, complicationCollectionViewModel));
    }
}
