package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationsFadeOutDurationFactory */
public final class C2092x3242d31f implements Factory<Integer> {
    private final Provider<Resources> resourcesProvider;

    public C2092x3242d31f(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesComplicationsFadeOutDuration(this.resourcesProvider.get()));
    }

    public static C2092x3242d31f create(Provider<Resources> provider) {
        return new C2092x3242d31f(provider);
    }

    public static int providesComplicationsFadeOutDuration(Resources resources) {
        return ComplicationHostViewModule.providesComplicationsFadeOutDuration(resources);
    }
}
