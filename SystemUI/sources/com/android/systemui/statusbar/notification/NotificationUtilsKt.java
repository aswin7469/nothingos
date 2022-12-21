package com.android.systemui.statusbar.notification;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0012\u0010\u0000\u001a\u0004\u0018\u00010\u00012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001\"\u0019\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u0004\u0018\u00010\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0019\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u0004\u0018\u00010\u00058F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0006¨\u0006\b"}, mo64987d2 = {"logKey", "", "Landroid/service/notification/StatusBarNotification;", "getLogKey", "(Landroid/service/notification/StatusBarNotification;)Ljava/lang/String;", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "(Lcom/android/systemui/statusbar/notification/collection/ListEntry;)Ljava/lang/String;", "key", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationUtils.kt */
public final class NotificationUtilsKt {
    public static final String getLogKey(ListEntry listEntry) {
        if (listEntry != null) {
            return NotificationUtils.logKey(listEntry);
        }
        return null;
    }

    public static final String getLogKey(StatusBarNotification statusBarNotification) {
        String key;
        if (statusBarNotification == null || (key = statusBarNotification.getKey()) == null) {
            return null;
        }
        return NotificationUtils.logKey(key);
    }

    public static final String logKey(String str) {
        return NotificationUtils.logKey(str);
    }
}
