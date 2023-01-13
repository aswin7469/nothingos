package com.android.systemui.statusbar.notification;

import android.util.FloatProperty;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0018\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0002¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\u0004H\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/statusbar/notification/NotificationWakeUpCoordinator$mNotificationVisibility$1", "Landroid/util/FloatProperty;", "Lcom/android/systemui/statusbar/notification/NotificationWakeUpCoordinator;", "get", "", "coordinator", "(Lcom/android/systemui/statusbar/notification/NotificationWakeUpCoordinator;)Ljava/lang/Float;", "setValue", "", "value", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationWakeUpCoordinator.kt */
public final class NotificationWakeUpCoordinator$mNotificationVisibility$1 extends FloatProperty<NotificationWakeUpCoordinator> {
    NotificationWakeUpCoordinator$mNotificationVisibility$1() {
        super("notificationVisibility");
    }

    public void setValue(NotificationWakeUpCoordinator notificationWakeUpCoordinator, float f) {
        Intrinsics.checkNotNullParameter(notificationWakeUpCoordinator, "coordinator");
        notificationWakeUpCoordinator.setVisibilityAmount(f);
    }

    public Float get(NotificationWakeUpCoordinator notificationWakeUpCoordinator) {
        Intrinsics.checkNotNullParameter(notificationWakeUpCoordinator, "coordinator");
        return Float.valueOf(notificationWakeUpCoordinator.mLinearVisibilityAmount);
    }
}
