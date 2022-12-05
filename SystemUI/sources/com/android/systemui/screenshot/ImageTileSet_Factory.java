package com.android.systemui.screenshot;

import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ImageTileSet_Factory implements Factory<ImageTileSet> {
    private final Provider<Handler> handlerProvider;

    public ImageTileSet_Factory(Provider<Handler> provider) {
        this.handlerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ImageTileSet mo1933get() {
        return newInstance(this.handlerProvider.mo1933get());
    }

    public static ImageTileSet_Factory create(Provider<Handler> provider) {
        return new ImageTileSet_Factory(provider);
    }

    public static ImageTileSet newInstance(Handler handler) {
        return new ImageTileSet(handler);
    }
}
