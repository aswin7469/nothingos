package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.render.NotifViewController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PreparationCoordinator$$ExternalSyntheticLambda2 implements NotifInflater.InflationCallback {
    public final /* synthetic */ PreparationCoordinator f$0;

    public /* synthetic */ PreparationCoordinator$$ExternalSyntheticLambda2(PreparationCoordinator preparationCoordinator) {
        this.f$0 = preparationCoordinator;
    }

    public final void onInflationFinished(NotificationEntry notificationEntry, NotifViewController notifViewController) {
        this.f$0.onInflationFinished(notificationEntry, notifViewController);
    }
}
