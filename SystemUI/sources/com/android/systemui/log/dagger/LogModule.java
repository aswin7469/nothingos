package com.android.systemui.log.dagger;

import android.content.ContentResolver;
import android.os.Build;
import android.os.Looper;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.log.LogcatEchoTracker;
import com.android.systemui.log.LogcatEchoTrackerDebug;
import com.android.systemui.log.LogcatEchoTrackerProd;
import dagger.Module;
import dagger.Provides;

@Module
public class LogModule {
    @SysUISingleton
    @DozeLog
    @Provides
    public static LogBuffer provideDozeLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("DozeLog", 100);
    }

    @SysUISingleton
    @NotificationLog
    @Provides
    public static LogBuffer provideNotificationsLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifLog", 1000, false);
    }

    @SysUISingleton
    @NotificationHeadsUpLog
    @Provides
    public static LogBuffer provideNotificationHeadsUpLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifHeadsUpLog", 1000);
    }

    @SysUISingleton
    @LSShadeTransitionLog
    @Provides
    public static LogBuffer provideLSShadeTransitionControllerBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("LSShadeTransitionLog", 50);
    }

    @SysUISingleton
    @NotificationSectionLog
    @Provides
    public static LogBuffer provideNotificationSectionLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifSectionLog", 1000, false);
    }

    @SysUISingleton
    @NotifInteractionLog
    @Provides
    public static LogBuffer provideNotifInteractionLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifInteractionLog", 50);
    }

    @SysUISingleton
    @QSLog
    @Provides
    public static LogBuffer provideQuickSettingsLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("QSLog", 500, false);
    }

    @SysUISingleton
    @BroadcastDispatcherLog
    @Provides
    public static LogBuffer provideBroadcastDispatcherLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("BroadcastDispatcherLog", 500, false);
    }

    @SysUISingleton
    @ToastLog
    @Provides
    public static LogBuffer provideToastLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("ToastLog", 50);
    }

    @SysUISingleton
    @PrivacyLog
    @Provides
    public static LogBuffer providePrivacyLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("PrivacyLog", 100);
    }

    @SysUISingleton
    @CollapsedSbFragmentLog
    @Provides
    public static LogBuffer provideCollapsedSbFragmentLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("CollapsedSbFragmentLog", 20);
    }

    @SysUISingleton
    @QSFragmentDisableLog
    @Provides
    public static LogBuffer provideQSFragmentDisableLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("QSFragmentDisableFlagsLog", 10, false);
    }

    @SysUISingleton
    @SwipeStatusBarAwayLog
    @Provides
    public static LogBuffer provideSwipeAwayGestureLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("SwipeStatusBarAwayLog", 30);
    }

    @SysUISingleton
    @MediaTttSenderLogBuffer
    @Provides
    public static LogBuffer provideMediaTttSenderLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaTttSender", 20);
    }

    @SysUISingleton
    @MediaTttReceiverLogBuffer
    @Provides
    public static LogBuffer provideMediaTttReceiverLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaTttReceiver", 20);
    }

    @SysUISingleton
    @MediaMuteAwaitLog
    @Provides
    public static LogBuffer provideMediaMuteAwaitLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaMuteAwaitLog", 20);
    }

    @SysUISingleton
    @NearbyMediaDevicesLog
    @Provides
    public static LogBuffer provideNearbyMediaDevicesLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NearbyMediaDevicesLog", 20);
    }

    @SysUISingleton
    @MediaViewLog
    @Provides
    public static LogBuffer provideMediaViewLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaView", 100);
    }

    @SysUISingleton
    @MediaTimeoutListenerLog
    @Provides
    public static LogBuffer providesMediaTimeoutListenerLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaTimeout", 100);
    }

    @SysUISingleton
    @MediaBrowserLog
    @Provides
    public static LogBuffer provideMediaBrowserBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaBrowser", 100);
    }

    @SysUISingleton
    @MediaCarouselControllerLog
    @Provides
    public static LogBuffer provideMediaCarouselControllerBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("MediaCarouselCtlrLog", 20);
    }

    @SysUISingleton
    @Provides
    public static LogcatEchoTracker provideLogcatEchoTracker(ContentResolver contentResolver, @Main Looper looper) {
        if (Build.isDebuggable()) {
            return LogcatEchoTrackerDebug.create(contentResolver, looper);
        }
        return new LogcatEchoTrackerProd();
    }
}
