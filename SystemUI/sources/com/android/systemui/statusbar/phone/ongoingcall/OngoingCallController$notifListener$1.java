package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.PendingIntent;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$notifListener$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "onEntryAdded", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onEntryRemoved", "reason", "", "onEntryUpdated", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$notifListener$1 implements NotifCollectionListener {
    final /* synthetic */ OngoingCallController this$0;

    OngoingCallController$notifListener$1(OngoingCallController ongoingCallController) {
        this.this$0 = ongoingCallController;
    }

    public void onEntryAdded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        onEntryUpdated(notificationEntry, true);
    }

    public void onEntryUpdated(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (this.this$0.callNotificationInfo != null || !OngoingCallControllerKt.isCallNotification(notificationEntry)) {
            String key = notificationEntry.getSbn().getKey();
            OngoingCallController.CallNotificationInfo access$getCallNotificationInfo$p = this.this$0.callNotificationInfo;
            if (!Intrinsics.areEqual((Object) key, (Object) access$getCallNotificationInfo$p != null ? access$getCallNotificationInfo$p.getKey() : null)) {
                return;
            }
        }
        String key2 = notificationEntry.getSbn().getKey();
        Intrinsics.checkNotNullExpressionValue(key2, "entry.sbn.key");
        long j = notificationEntry.getSbn().getNotification().when;
        PendingIntent pendingIntent = notificationEntry.getSbn().getNotification().contentIntent;
        int uid = notificationEntry.getSbn().getUid();
        boolean z = notificationEntry.getSbn().getNotification().extras.getInt("android.callType", -1) == 2;
        OngoingCallController.CallNotificationInfo access$getCallNotificationInfo$p2 = this.this$0.callNotificationInfo;
        OngoingCallController.CallNotificationInfo callNotificationInfo = new OngoingCallController.CallNotificationInfo(key2, j, pendingIntent, uid, z, access$getCallNotificationInfo$p2 != null ? access$getCallNotificationInfo$p2.getStatusBarSwipedAway() : false);
        if (!Intrinsics.areEqual((Object) callNotificationInfo, (Object) this.this$0.callNotificationInfo)) {
            this.this$0.callNotificationInfo = callNotificationInfo;
            if (callNotificationInfo.isOngoing()) {
                this.this$0.updateChip();
            } else {
                this.this$0.removeChip();
            }
        }
    }

    public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        String key = notificationEntry.getSbn().getKey();
        OngoingCallController.CallNotificationInfo access$getCallNotificationInfo$p = this.this$0.callNotificationInfo;
        if (Intrinsics.areEqual((Object) key, (Object) access$getCallNotificationInfo$p != null ? access$getCallNotificationInfo$p.getKey() : null)) {
            this.this$0.removeChip();
        }
    }
}
