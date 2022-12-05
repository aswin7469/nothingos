package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import java.util.Map;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1 extends Lambda implements Function1<Map.Entry<? extends String, ? extends ConversationNotificationManager.ConversationState>, Pair<? extends String, ? extends NotificationEntry>> {
    final /* synthetic */ ConversationNotificationManager this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1(ConversationNotificationManager conversationNotificationManager) {
        super(1);
        this.this$0 = conversationNotificationManager;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Pair<? extends String, ? extends NotificationEntry> mo1949invoke(Map.Entry<? extends String, ? extends ConversationNotificationManager.ConversationState> entry) {
        return invoke2((Map.Entry<String, ConversationNotificationManager.ConversationState>) entry);
    }

    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Pair<String, NotificationEntry> invoke2(@NotNull Map.Entry<String, ConversationNotificationManager.ConversationState> dstr$key$_u24__u24) {
        NotificationEntryManager notificationEntryManager;
        Intrinsics.checkNotNullParameter(dstr$key$_u24__u24, "$dstr$key$_u24__u24");
        String key = dstr$key$_u24__u24.getKey();
        notificationEntryManager = this.this$0.notificationEntryManager;
        NotificationEntry activeNotificationUnfiltered = notificationEntryManager.getActiveNotificationUnfiltered(key);
        if (activeNotificationUnfiltered == null) {
            return null;
        }
        ExpandableNotificationRow row = activeNotificationUnfiltered.getRow();
        if (!Intrinsics.areEqual(row == null ? null : Boolean.valueOf(row.isExpanded()), Boolean.TRUE)) {
            return null;
        }
        return TuplesKt.to(key, activeNotificationUnfiltered);
    }
}
