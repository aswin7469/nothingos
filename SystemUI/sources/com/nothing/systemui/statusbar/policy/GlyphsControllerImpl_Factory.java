package com.nothing.systemui.statusbar.policy;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GlyphsControllerImpl_Factory implements Factory<GlyphsControllerImpl> {
    private final Provider<Context> contextProvider;

    public GlyphsControllerImpl_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public GlyphsControllerImpl get() {
        return newInstance(this.contextProvider.get());
    }

    public static GlyphsControllerImpl_Factory create(Provider<Context> provider) {
        return new GlyphsControllerImpl_Factory(provider);
    }

    public static GlyphsControllerImpl newInstance(Context context) {
        return new GlyphsControllerImpl(context);
    }
}
