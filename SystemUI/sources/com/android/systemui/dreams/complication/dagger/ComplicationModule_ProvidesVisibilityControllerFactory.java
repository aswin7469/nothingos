package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ComplicationModule_ProvidesVisibilityControllerFactory implements Factory<Complication.VisibilityController> {
    private final Provider<ComplicationLayoutEngine> engineProvider;

    public ComplicationModule_ProvidesVisibilityControllerFactory(Provider<ComplicationLayoutEngine> provider) {
        this.engineProvider = provider;
    }

    public Complication.VisibilityController get() {
        return providesVisibilityController(this.engineProvider.get());
    }

    public static ComplicationModule_ProvidesVisibilityControllerFactory create(Provider<ComplicationLayoutEngine> provider) {
        return new ComplicationModule_ProvidesVisibilityControllerFactory(provider);
    }

    public static Complication.VisibilityController providesVisibilityController(ComplicationLayoutEngine complicationLayoutEngine) {
        return (Complication.VisibilityController) Preconditions.checkNotNullFromProvides(ComplicationModule.providesVisibilityController(complicationLayoutEngine));
    }
}
