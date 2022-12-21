package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\n"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator$collectionListener$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "onEntryAdded", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onEntryRemoved", "reason", "", "onEntryUpdated", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SmartspaceDedupingCoordinator.kt */
public final class SmartspaceDedupingCoordinator$collectionListener$1 implements NotifCollectionListener {
    final /* synthetic */ SmartspaceDedupingCoordinator this$0;

    SmartspaceDedupingCoordinator$collectionListener$1(SmartspaceDedupingCoordinator smartspaceDedupingCoordinator) {
        this.this$0 = smartspaceDedupingCoordinator;
    }

    public void onEntryAdded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) this.this$0.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget != null) {
            boolean unused = this.this$0.updateFilterStatus(trackedSmartspaceTarget);
        }
    }

    public void onEntryUpdated(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) this.this$0.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget != null) {
            boolean unused = this.this$0.updateFilterStatus(trackedSmartspaceTarget);
        }
    }

    public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) this.this$0.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget != null) {
            this.this$0.cancelExceptionTimeout(trackedSmartspaceTarget);
        }
    }
}
