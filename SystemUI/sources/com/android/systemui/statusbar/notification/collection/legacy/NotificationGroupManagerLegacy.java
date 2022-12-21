package com.android.systemui.statusbar.notification.collection.legacy;

import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.Log;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Function;
import javax.inject.Inject;

@SysUISingleton
public class NotificationGroupManagerLegacy implements OnHeadsUpChangedListener, StatusBarStateController.StateListener, GroupMembershipManager, GroupExpansionManager, Dumpable {
    private static final boolean DEBUG = false;
    private static final long POST_BATCH_MAX_AGE = 5000;
    /* access modifiers changed from: private */
    public static final boolean SPEW = false;
    private static final String TAG = "LegacyNotifGroupManager";
    private int mBarState;
    private final Optional<Bubbles> mBubblesOptional;
    private final GroupEventDispatcher mEventDispatcher;
    private final ArraySet<GroupExpansionManager.OnGroupExpansionChangeListener> mExpansionChangeListeners = new ArraySet<>();
    private final HashMap<String, NotificationGroup> mGroupMap;
    private HeadsUpManager mHeadsUpManager;
    private boolean mIsUpdatingUnchangedGroup;
    private HashMap<String, StatusBarNotification> mIsolatedEntries;
    private final Lazy<PeopleNotificationIdentifier> mPeopleNotificationIdentifier;

    public interface OnGroupChangeListener {
        void onGroupAlertOverrideChanged(NotificationGroup notificationGroup, NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        }

        void onGroupCreated(NotificationGroup notificationGroup, String str) {
        }

        void onGroupRemoved(NotificationGroup notificationGroup, String str) {
        }

        void onGroupSuppressionChanged(NotificationGroup notificationGroup, boolean z) {
        }

        void onGroupsChanged() {
        }
    }

    @Inject
    public NotificationGroupManagerLegacy(StatusBarStateController statusBarStateController, Lazy<PeopleNotificationIdentifier> lazy, Optional<Bubbles> optional, DumpManager dumpManager) {
        HashMap<String, NotificationGroup> hashMap = new HashMap<>();
        this.mGroupMap = hashMap;
        Objects.requireNonNull(hashMap);
        this.mEventDispatcher = new GroupEventDispatcher(new NotificationGroupManagerLegacy$$ExternalSyntheticLambda0(hashMap));
        this.mBarState = -1;
        this.mIsolatedEntries = new HashMap<>();
        statusBarStateController.addCallback(this);
        this.mPeopleNotificationIdentifier = lazy;
        this.mBubblesOptional = optional;
        dumpManager.registerDumpable(this);
    }

    public void registerGroupChangeListener(OnGroupChangeListener onGroupChangeListener) {
        this.mEventDispatcher.registerGroupChangeListener(onGroupChangeListener);
    }

    public void registerGroupExpansionChangeListener(GroupExpansionManager.OnGroupExpansionChangeListener onGroupExpansionChangeListener) {
        this.mExpansionChangeListeners.add(onGroupExpansionChangeListener);
    }

    public boolean isGroupExpanded(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.getSbn()));
        if (notificationGroup == null) {
            return false;
        }
        return notificationGroup.expanded;
    }

    public boolean isLogicalGroupExpanded(StatusBarNotification statusBarNotification) {
        NotificationGroup notificationGroup = this.mGroupMap.get(statusBarNotification.getGroupKey());
        if (notificationGroup == null) {
            return false;
        }
        return notificationGroup.expanded;
    }

    public void setGroupExpanded(NotificationEntry notificationEntry, boolean z) {
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.getSbn()));
        if (notificationGroup != null) {
            setGroupExpanded(notificationGroup, z);
        }
    }

    private void setGroupExpanded(NotificationGroup notificationGroup, boolean z) {
        notificationGroup.expanded = z;
        if (notificationGroup.summary != null) {
            Iterator<GroupExpansionManager.OnGroupExpansionChangeListener> it = this.mExpansionChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupExpansionChange(notificationGroup.summary.getRow(), z);
            }
        }
    }

    public void onEntryRemoved(NotificationEntry notificationEntry) {
        if (SPEW) {
            Log.d(TAG, "onEntryRemoved: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        this.mEventDispatcher.openBufferScope();
        onEntryRemovedInternal(notificationEntry, notificationEntry.getSbn());
        StatusBarNotification remove = this.mIsolatedEntries.remove(notificationEntry.getKey());
        if (remove != null) {
            updateSuppression(this.mGroupMap.get(remove.getGroupKey()));
        }
        this.mEventDispatcher.closeBufferScope();
    }

    private void onEntryRemovedInternal(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        onEntryRemovedInternal(notificationEntry, statusBarNotification.getGroupKey(), statusBarNotification.isGroup(), statusBarNotification.getNotification().isGroupSummary());
    }

    private void onEntryRemovedInternal(NotificationEntry notificationEntry, String str, boolean z, boolean z2) {
        String groupKey = getGroupKey(notificationEntry.getKey(), str);
        NotificationGroup notificationGroup = this.mGroupMap.get(groupKey);
        if (notificationGroup != null) {
            if (SPEW) {
                Log.d(TAG, "onEntryRemovedInternal: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " group=" + logGroupKey(notificationGroup));
            }
            if (isGroupChild(notificationEntry.getKey(), z, z2)) {
                notificationGroup.children.remove(notificationEntry.getKey());
            } else {
                notificationGroup.summary = null;
            }
            updateSuppression(notificationGroup);
            if (notificationGroup.children.isEmpty() && notificationGroup.summary == null) {
                this.mGroupMap.remove(groupKey);
                this.mEventDispatcher.notifyGroupRemoved(notificationGroup);
            }
        }
    }

    public void onEntryAdded(NotificationEntry notificationEntry) {
        if (SPEW) {
            Log.d(TAG, "onEntryAdded: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        this.mEventDispatcher.openBufferScope();
        updateIsolation(notificationEntry);
        onEntryAddedInternal(notificationEntry);
        this.mEventDispatcher.closeBufferScope();
    }

    private void onEntryAddedInternal(NotificationEntry notificationEntry) {
        if (notificationEntry.isRowRemoved()) {
            notificationEntry.setDebugThrowable(new Throwable());
        }
        StatusBarNotification sbn = notificationEntry.getSbn();
        boolean isGroupChild = isGroupChild(sbn);
        String groupKey = getGroupKey(sbn);
        NotificationGroup notificationGroup = this.mGroupMap.get(groupKey);
        if (notificationGroup == null) {
            notificationGroup = new NotificationGroup(groupKey);
            this.mGroupMap.put(groupKey, notificationGroup);
            this.mEventDispatcher.notifyGroupCreated(notificationGroup);
        }
        if (SPEW) {
            Log.d(TAG, "onEntryAddedInternal: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " group=" + logGroupKey(notificationGroup));
        }
        if (isGroupChild) {
            NotificationEntry notificationEntry2 = notificationGroup.children.get(notificationEntry.getKey());
            if (!(notificationEntry2 == null || notificationEntry2 == notificationEntry)) {
                Throwable debugThrowable = notificationEntry2.getDebugThrowable();
                Log.wtf(TAG, "Inconsistent entries found with the same key " + NotificationUtils.logKey((ListEntry) notificationEntry) + "existing removed: " + notificationEntry2.isRowRemoved() + (debugThrowable != null ? Log.getStackTraceString(debugThrowable) + "\n" : "") + " added removed" + notificationEntry.isRowRemoved(), new Throwable());
            }
            notificationGroup.children.put(notificationEntry.getKey(), notificationEntry);
            addToPostBatchHistory(notificationGroup, notificationEntry);
            updateSuppression(notificationGroup);
            return;
        }
        notificationGroup.summary = notificationEntry;
        addToPostBatchHistory(notificationGroup, notificationEntry);
        notificationGroup.expanded = notificationEntry.areChildrenExpanded();
        updateSuppression(notificationGroup);
        if (!notificationGroup.children.isEmpty()) {
            Iterator it = new ArrayList(notificationGroup.children.values()).iterator();
            while (it.hasNext()) {
                onEntryBecomingChild((NotificationEntry) it.next());
            }
            this.mEventDispatcher.notifyGroupsChanged();
        }
    }

    private void addToPostBatchHistory(NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
        if (notificationEntry != null && notificationGroup.postBatchHistory.add(new PostRecord(notificationEntry))) {
            trimPostBatchHistory(notificationGroup.postBatchHistory);
        }
    }

    private void trimPostBatchHistory(TreeSet<PostRecord> treeSet) {
        if (treeSet.size() > 1) {
            long j = treeSet.last().postTime - 5000;
            while (!treeSet.isEmpty() && treeSet.first().postTime < j) {
                treeSet.pollFirst();
            }
        }
    }

    private void onEntryBecomingChild(NotificationEntry notificationEntry) {
        updateIsolation(notificationEntry);
    }

    private void updateSuppression(NotificationGroup notificationGroup) {
        if (notificationGroup != null) {
            NotificationEntry notificationEntry = notificationGroup.alertOverride;
            notificationGroup.alertOverride = getPriorityConversationAlertOverride(notificationGroup);
            boolean z = false;
            int i = 0;
            boolean z2 = false;
            for (NotificationEntry next : notificationGroup.children.values()) {
                if (!this.mBubblesOptional.isPresent() || !this.mBubblesOptional.get().isBubbleNotificationSuppressedFromShade(next.getKey(), next.getSbn().getGroupKey())) {
                    i++;
                } else {
                    z2 = true;
                }
            }
            boolean z3 = notificationGroup.suppressed;
            notificationGroup.suppressed = notificationGroup.summary != null && !notificationGroup.expanded && (i == 1 || (i == 0 && notificationGroup.summary.getSbn().getNotification().isGroupSummary() && (hasIsolatedChildren(notificationGroup) || z2)));
            boolean z4 = notificationEntry != notificationGroup.alertOverride;
            if (z3 != notificationGroup.suppressed) {
                z = true;
            }
            if (z4 || z) {
                if (DEBUG) {
                    Log.d(TAG, "updateSuppression: willNotifyListeners=" + (!this.mIsUpdatingUnchangedGroup) + " changes for group:\n" + notificationGroup);
                    if (z4) {
                        Log.d(TAG, "updateSuppression: alertOverride was=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " now=" + NotificationUtils.logKey((ListEntry) notificationGroup.alertOverride));
                    }
                    if (z) {
                        Log.d(TAG, "updateSuppression: suppressed changed to " + notificationGroup.suppressed);
                    }
                }
                if (z4) {
                    this.mEventDispatcher.notifyAlertOverrideChanged(notificationGroup, notificationEntry);
                }
                if (z) {
                    this.mEventDispatcher.notifySuppressedChanged(notificationGroup);
                }
                if (!this.mIsUpdatingUnchangedGroup) {
                    this.mEventDispatcher.notifyGroupsChanged();
                }
            }
        }
    }

    private NotificationEntry getPriorityConversationAlertOverride(NotificationGroup notificationGroup) {
        if (notificationGroup == null || notificationGroup.summary == null) {
            if (SPEW) {
                Log.d(TAG, "getPriorityConversationAlertOverride: null group or summary group=" + logGroupKey(notificationGroup));
            }
            return null;
        } else if (isIsolated(notificationGroup.summary.getKey())) {
            if (SPEW) {
                Log.d(TAG, "getPriorityConversationAlertOverride: isolated group group=" + logGroupKey(notificationGroup));
            }
            return null;
        } else if (notificationGroup.summary.getSbn().getNotification().getGroupAlertBehavior() == 2) {
            if (SPEW) {
                Log.d(TAG, "getPriorityConversationAlertOverride: summary == GROUP_ALERT_CHILDREN group=" + logGroupKey(notificationGroup));
            }
            return null;
        } else {
            HashMap<String, NotificationEntry> importantConversations = getImportantConversations(notificationGroup);
            if (importantConversations == null || importantConversations.isEmpty()) {
                if (SPEW) {
                    Log.d(TAG, "getPriorityConversationAlertOverride: no important conversations group=" + logGroupKey(notificationGroup));
                }
                return null;
            }
            HashSet hashSet = new HashSet(importantConversations.keySet());
            importantConversations.putAll(notificationGroup.children);
            for (NotificationEntry sbn : importantConversations.values()) {
                if (sbn.getSbn().getNotification().getGroupAlertBehavior() != 1) {
                    if (SPEW) {
                        Log.d(TAG, "getPriorityConversationAlertOverride: child != GROUP_ALERT_SUMMARY group=" + logGroupKey(notificationGroup));
                    }
                    return null;
                }
            }
            TreeSet treeSet = new TreeSet(notificationGroup.postBatchHistory);
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                treeSet.addAll(this.mGroupMap.get((String) it.next()).postBatchHistory);
            }
            trimPostBatchHistory(treeSet);
            HashSet hashSet2 = new HashSet();
            long j = -1;
            NotificationEntry notificationEntry = null;
            for (PostRecord postRecord : treeSet.descendingSet()) {
                if (hashSet2.contains(postRecord.key)) {
                    break;
                }
                hashSet2.add(postRecord.key);
                NotificationEntry notificationEntry2 = importantConversations.get(postRecord.key);
                if (notificationEntry2 != null) {
                    long j2 = notificationEntry2.getSbn().getNotification().when;
                    if (notificationEntry == null || j2 > j) {
                        notificationEntry = notificationEntry2;
                        j = j2;
                    }
                }
            }
            if (notificationEntry == null || !hashSet.contains(notificationEntry.getKey())) {
                if (SPEW) {
                    Log.d(TAG, "getPriorityConversationAlertOverride: result=null newestChild=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " group=" + logGroupKey(notificationGroup));
                }
                return null;
            }
            if (SPEW) {
                Log.d(TAG, "getPriorityConversationAlertOverride: result=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " group=" + logGroupKey(notificationGroup));
            }
            return notificationEntry;
        }
    }

    private boolean hasIsolatedChildren(NotificationGroup notificationGroup) {
        return getNumberOfIsolatedChildren(notificationGroup.summary.getSbn().getGroupKey()) != 0;
    }

    private int getNumberOfIsolatedChildren(String str) {
        int i = 0;
        for (StatusBarNotification next : this.mIsolatedEntries.values()) {
            if (next.getGroupKey().equals(str) && isIsolated(next.getKey())) {
                i++;
            }
        }
        return i;
    }

    private HashMap<String, NotificationEntry> getImportantConversations(NotificationGroup notificationGroup) {
        String groupKey = notificationGroup.summary.getSbn().getGroupKey();
        HashMap<String, NotificationEntry> hashMap = null;
        for (StatusBarNotification next : this.mIsolatedEntries.values()) {
            if (next.getGroupKey().equals(groupKey)) {
                NotificationEntry notificationEntry = this.mGroupMap.get(next.getKey()).summary;
                if (isImportantConversation(notificationEntry)) {
                    if (hashMap == null) {
                        hashMap = new HashMap<>();
                    }
                    hashMap.put(next.getKey(), notificationEntry);
                }
            }
        }
        return hashMap;
    }

    public void onEntryUpdated(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        if (SPEW) {
            Log.d(TAG, "onEntryUpdated: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        onEntryUpdated(notificationEntry, statusBarNotification.getGroupKey(), statusBarNotification.isGroup(), statusBarNotification.getNotification().isGroupSummary());
    }

    public void onEntryUpdated(NotificationEntry notificationEntry, String str, boolean z, boolean z2) {
        String groupKey = notificationEntry.getSbn().getGroupKey();
        boolean z3 = true;
        boolean z4 = !str.equals(groupKey);
        boolean isGroupChild = isGroupChild(notificationEntry.getKey(), z, z2);
        boolean isGroupChild2 = isGroupChild(notificationEntry.getSbn());
        this.mEventDispatcher.openBufferScope();
        if (z4 || isGroupChild != isGroupChild2) {
            z3 = false;
        }
        this.mIsUpdatingUnchangedGroup = z3;
        if (this.mGroupMap.get(getGroupKey(notificationEntry.getKey(), str)) != null) {
            onEntryRemovedInternal(notificationEntry, str, z, z2);
        }
        onEntryAddedInternal(notificationEntry);
        this.mIsUpdatingUnchangedGroup = false;
        if (isIsolated(notificationEntry.getSbn().getKey())) {
            this.mIsolatedEntries.put(notificationEntry.getKey(), notificationEntry.getSbn());
            if (z4) {
                updateSuppression(this.mGroupMap.get(str));
            }
            updateSuppression(this.mGroupMap.get(groupKey));
        } else if (!isGroupChild && isGroupChild2) {
            onEntryBecomingChild(notificationEntry);
        }
        this.mEventDispatcher.closeBufferScope();
    }

    public boolean isSummaryOfSuppressedGroup(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getNotification().isGroupSummary() && isGroupSuppressed(getGroupKey(statusBarNotification));
    }

    public NotificationGroup getGroupForSummary(StatusBarNotification statusBarNotification) {
        if (statusBarNotification.getNotification().isGroupSummary()) {
            return this.mGroupMap.get(getGroupKey(statusBarNotification));
        }
        return null;
    }

    private boolean isOnlyChild(StatusBarNotification statusBarNotification) {
        return !statusBarNotification.getNotification().isGroupSummary() && getTotalNumberOfChildren(statusBarNotification) == 1;
    }

    public boolean isOnlyChildInGroup(NotificationEntry notificationEntry) {
        NotificationEntry logicalGroupSummary;
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (isOnlyChild(sbn) && (logicalGroupSummary = getLogicalGroupSummary(notificationEntry)) != null && !logicalGroupSummary.getSbn().equals(sbn)) {
            return true;
        }
        return false;
    }

    private int getTotalNumberOfChildren(StatusBarNotification statusBarNotification) {
        int numberOfIsolatedChildren = getNumberOfIsolatedChildren(statusBarNotification.getGroupKey());
        NotificationGroup notificationGroup = this.mGroupMap.get(statusBarNotification.getGroupKey());
        return numberOfIsolatedChildren + (notificationGroup != null ? notificationGroup.children.size() : 0);
    }

    private boolean isGroupSuppressed(String str) {
        NotificationGroup notificationGroup = this.mGroupMap.get(str);
        return notificationGroup != null && notificationGroup.suppressed;
    }

    private void setStatusBarState(int i) {
        this.mBarState = i;
        if (i == 1) {
            collapseGroups();
        }
    }

    public void collapseGroups() {
        ArrayList arrayList = new ArrayList(this.mGroupMap.values());
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            NotificationGroup notificationGroup = (NotificationGroup) arrayList.get(i);
            if (notificationGroup.expanded) {
                setGroupExpanded(notificationGroup, false);
            }
            updateSuppression(notificationGroup);
        }
    }

    public boolean isChildInGroup(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup;
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (isGroupChild(sbn) && (notificationGroup = this.mGroupMap.get(getGroupKey(sbn))) != null && notificationGroup.summary != null && !notificationGroup.suppressed && !notificationGroup.children.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isGroupSummary(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup;
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (isGroupSummary(sbn) && (notificationGroup = this.mGroupMap.get(getGroupKey(sbn))) != null && notificationGroup.summary != null && !notificationGroup.children.isEmpty() && Objects.equals(notificationGroup.summary.getSbn(), sbn)) {
            return true;
        }
        return false;
    }

    public NotificationEntry getGroupSummary(NotificationEntry notificationEntry) {
        return getGroupSummary(getGroupKey(notificationEntry.getSbn()));
    }

    public NotificationEntry getLogicalGroupSummary(NotificationEntry notificationEntry) {
        return getGroupSummary(notificationEntry.getSbn().getGroupKey());
    }

    private NotificationEntry getGroupSummary(String str) {
        NotificationGroup notificationGroup = this.mGroupMap.get(str);
        if (notificationGroup == null) {
            return null;
        }
        return notificationGroup.summary;
    }

    public ArrayList<NotificationEntry> getLogicalChildren(StatusBarNotification statusBarNotification) {
        NotificationGroup notificationGroup = this.mGroupMap.get(statusBarNotification.getGroupKey());
        if (notificationGroup == null) {
            return null;
        }
        ArrayList<NotificationEntry> arrayList = new ArrayList<>(notificationGroup.children.values());
        for (StatusBarNotification next : this.mIsolatedEntries.values()) {
            if (next.getGroupKey().equals(statusBarNotification.getGroupKey())) {
                arrayList.add(this.mGroupMap.get(next.getKey()).summary);
            }
        }
        return arrayList;
    }

    public List<NotificationEntry> getChildren(ListEntry listEntry) {
        NotificationGroup notificationGroup = this.mGroupMap.get(listEntry.getRepresentativeEntry().getSbn().getGroupKey());
        if (notificationGroup == null) {
            return null;
        }
        return new ArrayList(notificationGroup.children.values());
    }

    public void updateSuppression(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.getSbn()));
        if (notificationGroup != null) {
            updateSuppression(notificationGroup);
        }
    }

    public String getGroupKey(StatusBarNotification statusBarNotification) {
        return getGroupKey(statusBarNotification.getKey(), statusBarNotification.getGroupKey());
    }

    private String getGroupKey(String str, String str2) {
        return isIsolated(str) ? str : str2;
    }

    public boolean toggleGroupExpansion(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.getSbn()));
        if (notificationGroup == null) {
            return false;
        }
        setGroupExpanded(notificationGroup, !notificationGroup.expanded);
        return notificationGroup.expanded;
    }

    private boolean isIsolated(String str) {
        return this.mIsolatedEntries.containsKey(str);
    }

    public boolean isGroupSummary(StatusBarNotification statusBarNotification) {
        if (isIsolated(statusBarNotification.getKey())) {
            return true;
        }
        return statusBarNotification.getNotification().isGroupSummary();
    }

    public boolean isGroupChild(StatusBarNotification statusBarNotification) {
        return isGroupChild(statusBarNotification.getKey(), statusBarNotification.isGroup(), statusBarNotification.getNotification().isGroupSummary());
    }

    private boolean isGroupChild(String str, boolean z, boolean z2) {
        return !isIsolated(str) && z && !z2;
    }

    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        updateIsolation(notificationEntry);
    }

    private boolean shouldIsolate(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!sbn.isGroup() || sbn.getNotification().isGroupSummary()) {
            return false;
        }
        if (isImportantConversation(notificationEntry)) {
            return true;
        }
        HeadsUpManager headsUpManager = this.mHeadsUpManager;
        if (headsUpManager != null && !headsUpManager.isAlerting(notificationEntry.getKey())) {
            return false;
        }
        NotificationGroup notificationGroup = this.mGroupMap.get(sbn.getGroupKey());
        if (sbn.getNotification().fullScreenIntent != null || notificationGroup == null || !notificationGroup.expanded || isGroupNotFullyVisible(notificationGroup)) {
            return true;
        }
        return false;
    }

    private boolean isImportantConversation(NotificationEntry notificationEntry) {
        return this.mPeopleNotificationIdentifier.get().getPeopleNotificationType(notificationEntry) == 3;
    }

    private void isolateNotification(NotificationEntry notificationEntry) {
        if (SPEW) {
            Log.d(TAG, "isolateNotification: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        onEntryRemovedInternal(notificationEntry, notificationEntry.getSbn());
        this.mIsolatedEntries.put(notificationEntry.getKey(), notificationEntry.getSbn());
        onEntryAddedInternal(notificationEntry);
        updateSuppression(this.mGroupMap.get(notificationEntry.getSbn().getGroupKey()));
        this.mEventDispatcher.notifyGroupsChanged();
    }

    public void updateIsolation(NotificationEntry notificationEntry) {
        this.mEventDispatcher.openBufferScope();
        boolean isIsolated = isIsolated(notificationEntry.getSbn().getKey());
        if (shouldIsolate(notificationEntry)) {
            if (!isIsolated) {
                isolateNotification(notificationEntry);
            }
        } else if (isIsolated) {
            stopIsolatingNotification(notificationEntry);
        }
        this.mEventDispatcher.closeBufferScope();
    }

    private void stopIsolatingNotification(NotificationEntry notificationEntry) {
        if (SPEW) {
            Log.d(TAG, "stopIsolatingNotification: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        onEntryRemovedInternal(notificationEntry, notificationEntry.getSbn());
        this.mIsolatedEntries.remove(notificationEntry.getKey());
        onEntryAddedInternal(notificationEntry);
        this.mEventDispatcher.notifyGroupsChanged();
    }

    private boolean isGroupNotFullyVisible(NotificationGroup notificationGroup) {
        return notificationGroup.summary == null || notificationGroup.summary.isGroupNotFullyVisible();
    }

    public void setHeadsUpManager(HeadsUpManager headsUpManager) {
        this.mHeadsUpManager = headsUpManager;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("GroupManagerLegacy state:");
        printWriter.println("  number of groups: " + this.mGroupMap.size());
        for (Map.Entry next : this.mGroupMap.entrySet()) {
            printWriter.println("\n    key: " + NotificationUtils.logKey((String) next.getKey()));
            printWriter.println(next.getValue());
        }
        printWriter.println("\n    isolated entries: " + this.mIsolatedEntries.size());
        for (Map.Entry next2 : this.mIsolatedEntries.entrySet()) {
            printWriter.print("      ");
            printWriter.print(NotificationUtils.logKey((String) next2.getKey()));
            printWriter.print(", ");
            printWriter.println(next2.getValue());
        }
    }

    public void onStateChanged(int i) {
        setStatusBarState(i);
    }

    public static String logGroupKey(NotificationGroup notificationGroup) {
        return notificationGroup == null ? "null" : NotificationUtils.logKey(notificationGroup.groupKey);
    }

    public static class PostRecord implements Comparable<PostRecord> {
        public final String key;
        public final long postTime;

        public PostRecord(NotificationEntry notificationEntry) {
            this.postTime = notificationEntry.getSbn().getPostTime();
            this.key = notificationEntry.getKey();
        }

        public int compareTo(PostRecord postRecord) {
            int compare = Long.compare(this.postTime, postRecord.postTime);
            return compare == 0 ? String.CASE_INSENSITIVE_ORDER.compare(this.key, postRecord.key) : compare;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PostRecord postRecord = (PostRecord) obj;
            if (this.postTime != postRecord.postTime || !this.key.equals(postRecord.key)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(Long.valueOf(this.postTime), this.key);
        }
    }

    public static class NotificationGroup {
        public NotificationEntry alertOverride;
        public final HashMap<String, NotificationEntry> children = new HashMap<>();
        public boolean expanded;
        public final String groupKey;
        public final TreeSet<PostRecord> postBatchHistory = new TreeSet<>();
        public NotificationEntry summary;
        public boolean suppressed;

        NotificationGroup(String str) {
            this.groupKey = str;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("    groupKey: ").append(this.groupKey);
            sb.append("\n    summary:");
            appendEntry(sb, this.summary);
            sb.append("\n    children size: ").append(this.children.size());
            for (NotificationEntry appendEntry : this.children.values()) {
                appendEntry(sb, appendEntry);
            }
            sb.append("\n    alertOverride:");
            appendEntry(sb, this.alertOverride);
            sb.append("\n    summary suppressed: ").append(this.suppressed);
            return sb.toString();
        }

        private void appendEntry(StringBuilder sb, NotificationEntry notificationEntry) {
            sb.append("\n      ").append(notificationEntry != null ? notificationEntry.getSbn() : "null");
            if (notificationEntry != null && notificationEntry.getDebugThrowable() != null) {
                sb.append(Log.getStackTraceString(notificationEntry.getDebugThrowable()));
            }
        }
    }

    static class GroupEventDispatcher {
        private int mBufferScopeDepth = 0;
        private boolean mDidGroupsChange = false;
        private final ArraySet<OnGroupChangeListener> mGroupChangeListeners = new ArraySet<>();
        private final Function<String, NotificationGroup> mGroupMapGetter;
        private final HashMap<String, NotificationEntry> mOldAlertOverrideByGroup = new HashMap<>();
        private final HashMap<String, Boolean> mOldSuppressedByGroup = new HashMap<>();

        /* JADX WARNING: type inference failed for: r2v0, types: [java.util.function.Function<java.lang.String, com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy$NotificationGroup>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        GroupEventDispatcher(java.util.function.Function<java.lang.String, com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.NotificationGroup> r2) {
            /*
                r1 = this;
                r1.<init>()
                android.util.ArraySet r0 = new android.util.ArraySet
                r0.<init>()
                r1.mGroupChangeListeners = r0
                java.util.HashMap r0 = new java.util.HashMap
                r0.<init>()
                r1.mOldAlertOverrideByGroup = r0
                java.util.HashMap r0 = new java.util.HashMap
                r0.<init>()
                r1.mOldSuppressedByGroup = r0
                r0 = 0
                r1.mBufferScopeDepth = r0
                r1.mDidGroupsChange = r0
                java.lang.Object r2 = java.util.Objects.requireNonNull(r2)
                java.util.function.Function r2 = (java.util.function.Function) r2
                r1.mGroupMapGetter = r2
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.GroupEventDispatcher.<init>(java.util.function.Function):void");
        }

        /* access modifiers changed from: package-private */
        public void registerGroupChangeListener(OnGroupChangeListener onGroupChangeListener) {
            this.mGroupChangeListeners.add(onGroupChangeListener);
        }

        private boolean isBuffering() {
            return this.mBufferScopeDepth > 0;
        }

        /* access modifiers changed from: package-private */
        public void notifyAlertOverrideChanged(NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
            if (!isBuffering()) {
                Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
                while (it.hasNext()) {
                    it.next().onGroupAlertOverrideChanged(notificationGroup, notificationEntry, notificationGroup.alertOverride);
                }
            } else if (!this.mOldAlertOverrideByGroup.containsKey(notificationGroup.groupKey)) {
                this.mOldAlertOverrideByGroup.put(notificationGroup.groupKey, notificationEntry);
            }
        }

        /* access modifiers changed from: package-private */
        public void notifySuppressedChanged(NotificationGroup notificationGroup) {
            if (isBuffering()) {
                this.mOldSuppressedByGroup.putIfAbsent(notificationGroup.groupKey, Boolean.valueOf(!notificationGroup.suppressed));
                return;
            }
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupSuppressionChanged(notificationGroup, notificationGroup.suppressed);
            }
        }

        /* access modifiers changed from: package-private */
        public void notifyGroupsChanged() {
            if (isBuffering()) {
                this.mDidGroupsChange = true;
                return;
            }
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupsChanged();
            }
        }

        /* access modifiers changed from: package-private */
        public void notifyGroupCreated(NotificationGroup notificationGroup) {
            String str = notificationGroup.groupKey;
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupCreated(notificationGroup, str);
            }
        }

        /* access modifiers changed from: package-private */
        public void notifyGroupRemoved(NotificationGroup notificationGroup) {
            String str = notificationGroup.groupKey;
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupRemoved(notificationGroup, str);
            }
        }

        /* access modifiers changed from: package-private */
        public void openBufferScope() {
            this.mBufferScopeDepth++;
            if (NotificationGroupManagerLegacy.SPEW) {
                Log.d(NotificationGroupManagerLegacy.TAG, "openBufferScope: scopeDepth=" + this.mBufferScopeDepth);
            }
        }

        /* access modifiers changed from: package-private */
        public void closeBufferScope() {
            this.mBufferScopeDepth--;
            if (NotificationGroupManagerLegacy.SPEW) {
                Log.d(NotificationGroupManagerLegacy.TAG, "closeBufferScope: scopeDepth=" + this.mBufferScopeDepth);
            }
            if (!isBuffering()) {
                flushBuffer();
            }
        }

        private void flushBuffer() {
            if (NotificationGroupManagerLegacy.SPEW) {
                Log.d(NotificationGroupManagerLegacy.TAG, "flushBuffer:  suppressed.size=" + this.mOldSuppressedByGroup.size() + " alertOverride.size=" + this.mOldAlertOverrideByGroup.size() + " mDidGroupsChange=" + this.mDidGroupsChange);
            }
            for (Map.Entry next : this.mOldSuppressedByGroup.entrySet()) {
                NotificationGroup apply = this.mGroupMapGetter.apply((String) next.getKey());
                if (apply != null) {
                    if (apply.suppressed != ((Boolean) next.getValue()).booleanValue()) {
                        notifySuppressedChanged(apply);
                    } else if (NotificationGroupManagerLegacy.SPEW) {
                        Log.d(NotificationGroupManagerLegacy.TAG, "flushBuffer: suppressed: did not change for group: " + NotificationUtils.logKey((String) next.getKey()));
                    }
                } else if (NotificationGroupManagerLegacy.SPEW) {
                    Log.d(NotificationGroupManagerLegacy.TAG, "flushBuffer: suppressed: cannot report for removed group: " + NotificationUtils.logKey((String) next.getKey()));
                }
            }
            this.mOldSuppressedByGroup.clear();
            for (Map.Entry next2 : this.mOldAlertOverrideByGroup.entrySet()) {
                NotificationGroup apply2 = this.mGroupMapGetter.apply((String) next2.getKey());
                if (apply2 != null) {
                    NotificationEntry notificationEntry = (NotificationEntry) next2.getValue();
                    if (apply2.alertOverride != notificationEntry) {
                        notifyAlertOverrideChanged(apply2, notificationEntry);
                    } else if (NotificationGroupManagerLegacy.SPEW) {
                        Log.d(NotificationGroupManagerLegacy.TAG, "flushBuffer: alertOverride: did not change for group: " + NotificationUtils.logKey((String) next2.getKey()));
                    }
                } else if (NotificationGroupManagerLegacy.SPEW) {
                    Log.d(NotificationGroupManagerLegacy.TAG, "flushBuffer: alertOverride: cannot report for removed group: " + ((String) next2.getKey()));
                }
            }
            this.mOldAlertOverrideByGroup.clear();
            if (this.mDidGroupsChange) {
                notifyGroupsChanged();
                this.mDidGroupsChange = false;
            }
        }
    }
}
