package com.nothing.systemui.people.widget;

import android.service.notification.StatusBarNotification;
import com.android.systemui.people.PeopleSpaceUtils;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PeopleSpaceWidgetManagerEx$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PeopleSpaceWidgetManagerEx f$0;
    public final /* synthetic */ StatusBarNotification f$1;
    public final /* synthetic */ PeopleSpaceUtils.NotificationAction f$2;

    public /* synthetic */ PeopleSpaceWidgetManagerEx$$ExternalSyntheticLambda0(PeopleSpaceWidgetManagerEx peopleSpaceWidgetManagerEx, StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        this.f$0 = peopleSpaceWidgetManagerEx;
        this.f$1 = statusBarNotification;
        this.f$2 = notificationAction;
    }

    public final void run() {
        PeopleSpaceWidgetManagerEx.m3507updateWidgetsWithNotificationChangedDelay$lambda0(this.f$0, this.f$1, this.f$2);
    }
}
