package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationGutsManager$$ExternalSyntheticLambda8 implements NotificationGuts.OnGutsClosedListener {
    public final /* synthetic */ NotificationGutsManager f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;
    public final /* synthetic */ NotificationEntry f$2;

    public /* synthetic */ NotificationGutsManager$$ExternalSyntheticLambda8(NotificationGutsManager notificationGutsManager, ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry) {
        this.f$0 = notificationGutsManager;
        this.f$1 = expandableNotificationRow;
        this.f$2 = notificationEntry;
    }

    public final void onGutsClosed(NotificationGuts notificationGuts) {
        this.f$0.mo41608x8a83fb1b(this.f$1, this.f$2, notificationGuts);
    }
}
