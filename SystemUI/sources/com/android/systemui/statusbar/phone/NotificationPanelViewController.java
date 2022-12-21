package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.SensorLocationInternal;
import android.os.Debug;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Trace;
import android.os.UserManager;
import android.provider.Settings;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.IndentingPrintWriter;
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
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.policy.SystemBarUtils;
import com.android.keyguard.ActiveUnlockConfig;
import com.android.keyguard.EmergencyButton;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.LockIconViewController;
import com.android.keyguard.dagger.KeyguardQsUserSwitchComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardUserSwitcherComponent;
import com.android.p019wm.shell.animation.FlingAnimationUtils;
import com.android.systemui.C1893R;
import com.android.systemui.DejankUtils;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpsysTableLogger;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.GestureRecorder;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.QsFrameTranslateController;
import com.android.systemui.statusbar.RemoteInputController;
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
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator;
import com.android.systemui.statusbar.phone.KeyguardAffordanceHelper;
import com.android.systemui.statusbar.phone.KeyguardClockPositionAlgorithm;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.NPVCDownEventState;
import com.android.systemui.statusbar.phone.NotifPanelEvents;
import com.android.systemui.statusbar.phone.PanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.Utils;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.facerecognition.FaceRecognitionController;
import com.nothing.systemui.p024qs.NTQSStatusBarController;
import com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager;
import com.nothing.systemui.statusbar.phone.NotificationPanelViewControllerEx;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Provider;

@CentralSurfacesComponent.CentralSurfacesScope
public class NotificationPanelViewController extends PanelViewController {
    private static final long ANIMATION_DELAY_ICON_FADE_IN = (((ActivityLaunchAnimator.TIMINGS.getTotalDuration() - 320) - 50) - 48);
    private static final int CAP_HEIGHT = 1456;
    private static final String COUNTER_PANEL_OPEN = "panel_open";
    private static final String COUNTER_PANEL_OPEN_PEEK = "panel_open_peek";
    private static final String COUNTER_PANEL_OPEN_QS = "panel_open_qs";
    private static final boolean DEBUG_DRAWABLE = false;
    /* access modifiers changed from: private */
    public static final boolean DEBUG_LOGCAT = false;
    private static final Rect EMPTY_RECT = new Rect();
    private static final int FLING_COLLAPSE = 1;
    private static final int FLING_EXPAND = 0;
    private static final int FLING_HIDE = 2;
    private static final int FONT_HEIGHT = 2163;
    private static final int MAX_DOWN_EVENT_BUFFER_SIZE = 50;
    private static final int MAX_TIME_TO_OPEN_WHEN_FLINGING_FROM_LAUNCHER = 300;
    /* access modifiers changed from: private */
    public static final Rect M_DUMMY_DIRTY_RECT = new Rect(0, 0, 1, 1);
    private static final float QS_PARALLAX_AMOUNT = 0.175f;
    /* access modifiers changed from: private */
    public static final boolean SPEW_LOGCAT = false;
    private View.AccessibilityDelegate mAccessibilityDelegate;
    /* access modifiers changed from: private */
    public final AccessibilityManager mAccessibilityManager;
    private final ActivityManager mActivityManager;
    private boolean mAffordanceHasPreview;
    /* access modifiers changed from: private */
    public KeyguardAffordanceHelper mAffordanceHelper;
    private boolean mAllowExpandForSmallExpansion;
    private int mAmbientIndicationBottomPadding;
    private final Runnable mAnimateKeyguardBottomAreaInvisibleEndRunnable;
    private boolean mAnimateNextNotificationBounds;
    /* access modifiers changed from: private */
    public boolean mAnimateNextPositionUpdate;
    /* access modifiers changed from: private */
    public boolean mAnimatingQS;
    private final AuthController mAuthController;
    /* access modifiers changed from: private */
    public int mBarState;
    /* access modifiers changed from: private */
    public boolean mBlockTouches;
    /* access modifiers changed from: private */
    public boolean mBlockingExpansionForCurrentTouch;
    private float mBottomAreaShadeAlpha;
    private final ValueAnimator mBottomAreaShadeAlphaAnimator;
    private boolean mBouncerShowing;
    private final KeyguardClockPositionAlgorithm mClockPositionAlgorithm = new KeyguardClockPositionAlgorithm();
    /* access modifiers changed from: private */
    public final KeyguardClockPositionAlgorithm.Result mClockPositionResult = new KeyguardClockPositionAlgorithm.Result();
    private boolean mClosingWithAlphaFadeOut;
    /* access modifiers changed from: private */
    public final Runnable mCollapseExpandAction = new CollapseExpandAction();
    private boolean mCollapsedOnDown;
    /* access modifiers changed from: private */
    public final CommandQueue mCommandQueue;
    /* access modifiers changed from: private */
    public final ConfigurationController mConfigurationController;
    /* access modifiers changed from: private */
    public final ConfigurationListener mConfigurationListener = new ConfigurationListener();
    private boolean mConflictingQsExpansionGesture;
    private final ContentResolver mContentResolver;
    private final ControlsComponent mControlsComponent;
    private final ConversationNotificationManager mConversationNotificationManager;
    private int mCurrentPanelState;
    private int mDarkIconSize;
    /* access modifiers changed from: private */
    public NotificationShadeDepthController mDepthController;
    private int mDisplayId;
    /* access modifiers changed from: private */
    public int mDisplayLeftInset = 0;
    /* access modifiers changed from: private */
    public int mDisplayRightInset = 0;
    /* access modifiers changed from: private */
    public int mDisplayTopInset = 0;
    private int mDistanceForQSFullShadeTransition;
    /* access modifiers changed from: private */
    public float mDownX;
    /* access modifiers changed from: private */
    public float mDownY;
    /* access modifiers changed from: private */
    public final DozeParameters mDozeParameters;
    /* access modifiers changed from: private */
    public boolean mDozing;
    private boolean mDozingOnDown;
    private EmergencyButtonController mEmergencyButtonController;
    private final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
    private final NotificationEntryManager mEntryManager;
    /* access modifiers changed from: private */
    public Runnable mExpandAfterLayoutRunnable;
    private boolean mExpandingFromHeadsUp;
    private boolean mExpectingSynthesizedDown;
    /* access modifiers changed from: private */
    public final FalsingCollector mFalsingCollector;
    /* access modifiers changed from: private */
    public final FalsingManager mFalsingManager;
    /* access modifiers changed from: private */
    public final FalsingManager.FalsingTapListener mFalsingTapListener;
    private final FeatureFlags mFeatureFlags;
    private FlingAnimationUtils mFlingAnimationUtils;
    private final Provider<FlingAnimationUtils.Builder> mFlingAnimationUtilsBuilder;
    /* access modifiers changed from: private */
    public final FragmentHostManager.FragmentListener mFragmentListener;
    /* access modifiers changed from: private */
    public final FragmentService mFragmentService;
    /* access modifiers changed from: private */
    public String mHeaderDebugInfo;
    private boolean mHeadsUpAnimatingAway;
    /* access modifiers changed from: private */
    public HeadsUpAppearanceController mHeadsUpAppearanceController;
    /* access modifiers changed from: private */
    public Runnable mHeadsUpExistenceChangedRunnable = new NotificationPanelViewController$$ExternalSyntheticLambda14(this);
    private int mHeadsUpInset;
    /* access modifiers changed from: private */
    public boolean mHeadsUpPinnedMode;
    /* access modifiers changed from: private */
    public HeadsUpTouchHelper mHeadsUpTouchHelper;
    /* access modifiers changed from: private */
    public final HeightListener mHeightListener = new HeightListener();
    /* access modifiers changed from: private */
    public Runnable mHideExpandedRunnable;
    private boolean mHideIconsDuringLaunchAnimation = true;
    private int mIndicationBottomPadding;
    private float mInitialHeightOnTouch;
    /* access modifiers changed from: private */
    public float mInitialTouchX;
    private float mInitialTouchY;
    private final InteractionJankMonitor mInteractionJankMonitor;
    /* access modifiers changed from: private */
    public float mInterpolatedDarkAmount;
    /* access modifiers changed from: private */
    public boolean mIsExpanding;
    private boolean mIsFullWidth;
    private boolean mIsGestureNavigation;
    /* access modifiers changed from: private */
    public boolean mIsLaunchTransitionFinished;
    /* access modifiers changed from: private */
    public boolean mIsLaunchTransitionRunning;
    private boolean mIsPanelCollapseOnQQS;
    /* access modifiers changed from: private */
    public boolean mIsPulseExpansionResetAnimator;
    /* access modifiers changed from: private */
    public boolean mIsQsTranslationResetAnimator;
    private final KeyguardAffordanceHelperCallback mKeyguardAffordanceHelperCallback = new KeyguardAffordanceHelperCallback();
    /* access modifiers changed from: private */
    public final KeyguardBypassController mKeyguardBypassController;
    /* access modifiers changed from: private */
    public KeyguardIndicationController mKeyguardIndicationController;
    private KeyguardMediaController mKeyguardMediaController;
    /* access modifiers changed from: private */
    public float mKeyguardNotificationBottomPadding;
    /* access modifiers changed from: private */
    public float mKeyguardNotificationTopPadding;
    private float mKeyguardOnlyContentAlpha;
    private int mKeyguardOnlyTransitionTranslationY;
    private final KeyguardQsUserSwitchComponent.Factory mKeyguardQsUserSwitchComponentFactory;
    private KeyguardQsUserSwitchController mKeyguardQsUserSwitchController;
    /* access modifiers changed from: private */
    public boolean mKeyguardQsUserSwitchEnabled;
    /* access modifiers changed from: private */
    public boolean mKeyguardShowing;
    private final Rect mKeyguardStatusAreaClipBounds;
    private KeyguardStatusBarView mKeyguardStatusBar;
    private final KeyguardStatusBarViewComponent.Factory mKeyguardStatusBarViewComponentFactory;
    /* access modifiers changed from: private */
    public KeyguardStatusBarViewController mKeyguardStatusBarViewController;
    private final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
    /* access modifiers changed from: private */
    public KeyguardStatusViewController mKeyguardStatusViewController;
    private Optional<KeyguardUnfoldTransition> mKeyguardUnfoldTransition;
    private final KeyguardUserSwitcherComponent.Factory mKeyguardUserSwitcherComponentFactory;
    private KeyguardUserSwitcherController mKeyguardUserSwitcherController;
    /* access modifiers changed from: private */
    public boolean mKeyguardUserSwitcherEnabled;
    /* access modifiers changed from: private */
    public final LargeScreenShadeHeaderController mLargeScreenShadeHeaderController;
    private int mLargeScreenShadeHeaderHeight;
    /* access modifiers changed from: private */
    public String mLastCameraLaunchSource = KeyguardBottomAreaView.CAMERA_LAUNCH_SOURCE_AFFORDANCE;
    private final NPVCDownEventState.Buffer mLastDownEvents;
    /* access modifiers changed from: private */
    public boolean mLastEventSynthesizedDown;
    /* access modifiers changed from: private */
    public float mLastOverscroll;
    /* access modifiers changed from: private */
    public Runnable mLaunchAnimationEndRunnable;
    private boolean mLaunchingAffordance;
    private final LayoutInflater mLayoutInflater;
    /* access modifiers changed from: private */
    public float mLinearDarkAmount;
    /* access modifiers changed from: private */
    public boolean mListenForHeadsUp;
    /* access modifiers changed from: private */
    public LockIconViewController mLockIconViewController;
    private int mLockscreenNotificationQSPadding;
    /* access modifiers changed from: private */
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    private int mMaxAllowedKeyguardNotifications;
    private int mMaxOverscrollAmountForPulse;
    private final Runnable mMaybeHideExpandedRunnable;
    private final MediaDataManager mMediaDataManager;
    private final MediaHierarchyManager mMediaHierarchyManager;
    /* access modifiers changed from: private */
    public final MetricsLogger mMetricsLogger;
    private float mMinFraction;
    private final NTQSStatusBarController mNTQSStatusBarController;
    /* access modifiers changed from: private */
    public int mNavigationBarBottomHeight;
    private long mNotificationBoundsAnimationDelay;
    private long mNotificationBoundsAnimationDuration;
    private NotificationsQuickSettingsContainer mNotificationContainerParent;
    private final NotificationIconAreaController mNotificationIconAreaController;
    private final NotificationListContainer mNotificationListContainer;
    private Optional<NotificationPanelUnfoldAnimationController> mNotificationPanelUnfoldAnimationController;
    private final NotificationPanelViewStateProvider mNotificationPanelViewStateProvider;
    private NotificationShelfController mNotificationShelfController;
    /* access modifiers changed from: private */
    public final NotificationStackScrollLayoutController mNotificationStackScrollLayoutController;
    private final NotificationStackSizeCalculator mNotificationStackSizeCalculator;
    private NotificationsQSContainerController mNotificationsQSContainerController;
    private int mOldLayoutDirection;
    private final OnEmptySpaceClickListener mOnEmptySpaceClickListener = new OnEmptySpaceClickListener();
    private final MyOnHeadsUpChangedListener mOnHeadsUpChangedListener = new MyOnHeadsUpChangedListener();
    private final OnHeightChangedListener mOnHeightChangedListener = new OnHeightChangedListener();
    private final OnOverscrollTopChangedListener mOnOverscrollTopChangedListener = new OnOverscrollTopChangedListener();
    /* access modifiers changed from: private */
    public boolean mOnlyAffordanceInThisMotion;
    private float mOverStretchAmount;
    private int mPanelAlpha;
    private final AnimatableProperty mPanelAlphaAnimator;
    private Runnable mPanelAlphaEndAction;
    private final AnimationProperties mPanelAlphaInPropertiesAnimator;
    private final AnimationProperties mPanelAlphaOutPropertiesAnimator;
    private final PanelEventsEmitter mPanelEventsEmitter;
    private boolean mPanelExpanded;
    private int mPositionMinSideMargin;
    private final ArrayList<Runnable> mPostCollapseRunnables;
    private final PowerManager mPowerManager;
    private ViewGroup mPreviewContainer;
    private final PrivacyDotViewController mPrivacyDotViewController;
    /* access modifiers changed from: private */
    public final PulseExpansionHandler mPulseExpansionHandler;
    /* access modifiers changed from: private */
    public boolean mPulsing;
    private final QRCodeScannerController mQRCodeScannerController;
    /* access modifiers changed from: private */
    public boolean mQSAnimatingHiddenFromCollapsed;
    C2301QS mQs;
    private boolean mQsAnimatorExpand;
    private int mQsClipBottom;
    private int mQsClipTop;
    /* access modifiers changed from: private */
    public ValueAnimator mQsClippingAnimation;
    private final Rect mQsClippingAnimationEndBounds;
    boolean mQsExpandImmediate;
    /* access modifiers changed from: private */
    public boolean mQsExpanded;
    private boolean mQsExpandedWhenExpandingStarted;
    /* access modifiers changed from: private */
    public ValueAnimator mQsExpansionAnimator;
    private boolean mQsExpansionEnabledAmbient = true;
    private boolean mQsExpansionEnabledPolicy = true;
    /* access modifiers changed from: private */
    public boolean mQsExpansionFromOverscroll;
    /* access modifiers changed from: private */
    public float mQsExpansionHeight;
    private int mQsFalsingThreshold;
    private FrameLayout mQsFrame;
    private QsFrameTranslateController mQsFrameTranslateController;
    /* access modifiers changed from: private */
    public boolean mQsFullyExpanded;
    private final Region mQsInterceptRegion;
    /* access modifiers changed from: private */
    public int mQsMaxExpansionHeight;
    /* access modifiers changed from: private */
    public int mQsMinExpansionHeight;
    private int mQsPeekHeight;
    private boolean mQsScrimEnabled = true;
    /* access modifiers changed from: private */
    public ValueAnimator mQsSizeChangeAnimator;
    private boolean mQsTouchAboveFalsingThreshold;
    /* access modifiers changed from: private */
    public boolean mQsTracking;
    private float mQsTranslationForFullShadeTransition;
    private VelocityTracker mQsVelocityTracker;
    private boolean mQsVisible;
    private final QuickAccessWalletController mQuickAccessWalletController;
    private float mQuickQsHeaderHeight;
    private final RecordingController mRecordingController;
    private final NotificationRemoteInputManager mRemoteInputManager;
    private int mScreenCornerRadius;
    /* access modifiers changed from: private */
    public ScreenOffAnimationController mScreenOffAnimationController;
    private final ScrimController mScrimController;
    private int mScrimCornerRadius;
    public final C2301QS.ScrollListener mScrollListener;
    private final SecureSettings mSecureSettings;
    private final SettingsChangeObserver mSettingsChangeObserver;
    /* access modifiers changed from: private */
    public final ShadeTransitionController mShadeTransitionController;
    private int mShelfAndLockIconOverlap;
    private int mShelfHeight;
    private boolean mShowIconsWhenExpanded;
    /* access modifiers changed from: private */
    public boolean mSplitShadeEnabled;
    private int mSplitShadeNotificationsScrimMarginBottom;
    private int mStackScrollerMeasuringPass;
    /* access modifiers changed from: private */
    public boolean mStackScrollerOverscrolling;
    private int mStatusBarHeaderHeightKeyguard;
    /* access modifiers changed from: private */
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private int mStatusBarMinHeight;
    final StatusBarStateListener mStatusBarStateListener = new StatusBarStateListener();
    private final PhoneStatusBarView.TouchEventHandler mStatusBarViewTouchEventHandler;
    private boolean mStatusViewCentered;
    private final SysUiState mSysUiState;
    /* access modifiers changed from: private */
    public final TapAgainViewController mTapAgainViewController;
    /* access modifiers changed from: private */
    public int mThemeResId;
    private ExpandableNotificationRow mTrackedHeadsUpNotification;
    private final ArrayList<Consumer<ExpandableNotificationRow>> mTrackingHeadsUpListeners = new ArrayList<>();
    private int mTrackingPointer;
    private int mTransitionToFullShadeQSPosition;
    /* access modifiers changed from: private */
    public float mTransitioningToFullShadeProgress;
    private boolean mTwoFingerQsExpandPossible;
    private float mUdfpsMaxYBurnInOffset;
    private final Executor mUiExecutor;
    private UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private final boolean mUseCombinedQSHeaders;
    private final UserManager mUserManager;
    private boolean mUserSetupComplete;
    /* access modifiers changed from: private */
    public final VibratorHelper mVibratorHelper;
    /* access modifiers changed from: private */
    public final NotificationPanelView mView;
    private final NotificationWakeUpCoordinator mWakeUpCoordinator;

    public interface NotificationPanelViewStateProvider {
        float getLockscreenShadeDragProgress();

        float getPanelViewExpandedHeight();

        boolean shouldHeadsUpBeVisible();
    }

    public void setHeaderDebugInfo(String str) {
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44613xf1de5604() {
        setHeadsUpAnimatingAway(false);
        updatePanelExpansionAndVisibility();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44614x2abeb6a3(Property property) {
        Runnable runnable = this.mPanelAlphaEndAction;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    @javax.inject.Inject
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public NotificationPanelViewController(com.android.systemui.statusbar.phone.NotificationPanelView r20, @com.android.systemui.dagger.qualifiers.Main android.content.res.Resources r21, @com.android.systemui.dagger.qualifiers.Main android.os.Handler r22, android.view.LayoutInflater r23, com.android.systemui.flags.FeatureFlags r24, com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator r25, com.android.systemui.statusbar.PulseExpansionHandler r26, com.android.systemui.statusbar.notification.DynamicPrivacyController r27, com.android.systemui.statusbar.phone.KeyguardBypassController r28, com.android.systemui.plugins.FalsingManager r29, com.android.systemui.classifier.FalsingCollector r30, com.android.systemui.statusbar.notification.NotificationEntryManager r31, com.android.systemui.statusbar.policy.KeyguardStateController r32, com.android.systemui.plugins.statusbar.StatusBarStateController r33, com.android.systemui.statusbar.window.StatusBarWindowStateController r34, com.android.systemui.statusbar.NotificationShadeWindowController r35, com.android.systemui.doze.DozeLog r36, com.android.systemui.statusbar.phone.DozeParameters r37, com.android.systemui.statusbar.CommandQueue r38, com.android.systemui.statusbar.VibratorHelper r39, com.android.internal.util.LatencyTracker r40, android.os.PowerManager r41, android.view.accessibility.AccessibilityManager r42, @com.android.systemui.dagger.qualifiers.DisplayId int r43, com.android.keyguard.KeyguardUpdateMonitor r44, com.android.internal.logging.MetricsLogger r45, android.app.ActivityManager r46, com.android.systemui.statusbar.policy.ConfigurationController r47, javax.inject.Provider<com.android.p019wm.shell.animation.FlingAnimationUtils.Builder> r48, com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager r49, com.android.systemui.statusbar.notification.ConversationNotificationManager r50, com.android.systemui.media.MediaHierarchyManager r51, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r52, com.android.systemui.statusbar.phone.NotificationsQSContainerController r53, com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r54, com.android.keyguard.dagger.KeyguardStatusViewComponent.Factory r55, com.android.keyguard.dagger.KeyguardQsUserSwitchComponent.Factory r56, com.android.keyguard.dagger.KeyguardUserSwitcherComponent.Factory r57, com.android.keyguard.dagger.KeyguardStatusBarViewComponent.Factory r58, com.android.systemui.statusbar.LockscreenShadeTransitionController r59, com.android.systemui.statusbar.phone.NotificationIconAreaController r60, com.android.systemui.biometrics.AuthController r61, com.android.systemui.statusbar.phone.ScrimController r62, android.os.UserManager r63, com.android.systemui.media.MediaDataManager r64, com.android.systemui.statusbar.NotificationShadeDepthController r65, com.android.systemui.statusbar.notification.stack.AmbientState r66, com.android.keyguard.LockIconViewController r67, com.android.systemui.media.KeyguardMediaController r68, com.android.systemui.statusbar.events.PrivacyDotViewController r69, com.android.systemui.statusbar.phone.TapAgainViewController r70, com.android.systemui.navigationbar.NavigationModeController r71, com.android.systemui.fragments.FragmentService r72, android.content.ContentResolver r73, com.android.systemui.wallet.controller.QuickAccessWalletController r74, com.android.systemui.qrcodescanner.controller.QRCodeScannerController r75, com.android.systemui.screenrecord.RecordingController r76, @com.android.systemui.dagger.qualifiers.Main java.util.concurrent.Executor r77, com.android.systemui.util.settings.SecureSettings r78, com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController r79, com.android.systemui.statusbar.phone.ScreenOffAnimationController r80, com.android.systemui.statusbar.phone.LockscreenGestureLogger r81, com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager r82, com.android.systemui.statusbar.NotificationRemoteInputManager r83, java.util.Optional<com.android.systemui.unfold.SysUIUnfoldComponent> r84, com.android.systemui.controls.dagger.ControlsComponent r85, com.android.internal.jank.InteractionJankMonitor r86, com.android.systemui.statusbar.QsFrameTranslateController r87, com.android.systemui.model.SysUiState r88, com.android.systemui.keyguard.KeyguardUnlockAnimationController r89, com.android.systemui.statusbar.notification.stack.NotificationListContainer r90, com.android.systemui.statusbar.phone.NotificationPanelViewController.PanelEventsEmitter r91, com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator r92, com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController r93, com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController r94, com.android.systemui.util.time.SystemClock r95, com.nothing.systemui.p024qs.NTQSStatusBarController r96, com.android.keyguard.EmergencyButtonController.Factory r97) {
        /*
            r19 = this;
            r10 = r19
            r5 = r20
            r15 = r24
            r13 = r26
            r12 = r59
            r14 = r84
            r8 = r94
            r0 = r19
            r1 = r20
            r2 = r29
            r4 = r32
            r6 = r35
            r3 = r36
            r7 = r39
            r9 = r40
            r11 = r49
            r8 = r52
            r14 = r66
            r12 = r81
            r13 = r82
            r15 = r86
            r16 = r89
            r17 = r95
            r18 = r33
            com.android.systemui.statusbar.SysuiStatusBarStateController r18 = (com.android.systemui.statusbar.SysuiStatusBarStateController) r18
            r5 = r18
            java.lang.Object r18 = r48.get()
            com.android.wm.shell.animation.FlingAnimationUtils$Builder r18 = (com.android.p019wm.shell.animation.FlingAnimationUtils.Builder) r18
            r10 = r18
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$OnHeightChangedListener r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$OnHeightChangedListener
            r1 = 0
            r2 = r19
            r0.<init>()
            r2.mOnHeightChangedListener = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$CollapseExpandAction r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$CollapseExpandAction
            r0.<init>()
            r2.mCollapseExpandAction = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$OnOverscrollTopChangedListener r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$OnOverscrollTopChangedListener
            r0.<init>()
            r2.mOnOverscrollTopChangedListener = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$KeyguardAffordanceHelperCallback r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$KeyguardAffordanceHelperCallback
            r0.<init>()
            r2.mKeyguardAffordanceHelperCallback = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$OnEmptySpaceClickListener r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$OnEmptySpaceClickListener
            r0.<init>()
            r2.mOnEmptySpaceClickListener = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$MyOnHeadsUpChangedListener r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$MyOnHeadsUpChangedListener
            r0.<init>()
            r2.mOnHeadsUpChangedListener = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$HeightListener r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$HeightListener
            r0.<init>()
            r2.mHeightListener = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$ConfigurationListener r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$ConfigurationListener
            r0.<init>()
            r2.mConfigurationListener = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$StatusBarStateListener r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$StatusBarStateListener
            r0.<init>()
            r2.mStatusBarStateListener = r0
            r0 = 1
            r2.mQsExpansionEnabledPolicy = r0
            r2.mQsExpansionEnabledAmbient = r0
            r3 = 0
            r2.mDisplayTopInset = r3
            r2.mDisplayRightInset = r3
            r2.mDisplayLeftInset = r3
            com.android.systemui.statusbar.phone.KeyguardClockPositionAlgorithm r4 = new com.android.systemui.statusbar.phone.KeyguardClockPositionAlgorithm
            r4.<init>()
            r2.mClockPositionAlgorithm = r4
            com.android.systemui.statusbar.phone.KeyguardClockPositionAlgorithm$Result r4 = new com.android.systemui.statusbar.phone.KeyguardClockPositionAlgorithm$Result
            r4.<init>()
            r2.mClockPositionResult = r4
            r2.mQsScrimEnabled = r0
            java.lang.String r4 = "lockscreen_affordance"
            r2.mLastCameraLaunchSource = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda14 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda14
            r4.<init>(r2)
            r2.mHeadsUpExistenceChangedRunnable = r4
            r2.mHideIconsDuringLaunchAnimation = r0
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r2.mTrackingHeadsUpListeners = r4
            java.lang.String r5 = "panelAlpha"
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda16 r6 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda16
            r6.<init>()
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda17 r7 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda17
            r7.<init>()
            r8 = 2131428553(0x7f0b04c9, float:1.8478754E38)
            r9 = 2131428552(0x7f0b04c8, float:1.8478752E38)
            r10 = 2131428551(0x7f0b04c7, float:1.847875E38)
            com.android.systemui.statusbar.notification.AnimatableProperty r4 = com.android.systemui.statusbar.notification.AnimatableProperty.from(r5, r6, r7, r8, r9, r10)
            r2.mPanelAlphaAnimator = r4
            com.android.systemui.statusbar.notification.stack.AnimationProperties r5 = new com.android.systemui.statusbar.notification.stack.AnimationProperties
            r5.<init>()
            r6 = 150(0x96, double:7.4E-322)
            com.android.systemui.statusbar.notification.stack.AnimationProperties r5 = r5.setDuration(r6)
            android.util.Property r6 = r4.getProperty()
            android.view.animation.Interpolator r7 = com.android.systemui.animation.Interpolators.ALPHA_OUT
            com.android.systemui.statusbar.notification.stack.AnimationProperties r5 = r5.setCustomInterpolator(r6, r7)
            r2.mPanelAlphaOutPropertiesAnimator = r5
            com.android.systemui.statusbar.notification.stack.AnimationProperties r5 = new com.android.systemui.statusbar.notification.stack.AnimationProperties
            r5.<init>()
            r6 = 200(0xc8, double:9.9E-322)
            com.android.systemui.statusbar.notification.stack.AnimationProperties r5 = r5.setDuration(r6)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda18 r6 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda18
            r6.<init>(r2)
            com.android.systemui.statusbar.notification.stack.AnimationProperties r5 = r5.setAnimationEndAction(r6)
            android.util.Property r4 = r4.getProperty()
            android.view.animation.Interpolator r6 = com.android.systemui.animation.Interpolators.ALPHA_IN
            com.android.systemui.statusbar.notification.stack.AnimationProperties r4 = r5.setCustomInterpolator(r4, r6)
            r2.mPanelAlphaInPropertiesAnimator = r4
            android.graphics.Rect r4 = new android.graphics.Rect
            r4.<init>()
            r2.mQsClippingAnimationEndBounds = r4
            r2.mQsClippingAnimation = r1
            android.graphics.Rect r4 = new android.graphics.Rect
            r4.<init>()
            r2.mKeyguardStatusAreaClipBounds = r4
            android.graphics.Region r4 = new android.graphics.Region
            r4.<init>()
            r2.mQsInterceptRegion = r4
            r4 = 1065353216(0x3f800000, float:1.0)
            r2.mKeyguardOnlyContentAlpha = r4
            r2.mKeyguardOnlyTransitionTranslationY = r3
            r2.mStatusViewCentered = r0
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r2.mPostCollapseRunnables = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$1 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$1
            r4.<init>()
            r2.mAccessibilityDelegate = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$2 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$2
            r4.<init>()
            r2.mFalsingTapListener = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$7 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$7
            r4.<init>()
            r2.mAnimateKeyguardBottomAreaInvisibleEndRunnable = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$12 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$12
            r4.<init>()
            r2.mScrollListener = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$13 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$13
            r4.<init>()
            r2.mFragmentListener = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$15 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$15
            r4.<init>()
            r2.mMaybeHideExpandedRunnable = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$17 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$17
            r4.<init>()
            r2.mStatusBarViewTouchEventHandler = r4
            com.android.systemui.statusbar.phone.NotificationPanelViewController$18 r4 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$18
            r4.<init>()
            r2.mNotificationPanelViewStateProvider = r4
            r2.mCurrentPanelState = r3
            r4 = r20
            r2.mView = r4
            r5 = r39
            r2.mVibratorHelper = r5
            r5 = r68
            r2.mKeyguardMediaController = r5
            r5 = r69
            r2.mPrivacyDotViewController = r5
            r5 = r74
            r2.mQuickAccessWalletController = r5
            r5 = r75
            r2.mQRCodeScannerController = r5
            r5 = r85
            r2.mControlsComponent = r5
            r5 = r45
            r2.mMetricsLogger = r5
            r5 = r46
            r2.mActivityManager = r5
            r5 = r47
            r2.mConfigurationController = r5
            r5 = r48
            r2.mFlingAnimationUtilsBuilder = r5
            r5 = r51
            r2.mMediaHierarchyManager = r5
            r5 = r52
            r2.mStatusBarKeyguardViewManager = r5
            r5 = r53
            r2.mNotificationsQSContainerController = r5
            r6 = r90
            r2.mNotificationListContainer = r6
            r6 = r92
            r2.mNotificationStackSizeCalculator = r6
            r53.init()
            r5 = r54
            r2.mNotificationStackScrollLayoutController = r5
            r5 = r60
            r2.mNotificationIconAreaController = r5
            r5 = r55
            r2.mKeyguardStatusViewComponentFactory = r5
            r5 = r58
            r2.mKeyguardStatusBarViewComponentFactory = r5
            r5 = r65
            r2.mDepthController = r5
            r5 = r73
            r2.mContentResolver = r5
            r5 = r56
            r2.mKeyguardQsUserSwitchComponentFactory = r5
            r5 = r57
            r2.mKeyguardUserSwitcherComponentFactory = r5
            r5 = r97
            r2.mEmergencyButtonControllerFactory = r5
            r5 = r72
            r2.mFragmentService = r5
            com.android.systemui.statusbar.phone.NotificationPanelViewController$SettingsChangeObserver r5 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$SettingsChangeObserver
            r6 = r22
            r5.<init>(r6)
            r2.mSettingsChangeObserver = r5
            android.content.res.Resources r5 = r2.mResources
            boolean r5 = com.android.systemui.util.LargeScreenUtils.shouldUseSplitNotificationShade(r5)
            r2.mSplitShadeEnabled = r5
            r4.setWillNotDraw(r0)
            r0 = r79
            r2.mLargeScreenShadeHeaderController = r0
            r0 = r23
            r2.mLayoutInflater = r0
            r0 = r24
            r2.mFeatureFlags = r0
            r5 = r29
            r2.mFalsingManager = r5
            r5 = r30
            r2.mFalsingCollector = r5
            r5 = r41
            r2.mPowerManager = r5
            r5 = r25
            r2.mWakeUpCoordinator = r5
            r5 = r42
            r2.mAccessibilityManager = r5
            java.lang.String r5 = r19.determineAccessibilityPaneTitle()
            r4.setAccessibilityPaneTitle(r5)
            r5 = 255(0xff, float:3.57E-43)
            r2.setPanelAlpha(r5, r3)
            r5 = r38
            r2.mCommandQueue = r5
            r5 = r76
            r2.mRecordingController = r5
            r5 = r43
            r2.mDisplayId = r5
            r5 = r26
            r2.mPulseExpansionHandler = r5
            r6 = r37
            r2.mDozeParameters = r6
            r6 = r62
            r2.mScrimController = r6
            r6 = r63
            r2.mUserManager = r6
            r6 = r64
            r2.mMediaDataManager = r6
            r6 = r70
            r2.mTapAgainViewController = r6
            r6 = r77
            r2.mUiExecutor = r6
            r6 = r78
            r2.mSecureSettings = r6
            r6 = r86
            r2.mInteractionJankMonitor = r6
            r6 = r88
            r2.mSysUiState = r6
            r6 = r91
            r2.mPanelEventsEmitter = r6
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda19 r6 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda19
            r6.<init>(r2)
            r5.setPulseExpandAbortListener(r6)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda20 r5 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda20
            r5.<init>(r2)
            r6 = r34
            r6.addListener(r5)
            android.content.Context r5 = r20.getContext()
            int r5 = r5.getThemeResId()
            r2.mThemeResId = r5
            r5 = r28
            r2.mKeyguardBypassController = r5
            r5 = r44
            r2.mUpdateMonitor = r5
            r5 = r59
            r2.mLockscreenShadeTransitionController = r5
            r6 = r94
            r2.mShadeTransitionController = r6
            r5.setNotificationPanelController(r2)
            r6.setNotificationPanelViewController(r2)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$DynamicPrivacyControlListener r5 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$DynamicPrivacyControlListener
            r5.<init>()
            r6 = r27
            r6.addListener(r5)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda21 r5 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda21
            r5.<init>(r2)
            r6 = r82
            r6.addStateListener(r5)
            r5 = 2
            float[] r5 = new float[r5]
            r5 = {1065353216, 0} // fill-array
            android.animation.ValueAnimator r5 = android.animation.ValueAnimator.ofFloat(r5)
            r2.mBottomAreaShadeAlphaAnimator = r5
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda1 r6 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda1
            r6.<init>(r2)
            r5.addUpdateListener(r6)
            r6 = 160(0xa0, double:7.9E-322)
            r5.setDuration(r6)
            android.view.animation.Interpolator r6 = com.android.systemui.animation.Interpolators.ALPHA_OUT
            r5.setInterpolator(r6)
            r5 = r31
            r2.mEntryManager = r5
            r5 = r50
            r2.mConversationNotificationManager = r5
            r5 = r61
            r2.mAuthController = r5
            r5 = r67
            r2.mLockIconViewController = r5
            r5 = r80
            r2.mScreenOffAnimationController = r5
            r5 = r93
            r2.mUnlockedScreenOffAnimationController = r5
            r5 = r83
            r2.mRemoteInputManager = r5
            com.android.systemui.statusbar.phone.NPVCDownEventState$Buffer r5 = new com.android.systemui.statusbar.phone.NPVCDownEventState$Buffer
            r6 = 50
            r5.<init>(r6)
            r2.mLastDownEvents = r5
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda2 r5 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda2
            r5.<init>(r2)
            r6 = r71
            int r5 = r6.addListener(r5)
            boolean r5 = com.android.systemui.shared.system.QuickStepContract.isGesturalMode(r5)
            r2.mIsGestureNavigation = r5
            r4.setBackgroundColor(r3)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$OnAttachStateChangeListener r3 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$OnAttachStateChangeListener
            r3.<init>()
            r4.addOnAttachStateChangeListener(r3)
            boolean r5 = r20.isAttachedToWindow()
            if (r5 == 0) goto L_0x02f4
            r3.onViewAttachedToWindow(r4)
        L_0x02f4:
            com.android.systemui.statusbar.phone.NotificationPanelViewController$OnApplyWindowInsetsListener r3 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$OnApplyWindowInsetsListener
            r3.<init>()
            r4.setOnApplyWindowInsetsListener(r3)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda3 r1 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda3
            r1.<init>()
            r3 = r84
            java.util.Optional r1 = r3.map(r1)
            r2.mKeyguardUnfoldTransition = r1
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda15 r1 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda15
            r1.<init>()
            java.util.Optional r1 = r3.map(r1)
            r2.mNotificationPanelUnfoldAnimationController = r1
            r1 = r87
            r2.mQsFrameTranslateController = r1
            r19.updateUserSwitcherFlags()
            r19.onFinishInflate()
            com.android.systemui.flags.BooleanFlag r1 = com.android.systemui.flags.Flags.COMBINED_QS_HEADERS
            boolean r0 = r0.isEnabled((com.android.systemui.flags.BooleanFlag) r1)
            r2.mUseCombinedQSHeaders = r0
            com.android.systemui.statusbar.phone.NotificationPanelViewController$3 r0 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$3
            r0.<init>()
            r1 = r89
            r1.addKeyguardUnlockAnimationListener(r0)
            r0 = r96
            r2.mNTQSStatusBarController = r0
            r96.init()
            com.nothing.systemui.statusbar.phone.NotificationPanelViewControllerEx r0 = r2.mEx
            if (r0 != 0) goto L_0x0342
            com.nothing.systemui.statusbar.phone.NotificationPanelViewControllerEx r0 = new com.nothing.systemui.statusbar.phone.NotificationPanelViewControllerEx
            r0.<init>(r2)
            r2.mEx = r0
        L_0x0342:
            com.nothing.systemui.statusbar.phone.NotificationPanelViewControllerEx r0 = r2.mEx
            r0.makeQsStatusBarView(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.<init>(com.android.systemui.statusbar.phone.NotificationPanelView, android.content.res.Resources, android.os.Handler, android.view.LayoutInflater, com.android.systemui.flags.FeatureFlags, com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator, com.android.systemui.statusbar.PulseExpansionHandler, com.android.systemui.statusbar.notification.DynamicPrivacyController, com.android.systemui.statusbar.phone.KeyguardBypassController, com.android.systemui.plugins.FalsingManager, com.android.systemui.classifier.FalsingCollector, com.android.systemui.statusbar.notification.NotificationEntryManager, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.statusbar.window.StatusBarWindowStateController, com.android.systemui.statusbar.NotificationShadeWindowController, com.android.systemui.doze.DozeLog, com.android.systemui.statusbar.phone.DozeParameters, com.android.systemui.statusbar.CommandQueue, com.android.systemui.statusbar.VibratorHelper, com.android.internal.util.LatencyTracker, android.os.PowerManager, android.view.accessibility.AccessibilityManager, int, com.android.keyguard.KeyguardUpdateMonitor, com.android.internal.logging.MetricsLogger, android.app.ActivityManager, com.android.systemui.statusbar.policy.ConfigurationController, javax.inject.Provider, com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager, com.android.systemui.statusbar.notification.ConversationNotificationManager, com.android.systemui.media.MediaHierarchyManager, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager, com.android.systemui.statusbar.phone.NotificationsQSContainerController, com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController, com.android.keyguard.dagger.KeyguardStatusViewComponent$Factory, com.android.keyguard.dagger.KeyguardQsUserSwitchComponent$Factory, com.android.keyguard.dagger.KeyguardUserSwitcherComponent$Factory, com.android.keyguard.dagger.KeyguardStatusBarViewComponent$Factory, com.android.systemui.statusbar.LockscreenShadeTransitionController, com.android.systemui.statusbar.phone.NotificationIconAreaController, com.android.systemui.biometrics.AuthController, com.android.systemui.statusbar.phone.ScrimController, android.os.UserManager, com.android.systemui.media.MediaDataManager, com.android.systemui.statusbar.NotificationShadeDepthController, com.android.systemui.statusbar.notification.stack.AmbientState, com.android.keyguard.LockIconViewController, com.android.systemui.media.KeyguardMediaController, com.android.systemui.statusbar.events.PrivacyDotViewController, com.android.systemui.statusbar.phone.TapAgainViewController, com.android.systemui.navigationbar.NavigationModeController, com.android.systemui.fragments.FragmentService, android.content.ContentResolver, com.android.systemui.wallet.controller.QuickAccessWalletController, com.android.systemui.qrcodescanner.controller.QRCodeScannerController, com.android.systemui.screenrecord.RecordingController, java.util.concurrent.Executor, com.android.systemui.util.settings.SecureSettings, com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController, com.android.systemui.statusbar.phone.ScreenOffAnimationController, com.android.systemui.statusbar.phone.LockscreenGestureLogger, com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager, com.android.systemui.statusbar.NotificationRemoteInputManager, java.util.Optional, com.android.systemui.controls.dagger.ControlsComponent, com.android.internal.jank.InteractionJankMonitor, com.android.systemui.statusbar.QsFrameTranslateController, com.android.systemui.model.SysUiState, com.android.systemui.keyguard.KeyguardUnlockAnimationController, com.android.systemui.statusbar.notification.stack.NotificationListContainer, com.android.systemui.statusbar.phone.NotificationPanelViewController$PanelEventsEmitter, com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator, com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController, com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController, com.android.systemui.util.time.SystemClock, com.nothing.systemui.qs.NTQSStatusBarController, com.android.keyguard.EmergencyButtonController$Factory):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44615x639f1742() {
        C2301QS qs = this.mQs;
        if (qs != null) {
            qs.animateHeaderSlidingOut();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44616x9c7f77e1(ValueAnimator valueAnimator) {
        this.mBottomAreaShadeAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateKeyguardBottomAreaAlpha();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44617xd55fd880(int i) {
        this.mIsGestureNavigation = QuickStepContract.isGesturalMode(i);
    }

    /* access modifiers changed from: package-private */
    public void onFinishInflate() {
        KeyguardUserSwitcherView keyguardUserSwitcherView;
        loadDimens();
        this.mKeyguardStatusBar = (KeyguardStatusBarView) this.mView.findViewById(C1893R.C1897id.keyguard_header);
        FrameLayout frameLayout = null;
        if (!this.mKeyguardUserSwitcherEnabled || !this.mUserManager.isUserSwitcherEnabled(this.mResources.getBoolean(C1893R.bool.qs_show_user_switcher_for_single_user))) {
            keyguardUserSwitcherView = null;
        } else if (this.mKeyguardQsUserSwitchEnabled) {
            frameLayout = (FrameLayout) ((ViewStub) this.mView.findViewById(C1893R.C1897id.keyguard_qs_user_switch_stub)).inflate();
            keyguardUserSwitcherView = null;
        } else {
            keyguardUserSwitcherView = (KeyguardUserSwitcherView) ((ViewStub) this.mView.findViewById(C1893R.C1897id.keyguard_user_switcher_stub)).inflate();
        }
        KeyguardStatusBarViewController keyguardStatusBarViewController = this.mKeyguardStatusBarViewComponentFactory.build(this.mKeyguardStatusBar, this.mNotificationPanelViewStateProvider).getKeyguardStatusBarViewController();
        this.mKeyguardStatusBarViewController = keyguardStatusBarViewController;
        keyguardStatusBarViewController.init();
        this.mNotificationContainerParent = (NotificationsQuickSettingsContainer) this.mView.findViewById(C1893R.C1897id.notification_container_parent);
        updateViewControllers((KeyguardStatusView) this.mView.findViewById(C1893R.C1897id.keyguard_status_view), frameLayout, keyguardUserSwitcherView);
        this.mNotificationStackScrollLayoutController.attach((NotificationStackScrollLayout) this.mView.findViewById(C1893R.C1897id.notification_stack_scroller));
        this.mNotificationStackScrollLayoutController.setOnHeightChangedListener(this.mOnHeightChangedListener);
        this.mNotificationStackScrollLayoutController.setOverscrollTopChangedListener(this.mOnOverscrollTopChangedListener);
        this.mNotificationStackScrollLayoutController.setOnScrollListener(new NotificationPanelViewController$$ExternalSyntheticLambda6(this));
        this.mNotificationStackScrollLayoutController.setOnStackYChanged(new NotificationPanelViewController$$ExternalSyntheticLambda7(this));
        this.mNotificationStackScrollLayoutController.setOnEmptySpaceClickListener(this.mOnEmptySpaceClickListener);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        addTrackingHeadsUpListener(new NotificationPanelViewController$$ExternalSyntheticLambda8(notificationStackScrollLayoutController));
        this.mKeyguardBottomArea = (KeyguardBottomAreaView) this.mView.findViewById(C1893R.C1897id.keyguard_bottom_area);
        this.mPreviewContainer = (ViewGroup) this.mView.findViewById(C1893R.C1897id.preview_container);
        this.mKeyguardBottomArea.setPreviewContainer(this.mPreviewContainer);
        initBottomArea();
        this.mWakeUpCoordinator.setStackScroller(this.mNotificationStackScrollLayoutController);
        this.mQsFrame = (FrameLayout) this.mView.findViewById(C1893R.C1897id.qs_frame);
        this.mPulseExpansionHandler.setUp(this.mNotificationStackScrollLayoutController);
        this.mWakeUpCoordinator.addListener(new NotificationWakeUpCoordinator.WakeUpListener() {
            public void onFullyHiddenChanged(boolean z) {
                NotificationPanelViewController.this.mKeyguardStatusBarViewController.updateForHeadsUp();
            }

            public void onPulseExpansionChanged(boolean z) {
                if (NotificationPanelViewController.this.mKeyguardBypassController.getBypassEnabled()) {
                    NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                }
            }
        });
        this.mView.setRtlChangeListener(new NotificationPanelViewController$$ExternalSyntheticLambda9(this));
        this.mView.setAccessibilityDelegate(this.mAccessibilityDelegate);
        if (this.mSplitShadeEnabled) {
            updateResources();
        }
        this.mTapAgainViewController.init();
        this.mKeyguardUnfoldTransition.ifPresent(new NotificationPanelViewController$$ExternalSyntheticLambda10(this));
        this.mNotificationPanelUnfoldAnimationController.ifPresent(new NotificationPanelViewController$$ExternalSyntheticLambda12(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$6$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44618x8d3b8be9(int i) {
        if (i != this.mOldLayoutDirection) {
            this.mAffordanceHelper.onRtlPropertiesChanged();
            this.mOldLayoutDirection = i;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$7$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44619xc61bec88(KeyguardUnfoldTransition keyguardUnfoldTransition) {
        keyguardUnfoldTransition.setup(this.mView);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$8$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44620xfefc4d27(NotificationPanelUnfoldAnimationController notificationPanelUnfoldAnimationController) {
        notificationPanelUnfoldAnimationController.setup(this.mNotificationContainerParent);
    }

    /* access modifiers changed from: protected */
    public void loadDimens() {
        super.loadDimens();
        this.mFlingAnimationUtils = this.mFlingAnimationUtilsBuilder.get().setMaxLengthSeconds(0.4f).build();
        this.mStatusBarMinHeight = SystemBarUtils.getStatusBarHeight(this.mView.getContext());
        this.mStatusBarHeaderHeightKeyguard = Utils.getStatusBarHeaderHeightKeyguard(this.mView.getContext());
        this.mQsPeekHeight = this.mResources.getDimensionPixelSize(C1893R.dimen.qs_peek_height);
        this.mClockPositionAlgorithm.loadDimens(this.mResources);
        this.mQsFalsingThreshold = this.mResources.getDimensionPixelSize(C1893R.dimen.qs_falsing_threshold);
        this.mPositionMinSideMargin = this.mResources.getDimensionPixelSize(C1893R.dimen.notification_panel_min_side_margin);
        this.mIndicationBottomPadding = this.mResources.getDimensionPixelSize(C1893R.dimen.keyguard_indication_bottom_padding);
        this.mShelfHeight = this.mResources.getDimensionPixelSize(C1893R.dimen.notification_shelf_height);
        this.mDarkIconSize = this.mResources.getDimensionPixelSize(C1893R.dimen.status_bar_icon_drawing_size_dark);
        this.mHeadsUpInset = SystemBarUtils.getStatusBarHeight(this.mView.getContext()) + this.mResources.getDimensionPixelSize(C1893R.dimen.heads_up_status_bar_padding);
        this.mDistanceForQSFullShadeTransition = this.mResources.getDimensionPixelSize(C1893R.dimen.lockscreen_shade_qs_transition_distance);
        this.mMaxOverscrollAmountForPulse = this.mResources.getDimensionPixelSize(C1893R.dimen.pulse_expansion_max_top_overshoot);
        this.mScrimCornerRadius = this.mResources.getDimensionPixelSize(C1893R.dimen.notification_scrim_corner_radius);
        this.mScreenCornerRadius = (int) ScreenDecorationsUtils.getWindowCornerRadius(this.mView.getContext());
        this.mLockscreenNotificationQSPadding = this.mResources.getDimensionPixelSize(C1893R.dimen.notification_side_paddings);
        this.mUdfpsMaxYBurnInOffset = (float) this.mResources.getDimensionPixelSize(C1893R.dimen.udfps_burn_in_offset_y);
    }

    private void updateViewControllers(KeyguardStatusView keyguardStatusView, FrameLayout frameLayout, KeyguardUserSwitcherView keyguardUserSwitcherView) {
        KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewComponentFactory.build(keyguardStatusView).getKeyguardStatusViewController();
        this.mKeyguardStatusViewController = keyguardStatusViewController;
        keyguardStatusViewController.init();
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(false);
        }
        this.mKeyguardQsUserSwitchController = null;
        this.mKeyguardUserSwitcherController = null;
        if (frameLayout != null) {
            KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchComponentFactory.build(frameLayout).getKeyguardQsUserSwitchController();
            this.mKeyguardQsUserSwitchController = keyguardQsUserSwitchController;
            keyguardQsUserSwitchController.init();
            this.mKeyguardStatusBarViewController.setKeyguardUserSwitcherEnabled(true);
        } else if (keyguardUserSwitcherView != null) {
            KeyguardUserSwitcherController keyguardUserSwitcherController2 = this.mKeyguardUserSwitcherComponentFactory.build(keyguardUserSwitcherView).getKeyguardUserSwitcherController();
            this.mKeyguardUserSwitcherController = keyguardUserSwitcherController2;
            keyguardUserSwitcherController2.init();
            this.mKeyguardStatusBarViewController.setKeyguardUserSwitcherEnabled(true);
        } else {
            this.mKeyguardStatusBarViewController.setKeyguardUserSwitcherEnabled(false);
        }
    }

    public boolean hasCustomClock() {
        return this.mKeyguardStatusViewController.hasCustomClock();
    }

    private void setCentralSurfaces(CentralSurfaces centralSurfaces) {
        this.mCentralSurfaces = centralSurfaces;
        this.mKeyguardBottomArea.setCentralSurfaces(this.mCentralSurfaces);
    }

    public void updateResources() {
        int i;
        this.mSplitShadeNotificationsScrimMarginBottom = this.mResources.getDimensionPixelSize(C1893R.dimen.split_shade_notifications_scrim_margin_bottom);
        this.mShelfAndLockIconOverlap = this.mResources.getDimensionPixelSize(C1893R.dimen.shelf_and_lock_icon_overlap);
        boolean shouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(this.mResources);
        boolean z = this.mSplitShadeEnabled != shouldUseSplitNotificationShade;
        this.mSplitShadeEnabled = shouldUseSplitNotificationShade;
        boolean shouldUseLargeScreenShadeHeader = LargeScreenUtils.shouldUseLargeScreenShadeHeader(this.mView.getResources());
        if (this.mEx == null) {
            this.mEx = new NotificationPanelViewControllerEx(this);
        }
        this.mEx.setHasQSLayout(false);
        this.mEx.setCanQsCollapseForLand(true);
        C2301QS qs = this.mQs;
        if (qs != null) {
            qs.setInSplitShade(this.mSplitShadeEnabled);
        }
        int dimensionPixelSize = this.mResources.getDimensionPixelSize(C1893R.dimen.large_screen_shade_header_height);
        this.mLargeScreenShadeHeaderHeight = dimensionPixelSize;
        if (!shouldUseLargeScreenShadeHeader) {
            dimensionPixelSize = SystemBarUtils.getQuickQsOffsetHeight(this.mView.getContext());
        }
        this.mQuickQsHeaderHeight = (float) dimensionPixelSize;
        if (shouldUseLargeScreenShadeHeader) {
            i = this.mLargeScreenShadeHeaderHeight;
        } else {
            i = this.mResources.getDimensionPixelSize(C1893R.dimen.notification_panel_margin_top);
        }
        this.mLargeScreenShadeHeaderController.setActive(false);
        this.mAmbientState.setStackTopMargin(i);
        this.mNotificationsQSContainerController.updateResources();
        updateKeyguardStatusViewAlignment(false);
        this.mKeyguardMediaController.refreshMediaPosition();
        if (z) {
            if (!isOnKeyguard() && this.mPanelExpanded) {
                setQsExpanded(this.mSplitShadeEnabled);
            }
            updateClockAppearance();
            updateQsState();
            this.mNotificationStackScrollLayoutController.updateFooter();
        }
        this.mEx.updateResources();
        this.mEx.updateQsStatusBarVisibility();
    }

    private View reInflateStub(int i, int i2, int i3, boolean z) {
        View findViewById = this.mView.findViewById(i);
        if (findViewById == null) {
            return z ? ((ViewStub) this.mView.findViewById(i2)).inflate() : findViewById;
        }
        int indexOfChild = this.mView.indexOfChild(findViewById);
        this.mView.removeView(findViewById);
        if (z) {
            View inflate = this.mLayoutInflater.inflate(i3, this.mView, false);
            this.mView.addView(inflate, indexOfChild);
            return inflate;
        }
        ViewStub viewStub = new ViewStub(this.mView.getContext(), i3);
        viewStub.setId(i2);
        this.mView.addView(viewStub, indexOfChild);
        return null;
    }

    /* access modifiers changed from: package-private */
    public void reInflateViews() {
        if (DEBUG_LOGCAT) {
            Log.d(TAG, "reInflateViews");
        }
        KeyguardStatusView keyguardStatusView = (KeyguardStatusView) this.mNotificationContainerParent.findViewById(C1893R.C1897id.keyguard_status_view);
        int indexOfChild = this.mNotificationContainerParent.indexOfChild(keyguardStatusView);
        this.mNotificationContainerParent.removeView(keyguardStatusView);
        KeyguardStatusView keyguardStatusView2 = (KeyguardStatusView) this.mLayoutInflater.inflate(C1893R.layout.keyguard_status_view, this.mNotificationContainerParent, false);
        this.mNotificationContainerParent.addView(keyguardStatusView2, indexOfChild);
        boolean z = true;
        this.mStatusViewCentered = true;
        attachSplitShadeMediaPlayerContainer((FrameLayout) keyguardStatusView2.findViewById(C1893R.C1897id.status_view_media_container));
        updateResources();
        updateUserSwitcherFlags();
        boolean isUserSwitcherEnabled = this.mUserManager.isUserSwitcherEnabled(this.mResources.getBoolean(C1893R.bool.qs_show_user_switcher_for_single_user));
        boolean z2 = this.mKeyguardQsUserSwitchEnabled;
        boolean z3 = z2 && isUserSwitcherEnabled;
        if (z2 || !this.mKeyguardUserSwitcherEnabled || !isUserSwitcherEnabled) {
            z = false;
        }
        updateViewControllers((KeyguardStatusView) this.mView.findViewById(C1893R.C1897id.keyguard_status_view), (FrameLayout) reInflateStub(C1893R.C1897id.keyguard_qs_user_switch_view, C1893R.C1897id.keyguard_qs_user_switch_stub, C1893R.layout.keyguard_qs_user_switch, z3), (KeyguardUserSwitcherView) reInflateStub(C1893R.C1897id.keyguard_user_switcher_view, C1893R.C1897id.keyguard_user_switcher_stub, C1893R.layout.keyguard_user_switcher, z));
        int indexOfChild2 = this.mView.indexOfChild(this.mKeyguardBottomArea);
        this.mView.removeView(this.mKeyguardBottomArea);
        KeyguardBottomAreaView keyguardBottomAreaView = this.mKeyguardBottomArea;
        this.mKeyguardBottomArea = (KeyguardBottomAreaView) this.mLayoutInflater.inflate(C1893R.layout.keyguard_bottom_area, this.mView, false);
        this.mKeyguardBottomArea.initFrom(keyguardBottomAreaView);
        this.mKeyguardBottomArea.setPreviewContainer(this.mPreviewContainer);
        this.mView.addView(this.mKeyguardBottomArea, indexOfChild2);
        initBottomArea();
        this.mKeyguardIndicationController.setIndicationArea(this.mKeyguardBottomArea);
        this.mStatusBarStateListener.onDozeAmountChanged(this.mStatusBarStateController.getDozeAmount(), this.mStatusBarStateController.getInterpolatedDozeAmount());
        KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewController;
        int i = this.mBarState;
        keyguardStatusViewController.setKeyguardStatusViewVisibility(i, false, false, i);
        KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchController;
        if (keyguardQsUserSwitchController != null) {
            int i2 = this.mBarState;
            keyguardQsUserSwitchController.setKeyguardQsUserSwitchVisibility(i2, false, false, i2);
        }
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            int i3 = this.mBarState;
            keyguardUserSwitcherController.setKeyguardUserSwitcherVisibility(i3, false, false, i3);
        }
        setKeyguardBottomAreaVisibility(this.mBarState, false);
        this.mKeyguardUnfoldTransition.ifPresent(new NotificationPanelViewController$$ExternalSyntheticLambda0(this));
        this.mNotificationPanelUnfoldAnimationController.ifPresent(new NotificationPanelViewController$$ExternalSyntheticLambda11(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$reInflateViews$9$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44622xbdee7f67(KeyguardUnfoldTransition keyguardUnfoldTransition) {
        keyguardUnfoldTransition.setup(this.mView);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$reInflateViews$10$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44621x8dd9d247(NotificationPanelUnfoldAnimationController notificationPanelUnfoldAnimationController) {
        notificationPanelUnfoldAnimationController.setup(this.mView);
    }

    private void attachSplitShadeMediaPlayerContainer(FrameLayout frameLayout) {
        this.mKeyguardMediaController.attachSplitShadeContainer(frameLayout);
    }

    private void initBottomArea() {
        this.mAffordanceHelper = new KeyguardAffordanceHelper(this.mKeyguardAffordanceHelperCallback, this.mView.getContext(), this.mFalsingManager);
        this.mKeyguardBottomArea.setAffordanceHelper(this.mAffordanceHelper);
        this.mKeyguardBottomArea.setCentralSurfaces(this.mCentralSurfaces);
        this.mKeyguardBottomArea.setUserSetupComplete(this.mUserSetupComplete);
        this.mKeyguardBottomArea.setFalsingManager(this.mFalsingManager);
        this.mKeyguardBottomArea.initWallet(this.mQuickAccessWalletController);
        this.mKeyguardBottomArea.initControls(this.mControlsComponent);
        this.mKeyguardBottomArea.initQRCodeScanner(this.mQRCodeScannerController);
        EmergencyButtonController create = this.mEmergencyButtonControllerFactory.create((EmergencyButton) this.mKeyguardBottomArea.findViewById(C1893R.C1897id.emergency_call_button));
        this.mEmergencyButtonController = create;
        create.init();
    }

    /* access modifiers changed from: package-private */
    public void setMaxDisplayedNotifications(int i) {
        this.mMaxAllowedKeyguardNotifications = i;
    }

    /* access modifiers changed from: private */
    public void updateMaxDisplayedNotifications(boolean z) {
        if (z) {
            this.mMaxAllowedKeyguardNotifications = Math.max(computeMaxKeyguardNotifications(), 1);
        } else if (SPEW_LOGCAT) {
            Log.d(TAG, "Skipping computeMaxKeyguardNotifications() by request");
        }
        if (!this.mKeyguardShowing || this.mKeyguardBypassController.getBypassEnabled()) {
            this.mNotificationStackScrollLayoutController.setMaxDisplayedNotifications(-1);
            this.mNotificationStackScrollLayoutController.setKeyguardBottomPaddingForDebug(-1.0f);
            return;
        }
        this.mNotificationStackScrollLayoutController.setMaxDisplayedNotifications(this.mMaxAllowedKeyguardNotifications);
        this.mNotificationStackScrollLayoutController.setKeyguardBottomPaddingForDebug(this.mKeyguardNotificationBottomPadding);
    }

    /* access modifiers changed from: private */
    public boolean shouldAvoidChangingNotificationsCount() {
        return this.mHintAnimationRunning || this.mUnlockedScreenOffAnimationController.isAnimationPlaying();
    }

    public void setKeyguardIndicationController(KeyguardIndicationController keyguardIndicationController) {
        this.mKeyguardIndicationController = keyguardIndicationController;
        keyguardIndicationController.setIndicationArea(this.mKeyguardBottomArea);
    }

    /* access modifiers changed from: private */
    public void updateGestureExclusionRect() {
        List list;
        Rect calculateGestureExclusionRect = calculateGestureExclusionRect();
        NotificationPanelView notificationPanelView = this.mView;
        if (calculateGestureExclusionRect.isEmpty()) {
            list = Collections.EMPTY_LIST;
        } else {
            list = Collections.singletonList(calculateGestureExclusionRect);
        }
        notificationPanelView.setSystemGestureExclusionRects(list);
    }

    private Rect calculateGestureExclusionRect() {
        Region calculateTouchableRegion = this.mStatusBarTouchableRegionManager.calculateTouchableRegion();
        Rect bounds = (!isFullyCollapsed() || calculateTouchableRegion == null) ? null : calculateTouchableRegion.getBounds();
        return bounds != null ? bounds : EMPTY_RECT;
    }

    /* access modifiers changed from: private */
    public void setIsFullWidth(boolean z) {
        this.mIsFullWidth = z;
        this.mScrimController.setClipsQsScrim(z);
        this.mNotificationStackScrollLayoutController.setIsFullWidth(z);
    }

    /* access modifiers changed from: private */
    public void startQsSizeChangeAnimation(int i, int i2) {
        ValueAnimator valueAnimator = this.mQsSizeChangeAnimator;
        if (valueAnimator != null) {
            i = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            this.mQsSizeChangeAnimator.cancel();
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
        this.mQsSizeChangeAnimator = ofInt;
        ofInt.setDuration(300);
        this.mQsSizeChangeAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mQsSizeChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                NotificationPanelViewController.this.requestPanelHeightUpdate();
                NotificationPanelViewController.this.mQs.setHeightOverride(((Integer) NotificationPanelViewController.this.mQsSizeChangeAnimator.getAnimatedValue()).intValue());
            }
        });
        this.mQsSizeChangeAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ValueAnimator unused = NotificationPanelViewController.this.mQsSizeChangeAnimator = null;
            }
        });
        this.mQsSizeChangeAnimator.start();
    }

    /* access modifiers changed from: private */
    public void positionClockAndNotifications() {
        positionClockAndNotifications(false);
    }

    /* access modifiers changed from: private */
    public void positionClockAndNotifications(boolean z) {
        int i;
        boolean isAddOrRemoveAnimationPending = this.mNotificationStackScrollLayoutController.isAddOrRemoveAnimationPending();
        boolean isOnKeyguard = isOnKeyguard();
        if (isOnKeyguard || z) {
            updateClockAppearance();
        }
        if (isOnKeyguard) {
            i = this.mClockPositionResult.stackScrollerPaddingExpanded;
        } else if (this.mSplitShadeEnabled) {
            i = 0;
        } else {
            i = getUnlockedStackScrollerPadding();
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        if (this.mInSplitShade) {
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
        float f;
        float f2;
        float f3;
        int i = this.mStatusBarHeaderHeightKeyguard;
        boolean bypassEnabled = this.mKeyguardBypassController.getBypassEnabled();
        boolean z = this.mNotificationStackScrollLayoutController.getVisibleNotificationCount() != 0 || this.mMediaDataManager.hasActiveMediaOrRecommendation();
        boolean z2 = this.mSplitShadeEnabled && this.mMediaDataManager.hasActiveMediaOrRecommendation();
        boolean shouldAnimateClockChange = this.mScreenOffAnimationController.shouldAnimateClockChange();
        if ((!z || this.mSplitShadeEnabled) && (!z2 || this.mDozing)) {
            this.mKeyguardStatusViewController.displayClock(0, shouldAnimateClockChange);
        } else {
            this.mKeyguardStatusViewController.displayClock(1, shouldAnimateClockChange);
        }
        updateKeyguardStatusViewAlignment(true);
        KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchController;
        int userIconHeight = keyguardQsUserSwitchController != null ? keyguardQsUserSwitchController.getUserIconHeight() : 0;
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            userIconHeight = keyguardUserSwitcherController.getHeight();
        }
        int i2 = userIconHeight;
        if (this.mScreenOffAnimationController.shouldExpandNotifications()) {
            f = 1.0f;
        } else {
            f = getExpandedFraction();
        }
        if (this.mScreenOffAnimationController.shouldExpandNotifications()) {
            f2 = 1.0f;
        } else {
            f2 = this.mInterpolatedDarkAmount;
        }
        if (!this.mUpdateMonitor.isUdfpsEnrolled() || this.mAuthController.getUdfpsProps().size() <= 0) {
            f3 = -1.0f;
        } else {
            SensorLocationInternal location = this.mAuthController.getUdfpsProps().get(0).getLocation();
            f3 = ((float) (location.sensorLocationY - location.sensorRadius)) - this.mUdfpsMaxYBurnInOffset;
        }
        boolean z3 = shouldAnimateClockChange;
        this.mClockPositionAlgorithm.setup(this.mStatusBarHeaderHeightKeyguard, f, this.mKeyguardStatusViewController.getLockscreenHeight(), i2, i, f2, this.mOverStretchAmount, bypassEnabled, getUnlockedStackScrollerPadding(), computeQsExpansionFraction(), this.mDisplayTopInset, this.mSplitShadeEnabled, f3, (float) this.mKeyguardStatusViewController.getClockBottom(this.mStatusBarHeaderHeightKeyguard), this.mKeyguardStatusViewController.isClockTopAligned());
        this.mClockPositionAlgorithm.run(this.mClockPositionResult);
        boolean z4 = (this.mNotificationStackScrollLayoutController.isAddOrRemoveAnimationPending() || this.mAnimateNextPositionUpdate) && z3;
        this.mKeyguardStatusViewController.updatePosition(this.mClockPositionResult.clockX, this.mClockPositionResult.clockY, this.mClockPositionResult.clockScale, z4);
        KeyguardQsUserSwitchController keyguardQsUserSwitchController2 = this.mKeyguardQsUserSwitchController;
        if (keyguardQsUserSwitchController2 != null) {
            keyguardQsUserSwitchController2.updatePosition(this.mClockPositionResult.clockX, this.mClockPositionResult.userSwitchY, z4);
        }
        KeyguardUserSwitcherController keyguardUserSwitcherController2 = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController2 != null) {
            keyguardUserSwitcherController2.updatePosition(this.mClockPositionResult.clockX, this.mClockPositionResult.userSwitchY, z4);
        }
        updateNotificationTranslucency();
        updateClock();
    }

    private void updateKeyguardStatusViewAlignment(boolean z) {
        int i = 0;
        boolean z2 = !this.mSplitShadeEnabled || !(this.mNotificationStackScrollLayoutController.getVisibleNotificationCount() != 0 || this.mMediaDataManager.hasActiveMediaOrRecommendation()) || this.mDozing;
        if (this.mStatusViewCentered != z2) {
            this.mStatusViewCentered = z2;
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) this.mNotificationContainerParent);
            if (!z2) {
                i = C1893R.C1897id.qs_edge_guideline;
            }
            constraintSet.connect(C1893R.C1897id.keyguard_status_view, 7, i, 7);
            if (z) {
                ChangeBounds changeBounds = new ChangeBounds();
                if (this.mSplitShadeEnabled) {
                    changeBounds.excludeTarget(C1893R.C1897id.status_view_media_container, true);
                }
                changeBounds.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                changeBounds.setDuration(360);
                TransitionManager.beginDelayedTransition(this.mNotificationContainerParent, changeBounds);
            }
            constraintSet.applyTo(this.mNotificationContainerParent);
        }
        this.mKeyguardUnfoldTransition.ifPresent(new NotificationPanelViewController$$ExternalSyntheticLambda13(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateKeyguardStatusViewAlignment$11$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44623x450ea92f(KeyguardUnfoldTransition keyguardUnfoldTransition) {
        keyguardUnfoldTransition.setStatusViewCentered(this.mStatusViewCentered);
    }

    private int getUnlockedStackScrollerPadding() {
        C2301QS qs = this.mQs;
        return (qs != null ? qs.getHeader().getHeight() : 0) + this.mQsPeekHeight;
    }

    private float getLockIconPadding() {
        if (this.mLockIconViewController.getTop() != 0.0f) {
            return ((float) this.mNotificationStackScrollLayoutController.getBottom()) - this.mLockIconViewController.getTop();
        }
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public float getVerticalSpaceForLockscreenNotifications() {
        float lockIconPadding = getLockIconPadding();
        float max = Math.max(lockIconPadding, (float) Math.max(this.mIndicationBottomPadding, this.mAmbientIndicationBottomPadding));
        this.mKeyguardNotificationBottomPadding = max;
        float lockscreenMinStackScrollerPadding = this.mClockPositionAlgorithm.getLockscreenMinStackScrollerPadding() - ((float) this.mNotificationStackScrollLayoutController.getTop());
        this.mKeyguardNotificationTopPadding = lockscreenMinStackScrollerPadding;
        float height = (((float) this.mNotificationStackScrollLayoutController.getHeight()) - lockscreenMinStackScrollerPadding) - max;
        if (SPEW_LOGCAT) {
            Log.i(TAG, "\n");
            Log.i(TAG, "staticTopPadding[" + lockscreenMinStackScrollerPadding + "] = Clock.padding[" + this.mClockPositionAlgorithm.getLockscreenMinStackScrollerPadding() + "] - NSSLC.top[" + this.mNotificationStackScrollLayoutController.getTop() + NavigationBarInflaterView.SIZE_MOD_END);
            Log.i(TAG, "bottomPadding[" + max + "] = max(ambientIndicationBottomPadding[" + this.mAmbientIndicationBottomPadding + "], mIndicationBottomPadding[" + this.mIndicationBottomPadding + "], lockIconPadding[" + lockIconPadding + "])");
            Log.i(TAG, "verticalSpaceForNotifications[" + height + "] = NSSL.height[" + this.mNotificationStackScrollLayoutController.getHeight() + "] - staticTopPadding[" + lockscreenMinStackScrollerPadding + "] - bottomPadding[" + max + NavigationBarInflaterView.SIZE_MOD_END);
        }
        return height;
    }

    /* access modifiers changed from: package-private */
    public float getVerticalSpaceForLockscreenShelf() {
        float lockIconPadding = getLockIconPadding() - ((float) Math.max(this.mIndicationBottomPadding, this.mAmbientIndicationBottomPadding));
        if (lockIconPadding > 0.0f) {
            return Math.min((float) this.mNotificationShelfController.getIntrinsicHeight(), lockIconPadding);
        }
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public int computeMaxKeyguardNotifications() {
        if (this.mAmbientState.getFractionToShade() <= 0.0f) {
            return this.mNotificationStackSizeCalculator.computeMaxKeyguardNotifications(this.mNotificationStackScrollLayoutController.getView(), getVerticalSpaceForLockscreenNotifications(), getVerticalSpaceForLockscreenShelf(), (float) this.mNotificationShelfController.getIntrinsicHeight());
        }
        if (SPEW_LOGCAT) {
            Log.v(TAG, "Internally skipping computeMaxKeyguardNotifications() fractionToShade=" + this.mAmbientState.getFractionToShade());
        }
        return this.mMaxAllowedKeyguardNotifications;
    }

    private void updateClock() {
        float f = this.mClockPositionResult.clockAlpha * this.mKeyguardOnlyContentAlpha;
        this.mKeyguardStatusViewController.setAlpha(f);
        this.mKeyguardStatusViewController.setTranslationYExcludingMedia((float) this.mKeyguardOnlyTransitionTranslationY);
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
        C2301QS qs = this.mQs;
        if (qs != null) {
            qs.setHeaderClickable(isQsExpansionEnabled());
        }
    }

    public void setQsExpansionEnabledPolicy(boolean z) {
        this.mQsExpansionEnabledPolicy = z;
        setQsExpansionEnabled();
    }

    public void resetViews(boolean z) {
        this.mEx.resetViews();
        this.mEx.setHasOrientationChanged(false);
        this.mIsLaunchTransitionFinished = false;
        this.mBlockTouches = false;
        if (!this.mLaunchingAffordance) {
            this.mAffordanceHelper.reset(false);
            this.mLastCameraLaunchSource = KeyguardBottomAreaView.CAMERA_LAUNCH_SOURCE_AFFORDANCE;
        }
        this.mCentralSurfaces.getGutsManager().closeAndSaveGuts(true, true, true, -1, -1, true);
        if (!z || isFullyCollapsed()) {
            closeQs();
        } else {
            animateCloseQs(!this.mDozing);
        }
        this.mNotificationStackScrollLayoutController.setOverScrollAmount(0.0f, true, z, !z);
        this.mNotificationStackScrollLayoutController.resetScrollPosition();
    }

    public void collapsePanel(boolean z, boolean z2, float f) {
        boolean z3;
        if (!z || isFullyCollapsed()) {
            resetViews(false);
            setExpandedFraction(0.0f);
            z3 = false;
        } else {
            collapse(z2, f);
            z3 = true;
        }
        if (!z3) {
            getPanelExpansionStateManager().updateState(0);
        }
    }

    public void collapse(boolean z, float f) {
        if (canPanelBeCollapsed()) {
            if (this.mQsExpanded) {
                this.mQsExpandImmediate = true;
                setShowShelfOnly(true);
            }
            super.collapse(z, f);
        }
    }

    private void setShowShelfOnly(boolean z) {
        this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(z && !this.mSplitShadeEnabled);
    }

    public void closeQs() {
        cancelQsAnimation();
        setQsExpansion((float) this.mQsMinExpansionHeight);
    }

    public void cancelAnimation() {
        this.mView.animate().cancel();
    }

    public void animateCloseQs(boolean z) {
        ValueAnimator valueAnimator = this.mQsExpansionAnimator;
        if (valueAnimator != null) {
            if (this.mQsAnimatorExpand) {
                float f = this.mQsExpansionHeight;
                valueAnimator.cancel();
                setQsExpansion(f);
            } else {
                return;
            }
        }
        flingSettings(0.0f, z ? 2 : 1);
    }

    /* access modifiers changed from: private */
    public boolean isQsExpansionEnabled() {
        return this.mQsExpansionEnabledPolicy && this.mQsExpansionEnabledAmbient && !this.mRemoteInputManager.isRemoteInputActive();
    }

    public void expandWithQs() {
        if (isQsExpansionEnabled()) {
            this.mQsExpandImmediate = true;
            setShowShelfOnly(true);
        }
        if (this.mSplitShadeEnabled && isOnKeyguard()) {
            this.mLockscreenShadeTransitionController.goToLockedShade((View) null, false);
        } else if (isFullyCollapsed()) {
            expand(true);
        } else {
            traceQsJank(true, false);
            flingSettings(0.0f, 0);
        }
    }

    public void expandWithoutQs() {
        if (isQsExpanded()) {
            flingSettings(0.0f, 1);
        } else {
            expand(true);
        }
    }

    public void fling(float f, boolean z) {
        GestureRecorder gestureRecorder = this.mCentralSurfaces.getGestureRecorder();
        if (gestureRecorder != null) {
            gestureRecorder.tag("fling " + (f > 0.0f ? "open" : "closed"), "notifications,v=" + f);
        }
        super.fling(f, z);
    }

    /* access modifiers changed from: protected */
    public void flingToHeight(float f, boolean z, float f2, float f3, boolean z2) {
        this.mHeadsUpTouchHelper.notifyFling(!z);
        this.mKeyguardStateController.notifyPanelFlingStart(!z);
        setClosingWithAlphaFadeout(!z && !isOnKeyguard() && getFadeoutAlpha() == 1.0f);
        this.mNotificationStackScrollLayoutController.setPanelFlinging(true);
        super.flingToHeight(f, z, f2, f3, z2);
    }

    /* access modifiers changed from: protected */
    public void onFlingEnd(boolean z) {
        super.onFlingEnd(z);
        this.mNotificationStackScrollLayoutController.setPanelFlinging(false);
    }

    /* access modifiers changed from: private */
    public boolean onQsIntercept(MotionEvent motionEvent) {
        int pointerId;
        boolean z = DEBUG_LOGCAT;
        if (z) {
            Log.d(TAG, "onQsIntercept");
        }
        int findPointerIndex = motionEvent.findPointerIndex(this.mTrackingPointer);
        if (findPointerIndex < 0) {
            this.mTrackingPointer = motionEvent.getPointerId(0);
            findPointerIndex = 0;
        }
        float x = motionEvent.getX(findPointerIndex);
        float y = motionEvent.getY(findPointerIndex);
        int actionMasked = motionEvent.getActionMasked();
        int i = 1;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    float f = y - this.mInitialTouchY;
                    trackMovement(motionEvent);
                    if (this.mQsTracking) {
                        setQsExpansion(f + this.mInitialHeightOnTouch);
                        trackMovement(motionEvent);
                        return true;
                    } else if ((f > getTouchSlop(motionEvent) || (f < (-getTouchSlop(motionEvent)) && this.mQsExpanded)) && Math.abs(f) > Math.abs(x - this.mInitialTouchX) && shouldQuickSettingsIntercept(this.mInitialTouchX, this.mInitialTouchY, f)) {
                        if (z) {
                            Log.d(TAG, "onQsIntercept - start tracking expansion");
                        }
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
        } else {
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
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isQsTracking() {
        return this.mQsTracking;
    }

    /* access modifiers changed from: protected */
    public boolean isInContentBounds(float f, float f2) {
        float x = this.mNotificationStackScrollLayoutController.getX();
        return !this.mNotificationStackScrollLayoutController.isBelowLastNotification(f - x, f2) && x < f && f < x + this.mNotificationStackScrollLayoutController.getWidth();
    }

    /* access modifiers changed from: private */
    public void traceQsJank(boolean z, boolean z2) {
        InteractionJankMonitor interactionJankMonitor = this.mInteractionJankMonitor;
        if (interactionJankMonitor != null) {
            if (z) {
                interactionJankMonitor.begin(this.mView, 5);
            } else if (z2) {
                interactionJankMonitor.cancel(5);
            } else {
                interactionJankMonitor.end(5);
            }
        }
    }

    /* access modifiers changed from: private */
    public void initDownStates(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.mOnlyAffordanceInThisMotion = false;
            this.mQsTouchAboveFalsingThreshold = this.mQsFullyExpanded;
            this.mDozingOnDown = isDozing();
            this.mDownX = motionEvent.getX();
            this.mDownY = motionEvent.getY();
            this.mCollapsedOnDown = isFullyCollapsed();
            this.mEx.setCanQsCollapseForLand(!this.mQsExpanded);
            this.mIsPanelCollapseOnQQS = canPanelCollapseOnQQS(this.mDownX, this.mDownY);
            this.mListenForHeadsUp = this.mCollapsedOnDown && this.mHeadsUpManager.hasPinnedHeadsUp();
            boolean z = this.mExpectingSynthesizedDown;
            this.mAllowExpandForSmallExpansion = z;
            this.mTouchSlopExceededBeforeDown = z;
            if (this.mExpectingSynthesizedDown) {
                this.mLastEventSynthesizedDown = true;
            } else {
                this.mLastEventSynthesizedDown = false;
            }
            this.mLastDownEvents.insert(this.mSystemClock.currentTimeMillis(), this.mDownX, this.mDownY, this.mQsTouchAboveFalsingThreshold, this.mDozingOnDown, this.mCollapsedOnDown, this.mIsPanelCollapseOnQQS, this.mListenForHeadsUp, this.mAllowExpandForSmallExpansion, this.mTouchSlopExceededBeforeDown, this.mLastEventSynthesizedDown);
            return;
        }
        this.mLastEventSynthesizedDown = false;
    }

    private boolean canPanelCollapseOnQQS(float f, float f2) {
        if (this.mCollapsedOnDown || this.mKeyguardShowing || this.mQsExpanded) {
            return false;
        }
        C2301QS qs = this.mQs;
        View header = qs == null ? this.mKeyguardStatusBar : qs.getHeader();
        if (f < this.mQsFrame.getX() || f > this.mQsFrame.getX() + ((float) this.mQsFrame.getWidth()) || f2 > ((float) header.getBottom())) {
            return false;
        }
        return true;
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
        if (!flingExpandsQs || z) {
            i = this.mSplitShadeEnabled ? 2 : 1;
        }
        flingSettings(currentQSVelocity, i);
    }

    private void logQsSwipeDown(float f) {
        this.mLockscreenGestureLogger.write(this.mBarState == 1 ? 193 : 194, (int) ((f - this.mInitialTouchY) / this.mCentralSurfaces.getDisplayDensity()), (int) (getCurrentQSVelocity() / this.mCentralSurfaces.getDisplayDensity()));
    }

    private boolean flingExpandsQs(float f) {
        if (Math.abs(f) < this.mFlingAnimationUtils.getMinVelocityPxPerSecond()) {
            if (computeQsExpansionFraction() > 0.5f) {
                return true;
            }
            return false;
        } else if (f > 0.0f) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isFalseTouch(int i) {
        if (this.mFalsingManager.isClassifierEnabled()) {
            return this.mFalsingManager.isFalseTouch(i);
        }
        return !this.mQsTouchAboveFalsingThreshold;
    }

    /* access modifiers changed from: private */
    public float computeQsExpansionFraction() {
        if (this.mQSAnimatingHiddenFromCollapsed) {
            return 0.0f;
        }
        float f = this.mQsExpansionHeight;
        int i = this.mQsMinExpansionHeight;
        return this.mEx.computeQsExpansionFraction(Math.min(1.0f, (f - ((float) i)) / ((float) (this.mQsMaxExpansionHeight - i))));
    }

    /* access modifiers changed from: protected */
    public boolean shouldExpandWhenNotFlinging() {
        if (super.shouldExpandWhenNotFlinging()) {
            return true;
        }
        if (!this.mAllowExpandForSmallExpansion) {
            return false;
        }
        if (this.mSystemClock.uptimeMillis() - this.mDownTime <= 300) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public float getOpeningHeight() {
        return this.mNotificationStackScrollLayoutController.getOpeningHeight();
    }

    /* access modifiers changed from: private */
    public boolean handleQsTouch(MotionEvent motionEvent) {
        if (this.mSplitShadeEnabled && touchXOutsideOfQs(motionEvent.getX())) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        boolean z = getExpandedFraction() == 1.0f && this.mBarState != 1 && (!this.mQsExpanded && !this.mSplitShadeEnabled) && isQsExpansionEnabled();
        if (actionMasked == 0 && z) {
            this.mQsTracking = true;
            traceQsJank(true, false);
            this.mConflictingQsExpansionGesture = true;
            onQsExpansionStarted();
            this.mInitialHeightOnTouch = this.mQsExpansionHeight;
            this.mInitialTouchY = motionEvent.getY();
            this.mInitialTouchX = motionEvent.getX();
        }
        if (!isFullyCollapsed() && !this.mInSplitShade) {
            handleQsDown(motionEvent);
        }
        if (!this.mQsExpandImmediate && this.mQsTracking) {
            onQsTouch(motionEvent);
            if (!this.mConflictingQsExpansionGesture && !this.mSplitShadeEnabled) {
                return true;
            }
        }
        if (actionMasked == 3 || actionMasked == 1) {
            this.mConflictingQsExpansionGesture = false;
        }
        if (actionMasked == 0 && isFullyCollapsed() && isQsExpansionEnabled()) {
            this.mTwoFingerQsExpandPossible = true;
        }
        if ((this.mInSplitShade || (this.mTwoFingerQsExpandPossible && isOpenQsEvent(motionEvent))) && motionEvent.getY(motionEvent.getActionIndex()) < ((float) this.mStatusBarMinHeight)) {
            this.mMetricsLogger.count(COUNTER_PANEL_OPEN_QS, 1);
            this.mQsExpandImmediate = true;
            setShowShelfOnly(true);
            requestPanelHeightUpdate();
            setListening(true);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean touchXOutsideOfQs(float f) {
        return f < this.mQsFrame.getX() || f > this.mQsFrame.getX() + ((float) this.mQsFrame.getWidth());
    }

    private boolean isInQsArea(float f, float f2) {
        if (touchXOutsideOfQs(f)) {
            return false;
        }
        if (this.mIsGestureNavigation && f2 > ((float) (this.mView.getHeight() - this.mNavigationBarBottomHeight))) {
            return false;
        }
        if (f2 <= this.mNotificationStackScrollLayoutController.getBottomMostNotificationBottom() || f2 <= this.mQs.getView().getY() + ((float) this.mQs.getView().getHeight())) {
            return true;
        }
        return false;
    }

    private boolean isOpenQsEvent(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        int actionMasked = motionEvent.getActionMasked();
        boolean z = actionMasked == 5 && pointerCount == 2;
        boolean z2 = actionMasked == 0 && (motionEvent.isButtonPressed(32) || motionEvent.isButtonPressed(64));
        boolean z3 = actionMasked == 0 && (motionEvent.isButtonPressed(2) || motionEvent.isButtonPressed(4));
        if (z || z2 || z3) {
            return true;
        }
        return false;
    }

    private void handleQsDown(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 && shouldQuickSettingsIntercept(motionEvent.getX(), motionEvent.getY(), -1.0f)) {
            if (DEBUG_LOGCAT) {
                Log.d(TAG, "handleQsDown");
            }
            this.mFalsingCollector.onQsDown();
            this.mQsTracking = true;
            onQsExpansionStarted();
            this.mInitialHeightOnTouch = this.mQsExpansionHeight;
            this.mInitialTouchY = motionEvent.getY();
            this.mInitialTouchX = motionEvent.getX();
            notifyExpandingFinished();
        }
    }

    public void startWaitingForOpenPanelGesture() {
        if (isFullyCollapsed()) {
            NTLogUtil.m1680d(TAG, "startWaitingForOpenPanelGesture");
            this.mExpectingSynthesizedDown = true;
            onTrackingStarted();
            updatePanelExpanded();
        }
    }

    public void stopWaitingForOpenPanelGesture(boolean z, float f) {
        if (this.mExpectingSynthesizedDown) {
            NTLogUtil.m1680d(TAG, "stopWaitingForOpenPanelGesture");
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

    /* access modifiers changed from: protected */
    public boolean flingExpands(float f, float f2, float f3, float f4) {
        boolean flingExpands = super.flingExpands(f, f2, f3, f4);
        if (this.mQsExpansionAnimator != null) {
            return true;
        }
        return flingExpands;
    }

    /* access modifiers changed from: protected */
    public boolean shouldGestureWaitForTouchSlop() {
        if (this.mExpectingSynthesizedDown) {
            this.mExpectingSynthesizedDown = false;
            return false;
        } else if (isFullyCollapsed() || this.mBarState != 0) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldGestureIgnoreXTouchSlop(float f, float f2) {
        return !this.mAffordanceHelper.isOnAffordanceIcon(f, f2);
    }

    private void onQsTouch(MotionEvent motionEvent) {
        int pointerId;
        int findPointerIndex = motionEvent.findPointerIndex(this.mTrackingPointer);
        boolean z = false;
        if (findPointerIndex < 0) {
            this.mTrackingPointer = motionEvent.getPointerId(0);
            findPointerIndex = 0;
        }
        float y = motionEvent.getY(findPointerIndex);
        float x = motionEvent.getX(findPointerIndex);
        float f = y - this.mInitialTouchY;
        int actionMasked = motionEvent.getActionMasked();
        boolean z2 = true;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (DEBUG_LOGCAT) {
                        Log.d(TAG, "onQSTouch move");
                    }
                    setQsExpansion(this.mInitialHeightOnTouch + f);
                    if (f >= ((float) getFalsingThreshold())) {
                        this.mQsTouchAboveFalsingThreshold = true;
                    }
                    trackMovement(motionEvent);
                    return;
                } else if (actionMasked != 3) {
                    if (actionMasked == 6 && this.mTrackingPointer == (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                        if (motionEvent.getPointerId(0) == pointerId) {
                            z = true;
                        }
                        float y2 = motionEvent.getY(z ? 1 : 0);
                        float x2 = motionEvent.getX(z);
                        this.mTrackingPointer = motionEvent.getPointerId(z);
                        this.mInitialHeightOnTouch = this.mQsExpansionHeight;
                        this.mInitialTouchY = y2;
                        this.mInitialTouchX = x2;
                        return;
                    }
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
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.mQsVelocityTracker = null;
                return;
            }
            return;
        }
        this.mQsTracking = true;
        traceQsJank(true, false);
        this.mInitialTouchY = y;
        this.mInitialTouchX = x;
        onQsExpansionStarted();
        this.mInitialHeightOnTouch = this.mQsExpansionHeight;
        initVelocityTracker();
        trackMovement(motionEvent);
    }

    private int getFalsingThreshold() {
        return (int) (((float) this.mQsFalsingThreshold) * (this.mCentralSurfaces.isWakeUpComingFromTouch() ? 1.5f : 1.0f));
    }

    /* access modifiers changed from: private */
    public void setOverScrolling(boolean z) {
        this.mStackScrollerOverscrolling = z;
        C2301QS qs = this.mQs;
        if (qs != null) {
            qs.setOverscrolling(z);
        }
    }

    /* access modifiers changed from: private */
    public void onQsExpansionStarted() {
        onQsExpansionStarted(0);
    }

    /* access modifiers changed from: protected */
    public void onQsExpansionStarted(int i) {
        cancelQsAnimation();
        cancelHeightAnimator();
        float f = this.mQsExpansionHeight - ((float) i);
        setQsExpansion(f);
        requestPanelHeightUpdate();
        this.mNotificationStackScrollLayoutController.checkSnoozeLeavebehind();
        this.mNotificationStackScrollLayoutController.resetScrollAnimation();
        if (f == 0.0f) {
            this.mCentralSurfaces.requestFaceAuth(false);
        }
    }

    public void setQsExpanded(boolean z) {
        boolean z2 = this.mQsExpanded != z;
        this.mEx.setQsExpanded(z);
        if (z2) {
            this.mQsExpanded = z;
            updateQsState();
            requestPanelHeightUpdate();
            this.mFalsingCollector.setQsExpanded(z);
            this.mCentralSurfaces.setQsExpanded(z);
            this.mNotificationsQSContainerController.setQsExpanded(z);
            this.mPulseExpansionHandler.setQsExpanded(z);
            this.mKeyguardBypassController.setQSExpanded(z);
            this.mPrivacyDotViewController.setQsExpanded(z);
        }
    }

    /* access modifiers changed from: private */
    public void maybeAnimateBottomAreaAlpha() {
        this.mBottomAreaShadeAlphaAnimator.cancel();
        if (this.mBarState == 2) {
            this.mBottomAreaShadeAlphaAnimator.setFloatValues(new float[]{this.mBottomAreaShadeAlpha, 0.0f});
            this.mBottomAreaShadeAlphaAnimator.start();
            return;
        }
        this.mBottomAreaShadeAlpha = 1.0f;
    }

    /* access modifiers changed from: private */
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

    public void updateQsState() {
        boolean z = false;
        boolean z2 = this.mQsExpanded && !this.mSplitShadeEnabled;
        this.mNotificationStackScrollLayoutController.setQsFullScreen(z2);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        if (this.mBarState != 1 && (!z2 || this.mQsExpansionFromOverscroll)) {
            z = true;
        }
        notificationStackScrollLayoutController.setScrollingEnabled(z);
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null && this.mQsExpanded && !this.mStackScrollerOverscrolling) {
            keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(true);
        }
        C2301QS qs = this.mQs;
        if (qs != null) {
            qs.setExpanded(this.mQsExpanded);
        }
    }

    /* access modifiers changed from: package-private */
    public void setQsExpansion(float f) {
        float min = Math.min(Math.max(f, (float) this.mQsMinExpansionHeight), (float) this.mQsMaxExpansionHeight);
        int i = this.mQsMaxExpansionHeight;
        this.mQsFullyExpanded = min == ((float) i) && i != 0;
        boolean z = !this.mQsAnimatorExpand && this.mAnimatingQS;
        int i2 = this.mQsMinExpansionHeight;
        if (min > ((float) i2) && !this.mQsExpanded && !this.mStackScrollerOverscrolling && !this.mDozing && !z) {
            setQsExpanded(true);
        } else if (min <= ((float) i2) && this.mQsExpanded) {
            setQsExpanded(false);
        }
        this.mQsExpansionHeight = min;
        updateQsExpansion();
        requestScrollerTopPaddingUpdate(false);
        this.mKeyguardStatusBarViewController.updateViewState();
        int i3 = this.mBarState;
        if (i3 == 2 || i3 == 1) {
            updateKeyguardBottomAreaAlpha();
            positionClockAndNotifications();
        }
        if (this.mAccessibilityManager.isEnabled()) {
            this.mView.setAccessibilityPaneTitle(determineAccessibilityPaneTitle());
        }
        if (!this.mFalsingManager.isUnlockingDisabled() && this.mQsFullyExpanded && this.mFalsingCollector.shouldEnforceBouncer()) {
            this.mCentralSurfaces.executeRunnableDismissingKeyguard((Runnable) null, (Runnable) null, false, true, false);
        }
    }

    /* access modifiers changed from: private */
    public void updateQsExpansion() {
        float f;
        float f2;
        if (this.mQs != null) {
            float f3 = 1.0f;
            if ((this.mQsExpandImmediate || this.mQsExpanded) && !this.mSplitShadeEnabled) {
                f = 1.0f;
            } else if (this.mLockscreenShadeTransitionController.getQSDragProgress() > 0.0f) {
                f = this.mLockscreenShadeTransitionController.getQSDragProgress();
            } else {
                f = this.mNotificationStackScrollLayoutController.getNotificationSquishinessFraction();
            }
            float computeQsExpansionFraction = computeQsExpansionFraction();
            if (!this.mSplitShadeEnabled) {
                f3 = computeQsExpansionFraction();
            }
            this.mQs.setQsExpansion(f3, getExpandedFraction(), getHeaderTranslation(), f);
            this.mMediaHierarchyManager.setQsExpansion(computeQsExpansionFraction);
            this.mScrimController.setQsPosition(computeQsExpansionFraction, calculateQsBottomPosition(computeQsExpansionFraction));
            setQSClippingBounds();
            if (this.mSplitShadeEnabled) {
                this.mNotificationStackScrollLayoutController.setQsExpansionFraction(0.0f);
            } else {
                this.mNotificationStackScrollLayoutController.setQsExpansionFraction(computeQsExpansionFraction);
            }
            this.mDepthController.setQsPanelExpansion(computeQsExpansionFraction);
            this.mStatusBarKeyguardViewManager.setQsExpansion(computeQsExpansionFraction);
            if (this.mTransitioningToFullShadeProgress > 0.0f) {
                f2 = this.mLockscreenShadeTransitionController.getQSDragProgress();
            } else {
                f2 = getExpandedFraction();
            }
            this.mLargeScreenShadeHeaderController.setShadeExpandedFraction(f2);
            this.mLargeScreenShadeHeaderController.setQsExpandedFraction(computeQsExpansionFraction);
            this.mLargeScreenShadeHeaderController.setShadeExpanded(this.mQsVisible);
        }
    }

    /* access modifiers changed from: private */
    public void onStackYChanged(boolean z) {
        if (this.mQs != null) {
            if (z) {
                animateNextNotificationBounds(360, 0);
                this.mNotificationBoundsAnimationDelay = 0;
            }
            setQSClippingBounds();
        }
    }

    /* access modifiers changed from: private */
    public void onNotificationScrolled(int i) {
        updateQSExpansionEnabledAmbient();
    }

    private void updateQSExpansionEnabledAmbient() {
        this.mQsExpansionEnabledAmbient = this.mSplitShadeEnabled || ((float) this.mAmbientState.getScrollY()) <= this.mAmbientState.getTopPadding() - this.mQuickQsHeaderHeight;
        setQsExpansionEnabled();
    }

    private void setQSClippingBounds() {
        int calculateQsBottomPosition = calculateQsBottomPosition(computeQsExpansionFraction());
        boolean z = computeQsExpansionFraction() > 0.0f || calculateQsBottomPosition > 0;
        int calculateTopQsClippingBound = calculateTopQsClippingBound(calculateQsBottomPosition);
        int calculateBottomQsClippingBound = calculateBottomQsClippingBound(calculateTopQsClippingBound);
        applyQSClippingBounds(calculateLeftQsClippingBound(), Math.min(calculateTopQsClippingBound, calculateBottomQsClippingBound), calculateRightQsClippingBound(), calculateBottomQsClippingBound, z);
    }

    private int calculateTopQsClippingBound(int i) {
        if (this.mSplitShadeEnabled) {
            return Math.min(i, this.mLargeScreenShadeHeaderHeight);
        }
        if (this.mTransitioningToFullShadeProgress > 0.0f) {
            i = this.mTransitionToFullShadeQSPosition;
        } else {
            float qSEdgePosition = getQSEdgePosition();
            if (!isOnKeyguard()) {
                i = (int) qSEdgePosition;
            } else if (!this.mKeyguardBypassController.getBypassEnabled()) {
                i = (int) Math.min((float) i, qSEdgePosition);
            }
        }
        int i2 = (int) (((float) i) + this.mOverStretchAmount);
        float f = this.mMinFraction;
        if (f <= 0.0f || f >= 1.0f) {
            return i2;
        }
        float expandedFraction = getExpandedFraction();
        float f2 = this.mMinFraction;
        return (int) (((float) i2) * MathUtils.saturate(((expandedFraction - f2) / (1.0f - f2)) / f2));
    }

    private int calculateBottomQsClippingBound(int i) {
        if (this.mSplitShadeEnabled) {
            return i + this.mNotificationStackScrollLayoutController.getHeight() + this.mSplitShadeNotificationsScrimMarginBottom;
        }
        return getView().getBottom();
    }

    private int calculateLeftQsClippingBound() {
        if (isFullWidth()) {
            return 0;
        }
        return this.mNotificationStackScrollLayoutController.getLeft();
    }

    private int calculateRightQsClippingBound() {
        if (isFullWidth()) {
            return getView().getRight() + this.mDisplayRightInset;
        }
        return this.mNotificationStackScrollLayoutController.getRight();
    }

    private void applyQSClippingBounds(int i, int i2, int i3, int i4, boolean z) {
        if (this.mAnimateNextNotificationBounds && !this.mKeyguardStatusAreaClipBounds.isEmpty()) {
            this.mQsClippingAnimationEndBounds.set(i, i2, i3, i4);
            int i5 = this.mKeyguardStatusAreaClipBounds.left;
            int i6 = this.mKeyguardStatusAreaClipBounds.top;
            int i7 = this.mKeyguardStatusAreaClipBounds.right;
            int i8 = this.mKeyguardStatusAreaClipBounds.bottom;
            ValueAnimator valueAnimator = this.mQsClippingAnimation;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mQsClippingAnimation = ofFloat;
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mQsClippingAnimation.setDuration(this.mNotificationBoundsAnimationDuration);
            this.mQsClippingAnimation.setStartDelay(this.mNotificationBoundsAnimationDelay);
            this.mQsClippingAnimation.addUpdateListener(new NotificationPanelViewController$$ExternalSyntheticLambda4(this, i5, i6, i7, i8, z));
            this.mQsClippingAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    ValueAnimator unused = NotificationPanelViewController.this.mQsClippingAnimation = null;
                    boolean unused2 = NotificationPanelViewController.this.mIsQsTranslationResetAnimator = false;
                    boolean unused3 = NotificationPanelViewController.this.mIsPulseExpansionResetAnimator = false;
                }
            });
            this.mQsClippingAnimation.start();
        } else if (this.mQsClippingAnimation != null) {
            this.mQsClippingAnimationEndBounds.set(i, i2, i3, i4);
        } else {
            applyQSClippingImmediately(i, i2, i3, i4, z);
        }
        this.mAnimateNextNotificationBounds = false;
        this.mNotificationBoundsAnimationDelay = 0;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyQSClippingBounds$12$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44611xdd0cc2b8(int i, int i2, int i3, int i4, boolean z, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        applyQSClippingImmediately((int) MathUtils.lerp(i, this.mQsClippingAnimationEndBounds.left, animatedFraction), (int) MathUtils.lerp(i2, this.mQsClippingAnimationEndBounds.top, animatedFraction), (int) MathUtils.lerp(i3, this.mQsClippingAnimationEndBounds.right, animatedFraction), (int) MathUtils.lerp(i4, this.mQsClippingAnimationEndBounds.bottom, animatedFraction), z);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00c9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyQSClippingImmediately(int r18, int r19, int r20, int r21, boolean r22) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r20
            r4 = r21
            r5 = r22
            int r6 = r0.mScrimCornerRadius
            boolean r7 = r17.isFullWidth()
            r8 = 0
            r9 = 0
            if (r7 == 0) goto L_0x003c
            android.graphics.Rect r6 = r0.mKeyguardStatusAreaClipBounds
            r6.set(r1, r2, r3, r4)
            com.android.systemui.screenrecord.RecordingController r6 = r0.mRecordingController
            boolean r6 = r6.isRecording()
            if (r6 == 0) goto L_0x0025
            r6 = r8
            goto L_0x0028
        L_0x0025:
            int r6 = r0.mScreenCornerRadius
            float r6 = (float) r6
        L_0x0028:
            int r7 = r0.mScrimCornerRadius
            float r10 = (float) r7
            float r11 = (float) r2
            float r7 = (float) r7
            float r11 = r11 / r7
            r7 = 1065353216(0x3f800000, float:1.0)
            float r7 = java.lang.Math.min((float) r11, (float) r7)
            float r6 = android.util.MathUtils.lerp(r6, r10, r7)
            int r6 = (int) r6
            r15 = r6
            r6 = r5
            goto L_0x003e
        L_0x003c:
            r15 = r6
            r6 = r9
        L_0x003e:
            com.android.systemui.plugins.qs.QS r7 = r0.mQs
            if (r7 == 0) goto L_0x00cd
            com.android.systemui.statusbar.PulseExpansionHandler r7 = r0.mPulseExpansionHandler
            boolean r7 = r7.isExpanding()
            float r10 = r0.mTransitioningToFullShadeProgress
            int r10 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r10 > 0) goto L_0x005c
            if (r7 != 0) goto L_0x005c
            android.animation.ValueAnimator r10 = r0.mQsClippingAnimation
            if (r10 == 0) goto L_0x0079
            boolean r10 = r0.mIsQsTranslationResetAnimator
            if (r10 != 0) goto L_0x005c
            boolean r10 = r0.mIsPulseExpansionResetAnimator
            if (r10 == 0) goto L_0x0079
        L_0x005c:
            if (r7 != 0) goto L_0x007b
            boolean r7 = r0.mIsPulseExpansionResetAnimator
            if (r7 == 0) goto L_0x0063
            goto L_0x007b
        L_0x0063:
            boolean r7 = r0.mSplitShadeEnabled
            if (r7 != 0) goto L_0x0079
            com.android.systemui.plugins.qs.QS r7 = r0.mQs
            android.view.View r7 = r7.getHeader()
            int r7 = r7.getHeight()
            int r7 = r2 - r7
            float r7 = (float) r7
            r10 = 1043542835(0x3e333333, float:0.175)
            float r7 = r7 * r10
            goto L_0x008f
        L_0x0079:
            r7 = r8
            goto L_0x008f
        L_0x007b:
            com.android.systemui.plugins.qs.QS r7 = r0.mQs
            android.view.View r7 = r7.getHeader()
            int r7 = r7.getHeight()
            int r7 = r2 - r7
            float r7 = (float) r7
            r10 = 1073741824(0x40000000, float:2.0)
            float r7 = r7 / r10
            float r7 = java.lang.Math.max((float) r8, (float) r7)
        L_0x008f:
            r0.mQsTranslationForFullShadeTransition = r7
            r17.updateQsFrameTranslation()
            android.widget.FrameLayout r7 = r0.mQsFrame
            float r7 = r7.getTranslationY()
            float r10 = (float) r2
            float r10 = r10 - r7
            android.widget.FrameLayout r11 = r0.mQsFrame
            int r11 = r11.getTop()
            float r11 = (float) r11
            float r10 = r10 - r11
            int r10 = (int) r10
            r0.mQsClipTop = r10
            float r10 = (float) r4
            float r10 = r10 - r7
            android.widget.FrameLayout r7 = r0.mQsFrame
            int r7 = r7.getTop()
            float r7 = (float) r7
            float r10 = r10 - r7
            int r7 = (int) r10
            r0.mQsClipBottom = r7
            r0.mQsVisible = r5
            com.android.systemui.plugins.qs.QS r7 = r0.mQs
            r7.setQsVisible(r5)
            com.android.systemui.plugins.qs.QS r7 = r0.mQs
            int r10 = r0.mQsClipTop
            int r11 = r0.mQsClipBottom
            if (r5 == 0) goto L_0x00c9
            boolean r12 = r0.mSplitShadeEnabled
            if (r12 != 0) goto L_0x00c9
            r12 = 1
            goto L_0x00ca
        L_0x00c9:
            r12 = r9
        L_0x00ca:
            r7.setFancyClipping(r10, r11, r15, r12)
        L_0x00cd:
            com.android.keyguard.KeyguardStatusViewController r7 = r0.mKeyguardStatusViewController
            if (r6 == 0) goto L_0x00d4
            android.graphics.Rect r6 = r0.mKeyguardStatusAreaClipBounds
            goto L_0x00d5
        L_0x00d4:
            r6 = 0
        L_0x00d5:
            r7.setClipBounds(r6)
            if (r5 != 0) goto L_0x00e4
            boolean r5 = r0.mSplitShadeEnabled
            if (r5 == 0) goto L_0x00e4
            com.android.systemui.statusbar.phone.ScrimController r5 = r0.mScrimController
            r5.setNotificationsBounds(r8, r8, r8, r8)
            goto L_0x00fa
        L_0x00e4:
            boolean r5 = r0.mSplitShadeEnabled
            if (r5 == 0) goto L_0x00ea
            r5 = r4
            goto L_0x00ec
        L_0x00ea:
            int r5 = r4 + r15
        L_0x00ec:
            com.android.systemui.statusbar.phone.ScrimController r6 = r0.mScrimController
            int r7 = r0.mDisplayLeftInset
            int r8 = r1 + r7
            float r8 = (float) r8
            float r10 = (float) r2
            int r7 = r7 + r3
            float r7 = (float) r7
            float r5 = (float) r5
            r6.setNotificationsBounds(r8, r10, r7, r5)
        L_0x00fa:
            boolean r5 = r0.mSplitShadeEnabled
            if (r5 == 0) goto L_0x0104
            com.android.systemui.statusbar.phone.KeyguardStatusBarViewController r5 = r0.mKeyguardStatusBarViewController
            r5.setNoTopClipping()
            goto L_0x0109
        L_0x0104:
            com.android.systemui.statusbar.phone.KeyguardStatusBarViewController r5 = r0.mKeyguardStatusBarViewController
            r5.updateTopClipping(r2)
        L_0x0109:
            com.android.systemui.statusbar.phone.ScrimController r5 = r0.mScrimController
            r5.setScrimCornerRadius(r15)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r5 = r0.mNotificationStackScrollLayoutController
            int r5 = r5.getLeft()
            int r11 = r1 - r5
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r1 = r0.mNotificationStackScrollLayoutController
            int r1 = r1.getLeft()
            int r13 = r3 - r1
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r1 = r0.mNotificationStackScrollLayoutController
            int r1 = r1.getTop()
            int r12 = r2 - r1
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r1 = r0.mNotificationStackScrollLayoutController
            int r1 = r1.getTop()
            int r14 = r4 - r1
            boolean r1 = r0.mSplitShadeEnabled
            if (r1 == 0) goto L_0x0135
            r16 = r15
            goto L_0x0137
        L_0x0135:
            r16 = r9
        L_0x0137:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r10 = r0.mNotificationStackScrollLayoutController
            r10.setRoundedClippingBounds(r11, r12, r13, r14, r15, r16)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.applyQSClippingImmediately(int, int, int, int, boolean):void");
    }

    private float getQSEdgePosition() {
        if (this.mEx.shouldGetQSEdgePosition(this.mBarState, this.mKeyguardBypassController.getBypassEnabled(), this.mTransitioningToFullShadeProgress)) {
            return this.mEx.getQSEdgePosition();
        }
        return Math.max(this.mQuickQsHeaderHeight * this.mAmbientState.getExpansionFraction(), (this.mAmbientState.getStackY() + (((float) this.mAmbientState.getStackTopMargin()) * this.mAmbientState.getExpansionFraction())) - ((float) this.mAmbientState.getScrollY()));
    }

    private int calculateQsBottomPosition(float f) {
        if (this.mTransitioningToFullShadeProgress > 0.0f) {
            return this.mTransitionToFullShadeQSPosition;
        }
        int headerTranslation = ((int) getHeaderTranslation()) + this.mQs.getQsMinExpansionHeight();
        return ((double) f) != 0.0d ? (int) MathUtils.lerp(headerTranslation, this.mQs.getDesiredHeight(), f) : headerTranslation;
    }

    /* access modifiers changed from: private */
    public String determineAccessibilityPaneTitle() {
        C2301QS qs = this.mQs;
        if (qs != null && qs.isCustomizing()) {
            return this.mResources.getString(C1893R.string.accessibility_desc_quick_settings_edit);
        }
        if (this.mQsExpansionHeight != 0.0f && this.mQsFullyExpanded) {
            return this.mResources.getString(C1893R.string.accessibility_desc_quick_settings);
        }
        if (this.mBarState == 1) {
            return this.mResources.getString(C1893R.string.accessibility_desc_lock_screen);
        }
        return this.mResources.getString(C1893R.string.accessibility_desc_notification_shade);
    }

    /* access modifiers changed from: private */
    public float calculateNotificationsTopPadding() {
        if (this.mSplitShadeEnabled && !this.mKeyguardShowing) {
            return 0.0f;
        }
        boolean z = this.mKeyguardShowing;
        if (!z || (!this.mQsExpandImmediate && (!this.mIsExpanding || !this.mQsExpandedWhenExpandingStarted))) {
            ValueAnimator valueAnimator = this.mQsSizeChangeAnimator;
            if (valueAnimator != null) {
                return (float) Math.max(((Integer) valueAnimator.getAnimatedValue()).intValue(), getKeyguardNotificationStaticPadding());
            }
            if (z) {
                return MathUtils.lerp((float) getKeyguardNotificationStaticPadding(), (float) this.mQsMaxExpansionHeight, computeQsExpansionFraction());
            }
            return this.mQsFrameTranslateController.getNotificationsTopPadding(this.mQsExpansionHeight, this.mNotificationStackScrollLayoutController);
        }
        int keyguardNotificationStaticPadding = getKeyguardNotificationStaticPadding();
        int i = this.mQsMaxExpansionHeight;
        if (this.mBarState == 1) {
            i = Math.max(keyguardNotificationStaticPadding, i);
        }
        return (float) ((int) MathUtils.lerp((float) this.mQsMinExpansionHeight, (float) i, getExpandedFraction()));
    }

    private int getKeyguardNotificationStaticPadding() {
        if (!this.mKeyguardShowing) {
            return 0;
        }
        if (!this.mKeyguardBypassController.getBypassEnabled()) {
            return this.mEx.getKeyguardNotificationStaticPaddingForNotBypassEnableCase(this.mClockPositionResult.stackScrollerPadding, this.mQs.getQsMinExpansionHeight(), this.mQsClipTop);
        }
        int i = this.mHeadsUpInset;
        if (!this.mNotificationStackScrollLayoutController.isPulseExpanding()) {
            return i;
        }
        return (int) MathUtils.lerp(i, this.mClockPositionResult.stackScrollerPadding, this.mNotificationStackScrollLayoutController.calculateAppearFractionBypass());
    }

    public void requestScrollerTopPaddingUpdate(boolean z) {
        this.mNotificationStackScrollLayoutController.updateTopPadding(calculateNotificationsTopPadding(), z);
        if (this.mKeyguardShowing && this.mKeyguardBypassController.getBypassEnabled()) {
            updateQsExpansion();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x007f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setTransitionToFullShadeAmount(float r6, boolean r7, long r8) {
        /*
            r5 = this;
            r0 = 0
            r1 = 1
            r2 = 0
            if (r7 == 0) goto L_0x001b
            boolean r7 = r5.isFullWidth()
            if (r7 == 0) goto L_0x001b
            r3 = 448(0x1c0, double:2.213E-321)
            r5.animateNextNotificationBounds(r3, r8)
            float r7 = r5.mQsTranslationForFullShadeTransition
            int r7 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r7 <= 0) goto L_0x0018
            r7 = r1
            goto L_0x0019
        L_0x0018:
            r7 = r0
        L_0x0019:
            r5.mIsQsTranslationResetAnimator = r7
        L_0x001b:
            int r7 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r7 <= 0) goto L_0x0063
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r7 = r5.mNotificationStackScrollLayoutController
            int r7 = r7.getVisibleNotificationCount()
            if (r7 != 0) goto L_0x004c
            com.android.systemui.media.MediaDataManager r7 = r5.mMediaDataManager
            boolean r7 = r7.hasActiveMediaOrRecommendation()
            if (r7 != 0) goto L_0x004c
            com.android.systemui.plugins.qs.QS r7 = r5.mQs
            if (r7 == 0) goto L_0x0063
            float r7 = r5.getQSEdgePosition()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r8 = r5.mNotificationStackScrollLayoutController
            int r8 = r8.getTopPadding()
            float r8 = (float) r8
            float r7 = r7 - r8
            com.android.systemui.plugins.qs.QS r8 = r5.mQs
            android.view.View r8 = r8.getHeader()
            int r8 = r8.getHeight()
            float r8 = (float) r8
            float r7 = r7 + r8
            goto L_0x0064
        L_0x004c:
            float r7 = r5.getQSEdgePosition()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r8 = r5.mNotificationStackScrollLayoutController
            int r8 = r8.getFullShadeTransitionInset()
            float r8 = (float) r8
            float r7 = r7 + r8
            boolean r8 = r5.isOnKeyguard()
            if (r8 == 0) goto L_0x0064
            int r8 = r5.mLockscreenNotificationQSPadding
            float r8 = (float) r8
            float r7 = r7 - r8
            goto L_0x0064
        L_0x0063:
            r7 = r2
        L_0x0064:
            android.view.animation.Interpolator r8 = com.android.systemui.animation.Interpolators.FAST_OUT_SLOW_IN
            int r9 = r5.mDistanceForQSFullShadeTransition
            float r9 = (float) r9
            float r6 = r6 / r9
            float r6 = android.util.MathUtils.saturate(r6)
            float r6 = r8.getInterpolation(r6)
            r5.mTransitioningToFullShadeProgress = r6
            float r6 = android.util.MathUtils.lerp(r2, r7, r6)
            int r6 = (int) r6
            float r7 = r5.mTransitioningToFullShadeProgress
            int r7 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r7 <= 0) goto L_0x0086
            int r6 = java.lang.Math.max((int) r1, (int) r6)
            r5.requestScrollerTopPaddingUpdate(r0)
        L_0x0086:
            r5.mTransitionToFullShadeQSPosition = r6
            r5.updateQsExpansion()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.setTransitionToFullShadeAmount(float, boolean, long):void");
    }

    public void onPulseExpansionFinished() {
        animateNextNotificationBounds(448, 0);
        this.mIsPulseExpansionResetAnimator = true;
    }

    public void setKeyguardTransitionProgress(float f, int i) {
        float interpolation = Interpolators.ALPHA_IN.getInterpolation(f);
        this.mKeyguardOnlyContentAlpha = interpolation;
        this.mKeyguardOnlyTransitionTranslationY = i;
        if (this.mBarState == 1) {
            this.mBottomAreaShadeAlpha = interpolation;
            updateKeyguardBottomAreaAlpha();
        }
        updateClock();
    }

    public void setKeyguardStatusBarAlpha(float f) {
        this.mKeyguardStatusBarViewController.setAlpha(f);
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

    /* access modifiers changed from: private */
    public void cancelQsAnimation() {
        ValueAnimator valueAnimator = this.mQsExpansionAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void flingSettings(float f, int i) {
        flingSettings(f, i, (Runnable) null, false);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x001c  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0029  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void flingSettings(float r9, int r10, final java.lang.Runnable r11, boolean r12) {
        /*
            r8 = this;
            r0 = 0
            r1 = 1
            if (r10 == 0) goto L_0x0012
            if (r10 == r1) goto L_0x000f
            com.android.systemui.plugins.qs.QS r2 = r8.mQs
            if (r2 == 0) goto L_0x000d
            r2.closeDetail()
        L_0x000d:
            r2 = r0
            goto L_0x0015
        L_0x000f:
            int r2 = r8.mQsMinExpansionHeight
            goto L_0x0014
        L_0x0012:
            int r2 = r8.mQsMaxExpansionHeight
        L_0x0014:
            float r2 = (float) r2
        L_0x0015:
            float r3 = r8.mQsExpansionHeight
            int r4 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            r5 = 0
            if (r4 != 0) goto L_0x0029
            if (r11 == 0) goto L_0x0021
            r11.run()
        L_0x0021:
            if (r10 == 0) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r1 = r5
        L_0x0025:
            r8.traceQsJank(r5, r1)
            return
        L_0x0029:
            if (r10 != 0) goto L_0x002d
            r10 = r1
            goto L_0x002e
        L_0x002d:
            r10 = r5
        L_0x002e:
            int r4 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r4 <= 0) goto L_0x0034
            if (r10 == 0) goto L_0x003a
        L_0x0034:
            int r4 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r4 >= 0) goto L_0x003d
            if (r10 == 0) goto L_0x003d
        L_0x003a:
            r9 = r0
            r4 = r1
            goto L_0x003e
        L_0x003d:
            r4 = r5
        L_0x003e:
            r6 = 2
            float[] r6 = new float[r6]
            r6[r5] = r3
            r6[r1] = r2
            android.animation.ValueAnimator r3 = android.animation.ValueAnimator.ofFloat(r6)
            if (r12 == 0) goto L_0x0056
            android.view.animation.Interpolator r9 = com.android.systemui.animation.Interpolators.TOUCH_RESPONSE
            r3.setInterpolator(r9)
            r6 = 368(0x170, double:1.82E-321)
            r3.setDuration(r6)
            goto L_0x005d
        L_0x0056:
            com.android.wm.shell.animation.FlingAnimationUtils r12 = r8.mFlingAnimationUtils
            float r6 = r8.mQsExpansionHeight
            r12.apply((android.animation.Animator) r3, (float) r6, (float) r2, (float) r9)
        L_0x005d:
            if (r4 == 0) goto L_0x0064
            r6 = 350(0x15e, double:1.73E-321)
            r3.setDuration(r6)
        L_0x0064:
            com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda5 r9 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda5
            r9.<init>(r8)
            r3.addUpdateListener(r9)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$9 r9 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$9
            r9.<init>(r11)
            r3.addListener(r9)
            r8.mAnimatingQS = r1
            r3.start()
            r8.mQsExpansionAnimator = r3
            r8.mQsAnimatorExpand = r10
            float r9 = r8.computeQsExpansionFraction()
            int r9 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r9 != 0) goto L_0x008a
            int r9 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r9 != 0) goto L_0x008a
            goto L_0x008b
        L_0x008a:
            r1 = r5
        L_0x008b:
            r8.mQSAnimatingHiddenFromCollapsed = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.flingSettings(float, int, java.lang.Runnable, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$flingSettings$13$com-android-systemui-statusbar-phone-NotificationPanelViewController */
    public /* synthetic */ void mo44612x647b0faf(ValueAnimator valueAnimator) {
        setQsExpansion(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
        r1 = r9.mQs;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldQuickSettingsIntercept(float r10, float r11, float r12) {
        /*
            r9 = this;
            r0 = 0
            int r1 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            r2 = 0
            if (r1 <= 0) goto L_0x000f
            com.nothing.systemui.statusbar.phone.NotificationPanelViewControllerEx r1 = r9.mEx
            boolean r1 = r1.shouldQuickSettingsIntercept()
            if (r1 != 0) goto L_0x000f
            return r2
        L_0x000f:
            boolean r1 = r9.isQsExpansionEnabled()
            if (r1 == 0) goto L_0x0091
            boolean r1 = r9.mCollapsedOnDown
            if (r1 != 0) goto L_0x0091
            boolean r1 = r9.mKeyguardShowing
            if (r1 == 0) goto L_0x0025
            com.android.systemui.statusbar.phone.KeyguardBypassController r1 = r9.mKeyguardBypassController
            boolean r1 = r1.getBypassEnabled()
            if (r1 != 0) goto L_0x0091
        L_0x0025:
            boolean r1 = r9.mSplitShadeEnabled
            if (r1 == 0) goto L_0x002a
            goto L_0x0091
        L_0x002a:
            boolean r1 = r9.mKeyguardShowing
            if (r1 != 0) goto L_0x0038
            com.android.systemui.plugins.qs.QS r1 = r9.mQs
            if (r1 != 0) goto L_0x0033
            goto L_0x0038
        L_0x0033:
            android.view.View r1 = r1.getHeader()
            goto L_0x003a
        L_0x0038:
            com.android.systemui.statusbar.phone.KeyguardStatusBarView r1 = r9.mKeyguardStatusBar
        L_0x003a:
            boolean r3 = r9.mKeyguardShowing
            if (r3 != 0) goto L_0x004a
            com.android.systemui.plugins.qs.QS r3 = r9.mQs
            if (r3 != 0) goto L_0x0043
            goto L_0x004a
        L_0x0043:
            android.widget.FrameLayout r3 = r9.mQsFrame
            int r3 = r3.getTop()
            goto L_0x004b
        L_0x004a:
            r3 = r2
        L_0x004b:
            android.graphics.Region r4 = r9.mQsInterceptRegion
            android.widget.FrameLayout r5 = r9.mQsFrame
            float r5 = r5.getX()
            int r5 = (int) r5
            int r6 = r1.getTop()
            int r6 = r6 + r3
            android.widget.FrameLayout r7 = r9.mQsFrame
            float r7 = r7.getX()
            int r7 = (int) r7
            android.widget.FrameLayout r8 = r9.mQsFrame
            int r8 = r8.getWidth()
            int r7 = r7 + r8
            int r1 = r1.getBottom()
            int r1 = r1 + r3
            r4.set(r5, r6, r7, r1)
            com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager r1 = r9.mStatusBarTouchableRegionManager
            android.graphics.Region r3 = r9.mQsInterceptRegion
            r1.updateRegionForNotch(r3)
            android.graphics.Region r1 = r9.mQsInterceptRegion
            int r3 = (int) r10
            int r4 = (int) r11
            boolean r1 = r1.contains(r3, r4)
            boolean r3 = r9.mQsExpanded
            if (r3 == 0) goto L_0x0090
            if (r1 != 0) goto L_0x008e
            int r12 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r12 >= 0) goto L_0x008f
            boolean r9 = r9.isInQsArea(r10, r11)
            if (r9 == 0) goto L_0x008f
        L_0x008e:
            r2 = 1
        L_0x008f:
            return r2
        L_0x0090:
            return r1
        L_0x0091:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.shouldQuickSettingsIntercept(float, float, float):boolean");
    }

    /* access modifiers changed from: protected */
    public boolean canCollapsePanelOnTouch() {
        if ((!isInSettings() && this.mBarState == 1) || this.mNotificationStackScrollLayoutController.isScrolledToBottom()) {
            return true;
        }
        if (this.mSplitShadeEnabled || (!isInSettings() && !this.mIsPanelCollapseOnQQS)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public int getMaxPanelHeight() {
        int i;
        int i2 = this.mStatusBarMinHeight;
        if (this.mBarState != 1 && this.mNotificationStackScrollLayoutController.getNotGoneChildCount() == 0) {
            i2 = Math.max(i2, this.mQsMinExpansionHeight);
        }
        if (this.mQsExpandImmediate || this.mQsExpanded || ((this.mIsExpanding && this.mQsExpandedWhenExpandingStarted) || this.mPulsing)) {
            i = calculatePanelHeightQsExpanded();
        } else {
            i = calculatePanelHeightShade();
        }
        int max = Math.max(i2, i);
        if (max == 0 || Float.isNaN((float) max)) {
            Log.wtf(TAG, "maxPanelHeight is invalid. mOverExpansion: " + this.mOverExpansion + ", calculatePanelHeightQsExpanded: " + calculatePanelHeightQsExpanded() + ", calculatePanelHeightShade: " + calculatePanelHeightShade() + ", mStatusBarMinHeight = " + this.mStatusBarMinHeight + ", mQsMinExpansionHeight = " + this.mQsMinExpansionHeight);
        }
        return max;
    }

    public boolean isInSettings() {
        return this.mQsExpanded;
    }

    public boolean isExpanding() {
        return this.mIsExpanding;
    }

    /* access modifiers changed from: protected */
    public void onHeightUpdated(float f) {
        float f2;
        if (!this.mQsExpanded || this.mQsExpandImmediate || (this.mIsExpanding && this.mQsExpandedWhenExpandingStarted)) {
            if (this.mStackScrollerMeasuringPass <= 2) {
                positionClockAndNotifications();
            } else if (DEBUG_LOGCAT) {
                Log.d(TAG, "Unstable notification panel height. Aborting.");
            }
        }
        if (this.mQsExpandImmediate || (this.mQsExpanded && !this.mQsTracking && this.mQsExpansionAnimator == null && !this.mQsExpansionFromOverscroll)) {
            if (this.mKeyguardShowing) {
                f2 = f / ((float) getMaxPanelHeight());
            } else {
                float intrinsicPadding = this.mNotificationStackScrollLayoutController.getIntrinsicPadding() + this.mNotificationStackScrollLayoutController.getLayoutMinHeight();
                f2 = (f - intrinsicPadding) / (((float) calculatePanelHeightQsExpanded()) - intrinsicPadding);
            }
            int i = this.mQsMinExpansionHeight;
            setQsExpansion(((float) i) + (f2 * ((float) (this.mQsMaxExpansionHeight - i))));
        }
        updateExpandedHeight(f);
        updateHeader();
        updateNotificationTranslucency();
        updatePanelExpanded();
        updateGestureExclusionRect();
        this.mEx.updateQsStatusBarAlpha(isOnKeyguard(), getExpandedFraction());
        this.mEx.updateQsFrameAlpha(this.mQsFrame, getExpandedFraction());
    }

    private void updatePanelExpanded() {
        C2301QS qs;
        boolean z = !isFullyCollapsed() || this.mExpectingSynthesizedDown;
        if (this.mPanelExpanded != z) {
            this.mPanelExpanded = z;
            this.mHeadsUpManager.setIsPanelExpanded(z);
            this.mStatusBarTouchableRegionManager.setPanelExpanded(z);
            this.mCentralSurfaces.setPanelExpanded(z);
            if (!z && (qs = this.mQs) != null && qs.isCustomizing()) {
                this.mQs.closeCustomizer();
            }
        }
    }

    public boolean isPanelExpanded() {
        return this.mPanelExpanded;
    }

    /* access modifiers changed from: private */
    public int calculatePanelHeightShade() {
        int height = this.mNotificationStackScrollLayoutController.getHeight() - this.mNotificationStackScrollLayoutController.getEmptyBottomMargin();
        return this.mBarState == 1 ? Math.max(height, this.mClockPositionAlgorithm.getLockscreenStatusViewHeight() + this.mNotificationStackScrollLayoutController.getIntrinsicContentHeight()) : height;
    }

    /* access modifiers changed from: private */
    public int calculatePanelHeightQsExpanded() {
        float height = (float) ((this.mNotificationStackScrollLayoutController.getHeight() - this.mNotificationStackScrollLayoutController.getEmptyBottomMargin()) - this.mNotificationStackScrollLayoutController.getTopPadding());
        if (this.mNotificationStackScrollLayoutController.getNotGoneChildCount() == 0 && this.mNotificationStackScrollLayoutController.isShowingEmptyShadeView()) {
            height = this.mNotificationStackScrollLayoutController.getEmptyShadeViewHeight();
        }
        int i = this.mQsMaxExpansionHeight;
        ValueAnimator valueAnimator = this.mQsSizeChangeAnimator;
        if (valueAnimator != null) {
            i = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        }
        float max = ((float) Math.max(i, this.mBarState == 1 ? this.mClockPositionResult.stackScrollerPadding : 0)) + height + this.mNotificationStackScrollLayoutController.getTopPaddingOverflow();
        if (max > ((float) this.mNotificationStackScrollLayoutController.getHeight())) {
            max = Math.max(((float) i) + this.mNotificationStackScrollLayoutController.getLayoutMinHeight(), (float) this.mNotificationStackScrollLayoutController.getHeight());
        }
        return (int) max;
    }

    /* access modifiers changed from: private */
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
        return (float) Math.pow((double) Math.max(0.0f, Math.min(getExpandedHeight() / ((float) this.mQsMinExpansionHeight), 1.0f)), 0.75d);
    }

    /* access modifiers changed from: private */
    public void updateHeader() {
        if (this.mBarState == 1) {
            this.mKeyguardStatusBarViewController.updateViewState();
        }
        updateQsExpansion();
    }

    /* access modifiers changed from: protected */
    public float getHeaderTranslation() {
        if (this.mBarState == 1 && !this.mKeyguardBypassController.getBypassEnabled()) {
            return (float) (-this.mQs.getQsMinExpansionHeight());
        }
        float calculateAppearFraction = this.mNotificationStackScrollLayoutController.calculateAppearFraction(this.mExpandedHeight);
        float f = this.mQsExpansionHeight;
        float f2 = -f;
        if (!this.mSplitShadeEnabled && this.mBarState == 0) {
            f2 = QS_PARALLAX_AMOUNT * (-f);
        }
        if (this.mKeyguardBypassController.getBypassEnabled() && isOnKeyguard()) {
            calculateAppearFraction = this.mNotificationStackScrollLayoutController.calculateAppearFractionBypass();
            f2 = (float) (-this.mQs.getQsMinExpansionHeight());
        }
        return Math.min(0.0f, MathUtils.lerp(f2, 0.0f, Math.min(1.0f, calculateAppearFraction)));
    }

    private void updateKeyguardBottomAreaAlpha() {
        float min = Math.min(MathUtils.map(isUnlockHintRunning() ? 0.0f : 0.95f, 1.0f, 0.0f, 1.0f, getExpandedFraction()), 1.0f - computeQsExpansionFraction()) * this.mBottomAreaShadeAlpha;
        this.mKeyguardBottomArea.setAffordanceAlpha(min);
        this.mKeyguardBottomArea.setImportantForAccessibility(min == 0.0f ? 4 : 0);
        View ambientIndicationContainer = this.mCentralSurfaces.getAmbientIndicationContainer();
        if (ambientIndicationContainer != null) {
            ambientIndicationContainer.setAlpha(min);
        }
        this.mLockIconViewController.setAlpha(min);
    }

    /* access modifiers changed from: protected */
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
        C2301QS qs = this.mQs;
        if (qs != null) {
            qs.setHeaderListening(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onExpandingFinished() {
        this.mScrimController.onExpandingFinished();
        this.mNotificationStackScrollLayoutController.onExpansionStopped();
        this.mHeadsUpManager.onExpandingFinished();
        this.mConversationNotificationManager.onNotificationPanelExpandStateChanged(isFullyCollapsed());
        this.mIsExpanding = false;
        this.mMediaHierarchyManager.setCollapsingShadeFromQS(false);
        this.mMediaHierarchyManager.setQsExpanded(this.mQsExpanded);
        if (isFullyCollapsed()) {
            DejankUtils.postAfterTraversal(new Runnable() {
                public void run() {
                    NotificationPanelViewController.this.setListening(false);
                }
            });
            this.mView.postOnAnimation(new Runnable() {
                public void run() {
                    NotificationPanelViewController.this.mView.getParent().invalidateChild(NotificationPanelViewController.this.mView, NotificationPanelViewController.M_DUMMY_DIRTY_RECT);
                }
            });
        } else {
            setListening(true);
        }
        this.mQsExpandImmediate = false;
        setShowShelfOnly(false);
        this.mTwoFingerQsExpandPossible = false;
        updateTrackingHeadsUp((ExpandableNotificationRow) null);
        this.mExpandingFromHeadsUp = false;
        setPanelScrimMinFraction(0.0f);
        setKeyguardStatusBarAlpha(-1.0f);
    }

    private void updateTrackingHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        this.mTrackedHeadsUpNotification = expandableNotificationRow;
        for (int i = 0; i < this.mTrackingHeadsUpListeners.size(); i++) {
            this.mTrackingHeadsUpListeners.get(i).accept(expandableNotificationRow);
        }
    }

    public ExpandableNotificationRow getTrackedHeadsUpNotification() {
        return this.mTrackedHeadsUpNotification;
    }

    /* access modifiers changed from: private */
    public void setListening(boolean z) {
        this.mKeyguardStatusBarViewController.setBatteryListening(z);
        C2301QS qs = this.mQs;
        if (qs != null) {
            qs.setListening(z);
        }
    }

    public void expand(boolean z) {
        super.expand(z);
        setListening(true);
    }

    public void setOverExpansion(float f) {
        if (f != this.mOverExpansion) {
            super.setOverExpansion(f);
            updateQsFrameTranslation();
            this.mNotificationStackScrollLayoutController.setOverExpansion(f);
        }
    }

    private void updateQsFrameTranslation() {
        this.mQsFrameTranslateController.translateQsFrame(this.mQsFrame, this.mQs, this.mOverExpansion, this.mQsTranslationForFullShadeTransition);
    }

    /* access modifiers changed from: protected */
    public void onTrackingStarted() {
        this.mFalsingCollector.onTrackingStarted(!this.mKeyguardStateController.canDismissLockScreen());
        super.onTrackingStarted();
        this.mScrimController.onTrackingStarted();
        if (this.mQsFullyExpanded) {
            this.mQsExpandImmediate = true;
            setShowShelfOnly(true);
        }
        int i = this.mBarState;
        if (i == 1 || i == 2) {
            this.mAffordanceHelper.animateHideLeftRightIcon();
        }
        this.mNotificationStackScrollLayoutController.onPanelTrackingStarted();
        cancelPendingPanelCollapse();
        ((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).onPanelTrackingStarted();
    }

    /* access modifiers changed from: protected */
    public void onTrackingStopped(boolean z) {
        int i;
        this.mFalsingCollector.onTrackingStopped();
        super.onTrackingStopped(z);
        if (z) {
            this.mNotificationStackScrollLayoutController.setOverScrollAmount(0.0f, true, true);
        }
        this.mNotificationStackScrollLayoutController.onPanelTrackingStopped();
        if (z && (((i = this.mBarState) == 1 || i == 2) && !this.mHintAnimationRunning)) {
            this.mAffordanceHelper.reset(true);
        }
        this.mDepthController.setBlursDisabledForUnlock(false);
    }

    /* access modifiers changed from: private */
    public void updateMaxHeadsUpTranslation() {
        this.mNotificationStackScrollLayoutController.setHeadsUpBoundaries(getHeight(), this.mNavigationBarBottomHeight);
    }

    /* access modifiers changed from: protected */
    public void startUnlockHintAnimation() {
        if (this.mPowerManager.isPowerSaveMode() || this.mAmbientState.getDozeAmount() > 0.0f) {
            onUnlockHintStarted();
            onUnlockHintFinished();
            return;
        }
        super.startUnlockHintAnimation();
    }

    /* access modifiers changed from: protected */
    public void onUnlockHintFinished() {
        super.onUnlockHintFinished();
        this.mScrimController.setExpansionAffectsAlpha(true);
        this.mNotificationStackScrollLayoutController.setUnlockHintRunning(false);
    }

    /* access modifiers changed from: protected */
    public void onUnlockHintStarted() {
        super.onUnlockHintStarted();
        this.mScrimController.setExpansionAffectsAlpha(false);
        this.mNotificationStackScrollLayoutController.setUnlockHintRunning(true);
    }

    /* access modifiers changed from: protected */
    public boolean shouldUseDismissingAnimation() {
        return this.mBarState != 0 && (this.mKeyguardStateController.canDismissLockScreen() || !isTracking());
    }

    /* access modifiers changed from: protected */
    public boolean isTrackingBlocked() {
        return (this.mConflictingQsExpansionGesture && this.mQsExpanded) || this.mBlockingExpansionForCurrentTouch;
    }

    public boolean isQsExpanded() {
        return this.mQsExpanded;
    }

    public boolean isQsDetailShowing() {
        return this.mQs.isShowingDetail();
    }

    public boolean isQsCustomizing() {
        return this.mQs.isCustomizing();
    }

    public void closeQsDetail() {
        this.mQs.closeDetail();
    }

    public void closeQsCustomizer() {
        this.mQs.closeCustomizer();
    }

    public boolean isLaunchTransitionFinished() {
        return this.mIsLaunchTransitionFinished;
    }

    public boolean isLaunchTransitionRunning() {
        return this.mIsLaunchTransitionRunning;
    }

    public void setIsLaunchAnimationRunning(boolean z) {
        boolean z2 = this.mIsLaunchAnimationRunning;
        super.setIsLaunchAnimationRunning(z);
        if (z2 != this.mIsLaunchAnimationRunning) {
            this.mPanelEventsEmitter.notifyLaunchingActivityChanged(z);
        }
    }

    /* access modifiers changed from: protected */
    public void setIsClosing(boolean z) {
        boolean isClosing = isClosing();
        super.setIsClosing(z);
        if (isClosing != z) {
            this.mPanelEventsEmitter.notifyPanelCollapsingChanged(z);
        }
    }

    public void setLaunchTransitionEndRunnable(Runnable runnable) {
        this.mLaunchAnimationEndRunnable = runnable;
    }

    /* access modifiers changed from: private */
    public void updateDozingVisibilities(boolean z) {
        this.mKeyguardBottomArea.setDozing(this.mDozing, z);
        if (!this.mDozing && z) {
            this.mKeyguardStatusBarViewController.animateKeyguardStatusBarIn();
        }
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

    /* access modifiers changed from: protected */
    public boolean onMiddleClicked() {
        int i = this.mBarState;
        if (i != 1) {
            if (i == 2 && !this.mQsExpanded) {
                this.mStatusBarStateController.setState(1);
            }
            return true;
        }
        if (!this.mDozingOnDown) {
            if (!this.mUpdateMonitor.isFaceRecognitionEnable() || this.mUpdateMonitor.isFaceCameraStarting() || this.mUpdateMonitor.isFaceRecognitionSucceeded() || ((FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class)).isFrozenMode()) {
                this.mLockscreenGestureLogger.write(188, 0, 0);
                this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_LOCK_SHOW_HINT);
                startUnlockHintAnimation();
            } else {
                this.mUpdateMonitor.requestFaceAuth(true);
            }
            if (this.mUpdateMonitor.isFaceEnrolled()) {
                this.mUpdateMonitor.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.UNLOCK_INTENT, "lockScreenEmptySpaceTap");
            }
        }
        return true;
    }

    public void setPanelAlpha(int i, boolean z) {
        if (this.mPanelAlpha != i) {
            this.mPanelAlpha = i;
            PropertyAnimator.setProperty(this.mView, this.mPanelAlphaAnimator, (float) i, i == 255 ? this.mPanelAlphaInPropertiesAnimator : this.mPanelAlphaOutPropertiesAnimator, z);
        }
    }

    public void setPanelAlphaEndAction(Runnable runnable) {
        this.mPanelAlphaEndAction = runnable;
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        this.mHeadsUpAnimatingAway = z;
        this.mNotificationStackScrollLayoutController.setHeadsUpAnimatingAway(z);
        updateVisibility();
    }

    public void setBouncerShowing(boolean z) {
        this.mBouncerShowing = z;
        updateVisibility();
    }

    /* access modifiers changed from: protected */
    public boolean shouldPanelBeVisible() {
        if ((this.mHeadsUpAnimatingAway || this.mHeadsUpPinnedMode) || isExpanded() || this.mBouncerShowing) {
            return true;
        }
        return false;
    }

    public void setHeadsUpManager(HeadsUpManagerPhone headsUpManagerPhone) {
        super.setHeadsUpManager(headsUpManagerPhone);
        this.mHeadsUpTouchHelper = new HeadsUpTouchHelper(headsUpManagerPhone, this.mNotificationStackScrollLayoutController.getHeadsUpCallback(), this);
    }

    public void setTrackedHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        if (expandableNotificationRow != null) {
            updateTrackingHeadsUp(expandableNotificationRow);
            this.mExpandingFromHeadsUp = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onClosingFinished() {
        this.mCentralSurfaces.onClosingFinished();
        setClosingWithAlphaFadeout(false);
        this.mMediaHierarchyManager.closeGuts();
    }

    private void setClosingWithAlphaFadeout(boolean z) {
        this.mClosingWithAlphaFadeOut = z;
        this.mNotificationStackScrollLayoutController.forceNoOverlappingRendering(z);
    }

    /* access modifiers changed from: protected */
    public void updateExpandedHeight(float f) {
        if (this.mTracking) {
            this.mNotificationStackScrollLayoutController.setExpandingVelocity(getCurrentExpandVelocity());
        }
        if (this.mKeyguardBypassController.getBypassEnabled() && isOnKeyguard()) {
            f = (float) getMaxPanelHeight();
        }
        this.mNotificationStackScrollLayoutController.setExpandedHeight(f);
        updateKeyguardBottomAreaAlpha();
        updateStatusBarIcons();
        runPostCollapseRunnablesIfNeeded();
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

    public boolean isOnKeyguard() {
        return this.mBarState == 1;
    }

    public void setPanelScrimMinFraction(float f) {
        this.mMinFraction = f;
        this.mDepthController.setPanelPullDownMinFraction(f);
        this.mScrimController.setPanelScrimMinFraction(this.mMinFraction);
    }

    public void clearNotificationEffects() {
        this.mCentralSurfaces.clearNotificationEffects();
    }

    /* access modifiers changed from: protected */
    public boolean isPanelVisibleBecauseOfHeadsUp() {
        return (this.mHeadsUpManager.hasPinnedHeadsUp() || this.mHeadsUpAnimatingAway) && this.mBarState == 0;
    }

    public void launchCamera(boolean z, int i) {
        boolean z2 = true;
        if (i == 1) {
            this.mLastCameraLaunchSource = KeyguardBottomAreaView.CAMERA_LAUNCH_SOURCE_POWER_DOUBLE_TAP;
        } else if (i == 0) {
            this.mLastCameraLaunchSource = KeyguardBottomAreaView.CAMERA_LAUNCH_SOURCE_WIGGLE;
        } else if (i == 2) {
            this.mLastCameraLaunchSource = KeyguardBottomAreaView.CAMERA_LAUNCH_SOURCE_LIFT_TRIGGER;
        } else {
            this.mLastCameraLaunchSource = KeyguardBottomAreaView.CAMERA_LAUNCH_SOURCE_AFFORDANCE;
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
        if (!this.mCentralSurfaces.isCameraAllowedByAdmin()) {
            return false;
        }
        ResolveInfo resolveCameraIntent = this.mKeyguardBottomArea.resolveCameraIntent();
        String str = (resolveCameraIntent == null || resolveCameraIntent.activityInfo == null) ? null : resolveCameraIntent.activityInfo.packageName;
        if (str == null) {
            return false;
        }
        if ((this.mBarState != 0 || !isForegroundApp(str)) && !this.mAffordanceHelper.isSwipingInProgress()) {
            return true;
        }
        return false;
    }

    private boolean isForegroundApp(String str) {
        List<ActivityManager.RunningTaskInfo> runningTasks = this.mActivityManager.getRunningTasks(1);
        if (runningTasks.isEmpty() || !str.equals(runningTasks.get(0).topActivity.getPackageName())) {
            return false;
        }
        return true;
    }

    public boolean hideStatusBarIconsWhenExpanded() {
        if (this.mIsLaunchAnimationRunning) {
            return this.mHideIconsDuringLaunchAnimation;
        }
        HeadsUpAppearanceController headsUpAppearanceController = this.mHeadsUpAppearanceController;
        if (headsUpAppearanceController != null && headsUpAppearanceController.shouldBeVisible()) {
            return false;
        }
        if (!isFullWidth() || !this.mShowIconsWhenExpanded) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void animateNextNotificationBounds(long j, long j2) {
        this.mAnimateNextNotificationBounds = true;
        this.mNotificationBoundsAnimationDuration = j;
        this.mNotificationBoundsAnimationDelay = j2;
    }

    public void setTouchAndAnimationDisabled(boolean z) {
        super.setTouchAndAnimationDisabled(z);
        if (z && this.mAffordanceHelper.isSwipingInProgress() && !this.mIsLaunchTransitionRunning) {
            this.mAffordanceHelper.reset(false);
        }
        this.mNotificationStackScrollLayoutController.setAnimationsEnabled(!z);
    }

    public void setDozing(boolean z, boolean z2, PointF pointF) {
        if (z != this.mDozing) {
            this.mView.setDozing(z);
            this.mDozing = z;
            this.mNotificationStackScrollLayoutController.setDozing(z, z2, pointF);
            this.mKeyguardBottomArea.setDozing(this.mDozing, z2);
            this.mKeyguardStatusBarViewController.setDozing(this.mDozing);
            if (z) {
                this.mBottomAreaShadeAlphaAnimator.cancel();
            }
            int i = this.mBarState;
            if (i == 1 || i == 2) {
                updateDozingVisibilities(z2);
            }
            this.mStatusBarStateController.setAndInstrumentDozeAmount(this.mView, z ? 1.0f : 0.0f, z2);
        }
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

    public void setAmbientIndicationTop(int i, boolean z) {
        int bottom = z ? this.mNotificationStackScrollLayoutController.getBottom() - i : 0;
        if (this.mAmbientIndicationBottomPadding != bottom) {
            this.mAmbientIndicationBottomPadding = bottom;
            updateMaxDisplayedNotifications(true);
        }
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

    public KeyguardBottomAreaView getKeyguardBottomAreaView() {
        return this.mKeyguardBottomArea;
    }

    public void setUserSetupComplete(boolean z) {
        this.mUserSetupComplete = z;
        this.mKeyguardBottomArea.setUserSetupComplete(z);
    }

    public void applyLaunchAnimationProgress(float f) {
        boolean z = LaunchAnimator.getProgress(ActivityLaunchAnimator.TIMINGS, f, ANIMATION_DELAY_ICON_FADE_IN, 100) == 0.0f;
        if (z != this.mHideIconsDuringLaunchAnimation) {
            this.mHideIconsDuringLaunchAnimation = z;
            if (!z) {
                this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, true);
            }
        }
    }

    public void addTrackingHeadsUpListener(Consumer<ExpandableNotificationRow> consumer) {
        this.mTrackingHeadsUpListeners.add(consumer);
    }

    public void removeTrackingHeadsUpListener(Consumer<ExpandableNotificationRow> consumer) {
        this.mTrackingHeadsUpListeners.remove((Object) consumer);
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

    public void prepareFoldToAodAnimation() {
        showAodUi();
        this.mView.setTranslationX((float) (-this.mView.getResources().getDimensionPixelSize(C1893R.dimen.below_clock_padding_start)));
        this.mView.setAlpha(0.0f);
    }

    public void startFoldToAodAnimation(final Runnable runnable) {
        this.mView.animate().translationX(0.0f).alpha(1.0f).setDuration(600).setInterpolator(Interpolators.EMPHASIZED_DECELERATE).setListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                runnable.run();
            }

            public void onAnimationEnd(Animator animator) {
                runnable.run();
            }
        }).start();
        this.mKeyguardStatusViewController.animateFoldToAod();
    }

    public void cancelFoldToAodAnimation() {
        cancelAnimation();
        resetAlpha();
        resetTranslation();
    }

    public void setImportantForAccessibility(int i) {
        this.mView.setImportantForAccessibility(i);
    }

    public void blockExpansionForCurrentTouch() {
        this.mBlockingExpansionForCurrentTouch = this.mTracking;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.increaseIndent();
        asIndenting.println("gestureExclusionRect:" + calculateGestureExclusionRect());
        asIndenting.println("applyQSClippingImmediately: top(" + this.mQsClipTop + ") bottom(" + this.mQsClipBottom + NavigationBarInflaterView.KEY_CODE_END);
        asIndenting.println("qsVisible:" + this.mQsVisible);
        new DumpsysTableLogger(TAG, NPVCDownEventState.TABLE_HEADERS, this.mLastDownEvents.toList()).printTableData(asIndenting);
        asIndenting.decreaseIndent();
        KeyguardStatusBarViewController keyguardStatusBarViewController = this.mKeyguardStatusBarViewController;
        if (keyguardStatusBarViewController != null) {
            keyguardStatusBarViewController.dump(printWriter, strArr);
        }
    }

    public boolean hasActiveClearableNotifications() {
        return this.mNotificationStackScrollLayoutController.hasActiveClearableNotifications(0);
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
        return this.mNotificationListContainer.hasPulsingNotifications();
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

    public void initDependencies(CentralSurfaces centralSurfaces, Runnable runnable, NotificationShelfController notificationShelfController) {
        setCentralSurfaces(centralSurfaces);
        this.mHideExpandedRunnable = runnable;
        this.mNotificationStackScrollLayoutController.setShelfController(notificationShelfController);
        this.mNotificationShelfController = notificationShelfController;
        this.mLockscreenShadeTransitionController.bindController(notificationShelfController);
        updateMaxDisplayedNotifications(true);
    }

    public void setAlpha(float f) {
        NTLogUtil.m1680d(TAG, "alpha: " + f + ", cb: " + Debug.getCallers(2));
        this.mView.setAlpha(f);
    }

    public void resetTranslation() {
        this.mView.setTranslationX(0.0f);
    }

    public void resetAlpha() {
        this.mView.setAlpha(1.0f);
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

    public OnLayoutChangeListener createLayoutChangeListener() {
        return new OnLayoutChangeListener();
    }

    /* access modifiers changed from: protected */
    public PanelViewController.TouchHandler createTouchHandler() {
        return new PanelViewController.TouchHandler() {
            private long mLastTouchDownTime = -1;

            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (NotificationPanelViewController.SPEW_LOGCAT) {
                    Log.v(PanelViewController.TAG, "NPVC onInterceptTouchEvent (" + motionEvent.getId() + "): (" + motionEvent.getX() + NavigationBarInflaterView.BUTTON_SEPARATOR + motionEvent.getY() + NavigationBarInflaterView.KEY_CODE_END);
                }
                if (NotificationPanelViewController.this.mBlockTouches || NotificationPanelViewController.this.mQs.disallowPanelTouches()) {
                    return false;
                }
                NotificationPanelViewController.this.initDownStates(motionEvent);
                if (NotificationPanelViewController.this.mCentralSurfaces.isBouncerShowing()) {
                    return true;
                }
                if (!NotificationPanelViewController.this.mCommandQueue.panelsEnabled() || NotificationPanelViewController.this.mNotificationStackScrollLayoutController.isLongPressInProgress() || !NotificationPanelViewController.this.mHeadsUpTouchHelper.onInterceptTouchEvent(motionEvent)) {
                    NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                    if (!notificationPanelViewController.shouldQuickSettingsIntercept(notificationPanelViewController.mDownX, NotificationPanelViewController.this.mDownY, 0.0f) && NotificationPanelViewController.this.mPulseExpansionHandler.onInterceptTouchEvent(motionEvent)) {
                        return true;
                    }
                    if ((NotificationPanelViewController.this.mInSplitShade && NotificationPanelViewController.this.mQsExpanded) || NotificationPanelViewController.this.isFullyCollapsed() || !NotificationPanelViewController.this.onQsIntercept(motionEvent)) {
                        return super.onInterceptTouchEvent(motionEvent);
                    }
                    if (NotificationPanelViewController.DEBUG_LOGCAT) {
                        Log.d(PanelViewController.TAG, "onQsIntercept true");
                    }
                    return true;
                }
                NotificationPanelViewController.this.mMetricsLogger.count(NotificationPanelViewController.COUNTER_PANEL_OPEN, 1);
                NotificationPanelViewController.this.mMetricsLogger.count(NotificationPanelViewController.COUNTER_PANEL_OPEN_PEEK, 1);
                return true;
            }

            /* JADX WARNING: Code restructure failed: missing block: B:32:0x009b, code lost:
                if (com.android.systemui.statusbar.phone.NotificationPanelViewController.access$5600(r0, com.android.systemui.statusbar.phone.NotificationPanelViewController.access$5400(r0), com.android.systemui.statusbar.phone.NotificationPanelViewController.access$5500(r6.this$0), 0.0f) != false) goto L_0x009d;
             */
            /* JADX WARNING: Removed duplicated region for block: B:40:0x00ba A[RETURN] */
            /* JADX WARNING: Removed duplicated region for block: B:41:0x00bb  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public boolean onTouch(android.view.View r7, android.view.MotionEvent r8) {
                /*
                    r6 = this;
                    int r0 = r8.getAction()
                    r1 = 1
                    if (r0 != 0) goto L_0x001f
                    long r2 = r8.getDownTime()
                    long r4 = r6.mLastTouchDownTime
                    int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                    if (r0 != 0) goto L_0x0019
                    java.lang.String r6 = com.android.systemui.statusbar.phone.PanelViewController.TAG
                    java.lang.String r7 = "Duplicate down event detected... ignoring"
                    android.util.Log.w(r6, r7)
                    return r1
                L_0x0019:
                    long r2 = r8.getDownTime()
                    r6.mLastTouchDownTime = r2
                L_0x001f:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mBlockTouches
                    r2 = 0
                    if (r0 != 0) goto L_0x01b6
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mQsFullyExpanded
                    if (r0 == 0) goto L_0x0042
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.plugins.qs.QS r0 = r0.mQs
                    if (r0 == 0) goto L_0x0042
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.plugins.qs.QS r0 = r0.mQs
                    boolean r0 = r0.disallowPanelTouches()
                    if (r0 == 0) goto L_0x0042
                    goto L_0x01b6
                L_0x0042:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.CentralSurfaces r0 = r0.mCentralSurfaces
                    boolean r0 = r0.isBouncerShowingScrimmed()
                    if (r0 != 0) goto L_0x01b6
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.CentralSurfaces r0 = r0.mCentralSurfaces
                    boolean r0 = r0.isBouncerShowingOverDream()
                    if (r0 == 0) goto L_0x0058
                    goto L_0x01b6
                L_0x0058:
                    int r0 = r8.getAction()
                    if (r0 == r1) goto L_0x0065
                    int r0 = r8.getAction()
                    r3 = 3
                    if (r0 != r3) goto L_0x006a
                L_0x0065:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean unused = r0.mBlockingExpansionForCurrentTouch = r2
                L_0x006a:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mLastEventSynthesizedDown
                    if (r0 == 0) goto L_0x007d
                    int r0 = r8.getAction()
                    if (r0 != r1) goto L_0x007d
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    r0.expand(r1)
                L_0x007d:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    r0.initDownStates(r8)
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mIsExpanding
                    if (r0 != 0) goto L_0x009d
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    float r3 = r0.mDownX
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r4 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    float r4 = r4.mDownY
                    r5 = 0
                    boolean r0 = r0.shouldQuickSettingsIntercept(r3, r4, r5)
                    if (r0 == 0) goto L_0x00a9
                L_0x009d:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.PulseExpansionHandler r0 = r0.mPulseExpansionHandler
                    boolean r0 = r0.isExpanding()
                    if (r0 == 0) goto L_0x00ab
                L_0x00a9:
                    r0 = r1
                    goto L_0x00ac
                L_0x00ab:
                    r0 = r2
                L_0x00ac:
                    if (r0 == 0) goto L_0x00bb
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.PulseExpansionHandler r0 = r0.mPulseExpansionHandler
                    boolean r0 = r0.onTouchEvent(r8)
                    if (r0 == 0) goto L_0x00bb
                    return r1
                L_0x00bb:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mListenForHeadsUp
                    if (r0 == 0) goto L_0x00f2
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.HeadsUpTouchHelper r0 = r0.mHeadsUpTouchHelper
                    boolean r0 = r0.isTrackingHeadsUp()
                    if (r0 != 0) goto L_0x00f2
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r0.mNotificationStackScrollLayoutController
                    boolean r0 = r0.isLongPressInProgress()
                    if (r0 != 0) goto L_0x00f2
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.HeadsUpTouchHelper r0 = r0.mHeadsUpTouchHelper
                    boolean r0 = r0.onInterceptTouchEvent(r8)
                    if (r0 == 0) goto L_0x00f2
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.internal.logging.MetricsLogger r0 = r0.mMetricsLogger
                    java.lang.String r3 = "panel_open_peek"
                    r0.count(r3, r1)
                L_0x00f2:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mIsExpanding
                    if (r0 == 0) goto L_0x0100
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mHintAnimationRunning
                    if (r0 == 0) goto L_0x0124
                L_0x0100:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mQsExpanded
                    if (r0 != 0) goto L_0x0124
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    int r0 = r0.mBarState
                    if (r0 == 0) goto L_0x0124
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r0 = r0.mDozing
                    if (r0 != 0) goto L_0x0124
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.KeyguardAffordanceHelper r0 = r0.mAffordanceHelper
                    boolean r0 = r0.onTouchEvent(r8)
                    r0 = r0 | r2
                    goto L_0x0125
                L_0x0124:
                    r0 = r2
                L_0x0125:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r3 = r3.mOnlyAffordanceInThisMotion
                    if (r3 == 0) goto L_0x012e
                    return r1
                L_0x012e:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.HeadsUpTouchHelper r3 = r3.mHeadsUpTouchHelper
                    boolean r3 = r3.onTouchEvent(r8)
                    r0 = r0 | r3
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.HeadsUpTouchHelper r3 = r3.mHeadsUpTouchHelper
                    boolean r3 = r3.isTrackingHeadsUp()
                    if (r3 != 0) goto L_0x015b
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r3 = r3.handleQsTouch(r8)
                    if (r3 == 0) goto L_0x015b
                    boolean r6 = com.android.systemui.statusbar.phone.NotificationPanelViewController.DEBUG_LOGCAT
                    if (r6 == 0) goto L_0x015a
                    java.lang.String r6 = com.android.systemui.statusbar.phone.PanelViewController.TAG
                    java.lang.String r7 = "handleQsTouch true"
                    android.util.Log.d(r6, r7)
                L_0x015a:
                    return r1
                L_0x015b:
                    int r3 = r8.getActionMasked()
                    if (r3 != 0) goto L_0x0175
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r3 = r3.isFullyCollapsed()
                    if (r3 == 0) goto L_0x0175
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.internal.logging.MetricsLogger r0 = r0.mMetricsLogger
                    java.lang.String r3 = "panel_open"
                    r0.count(r3, r1)
                    r0 = r1
                L_0x0175:
                    int r3 = r8.getActionMasked()
                    if (r3 != 0) goto L_0x019c
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r3 = r3.isFullyExpanded()
                    if (r3 == 0) goto L_0x019c
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r3 = r3.mStatusBarKeyguardViewManager
                    boolean r3 = r3.isShowing()
                    if (r3 == 0) goto L_0x019c
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r3 = r3.mStatusBarKeyguardViewManager
                    float r4 = r8.getX()
                    r3.updateKeyguardPosition(r4)
                L_0x019c:
                    boolean r7 = super.onTouch(r7, r8)
                    r7 = r7 | r0
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r8 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r8 = r8.mDozing
                    if (r8 == 0) goto L_0x01b5
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r6 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
                    boolean r6 = r6.mPulsing
                    if (r6 != 0) goto L_0x01b5
                    if (r7 == 0) goto L_0x01b4
                    goto L_0x01b5
                L_0x01b4:
                    r1 = r2
                L_0x01b5:
                    return r1
                L_0x01b6:
                    return r2
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.C302016.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        };
    }

    /* access modifiers changed from: protected */
    public PanelViewController.OnConfigurationChangedListener createOnConfigurationChangedListener() {
        return new OnConfigurationChangedListener();
    }

    public NotificationStackScrollLayoutController getNotificationStackScrollLayoutController() {
        return this.mNotificationStackScrollLayoutController;
    }

    public void disable(int i, int i2, boolean z) {
        this.mLargeScreenShadeHeaderController.disable(i, i2, z);
    }

    public boolean closeUserSwitcherIfOpen() {
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            return keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(true);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void updateUserSwitcherFlags() {
        boolean z = this.mResources.getBoolean(17891689);
        this.mKeyguardUserSwitcherEnabled = z;
        this.mKeyguardQsUserSwitchEnabled = z && this.mFeatureFlags.isEnabled(Flags.QS_USER_DETAIL_SHORTCUT);
    }

    /* access modifiers changed from: private */
    public void registerSettingsChangeListener() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("user_switcher_enabled"), false, this.mSettingsChangeObserver);
    }

    /* access modifiers changed from: private */
    public void unregisterSettingsChangeListener() {
        this.mContentResolver.unregisterContentObserver(this.mSettingsChangeObserver);
    }

    public void updateSystemUiStateFlags() {
        this.mSysUiState.setFlag(4, isFullyExpanded() && !isInSettings()).setFlag(2048, isInSettings()).commitUpdate(this.mDisplayId);
    }

    private class OnHeightChangedListener implements ExpandableView.OnHeightChangedListener {
        public void onReset(ExpandableView expandableView) {
        }

        private OnHeightChangedListener() {
        }

        public void onHeightChanged(ExpandableView expandableView, boolean z) {
            if (expandableView != null || !NotificationPanelViewController.this.mQsExpanded) {
                if (z && NotificationPanelViewController.this.mInterpolatedDarkAmount == 0.0f) {
                    boolean unused = NotificationPanelViewController.this.mAnimateNextPositionUpdate = true;
                }
                ExpandableView firstChildNotGone = NotificationPanelViewController.this.mNotificationStackScrollLayoutController.getFirstChildNotGone();
                ExpandableNotificationRow expandableNotificationRow = firstChildNotGone instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) firstChildNotGone : null;
                if (expandableNotificationRow != null && (expandableView == expandableNotificationRow || expandableNotificationRow.getNotificationParent() == expandableNotificationRow)) {
                    NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                }
                if (NotificationPanelViewController.this.mKeyguardShowing) {
                    NotificationPanelViewController.this.updateMaxDisplayedNotifications(true);
                }
                NotificationPanelViewController.this.requestPanelHeightUpdate();
            }
        }
    }

    private class CollapseExpandAction implements Runnable {
        private CollapseExpandAction() {
        }

        public void run() {
            NotificationPanelViewController.this.onQsExpansionStarted();
            if (NotificationPanelViewController.this.mQsExpanded) {
                NotificationPanelViewController.this.flingSettings(0.0f, 1, (Runnable) null, true);
            } else if (NotificationPanelViewController.this.isQsExpansionEnabled()) {
                NotificationPanelViewController.this.mLockscreenGestureLogger.write(195, 0, 0);
                NotificationPanelViewController.this.flingSettings(0.0f, 0, (Runnable) null, true);
            }
        }
    }

    private class OnOverscrollTopChangedListener implements NotificationStackScrollLayout.OnOverscrollTopChangedListener {
        private OnOverscrollTopChangedListener() {
        }

        public void onOverscrollTopChanged(float f, boolean z) {
            if (!NotificationPanelViewController.this.mSplitShadeEnabled) {
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
                NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                if (i == 0) {
                    z2 = false;
                }
                boolean unused = notificationPanelViewController.mQsExpansionFromOverscroll = z2;
                float unused2 = NotificationPanelViewController.this.mLastOverscroll = f;
                NotificationPanelViewController.this.updateQsState();
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                notificationPanelViewController2.setQsExpansion(((float) notificationPanelViewController2.mQsMinExpansionHeight) + f);
            }
        }

        public void flingTopOverscroll(float f, boolean z) {
            if (NotificationPanelViewController.this.mSplitShadeEnabled) {
                NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                if (notificationPanelViewController.touchXOutsideOfQs(notificationPanelViewController.mInitialTouchX)) {
                    return;
                }
            }
            float unused = NotificationPanelViewController.this.mLastOverscroll = 0.0f;
            boolean unused2 = NotificationPanelViewController.this.mQsExpansionFromOverscroll = false;
            if (z) {
                NotificationPanelViewController.this.setOverScrolling(false);
            }
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            notificationPanelViewController2.setQsExpansion(notificationPanelViewController2.mQsExpansionHeight);
            boolean access$3900 = NotificationPanelViewController.this.isQsExpansionEnabled();
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            if (!access$3900 && z) {
                f = 0.0f;
            }
            notificationPanelViewController3.flingSettings(f, (!z || !access$3900) ? 1 : 0, new C3034x5c9cd5cc(this), false);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$flingTopOverscroll$0$com-android-systemui-statusbar-phone-NotificationPanelViewController$OnOverscrollTopChangedListener */
        public /* synthetic */ void mo44738xe753d5c7() {
            NotificationPanelViewController.this.setOverScrolling(false);
            NotificationPanelViewController.this.updateQsState();
        }
    }

    private class DynamicPrivacyControlListener implements DynamicPrivacyController.Listener {
        private DynamicPrivacyControlListener() {
        }

        public void onDynamicPrivacyChanged() {
            if (NotificationPanelViewController.this.mLinearDarkAmount == 0.0f) {
                boolean unused = NotificationPanelViewController.this.mAnimateNextPositionUpdate = true;
            }
        }
    }

    private class KeyguardAffordanceHelperCallback implements KeyguardAffordanceHelper.Callback {
        private KeyguardAffordanceHelperCallback() {
        }

        public void onAnimationToSideStarted(boolean z, float f, float f2) {
            if (NotificationPanelViewController.this.mView.getLayoutDirection() != 1) {
                z = !z;
            }
            boolean unused = NotificationPanelViewController.this.mIsLaunchTransitionRunning = true;
            Runnable unused2 = NotificationPanelViewController.this.mLaunchAnimationEndRunnable = null;
            float displayDensity = NotificationPanelViewController.this.mCentralSurfaces.getDisplayDensity();
            int abs = Math.abs((int) (f / displayDensity));
            int abs2 = Math.abs((int) (f2 / displayDensity));
            if (z) {
                NotificationPanelViewController.this.mLockscreenGestureLogger.write(190, abs, abs2);
                NotificationPanelViewController.this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_DIALER);
                NotificationPanelViewController.this.mFalsingCollector.onLeftAffordanceOn();
                if (NotificationPanelViewController.this.mFalsingCollector.shouldEnforceBouncer()) {
                    NotificationPanelViewController.this.mCentralSurfaces.executeRunnableDismissingKeyguard(new C3031xe911bb01(this), (Runnable) null, true, false, true);
                } else {
                    NotificationPanelViewController.this.mKeyguardBottomArea.launchLeftAffordance();
                }
            } else {
                if (KeyguardBottomAreaView.CAMERA_LAUNCH_SOURCE_AFFORDANCE.equals(NotificationPanelViewController.this.mLastCameraLaunchSource)) {
                    NotificationPanelViewController.this.mLockscreenGestureLogger.write(189, abs, abs2);
                    NotificationPanelViewController.this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_CAMERA);
                }
                NotificationPanelViewController.this.mFalsingCollector.onCameraOn();
                if (NotificationPanelViewController.this.mFalsingCollector.shouldEnforceBouncer()) {
                    NotificationPanelViewController.this.mCentralSurfaces.executeRunnableDismissingKeyguard(new C3032xe911bb02(this), (Runnable) null, true, false, true);
                } else {
                    NotificationPanelViewController.this.mKeyguardBottomArea.launchCamera(NotificationPanelViewController.this.mLastCameraLaunchSource);
                }
            }
            NotificationPanelViewController.this.mCentralSurfaces.startLaunchTransitionTimeout();
            boolean unused3 = NotificationPanelViewController.this.mBlockTouches = true;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAnimationToSideStarted$0$com-android-systemui-statusbar-phone-NotificationPanelViewController$KeyguardAffordanceHelperCallback */
        public /* synthetic */ void mo44730xf71ae06c() {
            NotificationPanelViewController.this.mKeyguardBottomArea.launchLeftAffordance();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAnimationToSideStarted$1$com-android-systemui-statusbar-phone-NotificationPanelViewController$KeyguardAffordanceHelperCallback */
        public /* synthetic */ void mo44731x3a814fad() {
            NotificationPanelViewController.this.mKeyguardBottomArea.launchCamera(NotificationPanelViewController.this.mLastCameraLaunchSource);
        }

        public void onAnimationToSideEnded() {
            boolean unused = NotificationPanelViewController.this.mIsLaunchTransitionRunning = false;
            boolean unused2 = NotificationPanelViewController.this.mIsLaunchTransitionFinished = true;
            if (NotificationPanelViewController.this.mLaunchAnimationEndRunnable != null) {
                NotificationPanelViewController.this.mLaunchAnimationEndRunnable.run();
                Runnable unused3 = NotificationPanelViewController.this.mLaunchAnimationEndRunnable = null;
            }
            NotificationPanelViewController.this.mCentralSurfaces.readyForKeyguardDone();
        }

        public float getMaxTranslationDistance() {
            return (float) Math.hypot((double) NotificationPanelViewController.this.mView.getWidth(), (double) NotificationPanelViewController.this.getHeight());
        }

        public void onSwipingStarted(boolean z) {
            NotificationPanelViewController.this.mFalsingCollector.onAffordanceSwipingStarted(z);
            if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
                z = !z;
            }
            if (z) {
                NotificationPanelViewController.this.mKeyguardBottomArea.bindCameraPrewarmService();
            }
            NotificationPanelViewController.this.mView.requestDisallowInterceptTouchEvent(true);
            boolean unused = NotificationPanelViewController.this.mOnlyAffordanceInThisMotion = true;
            boolean unused2 = NotificationPanelViewController.this.mQsTracking = false;
        }

        public void onSwipingAborted() {
            NotificationPanelViewController.this.mFalsingCollector.onAffordanceSwipingAborted();
            NotificationPanelViewController.this.mKeyguardBottomArea.unbindCameraPrewarmService(false);
        }

        public void onIconClicked(boolean z) {
            if (!NotificationPanelViewController.this.mHintAnimationRunning) {
                NotificationPanelViewController.this.mHintAnimationRunning = true;
                NotificationPanelViewController.this.mAffordanceHelper.startHintAnimation(z, new C3033xe911bb03(this));
                if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
                    z = !z;
                }
                if (z) {
                    NotificationPanelViewController.this.mCentralSurfaces.onCameraHintStarted();
                } else if (NotificationPanelViewController.this.mKeyguardBottomArea.isLeftVoiceAssist()) {
                    NotificationPanelViewController.this.mCentralSurfaces.onVoiceAssistHintStarted();
                } else {
                    NotificationPanelViewController.this.mCentralSurfaces.onPhoneHintStarted();
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onIconClicked$2$com-android-systemui-statusbar-phone-NotificationPanelViewController$KeyguardAffordanceHelperCallback */
        public /* synthetic */ void mo44732x2cad4459() {
            NotificationPanelViewController.this.mHintAnimationRunning = false;
            NotificationPanelViewController.this.mCentralSurfaces.onHintFinished();
        }

        public KeyguardAffordanceView getLeftIcon() {
            return NotificationPanelViewController.this.mView.getLayoutDirection() == 1 ? NotificationPanelViewController.this.mKeyguardBottomArea.getRightView() : NotificationPanelViewController.this.mKeyguardBottomArea.getLeftView();
        }

        public KeyguardAffordanceView getRightIcon() {
            return NotificationPanelViewController.this.mView.getLayoutDirection() == 1 ? NotificationPanelViewController.this.mKeyguardBottomArea.getLeftView() : NotificationPanelViewController.this.mKeyguardBottomArea.getRightView();
        }

        public View getLeftPreview() {
            return NotificationPanelViewController.this.mView.getLayoutDirection() == 1 ? NotificationPanelViewController.this.mKeyguardBottomArea.getRightPreview() : NotificationPanelViewController.this.mKeyguardBottomArea.getLeftPreview();
        }

        public View getRightPreview() {
            return NotificationPanelViewController.this.mView.getLayoutDirection() == 1 ? NotificationPanelViewController.this.mKeyguardBottomArea.getLeftPreview() : NotificationPanelViewController.this.mKeyguardBottomArea.getRightPreview();
        }

        public float getAffordanceFalsingFactor() {
            return NotificationPanelViewController.this.mCentralSurfaces.isWakeUpComingFromTouch() ? 1.5f : 1.0f;
        }

        public boolean needsAntiFalsing() {
            return NotificationPanelViewController.this.mBarState == 1;
        }
    }

    private class OnEmptySpaceClickListener implements NotificationStackScrollLayout.OnEmptySpaceClickListener {
        private OnEmptySpaceClickListener() {
        }

        public void onEmptySpaceClicked(float f, float f2) {
            NotificationPanelViewController.this.onEmptySpaceClick(f);
        }
    }

    private class MyOnHeadsUpChangedListener implements OnHeadsUpChangedListener {
        private MyOnHeadsUpChangedListener() {
        }

        public void onHeadsUpPinnedModeChanged(boolean z) {
            if (z) {
                NotificationPanelViewController.this.mHeadsUpExistenceChangedRunnable.run();
                NotificationPanelViewController.this.updateNotificationTranslucency();
            } else {
                NotificationPanelViewController.this.setHeadsUpAnimatingAway(true);
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.runAfterAnimationFinished(NotificationPanelViewController.this.mHeadsUpExistenceChangedRunnable);
            }
            NotificationPanelViewController.this.updateGestureExclusionRect();
            boolean unused = NotificationPanelViewController.this.mHeadsUpPinnedMode = z;
            NotificationPanelViewController.this.updateVisibility();
            NotificationPanelViewController.this.mKeyguardStatusBarViewController.updateForHeadsUp();
        }

        public void onHeadsUpPinned(NotificationEntry notificationEntry) {
            if (!NotificationPanelViewController.this.isOnKeyguard()) {
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.generateHeadsUpAnimation(notificationEntry.getHeadsUpAnimationView(), true);
            }
        }

        public void onHeadsUpUnPinned(NotificationEntry notificationEntry) {
            if (NotificationPanelViewController.this.isFullyCollapsed() && notificationEntry.isRowHeadsUp() && !NotificationPanelViewController.this.isOnKeyguard()) {
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.generateHeadsUpAnimation(notificationEntry.getHeadsUpAnimationView(), false);
                notificationEntry.setHeadsUpIsVisible();
            }
        }
    }

    private class HeightListener implements C2301QS.HeightListener {
        private HeightListener() {
        }

        public void onQsHeightChanged() {
            if (NotificationPanelViewController.this.mEx != null) {
                NotificationPanelViewController.this.mEx.setHasQSLayout(true);
            }
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            int i = 0;
            int unused = notificationPanelViewController.mQsMaxExpansionHeight = notificationPanelViewController.mQs != null ? NotificationPanelViewController.this.mQs.getDesiredHeight() : 0;
            if (NotificationPanelViewController.this.mQsExpanded && NotificationPanelViewController.this.mQsFullyExpanded) {
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                float unused2 = notificationPanelViewController2.mQsExpansionHeight = (float) notificationPanelViewController2.mQsMaxExpansionHeight;
                NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                NotificationPanelViewController.this.requestPanelHeightUpdate();
            }
            if (NotificationPanelViewController.this.mAccessibilityManager.isEnabled()) {
                NotificationPanelViewController.this.mView.setAccessibilityPaneTitle(NotificationPanelViewController.this.determineAccessibilityPaneTitle());
            }
            NotificationPanelViewController.this.mNotificationStackScrollLayoutController.setMaxTopPadding(NotificationPanelViewController.this.mQsMaxExpansionHeight);
            NotificationStackScrollLayout view = NotificationPanelViewController.this.mNotificationStackScrollLayoutController.getView();
            if (NotificationPanelViewController.this.mQs.isCustomizing() && !NotificationPanelViewController.this.mSplitShadeEnabled) {
                i = 4;
            }
            view.setVisibility(i);
        }
    }

    private class ConfigurationListener implements ConfigurationController.ConfigurationListener {
        private ConfigurationListener() {
        }

        public void onThemeChanged() {
            if (NotificationPanelViewController.DEBUG_LOGCAT) {
                Log.d(PanelViewController.TAG, "onThemeChanged");
            }
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            int unused = notificationPanelViewController.mThemeResId = notificationPanelViewController.mView.getContext().getThemeResId();
            NotificationPanelViewController.this.reInflateViews();
        }

        public void onSmallestScreenWidthChanged() {
            Trace.beginSection("onSmallestScreenWidthChanged");
            if (NotificationPanelViewController.DEBUG_LOGCAT) {
                Log.d(PanelViewController.TAG, "onSmallestScreenWidthChanged");
            }
            boolean access$9900 = NotificationPanelViewController.this.mKeyguardUserSwitcherEnabled;
            boolean access$10000 = NotificationPanelViewController.this.mKeyguardQsUserSwitchEnabled;
            NotificationPanelViewController.this.updateUserSwitcherFlags();
            if (!(access$9900 == NotificationPanelViewController.this.mKeyguardUserSwitcherEnabled && access$10000 == NotificationPanelViewController.this.mKeyguardQsUserSwitchEnabled)) {
                NotificationPanelViewController.this.reInflateViews();
            }
            Trace.endSection();
        }

        public void onDensityOrFontScaleChanged() {
            if (NotificationPanelViewController.DEBUG_LOGCAT) {
                Log.d(PanelViewController.TAG, "onDensityOrFontScaleChanged");
            }
            NotificationPanelViewController.this.reInflateViews();
        }
    }

    private class SettingsChangeObserver extends ContentObserver {
        SettingsChangeObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z) {
            if (NotificationPanelViewController.DEBUG_LOGCAT) {
                Log.d(PanelViewController.TAG, "onSettingsChanged");
            }
            NotificationPanelViewController.this.reInflateViews();
        }
    }

    private class StatusBarStateListener implements StatusBarStateController.StateListener {
        private StatusBarStateListener() {
        }

        public void onStateChanged(int i) {
            long j;
            long j2;
            boolean goingToFullShade = NotificationPanelViewController.this.mStatusBarStateController.goingToFullShade();
            boolean isKeyguardFadingAway = NotificationPanelViewController.this.mKeyguardStateController.isKeyguardFadingAway();
            int access$6400 = NotificationPanelViewController.this.mBarState;
            boolean z = true;
            boolean z2 = i == 1;
            if (NotificationPanelViewController.this.mDozeParameters.shouldDelayKeyguardShow() && access$6400 == 0 && i == 1) {
                NotificationPanelViewController.this.mKeyguardStatusViewController.updatePosition(NotificationPanelViewController.this.mClockPositionResult.clockX, NotificationPanelViewController.this.mClockPositionResult.clockYFullyDozing, NotificationPanelViewController.this.mClockPositionResult.clockScale, false);
            }
            NotificationPanelViewController.this.mKeyguardStatusViewController.setKeyguardStatusViewVisibility(i, isKeyguardFadingAway, goingToFullShade, NotificationPanelViewController.this.mBarState);
            NotificationPanelViewController.this.setKeyguardBottomAreaVisibility(i, goingToFullShade);
            int unused = NotificationPanelViewController.this.mBarState = i;
            boolean unused2 = NotificationPanelViewController.this.mKeyguardShowing = z2;
            if (access$6400 == 1 && (goingToFullShade || i == 2)) {
                if (NotificationPanelViewController.this.mKeyguardStateController.isKeyguardFadingAway()) {
                    j2 = NotificationPanelViewController.this.mKeyguardStateController.getKeyguardFadingAwayDelay();
                    j = NotificationPanelViewController.this.mKeyguardStateController.getShortenedFadingAwayDuration();
                } else {
                    j2 = 0;
                    j = 360;
                }
                NotificationPanelViewController.this.mKeyguardStatusBarViewController.animateKeyguardStatusBarOut(j2, j);
                if (NotificationPanelViewController.this.mSplitShadeEnabled) {
                    NotificationPanelViewController.this.setQsExpanded(true);
                }
                NotificationPanelViewController.this.updateQSMinHeight();
            } else if (access$6400 == 2 && i == 1) {
                NotificationPanelViewController.this.mKeyguardStatusBarViewController.animateKeyguardStatusBarIn();
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.resetScrollPosition();
                if (!NotificationPanelViewController.this.mQsExpanded && NotificationPanelViewController.this.mSplitShadeEnabled) {
                    NotificationPanelViewController.this.mQs.animateHeaderSlidingOut();
                }
            } else {
                if (!(access$6400 == 0 && i == 1 && NotificationPanelViewController.this.mScreenOffAnimationController.isKeyguardShowDelayed())) {
                    z = false;
                }
                if (!z) {
                    NotificationPanelViewController.this.mKeyguardStatusBarViewController.updateViewState(1.0f, z2 ? 0 : 4);
                }
                if (!(!z2 || access$6400 == NotificationPanelViewController.this.mBarState || NotificationPanelViewController.this.mQs == null)) {
                    NotificationPanelViewController.this.mQs.hideImmediately();
                }
            }
            NotificationPanelViewController.this.mKeyguardStatusBarViewController.updateForHeadsUp();
            if (z2) {
                NotificationPanelViewController.this.updateDozingVisibilities(false);
            }
            NotificationPanelViewController.this.updateMaxDisplayedNotifications(false);
            NotificationPanelViewController.this.maybeAnimateBottomAreaAlpha();
            NotificationPanelViewController.this.updateQsState();
        }

        public void onDozeAmountChanged(float f, float f2) {
            float unused = NotificationPanelViewController.this.mInterpolatedDarkAmount = f2;
            float unused2 = NotificationPanelViewController.this.mLinearDarkAmount = f;
            NotificationPanelViewController.this.mKeyguardStatusViewController.setDarkAmount(NotificationPanelViewController.this.mInterpolatedDarkAmount);
            NotificationPanelViewController.this.mKeyguardBottomArea.setDarkAmount(NotificationPanelViewController.this.mInterpolatedDarkAmount);
            NotificationPanelViewController.this.positionClockAndNotifications();
        }
    }

    public void showAodUi() {
        setDozing(true, false, (PointF) null);
        this.mStatusBarStateController.setUpcomingState(1);
        this.mEntryManager.updateNotifications("showAodUi");
        this.mStatusBarStateListener.onStateChanged(1);
        this.mStatusBarStateListener.onDozeAmountChanged(1.0f, 1.0f);
        setExpandedFraction(1.0f);
    }

    public void setOverStrechAmount(float f) {
        this.mOverStretchAmount = Interpolators.getOvershootInterpolation(f / ((float) this.mView.getHeight())) * ((float) this.mMaxOverscrollAmountForPulse);
        positionClockAndNotifications(true);
    }

    private class OnAttachStateChangeListener implements View.OnAttachStateChangeListener {
        private OnAttachStateChangeListener() {
        }

        public void onViewAttachedToWindow(View view) {
            NotificationPanelViewController.this.mFragmentService.getFragmentHostManager(NotificationPanelViewController.this.mView).addTagListener(C2301QS.TAG, NotificationPanelViewController.this.mFragmentListener);
            NotificationPanelViewController.this.mStatusBarStateController.addCallback(NotificationPanelViewController.this.mStatusBarStateListener);
            NotificationPanelViewController.this.mStatusBarStateListener.onStateChanged(NotificationPanelViewController.this.mStatusBarStateController.getState());
            NotificationPanelViewController.this.mConfigurationController.addCallback(NotificationPanelViewController.this.mConfigurationListener);
            NotificationPanelViewController.this.mConfigurationListener.onThemeChanged();
            NotificationPanelViewController.this.mFalsingManager.addTapListener(NotificationPanelViewController.this.mFalsingTapListener);
            NotificationPanelViewController.this.mKeyguardIndicationController.init();
            NotificationPanelViewController.this.registerSettingsChangeListener();
        }

        public void onViewDetachedFromWindow(View view) {
            NotificationPanelViewController.this.unregisterSettingsChangeListener();
            NotificationPanelViewController.this.mFragmentService.getFragmentHostManager(NotificationPanelViewController.this.mView).removeTagListener(C2301QS.TAG, NotificationPanelViewController.this.mFragmentListener);
            NotificationPanelViewController.this.mStatusBarStateController.removeCallback(NotificationPanelViewController.this.mStatusBarStateListener);
            NotificationPanelViewController.this.mConfigurationController.removeCallback(NotificationPanelViewController.this.mConfigurationListener);
            NotificationPanelViewController.this.mFalsingManager.removeTapListener(NotificationPanelViewController.this.mFalsingTapListener);
        }
    }

    private class OnLayoutChangeListener extends PanelViewController.OnLayoutChangeListener {
        private OnLayoutChangeListener() {
            super();
        }

        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            DejankUtils.startDetectingBlockingIpcs("NVP#onLayout");
            super.onLayoutChange(view, i, i2, i3, i4, i5, i6, i7, i8);
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            boolean z = true;
            notificationPanelViewController.updateMaxDisplayedNotifications(!notificationPanelViewController.shouldAvoidChangingNotificationsCount());
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            if (notificationPanelViewController2.mNotificationStackScrollLayoutController.getWidth() != ((float) NotificationPanelViewController.this.mView.getWidth())) {
                z = false;
            }
            notificationPanelViewController2.setIsFullWidth(z);
            NotificationPanelViewController.this.mKeyguardStatusViewController.setPivotX((float) (NotificationPanelViewController.this.mView.getWidth() / 2));
            NotificationPanelViewController.this.mKeyguardStatusViewController.setPivotY(NotificationPanelViewController.this.mKeyguardStatusViewController.getClockTextSize() * 0.34521484f);
            int access$9500 = NotificationPanelViewController.this.mQsMaxExpansionHeight;
            if (NotificationPanelViewController.this.mQs != null) {
                NotificationPanelViewController.this.updateQSMinHeight();
                NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
                int unused = notificationPanelViewController3.mQsMaxExpansionHeight = notificationPanelViewController3.mQs.getDesiredHeight();
                NotificationPanelViewController.this.mNotificationStackScrollLayoutController.setMaxTopPadding(NotificationPanelViewController.this.mQsMaxExpansionHeight);
            }
            NotificationPanelViewController.this.positionClockAndNotifications();
            if (NotificationPanelViewController.this.mQsExpanded && NotificationPanelViewController.this.mQsFullyExpanded) {
                NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
                float unused2 = notificationPanelViewController4.mQsExpansionHeight = (float) notificationPanelViewController4.mQsMaxExpansionHeight;
                NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                NotificationPanelViewController.this.requestPanelHeightUpdate();
                if (NotificationPanelViewController.this.mQsMaxExpansionHeight != access$9500) {
                    NotificationPanelViewController notificationPanelViewController5 = NotificationPanelViewController.this;
                    notificationPanelViewController5.startQsSizeChangeAnimation(access$9500, notificationPanelViewController5.mQsMaxExpansionHeight);
                }
            } else if (!NotificationPanelViewController.this.mQsExpanded && NotificationPanelViewController.this.mQsExpansionAnimator == null) {
                NotificationPanelViewController notificationPanelViewController6 = NotificationPanelViewController.this;
                notificationPanelViewController6.setQsExpansion(((float) notificationPanelViewController6.mQsMinExpansionHeight) + NotificationPanelViewController.this.mLastOverscroll);
            }
            NotificationPanelViewController notificationPanelViewController7 = NotificationPanelViewController.this;
            notificationPanelViewController7.updateExpandedHeight(notificationPanelViewController7.getExpandedHeight());
            NotificationPanelViewController.this.updateHeader();
            if (NotificationPanelViewController.this.mQsSizeChangeAnimator == null && NotificationPanelViewController.this.mQs != null) {
                NotificationPanelViewController.this.mQs.setHeightOverride(NotificationPanelViewController.this.mQs.getDesiredHeight());
            }
            NotificationPanelViewController.this.updateMaxHeadsUpTranslation();
            NotificationPanelViewController.this.updateGestureExclusionRect();
            if (NotificationPanelViewController.this.mExpandAfterLayoutRunnable != null) {
                NotificationPanelViewController.this.mExpandAfterLayoutRunnable.run();
                Runnable unused3 = NotificationPanelViewController.this.mExpandAfterLayoutRunnable = null;
            }
            DejankUtils.stopDetectingBlockingIpcs("NVP#onLayout");
        }
    }

    public void updateQSMinHeight() {
        float f = (float) this.mQsMinExpansionHeight;
        if (this.mKeyguardShowing || this.mSplitShadeEnabled) {
            this.mQsMinExpansionHeight = 0;
        } else {
            this.mQsMinExpansionHeight = this.mQs.getQsMinExpansionHeight();
        }
        if (this.mQsExpansionHeight == f) {
            this.mQsExpansionHeight = (float) this.mQsMinExpansionHeight;
        }
    }

    private class DebugDrawable extends Drawable {
        private final Paint mDebugPaint = new Paint();
        private final Set<Integer> mDebugTextUsedYPositions = new HashSet();

        public int getOpacity() {
            return 0;
        }

        public void setAlpha(int i) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        private DebugDrawable() {
        }

        public void draw(Canvas canvas) {
            this.mDebugTextUsedYPositions.clear();
            this.mDebugPaint.setColor(-65536);
            this.mDebugPaint.setStrokeWidth(2.0f);
            this.mDebugPaint.setStyle(Paint.Style.STROKE);
            this.mDebugPaint.setTextSize(24.0f);
            if (NotificationPanelViewController.this.mHeaderDebugInfo != null) {
                canvas.drawText(NotificationPanelViewController.this.mHeaderDebugInfo, 50.0f, 100.0f, this.mDebugPaint);
            }
            drawDebugInfo(canvas, NotificationPanelViewController.this.getMaxPanelHeight(), -65536, "getMaxPanelHeight()");
            drawDebugInfo(canvas, (int) NotificationPanelViewController.this.getExpandedHeight(), ScrimController.DEBUG_BEHIND_TINT, "getExpandedHeight()");
            drawDebugInfo(canvas, NotificationPanelViewController.this.calculatePanelHeightQsExpanded(), ScrimController.DEBUG_FRONT_TINT, "calculatePanelHeightQsExpanded()");
            drawDebugInfo(canvas, NotificationPanelViewController.this.calculatePanelHeightShade(), -256, "calculatePanelHeightShade()");
            drawDebugInfo(canvas, (int) NotificationPanelViewController.this.calculateNotificationsTopPadding(), -65281, "calculateNotificationsTopPadding()");
            drawDebugInfo(canvas, NotificationPanelViewController.this.mClockPositionResult.clockY, -7829368, "mClockPositionResult.clockY");
            drawDebugInfo(canvas, (int) NotificationPanelViewController.this.mLockIconViewController.getTop(), -7829368, "mLockIconViewController.getTop()");
            if (NotificationPanelViewController.this.mKeyguardShowing) {
                drawDebugInfo(canvas, NotificationPanelViewController.this.mNotificationStackScrollLayoutController.getTop() + ((int) NotificationPanelViewController.this.mKeyguardNotificationTopPadding), -65536, "NSSL.getTop() + mKeyguardNotificationTopPadding");
                drawDebugInfo(canvas, NotificationPanelViewController.this.mNotificationStackScrollLayoutController.getBottom() - ((int) NotificationPanelViewController.this.mKeyguardNotificationBottomPadding), -65536, "NSSL.getBottom() - mKeyguardNotificationBottomPadding");
            }
            this.mDebugPaint.setColor(-16711681);
            canvas.drawLine(0.0f, (float) NotificationPanelViewController.this.mClockPositionResult.stackScrollerPadding, (float) NotificationPanelViewController.this.mView.getWidth(), (float) NotificationPanelViewController.this.mNotificationStackScrollLayoutController.getTopPadding(), this.mDebugPaint);
        }

        private void drawDebugInfo(Canvas canvas, int i, int i2, String str) {
            this.mDebugPaint.setColor(i2);
            float f = (float) i;
            canvas.drawLine(0.0f, f, (float) NotificationPanelViewController.this.mView.getWidth(), f, this.mDebugPaint);
            canvas.drawText(str, 0.0f, (float) computeDebugYTextPosition(i), this.mDebugPaint);
        }

        private int computeDebugYTextPosition(int i) {
            float f = (float) i;
            if (f - this.mDebugPaint.getTextSize() < 0.0f) {
                i = (int) (f + this.mDebugPaint.getTextSize());
            }
            while (this.mDebugTextUsedYPositions.contains(Integer.valueOf(i))) {
                i = (int) (((float) i) + this.mDebugPaint.getTextSize());
            }
            this.mDebugTextUsedYPositions.add(Integer.valueOf(i));
            return i;
        }
    }

    private class OnConfigurationChangedListener extends PanelViewController.OnConfigurationChangedListener {
        private OnConfigurationChangedListener() {
            super();
        }

        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            NotificationPanelViewController.this.mEx.onConfigurationChanged(configuration);
            NotificationPanelViewController.this.mAffordanceHelper.onConfigurationChanged();
        }
    }

    private class OnApplyWindowInsetsListener implements View.OnApplyWindowInsetsListener {
        private OnApplyWindowInsetsListener() {
        }

        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            int unused = NotificationPanelViewController.this.mDisplayTopInset = insetsIgnoringVisibility.top;
            int unused2 = NotificationPanelViewController.this.mDisplayRightInset = insetsIgnoringVisibility.right;
            int unused3 = NotificationPanelViewController.this.mDisplayLeftInset = windowInsets.getSystemWindowInsetLeft();
            int unused4 = NotificationPanelViewController.this.mNavigationBarBottomHeight = windowInsets.getStableInsetBottom();
            NotificationPanelViewController.this.updateMaxHeadsUpTranslation();
            return windowInsets;
        }
    }

    public void cancelPendingPanelCollapse() {
        this.mView.removeCallbacks(this.mMaybeHideExpandedRunnable);
    }

    /* access modifiers changed from: private */
    public void onPanelStateChanged(int i) {
        updateQSExpansionEnabledAmbient();
        if (i == 2 && this.mCurrentPanelState != i) {
            this.mView.sendAccessibilityEvent(32);
        }
        if (i == 1) {
            if (this.mSplitShadeEnabled && !isOnKeyguard()) {
                this.mQsExpandImmediate = true;
            }
            this.mCentralSurfaces.makeExpandedVisible(false);
        }
        if (i == 0) {
            this.mView.post(this.mMaybeHideExpandedRunnable);
        }
        this.mCurrentPanelState = i;
    }

    public PhoneStatusBarView.TouchEventHandler getStatusBarTouchEventHandler() {
        return this.mStatusBarViewTouchEventHandler;
    }

    /* access modifiers changed from: private */
    public void onStatusBarWindowStateChanged(int i) {
        if (i != 0 && this.mStatusBarStateController.getState() == 0) {
            collapsePanel(false, false, 1.0f);
        }
    }

    @SysUISingleton
    static class PanelEventsEmitter implements NotifPanelEvents {
        private final ListenerSet<NotifPanelEvents.Listener> mListeners = new ListenerSet<>();

        @Inject
        PanelEventsEmitter() {
        }

        public void registerListener(NotifPanelEvents.Listener listener) {
            this.mListeners.addIfAbsent(listener);
        }

        public void unregisterListener(NotifPanelEvents.Listener listener) {
            this.mListeners.remove(listener);
        }

        /* access modifiers changed from: private */
        public void notifyLaunchingActivityChanged(boolean z) {
            Iterator<NotifPanelEvents.Listener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                it.next().onLaunchingActivityChanged(z);
            }
        }

        /* access modifiers changed from: private */
        public void notifyPanelCollapsingChanged(boolean z) {
            Iterator<NotifPanelEvents.Listener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                it.next().onPanelCollapsingChanged(z);
            }
        }
    }

    public C2301QS getQs() {
        return this.mQs;
    }

    public boolean getQsFullyExpanded() {
        return this.mQsFullyExpanded;
    }

    public void setQsFullyExpanded(boolean z) {
        this.mQsFullyExpanded = z;
    }

    public boolean getQsExpanded() {
        return this.mQsExpanded;
    }

    public void setQsExpandImmediate(boolean z) {
        this.mQsExpandImmediate = z;
    }

    public void addPostFullyCollapseAction(Runnable runnable) {
        this.mPostCollapseRunnables.add(runnable);
    }

    private void runPostCollapseRunnablesIfNeeded() {
        int i = this.mBarState;
        if (i == 2 || i == 1) {
            if (!this.mQsVisible) {
                runPostCollapseRunnables();
            }
        } else if (isFullyCollapsed()) {
            runPostCollapseRunnables();
        }
    }

    private void runPostCollapseRunnables() {
        ArrayList arrayList = new ArrayList(this.mPostCollapseRunnables);
        this.mPostCollapseRunnables.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((Runnable) arrayList.get(i)).run();
        }
    }

    public boolean isPanelFullyCollapsed() {
        int i = this.mBarState;
        if (i != 2 && i != 1) {
            return isFullyCollapsed();
        }
        if (!this.mQsVisible) {
            return true;
        }
        return false;
    }

    public boolean isLandscapeScreenOff() {
        return this.mUnlockedScreenOffAnimationController.isLandscapeScreenOff();
    }

    public void setIsLandscapeOff(boolean z) {
        this.mUnlockedScreenOffAnimationController.setIsLandscapeOff(z);
    }

    public boolean isBouncerShowing() {
        return this.mBouncerShowing;
    }
}
