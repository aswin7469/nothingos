package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Insets;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.util.MathUtils;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.EmergencyButton;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.LockIconViewController;
import com.android.keyguard.dagger.KeyguardQsUserSwitchComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardUserSwitcherComponent;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSDetailDisplayer;
import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.ViewGroupFadeHelper;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.MediaHeaderView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.KeyguardAffordanceHelper;
import com.android.systemui.statusbar.phone.KeyguardClockPositionAlgorithm;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PanelViewController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.util.Utils;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.wm.shell.animation.FlingAnimationUtils;
import com.nothingos.headsup.NothingOSHeadsupManager;
import com.nothingos.systemui.facerecognition.FaceRecognitionController;
import com.nothingos.utils.SystemUIUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.inject.Provider;
/* loaded from: classes.dex */
public class NotificationPanelViewController extends PanelViewController {
    private final AccessibilityManager mAccessibilityManager;
    private final ActivityManager mActivityManager;
    private boolean mAffordanceHasPreview;
    private KeyguardAffordanceHelper mAffordanceHelper;
    private boolean mAllowExpandForSmallExpansion;
    private int mAmbientIndicationBottomPadding;
    private boolean mAnimateNextNotificationBounds;
    private boolean mAnimateNextPositionUpdate;
    private boolean mAnimatingQS;
    private final AuthController mAuthController;
    private int mBarState;
    private ViewGroup mBigClockContainer;
    private final BiometricUnlockController mBiometricUnlockController;
    private boolean mBlockTouches;
    private boolean mBlockingExpansionForCurrentTouch;
    private float mBottomAreaShadeAlpha;
    private final ValueAnimator mBottomAreaShadeAlphaAnimator;
    private boolean mCanQsCollapseForLand;
    private boolean mClosingWithAlphaFadeOut;
    private boolean mCollapsedOnDown;
    private final CommandQueue mCommandQueue;
    private final ConfigurationController mConfigurationController;
    private boolean mConflictingQsExpansionGesture;
    private final ContentResolver mContentResolver;
    private final ControlsComponent mControlsComponent;
    private final ConversationNotificationManager mConversationNotificationManager;
    private int mDarkIconSize;
    private boolean mDelayShowingKeyguardStatusBar;
    private NotificationShadeDepthController mDepthController;
    private int mDisplayId;
    private int mDistanceForQSFullShadeTransition;
    private float mDownX;
    private float mDownY;
    private final DozeParameters mDozeParameters;
    private boolean mDozing;
    private boolean mDozingOnDown;
    private EmergencyButtonController mEmergencyButtonController;
    private final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
    private final NotificationEntryManager mEntryManager;
    private Runnable mExpandAfterLayoutRunnable;
    private boolean mExpandingFromHeadsUp;
    private boolean mExpectingSynthesizedDown;
    private final FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    private final FeatureFlags mFeatureFlags;
    private boolean mFirstBypassAttempt;
    private FlingAnimationUtils mFlingAnimationUtils;
    private final Provider<FlingAnimationUtils.Builder> mFlingAnimationUtilsBuilder;
    private final FragmentService mFragmentService;
    private NotificationGroupManagerLegacy mGroupManager;
    private boolean mHasOrientationChanged;
    private boolean mHasQSLayout;
    private boolean mHeadsUpAnimatingAway;
    private HeadsUpAppearanceController mHeadsUpAppearanceController;
    private int mHeadsUpInset;
    private boolean mHeadsUpPinnedMode;
    private HeadsUpTouchHelper mHeadsUpTouchHelper;
    private int mIndicationBottomPadding;
    private float mInitialHeightOnTouch;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private float mInterpolatedDarkAmount;
    private boolean mIsExpanding;
    private boolean mIsFullWidth;
    private boolean mIsGestureNavigation;
    private boolean mIsLaunchTransitionFinished;
    private boolean mIsLaunchTransitionRunning;
    private boolean mIsPanelCollapseOnQQS;
    private boolean mIsPulseExpansionResetAnimator;
    private boolean mIsQsTranslationResetAnimator;
    private KeyguardStatusBarViewController mKeyguarStatusBarViewController;
    private final KeyguardBypassController mKeyguardBypassController;
    private KeyguardIndicationController mKeyguardIndicationController;
    private KeyguardMediaController mKeyguardMediaController;
    private final KeyguardQsUserSwitchComponent.Factory mKeyguardQsUserSwitchComponentFactory;
    private KeyguardQsUserSwitchController mKeyguardQsUserSwitchController;
    private boolean mKeyguardQsUserSwitchEnabled;
    private boolean mKeyguardShowing;
    private KeyguardStatusBarView mKeyguardStatusBar;
    private final KeyguardStatusBarViewComponent.Factory mKeyguardStatusBarViewComponentFactory;
    private final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
    private KeyguardStatusViewController mKeyguardStatusViewController;
    private final KeyguardUserSwitcherComponent.Factory mKeyguardUserSwitcherComponentFactory;
    private KeyguardUserSwitcherController mKeyguardUserSwitcherController;
    private boolean mKeyguardUserSwitcherEnabled;
    private boolean mLastEventSynthesizedDown;
    private float mLastOverscroll;
    private Runnable mLaunchAnimationEndRunnable;
    private boolean mLaunchingAffordance;
    private final LayoutInflater mLayoutInflater;
    private float mLinearDarkAmount;
    private boolean mListenForHeadsUp;
    private LockIconViewController mLockIconViewController;
    private int mLockscreenNotificationQSPadding;
    private final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    private final NotificationLockscreenUserManager mLockscreenUserManager;
    private int mMaxAllowedKeyguardNotifications;
    private final int mMaxKeyguardNotifications;
    private int mMaxOverscrollAmountForPulse;
    private final MediaDataManager mMediaDataManager;
    private final MediaHierarchyManager mMediaHierarchyManager;
    private final MetricsLogger mMetricsLogger;
    private float mMinFraction;
    private int mNavigationBarBottomHeight;
    private long mNotificationBoundsAnimationDelay;
    private long mNotificationBoundsAnimationDuration;
    private NotificationsQuickSettingsContainer mNotificationContainerParent;
    private final NotificationIconAreaController mNotificationIconAreaController;
    private NotificationShelfController mNotificationShelfController;
    private final NotificationStackScrollLayoutController mNotificationStackScrollLayoutController;
    private int mNotificationsHeaderCollideDistance;
    private int mOldLayoutDirection;
    private boolean mOnlyAffordanceInThisMotion;
    private float mOverStretchAmount;
    private int mPanelAlpha;
    private final AnimatableProperty mPanelAlphaAnimator;
    private Runnable mPanelAlphaEndAction;
    private final AnimationProperties mPanelAlphaInPropertiesAnimator;
    private final AnimationProperties mPanelAlphaOutPropertiesAnimator;
    private boolean mPanelExpanded;
    private int mPortState;
    private int mPositionMinSideMargin;
    private final PowerManager mPowerManager;
    private ViewGroup mPreviewContainer;
    private final PrivacyDotViewController mPrivacyDotViewController;
    private final PulseExpansionHandler mPulseExpansionHandler;
    private boolean mPulsing;
    private boolean mQSAnimatingHiddenFromCollapsed;
    private final QSDetailDisplayer mQSDetailDisplayer;
    @VisibleForTesting
    QS mQs;
    private boolean mQsAnimatorExpand;
    private int mQsClipBottom;
    private int mQsClipTop;
    private boolean mQsExpandImmediate;
    private boolean mQsExpanded;
    private boolean mQsExpandedWhenExpandingStarted;
    private ValueAnimator mQsExpansionAnimator;
    private boolean mQsExpansionFromOverscroll;
    private float mQsExpansionHeight;
    private int mQsFalsingThreshold;
    private FrameLayout mQsFrame;
    private boolean mQsFullyExpanded;
    private int mQsMaxExpansionHeight;
    private int mQsMinExpansionHeight;
    private int mQsPeekHeight;
    private ValueAnimator mQsSizeChangeAnimator;
    private RelativeLayout mQsStatusBarLayoutLand;
    private boolean mQsTouchAboveFalsingThreshold;
    private boolean mQsTracking;
    private float mQsTranslationForFullShadeTransition;
    private VelocityTracker mQsVelocityTracker;
    private boolean mQsVisible;
    private final QuickAccessWalletController mQuickAccessWalletController;
    private float mQuickQsOffsetHeight;
    private final RecordingController mRecordingController;
    private final NotificationRemoteInputManager mRemoteInputManager;
    private int mScreenCornerRadius;
    private final ScrimController mScrimController;
    private int mScrimCornerRadius;
    private final SecureSettings mSecureSettings;
    private final SettingsChangeObserver mSettingsChangeObserver;
    private int mShelfHeight;
    private boolean mShouldUseSplitNotificationShade;
    private boolean mShowIconsWhenExpanded;
    private boolean mShowingKeyguardHeadsUp;
    private int mSplitShadeNotificationsTopPadding;
    private int mStackScrollerMeasuringPass;
    private boolean mStackScrollerOverscrolling;
    private int mStatusBarHeaderHeightKeyguard;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private int mStatusBarMinHeight;
    private final TapAgainViewController mTapAgainViewController;
    private int mThemeResId;
    private int mTrackingPointer;
    private int mTransitionToFullShadeQSPosition;
    private float mTransitioningToFullShadeProgress;
    private boolean mTwoFingerQsExpandPossible;
    private float mUdfpsMaxYBurnInOffset;
    private final Executor mUiExecutor;
    private UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private final UserManager mUserManager;
    private boolean mUserSetupComplete;
    private Runnable mVerticalTranslationListener;
    private final VibratorHelper mVibratorHelper;
    private final NotificationPanelView mView;
    private final NotificationWakeUpCoordinator mWakeUpCoordinator;
    private static final Rect M_DUMMY_DIRTY_RECT = new Rect(0, 0, 1, 1);
    private static final Rect EMPTY_RECT = new Rect();
    private static final AnimationProperties KEYGUARD_HUN_PROPERTIES = new AnimationProperties().setDuration(360);
    private final OnHeightChangedListener mOnHeightChangedListener = new OnHeightChangedListener();
    private final OnClickListener mOnClickListener = new OnClickListener();
    private final OnOverscrollTopChangedListener mOnOverscrollTopChangedListener = new OnOverscrollTopChangedListener();
    private final KeyguardAffordanceHelperCallback mKeyguardAffordanceHelperCallback = new KeyguardAffordanceHelperCallback();
    private final OnEmptySpaceClickListener mOnEmptySpaceClickListener = new OnEmptySpaceClickListener();
    private final MyOnHeadsUpChangedListener mOnHeadsUpChangedListener = new MyOnHeadsUpChangedListener();
    private final HeightListener mHeightListener = new HeightListener();
    private final ConfigurationListener mConfigurationListener = new ConfigurationListener();
    @VisibleForTesting
    final StatusBarStateListener mStatusBarStateListener = new StatusBarStateListener();
    private final AnimatableProperty KEYGUARD_HEADS_UP_SHOWING_AMOUNT = AnimatableProperty.from("KEYGUARD_HEADS_UP_SHOWING_AMOUNT", new BiConsumer() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda8
        @Override // java.util.function.BiConsumer
        public final void accept(Object obj, Object obj2) {
            NotificationPanelViewController.this.lambda$new$0((NotificationPanelView) obj, (Float) obj2);
        }
    }, new Function() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda14
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            Float lambda$new$1;
            lambda$new$1 = NotificationPanelViewController.this.lambda$new$1((NotificationPanelView) obj);
            return lambda$new$1;
        }
    }, R$id.keyguard_hun_animator_tag, R$id.keyguard_hun_animator_end_tag, R$id.keyguard_hun_animator_start_tag);
    @VisibleForTesting
    final KeyguardUpdateMonitorCallback mKeyguardUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            if (!NotificationPanelViewController.this.mFirstBypassAttempt || !NotificationPanelViewController.this.mUpdateMonitor.isUnlockingWithBiometricAllowed(z)) {
                return;
            }
            NotificationPanelViewController.this.mDelayShowingKeyguardStatusBar = true;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            boolean z2 = true;
            if (NotificationPanelViewController.this.mBarState != 1 && NotificationPanelViewController.this.mBarState != 2) {
                z2 = false;
            }
            if (z || !NotificationPanelViewController.this.mFirstBypassAttempt || !z2 || NotificationPanelViewController.this.mDozing || NotificationPanelViewController.this.mDelayShowingKeyguardStatusBar || NotificationPanelViewController.this.mBiometricUnlockController.isBiometricUnlock()) {
                return;
            }
            NotificationPanelViewController.this.mFirstBypassAttempt = false;
            NotificationPanelViewController.this.animateKeyguardStatusBarIn(360L);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onFinishedGoingToSleep(int i) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mFirstBypassAttempt = notificationPanelViewController.mKeyguardBypassController.getBypassEnabled();
            NotificationPanelViewController.this.mDelayShowingKeyguardStatusBar = false;
        }
    };
    private boolean mQsExpansionEnabledPolicy = true;
    private boolean mQsExpansionEnabledAmbient = true;
    private int mDisplayTopInset = 0;
    private int mDisplayRightInset = 0;
    private final KeyguardClockPositionAlgorithm mClockPositionAlgorithm = new KeyguardClockPositionAlgorithm();
    private final KeyguardClockPositionAlgorithm.Result mClockPositionResult = new KeyguardClockPositionAlgorithm.Result();
    private boolean mQsScrimEnabled = true;
    private float mKeyguardStatusBarAnimateAlpha = 1.0f;
    private int mLastOrientation = -1;
    private String mLastCameraLaunchSource = "lockscreen_affordance";
    private Runnable mHeadsUpExistenceChangedRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            NotificationPanelViewController.this.lambda$new$2();
        }
    };
    private LockscreenGestureLogger mLockscreenGestureLogger = new LockscreenGestureLogger();
    private boolean mHideIconsDuringLaunchAnimation = true;
    private ArrayList<Consumer<ExpandableNotificationRow>> mTrackingHeadsUpListeners = new ArrayList<>();
    private float mKeyguardHeadsUpShowingAmount = 0.0f;
    private final Rect mQsClippingAnimationEndBounds = new Rect();
    private ValueAnimator mQsClippingAnimation = null;
    private final Rect mKeyguardStatusAreaClipBounds = new Rect();
    private final Region mQsInterceptRegion = new Region();
    private float mKeyguardOnlyContentAlpha = 1.0f;
    private View.AccessibilityDelegate mAccessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.2
        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i == AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD.getId() || i == AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP.getId()) {
                NotificationPanelViewController.this.mStatusBarKeyguardViewManager.showBouncer(true);
                return true;
            }
            return super.performAccessibilityAction(view, i, bundle);
        }
    };
    private final FalsingManager.FalsingTapListener mFalsingTapListener = new FalsingManager.FalsingTapListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.3
        @Override // com.android.systemui.plugins.FalsingManager.FalsingTapListener
        public void onDoubleTapRequired() {
            if (NotificationPanelViewController.this.mStatusBarStateController.getState() == 2) {
                NotificationPanelViewController.this.mTapAgainViewController.show();
            } else {
                NotificationPanelViewController.this.mKeyguardIndicationController.showTransientIndication(R$string.notification_tap_again);
            }
            NotificationPanelViewController.this.mVibratorHelper.vibrate(1);
        }
    };
    private final Runnable mAnimateKeyguardStatusBarInvisibleEndRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.8
        @Override // java.lang.Runnable
        public void run() {
            NotificationPanelViewController.this.mKeyguardStatusBar.setVisibility(4);
            NotificationPanelViewController.this.mKeyguardStatusBar.setAlpha(1.0f);
            NotificationPanelViewController.this.mKeyguardStatusBarAnimateAlpha = 1.0f;
        }
    };
    private final ValueAnimator.AnimatorUpdateListener mStatusBarAnimateAlphaListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.10
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            NotificationPanelViewController.this.mKeyguardStatusBarAnimateAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            NotificationPanelViewController.this.updateHeaderKeyguardAlpha();
        }
    };
    private final Runnable mAnimateKeyguardBottomAreaInvisibleEndRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.11
        @Override // java.lang.Runnable
        public void run() {
            NotificationPanelViewController.this.mKeyguardBottomArea.setVisibility(8);
        }
    };
    public final QS.ScrollListener mScrollListener = new QS.ScrollListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda4
        @Override // com.android.systemui.plugins.qs.QS.ScrollListener
        public final void onQsPanelScrollChanged(int i) {
            NotificationPanelViewController.this.lambda$new$10(i);
        }
    };
    private final FragmentHostManager.FragmentListener mFragmentListener = new AnonymousClass16();

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(NotificationPanelView notificationPanelView, Float f) {
        setKeyguardHeadsUpShowingAmount(f.floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Float lambda$new$1(NotificationPanelView notificationPanelView) {
        return Float.valueOf(getKeyguardHeadsUpShowingAmount());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        setHeadsUpAnimatingAway(false);
        notifyBarPanelExpansionChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(Property property) {
        Runnable runnable = this.mPanelAlphaEndAction;
        if (runnable != null) {
            runnable.run();
        }
    }

    public NotificationPanelViewController(NotificationPanelView notificationPanelView, Resources resources, Handler handler, LayoutInflater layoutInflater, NotificationWakeUpCoordinator notificationWakeUpCoordinator, PulseExpansionHandler pulseExpansionHandler, DynamicPrivacyController dynamicPrivacyController, KeyguardBypassController keyguardBypassController, FalsingManager falsingManager, FalsingCollector falsingCollector, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationEntryManager notificationEntryManager, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, DozeLog dozeLog, DozeParameters dozeParameters, CommandQueue commandQueue, VibratorHelper vibratorHelper, LatencyTracker latencyTracker, PowerManager powerManager, AccessibilityManager accessibilityManager, int i, KeyguardUpdateMonitor keyguardUpdateMonitor, MetricsLogger metricsLogger, ActivityManager activityManager, ConfigurationController configurationController, Provider<FlingAnimationUtils.Builder> provider, StatusBarTouchableRegionManager statusBarTouchableRegionManager, ConversationNotificationManager conversationNotificationManager, MediaHierarchyManager mediaHierarchyManager, BiometricUnlockController biometricUnlockController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, NotificationStackScrollLayoutController notificationStackScrollLayoutController, KeyguardStatusViewComponent.Factory factory, KeyguardQsUserSwitchComponent.Factory factory2, KeyguardUserSwitcherComponent.Factory factory3, KeyguardStatusBarViewComponent.Factory factory4, LockscreenShadeTransitionController lockscreenShadeTransitionController, QSDetailDisplayer qSDetailDisplayer, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationIconAreaController notificationIconAreaController, AuthController authController, ScrimController scrimController, UserManager userManager, MediaDataManager mediaDataManager, NotificationShadeDepthController notificationShadeDepthController, AmbientState ambientState, LockIconViewController lockIconViewController, FeatureFlags featureFlags, KeyguardMediaController keyguardMediaController, PrivacyDotViewController privacyDotViewController, TapAgainViewController tapAgainViewController, NavigationModeController navigationModeController, FragmentService fragmentService, ContentResolver contentResolver, QuickAccessWalletController quickAccessWalletController, RecordingController recordingController, Executor executor, SecureSettings secureSettings, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, NotificationRemoteInputManager notificationRemoteInputManager, ControlsComponent controlsComponent, EmergencyButtonController.Factory factory5) {
        super(notificationPanelView, falsingManager, dozeLog, keyguardStateController, (SysuiStatusBarStateController) statusBarStateController, vibratorHelper, statusBarKeyguardViewManager, latencyTracker, provider.mo1933get(), statusBarTouchableRegionManager, ambientState);
        AnimatableProperty from = AnimatableProperty.from("panelAlpha", NotificationPanelViewController$$ExternalSyntheticLambda9.INSTANCE, NotificationPanelViewController$$ExternalSyntheticLambda15.INSTANCE, R$id.panel_alpha_animator_tag, R$id.panel_alpha_animator_start_tag, R$id.panel_alpha_animator_end_tag);
        this.mPanelAlphaAnimator = from;
        AnimationProperties duration = new AnimationProperties().setDuration(150L);
        Property property = from.getProperty();
        Interpolator interpolator = Interpolators.ALPHA_OUT;
        this.mPanelAlphaOutPropertiesAnimator = duration.setCustomInterpolator(property, interpolator);
        this.mPanelAlphaInPropertiesAnimator = new AnimationProperties().setDuration(200L).setAnimationEndAction(new Consumer() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NotificationPanelViewController.this.lambda$new$3((Property) obj);
            }
        }).setCustomInterpolator(from.getProperty(), Interpolators.ALPHA_IN);
        this.mView = notificationPanelView;
        this.mVibratorHelper = vibratorHelper;
        this.mKeyguardMediaController = keyguardMediaController;
        this.mPrivacyDotViewController = privacyDotViewController;
        this.mQuickAccessWalletController = quickAccessWalletController;
        this.mControlsComponent = controlsComponent;
        this.mMetricsLogger = metricsLogger;
        this.mActivityManager = activityManager;
        this.mConfigurationController = configurationController;
        this.mFlingAnimationUtilsBuilder = provider;
        this.mMediaHierarchyManager = mediaHierarchyManager;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mNotificationStackScrollLayoutController = notificationStackScrollLayoutController;
        this.mGroupManager = notificationGroupManagerLegacy;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mKeyguardStatusViewComponentFactory = factory;
        this.mKeyguardStatusBarViewComponentFactory = factory4;
        this.mDepthController = notificationShadeDepthController;
        this.mFeatureFlags = featureFlags;
        this.mContentResolver = contentResolver;
        this.mKeyguardQsUserSwitchComponentFactory = factory2;
        this.mKeyguardUserSwitcherComponentFactory = factory3;
        this.mEmergencyButtonControllerFactory = factory5;
        this.mQSDetailDisplayer = qSDetailDisplayer;
        this.mFragmentService = fragmentService;
        this.mSettingsChangeObserver = new SettingsChangeObserver(handler);
        this.mShouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(featureFlags, this.mResources);
        notificationPanelView.setWillNotDraw(true);
        this.mLayoutInflater = layoutInflater;
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        this.mPowerManager = powerManager;
        this.mWakeUpCoordinator = notificationWakeUpCoordinator;
        this.mAccessibilityManager = accessibilityManager;
        notificationPanelView.setAccessibilityPaneTitle(determineAccessibilityPaneTitle());
        setPanelAlpha(255, false);
        this.mCommandQueue = commandQueue;
        this.mRecordingController = recordingController;
        this.mDisplayId = i;
        this.mPulseExpansionHandler = pulseExpansionHandler;
        this.mDozeParameters = dozeParameters;
        this.mBiometricUnlockController = biometricUnlockController;
        this.mScrimController = scrimController;
        scrimController.setClipsQsScrim(true ^ this.mShouldUseSplitNotificationShade);
        this.mUserManager = userManager;
        this.mMediaDataManager = mediaDataManager;
        this.mTapAgainViewController = tapAgainViewController;
        this.mUiExecutor = executor;
        this.mSecureSettings = secureSettings;
        pulseExpansionHandler.setPulseExpandAbortListener(new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NotificationPanelViewController.this.lambda$new$4();
            }
        });
        this.mThemeResId = notificationPanelView.getContext().getThemeResId();
        this.mKeyguardBypassController = keyguardBypassController;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mFirstBypassAttempt = keyguardBypassController.getBypassEnabled();
        KeyguardStateController.Callback callback = new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.4
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public void onKeyguardFadingAwayChanged() {
                if (!NotificationPanelViewController.this.mKeyguardStateController.isKeyguardFadingAway()) {
                    NotificationPanelViewController.this.mFirstBypassAttempt = false;
                    NotificationPanelViewController.this.mDelayShowingKeyguardStatusBar = false;
                }
            }
        };
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        lockscreenShadeTransitionController.setNotificationPanelController(this);
        this.mKeyguardStateController.addCallback(callback);
        dynamicPrivacyController.addListener(new DynamicPrivacyControlListener());
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.mBottomAreaShadeAlphaAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                NotificationPanelViewController.this.lambda$new$5(valueAnimator);
            }
        });
        ofFloat.setDuration(160L);
        ofFloat.setInterpolator(interpolator);
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mEntryManager = notificationEntryManager;
        this.mConversationNotificationManager = conversationNotificationManager;
        this.mAuthController = authController;
        this.mLockIconViewController = lockIconViewController;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mIsGestureNavigation = QuickStepContract.isGesturalMode(navigationModeController.addListener(new NavigationModeController.ModeChangedListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda3
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i2) {
                NotificationPanelViewController.this.lambda$new$6(i2);
            }
        }));
        notificationPanelView.setBackgroundColor(0);
        OnAttachStateChangeListener onAttachStateChangeListener = new OnAttachStateChangeListener();
        notificationPanelView.addOnAttachStateChangeListener(onAttachStateChangeListener);
        if (notificationPanelView.isAttachedToWindow()) {
            onAttachStateChangeListener.onViewAttachedToWindow(notificationPanelView);
        }
        notificationPanelView.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener());
        this.mMaxKeyguardNotifications = resources.getInteger(R$integer.keyguard_max_notification_count);
        updateUserSwitcherFlags();
        onFinishInflate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        QS qs = this.mQs;
        if (qs != null) {
            qs.animateHeaderSlidingOut();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(ValueAnimator valueAnimator) {
        this.mBottomAreaShadeAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateKeyguardBottomAreaAlpha();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(int i) {
        this.mIsGestureNavigation = QuickStepContract.isGesturalMode(i);
    }

    private void onFinishInflate() {
        KeyguardUserSwitcherView keyguardUserSwitcherView;
        loadDimens();
        this.mKeyguardStatusBar = (KeyguardStatusBarView) this.mView.findViewById(R$id.keyguard_header);
        this.mBigClockContainer = (ViewGroup) this.mView.findViewById(R$id.big_clock_container);
        FrameLayout frameLayout = null;
        if (!this.mKeyguardUserSwitcherEnabled || !this.mUserManager.isUserSwitcherEnabled()) {
            keyguardUserSwitcherView = null;
        } else if (this.mKeyguardQsUserSwitchEnabled) {
            frameLayout = (FrameLayout) ((ViewStub) this.mView.findViewById(R$id.keyguard_qs_user_switch_stub)).inflate();
            keyguardUserSwitcherView = null;
        } else {
            keyguardUserSwitcherView = (KeyguardUserSwitcherView) ((ViewStub) this.mView.findViewById(R$id.keyguard_user_switcher_stub)).inflate();
        }
        updateViewControllers((KeyguardStatusView) this.mView.findViewById(R$id.keyguard_status_view), frameLayout, this.mKeyguardStatusBar, keyguardUserSwitcherView);
        this.mNotificationContainerParent = (NotificationsQuickSettingsContainer) this.mView.findViewById(R$id.notification_container_parent);
        this.mNotificationStackScrollLayoutController.attach((NotificationStackScrollLayout) this.mView.findViewById(R$id.notification_stack_scroller));
        this.mNotificationStackScrollLayoutController.setOnHeightChangedListener(this.mOnHeightChangedListener);
        this.mNotificationStackScrollLayoutController.setOverscrollTopChangedListener(this.mOnOverscrollTopChangedListener);
        this.mNotificationStackScrollLayoutController.setOnScrollListener(new Consumer() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NotificationPanelViewController.this.onNotificationScrolled(((Integer) obj).intValue());
            }
        });
        this.mNotificationStackScrollLayoutController.setOnStackYChanged(new Consumer() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NotificationPanelViewController.this.onStackYChanged(((Boolean) obj).booleanValue());
            }
        });
        this.mNotificationStackScrollLayoutController.setOnEmptySpaceClickListener(this.mOnEmptySpaceClickListener);
        final NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        addTrackingHeadsUpListener(new Consumer() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NotificationStackScrollLayoutController.this.setTrackingHeadsUp((ExpandableNotificationRow) obj);
            }
        });
        this.mKeyguardBottomArea = (KeyguardBottomAreaView) this.mView.findViewById(R$id.keyguard_bottom_area);
        ViewGroup viewGroup = (ViewGroup) this.mView.findViewById(R$id.preview_container);
        this.mPreviewContainer = viewGroup;
        this.mKeyguardBottomArea.setPreviewContainer(viewGroup);
        this.mLastOrientation = this.mResources.getConfiguration().orientation;
        initBottomArea();
        this.mWakeUpCoordinator.setStackScroller(this.mNotificationStackScrollLayoutController);
        this.mQsFrame = (FrameLayout) this.mView.findViewById(R$id.qs_frame);
        this.mPulseExpansionHandler.setUp(this.mNotificationStackScrollLayoutController);
        this.mWakeUpCoordinator.addListener(new NotificationWakeUpCoordinator.WakeUpListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.5
            @Override // com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.WakeUpListener
            public void onFullyHiddenChanged(boolean z) {
                NotificationPanelViewController.this.updateKeyguardStatusBarForHeadsUp();
            }

            @Override // com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.WakeUpListener
            public void onPulseExpansionChanged(boolean z) {
                if (NotificationPanelViewController.this.mKeyguardBypassController.getBypassEnabled()) {
                    NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                }
            }
        });
        this.mView.setRtlChangeListener(new NotificationPanelView.RtlChangeListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda5
            @Override // com.android.systemui.statusbar.phone.NotificationPanelView.RtlChangeListener
            public final void onRtlPropertielsChanged(int i) {
                NotificationPanelViewController.this.lambda$onFinishInflate$7(i);
            }
        });
        makeStatusBarView();
        this.mView.setAccessibilityDelegate(this.mAccessibilityDelegate);
        if (this.mShouldUseSplitNotificationShade) {
            updateResources();
        }
        this.mTapAgainViewController.init();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$7(int i) {
        if (i != this.mOldLayoutDirection) {
            this.mAffordanceHelper.onRtlPropertiesChanged();
            this.mOldLayoutDirection = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void loadDimens() {
        super.loadDimens();
        this.mFlingAnimationUtils = this.mFlingAnimationUtilsBuilder.mo1933get().setMaxLengthSeconds(0.4f).build();
        this.mStatusBarMinHeight = this.mResources.getDimensionPixelSize(17105526);
        this.mStatusBarHeaderHeightKeyguard = this.mResources.getDimensionPixelSize(R$dimen.status_bar_header_height_keyguard);
        this.mQsPeekHeight = this.mResources.getDimensionPixelSize(R$dimen.qs_peek_height);
        this.mNotificationsHeaderCollideDistance = this.mResources.getDimensionPixelSize(R$dimen.header_notifications_collide_distance);
        this.mClockPositionAlgorithm.loadDimens(this.mResources);
        this.mQsFalsingThreshold = this.mResources.getDimensionPixelSize(R$dimen.qs_falsing_threshold);
        this.mPositionMinSideMargin = this.mResources.getDimensionPixelSize(R$dimen.notification_panel_min_side_margin);
        this.mIndicationBottomPadding = this.mResources.getDimensionPixelSize(R$dimen.keyguard_indication_bottom_padding);
        this.mShelfHeight = this.mResources.getDimensionPixelSize(R$dimen.notification_shelf_height);
        this.mDarkIconSize = this.mResources.getDimensionPixelSize(R$dimen.status_bar_icon_drawing_size_dark);
        this.mHeadsUpInset = this.mResources.getDimensionPixelSize(17105526) + this.mResources.getDimensionPixelSize(R$dimen.heads_up_status_bar_padding);
        this.mDistanceForQSFullShadeTransition = this.mResources.getDimensionPixelSize(R$dimen.lockscreen_shade_qs_transition_distance);
        this.mMaxOverscrollAmountForPulse = this.mResources.getDimensionPixelSize(R$dimen.pulse_expansion_max_top_overshoot);
        this.mScrimCornerRadius = this.mResources.getDimensionPixelSize(R$dimen.notification_scrim_corner_radius);
        this.mScreenCornerRadius = (int) ScreenDecorationsUtils.getWindowCornerRadius(this.mResources);
        this.mLockscreenNotificationQSPadding = this.mResources.getDimensionPixelSize(R$dimen.notification_side_paddings);
        this.mUdfpsMaxYBurnInOffset = this.mResources.getDimensionPixelSize(R$dimen.udfps_burn_in_offset_y);
    }

    private void updateViewControllers(KeyguardStatusView keyguardStatusView, FrameLayout frameLayout, KeyguardStatusBarView keyguardStatusBarView, KeyguardUserSwitcherView keyguardUserSwitcherView) {
        KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewComponentFactory.build(keyguardStatusView).getKeyguardStatusViewController();
        this.mKeyguardStatusViewController = keyguardStatusViewController;
        keyguardStatusViewController.init();
        KeyguardStatusBarViewController keyguardStatusBarViewController = this.mKeyguardStatusBarViewComponentFactory.build(keyguardStatusBarView).getKeyguardStatusBarViewController();
        this.mKeyguarStatusBarViewController = keyguardStatusBarViewController;
        keyguardStatusBarViewController.init();
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(false);
        }
        this.mKeyguardQsUserSwitchController = null;
        this.mKeyguardUserSwitcherController = null;
        if (frameLayout != null) {
            KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchComponentFactory.build(frameLayout).getKeyguardQsUserSwitchController();
            this.mKeyguardQsUserSwitchController = keyguardQsUserSwitchController;
            keyguardQsUserSwitchController.setNotificationPanelViewController(this);
            this.mKeyguardQsUserSwitchController.init();
            this.mKeyguardStatusBar.setKeyguardUserSwitcherEnabled(true);
        } else if (keyguardUserSwitcherView != null) {
            KeyguardUserSwitcherController keyguardUserSwitcherController2 = this.mKeyguardUserSwitcherComponentFactory.build(keyguardUserSwitcherView).getKeyguardUserSwitcherController();
            this.mKeyguardUserSwitcherController = keyguardUserSwitcherController2;
            keyguardUserSwitcherController2.init();
            this.mKeyguardStatusBar.setKeyguardUserSwitcherEnabled(true);
        } else {
            this.mKeyguardStatusBar.setKeyguardUserSwitcherEnabled(false);
        }
    }

    public boolean hasCustomClock() {
        return this.mKeyguardStatusViewController.hasCustomClock();
    }

    private void setStatusBar(StatusBar statusBar) {
        this.mStatusBar = statusBar;
        this.mKeyguardBottomArea.setStatusBar(statusBar);
    }

    public void updateResources() {
        this.mSplitShadeNotificationsTopPadding = this.mResources.getDimensionPixelSize(R$dimen.notifications_top_padding_split_shade);
        int dimensionPixelSize = this.mResources.getDimensionPixelSize(R$dimen.qs_panel_width);
        int dimensionPixelSize2 = this.mResources.getDimensionPixelSize(R$dimen.notification_panel_width);
        boolean shouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(this.mFeatureFlags, this.mResources);
        this.mShouldUseSplitNotificationShade = shouldUseSplitNotificationShade;
        int i = 0;
        this.mHasQSLayout = false;
        this.mCanQsCollapseForLand = true;
        this.mScrimController.setClipsQsScrim(!shouldUseSplitNotificationShade);
        QS qs = this.mQs;
        if (qs != null) {
            qs.setTranslateWhileExpanding(this.mShouldUseSplitNotificationShade);
        }
        ensureAllViewsHaveIds(this.mNotificationContainerParent);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mNotificationContainerParent);
        if (this.mShouldUseSplitNotificationShade) {
            int i2 = R$id.qs_frame;
            int i3 = R$id.qs_edge_guideline;
            constraintSet.connect(i2, 7, i3, 7);
            constraintSet.connect(R$id.notification_stack_scroller, 6, i3, 6);
            constraintSet.connect(R$id.keyguard_status_view, 7, i3, 7);
            dimensionPixelSize = 0;
            dimensionPixelSize2 = 0;
        } else {
            constraintSet.connect(R$id.qs_frame, 7, 0, 7);
            constraintSet.connect(R$id.notification_stack_scroller, 6, 0, 6);
            constraintSet.connect(R$id.keyguard_status_view, 7, 0, 7);
        }
        constraintSet.getConstraint(R$id.notification_stack_scroller).layout.mWidth = dimensionPixelSize2;
        constraintSet.getConstraint(R$id.qs_frame).layout.mWidth = dimensionPixelSize;
        constraintSet.applyTo(this.mNotificationContainerParent);
        this.mKeyguardMediaController.refreshMediaPosition();
        this.mQuickQsOffsetHeight = this.mResources.getDimensionPixelSize(this.mShouldUseSplitNotificationShade ? R$dimen.quick_qs_offset_height : 17105476);
        RelativeLayout relativeLayout = this.mQsStatusBarLayoutLand;
        if (relativeLayout != null) {
            if (!this.mShouldUseSplitNotificationShade) {
                i = 8;
            }
            relativeLayout.setVisibility(i);
        }
        updateQsState();
    }

    private static void ensureAllViewsHaveIds(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getId() == -1) {
                childAt.setId(View.generateViewId());
            }
        }
    }

    private View reInflateStub(int i, int i2, int i3, boolean z) {
        View findViewById = this.mView.findViewById(i);
        if (findViewById == null) {
            return z ? ((ViewStub) this.mView.findViewById(i2)).inflate() : findViewById;
        }
        int indexOfChild = this.mView.indexOfChild(findViewById);
        this.mView.removeView(findViewById);
        if (z) {
            View inflate = this.mLayoutInflater.inflate(i3, (ViewGroup) this.mView, false);
            this.mView.addView(inflate, indexOfChild);
            return inflate;
        }
        ViewStub viewStub = new ViewStub(this.mView.getContext(), i3);
        viewStub.setId(i2);
        this.mView.addView(viewStub, indexOfChild);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reInflateViews() {
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = this.mNotificationContainerParent;
        int i = R$id.keyguard_status_view;
        KeyguardStatusView keyguardStatusView = (KeyguardStatusView) notificationsQuickSettingsContainer.findViewById(i);
        int indexOfChild = this.mNotificationContainerParent.indexOfChild(keyguardStatusView);
        this.mNotificationContainerParent.removeView(keyguardStatusView);
        KeyguardStatusView keyguardStatusView2 = (KeyguardStatusView) this.mLayoutInflater.inflate(R$layout.keyguard_status_view, (ViewGroup) this.mNotificationContainerParent, false);
        this.mNotificationContainerParent.addView(keyguardStatusView2, indexOfChild);
        attachSplitShadeMediaPlayerContainer((FrameLayout) keyguardStatusView2.findViewById(R$id.status_view_media_container));
        updateResources();
        updateUserSwitcherFlags();
        boolean isUserSwitcherEnabled = this.mUserManager.isUserSwitcherEnabled();
        boolean z = this.mKeyguardQsUserSwitchEnabled;
        boolean z2 = true;
        boolean z3 = z && isUserSwitcherEnabled;
        if (z || !this.mKeyguardUserSwitcherEnabled || !isUserSwitcherEnabled) {
            z2 = false;
        }
        this.mBigClockContainer.removeAllViews();
        updateViewControllers((KeyguardStatusView) this.mView.findViewById(i), (FrameLayout) reInflateStub(R$id.keyguard_qs_user_switch_view, R$id.keyguard_qs_user_switch_stub, R$layout.keyguard_qs_user_switch, z3), this.mKeyguardStatusBar, (KeyguardUserSwitcherView) reInflateStub(R$id.keyguard_user_switcher_view, R$id.keyguard_user_switcher_stub, R$layout.keyguard_user_switcher, z2));
        int indexOfChild2 = this.mView.indexOfChild(this.mKeyguardBottomArea);
        this.mView.removeView(this.mKeyguardBottomArea);
        KeyguardBottomAreaView keyguardBottomAreaView = this.mKeyguardBottomArea;
        KeyguardBottomAreaView keyguardBottomAreaView2 = (KeyguardBottomAreaView) this.mLayoutInflater.inflate(R$layout.keyguard_bottom_area, (ViewGroup) this.mView, false);
        this.mKeyguardBottomArea = keyguardBottomAreaView2;
        keyguardBottomAreaView2.initFrom(keyguardBottomAreaView);
        this.mKeyguardBottomArea.setPreviewContainer(this.mPreviewContainer);
        this.mView.addView(this.mKeyguardBottomArea, indexOfChild2);
        initBottomArea();
        this.mKeyguardIndicationController.setIndicationArea(this.mKeyguardBottomArea);
        this.mStatusBarStateListener.onDozeAmountChanged(this.mStatusBarStateController.getDozeAmount(), this.mStatusBarStateController.getInterpolatedDozeAmount());
        KeyguardStatusBarView keyguardStatusBarView = this.mKeyguardStatusBar;
        if (keyguardStatusBarView != null) {
            keyguardStatusBarView.onThemeChanged();
        }
        KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewController;
        int i2 = this.mBarState;
        keyguardStatusViewController.setKeyguardStatusViewVisibility(i2, false, false, i2);
        KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchController;
        if (keyguardQsUserSwitchController != null) {
            int i3 = this.mBarState;
            keyguardQsUserSwitchController.setKeyguardQsUserSwitchVisibility(i3, false, false, i3);
        }
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            int i4 = this.mBarState;
            keyguardUserSwitcherController.setKeyguardUserSwitcherVisibility(i4, false, false, i4);
        }
        setKeyguardBottomAreaVisibility(this.mBarState, false);
    }

    private void attachSplitShadeMediaPlayerContainer(FrameLayout frameLayout) {
        this.mKeyguardMediaController.attachSplitShadeContainer(frameLayout);
    }

    private void initBottomArea() {
        KeyguardAffordanceHelper keyguardAffordanceHelper = new KeyguardAffordanceHelper(this.mKeyguardAffordanceHelperCallback, this.mView.getContext(), this.mFalsingManager);
        this.mAffordanceHelper = keyguardAffordanceHelper;
        this.mKeyguardBottomArea.setAffordanceHelper(keyguardAffordanceHelper);
        this.mKeyguardBottomArea.setStatusBar(this.mStatusBar);
        this.mKeyguardBottomArea.setUserSetupComplete(this.mUserSetupComplete);
        this.mKeyguardBottomArea.setFalsingManager(this.mFalsingManager);
        this.mKeyguardBottomArea.initWallet(this.mQuickAccessWalletController);
        this.mKeyguardBottomArea.initControls(this.mControlsComponent);
        EmergencyButtonController create = this.mEmergencyButtonControllerFactory.create((EmergencyButton) this.mKeyguardBottomArea.findViewById(R$id.emergency_call_button));
        this.mEmergencyButtonController = create;
        create.init();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxDisplayedNotifications(boolean z) {
        if (z) {
            this.mMaxAllowedKeyguardNotifications = Math.max(computeMaxKeyguardNotifications(), 1);
        }
        if (this.mKeyguardShowing && !this.mKeyguardBypassController.getBypassEnabled()) {
            this.mNotificationStackScrollLayoutController.setMaxDisplayedNotifications(this.mMaxAllowedKeyguardNotifications);
        } else {
            this.mNotificationStackScrollLayoutController.setMaxDisplayedNotifications(-1);
        }
    }

    public void setKeyguardIndicationController(KeyguardIndicationController keyguardIndicationController) {
        this.mKeyguardIndicationController = keyguardIndicationController;
        keyguardIndicationController.setIndicationArea(this.mKeyguardBottomArea);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateGestureExclusionRect() {
        Rect calculateGestureExclusionRect = calculateGestureExclusionRect();
        this.mView.setSystemGestureExclusionRects(calculateGestureExclusionRect.isEmpty() ? Collections.EMPTY_LIST : Collections.singletonList(calculateGestureExclusionRect));
    }

    private Rect calculateGestureExclusionRect() {
        Region calculateTouchableRegion = this.mStatusBarTouchableRegionManager.calculateTouchableRegion();
        Rect bounds = (!isFullyCollapsed() || calculateTouchableRegion == null) ? null : calculateTouchableRegion.getBounds();
        return bounds != null ? bounds : EMPTY_RECT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIsFullWidth(boolean z) {
        this.mIsFullWidth = z;
        this.mNotificationStackScrollLayoutController.setIsFullWidth(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startQsSizeChangeAnimation(int i, int i2) {
        ValueAnimator valueAnimator = this.mQsSizeChangeAnimator;
        if (valueAnimator != null) {
            i = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            this.mQsSizeChangeAnimator.cancel();
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(i, i2);
        this.mQsSizeChangeAnimator = ofInt;
        ofInt.setDuration(300L);
        this.mQsSizeChangeAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mQsSizeChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                NotificationPanelViewController.this.requestPanelHeightUpdate();
                NotificationPanelViewController.this.mQs.setHeightOverride(((Integer) NotificationPanelViewController.this.mQsSizeChangeAnimator.getAnimatedValue()).intValue());
            }
        });
        this.mQsSizeChangeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.7
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                NotificationPanelViewController.this.mQsSizeChangeAnimator = null;
            }
        });
        this.mQsSizeChangeAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void positionClockAndNotifications() {
        positionClockAndNotifications(false);
    }

    private void positionClockAndNotifications(boolean z) {
        int i;
        boolean isAddOrRemoveAnimationPending = this.mNotificationStackScrollLayoutController.isAddOrRemoveAnimationPending();
        boolean isOnKeyguard = isOnKeyguard();
        if (isOnKeyguard || z) {
            updateClockAppearance();
        }
        if (!isOnKeyguard) {
            i = getUnlockedStackScrollerPadding();
        } else {
            i = this.mClockPositionResult.stackScrollerPaddingExpanded;
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        if (this.mShouldUseSplitNotificationShade) {
            i = 0;
        }
        notificationStackScrollLayoutController.setIntrinsicPadding(i);
        this.mKeyguardBottomArea.setAntiBurnInOffsetX(this.mClockPositionResult.clockX);
        this.mStackScrollerMeasuringPass++;
        requestScrollerTopPaddingUpdate(isAddOrRemoveAnimationPending);
        this.mStackScrollerMeasuringPass = 0;
        this.mAnimateNextPositionUpdate = false;
    }

    private void updateClockAppearance() {
        int height = this.mView.getHeight();
        int max = Math.max(this.mIndicationBottomPadding, this.mAmbientIndicationBottomPadding);
        int i = this.mStatusBarHeaderHeightKeyguard;
        boolean bypassEnabled = this.mKeyguardBypassController.getBypassEnabled();
        boolean z = this.mNotificationStackScrollLayoutController.getVisibleNotificationCount() != 0 || this.mMediaDataManager.hasActiveMedia();
        this.mKeyguardStatusViewController.setHasVisibleNotifications(z);
        KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchController;
        int userIconHeight = keyguardQsUserSwitchController != null ? keyguardQsUserSwitchController.getUserIconHeight() : 0;
        float expandedFraction = this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying() ? 1.0f : getExpandedFraction();
        float f = this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying() ? 1.0f : this.mInterpolatedDarkAmount;
        float f2 = -1.0f;
        if (this.mUpdateMonitor.isUdfpsEnrolled() && this.mAuthController.getUdfpsProps().size() > 0) {
            FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mAuthController.getUdfpsProps().get(0);
            f2 = (fingerprintSensorPropertiesInternal.sensorLocationY - fingerprintSensorPropertiesInternal.sensorRadius) - this.mUdfpsMaxYBurnInOffset;
        }
        boolean z2 = false;
        this.mClockPositionAlgorithm.setup(this.mStatusBarHeaderHeightKeyguard, height - max, this.mNotificationStackScrollLayoutController.getIntrinsicContentHeight(), expandedFraction, height, this.mKeyguardStatusViewController.getLockscreenHeight(), userIconHeight, i, hasCustomClock(), z, f, this.mOverStretchAmount, bypassEnabled, getUnlockedStackScrollerPadding(), computeQsExpansionFraction(), this.mDisplayTopInset, this.mShouldUseSplitNotificationShade, f2, this.mKeyguardStatusViewController.getClockBottom(this.mStatusBarHeaderHeightKeyguard), this.mKeyguardStatusViewController.isClockTopAligned());
        this.mClockPositionAlgorithm.run(this.mClockPositionResult);
        if (this.mNotificationStackScrollLayoutController.isAddOrRemoveAnimationPending() || this.mAnimateNextPositionUpdate) {
            z2 = true;
        }
        KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewController;
        KeyguardClockPositionAlgorithm.Result result = this.mClockPositionResult;
        keyguardStatusViewController.updatePosition(result.clockX, result.clockY, result.clockScale, z2);
        KeyguardQsUserSwitchController keyguardQsUserSwitchController2 = this.mKeyguardQsUserSwitchController;
        if (keyguardQsUserSwitchController2 != null) {
            KeyguardClockPositionAlgorithm.Result result2 = this.mClockPositionResult;
            keyguardQsUserSwitchController2.updatePosition(result2.clockX, result2.userSwitchY, z2);
        }
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            KeyguardClockPositionAlgorithm.Result result3 = this.mClockPositionResult;
            keyguardUserSwitcherController.updatePosition(result3.clockX, result3.userSwitchY, z2);
        }
        updateNotificationTranslucency();
        updateClock();
    }

    private int getUnlockedStackScrollerPadding() {
        QS qs = this.mQs;
        return (qs != null ? qs.getHeader().getHeight() : 0) + this.mQsPeekHeight;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int computeMaxKeyguardNotifications() {
        int i;
        this.mClockPositionAlgorithm.getMinStackScrollerPadding();
        int max = Math.max(1, this.mResources.getDimensionPixelSize(R$dimen.notification_divider_height));
        float intrinsicHeight = this.mNotificationShelfController.getVisibility() == 8 ? 0.0f : this.mNotificationShelfController.getIntrinsicHeight() + max;
        Math.max(this.mLockIconViewController.getTop() != 0.0f ? (this.mStatusBar.getDisplayHeight() - this.mLockIconViewController.getTop()) + this.mResources.getDimensionPixelSize(R$dimen.min_lock_icon_padding) : 0.0f, Math.max(this.mIndicationBottomPadding, this.mAmbientIndicationBottomPadding));
        float dimensionPixelSize = this.mView.getContext().getResources().getDimensionPixelSize(R$dimen.keyguard_notification_max_total_height);
        ExpandableView expandableView = null;
        int i2 = 0;
        for (int i3 = 0; i3 < this.mNotificationStackScrollLayoutController.getChildCount(); i3++) {
            ExpandableView childAt = this.mNotificationStackScrollLayoutController.getChildAt(i3);
            if (childAt instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.mGroupManager;
                if (notificationGroupManagerLegacy != null && notificationGroupManagerLegacy.isSummaryOfSuppressedGroup(expandableNotificationRow.getEntry().getSbn())) {
                    continue;
                } else if (canShowViewOnLockscreen(childAt)) {
                    if (expandableNotificationRow.isRemoved()) {
                        continue;
                    }
                    dimensionPixelSize = ((dimensionPixelSize - childAt.getMinHeight(true)) - (i2 != 0 ? 0.0f : max)) - this.mNotificationStackScrollLayoutController.calculateGapHeight(expandableView, childAt, i2);
                    if (dimensionPixelSize >= 0.0f || ((i = this.mMaxKeyguardNotifications) != -1 && i2 >= i)) {
                        if (dimensionPixelSize > (-intrinsicHeight)) {
                            return i2;
                        }
                        int childCount = this.mNotificationStackScrollLayoutController.getChildCount();
                        for (int i4 = i3 + 1; i4 < childCount; i4++) {
                            ExpandableView childAt2 = this.mNotificationStackScrollLayoutController.getChildAt(i4);
                            if ((childAt2 instanceof ExpandableNotificationRow) && canShowViewOnLockscreen(childAt2)) {
                                return i2;
                            }
                        }
                        return i2 + 1;
                    }
                    i2++;
                    expandableView = childAt;
                } else {
                    continue;
                }
            } else {
                if (!(childAt instanceof MediaHeaderView)) {
                    continue;
                } else if (childAt.getVisibility() == 8) {
                    continue;
                } else {
                    if (childAt.getIntrinsicHeight() == 0) {
                        continue;
                    }
                    dimensionPixelSize = ((dimensionPixelSize - childAt.getMinHeight(true)) - (i2 != 0 ? 0.0f : max)) - this.mNotificationStackScrollLayoutController.calculateGapHeight(expandableView, childAt, i2);
                    if (dimensionPixelSize >= 0.0f) {
                    }
                    if (dimensionPixelSize > (-intrinsicHeight)) {
                    }
                }
            }
        }
        return i2;
    }

    private boolean canShowViewOnLockscreen(ExpandableView expandableView) {
        if (expandableView.hasNoContentHeight()) {
            return false;
        }
        return (!(expandableView instanceof ExpandableNotificationRow) || canShowRowOnLockscreen((ExpandableNotificationRow) expandableView)) && expandableView.getVisibility() != 8;
    }

    private boolean canShowRowOnLockscreen(ExpandableNotificationRow expandableNotificationRow) {
        NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.mGroupManager;
        return !(notificationGroupManagerLegacy != null && notificationGroupManagerLegacy.isSummaryOfSuppressedGroup(expandableNotificationRow.getEntry().getSbn())) && this.mLockscreenUserManager.shouldShowOnKeyguard(expandableNotificationRow.getEntry()) && !expandableNotificationRow.isRemoved();
    }

    private void updateClock() {
        float f = this.mClockPositionResult.clockAlpha * this.mKeyguardOnlyContentAlpha;
        this.mKeyguardStatusViewController.setAlpha(f);
        KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchController;
        if (keyguardQsUserSwitchController != null) {
            keyguardQsUserSwitchController.setAlpha(f);
        }
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            keyguardUserSwitcherController.setAlpha(f);
        }
    }

    public void animateToFullShade(long j) {
        this.mNotificationStackScrollLayoutController.goToFullShade(j);
        this.mView.requestLayout();
        this.mAnimateNextPositionUpdate = true;
    }

    private void setQsExpansionEnabled() {
        QS qs = this.mQs;
        if (qs == null) {
            return;
        }
        qs.setHeaderClickable(isQsExpansionEnabled());
    }

    public void setQsExpansionEnabledPolicy(boolean z) {
        this.mQsExpansionEnabledPolicy = z;
        setQsExpansionEnabled();
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void resetViews(boolean z) {
        this.mIsLaunchTransitionFinished = false;
        this.mBlockTouches = false;
        this.mHasOrientationChanged = false;
        if (!this.mLaunchingAffordance) {
            this.mAffordanceHelper.reset(false);
            this.mLastCameraLaunchSource = "lockscreen_affordance";
        }
        this.mStatusBar.getGutsManager().closeAndSaveGuts(true, true, true, -1, -1, true);
        if (z && !isFullyCollapsed()) {
            animateCloseQs(true);
        } else {
            closeQs();
        }
        this.mNotificationStackScrollLayoutController.setOverScrollAmount(0.0f, true, z, !z);
        this.mNotificationStackScrollLayoutController.resetScrollPosition();
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void collapse(boolean z, float f) {
        if (!canPanelBeCollapsed()) {
            return;
        }
        if (this.mQsExpanded) {
            this.mQsExpandImmediate = true;
            this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(true);
        }
        super.collapse(z, f);
    }

    public void closeQs() {
        cancelQsAnimation();
        setQsExpansion(this.mQsMinExpansionHeight);
    }

    public void cancelAnimation() {
        this.mView.animate().cancel();
    }

    public void animateCloseQs(boolean z) {
        ValueAnimator valueAnimator = this.mQsExpansionAnimator;
        if (valueAnimator != null) {
            if (!this.mQsAnimatorExpand) {
                return;
            }
            float f = this.mQsExpansionHeight;
            valueAnimator.cancel();
            setQsExpansion(f);
        }
        flingSettings(0.0f, z ? 2 : 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isQsExpansionEnabled() {
        return this.mQsExpansionEnabledPolicy && this.mQsExpansionEnabledAmbient && !this.mRemoteInputManager.getController().isRemoteInputActive();
    }

    public void expandWithQs() {
        if (isQsExpansionEnabled()) {
            this.mQsExpandImmediate = true;
            this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(true);
        }
        if (isFullyCollapsed()) {
            expand(true);
            return;
        }
        traceQsJank(true, false);
        flingSettings(0.0f, 0);
    }

    public void expandWithQsDetail(DetailAdapter detailAdapter) {
        traceQsJank(true, false);
        flingSettings(0.0f, 0);
        this.mQSDetailDisplayer.showDetailAdapter(detailAdapter, this.mQsFrame.getWidth() / 2, -getHeight());
        if (this.mAccessibilityManager.isEnabled()) {
            this.mView.setAccessibilityPaneTitle(determineAccessibilityPaneTitle());
        }
    }

    public void expandWithoutQs() {
        if (isQsExpanded()) {
            flingSettings(0.0f, 1);
        } else {
            expand(true);
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void fling(float f, boolean z) {
        ((PhoneStatusBarView) this.mBar).mBar.getGestureRecorder();
        super.fling(f, z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void flingToHeight(float f, boolean z, float f2, float f3, boolean z2) {
        this.mHeadsUpTouchHelper.notifyFling(!z);
        this.mKeyguardStateController.notifyPanelFlingStart(!z);
        setClosingWithAlphaFadeout(!z && !isOnKeyguard() && getFadeoutAlpha() == 1.0f);
        super.flingToHeight(f, z, f2, f3, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onQsIntercept(MotionEvent motionEvent) {
        int pointerId;
        int findPointerIndex = motionEvent.findPointerIndex(this.mTrackingPointer);
        if (findPointerIndex < 0) {
            this.mTrackingPointer = motionEvent.getPointerId(0);
            findPointerIndex = 0;
        }
        float x = motionEvent.getX(findPointerIndex);
        float y = motionEvent.getY(findPointerIndex);
        int actionMasked = motionEvent.getActionMasked();
        int i = 1;
        if (actionMasked == 0) {
            this.mInitialTouchY = y;
            this.mInitialTouchX = x;
            initVelocityTracker();
            trackMovement(motionEvent);
            if (this.mKeyguardShowing && shouldQuickSettingsIntercept(this.mInitialTouchX, this.mInitialTouchY, 0.0f)) {
                this.mView.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (this.mQsExpansionAnimator != null) {
                this.mInitialHeightOnTouch = this.mQsExpansionHeight;
                this.mQsTracking = true;
                traceQsJank(true, false);
                this.mNotificationStackScrollLayoutController.cancelLongPress();
            }
        } else {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    float f = y - this.mInitialTouchY;
                    trackMovement(motionEvent);
                    if (this.mQsTracking) {
                        setQsExpansion(f + this.mInitialHeightOnTouch);
                        trackMovement(motionEvent);
                        return true;
                    } else if ((f > getTouchSlop(motionEvent) || (f < (-getTouchSlop(motionEvent)) && this.mQsExpanded)) && Math.abs(f) > Math.abs(x - this.mInitialTouchX) && shouldQuickSettingsIntercept(this.mInitialTouchX, this.mInitialTouchY, f)) {
                        this.mView.getParent().requestDisallowInterceptTouchEvent(true);
                        this.mQsTracking = true;
                        traceQsJank(true, false);
                        onQsExpansionStarted();
                        notifyExpandingFinished();
                        this.mInitialHeightOnTouch = this.mQsExpansionHeight;
                        this.mInitialTouchY = y;
                        this.mInitialTouchX = x;
                        this.mNotificationStackScrollLayoutController.cancelLongPress();
                        return true;
                    }
                } else if (actionMasked != 3) {
                    if (actionMasked == 6 && this.mTrackingPointer == (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                        if (motionEvent.getPointerId(0) != pointerId) {
                            i = 0;
                        }
                        this.mTrackingPointer = motionEvent.getPointerId(i);
                        this.mInitialTouchX = motionEvent.getX(i);
                        this.mInitialTouchY = motionEvent.getY(i);
                    }
                }
            }
            trackMovement(motionEvent);
            this.mQsTracking = false;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected boolean isInContentBounds(float f, float f2) {
        float x = this.mNotificationStackScrollLayoutController.getX();
        return !this.mNotificationStackScrollLayoutController.isBelowLastNotification(f - x, f2) && x < f && f < x + this.mNotificationStackScrollLayoutController.getWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void traceQsJank(boolean z, boolean z2) {
        InteractionJankMonitor interactionJankMonitor = InteractionJankMonitor.getInstance();
        if (z) {
            interactionJankMonitor.begin(this.mView, 5);
        } else if (z2) {
            interactionJankMonitor.cancel(5);
        } else {
            interactionJankMonitor.end(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initDownStates(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.mOnlyAffordanceInThisMotion = false;
            this.mQsTouchAboveFalsingThreshold = this.mQsFullyExpanded;
            this.mDozingOnDown = isDozing();
            this.mDownX = motionEvent.getX();
            this.mDownY = motionEvent.getY();
            this.mCollapsedOnDown = isFullyCollapsed();
            this.mCanQsCollapseForLand = !this.mQsExpanded;
            this.mIsPanelCollapseOnQQS = canPanelCollapseOnQQS(this.mDownX, this.mDownY);
            this.mListenForHeadsUp = this.mCollapsedOnDown && this.mHeadsUpManager.hasPinnedHeadsUp();
            boolean z = this.mExpectingSynthesizedDown;
            this.mAllowExpandForSmallExpansion = z;
            this.mTouchSlopExceededBeforeDown = z;
            if (z) {
                this.mLastEventSynthesizedDown = true;
                return;
            } else {
                this.mLastEventSynthesizedDown = false;
                return;
            }
        }
        this.mLastEventSynthesizedDown = false;
    }

    private boolean canPanelCollapseOnQQS(float f, float f2) {
        if (this.mCollapsedOnDown || this.mKeyguardShowing || this.mQsExpanded) {
            return false;
        }
        QS qs = this.mQs;
        return f >= this.mQsFrame.getX() && f <= this.mQsFrame.getX() + ((float) this.mQsFrame.getWidth()) && f2 <= ((float) (qs == null ? this.mKeyguardStatusBar : qs.getHeader()).getBottom());
    }

    private void flingQsWithCurrentVelocity(float f, boolean z) {
        float currentQSVelocity = getCurrentQSVelocity();
        boolean flingExpandsQs = flingExpandsQs(currentQSVelocity);
        int i = 0;
        if (flingExpandsQs) {
            if (this.mFalsingManager.isUnlockingDisabled() || isFalseTouch(0)) {
                flingExpandsQs = false;
            } else {
                logQsSwipeDown(f);
            }
        } else if (currentQSVelocity < 0.0f) {
            this.mFalsingManager.isFalseTouch(12);
        }
        if (SystemUIUtils.getInstance().shouldUseSplitNotificationShade()) {
            if (!flingExpandsQs || z) {
                i = 2;
            }
            flingSettings(currentQSVelocity, i);
            return;
        }
        if (!flingExpandsQs || z) {
            i = 1;
        }
        flingSettings(currentQSVelocity, i);
    }

    private void logQsSwipeDown(float f) {
        this.mLockscreenGestureLogger.write(this.mBarState == 1 ? 193 : 194, (int) ((f - this.mInitialTouchY) / this.mStatusBar.getDisplayDensity()), (int) (getCurrentQSVelocity() / this.mStatusBar.getDisplayDensity()));
    }

    private boolean flingExpandsQs(float f) {
        return Math.abs(f) < this.mFlingAnimationUtils.getMinVelocityPxPerSecond() ? computeQsExpansionFraction() > 0.5f : f > 0.0f;
    }

    private boolean isFalseTouch(int i) {
        if (this.mFalsingManager.isClassifierEnabled()) {
            return this.mFalsingManager.isFalseTouch(i);
        }
        return !this.mQsTouchAboveFalsingThreshold;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void setMinFraction(float f) {
        this.mMinFraction = f;
        this.mDepthController.setPanelPullDownMinFraction(f);
    }

    private float computeQsExpansionFraction() {
        if (this.mQSAnimatingHiddenFromCollapsed) {
            return 0.0f;
        }
        float f = this.mQsExpansionHeight;
        int i = this.mQsMinExpansionHeight;
        float min = Math.min(1.0f, (f - i) / (this.mQsMaxExpansionHeight - i));
        if (Float.isNaN(min) && !this.mHasQSLayout && this.mQsFullyExpanded) {
            return 1.0f;
        }
        if (!Float.isNaN(min)) {
            return min;
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public boolean shouldExpandWhenNotFlinging() {
        if (super.shouldExpandWhenNotFlinging()) {
            return true;
        }
        return this.mAllowExpandForSmallExpansion && SystemClock.uptimeMillis() - this.mDownTime <= 300;
    }

    protected float getOpeningHeight() {
        return this.mNotificationStackScrollLayoutController.getOpeningHeight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleQsTouch(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0 && getExpandedFraction() == 1.0f && this.mBarState != 1 && !this.mQsExpanded && isQsExpansionEnabled()) {
            this.mQsTracking = true;
            traceQsJank(true, false);
            this.mConflictingQsExpansionGesture = true;
            onQsExpansionStarted();
            this.mInitialHeightOnTouch = this.mQsExpansionHeight;
            this.mInitialTouchY = motionEvent.getX();
            this.mInitialTouchX = motionEvent.getY();
        }
        if (!isFullyCollapsed() && !this.mShouldUseSplitNotificationShade) {
            handleQsDown(motionEvent);
        }
        if (!this.mQsExpandImmediate && this.mQsTracking) {
            onQsTouch(motionEvent);
            if (!this.mConflictingQsExpansionGesture) {
                return true;
            }
        }
        if (actionMasked == 3 || actionMasked == 1) {
            this.mConflictingQsExpansionGesture = false;
        }
        if (actionMasked == 0 && isFullyCollapsed() && isQsExpansionEnabled()) {
            this.mTwoFingerQsExpandPossible = true;
        }
        if ((SystemUIUtils.getInstance().shouldUseSplitNotificationShade() || (this.mTwoFingerQsExpandPossible && isOpenQsEvent(motionEvent))) && motionEvent.getY(motionEvent.getActionIndex()) < this.mStatusBarMinHeight) {
            this.mMetricsLogger.count("panel_open_qs", 1);
            this.mQsExpandImmediate = true;
            this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(true);
            requestPanelHeightUpdate();
            setListening(true);
        }
        return false;
    }

    private boolean isInQsArea(float f, float f2) {
        if (f < this.mQsFrame.getX() || f > this.mQsFrame.getX() + this.mQsFrame.getWidth()) {
            return false;
        }
        if (this.mIsGestureNavigation && f2 > this.mView.getHeight() - this.mNavigationBarBottomHeight) {
            return false;
        }
        return f2 <= this.mNotificationStackScrollLayoutController.getBottomMostNotificationBottom() || f2 <= this.mQs.getView().getY() + ((float) this.mQs.getView().getHeight());
    }

    private boolean isOpenQsEvent(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        int actionMasked = motionEvent.getActionMasked();
        return (actionMasked == 5 && pointerCount == 2) || (actionMasked == 0 && (motionEvent.isButtonPressed(32) || motionEvent.isButtonPressed(64))) || (actionMasked == 0 && (motionEvent.isButtonPressed(2) || motionEvent.isButtonPressed(4)));
    }

    private void handleQsDown(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0 || !shouldQuickSettingsIntercept(motionEvent.getX(), motionEvent.getY(), -1.0f)) {
            return;
        }
        this.mFalsingCollector.onQsDown();
        this.mQsTracking = true;
        onQsExpansionStarted();
        this.mInitialHeightOnTouch = this.mQsExpansionHeight;
        this.mInitialTouchY = motionEvent.getX();
        this.mInitialTouchX = motionEvent.getY();
        notifyExpandingFinished();
    }

    public void startWaitingForOpenPanelGesture() {
        if (!isFullyCollapsed()) {
            return;
        }
        this.mExpectingSynthesizedDown = true;
        onTrackingStarted();
        updatePanelExpanded();
    }

    public void stopWaitingForOpenPanelGesture(boolean z, float f) {
        if (this.mExpectingSynthesizedDown) {
            this.mExpectingSynthesizedDown = false;
            if (z) {
                collapse(false, 1.0f);
            } else {
                maybeVibrateOnOpening();
                fling(f > 1.0f ? f * 1000.0f : 0.0f, true);
            }
            onTrackingStopped(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public boolean flingExpands(float f, float f2, float f3, float f4) {
        boolean flingExpands = super.flingExpands(f, f2, f3, f4);
        if (this.mQsExpansionAnimator != null) {
            flingExpands = true;
        }
        if (this.mShouldUseSplitNotificationShade && flingExpands) {
            this.mQsExpandImmediate = true;
        }
        if (flingExpands) {
            ((NothingOSHeadsupManager) Dependency.get(NothingOSHeadsupManager.class)).hidePopNotificationView();
        }
        return flingExpands;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected boolean shouldGestureWaitForTouchSlop() {
        if (!this.mExpectingSynthesizedDown) {
            return isFullyCollapsed() || this.mBarState != 0;
        }
        this.mExpectingSynthesizedDown = false;
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected boolean shouldGestureIgnoreXTouchSlop(float f, float f2) {
        return !this.mAffordanceHelper.isOnAffordanceIcon(f, f2);
    }

    private void onQsTouch(MotionEvent motionEvent) {
        int pointerId;
        int findPointerIndex = motionEvent.findPointerIndex(this.mTrackingPointer);
        boolean z = false;
        int i = 0;
        if (findPointerIndex < 0) {
            this.mTrackingPointer = motionEvent.getPointerId(0);
            findPointerIndex = 0;
        }
        float y = motionEvent.getY(findPointerIndex);
        float x = motionEvent.getX(findPointerIndex);
        float f = y - this.mInitialTouchY;
        int actionMasked = motionEvent.getActionMasked();
        boolean z2 = true;
        if (actionMasked == 0) {
            this.mQsTracking = true;
            traceQsJank(true, false);
            this.mInitialTouchY = y;
            this.mInitialTouchX = x;
            onQsExpansionStarted();
            this.mInitialHeightOnTouch = this.mQsExpansionHeight;
            initVelocityTracker();
            trackMovement(motionEvent);
            return;
        }
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                setQsExpansion(this.mInitialHeightOnTouch + f);
                if (f >= getFalsingThreshold()) {
                    this.mQsTouchAboveFalsingThreshold = true;
                }
                trackMovement(motionEvent);
                return;
            } else if (actionMasked != 3) {
                if (actionMasked != 6 || this.mTrackingPointer != (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                    return;
                }
                if (motionEvent.getPointerId(0) == pointerId) {
                    i = 1;
                }
                float y2 = motionEvent.getY(i);
                float x2 = motionEvent.getX(i);
                this.mTrackingPointer = motionEvent.getPointerId(i);
                this.mInitialHeightOnTouch = this.mQsExpansionHeight;
                this.mInitialTouchY = y2;
                this.mInitialTouchX = x2;
                return;
            }
        }
        this.mQsTracking = false;
        this.mTrackingPointer = -1;
        trackMovement(motionEvent);
        if (computeQsExpansionFraction() != 0.0f || y >= this.mInitialTouchY) {
            if (motionEvent.getActionMasked() == 3) {
                z = true;
            }
            flingQsWithCurrentVelocity(y, z);
        } else {
            if (motionEvent.getActionMasked() != 3) {
                z2 = false;
            }
            traceQsJank(false, z2);
        }
        VelocityTracker velocityTracker = this.mQsVelocityTracker;
        if (velocityTracker == null) {
            return;
        }
        velocityTracker.recycle();
        this.mQsVelocityTracker = null;
    }

    private int getFalsingThreshold() {
        return (int) (this.mQsFalsingThreshold * (this.mStatusBar.isWakeUpComingFromTouch() ? 1.5f : 1.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOverScrolling(boolean z) {
        this.mStackScrollerOverscrolling = z;
        QS qs = this.mQs;
        if (qs == null) {
            return;
        }
        qs.setOverscrolling(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onQsExpansionStarted() {
        onQsExpansionStarted(0);
    }

    protected void onQsExpansionStarted(int i) {
        cancelQsAnimation();
        cancelHeightAnimator();
        float f = this.mQsExpansionHeight - i;
        setQsExpansion(f);
        requestPanelHeightUpdate();
        this.mNotificationStackScrollLayoutController.checkSnoozeLeavebehind();
        if (f == 0.0f) {
            this.mStatusBar.requestFaceAuth(false);
        }
    }

    @VisibleForTesting
    void setQsExpanded(boolean z) {
        if (this.mQsExpanded != z) {
            this.mQsExpanded = z;
            updateQsState();
            requestPanelHeightUpdate();
            this.mFalsingCollector.setQsExpanded(z);
            this.mStatusBar.setQsExpanded(z);
            this.mNotificationContainerParent.setQsExpanded(z);
            this.mPulseExpansionHandler.setQsExpanded(z);
            this.mKeyguardBypassController.setQSExpanded(z);
            this.mStatusBarKeyguardViewManager.setQsExpanded(z);
            this.mLockIconViewController.setQsExpanded(z);
            this.mPrivacyDotViewController.setQsExpanded(z);
            if (!z) {
                this.mCanQsCollapseForLand = true;
            }
            this.mUpdateMonitor.setPanelDown(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeAnimateBottomAreaAlpha() {
        this.mBottomAreaShadeAlphaAnimator.cancel();
        if (this.mBarState == 2) {
            this.mBottomAreaShadeAlphaAnimator.setFloatValues(this.mBottomAreaShadeAlpha, 0.0f);
            this.mBottomAreaShadeAlphaAnimator.start();
            return;
        }
        this.mBottomAreaShadeAlpha = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateKeyguardStatusBarOut() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mKeyguardStatusBar.getAlpha(), 0.0f);
        ofFloat.addUpdateListener(this.mStatusBarAnimateAlphaListener);
        ofFloat.setStartDelay(this.mKeyguardStateController.isKeyguardFadingAway() ? this.mKeyguardStateController.getKeyguardFadingAwayDelay() : 0L);
        ofFloat.setDuration(this.mKeyguardStateController.isKeyguardFadingAway() ? this.mKeyguardStateController.getShortenedFadingAwayDuration() : 360L);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                NotificationPanelViewController.this.mAnimateKeyguardStatusBarInvisibleEndRunnable.run();
            }
        });
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateKeyguardStatusBarIn(long j) {
        this.mKeyguardStatusBar.setVisibility(0);
        this.mKeyguardStatusBar.setAlpha(0.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(this.mStatusBarAnimateAlphaListener);
        ofFloat.setDuration(j);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setKeyguardBottomAreaVisibility(int i, boolean z) {
        this.mKeyguardBottomArea.animate().cancel();
        if (z) {
            this.mKeyguardBottomArea.animate().alpha(0.0f).setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).setDuration(this.mKeyguardStateController.getShortenedFadingAwayDuration()).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(this.mAnimateKeyguardBottomAreaInvisibleEndRunnable).start();
        } else if (i == 1 || i == 2) {
            this.mKeyguardBottomArea.setVisibility(0);
            this.mKeyguardBottomArea.setAlpha(1.0f);
        } else {
            this.mKeyguardBottomArea.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateQsState() {
        this.mNotificationStackScrollLayoutController.setQsExpanded(this.mQsExpanded);
        this.mNotificationStackScrollLayoutController.setScrollingEnabled(this.mBarState != 1 && (!this.mQsExpanded || this.mQsExpansionFromOverscroll || this.mShouldUseSplitNotificationShade));
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null && this.mQsExpanded && !this.mStackScrollerOverscrolling) {
            keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(true);
        }
        QS qs = this.mQs;
        if (qs == null) {
            return;
        }
        qs.setExpanded(this.mQsExpanded);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setQsExpansion(float f) {
        float min = Math.min(Math.max(f, this.mQsMinExpansionHeight), this.mQsMaxExpansionHeight);
        int i = this.mQsMaxExpansionHeight;
        this.mQsFullyExpanded = min == ((float) i) && i != 0;
        if (!this.mHasQSLayout && isFullyCollapsed()) {
            this.mQsFullyExpanded = false;
        }
        int i2 = this.mQsMinExpansionHeight;
        if (min > i2 && !this.mQsExpanded && !this.mStackScrollerOverscrolling && !this.mDozing) {
            setQsExpanded(true);
        } else if (min <= i2 && this.mQsExpanded) {
            setQsExpanded(false);
        }
        this.mQsExpansionHeight = min;
        updateQsExpansion();
        requestScrollerTopPaddingUpdate(false);
        updateHeaderKeyguardAlpha();
        int i3 = this.mBarState;
        if (i3 == 2 || i3 == 1) {
            updateKeyguardBottomAreaAlpha();
            positionClockAndNotifications();
            updateBigClockAlpha();
        }
        if (this.mAccessibilityManager.isEnabled()) {
            this.mView.setAccessibilityPaneTitle(determineAccessibilityPaneTitle());
        }
        if (!this.mFalsingManager.isUnlockingDisabled() && this.mQsFullyExpanded && this.mFalsingCollector.shouldEnforceBouncer()) {
            this.mStatusBar.executeRunnableDismissingKeyguard(null, null, false, true, false);
        }
        for (int i4 = 0; i4 < this.mExpansionListeners.size(); i4++) {
            PanelExpansionListener panelExpansionListener = this.mExpansionListeners.get(i4);
            int i5 = this.mQsMaxExpansionHeight;
            panelExpansionListener.onQsExpansionChanged(i5 != 0 ? this.mQsExpansionHeight / i5 : 0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateQsExpansion() {
        if (this.mQs == null) {
            return;
        }
        float computeQsExpansionFraction = computeQsExpansionFraction();
        if (!this.mShouldUseSplitNotificationShade || this.mIsExpandFling || (!isCollapsing() && this.mCanQsCollapseForLand)) {
            this.mQs.setQsExpansion(computeQsExpansionFraction, getHeaderTranslation());
            this.mMediaHierarchyManager.setQsExpansion(this.mShouldUseSplitNotificationShade ? 1.0f : computeQsExpansionFraction);
        }
        this.mScrimController.setQsPosition(computeQsExpansionFraction, calculateQsBottomPosition(computeQsExpansionFraction));
        setQSClippingBounds();
        this.mNotificationStackScrollLayoutController.setQsExpansionFraction(computeQsExpansionFraction);
        this.mDepthController.setQsPanelExpansion(computeQsExpansionFraction);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStackYChanged(boolean z) {
        if (this.mQs != null) {
            if (z) {
                animateNextNotificationBounds(360L, 0L);
                this.mNotificationBoundsAnimationDelay = 0L;
            }
            setQSClippingBounds();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNotificationScrolled(int i) {
        updateQSExpansionEnabledAmbient();
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void setIsShadeOpening(boolean z) {
        this.mAmbientState.setIsShadeOpening(z);
        updateQSExpansionEnabledAmbient();
    }

    private void updateQSExpansionEnabledAmbient() {
        float topPadding = this.mAmbientState.getTopPadding() - this.mQuickQsOffsetHeight;
        boolean z = true;
        if (!this.mShouldUseSplitNotificationShade && this.mAmbientState.getScrollY() > topPadding) {
            z = false;
        }
        this.mQsExpansionEnabledAmbient = z;
        setQsExpansionEnabled();
    }

    private void setQSClippingBounds() {
        int min;
        int height;
        int right;
        int calculateQsBottomPosition = calculateQsBottomPosition(computeQsExpansionFraction());
        int i = 0;
        boolean z = computeQsExpansionFraction() > 0.0f || calculateQsBottomPosition > 0;
        if (!this.mShouldUseSplitNotificationShade) {
            if (this.mTransitioningToFullShadeProgress > 0.0f) {
                calculateQsBottomPosition = this.mTransitionToFullShadeQSPosition;
            } else {
                float qSEdgePosition = getQSEdgePosition();
                if (!isOnKeyguard()) {
                    calculateQsBottomPosition = (int) qSEdgePosition;
                } else if (!this.mKeyguardBypassController.getBypassEnabled()) {
                    calculateQsBottomPosition = (int) Math.min(calculateQsBottomPosition, qSEdgePosition);
                }
            }
            min = (int) (calculateQsBottomPosition + this.mOverStretchAmount);
            float f = this.mMinFraction;
            if (f > 0.0f && f < 1.0f) {
                float expandedFraction = getExpandedFraction();
                float f2 = this.mMinFraction;
                min = (int) (min * MathUtils.saturate(((expandedFraction - f2) / (1.0f - f2)) / f2));
            }
            height = getView().getBottom();
            right = getView().getRight() + this.mDisplayRightInset;
        } else {
            min = Math.min(calculateQsBottomPosition, this.mSplitShadeNotificationsTopPadding);
            height = this.mNotificationStackScrollLayoutController.getHeight();
            i = this.mNotificationStackScrollLayoutController.getLeft();
            right = this.mNotificationStackScrollLayoutController.getRight();
        }
        int i2 = height;
        applyQSClippingBounds(i, Math.min(min, i2), right, i2, z);
    }

    private void applyQSClippingBounds(int i, int i2, int i3, int i4, final boolean z) {
        if (!this.mAnimateNextNotificationBounds || this.mKeyguardStatusAreaClipBounds.isEmpty()) {
            if (this.mQsClippingAnimation != null) {
                this.mQsClippingAnimationEndBounds.set(i, i2, i3, i4);
            } else {
                applyQSClippingImmediately(i, i2, i3, i4, z);
            }
        } else {
            this.mQsClippingAnimationEndBounds.set(i, i2, i3, i4);
            Rect rect = this.mKeyguardStatusAreaClipBounds;
            final int i5 = rect.left;
            final int i6 = rect.top;
            final int i7 = rect.right;
            final int i8 = rect.bottom;
            ValueAnimator valueAnimator = this.mQsClippingAnimation;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mQsClippingAnimation = ofFloat;
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mQsClippingAnimation.setDuration(this.mNotificationBoundsAnimationDuration);
            this.mQsClippingAnimation.setStartDelay(this.mNotificationBoundsAnimationDelay);
            this.mQsClippingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    NotificationPanelViewController.this.lambda$applyQSClippingBounds$8(i5, i6, i7, i8, z, valueAnimator2);
                }
            });
            this.mQsClippingAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.12
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    NotificationPanelViewController.this.mQsClippingAnimation = null;
                    NotificationPanelViewController.this.mIsQsTranslationResetAnimator = false;
                    NotificationPanelViewController.this.mIsPulseExpansionResetAnimator = false;
                }
            });
            this.mQsClippingAnimation.start();
        }
        this.mAnimateNextNotificationBounds = false;
        this.mNotificationBoundsAnimationDelay = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyQSClippingBounds$8(int i, int i2, int i3, int i4, boolean z, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        applyQSClippingImmediately((int) MathUtils.lerp(i, this.mQsClippingAnimationEndBounds.left, animatedFraction), (int) MathUtils.lerp(i2, this.mQsClippingAnimationEndBounds.top, animatedFraction), (int) MathUtils.lerp(i3, this.mQsClippingAnimationEndBounds.right, animatedFraction), (int) MathUtils.lerp(i4, this.mQsClippingAnimationEndBounds.bottom, animatedFraction), z);
    }

    private void applyQSClippingImmediately(int i, int i2, int i3, int i4, boolean z) {
        int i5;
        boolean z2;
        int i6;
        float max;
        int i7 = this.mScrimCornerRadius;
        if (!this.mShouldUseSplitNotificationShade) {
            this.mKeyguardStatusAreaClipBounds.set(i, i2, i3, i4);
            float f = this.mRecordingController.isRecording() ? 0.0f : this.mScreenCornerRadius;
            int i8 = this.mScrimCornerRadius;
            i6 = i2 - this.mKeyguardStatusBar.getTop();
            i5 = (int) MathUtils.lerp(f, i8, Math.min(i2 / i8, 1.0f));
            z2 = z;
        } else {
            i5 = i7;
            z2 = false;
            i6 = 0;
        }
        if (this.mQs != null) {
            boolean isExpanding = this.mPulseExpansionHandler.isExpanding();
            if (this.mTransitioningToFullShadeProgress <= 0.0f && !isExpanding && (this.mQsClippingAnimation == null || (!this.mIsQsTranslationResetAnimator && !this.mIsPulseExpansionResetAnimator))) {
                max = 0.0f;
            } else if (isExpanding || this.mIsPulseExpansionResetAnimator) {
                max = Math.max(0.0f, (i2 - this.mQs.getHeader().getHeight()) / 2.0f);
            } else {
                max = (i2 - this.mQs.getHeader().getHeight()) * 0.175f;
            }
            this.mQsTranslationForFullShadeTransition = max;
            updateQsFrameTranslation();
            float translationY = this.mQsFrame.getTranslationY();
            int i9 = (int) (i2 - translationY);
            this.mQsClipTop = i9;
            int i10 = (int) (i4 - translationY);
            this.mQsClipBottom = i10;
            this.mQsVisible = z;
            this.mQs.setFancyClipping(i9, i10, i5, z && !this.mShouldUseSplitNotificationShade);
        }
        this.mKeyguardStatusViewController.setClipBounds(z2 ? this.mKeyguardStatusAreaClipBounds : null);
        if (!z && this.mShouldUseSplitNotificationShade) {
            this.mScrimController.setNotificationsBounds(0.0f, 0.0f, 0.0f, 0.0f);
        } else {
            this.mScrimController.setNotificationsBounds(i, i2, i3, i4);
        }
        this.mScrimController.setScrimCornerRadius(i5);
        this.mKeyguardStatusBar.setTopClipping(i6);
        this.mNotificationStackScrollLayoutController.setRoundedClippingBounds(i - this.mNotificationStackScrollLayoutController.getLeft(), i2 - this.mNotificationStackScrollLayoutController.getTop(), i3 - this.mNotificationStackScrollLayoutController.getLeft(), i4 - this.mNotificationStackScrollLayoutController.getTop(), i5, this.mShouldUseSplitNotificationShade ? i5 : 0);
    }

    private float getQSEdgePosition() {
        return Math.max(this.mQuickQsOffsetHeight * this.mAmbientState.getExpansionFraction(), this.mAmbientState.getStackY() - this.mAmbientState.getScrollY());
    }

    private int calculateQsBottomPosition(float f) {
        if (this.mTransitioningToFullShadeProgress > 0.0f) {
            return this.mTransitionToFullShadeQSPosition;
        }
        int headerTranslation = ((int) getHeaderTranslation()) + this.mQs.getQsMinExpansionHeight();
        return ((double) f) != 0.0d ? (int) MathUtils.lerp(headerTranslation, this.mQs.getDesiredHeight(), f) : headerTranslation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String determineAccessibilityPaneTitle() {
        QS qs = this.mQs;
        if (qs != null && qs.isCustomizing()) {
            return this.mResources.getString(R$string.accessibility_desc_quick_settings_edit);
        }
        if (this.mQsExpansionHeight != 0.0f && this.mQsFullyExpanded) {
            return this.mResources.getString(R$string.accessibility_desc_quick_settings);
        }
        if (this.mBarState == 1) {
            return this.mResources.getString(R$string.accessibility_desc_lock_screen);
        }
        return this.mResources.getString(R$string.accessibility_desc_notification_shade);
    }

    private float calculateNotificationsTopPadding() {
        if (this.mShouldUseSplitNotificationShade && !this.mKeyguardShowing) {
            if (this.mNotificationStackScrollLayoutController.isShowingEmptyShadeView()) {
                return (this.mNotificationStackScrollLayoutController.getView().getHeight() - this.mSplitShadeNotificationsTopPadding) / 2;
            }
            return this.mSplitShadeNotificationsTopPadding;
        }
        boolean z = this.mKeyguardShowing;
        if (z && (this.mQsExpandImmediate || (this.mIsExpanding && this.mQsExpandedWhenExpandingStarted))) {
            int keyguardNotificationStaticPadding = getKeyguardNotificationStaticPadding();
            int i = this.mQsMaxExpansionHeight;
            if (this.mBarState == 1) {
                i = Math.max(keyguardNotificationStaticPadding, i);
            }
            return (int) MathUtils.lerp(this.mQsMinExpansionHeight, i, getExpandedFraction());
        }
        ValueAnimator valueAnimator = this.mQsSizeChangeAnimator;
        if (valueAnimator != null) {
            return Math.max(((Integer) valueAnimator.getAnimatedValue()).intValue(), getKeyguardNotificationStaticPadding());
        }
        if (z) {
            return MathUtils.lerp(getKeyguardNotificationStaticPadding(), this.mQsMaxExpansionHeight, computeQsExpansionFraction());
        }
        return this.mQsExpansionHeight;
    }

    private int getKeyguardNotificationStaticPadding() {
        if (!this.mKeyguardShowing) {
            return 0;
        }
        if (!this.mKeyguardBypassController.getBypassEnabled()) {
            return this.mClockPositionResult.stackScrollerPadding;
        }
        int i = this.mHeadsUpInset;
        return !this.mNotificationStackScrollLayoutController.isPulseExpanding() ? i : (int) MathUtils.lerp(i, this.mClockPositionResult.stackScrollerPadding, this.mNotificationStackScrollLayoutController.calculateAppearFractionBypass());
    }

    protected void requestScrollerTopPaddingUpdate(boolean z) {
        this.mNotificationStackScrollLayoutController.updateTopPadding(calculateNotificationsTopPadding(), z);
        if (!this.mKeyguardShowing || !this.mKeyguardBypassController.getBypassEnabled()) {
            return;
        }
        updateQsExpansion();
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x007c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setTransitionToFullShadeAmount(float f, boolean z, long j) {
        float f2;
        if (z && !this.mShouldUseSplitNotificationShade) {
            animateNextNotificationBounds(448L, j);
            this.mIsQsTranslationResetAnimator = this.mQsTranslationForFullShadeTransition > 0.0f;
        }
        if (f > 0.0f) {
            if (this.mNotificationStackScrollLayoutController.getVisibleNotificationCount() == 0 && !this.mMediaDataManager.hasActiveMedia()) {
                if (this.mQs != null) {
                    f2 = (getQSEdgePosition() - this.mNotificationStackScrollLayoutController.getTopPadding()) + this.mQs.getHeader().getHeight();
                }
            } else {
                f2 = getQSEdgePosition() + this.mNotificationStackScrollLayoutController.getFullShadeTransitionInset();
                if (isOnKeyguard()) {
                    f2 -= this.mLockscreenNotificationQSPadding;
                }
            }
            float interpolation = Interpolators.FAST_OUT_SLOW_IN.getInterpolation(MathUtils.saturate(f / this.mDistanceForQSFullShadeTransition));
            this.mTransitioningToFullShadeProgress = interpolation;
            int lerp = (int) MathUtils.lerp(0.0f, f2, interpolation);
            if (this.mTransitioningToFullShadeProgress > 0.0f) {
                lerp = Math.max(1, lerp);
            }
            this.mTransitionToFullShadeQSPosition = lerp;
            updateQsExpansion();
        }
        f2 = 0.0f;
        float interpolation2 = Interpolators.FAST_OUT_SLOW_IN.getInterpolation(MathUtils.saturate(f / this.mDistanceForQSFullShadeTransition));
        this.mTransitioningToFullShadeProgress = interpolation2;
        int lerp2 = (int) MathUtils.lerp(0.0f, f2, interpolation2);
        if (this.mTransitioningToFullShadeProgress > 0.0f) {
        }
        this.mTransitionToFullShadeQSPosition = lerp2;
        updateQsExpansion();
    }

    public void onPulseExpansionFinished() {
        animateNextNotificationBounds(448L, 0L);
        this.mIsPulseExpansionResetAnimator = true;
    }

    public void setKeyguardOnlyContentAlpha(float f) {
        float interpolation = Interpolators.ALPHA_IN.getInterpolation(f);
        this.mKeyguardOnlyContentAlpha = interpolation;
        if (this.mBarState == 1) {
            this.mBottomAreaShadeAlpha = interpolation;
            updateKeyguardBottomAreaAlpha();
        }
        updateClock();
    }

    private void trackMovement(MotionEvent motionEvent) {
        VelocityTracker velocityTracker = this.mQsVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.addMovement(motionEvent);
        }
    }

    private void initVelocityTracker() {
        VelocityTracker velocityTracker = this.mQsVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.mQsVelocityTracker = VelocityTracker.obtain();
    }

    private float getCurrentQSVelocity() {
        VelocityTracker velocityTracker = this.mQsVelocityTracker;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000);
        return this.mQsVelocityTracker.getYVelocity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelQsAnimation() {
        ValueAnimator valueAnimator = this.mQsExpansionAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void flingSettings(float f, int i) {
        flingSettings(f, i, null, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x001c  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void flingSettings(float f, int i, final Runnable runnable, boolean z) {
        int i2;
        float f2;
        float f3;
        boolean z2;
        boolean z3 = true;
        if (i == 0) {
            i2 = this.mQsMaxExpansionHeight;
        } else if (i == 1) {
            i2 = this.mQsMinExpansionHeight;
        } else {
            QS qs = this.mQs;
            if (qs != null) {
                qs.closeDetail();
            }
            f2 = 0.0f;
            f3 = this.mQsExpansionHeight;
            if (f2 != f3) {
                if (runnable != null) {
                    runnable.run();
                }
                if (i == 0) {
                    z3 = false;
                }
                traceQsJank(false, z3);
                return;
            }
            boolean z4 = i == 0;
            if ((f <= 0.0f || z4) && (f >= 0.0f || !z4)) {
                z2 = false;
            } else {
                f = 0.0f;
                z2 = true;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(f3, f2);
            if (z) {
                ofFloat.setInterpolator(Interpolators.TOUCH_RESPONSE);
                ofFloat.setDuration(z4 ? 300L : 225L);
            } else {
                this.mFlingAnimationUtils.apply(ofFloat, this.mQsExpansionHeight, f2, f);
            }
            if (z2) {
                ofFloat.setDuration(225L);
            }
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    NotificationPanelViewController.this.lambda$flingSettings$9(valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.13
                private boolean mIsCanceled;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    NotificationPanelViewController.this.notifyExpandingStarted();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mIsCanceled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    NotificationPanelViewController.this.mQSAnimatingHiddenFromCollapsed = false;
                    NotificationPanelViewController.this.mAnimatingQS = false;
                    NotificationPanelViewController.this.notifyExpandingFinished();
                    NotificationPanelViewController.this.mNotificationStackScrollLayoutController.resetCheckSnoozeLeavebehind();
                    NotificationPanelViewController.this.mQsExpansionAnimator = null;
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                    NotificationPanelViewController.this.traceQsJank(false, this.mIsCanceled);
                }
            });
            this.mAnimatingQS = true;
            ofFloat.start();
            this.mQsExpansionAnimator = ofFloat;
            this.mQsAnimatorExpand = z4;
            if (computeQsExpansionFraction() != 0.0f || f2 != 0.0f) {
                z3 = false;
            }
            this.mQSAnimatingHiddenFromCollapsed = z3;
            return;
        }
        f2 = i2;
        f3 = this.mQsExpansionHeight;
        if (f2 != f3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$flingSettings$9(ValueAnimator valueAnimator) {
        setQsExpansion(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldQuickSettingsIntercept(float f, float f2, float f3) {
        QS qs;
        if (!isQsExpansionEnabled() || this.mCollapsedOnDown || (this.mKeyguardShowing && this.mKeyguardBypassController.getBypassEnabled())) {
            return false;
        }
        View header = (this.mKeyguardShowing || (qs = this.mQs) == null) ? this.mKeyguardStatusBar : qs.getHeader();
        this.mQsInterceptRegion.set((int) this.mQsFrame.getX(), header.getTop(), ((int) this.mQsFrame.getX()) + this.mQsFrame.getWidth(), header.getBottom());
        this.mStatusBarTouchableRegionManager.updateRegionForNotch(this.mQsInterceptRegion);
        boolean contains = this.mQsInterceptRegion.contains((int) f, (int) f2);
        if (this.mShouldUseSplitNotificationShade && this.mLastOrientation == 2 && f3 > 0.0f && this.mQsExpansionHeight == this.mQsMaxExpansionHeight) {
            return false;
        }
        return this.mQsExpanded ? contains || (f3 < 0.0f && isInQsArea(f, f2)) : contains;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected boolean canCollapsePanelOnTouch() {
        if ((isInSettings() || this.mBarState != 1) && !this.mNotificationStackScrollLayoutController.isScrolledToBottom()) {
            return !this.mShouldUseSplitNotificationShade && (isInSettings() || this.mIsPanelCollapseOnQQS);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public int getMaxPanelHeight() {
        int calculatePanelHeightQsExpanded;
        int i = this.mStatusBarMinHeight;
        if (this.mBarState != 1 && this.mNotificationStackScrollLayoutController.getNotGoneChildCount() == 0) {
            i = Math.max(i, this.mQsMinExpansionHeight);
        }
        if (this.mQsExpandImmediate || this.mQsExpanded || ((this.mIsExpanding && this.mQsExpandedWhenExpandingStarted) || this.mPulsing)) {
            calculatePanelHeightQsExpanded = calculatePanelHeightQsExpanded();
        } else {
            calculatePanelHeightQsExpanded = calculatePanelHeightShade();
        }
        int max = Math.max(i, calculatePanelHeightQsExpanded);
        if (max == 0 || Float.isNaN(max)) {
            String str = PanelViewController.TAG;
            Log.wtf(str, "maxPanelHeight is invalid. mOverExpansion: " + this.mOverExpansion + ", calculatePanelHeightQsExpanded: " + calculatePanelHeightQsExpanded() + ", calculatePanelHeightShade: " + calculatePanelHeightShade() + ", mStatusBarMinHeight = " + this.mStatusBarMinHeight + ", mQsMinExpansionHeight = " + this.mQsMinExpansionHeight);
        }
        return max;
    }

    public boolean isInSettings() {
        return this.mQsExpanded;
    }

    public boolean isExpanding() {
        return this.mIsExpanding;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected void onHeightUpdated(float f) {
        float calculatePanelHeightQsExpanded;
        if ((!this.mQsExpanded || this.mQsExpandImmediate || (this.mIsExpanding && this.mQsExpandedWhenExpandingStarted)) && this.mStackScrollerMeasuringPass <= 2) {
            positionClockAndNotifications();
        }
        if (this.mQsExpandImmediate || (this.mQsExpanded && !this.mQsTracking && this.mQsExpansionAnimator == null && !this.mQsExpansionFromOverscroll)) {
            if (this.mKeyguardShowing) {
                calculatePanelHeightQsExpanded = f / getMaxPanelHeight();
            } else {
                float intrinsicPadding = this.mNotificationStackScrollLayoutController.getIntrinsicPadding() + this.mNotificationStackScrollLayoutController.getLayoutMinHeight();
                calculatePanelHeightQsExpanded = (f - intrinsicPadding) / (calculatePanelHeightQsExpanded() - intrinsicPadding);
            }
            int i = this.mQsMinExpansionHeight;
            setQsExpansion(i + (calculatePanelHeightQsExpanded * (this.mQsMaxExpansionHeight - i)));
        }
        updateExpandedHeight(f);
        updateHeader();
        updateNotificationTranslucency();
        updatePanelExpanded();
        updateGestureExclusionRect();
        updateStatusBarAlpha();
        updateQsFrameAlpha();
    }

    private void updatePanelExpanded() {
        boolean z = !isFullyCollapsed() || this.mExpectingSynthesizedDown;
        if (this.mPanelExpanded != z) {
            this.mHeadsUpManager.setIsPanelExpanded(z);
            this.mStatusBarTouchableRegionManager.setPanelExpanded(z);
            this.mStatusBar.setPanelExpanded(z);
            this.mPanelExpanded = z;
        }
    }

    private int calculatePanelHeightShade() {
        int height = this.mNotificationStackScrollLayoutController.getHeight() - this.mNotificationStackScrollLayoutController.getEmptyBottomMargin();
        return this.mBarState == 1 ? Math.max(height, this.mClockPositionAlgorithm.getLockscreenStatusViewHeight() + this.mNotificationStackScrollLayoutController.getIntrinsicContentHeight()) : height;
    }

    private int calculatePanelHeightQsExpanded() {
        float height = (this.mNotificationStackScrollLayoutController.getHeight() - this.mNotificationStackScrollLayoutController.getEmptyBottomMargin()) - this.mNotificationStackScrollLayoutController.getTopPadding();
        if (this.mNotificationStackScrollLayoutController.getNotGoneChildCount() == 0 && this.mNotificationStackScrollLayoutController.isShowingEmptyShadeView()) {
            height = this.mNotificationStackScrollLayoutController.getEmptyShadeViewHeight();
        }
        int i = this.mQsMaxExpansionHeight;
        ValueAnimator valueAnimator = this.mQsSizeChangeAnimator;
        if (valueAnimator != null) {
            i = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        }
        float max = Math.max(i, this.mBarState == 1 ? this.mClockPositionResult.stackScrollerPadding : 0) + height + this.mNotificationStackScrollLayoutController.getTopPaddingOverflow();
        if (max > this.mNotificationStackScrollLayoutController.getHeight()) {
            max = Math.max(i + this.mNotificationStackScrollLayoutController.getLayoutMinHeight(), this.mNotificationStackScrollLayoutController.getHeight());
        }
        return (int) max;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNotificationTranslucency() {
        float fadeoutAlpha = (!this.mClosingWithAlphaFadeOut || this.mExpandingFromHeadsUp || this.mHeadsUpManager.hasPinnedHeadsUp()) ? 1.0f : getFadeoutAlpha();
        if (this.mBarState == 1 && !this.mHintAnimationRunning && !this.mKeyguardBypassController.getBypassEnabled()) {
            fadeoutAlpha *= this.mClockPositionResult.clockAlpha;
        }
        this.mNotificationStackScrollLayoutController.setAlpha(fadeoutAlpha);
    }

    private float getFadeoutAlpha() {
        if (this.mQsMinExpansionHeight == 0) {
            return 1.0f;
        }
        return (float) Math.pow(Math.max(0.0f, Math.min(getExpandedHeight() / this.mQsMinExpansionHeight, 1.0f)), 0.75d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeader() {
        if (this.mBarState == 1) {
            updateHeaderKeyguardAlpha();
        }
        updateQsExpansion();
    }

    protected float getHeaderTranslation() {
        if (this.mBarState == 1 && !this.mKeyguardBypassController.getBypassEnabled()) {
            return -this.mQs.getQsMinExpansionHeight();
        }
        float calculateAppearFraction = this.mNotificationStackScrollLayoutController.calculateAppearFraction(this.mExpandedHeight);
        float f = this.mQsExpansionHeight;
        float f2 = -f;
        if (!this.mShouldUseSplitNotificationShade && this.mBarState == 0) {
            f2 = 0.175f * (-f);
        }
        if (this.mKeyguardBypassController.getBypassEnabled() && isOnKeyguard()) {
            calculateAppearFraction = this.mNotificationStackScrollLayoutController.calculateAppearFractionBypass();
            f2 = -this.mQs.getQsMinExpansionHeight();
        }
        return Math.min(0.0f, MathUtils.lerp(f2, 0.0f, Math.min(1.0f, calculateAppearFraction)));
    }

    private float getKeyguardContentsAlpha() {
        float expandedHeight;
        float height;
        if (this.mBarState == 1) {
            expandedHeight = getExpandedHeight();
            height = this.mKeyguardStatusBar.getHeight() + this.mNotificationsHeaderCollideDistance;
        } else {
            expandedHeight = getExpandedHeight();
            height = this.mKeyguardStatusBar.getHeight();
        }
        return (float) Math.pow(MathUtils.saturate(expandedHeight / height), 0.75d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeaderKeyguardAlpha() {
        if (!this.mKeyguardShowing) {
            return;
        }
        float min = Math.min(getKeyguardContentsAlpha(), 1.0f - Math.min(1.0f, computeQsExpansionFraction() * 2.0f)) * this.mKeyguardStatusBarAnimateAlpha * (1.0f - this.mKeyguardHeadsUpShowingAmount);
        this.mKeyguardStatusBar.setAlpha(min);
        int i = 0;
        boolean z = (this.mFirstBypassAttempt && this.mUpdateMonitor.shouldListenForFace()) || this.mDelayShowingKeyguardStatusBar;
        KeyguardStatusBarView keyguardStatusBarView = this.mKeyguardStatusBar;
        if (min == 0.0f || this.mDozing || z) {
            i = 4;
        }
        keyguardStatusBarView.setVisibility(i);
    }

    private void updateKeyguardBottomAreaAlpha() {
        float min = Math.min(MathUtils.map(isUnlockHintRunning() ? 0.0f : 0.95f, 1.0f, 0.0f, 1.0f, getExpandedFraction()), 1.0f - computeQsExpansionFraction()) * this.mBottomAreaShadeAlpha;
        this.mKeyguardBottomArea.setAffordanceAlpha(min);
        this.mKeyguardBottomArea.setImportantForAccessibility(min == 0.0f ? 4 : 0);
        View ambientIndicationContainer = this.mStatusBar.getAmbientIndicationContainer();
        if (ambientIndicationContainer != null) {
            ambientIndicationContainer.setAlpha(min);
        }
        this.mLockIconViewController.setAlpha(min);
    }

    private void updateBigClockAlpha() {
        this.mBigClockContainer.setAlpha(Math.min(MathUtils.map(isUnlockHintRunning() ? 0.0f : 0.95f, 1.0f, 0.0f, 1.0f, getExpandedFraction()), 1.0f - computeQsExpansionFraction()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void onExpandingStarted() {
        super.onExpandingStarted();
        this.mNotificationStackScrollLayoutController.onExpansionStarted();
        this.mIsExpanding = true;
        boolean z = this.mQsFullyExpanded;
        this.mQsExpandedWhenExpandingStarted = z;
        this.mMediaHierarchyManager.setCollapsingShadeFromQS(z && !this.mAnimatingQS);
        if (this.mQsExpanded) {
            onQsExpansionStarted();
        }
        QS qs = this.mQs;
        if (qs == null) {
            return;
        }
        qs.setHeaderListening(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void onExpandingFinished() {
        super.onExpandingFinished();
        this.mNotificationStackScrollLayoutController.onExpansionStopped();
        this.mHeadsUpManager.onExpandingFinished();
        this.mConversationNotificationManager.onNotificationPanelExpandStateChanged(isFullyCollapsed());
        this.mIsExpanding = false;
        this.mMediaHierarchyManager.setCollapsingShadeFromQS(false);
        this.mMediaHierarchyManager.setQsExpanded(this.mQsExpanded);
        if (isFullyCollapsed()) {
            DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.14
                @Override // java.lang.Runnable
                public void run() {
                    NotificationPanelViewController.this.setListening(false);
                }
            });
            this.mView.postOnAnimation(new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.15
                @Override // java.lang.Runnable
                public void run() {
                    NotificationPanelViewController.this.mView.getParent().invalidateChild(NotificationPanelViewController.this.mView, NotificationPanelViewController.M_DUMMY_DIRTY_RECT);
                }
            });
        } else {
            setListening(true);
        }
        this.mQsExpandImmediate = false;
        this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(false);
        this.mTwoFingerQsExpandPossible = false;
        notifyListenersTrackingHeadsUp(null);
        this.mExpandingFromHeadsUp = false;
        setPanelScrimMinFraction(0.0f);
    }

    private void notifyListenersTrackingHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        for (int i = 0; i < this.mTrackingHeadsUpListeners.size(); i++) {
            this.mTrackingHeadsUpListeners.get(i).accept(expandableNotificationRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListening(boolean z) {
        this.mKeyguardStatusBar.setListening(z);
        QS qs = this.mQs;
        if (qs == null) {
            return;
        }
        qs.setListening(z);
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void expand(boolean z) {
        super.expand(z);
        setListening(true);
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void setOverExpansion(float f) {
        if (f == this.mOverExpansion) {
            return;
        }
        super.setOverExpansion(f);
        updateQsFrameTranslation();
        this.mNotificationStackScrollLayoutController.setOverExpansion(f);
    }

    private void updateQsFrameTranslation() {
        this.mQsFrame.setTranslationY((this.mOverExpansion / 2.0f) + this.mQsTranslationForFullShadeTransition);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void onTrackingStarted() {
        this.mFalsingCollector.onTrackingStarted(!this.mKeyguardStateController.canDismissLockScreen());
        super.onTrackingStarted();
        if (this.mQsFullyExpanded) {
            this.mQsExpandImmediate = true;
            if (!this.mShouldUseSplitNotificationShade) {
                this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(true);
            }
        }
        int i = this.mBarState;
        if (i == 1 || i == 2) {
            this.mAffordanceHelper.animateHideLeftRightIcon();
        }
        this.mNotificationStackScrollLayoutController.onPanelTrackingStarted();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void onTrackingStopped(boolean z) {
        this.mFalsingCollector.onTrackingStopped();
        super.onTrackingStopped(z);
        if (z) {
            this.mNotificationStackScrollLayoutController.setOverScrollAmount(0.0f, true, true);
        }
        this.mNotificationStackScrollLayoutController.onPanelTrackingStopped();
        if (z) {
            int i = this.mBarState;
            if ((i != 1 && i != 2) || this.mHintAnimationRunning) {
                return;
            }
            this.mAffordanceHelper.reset(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxHeadsUpTranslation() {
        this.mNotificationStackScrollLayoutController.setHeadsUpBoundaries(getHeight(), this.mNavigationBarBottomHeight);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void startUnlockHintAnimation() {
        if (this.mPowerManager.isPowerSaveMode()) {
            onUnlockHintStarted();
            onUnlockHintFinished();
            return;
        }
        super.startUnlockHintAnimation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void onUnlockHintFinished() {
        super.onUnlockHintFinished();
        this.mNotificationStackScrollLayoutController.setUnlockHintRunning(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void onUnlockHintStarted() {
        super.onUnlockHintStarted();
        this.mNotificationStackScrollLayoutController.setUnlockHintRunning(true);
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected boolean shouldUseDismissingAnimation() {
        return this.mBarState != 0 && (this.mKeyguardStateController.canDismissLockScreen() || !isTracking());
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected boolean isTrackingBlocked() {
        return (this.mConflictingQsExpansionGesture && this.mQsExpanded) || this.mBlockingExpansionForCurrentTouch;
    }

    public boolean isQsExpanded() {
        return this.mQsExpanded;
    }

    public boolean isQsDetailShowing() {
        return this.mQs.isShowingDetail();
    }

    public void closeQsDetail() {
        this.mQs.closeDetail();
    }

    public boolean isLaunchTransitionFinished() {
        return this.mIsLaunchTransitionFinished;
    }

    public boolean isLaunchTransitionRunning() {
        return this.mIsLaunchTransitionRunning;
    }

    public void setLaunchTransitionEndRunnable(Runnable runnable) {
        this.mLaunchAnimationEndRunnable = runnable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDozingVisibilities(boolean z) {
        this.mKeyguardBottomArea.setDozing(this.mDozing, z);
        if (this.mDozing || !z) {
            return;
        }
        animateKeyguardStatusBarIn(360L);
    }

    public boolean isDozing() {
        return this.mDozing;
    }

    public void setQsScrimEnabled(boolean z) {
        boolean z2 = this.mQsScrimEnabled != z;
        this.mQsScrimEnabled = z;
        if (z2) {
            updateQsState();
        }
    }

    public void onScreenTurningOn() {
        this.mKeyguardStatusViewController.dozeTimeTick();
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected boolean onMiddleClicked() {
        int i = this.mBarState;
        if (i == 0) {
            this.mView.post(this.mPostCollapseRunnable);
            return false;
        } else if (i == 1) {
            if (!this.mDozingOnDown) {
                Log.d(PanelViewController.TAG, "onMiddleClicked: ");
                if (this.mUpdateMonitor.isFaceRecognitionEnable() && !this.mUpdateMonitor.isFaceCameraStarting() && !this.mUpdateMonitor.isFaceRecognitionSucceeded() && !((FaceRecognitionController) Dependency.get(FaceRecognitionController.class)).isFrozenMode()) {
                    this.mUpdateMonitor.requestFaceAuth(true);
                } else {
                    this.mLockscreenGestureLogger.write(188, 0, 0);
                    this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_LOCK_SHOW_HINT);
                    startUnlockHintAnimation();
                }
            }
            return true;
        } else {
            if (i == 2 && !this.mQsExpanded) {
                this.mStatusBarStateController.setState(1);
            }
            return true;
        }
    }

    public void setPanelAlpha(int i, boolean z) {
        if (this.mPanelAlpha != i) {
            this.mPanelAlpha = i;
            PropertyAnimator.setProperty(this.mView, this.mPanelAlphaAnimator, i, i == 255 ? this.mPanelAlphaInPropertiesAnimator : this.mPanelAlphaOutPropertiesAnimator, z);
        }
    }

    public void setPanelAlphaEndAction(Runnable runnable) {
        this.mPanelAlphaEndAction = runnable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKeyguardStatusBarForHeadsUp() {
        HeadsUpAppearanceController headsUpAppearanceController;
        boolean z = this.mKeyguardShowing && (headsUpAppearanceController = this.mHeadsUpAppearanceController) != null && headsUpAppearanceController.shouldBeVisible();
        if (this.mShowingKeyguardHeadsUp != z) {
            this.mShowingKeyguardHeadsUp = z;
            float f = 0.0f;
            if (this.mKeyguardShowing) {
                NotificationPanelView notificationPanelView = this.mView;
                AnimatableProperty animatableProperty = this.KEYGUARD_HEADS_UP_SHOWING_AMOUNT;
                if (z) {
                    f = 1.0f;
                }
                PropertyAnimator.setProperty(notificationPanelView, animatableProperty, f, KEYGUARD_HUN_PROPERTIES, true);
                return;
            }
            PropertyAnimator.applyImmediately(this.mView, this.KEYGUARD_HEADS_UP_SHOWING_AMOUNT, 0.0f);
        }
    }

    private void setKeyguardHeadsUpShowingAmount(float f) {
        this.mKeyguardHeadsUpShowingAmount = f;
        updateHeaderKeyguardAlpha();
    }

    private float getKeyguardHeadsUpShowingAmount() {
        return this.mKeyguardHeadsUpShowingAmount;
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        this.mHeadsUpAnimatingAway = z;
        this.mNotificationStackScrollLayoutController.setHeadsUpAnimatingAway(z);
        updateHeadsUpVisibility();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeadsUpVisibility() {
        ((PhoneStatusBarView) this.mBar).setHeadsUpVisible(this.mHeadsUpAnimatingAway || this.mHeadsUpPinnedMode);
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void setHeadsUpManager(HeadsUpManagerPhone headsUpManagerPhone) {
        super.setHeadsUpManager(headsUpManagerPhone);
        this.mHeadsUpTouchHelper = new HeadsUpTouchHelper(headsUpManagerPhone, this.mNotificationStackScrollLayoutController.getHeadsUpCallback(), this);
    }

    public void setTrackedHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        if (expandableNotificationRow != null) {
            notifyListenersTrackingHeadsUp(expandableNotificationRow);
            this.mExpandingFromHeadsUp = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void onClosingFinished() {
        super.onClosingFinished();
        resetHorizontalPanelPosition();
        setClosingWithAlphaFadeout(false);
        this.mMediaHierarchyManager.closeGuts();
    }

    private void setClosingWithAlphaFadeout(boolean z) {
        this.mClosingWithAlphaFadeOut = z;
        this.mNotificationStackScrollLayoutController.forceNoOverlappingRendering(z);
    }

    protected void updateHorizontalPanelPosition(float f) {
        if (this.mNotificationStackScrollLayoutController.getWidth() * 1.75f > this.mView.getWidth() || this.mShouldUseSplitNotificationShade) {
            resetHorizontalPanelPosition();
            return;
        }
        float width = this.mPositionMinSideMargin + (this.mNotificationStackScrollLayoutController.getWidth() / 2.0f);
        float width2 = (this.mView.getWidth() - this.mPositionMinSideMargin) - (this.mNotificationStackScrollLayoutController.getWidth() / 2.0f);
        if (Math.abs(f - (this.mView.getWidth() / 2)) < this.mNotificationStackScrollLayoutController.getWidth() / 4.0f) {
            f = this.mView.getWidth() / 2;
        }
        setHorizontalPanelTranslation(Math.min(width2, Math.max(width, f)) - (this.mNotificationStackScrollLayoutController.getLeft() + (this.mNotificationStackScrollLayoutController.getWidth() / 2.0f)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetHorizontalPanelPosition() {
        setHorizontalPanelTranslation(0.0f);
    }

    protected void setHorizontalPanelTranslation(float f) {
        this.mNotificationStackScrollLayoutController.setTranslationX(f);
        this.mQsFrame.setTranslationX(f);
        Runnable runnable = this.mVerticalTranslationListener;
        if (runnable != null) {
            runnable.run();
        }
    }

    protected void updateExpandedHeight(float f) {
        if (this.mTracking) {
            this.mNotificationStackScrollLayoutController.setExpandingVelocity(getCurrentExpandVelocity());
        }
        if (this.mKeyguardBypassController.getBypassEnabled() && isOnKeyguard()) {
            f = getMaxPanelHeight();
        }
        this.mNotificationStackScrollLayoutController.setExpandedHeight(f);
        updateKeyguardBottomAreaAlpha();
        updateBigClockAlpha();
        updateStatusBarIcons();
    }

    public boolean isFullWidth() {
        return this.mIsFullWidth;
    }

    private void updateStatusBarIcons() {
        boolean z = (isPanelVisibleBecauseOfHeadsUp() || isFullWidth()) && getExpandedHeight() < getOpeningHeight();
        if (z && isOnKeyguard()) {
            z = false;
        }
        if (z != this.mShowIconsWhenExpanded) {
            this.mShowIconsWhenExpanded = z;
            this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOnKeyguard() {
        return this.mBarState == 1;
    }

    public void setPanelScrimMinFraction(float f) {
        this.mBar.onPanelMinFractionChanged(f);
    }

    public void clearNotificationEffects() {
        this.mStatusBar.clearNotificationEffects();
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected boolean isPanelVisibleBecauseOfHeadsUp() {
        return (this.mHeadsUpManager.hasPinnedHeadsUp() || this.mHeadsUpAnimatingAway) && this.mBarState == 0;
    }

    public void launchCamera(boolean z, int i) {
        boolean z2 = true;
        if (i == 1) {
            this.mLastCameraLaunchSource = "power_double_tap";
        } else if (i == 0) {
            this.mLastCameraLaunchSource = "wiggle_gesture";
        } else if (i == 2) {
            this.mLastCameraLaunchSource = "lift_to_launch_ml";
        } else {
            this.mLastCameraLaunchSource = "lockscreen_affordance";
        }
        if (!isFullyCollapsed()) {
            setLaunchingAffordance(true);
        } else {
            z = false;
        }
        this.mAffordanceHasPreview = this.mKeyguardBottomArea.getRightPreview() != null;
        KeyguardAffordanceHelper keyguardAffordanceHelper = this.mAffordanceHelper;
        if (this.mView.getLayoutDirection() != 1) {
            z2 = false;
        }
        keyguardAffordanceHelper.launchAffordance(z, z2);
    }

    public void onAffordanceLaunchEnded() {
        setLaunchingAffordance(false);
    }

    private void setLaunchingAffordance(boolean z) {
        this.mLaunchingAffordance = z;
        this.mKeyguardAffordanceHelperCallback.getLeftIcon().setLaunchingAffordance(z);
        this.mKeyguardAffordanceHelperCallback.getRightIcon().setLaunchingAffordance(z);
        this.mKeyguardBypassController.setLaunchingAffordance(z);
    }

    public boolean isLaunchingAffordanceWithPreview() {
        return this.mLaunchingAffordance && this.mAffordanceHasPreview;
    }

    public boolean canCameraGestureBeLaunched() {
        ActivityInfo activityInfo;
        if (!this.mStatusBar.isCameraAllowedByAdmin()) {
            return false;
        }
        ResolveInfo resolveCameraIntent = this.mKeyguardBottomArea.resolveCameraIntent();
        String str = (resolveCameraIntent == null || (activityInfo = resolveCameraIntent.activityInfo) == null) ? null : activityInfo.packageName;
        if (str == null) {
            return false;
        }
        return (this.mBarState != 0 || !isForegroundApp(str)) && !this.mAffordanceHelper.isSwipingInProgress();
    }

    private boolean isForegroundApp(String str) {
        List<ActivityManager.RunningTaskInfo> runningTasks = this.mActivityManager.getRunningTasks(1);
        return !runningTasks.isEmpty() && str.equals(runningTasks.get(0).topActivity.getPackageName());
    }

    public boolean hideStatusBarIconsWhenExpanded() {
        if (this.mIsLaunchAnimationRunning) {
            return this.mHideIconsDuringLaunchAnimation;
        }
        HeadsUpAppearanceController headsUpAppearanceController = this.mHeadsUpAppearanceController;
        if (headsUpAppearanceController != null && headsUpAppearanceController.shouldBeVisible()) {
            return false;
        }
        return !isFullWidth() || !this.mShowIconsWhenExpanded;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10(int i) {
        if (i <= 0 || this.mQsFullyExpanded) {
            return;
        }
        expandWithQs();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.NotificationPanelViewController$16  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass16 implements FragmentHostManager.FragmentListener {
        AnonymousClass16() {
        }

        @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
        public void onFragmentViewCreated(String str, Fragment fragment) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            QS qs = (QS) fragment;
            notificationPanelViewController.mQs = qs;
            qs.setPanelView(notificationPanelViewController.mHeightListener);
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            notificationPanelViewController2.mQs.setExpandClickListener(notificationPanelViewController2.mOnClickListener);
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            notificationPanelViewController3.mQs.setHeaderClickable(notificationPanelViewController3.isQsExpansionEnabled());
            NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
            notificationPanelViewController4.mQs.setOverscrolling(notificationPanelViewController4.mStackScrollerOverscrolling);
            NotificationPanelViewController notificationPanelViewController5 = NotificationPanelViewController.this;
            notificationPanelViewController5.mQs.setTranslateWhileExpanding(notificationPanelViewController5.mShouldUseSplitNotificationShade);
            NotificationPanelViewController.this.mQs.getView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$16$$ExternalSyntheticLambda0
                @Override // android.view.View.OnLayoutChangeListener
                public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    NotificationPanelViewController.AnonymousClass16.this.lambda$onFragmentViewCreated$0(view, i, i2, i3, i4, i5, i6, i7, i8);
                }
            });
            NotificationPanelViewController.this.mQs.setCollapsedMediaVisibilityChangedListener(new Consumer() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$16$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    NotificationPanelViewController.AnonymousClass16.this.lambda$onFragmentViewCreated$1((Boolean) obj);
                }
            });
            NotificationPanelViewController.this.mLockscreenShadeTransitionController.setQS(NotificationPanelViewController.this.mQs);
            NotificationPanelViewController.this.mNotificationStackScrollLayoutController.setQsContainer((ViewGroup) NotificationPanelViewController.this.mQs.getView());
            NotificationPanelViewController notificationPanelViewController6 = NotificationPanelViewController.this;
            notificationPanelViewController6.mQs.setScrollListener(notificationPanelViewController6.mScrollListener);
            NotificationPanelViewController.this.updateQsExpansion();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFragmentViewCreated$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            if (i4 - i2 != i8 - i6) {
                NotificationPanelViewController.this.mHeightListener.onQsHeightChanged();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFragmentViewCreated$1(Boolean bool) {
            if (NotificationPanelViewController.this.mQs.getHeader().isShown() && !NotificationPanelViewController.this.mShouldUseSplitNotificationShade) {
                NotificationPanelViewController.this.animateNextNotificationBounds(360L, 0L);
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.animateNextTopPaddingChange();
            }
            if (NotificationPanelViewController.this.mQs.getHeader() instanceof QuickStatusBarHeader) {
                ((QuickStatusBarHeader) NotificationPanelViewController.this.mQs.getHeader()).updateMediaVisibility(bool.booleanValue());
            }
        }

        @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
        public void onFragmentViewDestroyed(String str, Fragment fragment) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            if (fragment == notificationPanelViewController.mQs) {
                notificationPanelViewController.mQs = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateNextNotificationBounds(long j, long j2) {
        this.mAnimateNextNotificationBounds = true;
        this.mNotificationBoundsAnimationDuration = j;
        this.mNotificationBoundsAnimationDelay = j2;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void setTouchAndAnimationDisabled(boolean z) {
        super.setTouchAndAnimationDisabled(z);
        if (z && this.mAffordanceHelper.isSwipingInProgress() && !this.mIsLaunchTransitionRunning) {
            this.mAffordanceHelper.reset(false);
        }
        this.mNotificationStackScrollLayoutController.setAnimationsEnabled(!z);
    }

    public void setDozing(boolean z, boolean z2, PointF pointF) {
        if (z == this.mDozing) {
            return;
        }
        this.mView.setDozing(z);
        this.mDozing = z;
        this.mNotificationStackScrollLayoutController.setDozing(z, z2, pointF);
        this.mKeyguardBottomArea.setDozing(this.mDozing, z2);
        if (z) {
            this.mBottomAreaShadeAlphaAnimator.cancel();
        }
        int i = this.mBarState;
        if (i == 1 || i == 2) {
            updateDozingVisibilities(z2);
        }
        this.mStatusBarStateController.setAndInstrumentDozeAmount(this.mView, z ? 1.0f : 0.0f, z2);
    }

    public void setPulsing(boolean z) {
        this.mPulsing = z;
        boolean z2 = !this.mDozeParameters.getDisplayNeedsBlanking() && this.mDozeParameters.getAlwaysOn();
        if (z2) {
            this.mAnimateNextPositionUpdate = true;
        }
        if (!this.mPulsing && !this.mDozing) {
            this.mAnimateNextPositionUpdate = false;
        }
        this.mNotificationStackScrollLayoutController.setPulsing(z, z2);
    }

    public void dozeTimeTick() {
        this.mLockIconViewController.dozeTimeTick();
        this.mKeyguardBottomArea.dozeTimeTick();
        this.mKeyguardStatusViewController.dozeTimeTick();
        if (this.mInterpolatedDarkAmount > 0.0f) {
            positionClockAndNotifications();
        }
    }

    public void setStatusAccessibilityImportance(int i) {
        this.mKeyguardStatusViewController.setStatusAccessibilityImportance(i);
    }

    public void setUserSetupComplete(boolean z) {
        this.mUserSetupComplete = z;
        this.mKeyguardBottomArea.setUserSetupComplete(z);
    }

    public void applyLaunchAnimationProgress(float f) {
        boolean z = ActivityLaunchAnimator.getProgress(f, 82L, 100L) == 0.0f;
        if (z != this.mHideIconsDuringLaunchAnimation) {
            this.mHideIconsDuringLaunchAnimation = z;
            if (z) {
                return;
            }
            this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, true);
        }
    }

    public void addTrackingHeadsUpListener(Consumer<ExpandableNotificationRow> consumer) {
        this.mTrackingHeadsUpListeners.add(consumer);
    }

    public void removeTrackingHeadsUpListener(Consumer<ExpandableNotificationRow> consumer) {
        this.mTrackingHeadsUpListeners.remove(consumer);
    }

    public void setVerticalTranslationListener(Runnable runnable) {
        this.mVerticalTranslationListener = runnable;
    }

    public void setHeadsUpAppearanceController(HeadsUpAppearanceController headsUpAppearanceController) {
        this.mHeadsUpAppearanceController = headsUpAppearanceController;
    }

    public void onBouncerPreHideAnimation() {
        KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchController;
        if (keyguardQsUserSwitchController != null) {
            int i = this.mBarState;
            keyguardQsUserSwitchController.setKeyguardQsUserSwitchVisibility(i, true, false, i);
        }
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            int i2 = this.mBarState;
            keyguardUserSwitcherController.setKeyguardUserSwitcherVisibility(i2, true, false, i2);
        }
    }

    public void blockExpansionForCurrentTouch() {
        this.mBlockingExpansionForCurrentTouch = this.mTracking;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("    gestureExclusionRect: " + calculateGestureExclusionRect() + " applyQSClippingImmediately: top(" + this.mQsClipTop + ") bottom(" + this.mQsClipBottom + ") qsVisible(" + this.mQsVisible);
        KeyguardStatusBarView keyguardStatusBarView = this.mKeyguardStatusBar;
        if (keyguardStatusBarView != null) {
            keyguardStatusBarView.dump(fileDescriptor, printWriter, strArr);
        }
    }

    public RemoteInputController.Delegate createRemoteInputDelegate() {
        return this.mNotificationStackScrollLayoutController.createDelegate();
    }

    public void updateNotificationViews(String str) {
        this.mNotificationStackScrollLayoutController.updateSectionBoundaries(str);
        this.mNotificationStackScrollLayoutController.updateFooter();
        this.mNotificationIconAreaController.updateNotificationIcons(createVisibleEntriesList());
    }

    private List<ListEntry> createVisibleEntriesList() {
        ArrayList arrayList = new ArrayList(this.mNotificationStackScrollLayoutController.getChildCount());
        for (int i = 0; i < this.mNotificationStackScrollLayoutController.getChildCount(); i++) {
            ExpandableView childAt = this.mNotificationStackScrollLayoutController.getChildAt(i);
            if (childAt instanceof ExpandableNotificationRow) {
                arrayList.add(((ExpandableNotificationRow) childAt).getEntry());
            }
        }
        return arrayList;
    }

    public void onUpdateRowStates() {
        this.mNotificationStackScrollLayoutController.onUpdateRowStates();
    }

    public boolean hasPulsingNotifications() {
        return this.mNotificationStackScrollLayoutController.getNotificationListContainer().hasPulsingNotifications();
    }

    public ActivatableNotificationView getActivatedChild() {
        return this.mNotificationStackScrollLayoutController.getActivatedChild();
    }

    public void setActivatedChild(ActivatableNotificationView activatableNotificationView) {
        this.mNotificationStackScrollLayoutController.setActivatedChild(activatableNotificationView);
    }

    public void runAfterAnimationFinished(Runnable runnable) {
        this.mNotificationStackScrollLayoutController.runAfterAnimationFinished(runnable);
    }

    public void initDependencies(StatusBar statusBar, NotificationShelfController notificationShelfController) {
        setStatusBar(statusBar);
        this.mNotificationStackScrollLayoutController.setShelfController(notificationShelfController);
        this.mNotificationShelfController = notificationShelfController;
        this.mLockscreenShadeTransitionController.bindController(notificationShelfController);
        updateMaxDisplayedNotifications(true);
    }

    public void setAlpha(float f) {
        this.mView.setAlpha(f);
    }

    public ViewPropertyAnimator fadeOut(long j, long j2, Runnable runnable) {
        return this.mView.animate().alpha(0.0f).setStartDelay(j).setDuration(j2).setInterpolator(Interpolators.ALPHA_OUT).withLayer().withEndAction(runnable);
    }

    public void resetViewGroupFade() {
        ViewGroupFadeHelper.reset(this.mView);
    }

    public void addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        this.mView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void removeOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        this.mView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public MyOnHeadsUpChangedListener getOnHeadsUpChangedListener() {
        return this.mOnHeadsUpChangedListener;
    }

    public int getHeight() {
        return this.mView.getHeight();
    }

    public void onThemeChanged() {
        this.mConfigurationListener.onThemeChanged();
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    /* renamed from: createLayoutChangeListener */
    public OnLayoutChangeListener mo1222createLayoutChangeListener() {
        return new OnLayoutChangeListener();
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected PanelViewController.TouchHandler createTouchHandler() {
        return new PanelViewController.TouchHandler() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.17
            private long mLastTouchDownTime = -1;

            @Override // com.android.systemui.statusbar.phone.PanelViewController.TouchHandler
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (!NotificationPanelViewController.this.mBlockTouches) {
                    if (NotificationPanelViewController.this.mQsFullyExpanded && NotificationPanelViewController.this.mQs.disallowPanelTouches()) {
                        return false;
                    }
                    NotificationPanelViewController.this.initDownStates(motionEvent);
                    if (NotificationPanelViewController.this.mStatusBar.isBouncerShowing()) {
                        return true;
                    }
                    if (NotificationPanelViewController.this.mBar.panelEnabled() && NotificationPanelViewController.this.mHeadsUpTouchHelper.onInterceptTouchEvent(motionEvent)) {
                        NotificationPanelViewController.this.mMetricsLogger.count("panel_open", 1);
                        NotificationPanelViewController.this.mMetricsLogger.count("panel_open_peek", 1);
                        return true;
                    }
                    NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                    if (!notificationPanelViewController.shouldQuickSettingsIntercept(notificationPanelViewController.mDownX, NotificationPanelViewController.this.mDownY, 0.0f) && NotificationPanelViewController.this.mPulseExpansionHandler.onInterceptTouchEvent(motionEvent)) {
                        return true;
                    }
                    if ((NotificationPanelViewController.this.mShouldUseSplitNotificationShade && NotificationPanelViewController.this.mQsExpanded) || NotificationPanelViewController.this.isFullyCollapsed() || !NotificationPanelViewController.this.onQsIntercept(motionEvent)) {
                        return super.onInterceptTouchEvent(motionEvent);
                    }
                    return true;
                }
                return false;
            }

            /* JADX WARN: Code restructure failed: missing block: B:32:0x008c, code lost:
                if (r0.shouldQuickSettingsIntercept(r0.mDownX, r6.this$0.mDownY, 0.0f) != false) goto L87;
             */
            /* JADX WARN: Removed duplicated region for block: B:52:0x0112 A[RETURN] */
            /* JADX WARN: Removed duplicated region for block: B:53:0x0113  */
            @Override // com.android.systemui.statusbar.phone.PanelViewController.TouchHandler, android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean z;
                QS qs;
                if (motionEvent.getAction() == 0) {
                    if (motionEvent.getDownTime() == this.mLastTouchDownTime) {
                        Log.w(PanelViewController.TAG, "Duplicate down event detected... ignoring");
                        return true;
                    }
                    this.mLastTouchDownTime = motionEvent.getDownTime();
                }
                if (NotificationPanelViewController.this.mBlockTouches || ((NotificationPanelViewController.this.mQsFullyExpanded && (qs = NotificationPanelViewController.this.mQs) != null && qs.disallowPanelTouches()) || NotificationPanelViewController.this.mStatusBar.isBouncerShowingScrimmed())) {
                    return false;
                }
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    NotificationPanelViewController.this.mBlockingExpansionForCurrentTouch = false;
                }
                if (NotificationPanelViewController.this.mLastEventSynthesizedDown && motionEvent.getAction() == 1) {
                    NotificationPanelViewController.this.expand(true);
                }
                NotificationPanelViewController.this.initDownStates(motionEvent);
                if (!NotificationPanelViewController.this.mIsExpanding) {
                    NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                }
                if (!NotificationPanelViewController.this.mPulseExpansionHandler.isExpanding()) {
                    z = false;
                    if (!z && NotificationPanelViewController.this.mPulseExpansionHandler.onTouchEvent(motionEvent)) {
                        return true;
                    }
                    if (NotificationPanelViewController.this.mListenForHeadsUp && !NotificationPanelViewController.this.mHeadsUpTouchHelper.isTrackingHeadsUp() && NotificationPanelViewController.this.mHeadsUpTouchHelper.onInterceptTouchEvent(motionEvent)) {
                        NotificationPanelViewController.this.mMetricsLogger.count("panel_open_peek", 1);
                    }
                    boolean onTouchEvent = ((NotificationPanelViewController.this.mIsExpanding || NotificationPanelViewController.this.mHintAnimationRunning) && !NotificationPanelViewController.this.mQsExpanded && NotificationPanelViewController.this.mBarState != 0 && !NotificationPanelViewController.this.mDozing) ? NotificationPanelViewController.this.mAffordanceHelper.onTouchEvent(motionEvent) | false : false;
                    if (!NotificationPanelViewController.this.mOnlyAffordanceInThisMotion) {
                        return true;
                    }
                    boolean onTouchEvent2 = onTouchEvent | NotificationPanelViewController.this.mHeadsUpTouchHelper.onTouchEvent(motionEvent);
                    if (!NotificationPanelViewController.this.mHeadsUpTouchHelper.isTrackingHeadsUp() && NotificationPanelViewController.this.handleQsTouch(motionEvent)) {
                        return true;
                    }
                    if (motionEvent.getActionMasked() == 0 && NotificationPanelViewController.this.isFullyCollapsed()) {
                        NotificationPanelViewController.this.mMetricsLogger.count("panel_open", 1);
                        NotificationPanelViewController.this.updateHorizontalPanelPosition(motionEvent.getX());
                        onTouchEvent2 = true;
                    }
                    if (motionEvent.getActionMasked() == 0 && NotificationPanelViewController.this.isFullyExpanded() && NotificationPanelViewController.this.mStatusBarKeyguardViewManager.isShowing()) {
                        NotificationPanelViewController.this.mStatusBarKeyguardViewManager.updateKeyguardPosition(motionEvent.getX());
                    }
                    return !NotificationPanelViewController.this.mDozing || NotificationPanelViewController.this.mPulsing || super.onTouch(view, motionEvent) || onTouchEvent2;
                }
                z = true;
                if (!z) {
                }
                if (NotificationPanelViewController.this.mListenForHeadsUp) {
                    NotificationPanelViewController.this.mMetricsLogger.count("panel_open_peek", 1);
                }
                if (NotificationPanelViewController.this.mIsExpanding) {
                }
                if (!NotificationPanelViewController.this.mOnlyAffordanceInThisMotion) {
                }
            }
        };
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    protected PanelViewController.OnConfigurationChangedListener createOnConfigurationChangedListener() {
        return new OnConfigurationChangedListener();
    }

    public NotificationStackScrollLayoutController getNotificationStackScrollLayoutController() {
        return this.mNotificationStackScrollLayoutController;
    }

    public boolean closeUserSwitcherIfOpen() {
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            return keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(true);
        }
        return false;
    }

    private void updateUserSwitcherFlags() {
        boolean z = this.mResources.getBoolean(17891581);
        this.mKeyguardUserSwitcherEnabled = z;
        this.mKeyguardQsUserSwitchEnabled = z && this.mResources.getBoolean(R$bool.config_keyguard_user_switch_opens_qs_details);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSettingsChangeListener() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("user_switcher_enabled"), false, this.mSettingsChangeObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterSettingsChangeListener() {
        this.mContentResolver.unregisterContentObserver(this.mSettingsChangeObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OnHeightChangedListener implements ExpandableView.OnHeightChangedListener {
        @Override // com.android.systemui.statusbar.notification.row.ExpandableView.OnHeightChangedListener
        public void onReset(ExpandableView expandableView) {
        }

        private OnHeightChangedListener() {
        }

        @Override // com.android.systemui.statusbar.notification.row.ExpandableView.OnHeightChangedListener
        public void onHeightChanged(ExpandableView expandableView, boolean z) {
            if (expandableView != null || !NotificationPanelViewController.this.mQsExpanded) {
                if (z && NotificationPanelViewController.this.mInterpolatedDarkAmount == 0.0f) {
                    NotificationPanelViewController.this.mAnimateNextPositionUpdate = true;
                }
                ExpandableView firstChildNotGone = NotificationPanelViewController.this.mNotificationStackScrollLayoutController.getFirstChildNotGone();
                ExpandableNotificationRow expandableNotificationRow = firstChildNotGone instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) firstChildNotGone : null;
                if (expandableNotificationRow != null && (expandableView == expandableNotificationRow || expandableNotificationRow.getNotificationParent() == expandableNotificationRow)) {
                    NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                }
                NotificationPanelViewController.this.requestPanelHeightUpdate();
            }
        }
    }

    /* loaded from: classes.dex */
    private class OnClickListener implements View.OnClickListener {
        private OnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            NotificationPanelViewController.this.onQsExpansionStarted();
            if (!NotificationPanelViewController.this.mQsExpanded) {
                if (!NotificationPanelViewController.this.isQsExpansionEnabled()) {
                    return;
                }
                NotificationPanelViewController.this.mLockscreenGestureLogger.write(195, 0, 0);
                NotificationPanelViewController.this.flingSettings(0.0f, 0, null, true);
                return;
            }
            NotificationPanelViewController.this.flingSettings(0.0f, 1, null, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OnOverscrollTopChangedListener implements NotificationStackScrollLayout.OnOverscrollTopChangedListener {
        private OnOverscrollTopChangedListener() {
        }

        @Override // com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.OnOverscrollTopChangedListener
        public void onOverscrollTopChanged(float f, boolean z) {
            NotificationPanelViewController notificationPanelViewController;
            if (NotificationPanelViewController.this.mShouldUseSplitNotificationShade) {
                return;
            }
            NotificationPanelViewController.this.cancelQsAnimation();
            if (!NotificationPanelViewController.this.isQsExpansionEnabled()) {
                f = 0.0f;
            }
            if (f < 1.0f) {
                f = 0.0f;
            }
            int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
            boolean z2 = true;
            NotificationPanelViewController.this.setOverScrolling(i != 0 && z);
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            if (i == 0) {
                z2 = false;
            }
            notificationPanelViewController2.mQsExpansionFromOverscroll = z2;
            NotificationPanelViewController.this.mLastOverscroll = f;
            NotificationPanelViewController.this.updateQsState();
            NotificationPanelViewController.this.setQsExpansion(notificationPanelViewController.mQsMinExpansionHeight + f);
        }

        @Override // com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.OnOverscrollTopChangedListener
        public void flingTopOverscroll(float f, boolean z) {
            if (!NotificationPanelViewController.this.mShouldUseSplitNotificationShade || (NotificationPanelViewController.this.mInitialTouchX >= NotificationPanelViewController.this.mQsFrame.getX() && NotificationPanelViewController.this.mInitialTouchX <= NotificationPanelViewController.this.mQsFrame.getX() + NotificationPanelViewController.this.mQsFrame.getWidth())) {
                NotificationPanelViewController.this.mLastOverscroll = 0.0f;
                NotificationPanelViewController.this.mQsExpansionFromOverscroll = false;
                if (z) {
                    NotificationPanelViewController.this.setOverScrolling(false);
                }
                NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                notificationPanelViewController.setQsExpansion(notificationPanelViewController.mQsExpansionHeight);
                boolean isQsExpansionEnabled = NotificationPanelViewController.this.isQsExpansionEnabled();
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                if (!isQsExpansionEnabled && z) {
                    f = 0.0f;
                }
                notificationPanelViewController2.flingSettings(f, (!z || !isQsExpansionEnabled) ? 1 : 0, new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$OnOverscrollTopChangedListener$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationPanelViewController.OnOverscrollTopChangedListener.this.lambda$flingTopOverscroll$0();
                    }
                }, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$flingTopOverscroll$0() {
            NotificationPanelViewController.this.setOverScrolling(false);
            NotificationPanelViewController.this.updateQsState();
        }
    }

    /* loaded from: classes.dex */
    private class DynamicPrivacyControlListener implements DynamicPrivacyController.Listener {
        private DynamicPrivacyControlListener() {
        }

        @Override // com.android.systemui.statusbar.notification.DynamicPrivacyController.Listener
        public void onDynamicPrivacyChanged() {
            if (NotificationPanelViewController.this.mLinearDarkAmount != 0.0f) {
                return;
            }
            NotificationPanelViewController.this.mAnimateNextPositionUpdate = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class KeyguardAffordanceHelperCallback implements KeyguardAffordanceHelper.Callback {
        private KeyguardAffordanceHelperCallback() {
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public void onAnimationToSideStarted(boolean z, float f, float f2) {
            if (NotificationPanelViewController.this.mView.getLayoutDirection() != 1) {
                z = !z;
            }
            NotificationPanelViewController.this.mIsLaunchTransitionRunning = true;
            NotificationPanelViewController.this.mLaunchAnimationEndRunnable = null;
            float displayDensity = NotificationPanelViewController.this.mStatusBar.getDisplayDensity();
            int abs = Math.abs((int) (f / displayDensity));
            int abs2 = Math.abs((int) (f2 / displayDensity));
            if (z) {
                NotificationPanelViewController.this.mLockscreenGestureLogger.write(190, abs, abs2);
                NotificationPanelViewController.this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_DIALER);
                NotificationPanelViewController.this.mFalsingCollector.onLeftAffordanceOn();
                if (NotificationPanelViewController.this.mFalsingCollector.shouldEnforceBouncer()) {
                    NotificationPanelViewController.this.mStatusBar.executeRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$KeyguardAffordanceHelperCallback$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationPanelViewController.KeyguardAffordanceHelperCallback.this.lambda$onAnimationToSideStarted$0();
                        }
                    }, null, true, false, true);
                } else {
                    NotificationPanelViewController.this.mKeyguardBottomArea.launchLeftAffordance();
                }
            } else {
                if ("lockscreen_affordance".equals(NotificationPanelViewController.this.mLastCameraLaunchSource)) {
                    NotificationPanelViewController.this.mLockscreenGestureLogger.write(189, abs, abs2);
                    NotificationPanelViewController.this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_CAMERA);
                }
                NotificationPanelViewController.this.mFalsingCollector.onCameraOn();
                if (NotificationPanelViewController.this.mFalsingCollector.shouldEnforceBouncer()) {
                    NotificationPanelViewController.this.mStatusBar.executeRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$KeyguardAffordanceHelperCallback$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationPanelViewController.KeyguardAffordanceHelperCallback.this.lambda$onAnimationToSideStarted$1();
                        }
                    }, null, true, false, true);
                } else {
                    NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                    notificationPanelViewController.mKeyguardBottomArea.launchCamera(notificationPanelViewController.mLastCameraLaunchSource);
                }
            }
            NotificationPanelViewController.this.mStatusBar.startLaunchTransitionTimeout();
            NotificationPanelViewController.this.mBlockTouches = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationToSideStarted$0() {
            NotificationPanelViewController.this.mKeyguardBottomArea.launchLeftAffordance();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationToSideStarted$1() {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mKeyguardBottomArea.launchCamera(notificationPanelViewController.mLastCameraLaunchSource);
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public void onAnimationToSideEnded() {
            NotificationPanelViewController.this.mIsLaunchTransitionRunning = false;
            NotificationPanelViewController.this.mIsLaunchTransitionFinished = true;
            if (NotificationPanelViewController.this.mLaunchAnimationEndRunnable != null) {
                NotificationPanelViewController.this.mLaunchAnimationEndRunnable.run();
                NotificationPanelViewController.this.mLaunchAnimationEndRunnable = null;
            }
            NotificationPanelViewController.this.mStatusBar.readyForKeyguardDone();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public float getMaxTranslationDistance() {
            return (float) Math.hypot(NotificationPanelViewController.this.mView.getWidth(), NotificationPanelViewController.this.getHeight());
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public void onSwipingStarted(boolean z) {
            NotificationPanelViewController.this.mFalsingCollector.onAffordanceSwipingStarted(z);
            if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
                z = !z;
            }
            if (z) {
                NotificationPanelViewController.this.mKeyguardBottomArea.bindCameraPrewarmService();
            }
            NotificationPanelViewController.this.mView.requestDisallowInterceptTouchEvent(true);
            NotificationPanelViewController.this.mOnlyAffordanceInThisMotion = true;
            NotificationPanelViewController.this.mQsTracking = false;
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public void onSwipingAborted() {
            NotificationPanelViewController.this.mFalsingCollector.onAffordanceSwipingAborted();
            NotificationPanelViewController.this.mKeyguardBottomArea.unbindCameraPrewarmService(false);
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public void onIconClicked(boolean z) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            if (notificationPanelViewController.mHintAnimationRunning) {
                return;
            }
            notificationPanelViewController.mHintAnimationRunning = true;
            notificationPanelViewController.mAffordanceHelper.startHintAnimation(z, new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$KeyguardAffordanceHelperCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationPanelViewController.KeyguardAffordanceHelperCallback.this.lambda$onIconClicked$2();
                }
            });
            if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
                z = !z;
            }
            if (z) {
                NotificationPanelViewController.this.mStatusBar.onCameraHintStarted();
            } else if (NotificationPanelViewController.this.mKeyguardBottomArea.isLeftVoiceAssist()) {
                NotificationPanelViewController.this.mStatusBar.onVoiceAssistHintStarted();
            } else {
                NotificationPanelViewController.this.mStatusBar.onPhoneHintStarted();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onIconClicked$2() {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mHintAnimationRunning = false;
            notificationPanelViewController.mStatusBar.onHintFinished();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public KeyguardAffordanceView getLeftIcon() {
            return NotificationPanelViewController.this.mView.getLayoutDirection() == 1 ? NotificationPanelViewController.this.mKeyguardBottomArea.getRightView() : NotificationPanelViewController.this.mKeyguardBottomArea.getLeftView();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public KeyguardAffordanceView getRightIcon() {
            return NotificationPanelViewController.this.mView.getLayoutDirection() == 1 ? NotificationPanelViewController.this.mKeyguardBottomArea.getLeftView() : NotificationPanelViewController.this.mKeyguardBottomArea.getRightView();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public View getLeftPreview() {
            return NotificationPanelViewController.this.mView.getLayoutDirection() == 1 ? NotificationPanelViewController.this.mKeyguardBottomArea.getRightPreview() : NotificationPanelViewController.this.mKeyguardBottomArea.getLeftPreview();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public View getRightPreview() {
            return NotificationPanelViewController.this.mView.getLayoutDirection() == 1 ? NotificationPanelViewController.this.mKeyguardBottomArea.getLeftPreview() : NotificationPanelViewController.this.mKeyguardBottomArea.getRightPreview();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public float getAffordanceFalsingFactor() {
            return NotificationPanelViewController.this.mStatusBar.isWakeUpComingFromTouch() ? 1.5f : 1.0f;
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.Callback
        public boolean needsAntiFalsing() {
            return NotificationPanelViewController.this.mBarState == 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OnEmptySpaceClickListener implements NotificationStackScrollLayout.OnEmptySpaceClickListener {
        private OnEmptySpaceClickListener() {
        }

        @Override // com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.OnEmptySpaceClickListener
        public void onEmptySpaceClicked(float f, float f2) {
            NotificationPanelViewController.this.onEmptySpaceClick(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyOnHeadsUpChangedListener implements OnHeadsUpChangedListener {
        private MyOnHeadsUpChangedListener() {
        }

        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public void onHeadsUpPinnedModeChanged(boolean z) {
            if (z) {
                NotificationPanelViewController.this.mHeadsUpExistenceChangedRunnable.run();
                NotificationPanelViewController.this.updateNotificationTranslucency();
            } else {
                NotificationPanelViewController.this.setHeadsUpAnimatingAway(true);
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.runAfterAnimationFinished(NotificationPanelViewController.this.mHeadsUpExistenceChangedRunnable);
            }
            NotificationPanelViewController.this.updateGestureExclusionRect();
            NotificationPanelViewController.this.mHeadsUpPinnedMode = z;
            NotificationPanelViewController.this.updateHeadsUpVisibility();
            NotificationPanelViewController.this.updateKeyguardStatusBarForHeadsUp();
        }

        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public void onHeadsUpPinned(NotificationEntry notificationEntry) {
            if (!NotificationPanelViewController.this.isOnKeyguard()) {
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.generateHeadsUpAnimation(notificationEntry.getHeadsUpAnimationView(), true);
            }
        }

        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public void onHeadsUpUnPinned(NotificationEntry notificationEntry) {
            if (!NotificationPanelViewController.this.isFullyCollapsed() || !notificationEntry.isRowHeadsUp() || NotificationPanelViewController.this.isOnKeyguard()) {
                return;
            }
            NotificationPanelViewController.this.mNotificationStackScrollLayoutController.generateHeadsUpAnimation(notificationEntry.getHeadsUpAnimationView(), false);
            notificationEntry.setHeadsUpIsVisible();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class HeightListener implements QS.HeightListener {
        private HeightListener() {
        }

        @Override // com.android.systemui.plugins.qs.QS.HeightListener
        public void onQsHeightChanged() {
            NotificationPanelViewController.this.mHasQSLayout = true;
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            QS qs = notificationPanelViewController.mQs;
            notificationPanelViewController.mQsMaxExpansionHeight = qs != null ? qs.getDesiredHeight() : 0;
            if (NotificationPanelViewController.this.mQsExpanded && NotificationPanelViewController.this.mQsFullyExpanded) {
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                notificationPanelViewController2.mQsExpansionHeight = notificationPanelViewController2.mQsMaxExpansionHeight;
                NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                NotificationPanelViewController.this.requestPanelHeightUpdate();
            }
            if (NotificationPanelViewController.this.mAccessibilityManager.isEnabled()) {
                NotificationPanelViewController.this.mView.setAccessibilityPaneTitle(NotificationPanelViewController.this.determineAccessibilityPaneTitle());
            }
            NotificationPanelViewController.this.mNotificationStackScrollLayoutController.setMaxTopPadding(NotificationPanelViewController.this.mQsMaxExpansionHeight);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ConfigurationListener implements ConfigurationController.ConfigurationListener {
        private ConfigurationListener() {
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onThemeChanged() {
            int themeResId = NotificationPanelViewController.this.mView.getContext().getThemeResId();
            if (NotificationPanelViewController.this.mThemeResId == themeResId) {
                return;
            }
            NotificationPanelViewController.this.mThemeResId = themeResId;
            NotificationPanelViewController.this.reInflateViews();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onSmallestScreenWidthChanged() {
            NotificationPanelViewController.this.reInflateViews();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onOverlayChanged() {
            NotificationPanelViewController.this.reInflateViews();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onDensityOrFontScaleChanged() {
            NotificationPanelViewController.this.reInflateViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SettingsChangeObserver extends ContentObserver {
        SettingsChangeObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            NotificationPanelViewController.this.reInflateViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class StatusBarStateListener implements StatusBarStateController.StateListener {
        private StatusBarStateListener() {
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onStateChanged(int i) {
            QS qs;
            boolean goingToFullShade = NotificationPanelViewController.this.mStatusBarStateController.goingToFullShade();
            boolean isKeyguardFadingAway = NotificationPanelViewController.this.mKeyguardStateController.isKeyguardFadingAway();
            int i2 = NotificationPanelViewController.this.mBarState;
            boolean z = i == 1;
            if (NotificationPanelViewController.this.mDozeParameters.shouldControlUnlockedScreenOff() && i2 == 0 && i == 1) {
                NotificationPanelViewController.this.mKeyguardStatusViewController.updatePosition(NotificationPanelViewController.this.mClockPositionResult.clockX, NotificationPanelViewController.this.mClockPositionResult.clockYFullyDozing, NotificationPanelViewController.this.mClockPositionResult.clockScale, false);
            }
            String str = PanelViewController.TAG;
            Log.i(str, "onStateChanged newState=" + i + ", oldState=" + i2);
            NotificationPanelViewController.this.mKeyguardStatusViewController.setKeyguardStatusViewVisibility(i, isKeyguardFadingAway, goingToFullShade, NotificationPanelViewController.this.mBarState);
            NotificationPanelViewController.this.setKeyguardBottomAreaVisibility(i, goingToFullShade);
            NotificationPanelViewController.this.mBarState = i;
            NotificationPanelViewController.this.mKeyguardShowing = z;
            if (i2 == 1 && (goingToFullShade || i == 2)) {
                NotificationPanelViewController.this.animateKeyguardStatusBarOut();
                NotificationPanelViewController.this.updateQSMinHeight();
            } else if (i2 != 2 || i != 1) {
                NotificationPanelViewController.this.mKeyguardStatusBar.setAlpha(1.0f);
                NotificationPanelViewController.this.mKeyguardStatusBar.setVisibility(z ? 0 : 4);
                if (z && i2 != NotificationPanelViewController.this.mBarState && (qs = NotificationPanelViewController.this.mQs) != null) {
                    qs.hideImmediately();
                }
            } else {
                NotificationPanelViewController.this.animateKeyguardStatusBarIn(360L);
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.resetScrollPosition();
                if (!NotificationPanelViewController.this.mQsExpanded && NotificationPanelViewController.this.mShouldUseSplitNotificationShade) {
                    NotificationPanelViewController.this.mQs.animateHeaderSlidingOut();
                }
            }
            NotificationPanelViewController.this.updateKeyguardStatusBarForHeadsUp();
            if (z) {
                NotificationPanelViewController.this.updateDozingVisibilities(false);
            }
            NotificationPanelViewController.this.updateMaxDisplayedNotifications(false);
            NotificationPanelViewController.this.maybeAnimateBottomAreaAlpha();
            NotificationPanelViewController.this.resetHorizontalPanelPosition();
            NotificationPanelViewController.this.updateQsState();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozeAmountChanged(float f, float f2) {
            NotificationPanelViewController.this.mInterpolatedDarkAmount = f2;
            NotificationPanelViewController.this.mLinearDarkAmount = f;
            NotificationPanelViewController.this.mKeyguardStatusViewController.setDarkAmount(NotificationPanelViewController.this.mInterpolatedDarkAmount);
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mKeyguardBottomArea.setDarkAmount(notificationPanelViewController.mInterpolatedDarkAmount);
            NotificationPanelViewController.this.positionClockAndNotifications();
        }
    }

    public void showAodUi() {
        setDozing(true, false, null);
        this.mStatusBarStateController.setUpcomingState(1);
        this.mEntryManager.updateNotifications("showAodUi");
        this.mStatusBarStateListener.onStateChanged(1);
        this.mStatusBarStateListener.onDozeAmountChanged(1.0f, 1.0f);
        setExpandedFraction(1.0f);
    }

    public void setOverStrechAmount(float f) {
        this.mOverStretchAmount = Interpolators.getOvershootInterpolation(f / this.mView.getHeight()) * this.mMaxOverscrollAmountForPulse;
        positionClockAndNotifications(true);
    }

    /* loaded from: classes.dex */
    private class OnAttachStateChangeListener implements View.OnAttachStateChangeListener {
        private OnAttachStateChangeListener() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            NotificationPanelViewController.this.mFragmentService.getFragmentHostManager(NotificationPanelViewController.this.mView).addTagListener(QS.TAG, NotificationPanelViewController.this.mFragmentListener);
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mStatusBarStateController.addCallback(notificationPanelViewController.mStatusBarStateListener);
            NotificationPanelViewController.this.mConfigurationController.addCallback(NotificationPanelViewController.this.mConfigurationListener);
            NotificationPanelViewController.this.mUpdateMonitor.registerCallback(NotificationPanelViewController.this.mKeyguardUpdateCallback);
            NotificationPanelViewController.this.mConfigurationListener.onThemeChanged();
            NotificationPanelViewController.this.mFalsingManager.addTapListener(NotificationPanelViewController.this.mFalsingTapListener);
            NotificationPanelViewController.this.mKeyguardIndicationController.init();
            NotificationPanelViewController.this.registerSettingsChangeListener();
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            NotificationPanelViewController.this.unregisterSettingsChangeListener();
            NotificationPanelViewController.this.mFragmentService.getFragmentHostManager(NotificationPanelViewController.this.mView).removeTagListener(QS.TAG, NotificationPanelViewController.this.mFragmentListener);
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mStatusBarStateController.removeCallback(notificationPanelViewController.mStatusBarStateListener);
            NotificationPanelViewController.this.mConfigurationController.removeCallback(NotificationPanelViewController.this.mConfigurationListener);
            NotificationPanelViewController.this.mUpdateMonitor.removeCallback(NotificationPanelViewController.this.mKeyguardUpdateCallback);
            NotificationPanelViewController.this.mFalsingManager.removeTapListener(NotificationPanelViewController.this.mFalsingTapListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OnLayoutChangeListener extends PanelViewController.OnLayoutChangeListener {
        private OnLayoutChangeListener() {
            super();
        }

        @Override // com.android.systemui.statusbar.phone.PanelViewController.OnLayoutChangeListener, android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            NotificationPanelViewController notificationPanelViewController;
            QS qs;
            NotificationPanelViewController notificationPanelViewController2;
            DejankUtils.startDetectingBlockingIpcs("NVP#onLayout");
            super.onLayoutChange(view, i, i2, i3, i4, i5, i6, i7, i8);
            boolean z = true;
            NotificationPanelViewController.this.updateMaxDisplayedNotifications(true);
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            if (notificationPanelViewController3.mNotificationStackScrollLayoutController.getWidth() != NotificationPanelViewController.this.mView.getWidth()) {
                z = false;
            }
            notificationPanelViewController3.setIsFullWidth(z);
            NotificationPanelViewController.this.mKeyguardStatusViewController.setPivotX(NotificationPanelViewController.this.mView.getWidth() / 2);
            NotificationPanelViewController.this.mKeyguardStatusViewController.setPivotY(NotificationPanelViewController.this.mKeyguardStatusViewController.getClockTextSize() * 0.34521484f);
            int i9 = NotificationPanelViewController.this.mQsMaxExpansionHeight;
            NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
            if (notificationPanelViewController4.mQs != null) {
                notificationPanelViewController4.updateQSMinHeight();
                NotificationPanelViewController notificationPanelViewController5 = NotificationPanelViewController.this;
                notificationPanelViewController5.mQsMaxExpansionHeight = notificationPanelViewController5.mQs.getDesiredHeight();
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.setMaxTopPadding(NotificationPanelViewController.this.mQsMaxExpansionHeight);
            }
            NotificationPanelViewController.this.positionClockAndNotifications();
            if (!NotificationPanelViewController.this.mQsExpanded || !NotificationPanelViewController.this.mQsFullyExpanded) {
                if (!NotificationPanelViewController.this.mQsExpanded && NotificationPanelViewController.this.mQsExpansionAnimator == null) {
                    NotificationPanelViewController.this.setQsExpansion(notificationPanelViewController.mQsMinExpansionHeight + NotificationPanelViewController.this.mLastOverscroll);
                }
            } else {
                NotificationPanelViewController.this.mQsExpansionHeight = notificationPanelViewController2.mQsMaxExpansionHeight;
                NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                NotificationPanelViewController.this.requestPanelHeightUpdate();
                if (NotificationPanelViewController.this.mQsMaxExpansionHeight != i9) {
                    NotificationPanelViewController notificationPanelViewController6 = NotificationPanelViewController.this;
                    notificationPanelViewController6.startQsSizeChangeAnimation(i9, notificationPanelViewController6.mQsMaxExpansionHeight);
                }
            }
            NotificationPanelViewController notificationPanelViewController7 = NotificationPanelViewController.this;
            notificationPanelViewController7.updateExpandedHeight(notificationPanelViewController7.getExpandedHeight());
            NotificationPanelViewController.this.updateHeader();
            if (NotificationPanelViewController.this.mQsSizeChangeAnimator == null && (qs = NotificationPanelViewController.this.mQs) != null) {
                qs.setHeightOverride(qs.getDesiredHeight());
            }
            NotificationPanelViewController.this.updateMaxHeadsUpTranslation();
            NotificationPanelViewController.this.updateGestureExclusionRect();
            if (NotificationPanelViewController.this.mExpandAfterLayoutRunnable != null) {
                NotificationPanelViewController.this.mExpandAfterLayoutRunnable.run();
                NotificationPanelViewController.this.mExpandAfterLayoutRunnable = null;
            }
            DejankUtils.stopDetectingBlockingIpcs("NVP#onLayout");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateQSMinHeight() {
        float f = this.mQsMinExpansionHeight;
        int qsMinExpansionHeight = this.mKeyguardShowing ? 0 : this.mQs.getQsMinExpansionHeight();
        this.mQsMinExpansionHeight = qsMinExpansionHeight;
        if (this.mQsExpansionHeight == f) {
            this.mQsExpansionHeight = qsMinExpansionHeight;
        }
    }

    /* loaded from: classes.dex */
    private class OnConfigurationChangedListener extends PanelViewController.OnConfigurationChangedListener {
        private OnConfigurationChangedListener() {
            super();
        }

        @Override // com.android.systemui.statusbar.phone.PanelViewController.OnConfigurationChangedListener, com.android.systemui.statusbar.phone.PanelView.OnConfigurationChangedListener
        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            NotificationPanelViewController.this.mAffordanceHelper.onConfigurationChanged();
            if (configuration.orientation != NotificationPanelViewController.this.mLastOrientation) {
                NotificationPanelViewController.this.resetHorizontalPanelPosition();
                NotificationPanelViewController.this.updateStates();
            }
            NotificationPanelViewController.this.mLastOrientation = configuration.orientation;
        }
    }

    /* loaded from: classes.dex */
    private class OnApplyWindowInsetsListener implements View.OnApplyWindowInsetsListener {
        private OnApplyWindowInsetsListener() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            NotificationPanelViewController.this.mDisplayTopInset = insetsIgnoringVisibility.top;
            NotificationPanelViewController.this.mDisplayRightInset = insetsIgnoringVisibility.right;
            NotificationPanelViewController.this.mNavigationBarBottomHeight = windowInsets.getStableInsetBottom();
            NotificationPanelViewController.this.updateMaxHeadsUpTranslation();
            return windowInsets;
        }
    }

    private void makeStatusBarView() {
        RelativeLayout relativeLayout = (RelativeLayout) this.mView.findViewById(R$id.qs_status_bar_layout_land);
        this.mQsStatusBarLayoutLand = relativeLayout;
        relativeLayout.setAlpha(0.0f);
    }

    private void updateStatusBarAlpha() {
        if (isOnKeyguard() || this.mQsStatusBarLayoutLand.getVisibility() != 0) {
            return;
        }
        this.mQsStatusBarLayoutLand.setAlpha(Interpolators.getNotificationScrimAlpha(getExpandedFraction(), false, SystemUIUtils.getInstance().shouldUseSplitNotificationShade()));
    }

    private void updateQsFrameAlpha() {
        if (this.mShouldUseSplitNotificationShade) {
            this.mQsFrame.setAlpha(Interpolators.getNotificationScrimAlpha(getExpandedFraction(), false, SystemUIUtils.getInstance().shouldUseSplitNotificationShade()));
            return;
        }
        this.mQsFrame.setAlpha(1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStates() {
        if (!this.mPanelExpanded || isOnKeyguard()) {
            return;
        }
        if (this.mQs != null) {
            updateQSMinHeight();
        }
        if (SystemUIUtils.getInstance().shouldUseSplitNotificationShade()) {
            this.mHasOrientationChanged = true;
            if (this.mQsFullyExpanded) {
                this.mPortState = 1;
            } else {
                this.mPortState = 2;
            }
            this.mQsFullyExpanded = true;
            this.mQsExpanded = true;
        } else if (this.mHasOrientationChanged) {
            if (this.mPortState == 2) {
                this.mQsExpanded = false;
                this.mQsFullyExpanded = false;
            }
        } else {
            this.mQsExpanded = false;
            this.mQsFullyExpanded = false;
        }
        updateQsState();
    }
}
