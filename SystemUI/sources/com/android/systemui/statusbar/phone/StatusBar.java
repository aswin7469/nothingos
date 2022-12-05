package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.Fragment;
import android.app.IApplicationThread;
import android.app.IWallpaperManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProfilerInfo;
import android.app.StatusBarManager;
import android.app.TaskInfo;
import android.app.UiModeManager;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.media.AudioAttributes;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.MathUtils;
import android.util.Slog;
import android.view.Display;
import android.view.IRemoteAnimationRunner;
import android.view.IWindowManager;
import android.view.InsetsState;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.RemoteAnimationAdapter;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import android.widget.DateTimeView;
import android.widget.ImageView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.RegisterStatusBarResult;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.view.AppearanceRegion;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.AutoReinflateContainer;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.EventLogTags;
import com.android.systemui.InitController;
import com.android.systemui.Prefs;
import com.android.systemui.R$array;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.SystemUI;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DelegateLaunchAnimatorController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.camera.CameraIntents;
import com.android.systemui.charging.WirelessChargingAnimation;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.fragments.ExtensionFragmentListener;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.OverlayPlugin;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.qs.QSPanelController;
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.settings.brightness.BrightnessSlider;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.BackDropView;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.GestureRecorder;
import com.android.systemui.statusbar.KeyboardShortcuts;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LiftReveal;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.PowerButtonReveal;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.SuperStatusBarViewFactory;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.interruption.BypassHeadsUpNotifier;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.phone.dagger.StatusBarComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.volume.VolumeComponent;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.startingsurface.SplashscreenContentDrawer;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.nothingos.headsup.NothingOSHeadsupManager;
import com.nothingos.systemui.doze.AODController;
import com.nothingos.systemui.doze.LiftWakeGestureController;
import com.nothingos.systemui.facerecognition.FaceRecognitionController;
import com.nothingos.systemui.facerecognition.IFaceRecognitionAnimationCallback;
import com.nothingos.utils.SystemUIDebugConfig;
import com.nothingos.utils.SystemUIUtils;
import dagger.Lazy;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.inject.Provider;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public class StatusBar extends SystemUI implements DemoMode, ActivityStarter, KeyguardStateController.Callback, OnHeadsUpChangedListener, CommandQueue.Callbacks, ColorExtractor.OnColorsChangedListener, ConfigurationController.ConfigurationListener, StatusBarStateController.StateListener, LifecycleOwner, BatteryController.BatteryStateChangeCallback, ActivityLaunchAnimator.Callback {
    public static final boolean ONLY_CORE_APPS;
    protected AccessibilityManager mAccessibilityManager;
    private ActivityLaunchAnimator mActivityLaunchAnimator;
    private View mAmbientIndicationContainer;
    private final SystemStatusAnimationScheduler mAnimationScheduler;
    private int mAppearance;
    private final Lazy<AssistManager> mAssistManagerLazy;
    private AuthRippleController mAuthRippleController;
    private final AutoHideController mAutoHideController;
    protected IStatusBarService mBarService;
    protected final BatteryController mBatteryController;
    private BiometricUnlockController mBiometricUnlockController;
    private final Lazy<BiometricUnlockController> mBiometricUnlockControllerLazy;
    protected boolean mBouncerShowing;
    private boolean mBouncerWasShowingWhenHidden;
    private BrightnessMirrorController mBrightnessMirrorController;
    private boolean mBrightnessMirrorVisible;
    private final BrightnessSlider.Factory mBrightnessSliderFactory;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    private final Optional<Bubbles> mBubblesOptional;
    private final BypassHeadsUpNotifier mBypassHeadsUpNotifier;
    private VibrationEffect mCameraLaunchGestureVibrationEffect;
    private WiredChargingRippleController mChargingRippleAnimationController;
    private CircleReveal mCircleReveal;
    private final SysuiColorExtractor mColorExtractor;
    protected final CommandQueue mCommandQueue;
    private final ConfigurationController mConfigurationController;
    private final DemoModeController mDemoModeController;
    protected boolean mDeviceInteractive;
    protected DevicePolicyManager mDevicePolicyManager;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private final DismissCallbackRegistry mDismissCallbackRegistry;
    protected Display mDisplay;
    private int mDisplayId;
    private final DisplayMetrics mDisplayMetrics;
    private final DozeParameters mDozeParameters;
    protected DozeScrimController mDozeScrimController;
    @VisibleForTesting
    DozeServiceHost mDozeServiceHost;
    protected boolean mDozing;
    private IDreamManager mDreamManager;
    private final DynamicPrivacyController mDynamicPrivacyController;
    private boolean mExpandedVisible;
    private final ExtensionController mExtensionController;
    private final FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    private final FeatureFlags mFeatureFlags;
    private final GestureRecorder mGestureRec;
    protected PowerManager.WakeLock mGestureWakeLock;
    private final NotificationGutsManager mGutsManager;
    private HeadsUpAppearanceController mHeadsUpAppearanceController;
    private final HeadsUpManagerPhone mHeadsUpManager;
    private boolean mHideIconsForBouncer;
    private PhoneStatusBarPolicy mIconPolicy;
    private final InitController mInitController;
    private int mInteractingWindows;
    protected boolean mIsKeyguard;
    private boolean mIsOccluded;
    private final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardDismissUtil mKeyguardDismissUtil;
    KeyguardIndicationController mKeyguardIndicationController;
    private final KeyguardLiftController mKeyguardLiftController;
    protected KeyguardManager mKeyguardManager;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardViewMediator mKeyguardViewMediator;
    private ViewMediatorCallback mKeyguardViewMediatorCallback;
    private int mLastCameraLaunchSource;
    private int mLastLoggedStateFingerprint;
    private boolean mLaunchCameraOnFinishedGoingToSleep;
    private boolean mLaunchCameraWhenFinishedWaking;
    private boolean mLaunchEmergencyActionOnFinishedGoingToSleep;
    private boolean mLaunchEmergencyActionWhenFinishedWaking;
    private Runnable mLaunchTransitionEndRunnable;
    public LiftWakeGestureController mLiftWakeGestureController;
    private final LightBarController mLightBarController;
    private LightRevealScrim mLightRevealScrim;
    private final LightsOutNotifController mLightsOutNotifController;
    private LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    private final NotificationLockscreenUserManager mLockscreenUserManager;
    protected LockscreenWallpaper mLockscreenWallpaper;
    private final Lazy<LockscreenWallpaper> mLockscreenWallpaperLazy;
    private final NotificationMediaManager mMediaManager;
    private final MetricsLogger mMetricsLogger;
    private final NavigationBarController mNavigationBarController;
    private final NetworkController mNetworkController;
    private boolean mNoAnimationOnNextBarModeChange;
    private NotificationActivityStarter mNotificationActivityStarter;
    private NotificationLaunchAnimatorControllerProvider mNotificationAnimationProvider;
    protected final NotificationIconAreaController mNotificationIconAreaController;
    protected final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    private final NotificationLogger mNotificationLogger;
    protected NotificationPanelViewController mNotificationPanelViewController;
    private Lazy<NotificationShadeDepthController> mNotificationShadeDepthControllerLazy;
    protected NotificationShadeWindowController mNotificationShadeWindowController;
    protected NotificationShadeWindowView mNotificationShadeWindowView;
    protected NotificationShadeWindowViewController mNotificationShadeWindowViewController;
    protected NotificationShelfController mNotificationShelfController;
    private NotificationsController mNotificationsController;
    private final OngoingCallController mOngoingCallController;
    protected boolean mPanelExpanded;
    protected StatusBarWindowView mPhoneStatusBarWindow;
    private final PluginDependencyProvider mPluginDependencyProvider;
    private final PluginManager mPluginManager;
    private PowerButtonReveal mPowerButtonReveal;
    private final PowerManager mPowerManager;
    protected StatusBarNotificationPresenter mPresenter;
    private final PulseExpansionHandler mPulseExpansionHandler;
    private QSPanelController mQSPanelController;
    private final NotificationRemoteInputManager mRemoteInputManager;
    private final RemoteInputQuickSettingsDisabler mRemoteInputQuickSettingsDisabler;
    private View mReportRejectedTouch;
    private final ScreenLifecycle mScreenLifecycle;
    private final ScreenPinningRequest mScreenPinningRequest;
    private final ScrimController mScrimController;
    private final ShadeController mShadeController;
    private StatusBarSignalPolicy mSignalPolicy;
    private final Optional<LegacySplitScreen> mSplitScreenOptional;
    protected NotificationStackScrollLayout mStackScroller;
    private NotificationStackScrollLayoutController mStackScrollerController;
    private final Optional<StartingSurface> mStartingSurfaceOptional;
    protected int mState;
    private final Provider<StatusBarComponent.Builder> mStatusBarComponentBuilder;
    private final StatusBarIconController mStatusBarIconController;
    protected StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final StatusBarLocationPublisher mStatusBarLocationPublisher;
    private int mStatusBarMode;
    private final StatusBarNotificationActivityStarter.Builder mStatusBarNotificationActivityStarterBuilder;
    private final SysuiStatusBarStateController mStatusBarStateController;
    private LogMaker mStatusBarStateLog;
    private final StatusBarTouchableRegionManager mStatusBarTouchableRegionManager;
    protected PhoneStatusBarView mStatusBarView;
    protected StatusBarWindowController mStatusBarWindowController;
    private boolean mStatusBarWindowHidden;
    private final SuperStatusBarViewFactory mSuperStatusBarViewFactory;
    private boolean mTopHidesStatusBar;
    private boolean mTransientShown;
    private final Executor mUiBgExecutor;
    private UiModeManager mUiModeManager;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private final UserInfoControllerImpl mUserInfoControllerImpl;
    private final UserSwitcherController mUserSwitcherController;
    private boolean mVibrateOnOpening;
    private Vibrator mVibrator;
    private final VibratorHelper mVibratorHelper;
    private final NotificationViewHierarchyManager mViewHierarchyManager;
    protected boolean mVisible;
    private boolean mVisibleToUser;
    private final VisualStabilityManager mVisualStabilityManager;
    private final VolumeComponent mVolumeComponent;
    private boolean mWakeUpComingFromTouch;
    private final NotificationWakeUpCoordinator mWakeUpCoordinator;
    private PointF mWakeUpTouchLocation;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private boolean mWallpaperSupported;
    private boolean mWereIconsJustHidden;
    protected WindowManager mWindowManager;
    protected IWindowManager mWindowManagerService;
    private static final AudioAttributes VIBRATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    public static final long[] CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS = {20, 20, 20, 20, 100, 20};
    public static final int[] CAMERA_LAUNCH_GESTURE_VIBRATION_AMPLITUDES = {39, 82, 139, 213, 0, 127};
    private static final UiEventLogger sUiEventLogger = new UiEventLoggerImpl();
    private final Point mCurrentDisplaySize = new Point();
    private int mStatusBarWindowState = 0;
    private final Object mQueueLock = new Object();
    private final int[] mAbsPos = new int[2];
    private int mDisabled1 = 0;
    private int mDisabled2 = 0;
    @VisibleForTesting
    protected boolean mUserSetup = false;
    private final DeviceProvisionedController.DeviceProvisionedListener mUserSetupObserver = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.statusbar.phone.StatusBar.1
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public void onUserSetupChanged() {
            boolean isUserSetup = StatusBar.this.mDeviceProvisionedController.isUserSetup(StatusBar.this.mDeviceProvisionedController.getCurrentUser());
            Log.d("StatusBar", "mUserSetupObserver - DeviceProvisionedListener called for user " + StatusBar.this.mDeviceProvisionedController.getCurrentUser());
            StatusBar statusBar = StatusBar.this;
            if (isUserSetup != statusBar.mUserSetup) {
                statusBar.mUserSetup = isUserSetup;
                if (!isUserSetup && statusBar.mStatusBarView != null) {
                    statusBar.animateCollapseQuickSettings();
                }
                StatusBar statusBar2 = StatusBar.this;
                NotificationPanelViewController notificationPanelViewController = statusBar2.mNotificationPanelViewController;
                if (notificationPanelViewController != null) {
                    notificationPanelViewController.setUserSetupComplete(statusBar2.mUserSetup);
                }
                StatusBar.this.updateQsExpansionEnabled();
            }
        }
    };
    protected final H mHandler = createHandler();
    private final BroadcastReceiver mWallpaperChangedReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.StatusBar.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!StatusBar.this.mWallpaperSupported) {
                Log.wtf("StatusBar", "WallpaperManager not supported");
                return;
            }
            WallpaperInfo wallpaperInfo = ((WallpaperManager) context.getSystemService(WallpaperManager.class)).getWallpaperInfo(-2);
            boolean z = ((SystemUI) StatusBar.this).mContext.getResources().getBoolean(17891515) && wallpaperInfo != null && wallpaperInfo.supportsAmbientMode();
            StatusBar.this.mNotificationShadeWindowController.setWallpaperSupportsAmbientMode(z);
            StatusBar.this.mScrimController.setWallpaperSupportsAmbientMode(z);
            StatusBar.this.mKeyguardViewMediator.setWallpaperSupportsAmbientMode(z);
        }
    };
    BroadcastReceiver mTaskbarChangeReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.StatusBar.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (StatusBar.this.mBubblesOptional.isPresent()) {
                ((Bubbles) StatusBar.this.mBubblesOptional.get()).onTaskbarChanged(intent.getExtras());
            }
        }
    };
    private final int[] mTmpInt2 = new int[2];
    private final ScrimController.Callback mUnlockScrimCallback = new ScrimController.Callback() { // from class: com.android.systemui.statusbar.phone.StatusBar.4
        @Override // com.android.systemui.statusbar.phone.ScrimController.Callback
        public void onFinished() {
            StatusBar statusBar = StatusBar.this;
            if (statusBar.mStatusBarKeyguardViewManager != null) {
                if (!statusBar.mKeyguardStateController.isKeyguardFadingAway()) {
                    return;
                }
                StatusBar.this.mStatusBarKeyguardViewManager.onKeyguardFadedAway();
                return;
            }
            Log.w("StatusBar", "Tried to notify keyguard visibility when mStatusBarKeyguardViewManager was null");
        }

        @Override // com.android.systemui.statusbar.phone.ScrimController.Callback
        public void onCancelled() {
            onFinished();
        }
    };
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    private final KeyguardUpdateMonitorCallback mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.StatusBar.5
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onDreamingStateChanged(boolean z) {
            if (z) {
                StatusBar.this.maybeEscalateHeadsUp();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onStrongAuthStateChanged(int i) {
            super.onStrongAuthStateChanged(i);
            StatusBar.this.mNotificationsController.requestNotificationUpdate("onStrongAuthStateChanged");
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            if (biometricSourceType != BiometricSourceType.FINGERPRINT || !z) {
                return;
            }
            StatusBar statusBar = StatusBar.this;
            if (!statusBar.mDozing) {
                return;
            }
            statusBar.onDozeFingerprintRunningStateChanged();
        }
    };
    private final FalsingManager.FalsingBeliefListener mFalsingBeliefListener = new FalsingManager.FalsingBeliefListener() { // from class: com.android.systemui.statusbar.phone.StatusBar.6
        @Override // com.android.systemui.plugins.FalsingManager.FalsingBeliefListener
        public void onFalse() {
        }
    };
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final Runnable mCheckBarModes = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda12
        @Override // java.lang.Runnable
        public final void run() {
            StatusBar.this.checkBarModes();
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new AnonymousClass13();
    private final BroadcastReceiver mDemoReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.StatusBar.14
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            "fake_artwork".equals(intent.getAction());
        }
    };
    final Runnable mStartTracing = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar.15
        @Override // java.lang.Runnable
        public void run() {
            StatusBar.this.vibrate();
            SystemClock.sleep(250L);
            Log.d("StatusBar", "startTracing");
            Debug.startMethodTracing("/data/statusbar-traces/trace");
            StatusBar statusBar = StatusBar.this;
            statusBar.mHandler.postDelayed(statusBar.mStopTracing, 10000L);
        }
    };
    final Runnable mStopTracing = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda16
        @Override // java.lang.Runnable
        public final void run() {
            StatusBar.this.lambda$new$24();
        }
    };
    @VisibleForTesting
    final WakefulnessLifecycle.Observer mWakefulnessObserver = new AnonymousClass16();
    final ScreenLifecycle.Observer mScreenObserver = new ScreenLifecycle.Observer() { // from class: com.android.systemui.statusbar.phone.StatusBar.17
        @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
        public void onScreenTurningOn() {
            StatusBar.this.mFalsingCollector.onScreenTurningOn();
            StatusBar.this.mNotificationPanelViewController.onScreenTurningOn();
        }

        @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
        public void onScreenTurnedOn() {
            StatusBar.this.mScrimController.onScreenTurnedOn();
        }

        @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
        public void onScreenTurnedOff() {
            StatusBar.this.mDozeServiceHost.updateDozing();
            StatusBar.this.mFalsingCollector.onScreenOff();
            StatusBar.this.mScrimController.onScreenTurnedOff();
            StatusBar.this.updateIsKeyguard();
        }
    };
    private final BroadcastReceiver mBannerActionBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.StatusBar.19
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.android.systemui.statusbar.banner_action_cancel".equals(action) || "com.android.systemui.statusbar.banner_action_setup".equals(action)) {
                ((NotificationManager) ((SystemUI) StatusBar.this).mContext.getSystemService("notification")).cancel(5);
                Settings.Secure.putInt(((SystemUI) StatusBar.this).mContext.getContentResolver(), "show_note_about_notification_hiding", 0);
                if (!"com.android.systemui.statusbar.banner_action_setup".equals(action)) {
                    return;
                }
                StatusBar.this.mShadeController.animateCollapsePanels(2, true);
                ((SystemUI) StatusBar.this).mContext.startActivity(new Intent("android.settings.ACTION_APP_NOTIFICATION_REDACTION").addFlags(268435456));
            }
        }
    };
    private IFaceRecognitionAnimationCallback mFaceAnimationCallback = new IFaceRecognitionAnimationCallback() { // from class: com.android.systemui.statusbar.phone.StatusBar.22
        @Override // com.nothingos.systemui.facerecognition.IFaceRecognitionAnimationCallback
        public void onFaceSuccessConnect() {
        }

        @Override // com.nothingos.systemui.facerecognition.IFaceRecognitionAnimationCallback
        public void resetFaceImage() {
        }

        @Override // com.nothingos.systemui.facerecognition.IFaceRecognitionAnimationCallback
        public void startLoadingAnimation() {
        }

        @Override // com.nothingos.systemui.facerecognition.IFaceRecognitionAnimationCallback
        public void startFailureAnimation() {
            StatusBar.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar.22.1
                @Override // java.lang.Runnable
                public void run() {
                    StatusBar.this.mKeyguardIndicationController.showTransientIndication(R$string.nt_face_recognition_fail);
                    StatusBar.this.mKeyguardIndicationController.hideTransientIndication();
                }
            });
        }

        @Override // com.nothingos.systemui.facerecognition.IFaceRecognitionAnimationCallback
        public void startFreezeAnimation() {
            StatusBar.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar.22.2
                @Override // java.lang.Runnable
                public void run() {
                    StatusBar.this.mKeyguardIndicationController.showTransientIndication(R$string.nt_face_recognition_error);
                    StatusBar.this.mKeyguardIndicationController.hideTransientIndication();
                }
            });
        }

        @Override // com.nothingos.systemui.facerecognition.IFaceRecognitionAnimationCallback
        public void startSuccessAnimation() {
            StatusBar.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar.22.3
                @Override // java.lang.Runnable
                public void run() {
                    StatusBar.this.mKeyguardIndicationController.hideTransientIndication();
                }
            });
        }

        @Override // com.nothingos.systemui.facerecognition.IFaceRecognitionAnimationCallback
        public void onFaceAuthenticationTimeout() {
            StatusBar.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar.22.4
                @Override // java.lang.Runnable
                public void run() {
                    StatusBar.this.mKeyguardIndicationController.showTransientIndication(R$string.nt_face_recognition_timeout);
                    StatusBar.this.mKeyguardIndicationController.hideTransientIndication();
                }
            });
        }
    };
    private LiftWakeGestureController.LiftAndMotionCallback mLiftAndMotionCallback = new LiftWakeGestureController.LiftAndMotionCallback() { // from class: com.android.systemui.statusbar.phone.StatusBar.23
        @Override // com.nothingos.systemui.doze.LiftWakeGestureController.LiftAndMotionCallback
        public void onLiftUp() {
            synchronized (this) {
                if (((AODController) Dependency.get(AODController.class)).isLiftWakeEnable()) {
                    StatusBar.this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:NODOZE");
                } else {
                    DozeServiceHost dozeServiceHost = StatusBar.this.mDozeServiceHost;
                    if (dozeServiceHost != null) {
                        dozeServiceHost.fireLiftWake();
                    }
                }
            }
        }

        @Override // com.nothingos.systemui.doze.LiftWakeGestureController.LiftAndMotionCallback
        public void onMotion() {
            DozeServiceHost dozeServiceHost = StatusBar.this.mDozeServiceHost;
            if (dozeServiceHost != null) {
                dozeServiceHost.fireMotion();
            }
        }
    };
    private boolean mIsTapWake = false;
    private final List<ExpansionChangedListener> mExpansionChangedListeners = new ArrayList();
    private final Bubbles.BubbleExpandListener mBubbleExpandListener = new Bubbles.BubbleExpandListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda8
        @Override // com.android.wm.shell.bubbles.Bubbles.BubbleExpandListener
        public final void onBubbleExpandChanged(boolean z, String str) {
            StatusBar.this.lambda$new$1(z, str);
        }
    };
    private ActivityIntentHelper mActivityIntentHelper = new ActivityIntentHelper(this.mContext);

    /* loaded from: classes.dex */
    public interface ExpansionChangedListener {
        void onExpansionChanged(float f, boolean z);
    }

    private static int barMode(boolean z, int i) {
        if (z) {
            return 1;
        }
        if ((i & 5) == 5) {
            return 3;
        }
        if ((i & 4) != 0) {
            return 6;
        }
        if ((i & 1) != 0) {
            return 4;
        }
        return (i & 32) != 0 ? 1 : 0;
    }

    private static int getLoggingFingerprint(int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        return (i & 255) | ((z ? 1 : 0) << 8) | ((z2 ? 1 : 0) << 9) | ((z3 ? 1 : 0) << 10) | ((z4 ? 1 : 0) << 11) | ((z5 ? 1 : 0) << 12);
    }

    public boolean isFalsingThresholdNeeded() {
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
    }

    static {
        boolean z;
        try {
            z = IPackageManager.Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
        } catch (RemoteException unused) {
            z = false;
        }
        ONLY_CORE_APPS = z;
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum StatusBarUiEvent implements UiEventLogger.UiEventEnum {
        LOCKSCREEN_OPEN_SECURE(405),
        LOCKSCREEN_OPEN_INSECURE(406),
        LOCKSCREEN_CLOSE_SECURE(407),
        LOCKSCREEN_CLOSE_INSECURE(408),
        BOUNCER_OPEN_SECURE(409),
        BOUNCER_OPEN_INSECURE(410),
        BOUNCER_CLOSE_SECURE(411),
        BOUNCER_CLOSE_INSECURE(412);
        
        private final int mId;

        StatusBarUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public StatusBar(Context context, NotificationsController notificationsController, LightBarController lightBarController, AutoHideController autoHideController, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarSignalPolicy statusBarSignalPolicy, PulseExpansionHandler pulseExpansionHandler, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, KeyguardStateController keyguardStateController, HeadsUpManagerPhone headsUpManagerPhone, DynamicPrivacyController dynamicPrivacyController, BypassHeadsUpNotifier bypassHeadsUpNotifier, FalsingManager falsingManager, FalsingCollector falsingCollector, BroadcastDispatcher broadcastDispatcher, RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, NotificationGutsManager notificationGutsManager, NotificationLogger notificationLogger, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationViewHierarchyManager notificationViewHierarchyManager, KeyguardViewMediator keyguardViewMediator, DisplayMetrics displayMetrics, MetricsLogger metricsLogger, Executor executor, NotificationMediaManager notificationMediaManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationRemoteInputManager notificationRemoteInputManager, UserSwitcherController userSwitcherController, NetworkController networkController, BatteryController batteryController, SysuiColorExtractor sysuiColorExtractor, ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle, SysuiStatusBarStateController sysuiStatusBarStateController, VibratorHelper vibratorHelper, Optional<BubblesManager> optional, Optional<Bubbles> optional2, VisualStabilityManager visualStabilityManager, DeviceProvisionedController deviceProvisionedController, NavigationBarController navigationBarController, AccessibilityFloatingMenuController accessibilityFloatingMenuController, Lazy<AssistManager> lazy, ConfigurationController configurationController, NotificationShadeWindowController notificationShadeWindowController, DozeParameters dozeParameters, ScrimController scrimController, KeyguardLiftController keyguardLiftController, Lazy<LockscreenWallpaper> lazy2, Lazy<BiometricUnlockController> lazy3, DozeServiceHost dozeServiceHost, PowerManager powerManager, ScreenPinningRequest screenPinningRequest, DozeScrimController dozeScrimController, VolumeComponent volumeComponent, CommandQueue commandQueue, Provider<StatusBarComponent.Builder> provider, PluginManager pluginManager, Optional<LegacySplitScreen> optional3, LightsOutNotifController lightsOutNotifController, StatusBarNotificationActivityStarter.Builder builder, ShadeController shadeController, SuperStatusBarViewFactory superStatusBarViewFactory, StatusBarKeyguardViewManager statusBarKeyguardViewManager, ViewMediatorCallback viewMediatorCallback, InitController initController, Handler handler, PluginDependencyProvider pluginDependencyProvider, KeyguardDismissUtil keyguardDismissUtil, ExtensionController extensionController, UserInfoControllerImpl userInfoControllerImpl, PhoneStatusBarPolicy phoneStatusBarPolicy, KeyguardIndicationController keyguardIndicationController, DismissCallbackRegistry dismissCallbackRegistry, DemoModeController demoModeController, Lazy<NotificationShadeDepthController> lazy4, StatusBarTouchableRegionManager statusBarTouchableRegionManager, NotificationIconAreaController notificationIconAreaController, BrightnessSlider.Factory factory, WiredChargingRippleController wiredChargingRippleController, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, StatusBarIconController statusBarIconController, LockscreenShadeTransitionController lockscreenShadeTransitionController, FeatureFlags featureFlags, KeyguardUnlockAnimationController keyguardUnlockAnimationController, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Optional<StartingSurface> optional4) {
        super(context);
        this.mNotificationsController = notificationsController;
        this.mLightBarController = lightBarController;
        this.mAutoHideController = autoHideController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mSignalPolicy = statusBarSignalPolicy;
        this.mPulseExpansionHandler = pulseExpansionHandler;
        this.mWakeUpCoordinator = notificationWakeUpCoordinator;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mKeyguardStateController = keyguardStateController;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mKeyguardIndicationController = keyguardIndicationController;
        this.mStatusBarTouchableRegionManager = statusBarTouchableRegionManager;
        this.mDynamicPrivacyController = dynamicPrivacyController;
        this.mBypassHeadsUpNotifier = bypassHeadsUpNotifier;
        this.mFalsingCollector = falsingCollector;
        this.mFalsingManager = falsingManager;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mRemoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler;
        this.mGutsManager = notificationGutsManager;
        this.mNotificationLogger = notificationLogger;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mViewHierarchyManager = notificationViewHierarchyManager;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mDisplayMetrics = displayMetrics;
        this.mMetricsLogger = metricsLogger;
        this.mUiBgExecutor = executor;
        this.mMediaManager = notificationMediaManager;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mNetworkController = networkController;
        this.mBatteryController = batteryController;
        this.mColorExtractor = sysuiColorExtractor;
        this.mScreenLifecycle = screenLifecycle;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mVibratorHelper = vibratorHelper;
        this.mBubblesManagerOptional = optional;
        this.mBubblesOptional = optional2;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mNavigationBarController = navigationBarController;
        this.mAssistManagerLazy = lazy;
        this.mConfigurationController = configurationController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mDozeServiceHost = dozeServiceHost;
        this.mPowerManager = powerManager;
        this.mDozeParameters = dozeParameters;
        this.mScrimController = scrimController;
        this.mKeyguardLiftController = keyguardLiftController;
        this.mLockscreenWallpaperLazy = lazy2;
        this.mScreenPinningRequest = screenPinningRequest;
        this.mDozeScrimController = dozeScrimController;
        this.mBiometricUnlockControllerLazy = lazy3;
        this.mNotificationShadeDepthControllerLazy = lazy4;
        this.mVolumeComponent = volumeComponent;
        this.mCommandQueue = commandQueue;
        this.mStatusBarComponentBuilder = provider;
        this.mPluginManager = pluginManager;
        this.mSplitScreenOptional = optional3;
        this.mStatusBarNotificationActivityStarterBuilder = builder;
        this.mShadeController = shadeController;
        this.mSuperStatusBarViewFactory = superStatusBarViewFactory;
        this.mLightsOutNotifController = lightsOutNotifController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardViewMediatorCallback = viewMediatorCallback;
        this.mInitController = initController;
        this.mPluginDependencyProvider = pluginDependencyProvider;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mExtensionController = extensionController;
        this.mUserInfoControllerImpl = userInfoControllerImpl;
        this.mIconPolicy = phoneStatusBarPolicy;
        phoneStatusBarPolicy.setupContext(context);
        this.mDismissCallbackRegistry = dismissCallbackRegistry;
        this.mDemoModeController = demoModeController;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mBrightnessSliderFactory = factory;
        this.mChargingRippleAnimationController = wiredChargingRippleController;
        this.mOngoingCallController = ongoingCallController;
        this.mAnimationScheduler = systemStatusAnimationScheduler;
        this.mStatusBarLocationPublisher = statusBarLocationPublisher;
        this.mStatusBarIconController = statusBarIconController;
        this.mFeatureFlags = featureFlags;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mStartingSurfaceOptional = optional4;
        lockscreenShadeTransitionController.setStatusbar(this);
        DateTimeView.setReceiverHandler(handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(boolean z, String str) {
        this.mContext.getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mNotificationsController.requestNotificationUpdate("onBubbleExpandChanged");
        updateScrimController();
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        RegisterStatusBarResult registerStatusBarResult;
        this.mScreenLifecycle.addObserver(this.mScreenObserver);
        this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
        this.mUiModeManager = (UiModeManager) this.mContext.getSystemService(UiModeManager.class);
        this.mBypassHeadsUpNotifier.setUp();
        if (this.mBubblesOptional.isPresent()) {
            this.mBubblesOptional.get().setExpandListener(this.mBubbleExpandListener);
            this.mBroadcastDispatcher.registerReceiver(this.mTaskbarChangeReceiver, new IntentFilter("taskbarChanged"));
        }
        this.mKeyguardIndicationController.init();
        this.mColorExtractor.addOnColorsChangedListener(this);
        this.mStatusBarStateController.addCallback(this, 0);
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
        Display defaultDisplay = this.mWindowManager.getDefaultDisplay();
        this.mDisplay = defaultDisplay;
        this.mDisplayId = defaultDisplay.getDisplayId();
        updateDisplaySize();
        this.mVibrateOnOpening = this.mContext.getResources().getBoolean(R$bool.config_vibrateOnIconAnimation);
        this.mWindowManagerService = WindowManagerGlobal.getWindowManagerService();
        this.mDevicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        this.mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
        this.mKeyguardUpdateMonitor.setKeyguardBypassController(this.mKeyguardBypassController);
        this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mKeyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
        this.mWallpaperSupported = ((WallpaperManager) this.mContext.getSystemService(WallpaperManager.class)).isWallpaperSupported();
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mDemoModeController.addCallback((DemoMode) this);
        try {
            registerStatusBarResult = this.mBarService.registerStatusBar(this.mCommandQueue);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            registerStatusBarResult = null;
        }
        createAndAddWindows(registerStatusBarResult);
        if (this.mWallpaperSupported) {
            this.mBroadcastDispatcher.registerReceiver(this.mWallpaperChangedReceiver, new IntentFilter("android.intent.action.WALLPAPER_CHANGED"), null, UserHandle.ALL);
            this.mWallpaperChangedReceiver.onReceive(this.mContext, null);
        }
        setUpPresenter();
        if (InsetsState.containsType(registerStatusBarResult.mTransientBarTypes, 0)) {
            showTransientUnchecked();
        }
        onSystemBarAttributesChanged(this.mDisplayId, registerStatusBarResult.mAppearance, registerStatusBarResult.mAppearanceRegions, registerStatusBarResult.mNavbarColorManagedByIme, registerStatusBarResult.mBehavior, registerStatusBarResult.mAppFullscreen);
        setImeWindowStatus(this.mDisplayId, registerStatusBarResult.mImeToken, registerStatusBarResult.mImeWindowVis, registerStatusBarResult.mImeBackDisposition, registerStatusBarResult.mShowImeSwitcher);
        int size = registerStatusBarResult.mIcons.size();
        for (int i = 0; i < size; i++) {
            this.mCommandQueue.setIcon((String) registerStatusBarResult.mIcons.keyAt(i), (StatusBarIcon) registerStatusBarResult.mIcons.valueAt(i));
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.systemui.statusbar.banner_action_cancel");
        intentFilter.addAction("com.android.systemui.statusbar.banner_action_setup");
        this.mContext.registerReceiver(this.mBannerActionBroadcastReceiver, intentFilter, "com.android.systemui.permission.SELF", null);
        if (this.mWallpaperSupported) {
            try {
                IWallpaperManager.Stub.asInterface(ServiceManager.getService("wallpaper")).setInAmbientMode(false, 0L);
            } catch (RemoteException unused) {
            }
        }
        this.mIconPolicy.init();
        this.mKeyguardStateController.addCallback(this);
        startKeyguard();
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateCallback);
        this.mDozeServiceHost.initialize(this, this.mStatusBarKeyguardViewManager, this.mNotificationShadeWindowViewController, this.mNotificationPanelViewController, this.mAmbientIndicationContainer);
        updateLightRevealScrimVisibility();
        this.mConfigurationController.addCallback(this);
        this.mBatteryController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
        this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
        final int i2 = registerStatusBarResult.mDisabledFlags1;
        final int i3 = registerStatusBarResult.mDisabledFlags2;
        this.mInitController.addPostInitTask(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$start$2(i2, i3);
            }
        });
        this.mFalsingManager.addFalsingBeliefListener(this.mFalsingBeliefListener);
        this.mPluginManager.addPluginListener((PluginListener) new AnonymousClass7(), OverlayPlugin.class, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$7  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass7 implements PluginListener<OverlayPlugin> {
        private ArraySet<OverlayPlugin> mOverlays = new ArraySet<>();

        AnonymousClass7() {
        }

        @Override // com.android.systemui.plugins.PluginListener
        public void onPluginConnected(final OverlayPlugin overlayPlugin, Context context) {
            StatusBar.this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBar.AnonymousClass7.this.lambda$onPluginConnected$0(overlayPlugin);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPluginConnected$0(OverlayPlugin overlayPlugin) {
            overlayPlugin.setup(StatusBar.this.getNotificationShadeWindowView(), StatusBar.this.getNavigationBarView(), new Callback(overlayPlugin), StatusBar.this.mDozeParameters);
        }

        @Override // com.android.systemui.plugins.PluginListener
        public void onPluginDisconnected(final OverlayPlugin overlayPlugin) {
            StatusBar.this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBar.AnonymousClass7.this.lambda$onPluginDisconnected$1(overlayPlugin);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPluginDisconnected$1(OverlayPlugin overlayPlugin) {
            this.mOverlays.remove(overlayPlugin);
            StatusBar.this.mNotificationShadeWindowController.setForcePluginOpen(this.mOverlays.size() != 0, this);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.android.systemui.statusbar.phone.StatusBar$7$Callback */
        /* loaded from: classes.dex */
        public class Callback implements OverlayPlugin.Callback {
            private final OverlayPlugin mPlugin;

            Callback(OverlayPlugin overlayPlugin) {
                this.mPlugin = overlayPlugin;
            }

            @Override // com.android.systemui.plugins.OverlayPlugin.Callback
            public void onHoldStatusBarOpenChange() {
                if (this.mPlugin.holdStatusBarOpen()) {
                    AnonymousClass7.this.mOverlays.add(this.mPlugin);
                } else {
                    AnonymousClass7.this.mOverlays.remove(this.mPlugin);
                }
                StatusBar.this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$7$Callback$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        StatusBar.AnonymousClass7.Callback.this.lambda$onHoldStatusBarOpenChange$2();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onHoldStatusBarOpenChange$2() {
                StatusBar.this.mNotificationShadeWindowController.setStateListener(new NotificationShadeWindowController.OtherwisedCollapsedListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$7$Callback$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.statusbar.NotificationShadeWindowController.OtherwisedCollapsedListener
                    public final void setWouldOtherwiseCollapse(boolean z) {
                        StatusBar.AnonymousClass7.Callback.this.lambda$onHoldStatusBarOpenChange$1(z);
                    }
                });
                AnonymousClass7 anonymousClass7 = AnonymousClass7.this;
                StatusBar.this.mNotificationShadeWindowController.setForcePluginOpen(anonymousClass7.mOverlays.size() != 0, this);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onHoldStatusBarOpenChange$1(final boolean z) {
                AnonymousClass7.this.mOverlays.forEach(new Consumer() { // from class: com.android.systemui.statusbar.phone.StatusBar$7$Callback$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((OverlayPlugin) obj).setCollapseDesired(z);
                    }
                });
            }
        }
    }

    protected void makeStatusBarView(RegisterStatusBarResult registerStatusBarResult) {
        Context context = this.mContext;
        updateDisplaySize();
        updateResources();
        updateTheme();
        inflateStatusBarWindow();
        this.mNotificationShadeWindowViewController.setService(this, this.mNotificationShadeWindowController);
        this.mNotificationShadeWindowView.setOnTouchListener(getStatusBarWindowTouchListener());
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationPanelViewController.getNotificationStackScrollLayoutController();
        this.mStackScrollerController = notificationStackScrollLayoutController;
        this.mStackScroller = notificationStackScrollLayoutController.getView();
        this.mNotificationLogger.setUpWithContainer(this.mStackScrollerController.getNotificationListContainer());
        inflateShelf();
        this.mNotificationIconAreaController.setupShelf(this.mNotificationShelfController);
        this.mNotificationPanelViewController.addExpansionListener(this.mWakeUpCoordinator);
        this.mNotificationPanelViewController.addExpansionListener(new PanelExpansionListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda7
            @Override // com.android.systemui.statusbar.phone.PanelExpansionListener
            public final void onPanelExpansionChanged(float f, boolean z) {
                StatusBar.this.dispatchPanelExpansionForKeyguardDismiss(f, z);
            }
        });
        this.mPluginDependencyProvider.allowPluginDependency(DarkIconDispatcher.class);
        this.mPluginDependencyProvider.allowPluginDependency(StatusBarStateController.class);
        FragmentHostManager.get(this.mPhoneStatusBarWindow).addTagListener("CollapsedStatusBarFragment", new FragmentHostManager.FragmentListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda3
            @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
            public final void onFragmentViewCreated(String str, Fragment fragment) {
                StatusBar.this.lambda$makeStatusBarView$3(str, fragment);
            }
        }).getFragmentManager().beginTransaction().replace(R$id.status_bar_container, new CollapsedStatusBarFragment(this.mOngoingCallController, this.mAnimationScheduler, this.mStatusBarLocationPublisher, this.mNotificationIconAreaController, this.mFeatureFlags, this.mStatusBarIconController, this.mKeyguardStateController, this.mNetworkController, this.mStatusBarStateController, this, this.mCommandQueue), "CollapsedStatusBarFragment").commit();
        this.mHeadsUpManager.setup(this.mVisualStabilityManager);
        this.mStatusBarTouchableRegionManager.setup(this, this.mNotificationShadeWindowView);
        this.mHeadsUpManager.addListener(this);
        this.mHeadsUpManager.addListener(this.mNotificationPanelViewController.getOnHeadsUpChangedListener());
        this.mHeadsUpManager.addListener(this.mVisualStabilityManager);
        this.mNotificationPanelViewController.setHeadsUpManager(this.mHeadsUpManager);
        createNavigationBar(registerStatusBarResult);
        if (this.mWallpaperSupported) {
            this.mLockscreenWallpaper = this.mLockscreenWallpaperLazy.get();
        }
        this.mNotificationPanelViewController.setKeyguardIndicationController(this.mKeyguardIndicationController);
        this.mAmbientIndicationContainer = this.mNotificationShadeWindowView.findViewById(R$id.ambient_indication_container);
        this.mAutoHideController.setStatusBar(new AutoHideUiElement() { // from class: com.android.systemui.statusbar.phone.StatusBar.8
            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public void synchronizeState() {
                StatusBar.this.checkBarModes();
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public boolean shouldHideOnTouch() {
                return !StatusBar.this.mRemoteInputManager.getController().isRemoteInputActive();
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public boolean isVisible() {
                return StatusBar.this.isTransientShown();
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public void hide() {
                StatusBar.this.clearTransient();
            }
        });
        ScrimView scrimView = (ScrimView) this.mNotificationShadeWindowView.findViewById(R$id.scrim_behind);
        ScrimView scrimView2 = (ScrimView) this.mNotificationShadeWindowView.findViewById(R$id.scrim_notifications);
        ScrimView scrimView3 = (ScrimView) this.mNotificationShadeWindowView.findViewById(R$id.scrim_in_front);
        ScrimView scrimForBubble = this.mBubblesManagerOptional.isPresent() ? this.mBubblesManagerOptional.get().getScrimForBubble() : null;
        this.mScrimController.setScrimVisibleListener(new Consumer() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda36
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatusBar.this.lambda$makeStatusBarView$4((Integer) obj);
            }
        });
        this.mScrimController.attachViews(scrimView, scrimView2, scrimView3, scrimForBubble);
        LightRevealScrim lightRevealScrim = (LightRevealScrim) this.mNotificationShadeWindowView.findViewById(R$id.light_reveal_scrim);
        this.mLightRevealScrim = lightRevealScrim;
        lightRevealScrim.setScrimOpaqueChangedListener(new Consumer() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatusBar.this.lambda$makeStatusBarView$6((Boolean) obj);
            }
        });
        this.mUnlockedScreenOffAnimationController.initialize(this, this.mLightRevealScrim);
        updateLightRevealScrimVisibility();
        this.mNotificationPanelViewController.initDependencies(this, this.mNotificationShelfController);
        final BackDropView backDropView = (BackDropView) this.mNotificationShadeWindowView.findViewById(R$id.backdrop);
        this.mMediaManager.setup(backDropView, (ImageView) backDropView.findViewById(R$id.backdrop_front), (ImageView) backDropView.findViewById(R$id.backdrop_back), this.mScrimController, this.mLockscreenWallpaper);
        final float f = this.mContext.getResources().getFloat(17105116);
        this.mNotificationShadeDepthControllerLazy.get().addListener(new NotificationShadeDepthController.DepthListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda5
            @Override // com.android.systemui.statusbar.NotificationShadeDepthController.DepthListener
            public final void onWallpaperZoomOutChanged(float f2) {
                StatusBar.lambda$makeStatusBarView$7(f, backDropView, f2);
            }
        });
        this.mNotificationPanelViewController.setUserSetupComplete(this.mUserSetup);
        NotificationShadeWindowView notificationShadeWindowView = this.mNotificationShadeWindowView;
        int i = R$id.qs_frame;
        View findViewById = notificationShadeWindowView.findViewById(i);
        if (findViewById != null) {
            FragmentHostManager fragmentHostManager = FragmentHostManager.get(findViewById);
            ExtensionFragmentListener.attachExtensonToFragment(findViewById, QS.TAG, i, this.mExtensionController.mo1298newExtension(QS.class).withPlugin(QS.class).withDefault(new Supplier() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda40
                @Override // java.util.function.Supplier
                public final Object get() {
                    return StatusBar.this.createDefaultQSFragment();
                }
            }).build());
            this.mBrightnessMirrorController = new BrightnessMirrorController(this.mNotificationShadeWindowView, this.mNotificationPanelViewController, this.mNotificationShadeDepthControllerLazy.get(), this.mBrightnessSliderFactory, new Consumer() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda35
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    StatusBar.this.lambda$makeStatusBarView$8((Boolean) obj);
                }
            });
            fragmentHostManager.addTagListener(QS.TAG, new FragmentHostManager.FragmentListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda4
                @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
                public final void onFragmentViewCreated(String str, Fragment fragment) {
                    StatusBar.this.lambda$makeStatusBarView$9(str, fragment);
                }
            });
        }
        View findViewById2 = this.mNotificationShadeWindowView.findViewById(R$id.report_rejected_touch);
        this.mReportRejectedTouch = findViewById2;
        if (findViewById2 != null) {
            updateReportRejectedTouchVisibility();
            this.mReportRejectedTouch.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StatusBar.this.lambda$makeStatusBarView$10(view);
                }
            });
        }
        if (!this.mPowerManager.isScreenOn()) {
            this.mBroadcastReceiver.onReceive(this.mContext, new Intent("android.intent.action.SCREEN_OFF"));
        }
        this.mGestureWakeLock = this.mPowerManager.newWakeLock(10, "GestureWakeLock");
        Vibrator vibrator = (Vibrator) this.mContext.getSystemService(Vibrator.class);
        this.mVibrator = vibrator;
        this.mCameraLaunchGestureVibrationEffect = getCameraGestureVibrationEffect(vibrator, context.getResources());
        registerBroadcastReceiver();
        context.registerReceiverAsUser(this.mDemoReceiver, UserHandle.ALL, new IntentFilter(), "android.permission.DUMP", null);
        this.mDeviceProvisionedController.addCallback(this.mUserSetupObserver);
        this.mUserSetupObserver.onUserSetupChanged();
        ThreadedRenderer.overrideProperty("disableProfileBars", "true");
        ThreadedRenderer.overrideProperty("ambientRatio", String.valueOf(1.5f));
        ((FaceRecognitionController) Dependency.get(FaceRecognitionController.class)).registerCallback(this.mFaceAnimationCallback);
        LiftWakeGestureController liftWakeGestureController = (LiftWakeGestureController) Dependency.get(LiftWakeGestureController.class);
        this.mLiftWakeGestureController = liftWakeGestureController;
        liftWakeGestureController.setCallback(this.mLiftAndMotionCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$makeStatusBarView$3(String str, Fragment fragment) {
        PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
        PhoneStatusBarView phoneStatusBarView2 = (PhoneStatusBarView) ((CollapsedStatusBarFragment) fragment).getView();
        this.mStatusBarView = phoneStatusBarView2;
        phoneStatusBarView2.setBar(this);
        this.mStatusBarView.setPanel(this.mNotificationPanelViewController);
        this.mStatusBarView.setScrimController(this.mScrimController);
        this.mStatusBarView.setExpansionChangedListeners(this.mExpansionChangedListeners);
        for (ExpansionChangedListener expansionChangedListener : this.mExpansionChangedListeners) {
            sendInitialExpansionAmount(expansionChangedListener);
        }
        if (this.mHeadsUpManager.hasPinnedHeadsUp()) {
            this.mNotificationPanelViewController.notifyBarPanelExpansionChanged();
        }
        this.mStatusBarView.setBouncerShowing(this.mBouncerShowing);
        if (phoneStatusBarView != null) {
            this.mStatusBarView.panelExpansionChanged(phoneStatusBarView.getExpansionFraction(), phoneStatusBarView.isExpanded());
        }
        HeadsUpAppearanceController headsUpAppearanceController = this.mHeadsUpAppearanceController;
        if (headsUpAppearanceController != null) {
            headsUpAppearanceController.destroy();
        }
        HeadsUpAppearanceController headsUpAppearanceController2 = new HeadsUpAppearanceController(this.mNotificationIconAreaController, this.mHeadsUpManager, this.mStackScroller.getController(), this.mStatusBarStateController, this.mKeyguardBypassController, this.mKeyguardStateController, this.mWakeUpCoordinator, this.mCommandQueue, this.mNotificationPanelViewController, this.mStatusBarView);
        this.mHeadsUpAppearanceController = headsUpAppearanceController2;
        headsUpAppearanceController2.readFrom(headsUpAppearanceController);
        this.mLightsOutNotifController.setLightsOutNotifView(this.mStatusBarView.findViewById(R$id.notification_lights_out));
        this.mNotificationShadeWindowViewController.setStatusBarView(this.mStatusBarView);
        checkBarModes();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$makeStatusBarView$4(Integer num) {
        this.mNotificationShadeWindowController.setScrimsVisibility(num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$makeStatusBarView$6(Boolean bool) {
        Runnable runnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$makeStatusBarView$5();
            }
        };
        if (bool.booleanValue()) {
            this.mLightRevealScrim.post(runnable);
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$makeStatusBarView$5() {
        this.mNotificationShadeWindowController.setLightRevealScrimOpaque(this.mLightRevealScrim.isScrimOpaque());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$makeStatusBarView$7(float f, BackDropView backDropView, float f2) {
        float lerp = MathUtils.lerp(f, 1.0f, f2);
        backDropView.setPivotX(backDropView.getWidth() / 2.0f);
        backDropView.setPivotY(backDropView.getHeight() / 2.0f);
        backDropView.setScaleX(lerp);
        backDropView.setScaleY(lerp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$makeStatusBarView$8(Boolean bool) {
        this.mBrightnessMirrorVisible = bool.booleanValue();
        updateScrimController();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$makeStatusBarView$9(String str, Fragment fragment) {
        QS qs = (QS) fragment;
        if (qs instanceof QSFragment) {
            QSPanelController qSPanelController = ((QSFragment) qs).getQSPanelController();
            this.mQSPanelController = qSPanelController;
            qSPanelController.setBrightnessMirror(this.mBrightnessMirrorController);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$makeStatusBarView$10(View view) {
        Uri reportRejectedTouch = this.mFalsingManager.reportRejectedTouch();
        if (reportRejectedTouch == null) {
            return;
        }
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("Build info: ");
        stringWriter.write(SystemProperties.get("ro.build.description"));
        stringWriter.write("\nSerial number: ");
        stringWriter.write(SystemProperties.get("ro.serialno"));
        stringWriter.write("\n");
        startActivityDismissingKeyguard(Intent.createChooser(new Intent("android.intent.action.SEND").setType("*/*").putExtra("android.intent.extra.SUBJECT", "Rejected touch report").putExtra("android.intent.extra.STREAM", reportRejectedTouch).putExtra("android.intent.extra.TEXT", stringWriter.toString()), "Share rejected touch report").addFlags(268435456), true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchPanelExpansionForKeyguardDismiss(float f, boolean z) {
        if (!isKeyguardShowing() || !this.mKeyguardStateController.canDismissLockScreen()) {
            return;
        }
        if (this.mNotificationPanelViewController.isQsExpanded() && z) {
            return;
        }
        if (!z && !this.mKeyguardViewMediator.isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe() && !this.mKeyguardUnlockAnimationController.isUnlockingWithSmartSpaceTransition()) {
            return;
        }
        this.mKeyguardStateController.notifyKeyguardDismissAmountChanged(1.0f - f, z);
    }

    @Override // androidx.lifecycle.LifecycleOwner
    /* renamed from: getLifecycle */
    public Lifecycle mo1437getLifecycle() {
        return this.mLifecycle;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onPowerSaveChanged(boolean z) {
        this.mHandler.post(this.mCheckBarModes);
        DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
        if (dozeServiceHost != null) {
            dozeServiceHost.firePowerSaveChanged(z);
        }
    }

    @VisibleForTesting
    protected void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.app.action.SHOW_DEVICE_MONITORING_DIALOG");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter, null, UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public QS createDefaultQSFragment() {
        return (QS) FragmentHostManager.get(this.mNotificationShadeWindowView).create(QSFragment.class);
    }

    private void setUpPresenter() {
        this.mActivityLaunchAnimator = new ActivityLaunchAnimator(this, this.mContext);
        this.mNotificationAnimationProvider = new NotificationLaunchAnimatorControllerProvider(this.mNotificationShadeWindowViewController, this.mStackScrollerController.getNotificationListContainer(), this.mHeadsUpManager);
        StatusBarNotificationPresenter statusBarNotificationPresenter = new StatusBarNotificationPresenter(this.mContext, this.mNotificationPanelViewController, this.mHeadsUpManager, this.mNotificationShadeWindowView, this.mStackScrollerController, this.mDozeScrimController, this.mScrimController, this.mNotificationShadeWindowController, this.mDynamicPrivacyController, this.mKeyguardStateController, this.mKeyguardIndicationController, this, this.mShadeController, this.mLockscreenShadeTransitionController, this.mCommandQueue, this.mInitController, this.mNotificationInterruptStateProvider);
        this.mPresenter = statusBarNotificationPresenter;
        this.mNotificationShelfController.setOnActivatedListener(statusBarNotificationPresenter);
        this.mRemoteInputManager.getController().addCallback(this.mNotificationShadeWindowController);
        StatusBarNotificationActivityStarter build = this.mStatusBarNotificationActivityStarterBuilder.setStatusBar(this).setActivityLaunchAnimator(this.mActivityLaunchAnimator).setNotificationAnimatorControllerProvider(this.mNotificationAnimationProvider).setNotificationPresenter(this.mPresenter).setNotificationPanelViewController(this.mNotificationPanelViewController).build();
        this.mNotificationActivityStarter = build;
        this.mStackScroller.setNotificationActivityStarter(build);
        this.mGutsManager.setNotificationActivityStarter(this.mNotificationActivityStarter);
        this.mNotificationsController.initialize(this, this.mBubblesOptional, this.mPresenter, this.mStackScrollerController.getNotificationListContainer(), this.mNotificationActivityStarter, this.mPresenter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: setUpDisableFlags */
    public void lambda$start$2(int i, int i2) {
        this.mCommandQueue.setDisableFlagsForSetup(this.mDisplayId, i, i2);
    }

    public void wakeUpIfDozing(long j, View view, String str) {
        if (!this.mDozing || this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying()) {
            return;
        }
        PowerManager powerManager = this.mPowerManager;
        powerManager.wakeUp(j, 4, "com.android.systemui:" + str);
        this.mWakeUpComingFromTouch = true;
        view.getLocationInWindow(this.mTmpInt2);
        this.mWakeUpTouchLocation = new PointF((float) (this.mTmpInt2[0] + (view.getWidth() / 2)), (float) (this.mTmpInt2[1] + (view.getHeight() / 2)));
        this.mFalsingCollector.onScreenOnFromTouch();
    }

    protected void createNavigationBar(RegisterStatusBarResult registerStatusBarResult) {
        this.mNavigationBarController.createNavigationBars(true, registerStatusBarResult);
    }

    protected View.OnTouchListener getStatusBarWindowTouchListener() {
        return new View.OnTouchListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$getStatusBarWindowTouchListener$11;
                lambda$getStatusBarWindowTouchListener$11 = StatusBar.this.lambda$getStatusBarWindowTouchListener$11(view, motionEvent);
                return lambda$getStatusBarWindowTouchListener$11;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getStatusBarWindowTouchListener$11(View view, MotionEvent motionEvent) {
        this.mAutoHideController.checkUserAutoHide(motionEvent);
        this.mRemoteInputManager.checkRemoteInputOutside(motionEvent);
        if (motionEvent.getAction() == 0 && this.mExpandedVisible) {
            this.mShadeController.animateCollapsePanels();
        }
        return this.mNotificationShadeWindowView.onTouchEvent(motionEvent);
    }

    private void inflateShelf() {
        this.mNotificationShelfController = this.mSuperStatusBarViewFactory.getNotificationShelfController(this.mStackScroller);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        BrightnessMirrorController brightnessMirrorController = this.mBrightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.onDensityOrFontScaleChanged();
        }
        this.mUserInfoControllerImpl.onDensityOrFontScaleChanged();
        this.mUserSwitcherController.onDensityOrFontScaleChanged();
        this.mNotificationIconAreaController.onDensityOrFontScaleChanged(this.mContext);
        this.mHeadsUpManager.onDensityOrFontScaleChanged();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onThemeChanged() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager != null) {
            statusBarKeyguardViewManager.onThemeChanged();
        }
        View view = this.mAmbientIndicationContainer;
        if (view instanceof AutoReinflateContainer) {
            ((AutoReinflateContainer) view).inflateLayout();
        }
        this.mNotificationIconAreaController.onThemeChanged();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onOverlayChanged() {
        BrightnessMirrorController brightnessMirrorController = this.mBrightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.onOverlayChanged();
        }
        this.mNotificationPanelViewController.onThemeChanged();
        onThemeChanged();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onUiModeChanged() {
        BrightnessMirrorController brightnessMirrorController = this.mBrightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.onUiModeChanged();
        }
    }

    private void inflateStatusBarWindow() {
        this.mNotificationShadeWindowView = this.mSuperStatusBarViewFactory.getNotificationShadeWindowView();
        StatusBarComponent build = this.mStatusBarComponentBuilder.mo1933get().mo1417statusBarWindowView(this.mNotificationShadeWindowView).build();
        this.mNotificationShadeWindowViewController = build.getNotificationShadeWindowViewController();
        this.mNotificationShadeWindowController.setNotificationShadeView(this.mNotificationShadeWindowView);
        this.mNotificationShadeWindowViewController.setupExpandedStatusBar();
        this.mStatusBarWindowController = build.getStatusBarWindowController();
        this.mPhoneStatusBarWindow = this.mSuperStatusBarViewFactory.getStatusBarWindowView();
        this.mNotificationPanelViewController = build.getNotificationPanelViewController();
        build.getLockIconViewController().init();
        AuthRippleController authRippleController = build.getAuthRippleController();
        this.mAuthRippleController = authRippleController;
        authRippleController.init();
    }

    protected void startKeyguard() {
        Trace.beginSection("StatusBar#startKeyguard");
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockControllerLazy.get();
        this.mBiometricUnlockController = biometricUnlockController;
        biometricUnlockController.setBiometricModeListener(new BiometricUnlockController.BiometricModeListener() { // from class: com.android.systemui.statusbar.phone.StatusBar.9
            @Override // com.android.systemui.statusbar.phone.BiometricUnlockController.BiometricModeListener
            public void onResetMode() {
                setWakeAndUnlocking(false);
            }

            @Override // com.android.systemui.statusbar.phone.BiometricUnlockController.BiometricModeListener
            public void onModeChanged(int i) {
                if (i == 1 || i == 2 || i == 6) {
                    setWakeAndUnlocking(true);
                }
            }

            @Override // com.android.systemui.statusbar.phone.BiometricUnlockController.BiometricModeListener
            public void notifyBiometricAuthModeChanged() {
                StatusBar.this.notifyBiometricAuthModeChanged();
            }

            private void setWakeAndUnlocking(boolean z) {
                if (StatusBar.this.getNavigationBarView() != null) {
                    StatusBar.this.getNavigationBarView().setWakeAndUnlocking(z);
                }
            }
        });
        this.mStatusBarKeyguardViewManager.registerStatusBar(this, getBouncerContainer(), this.mNotificationPanelViewController, this.mBiometricUnlockController, this.mStackScroller, this.mKeyguardBypassController);
        this.mKeyguardIndicationController.setStatusBarKeyguardViewManager(this.mStatusBarKeyguardViewManager);
        this.mBiometricUnlockController.setKeyguardViewController(this.mStatusBarKeyguardViewManager);
        this.mRemoteInputManager.getController().addCallback(this.mStatusBarKeyguardViewManager);
        this.mDynamicPrivacyController.setStatusBarKeyguardViewManager(this.mStatusBarKeyguardViewManager);
        this.mLightBarController.setBiometricUnlockController(this.mBiometricUnlockController);
        this.mMediaManager.setBiometricUnlockController(this.mBiometricUnlockController);
        this.mKeyguardDismissUtil.setDismissHandler(new KeyguardDismissHandler() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda6
            @Override // com.android.systemui.statusbar.phone.KeyguardDismissHandler
            public final void executeWhenUnlocked(ActivityStarter.OnDismissAction onDismissAction, boolean z, boolean z2) {
                StatusBar.this.executeWhenUnlocked(onDismissAction, z, z2);
            }
        });
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View getStatusBarView() {
        return this.mStatusBarView;
    }

    public NotificationShadeWindowView getNotificationShadeWindowView() {
        return this.mNotificationShadeWindowView;
    }

    public NotificationShadeWindowViewController getNotificationShadeWindowViewController() {
        return this.mNotificationShadeWindowViewController;
    }

    public NotificationPanelViewController getNotificationPanelViewController() {
        return this.mNotificationPanelViewController;
    }

    protected ViewGroup getBouncerContainer() {
        return this.mNotificationShadeWindowView;
    }

    public int getStatusBarHeight() {
        return this.mStatusBarWindowController.getStatusBarHeight();
    }

    public boolean toggleSplitScreenMode(int i, int i2) {
        if (!this.mSplitScreenOptional.isPresent()) {
            return false;
        }
        LegacySplitScreen legacySplitScreen = this.mSplitScreenOptional.get();
        if (legacySplitScreen.isDividerVisible()) {
            if (legacySplitScreen.isMinimized() && !legacySplitScreen.isHomeStackResizable()) {
                return false;
            }
            legacySplitScreen.onUndockingTask();
            if (i2 != -1) {
                this.mMetricsLogger.action(i2);
            }
            return true;
        } else if (!legacySplitScreen.splitPrimaryTask()) {
            return false;
        } else {
            if (i != -1) {
                this.mMetricsLogger.action(i);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateQsExpansionEnabled() {
        UserSwitcherController userSwitcherController;
        boolean z = true;
        if (!this.mDeviceProvisionedController.isDeviceProvisioned() || ((!this.mUserSetup && (userSwitcherController = this.mUserSwitcherController) != null && userSwitcherController.isSimpleUserSwitcher()) || isShadeDisabled() || (this.mDisabled2 & 1) != 0 || this.mDozing || ONLY_CORE_APPS)) {
            z = false;
        }
        this.mNotificationPanelViewController.setQsExpansionEnabledPolicy(z);
        Log.d("StatusBar", "updateQsExpansionEnabled - QS Expand enabled: " + z);
    }

    public boolean isShadeDisabled() {
        return (this.mDisabled2 & 4) != 0;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void addQsTile(ComponentName componentName) {
        QSPanelController qSPanelController = this.mQSPanelController;
        if (qSPanelController == null || qSPanelController.getHost() == null) {
            return;
        }
        this.mQSPanelController.getHost().addTile(componentName);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void remQsTile(ComponentName componentName) {
        QSPanelController qSPanelController = this.mQSPanelController;
        if (qSPanelController == null || qSPanelController.getHost() == null) {
            return;
        }
        this.mQSPanelController.getHost().removeTile(componentName);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void clickTile(ComponentName componentName) {
        this.mQSPanelController.clickTile(componentName);
    }

    public void requestNotificationUpdate(String str) {
        this.mNotificationsController.requestNotificationUpdate(str);
    }

    public void requestFaceAuth(boolean z) {
        if (!this.mKeyguardStateController.canDismissLockScreen()) {
            this.mKeyguardUpdateMonitor.requestFaceAuth(z);
        }
    }

    private void updateReportRejectedTouchVisibility() {
        View view = this.mReportRejectedTouch;
        if (view == null) {
            return;
        }
        view.setVisibility((this.mState != 1 || this.mDozing || !this.mFalsingCollector.isReportingEnabled()) ? 4 : 0);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void disable(int i, int i2, int i3, boolean z) {
        if (i != this.mDisplayId) {
            return;
        }
        int adjustDisableFlags = this.mRemoteInputQuickSettingsDisabler.adjustDisableFlags(i3);
        int i4 = this.mDisabled1 ^ i2;
        this.mDisabled1 = i2;
        int i5 = this.mDisabled2 ^ adjustDisableFlags;
        this.mDisabled2 = adjustDisableFlags;
        StringBuilder sb = new StringBuilder();
        sb.append("disable<");
        int i6 = i2 & 65536;
        sb.append(i6 != 0 ? 'E' : 'e');
        int i7 = 65536 & i4;
        sb.append(i7 != 0 ? '!' : ' ');
        char c = 'I';
        sb.append((i2 & 131072) != 0 ? 'I' : 'i');
        sb.append((131072 & i4) != 0 ? '!' : ' ');
        sb.append((i2 & 262144) != 0 ? 'A' : 'a');
        int i8 = 262144 & i4;
        sb.append(i8 != 0 ? '!' : ' ');
        char c2 = 'S';
        sb.append((i2 & 1048576) != 0 ? 'S' : 's');
        sb.append((1048576 & i4) != 0 ? '!' : ' ');
        sb.append((i2 & 4194304) != 0 ? 'B' : 'b');
        sb.append((4194304 & i4) != 0 ? '!' : ' ');
        sb.append((i2 & 2097152) != 0 ? 'H' : 'h');
        sb.append((2097152 & i4) != 0 ? '!' : ' ');
        int i9 = i2 & 16777216;
        sb.append(i9 != 0 ? 'R' : 'r');
        int i10 = i4 & 16777216;
        sb.append(i10 != 0 ? '!' : ' ');
        sb.append((i2 & 8388608) != 0 ? 'C' : 'c');
        sb.append((i4 & 8388608) != 0 ? '!' : ' ');
        if ((i2 & 33554432) == 0) {
            c2 = 's';
        }
        sb.append(c2);
        sb.append((i4 & 33554432) != 0 ? '!' : ' ');
        sb.append("> disable2<");
        sb.append((adjustDisableFlags & 1) != 0 ? 'Q' : 'q');
        int i11 = i5 & 1;
        sb.append(i11 != 0 ? '!' : ' ');
        if ((adjustDisableFlags & 2) == 0) {
            c = 'i';
        }
        sb.append(c);
        sb.append((i5 & 2) != 0 ? '!' : ' ');
        int i12 = adjustDisableFlags & 4;
        sb.append(i12 != 0 ? 'N' : 'n');
        int i13 = i5 & 4;
        sb.append(i13 != 0 ? '!' : ' ');
        sb.append('>');
        Log.d("StatusBar", sb.toString());
        if (i7 != 0 && i6 != 0) {
            this.mShadeController.animateCollapsePanels();
        }
        if (i10 != 0 && i9 != 0) {
            this.mHandler.removeMessages(1020);
            this.mHandler.sendEmptyMessage(1020);
        }
        if (i8 != 0 && areNotificationAlertsDisabled()) {
            this.mHeadsUpManager.releaseAllImmediately();
        }
        if (i11 != 0) {
            updateQsExpansionEnabled();
        }
        if (i13 == 0) {
            return;
        }
        updateQsExpansionEnabled();
        if (i12 == 0) {
            return;
        }
        this.mShadeController.animateCollapsePanels();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean areNotificationAlertsDisabled() {
        return (this.mDisabled1 & 262144) != 0;
    }

    protected H createHandler() {
        return new H();
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z, boolean z2, int i) {
        startActivityDismissingKeyguard(intent, z, z2, i);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z) {
        startActivityDismissingKeyguard(intent, false, z);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller) {
        startActivityDismissingKeyguard(intent, false, z, false, null, 0, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z, boolean z2) {
        startActivityDismissingKeyguard(intent, z, z2);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z, ActivityStarter.Callback callback) {
        startActivityDismissingKeyguard(intent, false, z, false, callback, 0, null);
    }

    public void setQsExpanded(boolean z) {
        this.mNotificationShadeWindowController.setQsExpanded(z);
        this.mNotificationPanelViewController.setStatusAccessibilityImportance(z ? 4 : 0);
        if (getNavigationBarView() != null) {
            getNavigationBarView().onStatusBarPanelStateChanged();
        }
    }

    public boolean isWakeUpComingFromTouch() {
        return this.mWakeUpComingFromTouch;
    }

    public void onKeyguardViewManagerStatesUpdated() {
        logStateToEventlog();
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onUnlockedChanged() {
        updateKeyguardState();
        logStateToEventlog();
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpPinnedModeChanged(boolean z) {
        if (z) {
            this.mNotificationShadeWindowController.setHeadsUpShowing(true);
            this.mStatusBarWindowController.setForceStatusBarVisible(true);
            if (!this.mNotificationPanelViewController.isFullyCollapsed()) {
                return;
            }
            this.mNotificationPanelViewController.getView().requestLayout();
            this.mNotificationShadeWindowController.setForceWindowCollapsed(true);
            this.mNotificationPanelViewController.getView().post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBar.this.lambda$onHeadsUpPinnedModeChanged$12();
                }
            });
            return;
        }
        boolean z2 = this.mKeyguardBypassController.getBypassEnabled() && this.mState == 1;
        if (!this.mNotificationPanelViewController.isFullyCollapsed() || this.mNotificationPanelViewController.isTracking() || z2) {
            this.mNotificationShadeWindowController.setHeadsUpShowing(false);
            if (!z2) {
                return;
            }
            this.mStatusBarWindowController.setForceStatusBarVisible(false);
            return;
        }
        this.mHeadsUpManager.setHeadsUpGoingAway(true);
        this.mNotificationPanelViewController.runAfterAnimationFinished(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$onHeadsUpPinnedModeChanged$13();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHeadsUpPinnedModeChanged$12() {
        this.mNotificationShadeWindowController.setForceWindowCollapsed(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHeadsUpPinnedModeChanged$13() {
        if (!this.mHeadsUpManager.hasPinnedHeadsUp()) {
            this.mNotificationShadeWindowController.setHeadsUpShowing(false);
            this.mHeadsUpManager.setHeadsUpGoingAway(false);
        }
        this.mRemoteInputManager.onPanelCollapsed();
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        this.mNotificationsController.requestNotificationUpdate("onHeadsUpStateChanged");
        if (this.mStatusBarStateController.isDozing() && z) {
            notificationEntry.setPulseSuppressed(false);
            if (!((AODController) Dependency.get(AODController.class)).shouldShowAODView()) {
                setNotificationPanelViewAlpha(1.0f);
            }
            this.mDozeServiceHost.fireNotificationPulse(notificationEntry);
            if (this.mDozeServiceHost.isPulsing()) {
                this.mDozeScrimController.cancelPendingPulseTimeout();
            }
        }
        if (z || this.mHeadsUpManager.hasNotifications()) {
            return;
        }
        this.mDozeScrimController.pulseOutNow();
    }

    public void setPanelExpanded(boolean z) {
        if (this.mPanelExpanded != z) {
            this.mNotificationLogger.onPanelExpandedChanged(z);
        }
        this.mPanelExpanded = z;
        updateHideIconsForBouncer(false);
        this.mNotificationShadeWindowController.setPanelExpanded(z);
        this.mStatusBarStateController.setPanelExpanded(z);
        if (z && this.mStatusBarStateController.getState() != 1) {
            clearNotificationEffects();
        }
        if (!z) {
            this.mRemoteInputManager.onPanelCollapsed();
        }
    }

    public boolean isPulsing() {
        return this.mDozeServiceHost.isPulsing();
    }

    public boolean hideStatusBarIconsWhenExpanded() {
        return this.mNotificationPanelViewController.hideStatusBarIconsWhenExpanded();
    }

    public void onColorsChanged(ColorExtractor colorExtractor, int i) {
        updateTheme();
    }

    public View getAmbientIndicationContainer() {
        return this.mAmbientIndicationContainer;
    }

    public boolean isOccluded() {
        return this.mIsOccluded;
    }

    public void setOccluded(boolean z) {
        this.mIsOccluded = z;
        this.mScrimController.setKeyguardOccluded(z);
        updateHideIconsForBouncer(false);
    }

    public boolean hideStatusBarIconsForBouncer() {
        return this.mHideIconsForBouncer || this.mWereIconsJustHidden;
    }

    private void updateHideIconsForBouncer(boolean z) {
        boolean z2 = false;
        boolean z3 = this.mTopHidesStatusBar && this.mIsOccluded && (this.mStatusBarWindowHidden || this.mBouncerShowing);
        boolean z4 = !this.mPanelExpanded && !this.mIsOccluded && this.mBouncerShowing;
        if (z3 || z4) {
            z2 = true;
        }
        if (this.mHideIconsForBouncer != z2) {
            this.mHideIconsForBouncer = z2;
            if (!z2 && this.mBouncerWasShowingWhenHidden) {
                this.mWereIconsJustHidden = true;
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda22
                    @Override // java.lang.Runnable
                    public final void run() {
                        StatusBar.this.lambda$updateHideIconsForBouncer$14();
                    }
                }, 500L);
            } else {
                this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, z);
            }
        }
        if (z2) {
            this.mBouncerWasShowingWhenHidden = this.mBouncerShowing;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateHideIconsForBouncer$14() {
        this.mWereIconsJustHidden = false;
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, true);
    }

    public boolean headsUpShouldBeVisible() {
        return this.mHeadsUpAppearanceController.shouldBeVisible();
    }

    public void onLaunchAnimationCancelled(boolean z) {
        if (this.mPresenter.isPresenterFullyCollapsed() && !this.mPresenter.isCollapsing() && z) {
            onClosingFinished();
        } else {
            this.mShadeController.collapsePanel(true);
        }
    }

    public void onLaunchAnimationEnd(boolean z) {
        if (!this.mPresenter.isCollapsing()) {
            onClosingFinished();
        }
        if (z) {
            instantCollapseNotificationPanel();
        }
    }

    public boolean shouldAnimateLaunch(boolean z) {
        if (isOccluded()) {
            return false;
        }
        if (!this.mKeyguardStateController.isShowing()) {
            return true;
        }
        return z && KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation;
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Callback
    public boolean isOnKeyguard() {
        return this.mKeyguardStateController.isShowing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideKeyguardWithAnimation$15(IRemoteAnimationRunner iRemoteAnimationRunner) {
        this.mKeyguardViewMediator.hideWithAnimation(iRemoteAnimationRunner);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Callback
    public void hideKeyguardWithAnimation(final IRemoteAnimationRunner iRemoteAnimationRunner) {
        this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$hideKeyguardWithAnimation$15(iRemoteAnimationRunner);
            }
        });
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Callback
    public void setBlursDisabledForAppLaunch(boolean z) {
        this.mKeyguardViewMediator.setBlursDisabledForAppLaunch(z);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Callback
    public int getBackgroundColor(TaskInfo taskInfo) {
        if (!this.mStartingSurfaceOptional.isPresent()) {
            Log.w("StatusBar", "No starting surface, defaulting to SystemBGColor");
            return SplashscreenContentDrawer.getSystemBGColor();
        }
        return this.mStartingSurfaceOptional.get().getBackgroundColor(taskInfo);
    }

    public boolean isDeviceInVrMode() {
        return this.mPresenter.isDeviceInVrMode();
    }

    public NotificationPresenter getPresenter() {
        return this.mPresenter;
    }

    @VisibleForTesting
    void setBarStateForTest(int i) {
        this.mState = i;
    }

    @VisibleForTesting
    void setUserSetupForTest(boolean z) {
        this.mUserSetup = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class H extends Handler {
        protected H() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1026) {
                StatusBar.this.toggleKeyboardShortcuts(message.arg1);
            } else if (i == 1027) {
                StatusBar.this.dismissKeyboardShortcuts();
            } else {
                switch (i) {
                    case 1000:
                        StatusBar.this.animateExpandNotificationsPanel();
                        return;
                    case 1001:
                        StatusBar.this.mShadeController.animateCollapsePanels();
                        return;
                    case 1002:
                        StatusBar.this.animateExpandSettingsPanel((String) message.obj);
                        return;
                    case 1003:
                        StatusBar.this.onLaunchTransitionTimeout();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void maybeEscalateHeadsUp() {
        this.mHeadsUpManager.getAllEntries().forEach(StatusBar$$ExternalSyntheticLambda37.INSTANCE);
        this.mHeadsUpManager.releaseAllImmediately();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeEscalateHeadsUp$16(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        Notification notification = sbn.getNotification();
        if (notification.fullScreenIntent != null) {
            try {
                EventLog.writeEvent(36003, sbn.getKey());
                notification.fullScreenIntent.send();
                notificationEntry.notifyFullScreenIntentLaunched();
            } catch (PendingIntent.CanceledException unused) {
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void handleSystemKey(int i) {
        if (!this.mCommandQueue.panelsEnabled() || !this.mKeyguardUpdateMonitor.isDeviceInteractive()) {
            return;
        }
        if ((this.mKeyguardStateController.isShowing() && !this.mKeyguardStateController.isOccluded()) || !this.mUserSetup) {
            return;
        }
        if (280 == i) {
            this.mMetricsLogger.action(493);
            this.mNotificationPanelViewController.collapse(false, 1.0f);
        } else if (281 != i) {
        } else {
            this.mMetricsLogger.action(494);
            if (this.mNotificationPanelViewController.isFullyCollapsed()) {
                if (this.mVibrateOnOpening) {
                    this.mVibratorHelper.vibrate(2);
                }
                this.mNotificationPanelViewController.expand(true);
                this.mStackScroller.setWillExpand(true);
                this.mHeadsUpManager.unpinAll(true);
                this.mMetricsLogger.count("panel_open", 1);
            } else if (this.mNotificationPanelViewController.isInSettings() || this.mNotificationPanelViewController.isExpanding()) {
            } else {
                this.mNotificationPanelViewController.flingSettings(0.0f, 0);
                this.mMetricsLogger.count("panel_open_qs", 1);
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showPinningEnterExitToast(boolean z) {
        if (getNavigationBarView() != null) {
            getNavigationBarView().showPinningEnterExitToast(z);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showPinningEscapeToast() {
        if (getNavigationBarView() != null) {
            getNavigationBarView().showPinningEscapeToast();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeExpandedVisible(boolean z) {
        if (z || (!this.mExpandedVisible && this.mCommandQueue.panelsEnabled())) {
            this.mExpandedVisible = true;
            this.mNotificationShadeWindowController.setPanelVisible(true);
            visibilityChanged(true);
            this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, !z);
            setInteracting(1, true);
        }
    }

    public void postAnimateCollapsePanels() {
        H h = this.mHandler;
        final ShadeController shadeController = this.mShadeController;
        Objects.requireNonNull(shadeController);
        h.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ShadeController.this.animateCollapsePanels();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postAnimateForceCollapsePanels$17() {
        this.mShadeController.animateCollapsePanels(0, true);
    }

    public void postAnimateForceCollapsePanels() {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$postAnimateForceCollapsePanels$17();
            }
        });
    }

    public void postAnimateOpenPanels() {
        this.mHandler.sendEmptyMessage(1002);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void togglePanel() {
        if (this.mPanelExpanded) {
            this.mShadeController.animateCollapsePanels();
        } else {
            animateExpandNotificationsPanel();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void animateCollapsePanels(int i, boolean z) {
        this.mShadeController.animateCollapsePanels(i, z, false, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postHideRecentApps() {
        if (!this.mHandler.hasMessages(1020)) {
            this.mHandler.removeMessages(1020);
            this.mHandler.sendEmptyMessage(1020);
        }
    }

    public boolean isPanelExpanded() {
        return this.mPanelExpanded;
    }

    public void onInputFocusTransfer(boolean z, boolean z2, float f) {
        if (!this.mCommandQueue.panelsEnabled()) {
            return;
        }
        if (z) {
            this.mNotificationPanelViewController.startWaitingForOpenPanelGesture();
        } else {
            this.mNotificationPanelViewController.stopWaitingForOpenPanelGesture(z2, f);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void animateExpandNotificationsPanel() {
        if (!this.mCommandQueue.panelsEnabled()) {
            return;
        }
        this.mNotificationPanelViewController.expandWithoutQs();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void animateExpandSettingsPanel(String str) {
        if (this.mCommandQueue.panelsEnabled() && this.mUserSetup) {
            if (str != null) {
                this.mQSPanelController.openDetails(str);
            }
            this.mNotificationPanelViewController.expandWithQs();
        }
    }

    public void animateCollapseQuickSettings() {
        if (this.mState == 0) {
            this.mStatusBarView.collapsePanel(true, false, 1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeExpandedInvisible() {
        if (SystemUIDebugConfig.DEBUG_STATUS_BAR) {
            Log.d("StatusBar", "makeExpandedInvisible: mExpandedVisible=" + this.mExpandedVisible + " mExpandedVisible=" + this.mExpandedVisible);
        }
        if (!this.mExpandedVisible || this.mNotificationShadeWindowView == null) {
            return;
        }
        this.mStatusBarView.collapsePanel(false, false, 1.0f);
        this.mNotificationPanelViewController.closeQs();
        this.mExpandedVisible = false;
        visibilityChanged(false);
        this.mNotificationShadeWindowController.setPanelVisible(false);
        this.mStatusBarWindowController.setForceStatusBarVisible(false);
        this.mGutsManager.closeAndSaveGuts(true, true, true, -1, -1, true);
        this.mShadeController.runPostCollapseRunnables();
        setInteracting(1, false);
        if (!this.mNotificationActivityStarter.isCollapsingToShowActivityOverLockscreen()) {
            showBouncerIfKeyguard();
        }
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, this.mNotificationPanelViewController.hideStatusBarIconsWhenExpanded());
        if (this.mStatusBarKeyguardViewManager.isShowing()) {
            return;
        }
        WindowManagerGlobal.getInstance().trimMemory(20);
    }

    public boolean interceptTouchEvent(MotionEvent motionEvent) {
        if (this.mStatusBarWindowState == 0) {
            if ((motionEvent.getAction() == 1 || motionEvent.getAction() == 3) && !this.mExpandedVisible) {
                setInteracting(1, false);
            } else {
                setInteracting(1, true);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSameStatusBarState(int i) {
        return this.mStatusBarWindowState == i;
    }

    public GestureRecorder getGestureRecorder() {
        return this.mGestureRec;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void setWindowState(int i, int i2, int i3) {
        if (i != this.mDisplayId) {
            return;
        }
        boolean z = true;
        boolean z2 = i3 == 0;
        if (this.mNotificationShadeWindowView != null && i2 == 1 && this.mStatusBarWindowState != i3) {
            this.mStatusBarWindowState = i3;
            PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
            if (phoneStatusBarView != null) {
                if (!z2 && this.mState == 0) {
                    phoneStatusBarView.collapsePanel(false, false, 1.0f);
                }
                if (i3 != 2) {
                    z = false;
                }
                this.mStatusBarWindowHidden = z;
                updateHideIconsForBouncer(false);
            }
        }
        updateBubblesVisibility();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, boolean z2) {
        if (i != this.mDisplayId) {
            return;
        }
        boolean z3 = false;
        if (this.mAppearance != i2) {
            this.mAppearance = i2;
            z3 = updateBarMode(barMode(this.mTransientShown, i2));
        }
        this.mLightBarController.onStatusBarAppearanceChanged(appearanceRegionArr, z3, this.mStatusBarMode, z);
        updateBubblesVisibility();
        this.mStatusBarStateController.setFullscreenState(z2);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 0)) {
            showTransientUnchecked();
        }
    }

    private void showTransientUnchecked() {
        if (!this.mTransientShown) {
            this.mTransientShown = true;
            this.mNoAnimationOnNextBarModeChange = true;
            handleTransientChanged();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void abortTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 0)) {
            clearTransient();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearTransient() {
        if (this.mTransientShown) {
            this.mTransientShown = false;
            handleTransientChanged();
        }
    }

    private void handleTransientChanged() {
        int barMode = barMode(this.mTransientShown, this.mAppearance);
        if (updateBarMode(barMode)) {
            this.mLightBarController.onStatusBarModeChanged(barMode);
            updateBubblesVisibility();
        }
    }

    private boolean updateBarMode(int i) {
        if (this.mStatusBarMode != i) {
            this.mStatusBarMode = i;
            checkBarModes();
            this.mAutoHideController.touchAutoHide();
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showWirelessChargingAnimation(int i) {
        showChargingAnimation(i, -1, 0L);
    }

    protected void showChargingAnimation(int i, int i2, long j) {
        WirelessChargingAnimation.makeWirelessChargingAnimation(this.mContext, null, i2, i, new WirelessChargingAnimation.Callback() { // from class: com.android.systemui.statusbar.phone.StatusBar.10
            @Override // com.android.systemui.charging.WirelessChargingAnimation.Callback
            public void onAnimationStarting() {
                StatusBar.this.mNotificationShadeWindowController.setRequestTopUi(true, "StatusBar");
            }

            @Override // com.android.systemui.charging.WirelessChargingAnimation.Callback
            public void onAnimationEnded() {
                StatusBar.this.mNotificationShadeWindowController.setRequestTopUi(false, "StatusBar");
            }
        }, false, sUiEventLogger).show(j);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onRecentsAnimationStateChanged(boolean z) {
        setInteracting(2, z);
    }

    protected BarTransitions getStatusBarTransitions() {
        return this.mNotificationShadeWindowViewController.getBarTransitions();
    }

    public void checkBarModes() {
        if (this.mDemoModeController.isInDemoMode()) {
            return;
        }
        if (this.mNotificationShadeWindowViewController != null && getStatusBarTransitions() != null) {
            checkBarMode(this.mStatusBarMode, this.mStatusBarWindowState, getStatusBarTransitions());
        }
        this.mNavigationBarController.checkNavBarModes(this.mDisplayId);
        this.mNoAnimationOnNextBarModeChange = false;
    }

    public void setQsScrimEnabled(boolean z) {
        this.mNotificationPanelViewController.setQsScrimEnabled(z);
    }

    private void updateBubblesVisibility() {
        if (this.mBubblesOptional.isPresent()) {
            Bubbles bubbles = this.mBubblesOptional.get();
            int i = this.mStatusBarMode;
            bubbles.onStatusBarVisibilityChanged((i == 3 || i == 6 || this.mStatusBarWindowHidden) ? false : true);
        }
    }

    void checkBarMode(int i, int i2, BarTransitions barTransitions) {
        barTransitions.transitionTo(i, !this.mNoAnimationOnNextBarModeChange && this.mDeviceInteractive && i2 != 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishBarAnimations() {
        if (this.mNotificationShadeWindowController != null && this.mNotificationShadeWindowViewController.getBarTransitions() != null) {
            this.mNotificationShadeWindowViewController.getBarTransitions().finishAnimations();
        }
        this.mNavigationBarController.finishBarAnimations(this.mDisplayId);
    }

    public void setInteracting(int i, boolean z) {
        int i2;
        if (z) {
            i2 = i | this.mInteractingWindows;
        } else {
            i2 = (~i) & this.mInteractingWindows;
        }
        this.mInteractingWindows = i2;
        if (i2 != 0) {
            this.mAutoHideController.suspendAutoHide();
        } else {
            this.mAutoHideController.resumeSuspendedAutoHide();
        }
        checkBarModes();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissVolumeDialog() {
        VolumeComponent volumeComponent = this.mVolumeComponent;
        if (volumeComponent != null) {
            volumeComponent.dismissNow();
        }
    }

    public static String viewInfo(View view) {
        return "[(" + view.getLeft() + "," + view.getTop() + ")(" + view.getRight() + "," + view.getBottom() + ") " + view.getWidth() + "x" + view.getHeight() + "]";
    }

    @Override // com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (this.mCommandQueue.isDumpCommandQueue(strArr)) {
            this.mCommandQueue.dump(printWriter);
            return;
        }
        synchronized (this.mQueueLock) {
            printWriter.println("Current Status Bar state:");
            printWriter.println("  mExpandedVisible=" + this.mExpandedVisible);
            printWriter.println("  mDisplayMetrics=" + this.mDisplayMetrics);
            printWriter.println("  mStackScroller: " + viewInfo(this.mStackScroller));
            printWriter.println("  mStackScroller: " + viewInfo(this.mStackScroller) + " scroll " + this.mStackScroller.getScrollX() + "," + this.mStackScroller.getScrollY());
        }
        printWriter.print("  mInteractingWindows=");
        printWriter.println(this.mInteractingWindows);
        printWriter.print("  mStatusBarWindowState=");
        printWriter.println(StatusBarManager.windowStateToString(this.mStatusBarWindowState));
        printWriter.print("  mStatusBarMode=");
        printWriter.println(BarTransitions.modeToString(this.mStatusBarMode));
        printWriter.print("  mDozing=");
        printWriter.println(this.mDozing);
        printWriter.print("  mWallpaperSupported= ");
        printWriter.println(this.mWallpaperSupported);
        printWriter.println("  StatusBarWindowView: ");
        NotificationShadeWindowViewController notificationShadeWindowViewController = this.mNotificationShadeWindowViewController;
        if (notificationShadeWindowViewController != null) {
            notificationShadeWindowViewController.dump(fileDescriptor, printWriter, strArr);
            dumpBarTransitions(printWriter, "PhoneStatusBarTransitions", this.mNotificationShadeWindowViewController.getBarTransitions());
        }
        printWriter.println("  mMediaManager: ");
        NotificationMediaManager notificationMediaManager = this.mMediaManager;
        if (notificationMediaManager != null) {
            notificationMediaManager.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.println("  Panels: ");
        if (this.mNotificationPanelViewController != null) {
            printWriter.println("    mNotificationPanel=" + this.mNotificationPanelViewController.getView() + " params=" + this.mNotificationPanelViewController.getView().getLayoutParams().debug("") + "mNotificationPanel.alpha = " + this.mNotificationPanelViewController.getView().getAlpha());
            printWriter.print("      ");
            this.mNotificationPanelViewController.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.println("  mStackScroller: ");
        if (this.mStackScroller instanceof Dumpable) {
            printWriter.print("      ");
            this.mStackScroller.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.println("  Theme:");
        String str = this.mUiModeManager == null ? "null" : this.mUiModeManager.getNightMode() + "";
        StringBuilder sb = new StringBuilder();
        sb.append("    dark theme: ");
        sb.append(str);
        sb.append(" (auto: ");
        boolean z = false;
        sb.append(0);
        sb.append(", yes: ");
        sb.append(2);
        sb.append(", no: ");
        sb.append(1);
        sb.append(")");
        printWriter.println(sb.toString());
        if (this.mContext.getThemeResId() == R$style.Theme_SystemUI_LightWallpaper) {
            z = true;
        }
        printWriter.println("    light wallpaper theme: " + z);
        KeyguardIndicationController keyguardIndicationController = this.mKeyguardIndicationController;
        if (keyguardIndicationController != null) {
            keyguardIndicationController.dump(fileDescriptor, printWriter, strArr);
        }
        ScrimController scrimController = this.mScrimController;
        if (scrimController != null) {
            scrimController.dump(fileDescriptor, printWriter, strArr);
        }
        if (this.mLightRevealScrim != null) {
            printWriter.println("mLightRevealScrim.getRevealAmount(): " + this.mLightRevealScrim.getRevealAmount());
            printWriter.println("mLightRevealScrim.alpha: " + this.mLightRevealScrim.getAlpha());
            printWriter.println("mLightRevealScrim.visibility: " + this.mLightRevealScrim.getVisibility());
        }
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager != null) {
            statusBarKeyguardViewManager.dump(printWriter);
        }
        this.mNotificationsController.dump(fileDescriptor, printWriter, strArr, true);
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        if (headsUpManagerPhone != null) {
            headsUpManagerPhone.dump(fileDescriptor, printWriter, strArr);
        } else {
            printWriter.println("  mHeadsUpManager: null");
        }
        PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
        if (phoneStatusBarView != null) {
            phoneStatusBarView.dump(fileDescriptor, printWriter, strArr);
        } else {
            printWriter.println("  mStatusBarView: null");
        }
        StatusBarTouchableRegionManager statusBarTouchableRegionManager = this.mStatusBarTouchableRegionManager;
        if (statusBarTouchableRegionManager != null) {
            statusBarTouchableRegionManager.dump(fileDescriptor, printWriter, strArr);
        } else {
            printWriter.println("  mStatusBarTouchableRegionManager: null");
        }
        LightBarController lightBarController = this.mLightBarController;
        if (lightBarController != null) {
            lightBarController.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.println("SharedPreferences:");
        for (Map.Entry<String, ?> entry : Prefs.getAll(this.mContext).entrySet()) {
            printWriter.print("  ");
            printWriter.print(entry.getKey());
            printWriter.print("=");
            printWriter.println(entry.getValue());
        }
        printWriter.println("Camera gesture intents:");
        printWriter.println("   Insecure camera: " + CameraIntents.getInsecureCameraIntent(this.mContext));
        printWriter.println("   Secure camera: " + CameraIntents.getSecureCameraIntent(this.mContext));
        printWriter.println("   Override package: " + String.valueOf(CameraIntents.getOverrideCameraPackage(this.mContext)));
    }

    public static void dumpBarTransitions(PrintWriter printWriter, String str, BarTransitions barTransitions) {
        printWriter.print("  ");
        printWriter.print(str);
        printWriter.print(".BarTransitions.mMode=");
        if (barTransitions != null) {
            printWriter.println(BarTransitions.modeToString(barTransitions.getMode()));
        } else {
            printWriter.println("Unknown");
        }
    }

    public void createAndAddWindows(RegisterStatusBarResult registerStatusBarResult) {
        makeStatusBarView(registerStatusBarResult);
        this.mNotificationShadeWindowController.attach();
        this.mStatusBarWindowController.attach();
    }

    void updateDisplaySize() {
        this.mDisplay.getMetrics(this.mDisplayMetrics);
        this.mDisplay.getSize(this.mCurrentDisplaySize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getDisplayDensity() {
        return this.mDisplayMetrics.density;
    }

    public float getDisplayWidth() {
        return this.mDisplayMetrics.widthPixels;
    }

    public float getDisplayHeight() {
        return this.mDisplayMetrics.heightPixels;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRotation() {
        return this.mDisplay.getRotation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDisplayId() {
        return this.mDisplayId;
    }

    public void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2, int i) {
        startActivityDismissingKeyguard(intent, z, z2, false, null, i, null);
    }

    public void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2) {
        startActivityDismissingKeyguard(intent, z, z2, 0);
    }

    private void startActivityDismissingKeyguard(final Intent intent, boolean z, boolean z2, final boolean z3, final ActivityStarter.Callback callback, final int i, ActivityLaunchAnimator.Controller controller) {
        if (!z || this.mDeviceProvisionedController.isDeviceProvisioned()) {
            boolean wouldLaunchResolverActivity = this.mActivityIntentHelper.wouldLaunchResolverActivity(intent, this.mLockscreenUserManager.getCurrentUserId());
            boolean z4 = controller != null && !wouldLaunchResolverActivity && shouldAnimateLaunch(true);
            final ActivityLaunchAnimator.Controller wrapAnimationController = z4 ? wrapAnimationController(controller, z2) : null;
            boolean z5 = z2 && wrapAnimationController == null;
            final boolean z6 = z4;
            executeRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBar.this.lambda$startActivityDismissingKeyguard$20(intent, i, wrapAnimationController, z6, z3, callback);
                }
            }, new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBar.lambda$startActivityDismissingKeyguard$21(ActivityStarter.Callback.this);
                }
            }, z5, wouldLaunchResolverActivity, true, z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startActivityDismissingKeyguard$20(final Intent intent, int i, ActivityLaunchAnimator.Controller controller, boolean z, final boolean z2, ActivityStarter.Callback callback) {
        this.mAssistManagerLazy.get().hideAssist();
        intent.setFlags(335544320);
        intent.addFlags(i);
        final int[] iArr = {-96};
        this.mActivityLaunchAnimator.startIntentWithAnimation(controller, z, intent.getPackage(), new Function1() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda41
            @Override // kotlin.jvm.functions.Function1
            /* renamed from: invoke */
            public final Object mo1949invoke(Object obj) {
                Integer lambda$startActivityDismissingKeyguard$19;
                lambda$startActivityDismissingKeyguard$19 = StatusBar.this.lambda$startActivityDismissingKeyguard$19(z2, intent, iArr, (RemoteAnimationAdapter) obj);
                return lambda$startActivityDismissingKeyguard$19;
            }
        });
        if (callback != null) {
            callback.onActivityStarted(iArr[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$startActivityDismissingKeyguard$19(boolean z, Intent intent, int[] iArr, RemoteAnimationAdapter remoteAnimationAdapter) {
        ActivityOptions activityOptions = new ActivityOptions(getActivityOptions(this.mDisplayId, remoteAnimationAdapter));
        activityOptions.setDisallowEnterPictureInPictureWhileLaunching(z);
        if (CameraIntents.isInsecureCameraIntent(intent)) {
            activityOptions.setRotationAnimationHint(3);
        }
        if (intent.getAction() == "android.settings.panel.action.VOLUME") {
            activityOptions.setDisallowEnterPictureInPictureWhileLaunching(true);
        }
        try {
            iArr[0] = ActivityTaskManager.getService().startActivityAsUser((IApplicationThread) null, this.mContext.getBasePackageName(), this.mContext.getAttributionTag(), intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), (IBinder) null, (String) null, 0, 268435456, (ProfilerInfo) null, activityOptions.toBundle(), UserHandle.CURRENT.getIdentifier());
        } catch (RemoteException e) {
            Log.w("StatusBar", "Unable to start activity", e);
        }
        return Integer.valueOf(iArr[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startActivityDismissingKeyguard$21(ActivityStarter.Callback callback) {
        if (callback != null) {
            callback.onActivityStarted(-96);
        }
    }

    private ActivityLaunchAnimator.Controller wrapAnimationController(ActivityLaunchAnimator.Controller controller, boolean z) {
        View rootView = controller.getLaunchContainer().getRootView();
        if (rootView != this.mSuperStatusBarViewFactory.getStatusBarWindowView()) {
            return (!z || rootView != this.mNotificationShadeWindowView) ? controller : new StatusBarLaunchAnimatorController(controller, this, true);
        }
        controller.setLaunchContainer(this.mStatusBarWindowController.getLaunchAnimationContainer());
        return new DelegateLaunchAnimatorController(controller) { // from class: com.android.systemui.statusbar.phone.StatusBar.11
            @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
            public void onLaunchAnimationStart(boolean z2) {
                getDelegate().onLaunchAnimationStart(z2);
                StatusBar.this.mStatusBarWindowController.setLaunchAnimationRunning(true);
            }

            @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
            public void onLaunchAnimationEnd(boolean z2) {
                getDelegate().onLaunchAnimationEnd(z2);
                StatusBar.this.mStatusBarWindowController.setLaunchAnimationRunning(false);
            }
        };
    }

    public void readyForKeyguardDone() {
        this.mStatusBarKeyguardViewManager.readyForKeyguardDone();
    }

    public void executeRunnableDismissingKeyguard(Runnable runnable, Runnable runnable2, boolean z, boolean z2, boolean z3) {
        executeRunnableDismissingKeyguard(runnable, runnable2, z, z2, z3, false);
    }

    public void executeRunnableDismissingKeyguard(final Runnable runnable, Runnable runnable2, final boolean z, boolean z2, final boolean z3, final boolean z4) {
        dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.phone.StatusBar.12
            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public boolean onDismiss() {
                if (runnable != null) {
                    if (StatusBar.this.mStatusBarKeyguardViewManager.isShowing() && StatusBar.this.mStatusBarKeyguardViewManager.isOccluded()) {
                        StatusBar.this.mStatusBarKeyguardViewManager.addAfterKeyguardGoneRunnable(runnable);
                    } else {
                        AsyncTask.execute(runnable);
                    }
                }
                if (z) {
                    if (StatusBar.this.mExpandedVisible) {
                        StatusBar statusBar = StatusBar.this;
                        if (!statusBar.mBouncerShowing) {
                            statusBar.mShadeController.animateCollapsePanels(2, true, true);
                        }
                    }
                    StatusBar statusBar2 = StatusBar.this;
                    H h = statusBar2.mHandler;
                    final ShadeController shadeController = statusBar2.mShadeController;
                    Objects.requireNonNull(shadeController);
                    h.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$12$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ShadeController.this.runPostCollapseRunnables();
                        }
                    });
                } else if (StatusBar.this.isInLaunchTransition() && StatusBar.this.mNotificationPanelViewController.isLaunchTransitionFinished()) {
                    StatusBar statusBar3 = StatusBar.this;
                    H h2 = statusBar3.mHandler;
                    final StatusBarKeyguardViewManager statusBarKeyguardViewManager = statusBar3.mStatusBarKeyguardViewManager;
                    Objects.requireNonNull(statusBarKeyguardViewManager);
                    h2.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$12$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            StatusBarKeyguardViewManager.this.readyForKeyguardDone();
                        }
                    });
                }
                return z3;
            }

            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public boolean willRunAnimationOnKeyguard() {
                return z4;
            }
        }, runnable2, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$13  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass13 extends BroadcastReceiver {
        AnonymousClass13() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Trace.beginSection("StatusBar#onReceive");
            String action = intent.getAction();
            int i = 0;
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action)) {
                KeyboardShortcuts.dismiss();
                if (StatusBar.this.mRemoteInputManager.getController() != null) {
                    StatusBar.this.mRemoteInputManager.getController().closeRemoteInputs();
                }
                if (StatusBar.this.mBubblesOptional.isPresent() && ((Bubbles) StatusBar.this.mBubblesOptional.get()).isStackExpanded()) {
                    ((Bubbles) StatusBar.this.mBubblesOptional.get()).collapseStack();
                }
                if (StatusBar.this.mLockscreenUserManager.isCurrentProfile(getSendingUserId())) {
                    String stringExtra = intent.getStringExtra("reason");
                    if (stringExtra != null && stringExtra.equals("recentapps")) {
                        i = 2;
                    }
                    StatusBar.this.mShadeController.animateCollapsePanels(i);
                }
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                NotificationShadeWindowController notificationShadeWindowController = StatusBar.this.mNotificationShadeWindowController;
                if (notificationShadeWindowController != null) {
                    notificationShadeWindowController.setNotTouchable(false);
                }
                if (StatusBar.this.mBubblesOptional.isPresent() && ((Bubbles) StatusBar.this.mBubblesOptional.get()).isStackExpanded()) {
                    StatusBar.this.mMainThreadHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$13$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            StatusBar.AnonymousClass13.this.lambda$onReceive$0();
                        }
                    });
                }
                StatusBar.this.finishBarAnimations();
                StatusBar.this.resetUserExpandedStates();
            } else if ("android.app.action.SHOW_DEVICE_MONITORING_DIALOG".equals(action)) {
                StatusBar.this.mQSPanelController.showDeviceMonitoringDialog();
            }
            Trace.endSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            ((Bubbles) StatusBar.this.mBubblesOptional.get()).collapseStack();
        }
    }

    public void resetUserExpandedStates() {
        this.mNotificationsController.resetUserExpandedStates();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeWhenUnlocked(ActivityStarter.OnDismissAction onDismissAction, boolean z, boolean z2) {
        if (this.mStatusBarKeyguardViewManager.isShowing() && z) {
            this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
        }
        dismissKeyguardThenExecute(onDismissAction, null, z2);
    }

    protected void dismissKeyguardThenExecute(ActivityStarter.OnDismissAction onDismissAction, boolean z) {
        dismissKeyguardThenExecute(onDismissAction, null, z);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void dismissKeyguardThenExecute(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z) {
        if (this.mWakefulnessLifecycle.getWakefulness() == 0 && this.mKeyguardStateController.canDismissLockScreen() && !this.mStatusBarStateController.leaveOpenOnKeyguardHide() && this.mDozeServiceHost.isPulsing()) {
            this.mBiometricUnlockController.startWakeAndUnlock(2);
        }
        if (this.mStatusBarKeyguardViewManager.isShowing()) {
            this.mStatusBarKeyguardViewManager.dismissWithAction(onDismissAction, runnable, z);
        } else {
            onDismissAction.onDismiss();
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onConfigChanged(Configuration configuration) {
        updateResources();
        updateDisplaySize();
        this.mViewHierarchyManager.updateRowStates();
        this.mScreenPinningRequest.onConfigurationChanged();
        ((NothingOSHeadsupManager) Dependency.get(NothingOSHeadsupManager.class)).onConfigurationChanged(configuration);
    }

    public void setLockscreenUser(int i) {
        LockscreenWallpaper lockscreenWallpaper = this.mLockscreenWallpaper;
        if (lockscreenWallpaper != null) {
            lockscreenWallpaper.setCurrentUser(i);
        }
        this.mScrimController.setCurrentUser(i);
        if (this.mWallpaperSupported) {
            this.mWallpaperChangedReceiver.onReceive(this.mContext, null);
        }
    }

    void updateResources() {
        QSPanelController qSPanelController = this.mQSPanelController;
        if (qSPanelController != null) {
            qSPanelController.updateResources();
        }
        StatusBarWindowController statusBarWindowController = this.mStatusBarWindowController;
        if (statusBarWindowController != null) {
            statusBarWindowController.refreshStatusBarHeight();
        }
        PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
        if (phoneStatusBarView != null) {
            phoneStatusBarView.updateResources();
        }
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        if (notificationPanelViewController != null) {
            notificationPanelViewController.updateResources();
        }
        BrightnessMirrorController brightnessMirrorController = this.mBrightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.updateResources();
        }
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager != null) {
            statusBarKeyguardViewManager.updateResources();
        }
        this.mPowerButtonReveal = new PowerButtonReveal(this.mContext.getResources().getDimensionPixelSize(R$dimen.physical_power_button_center_screen_location_y));
    }

    protected void handleVisibleToUserChanged(boolean z) {
        if (z) {
            handleVisibleToUserChangedImpl(z);
            this.mNotificationLogger.startNotificationLogging();
            return;
        }
        this.mNotificationLogger.stopNotificationLogging();
        handleVisibleToUserChangedImpl(z);
    }

    void handleVisibleToUserChangedImpl(boolean z) {
        int i;
        if (z) {
            boolean hasPinnedHeadsUp = this.mHeadsUpManager.hasPinnedHeadsUp();
            final int i2 = 1;
            final boolean z2 = !this.mPresenter.isPresenterFullyCollapsed() && ((i = this.mState) == 0 || i == 2);
            int activeNotificationsCount = this.mNotificationsController.getActiveNotificationsCount();
            if (!hasPinnedHeadsUp || !this.mPresenter.isPresenterFullyCollapsed()) {
                i2 = activeNotificationsCount;
            }
            this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBar.this.lambda$handleVisibleToUserChangedImpl$22(z2, i2);
                }
            });
            return;
        }
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$handleVisibleToUserChangedImpl$23();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleVisibleToUserChangedImpl$22(boolean z, int i) {
        try {
            this.mBarService.onPanelRevealed(z, i);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleVisibleToUserChangedImpl$23() {
        try {
            this.mBarService.onPanelHidden();
        } catch (RemoteException unused) {
        }
    }

    /* JADX WARN: Type inference failed for: r9v0, types: [boolean, int] */
    private void logStateToEventlog() {
        boolean isShowing = this.mStatusBarKeyguardViewManager.isShowing();
        boolean isOccluded = this.mStatusBarKeyguardViewManager.isOccluded();
        boolean isBouncerShowing = this.mStatusBarKeyguardViewManager.isBouncerShowing();
        ?? isMethodSecure = this.mKeyguardStateController.isMethodSecure();
        boolean canDismissLockScreen = this.mKeyguardStateController.canDismissLockScreen();
        int loggingFingerprint = getLoggingFingerprint(this.mState, isShowing, isOccluded, isBouncerShowing, isMethodSecure, canDismissLockScreen);
        if (loggingFingerprint != this.mLastLoggedStateFingerprint) {
            if (this.mStatusBarStateLog == null) {
                this.mStatusBarStateLog = new LogMaker(0);
            }
            this.mMetricsLogger.write(this.mStatusBarStateLog.setCategory(isBouncerShowing ? 197 : 196).setType(isShowing ? 1 : 2).setSubtype(isMethodSecure == true ? 1 : 0));
            EventLogTags.writeSysuiStatusBarState(this.mState, isShowing ? 1 : 0, isOccluded ? 1 : 0, isBouncerShowing ? 1 : 0, isMethodSecure, canDismissLockScreen ? 1 : 0);
            this.mLastLoggedStateFingerprint = loggingFingerprint;
            StringBuilder sb = new StringBuilder();
            sb.append(isBouncerShowing ? "BOUNCER" : "LOCKSCREEN");
            sb.append(isShowing ? "_OPEN" : "_CLOSE");
            sb.append(isMethodSecure != 0 ? "_SECURE" : "_INSECURE");
            sUiEventLogger.log(StatusBarUiEvent.valueOf(sb.toString()));
        }
    }

    void vibrate() {
        ((Vibrator) this.mContext.getSystemService("vibrator")).vibrate(250L, VIBRATION_ATTRIBUTES);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$24() {
        Debug.stopMethodTracing();
        Log.d("StatusBar", "stopTracing");
        vibrate();
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postQSRunnableDismissingKeyguard(final Runnable runnable) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$postQSRunnableDismissingKeyguard$26(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postQSRunnableDismissingKeyguard$26(final Runnable runnable) {
        this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
        executeRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$postQSRunnableDismissingKeyguard$25(runnable);
            }
        }, null, false, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postQSRunnableDismissingKeyguard$25(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(PendingIntent pendingIntent) {
        postStartActivityDismissingKeyguard(pendingIntent, (ActivityLaunchAnimator.Controller) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postStartActivityDismissingKeyguard$27(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller) {
        startPendingIntentDismissingKeyguard(pendingIntent, (Runnable) null, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(final PendingIntent pendingIntent, final ActivityLaunchAnimator.Controller controller) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$postStartActivityDismissingKeyguard$27(pendingIntent, controller);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(Intent intent, int i) {
        postStartActivityDismissingKeyguard(intent, i, null);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(final Intent intent, int i, final ActivityLaunchAnimator.Controller controller) {
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$postStartActivityDismissingKeyguard$28(intent, controller);
            }
        }, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postStartActivityDismissingKeyguard$28(Intent intent, ActivityLaunchAnimator.Controller controller) {
        startActivityDismissingKeyguard(intent, true, true, false, null, 0, controller);
    }

    @Override // com.android.systemui.demomode.DemoMode
    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("bars");
        arrayList.add("clock");
        arrayList.add("operator");
        return arrayList;
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void onDemoModeStarted() {
        dispatchDemoModeStartedToView(R$id.clock);
        dispatchDemoModeStartedToView(R$id.operator_name);
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void onDemoModeFinished() {
        dispatchDemoModeFinishedToView(R$id.clock);
        dispatchDemoModeFinishedToView(R$id.operator_name);
        checkBarModes();
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void dispatchDemoCommand(String str, Bundle bundle) {
        int i;
        if (str.equals("clock")) {
            dispatchDemoCommandToView(str, bundle, R$id.clock);
        }
        if (str.equals("bars")) {
            String string = bundle.getString("mode");
            if ("opaque".equals(string)) {
                i = 4;
            } else if ("translucent".equals(string)) {
                i = 2;
            } else if ("semi-transparent".equals(string)) {
                i = 1;
            } else if ("transparent".equals(string)) {
                i = 0;
            } else {
                i = "warning".equals(string) ? 5 : -1;
            }
            if (i != -1) {
                if (this.mNotificationShadeWindowController != null && this.mNotificationShadeWindowViewController.getBarTransitions() != null) {
                    this.mNotificationShadeWindowViewController.getBarTransitions().transitionTo(i, true);
                }
                this.mNavigationBarController.transitionTo(this.mDisplayId, i, true);
            }
        }
        if (str.equals("operator")) {
            dispatchDemoCommandToView(str, bundle, R$id.operator_name);
        }
    }

    private void dispatchDemoCommandToView(String str, Bundle bundle, int i) {
        PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
        if (phoneStatusBarView == null) {
            return;
        }
        View findViewById = phoneStatusBarView.findViewById(i);
        if (!(findViewById instanceof DemoModeCommandReceiver)) {
            return;
        }
        ((DemoModeCommandReceiver) findViewById).dispatchDemoCommand(str, bundle);
    }

    private void dispatchDemoModeStartedToView(int i) {
        PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
        if (phoneStatusBarView == null) {
            return;
        }
        View findViewById = phoneStatusBarView.findViewById(i);
        if (!(findViewById instanceof DemoModeCommandReceiver)) {
            return;
        }
        ((DemoModeCommandReceiver) findViewById).onDemoModeStarted();
    }

    private void dispatchDemoModeFinishedToView(int i) {
        PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
        if (phoneStatusBarView == null) {
            return;
        }
        View findViewById = phoneStatusBarView.findViewById(i);
        if (!(findViewById instanceof DemoModeCommandReceiver)) {
            return;
        }
        ((DemoModeCommandReceiver) findViewById).onDemoModeFinished();
    }

    public void showKeyguard() {
        this.mStatusBarStateController.setKeyguardRequested(true);
        this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(false);
        updateIsKeyguard();
        this.mAssistManagerLazy.get().onLockscreenShown();
    }

    public boolean hideKeyguard() {
        this.mStatusBarStateController.setKeyguardRequested(false);
        return updateIsKeyguard();
    }

    public boolean isFullScreenUserSwitcherState() {
        return this.mState == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateIsKeyguard() {
        return updateIsKeyguard(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateIsKeyguard(boolean z) {
        boolean z2 = true;
        boolean z3 = this.mBiometricUnlockController.getMode() == 1;
        boolean z4 = this.mDozeServiceHost.getDozingRequested() && (!this.mDeviceInteractive || (isGoingToSleep() && (isScreenFullyOff() || this.mIsKeyguard)));
        if ((!this.mStatusBarStateController.isKeyguardRequested() && !z4) || z3) {
            z2 = false;
        }
        if (z4) {
            updatePanelExpansionForKeyguard();
        }
        if (z2) {
            if (!this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying() && (!isGoingToSleep() || this.mScreenLifecycle.getScreenState() != 3)) {
                showKeyguardImpl();
            }
            return false;
        }
        return hideKeyguardImpl(z);
    }

    public void showKeyguardImpl() {
        this.mIsKeyguard = true;
        if (this.mKeyguardStateController.isLaunchTransitionFadingAway()) {
            this.mNotificationPanelViewController.cancelAnimation();
            onLaunchTransitionFadingEnded();
        }
        this.mHandler.removeMessages(1003);
        UserSwitcherController userSwitcherController = this.mUserSwitcherController;
        if (userSwitcherController != null && userSwitcherController.useFullscreenUserSwitcher()) {
            this.mStatusBarStateController.setState(3);
        } else if (!this.mPulseExpansionHandler.isWakingToShadeLocked()) {
            this.mStatusBarStateController.setState(1);
        }
        updatePanelExpansionForKeyguard();
    }

    private void updatePanelExpansionForKeyguard() {
        if (this.mState == 1 && this.mBiometricUnlockController.getMode() != 1 && !this.mBouncerShowing) {
            this.mShadeController.instantExpandNotificationsPanel();
        } else if (this.mState != 3) {
        } else {
            instantCollapseNotificationPanel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLaunchTransitionFadingEnded() {
        this.mNotificationPanelViewController.setAlpha(1.0f);
        this.mNotificationPanelViewController.onAffordanceLaunchEnded();
        releaseGestureWakeLock();
        runLaunchTransitionEndRunnable();
        this.mKeyguardStateController.setLaunchTransitionFadingAway(false);
        this.mPresenter.updateMediaMetaData(true, true);
    }

    public boolean isInLaunchTransition() {
        return this.mNotificationPanelViewController.isLaunchTransitionRunning() || this.mNotificationPanelViewController.isLaunchTransitionFinished();
    }

    public void fadeKeyguardAfterLaunchTransition(final Runnable runnable, Runnable runnable2) {
        this.mHandler.removeMessages(1003);
        this.mLaunchTransitionEndRunnable = runnable2;
        Runnable runnable3 = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$fadeKeyguardAfterLaunchTransition$29(runnable);
            }
        };
        if (this.mNotificationPanelViewController.isLaunchTransitionRunning()) {
            this.mNotificationPanelViewController.setLaunchTransitionEndRunnable(runnable3);
        } else {
            runnable3.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeKeyguardAfterLaunchTransition$29(Runnable runnable) {
        this.mKeyguardStateController.setLaunchTransitionFadingAway(true);
        if (runnable != null) {
            runnable.run();
        }
        updateScrimController();
        this.mPresenter.updateMediaMetaData(false, true);
        this.mNotificationPanelViewController.setAlpha(1.0f);
        this.mNotificationPanelViewController.fadeOut(100L, 300L, new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.onLaunchTransitionFadingEnded();
            }
        });
        this.mCommandQueue.appTransitionStarting(this.mDisplayId, SystemClock.uptimeMillis(), 120L, true);
    }

    public void fadeKeyguardWhilePulsing() {
        this.mNotificationPanelViewController.fadeOut(0L, 96L, new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$fadeKeyguardWhilePulsing$30();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeKeyguardWhilePulsing$30() {
        hideKeyguard();
        this.mStatusBarKeyguardViewManager.onKeyguardFadedAway();
    }

    public void animateKeyguardUnoccluding() {
        this.mNotificationPanelViewController.setExpandedFraction(0.0f);
        animateExpandNotificationsPanel();
        this.mScrimController.setUnocclusionAnimationRunning(true);
    }

    public void startLaunchTransitionTimeout() {
        this.mHandler.sendEmptyMessageDelayed(1003, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLaunchTransitionTimeout() {
        Log.w("StatusBar", "Launch transition: Timeout!");
        this.mNotificationPanelViewController.onAffordanceLaunchEnded();
        releaseGestureWakeLock();
        this.mNotificationPanelViewController.resetViews(false);
    }

    private void runLaunchTransitionEndRunnable() {
        Runnable runnable = this.mLaunchTransitionEndRunnable;
        if (runnable != null) {
            this.mLaunchTransitionEndRunnable = null;
            runnable.run();
        }
    }

    public boolean hideKeyguardImpl(boolean z) {
        this.mIsKeyguard = false;
        Trace.beginSection("StatusBar#hideKeyguard");
        boolean leaveOpenOnKeyguardHide = this.mStatusBarStateController.leaveOpenOnKeyguardHide();
        int state = this.mStatusBarStateController.getState();
        if (!this.mStatusBarStateController.setState(0, z)) {
            this.mLockscreenUserManager.updatePublicMode();
        }
        if (this.mStatusBarStateController.leaveOpenOnKeyguardHide()) {
            if (!this.mStatusBarStateController.isKeyguardRequested()) {
                this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(false);
            }
            long calculateGoingToFullShadeDelay = this.mKeyguardStateController.calculateGoingToFullShadeDelay();
            this.mLockscreenShadeTransitionController.onHideKeyguard(calculateGoingToFullShadeDelay, state);
            this.mNavigationBarController.disableAnimationsDuringHide(this.mDisplayId, calculateGoingToFullShadeDelay);
        } else if (!this.mNotificationPanelViewController.isCollapsing()) {
            instantCollapseNotificationPanel();
        }
        QSPanelController qSPanelController = this.mQSPanelController;
        if (qSPanelController != null) {
            qSPanelController.refreshAllTiles();
        }
        this.mHandler.removeMessages(1003);
        releaseGestureWakeLock();
        this.mNotificationPanelViewController.onAffordanceLaunchEnded();
        this.mNotificationPanelViewController.cancelAnimation();
        this.mNotificationPanelViewController.setAlpha(1.0f);
        this.mNotificationPanelViewController.resetViewGroupFade();
        updateDozingState();
        updateScrimController();
        Trace.endSection();
        return leaveOpenOnKeyguardHide;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseGestureWakeLock() {
        if (this.mGestureWakeLock.isHeld()) {
            this.mGestureWakeLock.release();
        }
    }

    public void keyguardGoingAway() {
        this.mKeyguardStateController.notifyKeyguardGoingAway(true);
        this.mCommandQueue.appTransitionPending(this.mDisplayId, true);
    }

    public void setKeyguardFadingAway(long j, long j2, long j3, boolean z) {
        this.mCommandQueue.appTransitionStarting(this.mDisplayId, (j + j3) - 120, 120L, true);
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, j3 > 0);
        this.mCommandQueue.appTransitionStarting(this.mDisplayId, j - 120, 120L, true);
        this.mKeyguardStateController.notifyKeyguardFadingAway(j2, j3, z);
    }

    public void finishKeyguardFadingAway() {
        this.mKeyguardStateController.notifyKeyguardDoneFading();
        this.mScrimController.setExpansionAffectsAlpha(true);
    }

    protected void updateTheme() {
        int i;
        if (this.mColorExtractor.getNeutralColors().supportsDarkText()) {
            i = R$style.Theme_SystemUI_LightWallpaper;
        } else {
            i = R$style.Theme_SystemUI;
        }
        if (this.mContext.getThemeResId() != i) {
            this.mContext.setTheme(i);
            this.mConfigurationController.notifyThemeChanged();
        }
    }

    private void updateDozingState() {
        Trace.traceCounter(4096L, "dozing", this.mDozing ? 1 : 0);
        Trace.beginSection("StatusBar#updateDozingState");
        boolean z = true;
        boolean z2 = ((!this.mStatusBarKeyguardViewManager.isShowing() || this.mStatusBarKeyguardViewManager.isOccluded()) ? null : 1) != null || (this.mDozing && this.mDozeParameters.shouldControlUnlockedScreenOff());
        boolean isWakeAndUnlock = this.mBiometricUnlockController.isWakeAndUnlock();
        Log.d("StatusBar", "mDozing = " + this.mDozing + " shouldAnimateWakeup = " + this.mDozeServiceHost.shouldAnimateWakeup() + " wakeAndUnlock = " + isWakeAndUnlock);
        Log.d("StatusBar", "mDozing = " + this.mDozing + " shouldAnimateScreenOff = " + this.mDozeServiceHost.shouldAnimateScreenOff() + " visibleNotOccludedOrWillBe = " + z2);
        if ((this.mDozing || !this.mDozeServiceHost.shouldAnimateWakeup() || isWakeAndUnlock) && (!this.mDozing || !this.mDozeServiceHost.shouldAnimateScreenOff() || !z2)) {
            z = false;
        }
        this.mNotificationPanelViewController.setDozing(this.mDozing, z, this.mWakeUpTouchLocation);
        updateQsExpansionEnabled();
        Trace.endSection();
    }

    public void userActivity() {
        if (this.mState == 1) {
            this.mKeyguardViewMediatorCallback.userActivity();
        }
    }

    public boolean interceptMediaKey(KeyEvent keyEvent) {
        return this.mState == 1 && this.mStatusBarKeyguardViewManager.interceptMediaKey(keyEvent);
    }

    public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 4 && this.mState == 1 && this.mStatusBarKeyguardViewManager.dispatchBackKeyEventPreIme()) {
            return onBackPressed();
        }
        return false;
    }

    protected boolean shouldUnlockOnMenuPressed() {
        return this.mDeviceInteractive && this.mState != 0 && this.mStatusBarKeyguardViewManager.shouldDismissOnMenuPressed();
    }

    public boolean onMenuPressed() {
        if (shouldUnlockOnMenuPressed()) {
            this.mShadeController.animateCollapsePanels(2, true);
            return true;
        }
        return false;
    }

    public void endAffordanceLaunch() {
        releaseGestureWakeLock();
        this.mNotificationPanelViewController.onAffordanceLaunchEnded();
    }

    public boolean onBackPressed() {
        boolean z = this.mScrimController.getState() == ScrimState.BOUNCER_SCRIMMED;
        if (this.mStatusBarKeyguardViewManager.onBackPressed(z)) {
            if (z) {
                this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(false);
            } else {
                this.mNotificationPanelViewController.expandWithoutQs();
            }
            return true;
        } else if (!SystemUIUtils.getInstance().shouldUseSplitNotificationShade() && this.mNotificationPanelViewController.isQsExpanded()) {
            if (this.mNotificationPanelViewController.isQsDetailShowing()) {
                this.mNotificationPanelViewController.closeQsDetail();
            } else {
                this.mNotificationPanelViewController.animateCloseQs(false);
            }
            return true;
        } else if (this.mNotificationPanelViewController.closeUserSwitcherIfOpen()) {
            return true;
        } else {
            int i = this.mState;
            if (i == 1 || i == 2) {
                return false;
            }
            if (this.mNotificationPanelViewController.canPanelBeCollapsed()) {
                this.mShadeController.animateCollapsePanels();
            }
            return true;
        }
    }

    public boolean onSpacePressed() {
        if (!this.mDeviceInteractive || this.mState == 0) {
            return false;
        }
        this.mShadeController.animateCollapsePanels(2, true);
        return true;
    }

    private void showBouncerIfKeyguard() {
        if (!this.mKeyguardViewMediator.isHiding()) {
            if (this.mState == 1 && !this.mStatusBarKeyguardViewManager.bouncerIsOrWillBeShowing()) {
                this.mStatusBarKeyguardViewManager.showGenericBouncer(true);
            } else if (this.mState != 2) {
            } else {
                this.mStatusBarKeyguardViewManager.showBouncer(true);
            }
        }
    }

    public void showBouncerWithDimissAndCancelIfKeyguard(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable) {
        int i = this.mState;
        if ((i == 1 || i == 2) && !this.mKeyguardViewMediator.isHiding()) {
            this.mStatusBarKeyguardViewManager.dismissWithAction(onDismissAction, runnable, false);
        } else if (runnable == null) {
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void instantCollapseNotificationPanel() {
        this.mNotificationPanelViewController.instantCollapse();
        this.mShadeController.runPostCollapseRunnables();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collapsePanelOnMainThread() {
        if (Looper.getMainLooper().isCurrentThread()) {
            this.mShadeController.collapsePanel();
            return;
        }
        Executor mainExecutor = this.mContext.getMainExecutor();
        ShadeController shadeController = this.mShadeController;
        Objects.requireNonNull(shadeController);
        mainExecutor.execute(new StatusBar$$ExternalSyntheticLambda11(shadeController));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void collapsePanelWithDuration(int i) {
        this.mNotificationPanelViewController.collapseWithDuration(i);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStatePreChange(int i, int i2) {
        if (this.mVisible && (i2 == 2 || this.mStatusBarStateController.goingToFullShade())) {
            clearNotificationEffects();
        }
        if (i2 == 1) {
            this.mRemoteInputManager.onPanelCollapsed();
            maybeEscalateHeadsUp();
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        PhoneStatusBarView phoneStatusBarView;
        this.mState = i;
        updateReportRejectedTouchVisibility();
        this.mDozeServiceHost.updateDozing();
        updateTheme();
        this.mNavigationBarController.touchAutoDim(this.mDisplayId);
        Trace.beginSection("StatusBar#updateKeyguardState");
        boolean z = true;
        if (this.mState == 1 && (phoneStatusBarView = this.mStatusBarView) != null) {
            phoneStatusBarView.removePendingHideExpandedRunnables();
        }
        updateDozingState();
        checkBarModes();
        updateScrimController();
        StatusBarNotificationPresenter statusBarNotificationPresenter = this.mPresenter;
        if (this.mState == 1) {
            z = false;
        }
        statusBarNotificationPresenter.updateMediaMetaData(false, z);
        updateKeyguardState();
        Trace.endSection();
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozeAmountChanged(float f, float f2) {
        if (!this.mFeatureFlags.useNewLockscreenAnimations() || this.mBiometricUnlockController.isWakeAndUnlock()) {
            return;
        }
        this.mLightRevealScrim.setRevealAmount(1.0f - f);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        Trace.beginSection("StatusBar#updateDozing");
        this.mDozing = z;
        this.mNotificationPanelViewController.resetViews(this.mDozeServiceHost.getDozingRequested() && this.mDozeParameters.shouldControlScreenOff());
        updateQsExpansionEnabled();
        this.mKeyguardViewMediator.setDozing(this.mDozing);
        this.mNotificationsController.requestNotificationUpdate("onDozingChanged");
        updateDozingState();
        this.mDozeServiceHost.updateDozing();
        updateScrimController();
        updateReportRejectedTouchVisibility();
        Trace.endSection();
        if (z) {
            if (!((AODController) Dependency.get(AODController.class)).shouldShowAODView()) {
                setNotificationPanelViewAlpha(0.0f);
            } else {
                setNotificationPanelViewAlpha(1.0f);
            }
        } else if (this.mState == 0 || isWakeAndUnlock()) {
        } else {
            setNotificationPanelViewAlpha(1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRevealEffect(boolean z) {
        float f;
        float f2;
        if (this.mLightRevealScrim == null) {
            return;
        }
        Log.d("StatusBar", " wake reason = " + this.mWakefulnessLifecycle.getLastWakeReason());
        int lastWakeReason = this.mWakefulnessLifecycle.getLastWakeReason();
        int lastSleepReason = this.mWakefulnessLifecycle.getLastSleepReason();
        if (lastWakeReason == 6) {
            if (this.mWakeUpComingFromTouch) {
                PointF pointF = this.mWakeUpTouchLocation;
                f = pointF.x;
                f2 = pointF.y;
            } else {
                Point tapXY = getTapXY();
                f = tapXY.x;
                f2 = tapXY.y;
            }
            Log.d("StatusBar", "updateRevealEffect:  x= " + f + " y = " + f2);
            this.mCircleReveal = new CircleReveal(f, f2, 0.0f, Math.max(Math.max(f, getDisplayWidth() - f), Math.max(f2, getDisplayHeight() - f2)));
        }
        if (!z) {
            if (z) {
                return;
            }
            if (lastSleepReason == 4) {
                this.mLightRevealScrim.setRevealEffect(this.mPowerButtonReveal);
            } else {
                this.mLightRevealScrim.setRevealEffect(LiftReveal.INSTANCE);
            }
        } else if (lastWakeReason == 1) {
            this.mLightRevealScrim.setRevealEffect(this.mPowerButtonReveal);
        } else if (lastWakeReason == 6) {
            this.mIsTapWake = false;
            this.mLightRevealScrim.setRevealEffect(this.mCircleReveal);
        } else if (lastWakeReason == 4) {
            this.mLightRevealScrim.setRevealEffect(LiftReveal.INSTANCE);
        } else {
            this.mLightRevealScrim.setRevealEffect(LiftReveal.INSTANCE);
        }
    }

    public LightRevealScrim getLightRevealScrim() {
        return this.mLightRevealScrim;
    }

    private void updateKeyguardState() {
        this.mKeyguardStateController.notifyKeyguardState(this.mStatusBarKeyguardViewManager.isShowing(), this.mStatusBarKeyguardViewManager.isOccluded());
    }

    public void onTrackingStarted() {
        this.mShadeController.runPostCollapseRunnables();
    }

    public void onClosingFinished() {
        this.mShadeController.runPostCollapseRunnables();
        if (!this.mPresenter.isPresenterFullyCollapsed()) {
            this.mNotificationShadeWindowController.setNotificationShadeFocusable(true);
        }
    }

    public void onUnlockHintStarted() {
        this.mFalsingCollector.onUnlockHintStarted();
        this.mKeyguardIndicationController.showActionToUnlock();
    }

    public void onHintFinished() {
        this.mKeyguardIndicationController.hideTransientIndicationDelayed(1200L);
    }

    public void onCameraHintStarted() {
        this.mFalsingCollector.onCameraHintStarted();
        this.mKeyguardIndicationController.showTransientIndication(R$string.camera_hint);
    }

    public void onVoiceAssistHintStarted() {
        this.mFalsingCollector.onLeftAffordanceHintStarted();
        this.mKeyguardIndicationController.showTransientIndication(R$string.voice_hint);
    }

    public void onPhoneHintStarted() {
        this.mFalsingCollector.onLeftAffordanceHintStarted();
        this.mKeyguardIndicationController.showTransientIndication(R$string.phone_hint);
    }

    public void onTrackingStopped(boolean z) {
        int i = this.mState;
        if ((i == 1 || i == 2) && !z && !this.mKeyguardStateController.canDismissLockScreen()) {
            this.mStatusBarKeyguardViewManager.showBouncer(false);
        }
    }

    public NavigationBarView getNavigationBarView() {
        return this.mNavigationBarController.getNavigationBarView(this.mDisplayId);
    }

    public void setBouncerShowing(boolean z) {
        this.mBouncerShowing = z;
        this.mKeyguardBypassController.setBouncerShowing(z);
        this.mPulseExpansionHandler.setBouncerShowing(z);
        PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
        if (phoneStatusBarView != null) {
            phoneStatusBarView.setBouncerShowing(z);
        }
        updateHideIconsForBouncer(true);
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, true);
        updateScrimController();
        if (!this.mBouncerShowing) {
            updatePanelExpansionForKeyguard();
        }
    }

    public void collapseShade() {
        if (this.mNotificationPanelViewController.isTracking()) {
            this.mNotificationShadeWindowViewController.cancelCurrentTouch();
        }
        if (!this.mPanelExpanded || this.mState != 0) {
            return;
        }
        this.mShadeController.animateCollapsePanels();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$16  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass16 implements WakefulnessLifecycle.Observer {
        AnonymousClass16() {
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public void onFinishedGoingToSleep() {
            StatusBar.this.mNotificationPanelViewController.onAffordanceLaunchEnded();
            StatusBar.this.releaseGestureWakeLock();
            StatusBar.this.mLaunchCameraWhenFinishedWaking = false;
            StatusBar statusBar = StatusBar.this;
            statusBar.mDeviceInteractive = false;
            statusBar.mWakeUpComingFromTouch = false;
            StatusBar.this.mWakeUpTouchLocation = null;
            StatusBar.this.updateVisibleToUser();
            StatusBar.this.updateNotificationPanelTouchState();
            StatusBar.this.mNotificationShadeWindowViewController.cancelCurrentTouch();
            if (StatusBar.this.mLaunchCameraOnFinishedGoingToSleep) {
                StatusBar.this.mLaunchCameraOnFinishedGoingToSleep = false;
                StatusBar.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$16$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        StatusBar.AnonymousClass16.this.lambda$onFinishedGoingToSleep$0();
                    }
                });
            }
            if (StatusBar.this.mLaunchEmergencyActionOnFinishedGoingToSleep) {
                StatusBar.this.mLaunchEmergencyActionOnFinishedGoingToSleep = false;
                StatusBar.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$16$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        StatusBar.AnonymousClass16.this.lambda$onFinishedGoingToSleep$1();
                    }
                });
            }
            StatusBar.this.updateIsKeyguard();
            StatusBar statusBar2 = StatusBar.this;
            if (statusBar2.mLiftWakeGestureController != null && statusBar2.shouldEnableProximity()) {
                StatusBar.this.mLiftWakeGestureController.requestProximityTrigger();
            }
            StatusBar statusBar3 = StatusBar.this;
            if (statusBar3.mLiftWakeGestureController != null && statusBar3.shouldEnableLiftGestureLp()) {
                StatusBar.this.mLiftWakeGestureController.requestWakeUpTrigger();
            }
            StatusBar statusBar4 = StatusBar.this;
            if (statusBar4.mLiftWakeGestureController == null || !statusBar4.shouldEnableMotionGestureLp()) {
                return;
            }
            StatusBar.this.mLiftWakeGestureController.requestMotionTrigger();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFinishedGoingToSleep$0() {
            StatusBar statusBar = StatusBar.this;
            statusBar.onCameraLaunchGestureDetected(statusBar.mLastCameraLaunchSource);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFinishedGoingToSleep$1() {
            StatusBar.this.onEmergencyActionLaunchGestureDetected();
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public void onStartedGoingToSleep() {
            StatusBar.this.mIsTapWake = false;
            DejankUtils.startDetectingBlockingIpcs("StatusBar#onStartedGoingToSleep");
            StatusBar.this.updateRevealEffect(false);
            StatusBar.this.updateNotificationPanelTouchState();
            StatusBar.this.notifyHeadsUpGoingToSleep();
            StatusBar.this.dismissVolumeDialog();
            StatusBar.this.mWakeUpCoordinator.setFullyAwake(false);
            StatusBar.this.mBypassHeadsUpNotifier.setFullyAwake(false);
            StatusBar.this.mKeyguardBypassController.onStartedGoingToSleep();
            if (StatusBar.this.mDozeParameters.shouldControlUnlockedScreenOff()) {
                StatusBar.this.makeExpandedVisible(true);
            }
            DejankUtils.stopDetectingBlockingIpcs("StatusBar#onStartedGoingToSleep");
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public void onStartedWakingUp() {
            LiftWakeGestureController liftWakeGestureController = StatusBar.this.mLiftWakeGestureController;
            if (liftWakeGestureController != null) {
                liftWakeGestureController.cancelProximityTrigger();
                StatusBar.this.mLiftWakeGestureController.cancelWakeUpTrigger();
                StatusBar.this.mLiftWakeGestureController.cancelMotionTrigger();
            }
            DejankUtils.startDetectingBlockingIpcs("StatusBar#onStartedWakingUp");
            StatusBar statusBar = StatusBar.this;
            statusBar.mDeviceInteractive = true;
            statusBar.mWakeUpCoordinator.setWakingUp(true);
            if (!StatusBar.this.mKeyguardBypassController.getBypassEnabled()) {
                StatusBar.this.mHeadsUpManager.releaseAllImmediately();
            }
            StatusBar.this.updateVisibleToUser();
            StatusBar.this.updateIsKeyguard();
            StatusBar.this.mDozeServiceHost.stopDozing();
            StatusBar.this.updateRevealEffect(true);
            StatusBar.this.updateNotificationPanelTouchState();
            StatusBar.this.mPulseExpansionHandler.onStartedWakingUp();
            if (StatusBar.this.mUnlockedScreenOffAnimationController.isScreenOffLightRevealAnimationPlaying()) {
                StatusBar.this.makeExpandedInvisible();
            }
            DejankUtils.stopDetectingBlockingIpcs("StatusBar#onStartedWakingUp");
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public void onFinishedWakingUp() {
            StatusBar.this.mWakeUpCoordinator.setFullyAwake(true);
            StatusBar.this.mBypassHeadsUpNotifier.setFullyAwake(true);
            StatusBar.this.mWakeUpCoordinator.setWakingUp(false);
            if (StatusBar.this.mLaunchCameraWhenFinishedWaking) {
                StatusBar statusBar = StatusBar.this;
                statusBar.mNotificationPanelViewController.launchCamera(false, statusBar.mLastCameraLaunchSource);
                StatusBar.this.mLaunchCameraWhenFinishedWaking = false;
            }
            if (StatusBar.this.mLaunchEmergencyActionWhenFinishedWaking) {
                StatusBar.this.mLaunchEmergencyActionWhenFinishedWaking = false;
                Intent emergencyActionIntent = StatusBar.this.getEmergencyActionIntent();
                if (emergencyActionIntent != null) {
                    ((SystemUI) StatusBar.this).mContext.startActivityAsUser(emergencyActionIntent, UserHandle.CURRENT);
                }
            }
            StatusBar.this.updateScrimController();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateNotificationPanelTouchState() {
        boolean z = false;
        boolean z2 = isGoingToSleep() && !this.mDozeParameters.shouldControlScreenOff();
        if ((!this.mDeviceInteractive && !this.mDozeServiceHost.isPulsing()) || z2) {
            z = true;
        }
        this.mNotificationPanelViewController.setTouchAndAnimationDisabled(z);
        this.mNotificationIconAreaController.setAnimationsEnabled(!z);
    }

    private void vibrateForCameraGesture() {
        this.mVibrator.vibrate(VibrationEffect.get(5));
    }

    private static VibrationEffect getCameraGestureVibrationEffect(Vibrator vibrator, Resources resources) {
        if (vibrator.areAllPrimitivesSupported(4, 1)) {
            return VibrationEffect.startComposition().addPrimitive(4).addPrimitive(1, 1.0f, 50).compose();
        }
        if (vibrator.hasAmplitudeControl()) {
            return VibrationEffect.createWaveform(CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS, CAMERA_LAUNCH_GESTURE_VIBRATION_AMPLITUDES, -1);
        }
        int[] intArray = resources.getIntArray(R$array.config_cameraLaunchGestureVibePattern);
        long[] jArr = new long[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            jArr[i] = intArray[i];
        }
        return VibrationEffect.createWaveform(jArr, -1);
    }

    public boolean isScreenFullyOff() {
        return this.mScreenLifecycle.getScreenState() == 0;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showScreenPinningRequest(int i) {
        if (this.mKeyguardStateController.isShowing()) {
            return;
        }
        showScreenPinningRequest(i, true);
    }

    public void showScreenPinningRequest(int i, boolean z) {
        this.mScreenPinningRequest.showPrompt(i, z);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void appTransitionCancelled(int i) {
        if (i == this.mDisplayId) {
            this.mSplitScreenOptional.ifPresent(StatusBar$$ExternalSyntheticLambda39.INSTANCE);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void appTransitionFinished(int i) {
        if (i == this.mDisplayId) {
            this.mSplitScreenOptional.ifPresent(StatusBar$$ExternalSyntheticLambda38.INSTANCE);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onCameraLaunchGestureDetected(int i) {
        this.mLastCameraLaunchSource = i;
        if (isGoingToSleep()) {
            this.mLaunchCameraOnFinishedGoingToSleep = true;
        } else if (!this.mNotificationPanelViewController.canCameraGestureBeLaunched()) {
        } else {
            if (!this.mDeviceInteractive) {
                this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 5, "com.android.systemui:CAMERA_GESTURE");
            }
            vibrateForCameraGesture();
            if (i == 1) {
                Log.v("StatusBar", "Camera launch");
                this.mKeyguardUpdateMonitor.onCameraLaunched();
            }
            if (!this.mStatusBarKeyguardViewManager.isShowing()) {
                Intent insecureCameraIntent = CameraIntents.getInsecureCameraIntent(this.mContext);
                insecureCameraIntent.setPackage("com.nothing.camera");
                startActivityDismissingKeyguard(insecureCameraIntent, false, true, true, null, 0, null);
                return;
            }
            if (!this.mDeviceInteractive) {
                this.mGestureWakeLock.acquire(6000L);
            }
            if (isWakingUpOrAwake()) {
                if (this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    this.mStatusBarKeyguardViewManager.reset(true);
                }
                this.mNotificationPanelViewController.launchCamera(this.mDeviceInteractive, i);
                updateScrimController();
                return;
            }
            this.mLaunchCameraWhenFinishedWaking = true;
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onEmergencyActionLaunchGestureDetected() {
        Intent emergencyActionIntent = getEmergencyActionIntent();
        if (emergencyActionIntent == null) {
            Log.wtf("StatusBar", "Couldn't find an app to process the emergency intent.");
        } else if (isGoingToSleep()) {
            this.mLaunchEmergencyActionOnFinishedGoingToSleep = true;
        } else {
            if (!this.mDeviceInteractive) {
                this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:EMERGENCY_GESTURE");
            }
            if (!this.mStatusBarKeyguardViewManager.isShowing()) {
                startActivityDismissingKeyguard(emergencyActionIntent, false, true, true, null, 0, null);
                return;
            }
            if (!this.mDeviceInteractive) {
                this.mGestureWakeLock.acquire(6000L);
            }
            if (isWakingUpOrAwake()) {
                if (this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    this.mStatusBarKeyguardViewManager.reset(true);
                }
                this.mContext.startActivityAsUser(emergencyActionIntent, UserHandle.CURRENT);
                return;
            }
            this.mLaunchEmergencyActionWhenFinishedWaking = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent getEmergencyActionIntent() {
        Intent intent = new Intent("com.android.systemui.action.LAUNCH_EMERGENCY");
        ResolveInfo topEmergencySosInfo = getTopEmergencySosInfo(this.mContext.getPackageManager().queryIntentActivities(intent, 1048576));
        if (topEmergencySosInfo == null) {
            Log.wtf("StatusBar", "Couldn't find an app to process the emergency intent.");
            return null;
        }
        ActivityInfo activityInfo = topEmergencySosInfo.activityInfo;
        intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
        intent.setFlags(268435456);
        return intent;
    }

    private ResolveInfo getTopEmergencySosInfo(List<ResolveInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        String string = this.mContext.getString(R$string.config_preferredEmergencySosPackage);
        if (TextUtils.isEmpty(string)) {
            return list.get(0);
        }
        for (ResolveInfo resolveInfo : list) {
            if (TextUtils.equals(resolveInfo.activityInfo.packageName, string)) {
                return resolveInfo;
            }
        }
        return list.get(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCameraAllowedByAdmin() {
        if (this.mDevicePolicyManager.getCameraDisabled(null, this.mLockscreenUserManager.getCurrentUserId())) {
            return false;
        }
        return (this.mStatusBarKeyguardViewManager != null && (!isKeyguardShowing() || !isKeyguardSecure())) || (this.mDevicePolicyManager.getKeyguardDisabledFeatures(null, this.mLockscreenUserManager.getCurrentUserId()) & 2) == 0;
    }

    private boolean isGoingToSleep() {
        return this.mWakefulnessLifecycle.getWakefulness() == 3;
    }

    private boolean isWakingUpOrAwake() {
        return this.mWakefulnessLifecycle.getWakefulness() == 2 || this.mWakefulnessLifecycle.getWakefulness() == 1;
    }

    public void notifyBiometricAuthModeChanged() {
        this.mDozeServiceHost.updateDozing();
        updateScrimController();
    }

    @VisibleForTesting
    public void updateScrimController() {
        Trace.beginSection("StatusBar#updateScrimController");
        boolean z = false;
        boolean z2 = this.mBiometricUnlockController.isWakeAndUnlock() || this.mKeyguardStateController.isKeyguardFadingAway();
        boolean z3 = this.mKeyguardStateController.isShowing() || this.mKeyguardStateController.isKeyguardFadingAway() || this.mKeyguardStateController.isKeyguardGoingAway();
        ScrimController scrimController = this.mScrimController;
        if (!this.mBiometricUnlockController.isBiometricUnlock() || !z3) {
            z = true;
        }
        scrimController.setExpansionAffectsAlpha(z);
        boolean isLaunchingAffordanceWithPreview = this.mNotificationPanelViewController.isLaunchingAffordanceWithPreview();
        this.mScrimController.setLaunchingAffordanceWithPreview(isLaunchingAffordanceWithPreview);
        if (this.mStatusBarKeyguardViewManager.isShowingAlternateAuth()) {
            this.mScrimController.transitionTo(ScrimState.AUTH_SCRIMMED);
        } else if (this.mBouncerShowing) {
            this.mScrimController.transitionTo(this.mStatusBarKeyguardViewManager.bouncerNeedsScrimming() ? ScrimState.BOUNCER_SCRIMMED : ScrimState.BOUNCER);
        } else if (isLaunchingAffordanceWithPreview) {
            this.mScrimController.transitionTo(ScrimState.UNLOCKED, this.mUnlockScrimCallback);
        } else if (this.mBrightnessMirrorVisible) {
            this.mScrimController.transitionTo(ScrimState.BRIGHTNESS_MIRROR);
        } else if (this.mState == 2) {
            this.mScrimController.transitionTo(ScrimState.SHADE_LOCKED);
        } else if (this.mDozeServiceHost.isPulsing()) {
            this.mScrimController.transitionTo(ScrimState.PULSING, this.mDozeScrimController.getScrimCallback());
        } else if (this.mDozeServiceHost.hasPendingScreenOffCallback()) {
            this.mScrimController.transitionTo(ScrimState.OFF, new ScrimController.Callback() { // from class: com.android.systemui.statusbar.phone.StatusBar.18
                @Override // com.android.systemui.statusbar.phone.ScrimController.Callback
                public void onFinished() {
                    StatusBar.this.mDozeServiceHost.executePendingScreenOffCallback();
                }
            });
        } else if (this.mDozing && !z2) {
            this.mScrimController.transitionTo(ScrimState.AOD);
        } else if (this.mIsKeyguard && !z2) {
            this.mScrimController.transitionTo(ScrimState.KEYGUARD);
        } else if (this.mBubblesOptional.isPresent() && this.mBubblesOptional.get().isStackExpanded()) {
            this.mScrimController.transitionTo(ScrimState.BUBBLE_EXPANDED, this.mUnlockScrimCallback);
        } else {
            this.mScrimController.transitionTo(ScrimState.UNLOCKED, this.mUnlockScrimCallback);
        }
        updateLightRevealScrimVisibility();
        Trace.endSection();
    }

    public boolean isKeyguardShowing() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager == null) {
            Slog.i("StatusBar", "isKeyguardShowing() called before startKeyguard(), returning true");
            return true;
        }
        return statusBarKeyguardViewManager.isShowing();
    }

    public boolean shouldIgnoreTouch() {
        return (this.mStatusBarStateController.isDozing() && this.mDozeServiceHost.getIgnoreTouchWhilePulsing()) || this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying();
    }

    public boolean isDeviceInteractive() {
        return this.mDeviceInteractive;
    }

    public void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        this.mNotificationsController.setNotificationSnoozed(statusBarNotification, snoozeOption);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void toggleSplitScreen() {
        toggleSplitScreenMode(-1, -1);
    }

    public void awakenDreams() {
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$awakenDreams$33();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$awakenDreams$33() {
        try {
            this.mDreamManager.awaken();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void preloadRecentApps() {
        this.mHandler.removeMessages(1022);
        this.mHandler.sendEmptyMessage(1022);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void cancelPreloadRecentApps() {
        this.mHandler.removeMessages(1023);
        this.mHandler.sendEmptyMessage(1023);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void dismissKeyboardShortcutsMenu() {
        this.mHandler.removeMessages(1027);
        this.mHandler.sendEmptyMessage(1027);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void toggleKeyboardShortcutsMenu(int i) {
        this.mHandler.removeMessages(1026);
        this.mHandler.obtainMessage(1026, i, 0).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void setTopAppHidesStatusBar(boolean z) {
        this.mTopHidesStatusBar = z;
        if (!z && this.mWereIconsJustHidden) {
            this.mWereIconsJustHidden = false;
            this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, true);
        }
        updateHideIconsForBouncer(true);
    }

    protected void toggleKeyboardShortcuts(int i) {
        KeyboardShortcuts.toggle(this.mContext, i);
    }

    protected void dismissKeyboardShortcuts() {
        KeyboardShortcuts.dismiss();
    }

    private void executeActionDismissingKeyguard(Runnable runnable, boolean z, boolean z2, boolean z3) {
        if (!this.mDeviceProvisionedController.isDeviceProvisioned()) {
            return;
        }
        dismissKeyguardThenExecute(new AnonymousClass20(runnable, z2, z3), z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$20  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass20 implements ActivityStarter.OnDismissAction {
        final /* synthetic */ Runnable val$action;
        final /* synthetic */ boolean val$collapsePanel;
        final /* synthetic */ boolean val$willAnimateOnKeyguard;

        AnonymousClass20(Runnable runnable, boolean z, boolean z2) {
            this.val$action = runnable;
            this.val$collapsePanel = z;
            this.val$willAnimateOnKeyguard = z2;
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public boolean onDismiss() {
            final Runnable runnable = this.val$action;
            new Thread(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$20$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBar.AnonymousClass20.lambda$onDismiss$0(runnable);
                }
            }).start();
            return this.val$collapsePanel ? StatusBar.this.mShadeController.collapsePanel() : this.val$willAnimateOnKeyguard;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onDismiss$0(Runnable runnable) {
            try {
                ActivityManager.getService().resumeAppSwitches();
            } catch (RemoteException unused) {
            }
            runnable.run();
        }

        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public boolean willRunAnimationOnKeyguard() {
            return this.val$willAnimateOnKeyguard;
        }
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent) {
        startPendingIntentDismissingKeyguard(pendingIntent, null);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable) {
        startPendingIntentDismissingKeyguard(pendingIntent, runnable, (ActivityLaunchAnimator.Controller) null);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, View view) {
        startPendingIntentDismissingKeyguard(pendingIntent, runnable, view instanceof ExpandableNotificationRow ? this.mNotificationAnimationProvider.getAnimatorController((ExpandableNotificationRow) view) : null);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(final PendingIntent pendingIntent, final Runnable runnable, final ActivityLaunchAnimator.Controller controller) {
        boolean z = false;
        boolean z2 = pendingIntent.isActivity() && this.mActivityIntentHelper.wouldLaunchResolverActivity(pendingIntent.getIntent(), this.mLockscreenUserManager.getCurrentUserId());
        if (!z2 && controller != null && shouldAnimateLaunch(pendingIntent.isActivity())) {
            z = true;
        }
        final boolean z3 = !z;
        final boolean z4 = z;
        executeActionDismissingKeyguard(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar.this.lambda$startPendingIntentDismissingKeyguard$35(controller, pendingIntent, z4, z3, runnable);
            }
        }, z2, z3, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$35(ActivityLaunchAnimator.Controller controller, final PendingIntent pendingIntent, boolean z, boolean z2, Runnable runnable) {
        StatusBarLaunchAnimatorController statusBarLaunchAnimatorController;
        if (controller != null) {
            try {
                statusBarLaunchAnimatorController = new StatusBarLaunchAnimatorController(controller, this, pendingIntent.isActivity());
            } catch (PendingIntent.CanceledException e) {
                Log.w("StatusBar", "Sending intent failed: " + e);
                if (!z2) {
                    collapsePanelOnMainThread();
                }
            }
        } else {
            statusBarLaunchAnimatorController = null;
        }
        this.mActivityLaunchAnimator.startPendingIntentWithAnimation(statusBarLaunchAnimatorController, z, pendingIntent.getCreatorPackage(), new ActivityLaunchAnimator.PendingIntentStarter() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda2
            @Override // com.android.systemui.animation.ActivityLaunchAnimator.PendingIntentStarter
            public final int startPendingIntent(RemoteAnimationAdapter remoteAnimationAdapter) {
                int lambda$startPendingIntentDismissingKeyguard$34;
                lambda$startPendingIntentDismissingKeyguard$34 = StatusBar.this.lambda$startPendingIntentDismissingKeyguard$34(pendingIntent, remoteAnimationAdapter);
                return lambda$startPendingIntentDismissingKeyguard$34;
            }
        });
        if (pendingIntent.isActivity()) {
            this.mAssistManagerLazy.get().hideAssist();
        }
        if (runnable != null) {
            postOnUiThread(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$startPendingIntentDismissingKeyguard$34(PendingIntent pendingIntent, RemoteAnimationAdapter remoteAnimationAdapter) throws PendingIntent.CanceledException {
        return pendingIntent.sendAndReturnResult(null, 0, null, null, null, null, getActivityOptions(this.mDisplayId, remoteAnimationAdapter));
    }

    private void postOnUiThread(Runnable runnable) {
        this.mMainThreadHandler.post(runnable);
    }

    public static Bundle getActivityOptions(int i, RemoteAnimationAdapter remoteAnimationAdapter) {
        ActivityOptions defaultActivityOptions = getDefaultActivityOptions(remoteAnimationAdapter);
        defaultActivityOptions.setLaunchDisplayId(i);
        defaultActivityOptions.setCallerDisplayId(i);
        return defaultActivityOptions.toBundle();
    }

    public static Bundle getActivityOptions(int i, RemoteAnimationAdapter remoteAnimationAdapter, boolean z, long j) {
        ActivityOptions defaultActivityOptions = getDefaultActivityOptions(remoteAnimationAdapter);
        defaultActivityOptions.setSourceInfo(z ? 3 : 2, j);
        defaultActivityOptions.setLaunchDisplayId(i);
        defaultActivityOptions.setCallerDisplayId(i);
        return defaultActivityOptions.toBundle();
    }

    public static ActivityOptions getDefaultActivityOptions(RemoteAnimationAdapter remoteAnimationAdapter) {
        if (remoteAnimationAdapter != null) {
            return ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter);
        }
        return ActivityOptions.makeBasic();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void visibilityChanged(boolean z) {
        if (this.mVisible != z) {
            this.mVisible = z;
            if (!z) {
                this.mGutsManager.closeAndSaveGuts(true, true, true, -1, -1, true);
            }
        }
        updateVisibleToUser();
    }

    protected void updateVisibleToUser() {
        boolean z = this.mVisibleToUser;
        boolean z2 = this.mVisible && this.mDeviceInteractive;
        this.mVisibleToUser = z2;
        if (z != z2) {
            handleVisibleToUserChanged(z2);
        }
    }

    public void clearNotificationEffects() {
        try {
            this.mBarService.clearNotificationEffects();
        } catch (RemoteException unused) {
        }
    }

    protected void notifyHeadsUpGoingToSleep() {
        maybeEscalateHeadsUp();
    }

    public boolean isBouncerShowing() {
        return this.mBouncerShowing;
    }

    public boolean isBouncerShowingScrimmed() {
        return isBouncerShowing() && this.mStatusBarKeyguardViewManager.bouncerNeedsScrimming();
    }

    public void onBouncerPreHideAnimation() {
        this.mNotificationPanelViewController.onBouncerPreHideAnimation();
    }

    public static PackageManager getPackageManagerForUser(Context context, int i) {
        if (i >= 0) {
            try {
                context = context.createPackageContextAsUser(context.getPackageName(), 4, new UserHandle(i));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return context.getPackageManager();
    }

    public boolean isKeyguardSecure() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager == null) {
            Slog.w("StatusBar", "isKeyguardSecure() called before startKeyguard(), returning false", new Throwable());
            return false;
        }
        return statusBarKeyguardViewManager.isSecure();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showAssistDisclosure() {
        this.mAssistManagerLazy.get().showDisclosure();
    }

    public NotificationPanelViewController getPanelController() {
        return this.mNotificationPanelViewController;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void startAssist(Bundle bundle) {
        this.mAssistManagerLazy.get().startAssist(bundle);
    }

    public NotificationGutsManager getGutsManager() {
        return this.mGutsManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTransientShown() {
        return this.mTransientShown;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void suppressAmbientDisplay(boolean z) {
        this.mDozeServiceHost.setDozeSuppressed(z);
    }

    public void addExpansionChangedListener(ExpansionChangedListener expansionChangedListener) {
        this.mExpansionChangedListeners.add(expansionChangedListener);
        sendInitialExpansionAmount(expansionChangedListener);
    }

    private void sendInitialExpansionAmount(ExpansionChangedListener expansionChangedListener) {
        PhoneStatusBarView phoneStatusBarView = this.mStatusBarView;
        if (phoneStatusBarView != null) {
            expansionChangedListener.onExpansionChanged(phoneStatusBarView.getExpansionFraction(), this.mStatusBarView.isExpanded());
        }
    }

    public void removeExpansionChangedListener(ExpansionChangedListener expansionChangedListener) {
        this.mExpansionChangedListeners.remove(expansionChangedListener);
    }

    private void updateLightRevealScrimVisibility() {
        LightRevealScrim lightRevealScrim = this.mLightRevealScrim;
        if (lightRevealScrim == null) {
            return;
        }
        lightRevealScrim.setAlpha(this.mScrimController.getState().getMaxLightRevealScrimAlpha());
    }

    public void setNotificationPanelViewAlpha(final float f) {
        Log.d("StatusBar", "setNotificationPanelViewAlpha:  alpha = " + f, new Exception());
        if (this.mNotificationPanelViewController != null) {
            this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar.21
                @Override // java.lang.Runnable
                public void run() {
                    StatusBar.this.mNotificationPanelViewController.setAlpha(f);
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x00fc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00f2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Point getTapXY() {
        BufferedReader bufferedReader;
        Exception e;
        InputStreamReader inputStreamReader;
        Point point = new Point();
        InputStreamReader inputStreamReader2 = null;
        String str = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream("/sys/devices/platform/soc/a94000.spi/spi_master/spi0/spi0.0/fts_parse_coordinate"));
        } catch (Exception e2) {
            bufferedReader = null;
            e = e2;
            inputStreamReader = null;
        } catch (Throwable th) {
            th = th;
            bufferedReader = null;
            if (inputStreamReader2 != null) {
            }
            if (bufferedReader != null) {
            }
            throw th;
        }
        try {
            bufferedReader = new BufferedReader(inputStreamReader);
            int i = 0;
            String str2 = null;
            while (true) {
                try {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        Log.d("StatusBar", "tempText = " + readLine);
                        if (i == 0) {
                            str = readLine;
                        } else if (i == 1) {
                            str2 = readLine;
                        }
                        i++;
                    } catch (Exception e3) {
                        e = e3;
                        Log.d("StatusBar", "getTapXY ", e);
                        if (inputStreamReader != null) {
                            try {
                                inputStreamReader.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (bufferedReader == null) {
                            return point;
                        }
                        try {
                            bufferedReader.close();
                            return point;
                        } catch (IOException e5) {
                            e5.printStackTrace();
                            return point;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inputStreamReader2 = inputStreamReader;
                    if (inputStreamReader2 != null) {
                        try {
                            inputStreamReader2.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e7) {
                            e7.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
            Log.d("StatusBar", "xText = " + str + " ytext = " + str2);
            String substring = str.substring(2);
            String substring2 = str2.substring(2);
            Log.d("StatusBar", "x = " + substring + " y = " + substring2);
            int parseInt = Integer.parseInt(substring, 16);
            int parseInt2 = Integer.parseInt(substring2, 16);
            Log.d("StatusBar", "tapx = " + parseInt + " tapy = " + parseInt2);
            Point point2 = new Point(parseInt, parseInt2);
            try {
                inputStreamReader.close();
            } catch (IOException e8) {
                e8.printStackTrace();
            }
            try {
                bufferedReader.close();
            } catch (IOException e9) {
                e9.printStackTrace();
            }
            return point2;
        } catch (Exception e10) {
            bufferedReader = null;
            e = e10;
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            inputStreamReader2 = inputStreamReader;
            if (inputStreamReader2 != null) {
            }
            if (bufferedReader != null) {
            }
            throw th;
        }
    }

    public void wakeUpIfDozing(long j, View view, String str, PointF pointF) {
        if (!this.mDozing || this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying()) {
            return;
        }
        PowerManager powerManager = this.mPowerManager;
        powerManager.wakeUp(j, 6, "com.android.systemui:" + str);
        this.mWakeUpComingFromTouch = true;
        view.getLocationInWindow(this.mTmpInt2);
        this.mWakeUpTouchLocation = pointF;
        this.mFalsingCollector.onScreenOnFromTouch();
    }

    public void onFingerprintDown() {
        DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
        if (dozeServiceHost != null) {
            dozeServiceHost.fireFingerprintDown();
        }
    }

    public void onFingerprintUp() {
        DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
        if (dozeServiceHost != null) {
            dozeServiceHost.fireFingerprintUp();
        }
    }

    public void onDozeFingerprintRunningStateChanged() {
        DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
        if (dozeServiceHost != null) {
            dozeServiceHost.fireDozeFingerprintRunningStateChanged();
        }
    }

    public boolean isWakeAndUnlock() {
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        if (biometricUnlockController != null) {
            return biometricUnlockController.isWakeAndUnlock();
        }
        return false;
    }

    public void onTapWake() {
        this.mIsTapWake = true;
        if (((LiftWakeGestureController) Dependency.get(LiftWakeGestureController.class)).isNear()) {
            Log.i("StatusBar", "retrun on onTapWake, don't wake up.");
        } else if (((AODController) Dependency.get(AODController.class)).isTapWakeEnable()) {
            this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 6, "com.android.systemui:NODOZE");
        } else {
            DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
            if (dozeServiceHost == null) {
                return;
            }
            dozeServiceHost.fireTapWake();
        }
    }

    public boolean isTapWake() {
        return this.mIsTapWake;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldEnableLiftGestureLp() {
        return (((AODController) Dependency.get(AODController.class)).isLiftWakeEnable() || this.mKeyguardUpdateMonitor.isUdfpsEnrolled()) && this.mLiftWakeGestureController.isLiftSensorSupported();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldEnableProximity() {
        return ((AODController) Dependency.get(AODController.class)).isLiftWakeEnable() || this.mKeyguardUpdateMonitor.isUdfpsEnrolled() || ((AODController) Dependency.get(AODController.class)).isTapWakeEnable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldEnableMotionGestureLp() {
        return this.mKeyguardUpdateMonitor.isUdfpsEnrolled() && this.mLiftWakeGestureController.isMotionSensorSupported();
    }

    public void setLightRevealScrimValue(float f) {
        this.mLightRevealScrim.setRevealAmount(f);
    }
}
