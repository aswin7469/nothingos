package com.android.systemui.statusbar.notification.people;

import android.service.notification.StatusBarNotification;
import org.jetbrains.annotations.NotNull;
/* compiled from: PeopleHubNotificationListener.kt */
/* loaded from: classes.dex */
public interface NotificationPersonExtractor {
    boolean isPersonNotification(@NotNull StatusBarNotification statusBarNotification);
}
