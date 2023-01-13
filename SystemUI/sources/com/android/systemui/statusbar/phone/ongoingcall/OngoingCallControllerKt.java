package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.Notification;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"DEBUG", "", "TAG", "", "isCallNotification", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: OngoingCallController.kt */
public final class OngoingCallControllerKt {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "OngoingCallController";

    /* access modifiers changed from: private */
    public static final boolean isCallNotification(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().isStyle(Notification.CallStyle.class);
    }
}
