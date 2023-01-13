package com.android.systemui.statusbar.notification.people;

import android.service.notification.StatusBarNotification;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractor;", "", "extractPerson", "Lcom/android/systemui/statusbar/notification/people/PersonModel;", "sbn", "Landroid/service/notification/StatusBarNotification;", "extractPersonKey", "", "isPersonNotification", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
public interface NotificationPersonExtractor {
    PersonModel extractPerson(StatusBarNotification statusBarNotification);

    String extractPersonKey(StatusBarNotification statusBarNotification);

    boolean isPersonNotification(StatusBarNotification statusBarNotification);
}
