package com.android.systemui.people.widget;

import android.service.notification.StatusBarNotification;
import com.android.systemui.people.PeopleSpaceUtils;
import java.util.Collection;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ PeopleSpaceWidgetManager f$0;
    public final /* synthetic */ StatusBarNotification f$1;
    public final /* synthetic */ PeopleSpaceUtils.NotificationAction f$2;
    public final /* synthetic */ Collection f$3;

    public /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda5(PeopleSpaceWidgetManager peopleSpaceWidgetManager, StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction, Collection collection) {
        this.f$0 = peopleSpaceWidgetManager;
        this.f$1 = statusBarNotification;
        this.f$2 = notificationAction;
        this.f$3 = collection;
    }

    public final void run() {
        this.f$0.mo35185xc690c0c0(this.f$1, this.f$2, this.f$3);
    }
}
