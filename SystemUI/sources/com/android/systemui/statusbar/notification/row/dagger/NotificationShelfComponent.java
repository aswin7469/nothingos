package com.android.systemui.statusbar.notification.row.dagger;

import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Module;
import dagger.Subcomponent;

@NotificationRowScope
@Subcomponent(modules = {ActivatableNotificationViewModule.class, NotificationShelfModule.class})
public interface NotificationShelfComponent {

    @Subcomponent.Builder
    public interface Builder {
        NotificationShelfComponent build();

        @BindsInstance
        Builder notificationShelf(NotificationShelf notificationShelf);
    }

    @Module
    public static abstract class NotificationShelfModule {
        /* access modifiers changed from: package-private */
        @Binds
        public abstract ActivatableNotificationView bindNotificationShelf(NotificationShelf notificationShelf);
    }

    @NotificationRowScope
    NotificationShelfController getNotificationShelfController();
}
