package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/ConversationNotificationProcessor;", "", "launcherApps", "Landroid/content/pm/LauncherApps;", "conversationNotificationManager", "Lcom/android/systemui/statusbar/notification/ConversationNotificationManager;", "(Landroid/content/pm/LauncherApps;Lcom/android/systemui/statusbar/notification/ConversationNotificationManager;)V", "processNotification", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "recoveredBuilder", "Landroid/app/Notification$Builder;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationProcessor {
    private final ConversationNotificationManager conversationNotificationManager;
    private final LauncherApps launcherApps;

    @Inject
    public ConversationNotificationProcessor(LauncherApps launcherApps2, ConversationNotificationManager conversationNotificationManager2) {
        Intrinsics.checkNotNullParameter(launcherApps2, "launcherApps");
        Intrinsics.checkNotNullParameter(conversationNotificationManager2, "conversationNotificationManager");
        this.launcherApps = launcherApps2;
        this.conversationNotificationManager = conversationNotificationManager2;
    }

    public final void processNotification(NotificationEntry notificationEntry, Notification.Builder builder) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(builder, "recoveredBuilder");
        Notification.Style style = builder.getStyle();
        Notification.MessagingStyle messagingStyle = style instanceof Notification.MessagingStyle ? (Notification.MessagingStyle) style : null;
        if (messagingStyle != null) {
            messagingStyle.setConversationType(notificationEntry.getRanking().getChannel().isImportantConversation() ? 2 : 1);
            ShortcutInfo conversationShortcutInfo = notificationEntry.getRanking().getConversationShortcutInfo();
            if (conversationShortcutInfo != null) {
                messagingStyle.setShortcutIcon(this.launcherApps.getShortcutIcon(conversationShortcutInfo));
                CharSequence label = conversationShortcutInfo.getLabel();
                if (label != null) {
                    Intrinsics.checkNotNullExpressionValue(label, BaseIconCache.IconDB.COLUMN_LABEL);
                    messagingStyle.setConversationTitle(label);
                }
            }
            messagingStyle.setUnreadMessageCount(this.conversationNotificationManager.getUnreadCount(notificationEntry, builder));
        }
    }
}
