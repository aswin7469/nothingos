package com.android.systemui.screenshot;

import android.content.ContentResolver;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ImageLoader_Factory implements Factory<ImageLoader> {
    private final Provider<ContentResolver> resolverProvider;

    public ImageLoader_Factory(Provider<ContentResolver> provider) {
        this.resolverProvider = provider;
    }

    public ImageLoader get() {
        return newInstance(this.resolverProvider.get());
    }

    public static ImageLoader_Factory create(Provider<ContentResolver> provider) {
        return new ImageLoader_Factory(provider);
    }

    public static ImageLoader newInstance(ContentResolver contentResolver) {
        return new ImageLoader(contentResolver);
    }
}
