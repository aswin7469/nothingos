package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.metrics.LogMaker;
import android.util.Log;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationCounters;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NotificationBlockingHelperManager {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_ALWAYS_SHOW = false;
    private static final String TAG = "BlockingHelper";
    private ExpandableNotificationRow mBlockingHelperRow;
    private final Context mContext;
    private final GroupMembershipManager mGroupMembershipManager;
    private boolean mIsShadeExpanded;
    private final MetricsLogger mMetricsLogger;
    private Set<String> mNonBlockablePkgs;
    private final NotificationEntryManager mNotificationEntryManager;
    private final NotificationGutsManager mNotificationGutsManager;

    public NotificationBlockingHelperManager(Context context, NotificationGutsManager notificationGutsManager, NotificationEntryManager notificationEntryManager, MetricsLogger metricsLogger, GroupMembershipManager groupMembershipManager) {
        this.mContext = context;
        this.mNotificationGutsManager = notificationGutsManager;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mMetricsLogger = metricsLogger;
        HashSet hashSet = new HashSet();
        this.mNonBlockablePkgs = hashSet;
        Collections.addAll(hashSet, context.getResources().getStringArray(17236097));
        this.mGroupMembershipManager = groupMembershipManager;
    }

    /* access modifiers changed from: package-private */
    public boolean perhapsShowBlockingHelper(ExpandableNotificationRow expandableNotificationRow, NotificationMenuRowPlugin notificationMenuRowPlugin) {
        NotificationEntry entry = expandableNotificationRow.getEntry();
        if (entry.getUserSentiment() != -1 || !this.mIsShadeExpanded || expandableNotificationRow.getIsNonblockable() || ((expandableNotificationRow.isChildInGroup() && !this.mGroupMembershipManager.isOnlyChildInGroup(entry)) || expandableNotificationRow.getNumUniqueChannels() > 1)) {
            return false;
        }
        dismissCurrentBlockingHelper();
        if (DEBUG) {
            Log.d(TAG, "Manager.perhapsShowBlockingHelper: Showing new blocking helper");
        }
        this.mBlockingHelperRow = expandableNotificationRow;
        expandableNotificationRow.setBlockingHelperShowing(true);
        this.mMetricsLogger.write(getLogMaker().setSubtype(3));
        this.mNotificationGutsManager.openGuts(this.mBlockingHelperRow, 0, 0, notificationMenuRowPlugin.getLongpressMenuItem(this.mContext));
        this.mMetricsLogger.count(NotificationCounters.BLOCKING_HELPER_SHOWN, 1);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean dismissCurrentBlockingHelper() {
        if (isBlockingHelperRowNull()) {
            return false;
        }
        if (DEBUG) {
            Log.d(TAG, "Manager.dismissCurrentBlockingHelper: Dismissing current helper");
        }
        if (!this.mBlockingHelperRow.isBlockingHelperShowing()) {
            Log.e(TAG, "Manager.dismissCurrentBlockingHelper: Non-null row is not showing a blocking helper");
        }
        this.mBlockingHelperRow.setBlockingHelperShowing(false);
        if (this.mBlockingHelperRow.isAttachedToWindow()) {
            this.mNotificationEntryManager.updateNotifications("dismissCurrentBlockingHelper");
        }
        this.mBlockingHelperRow = null;
        return true;
    }

    public void setNotificationShadeExpanded(float f) {
        this.mIsShadeExpanded = f > 0.0f;
    }

    public boolean isNonblockable(String str, String str2) {
        return this.mNonBlockablePkgs.contains(str) || this.mNonBlockablePkgs.contains(makeChannelKey(str, str2));
    }

    private LogMaker getLogMaker() {
        return this.mBlockingHelperRow.getEntry().getSbn().getLogMaker().setCategory(1621);
    }

    private String makeChannelKey(String str, String str2) {
        return str + ":" + str2;
    }

    /* access modifiers changed from: package-private */
    public boolean isBlockingHelperRowNull() {
        return this.mBlockingHelperRow == null;
    }

    /* access modifiers changed from: package-private */
    public void setBlockingHelperRowForTest(ExpandableNotificationRow expandableNotificationRow) {
        this.mBlockingHelperRow = expandableNotificationRow;
    }

    /* access modifiers changed from: package-private */
    public void setNonBlockablePkgs(String[] strArr) {
        HashSet hashSet = new HashSet();
        this.mNonBlockablePkgs = hashSet;
        Collections.addAll(hashSet, strArr);
    }
}
