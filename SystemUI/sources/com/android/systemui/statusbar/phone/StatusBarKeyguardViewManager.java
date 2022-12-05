package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.WindowInsets;
import android.view.WindowManagerGlobal;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageArea;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.DejankUtils;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.FaceAuthScreenBrightnessController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.ViewGroupFadeHelper;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class StatusBarKeyguardViewManager implements RemoteInputController.Callback, StatusBarStateController.StateListener, ConfigurationController.ConfigurationListener, PanelExpansionListener, NavigationModeController.ModeChangedListener, KeyguardViewController, WakefulnessLifecycle.Observer {
    private ActivityStarter.OnDismissAction mAfterKeyguardGoneAction;
    private AlternateAuthInterceptor mAlternateAuthInterceptor;
    private BiometricUnlockController mBiometricUnlockController;
    protected KeyguardBouncer mBouncer;
    private KeyguardBypassController mBypassController;
    private final ConfigurationController mConfigurationController;
    private ViewGroup mContainer;
    protected final Context mContext;
    private boolean mDismissActionWillAnimateOnKeyguard;
    private final DockManager mDockManager;
    private boolean mDozing;
    private final Optional<FaceAuthScreenBrightnessController> mFaceAuthScreenBrightnessController;
    private boolean mGesturalNav;
    private boolean mIsDocked;
    private final KeyguardBouncer.Factory mKeyguardBouncerFactory;
    private Runnable mKeyguardGoneCancelAction;
    private KeyguardMessageAreaController mKeyguardMessageAreaController;
    private final KeyguardMessageAreaController.Factory mKeyguardMessageAreaFactory;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateManager;
    private int mLastBiometricMode;
    private boolean mLastBouncerDismissible;
    private boolean mLastBouncerIsOrWillBeShowing;
    private boolean mLastBouncerShowing;
    private boolean mLastDozing;
    private boolean mLastGesturalNav;
    private boolean mLastIsDocked;
    protected boolean mLastOccluded;
    private boolean mLastPulsing;
    protected boolean mLastRemoteInputActive;
    protected boolean mLastShowing;
    protected LockPatternUtils mLockPatternUtils;
    private final NotificationMediaManager mMediaManager;
    private final NavigationModeController mNavigationModeController;
    private View mNotificationContainer;
    private NotificationPanelViewController mNotificationPanelViewController;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    protected boolean mOccluded;
    private DismissWithActionRequest mPendingWakeupAction;
    private boolean mPulsing;
    private boolean mQsExpanded;
    protected boolean mRemoteInputActive;
    protected boolean mShowing;
    protected StatusBar mStatusBar;
    private final SysuiStatusBarStateController mStatusBarStateController;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    protected ViewMediatorCallback mViewMediatorCallback;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private final KeyguardBouncer.BouncerExpansionCallback mExpansionCallback = new KeyguardBouncer.BouncerExpansionCallback() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.1
        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public void onFullyHidden() {
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public void onFullyShown() {
            StatusBarKeyguardViewManager.this.updateStates();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public void onStartingToHide() {
            StatusBarKeyguardViewManager.this.updateStates();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public void onStartingToShow() {
            StatusBarKeyguardViewManager.this.updateStates();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public void onExpansionChanged(float f) {
            if (StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor != null) {
                StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor.setBouncerExpansionChanged(f);
            }
            StatusBarKeyguardViewManager.this.updateStates();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public void onVisibilityChanged(boolean z) {
            if (!z) {
                StatusBarKeyguardViewManager.this.cancelPostAuthActions();
            }
            if (StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor != null) {
                StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor.onBouncerVisibilityChanged();
            }
        }
    };
    private final DockManager.DockEventListener mDockEventListener = new DockManager.DockEventListener() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.2
    };
    private boolean mGlobalActionsVisible = false;
    private boolean mLastGlobalActionsVisible = false;
    protected boolean mFirstUpdate = true;
    private final ArrayList<Runnable> mAfterKeyguardGoneRunnables = new ArrayList<>();
    private final KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.3
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onEmergencyCallAction() {
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = StatusBarKeyguardViewManager.this;
            if (statusBarKeyguardViewManager.mOccluded) {
                statusBarKeyguardViewManager.reset(true);
            }
        }
    };
    private Runnable mMakeNavigationBarVisibleRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.7
        @Override // java.lang.Runnable
        public void run() {
            StatusBarKeyguardViewManager.this.mStatusBar.getNotificationShadeWindowView().getWindowInsetsController().show(WindowInsets.Type.navigationBars());
        }
    };

    /* loaded from: classes.dex */
    public interface AlternateAuthInterceptor {
        void dump(PrintWriter printWriter);

        boolean hideAlternateAuthBouncer();

        boolean isAnimating();

        boolean isShowingAlternateAuthBouncer();

        void onBouncerVisibilityChanged();

        boolean onTouch(MotionEvent motionEvent);

        void requestUdfps(boolean z, int i);

        void setBouncerExpansionChanged(float f);

        void setQsExpanded(boolean z);

        boolean showAlternateAuthBouncer();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void onCancelClicked() {
    }

    protected boolean shouldDestroyViewOnReset() {
        return false;
    }

    public StatusBarKeyguardViewManager(Context context, ViewMediatorCallback viewMediatorCallback, LockPatternUtils lockPatternUtils, SysuiStatusBarStateController sysuiStatusBarStateController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, DockManager dockManager, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, Optional<FaceAuthScreenBrightnessController> optional, NotificationMediaManager notificationMediaManager, KeyguardBouncer.Factory factory, WakefulnessLifecycle wakefulnessLifecycle, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, KeyguardMessageAreaController.Factory factory2) {
        this.mContext = context;
        this.mViewMediatorCallback = viewMediatorCallback;
        this.mLockPatternUtils = lockPatternUtils;
        this.mConfigurationController = configurationController;
        this.mNavigationModeController = navigationModeController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mKeyguardStateController = keyguardStateController;
        this.mMediaManager = notificationMediaManager;
        this.mKeyguardUpdateManager = keyguardUpdateMonitor;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mDockManager = dockManager;
        this.mFaceAuthScreenBrightnessController = optional;
        this.mKeyguardBouncerFactory = factory;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mKeyguardMessageAreaFactory = factory2;
    }

    public void registerStatusBar(StatusBar statusBar, final ViewGroup viewGroup, NotificationPanelViewController notificationPanelViewController, BiometricUnlockController biometricUnlockController, View view, KeyguardBypassController keyguardBypassController) {
        this.mStatusBar = statusBar;
        this.mContainer = viewGroup;
        this.mBiometricUnlockController = biometricUnlockController;
        this.mBouncer = this.mKeyguardBouncerFactory.create(viewGroup, this.mExpansionCallback);
        this.mNotificationPanelViewController = notificationPanelViewController;
        notificationPanelViewController.addExpansionListener(this);
        this.mBypassController = keyguardBypassController;
        this.mNotificationContainer = view;
        this.mKeyguardMessageAreaController = this.mKeyguardMessageAreaFactory.create(KeyguardMessageArea.findSecurityMessageDisplay(viewGroup));
        this.mFaceAuthScreenBrightnessController.ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatusBarKeyguardViewManager.this.lambda$registerStatusBar$0(viewGroup, (FaceAuthScreenBrightnessController) obj);
            }
        });
        registerListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerStatusBar$0(ViewGroup viewGroup, FaceAuthScreenBrightnessController faceAuthScreenBrightnessController) {
        View view = new View(this.mContext);
        viewGroup.addView(view);
        faceAuthScreenBrightnessController.attach(view);
    }

    public void removeAlternateAuthInterceptor(AlternateAuthInterceptor alternateAuthInterceptor) {
        if (Objects.equals(this.mAlternateAuthInterceptor, alternateAuthInterceptor)) {
            this.mAlternateAuthInterceptor = null;
            resetAlternateAuth(true);
        }
    }

    public void setAlternateAuthInterceptor(AlternateAuthInterceptor alternateAuthInterceptor) {
        if (!Objects.equals(this.mAlternateAuthInterceptor, alternateAuthInterceptor)) {
            this.mAlternateAuthInterceptor = alternateAuthInterceptor;
            resetAlternateAuth(false);
        }
    }

    private void registerListeners() {
        this.mKeyguardUpdateManager.registerCallback(this.mUpdateMonitorCallback);
        this.mStatusBarStateController.addCallback(this);
        this.mConfigurationController.addCallback(this);
        this.mGesturalNav = QuickStepContract.isGesturalMode(this.mNavigationModeController.addListener(this));
        DockManager dockManager = this.mDockManager;
        if (dockManager != null) {
            dockManager.addListener(this.mDockEventListener);
            this.mIsDocked = this.mDockManager.isDocked();
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        hideBouncer(true);
    }

    @Override // com.android.systemui.statusbar.phone.PanelExpansionListener
    public void onPanelExpansionChanged(float f, boolean z) {
        if (this.mNotificationPanelViewController.isUnlockHintRunning()) {
            this.mBouncer.setExpansion(1.0f);
        } else if (bouncerNeedsScrimming()) {
            this.mBouncer.setExpansion(0.0f);
        } else if (this.mShowing) {
            if (!isWakeAndUnlocking() && !this.mStatusBar.isInLaunchTransition()) {
                this.mBouncer.setExpansion(f);
            }
            if (f == 1.0f || !z || this.mKeyguardStateController.canDismissLockScreen() || this.mBouncer.isShowing() || this.mBouncer.isAnimatingAway()) {
                return;
            }
            this.mBouncer.show(false, false);
        } else if (!this.mPulsing || f != 0.0f) {
        } else {
            this.mStatusBar.wakeUpIfDozing(SystemClock.uptimeMillis(), this.mContainer, "BOUNCER_VISIBLE");
        }
    }

    public void setGlobalActionsVisible(boolean z) {
        this.mGlobalActionsVisible = z;
        updateStates();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void show(Bundle bundle) {
        this.mShowing = true;
        this.mNotificationShadeWindowController.setKeyguardShowing(true);
        KeyguardStateController keyguardStateController = this.mKeyguardStateController;
        keyguardStateController.notifyKeyguardState(this.mShowing, keyguardStateController.isOccluded());
        reset(true);
        SysUiStatsLog.write(62, 2);
    }

    protected void showBouncerOrKeyguard(boolean z) {
        if (this.mBouncer.needsFullscreenBouncer() && !this.mDozing) {
            this.mStatusBar.hideKeyguard();
            this.mBouncer.show(true);
        } else {
            this.mStatusBar.showKeyguard();
            if (z) {
                hideBouncer(shouldDestroyViewOnReset());
                this.mBouncer.prepare();
            }
        }
        updateStates();
    }

    public void showGenericBouncer(boolean z) {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor != null) {
            updateAlternateAuthShowing(alternateAuthInterceptor.showAlternateAuthBouncer());
        } else {
            showBouncer(z);
        }
    }

    void hideBouncer(boolean z) {
        if (this.mBouncer == null) {
            return;
        }
        if (this.mShowing) {
            cancelPostAuthActions();
        }
        this.mBouncer.hide(z);
        cancelPendingWakeupAction();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void showBouncer(boolean z) {
        if (this.mShowing && !this.mBouncer.isShowing()) {
            this.mBouncer.show(false, z);
        }
        updateStates();
    }

    public void dismissWithAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z) {
        dismissWithAction(onDismissAction, runnable, z, null);
    }

    public void dismissWithAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z, String str) {
        if (this.mShowing) {
            cancelPendingWakeupAction();
            if (this.mDozing && !isWakeAndUnlocking()) {
                this.mPendingWakeupAction = new DismissWithActionRequest(onDismissAction, runnable, z, str);
                return;
            }
            this.mAfterKeyguardGoneAction = onDismissAction;
            this.mKeyguardGoneCancelAction = runnable;
            this.mDismissActionWillAnimateOnKeyguard = onDismissAction != null && onDismissAction.willRunAnimationOnKeyguard();
            if (this.mAlternateAuthInterceptor != null) {
                if (!z) {
                    this.mBouncer.setDismissAction(this.mAfterKeyguardGoneAction, this.mKeyguardGoneCancelAction);
                    this.mAfterKeyguardGoneAction = null;
                    this.mKeyguardGoneCancelAction = null;
                }
                updateAlternateAuthShowing(this.mAlternateAuthInterceptor.showAlternateAuthBouncer());
                return;
            } else if (z) {
                this.mBouncer.show(false);
            } else {
                this.mBouncer.showWithDismissAction(this.mAfterKeyguardGoneAction, this.mKeyguardGoneCancelAction);
                this.mAfterKeyguardGoneAction = null;
                this.mKeyguardGoneCancelAction = null;
            }
        }
        updateStates();
    }

    private boolean isWakeAndUnlocking() {
        int mode = this.mBiometricUnlockController.getMode();
        return mode == 1 || mode == 2;
    }

    public void addAfterKeyguardGoneRunnable(Runnable runnable) {
        this.mAfterKeyguardGoneRunnables.add(runnable);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void reset(boolean z) {
        if (this.mShowing) {
            this.mNotificationPanelViewController.resetViews(false);
            if (this.mOccluded && !this.mDozing) {
                this.mStatusBar.hideKeyguard();
                if (z || this.mBouncer.needsFullscreenBouncer()) {
                    hideBouncer(false);
                }
            } else {
                showBouncerOrKeyguard(z);
            }
            resetAlternateAuth(false);
            this.mKeyguardUpdateManager.sendKeyguardReset();
            updateStates();
        }
    }

    public void resetAlternateAuth(boolean z) {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        updateAlternateAuthShowing((alternateAuthInterceptor != null && alternateAuthInterceptor.hideAlternateAuthBouncer()) || z);
    }

    private void updateAlternateAuthShowing(boolean z) {
        KeyguardMessageAreaController keyguardMessageAreaController = this.mKeyguardMessageAreaController;
        if (keyguardMessageAreaController != null) {
            keyguardMessageAreaController.setAltBouncerShowing(isShowingAlternateAuth());
        }
        this.mBypassController.setAltBouncerShowing(isShowingAlternateAuth());
        if (z) {
            this.mStatusBar.updateScrimController();
        }
    }

    @Override // com.android.keyguard.KeyguardViewController, com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedWakingUp() {
        this.mStatusBar.getNotificationShadeWindowView().getWindowInsetsController().setAnimationsDisabled(false);
        NavigationBarView navigationBarView = this.mStatusBar.getNavigationBarView();
        if (navigationBarView != null) {
            navigationBarView.forEachView(StatusBarKeyguardViewManager$$ExternalSyntheticLambda6.INSTANCE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onStartedWakingUp$1(View view) {
        view.animate().alpha(1.0f).setDuration(125L).start();
    }

    @Override // com.android.keyguard.KeyguardViewController, com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedGoingToSleep() {
        this.mStatusBar.getNotificationShadeWindowView().getWindowInsetsController().setAnimationsDisabled(true);
        NavigationBarView navigationBarView = this.mStatusBar.getNavigationBarView();
        if (navigationBarView != null) {
            navigationBarView.forEachView(StatusBarKeyguardViewManager$$ExternalSyntheticLambda5.INSTANCE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onStartedGoingToSleep$2(View view) {
        view.animate().alpha(0.0f).setDuration(125L).start();
    }

    @Override // com.android.keyguard.KeyguardViewController, com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onFinishedGoingToSleep() {
        this.mBouncer.onScreenTurnedOff();
    }

    @Override // com.android.systemui.statusbar.RemoteInputController.Callback
    public void onRemoteInputActive(boolean z) {
        this.mRemoteInputActive = z;
        updateStates();
    }

    private void setDozing(boolean z) {
        if (this.mDozing != z) {
            this.mDozing = z;
            if (z || this.mBouncer.needsFullscreenBouncer() || this.mOccluded) {
                reset(z);
            }
            updateStates();
            if (z) {
                return;
            }
            launchPendingWakeupAction();
        }
    }

    public void setPulsing(boolean z) {
        if (this.mPulsing != z) {
            this.mPulsing = z;
            updateStates();
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void setNeedsInput(boolean z) {
        this.mNotificationShadeWindowController.setKeyguardNeedsInput(z);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public boolean isUnlockWithWallpaper() {
        return this.mNotificationShadeWindowController.isShowingWallpaper();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void setOccluded(boolean z, boolean z2) {
        this.mStatusBar.setOccluded(z);
        boolean z3 = true;
        if (z && !this.mOccluded && this.mShowing) {
            SysUiStatsLog.write(62, 3);
            if (this.mStatusBar.isInLaunchTransition()) {
                this.mOccluded = true;
                this.mStatusBar.fadeKeyguardAfterLaunchTransition(null, new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.4
                    @Override // java.lang.Runnable
                    public void run() {
                        StatusBarKeyguardViewManager.this.mNotificationShadeWindowController.setKeyguardOccluded(StatusBarKeyguardViewManager.this.mOccluded);
                        StatusBarKeyguardViewManager.this.reset(true);
                    }
                });
                return;
            }
        } else if (!z && this.mOccluded && this.mShowing) {
            SysUiStatsLog.write(62, 2);
        }
        boolean z4 = !this.mOccluded && z;
        this.mOccluded = z;
        if (this.mShowing) {
            NotificationMediaManager notificationMediaManager = this.mMediaManager;
            if (!z2 || z) {
                z3 = false;
            }
            notificationMediaManager.updateMediaMetaData(false, z3);
        }
        this.mNotificationShadeWindowController.setKeyguardOccluded(z);
        if (!this.mDozing) {
            reset(z4);
        }
        if (!z2 || z || !this.mShowing || this.mBouncer.isShowing()) {
            return;
        }
        this.mStatusBar.animateKeyguardUnoccluding();
    }

    public boolean isOccluded() {
        return this.mOccluded;
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void startPreHideAnimation(Runnable runnable) {
        if (this.mBouncer.isShowing()) {
            this.mBouncer.startPreHideAnimation(runnable);
            this.mStatusBar.onBouncerPreHideAnimation();
            if (this.mDismissActionWillAnimateOnKeyguard) {
                updateStates();
            }
        } else if (runnable != null) {
            runnable.run();
        }
        this.mNotificationPanelViewController.blockExpansionForCurrentTouch();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void blockPanelExpansionFromCurrentTouch() {
        this.mNotificationPanelViewController.blockExpansionForCurrentTouch();
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0092  */
    @Override // com.android.keyguard.KeyguardViewController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void hide(long j, long j2) {
        int i;
        long j3;
        this.mShowing = false;
        KeyguardStateController keyguardStateController = this.mKeyguardStateController;
        keyguardStateController.notifyKeyguardState(false, keyguardStateController.isOccluded());
        launchPendingWakeupAction();
        long j4 = this.mKeyguardUpdateManager.needsSlowUnlockTransition() ? 2000L : j2;
        long max = Math.max(0L, (j - 48) - SystemClock.uptimeMillis());
        if (this.mStatusBar.isInLaunchTransition() || this.mKeyguardStateController.isFlingingToDismissKeyguard()) {
            i = 1;
            final boolean isFlingingToDismissKeyguard = this.mKeyguardStateController.isFlingingToDismissKeyguard();
            this.mStatusBar.fadeKeyguardAfterLaunchTransition(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.5
                @Override // java.lang.Runnable
                public void run() {
                    StatusBarKeyguardViewManager.this.mNotificationShadeWindowController.setKeyguardShowing(false);
                    StatusBarKeyguardViewManager.this.mNotificationShadeWindowController.setKeyguardFadingAway(true);
                    StatusBarKeyguardViewManager.this.hideBouncer(true);
                    StatusBarKeyguardViewManager.this.updateStates();
                }
            }, new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.6
                @Override // java.lang.Runnable
                public void run() {
                    StatusBarKeyguardViewManager.this.mStatusBar.hideKeyguard();
                    StatusBarKeyguardViewManager.this.mNotificationShadeWindowController.setKeyguardFadingAway(false);
                    if (isFlingingToDismissKeyguard) {
                        StatusBarKeyguardViewManager.this.mStatusBar.finishKeyguardFadingAway();
                    }
                    StatusBarKeyguardViewManager.this.mViewMediatorCallback.keyguardGone();
                    StatusBarKeyguardViewManager.this.executeAfterKeyguardGoneAction();
                }
            });
        } else {
            executeAfterKeyguardGoneAction();
            boolean z = this.mBiometricUnlockController.getMode() == 2;
            boolean needsBypassFading = needsBypassFading();
            if (needsBypassFading) {
                j4 = 67;
            } else if (z) {
                j4 = 240;
            } else {
                j3 = max;
                i = 1;
                this.mStatusBar.setKeyguardFadingAway(j, j3, j4, needsBypassFading);
                this.mBiometricUnlockController.startKeyguardFadingAway();
                hideBouncer(true);
                if (!z) {
                    if (needsBypassFading) {
                        ViewGroupFadeHelper.fadeOutAllChildrenExcept(this.mNotificationPanelViewController.getView(), this.mNotificationContainer, j4, new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                StatusBarKeyguardViewManager.this.lambda$hide$3();
                            }
                        });
                    } else {
                        this.mStatusBar.fadeKeyguardWhilePulsing();
                    }
                    wakeAndUnlockDejank();
                } else if (!this.mStatusBarStateController.leaveOpenOnKeyguardHide()) {
                    this.mNotificationShadeWindowController.setKeyguardFadingAway(true);
                    if (needsBypassFading) {
                        ViewGroupFadeHelper.fadeOutAllChildrenExcept(this.mNotificationPanelViewController.getView(), this.mNotificationContainer, j4, new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                StatusBarKeyguardViewManager.this.lambda$hide$4();
                            }
                        });
                    } else {
                        this.mStatusBar.hideKeyguard();
                    }
                    this.mStatusBar.updateScrimController();
                    wakeAndUnlockDejank();
                } else {
                    this.mStatusBar.hideKeyguard();
                    this.mStatusBar.finishKeyguardFadingAway();
                    this.mBiometricUnlockController.finishKeyguardFadingAway();
                }
                updateStates();
                this.mNotificationShadeWindowController.setKeyguardShowing(false);
                this.mViewMediatorCallback.keyguardGone();
            }
            j3 = 0;
            i = 1;
            this.mStatusBar.setKeyguardFadingAway(j, j3, j4, needsBypassFading);
            this.mBiometricUnlockController.startKeyguardFadingAway();
            hideBouncer(true);
            if (!z) {
            }
            updateStates();
            this.mNotificationShadeWindowController.setKeyguardShowing(false);
            this.mViewMediatorCallback.keyguardGone();
        }
        SysUiStatsLog.write(62, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hide$3() {
        this.mStatusBar.hideKeyguard();
        onKeyguardFadedAway();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hide$4() {
        this.mStatusBar.hideKeyguard();
    }

    private boolean needsBypassFading() {
        return (this.mBiometricUnlockController.getMode() == 7 || this.mBiometricUnlockController.getMode() == 2 || this.mBiometricUnlockController.getMode() == 1) && this.mBypassController.getBypassEnabled();
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public void onNavigationModeChanged(int i) {
        boolean isGesturalMode = QuickStepContract.isGesturalMode(i);
        if (isGesturalMode != this.mGesturalNav) {
            this.mGesturalNav = isGesturalMode;
            updateStates();
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onThemeChanged() {
        boolean isShowing = this.mBouncer.isShowing();
        boolean isScrimmed = this.mBouncer.isScrimmed();
        hideBouncer(true);
        this.mBouncer.prepare();
        if (isShowing) {
            showBouncer(isScrimmed);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onKeyguardFadedAway$5() {
        this.mNotificationShadeWindowController.setKeyguardFadingAway(false);
    }

    public void onKeyguardFadedAway() {
        this.mContainer.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarKeyguardViewManager.this.lambda$onKeyguardFadedAway$5();
            }
        }, 100L);
        ViewGroupFadeHelper.reset(this.mNotificationPanelViewController.getView());
        this.mStatusBar.finishKeyguardFadingAway();
        this.mBiometricUnlockController.finishKeyguardFadingAway();
        WindowManagerGlobal.getInstance().trimMemory(20);
    }

    private void wakeAndUnlockDejank() {
        if (this.mBiometricUnlockController.getMode() != 1 || !LatencyTracker.isEnabled(this.mContext)) {
            return;
        }
        final BiometricSourceType biometricType = this.mBiometricUnlockController.getBiometricType();
        DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarKeyguardViewManager.this.lambda$wakeAndUnlockDejank$6(biometricType);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$wakeAndUnlockDejank$6(BiometricSourceType biometricSourceType) {
        LatencyTracker.getInstance(this.mContext).onActionEnd(biometricSourceType == BiometricSourceType.FACE ? 7 : 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeAfterKeyguardGoneAction() {
        ActivityStarter.OnDismissAction onDismissAction = this.mAfterKeyguardGoneAction;
        if (onDismissAction != null) {
            onDismissAction.onDismiss();
            this.mAfterKeyguardGoneAction = null;
        }
        this.mKeyguardGoneCancelAction = null;
        this.mDismissActionWillAnimateOnKeyguard = false;
        for (int i = 0; i < this.mAfterKeyguardGoneRunnables.size(); i++) {
            this.mAfterKeyguardGoneRunnables.get(i).run();
        }
        this.mAfterKeyguardGoneRunnables.clear();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void dismissAndCollapse() {
        this.mStatusBar.executeRunnableDismissingKeyguard(null, null, true, false, true);
    }

    public boolean isSecure() {
        return this.mBouncer.isSecure();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public boolean isShowing() {
        return this.mShowing;
    }

    public boolean onBackPressed(boolean z) {
        if (this.mBouncer.isShowing()) {
            this.mStatusBar.endAffordanceLaunch();
            if (this.mBouncer.isScrimmed() && !this.mBouncer.needsFullscreenBouncer()) {
                hideBouncer(false);
                updateStates();
                return true;
            }
            reset(z);
            return true;
        }
        return false;
    }

    @Override // com.android.keyguard.KeyguardViewController
    public boolean isBouncerShowing() {
        return this.mBouncer.isShowing() || isShowingAlternateAuth();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public boolean bouncerIsOrWillBeShowing() {
        return isBouncerShowing() || this.mBouncer.getShowingSoon();
    }

    public void cancelPostAuthActions() {
        if (bouncerIsOrWillBeShowing()) {
            return;
        }
        this.mAfterKeyguardGoneAction = null;
        this.mDismissActionWillAnimateOnKeyguard = false;
        Runnable runnable = this.mKeyguardGoneCancelAction;
        if (runnable == null) {
            return;
        }
        runnable.run();
        this.mKeyguardGoneCancelAction = null;
    }

    private long getNavBarShowDelay() {
        if (this.mKeyguardStateController.isKeyguardFadingAway()) {
            return this.mKeyguardStateController.getKeyguardFadingAwayDelay();
        }
        return this.mBouncer.isShowing() ? 320L : 0L;
    }

    protected void updateStates() {
        ViewGroup viewGroup = this.mContainer;
        if (viewGroup == null) {
            return;
        }
        int systemUiVisibility = viewGroup.getSystemUiVisibility();
        boolean z = this.mShowing;
        boolean z2 = this.mOccluded;
        boolean isShowing = this.mBouncer.isShowing();
        boolean bouncerIsOrWillBeShowing = bouncerIsOrWillBeShowing();
        boolean z3 = true;
        boolean z4 = !this.mBouncer.isFullscreenBouncer();
        boolean z5 = this.mRemoteInputActive;
        if ((z4 || !z || z5) != (this.mLastBouncerDismissible || !this.mLastShowing || this.mLastRemoteInputActive) || this.mFirstUpdate) {
            if (z4 || !z || z5) {
                this.mContainer.setSystemUiVisibility(systemUiVisibility & (-4194305));
            } else {
                this.mContainer.setSystemUiVisibility(systemUiVisibility | 4194304);
            }
        }
        boolean isNavBarVisible = isNavBarVisible();
        if (isNavBarVisible != getLastNavBarVisible() || this.mFirstUpdate) {
            updateNavigationBarVisibility(isNavBarVisible);
        }
        if (isShowing != this.mLastBouncerShowing || this.mFirstUpdate) {
            this.mNotificationShadeWindowController.setBouncerShowing(isShowing);
            this.mStatusBar.setBouncerShowing(isShowing);
        }
        if ((z && !z2) != (this.mLastShowing && !this.mLastOccluded) || this.mFirstUpdate) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateManager;
            if (!z || z2) {
                z3 = false;
            }
            keyguardUpdateMonitor.onKeyguardVisibilityChanged(z3);
        }
        if (bouncerIsOrWillBeShowing != this.mLastBouncerIsOrWillBeShowing || this.mFirstUpdate) {
            this.mKeyguardUpdateManager.sendKeyguardBouncerChanged(bouncerIsOrWillBeShowing);
        }
        this.mFirstUpdate = false;
        this.mLastShowing = z;
        this.mLastGlobalActionsVisible = this.mGlobalActionsVisible;
        this.mLastOccluded = z2;
        this.mLastBouncerShowing = isShowing;
        this.mLastBouncerIsOrWillBeShowing = bouncerIsOrWillBeShowing;
        this.mLastBouncerDismissible = z4;
        this.mLastRemoteInputActive = z5;
        this.mLastDozing = this.mDozing;
        this.mLastPulsing = this.mPulsing;
        this.mLastBiometricMode = this.mBiometricUnlockController.getMode();
        this.mLastGesturalNav = this.mGesturalNav;
        this.mLastIsDocked = this.mIsDocked;
        this.mStatusBar.onKeyguardViewManagerStatesUpdated();
    }

    protected void updateNavigationBarVisibility(boolean z) {
        if (this.mStatusBar.getNavigationBarView() != null) {
            if (z) {
                long navBarShowDelay = getNavBarShowDelay();
                if (navBarShowDelay == 0) {
                    this.mMakeNavigationBarVisibleRunnable.run();
                    return;
                } else {
                    this.mContainer.postOnAnimationDelayed(this.mMakeNavigationBarVisibleRunnable, navBarShowDelay);
                    return;
                }
            }
            this.mContainer.removeCallbacks(this.mMakeNavigationBarVisibleRunnable);
            this.mStatusBar.getNotificationShadeWindowView().getWindowInsetsController().hide(WindowInsets.Type.navigationBars());
        }
    }

    protected boolean isNavBarVisible() {
        int mode = this.mBiometricUnlockController.getMode();
        boolean z = this.mShowing && !this.mOccluded;
        boolean z2 = this.mDozing;
        return (!z && !(z2 && mode != 2)) || this.mBouncer.isShowing() || this.mRemoteInputActive || (((z && !z2) || (this.mPulsing && !this.mIsDocked)) && this.mGesturalNav) || this.mGlobalActionsVisible;
    }

    protected boolean getLastNavBarVisible() {
        boolean z = this.mLastShowing && !this.mLastOccluded;
        boolean z2 = this.mLastDozing;
        return (!z && !(z2 && this.mLastBiometricMode != 2)) || this.mLastBouncerShowing || this.mLastRemoteInputActive || (((z && !z2) || (this.mLastPulsing && !this.mLastIsDocked)) && this.mLastGesturalNav) || this.mLastGlobalActionsVisible;
    }

    public boolean shouldDismissOnMenuPressed() {
        return this.mBouncer.shouldDismissOnMenuPressed();
    }

    public boolean interceptMediaKey(KeyEvent keyEvent) {
        return this.mBouncer.interceptMediaKey(keyEvent);
    }

    public boolean dispatchBackKeyEventPreIme() {
        return this.mBouncer.dispatchBackKeyEventPreIme();
    }

    public void readyForKeyguardDone() {
        this.mViewMediatorCallback.readyForKeyguardDone();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public boolean shouldDisableWindowAnimationsForUnlock() {
        return this.mStatusBar.isInLaunchTransition();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public boolean shouldSubtleWindowAnimationsForUnlock() {
        return needsBypassFading();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public boolean isGoingToNotificationShade() {
        return this.mStatusBarStateController.leaveOpenOnKeyguardHide();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void keyguardGoingAway() {
        this.mStatusBar.keyguardGoingAway();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void setKeyguardGoingAwayState(boolean z) {
        this.mNotificationShadeWindowController.setKeyguardGoingAway(z);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void notifyKeyguardAuthenticated(boolean z) {
        this.mBouncer.notifyKeyguardAuthenticated(z);
        if (this.mAlternateAuthInterceptor == null || !isShowingAlternateAuthOrAnimating()) {
            return;
        }
        resetAlternateAuth(false);
        executeAfterKeyguardGoneAction();
    }

    public void showBouncerMessage(String str, ColorStateList colorStateList) {
        if (isShowingAlternateAuth()) {
            KeyguardMessageAreaController keyguardMessageAreaController = this.mKeyguardMessageAreaController;
            if (keyguardMessageAreaController == null) {
                return;
            }
            keyguardMessageAreaController.setNextMessageColor(colorStateList);
            this.mKeyguardMessageAreaController.setMessage(str);
            return;
        }
        this.mBouncer.showMessage(str, colorStateList);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public ViewRootImpl getViewRootImpl() {
        return this.mStatusBar.getStatusBarView().getViewRootImpl();
    }

    public void launchPendingWakeupAction() {
        DismissWithActionRequest dismissWithActionRequest = this.mPendingWakeupAction;
        this.mPendingWakeupAction = null;
        if (dismissWithActionRequest != null) {
            if (this.mShowing) {
                dismissWithAction(dismissWithActionRequest.dismissAction, dismissWithActionRequest.cancelAction, dismissWithActionRequest.afterKeyguardGone, dismissWithActionRequest.message);
                return;
            }
            ActivityStarter.OnDismissAction onDismissAction = dismissWithActionRequest.dismissAction;
            if (onDismissAction == null) {
                return;
            }
            onDismissAction.onDismiss();
        }
    }

    public void cancelPendingWakeupAction() {
        Runnable runnable;
        DismissWithActionRequest dismissWithActionRequest = this.mPendingWakeupAction;
        this.mPendingWakeupAction = null;
        if (dismissWithActionRequest == null || (runnable = dismissWithActionRequest.cancelAction) == null) {
            return;
        }
        runnable.run();
    }

    public boolean bouncerNeedsScrimming() {
        return this.mOccluded || this.mBouncer.willDismissWithAction() || this.mStatusBar.isFullScreenUserSwitcherState() || (this.mBouncer.isShowing() && this.mBouncer.isScrimmed()) || this.mBouncer.isFullscreenBouncer();
    }

    public void updateResources() {
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        if (keyguardBouncer != null) {
            keyguardBouncer.updateResources();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("StatusBarKeyguardViewManager:");
        printWriter.println("  mShowing: " + this.mShowing);
        printWriter.println("  mOccluded: " + this.mOccluded);
        printWriter.println("  mRemoteInputActive: " + this.mRemoteInputActive);
        printWriter.println("  mDozing: " + this.mDozing);
        printWriter.println("  mAfterKeyguardGoneAction: " + this.mAfterKeyguardGoneAction);
        printWriter.println("  mAfterKeyguardGoneRunnables: " + this.mAfterKeyguardGoneRunnables);
        printWriter.println("  mPendingWakeupAction: " + this.mPendingWakeupAction);
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        if (keyguardBouncer != null) {
            keyguardBouncer.dump(printWriter);
        }
        if (this.mAlternateAuthInterceptor != null) {
            printWriter.println("AltAuthInterceptor: ");
            this.mAlternateAuthInterceptor.dump(printWriter);
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        setDozing(z);
    }

    public boolean isQsExpanded() {
        return this.mQsExpanded;
    }

    public void setQsExpanded(boolean z) {
        this.mQsExpanded = z;
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor != null) {
            alternateAuthInterceptor.setQsExpanded(z);
        }
    }

    public boolean isShowingAlternateAuth() {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        return alternateAuthInterceptor != null && alternateAuthInterceptor.isShowingAlternateAuthBouncer();
    }

    public boolean isShowingAlternateAuthOrAnimating() {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        return alternateAuthInterceptor != null && (alternateAuthInterceptor.isShowingAlternateAuthBouncer() || this.mAlternateAuthInterceptor.isAnimating());
    }

    public boolean onTouch(MotionEvent motionEvent) {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor == null) {
            return false;
        }
        return alternateAuthInterceptor.onTouch(motionEvent);
    }

    public void updateKeyguardPosition(float f) {
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        if (keyguardBouncer != null) {
            keyguardBouncer.updateKeyguardPosition(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DismissWithActionRequest {
        final boolean afterKeyguardGone;
        final Runnable cancelAction;
        final ActivityStarter.OnDismissAction dismissAction;
        final String message;

        DismissWithActionRequest(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z, String str) {
            this.dismissAction = onDismissAction;
            this.cancelAction = runnable;
            this.afterKeyguardGone = z;
            this.message = str;
        }
    }

    public void requestFace(boolean z) {
        this.mKeyguardUpdateManager.requestFaceAuthOnOccludingApp(z);
    }

    public void requestFp(boolean z, int i) {
        this.mKeyguardUpdateManager.requestFingerprintAuthOnOccludingApp(z);
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor != null) {
            alternateAuthInterceptor.requestUdfps(z, i);
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public void notifyKeyguardAuthenticatedForFaceRecognition(boolean z) {
        this.mBouncer.notifyKeyguardAuthenticatedForFaceRecognition(z);
    }
}
