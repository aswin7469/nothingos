package com.android.systemui.statusbar.notification.row;

import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.view.View;
import com.android.systemui.statusbar.notification.row.NotificationConversationInfo;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationGutsManager$$ExternalSyntheticLambda3 implements NotificationConversationInfo.OnAppSettingsClickListener {
    public final /* synthetic */ NotificationGutsManager f$0;
    public final /* synthetic */ NotificationGuts f$1;
    public final /* synthetic */ StatusBarNotification f$2;
    public final /* synthetic */ ExpandableNotificationRow f$3;

    public /* synthetic */ NotificationGutsManager$$ExternalSyntheticLambda3(NotificationGutsManager notificationGutsManager, NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, ExpandableNotificationRow expandableNotificationRow) {
        this.f$0 = notificationGutsManager;
        this.f$1 = notificationGuts;
        this.f$2 = statusBarNotification;
        this.f$3 = expandableNotificationRow;
    }

    public final void onClick(View view, Intent intent) {
        this.f$0.mo41609x211757a2(this.f$1, this.f$2, this.f$3, view, intent);
    }
}
