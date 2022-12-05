package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideNotificationPanelLoggerFactory implements Factory<NotificationPanelLogger> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public NotificationPanelLogger mo1933get() {
        return provideNotificationPanelLogger();
    }

    public static NotificationsModule_ProvideNotificationPanelLoggerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotificationPanelLogger provideNotificationPanelLogger() {
        return (NotificationPanelLogger) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotificationPanelLogger());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final NotificationsModule_ProvideNotificationPanelLoggerFactory INSTANCE = new NotificationsModule_ProvideNotificationPanelLoggerFactory();
    }
}
