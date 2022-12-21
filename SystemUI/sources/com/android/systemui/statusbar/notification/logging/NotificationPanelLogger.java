package com.android.systemui.statusbar.notification.logging;

import android.service.notification.StatusBarNotification;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.logging.nano.Notifications;
import java.util.List;

public interface NotificationPanelLogger {
    static int toNotificationSection(int i) {
        switch (i) {
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
                return 6;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            default:
                return 0;
        }
    }

    void logPanelShown(boolean z, List<NotificationEntry> list);

    public enum NotificationPanelEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_PANEL_OPEN_STATUS_BAR(200),
        NOTIFICATION_PANEL_OPEN_LOCKSCREEN(201);
        
        private final int mId;

        private NotificationPanelEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        public static NotificationPanelEvent fromLockscreen(boolean z) {
            if (z) {
                return NOTIFICATION_PANEL_OPEN_LOCKSCREEN;
            }
            return NOTIFICATION_PANEL_OPEN_STATUS_BAR;
        }
    }

    static Notifications.NotificationList toNotificationProto(List<NotificationEntry> list) {
        Notifications.NotificationList notificationList = new Notifications.NotificationList();
        if (list == null) {
            return notificationList;
        }
        Notifications.Notification[] notificationArr = new Notifications.Notification[list.size()];
        int i = 0;
        for (NotificationEntry next : list) {
            StatusBarNotification sbn = next.getSbn();
            if (sbn != null) {
                Notifications.Notification notification = new Notifications.Notification();
                notification.uid = sbn.getUid();
                notification.packageName = sbn.getPackageName();
                if (sbn.getInstanceId() != null) {
                    notification.instanceId = sbn.getInstanceId().getId();
                }
                if (sbn.getNotification() != null) {
                    notification.isGroupSummary = sbn.getNotification().isGroupSummary();
                }
                notification.section = toNotificationSection(next.getBucket());
                notificationArr[i] = notification;
            }
            i++;
        }
        notificationList.notifications = notificationArr;
        return notificationList;
    }
}
