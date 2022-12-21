package com.android.systemui.statusbar.notification.row;

import android.view.View;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationGutsManager$$ExternalSyntheticLambda2 implements ExpandableNotificationRow.LongPressListener {
    public final /* synthetic */ NotificationGutsManager f$0;

    public /* synthetic */ NotificationGutsManager$$ExternalSyntheticLambda2(NotificationGutsManager notificationGutsManager) {
        this.f$0 = notificationGutsManager;
    }

    public final boolean onLongPress(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        return this.f$0.openGuts(view, i, i2, menuItem);
    }
}
