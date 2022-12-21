package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class StatusBarNotificationPresenterModule {
    /* access modifiers changed from: package-private */
    @Binds
    public abstract NotificationRowBinderImpl.BindRowCallback bindBindRowCallback(StatusBarNotificationPresenter statusBarNotificationPresenter);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract NotificationPresenter bindPresenter(StatusBarNotificationPresenter statusBarNotificationPresenter);
}
