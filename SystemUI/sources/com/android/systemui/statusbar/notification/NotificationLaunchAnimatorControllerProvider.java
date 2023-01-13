package com.android.systemui.statusbar.notification;

import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0007R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/NotificationLaunchAnimatorControllerProvider;", "", "notificationShadeWindowViewController", "Lcom/android/systemui/statusbar/phone/NotificationShadeWindowViewController;", "notificationListContainer", "Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;", "headsUpManager", "Lcom/android/systemui/statusbar/phone/HeadsUpManagerPhone;", "jankMonitor", "Lcom/android/internal/jank/InteractionJankMonitor;", "(Lcom/android/systemui/statusbar/phone/NotificationShadeWindowViewController;Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;Lcom/android/systemui/statusbar/phone/HeadsUpManagerPhone;Lcom/android/internal/jank/InteractionJankMonitor;)V", "getAnimatorController", "Lcom/android/systemui/statusbar/notification/NotificationLaunchAnimatorController;", "notification", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "onFinishAnimationCallback", "Ljava/lang/Runnable;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@CentralSurfacesComponent.CentralSurfacesScope
/* compiled from: NotificationLaunchAnimatorController.kt */
public final class NotificationLaunchAnimatorControllerProvider {
    private final HeadsUpManagerPhone headsUpManager;
    private final InteractionJankMonitor jankMonitor;
    private final NotificationListContainer notificationListContainer;
    private final NotificationShadeWindowViewController notificationShadeWindowViewController;

    public final NotificationLaunchAnimatorController getAnimatorController(ExpandableNotificationRow expandableNotificationRow) {
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "notification");
        return getAnimatorController$default(this, expandableNotificationRow, (Runnable) null, 2, (Object) null);
    }

    @Inject
    public NotificationLaunchAnimatorControllerProvider(NotificationShadeWindowViewController notificationShadeWindowViewController2, NotificationListContainer notificationListContainer2, HeadsUpManagerPhone headsUpManagerPhone, InteractionJankMonitor interactionJankMonitor) {
        Intrinsics.checkNotNullParameter(notificationShadeWindowViewController2, "notificationShadeWindowViewController");
        Intrinsics.checkNotNullParameter(notificationListContainer2, "notificationListContainer");
        Intrinsics.checkNotNullParameter(headsUpManagerPhone, "headsUpManager");
        Intrinsics.checkNotNullParameter(interactionJankMonitor, "jankMonitor");
        this.notificationShadeWindowViewController = notificationShadeWindowViewController2;
        this.notificationListContainer = notificationListContainer2;
        this.headsUpManager = headsUpManagerPhone;
        this.jankMonitor = interactionJankMonitor;
    }

    public static /* synthetic */ NotificationLaunchAnimatorController getAnimatorController$default(NotificationLaunchAnimatorControllerProvider notificationLaunchAnimatorControllerProvider, ExpandableNotificationRow expandableNotificationRow, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            runnable = null;
        }
        return notificationLaunchAnimatorControllerProvider.getAnimatorController(expandableNotificationRow, runnable);
    }

    public final NotificationLaunchAnimatorController getAnimatorController(ExpandableNotificationRow expandableNotificationRow, Runnable runnable) {
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "notification");
        return new NotificationLaunchAnimatorController(this.notificationShadeWindowViewController, this.notificationListContainer, this.headsUpManager, expandableNotificationRow, this.jankMonitor, runnable);
    }
}
