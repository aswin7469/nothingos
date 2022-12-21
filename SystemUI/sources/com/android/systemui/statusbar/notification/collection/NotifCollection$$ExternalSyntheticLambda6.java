package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda6 implements NotifDismissInterceptor.OnEndDismissInterception {
    public final /* synthetic */ NotifCollection f$0;

    public /* synthetic */ NotifCollection$$ExternalSyntheticLambda6(NotifCollection notifCollection) {
        this.f$0 = notifCollection;
    }

    public final void onEndDismissInterception(NotifDismissInterceptor notifDismissInterceptor, NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats) {
        this.f$0.onEndDismissInterception(notifDismissInterceptor, notificationEntry, dismissedByUserStats);
    }
}
