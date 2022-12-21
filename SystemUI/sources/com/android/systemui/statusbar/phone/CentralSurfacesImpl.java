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
import android.app.TaskStackBuilder;
import android.app.UiModeManager;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.devicestate.DeviceStateManager;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.IndentingPrintWriter;
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
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.RegisterStatusBarResult;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.ViewMediatorCallback;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.p019wm.shell.startingsurface.SplashscreenContentDrawer;
import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.AutoReinflateContainer;
import com.android.systemui.C1893R;
import com.android.systemui.CoreStartable;
import com.android.systemui.DejankUtils;
import com.android.systemui.EventLogTags;
import com.android.systemui.InitController;
import com.android.systemui.Prefs;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.camera.CameraIntents;
import com.android.systemui.charging.WirelessChargingAnimation;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.emergency.EmergencyGesture;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.fragments.ExtensionFragmentListener;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.p012qs.QSFragment;
import com.android.systemui.p012qs.QSPanelController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.OverlayPlugin;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.BackDropView;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.GestureRecorder;
import com.android.systemui.statusbar.KeyboardShortcuts;
import com.android.systemui.statusbar.KeyguardIndicationController;
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
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.WallpaperController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.volume.VolumeComponent;
import com.android.systemui.wmshell.BubblesManager;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.facerecognition.FaceRecognitionController;
import com.nothing.systemui.facerecognition.IFaceRecognitionAnimationCallback;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.statusbar.CommandQueueEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import com.nothing.systemui.util.NTLogUtil;
import dagger.Lazy;
import java.net.HttpURLConnection;
import java.p026io.PrintWriter;
import java.p026io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Named;

@SysUISingleton
public class CentralSurfacesImpl extends CoreStartable implements CentralSurfaces {
    private static final String BANNER_ACTION_CANCEL = "com.android.systemui.statusbar.banner_action_cancel";
    private static final String BANNER_ACTION_SETUP = "com.android.systemui.statusbar.banner_action_setup";
    private static final int HINT_RESET_DELAY_MS = 1200;
    private static final int MSG_LAUNCH_TRANSITION_TIMEOUT = 1003;
    private static final int MSG_OPEN_SETTINGS_PANEL = 1002;
    public static final boolean ONLY_CORE_APPS;
    private static final UiEventLogger sUiEventLogger = new UiEventLoggerImpl();
    private final int[] mAbsPos = new int[2];
    private final AccessibilityFloatingMenuController mAccessibilityFloatingMenuController;
    protected AccessibilityManager mAccessibilityManager;
    private final ActivityIntentHelper mActivityIntentHelper;
    private final ActivityLaunchAnimator mActivityLaunchAnimator;
    private final ActivityLaunchAnimator.Callback mActivityLaunchAnimatorCallback = new ActivityLaunchAnimator.Callback() {
        public boolean isOnKeyguard() {
            return CentralSurfacesImpl.this.mKeyguardStateController.isShowing();
        }

        public void hideKeyguardWithAnimation(IRemoteAnimationRunner iRemoteAnimationRunner) {
            CentralSurfacesImpl.this.mMainExecutor.execute(new CentralSurfacesImpl$24$$ExternalSyntheticLambda0(this, iRemoteAnimationRunner));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$hideKeyguardWithAnimation$0$com-android-systemui-statusbar-phone-CentralSurfacesImpl$24 */
        public /* synthetic */ void mo43935xd03a6eb7(IRemoteAnimationRunner iRemoteAnimationRunner) {
            CentralSurfacesImpl.this.mKeyguardViewMediator.hideWithAnimation(iRemoteAnimationRunner);
        }

        public int getBackgroundColor(TaskInfo taskInfo) {
            if (CentralSurfacesImpl.this.mStartingSurfaceOptional.isPresent()) {
                return ((StartingSurface) CentralSurfacesImpl.this.mStartingSurfaceOptional.get()).getBackgroundColor(taskInfo);
            }
            Log.w(CentralSurfaces.TAG, "No starting surface, defaulting to SystemBGColor");
            return SplashscreenContentDrawer.getSystemBGColor();
        }
    };
    private final ActivityLaunchAnimator.Listener mActivityLaunchAnimatorListener = new ActivityLaunchAnimator.Listener() {
        public void onLaunchAnimationStart() {
            CentralSurfacesImpl.this.mKeyguardViewMediator.setBlursDisabledForAppLaunch(true);
        }

        public void onLaunchAnimationEnd() {
            CentralSurfacesImpl.this.mKeyguardViewMediator.setBlursDisabledForAppLaunch(false);
        }
    };
    /* access modifiers changed from: private */
    public View mAmbientIndicationContainer;
    private int mAppearance;
    private final Lazy<AssistManager> mAssistManagerLazy;
    /* access modifiers changed from: private */
    public AuthRippleController mAuthRippleController;
    private final AutoHideController mAutoHideController;
    private final BroadcastReceiver mBannerActionBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (CentralSurfacesImpl.BANNER_ACTION_CANCEL.equals(action) || CentralSurfacesImpl.BANNER_ACTION_SETUP.equals(action)) {
                ((NotificationManager) CentralSurfacesImpl.this.mContext.getSystemService("notification")).cancel(5);
                Settings.Secure.putInt(CentralSurfacesImpl.this.mContext.getContentResolver(), "show_note_about_notification_hiding", 0);
                if (CentralSurfacesImpl.BANNER_ACTION_SETUP.equals(action)) {
                    CentralSurfacesImpl.this.mShadeController.animateCollapsePanels(2, true);
                    CentralSurfacesImpl.this.mContext.startActivity(new Intent("android.settings.ACTION_APP_NOTIFICATION_REDACTION").addFlags(268435456));
                }
            }
        }
    };
    protected IStatusBarService mBarService;
    protected final BatteryController mBatteryController;
    private final BatteryController.BatteryStateChangeCallback mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() {
        public void onPowerSaveChanged(boolean z) {
            CentralSurfacesImpl.this.mMainExecutor.execute(CentralSurfacesImpl.this.mCheckBarModes);
            if (CentralSurfacesImpl.this.mDozeServiceHost != null) {
                CentralSurfacesImpl.this.mDozeServiceHost.firePowerSaveChanged(z);
            }
        }
    };
    /* access modifiers changed from: private */
    public BiometricUnlockController mBiometricUnlockController;
    private final Lazy<BiometricUnlockController> mBiometricUnlockControllerLazy;
    protected boolean mBouncerShowing;
    boolean mBouncerShowingChanged = false;
    /* access modifiers changed from: private */
    public BrightnessMirrorController mBrightnessMirrorController;
    private boolean mBrightnessMirrorVisible;
    private final BrightnessSliderController.Factory mBrightnessSliderFactory;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Trace.beginSection("CentralSurfaces#onReceive");
            String action = intent.getAction();
            String stringExtra = intent.getStringExtra("reason");
            int i = 0;
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action)) {
                KeyboardShortcuts.dismiss();
                CentralSurfacesImpl.this.mRemoteInputManager.closeRemoteInputs();
                if (CentralSurfacesImpl.this.mLockscreenUserManager.isCurrentProfile(getSendingUserId())) {
                    if (stringExtra != null) {
                        if (stringExtra.equals("recentapps")) {
                            i = 2;
                        }
                        if (stringExtra.equals("dream") && CentralSurfacesImpl.this.mScreenOffAnimationController.shouldExpandNotifications()) {
                            i |= 4;
                        }
                    }
                    CentralSurfacesImpl.this.mShadeController.animateCollapsePanels(i);
                }
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                if (CentralSurfacesImpl.this.mNotificationShadeWindowController != null) {
                    CentralSurfacesImpl.this.mNotificationShadeWindowController.setNotTouchable(false);
                }
                CentralSurfacesImpl.this.finishBarAnimations();
                CentralSurfacesImpl.this.resetUserExpandedStates();
            }
            Trace.endSection();
        }
    };
    private final Bubbles.BubbleExpandListener mBubbleExpandListener;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    private final Optional<Bubbles> mBubblesOptional;
    private CentralSurfacesComponent mCentralSurfacesComponent;
    private final CentralSurfacesComponent.Factory mCentralSurfacesComponentFactory;
    /* access modifiers changed from: private */
    public final Runnable mCheckBarModes = new CentralSurfacesImpl$$ExternalSyntheticLambda44(this);
    boolean mCloseQsBeforeScreenOff;
    private final SysuiColorExtractor mColorExtractor;
    protected final CommandQueue mCommandQueue;
    /* access modifiers changed from: private */
    public CentralSurfacesCommandQueueCallbacks mCommandQueueCallbacks;
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onConfigChanged(Configuration configuration) {
            CentralSurfacesImpl.this.updateResources();
            CentralSurfacesImpl.this.updateDisplaySize();
            if (!CentralSurfacesImpl.this.mNotifPipelineFlags.isNewPipelineEnabled()) {
                CentralSurfacesImpl.this.mViewHierarchyManager.updateRowStates();
            }
            CentralSurfacesImpl.this.mScreenPinningRequest.onConfigurationChanged();
            CentralSurfacesImpl.this.mNotificationPanelViewController.loadDimens();
        }

        public void onDensityOrFontScaleChanged() {
            if (CentralSurfacesImpl.this.mBrightnessMirrorController != null) {
                CentralSurfacesImpl.this.mBrightnessMirrorController.onDensityOrFontScaleChanged();
            }
            CentralSurfacesImpl.this.mUserInfoControllerImpl.onDensityOrFontScaleChanged();
            CentralSurfacesImpl.this.mUserSwitcherController.onDensityOrFontScaleChanged();
            CentralSurfacesImpl.this.mNotificationIconAreaController.onDensityOrFontScaleChanged(CentralSurfacesImpl.this.mContext);
            CentralSurfacesImpl.this.mHeadsUpManager.onDensityOrFontScaleChanged();
        }

        public void onThemeChanged() {
            if (CentralSurfacesImpl.this.mBrightnessMirrorController != null) {
                CentralSurfacesImpl.this.mBrightnessMirrorController.onOverlayChanged();
            }
            CentralSurfacesImpl.this.mNotificationPanelViewController.onThemeChanged();
            if (CentralSurfacesImpl.this.mStatusBarKeyguardViewManager != null) {
                CentralSurfacesImpl.this.mStatusBarKeyguardViewManager.onThemeChanged();
            }
            if (CentralSurfacesImpl.this.mAmbientIndicationContainer instanceof AutoReinflateContainer) {
                ((AutoReinflateContainer) CentralSurfacesImpl.this.mAmbientIndicationContainer).inflateLayout();
            }
            CentralSurfacesImpl.this.mNotificationIconAreaController.onThemeChanged();
        }

        public void onUiModeChanged() {
            if (CentralSurfacesImpl.this.mBrightnessMirrorController != null) {
                CentralSurfacesImpl.this.mBrightnessMirrorController.onUiModeChanged();
            }
        }
    };
    private final Point mCurrentDisplaySize = new Point();
    private final DemoMode mDemoModeCallback = new DemoMode() {
        public void dispatchDemoCommand(String str, Bundle bundle) {
        }

        public void onDemoModeFinished() {
            CentralSurfacesImpl.this.checkBarModes();
        }
    };
    private final DemoModeController mDemoModeController;
    private final BroadcastReceiver mDemoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            CentralSurfaces.ACTION_FAKE_ARTWORK.equals(intent.getAction());
        }
    };
    protected boolean mDeviceInteractive;
    protected DevicePolicyManager mDevicePolicyManager;
    /* access modifiers changed from: private */
    public final DeviceProvisionedController mDeviceProvisionedController;
    private int mDisabled1 = 0;
    private int mDisabled2 = 0;
    protected Display mDisplay;
    /* access modifiers changed from: private */
    public int mDisplayId;
    private final DisplayMetrics mDisplayMetrics;
    /* access modifiers changed from: private */
    public final DozeParameters mDozeParameters;
    protected DozeScrimController mDozeScrimController;
    DozeServiceHost mDozeServiceHost;
    protected boolean mDozing;
    private final IDreamManager mDreamManager;
    private final DreamOverlayStateController mDreamOverlayStateController;
    private final DynamicPrivacyController mDynamicPrivacyController;
    protected final NotificationEntryManager mEntryManager;
    /* access modifiers changed from: private */
    public boolean mExpandedVisible;
    private final ExtensionController mExtensionController;
    private IFaceRecognitionAnimationCallback mFaceAnimationCallback = new IFaceRecognitionAnimationCallback() {
        public void onFaceSuccessConnect() {
        }

        public void resetFaceImage() {
        }

        public void startLoadingAnimation() {
        }

        public void startSlideUpAnimation() {
        }

        public void startFailureAnimation() {
            CentralSurfacesImpl.this.mMainHandler.post(new Runnable() {
                public void run() {
                    CentralSurfacesImpl.this.mKeyguardIndicationController.showTransientIndication((int) C1893R.string.nt_face_recognition_fail);
                    CentralSurfacesImpl.this.mKeyguardIndicationController.hideTransientIndication();
                }
            });
        }

        public void startFreezeAnimation() {
            CentralSurfacesImpl.this.mMainHandler.post(new Runnable() {
                public void run() {
                    CentralSurfacesImpl.this.mKeyguardIndicationController.showTransientIndication((int) C1893R.string.nt_face_recognition_error);
                    CentralSurfacesImpl.this.mKeyguardIndicationController.hideTransientIndication();
                }
            });
        }

        public void startSuccessAnimation() {
            CentralSurfacesImpl.this.mMainHandler.post(new Runnable() {
                public void run() {
                    CentralSurfacesImpl.this.mKeyguardIndicationController.hideTransientIndication();
                }
            });
        }

        public void onFaceAuthenticationTimeout() {
            CentralSurfacesImpl.this.mMainHandler.post(new Runnable() {
                public void run() {
                    CentralSurfacesImpl.this.mKeyguardIndicationController.showTransientIndication((int) C1893R.string.nt_face_recognition_timeout);
                    CentralSurfacesImpl.this.mKeyguardIndicationController.hideTransientIndication();
                }
            });
        }
    };
    private final FalsingManager.FalsingBeliefListener mFalsingBeliefListener = new FalsingManager.FalsingBeliefListener() {
        public void onFalse() {
            CentralSurfacesImpl.this.mStatusBarKeyguardViewManager.reset(true);
        }
    };
    /* access modifiers changed from: private */
    public final FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    /* access modifiers changed from: private */
    public final FeatureFlags mFeatureFlags;
    private final FragmentService mFragmentService;
    private final GestureRecorder mGestureRec = null;
    protected PowerManager.WakeLock mGestureWakeLock;
    private final NotificationGutsManager mGutsManager;
    /* access modifiers changed from: private */
    public final HeadsUpManagerPhone mHeadsUpManager;
    private final PhoneStatusBarPolicy mIconPolicy;
    private final InitController mInitController;
    private int mInteractingWindows;
    /* access modifiers changed from: private */
    public boolean mIsFullscreen;
    /* access modifiers changed from: private */
    public boolean mIsLaunchingActivityOverLockscreen;
    private final InteractionJankMonitor mJankMonitor;
    /* access modifiers changed from: private */
    public final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardDismissUtil mKeyguardDismissUtil;
    KeyguardIndicationController mKeyguardIndicationController;
    protected KeyguardManager mKeyguardManager;
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    private final KeyguardStateController.Callback mKeyguardStateControllerCallback = new KeyguardStateController.Callback() {
        public void onKeyguardShowingChanged() {
            boolean isOccluded = CentralSurfacesImpl.this.mKeyguardStateController.isOccluded();
            CentralSurfacesImpl.this.mStatusBarHideIconsForBouncerManager.setIsOccludedAndTriggerUpdate(isOccluded);
            CentralSurfacesImpl.this.mScrimController.setKeyguardOccluded(isOccluded);
        }
    };
    private final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public final KeyguardViewMediator mKeyguardViewMediator;
    private final ViewMediatorCallback mKeyguardViewMediatorCallback;
    /* access modifiers changed from: private */
    public int mLastCameraLaunchSource;
    private int mLastLoggedStateFingerprint;
    int mLastMotionEventId = -1;
    /* access modifiers changed from: private */
    public boolean mLaunchCameraOnFinishedGoingToSleep;
    /* access modifiers changed from: private */
    public boolean mLaunchCameraWhenFinishedWaking;
    /* access modifiers changed from: private */
    public boolean mLaunchEmergencyActionOnFinishedGoingToSleep;
    /* access modifiers changed from: private */
    public boolean mLaunchEmergencyActionWhenFinishedWaking;
    private Runnable mLaunchTransitionCancelRunnable;
    private Runnable mLaunchTransitionEndRunnable;
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    private final LightBarController mLightBarController;
    /* access modifiers changed from: private */
    public LightRevealScrim mLightRevealScrim;
    private final LockscreenGestureLogger mLockscreenGestureLogger;
    private final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    /* access modifiers changed from: private */
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    protected LockscreenWallpaper mLockscreenWallpaper;
    private final Lazy<LockscreenWallpaper> mLockscreenWallpaperLazy;
    /* access modifiers changed from: private */
    public final DelayableExecutor mMainExecutor;
    /* access modifiers changed from: private */
    public Handler mMainHandler;
    private final NotificationMediaManager mMediaManager;
    private final MessageRouter mMessageRouter;
    private final MetricsLogger mMetricsLogger;
    /* access modifiers changed from: private */
    public final NavigationBarController mNavigationBarController;
    private final NetworkController mNetworkController;
    private boolean mNoAnimationOnNextBarModeChange;
    private NotificationListContainer mNotifListContainer;
    /* access modifiers changed from: private */
    public final NotifPipelineFlags mNotifPipelineFlags;
    private final NotifShadeEventSource mNotifShadeEventSource;
    private NotificationActivityStarter mNotificationActivityStarter;
    private NotificationLaunchAnimatorControllerProvider mNotificationAnimationProvider;
    protected final NotificationIconAreaController mNotificationIconAreaController;
    protected final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    private final NotificationLogger mNotificationLogger;
    protected NotificationPanelViewController mNotificationPanelViewController;
    private final Lazy<NotificationShadeDepthController> mNotificationShadeDepthControllerLazy;
    protected final NotificationShadeWindowController mNotificationShadeWindowController;
    protected NotificationShadeWindowView mNotificationShadeWindowView;
    protected NotificationShadeWindowViewController mNotificationShadeWindowViewController;
    protected NotificationShelfController mNotificationShelfController;
    /* access modifiers changed from: private */
    public final NotificationsController mNotificationsController;
    private final ColorExtractor.OnColorsChangedListener mOnColorsChangedListener = new CentralSurfacesImpl$$ExternalSyntheticLambda22(this);
    private final OngoingCallController mOngoingCallController;
    protected boolean mPanelExpanded;
    private final PanelExpansionStateManager mPanelExpansionStateManager;
    private PhoneStatusBarViewController mPhoneStatusBarViewController;
    private final PluginDependencyProvider mPluginDependencyProvider;
    private final PluginManager mPluginManager;
    private PowerButtonReveal mPowerButtonReveal;
    private final PowerManager mPowerManager;
    protected NotificationPresenter mPresenter;
    private final PulseExpansionHandler mPulseExpansionHandler;
    private QSPanelController mQSPanelController;
    private final Object mQueueLock = new Object();
    /* access modifiers changed from: private */
    public final NotificationRemoteInputManager mRemoteInputManager;
    private View mReportRejectedTouch;
    private final ScreenLifecycle mScreenLifecycle;
    final ScreenLifecycle.Observer mScreenObserver = new ScreenLifecycle.Observer() {
        public void onScreenTurningOn(Runnable runnable) {
            CentralSurfacesImpl.this.mFalsingCollector.onScreenTurningOn();
            CentralSurfacesImpl.this.mNotificationPanelViewController.onScreenTurningOn();
        }

        public void onScreenTurnedOn() {
            CentralSurfacesImpl.this.mScrimController.onScreenTurnedOn();
        }

        public void onScreenTurnedOff() {
            Trace.beginSection("CentralSurfaces#onScreenTurnedOff");
            CentralSurfacesImpl.this.mDozeServiceHost.updateDozing();
            CentralSurfacesImpl.this.mFalsingCollector.onScreenOff();
            CentralSurfacesImpl.this.mScrimController.onScreenTurnedOff();
            if (CentralSurfacesImpl.this.mCloseQsBeforeScreenOff) {
                CentralSurfacesImpl.this.mNotificationPanelViewController.closeQs();
                CentralSurfacesImpl.this.mCloseQsBeforeScreenOff = false;
            }
            CentralSurfacesImpl.this.updateIsKeyguard();
            Trace.endSection();
        }
    };
    /* access modifiers changed from: private */
    public final ScreenOffAnimationController mScreenOffAnimationController;
    /* access modifiers changed from: private */
    public final ScreenPinningRequest mScreenPinningRequest;
    /* access modifiers changed from: private */
    public final ScrimController mScrimController;
    /* access modifiers changed from: private */
    public final ShadeController mShadeController;
    protected NotificationStackScrollLayout mStackScroller;
    private NotificationStackScrollLayoutController mStackScrollerController;
    /* access modifiers changed from: private */
    public final Optional<StartingSurface> mStartingSurfaceOptional;
    protected int mState;
    private StatusBarStateController.StateListener mStateListener = new StatusBarStateController.StateListener() {
        public void onStatePreChange(int i, int i2) {
            if (CentralSurfacesImpl.this.mVisible && (i2 == 2 || CentralSurfacesImpl.this.mStatusBarStateController.goingToFullShade())) {
                CentralSurfacesImpl.this.clearNotificationEffects();
            }
            if (i2 == 1) {
                CentralSurfacesImpl.this.mRemoteInputManager.onPanelCollapsed();
                CentralSurfacesImpl.this.maybeEscalateHeadsUp();
            }
        }

        public void onStateChanged(int i) {
            CentralSurfacesImpl.this.mState = i;
            CentralSurfacesImpl.this.updateReportRejectedTouchVisibility();
            CentralSurfacesImpl.this.mDozeServiceHost.updateDozing();
            CentralSurfacesImpl.this.updateTheme();
            CentralSurfacesImpl.this.mNavigationBarController.touchAutoDim(CentralSurfacesImpl.this.mDisplayId);
            Trace.beginSection("CentralSurfaces#updateKeyguardState");
            boolean z = true;
            if (CentralSurfacesImpl.this.mState == 1) {
                CentralSurfacesImpl.this.mNotificationPanelViewController.cancelPendingPanelCollapse();
            }
            CentralSurfacesImpl.this.updateDozingState();
            CentralSurfacesImpl.this.checkBarModes();
            CentralSurfacesImpl.this.updateScrimController();
            NotificationPresenter notificationPresenter = CentralSurfacesImpl.this.mPresenter;
            if (CentralSurfacesImpl.this.mState == 1) {
                z = false;
            }
            notificationPresenter.updateMediaMetaData(false, z);
            Trace.endSection();
        }

        public void onDozeAmountChanged(float f, float f2) {
            if (CentralSurfacesImpl.this.mFeatureFlags.isEnabled(Flags.LOCKSCREEN_ANIMATIONS)) {
                CentralSurfacesImpl.this.mLightRevealScrim.setRevealAmount(1.0f - f);
            }
        }

        public void onDozingChanged(boolean z) {
            Trace.beginSection("CentralSurfaces#updateDozing");
            CentralSurfacesImpl.this.mDozing = z;
            CentralSurfacesImpl.this.mNotificationPanelViewController.resetViews(CentralSurfacesImpl.this.mDozeServiceHost.getDozingRequested() && CentralSurfacesImpl.this.mDozeParameters.shouldControlScreenOff());
            CentralSurfacesImpl.this.updateQsExpansionEnabled();
            CentralSurfacesImpl.this.mKeyguardViewMediator.setDozing(CentralSurfacesImpl.this.mDozing);
            CentralSurfacesImpl.this.mNotificationsController.requestNotificationUpdate("onDozingChanged");
            CentralSurfacesImpl.this.updateDozingState();
            CentralSurfacesImpl.this.mDozeServiceHost.updateDozing();
            CentralSurfacesImpl.this.updateScrimController();
            if (CentralSurfacesImpl.this.mBiometricUnlockController.isWakeAndUnlock()) {
                CentralSurfacesImpl.this.updateIsKeyguard();
            }
            CentralSurfacesImpl.this.updateReportRejectedTouchVisibility();
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).onDozingChanged(z, CentralSurfacesImpl.this.mState);
            Trace.endSection();
        }

        public void onFullscreenStateChanged(boolean z) {
            boolean unused = CentralSurfacesImpl.this.mIsFullscreen = z;
            CentralSurfacesImpl.this.maybeUpdateBarMode();
        }
    };
    /* access modifiers changed from: private */
    public final StatusBarHideIconsForBouncerManager mStatusBarHideIconsForBouncerManager;
    protected StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private int mStatusBarMode;
    private final StatusBarSignalPolicy mStatusBarSignalPolicy;
    /* access modifiers changed from: private */
    public final SysuiStatusBarStateController mStatusBarStateController;
    private LogMaker mStatusBarStateLog;
    private final StatusBarTouchableRegionManager mStatusBarTouchableRegionManager;
    private PhoneStatusBarTransitions mStatusBarTransitions;
    protected PhoneStatusBarView mStatusBarView;
    private final StatusBarWindowController mStatusBarWindowController;
    private boolean mStatusBarWindowHidden;
    private int mStatusBarWindowState = 0;
    private final int[] mTmpInt2 = new int[2];
    private boolean mTopHidesStatusBar;
    private boolean mTransientShown;
    private float mTransitionToFullShadeProgress = 0.0f;
    private final Executor mUiBgExecutor;
    private UiModeManager mUiModeManager;
    private final ScrimController.Callback mUnlockScrimCallback = new ScrimController.Callback() {
        public void onFinished() {
            if (CentralSurfacesImpl.this.mStatusBarKeyguardViewManager == null) {
                Log.w(CentralSurfaces.TAG, "Tried to notify keyguard visibility when mStatusBarKeyguardViewManager was null");
            } else if (CentralSurfacesImpl.this.mKeyguardStateController.isKeyguardFadingAway()) {
                CentralSurfacesImpl.this.mStatusBarKeyguardViewManager.onKeyguardFadedAway();
            }
        }

        public void onCancelled() {
            onFinished();
        }
    };
    private final KeyguardUpdateMonitorCallback mUpdateCallback = new KeyguardUpdateMonitorCallback() {
        public void onDreamingStateChanged(boolean z) {
            CentralSurfacesImpl.this.updateScrimController();
            if (z) {
                CentralSurfacesImpl.this.maybeEscalateHeadsUp();
            }
        }

        public void onStrongAuthStateChanged(int i) {
            super.onStrongAuthStateChanged(i);
            CentralSurfacesImpl.this.mNotificationsController.requestNotificationUpdate("onStrongAuthStateChanged");
        }

        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            if (biometricSourceType == BiometricSourceType.FINGERPRINT && z && CentralSurfacesImpl.this.mDozing) {
                ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).onDozeFingerprintRunningStateChanged();
            }
        }
    };
    /* access modifiers changed from: private */
    public final UserInfoControllerImpl mUserInfoControllerImpl;
    protected boolean mUserSetup = false;
    private final DeviceProvisionedController.DeviceProvisionedListener mUserSetupObserver = new DeviceProvisionedController.DeviceProvisionedListener() {
        public void onUserSetupChanged() {
            boolean isCurrentUserSetup = CentralSurfacesImpl.this.mDeviceProvisionedController.isCurrentUserSetup();
            Log.d(CentralSurfaces.TAG, "mUserSetupObserver - DeviceProvisionedListener called for current user");
            if (isCurrentUserSetup != CentralSurfacesImpl.this.mUserSetup) {
                CentralSurfacesImpl.this.mUserSetup = isCurrentUserSetup;
                if (!CentralSurfacesImpl.this.mUserSetup) {
                    CentralSurfacesImpl.this.animateCollapseQuickSettings();
                }
                if (CentralSurfacesImpl.this.mNotificationPanelViewController != null) {
                    CentralSurfacesImpl.this.mNotificationPanelViewController.setUserSetupComplete(CentralSurfacesImpl.this.mUserSetup);
                }
                CentralSurfacesImpl.this.updateQsExpansionEnabled();
            }
        }
    };
    /* access modifiers changed from: private */
    public final UserSwitcherController mUserSwitcherController;
    /* access modifiers changed from: private */
    public final NotificationViewHierarchyManager mViewHierarchyManager;
    protected boolean mVisible;
    private boolean mVisibleToUser;
    private final VisualStabilityManager mVisualStabilityManager;
    private final VolumeComponent mVolumeComponent;
    /* access modifiers changed from: private */
    public boolean mWakeUpComingFromTouch;
    /* access modifiers changed from: private */
    public final NotificationWakeUpCoordinator mWakeUpCoordinator;
    /* access modifiers changed from: private */
    public PointF mWakeUpTouchLocation;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    final WakefulnessLifecycle.Observer mWakefulnessObserver = new WakefulnessLifecycle.Observer() {
        public void onFinishedGoingToSleep() {
            CentralSurfacesImpl.this.mNotificationPanelViewController.onAffordanceLaunchEnded();
            CentralSurfacesImpl.this.releaseGestureWakeLock();
            boolean unused = CentralSurfacesImpl.this.mLaunchCameraWhenFinishedWaking = false;
            CentralSurfacesImpl.this.mDeviceInteractive = false;
            boolean unused2 = CentralSurfacesImpl.this.mWakeUpComingFromTouch = false;
            PointF unused3 = CentralSurfacesImpl.this.mWakeUpTouchLocation = null;
            CentralSurfacesImpl.this.updateVisibleToUser();
            CentralSurfacesImpl.this.updateNotificationPanelTouchState();
            CentralSurfacesImpl.this.mNotificationShadeWindowViewController.cancelCurrentTouch();
            if (CentralSurfacesImpl.this.mLaunchCameraOnFinishedGoingToSleep) {
                boolean unused4 = CentralSurfacesImpl.this.mLaunchCameraOnFinishedGoingToSleep = false;
                CentralSurfacesImpl.this.mMainExecutor.execute(new CentralSurfacesImpl$11$$ExternalSyntheticLambda1(this));
            }
            if (CentralSurfacesImpl.this.mLaunchEmergencyActionOnFinishedGoingToSleep) {
                boolean unused5 = CentralSurfacesImpl.this.mLaunchEmergencyActionOnFinishedGoingToSleep = false;
                CentralSurfacesImpl.this.mMainExecutor.execute(new CentralSurfacesImpl$11$$ExternalSyntheticLambda2(this));
            }
            CentralSurfacesImpl.this.updateIsKeyguard();
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).onFinishedGoingToSleep();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFinishedGoingToSleep$0$com-android-systemui-statusbar-phone-CentralSurfacesImpl$11 */
        public /* synthetic */ void mo43928x2663190c() {
            CentralSurfacesImpl.this.mCommandQueueCallbacks.onCameraLaunchGestureDetected(CentralSurfacesImpl.this.mLastCameraLaunchSource);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFinishedGoingToSleep$1$com-android-systemui-statusbar-phone-CentralSurfacesImpl$11 */
        public /* synthetic */ void mo43929xe0d8b98d() {
            CentralSurfacesImpl.this.mCommandQueueCallbacks.onEmergencyActionLaunchGestureDetected();
        }

        public void onStartedGoingToSleep() {
            DejankUtils.startDetectingBlockingIpcs("CentralSurfaces#onStartedGoingToSleep");
            CentralSurfacesImpl.this.cancelAfterLaunchTransitionRunnables();
            CentralSurfacesImpl.this.updateRevealEffect(false);
            CentralSurfacesImpl.this.updateNotificationPanelTouchState();
            CentralSurfacesImpl.this.maybeEscalateHeadsUp();
            CentralSurfacesImpl.this.dismissVolumeDialog();
            CentralSurfacesImpl.this.mWakeUpCoordinator.setFullyAwake(false);
            CentralSurfacesImpl.this.mKeyguardBypassController.onStartedGoingToSleep();
            if (CentralSurfacesImpl.this.mDozeParameters.shouldShowLightRevealScrim()) {
                CentralSurfacesImpl.this.makeExpandedVisible(true);
            }
            DejankUtils.stopDetectingBlockingIpcs("CentralSurfaces#onStartedGoingToSleep");
        }

        public void onStartedWakingUp() {
            CentralSurfacesImpl.this.updateRevealEffect(true);
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).onStartedWakingUp();
            DejankUtils.startDetectingBlockingIpcs("CentralSurfaces#onStartedWakingUp");
            CentralSurfacesImpl.this.mNotificationShadeWindowController.batchApplyWindowLayoutParams(new CentralSurfacesImpl$11$$ExternalSyntheticLambda0(this));
            DejankUtils.stopDetectingBlockingIpcs("CentralSurfaces#onStartedWakingUp");
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStartedWakingUp$2$com-android-systemui-statusbar-phone-CentralSurfacesImpl$11 */
        public /* synthetic */ void mo43930x71be48ff() {
            CentralSurfacesImpl.this.mDeviceInteractive = true;
            CentralSurfacesImpl.this.mWakeUpCoordinator.setWakingUp(true);
            if (!CentralSurfacesImpl.this.mKeyguardBypassController.getBypassEnabled()) {
                CentralSurfacesImpl.this.mHeadsUpManager.releaseAllImmediately();
            }
            CentralSurfacesImpl.this.updateVisibleToUser();
            CentralSurfacesImpl.this.updateIsKeyguard();
            CentralSurfacesImpl.this.mDozeServiceHost.stopDozing();
            CentralSurfacesImpl.this.updateNotificationPanelTouchState();
            if (CentralSurfacesImpl.this.mScreenOffAnimationController.shouldHideLightRevealScrimOnWakeUp()) {
                CentralSurfacesImpl.this.makeExpandedInvisible();
            }
        }

        public void onFinishedWakingUp() {
            CentralSurfacesImpl.this.mWakeUpCoordinator.setFullyAwake(true);
            CentralSurfacesImpl.this.mWakeUpCoordinator.setWakingUp(false);
            if (CentralSurfacesImpl.this.mKeyguardStateController.isOccluded() && !CentralSurfacesImpl.this.mDozeParameters.canControlUnlockedScreenOff()) {
                CentralSurfacesImpl.this.mLockscreenUserManager.updatePublicMode();
                CentralSurfacesImpl.this.mNotificationPanelViewController.getNotificationStackScrollLayoutController().updateSensitivenessForOccludedWakeup();
            }
            if (CentralSurfacesImpl.this.mLaunchCameraWhenFinishedWaking) {
                CentralSurfacesImpl.this.mNotificationPanelViewController.launchCamera(false, CentralSurfacesImpl.this.mLastCameraLaunchSource);
                boolean unused = CentralSurfacesImpl.this.mLaunchCameraWhenFinishedWaking = false;
            }
            if (CentralSurfacesImpl.this.mLaunchEmergencyActionWhenFinishedWaking) {
                boolean unused2 = CentralSurfacesImpl.this.mLaunchEmergencyActionWhenFinishedWaking = false;
                Intent emergencyActionIntent = CentralSurfacesImpl.this.getEmergencyActionIntent();
                if (emergencyActionIntent != null) {
                    CentralSurfacesImpl.this.mContext.startActivityAsUser(emergencyActionIntent, CentralSurfacesImpl.this.getActivityUserHandle(emergencyActionIntent));
                }
            }
            CentralSurfacesImpl.this.updateScrimController();
        }
    };
    private final BroadcastReceiver mWallpaperChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!CentralSurfacesImpl.this.mWallpaperSupported) {
                Log.wtf(CentralSurfaces.TAG, "WallpaperManager not supported");
                return;
            }
            WallpaperInfo wallpaperInfo = CentralSurfacesImpl.this.mWallpaperManager.getWallpaperInfo(-2);
            CentralSurfacesImpl.this.mWallpaperController.onWallpaperInfoUpdated(wallpaperInfo);
            boolean z = CentralSurfacesImpl.this.mContext.getResources().getBoolean(17891613) && wallpaperInfo != null && wallpaperInfo.supportsAmbientMode();
            CentralSurfacesImpl.this.mNotificationShadeWindowController.setWallpaperSupportsAmbientMode(z);
            CentralSurfacesImpl.this.mScrimController.setWallpaperSupportsAmbientMode(z);
            CentralSurfacesImpl.this.mKeyguardViewMediator.setWallpaperSupportsAmbientMode(z);
        }
    };
    /* access modifiers changed from: private */
    public final WallpaperController mWallpaperController;
    /* access modifiers changed from: private */
    public final WallpaperManager mWallpaperManager;
    /* access modifiers changed from: private */
    public boolean mWallpaperSupported;
    protected WindowManager mWindowManager;
    protected IWindowManager mWindowManagerService;

    private static int getLoggingFingerprint(int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        return (i & 255) | ((z ? 1 : 0) << true) | ((z2 ? 1 : 0) << true) | ((z3 ? 1 : 0) << true) | ((z4 ? 1 : 0) << true) | ((z5 ? 1 : 0) << true);
    }

    public boolean isFalsingThresholdNeeded() {
        return true;
    }

    public void onTrackingStopped(boolean z) {
    }

    static {
        boolean z = false;
        try {
            IPackageManager asInterface = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
            if (asInterface != null && asInterface.isOnlyCoreApps()) {
                z = true;
            }
        } catch (RemoteException unused) {
        }
        ONLY_CORE_APPS = z;
    }

    /* access modifiers changed from: package-private */
    public void onStatusBarWindowStateChanged(int i) {
        updateBubblesVisibility();
        this.mStatusBarWindowState = i;
    }

    public void acquireGestureWakeLock(long j) {
        this.mGestureWakeLock.acquire(j);
    }

    public boolean setAppearance(int i) {
        if (this.mAppearance == i) {
            return false;
        }
        this.mAppearance = i;
        return updateBarMode(barMode(isTransientShown(), i));
    }

    public int getBarMode() {
        return this.mStatusBarMode;
    }

    public void resendMessage(int i) {
        this.mMessageRouter.cancelMessages(i);
        this.mMessageRouter.sendMessage(i);
    }

    public void resendMessage(Object obj) {
        this.mMessageRouter.cancelMessages(obj.getClass());
        this.mMessageRouter.sendMessage(obj);
    }

    public int getDisabled1() {
        return this.mDisabled1;
    }

    public void setDisabled1(int i) {
        this.mDisabled1 = i;
    }

    public int getDisabled2() {
        return this.mDisabled2;
    }

    public void setDisabled2(int i) {
        this.mDisabled2 = i;
    }

    public void setLastCameraLaunchSource(int i) {
        this.mLastCameraLaunchSource = i;
    }

    public void setLaunchCameraOnFinishedGoingToSleep(boolean z) {
        this.mLaunchCameraOnFinishedGoingToSleep = z;
    }

    public void setLaunchCameraOnFinishedWaking(boolean z) {
        this.mLaunchCameraWhenFinishedWaking = z;
    }

    public void setLaunchEmergencyActionOnFinishedGoingToSleep(boolean z) {
        this.mLaunchEmergencyActionOnFinishedGoingToSleep = z;
    }

    public void setLaunchEmergencyActionOnFinishedWaking(boolean z) {
        this.mLaunchEmergencyActionWhenFinishedWaking = z;
    }

    public void setTopHidesStatusBar(boolean z) {
        this.mTopHidesStatusBar = z;
    }

    public QSPanelController getQSPanelController() {
        return this.mQSPanelController;
    }

    public void animateExpandNotificationsPanel() {
        this.mCommandQueueCallbacks.animateExpandNotificationsPanel();
    }

    public void animateExpandSettingsPanel(String str) {
        this.mCommandQueueCallbacks.animateExpandSettingsPanel(str);
    }

    public void animateCollapsePanels(int i, boolean z) {
        this.mCommandQueueCallbacks.animateCollapsePanels(i, z);
    }

    public void togglePanel() {
        this.mCommandQueueCallbacks.togglePanel();
    }

    public enum StatusBarUiEvent implements UiEventLogger.UiEventEnum {
        LOCKSCREEN_OPEN_SECURE(HttpURLConnection.HTTP_BAD_METHOD),
        LOCKSCREEN_OPEN_INSECURE(HttpURLConnection.HTTP_NOT_ACCEPTABLE),
        LOCKSCREEN_CLOSE_SECURE(HttpURLConnection.HTTP_PROXY_AUTH),
        LOCKSCREEN_CLOSE_INSECURE(HttpURLConnection.HTTP_CLIENT_TIMEOUT),
        BOUNCER_OPEN_SECURE(HttpURLConnection.HTTP_CONFLICT),
        BOUNCER_OPEN_INSECURE(HttpURLConnection.HTTP_GONE),
        BOUNCER_CLOSE_SECURE(HttpURLConnection.HTTP_LENGTH_REQUIRED),
        BOUNCER_CLOSE_INSECURE(HttpURLConnection.HTTP_PRECON_FAILED);
        
        private final int mId;

        private StatusBarUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43889xebb0e365(ColorExtractor colorExtractor, int i) {
        updateTheme();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public CentralSurfacesImpl(Context context, NotificationsController notificationsController, FragmentService fragmentService, LightBarController lightBarController, AutoHideController autoHideController, StatusBarWindowController statusBarWindowController, StatusBarWindowStateController statusBarWindowStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarSignalPolicy statusBarSignalPolicy, PulseExpansionHandler pulseExpansionHandler, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, KeyguardStateController keyguardStateController, HeadsUpManagerPhone headsUpManagerPhone, DynamicPrivacyController dynamicPrivacyController, FalsingManager falsingManager, FalsingCollector falsingCollector, BroadcastDispatcher broadcastDispatcher, NotifShadeEventSource notifShadeEventSource, NotificationEntryManager notificationEntryManager, NotificationGutsManager notificationGutsManager, NotificationLogger notificationLogger, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationViewHierarchyManager notificationViewHierarchyManager, PanelExpansionStateManager panelExpansionStateManager, KeyguardViewMediator keyguardViewMediator, DisplayMetrics displayMetrics, MetricsLogger metricsLogger, @UiBackground Executor executor, NotificationMediaManager notificationMediaManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationRemoteInputManager notificationRemoteInputManager, UserSwitcherController userSwitcherController, NetworkController networkController, BatteryController batteryController, SysuiColorExtractor sysuiColorExtractor, ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle, SysuiStatusBarStateController sysuiStatusBarStateController, Optional<BubblesManager> optional, Optional<Bubbles> optional2, VisualStabilityManager visualStabilityManager, DeviceProvisionedController deviceProvisionedController, NavigationBarController navigationBarController, AccessibilityFloatingMenuController accessibilityFloatingMenuController, Lazy<AssistManager> lazy, ConfigurationController configurationController, NotificationShadeWindowController notificationShadeWindowController, DozeParameters dozeParameters, ScrimController scrimController, Lazy<LockscreenWallpaper> lazy2, LockscreenGestureLogger lockscreenGestureLogger, Lazy<BiometricUnlockController> lazy3, DozeServiceHost dozeServiceHost, PowerManager powerManager, ScreenPinningRequest screenPinningRequest, DozeScrimController dozeScrimController, VolumeComponent volumeComponent, CommandQueue commandQueue, CentralSurfacesComponent.Factory factory, PluginManager pluginManager, ShadeController shadeController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, ViewMediatorCallback viewMediatorCallback, InitController initController, @Named("time_tick_handler") Handler handler, PluginDependencyProvider pluginDependencyProvider, KeyguardDismissUtil keyguardDismissUtil, ExtensionController extensionController, UserInfoControllerImpl userInfoControllerImpl, PhoneStatusBarPolicy phoneStatusBarPolicy, KeyguardIndicationController keyguardIndicationController, DemoModeController demoModeController, Lazy<NotificationShadeDepthController> lazy4, StatusBarTouchableRegionManager statusBarTouchableRegionManager, NotificationIconAreaController notificationIconAreaController, BrightnessSliderController.Factory factory2, ScreenOffAnimationController screenOffAnimationController, WallpaperController wallpaperController, OngoingCallController ongoingCallController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, FeatureFlags featureFlags, KeyguardUnlockAnimationController keyguardUnlockAnimationController, @Main Handler handler2, @Main DelayableExecutor delayableExecutor, @Main MessageRouter messageRouter, WallpaperManager wallpaperManager, Optional<StartingSurface> optional3, ActivityLaunchAnimator activityLaunchAnimator, NotifPipelineFlags notifPipelineFlags, InteractionJankMonitor interactionJankMonitor, DeviceStateManager deviceStateManager, DreamOverlayStateController dreamOverlayStateController, WiredChargingRippleController wiredChargingRippleController, IDreamManager iDreamManager) {
        super(context);
        PanelExpansionStateManager panelExpansionStateManager2 = panelExpansionStateManager;
        DozeParameters dozeParameters2 = dozeParameters;
        OngoingCallController ongoingCallController2 = ongoingCallController;
        LockscreenShadeTransitionController lockscreenShadeTransitionController2 = lockscreenShadeTransitionController;
        DelayableExecutor delayableExecutor2 = delayableExecutor;
        MessageRouter messageRouter2 = messageRouter;
        this.mNotificationsController = notificationsController;
        this.mFragmentService = fragmentService;
        this.mLightBarController = lightBarController;
        this.mAutoHideController = autoHideController;
        this.mStatusBarWindowController = statusBarWindowController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mPulseExpansionHandler = pulseExpansionHandler;
        this.mWakeUpCoordinator = notificationWakeUpCoordinator;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mKeyguardStateController = keyguardStateController;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mKeyguardIndicationController = keyguardIndicationController;
        this.mStatusBarTouchableRegionManager = statusBarTouchableRegionManager;
        this.mDynamicPrivacyController = dynamicPrivacyController;
        this.mFalsingCollector = falsingCollector;
        this.mFalsingManager = falsingManager;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mNotifShadeEventSource = notifShadeEventSource;
        this.mEntryManager = notificationEntryManager;
        this.mGutsManager = notificationGutsManager;
        this.mNotificationLogger = notificationLogger;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mViewHierarchyManager = notificationViewHierarchyManager;
        this.mPanelExpansionStateManager = panelExpansionStateManager2;
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
        this.mBubblesManagerOptional = optional;
        this.mBubblesOptional = optional2;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mNavigationBarController = navigationBarController;
        this.mAccessibilityFloatingMenuController = accessibilityFloatingMenuController;
        this.mAssistManagerLazy = lazy;
        this.mConfigurationController = configurationController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mDozeServiceHost = dozeServiceHost;
        this.mPowerManager = powerManager;
        this.mDozeParameters = dozeParameters2;
        this.mScrimController = scrimController;
        this.mLockscreenWallpaperLazy = lazy2;
        this.mLockscreenGestureLogger = lockscreenGestureLogger;
        this.mScreenPinningRequest = screenPinningRequest;
        this.mDozeScrimController = dozeScrimController;
        this.mBiometricUnlockControllerLazy = lazy3;
        this.mNotificationShadeDepthControllerLazy = lazy4;
        this.mVolumeComponent = volumeComponent;
        this.mCommandQueue = commandQueue;
        this.mCentralSurfacesComponentFactory = factory;
        this.mPluginManager = pluginManager;
        this.mShadeController = shadeController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardViewMediatorCallback = viewMediatorCallback;
        this.mInitController = initController;
        this.mPluginDependencyProvider = pluginDependencyProvider;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mExtensionController = extensionController;
        this.mUserInfoControllerImpl = userInfoControllerImpl;
        this.mIconPolicy = phoneStatusBarPolicy;
        this.mDemoModeController = demoModeController;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mBrightnessSliderFactory = factory2;
        this.mWallpaperController = wallpaperController;
        this.mOngoingCallController = ongoingCallController2;
        this.mStatusBarSignalPolicy = statusBarSignalPolicy;
        this.mStatusBarHideIconsForBouncerManager = statusBarHideIconsForBouncerManager;
        this.mFeatureFlags = featureFlags;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mMainHandler = handler2;
        this.mMainExecutor = delayableExecutor2;
        this.mMessageRouter = messageRouter2;
        this.mWallpaperManager = wallpaperManager;
        this.mJankMonitor = interactionJankMonitor;
        this.mDreamOverlayStateController = dreamOverlayStateController;
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController2;
        this.mStartingSurfaceOptional = optional3;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mDreamManager = iDreamManager;
        lockscreenShadeTransitionController2.setCentralSurfaces(this);
        statusBarWindowStateController.addListener(new CentralSurfacesImpl$$ExternalSyntheticLambda46(this));
        this.mScreenOffAnimationController = screenOffAnimationController;
        panelExpansionStateManager2.addExpansionListener(new CentralSurfacesImpl$$ExternalSyntheticLambda47(this));
        this.mBubbleExpandListener = new CentralSurfacesImpl$$ExternalSyntheticLambda48(this);
        this.mActivityIntentHelper = new ActivityIntentHelper(this.mContext);
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        ongoingCallController2.addCallback((OngoingCallListener) new CentralSurfacesImpl$$ExternalSyntheticLambda49(this));
        DateTimeView.setReceiverHandler(handler);
        messageRouter2.subscribeTo(CentralSurfaces.KeyboardShortcutsMessage.class, new CentralSurfacesImpl$$ExternalSyntheticLambda50(this));
        messageRouter2.subscribeTo((int) CentralSurfaces.MSG_DISMISS_KEYBOARD_SHORTCUTS_MENU, (MessageRouter.SimpleMessageListener) new CentralSurfacesImpl$$ExternalSyntheticLambda1(this));
        messageRouter2.subscribeTo(AnimateExpandSettingsPanelMessage.class, new CentralSurfacesImpl$$ExternalSyntheticLambda2(this));
        messageRouter2.subscribeTo(1003, (MessageRouter.SimpleMessageListener) new CentralSurfacesImpl$$ExternalSyntheticLambda3(this));
        deviceStateManager.registerCallback(delayableExecutor2, new FoldStateListener(this.mContext, new CentralSurfacesImpl$$ExternalSyntheticLambda33(this)));
        wiredChargingRippleController.registerCallbacks();
        ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).init(this.mMainHandler, lazy3.get(), this.mDozeServiceHost, dozeParameters2);
        ((FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class)).registerCallback(this.mFaceAnimationCallback);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43891x58b11a3(boolean z, String str) {
        this.mContext.getMainExecutor().execute(new CentralSurfacesImpl$$ExternalSyntheticLambda11(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43890x789dfa84() {
        this.mNotificationsController.requestNotificationUpdate("onBubbleExpandChanged");
        updateScrimController();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43892x927828c2(boolean z) {
        maybeUpdateBarMode();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43893x1f653fe1(CentralSurfaces.KeyboardShortcutsMessage keyboardShortcutsMessage) {
        toggleKeyboardShortcuts(keyboardShortcutsMessage.mDeviceId);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$5$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43894xac525700(int i) {
        dismissKeyboardShortcuts();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$6$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43895x393f6e1f(AnimateExpandSettingsPanelMessage animateExpandSettingsPanelMessage) {
        this.mCommandQueueCallbacks.animateExpandSettingsPanel(animateExpandSettingsPanelMessage.mSubpanel);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$7$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43896xc62c853e(int i) {
        onLaunchTransitionTimeout();
    }

    public void start() {
        RegisterStatusBarResult registerStatusBarResult;
        this.mScreenLifecycle.addObserver(this.mScreenObserver);
        this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
        this.mUiModeManager = (UiModeManager) this.mContext.getSystemService(UiModeManager.class);
        if (this.mBubblesOptional.isPresent()) {
            this.mBubblesOptional.get().setExpandListener(this.mBubbleExpandListener);
        }
        this.mStatusBarSignalPolicy.init();
        this.mKeyguardIndicationController.init();
        this.mColorExtractor.addOnColorsChangedListener(this.mOnColorsChangedListener);
        this.mStatusBarStateController.addCallback(this.mStateListener, 0);
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        Display display = this.mContext.getDisplay();
        this.mDisplay = display;
        this.mDisplayId = display.getDisplayId();
        updateDisplaySize();
        this.mStatusBarHideIconsForBouncerManager.setDisplayId(this.mDisplayId);
        this.mWindowManagerService = WindowManagerGlobal.getWindowManagerService();
        this.mDevicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        this.mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
        this.mKeyguardUpdateMonitor.setKeyguardBypassController(this.mKeyguardBypassController);
        this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mKeyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
        this.mWallpaperSupported = this.mWallpaperManager.isWallpaperSupported();
        try {
            registerStatusBarResult = this.mBarService.registerStatusBar(this.mCommandQueue);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            registerStatusBarResult = null;
        }
        createAndAddWindows(registerStatusBarResult);
        if (this.mWallpaperSupported) {
            this.mBroadcastDispatcher.registerReceiver(this.mWallpaperChangedReceiver, new IntentFilter("android.intent.action.WALLPAPER_CHANGED"), (Executor) null, UserHandle.ALL);
            this.mWallpaperChangedReceiver.onReceive(this.mContext, (Intent) null);
        }
        setUpPresenter();
        if (InsetsState.containsType(registerStatusBarResult.mTransientBarTypes, 0)) {
            showTransientUnchecked();
        }
        this.mCommandQueueCallbacks.onSystemBarAttributesChanged(this.mDisplayId, registerStatusBarResult.mAppearance, registerStatusBarResult.mAppearanceRegions, registerStatusBarResult.mNavbarColorManagedByIme, registerStatusBarResult.mBehavior, registerStatusBarResult.mRequestedVisibilities, registerStatusBarResult.mPackageName);
        this.mCommandQueueCallbacks.setImeWindowStatus(this.mDisplayId, registerStatusBarResult.mImeToken, registerStatusBarResult.mImeWindowVis, registerStatusBarResult.mImeBackDisposition, registerStatusBarResult.mShowImeSwitcher);
        int size = registerStatusBarResult.mIcons.size();
        for (int i = 0; i < size; i++) {
            this.mCommandQueue.setIcon((String) registerStatusBarResult.mIcons.keyAt(i), (StatusBarIcon) registerStatusBarResult.mIcons.valueAt(i));
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BANNER_ACTION_CANCEL);
        intentFilter.addAction(BANNER_ACTION_SETUP);
        this.mContext.registerReceiver(this.mBannerActionBroadcastReceiver, intentFilter, "com.android.systemui.permission.SELF", (Handler) null, 2);
        if (this.mWallpaperSupported) {
            try {
                IWallpaperManager.Stub.asInterface(ServiceManager.getService("wallpaper")).setInAmbientMode(false, 0);
            } catch (RemoteException unused) {
            }
        }
        this.mIconPolicy.init(this.mContext);
        this.mKeyguardStateController.addCallback(new KeyguardStateController.Callback() {
            public void onUnlockedChanged() {
                CentralSurfacesImpl.this.logStateToEventlog();
            }

            public void onKeyguardGoingAwayChanged() {
                if (!CentralSurfacesImpl.this.mKeyguardStateController.isKeyguardGoingAway()) {
                    if (CentralSurfacesImpl.this.mLightRevealScrim.getRevealAmount() != 1.0f) {
                        Log.e(CentralSurfaces.TAG, "Keyguard is done going away, but someone left the light reveal scrim at reveal amount: " + CentralSurfacesImpl.this.mLightRevealScrim.getRevealAmount());
                    }
                    if (!CentralSurfacesImpl.this.mAuthRippleController.isAnimatingLightRevealScrim()) {
                        CentralSurfacesImpl.this.mLightRevealScrim.setRevealAmount(1.0f);
                    }
                }
            }
        });
        startKeyguard();
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateCallback);
        this.mDozeServiceHost.initialize(this, this.mStatusBarKeyguardViewManager, this.mNotificationShadeWindowViewController, this.mNotificationPanelViewController, this.mAmbientIndicationContainer);
        updateLightRevealScrimVisibility();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mBatteryController.observe((Lifecycle) this.mLifecycle, this.mBatteryStateChangeCallback);
        this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
        this.mAccessibilityFloatingMenuController.init();
        this.mInitController.addPostInitTask(new CentralSurfacesImpl$$ExternalSyntheticLambda42(this, registerStatusBarResult.mDisabledFlags1, registerStatusBarResult.mDisabledFlags2));
        this.mFalsingManager.addFalsingBeliefListener(this.mFalsingBeliefListener);
        this.mPluginManager.addPluginListener(new PluginListener<OverlayPlugin>() {
            /* access modifiers changed from: private */
            public final ArraySet<OverlayPlugin> mOverlays = new ArraySet<>();

            public void onPluginConnected(OverlayPlugin overlayPlugin, Context context) {
                CentralSurfacesImpl.this.mMainExecutor.execute(new CentralSurfacesImpl$3$$ExternalSyntheticLambda1(this, overlayPlugin));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onPluginConnected$0$com-android-systemui-statusbar-phone-CentralSurfacesImpl$3 */
            public /* synthetic */ void mo43944x878737bd(OverlayPlugin overlayPlugin) {
                overlayPlugin.setup(CentralSurfacesImpl.this.getNotificationShadeWindowView(), CentralSurfacesImpl.this.getNavigationBarView(), new Callback(overlayPlugin), CentralSurfacesImpl.this.mDozeParameters);
            }

            public void onPluginDisconnected(OverlayPlugin overlayPlugin) {
                CentralSurfacesImpl.this.mMainExecutor.execute(new CentralSurfacesImpl$3$$ExternalSyntheticLambda0(this, overlayPlugin));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onPluginDisconnected$1$com-android-systemui-statusbar-phone-CentralSurfacesImpl$3 */
            public /* synthetic */ void mo43945xe44080ac(OverlayPlugin overlayPlugin) {
                this.mOverlays.remove(overlayPlugin);
                CentralSurfacesImpl.this.mNotificationShadeWindowController.setForcePluginOpen(this.mOverlays.size() != 0, this);
            }

            /* renamed from: com.android.systemui.statusbar.phone.CentralSurfacesImpl$3$Callback */
            class Callback implements OverlayPlugin.Callback {
                private final OverlayPlugin mPlugin;

                Callback(OverlayPlugin overlayPlugin) {
                    this.mPlugin = overlayPlugin;
                }

                public void onHoldStatusBarOpenChange() {
                    if (this.mPlugin.holdStatusBarOpen()) {
                        C29363.this.mOverlays.add(this.mPlugin);
                    } else {
                        C29363.this.mOverlays.remove(this.mPlugin);
                    }
                    CentralSurfacesImpl.this.mMainExecutor.execute(new CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda1(this));
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onHoldStatusBarOpenChange$2$com-android-systemui-statusbar-phone-CentralSurfacesImpl$3$Callback */
                public /* synthetic */ void mo43949xceb067d4() {
                    CentralSurfacesImpl.this.mNotificationShadeWindowController.setStateListener(new CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda0(this));
                    CentralSurfacesImpl.this.mNotificationShadeWindowController.setForcePluginOpen(C29363.this.mOverlays.size() != 0, this);
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onHoldStatusBarOpenChange$1$com-android-systemui-statusbar-phone-CentralSurfacesImpl$3$Callback */
                public /* synthetic */ void mo43948xdd5ed853(boolean z) {
                    C29363.this.mOverlays.forEach(new CentralSurfacesImpl$3$Callback$$ExternalSyntheticLambda2(z));
                }
            }
        }, OverlayPlugin.class, true);
        this.mStartingSurfaceOptional.ifPresent(new CentralSurfacesImpl$$ExternalSyntheticLambda43(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$start$11$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43903x3b3c5e95(StartingSurface startingSurface) {
        startingSurface.setSysuiProxy(new CentralSurfacesImpl$$ExternalSyntheticLambda6(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$start$10$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43902xae4f4776(boolean z, String str) {
        this.mMainExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda34(this, z, str));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$start$9$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43905x1bcd4c3a(boolean z, String str) {
        this.mNotificationShadeWindowController.setRequestTopUi(z, str);
    }

    /* access modifiers changed from: private */
    public void onFoldedStateChanged(boolean z, boolean z2) {
        Trace.beginSection("CentralSurfaces#onFoldedStateChanged");
        onFoldedStateChangedInternal(z, z2);
        Trace.endSection();
    }

    private void onFoldedStateChangedInternal(boolean z, boolean z2) {
        if (this.mShadeController.isShadeOpen() && !z2) {
            this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
            if (this.mKeyguardStateController.isShowing()) {
                this.mCloseQsBeforeScreenOff = true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void makeStatusBarView(RegisterStatusBarResult registerStatusBarResult) {
        updateDisplaySize();
        updateResources();
        updateTheme();
        inflateStatusBarWindow();
        this.mNotificationShadeWindowView.setOnTouchListener(getStatusBarWindowTouchListener());
        this.mWallpaperController.setRootView(this.mNotificationShadeWindowView);
        this.mNotificationLogger.setUpWithContainer(this.mNotifListContainer);
        this.mNotificationIconAreaController.setupShelf(this.mNotificationShelfController);
        this.mPanelExpansionStateManager.addExpansionListener(this.mWakeUpCoordinator);
        this.mUserSwitcherController.init(this.mNotificationShadeWindowView);
        this.mPluginDependencyProvider.allowPluginDependency(DarkIconDispatcher.class);
        this.mPluginDependencyProvider.allowPluginDependency(StatusBarStateController.class);
        StatusBarInitializer statusBarInitializer = this.mCentralSurfacesComponent.getStatusBarInitializer();
        statusBarInitializer.setStatusBarViewUpdatedListener(new CentralSurfacesImpl$$ExternalSyntheticLambda24(this));
        statusBarInitializer.initializeStatusBar(this.mCentralSurfacesComponent);
        this.mStatusBarTouchableRegionManager.setup(this, this.mNotificationShadeWindowView);
        this.mHeadsUpManager.addListener(this.mNotificationPanelViewController.getOnHeadsUpChangedListener());
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mHeadsUpManager.addListener(this.mVisualStabilityManager);
        }
        this.mNotificationPanelViewController.setHeadsUpManager(this.mHeadsUpManager);
        createNavigationBar(registerStatusBarResult);
        if (this.mWallpaperSupported) {
            this.mLockscreenWallpaper = this.mLockscreenWallpaperLazy.get();
        }
        this.mNotificationPanelViewController.setKeyguardIndicationController(this.mKeyguardIndicationController);
        this.mAmbientIndicationContainer = this.mNotificationShadeWindowView.findViewById(C1893R.C1897id.ambient_indication_container);
        this.mAutoHideController.setStatusBar(new AutoHideUiElement() {
            public void synchronizeState() {
                CentralSurfacesImpl.this.checkBarModes();
            }

            public boolean shouldHideOnTouch() {
                return !CentralSurfacesImpl.this.mRemoteInputManager.isRemoteInputActive();
            }

            public boolean isVisible() {
                return CentralSurfacesImpl.this.isTransientShown();
            }

            public void hide() {
                CentralSurfacesImpl.this.clearTransient();
            }
        });
        this.mScrimController.setScrimVisibleListener(new CentralSurfacesImpl$$ExternalSyntheticLambda25(this));
        this.mScrimController.attachViews((ScrimView) this.mNotificationShadeWindowView.findViewById(C1893R.C1897id.scrim_behind), (ScrimView) this.mNotificationShadeWindowView.findViewById(C1893R.C1897id.scrim_notifications), (ScrimView) this.mNotificationShadeWindowView.findViewById(C1893R.C1897id.scrim_in_front));
        LightRevealScrim lightRevealScrim = (LightRevealScrim) this.mNotificationShadeWindowView.findViewById(C1893R.C1897id.light_reveal_scrim);
        this.mLightRevealScrim = lightRevealScrim;
        lightRevealScrim.setScrimOpaqueChangedListener(new CentralSurfacesImpl$$ExternalSyntheticLambda26(this));
        this.mScreenOffAnimationController.initialize(this, this.mLightRevealScrim);
        updateLightRevealScrimVisibility();
        this.mNotificationPanelViewController.initDependencies(this, new CentralSurfacesImpl$$ExternalSyntheticLambda27(this), this.mNotificationShelfController);
        BackDropView backDropView = (BackDropView) this.mNotificationShadeWindowView.findViewById(C1893R.C1897id.backdrop);
        this.mMediaManager.setup(backDropView, (ImageView) backDropView.findViewById(C1893R.C1897id.backdrop_front), (ImageView) backDropView.findViewById(C1893R.C1897id.backdrop_back), this.mScrimController, this.mLockscreenWallpaper);
        this.mNotificationShadeDepthControllerLazy.get().addListener(new CentralSurfacesImpl$$ExternalSyntheticLambda28(this.mContext.getResources().getFloat(17105119), backDropView));
        this.mNotificationPanelViewController.setUserSetupComplete(this.mUserSetup);
        View findViewById = this.mNotificationShadeWindowView.findViewById(C1893R.C1897id.qs_frame);
        if (findViewById != null) {
            FragmentHostManager fragmentHostManager = FragmentHostManager.get(findViewById);
            ExtensionFragmentListener.attachExtensonToFragment(findViewById, C2301QS.TAG, C1893R.C1897id.qs_frame, this.mExtensionController.newExtension(C2301QS.class).withPlugin(C2301QS.class).withDefault(new CentralSurfacesImpl$$ExternalSyntheticLambda29(this)).build());
            this.mBrightnessMirrorController = new BrightnessMirrorController(this.mNotificationShadeWindowView, this.mNotificationPanelViewController, this.mNotificationShadeDepthControllerLazy.get(), this.mBrightnessSliderFactory, new CentralSurfacesImpl$$ExternalSyntheticLambda30(this));
            fragmentHostManager.addTagListener(C2301QS.TAG, new CentralSurfacesImpl$$ExternalSyntheticLambda31(this));
        }
        View findViewById2 = this.mNotificationShadeWindowView.findViewById(C1893R.C1897id.report_rejected_touch);
        this.mReportRejectedTouch = findViewById2;
        if (findViewById2 != null) {
            updateReportRejectedTouchVisibility();
            this.mReportRejectedTouch.setOnClickListener(new CentralSurfacesImpl$$ExternalSyntheticLambda32(this));
        }
        if (!this.mPowerManager.isInteractive()) {
            this.mBroadcastReceiver.onReceive(this.mContext, new Intent("android.intent.action.SCREEN_OFF"));
        }
        this.mGestureWakeLock = this.mPowerManager.newWakeLock(10, "sysui:GestureWakeLock");
        registerBroadcastReceiver();
        this.mContext.registerReceiverAsUser(this.mDemoReceiver, UserHandle.ALL, new IntentFilter(), "android.permission.DUMP", (Handler) null, 2);
        this.mDeviceProvisionedController.addCallback(this.mUserSetupObserver);
        this.mUserSetupObserver.onUserSetupChanged();
        ThreadedRenderer.overrideProperty("disableProfileBars", "true");
        ThreadedRenderer.overrideProperty("ambientRatio", String.valueOf(1.5f));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeStatusBarView$12$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43881xdbef998a(PhoneStatusBarView phoneStatusBarView, PhoneStatusBarViewController phoneStatusBarViewController, PhoneStatusBarTransitions phoneStatusBarTransitions) {
        this.mStatusBarView = phoneStatusBarView;
        this.mPhoneStatusBarViewController = phoneStatusBarViewController;
        this.mStatusBarTransitions = phoneStatusBarTransitions;
        this.mNotificationShadeWindowViewController.setStatusBarViewController(phoneStatusBarViewController);
        this.mNotificationPanelViewController.updatePanelExpansionAndVisibility();
        setBouncerShowingForStatusBarComponents(this.mBouncerShowing);
        checkBarModes();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeStatusBarView$13$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43882x68dcb0a9(Integer num) {
        this.mNotificationShadeWindowController.setScrimsVisibility(num.intValue());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeStatusBarView$15$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43884x82b6dee7(Boolean bool) {
        CentralSurfacesImpl$$ExternalSyntheticLambda8 centralSurfacesImpl$$ExternalSyntheticLambda8 = new CentralSurfacesImpl$$ExternalSyntheticLambda8(this);
        if (bool.booleanValue()) {
            this.mLightRevealScrim.post(centralSurfacesImpl$$ExternalSyntheticLambda8);
        } else {
            centralSurfacesImpl$$ExternalSyntheticLambda8.run();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeStatusBarView$14$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43883xf5c9c7c8() {
        this.mNotificationShadeWindowController.setLightRevealScrimOpaque(this.mLightRevealScrim.isScrimOpaque());
        this.mScreenOffAnimationController.onScrimOpaqueChanged(this.mLightRevealScrim.isScrimOpaque());
    }

    static /* synthetic */ void lambda$makeStatusBarView$16(float f, BackDropView backDropView, float f2) {
        float lerp = MathUtils.lerp(f, 1.0f, f2);
        backDropView.setPivotX(((float) backDropView.getWidth()) / 2.0f);
        backDropView.setPivotY(((float) backDropView.getHeight()) / 2.0f);
        backDropView.setScaleX(lerp);
        backDropView.setScaleY(lerp);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeStatusBarView$17$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43885x9c910d25(Boolean bool) {
        this.mBrightnessMirrorVisible = bool.booleanValue();
        updateScrimController();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeStatusBarView$18$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43886x297e2444(String str, Fragment fragment) {
        C2301QS qs = (C2301QS) fragment;
        if (qs instanceof QSFragment) {
            QSFragment qSFragment = (QSFragment) qs;
            this.mQSPanelController = qSFragment.getQSPanelController();
            qSFragment.setBrightnessMirrorController(this.mBrightnessMirrorController);
            ((QSTileHostEx) NTDependencyEx.get(QSTileHostEx.class)).init(this.mShadeController, this.mNotificationPanelViewController);
            this.mNetworkController.setQSHost(this.mQSPanelController.getHost());
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$makeStatusBarView$19$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43887xb66b3b63(View view) {
        Uri reportRejectedTouch = this.mFalsingManager.reportRejectedTouch();
        if (reportRejectedTouch != null) {
            StringWriter stringWriter = new StringWriter();
            stringWriter.write("Build info: ");
            stringWriter.write(SystemProperties.get("ro.build.description"));
            stringWriter.write("\nSerial number: ");
            stringWriter.write(SystemProperties.get("ro.serialno"));
            stringWriter.write("\n");
            startActivityDismissingKeyguard(Intent.createChooser(new Intent("android.intent.action.SEND").setType("*/*").putExtra("android.intent.extra.SUBJECT", "Rejected touch report").putExtra("android.intent.extra.STREAM", reportRejectedTouch).putExtra("android.intent.extra.TEXT", stringWriter.toString()), "Share rejected touch report").addFlags(268435456), true, true);
        }
    }

    private void dispatchPanelExpansionForKeyguardDismiss(float f, boolean z) {
        if (isKeyguardShowing() && !isOccluded() && this.mKeyguardStateController.canDismissLockScreen() && !this.mKeyguardViewMediator.isAnySimPinSecure()) {
            if (this.mNotificationPanelViewController.isQsExpanded() && z) {
                return;
            }
            if (z || this.mKeyguardViewMediator.isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe() || this.mKeyguardUnlockAnimationController.isUnlockingWithSmartSpaceTransition()) {
                this.mKeyguardStateController.notifyKeyguardDismissAmountChanged(1.0f - f, z);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        float fraction = panelExpansionChangeEvent.getFraction();
        dispatchPanelExpansionForKeyguardDismiss(fraction, panelExpansionChangeEvent.getTracking());
        if (fraction == 0.0f || fraction == 1.0f) {
            if (getNavigationBarView() != null) {
                getNavigationBarView().onStatusBarPanelStateChanged();
            }
            if (getNotificationPanelViewController() != null) {
                getNotificationPanelViewController().updateSystemUiStateFlags();
            }
        }
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    /* access modifiers changed from: protected */
    public void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter, (Executor) null, UserHandle.ALL);
    }

    /* access modifiers changed from: protected */
    public C2301QS createDefaultQSFragment() {
        return (C2301QS) FragmentHostManager.get(this.mNotificationShadeWindowView).create(QSFragment.class);
    }

    private void setUpPresenter() {
        this.mActivityLaunchAnimator.setCallback(this.mActivityLaunchAnimatorCallback);
        this.mActivityLaunchAnimator.addListener(this.mActivityLaunchAnimatorListener);
        this.mNotificationAnimationProvider = new NotificationLaunchAnimatorControllerProvider(this.mNotificationShadeWindowViewController, this.mNotifListContainer, this.mHeadsUpManager, this.mJankMonitor);
        this.mNotificationShelfController.setOnActivatedListener(this.mPresenter);
        this.mRemoteInputManager.addControllerCallback(this.mNotificationShadeWindowController);
        this.mStackScrollerController.setNotificationActivityStarter(this.mNotificationActivityStarter);
        this.mGutsManager.setNotificationActivityStarter(this.mNotificationActivityStarter);
        this.mNotificationsController.initialize(this.mPresenter, this.mNotifListContainer, this.mStackScrollerController.getNotifStackController(), this.mNotificationActivityStarter, this.mCentralSurfacesComponent.getBindRowCallback());
    }

    /* access modifiers changed from: protected */
    /* renamed from: setUpDisableFlags */
    public void mo43904x8ee0351b(int i, int i2) {
        ((CommandQueueEx) NTDependencyEx.get(CommandQueueEx.class)).setDisableFlagsForSetup(this.mDisplayId, i, i2);
    }

    public void wakeUpIfDozing(long j, View view, String str) {
        if (this.mDozing && this.mScreenOffAnimationController.allowWakeUpIfDozing()) {
            this.mPowerManager.wakeUp(j, 4, "com.android.systemui:" + str);
            this.mWakeUpComingFromTouch = true;
            view.getLocationInWindow(this.mTmpInt2);
            this.mWakeUpTouchLocation = new PointF((float) (this.mTmpInt2[0] + (view.getWidth() / 2)), (float) (this.mTmpInt2[1] + (view.getHeight() / 2)));
            this.mFalsingCollector.onScreenOnFromTouch();
        }
    }

    /* access modifiers changed from: protected */
    public void createNavigationBar(RegisterStatusBarResult registerStatusBarResult) {
        this.mNavigationBarController.createNavigationBars(true, registerStatusBarResult);
    }

    /* access modifiers changed from: protected */
    public View.OnTouchListener getStatusBarWindowTouchListener() {
        return new CentralSurfacesImpl$$ExternalSyntheticLambda0(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getStatusBarWindowTouchListener$20$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ boolean mo43878xe84e9e0d(View view, MotionEvent motionEvent) {
        this.mAutoHideController.checkUserAutoHide(motionEvent);
        this.mRemoteInputManager.checkRemoteInputOutside(motionEvent);
        if (motionEvent.getAction() == 1 && this.mExpandedVisible) {
            this.mShadeController.animateCollapsePanels();
        }
        return this.mNotificationShadeWindowView.onTouchEvent(motionEvent);
    }

    private void inflateStatusBarWindow() {
        CentralSurfacesComponent centralSurfacesComponent = this.mCentralSurfacesComponent;
        if (centralSurfacesComponent != null) {
            for (CentralSurfacesComponent.Startable stop : centralSurfacesComponent.getStartables()) {
                stop.stop();
            }
        }
        CentralSurfacesComponent create = this.mCentralSurfacesComponentFactory.create();
        this.mCentralSurfacesComponent = create;
        this.mFragmentService.addFragmentInstantiationProvider(create);
        this.mNotificationShadeWindowView = this.mCentralSurfacesComponent.getNotificationShadeWindowView();
        this.mNotificationShadeWindowViewController = this.mCentralSurfacesComponent.getNotificationShadeWindowViewController();
        this.mNotificationShadeWindowController.setNotificationShadeView(this.mNotificationShadeWindowView);
        this.mNotificationShadeWindowViewController.setupExpandedStatusBar();
        this.mNotificationPanelViewController = this.mCentralSurfacesComponent.getNotificationPanelViewController();
        this.mCentralSurfacesComponent.getLockIconViewController().init();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mCentralSurfacesComponent.getNotificationStackScrollLayoutController();
        this.mStackScrollerController = notificationStackScrollLayoutController;
        this.mStackScroller = notificationStackScrollLayoutController.getView();
        this.mNotifListContainer = this.mCentralSurfacesComponent.getNotificationListContainer();
        this.mPresenter = this.mCentralSurfacesComponent.getNotificationPresenter();
        this.mNotificationActivityStarter = this.mCentralSurfacesComponent.getNotificationActivityStarter();
        this.mNotificationShelfController = this.mCentralSurfacesComponent.getNotificationShelfController();
        AuthRippleController authRippleController = this.mCentralSurfacesComponent.getAuthRippleController();
        this.mAuthRippleController = authRippleController;
        authRippleController.init();
        this.mHeadsUpManager.addListener(this.mCentralSurfacesComponent.getStatusBarHeadsUpChangeListener());
        this.mDemoModeController.addCallback(this.mDemoModeCallback);
        CentralSurfacesCommandQueueCallbacks centralSurfacesCommandQueueCallbacks = this.mCommandQueueCallbacks;
        if (centralSurfacesCommandQueueCallbacks != null) {
            this.mCommandQueue.removeCallback((CommandQueue.Callbacks) centralSurfacesCommandQueueCallbacks);
        }
        CentralSurfacesCommandQueueCallbacks centralSurfacesCommandQueueCallbacks2 = this.mCentralSurfacesComponent.getCentralSurfacesCommandQueueCallbacks();
        this.mCommandQueueCallbacks = centralSurfacesCommandQueueCallbacks2;
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) centralSurfacesCommandQueueCallbacks2);
        for (CentralSurfacesComponent.Startable start : this.mCentralSurfacesComponent.getStartables()) {
            start.start();
        }
        ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewController(this.mNotificationPanelViewController);
    }

    /* access modifiers changed from: protected */
    public void startKeyguard() {
        Trace.beginSection("CentralSurfaces#startKeyguard");
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockControllerLazy.get();
        this.mBiometricUnlockController = biometricUnlockController;
        biometricUnlockController.setBiometricModeListener(new BiometricUnlockController.BiometricModeListener() {
            public void onResetMode() {
                setWakeAndUnlocking(false);
            }

            public void onModeChanged(int i) {
                if (i == 1 || i == 2 || i == 6) {
                    setWakeAndUnlocking(true);
                }
            }

            public void notifyBiometricAuthModeChanged() {
                CentralSurfacesImpl.this.notifyBiometricAuthModeChanged();
            }

            private void setWakeAndUnlocking(boolean z) {
                if (CentralSurfacesImpl.this.getNavigationBarView() != null) {
                    CentralSurfacesImpl.this.getNavigationBarView().setWakeAndUnlocking(z);
                }
            }
        });
        this.mKeyguardViewMediator.registerCentralSurfaces(this, this.mNotificationPanelViewController, this.mPanelExpansionStateManager, this.mBiometricUnlockController, this.mStackScroller, this.mKeyguardBypassController);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateControllerCallback);
        this.mKeyguardIndicationController.setStatusBarKeyguardViewManager(this.mStatusBarKeyguardViewManager);
        this.mBiometricUnlockController.setKeyguardViewController(this.mStatusBarKeyguardViewManager);
        this.mRemoteInputManager.addControllerCallback(this.mStatusBarKeyguardViewManager);
        this.mDynamicPrivacyController.setStatusBarKeyguardViewManager(this.mStatusBarKeyguardViewManager);
        this.mLightBarController.setBiometricUnlockController(this.mBiometricUnlockController);
        this.mMediaManager.setBiometricUnlockController(this.mBiometricUnlockController);
        this.mKeyguardDismissUtil.setDismissHandler(new CentralSurfacesImpl$$ExternalSyntheticLambda16(this));
        Trace.endSection();
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

    public ViewGroup getBouncerContainer() {
        return this.mNotificationShadeWindowViewController.getBouncerContainer();
    }

    public int getStatusBarHeight() {
        return this.mStatusBarWindowController.getStatusBarHeight();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
        if (ONLY_CORE_APPS == false) goto L_0x002c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateQsExpansionEnabled() {
        /*
            r2 = this;
            com.android.systemui.statusbar.policy.DeviceProvisionedController r0 = r2.mDeviceProvisionedController
            boolean r0 = r0.isDeviceProvisioned()
            if (r0 == 0) goto L_0x002b
            boolean r0 = r2.mUserSetup
            if (r0 != 0) goto L_0x0016
            com.android.systemui.statusbar.policy.UserSwitcherController r0 = r2.mUserSwitcherController
            if (r0 == 0) goto L_0x0016
            boolean r0 = r0.isSimpleUserSwitcher()
            if (r0 != 0) goto L_0x002b
        L_0x0016:
            boolean r0 = r2.isShadeDisabled()
            if (r0 != 0) goto L_0x002b
            int r0 = r2.mDisabled2
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L_0x002b
            boolean r0 = r2.mDozing
            if (r0 != 0) goto L_0x002b
            boolean r0 = ONLY_CORE_APPS
            if (r0 != 0) goto L_0x002b
            goto L_0x002c
        L_0x002b:
            r1 = 0
        L_0x002c:
            com.android.systemui.statusbar.phone.NotificationPanelViewController r2 = r2.mNotificationPanelViewController
            r2.setQsExpansionEnabledPolicy(r1)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r0 = "updateQsExpansionEnabled - QS Expand enabled: "
            r2.<init>((java.lang.String) r0)
            java.lang.StringBuilder r2 = r2.append((boolean) r1)
            java.lang.String r2 = r2.toString()
            java.lang.String r0 = "CentralSurfaces"
            android.util.Log.d(r0, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.CentralSurfacesImpl.updateQsExpansionEnabled():void");
    }

    public boolean isShadeDisabled() {
        return (this.mDisabled2 & 4) != 0;
    }

    public void requestNotificationUpdate(String str) {
        this.mNotificationsController.requestNotificationUpdate(str);
    }

    public void requestFaceAuth(boolean z) {
        if (!this.mKeyguardStateController.canDismissLockScreen()) {
            this.mKeyguardUpdateMonitor.requestFaceAuth(z);
        }
    }

    /* access modifiers changed from: private */
    public void updateReportRejectedTouchVisibility() {
        View view = this.mReportRejectedTouch;
        if (view != null) {
            view.setVisibility((this.mState != 1 || this.mDozing || !this.mFalsingCollector.isReportingEnabled()) ? 4 : 0);
        }
    }

    public boolean areNotificationAlertsDisabled() {
        return (this.mDisabled1 & 262144) != 0;
    }

    public void startActivity(Intent intent, boolean z, boolean z2, int i) {
        startActivityDismissingKeyguard(intent, z, z2, i);
    }

    public void startActivity(Intent intent, boolean z) {
        startActivityDismissingKeyguard(intent, false, z);
    }

    public void startActivity(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, boolean z2) {
        startActivity(intent, z, controller, z2, getActivityUserHandle(intent));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0012, code lost:
        if (shouldAnimateLaunch(true, r13) != false) goto L_0x0016;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startActivity(android.content.Intent r10, boolean r11, com.android.systemui.animation.ActivityLaunchAnimator.Controller r12, boolean r13, android.os.UserHandle r14) {
        /*
            r9 = this;
            com.android.systemui.statusbar.policy.KeyguardStateController r1 = r9.mKeyguardStateController
            boolean r1 = r1.isUnlocked()
            if (r1 != 0) goto L_0x0048
            if (r13 != 0) goto L_0x000b
            goto L_0x0048
        L_0x000b:
            if (r12 == 0) goto L_0x0015
            r1 = 1
            boolean r2 = r9.shouldAnimateLaunch(r1, r13)
            if (r2 == 0) goto L_0x0015
            goto L_0x0016
        L_0x0015:
            r1 = 0
        L_0x0016:
            r2 = r1
            if (r2 == 0) goto L_0x0024
            com.android.systemui.animation.ActivityLaunchAnimator$Controller r1 = r9.wrapAnimationController(r12, r11)
            com.android.systemui.statusbar.phone.CentralSurfacesImpl$6 r3 = new com.android.systemui.statusbar.phone.CentralSurfacesImpl$6
            r3.<init>(r1)
            r1 = r3
            goto L_0x002a
        L_0x0024:
            if (r11 == 0) goto L_0x0029
            r9.collapseShade()
        L_0x0029:
            r1 = 0
        L_0x002a:
            com.android.keyguard.KeyguardUpdateMonitor r3 = r9.mKeyguardUpdateMonitor
            boolean r3 = r3.isDreaming()
            if (r3 == 0) goto L_0x0035
            r9.awakenDreams()
        L_0x0035:
            com.android.systemui.animation.ActivityLaunchAnimator r3 = r9.mActivityLaunchAnimator
            java.lang.String r5 = r10.getPackage()
            com.android.systemui.statusbar.phone.CentralSurfacesImpl$$ExternalSyntheticLambda41 r6 = new com.android.systemui.statusbar.phone.CentralSurfacesImpl$$ExternalSyntheticLambda41
            r6.<init>(r9, r10, r14)
            r0 = r3
            r3 = r5
            r4 = r13
            r5 = r6
            r0.startIntentWithAnimation(r1, r2, r3, r4, r5)
            return
        L_0x0048:
            r2 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r0 = r9
            r1 = r10
            r3 = r11
            r7 = r12
            r8 = r14
            r0.startActivityDismissingKeyguard(r1, r2, r3, r4, r5, r6, r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.CentralSurfacesImpl.startActivity(android.content.Intent, boolean, com.android.systemui.animation.ActivityLaunchAnimator$Controller, boolean, android.os.UserHandle):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startActivity$21$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ Integer mo43906x2856e505(Intent intent, UserHandle userHandle, RemoteAnimationAdapter remoteAnimationAdapter) {
        return Integer.valueOf(TaskStackBuilder.create(this.mContext).addNextIntent(intent).startActivities(CentralSurfaces.getActivityOptions(getDisplayId(), remoteAnimationAdapter), userHandle));
    }

    public boolean isLaunchingActivityOverLockscreen() {
        return this.mIsLaunchingActivityOverLockscreen;
    }

    public void startActivity(Intent intent, boolean z, boolean z2) {
        startActivityDismissingKeyguard(intent, z, z2);
    }

    public void startActivity(Intent intent, boolean z, ActivityStarter.Callback callback) {
        startActivityDismissingKeyguard(intent, false, z, false, callback, 0, (ActivityLaunchAnimator.Controller) null, getActivityUserHandle(intent));
    }

    public void setQsExpanded(boolean z) {
        this.mNotificationShadeWindowController.setQsExpanded(z);
        this.mNotificationPanelViewController.setStatusAccessibilityImportance(z ? 4 : 0);
        this.mNotificationPanelViewController.updateSystemUiStateFlags();
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

    public void setPanelExpanded(boolean z) {
        if (this.mPanelExpanded != z) {
            this.mNotificationLogger.onPanelExpandedChanged(z);
        }
        this.mPanelExpanded = z;
        this.mStatusBarHideIconsForBouncerManager.setPanelExpandedAndTriggerUpdate(z);
        this.mNotificationShadeWindowController.setPanelExpanded(z);
        this.mStatusBarStateController.setPanelExpanded(z);
        if (z && this.mStatusBarStateController.getState() != 1) {
            clearNotificationEffects();
        }
        if (!z) {
            this.mRemoteInputManager.onPanelCollapsed();
        }
    }

    public ViewGroup getNotificationScrollLayout() {
        return this.mStackScroller;
    }

    public boolean isPulsing() {
        return this.mDozeServiceHost.isPulsing();
    }

    public View getAmbientIndicationContainer() {
        return this.mAmbientIndicationContainer;
    }

    public boolean isOccluded() {
        return this.mKeyguardStateController.isOccluded();
    }

    public void onLaunchAnimationCancelled(boolean z) {
        if (!this.mPresenter.isPresenterFullyCollapsed() || this.mPresenter.isCollapsing() || !z) {
            this.mShadeController.collapsePanel(true);
        } else {
            onClosingFinished();
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

    public boolean shouldAnimateLaunch(boolean z, boolean z2) {
        if (isOccluded()) {
            return false;
        }
        if (z2 || !this.mKeyguardStateController.isShowing()) {
            return true;
        }
        if (!z || !KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation) {
            return false;
        }
        return true;
    }

    public boolean shouldAnimateLaunch(boolean z) {
        return shouldAnimateLaunch(z, false);
    }

    public boolean isDeviceInVrMode() {
        return this.mPresenter.isDeviceInVrMode();
    }

    public NotificationPresenter getPresenter() {
        return this.mPresenter;
    }

    public void setBarStateForTest(int i) {
        this.mState = i;
    }

    static class AnimateExpandSettingsPanelMessage {
        final String mSubpanel;

        AnimateExpandSettingsPanelMessage(String str) {
            this.mSubpanel = str;
        }
    }

    /* access modifiers changed from: private */
    public void maybeEscalateHeadsUp() {
        this.mHeadsUpManager.getAllEntries().forEach(new CentralSurfacesImpl$$ExternalSyntheticLambda45(this));
        this.mHeadsUpManager.releaseAllImmediately();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$maybeEscalateHeadsUp$22$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43888x3e6d1861(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        Notification notification = sbn.getNotification();
        if (notification.fullScreenIntent != null) {
            try {
                EventLog.writeEvent(EventLogTags.SYSUI_HEADS_UP_ESCALATION, sbn.getKey());
                wakeUpForFullScreenIntent();
                notification.fullScreenIntent.send();
                notificationEntry.notifyFullScreenIntentLaunched();
            } catch (PendingIntent.CanceledException unused) {
            }
        }
    }

    public void wakeUpForFullScreenIntent() {
        if (isGoingToSleep() || this.mDozing) {
            this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 2, "com.android.systemui:full_screen_intent");
            this.mWakeUpComingFromTouch = false;
            this.mWakeUpTouchLocation = null;
        }
    }

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
        DelayableExecutor delayableExecutor = this.mMainExecutor;
        ShadeController shadeController = this.mShadeController;
        Objects.requireNonNull(shadeController);
        delayableExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda17(shadeController));
    }

    public void postAnimateForceCollapsePanels() {
        this.mMainExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda39(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$postAnimateForceCollapsePanels$23$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43897x7c89c4b2() {
        this.mShadeController.animateCollapsePanels(0, true);
    }

    public void postAnimateOpenPanels() {
        this.mMessageRouter.sendMessage(1002);
    }

    public boolean isExpandedVisible() {
        return this.mExpandedVisible;
    }

    public boolean isPanelExpanded() {
        return this.mPanelExpanded;
    }

    public void onInputFocusTransfer(boolean z, boolean z2, float f) {
        if (this.mCommandQueue.panelsEnabled()) {
            if (z) {
                this.mNotificationPanelViewController.startWaitingForOpenPanelGesture();
            } else {
                this.mNotificationPanelViewController.stopWaitingForOpenPanelGesture(z2, f);
            }
        }
    }

    public void animateCollapseQuickSettings() {
        if (this.mState == 0) {
            this.mNotificationPanelViewController.collapsePanel(true, false, 1.0f);
        }
    }

    /* access modifiers changed from: package-private */
    public void makeExpandedInvisible() {
        if (this.mExpandedVisible && this.mNotificationShadeWindowView != null) {
            this.mNotificationPanelViewController.collapsePanel(false, false, 1.0f);
            this.mNotificationPanelViewController.closeQs();
            this.mExpandedVisible = false;
            visibilityChanged(false);
            this.mNotificationShadeWindowController.setPanelVisible(false);
            this.mStatusBarWindowController.setForceStatusBarVisible(false);
            this.mGutsManager.closeAndSaveGuts(true, true, true, -1, -1, true);
            this.mShadeController.runPostCollapseRunnables();
            setInteracting(1, false);
            if (!this.mNotificationActivityStarter.isCollapsingToShowActivityOverLockscreen()) {
                showBouncerOrLockScreenIfKeyguard();
            }
            this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, this.mNotificationPanelViewController.hideStatusBarIconsWhenExpanded());
            if (!this.mStatusBarKeyguardViewManager.isShowing()) {
                WindowManagerGlobal.getInstance().trimMemory(20);
            }
        }
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getId() == this.mLastMotionEventId) {
            NTLogUtil.m1680d(CentralSurfaces.TAG, "ignore same MotionEvent = " + motionEvent);
            return;
        }
        this.mLastMotionEventId = motionEvent.getId();
        if (this.mStatusBarWindowState == 0) {
            boolean z = false;
            if (!(motionEvent.getAction() == 1 || motionEvent.getAction() == 3) || this.mExpandedVisible) {
                z = true;
            }
            setInteracting(1, z);
        }
    }

    public GestureRecorder getGestureRecorder() {
        return this.mGestureRec;
    }

    public BiometricUnlockController getBiometricUnlockController() {
        return this.mBiometricUnlockController;
    }

    public void showTransientUnchecked() {
        if (!this.mTransientShown) {
            this.mTransientShown = true;
            this.mNoAnimationOnNextBarModeChange = true;
            maybeUpdateBarMode();
        }
    }

    public void clearTransient() {
        if (this.mTransientShown) {
            this.mTransientShown = false;
            maybeUpdateBarMode();
        }
    }

    /* access modifiers changed from: private */
    public void maybeUpdateBarMode() {
        int barMode = barMode(this.mTransientShown, this.mAppearance);
        if (updateBarMode(barMode)) {
            this.mLightBarController.onStatusBarModeChanged(barMode);
            updateBubblesVisibility();
        }
    }

    private boolean updateBarMode(int i) {
        if (this.mStatusBarMode == i) {
            return false;
        }
        this.mStatusBarMode = i;
        checkBarModes();
        this.mAutoHideController.touchAutoHide();
        return true;
    }

    private int barMode(boolean z, int i) {
        if ((this.mOngoingCallController.hasOngoingCall() && this.mIsFullscreen) || z) {
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

    public void showWirelessChargingAnimation(int i) {
        showChargingAnimation(i, -1, 0);
    }

    /* access modifiers changed from: protected */
    public void showChargingAnimation(int i, int i2, long j) {
        WirelessChargingAnimation.makeWirelessChargingAnimation(this.mContext, (Looper) null, i2, i, new WirelessChargingAnimation.Callback() {
            public void onAnimationStarting() {
                CentralSurfacesImpl.this.mNotificationShadeWindowController.setRequestTopUi(true, CentralSurfaces.TAG);
            }

            public void onAnimationEnded() {
                CentralSurfacesImpl.this.mNotificationShadeWindowController.setRequestTopUi(false, CentralSurfaces.TAG);
            }
        }, false, sUiEventLogger).show(j);
    }

    public void checkBarModes() {
        if (!this.mDemoModeController.isInDemoMode()) {
            PhoneStatusBarTransitions phoneStatusBarTransitions = this.mStatusBarTransitions;
            if (phoneStatusBarTransitions != null) {
                checkBarMode(this.mStatusBarMode, this.mStatusBarWindowState, phoneStatusBarTransitions);
            }
            this.mNavigationBarController.checkNavBarModes(this.mDisplayId);
            this.mNoAnimationOnNextBarModeChange = false;
        }
    }

    public void setQsScrimEnabled(boolean z) {
        this.mNotificationPanelViewController.setQsScrimEnabled(z);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateBubblesVisibility$24$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43911x47c7eaa1(Bubbles bubbles) {
        int i = this.mStatusBarMode;
        bubbles.onStatusBarVisibilityChanged((i == 3 || i == 6 || this.mStatusBarWindowHidden) ? false : true);
    }

    public void updateBubblesVisibility() {
        this.mBubblesOptional.ifPresent(new CentralSurfacesImpl$$ExternalSyntheticLambda36(this));
    }

    /* access modifiers changed from: package-private */
    public void checkBarMode(int i, int i2, BarTransitions barTransitions) {
        barTransitions.transitionTo(i, !this.mNoAnimationOnNextBarModeChange && this.mDeviceInteractive && i2 != 2);
    }

    /* access modifiers changed from: private */
    public void finishBarAnimations() {
        PhoneStatusBarTransitions phoneStatusBarTransitions = this.mStatusBarTransitions;
        if (phoneStatusBarTransitions != null) {
            phoneStatusBarTransitions.finishAnimations();
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

    /* access modifiers changed from: private */
    public void dismissVolumeDialog() {
        VolumeComponent volumeComponent = this.mVolumeComponent;
        if (volumeComponent != null) {
            volumeComponent.dismissNow();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        synchronized (this.mQueueLock) {
            asIndenting.println("Current Status Bar state:");
            asIndenting.println("  mExpandedVisible=" + this.mExpandedVisible);
            asIndenting.println("  mDisplayMetrics=" + this.mDisplayMetrics);
            asIndenting.println("  mStackScroller: " + CentralSurfaces.viewInfo(this.mStackScroller));
            asIndenting.println("  mStackScroller: " + CentralSurfaces.viewInfo(this.mStackScroller) + " scroll " + this.mStackScroller.getScrollX() + NavigationBarInflaterView.BUTTON_SEPARATOR + this.mStackScroller.getScrollY());
        }
        asIndenting.print("  mInteractingWindows=");
        asIndenting.println(this.mInteractingWindows);
        asIndenting.print("  mStatusBarWindowState=");
        asIndenting.println(StatusBarManager.windowStateToString(this.mStatusBarWindowState));
        asIndenting.print("  mStatusBarMode=");
        asIndenting.println(BarTransitions.modeToString(this.mStatusBarMode));
        asIndenting.print("  mDozing=");
        asIndenting.println(this.mDozing);
        asIndenting.print("  mWallpaperSupported= ");
        asIndenting.println(this.mWallpaperSupported);
        asIndenting.println("  ShadeWindowView: ");
        NotificationShadeWindowViewController notificationShadeWindowViewController = this.mNotificationShadeWindowViewController;
        if (notificationShadeWindowViewController != null) {
            notificationShadeWindowViewController.dump(asIndenting, strArr);
            CentralSurfaces.dumpBarTransitions(asIndenting, "PhoneStatusBarTransitions", this.mStatusBarTransitions);
        }
        asIndenting.println("  mMediaManager: ");
        NotificationMediaManager notificationMediaManager = this.mMediaManager;
        if (notificationMediaManager != null) {
            notificationMediaManager.dump(asIndenting, strArr);
        }
        asIndenting.println("  Panels: ");
        if (this.mNotificationPanelViewController != null) {
            asIndenting.println("    mNotificationPanel=" + this.mNotificationPanelViewController.getView() + " params=" + this.mNotificationPanelViewController.getView().getLayoutParams().debug(""));
            asIndenting.print("      ");
            this.mNotificationPanelViewController.dump(asIndenting, strArr);
        }
        asIndenting.println("  mStackScroller: ");
        if (this.mStackScroller != null) {
            asIndenting.increaseIndent();
            asIndenting.increaseIndent();
            this.mStackScroller.dump(asIndenting, strArr);
            asIndenting.decreaseIndent();
            asIndenting.decreaseIndent();
        }
        asIndenting.println("  Theme:");
        asIndenting.println("    dark theme: " + (this.mUiModeManager == null ? "null" : this.mUiModeManager.getNightMode() + "") + " (auto: 0, yes: 2, no: 1)");
        asIndenting.println("    light wallpaper theme: " + (this.mContext.getThemeResId() == 2132018221));
        KeyguardIndicationController keyguardIndicationController = this.mKeyguardIndicationController;
        if (keyguardIndicationController != null) {
            keyguardIndicationController.dump(asIndenting, strArr);
        }
        ScrimController scrimController = this.mScrimController;
        if (scrimController != null) {
            scrimController.dump(asIndenting, strArr);
        }
        if (this.mLightRevealScrim != null) {
            asIndenting.println("mLightRevealScrim.getRevealEffect(): " + this.mLightRevealScrim.getRevealEffect());
            asIndenting.println("mLightRevealScrim.getRevealAmount(): " + this.mLightRevealScrim.getRevealAmount());
        }
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager != null) {
            statusBarKeyguardViewManager.dump(asIndenting);
        }
        this.mNotificationsController.dump(asIndenting, strArr, true);
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        if (headsUpManagerPhone != null) {
            headsUpManagerPhone.dump(asIndenting, strArr);
        } else {
            asIndenting.println("  mHeadsUpManager: null");
        }
        StatusBarTouchableRegionManager statusBarTouchableRegionManager = this.mStatusBarTouchableRegionManager;
        if (statusBarTouchableRegionManager != null) {
            statusBarTouchableRegionManager.dump(asIndenting, strArr);
        } else {
            asIndenting.println("  mStatusBarTouchableRegionManager: null");
        }
        LightBarController lightBarController = this.mLightBarController;
        if (lightBarController != null) {
            lightBarController.dump(asIndenting, strArr);
        }
        asIndenting.println("SharedPreferences:");
        for (Map.Entry next : Prefs.getAll(this.mContext).entrySet()) {
            asIndenting.print("  ");
            asIndenting.print((String) next.getKey());
            asIndenting.print("=");
            asIndenting.println(next.getValue());
        }
        asIndenting.println("Camera gesture intents:");
        asIndenting.println("   Insecure camera: " + CameraIntents.getInsecureCameraIntent(this.mContext));
        asIndenting.println("   Secure camera: " + CameraIntents.getSecureCameraIntent(this.mContext));
        asIndenting.println("   Override package: " + CameraIntents.getOverrideCameraPackage(this.mContext));
    }

    public void createAndAddWindows(RegisterStatusBarResult registerStatusBarResult) {
        makeStatusBarView(registerStatusBarResult);
        this.mNotificationShadeWindowController.attach();
        this.mStatusBarWindowController.attach();
    }

    /* access modifiers changed from: package-private */
    public void updateDisplaySize() {
        this.mDisplay.getMetrics(this.mDisplayMetrics);
        this.mDisplay.getSize(this.mCurrentDisplaySize);
    }

    public float getDisplayDensity() {
        return this.mDisplayMetrics.density;
    }

    public float getDisplayWidth() {
        return (float) this.mDisplayMetrics.widthPixels;
    }

    public float getDisplayHeight() {
        return (float) this.mDisplayMetrics.heightPixels;
    }

    public int getRotation() {
        return this.mDisplay.getRotation();
    }

    public int getDisplayId() {
        return this.mDisplayId;
    }

    public void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2, int i) {
        startActivityDismissingKeyguard(intent, z, z2, false, (ActivityStarter.Callback) null, i, (ActivityLaunchAnimator.Controller) null, getActivityUserHandle(intent));
    }

    public void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2) {
        startActivityDismissingKeyguard(intent, z, z2, 0);
    }

    public void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2, boolean z3, ActivityStarter.Callback callback, int i, ActivityLaunchAnimator.Controller controller, UserHandle userHandle) {
        boolean z4 = z2;
        ActivityLaunchAnimator.Controller controller2 = controller;
        if (!z || this.mDeviceProvisionedController.isDeviceProvisioned()) {
            Intent intent2 = intent;
            boolean wouldLaunchResolverActivity = this.mActivityIntentHelper.wouldLaunchResolverActivity(intent, this.mLockscreenUserManager.getCurrentUserId());
            boolean z5 = controller2 != null && !wouldLaunchResolverActivity && shouldAnimateLaunch(true);
            ActivityLaunchAnimator.Controller wrapAnimationController = controller2 != null ? wrapAnimationController(controller2, z4) : null;
            executeRunnableDismissingKeyguard(new CentralSurfacesImpl$$ExternalSyntheticLambda37(this, intent, i, wrapAnimationController, z5, z3, userHandle, callback), new CentralSurfacesImpl$$ExternalSyntheticLambda38(callback), z4 && wrapAnimationController == null, wouldLaunchResolverActivity, true, z5);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startActivityDismissingKeyguard$26$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43908x93d22f9e(Intent intent, int i, ActivityLaunchAnimator.Controller controller, boolean z, boolean z2, UserHandle userHandle, ActivityStarter.Callback callback) {
        ActivityStarter.Callback callback2 = callback;
        this.mAssistManagerLazy.get().hideAssist();
        intent.setFlags(335544320);
        intent.addFlags(i);
        int[] iArr = {-96};
        ActivityLaunchAnimator.Controller controller2 = controller;
        this.mActivityLaunchAnimator.startIntentWithAnimation(controller, z, intent.getPackage(), new CentralSurfacesImpl$$ExternalSyntheticLambda13(this, z2, intent, iArr, userHandle));
        if (callback2 != null) {
            callback2.onActivityStarted(iArr[0]);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startActivityDismissingKeyguard$25$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ Integer mo43907x6e5187f(boolean z, Intent intent, int[] iArr, UserHandle userHandle, RemoteAnimationAdapter remoteAnimationAdapter) {
        ActivityOptions activityOptions = new ActivityOptions(CentralSurfaces.getActivityOptions(this.mDisplayId, remoteAnimationAdapter));
        activityOptions.setDisallowEnterPictureInPictureWhileLaunching(z);
        if (CameraIntents.isInsecureCameraIntent(intent)) {
            activityOptions.setRotationAnimationHint(3);
        }
        if ("android.settings.panel.action.VOLUME".equals(intent.getAction())) {
            activityOptions.setDisallowEnterPictureInPictureWhileLaunching(true);
        }
        try {
            Intent intent2 = intent;
            iArr[0] = ActivityTaskManager.getService().startActivityAsUser((IApplicationThread) null, this.mContext.getBasePackageName(), this.mContext.getAttributionTag(), intent2, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), (IBinder) null, (String) null, 0, 268435456, (ProfilerInfo) null, activityOptions.toBundle(), userHandle.getIdentifier());
        } catch (RemoteException e) {
            Log.w(CentralSurfaces.TAG, "Unable to start activity", e);
        }
        return Integer.valueOf(iArr[0]);
    }

    static /* synthetic */ void lambda$startActivityDismissingKeyguard$27(ActivityStarter.Callback callback) {
        if (callback != null) {
            callback.onActivityStarted(-96);
        }
    }

    private ActivityLaunchAnimator.Controller wrapAnimationController(ActivityLaunchAnimator.Controller controller, boolean z) {
        Optional<ActivityLaunchAnimator.Controller> wrapAnimationControllerIfInStatusBar = this.mStatusBarWindowController.wrapAnimationControllerIfInStatusBar(controller.getLaunchContainer().getRootView(), controller);
        if (wrapAnimationControllerIfInStatusBar.isPresent()) {
            return wrapAnimationControllerIfInStatusBar.get();
        }
        return z ? new StatusBarLaunchAnimatorController(controller, this, true) : controller;
    }

    public void readyForKeyguardDone() {
        this.mStatusBarKeyguardViewManager.readyForKeyguardDone();
    }

    public void executeRunnableDismissingKeyguard(Runnable runnable, Runnable runnable2, boolean z, boolean z2, boolean z3) {
        executeRunnableDismissingKeyguard(runnable, runnable2, z, z2, z3, false);
    }

    public void executeRunnableDismissingKeyguard(Runnable runnable, Runnable runnable2, boolean z, boolean z2, boolean z3, boolean z4) {
        final Runnable runnable3 = runnable;
        final boolean z5 = z;
        final boolean z6 = z3;
        final boolean z7 = z4;
        dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() {
            public boolean onDismiss() {
                if (runnable3 != null) {
                    if (!CentralSurfacesImpl.this.mStatusBarKeyguardViewManager.isShowing() || !CentralSurfacesImpl.this.mStatusBarKeyguardViewManager.isOccluded()) {
                        CentralSurfacesImpl.this.mMainExecutor.execute(runnable3);
                    } else {
                        CentralSurfacesImpl.this.mStatusBarKeyguardViewManager.addAfterKeyguardGoneRunnable(runnable3);
                    }
                }
                if (z5) {
                    if (!CentralSurfacesImpl.this.mExpandedVisible || CentralSurfacesImpl.this.mBouncerShowing) {
                        DelayableExecutor access$600 = CentralSurfacesImpl.this.mMainExecutor;
                        ShadeController access$1300 = CentralSurfacesImpl.this.mShadeController;
                        Objects.requireNonNull(access$1300);
                        access$600.execute(new CentralSurfacesImpl$8$$ExternalSyntheticLambda0(access$1300));
                    } else {
                        CentralSurfacesImpl.this.mShadeController.animateCollapsePanels(2, true, true);
                    }
                } else if (CentralSurfacesImpl.this.isInLaunchTransition() && CentralSurfacesImpl.this.mNotificationPanelViewController.isLaunchTransitionFinished()) {
                    DelayableExecutor access$6002 = CentralSurfacesImpl.this.mMainExecutor;
                    StatusBarKeyguardViewManager statusBarKeyguardViewManager = CentralSurfacesImpl.this.mStatusBarKeyguardViewManager;
                    Objects.requireNonNull(statusBarKeyguardViewManager);
                    access$6002.execute(new CentralSurfacesImpl$8$$ExternalSyntheticLambda1(statusBarKeyguardViewManager));
                }
                return z6;
            }

            public boolean willRunAnimationOnKeyguard() {
                return z7;
            }
        }, runnable2, z2);
    }

    public void resetUserExpandedStates() {
        this.mNotificationsController.resetUserExpandedStates();
    }

    /* access modifiers changed from: private */
    public void executeWhenUnlocked(ActivityStarter.OnDismissAction onDismissAction, boolean z, boolean z2) {
        if (this.mStatusBarKeyguardViewManager.isShowing() && z) {
            this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
        }
        dismissKeyguardThenExecute(onDismissAction, (Runnable) null, z2);
    }

    /* access modifiers changed from: protected */
    public void dismissKeyguardThenExecute(ActivityStarter.OnDismissAction onDismissAction, boolean z) {
        dismissKeyguardThenExecute(onDismissAction, (Runnable) null, z);
    }

    public void dismissKeyguardThenExecute(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z) {
        if (this.mWakefulnessLifecycle.getWakefulness() == 0 && this.mKeyguardStateController.canDismissLockScreen() && !this.mStatusBarStateController.leaveOpenOnKeyguardHide() && this.mDozeServiceHost.isPulsing()) {
            this.mBiometricUnlockController.startWakeAndUnlock(2);
        }
        if (this.mStatusBarKeyguardViewManager.isShowing()) {
            this.mStatusBarKeyguardViewManager.dismissWithAction(onDismissAction, runnable, z);
            return;
        }
        if (this.mKeyguardUpdateMonitor.isDreaming()) {
            awakenDreams();
        }
        onDismissAction.onDismiss();
    }

    public void setLockscreenUser(int i) {
        LockscreenWallpaper lockscreenWallpaper = this.mLockscreenWallpaper;
        if (lockscreenWallpaper != null) {
            lockscreenWallpaper.setCurrentUser(i);
        }
        this.mScrimController.setCurrentUser(i);
        if (this.mWallpaperSupported) {
            this.mWallpaperChangedReceiver.onReceive(this.mContext, (Intent) null);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateResources() {
        QSPanelController qSPanelController = this.mQSPanelController;
        if (qSPanelController != null) {
            qSPanelController.updateResources();
        }
        StatusBarWindowController statusBarWindowController = this.mStatusBarWindowController;
        if (statusBarWindowController != null) {
            statusBarWindowController.refreshStatusBarHeight();
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
        this.mPowerButtonReveal = new PowerButtonReveal((float) this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.physical_power_button_center_screen_location_y));
    }

    /* access modifiers changed from: protected */
    public void handleVisibleToUserChanged(boolean z) {
        if (z) {
            handleVisibleToUserChangedImpl(z);
            this.mNotificationLogger.startNotificationLogging();
            return;
        }
        this.mNotificationLogger.stopNotificationLogging();
        handleVisibleToUserChangedImpl(z);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0011, code lost:
        r0 = r3.mState;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleVisibleToUserChangedImpl(boolean r4) {
        /*
            r3 = this;
            if (r4 == 0) goto L_0x0038
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone r4 = r3.mHeadsUpManager
            boolean r4 = r4.hasPinnedHeadsUp()
            com.android.systemui.statusbar.NotificationPresenter r0 = r3.mPresenter
            boolean r0 = r0.isPresenterFullyCollapsed()
            r1 = 1
            if (r0 != 0) goto L_0x001a
            int r0 = r3.mState
            if (r0 == 0) goto L_0x0018
            r2 = 2
            if (r0 != r2) goto L_0x001a
        L_0x0018:
            r0 = r1
            goto L_0x001b
        L_0x001a:
            r0 = 0
        L_0x001b:
            com.android.systemui.statusbar.notification.init.NotificationsController r2 = r3.mNotificationsController
            int r2 = r2.getActiveNotificationsCount()
            if (r4 == 0) goto L_0x002c
            com.android.systemui.statusbar.NotificationPresenter r4 = r3.mPresenter
            boolean r4 = r4.isPresenterFullyCollapsed()
            if (r4 == 0) goto L_0x002c
            goto L_0x002d
        L_0x002c:
            r1 = r2
        L_0x002d:
            java.util.concurrent.Executor r4 = r3.mUiBgExecutor
            com.android.systemui.statusbar.phone.CentralSurfacesImpl$$ExternalSyntheticLambda21 r2 = new com.android.systemui.statusbar.phone.CentralSurfacesImpl$$ExternalSyntheticLambda21
            r2.<init>(r3, r0, r1)
            r4.execute(r2)
            goto L_0x0042
        L_0x0038:
            java.util.concurrent.Executor r4 = r3.mUiBgExecutor
            com.android.systemui.statusbar.phone.CentralSurfacesImpl$$ExternalSyntheticLambda23 r0 = new com.android.systemui.statusbar.phone.CentralSurfacesImpl$$ExternalSyntheticLambda23
            r0.<init>(r3)
            r4.execute(r0)
        L_0x0042:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.CentralSurfacesImpl.handleVisibleToUserChangedImpl(boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleVisibleToUserChangedImpl$28$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43879x32dd988b(boolean z, int i) {
        try {
            this.mBarService.onPanelRevealed(z, i);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleVisibleToUserChangedImpl$29$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43880xbfcaafaa() {
        try {
            this.mBarService.onPanelHidden();
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void logStateToEventlog() {
        boolean isShowing = this.mStatusBarKeyguardViewManager.isShowing();
        boolean isOccluded = this.mStatusBarKeyguardViewManager.isOccluded();
        boolean isBouncerShowing = this.mStatusBarKeyguardViewManager.isBouncerShowing();
        boolean isMethodSecure = this.mKeyguardStateController.isMethodSecure();
        boolean canDismissLockScreen = this.mKeyguardStateController.canDismissLockScreen();
        int loggingFingerprint = getLoggingFingerprint(this.mState, isShowing, isOccluded, isBouncerShowing, isMethodSecure, canDismissLockScreen);
        if (loggingFingerprint != this.mLastLoggedStateFingerprint) {
            if (this.mStatusBarStateLog == null) {
                this.mStatusBarStateLog = new LogMaker(0);
            }
            this.mMetricsLogger.write(this.mStatusBarStateLog.setCategory(isBouncerShowing ? 197 : 196).setType(isShowing ? 1 : 2).setSubtype(isMethodSecure ? 1 : 0));
            EventLogTags.writeSysuiStatusBarState(this.mState, isShowing ? 1 : 0, isOccluded ? 1 : 0, isBouncerShowing ? 1 : 0, isMethodSecure ? 1 : 0, canDismissLockScreen ? 1 : 0);
            this.mLastLoggedStateFingerprint = loggingFingerprint;
            StringBuilder sb = new StringBuilder();
            sb.append(isBouncerShowing ? "BOUNCER" : "LOCKSCREEN");
            sb.append(isShowing ? "_OPEN" : "_CLOSE");
            sb.append(isMethodSecure ? "_SECURE" : "_INSECURE");
            sUiEventLogger.log(StatusBarUiEvent.valueOf(sb.toString()));
        }
    }

    public void postQSRunnableDismissingKeyguard(Runnable runnable) {
        this.mMainExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda12(this, runnable));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$postQSRunnableDismissingKeyguard$31$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43899x8443c1cc(Runnable runnable) {
        this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
        executeRunnableDismissingKeyguard(new CentralSurfacesImpl$$ExternalSyntheticLambda5(this, runnable), (Runnable) null, false, false, false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$postQSRunnableDismissingKeyguard$30$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43898xf756aaad(Runnable runnable) {
        this.mMainExecutor.execute(runnable);
    }

    public void postStartActivityDismissingKeyguard(PendingIntent pendingIntent) {
        postStartActivityDismissingKeyguard(pendingIntent, (ActivityLaunchAnimator.Controller) null);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$postStartActivityDismissingKeyguard$32$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43900xc6f81a3(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller) {
        startPendingIntentDismissingKeyguard(pendingIntent, (Runnable) null, controller);
    }

    public void postStartActivityDismissingKeyguard(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller) {
        this.mMainExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda4(this, pendingIntent, controller));
    }

    public void postStartActivityDismissingKeyguard(Intent intent, int i) {
        postStartActivityDismissingKeyguard(intent, i, (ActivityLaunchAnimator.Controller) null);
    }

    public void postStartActivityDismissingKeyguard(Intent intent, int i, ActivityLaunchAnimator.Controller controller) {
        this.mMainExecutor.executeDelayed(new CentralSurfacesImpl$$ExternalSyntheticLambda15(this, intent, controller), (long) i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$postStartActivityDismissingKeyguard$33$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43901x995c98c2(Intent intent, ActivityLaunchAnimator.Controller controller) {
        startActivityDismissingKeyguard(intent, true, true, false, (ActivityStarter.Callback) null, 0, controller, getActivityUserHandle(intent));
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

    public boolean updateIsKeyguard() {
        return updateIsKeyguard(false);
    }

    public boolean updateIsKeyguard(boolean z) {
        boolean isWakeAndUnlock = this.mBiometricUnlockController.isWakeAndUnlock();
        boolean z2 = true;
        boolean z3 = this.mDozeServiceHost.getDozingRequested() && (!this.mDeviceInteractive || (isGoingToSleep() && (isScreenFullyOff() || (this.mKeyguardStateController.isShowing() && !isOccluded()))));
        boolean z4 = isOccluded() && isWakingOrAwake();
        if ((!this.mStatusBarStateController.isKeyguardRequested() && !z3) || isWakeAndUnlock || z4) {
            z2 = false;
        }
        if (z3) {
            updatePanelExpansionForKeyguard();
        }
        if (z2) {
            if (!this.mScreenOffAnimationController.isKeyguardShowDelayed() && (!isGoingToSleep() || this.mScreenLifecycle.getScreenState() != 3)) {
                showKeyguardImpl();
            }
        } else if (!this.mScreenOffAnimationController.isKeyguardHideDelayed()) {
            return hideKeyguardImpl(z);
        }
        return false;
    }

    public void showKeyguardImpl() {
        Trace.beginSection("CentralSurfaces#showKeyguard");
        if (this.mKeyguardStateController.isLaunchTransitionFadingAway()) {
            this.mNotificationPanelViewController.cancelAnimation();
            onLaunchTransitionFadingEnded();
        }
        this.mMessageRouter.cancelMessages(1003);
        if (!this.mLockscreenShadeTransitionController.isWakingToShadeLocked()) {
            this.mStatusBarStateController.setState(1);
        }
        updatePanelExpansionForKeyguard();
        Trace.endSection();
    }

    private void updatePanelExpansionForKeyguard() {
        int i = this.mState;
        if ((i == 1 || (this.mBouncerShowingChanged && i == 2)) && this.mBiometricUnlockController.getMode() != 1 && !this.mBouncerShowing) {
            this.mShadeController.instantExpandNotificationsPanel();
        }
    }

    /* access modifiers changed from: private */
    public void onLaunchTransitionFadingEnded() {
        this.mNotificationPanelViewController.resetAlpha();
        this.mNotificationPanelViewController.onAffordanceLaunchEnded();
        releaseGestureWakeLock();
        runLaunchTransitionEndRunnable();
        this.mKeyguardStateController.setLaunchTransitionFadingAway(false);
        this.mPresenter.updateMediaMetaData(true, true);
    }

    public boolean isInLaunchTransition() {
        return this.mNotificationPanelViewController.isLaunchTransitionRunning() || this.mNotificationPanelViewController.isLaunchTransitionFinished();
    }

    public void fadeKeyguardAfterLaunchTransition(Runnable runnable, Runnable runnable2, Runnable runnable3) {
        this.mMessageRouter.cancelMessages(1003);
        this.mLaunchTransitionEndRunnable = runnable2;
        this.mLaunchTransitionCancelRunnable = runnable3;
        CentralSurfacesImpl$$ExternalSyntheticLambda35 centralSurfacesImpl$$ExternalSyntheticLambda35 = new CentralSurfacesImpl$$ExternalSyntheticLambda35(this, runnable);
        if (this.mNotificationPanelViewController.isLaunchTransitionRunning()) {
            this.mNotificationPanelViewController.setLaunchTransitionEndRunnable(centralSurfacesImpl$$ExternalSyntheticLambda35);
        } else {
            centralSurfacesImpl$$ExternalSyntheticLambda35.run();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fadeKeyguardAfterLaunchTransition$34$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43876x5a9a8cd4(Runnable runnable) {
        this.mKeyguardStateController.setLaunchTransitionFadingAway(true);
        if (runnable != null) {
            runnable.run();
        }
        updateScrimController();
        this.mPresenter.updateMediaMetaData(false, true);
        this.mNotificationPanelViewController.resetAlpha();
        this.mNotificationPanelViewController.fadeOut(100, 300, new CentralSurfacesImpl$$ExternalSyntheticLambda18(this));
        this.mCommandQueue.appTransitionStarting(this.mDisplayId, SystemClock.uptimeMillis(), 120, true);
    }

    /* access modifiers changed from: private */
    public void cancelAfterLaunchTransitionRunnables() {
        Runnable runnable = this.mLaunchTransitionCancelRunnable;
        if (runnable != null) {
            runnable.run();
        }
        this.mLaunchTransitionEndRunnable = null;
        this.mLaunchTransitionCancelRunnable = null;
        this.mNotificationPanelViewController.setLaunchTransitionEndRunnable((Runnable) null);
    }

    public void fadeKeyguardWhilePulsing() {
        this.mNotificationPanelViewController.fadeOut(0, 96, new CentralSurfacesImpl$$ExternalSyntheticLambda9(this)).start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fadeKeyguardWhilePulsing$35$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43877xcbae3212() {
        hideKeyguard();
        this.mStatusBarKeyguardViewManager.onKeyguardFadedAway();
    }

    public void animateKeyguardUnoccluding() {
        this.mNotificationPanelViewController.setExpandedFraction(0.0f);
        this.mCommandQueueCallbacks.animateExpandNotificationsPanel();
        this.mScrimController.setUnocclusionAnimationRunning(true);
    }

    public void startLaunchTransitionTimeout() {
        this.mMessageRouter.sendMessageDelayed(1003, 5000);
    }

    private void onLaunchTransitionTimeout() {
        Log.w(CentralSurfaces.TAG, "Launch transition: Timeout!");
        this.mNotificationPanelViewController.onAffordanceLaunchEnded();
        releaseGestureWakeLock();
        this.mNotificationPanelViewController.resetViews(false);
    }

    private void runLaunchTransitionEndRunnable() {
        this.mLaunchTransitionCancelRunnable = null;
        Runnable runnable = this.mLaunchTransitionEndRunnable;
        if (runnable != null) {
            this.mLaunchTransitionEndRunnable = null;
            runnable.run();
        }
    }

    public boolean hideKeyguardImpl(boolean z) {
        Trace.beginSection("CentralSurfaces#hideKeyguard");
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
        this.mMessageRouter.cancelMessages(1003);
        releaseGestureWakeLock();
        this.mNotificationPanelViewController.onAffordanceLaunchEnded();
        this.mNotificationPanelViewController.resetAlpha();
        this.mNotificationPanelViewController.resetTranslation();
        this.mNotificationPanelViewController.resetViewGroupFade();
        updateDozingState();
        updateScrimController();
        Trace.endSection();
        return leaveOpenOnKeyguardHide;
    }

    /* access modifiers changed from: private */
    public void releaseGestureWakeLock() {
        if (this.mGestureWakeLock.isHeld()) {
            this.mGestureWakeLock.release();
        }
    }

    public void keyguardGoingAway() {
        this.mKeyguardStateController.notifyKeyguardGoingAway(true);
        this.mCommandQueue.appTransitionPending(this.mDisplayId, true);
        updateScrimController();
    }

    public void setKeyguardFadingAway(long j, long j2, long j3, boolean z) {
        this.mCommandQueue.appTransitionStarting(this.mDisplayId, (j + j3) - 120, 120, true);
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, j3 > 0);
        this.mCommandQueue.appTransitionStarting(this.mDisplayId, j - 120, 120, true);
        this.mKeyguardStateController.notifyKeyguardFadingAway(j2, j3, z);
    }

    public void finishKeyguardFadingAway() {
        this.mKeyguardStateController.notifyKeyguardDoneFading();
        this.mScrimController.setExpansionAffectsAlpha(true);
        this.mKeyguardViewMediator.maybeHandlePendingLock();
    }

    /* access modifiers changed from: protected */
    public void updateTheme() {
        this.mUiBgExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda40(this));
        int i = this.mColorExtractor.getNeutralColors().supportsDarkText() ? C1893R.style.Theme_SystemUI_LightWallpaper : C1893R.style.Theme_SystemUI;
        if (this.mContext.getThemeResId() != i) {
            this.mContext.setTheme(i);
            this.mConfigurationController.notifyThemeChanged();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateTheme$37$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43913xee6659af() {
        this.mMainExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda19(this, this.mWallpaperManager.lockScreenWallpaperExists() ? this.mWallpaperManager.getWallpaperDimAmount() : 0.0f));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateTheme$36$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43912x61794290(float f) {
        this.mScrimController.setAdditionalScrimBehindAlphaKeyguard(f);
        this.mScrimController.applyCompositeAlphaOnScrimBehindKeyguard();
    }

    /* access modifiers changed from: private */
    public void updateDozingState() {
        Trace.traceCounter(4096, "dozing", this.mDozing ? 1 : 0);
        Trace.beginSection("CentralSurfaces#updateDozingState");
        boolean z = false;
        boolean z2 = (this.mStatusBarKeyguardViewManager.isShowing() && !this.mStatusBarKeyguardViewManager.isOccluded()) || (this.mDozing && this.mDozeParameters.shouldDelayKeyguardShow());
        boolean z3 = this.mBiometricUnlockController.getMode() == 1;
        if ((!this.mDozing && ((this.mDozeServiceHost.shouldAnimateWakeup() || ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).shouldPlayOnOffAnimation()) && !z3)) || (this.mDozing && this.mDozeParameters.shouldControlScreenOff() && z2)) {
            z = true;
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
        if (this.mState != 1 || !this.mStatusBarKeyguardViewManager.interceptMediaKey(keyEvent)) {
            return false;
        }
        return true;
    }

    public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 4 && this.mState == 1 && this.mStatusBarKeyguardViewManager.dispatchBackKeyEventPreIme()) {
            return onBackPressed();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean shouldUnlockOnMenuPressed() {
        return this.mDeviceInteractive && this.mState != 0 && this.mStatusBarKeyguardViewManager.shouldDismissOnMenuPressed();
    }

    public boolean onMenuPressed() {
        if (!shouldUnlockOnMenuPressed()) {
            return false;
        }
        this.mShadeController.animateCollapsePanels(2, true);
        return true;
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
        } else if (this.mNotificationPanelViewController.isQsCustomizing()) {
            this.mNotificationPanelViewController.closeQsCustomizer();
            return true;
        } else if (this.mNotificationPanelViewController.isQsExpanded()) {
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

    private void showBouncerOrLockScreenIfKeyguard() {
        if (!this.mKeyguardViewMediator.isHiding() && !this.mKeyguardUnlockAnimationController.isPlayingCannedUnlockAnimation()) {
            if (this.mState == 2 && this.mKeyguardUpdateMonitor.isUdfpsEnrolled()) {
                this.mStatusBarKeyguardViewManager.reset(true);
            } else if ((this.mState == 1 && !this.mStatusBarKeyguardViewManager.bouncerIsOrWillBeShowing()) || this.mState == 2) {
                this.mStatusBarKeyguardViewManager.showGenericBouncer(true);
            }
        }
    }

    public void showBouncerWithDimissAndCancelIfKeyguard(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable) {
        int i = this.mState;
        if ((i == 1 || i == 2) && !this.mKeyguardViewMediator.isHiding()) {
            this.mStatusBarKeyguardViewManager.dismissWithAction(onDismissAction, runnable, false);
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public void instantCollapseNotificationPanel() {
        this.mNotificationPanelViewController.instantCollapse();
        this.mShadeController.runPostCollapseRunnables();
    }

    public void collapsePanelOnMainThread() {
        if (Looper.getMainLooper().isCurrentThread()) {
            this.mShadeController.collapsePanel();
            return;
        }
        Executor mainExecutor = this.mContext.getMainExecutor();
        ShadeController shadeController = this.mShadeController;
        Objects.requireNonNull(shadeController);
        mainExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda14(shadeController));
    }

    public void collapsePanelWithDuration(int i) {
        this.mNotificationPanelViewController.collapseWithDuration(i);
    }

    /* access modifiers changed from: private */
    public void updateRevealEffect(boolean z) {
        if (this.mLightRevealScrim != null) {
            ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).updateRevealEffectEx(this.mStatusBarStateController, this.mDisplayMetrics, this.mWakefulnessLifecycle, this.mWakeUpComingFromTouch, this.mWakeUpTouchLocation, this.mLightRevealScrim, this.mPowerButtonReveal, z);
        }
    }

    public LightRevealScrim getLightRevealScrim() {
        return this.mLightRevealScrim;
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
        this.mKeyguardIndicationController.hideTransientIndicationDelayed(NotificationTapHelper.DOUBLE_TAP_TIMEOUT_MS);
    }

    public void onCameraHintStarted() {
        this.mFalsingCollector.onCameraHintStarted();
        this.mKeyguardIndicationController.showTransientIndication((int) C1893R.string.camera_hint);
    }

    public void onVoiceAssistHintStarted() {
        this.mFalsingCollector.onLeftAffordanceHintStarted();
        this.mKeyguardIndicationController.showTransientIndication((int) C1893R.string.voice_hint);
    }

    public void onPhoneHintStarted() {
        this.mFalsingCollector.onLeftAffordanceHintStarted();
        this.mKeyguardIndicationController.showTransientIndication((int) C1893R.string.phone_hint);
    }

    public NavigationBarView getNavigationBarView() {
        return this.mNavigationBarController.getNavigationBarView(this.mDisplayId);
    }

    public boolean isOverviewEnabled() {
        return this.mNavigationBarController.isOverviewEnabled(this.mDisplayId);
    }

    public void showPinningEnterExitToast(boolean z) {
        this.mNavigationBarController.showPinningEnterExitToast(this.mDisplayId, z);
    }

    public void showPinningEscapeToast() {
        this.mNavigationBarController.showPinningEscapeToast(this.mDisplayId);
    }

    public KeyguardBottomAreaView getKeyguardBottomAreaView() {
        return this.mNotificationPanelViewController.getKeyguardBottomAreaView();
    }

    public void setBouncerShowing(boolean z) {
        this.mBouncerShowingChanged = this.mBouncerShowing != z;
        this.mBouncerShowing = z;
        this.mKeyguardBypassController.setBouncerShowing(z);
        this.mPulseExpansionHandler.setBouncerShowing(z);
        setBouncerShowingForStatusBarComponents(z);
        this.mStatusBarHideIconsForBouncerManager.setBouncerShowingAndTriggerUpdate(z);
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, true);
        updateScrimController();
        if (!this.mBouncerShowing) {
            updatePanelExpansionForKeyguard();
        }
    }

    private void setBouncerShowingForStatusBarComponents(boolean z) {
        int i = z ? 4 : 0;
        PhoneStatusBarViewController phoneStatusBarViewController = this.mPhoneStatusBarViewController;
        if (phoneStatusBarViewController != null) {
            phoneStatusBarViewController.setImportantForAccessibility(i);
        }
        this.mNotificationPanelViewController.setImportantForAccessibility(i);
        this.mNotificationPanelViewController.setBouncerShowing(z);
    }

    public void collapseShade() {
        if (this.mNotificationPanelViewController.isTracking()) {
            this.mNotificationShadeWindowViewController.cancelCurrentTouch();
        }
        if (this.mPanelExpanded && this.mState == 0) {
            this.mShadeController.animateCollapsePanels();
        }
    }

    public void updateNotificationPanelTouchState() {
        boolean z = false;
        boolean z2 = isGoingToSleep() && !this.mDozeParameters.shouldControlScreenOff();
        if ((!this.mDeviceInteractive && !this.mDozeServiceHost.isPulsing()) || z2) {
            z = true;
        }
        this.mNotificationPanelViewController.setTouchAndAnimationDisabled(z);
        this.mNotificationIconAreaController.setAnimationsEnabled(!z);
    }

    public int getWakefulnessState() {
        return this.mWakefulnessLifecycle.getWakefulness();
    }

    public boolean isScreenFullyOff() {
        return this.mScreenLifecycle.getScreenState() == 0;
    }

    public void showScreenPinningRequest(int i, boolean z) {
        this.mScreenPinningRequest.showPrompt(i, z);
    }

    public Intent getEmergencyActionIntent() {
        Intent intent = new Intent(EmergencyGesture.ACTION_LAUNCH_EMERGENCY);
        ResolveInfo topEmergencySosInfo = getTopEmergencySosInfo(this.mContext.getPackageManager().queryIntentActivities(intent, 1048576));
        if (topEmergencySosInfo == null) {
            Log.wtf(CentralSurfaces.TAG, "Couldn't find an app to process the emergency intent.");
            return null;
        }
        intent.setComponent(new ComponentName(topEmergencySosInfo.activityInfo.packageName, topEmergencySosInfo.activityInfo.name));
        intent.setFlags(268435456);
        return intent;
    }

    private ResolveInfo getTopEmergencySosInfo(List<ResolveInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        String string = this.mContext.getString(C1893R.string.config_preferredEmergencySosPackage);
        if (TextUtils.isEmpty(string)) {
            return list.get(0);
        }
        for (ResolveInfo next : list) {
            if (TextUtils.equals(next.activityInfo.packageName, string)) {
                return next;
            }
        }
        return list.get(0);
    }

    public boolean isCameraAllowedByAdmin() {
        if (this.mDevicePolicyManager.getCameraDisabled((ComponentName) null, this.mLockscreenUserManager.getCurrentUserId())) {
            return false;
        }
        if (this.mStatusBarKeyguardViewManager != null && (!isKeyguardShowing() || !isKeyguardSecure())) {
            return true;
        }
        if ((this.mDevicePolicyManager.getKeyguardDisabledFeatures((ComponentName) null, this.mLockscreenUserManager.getCurrentUserId()) & 2) == 0) {
            return true;
        }
        return false;
    }

    public boolean isGoingToSleep() {
        return this.mWakefulnessLifecycle.getWakefulness() == 3;
    }

    /* access modifiers changed from: package-private */
    public boolean isWakingOrAwake() {
        if (this.mWakefulnessLifecycle.getWakefulness() == 1 || this.mWakefulnessLifecycle.getWakefulness() == 2) {
            return true;
        }
        return false;
    }

    public void notifyBiometricAuthModeChanged() {
        this.mDozeServiceHost.updateDozing();
        updateScrimController();
    }

    public void setTransitionToFullShadeProgress(float f) {
        this.mTransitionToFullShadeProgress = f;
    }

    public void setBouncerHiddenFraction(float f) {
        this.mScrimController.setBouncerHiddenFraction(f);
    }

    public void updateScrimController() {
        Trace.beginSection("CentralSurfaces#updateScrimController");
        boolean z = this.mKeyguardStateController.isShowing() && (this.mBiometricUnlockController.isWakeAndUnlock() || this.mKeyguardStateController.isKeyguardFadingAway() || this.mKeyguardStateController.isKeyguardGoingAway() || this.mKeyguardViewMediator.requestedShowSurfaceBehindKeyguard() || this.mKeyguardViewMediator.isAnimatingBetweenKeyguardAndSurfaceBehind());
        this.mScrimController.setExpansionAffectsAlpha(!z);
        boolean isLaunchingAffordanceWithPreview = this.mNotificationPanelViewController.isLaunchingAffordanceWithPreview();
        this.mScrimController.setLaunchingAffordanceWithPreview(isLaunchingAffordanceWithPreview);
        if (this.mStatusBarKeyguardViewManager.isShowingAlternateAuth()) {
            int i = this.mState;
            if (i == 0 || i == 2 || this.mTransitionToFullShadeProgress > 0.0f) {
                this.mScrimController.transitionTo(ScrimState.AUTH_SCRIMMED_SHADE);
            } else {
                this.mScrimController.transitionTo(ScrimState.AUTH_SCRIMMED);
            }
        } else if (this.mBouncerShowing && !z) {
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
            this.mScrimController.transitionTo(ScrimState.OFF, new ScrimController.Callback() {
                public void onFinished() {
                    CentralSurfacesImpl.this.mDozeServiceHost.executePendingScreenOffCallback();
                }
            });
        } else if (this.mDozing && !z) {
            this.mScrimController.transitionTo(ScrimState.AOD);
        } else if (this.mKeyguardStateController.isShowing() && !isOccluded() && !z) {
            this.mScrimController.transitionTo(ScrimState.KEYGUARD);
        } else if (!this.mKeyguardStateController.isShowing() || !this.mKeyguardUpdateMonitor.isDreaming()) {
            this.mScrimController.transitionTo(ScrimState.UNLOCKED, this.mUnlockScrimCallback);
        } else {
            this.mScrimController.transitionTo(ScrimState.DREAMING);
        }
        updateLightRevealScrimVisibility();
        Trace.endSection();
    }

    public boolean isKeyguardShowing() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager != null) {
            return statusBarKeyguardViewManager.isShowing();
        }
        Slog.i(CentralSurfaces.TAG, "isKeyguardShowing() called before startKeyguard(), returning true");
        return true;
    }

    public boolean shouldIgnoreTouch() {
        return (this.mStatusBarStateController.isDozing() && this.mDozeServiceHost.getIgnoreTouchWhilePulsing()) || this.mScreenOffAnimationController.shouldIgnoreKeyguardTouches();
    }

    public boolean isDeviceInteractive() {
        return this.mDeviceInteractive;
    }

    public void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        this.mNotificationsController.setNotificationSnoozed(statusBarNotification, snoozeOption);
    }

    public void awakenDreams() {
        this.mUiBgExecutor.execute(new CentralSurfacesImpl$$ExternalSyntheticLambda20(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$awakenDreams$38$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43875x12cb8a1() {
        try {
            this.mDreamManager.awaken();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void toggleKeyboardShortcuts(int i) {
        KeyboardShortcuts.toggle(this.mContext, i);
    }

    /* access modifiers changed from: protected */
    public void dismissKeyboardShortcuts() {
        KeyboardShortcuts.dismiss();
    }

    private void executeActionDismissingKeyguard(final Runnable runnable, boolean z, final boolean z2, final boolean z3) {
        if (this.mDeviceProvisionedController.isDeviceProvisioned()) {
            dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() {
                public boolean onDismiss() {
                    new Thread((Runnable) new CentralSurfacesImpl$15$$ExternalSyntheticLambda0(runnable)).start();
                    return z2 ? CentralSurfacesImpl.this.mShadeController.collapsePanel() : z3;
                }

                static /* synthetic */ void lambda$onDismiss$0(Runnable runnable) {
                    try {
                        ActivityManager.getService().resumeAppSwitches();
                    } catch (RemoteException unused) {
                    }
                    runnable.run();
                }

                public boolean willRunAnimationOnKeyguard() {
                    return z3;
                }
            }, z);
        }
    }

    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent) {
        startPendingIntentDismissingKeyguard(pendingIntent, (Runnable) null);
    }

    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable) {
        ActivityLaunchAnimator.Controller controller = null;
        startPendingIntentDismissingKeyguard(pendingIntent, runnable, (ActivityLaunchAnimator.Controller) null);
    }

    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, View view) {
        startPendingIntentDismissingKeyguard(pendingIntent, runnable, (ActivityLaunchAnimator.Controller) view instanceof ExpandableNotificationRow ? this.mNotificationAnimationProvider.getAnimatorController((ExpandableNotificationRow) view) : null);
    }

    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, ActivityLaunchAnimator.Controller controller) {
        boolean z = false;
        boolean z2 = pendingIntent.isActivity() && this.mActivityIntentHelper.wouldLaunchResolverActivity(pendingIntent.getIntent(), this.mLockscreenUserManager.getCurrentUserId());
        if (!z2 && controller != null && shouldAnimateLaunch(pendingIntent.isActivity())) {
            z = true;
        }
        boolean z3 = !z;
        executeActionDismissingKeyguard(new CentralSurfacesImpl$$ExternalSyntheticLambda7(this, controller, pendingIntent, z, z3, runnable), z2, z3, z);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startPendingIntentDismissingKeyguard$40$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ void mo43910x73efd340(ActivityLaunchAnimator.Controller controller, PendingIntent pendingIntent, boolean z, boolean z2, Runnable runnable) {
        StatusBarLaunchAnimatorController statusBarLaunchAnimatorController;
        if (controller != null) {
            try {
                statusBarLaunchAnimatorController = new StatusBarLaunchAnimatorController(controller, this, pendingIntent.isActivity());
            } catch (PendingIntent.CanceledException e) {
                Log.w(CentralSurfaces.TAG, "Sending intent failed: " + e);
                if (!z2) {
                    collapsePanelOnMainThread();
                }
            }
        } else {
            statusBarLaunchAnimatorController = null;
        }
        this.mActivityLaunchAnimator.startPendingIntentWithAnimation(statusBarLaunchAnimatorController, z, pendingIntent.getCreatorPackage(), new CentralSurfacesImpl$$ExternalSyntheticLambda10(this, pendingIntent));
        if (pendingIntent.isActivity()) {
            this.mAssistManagerLazy.get().hideAssist();
        }
        if (runnable != null) {
            postOnUiThread(runnable);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startPendingIntentDismissingKeyguard$39$com-android-systemui-statusbar-phone-CentralSurfacesImpl */
    public /* synthetic */ int mo43909x578fd696(PendingIntent pendingIntent, RemoteAnimationAdapter remoteAnimationAdapter) throws PendingIntent.CanceledException {
        ActivityOptions activityOptions = new ActivityOptions(CentralSurfaces.getActivityOptions(this.mDisplayId, remoteAnimationAdapter));
        activityOptions.setEligibleForLegacyPermissionPrompt(true);
        return pendingIntent.sendAndReturnResult((Context) null, 0, (Intent) null, (PendingIntent.OnFinished) null, (Handler) null, (String) null, activityOptions.toBundle());
    }

    private void postOnUiThread(Runnable runnable) {
        this.mMainExecutor.execute(runnable);
    }

    public void visibilityChanged(boolean z) {
        if (this.mVisible != z) {
            this.mVisible = z;
            if (!z) {
                this.mGutsManager.closeAndSaveGuts(true, true, true, -1, -1, true);
            }
        }
        updateVisibleToUser();
    }

    /* access modifiers changed from: protected */
    public void updateVisibleToUser() {
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

    public boolean isBouncerShowing() {
        return this.mBouncerShowing;
    }

    public boolean isBouncerShowingScrimmed() {
        return isBouncerShowing() && this.mStatusBarKeyguardViewManager.bouncerNeedsScrimming();
    }

    public boolean isBouncerShowingOverDream() {
        return isBouncerShowing() && this.mDreamOverlayStateController.isOverlayActive();
    }

    public void onBouncerPreHideAnimation() {
        this.mNotificationPanelViewController.onBouncerPreHideAnimation();
    }

    public boolean isKeyguardSecure() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager != null) {
            return statusBarKeyguardViewManager.isSecure();
        }
        Slog.w(CentralSurfaces.TAG, "isKeyguardSecure() called before startKeyguard(), returning false", new Throwable());
        return false;
    }

    public NotificationPanelViewController getPanelController() {
        return this.mNotificationPanelViewController;
    }

    public NotificationGutsManager getGutsManager() {
        return this.mGutsManager;
    }

    /* access modifiers changed from: package-private */
    public boolean isTransientShown() {
        return this.mTransientShown;
    }

    private void updateLightRevealScrimVisibility() {
        LightRevealScrim lightRevealScrim = this.mLightRevealScrim;
        if (lightRevealScrim != null) {
            lightRevealScrim.setAlpha(this.mScrimController.getState().getMaxLightRevealScrimAlpha());
        }
    }

    public void extendDozePulse() {
        this.mDozeScrimController.extendPulse();
    }

    /* access modifiers changed from: private */
    public UserHandle getActivityUserHandle(Intent intent) {
        String[] stringArray = this.mContext.getResources().getStringArray(C1893R.array.system_ui_packages);
        int length = stringArray.length;
        int i = 0;
        while (i < length) {
            String str = stringArray[i];
            if (intent.getComponent() == null) {
                break;
            } else if (str.equals(intent.getComponent().getPackageName())) {
                return new UserHandle(UserHandle.myUserId());
            } else {
                i++;
            }
        }
        return UserHandle.CURRENT;
    }

    public void wakeUpIfDozing(long j, View view, String str, PointF pointF) {
        if (this.mDozing && this.mScreenOffAnimationController.allowWakeUpIfDozing()) {
            this.mPowerManager.wakeUp(j, 6, "com.android.systemui:" + str);
            this.mWakeUpComingFromTouch = true;
            view.getLocationInWindow(this.mTmpInt2);
            this.mWakeUpTouchLocation = pointF;
            this.mFalsingCollector.onScreenOnFromTouch();
        }
    }
}
