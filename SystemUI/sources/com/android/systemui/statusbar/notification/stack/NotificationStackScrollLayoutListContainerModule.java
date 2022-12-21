package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class NotificationStackScrollLayoutListContainerModule {
    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    static NotificationListContainer provideListContainer(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return notificationStackScrollLayoutController.getNotificationListContainer();
    }
}
