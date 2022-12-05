package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.IActivityManager;
import android.app.IUidObserver;
import android.app.PendingIntent;
import android.util.Log;
import android.view.View;
import com.android.systemui.R$id;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: OngoingCallController.kt */
/* loaded from: classes.dex */
public final class OngoingCallController implements CallbackController<OngoingCallListener> {
    @NotNull
    private final ActivityStarter activityStarter;
    @Nullable
    private CallNotificationInfo callNotificationInfo;
    @Nullable
    private View chipView;
    @NotNull
    private final FeatureFlags featureFlags;
    @NotNull
    private final IActivityManager iActivityManager;
    @NotNull
    private final OngoingCallLogger logger;
    @NotNull
    private final Executor mainExecutor;
    @NotNull
    private final CommonNotifCollection notifCollection;
    @NotNull
    private final SystemClock systemClock;
    @Nullable
    private IUidObserver.Stub uidObserver;
    private boolean isCallAppVisible = true;
    @NotNull
    private final List<OngoingCallListener> mListeners = new ArrayList();
    @NotNull
    private final OngoingCallController$notifListener$1 notifListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$notifListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onEntryAdded(@NotNull NotificationEntry entry) {
            Intrinsics.checkNotNullParameter(entry, "entry");
            onEntryUpdated(entry);
        }

        /* JADX WARN: Code restructure failed: missing block: B:4:0x0011, code lost:
            if (r0 == false) goto L19;
         */
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onEntryUpdated(@NotNull NotificationEntry entry) {
            OngoingCallController.CallNotificationInfo callNotificationInfo;
            OngoingCallController.CallNotificationInfo callNotificationInfo2;
            OngoingCallController.CallNotificationInfo callNotificationInfo3;
            boolean isCallNotification;
            Intrinsics.checkNotNullParameter(entry, "entry");
            callNotificationInfo = OngoingCallController.this.callNotificationInfo;
            if (callNotificationInfo == null) {
                isCallNotification = OngoingCallControllerKt.isCallNotification(entry);
            }
            String key = entry.getSbn().getKey();
            callNotificationInfo2 = OngoingCallController.this.callNotificationInfo;
            if (!Intrinsics.areEqual(key, callNotificationInfo2 == null ? null : callNotificationInfo2.getKey())) {
                return;
            }
            String key2 = entry.getSbn().getKey();
            Intrinsics.checkNotNullExpressionValue(key2, "entry.sbn.key");
            OngoingCallController.CallNotificationInfo callNotificationInfo4 = new OngoingCallController.CallNotificationInfo(key2, entry.getSbn().getNotification().when, entry.getSbn().getNotification().contentIntent, entry.getSbn().getUid(), entry.getSbn().getNotification().extras.getInt("android.callType", -1) == 2);
            callNotificationInfo3 = OngoingCallController.this.callNotificationInfo;
            if (Intrinsics.areEqual(callNotificationInfo4, callNotificationInfo3)) {
                return;
            }
            OngoingCallController.this.callNotificationInfo = callNotificationInfo4;
            if (callNotificationInfo4.isOngoing()) {
                OngoingCallController.this.updateChip();
            } else {
                OngoingCallController.this.removeChip();
            }
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onEntryCleanUp(@NotNull NotificationEntry entry) {
            Intrinsics.checkNotNullParameter(entry, "entry");
            removeChipIfNeeded(entry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onEntryRemoved(@NotNull NotificationEntry entry, int i) {
            Intrinsics.checkNotNullParameter(entry, "entry");
            removeChipIfNeeded(entry);
        }

        private final void removeChipIfNeeded(NotificationEntry notificationEntry) {
            OngoingCallController.CallNotificationInfo callNotificationInfo;
            String key = notificationEntry.getSbn().getKey();
            callNotificationInfo = OngoingCallController.this.callNotificationInfo;
            if (Intrinsics.areEqual(key, callNotificationInfo == null ? null : callNotificationInfo.getKey())) {
                OngoingCallController.this.removeChip();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isProcessVisibleToUser(int i) {
        return i <= 2;
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$notifListener$1] */
    public OngoingCallController(@NotNull CommonNotifCollection notifCollection, @NotNull FeatureFlags featureFlags, @NotNull SystemClock systemClock, @NotNull ActivityStarter activityStarter, @NotNull Executor mainExecutor, @NotNull IActivityManager iActivityManager, @NotNull OngoingCallLogger logger) {
        Intrinsics.checkNotNullParameter(notifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(mainExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(iActivityManager, "iActivityManager");
        Intrinsics.checkNotNullParameter(logger, "logger");
        this.notifCollection = notifCollection;
        this.featureFlags = featureFlags;
        this.systemClock = systemClock;
        this.activityStarter = activityStarter;
        this.mainExecutor = mainExecutor;
        this.iActivityManager = iActivityManager;
        this.logger = logger;
    }

    public final void init() {
        if (this.featureFlags.isOngoingCallStatusBarChipEnabled()) {
            this.notifCollection.addCollectionListener(this.notifListener);
        }
    }

    public final void setChipView(@NotNull View chipView) {
        Intrinsics.checkNotNullParameter(chipView, "chipView");
        tearDownChipView();
        this.chipView = chipView;
        if (hasOngoingCall()) {
            updateChip();
        }
    }

    public final void notifyChipVisibilityChanged(boolean z) {
        this.logger.logChipVisibilityChanged(z);
    }

    public final boolean hasOngoingCall() {
        CallNotificationInfo callNotificationInfo = this.callNotificationInfo;
        return Intrinsics.areEqual(callNotificationInfo == null ? null : Boolean.valueOf(callNotificationInfo.isOngoing()), Boolean.TRUE) && !this.isCallAppVisible;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(@NotNull OngoingCallListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(@NotNull OngoingCallListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        synchronized (this.mListeners) {
            this.mListeners.remove(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateChip() {
        boolean z;
        CallNotificationInfo callNotificationInfo = this.callNotificationInfo;
        if (callNotificationInfo == null) {
            return;
        }
        View view = this.chipView;
        OngoingCallChronometer timeView = view == null ? null : getTimeView(view);
        final View findViewById = view == null ? null : view.findViewById(R$id.ongoing_call_chip_background);
        if (view != null && timeView != null && findViewById != null) {
            if (callNotificationInfo.hasValidStartTime()) {
                timeView.setShouldHideText(false);
                timeView.setBase((callNotificationInfo.getCallStartTime() - this.systemClock.currentTimeMillis()) + this.systemClock.elapsedRealtime());
                timeView.start();
            } else {
                timeView.setShouldHideText(true);
                timeView.stop();
            }
            final PendingIntent intent = callNotificationInfo.getIntent();
            if (intent != null) {
                view.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$updateChip$1$1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        OngoingCallLogger ongoingCallLogger;
                        ActivityStarter activityStarter;
                        ongoingCallLogger = OngoingCallController.this.logger;
                        ongoingCallLogger.logChipClicked();
                        activityStarter = OngoingCallController.this.activityStarter;
                        activityStarter.postStartActivityDismissingKeyguard(intent, ActivityLaunchAnimator.Controller.Companion.fromView(findViewById, 34));
                    }
                });
            }
            setUpUidObserver(callNotificationInfo);
            for (OngoingCallListener ongoingCallListener : this.mListeners) {
                ongoingCallListener.onOngoingCallStateChanged(true);
            }
            return;
        }
        this.callNotificationInfo = null;
        z = OngoingCallControllerKt.DEBUG;
        if (!z) {
            return;
        }
        Log.w("OngoingCallController", "Ongoing call chip view could not be found; Not displaying chip in status bar");
    }

    private final void setUpUidObserver(final CallNotificationInfo callNotificationInfo) {
        this.isCallAppVisible = isProcessVisibleToUser(this.iActivityManager.getUidProcessState(callNotificationInfo.getUid(), (String) null));
        IUidObserver.Stub stub = this.uidObserver;
        if (stub != null) {
            this.iActivityManager.unregisterUidObserver(stub);
        }
        IUidObserver.Stub stub2 = new IUidObserver.Stub() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1
            public void onUidActive(int i) {
            }

            public void onUidCachedChanged(int i, boolean z) {
            }

            public void onUidGone(int i, boolean z) {
            }

            public void onUidIdle(int i, boolean z) {
            }

            public void onUidStateChanged(int i, int i2, long j, int i3) {
                boolean z;
                boolean isProcessVisibleToUser;
                boolean z2;
                Executor executor;
                if (i == OngoingCallController.CallNotificationInfo.this.getUid()) {
                    z = this.isCallAppVisible;
                    OngoingCallController ongoingCallController = this;
                    isProcessVisibleToUser = ongoingCallController.isProcessVisibleToUser(i2);
                    ongoingCallController.isCallAppVisible = isProcessVisibleToUser;
                    z2 = this.isCallAppVisible;
                    if (z == z2) {
                        return;
                    }
                    executor = this.mainExecutor;
                    final OngoingCallController ongoingCallController2 = this;
                    executor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1$onUidStateChanged$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            List<OngoingCallListener> list;
                            list = OngoingCallController.this.mListeners;
                            for (OngoingCallListener ongoingCallListener : list) {
                                ongoingCallListener.onOngoingCallStateChanged(true);
                            }
                        }
                    });
                }
            }
        };
        this.uidObserver = stub2;
        this.iActivityManager.registerUidObserver(stub2, 1, -1, (String) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void removeChip() {
        this.callNotificationInfo = null;
        tearDownChipView();
        for (OngoingCallListener ongoingCallListener : this.mListeners) {
            ongoingCallListener.onOngoingCallStateChanged(true);
        }
        IUidObserver.Stub stub = this.uidObserver;
        if (stub != null) {
            this.iActivityManager.unregisterUidObserver(stub);
        }
    }

    @Nullable
    public final Unit tearDownChipView() {
        OngoingCallChronometer timeView;
        View view = this.chipView;
        if (view == null || (timeView = getTimeView(view)) == null) {
            return null;
        }
        timeView.stop();
        return Unit.INSTANCE;
    }

    private final OngoingCallChronometer getTimeView(View view) {
        return (OngoingCallChronometer) view.findViewById(R$id.ongoing_call_chip_time);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: OngoingCallController.kt */
    /* loaded from: classes.dex */
    public static final class CallNotificationInfo {
        private final long callStartTime;
        @Nullable
        private final PendingIntent intent;
        private final boolean isOngoing;
        @NotNull
        private final String key;
        private final int uid;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CallNotificationInfo)) {
                return false;
            }
            CallNotificationInfo callNotificationInfo = (CallNotificationInfo) obj;
            return Intrinsics.areEqual(this.key, callNotificationInfo.key) && this.callStartTime == callNotificationInfo.callStartTime && Intrinsics.areEqual(this.intent, callNotificationInfo.intent) && this.uid == callNotificationInfo.uid && this.isOngoing == callNotificationInfo.isOngoing;
        }

        public int hashCode() {
            int hashCode = ((this.key.hashCode() * 31) + Long.hashCode(this.callStartTime)) * 31;
            PendingIntent pendingIntent = this.intent;
            int hashCode2 = (((hashCode + (pendingIntent == null ? 0 : pendingIntent.hashCode())) * 31) + Integer.hashCode(this.uid)) * 31;
            boolean z = this.isOngoing;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            return hashCode2 + i;
        }

        @NotNull
        public String toString() {
            return "CallNotificationInfo(key=" + this.key + ", callStartTime=" + this.callStartTime + ", intent=" + this.intent + ", uid=" + this.uid + ", isOngoing=" + this.isOngoing + ')';
        }

        public CallNotificationInfo(@NotNull String key, long j, @Nullable PendingIntent pendingIntent, int i, boolean z) {
            Intrinsics.checkNotNullParameter(key, "key");
            this.key = key;
            this.callStartTime = j;
            this.intent = pendingIntent;
            this.uid = i;
            this.isOngoing = z;
        }

        @NotNull
        public final String getKey() {
            return this.key;
        }

        public final long getCallStartTime() {
            return this.callStartTime;
        }

        @Nullable
        public final PendingIntent getIntent() {
            return this.intent;
        }

        public final int getUid() {
            return this.uid;
        }

        public final boolean isOngoing() {
            return this.isOngoing;
        }

        public final boolean hasValidStartTime() {
            return this.callStartTime > 0;
        }
    }
}
