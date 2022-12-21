package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class StatusBarNotificationActivityStarterModule {
    /* access modifiers changed from: package-private */
    @Binds
    public abstract NotificationActivityStarter bindActivityStarter(StatusBarNotificationActivityStarter statusBarNotificationActivityStarter);
}
