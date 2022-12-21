package com.android.systemui.statusbar.notification.row.dagger;

import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController;
import dagger.BindsInstance;
import dagger.Subcomponent;

@NotificationRowScope
@Subcomponent(modules = {ActivatableNotificationViewModule.class})
public interface NotificationRowComponent {

    @Subcomponent.Builder
    public interface Builder {
        @BindsInstance
        Builder activatableNotificationView(ActivatableNotificationView activatableNotificationView);

        NotificationRowComponent build();
    }

    @NotificationRowScope
    ActivatableNotificationViewController getActivatableNotificationViewController();
}
