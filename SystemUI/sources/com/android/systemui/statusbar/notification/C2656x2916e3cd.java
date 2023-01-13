package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "it", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.ConversationNotificationManager$updateNotificationRanking$activeConversationEntries$1 */
/* compiled from: ConversationNotifications.kt */
final class C2656x2916e3cd extends Lambda implements Function1<String, NotificationEntry> {
    final /* synthetic */ ConversationNotificationManager this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2656x2916e3cd(ConversationNotificationManager conversationNotificationManager) {
        super(1);
        this.this$0 = conversationNotificationManager;
    }

    public final NotificationEntry invoke(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        return this.this$0.notifCollection.getEntry(str);
    }
}
