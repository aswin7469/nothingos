package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/statusbar/notification/AnimatedImageNotificationManager$bind$1", "Lcom/android/systemui/statusbar/policy/OnHeadsUpChangedListener;", "onHeadsUpStateChanged", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "isHeadsUp", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConversationNotifications.kt */
public final class AnimatedImageNotificationManager$bind$1 implements OnHeadsUpChangedListener {
    final /* synthetic */ AnimatedImageNotificationManager this$0;

    AnimatedImageNotificationManager$bind$1(AnimatedImageNotificationManager animatedImageNotificationManager) {
        this.this$0 = animatedImageNotificationManager;
    }

    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Unit unused = this.this$0.updateAnimatedImageDrawables(notificationEntry);
    }
}
