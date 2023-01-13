package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.provider.Settings;
import android.util.ArrayMap;
import androidx.core.app.NotificationCompat;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.EventLogTags;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.ListenerSet;
import java.p026io.PrintWriter;
import java.util.Iterator;

public abstract class HeadsUpManager extends AlertingNotificationManager {
    private static final String SETTING_HEADS_UP_SNOOZE_LENGTH_MS = "heads_up_snooze_length_ms";
    private static final String TAG = "HeadsUpManager";
    /* access modifiers changed from: private */
    public final AccessibilityManagerWrapper mAccessibilityMgr;
    protected final Context mContext;
    protected boolean mHasPinnedNotification;
    protected final ListenerSet<OnHeadsUpChangedListener> mListeners = new ListenerSet<>();
    protected int mSnoozeLengthMs;
    private final ArrayMap<String, Long> mSnoozedPackages;
    protected int mTouchAcceptanceDelay;
    private final UiEventLogger mUiEventLogger;
    protected int mUser;

    public int getContentFlag() {
        return 4;
    }

    public boolean isTrackingHeadsUp() {
        return false;
    }

    public void onDensityOrFontScaleChanged() {
    }

    enum NotificationPeekEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_PEEK(801);
        
        private final int mId;

        private NotificationPeekEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public HeadsUpManager(final Context context, HeadsUpManagerLogger headsUpManagerLogger) {
        super(headsUpManagerLogger);
        this.mContext = context;
        this.mAccessibilityMgr = (AccessibilityManagerWrapper) Dependency.get(AccessibilityManagerWrapper.class);
        this.mUiEventLogger = (UiEventLogger) Dependency.get(UiEventLogger.class);
        Resources resources = context.getResources();
        this.mMinimumDisplayTime = resources.getInteger(C1894R.integer.heads_up_notification_minimum_time);
        this.mAutoDismissNotificationDecay = resources.getInteger(C1894R.integer.heads_up_notification_decay);
        this.mTouchAcceptanceDelay = resources.getInteger(C1894R.integer.touch_acceptance_delay);
        this.mSnoozedPackages = new ArrayMap<>();
        this.mSnoozeLengthMs = Settings.Global.getInt(context.getContentResolver(), SETTING_HEADS_UP_SNOOZE_LENGTH_MS, resources.getInteger(C1894R.integer.heads_up_default_snooze_length_ms));
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor(SETTING_HEADS_UP_SNOOZE_LENGTH_MS), false, new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                int i = Settings.Global.getInt(context.getContentResolver(), HeadsUpManager.SETTING_HEADS_UP_SNOOZE_LENGTH_MS, -1);
                if (i > -1 && i != HeadsUpManager.this.mSnoozeLengthMs) {
                    HeadsUpManager.this.mSnoozeLengthMs = i;
                    HeadsUpManager.this.mLogger.logSnoozeLengthChange(i);
                }
            }
        });
    }

    public void addListener(OnHeadsUpChangedListener onHeadsUpChangedListener) {
        this.mListeners.addIfAbsent(onHeadsUpChangedListener);
    }

    public void removeListener(OnHeadsUpChangedListener onHeadsUpChangedListener) {
        this.mListeners.remove(onHeadsUpChangedListener);
    }

    public void updateNotification(String str, boolean z) {
        super.updateNotification(str, z);
        HeadsUpEntry headsUpEntry = getHeadsUpEntry(str);
        if (z && headsUpEntry != null) {
            setEntryPinned(headsUpEntry, shouldHeadsUpBecomePinned(headsUpEntry.mEntry));
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldHeadsUpBecomePinned(NotificationEntry notificationEntry) {
        return hasFullScreenIntent(notificationEntry);
    }

    /* access modifiers changed from: protected */
    public boolean hasFullScreenIntent(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().fullScreenIntent != null;
    }

    /* access modifiers changed from: protected */
    public void setEntryPinned(HeadsUpEntry headsUpEntry, boolean z) {
        this.mLogger.logSetEntryPinned(headsUpEntry.mEntry.getKey(), z);
        NotificationEntry notificationEntry = headsUpEntry.mEntry;
        if (notificationEntry.isRowPinned() != z) {
            notificationEntry.setRowPinned(z);
            updatePinnedMode();
            if (z && notificationEntry.getSbn() != null) {
                this.mUiEventLogger.logWithInstanceId(NotificationPeekEvent.NOTIFICATION_PEEK, notificationEntry.getSbn().getUid(), notificationEntry.getSbn().getPackageName(), notificationEntry.getSbn().getInstanceId());
            }
            Iterator<OnHeadsUpChangedListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                OnHeadsUpChangedListener next = it.next();
                if (z) {
                    next.onHeadsUpPinned(notificationEntry);
                } else {
                    next.onHeadsUpUnPinned(notificationEntry);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAlertEntryAdded(AlertingNotificationManager.AlertEntry alertEntry) {
        NotificationEntry notificationEntry = alertEntry.mEntry;
        notificationEntry.setHeadsUp(true);
        setEntryPinned((HeadsUpEntry) alertEntry, shouldHeadsUpBecomePinned(notificationEntry));
        EventLogTags.writeSysuiHeadsUpStatus(notificationEntry.getKey(), 1);
        Iterator<OnHeadsUpChangedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onHeadsUpStateChanged(notificationEntry, true);
        }
    }

    /* access modifiers changed from: protected */
    public void onAlertEntryRemoved(AlertingNotificationManager.AlertEntry alertEntry) {
        NotificationEntry notificationEntry = alertEntry.mEntry;
        notificationEntry.setHeadsUp(false);
        setEntryPinned((HeadsUpEntry) alertEntry, false);
        EventLogTags.writeSysuiHeadsUpStatus(notificationEntry.getKey(), 0);
        this.mLogger.logNotificationActuallyRemoved(notificationEntry.getKey());
        Iterator<OnHeadsUpChangedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onHeadsUpStateChanged(notificationEntry, false);
        }
    }

    /* access modifiers changed from: protected */
    public void updatePinnedMode() {
        boolean hasPinnedNotificationInternal = hasPinnedNotificationInternal();
        if (hasPinnedNotificationInternal != this.mHasPinnedNotification) {
            this.mLogger.logUpdatePinnedMode(hasPinnedNotificationInternal);
            this.mHasPinnedNotification = hasPinnedNotificationInternal;
            if (hasPinnedNotificationInternal) {
                MetricsLogger.count(this.mContext, "note_peek", 1);
            }
            Iterator<OnHeadsUpChangedListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                it.next().onHeadsUpPinnedModeChanged(hasPinnedNotificationInternal);
            }
        }
    }

    public boolean isSnoozed(String str) {
        String snoozeKey = snoozeKey(str, this.mUser);
        Long l = this.mSnoozedPackages.get(snoozeKey);
        if (l == null) {
            return false;
        }
        if (l.longValue() > this.mClock.currentTimeMillis()) {
            this.mLogger.logIsSnoozedReturned(snoozeKey);
            return true;
        }
        this.mLogger.logPackageUnsnoozed(snoozeKey);
        this.mSnoozedPackages.remove(snoozeKey);
        return false;
    }

    public void snooze() {
        for (String headsUpEntry : this.mAlertEntries.keySet()) {
            String snoozeKey = snoozeKey(getHeadsUpEntry(headsUpEntry).mEntry.getSbn().getPackageName(), this.mUser);
            this.mLogger.logPackageSnoozed(snoozeKey);
            this.mSnoozedPackages.put(snoozeKey, Long.valueOf(this.mClock.currentTimeMillis() + ((long) this.mSnoozeLengthMs)));
        }
    }

    private static String snoozeKey(String str, int i) {
        return i + NavigationBarInflaterView.BUTTON_SEPARATOR + str;
    }

    /* access modifiers changed from: protected */
    public HeadsUpEntry getHeadsUpEntry(String str) {
        return (HeadsUpEntry) this.mAlertEntries.get(str);
    }

    public NotificationEntry getTopEntry() {
        HeadsUpEntry topHeadsUpEntry = getTopHeadsUpEntry();
        if (topHeadsUpEntry != null) {
            return topHeadsUpEntry.mEntry;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public HeadsUpEntry getTopHeadsUpEntry() {
        HeadsUpEntry headsUpEntry = null;
        if (this.mAlertEntries.isEmpty()) {
            return null;
        }
        for (AlertingNotificationManager.AlertEntry alertEntry : this.mAlertEntries.values()) {
            if (headsUpEntry == null || alertEntry.compareTo((AlertingNotificationManager.AlertEntry) headsUpEntry) < 0) {
                headsUpEntry = (HeadsUpEntry) alertEntry;
            }
        }
        return headsUpEntry;
    }

    public void setUser(int i) {
        this.mUser = i;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("HeadsUpManager state:");
        dumpInternal(printWriter, strArr);
    }

    /* access modifiers changed from: protected */
    public void dumpInternal(PrintWriter printWriter, String[] strArr) {
        printWriter.print("  mTouchAcceptanceDelay=");
        printWriter.println(this.mTouchAcceptanceDelay);
        printWriter.print("  mSnoozeLengthMs=");
        printWriter.println(this.mSnoozeLengthMs);
        printWriter.print("  now=");
        printWriter.println(this.mClock.currentTimeMillis());
        printWriter.print("  mUser=");
        printWriter.println(this.mUser);
        for (AlertingNotificationManager.AlertEntry alertEntry : this.mAlertEntries.values()) {
            printWriter.print("  HeadsUpEntry=");
            printWriter.println((Object) alertEntry.mEntry);
        }
        int size = this.mSnoozedPackages.size();
        printWriter.println("  snoozed packages: " + size);
        for (int i = 0; i < size; i++) {
            printWriter.print("    ");
            printWriter.print((Object) this.mSnoozedPackages.valueAt(i));
            printWriter.print(", ");
            printWriter.println(this.mSnoozedPackages.keyAt(i));
        }
    }

    public boolean hasPinnedHeadsUp() {
        return this.mHasPinnedNotification;
    }

    private boolean hasPinnedNotificationInternal() {
        for (String headsUpEntry : this.mAlertEntries.keySet()) {
            if (getHeadsUpEntry(headsUpEntry).mEntry.isRowPinned()) {
                return true;
            }
        }
        return false;
    }

    public void unpinAll(boolean z) {
        for (String headsUpEntry : this.mAlertEntries.keySet()) {
            HeadsUpEntry headsUpEntry2 = getHeadsUpEntry(headsUpEntry);
            setEntryPinned(headsUpEntry2, false);
            headsUpEntry2.updateEntry(false);
            if (z && headsUpEntry2.mEntry != null && headsUpEntry2.mEntry.mustStayOnScreen()) {
                headsUpEntry2.mEntry.setHeadsUpIsVisible();
            }
        }
    }

    public int compare(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        boolean z = true;
        if (notificationEntry == null || notificationEntry2 == null) {
            boolean z2 = notificationEntry == null;
            if (notificationEntry2 != null) {
                z = false;
            }
            return Boolean.compare(z2, z);
        }
        HeadsUpEntry headsUpEntry = getHeadsUpEntry(notificationEntry.getKey());
        HeadsUpEntry headsUpEntry2 = getHeadsUpEntry(notificationEntry2.getKey());
        if (headsUpEntry != null && headsUpEntry2 != null) {
            return headsUpEntry.compareTo((AlertingNotificationManager.AlertEntry) headsUpEntry2);
        }
        boolean z3 = headsUpEntry == null;
        if (headsUpEntry2 != null) {
            z = false;
        }
        return Boolean.compare(z3, z);
    }

    public void setExpanded(NotificationEntry notificationEntry, boolean z) {
        HeadsUpEntry headsUpEntry = getHeadsUpEntry(notificationEntry.getKey());
        if (headsUpEntry != null && notificationEntry.isRowPinned()) {
            headsUpEntry.setExpanded(z);
        }
    }

    /* access modifiers changed from: protected */
    public HeadsUpEntry createAlertEntry() {
        return new HeadsUpEntry();
    }

    /* access modifiers changed from: private */
    public static boolean isCriticalCallNotif(NotificationEntry notificationEntry) {
        Notification notification = notificationEntry.getSbn().getNotification();
        if ((notification.isStyle(Notification.CallStyle.class) && notification.extras.getInt("android.callType") == 1) || (notificationEntry.getSbn().isOngoing() && NotificationCompat.CATEGORY_CALL.equals(notification.category))) {
            return true;
        }
        return false;
    }

    protected class HeadsUpEntry extends AlertingNotificationManager.AlertEntry {
        protected boolean expanded;
        public boolean remoteInputActive;

        protected HeadsUpEntry() {
            super();
        }

        public boolean isSticky() {
            return (this.mEntry.isRowPinned() && this.expanded) || this.remoteInputActive || HeadsUpManager.this.hasFullScreenIntent(this.mEntry);
        }

        public int compareTo(AlertingNotificationManager.AlertEntry alertEntry) {
            HeadsUpEntry headsUpEntry = (HeadsUpEntry) alertEntry;
            boolean isRowPinned = this.mEntry.isRowPinned();
            boolean isRowPinned2 = headsUpEntry.mEntry.isRowPinned();
            if (isRowPinned && !isRowPinned2) {
                return -1;
            }
            if (!isRowPinned && isRowPinned2) {
                return 1;
            }
            boolean hasFullScreenIntent = HeadsUpManager.this.hasFullScreenIntent(this.mEntry);
            boolean hasFullScreenIntent2 = HeadsUpManager.this.hasFullScreenIntent(headsUpEntry.mEntry);
            if (hasFullScreenIntent && !hasFullScreenIntent2) {
                return -1;
            }
            if (!hasFullScreenIntent && hasFullScreenIntent2) {
                return 1;
            }
            boolean access$100 = HeadsUpManager.isCriticalCallNotif(this.mEntry);
            boolean access$1002 = HeadsUpManager.isCriticalCallNotif(headsUpEntry.mEntry);
            if (access$100 && !access$1002) {
                return -1;
            }
            if (!access$100 && access$1002) {
                return 1;
            }
            boolean z = this.remoteInputActive;
            if (z && !headsUpEntry.remoteInputActive) {
                return -1;
            }
            if (z || !headsUpEntry.remoteInputActive) {
                return super.compareTo((AlertingNotificationManager.AlertEntry) headsUpEntry);
            }
            return 1;
        }

        public void setExpanded(boolean z) {
            this.expanded = z;
        }

        public void reset() {
            super.reset();
            this.expanded = false;
            this.remoteInputActive = false;
        }

        /* access modifiers changed from: protected */
        public long calculatePostTime() {
            return super.calculatePostTime() + ((long) HeadsUpManager.this.mTouchAcceptanceDelay);
        }

        /* access modifiers changed from: protected */
        public long calculateFinishTime() {
            return this.mPostTime + ((long) getRecommendedHeadsUpTimeoutMs(HeadsUpManager.this.mAutoDismissNotificationDecay));
        }

        /* access modifiers changed from: protected */
        public int getRecommendedHeadsUpTimeoutMs(int i) {
            return HeadsUpManager.this.mAccessibilityMgr.getRecommendedTimeoutMillis(i, 7);
        }
    }

    public void snoozePackage(String str) {
        this.mSnoozedPackages.put(snoozeKey(str, this.mUser), Long.valueOf(this.mClock.currentTimeMillis() + ((long) this.mSnoozeLengthMs)));
    }
}
