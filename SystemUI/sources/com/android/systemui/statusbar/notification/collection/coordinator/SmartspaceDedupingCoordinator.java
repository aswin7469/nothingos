package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.smartspace.SmartspaceTarget;
import android.os.Parcelable;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003*\u0003\u0012\u0015\u001a\b\u0007\u0018\u00002\u00020\u0001BA\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u000bH\u0016J\u0010\u0010#\u001a\u00020!2\u0006\u0010$\u001a\u00020\u001fH\u0002J\u0010\u0010%\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'H\u0002J\u0010\u0010(\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'H\u0002J\u0016\u0010)\u001a\u00020!2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020,0+H\u0002J\u0010\u0010-\u001a\u00020!2\u0006\u0010.\u001a\u00020/H\u0002J\u0018\u00100\u001a\u00020!2\u0006\u0010$\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020'H\u0002J\u0010\u00101\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u001fH\u0002R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0004\n\u0002\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0004\n\u0002\u0010\u001bR\u001a\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u001f0\u001dX\u000e¢\u0006\u0002\n\u0000¨\u00062"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "smartspaceController", "Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController;", "notificationEntryManager", "Lcom/android/systemui/statusbar/notification/NotificationEntryManager;", "notificationLockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "notifPipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "executor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "clock", "Lcom/android/systemui/util/time/SystemClock;", "(Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController;Lcom/android/systemui/statusbar/notification/NotificationEntryManager;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/util/time/SystemClock;)V", "collectionListener", "com/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator$collectionListener$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator$collectionListener$1;", "filter", "com/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator$filter$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator$filter$1;", "isOnLockscreen", "", "statusBarStateListener", "com/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator$statusBarStateListener$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator$statusBarStateListener$1;", "trackedSmartspaceTargets", "", "", "Lcom/android/systemui/statusbar/notification/collection/coordinator/TrackedSmartspaceTarget;", "attach", "", "pipeline", "cancelExceptionTimeout", "target", "hasRecentlyAlerted", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "isDupedWithSmartspaceContent", "onNewSmartspaceTargets", "targets", "", "Landroid/os/Parcelable;", "recordStatusBarState", "newState", "", "updateAlertException", "updateFilterStatus", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@CoordinatorScope
/* compiled from: SmartspaceDedupingCoordinator.kt */
public final class SmartspaceDedupingCoordinator implements Coordinator {
    private final SystemClock clock;
    private final SmartspaceDedupingCoordinator$collectionListener$1 collectionListener = new SmartspaceDedupingCoordinator$collectionListener$1(this);
    private final DelayableExecutor executor;
    private final SmartspaceDedupingCoordinator$filter$1 filter = new SmartspaceDedupingCoordinator$filter$1(this);
    /* access modifiers changed from: private */
    public boolean isOnLockscreen;
    private final NotifPipeline notifPipeline;
    private final NotificationEntryManager notificationEntryManager;
    private final NotificationLockscreenUserManager notificationLockscreenUserManager;
    private final LockscreenSmartspaceController smartspaceController;
    private final SysuiStatusBarStateController statusBarStateController;
    private final SmartspaceDedupingCoordinator$statusBarStateListener$1 statusBarStateListener = new SmartspaceDedupingCoordinator$statusBarStateListener$1(this);
    /* access modifiers changed from: private */
    public Map<String, TrackedSmartspaceTarget> trackedSmartspaceTargets = new LinkedHashMap();

    @Inject
    public SmartspaceDedupingCoordinator(SysuiStatusBarStateController sysuiStatusBarStateController, LockscreenSmartspaceController lockscreenSmartspaceController, NotificationEntryManager notificationEntryManager2, NotificationLockscreenUserManager notificationLockscreenUserManager2, NotifPipeline notifPipeline2, @Main DelayableExecutor delayableExecutor, SystemClock systemClock) {
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(lockscreenSmartspaceController, "smartspaceController");
        Intrinsics.checkNotNullParameter(notificationEntryManager2, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager2, "notificationLockscreenUserManager");
        Intrinsics.checkNotNullParameter(notifPipeline2, "notifPipeline");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        Intrinsics.checkNotNullParameter(systemClock, DemoMode.COMMAND_CLOCK);
        this.statusBarStateController = sysuiStatusBarStateController;
        this.smartspaceController = lockscreenSmartspaceController;
        this.notificationEntryManager = notificationEntryManager2;
        this.notificationLockscreenUserManager = notificationLockscreenUserManager2;
        this.notifPipeline = notifPipeline2;
        this.executor = delayableExecutor;
        this.clock = systemClock;
    }

    public void attach(NotifPipeline notifPipeline2) {
        Intrinsics.checkNotNullParameter(notifPipeline2, "pipeline");
        notifPipeline2.addPreGroupFilter(this.filter);
        notifPipeline2.addCollectionListener(this.collectionListener);
        this.statusBarStateController.addCallback(this.statusBarStateListener);
        this.smartspaceController.addListener(new SmartspaceDedupingCoordinator$$ExternalSyntheticLambda1(this));
        if (!notifPipeline2.isNewPipelineEnabled()) {
            this.notificationLockscreenUserManager.addKeyguardNotificationSuppressor(new SmartspaceDedupingCoordinator$$ExternalSyntheticLambda2(this));
        }
        recordStatusBarState(this.statusBarStateController.getState());
    }

    /* access modifiers changed from: private */
    /* renamed from: attach$lambda-0  reason: not valid java name */
    public static final boolean m3113attach$lambda0(SmartspaceDedupingCoordinator smartspaceDedupingCoordinator, NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(smartspaceDedupingCoordinator, "this$0");
        Intrinsics.checkNotNullExpressionValue(notificationEntry, "entry");
        return smartspaceDedupingCoordinator.isDupedWithSmartspaceContent(notificationEntry);
    }

    /* access modifiers changed from: private */
    public final boolean isDupedWithSmartspaceContent(NotificationEntry notificationEntry) {
        TrackedSmartspaceTarget trackedSmartspaceTarget = this.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget != null) {
            return trackedSmartspaceTarget.getShouldFilter();
        }
        return false;
    }

    /* access modifiers changed from: private */
    public final void onNewSmartspaceTargets(List<? extends Parcelable> list) {
        Runnable cancelTimeoutRunnable;
        String sourceNotificationKey;
        Map<String, TrackedSmartspaceTarget> linkedHashMap = new LinkedHashMap<>();
        Map<String, TrackedSmartspaceTarget> map = this.trackedSmartspaceTargets;
        Iterator<? extends Parcelable> it = list.iterator();
        boolean z = false;
        if (it.hasNext()) {
            SmartspaceTarget smartspaceTarget = (Parcelable) it.next();
            SmartspaceTarget smartspaceTarget2 = smartspaceTarget instanceof SmartspaceTarget ? smartspaceTarget : null;
            if (!(smartspaceTarget2 == null || (sourceNotificationKey = smartspaceTarget2.getSourceNotificationKey()) == null)) {
                TrackedSmartspaceTarget trackedSmartspaceTarget = map.get(sourceNotificationKey);
                if (trackedSmartspaceTarget == null) {
                    trackedSmartspaceTarget = new TrackedSmartspaceTarget(sourceNotificationKey);
                }
                TrackedSmartspaceTarget trackedSmartspaceTarget2 = trackedSmartspaceTarget;
                linkedHashMap.put(sourceNotificationKey, trackedSmartspaceTarget2);
                z = updateFilterStatus(trackedSmartspaceTarget2);
            }
        }
        for (String next : map.keySet()) {
            if (!linkedHashMap.containsKey(next)) {
                TrackedSmartspaceTarget trackedSmartspaceTarget3 = map.get(next);
                if (!(trackedSmartspaceTarget3 == null || (cancelTimeoutRunnable = trackedSmartspaceTarget3.getCancelTimeoutRunnable()) == null)) {
                    cancelTimeoutRunnable.run();
                }
                z = true;
            }
        }
        if (z) {
            this.filter.invalidateList();
            this.notificationEntryManager.updateNotifications("Smartspace targets changed");
        }
        this.trackedSmartspaceTargets = linkedHashMap;
    }

    /* access modifiers changed from: private */
    public final boolean updateFilterStatus(TrackedSmartspaceTarget trackedSmartspaceTarget) {
        boolean shouldFilter = trackedSmartspaceTarget.getShouldFilter();
        NotificationEntry entry = this.notifPipeline.getEntry(trackedSmartspaceTarget.getKey());
        if (entry != null) {
            updateAlertException(trackedSmartspaceTarget, entry);
            trackedSmartspaceTarget.setShouldFilter(!hasRecentlyAlerted(entry));
        }
        if (trackedSmartspaceTarget.getShouldFilter() == shouldFilter || !this.isOnLockscreen) {
            return false;
        }
        return true;
    }

    private final void updateAlertException(TrackedSmartspaceTarget trackedSmartspaceTarget, NotificationEntry notificationEntry) {
        long currentTimeMillis = this.clock.currentTimeMillis();
        long lastAudiblyAlertedMillis = notificationEntry.getRanking().getLastAudiblyAlertedMillis() + SmartspaceDedupingCoordinatorKt.ALERT_WINDOW;
        if (lastAudiblyAlertedMillis != trackedSmartspaceTarget.getAlertExceptionExpires() && lastAudiblyAlertedMillis > currentTimeMillis) {
            Runnable cancelTimeoutRunnable = trackedSmartspaceTarget.getCancelTimeoutRunnable();
            if (cancelTimeoutRunnable != null) {
                cancelTimeoutRunnable.run();
            }
            trackedSmartspaceTarget.setAlertExceptionExpires(lastAudiblyAlertedMillis);
            trackedSmartspaceTarget.setCancelTimeoutRunnable(this.executor.executeDelayed(new SmartspaceDedupingCoordinator$$ExternalSyntheticLambda0(trackedSmartspaceTarget, this), lastAudiblyAlertedMillis - currentTimeMillis));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateAlertException$lambda-3  reason: not valid java name */
    public static final void m3114updateAlertException$lambda3(TrackedSmartspaceTarget trackedSmartspaceTarget, SmartspaceDedupingCoordinator smartspaceDedupingCoordinator) {
        Intrinsics.checkNotNullParameter(trackedSmartspaceTarget, "$target");
        Intrinsics.checkNotNullParameter(smartspaceDedupingCoordinator, "this$0");
        trackedSmartspaceTarget.setCancelTimeoutRunnable((Runnable) null);
        trackedSmartspaceTarget.setShouldFilter(true);
        smartspaceDedupingCoordinator.filter.invalidateList();
        smartspaceDedupingCoordinator.notificationEntryManager.updateNotifications("deduping timeout expired");
    }

    /* access modifiers changed from: private */
    public final void cancelExceptionTimeout(TrackedSmartspaceTarget trackedSmartspaceTarget) {
        Runnable cancelTimeoutRunnable = trackedSmartspaceTarget.getCancelTimeoutRunnable();
        if (cancelTimeoutRunnable != null) {
            cancelTimeoutRunnable.run();
        }
        trackedSmartspaceTarget.setCancelTimeoutRunnable((Runnable) null);
        trackedSmartspaceTarget.setAlertExceptionExpires(0);
    }

    /* access modifiers changed from: private */
    public final void recordStatusBarState(int i) {
        boolean z = this.isOnLockscreen;
        boolean z2 = true;
        if (i != 1) {
            z2 = false;
        }
        this.isOnLockscreen = z2;
        if (z2 != z) {
            this.filter.invalidateList();
        }
    }

    private final boolean hasRecentlyAlerted(NotificationEntry notificationEntry) {
        return this.clock.currentTimeMillis() - notificationEntry.getRanking().getLastAudiblyAlertedMillis() <= SmartspaceDedupingCoordinatorKt.ALERT_WINDOW;
    }
}
