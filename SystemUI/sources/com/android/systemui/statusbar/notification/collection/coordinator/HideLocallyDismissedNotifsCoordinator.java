package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import javax.inject.Inject;

@CoordinatorScope
public class HideLocallyDismissedNotifsCoordinator implements Coordinator {
    private final NotifFilter mFilter = new NotifFilter("HideLocallyDismissedNotifsFilter") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return notificationEntry.getDismissState() != NotificationEntry.DismissState.NOT_DISMISSED;
        }
    };

    @Inject
    HideLocallyDismissedNotifsCoordinator() {
    }

    public void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.mFilter);
    }
}
