package com.android.systemui.statusbar.notification.people;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PeopleHubDataSourceImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PeopleHubDataSourceImpl f$0;
    public final /* synthetic */ NotificationEntry f$1;

    public /* synthetic */ PeopleHubDataSourceImpl$$ExternalSyntheticLambda2(PeopleHubDataSourceImpl peopleHubDataSourceImpl, NotificationEntry notificationEntry) {
        this.f$0 = peopleHubDataSourceImpl;
        this.f$1 = notificationEntry;
    }

    public final void run() {
        PeopleHubDataSourceImpl.m3131extractPerson$lambda9(this.f$0, this.f$1);
    }
}
