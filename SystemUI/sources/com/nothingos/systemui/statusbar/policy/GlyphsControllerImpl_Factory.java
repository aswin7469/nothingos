package com.nothingos.systemui.statusbar.policy;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GlyphsControllerImpl_Factory implements Factory<GlyphsControllerImpl> {
    private final Provider<Context> contextProvider;

    public GlyphsControllerImpl_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public GlyphsControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static GlyphsControllerImpl_Factory create(Provider<Context> provider) {
        return new GlyphsControllerImpl_Factory(provider);
    }

    public static GlyphsControllerImpl newInstance(Context context) {
        return new GlyphsControllerImpl(context);
    }
}
