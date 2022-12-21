package com.android.systemui.statusbar.notification.row.dagger;

import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineView;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import dagger.Binds;
import dagger.Module;

@Module
public interface ActivatableNotificationViewModule {
    @Binds
    ExpandableOutlineView bindExpandableOutlineView(ActivatableNotificationView activatableNotificationView);

    @Binds
    ExpandableView bindExpandableView(ActivatableNotificationView activatableNotificationView);
}
