package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.smartspace.SmartspaceTarget;
import android.os.Parcelable;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SmartspaceDedupingCoordinator.kt */
/* loaded from: classes.dex */
public final class SmartspaceDedupingCoordinator implements Coordinator {
    @NotNull
    private final SystemClock clock;
    @NotNull
    private final DelayableExecutor executor;
    private boolean isOnLockscreen;
    @NotNull
    private final NotifPipeline notifPipeline;
    @NotNull
    private final NotificationEntryManager notificationEntryManager;
    @NotNull
    private final NotificationLockscreenUserManager notificationLockscreenUserManager;
    @NotNull
    private final LockscreenSmartspaceController smartspaceController;
    @NotNull
    private final SysuiStatusBarStateController statusBarStateController;
    @NotNull
    private Map<String, TrackedSmartspaceTarget> trackedSmartspaceTargets = new LinkedHashMap();
    @NotNull
    private final SmartspaceDedupingCoordinator$filter$1 filter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$filter$1
        /* JADX INFO: Access modifiers changed from: package-private */
        {
            super("SmartspaceDedupingFilter");
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public boolean shouldFilterOut(@NotNull NotificationEntry entry, long j) {
            boolean z;
            boolean isDupedWithSmartspaceContent;
            Intrinsics.checkNotNullParameter(entry, "entry");
            z = SmartspaceDedupingCoordinator.this.isOnLockscreen;
            if (z) {
                isDupedWithSmartspaceContent = SmartspaceDedupingCoordinator.this.isDupedWithSmartspaceContent(entry);
                if (isDupedWithSmartspaceContent) {
                    return true;
                }
            }
            return false;
        }
    };
    @NotNull
    private final SmartspaceDedupingCoordinator$collectionListener$1 collectionListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$collectionListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onEntryAdded(@NotNull NotificationEntry entry) {
            Map map;
            Intrinsics.checkNotNullParameter(entry, "entry");
            map = SmartspaceDedupingCoordinator.this.trackedSmartspaceTargets;
            TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) map.get(entry.getKey());
            if (trackedSmartspaceTarget == null) {
                return;
            }
            SmartspaceDedupingCoordinator.this.updateFilterStatus(trackedSmartspaceTarget);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onEntryUpdated(@NotNull NotificationEntry entry) {
            Map map;
            Intrinsics.checkNotNullParameter(entry, "entry");
            map = SmartspaceDedupingCoordinator.this.trackedSmartspaceTargets;
            TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) map.get(entry.getKey());
            if (trackedSmartspaceTarget == null) {
                return;
            }
            SmartspaceDedupingCoordinator.this.updateFilterStatus(trackedSmartspaceTarget);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onEntryRemoved(@NotNull NotificationEntry entry, int i) {
            Map map;
            Intrinsics.checkNotNullParameter(entry, "entry");
            map = SmartspaceDedupingCoordinator.this.trackedSmartspaceTargets;
            TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) map.get(entry.getKey());
            if (trackedSmartspaceTarget == null) {
                return;
            }
            SmartspaceDedupingCoordinator.this.cancelExceptionTimeout(trackedSmartspaceTarget);
        }
    };
    @NotNull
    private final SmartspaceDedupingCoordinator$statusBarStateListener$1 statusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$statusBarStateListener$1
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onStateChanged(int i) {
            SmartspaceDedupingCoordinator.this.recordStatusBarState(i);
        }
    };

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$filter$1] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$collectionListener$1] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$statusBarStateListener$1] */
    public SmartspaceDedupingCoordinator(@NotNull SysuiStatusBarStateController statusBarStateController, @NotNull LockscreenSmartspaceController smartspaceController, @NotNull NotificationEntryManager notificationEntryManager, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull NotifPipeline notifPipeline, @NotNull DelayableExecutor executor, @NotNull SystemClock clock) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(smartspaceController, "smartspaceController");
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "notificationLockscreenUserManager");
        Intrinsics.checkNotNullParameter(notifPipeline, "notifPipeline");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(clock, "clock");
        this.statusBarStateController = statusBarStateController;
        this.smartspaceController = smartspaceController;
        this.notificationEntryManager = notificationEntryManager;
        this.notificationLockscreenUserManager = notificationLockscreenUserManager;
        this.notifPipeline = notifPipeline;
        this.executor = executor;
        this.clock = clock;
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public void attach(@NotNull NotifPipeline pipeline) {
        Intrinsics.checkNotNullParameter(pipeline, "pipeline");
        pipeline.addPreGroupFilter(this.filter);
        pipeline.addCollectionListener(this.collectionListener);
        this.statusBarStateController.addCallback(this.statusBarStateListener);
        this.smartspaceController.addListener(new BcSmartspaceDataPlugin.SmartspaceTargetListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$attach$1
            @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceTargetListener
            public final void onSmartspaceTargetsUpdated(@NotNull List<? extends Parcelable> p0) {
                Intrinsics.checkNotNullParameter(p0, "p0");
                SmartspaceDedupingCoordinator.this.onNewSmartspaceTargets(p0);
            }
        });
        this.notificationLockscreenUserManager.addKeyguardNotificationSuppressor(new NotificationLockscreenUserManager.KeyguardNotificationSuppressor() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$attach$2
            @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager.KeyguardNotificationSuppressor
            public final boolean shouldSuppressOnKeyguard(NotificationEntry entry) {
                boolean isDupedWithSmartspaceContent;
                SmartspaceDedupingCoordinator smartspaceDedupingCoordinator = SmartspaceDedupingCoordinator.this;
                Intrinsics.checkNotNullExpressionValue(entry, "entry");
                isDupedWithSmartspaceContent = smartspaceDedupingCoordinator.isDupedWithSmartspaceContent(entry);
                return isDupedWithSmartspaceContent;
            }
        });
        recordStatusBarState(this.statusBarStateController.getState());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isDupedWithSmartspaceContent(NotificationEntry notificationEntry) {
        TrackedSmartspaceTarget trackedSmartspaceTarget = this.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget == null) {
            return false;
        }
        return trackedSmartspaceTarget.getShouldFilter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0071  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void onNewSmartspaceTargets(List<? extends Parcelable> list) {
        boolean z;
        Runnable cancelTimeoutRunnable;
        String sourceNotificationKey;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Map<String, TrackedSmartspaceTarget> map = this.trackedSmartspaceTargets;
        Iterator<? extends Parcelable> it = list.iterator();
        if (it.hasNext()) {
            SmartspaceTarget smartspaceTarget = (Parcelable) it.next();
            SmartspaceTarget smartspaceTarget2 = smartspaceTarget instanceof SmartspaceTarget ? smartspaceTarget : null;
            if (smartspaceTarget2 != null && (sourceNotificationKey = smartspaceTarget2.getSourceNotificationKey()) != null) {
                TrackedSmartspaceTarget trackedSmartspaceTarget = map.get(sourceNotificationKey);
                if (trackedSmartspaceTarget == null) {
                    trackedSmartspaceTarget = new TrackedSmartspaceTarget(sourceNotificationKey);
                }
                TrackedSmartspaceTarget trackedSmartspaceTarget2 = trackedSmartspaceTarget;
                linkedHashMap.put(sourceNotificationKey, trackedSmartspaceTarget2);
                z = updateFilterStatus(trackedSmartspaceTarget2);
                for (String str : map.keySet()) {
                    if (!linkedHashMap.containsKey(str)) {
                        TrackedSmartspaceTarget trackedSmartspaceTarget3 = map.get(str);
                        if (trackedSmartspaceTarget3 != null && (cancelTimeoutRunnable = trackedSmartspaceTarget3.getCancelTimeoutRunnable()) != null) {
                            cancelTimeoutRunnable.run();
                        }
                        z = true;
                    }
                }
                if (z) {
                    invalidateList();
                    this.notificationEntryManager.updateNotifications("Smartspace targets changed");
                }
                this.trackedSmartspaceTargets = linkedHashMap;
            }
        }
        z = false;
        while (r2.hasNext()) {
        }
        if (z) {
        }
        this.trackedSmartspaceTargets = linkedHashMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean updateFilterStatus(TrackedSmartspaceTarget trackedSmartspaceTarget) {
        boolean shouldFilter = trackedSmartspaceTarget.getShouldFilter();
        NotificationEntry entry = this.notifPipeline.getEntry(trackedSmartspaceTarget.getKey());
        if (entry != null) {
            updateAlertException(trackedSmartspaceTarget, entry);
            trackedSmartspaceTarget.setShouldFilter(!hasRecentlyAlerted(entry));
        }
        return trackedSmartspaceTarget.getShouldFilter() != shouldFilter && this.isOnLockscreen;
    }

    private final void updateAlertException(final TrackedSmartspaceTarget trackedSmartspaceTarget, NotificationEntry notificationEntry) {
        long j;
        long currentTimeMillis = this.clock.currentTimeMillis();
        long lastAudiblyAlertedMillis = notificationEntry.getRanking().getLastAudiblyAlertedMillis();
        j = SmartspaceDedupingCoordinatorKt.ALERT_WINDOW;
        long j2 = lastAudiblyAlertedMillis + j;
        if (j2 == trackedSmartspaceTarget.getAlertExceptionExpires() || j2 <= currentTimeMillis) {
            return;
        }
        Runnable cancelTimeoutRunnable = trackedSmartspaceTarget.getCancelTimeoutRunnable();
        if (cancelTimeoutRunnable != null) {
            cancelTimeoutRunnable.run();
        }
        trackedSmartspaceTarget.setAlertExceptionExpires(j2);
        trackedSmartspaceTarget.setCancelTimeoutRunnable(this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator$updateAlertException$1
            @Override // java.lang.Runnable
            public final void run() {
                SmartspaceDedupingCoordinator$filter$1 smartspaceDedupingCoordinator$filter$1;
                NotificationEntryManager notificationEntryManager;
                TrackedSmartspaceTarget.this.setCancelTimeoutRunnable(null);
                TrackedSmartspaceTarget.this.setShouldFilter(true);
                smartspaceDedupingCoordinator$filter$1 = this.filter;
                smartspaceDedupingCoordinator$filter$1.invalidateList();
                notificationEntryManager = this.notificationEntryManager;
                notificationEntryManager.updateNotifications("deduping timeout expired");
            }
        }, j2 - currentTimeMillis));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void cancelExceptionTimeout(TrackedSmartspaceTarget trackedSmartspaceTarget) {
        Runnable cancelTimeoutRunnable = trackedSmartspaceTarget.getCancelTimeoutRunnable();
        if (cancelTimeoutRunnable != null) {
            cancelTimeoutRunnable.run();
        }
        trackedSmartspaceTarget.setCancelTimeoutRunnable(null);
        trackedSmartspaceTarget.setAlertExceptionExpires(0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void recordStatusBarState(int i) {
        boolean z = this.isOnLockscreen;
        boolean z2 = true;
        if (i != 1) {
            z2 = false;
        }
        this.isOnLockscreen = z2;
        if (z2 != z) {
            invalidateList();
        }
    }

    private final boolean hasRecentlyAlerted(NotificationEntry notificationEntry) {
        long j;
        long currentTimeMillis = this.clock.currentTimeMillis() - notificationEntry.getRanking().getLastAudiblyAlertedMillis();
        j = SmartspaceDedupingCoordinatorKt.ALERT_WINDOW;
        return currentTimeMillis <= j;
    }
}
