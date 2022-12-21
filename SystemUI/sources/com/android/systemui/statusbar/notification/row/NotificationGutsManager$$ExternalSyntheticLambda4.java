package com.android.systemui.statusbar.notification.row;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.row.NotificationConversationInfo;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationGutsManager$$ExternalSyntheticLambda4 implements NotificationConversationInfo.OnConversationSettingsClickListener {
    public final /* synthetic */ NotificationGutsManager f$0;
    public final /* synthetic */ StatusBarNotification f$1;
    public final /* synthetic */ ExpandableNotificationRow f$2;

    public /* synthetic */ NotificationGutsManager$$ExternalSyntheticLambda4(NotificationGutsManager notificationGutsManager, StatusBarNotification statusBarNotification, ExpandableNotificationRow expandableNotificationRow) {
        this.f$0 = notificationGutsManager;
        this.f$1 = statusBarNotification;
        this.f$2 = expandableNotificationRow;
    }

    public final void onClick() {
        this.f$0.mo41602xe8233ea3(this.f$1, this.f$2);
    }
}
