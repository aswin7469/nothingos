package com.android.systemui.statusbar.notification;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/statusbar/notification/AnimatedImageNotificationManager$bind$2", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController$StateListener;", "onExpandedChanged", "", "isExpanded", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ConversationNotifications.kt */
public final class AnimatedImageNotificationManager$bind$2 implements StatusBarStateController.StateListener {
    final /* synthetic */ AnimatedImageNotificationManager this$0;

    AnimatedImageNotificationManager$bind$2(AnimatedImageNotificationManager animatedImageNotificationManager) {
        this.this$0 = animatedImageNotificationManager;
    }

    public void onExpandedChanged(boolean z) {
        this.this$0.isStatusBarExpanded = z;
        Collection<NotificationEntry> allNotifs = this.this$0.notifCollection.getAllNotifs();
        Intrinsics.checkNotNullExpressionValue(allNotifs, "notifCollection.allNotifs");
        AnimatedImageNotificationManager animatedImageNotificationManager = this.this$0;
        for (NotificationEntry access$updateAnimatedImageDrawables : allNotifs) {
            Unit unused = animatedImageNotificationManager.updateAnimatedImageDrawables(access$updateAnimatedImageDrawables);
        }
    }
}
