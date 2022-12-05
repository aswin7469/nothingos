package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationProcessor {
    @NotNull
    private final ConversationNotificationManager conversationNotificationManager;
    @NotNull
    private final LauncherApps launcherApps;

    public ConversationNotificationProcessor(@NotNull LauncherApps launcherApps, @NotNull ConversationNotificationManager conversationNotificationManager) {
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        Intrinsics.checkNotNullParameter(conversationNotificationManager, "conversationNotificationManager");
        this.launcherApps = launcherApps;
        this.conversationNotificationManager = conversationNotificationManager;
    }

    public final void processNotification(@NotNull NotificationEntry entry, @NotNull Notification.Builder recoveredBuilder) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        Intrinsics.checkNotNullParameter(recoveredBuilder, "recoveredBuilder");
        Notification.Style style = recoveredBuilder.getStyle();
        Notification.MessagingStyle messagingStyle = style instanceof Notification.MessagingStyle ? (Notification.MessagingStyle) style : null;
        if (messagingStyle == null) {
            return;
        }
        messagingStyle.setConversationType(entry.getRanking().getChannel().isImportantConversation() ? 2 : 1);
        ShortcutInfo conversationShortcutInfo = entry.getRanking().getConversationShortcutInfo();
        if (conversationShortcutInfo != null) {
            messagingStyle.setShortcutIcon(this.launcherApps.getShortcutIcon(conversationShortcutInfo));
            CharSequence label = conversationShortcutInfo.getLabel();
            if (label != null) {
                messagingStyle.setConversationTitle(label);
            }
        }
        messagingStyle.setUnreadMessageCount(this.conversationNotificationManager.getUnreadCount(entry, recoveredBuilder));
    }
}
