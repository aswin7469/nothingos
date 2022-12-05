package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$1$onNotificationRankingUpdated$activeConversationEntries$1 extends Lambda implements Function1<String, NotificationEntry> {
    final /* synthetic */ ConversationNotificationManager this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$1$onNotificationRankingUpdated$activeConversationEntries$1(ConversationNotificationManager conversationNotificationManager) {
        super(1);
        this.this$0 = conversationNotificationManager;
    }

    @Override // kotlin.jvm.functions.Function1
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final NotificationEntry mo1949invoke(@NotNull String it) {
        NotificationEntryManager notificationEntryManager;
        Intrinsics.checkNotNullParameter(it, "it");
        notificationEntryManager = this.this$0.notificationEntryManager;
        return notificationEntryManager.getActiveNotificationUnfiltered(it);
    }
}
