package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$onNotificationPanelExpandStateChanged$2 extends Lambda implements Function1<NotificationEntry, ExpandableNotificationRow> {
    public static final ConversationNotificationManager$onNotificationPanelExpandStateChanged$2 INSTANCE = new ConversationNotificationManager$onNotificationPanelExpandStateChanged$2();

    ConversationNotificationManager$onNotificationPanelExpandStateChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final ExpandableNotificationRow mo1949invoke(@NotNull NotificationEntry it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getRow();
    }
}
