package com.android.systemui.statusbar.notification;

import android.app.Notification;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.BiFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConversationNotificationManager$$ExternalSyntheticLambda5 implements BiFunction {
    public final /* synthetic */ NotificationEntry f$0;
    public final /* synthetic */ ConversationNotificationManager f$1;
    public final /* synthetic */ Notification.Builder f$2;

    public /* synthetic */ ConversationNotificationManager$$ExternalSyntheticLambda5(NotificationEntry notificationEntry, ConversationNotificationManager conversationNotificationManager, Notification.Builder builder) {
        this.f$0 = notificationEntry;
        this.f$1 = conversationNotificationManager;
        this.f$2 = builder;
    }

    public final Object apply(Object obj, Object obj2) {
        return ConversationNotificationManager.m3086getUnreadCount$lambda5(this.f$0, this.f$1, this.f$2, (String) obj, (ConversationNotificationManager.ConversationState) obj2);
    }
}
