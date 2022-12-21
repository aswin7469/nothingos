package com.android.systemui.dagger;

import android.app.Service;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.SystemUIService;
import com.android.systemui.doze.DozeService;
import com.android.systemui.dreams.DreamOverlayService;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.screenrecord.RecordingService;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class DefaultServiceBinder {
    @IntoMap
    @ClassKey(DozeService.class)
    @Binds
    public abstract Service bindDozeService(DozeService dozeService);

    @IntoMap
    @ClassKey(DreamOverlayService.class)
    @Binds
    public abstract Service bindDreamOverlayService(DreamOverlayService dreamOverlayService);

    @IntoMap
    @ClassKey(ImageWallpaper.class)
    @Binds
    public abstract Service bindImageWallpaper(ImageWallpaper imageWallpaper);

    @IntoMap
    @ClassKey(KeyguardService.class)
    @Binds
    public abstract Service bindKeyguardService(KeyguardService keyguardService);

    @IntoMap
    @ClassKey(NotificationListenerWithPlugins.class)
    @Binds
    public abstract Service bindNotificationListenerWithPlugins(NotificationListenerWithPlugins notificationListenerWithPlugins);

    @IntoMap
    @ClassKey(RecordingService.class)
    @Binds
    public abstract Service bindRecordingService(RecordingService recordingService);

    @IntoMap
    @ClassKey(SystemUIAuxiliaryDumpService.class)
    @Binds
    public abstract Service bindSystemUIAuxiliaryDumpService(SystemUIAuxiliaryDumpService systemUIAuxiliaryDumpService);

    @IntoMap
    @ClassKey(SystemUIService.class)
    @Binds
    public abstract Service bindSystemUIService(SystemUIService systemUIService);
}
