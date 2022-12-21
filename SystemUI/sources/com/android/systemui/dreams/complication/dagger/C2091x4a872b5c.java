package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationsRestoreTimeoutFactory */
public final class C2091x4a872b5c implements Factory<Integer> {
    private final Provider<Resources> resourcesProvider;

    public C2091x4a872b5c(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public Integer get() {
        return Integer.valueOf(providesComplicationsRestoreTimeout(this.resourcesProvider.get()));
    }

    public static C2091x4a872b5c create(Provider<Resources> provider) {
        return new C2091x4a872b5c(provider);
    }

    public static int providesComplicationsRestoreTimeout(Resources resources) {
        return ComplicationHostViewModule.providesComplicationsRestoreTimeout(resources);
    }
}
