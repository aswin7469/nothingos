package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.NotificationChannel;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator$notificationPromoter$1", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifPromoter;", "shouldPromoteToTopLevel", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConversationCoordinator.kt */
public final class ConversationCoordinator$notificationPromoter$1 extends NotifPromoter {
    final /* synthetic */ ConversationCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ConversationCoordinator$notificationPromoter$1(ConversationCoordinator conversationCoordinator) {
        super("ConversationCoordinator");
        this.this$0 = conversationCoordinator;
    }

    public boolean shouldPromoteToTopLevel(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        NotificationChannel channel = notificationEntry.getChannel();
        boolean z = false;
        if (channel != null && channel.isImportantConversation()) {
            z = true;
        }
        if (z) {
            GroupEntry parent = notificationEntry.getParent();
            NotificationEntry summary = parent != null ? parent.getSummary() : null;
            if (summary != null && Intrinsics.areEqual((Object) notificationEntry.getChannel(), (Object) summary.getChannel())) {
                this.this$0.promotedEntriesToSummaryOfSameChannel.put(notificationEntry, summary);
            }
        }
        return z;
    }
}
