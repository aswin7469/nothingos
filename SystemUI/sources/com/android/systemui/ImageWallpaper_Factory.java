package com.android.systemui;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ImageWallpaper_Factory implements Factory<ImageWallpaper> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public ImageWallpaper mo1933get() {
        return newInstance();
    }

    public static ImageWallpaper_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ImageWallpaper newInstance() {
        return new ImageWallpaper();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final ImageWallpaper_Factory INSTANCE = new ImageWallpaper_Factory();
    }
}
