package com.android.systemui.dreams;

import android.content.Context;
import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamOverlayRegistrant_Factory implements Factory<DreamOverlayRegistrant> {
    private final Provider<Context> contextProvider;
    private final Provider<Resources> resourcesProvider;

    public DreamOverlayRegistrant_Factory(Provider<Context> provider, Provider<Resources> provider2) {
        this.contextProvider = provider;
        this.resourcesProvider = provider2;
    }

    public DreamOverlayRegistrant get() {
        return newInstance(this.contextProvider.get(), this.resourcesProvider.get());
    }

    public static DreamOverlayRegistrant_Factory create(Provider<Context> provider, Provider<Resources> provider2) {
        return new DreamOverlayRegistrant_Factory(provider, provider2);
    }

    public static DreamOverlayRegistrant newInstance(Context context, Resources resources) {
        return new DreamOverlayRegistrant(context, resources);
    }
}
