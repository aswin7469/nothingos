package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator$filter$1", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifFilter;", "shouldFilterOut", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "now", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SmartspaceDedupingCoordinator.kt */
public final class SmartspaceDedupingCoordinator$filter$1 extends NotifFilter {
    final /* synthetic */ SmartspaceDedupingCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SmartspaceDedupingCoordinator$filter$1(SmartspaceDedupingCoordinator smartspaceDedupingCoordinator) {
        super("SmartspaceDedupingFilter");
        this.this$0 = smartspaceDedupingCoordinator;
    }

    public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return this.this$0.isOnLockscreen && this.this$0.isDupedWithSmartspaceContent(notificationEntry);
    }
}
