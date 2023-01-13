package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.EventLog;
import android.view.RemoteAnimationAdapter;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.EventLogTags;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.policy.HeadsUpUtil;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@CentralSurfacesComponent.CentralSurfacesScope
class StatusBarNotificationActivityStarter implements NotificationActivityStarter {
    private final ActivityIntentHelper mActivityIntentHelper;
    /* access modifiers changed from: private */
    public final ActivityLaunchAnimator mActivityLaunchAnimator;
    private final ActivityStarter mActivityStarter;
    private final Lazy<AssistManager> mAssistManagerLazy;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    /* access modifiers changed from: private */
    public final CentralSurfaces mCentralSurfaces;
    private final NotificationClickNotifier mClickNotifier;
    private final CommandQueue mCommandQueue;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final IDreamManager mDreamManager;
    private final NotificationEntryManager mEntryManager;
    private final GroupMembershipManager mGroupMembershipManager;
    private final HeadsUpManagerPhone mHeadsUpManager;
    private boolean mIsCollapsingToShowActivityOverLockscreen;
    private final KeyguardManager mKeyguardManager;
    private final KeyguardStateController mKeyguardStateController;
    private final LockPatternUtils mLockPatternUtils;
    private final NotificationLockscreenUserManager mLockscreenUserManager;
    private final StatusBarNotificationActivityStarterLogger mLogger;
    private final Handler mMainThreadHandler;
    private final MetricsLogger mMetricsLogger;
    private final NotifPipeline mNotifPipeline;
    private final NotifPipelineFlags mNotifPipelineFlags;
    /* access modifiers changed from: private */
    public final NotificationLaunchAnimatorControllerProvider mNotificationAnimationProvider;
    private final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    private final NotificationPanelViewController mNotificationPanel;
    private final OnUserInteractionCallback mOnUserInteractionCallback;
    private final NotificationPresenter mPresenter;
    private final NotificationRemoteInputManager mRemoteInputManager;
    private final ShadeController mShadeController;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final StatusBarRemoteInputCallback mStatusBarRemoteInputCallback;
    private final StatusBarStateController mStatusBarStateController;
    private final Executor mUiBgExecutor;
    private final NotificationVisibilityProvider mVisibilityProvider;

    @Inject
    StatusBarNotificationActivityStarter(Context context, CommandQueue commandQueue, Handler handler, Executor executor, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManagerPhone headsUpManagerPhone, ActivityStarter activityStarter, NotificationClickNotifier notificationClickNotifier, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardManager keyguardManager, IDreamManager iDreamManager, Optional<BubblesManager> optional, Lazy<AssistManager> lazy, NotificationRemoteInputManager notificationRemoteInputManager, GroupMembershipManager groupMembershipManager, NotificationLockscreenUserManager notificationLockscreenUserManager, ShadeController shadeController, KeyguardStateController keyguardStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, LockPatternUtils lockPatternUtils, StatusBarRemoteInputCallback statusBarRemoteInputCallback, ActivityIntentHelper activityIntentHelper, NotifPipelineFlags notifPipelineFlags, MetricsLogger metricsLogger, StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger, OnUserInteractionCallback onUserInteractionCallback, CentralSurfaces centralSurfaces, NotificationPresenter notificationPresenter, NotificationPanelViewController notificationPanelViewController, ActivityLaunchAnimator activityLaunchAnimator, NotificationLaunchAnimatorControllerProvider notificationLaunchAnimatorControllerProvider) {
        this.mContext = context;
        this.mCommandQueue = commandQueue;
        this.mMainThreadHandler = handler;
        this.mUiBgExecutor = executor;
        this.mEntryManager = notificationEntryManager;
        this.mNotifPipeline = notifPipeline;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mActivityStarter = activityStarter;
        this.mClickNotifier = notificationClickNotifier;
        this.mStatusBarStateController = statusBarStateController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardManager = keyguardManager;
        this.mDreamManager = iDreamManager;
        this.mBubblesManagerOptional = optional;
        this.mAssistManagerLazy = lazy;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mGroupMembershipManager = groupMembershipManager;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mShadeController = shadeController;
        this.mKeyguardStateController = keyguardStateController;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mLockPatternUtils = lockPatternUtils;
        this.mStatusBarRemoteInputCallback = statusBarRemoteInputCallback;
        this.mActivityIntentHelper = activityIntentHelper;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mMetricsLogger = metricsLogger;
        this.mLogger = statusBarNotificationActivityStarterLogger;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mCentralSurfaces = centralSurfaces;
        this.mPresenter = notificationPresenter;
        this.mNotificationPanel = notificationPanelViewController;
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        this.mNotificationAnimationProvider = notificationLaunchAnimatorControllerProvider;
        if (!notifPipelineFlags.isNewPipelineEnabled()) {
            notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
                public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                    StatusBarNotificationActivityStarter.this.handleFullScreenIntent(notificationEntry);
                }
            });
        } else {
            notifPipeline.addCollectionListener(new NotifCollectionListener() {
                public void onEntryAdded(NotificationEntry notificationEntry) {
                    StatusBarNotificationActivityStarter.this.handleFullScreenIntent(notificationEntry);
                }
            });
        }
    }

    public void onNotificationClicked(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        PendingIntent pendingIntent;
        this.mLogger.logStartingActivityFromClick(notificationEntry);
        if (!this.mRemoteInputManager.isRemoteInputActive(notificationEntry) || TextUtils.isEmpty(expandableNotificationRow.getActiveRemoteInputText())) {
            Notification notification = notificationEntry.getSbn().getNotification();
            if (notification.contentIntent != null) {
                pendingIntent = notification.contentIntent;
            } else {
                pendingIntent = notification.fullScreenIntent;
            }
            final PendingIntent pendingIntent2 = pendingIntent;
            boolean isBubble = notificationEntry.isBubble();
            if (pendingIntent2 != null || isBubble) {
                final boolean z = pendingIntent2 != null && pendingIntent2.isActivity() && !isBubble;
                boolean z2 = z && this.mActivityIntentHelper.wouldLaunchResolverActivity(pendingIntent2.getIntent(), this.mLockscreenUserManager.getCurrentUserId());
                final boolean z3 = !z2 && this.mCentralSurfaces.shouldAnimateLaunch(z);
                boolean z4 = this.mKeyguardStateController.isShowing() && pendingIntent2 != null && this.mActivityIntentHelper.wouldShowOverLockscreen(pendingIntent2.getIntent(), this.mLockscreenUserManager.getCurrentUserId());
                final NotificationEntry notificationEntry2 = notificationEntry;
                final ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
                final boolean z5 = z4;
                C31003 r1 = new ActivityStarter.OnDismissAction() {
                    public boolean onDismiss() {
                        return StatusBarNotificationActivityStarter.this.handleNotificationClickAfterKeyguardDismissed(notificationEntry2, expandableNotificationRow2, pendingIntent2, z, z3, z5);
                    }

                    public boolean willRunAnimationOnKeyguard() {
                        return z3;
                    }
                };
                if (z4) {
                    this.mIsCollapsingToShowActivityOverLockscreen = true;
                    r1.onDismiss();
                    return;
                }
                this.mActivityStarter.dismissKeyguardThenExecute(r1, (Runnable) null, z2);
                return;
            }
            this.mLogger.logNonClickableNotification(notificationEntry);
            return;
        }
        this.mRemoteInputManager.closeRemoteInputs();
    }

    /* access modifiers changed from: private */
    public boolean handleNotificationClickAfterKeyguardDismissed(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, boolean z, boolean z2, boolean z3) {
        this.mLogger.logHandleClickAfterKeyguardDismissed(notificationEntry);
        StatusBarNotificationActivityStarter$$ExternalSyntheticLambda4 statusBarNotificationActivityStarter$$ExternalSyntheticLambda4 = new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda4(this, notificationEntry, expandableNotificationRow, pendingIntent, z, z2);
        if (z3) {
            this.mShadeController.addPostCollapseAction(statusBarNotificationActivityStarter$$ExternalSyntheticLambda4);
            this.mShadeController.collapsePanel(true);
        } else if (!this.mKeyguardStateController.isShowing() || !this.mCentralSurfaces.isOccluded()) {
            statusBarNotificationActivityStarter$$ExternalSyntheticLambda4.run();
        } else {
            this.mStatusBarKeyguardViewManager.addAfterKeyguardGoneRunnable(statusBarNotificationActivityStarter$$ExternalSyntheticLambda4);
            this.mShadeController.collapsePanel();
        }
        if (z2 || !this.mNotificationPanel.isFullyCollapsed()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* renamed from: handleNotificationClickAfterPanelCollapsed */
    public void mo45309xb52b7566(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, boolean z, boolean z2) {
        String key = notificationEntry.getKey();
        this.mLogger.logHandleClickAfterPanelCollapsed(notificationEntry);
        try {
            ActivityManager.getService().resumeAppSwitches();
        } catch (RemoteException unused) {
        }
        if (z) {
            int identifier = pendingIntent.getCreatorUserHandle().getIdentifier();
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(identifier) && this.mKeyguardManager.isDeviceLocked(identifier) && this.mStatusBarRemoteInputCallback.startWorkChallengeIfNecessary(identifier, pendingIntent.getIntentSender(), key)) {
                removeHunAfterClick(expandableNotificationRow);
                collapseOnMainThread();
                return;
            }
        }
        Intent intent = null;
        CharSequence charSequence = !TextUtils.isEmpty(notificationEntry.remoteInputText) ? notificationEntry.remoteInputText : null;
        if (!TextUtils.isEmpty(charSequence) && !this.mRemoteInputManager.isSpinning(key)) {
            intent = new Intent().putExtra("android.remoteInputDraft", charSequence.toString());
        }
        Intent intent2 = intent;
        boolean canBubble = notificationEntry.canBubble();
        if (canBubble) {
            this.mLogger.logExpandingBubble(notificationEntry);
            removeHunAfterClick(expandableNotificationRow);
            expandBubbleStackOnMainThread(notificationEntry);
        } else {
            startNotificationIntent(pendingIntent, intent2, notificationEntry, expandableNotificationRow, z2, z);
        }
        if (z || canBubble) {
            this.mAssistManagerLazy.get().hideAssist();
        }
        NotificationVisibility obtain = this.mVisibilityProvider.obtain(notificationEntry, true);
        if (!canBubble && (shouldAutoCancel(notificationEntry.getSbn()) || this.mRemoteInputManager.isNotificationKeptForRemoteInputHistory(key))) {
            this.mMainThreadHandler.post(new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda5(this, this.mOnUserInteractionCallback.registerFutureDismissal(notificationEntry, 1)));
        }
        this.mClickNotifier.onNotificationClick(key, obtain);
        this.mIsCollapsingToShowActivityOverLockscreen = false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleNotificationClickAfterPanelCollapsed$1$com-android-systemui-statusbar-phone-StatusBarNotificationActivityStarter */
    public /* synthetic */ void mo45310x4097d2ff(Runnable runnable) {
        if (this.mPresenter.isCollapsing()) {
            this.mShadeController.addPostCollapseAction(runnable);
        } else {
            runnable.run();
        }
    }

    public void onDragSuccess(NotificationEntry notificationEntry) {
        NotificationVisibility obtain = this.mVisibilityProvider.obtain(notificationEntry, true);
        String key = notificationEntry.getKey();
        if (shouldAutoCancel(notificationEntry.getSbn()) || this.mRemoteInputManager.isNotificationKeptForRemoteInputHistory(key)) {
            this.mMainThreadHandler.post(new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda2(this, this.mOnUserInteractionCallback.registerFutureDismissal(notificationEntry, 1)));
        }
        this.mClickNotifier.onNotificationClick(key, obtain);
        this.mIsCollapsingToShowActivityOverLockscreen = false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDragSuccess$2$com-android-systemui-statusbar-phone-StatusBarNotificationActivityStarter */
    public /* synthetic */ void mo45311x9701841c(Runnable runnable) {
        if (this.mPresenter.isCollapsing()) {
            this.mShadeController.addPostCollapseAction(runnable);
        } else {
            runnable.run();
        }
    }

    private void expandBubbleStackOnMainThread(NotificationEntry notificationEntry) {
        if (this.mBubblesManagerOptional.isPresent()) {
            if (Looper.getMainLooper().isCurrentThread()) {
                mo45307x44bb15f1(notificationEntry);
            } else {
                this.mMainThreadHandler.post(new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda3(this, notificationEntry));
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: expandBubbleStack */
    public void mo45307x44bb15f1(NotificationEntry notificationEntry) {
        this.mBubblesManagerOptional.get().expandStackAndSelectBubble(notificationEntry);
        this.mShadeController.collapsePanel();
    }

    private void startNotificationIntent(PendingIntent pendingIntent, Intent intent, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, boolean z, boolean z2) {
        this.mLogger.logStartNotificationIntent(notificationEntry);
        try {
            this.mActivityLaunchAnimator.startPendingIntentWithAnimation(new StatusBarLaunchAnimatorController(this.mNotificationAnimationProvider.getAnimatorController(expandableNotificationRow, (Runnable) null), this.mCentralSurfaces, z2), z, pendingIntent.getCreatorPackage(), new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda0(this, expandableNotificationRow, pendingIntent, intent, notificationEntry));
        } catch (PendingIntent.CanceledException e) {
            this.mLogger.logSendingIntentFailed(e);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startNotificationIntent$4$com-android-systemui-statusbar-phone-StatusBarNotificationActivityStarter */
    public /* synthetic */ int mo45312x41549ef7(ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, Intent intent, NotificationEntry notificationEntry, RemoteAnimationAdapter remoteAnimationAdapter) throws PendingIntent.CanceledException {
        Bundle bundle;
        long andResetLastActionUpTime = expandableNotificationRow.getAndResetLastActionUpTime();
        if (andResetLastActionUpTime > 0) {
            bundle = CentralSurfaces.getActivityOptions(this.mCentralSurfaces.getDisplayId(), remoteAnimationAdapter, this.mKeyguardStateController.isShowing(), andResetLastActionUpTime);
        } else {
            bundle = CentralSurfaces.getActivityOptions(this.mCentralSurfaces.getDisplayId(), remoteAnimationAdapter);
        }
        PendingIntent pendingIntent2 = pendingIntent;
        int sendAndReturnResult = pendingIntent2.sendAndReturnResult(this.mContext, 0, intent, (PendingIntent.OnFinished) null, (Handler) null, (String) null, bundle);
        this.mLogger.logSendPendingIntent(notificationEntry, pendingIntent, sendAndReturnResult);
        return sendAndReturnResult;
    }

    public void startNotificationGutsIntent(Intent intent, int i, ExpandableNotificationRow expandableNotificationRow) {
        final boolean shouldAnimateLaunch = this.mCentralSurfaces.shouldAnimateLaunch(true);
        final ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
        final Intent intent2 = intent;
        final int i2 = i;
        this.mActivityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() {
            public boolean onDismiss() {
                AsyncTask.execute(new StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda1(this, expandableNotificationRow2, shouldAnimateLaunch, intent2, i2));
                return true;
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onDismiss$1$com-android-systemui-statusbar-phone-StatusBarNotificationActivityStarter$4 */
            public /* synthetic */ void mo45314xc4764c46(ExpandableNotificationRow expandableNotificationRow, boolean z, Intent intent, int i) {
                StatusBarNotificationActivityStarter.this.mActivityLaunchAnimator.startIntentWithAnimation(new StatusBarLaunchAnimatorController(StatusBarNotificationActivityStarter.this.mNotificationAnimationProvider.getAnimatorController(expandableNotificationRow), StatusBarNotificationActivityStarter.this.mCentralSurfaces, true), z, intent.getPackage(), new StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda0(this, intent, i));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onDismiss$0$com-android-systemui-statusbar-phone-StatusBarNotificationActivityStarter$4 */
            public /* synthetic */ Integer mo45313xc3a7cdc5(Intent intent, int i, RemoteAnimationAdapter remoteAnimationAdapter) {
                return Integer.valueOf(TaskStackBuilder.create(StatusBarNotificationActivityStarter.this.mContext).addNextIntentWithParentStack(intent).startActivities(CentralSurfaces.getActivityOptions(StatusBarNotificationActivityStarter.this.mCentralSurfaces.getDisplayId(), remoteAnimationAdapter), new UserHandle(UserHandle.getUserId(i))));
            }

            public boolean willRunAnimationOnKeyguard() {
                return shouldAnimateLaunch;
            }
        }, (Runnable) null, false);
    }

    public void startHistoryIntent(final View view, final boolean z) {
        final boolean shouldAnimateLaunch = this.mCentralSurfaces.shouldAnimateLaunch(true);
        this.mActivityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() {
            public boolean onDismiss() {
                AsyncTask.execute(new StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda1(this, z, view, shouldAnimateLaunch));
                return true;
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onDismiss$1$com-android-systemui-statusbar-phone-StatusBarNotificationActivityStarter$5 */
            public /* synthetic */ void mo45316xc4764c47(boolean z, View view, boolean z2) {
                Intent intent;
                StatusBarLaunchAnimatorController statusBarLaunchAnimatorController;
                if (z) {
                    intent = new Intent("android.settings.NOTIFICATION_HISTORY");
                } else {
                    intent = new Intent("android.settings.NOTIFICATION_SETTINGS");
                }
                TaskStackBuilder addNextIntent = TaskStackBuilder.create(StatusBarNotificationActivityStarter.this.mContext).addNextIntent(new Intent("android.settings.NOTIFICATION_SETTINGS"));
                if (z) {
                    addNextIntent.addNextIntent(intent);
                }
                ActivityLaunchAnimator.Controller fromView = ActivityLaunchAnimator.Controller.fromView(view, 30);
                if (fromView == null) {
                    statusBarLaunchAnimatorController = null;
                } else {
                    statusBarLaunchAnimatorController = new StatusBarLaunchAnimatorController(fromView, StatusBarNotificationActivityStarter.this.mCentralSurfaces, true);
                }
                StatusBarNotificationActivityStarter.this.mActivityLaunchAnimator.startIntentWithAnimation(statusBarLaunchAnimatorController, z2, intent.getPackage(), new StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda0(this, addNextIntent));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onDismiss$0$com-android-systemui-statusbar-phone-StatusBarNotificationActivityStarter$5 */
            public /* synthetic */ Integer mo45315xc3a7cdc6(TaskStackBuilder taskStackBuilder, RemoteAnimationAdapter remoteAnimationAdapter) {
                return Integer.valueOf(taskStackBuilder.startActivities(CentralSurfaces.getActivityOptions(StatusBarNotificationActivityStarter.this.mCentralSurfaces.getDisplayId(), remoteAnimationAdapter), UserHandle.CURRENT));
            }

            public boolean willRunAnimationOnKeyguard() {
                return shouldAnimateLaunch;
            }
        }, (Runnable) null, false);
    }

    private void removeHunAfterClick(ExpandableNotificationRow expandableNotificationRow) {
        String key = expandableNotificationRow.getEntry().getSbn().getKey();
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        if (headsUpManagerPhone != null && headsUpManagerPhone.isAlerting(key)) {
            if (this.mPresenter.isPresenterFullyCollapsed()) {
                HeadsUpUtil.setNeedsHeadsUpDisappearAnimationAfterClick(expandableNotificationRow, true);
            }
            this.mHeadsUpManager.removeNotification(key, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void handleFullScreenIntent(NotificationEntry notificationEntry) {
        if (!this.mNotificationInterruptStateProvider.shouldLaunchFullScreenIntentWhenAdded(notificationEntry)) {
            return;
        }
        if (shouldSuppressFullScreenIntent(notificationEntry)) {
            this.mLogger.logFullScreenIntentSuppressedByDnD(notificationEntry);
        } else if (notificationEntry.getImportance() < 4) {
            this.mLogger.logFullScreenIntentNotImportantEnough(notificationEntry);
        } else {
            this.mUiBgExecutor.execute(new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1(this));
            PendingIntent pendingIntent = notificationEntry.getSbn().getNotification().fullScreenIntent;
            this.mLogger.logSendingFullScreenIntent(notificationEntry, pendingIntent);
            try {
                EventLog.writeEvent(EventLogTags.SYSUI_FULLSCREEN_NOTIFICATION, notificationEntry.getKey());
                this.mCentralSurfaces.wakeUpForFullScreenIntent();
                pendingIntent.send();
                notificationEntry.notifyFullScreenIntentLaunched();
                this.mMetricsLogger.count("note_fullscreen", 1);
            } catch (PendingIntent.CanceledException unused) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleFullScreenIntent$5$com-android-systemui-statusbar-phone-StatusBarNotificationActivityStarter */
    public /* synthetic */ void mo45308x5a69f648() {
        try {
            this.mDreamManager.awaken();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean isCollapsingToShowActivityOverLockscreen() {
        return this.mIsCollapsingToShowActivityOverLockscreen;
    }

    private static boolean shouldAutoCancel(StatusBarNotification statusBarNotification) {
        int i = statusBarNotification.getNotification().flags;
        return (i & 16) == 16 && (i & 64) == 0;
    }

    private void collapseOnMainThread() {
        if (Looper.getMainLooper().isCurrentThread()) {
            this.mShadeController.collapsePanel();
            return;
        }
        Handler handler = this.mMainThreadHandler;
        ShadeController shadeController = this.mShadeController;
        Objects.requireNonNull(shadeController);
        handler.post(new CentralSurfacesImpl$$ExternalSyntheticLambda14(shadeController));
    }

    private boolean shouldSuppressFullScreenIntent(NotificationEntry notificationEntry) {
        if (this.mPresenter.isDeviceInVrMode()) {
            return true;
        }
        return notificationEntry.shouldSuppressFullScreenIntent();
    }
}
