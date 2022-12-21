package com.android.systemui.statusbar;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.systemui.Dependency;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.Iterator;
import java.util.stream.Stream;

public abstract class AlertingNotificationManager implements NotificationLifetimeExtender {
    private static final String TAG = "AlertNotifManager";
    /* access modifiers changed from: protected */
    public final ArrayMap<String, AlertEntry> mAlertEntries = new ArrayMap<>();
    protected int mAutoDismissNotificationDecay;
    protected final Clock mClock = new Clock();
    protected final ArraySet<NotificationEntry> mExtendedLifetimeAlertEntries = new ArraySet<>();
    public Handler mHandler = new Handler(Looper.getMainLooper());
    protected final HeadsUpManagerLogger mLogger;
    protected int mMinimumDisplayTime;
    protected NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;

    public abstract int getContentFlag();

    /* access modifiers changed from: protected */
    public abstract void onAlertEntryAdded(AlertEntry alertEntry);

    /* access modifiers changed from: protected */
    public abstract void onAlertEntryRemoved(AlertEntry alertEntry);

    public AlertingNotificationManager(HeadsUpManagerLogger headsUpManagerLogger) {
        this.mLogger = headsUpManagerLogger;
    }

    public void showNotification(NotificationEntry notificationEntry) {
        this.mLogger.logShowNotification(notificationEntry.getKey());
        addAlertEntry(notificationEntry);
        updateNotification(notificationEntry.getKey(), true);
        notificationEntry.setInterruption();
    }

    public boolean removeNotification(String str, boolean z) {
        this.mLogger.logRemoveNotification(str, z);
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry == null) {
            return true;
        }
        if (z || canRemoveImmediately(str)) {
            removeAlertEntry(str);
            return true;
        }
        alertEntry.removeAsSoonAsPossible();
        return false;
    }

    public void updateNotification(String str, boolean z) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        this.mLogger.logUpdateNotification(str, z, alertEntry != null);
        if (alertEntry != null) {
            alertEntry.mEntry.sendAccessibilityEvent(2048);
            if (z) {
                alertEntry.updateEntry(true);
            }
        }
    }

    public void releaseAllImmediately() {
        this.mLogger.logReleaseAllImmediately();
        Iterator it = new ArraySet(this.mAlertEntries.keySet()).iterator();
        while (it.hasNext()) {
            removeAlertEntry((String) it.next());
        }
    }

    public NotificationEntry getEntry(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            return alertEntry.mEntry;
        }
        return null;
    }

    public Stream<NotificationEntry> getAllEntries() {
        return this.mAlertEntries.values().stream().map(new AlertingNotificationManager$$ExternalSyntheticLambda0());
    }

    public boolean hasNotifications() {
        return !this.mAlertEntries.isEmpty();
    }

    public boolean isAlerting(String str) {
        return this.mAlertEntries.containsKey(str);
    }

    /* access modifiers changed from: protected */
    public final void addAlertEntry(NotificationEntry notificationEntry) {
        AlertEntry createAlertEntry = createAlertEntry();
        createAlertEntry.setEntry(notificationEntry);
        this.mAlertEntries.put(notificationEntry.getKey(), createAlertEntry);
        onAlertEntryAdded(createAlertEntry);
        notificationEntry.sendAccessibilityEvent(2048);
        notificationEntry.setIsAlerting(true);
    }

    /* access modifiers changed from: protected */
    public final void removeAlertEntry(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            NotificationEntry notificationEntry = alertEntry.mEntry;
            if (notificationEntry == null || !notificationEntry.isExpandAnimationRunning()) {
                this.mAlertEntries.remove(str);
                onAlertEntryRemoved(alertEntry);
                notificationEntry.sendAccessibilityEvent(2048);
                alertEntry.reset();
                if (this.mExtendedLifetimeAlertEntries.contains(notificationEntry)) {
                    NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
                    if (notificationSafeToRemoveCallback != null) {
                        notificationSafeToRemoveCallback.onSafeToRemove(str);
                    }
                    this.mExtendedLifetimeAlertEntries.remove(notificationEntry);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public AlertEntry createAlertEntry() {
        return new AlertEntry();
    }

    public boolean canRemoveImmediately(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        return alertEntry == null || alertEntry.wasShownLongEnough() || alertEntry.mEntry.isRowDismissed();
    }

    public void setCallback(NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback) {
        this.mNotificationLifetimeFinishedCallback = notificationSafeToRemoveCallback;
    }

    public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        return !canRemoveImmediately(notificationEntry.getKey());
    }

    public boolean isSticky(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            return alertEntry.isSticky();
        }
        return false;
    }

    public long getEarliestRemovalTime(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            return Math.max(0, alertEntry.mEarliestRemovaltime - this.mClock.currentTimeMillis());
        }
        return 0;
    }

    public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mExtendedLifetimeAlertEntries.add(notificationEntry);
            this.mAlertEntries.get(notificationEntry.getKey()).removeAsSoonAsPossible();
            return;
        }
        this.mExtendedLifetimeAlertEntries.remove(notificationEntry);
    }

    protected class AlertEntry implements Comparable<AlertEntry> {
        public long mEarliestRemovaltime;
        public NotificationEntry mEntry;
        public long mPostTime;
        protected Runnable mRemoveAlertRunnable;

        public boolean isSticky() {
            return false;
        }

        protected AlertEntry() {
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setEntry$0$com-android-systemui-statusbar-AlertingNotificationManager$AlertEntry */
        public /* synthetic */ void mo38311xbf8c1f(NotificationEntry notificationEntry) {
            AlertingNotificationManager.this.removeAlertEntry(notificationEntry.getKey());
        }

        public void setEntry(NotificationEntry notificationEntry) {
            setEntry(notificationEntry, new AlertingNotificationManager$AlertEntry$$ExternalSyntheticLambda1(this, notificationEntry));
        }

        public void setEntry(NotificationEntry notificationEntry, Runnable runnable) {
            this.mEntry = notificationEntry;
            this.mRemoveAlertRunnable = runnable;
            this.mPostTime = calculatePostTime();
            updateEntry(true);
        }

        public void updateEntry(boolean z) {
            AlertingNotificationManager.this.mLogger.logUpdateEntry(this.mEntry.getKey(), z);
            long currentTimeMillis = AlertingNotificationManager.this.mClock.currentTimeMillis();
            this.mEarliestRemovaltime = ((long) AlertingNotificationManager.this.mMinimumDisplayTime) + currentTimeMillis;
            if (z) {
                this.mPostTime = Math.max(this.mPostTime, currentTimeMillis);
            }
            removeAutoRemovalCallbacks();
            if (!isSticky()) {
                long max = Math.max(calculateFinishTime() - currentTimeMillis, (long) AlertingNotificationManager.this.mMinimumDisplayTime);
                NotificationPanelViewController notificationPanelViewController = ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).getNotificationPanelViewController();
                MediaDataManager mediaDataManager = (MediaDataManager) NTDependencyEx.get(MediaDataManager.class);
                if (!notificationPanelViewController.isFullyExpanded() || !mediaDataManager.hasActiveMedia() || ((StatusBarStateController) Dependency.get(StatusBarStateController.class)).isDozing()) {
                    NTLogUtil.m1680d(AlertingNotificationManager.TAG, "removeAlertRunnable delay = " + max);
                    AlertingNotificationManager.this.mHandler.postDelayed(this.mRemoveAlertRunnable, max);
                    return;
                }
                AlertingNotificationManager.this.mHandler.post(new AlertingNotificationManager$AlertEntry$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$updateEntry$1$com-android-systemui-statusbar-AlertingNotificationManager$AlertEntry */
        public /* synthetic */ void mo38312x6e19aebf() {
            NTLogUtil.m1680d(AlertingNotificationManager.TAG, "removeAlertRunnable immediately");
            this.mRemoveAlertRunnable = null;
            NotificationEntry notificationEntry = this.mEntry;
            if (notificationEntry != null && notificationEntry.getKey() != null) {
                AlertingNotificationManager.this.removeAlertEntry(this.mEntry.getKey());
            }
        }

        public boolean wasShownLongEnough() {
            return this.mEarliestRemovaltime < AlertingNotificationManager.this.mClock.currentTimeMillis();
        }

        public int compareTo(AlertEntry alertEntry) {
            long j = this.mPostTime;
            long j2 = alertEntry.mPostTime;
            if (j < j2) {
                return 1;
            }
            if (j == j2) {
                return this.mEntry.getKey().compareTo(alertEntry.mEntry.getKey());
            }
            return -1;
        }

        public void reset() {
            this.mEntry = null;
            removeAutoRemovalCallbacks();
            this.mRemoveAlertRunnable = null;
        }

        public void removeAutoRemovalCallbacks() {
            if (this.mRemoveAlertRunnable != null) {
                AlertingNotificationManager.this.mHandler.removeCallbacks(this.mRemoveAlertRunnable);
            }
        }

        public void removeAsSoonAsPossible() {
            if (this.mRemoveAlertRunnable != null) {
                removeAutoRemovalCallbacks();
                AlertingNotificationManager.this.mHandler.postDelayed(this.mRemoveAlertRunnable, this.mEarliestRemovaltime - AlertingNotificationManager.this.mClock.currentTimeMillis());
            }
        }

        /* access modifiers changed from: protected */
        public long calculatePostTime() {
            return AlertingNotificationManager.this.mClock.currentTimeMillis();
        }

        /* access modifiers changed from: protected */
        public long calculateFinishTime() {
            return this.mPostTime + ((long) AlertingNotificationManager.this.mAutoDismissNotificationDecay);
        }
    }

    protected static final class Clock {
        protected Clock() {
        }

        public long currentTimeMillis() {
            return SystemClock.elapsedRealtime();
        }
    }
}
