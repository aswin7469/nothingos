package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010&\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005H\n¢\u0006\u0002\b\u0007"}, mo65043d2 = {"<anonymous>", "Lkotlin/Pair;", "", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "<name for destructuring parameter 0>", "", "Lcom/android/systemui/statusbar/notification/ConversationNotificationManager$ConversationState;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1 */
/* compiled from: ConversationNotifications.kt */
final class C2655x7388b338 extends Lambda implements Function1<Map.Entry<? extends String, ? extends ConversationNotificationManager.ConversationState>, Pair<? extends String, ? extends NotificationEntry>> {
    final /* synthetic */ ConversationNotificationManager this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2655x7388b338(ConversationNotificationManager conversationNotificationManager) {
        super(1);
        this.this$0 = conversationNotificationManager;
    }

    public final Pair<String, NotificationEntry> invoke(Map.Entry<String, ConversationNotificationManager.ConversationState> entry) {
        Intrinsics.checkNotNullParameter(entry, "<name for destructuring parameter 0>");
        String key = entry.getKey();
        NotificationEntry entry2 = this.this$0.notifCollection.getEntry(key);
        if (entry2 == null) {
            return null;
        }
        ExpandableNotificationRow row = entry2.getRow();
        boolean z = false;
        if (row != null && row.isExpanded()) {
            z = true;
        }
        if (z) {
            return TuplesKt.m1802to(key, entry2);
        }
        return null;
    }
}
