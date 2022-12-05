package com.android.systemui.statusbar.phone;

import android.app.IWallpaperManager;
import android.app.WallpaperManager;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.FaceAuthScreenBrightnessController;
import com.android.systemui.statusbar.NotificationMediaManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LockscreenWallpaper_Factory implements Factory<LockscreenWallpaper> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Optional<FaceAuthScreenBrightnessController>> faceAuthScreenBrightnessControllerProvider;
    private final Provider<IWallpaperManager> iWallpaperManagerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<NotificationMediaManager> mediaManagerProvider;
    private final Provider<WallpaperManager> wallpaperManagerProvider;

    public LockscreenWallpaper_Factory(Provider<WallpaperManager> provider, Provider<IWallpaperManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<DumpManager> provider4, Provider<NotificationMediaManager> provider5, Provider<Optional<FaceAuthScreenBrightnessController>> provider6, Provider<Handler> provider7) {
        this.wallpaperManagerProvider = provider;
        this.iWallpaperManagerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.mediaManagerProvider = provider5;
        this.faceAuthScreenBrightnessControllerProvider = provider6;
        this.mainHandlerProvider = provider7;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LockscreenWallpaper mo1933get() {
        return newInstance(this.wallpaperManagerProvider.mo1933get(), this.iWallpaperManagerProvider.mo1933get(), this.keyguardUpdateMonitorProvider.mo1933get(), this.dumpManagerProvider.mo1933get(), this.mediaManagerProvider.mo1933get(), this.faceAuthScreenBrightnessControllerProvider.mo1933get(), this.mainHandlerProvider.mo1933get());
    }

    public static LockscreenWallpaper_Factory create(Provider<WallpaperManager> provider, Provider<IWallpaperManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<DumpManager> provider4, Provider<NotificationMediaManager> provider5, Provider<Optional<FaceAuthScreenBrightnessController>> provider6, Provider<Handler> provider7) {
        return new LockscreenWallpaper_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static LockscreenWallpaper newInstance(WallpaperManager wallpaperManager, IWallpaperManager iWallpaperManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, NotificationMediaManager notificationMediaManager, Optional<FaceAuthScreenBrightnessController> optional, Handler handler) {
        return new LockscreenWallpaper(wallpaperManager, iWallpaperManager, keyguardUpdateMonitor, dumpManager, notificationMediaManager, optional, handler);
    }
}
