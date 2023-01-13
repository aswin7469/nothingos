package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConversationNotificationManager$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ ConversationNotificationManager f$1;
    public final /* synthetic */ NotificationEntry f$2;

    public /* synthetic */ ConversationNotificationManager$$ExternalSyntheticLambda2(boolean z, ConversationNotificationManager conversationNotificationManager, NotificationEntry notificationEntry) {
        this.f$0 = z;
        this.f$1 = conversationNotificationManager;
        this.f$2 = notificationEntry;
    }

    public final void run() {
        ConversationNotificationManager.m3092onEntryViewBound$lambda3$lambda2(this.f$0, this.f$1, this.f$2);
    }
}
