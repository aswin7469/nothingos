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
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.policy.HeadsUpUtil;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public class StatusBarNotificationActivityStarter implements NotificationActivityStarter {
    private final ActivityIntentHelper mActivityIntentHelper;
    private final ActivityLaunchAnimator mActivityLaunchAnimator;
    private final ActivityStarter mActivityStarter;
    private final Lazy<AssistManager> mAssistManagerLazy;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    private final NotificationClickNotifier mClickNotifier;
    private final CommandQueue mCommandQueue;
    private final Context mContext;
    private final IDreamManager mDreamManager;
    private final NotificationEntryManager mEntryManager;
    private final FeatureFlags mFeatureFlags;
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
    private final NotificationLaunchAnimatorControllerProvider mNotificationAnimationProvider;
    private final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    private final NotificationPanelViewController mNotificationPanel;
    private final OnUserInteractionCallback mOnUserInteractionCallback;
    private final NotificationPresenter mPresenter;
    private final NotificationRemoteInputManager mRemoteInputManager;
    private final ShadeController mShadeController;
    private final StatusBar mStatusBar;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final StatusBarRemoteInputCallback mStatusBarRemoteInputCallback;
    private final StatusBarStateController mStatusBarStateController;
    private final Executor mUiBgExecutor;

    private StatusBarNotificationActivityStarter(Context context, CommandQueue commandQueue, Handler handler, Executor executor, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, HeadsUpManagerPhone headsUpManagerPhone, ActivityStarter activityStarter, NotificationClickNotifier notificationClickNotifier, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardManager keyguardManager, IDreamManager iDreamManager, Optional<BubblesManager> optional, Lazy<AssistManager> lazy, NotificationRemoteInputManager notificationRemoteInputManager, GroupMembershipManager groupMembershipManager, NotificationLockscreenUserManager notificationLockscreenUserManager, ShadeController shadeController, KeyguardStateController keyguardStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, LockPatternUtils lockPatternUtils, StatusBarRemoteInputCallback statusBarRemoteInputCallback, ActivityIntentHelper activityIntentHelper, FeatureFlags featureFlags, MetricsLogger metricsLogger, StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger, OnUserInteractionCallback onUserInteractionCallback, StatusBar statusBar, NotificationPresenter notificationPresenter, NotificationPanelViewController notificationPanelViewController, ActivityLaunchAnimator activityLaunchAnimator, NotificationLaunchAnimatorControllerProvider notificationLaunchAnimatorControllerProvider) {
        this.mContext = context;
        this.mCommandQueue = commandQueue;
        this.mMainThreadHandler = handler;
        this.mUiBgExecutor = executor;
        this.mEntryManager = notificationEntryManager;
        this.mNotifPipeline = notifPipeline;
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
        this.mFeatureFlags = featureFlags;
        this.mMetricsLogger = metricsLogger;
        this.mLogger = statusBarNotificationActivityStarterLogger;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mStatusBar = statusBar;
        this.mPresenter = notificationPresenter;
        this.mNotificationPanel = notificationPanelViewController;
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        this.mNotificationAnimationProvider = notificationLaunchAnimatorControllerProvider;
        if (!featureFlags.isNewNotifPipelineRenderingEnabled()) {
            notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter.1
                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                    StatusBarNotificationActivityStarter.this.handleFullScreenIntent(notificationEntry);
                }
            });
        } else {
            notifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter.2
                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public void onEntryAdded(NotificationEntry notificationEntry) {
                    StatusBarNotificationActivityStarter.this.handleFullScreenIntent(notificationEntry);
                }
            });
        }
    }

    @Override // com.android.systemui.statusbar.notification.NotificationActivityStarter
    public void onNotificationClicked(StatusBarNotification statusBarNotification, final ExpandableNotificationRow expandableNotificationRow) {
        this.mLogger.logStartingActivityFromClick(statusBarNotification.getKey());
        final NotificationEntry entry = expandableNotificationRow.getEntry();
        final RemoteInputController controller = this.mRemoteInputManager.getController();
        if (controller.isRemoteInputActive(entry) && !TextUtils.isEmpty(expandableNotificationRow.getActiveRemoteInputText())) {
            controller.closeRemoteInputs();
            return;
        }
        Notification notification = statusBarNotification.getNotification();
        PendingIntent pendingIntent = notification.contentIntent;
        final PendingIntent pendingIntent2 = pendingIntent != null ? pendingIntent : notification.fullScreenIntent;
        boolean isBubble = entry.isBubble();
        if (pendingIntent2 == null && !isBubble) {
            this.mLogger.logNonClickableNotification(statusBarNotification.getKey());
            return;
        }
        boolean z = false;
        final boolean z2 = pendingIntent2 != null && pendingIntent2.isActivity() && !isBubble;
        boolean z3 = z2 && this.mActivityIntentHelper.wouldLaunchResolverActivity(pendingIntent2.getIntent(), this.mLockscreenUserManager.getCurrentUserId());
        final boolean z4 = !z3 && this.mStatusBar.shouldAnimateLaunch(z2);
        if (this.mKeyguardStateController.isShowing() && pendingIntent2 != null && this.mActivityIntentHelper.wouldShowOverLockscreen(pendingIntent2.getIntent(), this.mLockscreenUserManager.getCurrentUserId())) {
            z = true;
        }
        final boolean z5 = z;
        ActivityStarter.OnDismissAction onDismissAction = new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter.3
            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public boolean onDismiss() {
                return StatusBarNotificationActivityStarter.this.handleNotificationClickAfterKeyguardDismissed(entry, expandableNotificationRow, controller, pendingIntent2, z2, z4, z5);
            }

            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public boolean willRunAnimationOnKeyguard() {
                return z4;
            }
        };
        if (z) {
            this.mIsCollapsingToShowActivityOverLockscreen = true;
            onDismissAction.onDismiss();
            return;
        }
        this.mActivityStarter.dismissKeyguardThenExecute(onDismissAction, null, z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleNotificationClickAfterKeyguardDismissed(final NotificationEntry notificationEntry, final ExpandableNotificationRow expandableNotificationRow, final RemoteInputController remoteInputController, final PendingIntent pendingIntent, final boolean z, final boolean z2, boolean z3) {
        this.mLogger.logHandleClickAfterKeyguardDismissed(notificationEntry.getKey());
        Runnable runnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarNotificationActivityStarter.this.lambda$handleNotificationClickAfterKeyguardDismissed$0(notificationEntry, expandableNotificationRow, remoteInputController, pendingIntent, z, z2);
            }
        };
        if (z3) {
            this.mShadeController.addPostCollapseAction(runnable);
            this.mShadeController.collapsePanel(true);
        } else if (this.mKeyguardStateController.isShowing() && this.mStatusBar.isOccluded()) {
            this.mStatusBarKeyguardViewManager.addAfterKeyguardGoneRunnable(runnable);
            this.mShadeController.collapsePanel();
        } else {
            runnable.run();
        }
        return z2 || !this.mNotificationPanel.isFullyCollapsed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleNotificationClickAfterPanelCollapsed */
    public void lambda$handleNotificationClickAfterKeyguardDismissed$0(final NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, RemoteInputController remoteInputController, PendingIntent pendingIntent, boolean z, boolean z2) {
        String key = notificationEntry.getKey();
        this.mLogger.logHandleClickAfterPanelCollapsed(key);
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
        final NotificationEntry notificationEntry2 = null;
        CharSequence charSequence = !TextUtils.isEmpty(notificationEntry.remoteInputText) ? notificationEntry.remoteInputText : null;
        Intent putExtra = (TextUtils.isEmpty(charSequence) || remoteInputController.isSpinning(key)) ? null : new Intent().putExtra("android.remoteInputDraft", charSequence.toString());
        boolean canBubble = notificationEntry.canBubble();
        if (canBubble) {
            this.mLogger.logExpandingBubble(key);
            removeHunAfterClick(expandableNotificationRow);
            expandBubbleStackOnMainThread(notificationEntry);
        } else {
            startNotificationIntent(pendingIntent, putExtra, notificationEntry, expandableNotificationRow, z2, z);
        }
        if (z || canBubble) {
            this.mAssistManagerLazy.get().hideAssist();
        }
        NotificationVisibility obtain = NotificationVisibility.obtain(notificationEntry.getKey(), notificationEntry.getRanking().getRank(), getVisibleNotificationsCount(), true, NotificationLogger.getNotificationLocation(notificationEntry));
        boolean shouldAutoCancel = shouldAutoCancel(notificationEntry.getSbn());
        if (shouldAutoCancel) {
            notificationEntry2 = this.mOnUserInteractionCallback.getGroupSummaryToDismiss(notificationEntry);
        }
        this.mClickNotifier.onNotificationClick(key, obtain);
        if (!canBubble && (shouldAutoCancel || this.mRemoteInputManager.isNotificationKeptForRemoteInputHistory(key))) {
            this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBarNotificationActivityStarter.this.lambda$handleNotificationClickAfterPanelCollapsed$2(notificationEntry, notificationEntry2);
                }
            });
        }
        this.mIsCollapsingToShowActivityOverLockscreen = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleNotificationClickAfterPanelCollapsed$2(final NotificationEntry notificationEntry, final NotificationEntry notificationEntry2) {
        Runnable runnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarNotificationActivityStarter.this.lambda$handleNotificationClickAfterPanelCollapsed$1(notificationEntry, notificationEntry2);
            }
        };
        if (this.mPresenter.isCollapsing()) {
            this.mShadeController.addPostCollapseAction(runnable);
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleNotificationClickAfterPanelCollapsed$1(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        this.mOnUserInteractionCallback.onDismiss(notificationEntry, 1, notificationEntry2);
    }

    private void expandBubbleStackOnMainThread(final NotificationEntry notificationEntry) {
        if (!this.mBubblesManagerOptional.isPresent()) {
            return;
        }
        if (Looper.getMainLooper().isCurrentThread()) {
            lambda$expandBubbleStackOnMainThread$3(notificationEntry);
        } else {
            this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBarNotificationActivityStarter.this.lambda$expandBubbleStackOnMainThread$3(notificationEntry);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: expandBubbleStack */
    public void lambda$expandBubbleStackOnMainThread$3(NotificationEntry notificationEntry) {
        this.mBubblesManagerOptional.get().expandStackAndSelectBubble(notificationEntry);
        this.mShadeController.collapsePanel();
    }

    private void startNotificationIntent(final PendingIntent pendingIntent, final Intent intent, NotificationEntry notificationEntry, final ExpandableNotificationRow expandableNotificationRow, boolean z, boolean z2) {
        this.mLogger.logStartNotificationIntent(notificationEntry.getKey(), pendingIntent);
        try {
            this.mActivityLaunchAnimator.startPendingIntentWithAnimation(new StatusBarLaunchAnimatorController(this.mNotificationAnimationProvider.getAnimatorController(expandableNotificationRow), this.mStatusBar, z2), z, pendingIntent.getCreatorPackage(), new ActivityLaunchAnimator.PendingIntentStarter() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda0
                @Override // com.android.systemui.animation.ActivityLaunchAnimator.PendingIntentStarter
                public final int startPendingIntent(RemoteAnimationAdapter remoteAnimationAdapter) {
                    int lambda$startNotificationIntent$4;
                    lambda$startNotificationIntent$4 = StatusBarNotificationActivityStarter.this.lambda$startNotificationIntent$4(expandableNotificationRow, pendingIntent, intent, remoteAnimationAdapter);
                    return lambda$startNotificationIntent$4;
                }
            });
        } catch (PendingIntent.CanceledException e) {
            this.mLogger.logSendingIntentFailed(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$startNotificationIntent$4(ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, Intent intent, RemoteAnimationAdapter remoteAnimationAdapter) throws PendingIntent.CanceledException {
        Bundle activityOptions;
        long andResetLastActionUpTime = expandableNotificationRow.getAndResetLastActionUpTime();
        if (andResetLastActionUpTime > 0) {
            activityOptions = StatusBar.getActivityOptions(this.mStatusBar.getDisplayId(), remoteAnimationAdapter, this.mKeyguardStateController.isShowing(), andResetLastActionUpTime);
        } else {
            activityOptions = StatusBar.getActivityOptions(this.mStatusBar.getDisplayId(), remoteAnimationAdapter);
        }
        return pendingIntent.sendAndReturnResult(this.mContext, 0, intent, null, null, null, activityOptions);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements ActivityStarter.OnDismissAction {
        final /* synthetic */ boolean val$animate;
        final /* synthetic */ int val$appUid;
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ ExpandableNotificationRow val$row;

        AnonymousClass4(ExpandableNotificationRow expandableNotificationRow, boolean z, Intent intent, int i) {
            this.val$row = expandableNotificationRow;
            this.val$animate = z;
            this.val$intent = intent;
            this.val$appUid = i;
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public boolean onDismiss() {
            final ExpandableNotificationRow expandableNotificationRow = this.val$row;
            final boolean z = this.val$animate;
            final Intent intent = this.val$intent;
            final int i = this.val$appUid;
            AsyncTask.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBarNotificationActivityStarter.AnonymousClass4.this.lambda$onDismiss$1(expandableNotificationRow, z, intent, i);
                }
            });
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDismiss$1(ExpandableNotificationRow expandableNotificationRow, boolean z, final Intent intent, final int i) {
            StatusBarNotificationActivityStarter.this.mActivityLaunchAnimator.startIntentWithAnimation(new StatusBarLaunchAnimatorController(StatusBarNotificationActivityStarter.this.mNotificationAnimationProvider.getAnimatorController(expandableNotificationRow), StatusBarNotificationActivityStarter.this.mStatusBar, true), z, intent.getPackage(), new Function1() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                /* renamed from: invoke */
                public final Object mo1949invoke(Object obj) {
                    Integer lambda$onDismiss$0;
                    lambda$onDismiss$0 = StatusBarNotificationActivityStarter.AnonymousClass4.this.lambda$onDismiss$0(intent, i, (RemoteAnimationAdapter) obj);
                    return lambda$onDismiss$0;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Integer lambda$onDismiss$0(Intent intent, int i, RemoteAnimationAdapter remoteAnimationAdapter) {
            return Integer.valueOf(TaskStackBuilder.create(StatusBarNotificationActivityStarter.this.mContext).addNextIntentWithParentStack(intent).startActivities(StatusBar.getActivityOptions(StatusBarNotificationActivityStarter.this.mStatusBar.getDisplayId(), remoteAnimationAdapter), new UserHandle(UserHandle.getUserId(i))));
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public boolean willRunAnimationOnKeyguard() {
            return this.val$animate;
        }
    }

    @Override // com.android.systemui.statusbar.notification.NotificationActivityStarter
    public void startNotificationGutsIntent(Intent intent, int i, ExpandableNotificationRow expandableNotificationRow) {
        this.mActivityStarter.dismissKeyguardThenExecute(new AnonymousClass4(expandableNotificationRow, this.mStatusBar.shouldAnimateLaunch(true), intent, i), null, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements ActivityStarter.OnDismissAction {
        final /* synthetic */ boolean val$animate;
        final /* synthetic */ boolean val$showHistory;
        final /* synthetic */ View val$view;

        AnonymousClass5(boolean z, View view, boolean z2) {
            this.val$showHistory = z;
            this.val$view = view;
            this.val$animate = z2;
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public boolean onDismiss() {
            final boolean z = this.val$showHistory;
            final View view = this.val$view;
            final boolean z2 = this.val$animate;
            AsyncTask.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBarNotificationActivityStarter.AnonymousClass5.this.lambda$onDismiss$1(z, view, z2);
                }
            });
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDismiss$1(boolean z, View view, boolean z2) {
            Intent intent;
            if (z) {
                intent = new Intent("android.settings.NOTIFICATION_HISTORY");
            } else {
                intent = new Intent("android.settings.NOTIFICATION_SETTINGS");
            }
            final TaskStackBuilder addNextIntent = TaskStackBuilder.create(StatusBarNotificationActivityStarter.this.mContext).addNextIntent(new Intent("android.settings.NOTIFICATION_SETTINGS"));
            if (z) {
                addNextIntent.addNextIntent(intent);
            }
            ActivityLaunchAnimator.Controller fromView = ActivityLaunchAnimator.Controller.fromView(view, 30);
            StatusBarNotificationActivityStarter.this.mActivityLaunchAnimator.startIntentWithAnimation(fromView == null ? null : new StatusBarLaunchAnimatorController(fromView, StatusBarNotificationActivityStarter.this.mStatusBar, true), z2, intent.getPackage(), new Function1() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                /* renamed from: invoke */
                public final Object mo1949invoke(Object obj) {
                    Integer lambda$onDismiss$0;
                    lambda$onDismiss$0 = StatusBarNotificationActivityStarter.AnonymousClass5.this.lambda$onDismiss$0(addNextIntent, (RemoteAnimationAdapter) obj);
                    return lambda$onDismiss$0;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Integer lambda$onDismiss$0(TaskStackBuilder taskStackBuilder, RemoteAnimationAdapter remoteAnimationAdapter) {
            return Integer.valueOf(taskStackBuilder.startActivities(StatusBar.getActivityOptions(StatusBarNotificationActivityStarter.this.mStatusBar.getDisplayId(), remoteAnimationAdapter), UserHandle.CURRENT));
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public boolean willRunAnimationOnKeyguard() {
            return this.val$animate;
        }
    }

    @Override // com.android.systemui.statusbar.notification.NotificationActivityStarter
    public void startHistoryIntent(View view, boolean z) {
        this.mActivityStarter.dismissKeyguardThenExecute(new AnonymousClass5(z, view, this.mStatusBar.shouldAnimateLaunch(true)), null, false);
    }

    private void removeHunAfterClick(ExpandableNotificationRow expandableNotificationRow) {
        String key = expandableNotificationRow.getEntry().getSbn().getKey();
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        if (headsUpManagerPhone == null || !headsUpManagerPhone.isAlerting(key)) {
            return;
        }
        if (this.mPresenter.isPresenterFullyCollapsed()) {
            HeadsUpUtil.setNeedsHeadsUpDisappearAnimationAfterClick(expandableNotificationRow, true);
        }
        this.mHeadsUpManager.removeNotification(key, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFullScreenIntent(NotificationEntry notificationEntry) {
        if (this.mNotificationInterruptStateProvider.shouldLaunchFullScreenIntentWhenAdded(notificationEntry)) {
            if (shouldSuppressFullScreenIntent(notificationEntry)) {
                this.mLogger.logFullScreenIntentSuppressedByDnD(notificationEntry.getKey());
            } else if (notificationEntry.getImportance() < 4) {
                this.mLogger.logFullScreenIntentNotImportantEnough(notificationEntry.getKey());
            } else {
                this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        StatusBarNotificationActivityStarter.this.lambda$handleFullScreenIntent$5();
                    }
                });
                PendingIntent pendingIntent = notificationEntry.getSbn().getNotification().fullScreenIntent;
                this.mLogger.logSendingFullScreenIntent(notificationEntry.getKey(), pendingIntent);
                try {
                    EventLog.writeEvent(36002, notificationEntry.getKey());
                    pendingIntent.send();
                    notificationEntry.notifyFullScreenIntentLaunched();
                    this.mMetricsLogger.count("note_fullscreen", 1);
                } catch (PendingIntent.CanceledException unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleFullScreenIntent$5() {
        try {
            this.mDreamManager.awaken();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // com.android.systemui.statusbar.notification.NotificationActivityStarter
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
        handler.post(new StatusBar$$ExternalSyntheticLambda11(shadeController));
    }

    private boolean shouldSuppressFullScreenIntent(NotificationEntry notificationEntry) {
        if (this.mPresenter.isDeviceInVrMode()) {
            return true;
        }
        return notificationEntry.shouldSuppressFullScreenIntent();
    }

    private int getVisibleNotificationsCount() {
        if (this.mFeatureFlags.isNewNotifPipelineRenderingEnabled()) {
            return this.mNotifPipeline.getShadeListCount();
        }
        return this.mEntryManager.getActiveNotificationsCount();
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final ActivityIntentHelper mActivityIntentHelper;
        private ActivityLaunchAnimator mActivityLaunchAnimator;
        private final ActivityStarter mActivityStarter;
        private final Lazy<AssistManager> mAssistManagerLazy;
        private final Optional<BubblesManager> mBubblesManagerOptional;
        private final NotificationClickNotifier mClickNotifier;
        private final CommandQueue mCommandQueue;
        private final Context mContext;
        private final IDreamManager mDreamManager;
        private final NotificationEntryManager mEntryManager;
        private final FeatureFlags mFeatureFlags;
        private final GroupMembershipManager mGroupMembershipManager;
        private final HeadsUpManagerPhone mHeadsUpManager;
        private final KeyguardManager mKeyguardManager;
        private final KeyguardStateController mKeyguardStateController;
        private final LockPatternUtils mLockPatternUtils;
        private final NotificationLockscreenUserManager mLockscreenUserManager;
        private final StatusBarNotificationActivityStarterLogger mLogger;
        private final Handler mMainThreadHandler;
        private final MetricsLogger mMetricsLogger;
        private final NotifPipeline mNotifPipeline;
        private NotificationLaunchAnimatorControllerProvider mNotificationAnimationProvider;
        private final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
        private NotificationPanelViewController mNotificationPanelViewController;
        private NotificationPresenter mNotificationPresenter;
        private final OnUserInteractionCallback mOnUserInteractionCallback;
        private final StatusBarRemoteInputCallback mRemoteInputCallback;
        private final NotificationRemoteInputManager mRemoteInputManager;
        private final ShadeController mShadeController;
        private StatusBar mStatusBar;
        private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
        private final StatusBarStateController mStatusBarStateController;
        private final Executor mUiBgExecutor;

        public Builder(Context context, CommandQueue commandQueue, Handler handler, Executor executor, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, HeadsUpManagerPhone headsUpManagerPhone, ActivityStarter activityStarter, NotificationClickNotifier notificationClickNotifier, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardManager keyguardManager, IDreamManager iDreamManager, Optional<BubblesManager> optional, Lazy<AssistManager> lazy, NotificationRemoteInputManager notificationRemoteInputManager, GroupMembershipManager groupMembershipManager, NotificationLockscreenUserManager notificationLockscreenUserManager, ShadeController shadeController, KeyguardStateController keyguardStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, LockPatternUtils lockPatternUtils, StatusBarRemoteInputCallback statusBarRemoteInputCallback, ActivityIntentHelper activityIntentHelper, FeatureFlags featureFlags, MetricsLogger metricsLogger, StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger, OnUserInteractionCallback onUserInteractionCallback) {
            this.mContext = context;
            this.mCommandQueue = commandQueue;
            this.mMainThreadHandler = handler;
            this.mUiBgExecutor = executor;
            this.mEntryManager = notificationEntryManager;
            this.mNotifPipeline = notifPipeline;
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
            this.mRemoteInputCallback = statusBarRemoteInputCallback;
            this.mActivityIntentHelper = activityIntentHelper;
            this.mFeatureFlags = featureFlags;
            this.mMetricsLogger = metricsLogger;
            this.mLogger = statusBarNotificationActivityStarterLogger;
            this.mOnUserInteractionCallback = onUserInteractionCallback;
        }

        public Builder setStatusBar(StatusBar statusBar) {
            this.mStatusBar = statusBar;
            return this;
        }

        public Builder setNotificationPresenter(NotificationPresenter notificationPresenter) {
            this.mNotificationPresenter = notificationPresenter;
            return this;
        }

        public Builder setActivityLaunchAnimator(ActivityLaunchAnimator activityLaunchAnimator) {
            this.mActivityLaunchAnimator = activityLaunchAnimator;
            return this;
        }

        public Builder setNotificationAnimatorControllerProvider(NotificationLaunchAnimatorControllerProvider notificationLaunchAnimatorControllerProvider) {
            this.mNotificationAnimationProvider = notificationLaunchAnimatorControllerProvider;
            return this;
        }

        public Builder setNotificationPanelViewController(NotificationPanelViewController notificationPanelViewController) {
            this.mNotificationPanelViewController = notificationPanelViewController;
            return this;
        }

        public StatusBarNotificationActivityStarter build() {
            return new StatusBarNotificationActivityStarter(this.mContext, this.mCommandQueue, this.mMainThreadHandler, this.mUiBgExecutor, this.mEntryManager, this.mNotifPipeline, this.mHeadsUpManager, this.mActivityStarter, this.mClickNotifier, this.mStatusBarStateController, this.mStatusBarKeyguardViewManager, this.mKeyguardManager, this.mDreamManager, this.mBubblesManagerOptional, this.mAssistManagerLazy, this.mRemoteInputManager, this.mGroupMembershipManager, this.mLockscreenUserManager, this.mShadeController, this.mKeyguardStateController, this.mNotificationInterruptStateProvider, this.mLockPatternUtils, this.mRemoteInputCallback, this.mActivityIntentHelper, this.mFeatureFlags, this.mMetricsLogger, this.mLogger, this.mOnUserInteractionCallback, this.mStatusBar, this.mNotificationPresenter, this.mNotificationPanelViewController, this.mActivityLaunchAnimator, this.mNotificationAnimationProvider);
        }
    }
}
