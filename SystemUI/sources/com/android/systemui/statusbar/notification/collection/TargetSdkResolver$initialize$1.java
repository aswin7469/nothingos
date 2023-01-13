package com.android.systemui.statusbar.notification.collection;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/statusbar/notification/collection/TargetSdkResolver$initialize$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "onEntryBind", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "sbn", "Landroid/service/notification/StatusBarNotification;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TargetSdkResolver.kt */
public final class TargetSdkResolver$initialize$1 implements NotifCollectionListener {
    final /* synthetic */ TargetSdkResolver this$0;

    TargetSdkResolver$initialize$1(TargetSdkResolver targetSdkResolver) {
        this.this$0 = targetSdkResolver;
    }

    public void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        notificationEntry.targetSdk = this.this$0.resolveNotificationSdk(statusBarNotification);
    }
}
