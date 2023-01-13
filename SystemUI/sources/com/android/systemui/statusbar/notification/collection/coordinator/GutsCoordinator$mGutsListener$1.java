package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, mo65043d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/GutsCoordinator$mGutsListener$1", "Lcom/android/systemui/statusbar/notification/collection/render/NotifGutsViewListener;", "onGutsClose", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onGutsOpen", "guts", "Lcom/android/systemui/statusbar/notification/row/NotificationGuts;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: GutsCoordinator.kt */
public final class GutsCoordinator$mGutsListener$1 implements NotifGutsViewListener {
    final /* synthetic */ GutsCoordinator this$0;

    GutsCoordinator$mGutsListener$1(GutsCoordinator gutsCoordinator) {
        this.this$0 = gutsCoordinator;
    }

    public void onGutsOpen(NotificationEntry notificationEntry, NotificationGuts notificationGuts) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(notificationGuts, "guts");
        GutsCoordinatorLogger access$getLogger$p = this.this$0.logger;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        access$getLogger$p.logGutsOpened(key, notificationGuts);
        if (notificationGuts.isLeavebehind()) {
            this.this$0.closeGutsAndEndLifetimeExtension(notificationEntry);
        } else {
            this.this$0.notifsWithOpenGuts.add(notificationEntry.getKey());
        }
    }

    public void onGutsClose(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        GutsCoordinatorLogger access$getLogger$p = this.this$0.logger;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        access$getLogger$p.logGutsClosed(key);
        this.this$0.closeGutsAndEndLifetimeExtension(notificationEntry);
    }
}
