package com.android.systemui.statusbar.notification.collection;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a\f\u0010\u0002\u001a\u00020\u0003*\u00020\u0004H\u0002\u001a\f\u0010\u0005\u001a\u00020\u0003*\u00020\u0004H\u0002\u001a\f\u0010\u0006\u001a\u00020\u0003*\u00020\u0004H\u0002\u001a\f\u0010\u0007\u001a\u00020\u0003*\u00020\bH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\t"}, mo65043d2 = {"TAG", "", "isColorizedForegroundService", "", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "isImportantCall", "isSystemMax", "isSystemNotification", "Landroid/service/notification/StatusBarNotification;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationRankingManager.kt */
public final class NotificationRankingManagerKt {
    private static final String TAG = "NotifRankingManager";

    /* access modifiers changed from: private */
    public static final boolean isSystemMax(NotificationEntry notificationEntry) {
        if (notificationEntry.getImportance() >= 4) {
            StatusBarNotification sbn = notificationEntry.getSbn();
            Intrinsics.checkNotNullExpressionValue(sbn, "sbn");
            if (isSystemNotification(sbn)) {
                return true;
            }
        }
        return false;
    }

    private static final boolean isSystemNotification(StatusBarNotification statusBarNotification) {
        return Intrinsics.areEqual((Object) "android", (Object) statusBarNotification.getPackageName()) || Intrinsics.areEqual((Object) "com.android.systemui", (Object) statusBarNotification.getPackageName());
    }

    /* access modifiers changed from: private */
    public static final boolean isImportantCall(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().isStyle(Notification.CallStyle.class) && notificationEntry.getImportance() > 1;
    }

    /* access modifiers changed from: private */
    public static final boolean isColorizedForegroundService(NotificationEntry notificationEntry) {
        Notification notification = notificationEntry.getSbn().getNotification();
        return notification.isForegroundService() && notification.isColorized() && notificationEntry.getImportance() > 1;
    }
}
