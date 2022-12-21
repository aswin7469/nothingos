package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "it", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.ConversationNotificationManager$onNotificationPanelExpandStateChanged$2 */
/* compiled from: ConversationNotifications.kt */
final class C2648x5e24d3c0 extends Lambda implements Function1<NotificationEntry, ExpandableNotificationRow> {
    public static final C2648x5e24d3c0 INSTANCE = new C2648x5e24d3c0();

    C2648x5e24d3c0() {
        super(1);
    }

    public final ExpandableNotificationRow invoke(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "it");
        return notificationEntry.getRow();
    }
}
