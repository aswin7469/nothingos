package com.android.systemui.statusbar.phone;

import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

@SysUISingleton
public class NotificationGroupAlertTransferHelper implements OnHeadsUpChangedListener, StatusBarStateController.StateListener {
    private static final long ALERT_TRANSFER_TIMEOUT = 300;
    /* access modifiers changed from: private */
    public static final boolean DEBUG = false;
    private static final boolean SPEW = false;
    private static final String TAG = "NotifGroupAlertTransfer";
    private NotificationEntryManager mEntryManager;
    /* access modifiers changed from: private */
    public final ArrayMap<String, GroupAlertEntry> mGroupAlertEntries = new ArrayMap<>();
    /* access modifiers changed from: private */
    public final NotificationGroupManagerLegacy mGroupManager;
    private HeadsUpManager mHeadsUpManager;
    private boolean mIsDozing;
    private final NotificationEntryListener mNotificationEntryListener = new NotificationEntryListener() {
        public void onPendingEntryAdded(NotificationEntry notificationEntry) {
            if (NotificationGroupAlertTransferHelper.DEBUG) {
                Log.d(NotificationGroupAlertTransferHelper.TAG, "!! onPendingEntryAdded: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
            }
            GroupAlertEntry groupAlertEntry = (GroupAlertEntry) NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.get(NotificationGroupAlertTransferHelper.this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
            if (groupAlertEntry != null && groupAlertEntry.mGroup.alertOverride == null) {
                NotificationGroupAlertTransferHelper.this.checkShouldTransferBack(groupAlertEntry);
            }
        }

        public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
            NotificationGroupAlertTransferHelper.this.mPendingAlerts.remove(notificationEntry.getKey());
        }
    };
    private final NotificationGroupManagerLegacy.OnGroupChangeListener mOnGroupChangeListener = new NotificationGroupManagerLegacy.OnGroupChangeListener() {
        public void onGroupCreated(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, String str) {
            NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.put(str, new GroupAlertEntry(notificationGroup));
        }

        public void onGroupRemoved(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, String str) {
            NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.remove(str);
        }

        public void onGroupSuppressionChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, boolean z) {
            if (NotificationGroupAlertTransferHelper.DEBUG) {
                Log.d(NotificationGroupAlertTransferHelper.TAG, "!! onGroupSuppressionChanged: group=" + NotificationGroupManagerLegacy.logGroupKey(notificationGroup) + " group.summary=" + NotificationUtils.logKey((ListEntry) notificationGroup.summary) + " suppressed=" + z);
            }
            NotificationGroupAlertTransferHelper.this.onGroupChanged(notificationGroup, notificationGroup.alertOverride);
        }

        public void onGroupAlertOverrideChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
            if (NotificationGroupAlertTransferHelper.DEBUG) {
                Log.d(NotificationGroupAlertTransferHelper.TAG, "!! onGroupAlertOverrideChanged: group=" + NotificationGroupManagerLegacy.logGroupKey(notificationGroup) + " group.summary=" + NotificationUtils.logKey((ListEntry) notificationGroup.summary) + " oldAlertOverride=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " newAlertOverride=" + NotificationUtils.logKey((ListEntry) notificationEntry2));
            }
            NotificationGroupAlertTransferHelper.this.onGroupChanged(notificationGroup, notificationEntry);
        }
    };
    /* access modifiers changed from: private */
    public final ArrayMap<String, PendingAlertInfo> mPendingAlerts = new ArrayMap<>();
    private final RowContentBindStage mRowContentBindStage;

    public void onStateChanged(int i) {
    }

    @Inject
    public NotificationGroupAlertTransferHelper(RowContentBindStage rowContentBindStage, StatusBarStateController statusBarStateController, NotificationGroupManagerLegacy notificationGroupManagerLegacy) {
        this.mRowContentBindStage = rowContentBindStage;
        this.mGroupManager = notificationGroupManagerLegacy;
        statusBarStateController.addCallback(this);
    }

    public void bind(NotificationEntryManager notificationEntryManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy) {
        if (this.mEntryManager == null) {
            this.mEntryManager = notificationEntryManager;
            notificationEntryManager.addNotificationEntryListener(this.mNotificationEntryListener);
            notificationGroupManagerLegacy.registerGroupChangeListener(this.mOnGroupChangeListener);
            return;
        }
        throw new IllegalStateException("Already bound.");
    }

    public boolean isAlertTransferPending(NotificationEntry notificationEntry) {
        PendingAlertInfo pendingAlertInfo = this.mPendingAlerts.get(notificationEntry.getKey());
        return pendingAlertInfo != null && pendingAlertInfo.isStillValid();
    }

    public void setHeadsUpManager(HeadsUpManager headsUpManager) {
        this.mHeadsUpManager = headsUpManager;
    }

    public void onDozingChanged(boolean z) {
        if (this.mIsDozing != z) {
            for (GroupAlertEntry next : this.mGroupAlertEntries.values()) {
                next.mLastAlertTransferTime = 0;
                next.mAlertSummaryOnNextAddition = false;
            }
        }
        this.mIsDozing = z;
    }

    /* access modifiers changed from: private */
    public void onGroupChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
        if (notificationGroup.summary == null) {
            if (DEBUG) {
                Log.d(TAG, "onGroupChanged: summary is null");
            }
        } else if (notificationGroup.suppressed || notificationGroup.alertOverride != null) {
            checkForForwardAlertTransfer(notificationGroup.summary, notificationEntry);
        } else {
            if (DEBUG) {
                Log.d(TAG, "onGroupChanged: maybe transfer back");
            }
            GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationGroup.summary.getSbn()));
            if (groupAlertEntry.mAlertSummaryOnNextAddition) {
                if (!this.mHeadsUpManager.isAlerting(notificationGroup.summary.getKey())) {
                    alertNotificationWhenPossible(notificationGroup.summary);
                }
                groupAlertEntry.mAlertSummaryOnNextAddition = false;
                return;
            }
            checkShouldTransferBack(groupAlertEntry);
        }
    }

    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        if (DEBUG) {
            Log.d(TAG, "!! onHeadsUpStateChanged: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " isHeadsUp=" + z);
        }
        if (z && notificationEntry.getSbn().getNotification().isGroupSummary()) {
            checkForForwardAlertTransfer(notificationEntry, (NotificationEntry) null);
        }
    }

    private void checkForForwardAlertTransfer(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "checkForForwardAlertTransfer: enter");
        }
        NotificationGroupManagerLegacy.NotificationGroup groupForSummary = this.mGroupManager.getGroupForSummary(notificationEntry.getSbn());
        if (groupForSummary != null && groupForSummary.alertOverride != null) {
            handleOverriddenSummaryAlerted(notificationEntry);
        } else if (this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.getSbn())) {
            handleSuppressedSummaryAlerted(notificationEntry, notificationEntry2);
        }
        if (z) {
            Log.d(TAG, "checkForForwardAlertTransfer: done");
        }
    }

    private int getPendingChildrenNotAlerting(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        NotificationEntryManager notificationEntryManager = this.mEntryManager;
        int i = 0;
        if (notificationEntryManager == null) {
            return 0;
        }
        for (NotificationEntry next : notificationEntryManager.getPendingNotificationsIterator()) {
            if (isPendingNotificationInGroup(next, notificationGroup) && onlySummaryAlerts(next)) {
                i++;
            }
        }
        return i;
    }

    private boolean pendingInflationsWillAddChildren(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        NotificationEntryManager notificationEntryManager = this.mEntryManager;
        if (notificationEntryManager == null) {
            return false;
        }
        for (NotificationEntry isPendingNotificationInGroup : notificationEntryManager.getPendingNotificationsIterator()) {
            if (isPendingNotificationInGroup(isPendingNotificationInGroup, notificationGroup)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPendingNotificationInGroup(NotificationEntry notificationEntry, NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        return this.mGroupManager.isGroupChild(notificationEntry.getSbn()) && Objects.equals(this.mGroupManager.getGroupKey(notificationEntry.getSbn()), this.mGroupManager.getGroupKey(notificationGroup.summary.getSbn())) && !notificationGroup.children.containsKey(notificationEntry.getKey());
    }

    private void handleSuppressedSummaryAlerted(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "handleSuppressedSummaryAlerted: summary=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
        if (this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.getSbn()) && groupAlertEntry != null) {
            boolean isAlerting = this.mHeadsUpManager.isAlerting(notificationEntry.getKey());
            boolean z2 = notificationEntry2 != null && this.mHeadsUpManager.isAlerting(notificationEntry2.getKey());
            if (isAlerting || z2) {
                if (!pendingInflationsWillAddChildren(groupAlertEntry.mGroup)) {
                    NotificationEntry next = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn()).iterator().next();
                    if (isAlerting) {
                        if (z) {
                            Log.d(TAG, "handleSuppressedSummaryAlerted: transfer summary -> child");
                        }
                        tryTransferAlertState(notificationEntry, notificationEntry, next, groupAlertEntry);
                    } else if (canStillTransferBack(groupAlertEntry)) {
                        if (z) {
                            Log.d(TAG, "handleSuppressedSummaryAlerted: transfer override -> child");
                        }
                        tryTransferAlertState(notificationEntry, notificationEntry2, next, groupAlertEntry);
                    } else if (z) {
                        Log.d(TAG, "handleSuppressedSummaryAlerted: transfer from override: too late");
                    }
                } else if (z) {
                    Log.d(TAG, "handleSuppressedSummaryAlerted: pending inflations");
                }
            } else if (z) {
                Log.d(TAG, "handleSuppressedSummaryAlerted: no summary or override alerting");
            }
        } else if (z) {
            Log.d(TAG, "handleSuppressedSummaryAlerted: invalid state");
        }
    }

    private void handleOverriddenSummaryAlerted(NotificationEntry notificationEntry) {
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "handleOverriddenSummaryAlerted: summary=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
        NotificationGroupManagerLegacy.NotificationGroup groupForSummary = this.mGroupManager.getGroupForSummary(notificationEntry.getSbn());
        if (groupForSummary == null || groupForSummary.alertOverride == null || groupAlertEntry == null) {
            if (z) {
                Log.d(TAG, "handleOverriddenSummaryAlerted: invalid state");
            }
        } else if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            if (z) {
                Log.d(TAG, "handleOverriddenSummaryAlerted: transfer summary -> override");
            }
            tryTransferAlertState(notificationEntry, notificationEntry, groupForSummary.alertOverride, groupAlertEntry);
        } else if (canStillTransferBack(groupAlertEntry)) {
            ArrayList<NotificationEntry> logicalChildren = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn());
            if (logicalChildren != null) {
                logicalChildren.remove((Object) groupForSummary.alertOverride);
                if (releaseChildAlerts(logicalChildren)) {
                    if (z) {
                        Log.d(TAG, "handleOverriddenSummaryAlerted: transfer child -> override");
                    }
                    tryTransferAlertState(notificationEntry, (NotificationEntry) null, groupForSummary.alertOverride, groupAlertEntry);
                } else if (z) {
                    Log.d(TAG, "handleOverriddenSummaryAlerted: no child alert released");
                }
            } else if (z) {
                Log.d(TAG, "handleOverriddenSummaryAlerted: no children");
            }
        } else if (z) {
            Log.d(TAG, "handleOverriddenSummaryAlerted: transfer from child: too late");
        }
    }

    private void tryTransferAlertState(NotificationEntry notificationEntry, NotificationEntry notificationEntry2, NotificationEntry notificationEntry3, GroupAlertEntry groupAlertEntry) {
        if (notificationEntry3 != null && !notificationEntry3.getRow().keepInParent() && !notificationEntry3.isRowRemoved() && !notificationEntry3.isRowDismissed()) {
            if (!this.mHeadsUpManager.isAlerting(notificationEntry3.getKey()) && onlySummaryAlerts(notificationEntry)) {
                groupAlertEntry.mLastAlertTransferTime = SystemClock.elapsedRealtime();
            }
            if (DEBUG) {
                Log.d(TAG, "transferAlertState: fromEntry=" + NotificationUtils.logKey((ListEntry) notificationEntry2) + " toEntry=" + NotificationUtils.logKey((ListEntry) notificationEntry3));
            }
            transferAlertState(notificationEntry2, notificationEntry3);
        }
    }

    private void transferAlertState(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        if (notificationEntry != null) {
            this.mHeadsUpManager.removeNotification(notificationEntry.getKey(), true);
        }
        alertNotificationWhenPossible(notificationEntry2);
    }

    /* access modifiers changed from: private */
    public void checkShouldTransferBack(GroupAlertEntry groupAlertEntry) {
        if (canStillTransferBack(groupAlertEntry)) {
            NotificationEntry notificationEntry = groupAlertEntry.mGroup.summary;
            if (onlySummaryAlerts(notificationEntry)) {
                ArrayList<NotificationEntry> logicalChildren = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn());
                int size = logicalChildren.size();
                if (getPendingChildrenNotAlerting(groupAlertEntry.mGroup) + size > 1 && releaseChildAlerts(logicalChildren) && !this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
                    if (size > 1) {
                        alertNotificationWhenPossible(notificationEntry);
                    } else {
                        groupAlertEntry.mAlertSummaryOnNextAddition = true;
                    }
                    groupAlertEntry.mLastAlertTransferTime = 0;
                }
            }
        }
    }

    private boolean canStillTransferBack(GroupAlertEntry groupAlertEntry) {
        return SystemClock.elapsedRealtime() - groupAlertEntry.mLastAlertTransferTime < 300;
    }

    private boolean releaseChildAlerts(List<NotificationEntry> list) {
        if (SPEW) {
            Log.d(TAG, "releaseChildAlerts: numChildren=" + list.size());
        }
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            NotificationEntry notificationEntry = list.get(i);
            if (SPEW) {
                Log.d(TAG, "releaseChildAlerts: checking i=" + i + " entry=" + notificationEntry + " onlySummaryAlerts=" + onlySummaryAlerts(notificationEntry) + " isAlerting=" + this.mHeadsUpManager.isAlerting(notificationEntry.getKey()) + " isPendingAlert=" + this.mPendingAlerts.containsKey(notificationEntry.getKey()));
            }
            if (onlySummaryAlerts(notificationEntry) && this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
                this.mHeadsUpManager.removeNotification(notificationEntry.getKey(), true);
                z = true;
            }
            if (this.mPendingAlerts.containsKey(notificationEntry.getKey())) {
                this.mPendingAlerts.get(notificationEntry.getKey()).mAbortOnInflation = true;
                z = true;
            }
        }
        if (SPEW) {
            Log.d(TAG, "releaseChildAlerts: didRelease=" + z);
        }
        return z;
    }

    private void alertNotificationWhenPossible(NotificationEntry notificationEntry) {
        int contentFlag = this.mHeadsUpManager.getContentFlag();
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mRowContentBindStage.getStageParams(notificationEntry);
        if ((rowContentBindParams.getContentViews() & contentFlag) == 0) {
            if (DEBUG) {
                Log.d(TAG, "alertNotificationWhenPossible: async requestRebind entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
            }
            this.mPendingAlerts.put(notificationEntry.getKey(), new PendingAlertInfo(notificationEntry));
            rowContentBindParams.requireContentViews(contentFlag);
            this.mRowContentBindStage.requestRebind(notificationEntry, new NotificationGroupAlertTransferHelper$$ExternalSyntheticLambda0(this, notificationEntry, contentFlag));
        } else if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            if (DEBUG) {
                Log.d(TAG, "alertNotificationWhenPossible: continue alerting entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
            }
            this.mHeadsUpManager.updateNotification(notificationEntry.getKey(), true);
        } else {
            if (DEBUG) {
                Log.d(TAG, "alertNotificationWhenPossible: start alerting entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
            }
            this.mHeadsUpManager.showNotification(notificationEntry);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$alertNotificationWhenPossible$0$com-android-systemui-statusbar-phone-NotificationGroupAlertTransferHelper */
    public /* synthetic */ void mo44471xd966a209(NotificationEntry notificationEntry, int i, NotificationEntry notificationEntry2) {
        PendingAlertInfo remove = this.mPendingAlerts.remove(notificationEntry.getKey());
        if (remove == null) {
            return;
        }
        if (remove.isStillValid()) {
            alertNotificationWhenPossible(notificationEntry);
            return;
        }
        if (DEBUG) {
            Log.d(TAG, "alertNotificationWhenPossible: markContentViewsFreeable entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        ((RowContentBindParams) this.mRowContentBindStage.getStageParams(notificationEntry)).markContentViewsFreeable(i);
        this.mRowContentBindStage.requestRebind(notificationEntry, (NotifBindPipeline.BindCallback) null);
    }

    private boolean onlySummaryAlerts(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().getGroupAlertBehavior() == 1;
    }

    private class PendingAlertInfo {
        boolean mAbortOnInflation;
        final NotificationEntry mEntry;
        final StatusBarNotification mOriginalNotification;

        PendingAlertInfo(NotificationEntry notificationEntry) {
            this.mOriginalNotification = notificationEntry.getSbn();
            this.mEntry = notificationEntry;
        }

        /* access modifiers changed from: private */
        public boolean isStillValid() {
            if (!this.mAbortOnInflation && this.mEntry.getSbn().getGroupKey().equals(this.mOriginalNotification.getGroupKey()) && this.mEntry.getSbn().getNotification().isGroupSummary() == this.mOriginalNotification.getNotification().isGroupSummary()) {
                return true;
            }
            return false;
        }
    }

    private static class GroupAlertEntry {
        boolean mAlertSummaryOnNextAddition;
        final NotificationGroupManagerLegacy.NotificationGroup mGroup;
        long mLastAlertTransferTime;

        GroupAlertEntry(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
            this.mGroup = notificationGroup;
        }
    }
}
