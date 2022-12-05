package com.android.systemui.dagger;

import android.app.IWallpaperManager;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIWallPaperManagerFactory implements Factory<IWallpaperManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public IWallpaperManager mo1933get() {
        return provideIWallPaperManager();
    }

    public static FrameworkServicesModule_ProvideIWallPaperManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IWallpaperManager provideIWallPaperManager() {
        return FrameworkServicesModule.provideIWallPaperManager();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIWallPaperManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIWallPaperManagerFactory();
    }
}
