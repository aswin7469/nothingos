package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "it", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "invoke", "(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator$onBeforeFinalizeFilter$1$logicalMembersByGroup$1 */
/* compiled from: HeadsUpCoordinator.kt */
final class C2681xa0ab4184 extends Lambda implements Function1<NotificationEntry, Boolean> {
    final /* synthetic */ Map<String, List<HeadsUpCoordinator.PostedEntry>> $postedEntriesByGroup;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2681xa0ab4184(Map<String, ? extends List<HeadsUpCoordinator.PostedEntry>> map) {
        super(1);
        this.$postedEntriesByGroup = map;
    }

    public final Boolean invoke(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "it");
        return Boolean.valueOf(this.$postedEntriesByGroup.containsKey(notificationEntry.getSbn().getGroupKey()));
    }
}
