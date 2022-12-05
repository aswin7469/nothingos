package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public final class NotificationLaunchAnimatorControllerProvider {
    @NotNull
    private final HeadsUpManagerPhone headsUpManager;
    @NotNull
    private final NotificationListContainer notificationListContainer;
    @NotNull
    private final NotificationShadeWindowViewController notificationShadeWindowViewController;

    public NotificationLaunchAnimatorControllerProvider(@NotNull NotificationShadeWindowViewController notificationShadeWindowViewController, @NotNull NotificationListContainer notificationListContainer, @NotNull HeadsUpManagerPhone headsUpManager) {
        Intrinsics.checkNotNullParameter(notificationShadeWindowViewController, "notificationShadeWindowViewController");
        Intrinsics.checkNotNullParameter(notificationListContainer, "notificationListContainer");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        this.notificationShadeWindowViewController = notificationShadeWindowViewController;
        this.notificationListContainer = notificationListContainer;
        this.headsUpManager = headsUpManager;
    }

    @NotNull
    public final NotificationLaunchAnimatorController getAnimatorController(@NotNull ExpandableNotificationRow notification) {
        Intrinsics.checkNotNullParameter(notification, "notification");
        return new NotificationLaunchAnimatorController(this.notificationShadeWindowViewController, this.notificationListContainer, this.headsUpManager, notification);
    }
}
