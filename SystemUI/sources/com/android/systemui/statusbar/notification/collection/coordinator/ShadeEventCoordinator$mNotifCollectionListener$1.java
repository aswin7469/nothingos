package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinator$mNotifCollectionListener$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "onEntryRemoved", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "reason", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ShadeEventCoordinator.kt */
public final class ShadeEventCoordinator$mNotifCollectionListener$1 implements NotifCollectionListener {
    final /* synthetic */ ShadeEventCoordinator this$0;

    ShadeEventCoordinator$mNotifCollectionListener$1(ShadeEventCoordinator shadeEventCoordinator) {
        this.this$0 = shadeEventCoordinator;
    }

    public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        boolean z = true;
        this.this$0.mEntryRemoved = true;
        ShadeEventCoordinator shadeEventCoordinator = this.this$0;
        if (!(i == 1 || i == 3 || i == 2)) {
            z = false;
        }
        shadeEventCoordinator.mEntryRemovedByUser = z;
    }
}
