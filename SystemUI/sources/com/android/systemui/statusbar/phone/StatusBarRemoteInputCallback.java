package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.view.View;
import android.view.ViewParent;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class StatusBarRemoteInputCallback implements NotificationRemoteInputManager.Callback, CommandQueue.Callbacks, StatusBarStateController.StateListener {
    private final ActionClickLogger mActionClickLogger;
    private final ActivityIntentHelper mActivityIntentHelper;
    private final ActivityStarter mActivityStarter;
    protected BroadcastReceiver mChallengeReceiver;
    private final CommandQueue mCommandQueue;
    private final Context mContext;
    private int mDisabled2;
    private Executor mExecutor;
    private final GroupExpansionManager mGroupExpansionManager;
    private KeyguardManager mKeyguardManager;
    private final KeyguardStateController mKeyguardStateController;
    private final NotificationLockscreenUserManager mLockscreenUserManager;
    private View mPendingRemoteInputView;
    private View mPendingWorkRemoteInputView;
    private final ShadeController mShadeController;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final SysuiStatusBarStateController mStatusBarStateController;

    public StatusBarRemoteInputCallback(Context context, GroupExpansionManager groupExpansionManager, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, ActivityStarter activityStarter, ShadeController shadeController, CommandQueue commandQueue, ActionClickLogger actionClickLogger, Executor executor) {
        ChallengeReceiver challengeReceiver = new ChallengeReceiver();
        this.mChallengeReceiver = challengeReceiver;
        this.mContext = context;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mShadeController = shadeController;
        this.mExecutor = executor;
        context.registerReceiverAsUser(challengeReceiver, UserHandle.ALL, new IntentFilter("android.intent.action.DEVICE_LOCKED_CHANGED"), null, null);
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mKeyguardStateController = keyguardStateController;
        SysuiStatusBarStateController sysuiStatusBarStateController = (SysuiStatusBarStateController) statusBarStateController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mActivityStarter = activityStarter;
        sysuiStatusBarStateController.addCallback(this);
        this.mKeyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        this.mCommandQueue = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mActionClickLogger = actionClickLogger;
        this.mActivityIntentHelper = new ActivityIntentHelper(context);
        this.mGroupExpansionManager = groupExpansionManager;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        boolean z = this.mPendingRemoteInputView != null;
        if (i == 0) {
            if ((!this.mStatusBarStateController.leaveOpenOnKeyguardHide() && !z) || this.mStatusBarStateController.isKeyguardRequested() || !this.mKeyguardStateController.isUnlocked()) {
                return;
            }
            if (z) {
                Executor executor = this.mExecutor;
                final View view = this.mPendingRemoteInputView;
                Objects.requireNonNull(view);
                executor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        view.callOnClick();
                    }
                });
            }
            this.mPendingRemoteInputView = null;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public void onLockedRemoteInput(ExpandableNotificationRow expandableNotificationRow, View view) {
        if (!expandableNotificationRow.isPinned()) {
            this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
        }
        this.mStatusBarKeyguardViewManager.showGenericBouncer(true);
        this.mPendingRemoteInputView = view;
    }

    protected void onWorkChallengeChanged() {
        this.mLockscreenUserManager.updatePublicMode();
        if (this.mPendingWorkRemoteInputView == null || this.mLockscreenUserManager.isAnyProfilePublicMode()) {
            return;
        }
        this.mShadeController.postOnShadeExpanded(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarRemoteInputCallback.this.lambda$onWorkChallengeChanged$2();
            }
        });
        this.mShadeController.instantExpandNotificationsPanel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWorkChallengeChanged$2() {
        View view = this.mPendingWorkRemoteInputView;
        if (view == null) {
            return;
        }
        ViewParent parent = view.getParent();
        while (!(parent instanceof ExpandableNotificationRow)) {
            if (parent == null) {
                return;
            }
            parent = parent.getParent();
        }
        final ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) parent;
        ViewParent parent2 = expandableNotificationRow.getParent();
        if (!(parent2 instanceof NotificationStackScrollLayout)) {
            return;
        }
        final NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) parent2;
        expandableNotificationRow.makeActionsVisibile();
        expandableNotificationRow.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarRemoteInputCallback.this.lambda$onWorkChallengeChanged$1(notificationStackScrollLayout, expandableNotificationRow);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWorkChallengeChanged$1(final NotificationStackScrollLayout notificationStackScrollLayout, ExpandableNotificationRow expandableNotificationRow) {
        Runnable runnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarRemoteInputCallback.this.lambda$onWorkChallengeChanged$0(notificationStackScrollLayout);
            }
        };
        if (notificationStackScrollLayout.scrollTo(expandableNotificationRow)) {
            notificationStackScrollLayout.setFinishScrollingCallback(runnable);
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWorkChallengeChanged$0(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.mPendingWorkRemoteInputView.callOnClick();
        this.mPendingWorkRemoteInputView = null;
        notificationStackScrollLayout.setFinishScrollingCallback(null);
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public void onMakeExpandedVisibleForRemoteInput(ExpandableNotificationRow expandableNotificationRow, View view, boolean z, Runnable runnable) {
        if (!z && this.mKeyguardStateController.isShowing()) {
            onLockedRemoteInput(expandableNotificationRow, view);
            return;
        }
        if (expandableNotificationRow.isChildInGroup() && !expandableNotificationRow.areChildrenExpanded()) {
            this.mGroupExpansionManager.toggleGroupExpansion(expandableNotificationRow.getEntry());
        }
        expandableNotificationRow.setUserExpanded(true);
        expandableNotificationRow.getPrivateLayout().setOnExpandedVisibleListener(runnable);
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public void onLockedWorkRemoteInput(int i, ExpandableNotificationRow expandableNotificationRow, View view) {
        this.mCommandQueue.animateCollapsePanels();
        startWorkChallengeIfNecessary(i, null, null);
        this.mPendingWorkRemoteInputView = view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startWorkChallengeIfNecessary(int i, IntentSender intentSender, String str) {
        this.mPendingWorkRemoteInputView = null;
        Intent createConfirmDeviceCredentialIntent = this.mKeyguardManager.createConfirmDeviceCredentialIntent(null, null, i);
        if (createConfirmDeviceCredentialIntent == null) {
            return false;
        }
        Intent intent = new Intent("com.android.systemui.statusbar.work_challenge_unlocked_notification_action");
        intent.putExtra("android.intent.extra.INTENT", intentSender);
        intent.putExtra("android.intent.extra.INDEX", str);
        intent.setPackage(this.mContext.getPackageName());
        createConfirmDeviceCredentialIntent.putExtra("android.intent.extra.INTENT", PendingIntent.getBroadcast(this.mContext, 0, intent, 1409286144).getIntentSender());
        try {
            ActivityManager.getService().startConfirmDeviceCredentialIntent(createConfirmDeviceCredentialIntent, (Bundle) null);
            return true;
        } catch (RemoteException unused) {
            return true;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public boolean shouldHandleRemoteInput(View view, PendingIntent pendingIntent) {
        return (this.mDisabled2 & 4) != 0;
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.Callback
    public boolean handleRemoteViewClick(View view, final PendingIntent pendingIntent, boolean z, final NotificationRemoteInputManager.ClickHandler clickHandler) {
        if (pendingIntent.isActivity() || z) {
            this.mActionClickLogger.logWaitingToCloseKeyguard(pendingIntent);
            this.mActivityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback$$ExternalSyntheticLambda0
                @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                public final boolean onDismiss() {
                    boolean lambda$handleRemoteViewClick$3;
                    lambda$handleRemoteViewClick$3 = StatusBarRemoteInputCallback.this.lambda$handleRemoteViewClick$3(pendingIntent, clickHandler);
                    return lambda$handleRemoteViewClick$3;
                }
            }, null, this.mActivityIntentHelper.wouldLaunchResolverActivity(pendingIntent.getIntent(), this.mLockscreenUserManager.getCurrentUserId()));
            return true;
        }
        return clickHandler.handleClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$handleRemoteViewClick$3(PendingIntent pendingIntent, NotificationRemoteInputManager.ClickHandler clickHandler) {
        this.mActionClickLogger.logKeyguardGone(pendingIntent);
        try {
            ActivityManager.getService().resumeAppSwitches();
        } catch (RemoteException unused) {
        }
        return clickHandler.handleClick() && this.mShadeController.closeShadeIfOpen();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void disable(int i, int i2, int i3, boolean z) {
        if (i == this.mContext.getDisplayId()) {
            this.mDisabled2 = i3;
        }
    }

    /* loaded from: classes.dex */
    protected class ChallengeReceiver extends BroadcastReceiver {
        protected ChallengeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (!"android.intent.action.DEVICE_LOCKED_CHANGED".equals(action) || intExtra == StatusBarRemoteInputCallback.this.mLockscreenUserManager.getCurrentUserId() || !StatusBarRemoteInputCallback.this.mLockscreenUserManager.isCurrentProfile(intExtra)) {
                return;
            }
            StatusBarRemoteInputCallback.this.onWorkChallengeChanged();
        }
    }
}
