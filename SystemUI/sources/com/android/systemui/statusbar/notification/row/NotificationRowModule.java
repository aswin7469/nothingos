package com.android.systemui.statusbar.notification.row;

import com.android.systemui.dagger.SysUISingleton;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class NotificationRowModule {
    @SysUISingleton
    @Binds
    public abstract NotifRemoteViewCache provideNotifRemoteViewCache(NotifRemoteViewCacheImpl notifRemoteViewCacheImpl);

    @SysUISingleton
    @Binds
    public abstract NotificationRowContentBinder provideNotificationRowContentBinder(NotificationContentInflater notificationContentInflater);
}
