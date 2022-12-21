package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

/* renamed from: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$$ExternalSyntheticLambda12 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2818xa1546772 implements GroupExpansionManager.OnGroupExpansionChangeListener {
    public final /* synthetic */ NotificationStackScrollLayoutController f$0;

    public /* synthetic */ C2818xa1546772(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.f$0 = notificationStackScrollLayoutController;
    }

    public final void onGroupExpansionChange(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        this.f$0.mo42460x9c2f9288(expandableNotificationRow, z);
    }
}
