package com.android.systemui.dreams.touch.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BouncerSwipeModule_ProvidesSwipeToBouncerStartRegionFactory implements Factory<Float> {
    private final Provider<Resources> resourcesProvider;

    public BouncerSwipeModule_ProvidesSwipeToBouncerStartRegionFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Float get() {
        return Float.valueOf(providesSwipeToBouncerStartRegion(this.resourcesProvider.get()));
    }

    public static BouncerSwipeModule_ProvidesSwipeToBouncerStartRegionFactory create(Provider<Resources> provider) {
        return new BouncerSwipeModule_ProvidesSwipeToBouncerStartRegionFactory(provider);
    }

    public static float providesSwipeToBouncerStartRegion(Resources resources) {
        return BouncerSwipeModule.providesSwipeToBouncerStartRegion(resources);
    }
}
