package com.android.systemui.statusbar.notification.logging;

import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Trace;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class NotificationLogger implements StatusBarStateController.StateListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "NotificationLogger";
    private static final int VISIBILITY_REPORT_MIN_DELAY_MS = 500;
    protected IStatusBarService mBarService;
    /* access modifiers changed from: private */
    public final ArraySet<NotificationVisibility> mCurrentlyVisibleNotifications = new ArraySet<>();
    private Boolean mDozing = null;
    private final Object mDozingLock = new Object();
    private final NotificationEntryManager mEntryManager;
    /* access modifiers changed from: private */
    public final ExpansionStateLogger mExpansionStateLogger;
    protected Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public long mLastVisibilityReportUptimeMs;
    /* access modifiers changed from: private */
    public NotificationListContainer mListContainer;
    private Boolean mLockscreen = null;
    private boolean mLogging = false;
    private final NotifLiveDataStore mNotifLiveDataStore;
    private final NotifPipeline mNotifPipeline;
    private final NotificationListenerService mNotificationListener;
    protected final OnChildLocationsChangedListener mNotificationLocationsChangedListener = new OnChildLocationsChangedListener() {
        public void onChildLocationsChanged() {
            if (!NotificationLogger.this.mHandler.hasCallbacks(NotificationLogger.this.mVisibilityReporter)) {
                NotificationLogger.this.mHandler.postAtTime(NotificationLogger.this.mVisibilityReporter, NotificationLogger.this.mLastVisibilityReportUptimeMs + 500);
            }
        }
    };
    private final NotificationPanelLogger mNotificationPanelLogger;
    private Boolean mPanelExpanded = null;
    private final Executor mUiBgExecutor;
    private final NotificationVisibilityProvider mVisibilityProvider;
    protected Runnable mVisibilityReporter = new Runnable() {
        private final ArraySet<NotificationVisibility> mTmpCurrentlyVisibleNotifications = new ArraySet<>();
        private final ArraySet<NotificationVisibility> mTmpNewlyVisibleNotifications = new ArraySet<>();
        private final ArraySet<NotificationVisibility> mTmpNoLongerVisibleNotifications = new ArraySet<>();

        public void run() {
            long unused = NotificationLogger.this.mLastVisibilityReportUptimeMs = SystemClock.uptimeMillis();
            List access$100 = NotificationLogger.this.getVisibleNotifications();
            int size = access$100.size();
            for (int i = 0; i < size; i++) {
                NotificationEntry notificationEntry = (NotificationEntry) access$100.get(i);
                String key = notificationEntry.getSbn().getKey();
                boolean isInVisibleLocation = NotificationLogger.this.mListContainer.isInVisibleLocation(notificationEntry);
                NotificationVisibility obtain = NotificationVisibility.obtain(key, i, size, isInVisibleLocation, NotificationLogger.getNotificationLocation(notificationEntry));
                boolean contains = NotificationLogger.this.mCurrentlyVisibleNotifications.contains(obtain);
                if (isInVisibleLocation) {
                    this.mTmpCurrentlyVisibleNotifications.add(obtain);
                    if (!contains) {
                        this.mTmpNewlyVisibleNotifications.add(obtain);
                    }
                } else {
                    obtain.recycle();
                }
            }
            this.mTmpNoLongerVisibleNotifications.addAll(NotificationLogger.this.mCurrentlyVisibleNotifications);
            this.mTmpNoLongerVisibleNotifications.removeAll(this.mTmpCurrentlyVisibleNotifications);
            NotificationLogger.this.logNotificationVisibilityChanges(this.mTmpNewlyVisibleNotifications, this.mTmpNoLongerVisibleNotifications);
            NotificationLogger notificationLogger = NotificationLogger.this;
            notificationLogger.recycleAllVisibilityObjects((ArraySet<NotificationVisibility>) notificationLogger.mCurrentlyVisibleNotifications);
            NotificationLogger.this.mCurrentlyVisibleNotifications.addAll(this.mTmpCurrentlyVisibleNotifications);
            ExpansionStateLogger access$600 = NotificationLogger.this.mExpansionStateLogger;
            ArraySet<NotificationVisibility> arraySet = this.mTmpCurrentlyVisibleNotifications;
            access$600.onVisibilityChanged(arraySet, arraySet);
            Trace.traceCounter(4096, "Notifications [Active]", size);
            Trace.traceCounter(4096, "Notifications [Visible]", NotificationLogger.this.mCurrentlyVisibleNotifications.size());
            NotificationLogger.this.recycleAllVisibilityObjects(this.mTmpNoLongerVisibleNotifications);
            this.mTmpCurrentlyVisibleNotifications.clear();
            this.mTmpNewlyVisibleNotifications.clear();
            this.mTmpNoLongerVisibleNotifications.clear();
        }
    };

    public interface OnChildLocationsChangedListener {
        void onChildLocationsChanged();
    }

    /* access modifiers changed from: private */
    public List<NotificationEntry> getVisibleNotifications() {
        return this.mNotifLiveDataStore.getActiveNotifList().getValue();
    }

    public static NotificationVisibility.NotificationLocation getNotificationLocation(NotificationEntry notificationEntry) {
        if (notificationEntry == null || notificationEntry.getRow() == null || notificationEntry.getRow().getViewState() == null) {
            return NotificationVisibility.NotificationLocation.LOCATION_UNKNOWN;
        }
        return convertNotificationLocation(notificationEntry.getRow().getViewState().location);
    }

    private static NotificationVisibility.NotificationLocation convertNotificationLocation(int i) {
        if (i == 1) {
            return NotificationVisibility.NotificationLocation.LOCATION_FIRST_HEADS_UP;
        }
        if (i == 2) {
            return NotificationVisibility.NotificationLocation.LOCATION_HIDDEN_TOP;
        }
        if (i == 4) {
            return NotificationVisibility.NotificationLocation.LOCATION_MAIN_AREA;
        }
        if (i == 8) {
            return NotificationVisibility.NotificationLocation.LOCATION_BOTTOM_STACK_PEEKING;
        }
        if (i == 16) {
            return NotificationVisibility.NotificationLocation.LOCATION_BOTTOM_STACK_HIDDEN;
        }
        if (i != 64) {
            return NotificationVisibility.NotificationLocation.LOCATION_UNKNOWN;
        }
        return NotificationVisibility.NotificationLocation.LOCATION_GONE;
    }

    public NotificationLogger(NotificationListener notificationListener, @UiBackground Executor executor, NotifPipelineFlags notifPipelineFlags, NotifLiveDataStore notifLiveDataStore, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, StatusBarStateController statusBarStateController, ExpansionStateLogger expansionStateLogger, NotificationPanelLogger notificationPanelLogger) {
        this.mNotificationListener = notificationListener;
        this.mUiBgExecutor = executor;
        this.mNotifLiveDataStore = notifLiveDataStore;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mEntryManager = notificationEntryManager;
        this.mNotifPipeline = notifPipeline;
        this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mExpansionStateLogger = expansionStateLogger;
        this.mNotificationPanelLogger = notificationPanelLogger;
        statusBarStateController.addCallback(this);
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            registerNewPipelineListener();
        } else {
            registerLegacyListener();
        }
    }

    private void registerLegacyListener() {
        this.mEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                NotificationLogger.this.mExpansionStateLogger.onEntryRemoved(notificationEntry.getKey());
            }

            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                NotificationLogger.this.mExpansionStateLogger.onEntryUpdated(notificationEntry.getKey());
            }

            public void onInflationError(StatusBarNotification statusBarNotification, Exception exc) {
                NotificationLogger.this.logNotificationError(statusBarNotification, exc);
            }
        });
    }

    private void registerNewPipelineListener() {
        this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() {
            public void onEntryUpdated(NotificationEntry notificationEntry, boolean z) {
                NotificationLogger.this.mExpansionStateLogger.onEntryUpdated(notificationEntry.getKey());
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                NotificationLogger.this.mExpansionStateLogger.onEntryRemoved(notificationEntry.getKey());
            }
        });
    }

    public void setUpWithContainer(NotificationListContainer notificationListContainer) {
        this.mListContainer = notificationListContainer;
    }

    public void stopNotificationLogging() {
        if (this.mLogging) {
            this.mLogging = false;
            if (DEBUG) {
                Log.i(TAG, "stopNotificationLogging: log notifications invisible");
            }
            if (!this.mCurrentlyVisibleNotifications.isEmpty()) {
                logNotificationVisibilityChanges(Collections.emptyList(), this.mCurrentlyVisibleNotifications);
                recycleAllVisibilityObjects(this.mCurrentlyVisibleNotifications);
            }
            this.mHandler.removeCallbacks(this.mVisibilityReporter);
            this.mListContainer.setChildLocationsChangedListener((OnChildLocationsChangedListener) null);
        }
    }

    public void startNotificationLogging() {
        if (!this.mLogging) {
            this.mLogging = true;
            if (DEBUG) {
                Log.i(TAG, "startNotificationLogging");
            }
            this.mListContainer.setChildLocationsChangedListener(this.mNotificationLocationsChangedListener);
            this.mNotificationLocationsChangedListener.onChildLocationsChanged();
        }
    }

    private void setDozing(boolean z) {
        synchronized (this.mDozingLock) {
            this.mDozing = Boolean.valueOf(z);
            maybeUpdateLoggingStatus();
        }
    }

    /* access modifiers changed from: private */
    public void logNotificationError(StatusBarNotification statusBarNotification, Exception exc) {
        try {
            this.mBarService.onNotificationError(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId(), statusBarNotification.getUid(), statusBarNotification.getInitialPid(), exc.getMessage(), statusBarNotification.getUserId());
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void logNotificationVisibilityChanges(Collection<NotificationVisibility> collection, Collection<NotificationVisibility> collection2) {
        if (!collection.isEmpty() || !collection2.isEmpty()) {
            this.mUiBgExecutor.execute(new NotificationLogger$$ExternalSyntheticLambda0(this, cloneVisibilitiesAsArr(collection), cloneVisibilitiesAsArr(collection2)));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$logNotificationVisibilityChanges$0$com-android-systemui-statusbar-notification-logging-NotificationLogger */
    public /* synthetic */ void mo40796xde1be78(NotificationVisibility[] notificationVisibilityArr, NotificationVisibility[] notificationVisibilityArr2) {
        try {
            this.mBarService.onNotificationVisibilityChanged(notificationVisibilityArr, notificationVisibilityArr2);
        } catch (RemoteException unused) {
        }
        int length = notificationVisibilityArr.length;
        if (length > 0) {
            String[] strArr = new String[length];
            for (int i = 0; i < length; i++) {
                strArr[i] = notificationVisibilityArr[i].key;
            }
            try {
                this.mNotificationListener.setNotificationsShown(strArr);
            } catch (RuntimeException e) {
                Log.d(TAG, "failed setNotificationsShown: ", e);
            }
        }
        recycleAllVisibilityObjects(notificationVisibilityArr);
        recycleAllVisibilityObjects(notificationVisibilityArr2);
    }

    /* access modifiers changed from: private */
    public void recycleAllVisibilityObjects(ArraySet<NotificationVisibility> arraySet) {
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            arraySet.valueAt(i).recycle();
        }
        arraySet.clear();
    }

    private void recycleAllVisibilityObjects(NotificationVisibility[] notificationVisibilityArr) {
        for (NotificationVisibility notificationVisibility : notificationVisibilityArr) {
            if (notificationVisibility != null) {
                notificationVisibility.recycle();
            }
        }
    }

    /* access modifiers changed from: private */
    public static NotificationVisibility[] cloneVisibilitiesAsArr(Collection<NotificationVisibility> collection) {
        NotificationVisibility[] notificationVisibilityArr = new NotificationVisibility[collection.size()];
        int i = 0;
        for (NotificationVisibility next : collection) {
            if (next != null) {
                notificationVisibilityArr[i] = next.clone();
            }
            i++;
        }
        return notificationVisibilityArr;
    }

    public Runnable getVisibilityReporter() {
        return this.mVisibilityReporter;
    }

    public void onStateChanged(int i) {
        if (DEBUG) {
            Log.i(TAG, "onStateChanged: new=" + i);
        }
        synchronized (this.mDozingLock) {
            boolean z = true;
            if (!(i == 1 || i == 2)) {
                z = false;
            }
            this.mLockscreen = Boolean.valueOf(z);
        }
    }

    public void onDozingChanged(boolean z) {
        if (DEBUG) {
            Log.i(TAG, "onDozingChanged: new=" + z);
        }
        setDozing(z);
    }

    private void maybeUpdateLoggingStatus() {
        boolean z = false;
        if (this.mPanelExpanded != null && this.mDozing != null) {
            Boolean bool = this.mLockscreen;
            if (bool != null) {
                z = bool.booleanValue();
            }
            if (!this.mPanelExpanded.booleanValue() || this.mDozing.booleanValue()) {
                if (DEBUG) {
                    Log.i(TAG, "Notification panel hidden, lockscreen=" + z);
                }
                stopNotificationLogging();
                return;
            }
            this.mNotificationPanelLogger.logPanelShown(z, getVisibleNotifications());
            if (DEBUG) {
                Log.i(TAG, "Notification panel shown, lockscreen=" + z);
            }
            startNotificationLogging();
        } else if (DEBUG) {
            StringBuilder append = new StringBuilder("Panel status unclear: panelExpandedKnown=").append(this.mPanelExpanded == null).append(" dozingKnown=");
            if (this.mDozing == null) {
                z = true;
            }
            Log.i(TAG, append.append(z).toString());
        }
    }

    public void onPanelExpandedChanged(boolean z) {
        if (DEBUG) {
            Log.i(TAG, "onPanelExpandedChanged: new=" + z);
        }
        this.mPanelExpanded = Boolean.valueOf(z);
        synchronized (this.mDozingLock) {
            maybeUpdateLoggingStatus();
        }
    }

    public void onExpansionChanged(String str, boolean z, boolean z2) {
        this.mExpansionStateLogger.onExpansionChanged(str, z, z2, this.mVisibilityProvider.getLocation(str));
    }

    public void setVisibilityReporter(Runnable runnable) {
        this.mVisibilityReporter = runnable;
    }

    public static class ExpansionStateLogger {
        IStatusBarService mBarService;
        private final Map<String, State> mExpansionStates = new ArrayMap();
        private final Map<String, Boolean> mLoggedExpansionState = new ArrayMap();
        private final Executor mUiBgExecutor;

        @Inject
        public ExpansionStateLogger(@UiBackground Executor executor) {
            this.mUiBgExecutor = executor;
            this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        }

        /* access modifiers changed from: package-private */
        public void onExpansionChanged(String str, boolean z, boolean z2, NotificationVisibility.NotificationLocation notificationLocation) {
            State state = getState(str);
            state.mIsUserAction = Boolean.valueOf(z);
            state.mIsExpanded = Boolean.valueOf(z2);
            state.mLocation = notificationLocation;
            maybeNotifyOnNotificationExpansionChanged(str, state);
        }

        /* access modifiers changed from: package-private */
        public void onVisibilityChanged(Collection<NotificationVisibility> collection, Collection<NotificationVisibility> collection2) {
            NotificationVisibility[] access$800 = NotificationLogger.cloneVisibilitiesAsArr(collection);
            NotificationVisibility[] access$8002 = NotificationLogger.cloneVisibilitiesAsArr(collection2);
            for (NotificationVisibility notificationVisibility : access$800) {
                State state = getState(notificationVisibility.key);
                state.mIsVisible = true;
                state.mLocation = notificationVisibility.location;
                maybeNotifyOnNotificationExpansionChanged(notificationVisibility.key, state);
            }
            for (NotificationVisibility notificationVisibility2 : access$8002) {
                getState(notificationVisibility2.key).mIsVisible = false;
            }
        }

        /* access modifiers changed from: package-private */
        public void onEntryRemoved(String str) {
            this.mExpansionStates.remove(str);
            this.mLoggedExpansionState.remove(str);
        }

        /* access modifiers changed from: package-private */
        public void onEntryUpdated(String str) {
            this.mLoggedExpansionState.remove(str);
        }

        private State getState(String str) {
            State state = this.mExpansionStates.get(str);
            if (state != null) {
                return state;
            }
            State state2 = new State();
            this.mExpansionStates.put(str, state2);
            return state2;
        }

        private void maybeNotifyOnNotificationExpansionChanged(String str, State state) {
            if (state.isFullySet() && state.mIsVisible.booleanValue()) {
                Boolean bool = this.mLoggedExpansionState.get(str);
                if (bool == null && !state.mIsExpanded.booleanValue()) {
                    return;
                }
                if (bool == null || state.mIsExpanded != bool) {
                    this.mLoggedExpansionState.put(str, state.mIsExpanded);
                    this.mUiBgExecutor.execute(new C2735x56ec96fa(this, str, new State(state)));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$maybeNotifyOnNotificationExpansionChanged$0$com-android-systemui-statusbar-notification-logging-NotificationLogger$ExpansionStateLogger */
        public /* synthetic */ void mo40804xe665ec22(String str, State state) {
            try {
                this.mBarService.onNotificationExpansionChanged(str, state.mIsUserAction.booleanValue(), state.mIsExpanded.booleanValue(), state.mLocation.ordinal());
            } catch (RemoteException e) {
                Log.e(NotificationLogger.TAG, "Failed to call onNotificationExpansionChanged: ", e);
            }
        }

        private static class State {
            Boolean mIsExpanded;
            Boolean mIsUserAction;
            Boolean mIsVisible;
            NotificationVisibility.NotificationLocation mLocation;

            private State() {
            }

            private State(State state) {
                this.mIsUserAction = state.mIsUserAction;
                this.mIsExpanded = state.mIsExpanded;
                this.mIsVisible = state.mIsVisible;
                this.mLocation = state.mLocation;
            }

            /* access modifiers changed from: private */
            public boolean isFullySet() {
                return (this.mIsUserAction == null || this.mIsExpanded == null || this.mIsVisible == null || this.mLocation == null) ? false : true;
            }
        }
    }
}
