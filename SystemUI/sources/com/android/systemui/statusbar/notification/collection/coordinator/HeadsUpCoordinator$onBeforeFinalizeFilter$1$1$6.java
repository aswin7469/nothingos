package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "it", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$PostedEntry;", "invoke", "(Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$PostedEntry;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
final class HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$6 extends Lambda implements Function1<HeadsUpCoordinator.PostedEntry, Boolean> {
    final /* synthetic */ NotificationEntry $logicalSummary;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$6(NotificationEntry notificationEntry) {
        super(1);
        this.$logicalSummary = notificationEntry;
    }

    public final Boolean invoke(HeadsUpCoordinator.PostedEntry postedEntry) {
        Intrinsics.checkNotNullParameter(postedEntry, "it");
        return Boolean.valueOf(!Intrinsics.areEqual((Object) postedEntry.getKey(), (Object) this.$logicalSummary.getKey()));
    }
}
