package com.android.systemui.statusbar.notification.init;

import com.android.systemui.statusbar.NotificationListener;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsControllerStub_Factory implements Factory<NotificationsControllerStub> {
    private final Provider<NotificationListener> notificationListenerProvider;

    public NotificationsControllerStub_Factory(Provider<NotificationListener> provider) {
        this.notificationListenerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NotificationsControllerStub mo1933get() {
        return newInstance(this.notificationListenerProvider.mo1933get());
    }

    public static NotificationsControllerStub_Factory create(Provider<NotificationListener> provider) {
        return new NotificationsControllerStub_Factory(provider);
    }

    public static NotificationsControllerStub newInstance(NotificationListener notificationListener) {
        return new NotificationsControllerStub(notificationListener);
    }
}
