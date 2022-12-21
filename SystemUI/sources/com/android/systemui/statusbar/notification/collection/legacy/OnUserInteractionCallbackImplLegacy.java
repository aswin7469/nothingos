package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;

public class OnUserInteractionCallbackImplLegacy implements OnUserInteractionCallback {
    private final GroupMembershipManager mGroupMembershipManager;
    private final HeadsUpManager mHeadsUpManager;
    private final NotificationEntryManager mNotificationEntryManager;
    private final StatusBarStateController mStatusBarStateController;
    private final NotificationVisibilityProvider mVisibilityProvider;
    private final VisualStabilityManager mVisualStabilityManager;

    public OnUserInteractionCallbackImplLegacy(NotificationEntryManager notificationEntryManager, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, VisualStabilityManager visualStabilityManager, GroupMembershipManager groupMembershipManager) {
        this.mNotificationEntryManager = notificationEntryManager;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mHeadsUpManager = headsUpManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mGroupMembershipManager = groupMembershipManager;
    }

    /* access modifiers changed from: private */
    /* renamed from: onDismiss */
    public void mo40393xbaba125(NotificationEntry notificationEntry, int i, NotificationEntry notificationEntry2) {
        int i2;
        if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            i2 = 1;
        } else {
            i2 = this.mStatusBarStateController.isDozing() ? 2 : 3;
        }
        if (notificationEntry2 != null) {
            mo40393xbaba125(notificationEntry2, i, (NotificationEntry) null);
        }
        this.mNotificationEntryManager.performRemoveNotification(notificationEntry.getSbn(), new DismissedByUserStats(i2, 1, this.mVisibilityProvider.obtain(notificationEntry, true)), i);
    }

    public void onImportanceChanged(NotificationEntry notificationEntry) {
        this.mVisualStabilityManager.temporarilyAllowReordering();
    }

    private NotificationEntry getGroupSummaryToDismiss(NotificationEntry notificationEntry) {
        if (!this.mGroupMembershipManager.isOnlyChildInGroup(notificationEntry)) {
            return null;
        }
        NotificationEntry logicalGroupSummary = this.mGroupMembershipManager.getLogicalGroupSummary(notificationEntry);
        if (logicalGroupSummary.isDismissable()) {
            return logicalGroupSummary;
        }
        return null;
    }

    public Runnable registerFutureDismissal(NotificationEntry notificationEntry, int i) {
        return new OnUserInteractionCallbackImplLegacy$$ExternalSyntheticLambda0(this, notificationEntry, i, getGroupSummaryToDismiss(notificationEntry));
    }
}
