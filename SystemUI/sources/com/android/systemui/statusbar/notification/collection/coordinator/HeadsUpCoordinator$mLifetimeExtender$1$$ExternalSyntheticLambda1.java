package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class HeadsUpCoordinator$mLifetimeExtender$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ HeadsUpCoordinator f$0;
    public final /* synthetic */ NotificationEntry f$1;

    public /* synthetic */ HeadsUpCoordinator$mLifetimeExtender$1$$ExternalSyntheticLambda1(HeadsUpCoordinator headsUpCoordinator, NotificationEntry notificationEntry) {
        this.f$0 = headsUpCoordinator;
        this.f$1 = notificationEntry;
    }

    public final void run() {
        HeadsUpCoordinator$mLifetimeExtender$1.m3109maybeExtendLifetime$lambda1(this.f$0, this.f$1);
    }
}
