package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\u000b"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator$mCollectionListener$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "onEntryRemoved", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "reason", "", "onEntryUpdated", "fromSystem", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RemoteInputCoordinator.kt */
public final class RemoteInputCoordinator$mCollectionListener$1 implements NotifCollectionListener {
    final /* synthetic */ RemoteInputCoordinator this$0;

    RemoteInputCoordinator$mCollectionListener$1(RemoteInputCoordinator remoteInputCoordinator) {
        this.this$0 = remoteInputCoordinator;
    }

    public void onEntryUpdated(NotificationEntry notificationEntry, boolean z) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "mCollectionListener.onEntryUpdated(entry=" + notificationEntry.getKey() + ", fromSystem=" + z + ')');
        }
        if (z) {
            this.this$0.mSmartReplyController.stopSending(notificationEntry);
        }
    }

    public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "mCollectionListener.onEntryRemoved(entry=" + notificationEntry.getKey() + ')');
        }
        this.this$0.mSmartReplyController.stopSending(notificationEntry);
        if (i == 1 || i == 2) {
            this.this$0.mNotificationRemoteInputManager.cleanUpRemoteInputForUserRemoval(notificationEntry);
        }
    }
}
