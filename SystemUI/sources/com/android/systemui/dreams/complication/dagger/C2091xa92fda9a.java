package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationsFadeInDurationFactory */
public final class C2091xa92fda9a implements Factory<Integer> {
    private final Provider<Resources> resourcesProvider;

    public C2091xa92fda9a(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesComplicationsFadeInDuration(this.resourcesProvider.get()));
    }

    public static C2091xa92fda9a create(Provider<Resources> provider) {
        return new C2091xa92fda9a(provider);
    }

    public static int providesComplicationsFadeInDuration(Resources resources) {
        return ComplicationHostViewModule.providesComplicationsFadeInDuration(resources);
    }
}
