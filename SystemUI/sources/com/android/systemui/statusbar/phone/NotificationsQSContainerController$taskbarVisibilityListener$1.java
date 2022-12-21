package com.android.systemui.statusbar.phone;

import com.android.systemui.recents.OverviewProxyService;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/statusbar/phone/NotificationsQSContainerController$taskbarVisibilityListener$1", "Lcom/android/systemui/recents/OverviewProxyService$OverviewProxyListener;", "onTaskbarStatusUpdated", "", "visible", "", "stashed", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerController$taskbarVisibilityListener$1 implements OverviewProxyService.OverviewProxyListener {
    final /* synthetic */ NotificationsQSContainerController this$0;

    NotificationsQSContainerController$taskbarVisibilityListener$1(NotificationsQSContainerController notificationsQSContainerController) {
        this.this$0 = notificationsQSContainerController;
    }

    public void onTaskbarStatusUpdated(boolean z, boolean z2) {
        this.this$0.taskbarVisible = z;
    }
}
