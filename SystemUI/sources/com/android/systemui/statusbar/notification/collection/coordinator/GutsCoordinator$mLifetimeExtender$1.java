package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00001\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000eH\u0016¨\u0006\u000f"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/GutsCoordinator$mLifetimeExtender$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender;", "cancelLifetimeExtension", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getName", "", "maybeExtendLifetime", "", "reason", "", "setCallback", "callback", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender$OnEndLifetimeExtensionCallback;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: GutsCoordinator.kt */
public final class GutsCoordinator$mLifetimeExtender$1 implements NotifLifetimeExtender {
    final /* synthetic */ GutsCoordinator this$0;

    public String getName() {
        return "GutsCoordinator";
    }

    GutsCoordinator$mLifetimeExtender$1(GutsCoordinator gutsCoordinator) {
        this.this$0 = gutsCoordinator;
    }

    public void setCallback(NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback) {
        Intrinsics.checkNotNullParameter(onEndLifetimeExtensionCallback, "callback");
        this.this$0.onEndLifetimeExtensionCallback = onEndLifetimeExtensionCallback;
    }

    public boolean maybeExtendLifetime(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        boolean access$isCurrentlyShowingGuts = this.this$0.isCurrentlyShowingGuts(notificationEntry);
        if (access$isCurrentlyShowingGuts) {
            this.this$0.notifsExtendingLifetime.add(notificationEntry.getKey());
        }
        return access$isCurrentlyShowingGuts;
    }

    public void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.this$0.notifsExtendingLifetime.remove(notificationEntry.getKey());
    }
}
