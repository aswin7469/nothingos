package com.android.systemui.statusbar.phone;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.Slog;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingMessage;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.ForegroundServiceNotificationListener;
import com.android.systemui.InitController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.AboveShelfObserver;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

@CentralSurfacesComponent.CentralSurfacesScope
class StatusBarNotificationPresenter implements NotificationPresenter, ConfigurationController.ConfigurationListener, NotificationRowBinderImpl.BindRowCallback, CommandQueue.Callbacks {
    private static final String TAG = "StatusBarNotificationPresenter";
    private final AboveShelfObserver mAboveShelfObserver;
    /* access modifiers changed from: private */
    public final AccessibilityManager mAccessibilityManager;
    private final ActivityStarter mActivityStarter;
    /* access modifiers changed from: private */
    public final IStatusBarService mBarService;
    /* access modifiers changed from: private */
    public final CentralSurfaces mCentralSurfaces;
    private final NotificationInfo.CheckSaveListener mCheckSaveListener = new NotificationInfo.CheckSaveListener() {
        public void checkSave(Runnable runnable, StatusBarNotification statusBarNotification) {
            if (!StatusBarNotificationPresenter.this.mLockscreenUserManager.isLockscreenPublicMode(statusBarNotification.getUser().getIdentifier()) || !StatusBarNotificationPresenter.this.mKeyguardManager.isKeyguardLocked()) {
                runnable.run();
            } else {
                StatusBarNotificationPresenter.this.onLockedNotificationImportanceChange(new StatusBarNotificationPresenter$2$$ExternalSyntheticLambda0(runnable));
            }
        }
    };
    /* access modifiers changed from: private */
    public final CommandQueue mCommandQueue;
    private boolean mDispatchUiModeChangeOnUserSwitched;
    private final DozeScrimController mDozeScrimController;
    private final DynamicPrivacyController mDynamicPrivacyController;
    private final NotificationEntryManager mEntryManager;
    private final NotificationGutsManager mGutsManager;
    private final HeadsUpManagerPhone mHeadsUpManager;
    private final NotificationInterruptSuppressor mInterruptSuppressor = new NotificationInterruptSuppressor() {
        public String getName() {
            return StatusBarNotificationPresenter.TAG;
        }

        public boolean suppressAwakeHeadsUp(NotificationEntry notificationEntry) {
            StatusBarNotification sbn = notificationEntry.getSbn();
            if (StatusBarNotificationPresenter.this.mCentralSurfaces.isOccluded()) {
                boolean z = StatusBarNotificationPresenter.this.mLockscreenUserManager.isLockscreenPublicMode(StatusBarNotificationPresenter.this.mLockscreenUserManager.getCurrentUserId()) || StatusBarNotificationPresenter.this.mLockscreenUserManager.isLockscreenPublicMode(sbn.getUserId());
                boolean needsRedaction = StatusBarNotificationPresenter.this.mLockscreenUserManager.needsRedaction(notificationEntry);
                if (z && needsRedaction) {
                    return true;
                }
            }
            if (!StatusBarNotificationPresenter.this.mCommandQueue.panelsEnabled()) {
                return true;
            }
            return sbn.getNotification().fullScreenIntent != null && ((StatusBarNotificationPresenter.this.mKeyguardStateController.isShowing() && !StatusBarNotificationPresenter.this.mCentralSurfaces.isOccluded()) || StatusBarNotificationPresenter.this.mAccessibilityManager.isTouchExplorationEnabled());
        }

        public boolean suppressAwakeInterruptions(NotificationEntry notificationEntry) {
            return StatusBarNotificationPresenter.this.isDeviceInVrMode();
        }

        public boolean suppressInterruptions(NotificationEntry notificationEntry) {
            return StatusBarNotificationPresenter.this.mCentralSurfaces.areNotificationAlertsDisabled();
        }
    };
    private final KeyguardIndicationController mKeyguardIndicationController;
    /* access modifiers changed from: private */
    public final KeyguardManager mKeyguardManager;
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LockscreenGestureLogger mLockscreenGestureLogger;
    /* access modifiers changed from: private */
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    private final NotificationMediaManager mMediaManager;
    private final NotificationListContainer mNotifListContainer;
    private final NotifPipelineFlags mNotifPipelineFlags;
    private final NotifShadeEventSource mNotifShadeEventSource;
    private final NotificationPanelViewController mNotificationPanel;
    private TextView mNotificationPanelDebugText;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    private final NotificationGutsManager.OnSettingsClickListener mOnSettingsClickListener = new NotificationGutsManager.OnSettingsClickListener() {
        public void onSettingsClick(String str) {
            try {
                StatusBarNotificationPresenter.this.mBarService.onNotificationSettingsViewed(str);
            } catch (RemoteException unused) {
            }
        }
    };
    private boolean mReinflateNotificationsOnUserSwitched;
    private final ScrimController mScrimController;
    private final ShadeController mShadeController;
    private final LockscreenShadeTransitionController mShadeTransitionController;
    private final SysuiStatusBarStateController mStatusBarStateController;
    private final NotificationViewHierarchyManager mViewHierarchyManager;
    protected boolean mVrMode;
    private final IVrStateCallbacks mVrStateCallbacks;

    static /* synthetic */ boolean lambda$onExpandClicked$2() {
        return false;
    }

    @Inject
    StatusBarNotificationPresenter(Context context, NotificationPanelViewController notificationPanelViewController, HeadsUpManagerPhone headsUpManagerPhone, NotificationShadeWindowView notificationShadeWindowView, ActivityStarter activityStarter, NotificationStackScrollLayoutController notificationStackScrollLayoutController, DozeScrimController dozeScrimController, ScrimController scrimController, NotificationShadeWindowController notificationShadeWindowController, DynamicPrivacyController dynamicPrivacyController, KeyguardStateController keyguardStateController, KeyguardIndicationController keyguardIndicationController, CentralSurfaces centralSurfaces, ShadeController shadeController, LockscreenShadeTransitionController lockscreenShadeTransitionController, CommandQueue commandQueue, NotificationViewHierarchyManager notificationViewHierarchyManager, NotificationLockscreenUserManager notificationLockscreenUserManager, SysuiStatusBarStateController sysuiStatusBarStateController, NotifShadeEventSource notifShadeEventSource, NotificationEntryManager notificationEntryManager, NotificationMediaManager notificationMediaManager, NotificationGutsManager notificationGutsManager, KeyguardUpdateMonitor keyguardUpdateMonitor, LockscreenGestureLogger lockscreenGestureLogger, InitController initController, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationRemoteInputManager notificationRemoteInputManager, ConfigurationController configurationController, NotifPipelineFlags notifPipelineFlags, NotificationRemoteInputManager.Callback callback, NotificationListContainer notificationListContainer) {
        Context context2 = context;
        NotificationRemoteInputManager notificationRemoteInputManager2 = notificationRemoteInputManager;
        C31031 r3 = new IVrStateCallbacks.Stub() {
            public void onVrStateChanged(boolean z) {
                StatusBarNotificationPresenter.this.mVrMode = z;
            }
        };
        this.mVrStateCallbacks = r3;
        this.mActivityStarter = activityStarter;
        this.mKeyguardStateController = keyguardStateController;
        this.mNotificationPanel = notificationPanelViewController;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mDynamicPrivacyController = dynamicPrivacyController;
        this.mKeyguardIndicationController = keyguardIndicationController;
        this.mCentralSurfaces = centralSurfaces;
        this.mShadeController = shadeController;
        this.mShadeTransitionController = lockscreenShadeTransitionController;
        this.mCommandQueue = commandQueue;
        this.mViewHierarchyManager = notificationViewHierarchyManager;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mNotifShadeEventSource = notifShadeEventSource;
        this.mEntryManager = notificationEntryManager;
        this.mMediaManager = notificationMediaManager;
        this.mGutsManager = notificationGutsManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockscreenGestureLogger = lockscreenGestureLogger;
        AboveShelfObserver aboveShelfObserver = new AboveShelfObserver(notificationStackScrollLayoutController.getView());
        this.mAboveShelfObserver = aboveShelfObserver;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mNotifPipelineFlags = notifPipelineFlags;
        NotificationShadeWindowView notificationShadeWindowView2 = notificationShadeWindowView;
        aboveShelfObserver.setListener((AboveShelfObserver.HasViewAboveShelfChangedListener) notificationShadeWindowView.findViewById(C1893R.C1897id.notification_container_parent));
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mDozeScrimController = dozeScrimController;
        this.mScrimController = scrimController;
        this.mKeyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mNotifListContainer = notificationListContainer;
        IVrManager asInterface = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));
        if (asInterface != null) {
            try {
                asInterface.registerListener(r3);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to register VR mode state listener: " + e);
            }
        }
        notificationRemoteInputManager2.setUpWithCallback(callback, this.mNotificationPanel.createRemoteInputDelegate());
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationStackScrollLayoutController;
        initController.addPostInitTask(new StatusBarNotificationPresenter$$ExternalSyntheticLambda2(this, notificationStackScrollLayoutController, notificationRemoteInputManager2, notificationInterruptStateProvider));
        configurationController.addCallback(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-StatusBarNotificationPresenter */
    public /* synthetic */ void mo45328xd80122d2(NotificationStackScrollLayoutController notificationStackScrollLayoutController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationInterruptStateProvider notificationInterruptStateProvider) {
        this.mKeyguardIndicationController.init();
        this.mViewHierarchyManager.setUpWithPresenter(this, notificationStackScrollLayoutController.getNotifStackController(), this.mNotifListContainer);
        this.mNotifShadeEventSource.setShadeEmptiedCallback(new StatusBarNotificationPresenter$$ExternalSyntheticLambda0(this));
        this.mNotifShadeEventSource.setNotifRemovedByUserCallback(new StatusBarNotificationPresenter$$ExternalSyntheticLambda1(this));
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mEntryManager.setUpWithPresenter(this);
            this.mEntryManager.addNotificationLifetimeExtender(this.mHeadsUpManager);
            this.mEntryManager.addNotificationLifetimeExtender(this.mGutsManager);
            this.mEntryManager.addNotificationLifetimeExtenders(notificationRemoteInputManager.getLifetimeExtenders());
        }
        notificationInterruptStateProvider.addSuppressor(this.mInterruptSuppressor);
        this.mLockscreenUserManager.setUpWithPresenter(this);
        this.mMediaManager.setUpWithPresenter(this);
        this.mGutsManager.setUpWithPresenter(this, this.mNotifListContainer, this.mCheckSaveListener, this.mOnSettingsClickListener);
        Dependency.get(ForegroundServiceNotificationListener.class);
        onUserSwitched(this.mLockscreenUserManager.getCurrentUserId());
    }

    /* access modifiers changed from: private */
    public void maybeClosePanelForShadeEmptied() {
        if (!this.mNotificationPanel.isTracking() && !this.mNotificationPanel.isQsExpanded() && this.mStatusBarStateController.getState() == 2 && !isCollapsing()) {
            this.mStatusBarStateController.setState(1);
        }
    }

    public void onDensityOrFontScaleChanged() {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            MessagingMessage.dropCache();
            MessagingGroup.dropCache();
            if (!this.mKeyguardUpdateMonitor.isSwitchingUser()) {
                updateNotificationsOnDensityOrFontScaleChanged();
            } else {
                this.mReinflateNotificationsOnUserSwitched = true;
            }
        }
    }

    public void onUiModeChanged() {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            if (!this.mKeyguardUpdateMonitor.isSwitchingUser()) {
                updateNotificationsOnUiModeChanged();
            } else {
                this.mDispatchUiModeChangeOnUserSwitched = true;
            }
        }
    }

    public void onThemeChanged() {
        onDensityOrFontScaleChanged();
    }

    private void updateNotificationsOnUiModeChanged() {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            List<NotificationEntry> activeNotificationsForCurrentUser = this.mEntryManager.getActiveNotificationsForCurrentUser();
            for (int i = 0; i < activeNotificationsForCurrentUser.size(); i++) {
                ExpandableNotificationRow row = activeNotificationsForCurrentUser.get(i).getRow();
                if (row != null) {
                    row.onUiModeChanged();
                }
            }
        }
    }

    private void updateNotificationsOnDensityOrFontScaleChanged() {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            List<NotificationEntry> activeNotificationsForCurrentUser = this.mEntryManager.getActiveNotificationsForCurrentUser();
            for (int i = 0; i < activeNotificationsForCurrentUser.size(); i++) {
                NotificationEntry notificationEntry = activeNotificationsForCurrentUser.get(i);
                notificationEntry.onDensityOrFontScaleChanged();
                if (notificationEntry.areGutsExposed()) {
                    this.mGutsManager.onDensityOrFontScaleChanged(notificationEntry);
                }
            }
        }
    }

    public boolean isCollapsing() {
        return this.mNotificationPanel.isCollapsing() || this.mNotificationShadeWindowController.isLaunchingActivity();
    }

    /* access modifiers changed from: private */
    public void maybeEndAmbientPulse() {
        if (this.mNotificationPanel.hasPulsingNotifications() && !this.mHeadsUpManager.hasNotifications()) {
            this.mDozeScrimController.pulseOutNow();
        }
    }

    /* renamed from: updateNotificationViews */
    public void mo45329x4e722c2d(String str) {
        if (!this.mNotifPipelineFlags.checkLegacyPipelineEnabled() || this.mScrimController == null) {
            return;
        }
        if (isCollapsing()) {
            this.mShadeController.addPostCollapseAction(new StatusBarNotificationPresenter$$ExternalSyntheticLambda4(this, str));
            return;
        }
        this.mViewHierarchyManager.updateNotificationViews();
        this.mNotificationPanel.updateNotificationViews(str);
    }

    public void onUserSwitched(int i) {
        this.mHeadsUpManager.setUser(i);
        this.mCommandQueue.animateCollapsePanels();
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            if (this.mReinflateNotificationsOnUserSwitched) {
                updateNotificationsOnDensityOrFontScaleChanged();
                this.mReinflateNotificationsOnUserSwitched = false;
            }
            if (this.mDispatchUiModeChangeOnUserSwitched) {
                updateNotificationsOnUiModeChanged();
                this.mDispatchUiModeChangeOnUserSwitched = false;
            }
            mo45329x4e722c2d("user switched");
        }
        this.mMediaManager.clearCurrentMediaNotification();
        this.mCentralSurfaces.setLockscreenUser(i);
        updateMediaMetaData(true, false);
    }

    public void onBindRow(ExpandableNotificationRow expandableNotificationRow) {
        expandableNotificationRow.setAboveShelfChangedListener(this.mAboveShelfObserver);
        KeyguardStateController keyguardStateController = this.mKeyguardStateController;
        Objects.requireNonNull(keyguardStateController);
        expandableNotificationRow.setSecureStateProvider(new StatusBarNotificationPresenter$$ExternalSyntheticLambda3(keyguardStateController));
    }

    public boolean isPresenterFullyCollapsed() {
        return this.mNotificationPanel.isFullyCollapsed();
    }

    public void onActivated(ActivatableNotificationView activatableNotificationView) {
        onActivated();
        if (activatableNotificationView != null) {
            this.mNotificationPanel.setActivatedChild(activatableNotificationView);
        }
    }

    public void onActivated() {
        this.mLockscreenGestureLogger.write(192, 0, 0);
        this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_NOTIFICATION_FALSE_TOUCH);
        ActivatableNotificationView activatedChild = this.mNotificationPanel.getActivatedChild();
        if (activatedChild != null) {
            activatedChild.makeInactive(true);
        }
    }

    public void onActivationReset(ActivatableNotificationView activatableNotificationView) {
        if (activatableNotificationView == this.mNotificationPanel.getActivatedChild()) {
            this.mNotificationPanel.setActivatedChild((ActivatableNotificationView) null);
            this.mKeyguardIndicationController.hideTransientIndication();
        }
    }

    public void updateMediaMetaData(boolean z, boolean z2) {
        this.mMediaManager.updateMediaMetaData(z, z2);
    }

    public void onUpdateRowStates() {
        this.mNotificationPanel.onUpdateRowStates();
    }

    public void onExpandClicked(NotificationEntry notificationEntry, View view, boolean z) {
        this.mHeadsUpManager.setExpanded(notificationEntry, z);
        this.mCentralSurfaces.wakeUpIfDozing(SystemClock.uptimeMillis(), view, "NOTIFICATION_CLICK");
        if (!z) {
            return;
        }
        if (this.mStatusBarStateController.getState() == 1) {
            this.mShadeTransitionController.goToLockedShade(notificationEntry.getRow());
        } else if (notificationEntry.isSensitive() && this.mDynamicPrivacyController.isInLockedDownShade()) {
            this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
            this.mActivityStarter.dismissKeyguardThenExecute(new StatusBarNotificationPresenter$$ExternalSyntheticLambda5(), (Runnable) null, false);
        }
    }

    public boolean isDeviceInVrMode() {
        return this.mVrMode;
    }

    /* access modifiers changed from: private */
    public void onLockedNotificationImportanceChange(ActivityStarter.OnDismissAction onDismissAction) {
        this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
        this.mActivityStarter.dismissKeyguardThenExecute(onDismissAction, (Runnable) null, true);
    }
}
