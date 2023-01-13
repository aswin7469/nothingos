package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConversationNotificationManager$$ExternalSyntheticLambda4 implements ExpandableNotificationRow.OnExpansionChangedListener {
    public final /* synthetic */ NotificationEntry f$0;
    public final /* synthetic */ ConversationNotificationManager f$1;

    public /* synthetic */ ConversationNotificationManager$$ExternalSyntheticLambda4(NotificationEntry notificationEntry, ConversationNotificationManager conversationNotificationManager) {
        this.f$0 = notificationEntry;
        this.f$1 = conversationNotificationManager;
    }

    public final void onExpansionChanged(boolean z) {
        ConversationNotificationManager.m3091onEntryViewBound$lambda3(this.f$0, this.f$1, z);
    }
}
