package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.graphics.Matrix;
import android.hardware.biometrics.BiometricSourceType;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.core.app.NotificationCompat;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.mediator.ScreenOnCoordinator;
import com.android.systemui.C1894R;
import com.android.systemui.CoreStartable;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.DeviceConfigProxy;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.facerecognition.FaceRecognitionController;
import com.nothing.systemui.keyguard.KeyguardViewMediatorEx;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.systemui.util.SystemUIEventUtils;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;

public class KeyguardViewMediator extends CoreStartable implements Dumpable, StatusBarStateController.StateListener {
    public static final int AWAKE_INTERVAL_BOUNCER_MS = 10000;
    private static final int CANCEL_KEYGUARD_EXIT_ANIM = 19;
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_SIM_STATES = true;
    private static final String DELAYED_KEYGUARD_ACTION = "com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD";
    private static final String DELAYED_LOCK_PROFILE_ACTION = "com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK";
    private static final int DISMISS = 11;
    private static final int HIDE = 2;
    private static final int KEYGUARD_DISPLAY_TIMEOUT_DELAY_DEFAULT = 30000;
    private static final int KEYGUARD_DONE = 7;
    private static final int KEYGUARD_DONE_DRAWING = 8;
    private static final int KEYGUARD_DONE_DRAWING_TIMEOUT_MS = 2000;
    private static final int KEYGUARD_DONE_PENDING_TIMEOUT = 13;
    private static final long KEYGUARD_DONE_PENDING_TIMEOUT_MS = 3000;
    private static final int KEYGUARD_LOCK_AFTER_DELAY_DEFAULT = 5000;
    private static final int KEYGUARD_TIMEOUT = 10;
    private static final int MSG_INIT_FACE_RECOGNITION = 603;
    private static final int NOTIFY_FINISHED_GOING_TO_SLEEP = 5;
    private static final int NOTIFY_STARTED_GOING_TO_SLEEP = 17;
    private static final int NOTIFY_STARTED_WAKING_UP = 14;
    public static final String OPTION_FORCE_SHOW = "force_show";
    private static final int RESET = 3;
    private static final int SET_OCCLUDED = 9;
    private static final int SHOW = 1;
    private static final int START_KEYGUARD_EXIT_ANIM = 12;
    private static final int STATE_INVALID = -1;
    private static final String SYSTEMUI_PERMISSION = "com.android.systemui.permission.SELF";
    private static final int SYSTEM_READY = 18;
    private static final String TAG = "KeyguardViewMediator";
    private static final int UNOCCLUDE_ANIMATION_DURATION = 250;
    private static final float UNOCCLUDE_TRANSLATE_DISTANCE_PERCENT = 0.1f;
    private static final Intent USER_PRESENT_INTENT = new Intent("android.intent.action.USER_PRESENT").addFlags(606076928);
    private static final int VERIFY_UNLOCK = 4;
    /* access modifiers changed from: private */
    public static SparseIntArray mUnlockTrackSimStates = new SparseIntArray();
    /* access modifiers changed from: private */
    public Lazy<ActivityLaunchAnimator> mActivityLaunchAnimator;
    private AlarmManager mAlarmManager;
    private boolean mAnimatingScreenOff;
    private boolean mAodShowing;
    private AudioManager mAudioManager;
    private boolean mBootCompleted;
    private boolean mBootSendUserPresent;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final BroadcastReceiver mBroadcastReceiver;
    /* access modifiers changed from: private */
    public CentralSurfaces mCentralSurfaces;
    /* access modifiers changed from: private */
    public CharSequence mCustomMessage;
    private final BroadcastReceiver mDelayedLockBroadcastReceiver;
    /* access modifiers changed from: private */
    public int mDelayedProfileShowingSequence;
    /* access modifiers changed from: private */
    public int mDelayedShowingSequence;
    private DeviceConfigProxy mDeviceConfig;
    /* access modifiers changed from: private */
    public boolean mDeviceInteractive;
    private final DismissCallbackRegistry mDismissCallbackRegistry;
    private DozeParameters mDozeParameters;
    private boolean mDozing;
    /* access modifiers changed from: private */
    public boolean mDreamOverlayShowing;
    private final DreamOverlayStateController.Callback mDreamOverlayStateCallback;
    /* access modifiers changed from: private */
    public final DreamOverlayStateController mDreamOverlayStateController;
    private IKeyguardExitCallback mExitSecureCallback;
    private boolean mExternallyEnabled = true;
    private FaceRecognitionController mFaceRecognitionController;
    /* access modifiers changed from: private */
    public final FalsingCollector mFalsingCollector;
    private boolean mGoingToSleep;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private Animation mHideAnimation;
    /* access modifiers changed from: private */
    public final Runnable mHideAnimationFinishedRunnable;
    /* access modifiers changed from: private */
    public boolean mHideAnimationRun = false;
    /* access modifiers changed from: private */
    public boolean mHideAnimationRunning = false;
    private boolean mHiding;
    private boolean mInGestureNavigationMode;
    private boolean mInputRestricted;
    /* access modifiers changed from: private */
    public final InteractionJankMonitor mInteractionJankMonitor;
    /* access modifiers changed from: private */
    public final KeyguardDisplayManager mKeyguardDisplayManager;
    /* access modifiers changed from: private */
    public boolean mKeyguardDonePending = false;
    private IRemoteAnimationRunner mKeyguardExitAnimationRunner;
    private final Runnable mKeyguardGoingAwayRunnable;
    /* access modifiers changed from: private */
    public final ArrayList<IKeyguardStateCallback> mKeyguardStateCallbacks = new ArrayList<>();
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    private final KeyguardStateController.Callback mKeyguardStateControllerCallback;
    private final Lazy<KeyguardUnlockAnimationController> mKeyguardUnlockAnimationControllerLazy;
    /* access modifiers changed from: private */
    public final Lazy<KeyguardViewController> mKeyguardViewControllerLazy;
    /* access modifiers changed from: private */
    public final SparseIntArray mLastSimStates = new SparseIntArray();
    private boolean mLockLater;
    /* access modifiers changed from: private */
    public final LockPatternUtils mLockPatternUtils;
    private int mLockSoundId;
    private int mLockSoundStreamId;
    private float mLockSoundVolume;
    private SoundPool mLockSounds;
    private boolean mNeedToReshowWhenReenabled = false;
    private final Lazy<NotificationShadeDepthController> mNotificationShadeDepthController;
    /* access modifiers changed from: private */
    public final Lazy<NotificationShadeWindowController> mNotificationShadeWindowControllerLazy;
    private final ActivityLaunchAnimator.Controller mOccludeAnimationController;
    private IRemoteAnimationRunner mOccludeAnimationRunner;
    /* access modifiers changed from: private */
    public boolean mOccluded = false;
    private final DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener;
    private final PowerManager mPM;
    private boolean mPendingLock;
    private boolean mPendingReset;
    private String mPhoneState = TelephonyManager.EXTRA_STATE_IDLE;
    /* access modifiers changed from: private */
    public final float mPowerButtonY;
    private boolean mPowerGestureIntercepted = false;
    private final ScreenOffAnimationController mScreenOffAnimationController;
    private ScreenOnCoordinator mScreenOnCoordinator;
    /* access modifiers changed from: private */
    public boolean mShowHomeOverLockscreen;
    private PowerManager.WakeLock mShowKeyguardWakeLock;
    /* access modifiers changed from: private */
    public boolean mShowing;
    /* access modifiers changed from: private */
    public boolean mShuttingDown;
    /* access modifiers changed from: private */
    public final SparseBooleanArray mSimWasLocked = new SparseBooleanArray();
    private StatusBarManager mStatusBarManager;
    private final SysuiStatusBarStateController mStatusBarStateController;
    private IRemoteAnimationFinishedCallback mSurfaceBehindRemoteAnimationFinishedCallback;
    private boolean mSurfaceBehindRemoteAnimationRequested = false;
    private boolean mSurfaceBehindRemoteAnimationRunning;
    private boolean mSystemReady;
    private final TrustManager mTrustManager;
    private int mTrustedSoundId;
    /* access modifiers changed from: private */
    public final Executor mUiBgExecutor;
    private int mUiSoundsStreamType;
    private int mUnlockSoundId;
    private final IRemoteAnimationRunner mUnoccludeAnimationRunner;
    KeyguardUpdateMonitorCallback mUpdateCallback;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mUpdateMonitor;
    private final UserSwitcherController mUserSwitcherController;
    ViewMediatorCallback mViewMediatorCallback;
    private boolean mWaitingUntilKeyguardVisible = false;
    /* access modifiers changed from: private */
    public boolean mWakeAndUnlocking = false;
    /* access modifiers changed from: private */
    public boolean mWallpaperSupportsAmbientMode;
    /* access modifiers changed from: private */
    public final float mWindowCornerRadius;
    private WorkLockActivityController mWorkLockController;

    public void dismissKeyguardToLaunch(Intent intent) {
    }

    public void onShortPowerPressedGoHome() {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardViewMediator(Context context, FalsingCollector falsingCollector, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, Lazy<KeyguardViewController> lazy, DismissCallbackRegistry dismissCallbackRegistry, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, @UiBackground Executor executor, PowerManager powerManager, TrustManager trustManager, UserSwitcherController userSwitcherController, DeviceConfigProxy deviceConfigProxy, NavigationModeController navigationModeController, KeyguardDisplayManager keyguardDisplayManager, DozeParameters dozeParameters, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, Lazy<KeyguardUnlockAnimationController> lazy2, ScreenOffAnimationController screenOffAnimationController, Lazy<NotificationShadeDepthController> lazy3, ScreenOnCoordinator screenOnCoordinator, InteractionJankMonitor interactionJankMonitor, DreamOverlayStateController dreamOverlayStateController, Lazy<NotificationShadeWindowController> lazy4, Lazy<ActivityLaunchAnimator> lazy5) {
        super(context);
        DeviceConfigProxy deviceConfigProxy2 = deviceConfigProxy;
        SysuiStatusBarStateController sysuiStatusBarStateController2 = sysuiStatusBarStateController;
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        C21631 r5 = new DeviceConfig.OnPropertiesChangedListener() {
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("nav_bar_handle_show_over_lockscreen")) {
                    boolean unused = KeyguardViewMediator.this.mShowHomeOverLockscreen = properties.getBoolean("nav_bar_handle_show_over_lockscreen", true);
                }
            }
        };
        this.mOnPropertiesChangedListener = r5;
        this.mDreamOverlayStateCallback = new DreamOverlayStateController.Callback() {
            public void onStateChanged() {
                KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                boolean unused = keyguardViewMediator.mDreamOverlayShowing = keyguardViewMediator.mDreamOverlayStateController.isOverlayActive();
            }
        };
        this.mUpdateCallback = new KeyguardUpdateMonitorCallback() {
            public void onUserInfoChanged(int i) {
            }

            public void onUserSwitching(int i) {
                Log.d(KeyguardViewMediator.TAG, String.format("onUserSwitching %d", Integer.valueOf(i)));
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator.this.resetKeyguardDonePendingLocked();
                    if (KeyguardViewMediator.this.mLockPatternUtils.isLockScreenDisabled(i)) {
                        KeyguardViewMediator.this.dismiss((IKeyguardDismissCallback) null, (CharSequence) null);
                    } else {
                        KeyguardViewMediator.this.resetStateLocked();
                    }
                    KeyguardViewMediator.this.adjustStatusBarLocked();
                }
            }

            public void onUserSwitchComplete(int i) {
                UserInfo userInfo;
                Log.d(KeyguardViewMediator.TAG, String.format("onUserSwitchComplete %d", Integer.valueOf(i)));
                if (i != 0 && (userInfo = UserManager.get(KeyguardViewMediator.this.mContext).getUserInfo(i)) != null && !KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                    if (userInfo.isGuest() || userInfo.isDemo()) {
                        KeyguardViewMediator.this.dismiss((IKeyguardDismissCallback) null, (CharSequence) null);
                    }
                }
            }

            public void onClockVisibilityChanged() {
                KeyguardViewMediator.this.adjustStatusBarLocked();
            }

            public void onDeviceProvisioned() {
                KeyguardViewMediator.this.sendUserPresentBroadcast();
                synchronized (KeyguardViewMediator.this) {
                    if (KeyguardViewMediator.this.mustNotUnlockCurrentUser()) {
                        KeyguardViewMediator.this.doKeyguardLocked((Bundle) null);
                    }
                }
            }

            /* JADX WARNING: Code restructure failed: missing block: B:110:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:111:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:112:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:113:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:114:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:0x00cb, code lost:
                if (r11 == 1) goto L_0x0193;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:36:0x00cd, code lost:
                if (r11 == 2) goto L_0x0168;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:37:0x00cf, code lost:
                if (r11 == 3) goto L_0x0168;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:38:0x00d1, code lost:
                if (r11 == 5) goto L_0x0119;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:40:0x00d4, code lost:
                if (r11 == 6) goto L_0x0193;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:42:0x00d7, code lost:
                if (r11 == 7) goto L_0x00ef;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:43:0x00d9, code lost:
                android.util.Log.v(com.android.systemui.keyguard.KeyguardViewMediator.TAG, "Unspecific state: " + r11);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:44:0x00ef, code lost:
                r2 = r8.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:45:0x00f1, code lost:
                monitor-enter(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:48:0x00f8, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.access$1300(r8.this$0) != false) goto L_0x0107;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:49:0x00fa, code lost:
                android.util.Log.d(com.android.systemui.keyguard.KeyguardViewMediator.TAG, "PERM_DISABLED and keygaurd isn't showing.");
                r8.this$0.doKeyguardLocked((android.os.Bundle) null);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:50:0x0107, code lost:
                android.util.Log.d(com.android.systemui.keyguard.KeyguardViewMediator.TAG, "PERM_DISABLED, resetStateLocked toshow permanently disabled message in lockscreen.");
                r8.this$0.resetStateLocked();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:51:0x0113, code lost:
                monitor-exit(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:56:0x0119, code lost:
                r11 = r8.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:57:0x011b, code lost:
                monitor-enter(r11);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
                android.util.Log.d(com.android.systemui.keyguard.KeyguardViewMediator.TAG, "READY, reset state? " + com.android.systemui.keyguard.KeyguardViewMediator.access$1300(r8.this$0));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:61:0x0140, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.access$1300(r8.this$0) == false) goto L_0x0163;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:63:0x014c, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.access$1400(r8.this$0).get(r10, false) == false) goto L_0x0163;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:64:0x014e, code lost:
                android.util.Log.d(com.android.systemui.keyguard.KeyguardViewMediator.TAG, "SIM moved to READY when the previously was locked. Reset the state.");
                com.android.systemui.keyguard.KeyguardViewMediator.access$1400(r8.this$0).append(r10, false);
                r8.this$0.resetStateLocked();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:65:0x0163, code lost:
                monitor-exit(r11);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:70:0x0168, code lost:
                r2 = r8.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:71:0x016a, code lost:
                monitor-enter(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:73:?, code lost:
                com.android.systemui.keyguard.KeyguardViewMediator.access$1400(r8.this$0).append(r10, true);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:74:0x017a, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.access$1300(r8.this$0) != false) goto L_0x0189;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:75:0x017c, code lost:
                android.util.Log.d(com.android.systemui.keyguard.KeyguardViewMediator.TAG, "INTENT_VALUE_ICC_LOCKED and keygaurd isn't showing; need to show keyguard so user can enter sim pin");
                r8.this$0.doKeyguardLocked((android.os.Bundle) null);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:76:0x0189, code lost:
                r8.this$0.resetStateLocked();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:77:0x018e, code lost:
                monitor-exit(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:82:0x0193, code lost:
                r2 = r8.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:83:0x0195, code lost:
                monitor-enter(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:86:0x019c, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.access$1200(r8.this$0) == false) goto L_0x01b8;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:88:0x01a4, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.access$1300(r8.this$0) != false) goto L_0x01b3;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:89:0x01a6, code lost:
                android.util.Log.d(com.android.systemui.keyguard.KeyguardViewMediator.TAG, "ICC_ABSENT isn't showing, we need to show the keyguard since the device isn't provisioned yet.");
                r8.this$0.doKeyguardLocked((android.os.Bundle) null);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:90:0x01b3, code lost:
                r8.this$0.resetStateLocked();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:91:0x01b8, code lost:
                if (r11 != 1) goto L_0x01d1;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:92:0x01ba, code lost:
                if (r0 == false) goto L_0x01c8;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:93:0x01bc, code lost:
                android.util.Log.d(com.android.systemui.keyguard.KeyguardViewMediator.TAG, "SIM moved to ABSENT when the previous state was locked. Reset the state.");
                r8.this$0.resetStateLocked();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:94:0x01c8, code lost:
                com.android.systemui.keyguard.KeyguardViewMediator.access$1400(r8.this$0).append(r10, false);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:95:0x01d1, code lost:
                monitor-exit(r2);
             */
            /* JADX WARNING: Removed duplicated region for block: B:20:0x009c  */
            /* JADX WARNING: Removed duplicated region for block: B:26:0x00b2  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onSimStateChanged(int r9, int r10, int r11) {
                /*
                    r8 = this;
                    java.lang.String r0 = "KeyguardViewMediator"
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    java.lang.String r2 = "onSimStateChanged(subId="
                    r1.<init>((java.lang.String) r2)
                    java.lang.StringBuilder r9 = r1.append((int) r9)
                    java.lang.String r1 = ", slotId="
                    java.lang.StringBuilder r9 = r9.append((java.lang.String) r1)
                    java.lang.StringBuilder r9 = r9.append((int) r10)
                    java.lang.String r1 = ",state="
                    java.lang.StringBuilder r9 = r9.append((java.lang.String) r1)
                    java.lang.StringBuilder r9 = r9.append((int) r11)
                    java.lang.String r1 = ")"
                    java.lang.StringBuilder r9 = r9.append((java.lang.String) r1)
                    java.lang.String r9 = r9.toString()
                    android.util.Log.d(r0, r9)
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    java.util.ArrayList r9 = r9.mKeyguardStateCallbacks
                    int r9 = r9.size()
                    com.android.systemui.keyguard.KeyguardViewMediator r0 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    com.android.keyguard.KeyguardUpdateMonitor r0 = r0.mUpdateMonitor
                    boolean r0 = r0.isSimPinSecure()
                    r1 = 1
                    int r9 = r9 - r1
                L_0x0045:
                    if (r9 < 0) goto L_0x006f
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ RemoteException -> 0x0057 }
                    java.util.ArrayList r2 = r2.mKeyguardStateCallbacks     // Catch:{ RemoteException -> 0x0057 }
                    java.lang.Object r2 = r2.get(r9)     // Catch:{ RemoteException -> 0x0057 }
                    com.android.internal.policy.IKeyguardStateCallback r2 = (com.android.internal.policy.IKeyguardStateCallback) r2     // Catch:{ RemoteException -> 0x0057 }
                    r2.onSimSecureStateChanged(r0)     // Catch:{ RemoteException -> 0x0057 }
                    goto L_0x006c
                L_0x0057:
                    r2 = move-exception
                    java.lang.String r3 = "KeyguardViewMediator"
                    java.lang.String r4 = "Failed to call onSimSecureStateChanged"
                    android.util.Slog.w(r3, r4, r2)
                    boolean r2 = r2 instanceof android.os.DeadObjectException
                    if (r2 == 0) goto L_0x006c
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    java.util.ArrayList r2 = r2.mKeyguardStateCallbacks
                    r2.remove((int) r9)
                L_0x006c:
                    int r9 = r9 + -1
                    goto L_0x0045
                L_0x006f:
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r9)
                    com.android.systemui.keyguard.KeyguardViewMediator r0 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01d6 }
                    android.util.SparseIntArray r0 = r0.mLastSimStates     // Catch:{ all -> 0x01d6 }
                    int r0 = r0.get(r10)     // Catch:{ all -> 0x01d6 }
                    r2 = 3
                    r3 = 0
                    r4 = 2
                    if (r0 == r4) goto L_0x0086
                    if (r0 != r2) goto L_0x0084
                    goto L_0x0086
                L_0x0084:
                    r0 = r3
                    goto L_0x0087
                L_0x0086:
                    r0 = r1
                L_0x0087:
                    com.android.systemui.keyguard.KeyguardViewMediator r5 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01d6 }
                    android.util.SparseIntArray r5 = r5.mLastSimStates     // Catch:{ all -> 0x01d6 }
                    r5.append(r10, r11)     // Catch:{ all -> 0x01d6 }
                    android.util.SparseIntArray r5 = com.android.systemui.keyguard.KeyguardViewMediator.mUnlockTrackSimStates     // Catch:{ all -> 0x01d6 }
                    r6 = -1
                    int r5 = r5.get(r10, r6)     // Catch:{ all -> 0x01d6 }
                    r6 = 5
                    if (r11 != r6) goto L_0x00b2
                    r7 = 10
                    if (r5 != r7) goto L_0x00aa
                    java.lang.String r8 = "KeyguardViewMediator"
                    java.lang.String r10 = "skip the redundant SIM_STATE_READY state"
                    android.util.Log.e(r8, r10)     // Catch:{ all -> 0x01d6 }
                    monitor-exit(r9)     // Catch:{ all -> 0x01d6 }
                    return
                L_0x00aa:
                    android.util.SparseIntArray r7 = com.android.systemui.keyguard.KeyguardViewMediator.mUnlockTrackSimStates     // Catch:{ all -> 0x01d6 }
                    r7.put(r10, r11)     // Catch:{ all -> 0x01d6 }
                    goto L_0x00bb
                L_0x00b2:
                    if (r11 == r4) goto L_0x00bb
                    android.util.SparseIntArray r7 = com.android.systemui.keyguard.KeyguardViewMediator.mUnlockTrackSimStates     // Catch:{ all -> 0x01d6 }
                    r7.put(r10, r11)     // Catch:{ all -> 0x01d6 }
                L_0x00bb:
                    if (r5 != r6) goto L_0x00c9
                    if (r11 != r4) goto L_0x00c9
                    java.lang.String r8 = "KeyguardViewMediator"
                    java.lang.String r10 = "skip the unnecessary SIM_STATE_PIN_REQUIRED state"
                    android.util.Log.e(r8, r10)     // Catch:{ all -> 0x01d6 }
                    monitor-exit(r9)     // Catch:{ all -> 0x01d6 }
                    return
                L_0x00c9:
                    monitor-exit(r9)     // Catch:{ all -> 0x01d6 }
                    r9 = 0
                    if (r11 == r1) goto L_0x0193
                    if (r11 == r4) goto L_0x0168
                    if (r11 == r2) goto L_0x0168
                    if (r11 == r6) goto L_0x0119
                    r2 = 6
                    if (r11 == r2) goto L_0x0193
                    r10 = 7
                    if (r11 == r10) goto L_0x00ef
                    java.lang.String r8 = "KeyguardViewMediator"
                    java.lang.StringBuilder r9 = new java.lang.StringBuilder
                    java.lang.String r10 = "Unspecific state: "
                    r9.<init>((java.lang.String) r10)
                    java.lang.StringBuilder r9 = r9.append((int) r11)
                    java.lang.String r9 = r9.toString()
                    android.util.Log.v(r8, r9)
                    goto L_0x01d2
                L_0x00ef:
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r2)
                    com.android.systemui.keyguard.KeyguardViewMediator r10 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0116 }
                    boolean r10 = r10.mShowing     // Catch:{ all -> 0x0116 }
                    if (r10 != 0) goto L_0x0107
                    java.lang.String r10 = "KeyguardViewMediator"
                    java.lang.String r11 = "PERM_DISABLED and keygaurd isn't showing."
                    android.util.Log.d(r10, r11)     // Catch:{ all -> 0x0116 }
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0116 }
                    r8.doKeyguardLocked(r9)     // Catch:{ all -> 0x0116 }
                    goto L_0x0113
                L_0x0107:
                    java.lang.String r9 = "KeyguardViewMediator"
                    java.lang.String r10 = "PERM_DISABLED, resetStateLocked toshow permanently disabled message in lockscreen."
                    android.util.Log.d(r9, r10)     // Catch:{ all -> 0x0116 }
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0116 }
                    r8.resetStateLocked()     // Catch:{ all -> 0x0116 }
                L_0x0113:
                    monitor-exit(r2)     // Catch:{ all -> 0x0116 }
                    goto L_0x01d2
                L_0x0116:
                    r8 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x0116 }
                    throw r8
                L_0x0119:
                    com.android.systemui.keyguard.KeyguardViewMediator r11 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r11)
                    java.lang.String r9 = "KeyguardViewMediator"
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0165 }
                    r0.<init>()     // Catch:{ all -> 0x0165 }
                    java.lang.String r1 = "READY, reset state? "
                    java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)     // Catch:{ all -> 0x0165 }
                    com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0165 }
                    boolean r1 = r1.mShowing     // Catch:{ all -> 0x0165 }
                    java.lang.StringBuilder r0 = r0.append((boolean) r1)     // Catch:{ all -> 0x0165 }
                    java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0165 }
                    android.util.Log.d(r9, r0)     // Catch:{ all -> 0x0165 }
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0165 }
                    boolean r9 = r9.mShowing     // Catch:{ all -> 0x0165 }
                    if (r9 == 0) goto L_0x0163
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0165 }
                    android.util.SparseBooleanArray r9 = r9.mSimWasLocked     // Catch:{ all -> 0x0165 }
                    boolean r9 = r9.get(r10, r3)     // Catch:{ all -> 0x0165 }
                    if (r9 == 0) goto L_0x0163
                    java.lang.String r9 = "KeyguardViewMediator"
                    java.lang.String r0 = "SIM moved to READY when the previously was locked. Reset the state."
                    android.util.Log.d(r9, r0)     // Catch:{ all -> 0x0165 }
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0165 }
                    android.util.SparseBooleanArray r9 = r9.mSimWasLocked     // Catch:{ all -> 0x0165 }
                    r9.append(r10, r3)     // Catch:{ all -> 0x0165 }
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0165 }
                    r8.resetStateLocked()     // Catch:{ all -> 0x0165 }
                L_0x0163:
                    monitor-exit(r11)     // Catch:{ all -> 0x0165 }
                    goto L_0x01d2
                L_0x0165:
                    r8 = move-exception
                    monitor-exit(r11)     // Catch:{ all -> 0x0165 }
                    throw r8
                L_0x0168:
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r2)
                    com.android.systemui.keyguard.KeyguardViewMediator r11 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0190 }
                    android.util.SparseBooleanArray r11 = r11.mSimWasLocked     // Catch:{ all -> 0x0190 }
                    r11.append(r10, r1)     // Catch:{ all -> 0x0190 }
                    com.android.systemui.keyguard.KeyguardViewMediator r10 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0190 }
                    boolean r10 = r10.mShowing     // Catch:{ all -> 0x0190 }
                    if (r10 != 0) goto L_0x0189
                    java.lang.String r10 = "KeyguardViewMediator"
                    java.lang.String r11 = "INTENT_VALUE_ICC_LOCKED and keygaurd isn't showing; need to show keyguard so user can enter sim pin"
                    android.util.Log.d(r10, r11)     // Catch:{ all -> 0x0190 }
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0190 }
                    r8.doKeyguardLocked(r9)     // Catch:{ all -> 0x0190 }
                    goto L_0x018e
                L_0x0189:
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0190 }
                    r8.resetStateLocked()     // Catch:{ all -> 0x0190 }
                L_0x018e:
                    monitor-exit(r2)     // Catch:{ all -> 0x0190 }
                    goto L_0x01d2
                L_0x0190:
                    r8 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x0190 }
                    throw r8
                L_0x0193:
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r2)
                    com.android.systemui.keyguard.KeyguardViewMediator r4 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01d3 }
                    boolean r4 = r4.shouldWaitForProvisioning()     // Catch:{ all -> 0x01d3 }
                    if (r4 == 0) goto L_0x01b8
                    com.android.systemui.keyguard.KeyguardViewMediator r4 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01d3 }
                    boolean r4 = r4.mShowing     // Catch:{ all -> 0x01d3 }
                    if (r4 != 0) goto L_0x01b3
                    java.lang.String r4 = "KeyguardViewMediator"
                    java.lang.String r5 = "ICC_ABSENT isn't showing, we need to show the keyguard since the device isn't provisioned yet."
                    android.util.Log.d(r4, r5)     // Catch:{ all -> 0x01d3 }
                    com.android.systemui.keyguard.KeyguardViewMediator r4 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01d3 }
                    r4.doKeyguardLocked(r9)     // Catch:{ all -> 0x01d3 }
                    goto L_0x01b8
                L_0x01b3:
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01d3 }
                    r9.resetStateLocked()     // Catch:{ all -> 0x01d3 }
                L_0x01b8:
                    if (r11 != r1) goto L_0x01d1
                    if (r0 == 0) goto L_0x01c8
                    java.lang.String r9 = "KeyguardViewMediator"
                    java.lang.String r11 = "SIM moved to ABSENT when the previous state was locked. Reset the state."
                    android.util.Log.d(r9, r11)     // Catch:{ all -> 0x01d3 }
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01d3 }
                    r9.resetStateLocked()     // Catch:{ all -> 0x01d3 }
                L_0x01c8:
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01d3 }
                    android.util.SparseBooleanArray r8 = r8.mSimWasLocked     // Catch:{ all -> 0x01d3 }
                    r8.append(r10, r3)     // Catch:{ all -> 0x01d3 }
                L_0x01d1:
                    monitor-exit(r2)     // Catch:{ all -> 0x01d3 }
                L_0x01d2:
                    return
                L_0x01d3:
                    r8 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x01d3 }
                    throw r8
                L_0x01d6:
                    r8 = move-exception
                    monitor-exit(r9)     // Catch:{ all -> 0x01d6 }
                    throw r8
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.C21693.onSimStateChanged(int, int, int):void");
            }

            public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(currentUser)) {
                    KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportFailedBiometricAttempt(currentUser);
                }
            }

            public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
                if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                    KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportSuccessfulBiometricAttempt(i);
                }
            }

            public void onTrustChanged(int i) {
                if (i == KeyguardUpdateMonitor.getCurrentUser()) {
                    synchronized (KeyguardViewMediator.this) {
                        KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                        keyguardViewMediator.notifyTrustedChangedLocked(keyguardViewMediator.mUpdateMonitor.getUserHasTrust(i));
                    }
                }
            }
        };
        this.mViewMediatorCallback = new ViewMediatorCallback() {
            public void userActivity() {
                KeyguardViewMediator.this.userActivity();
            }

            public void keyguardDone(boolean z, int i) {
                if (i == ActivityManager.getCurrentUser()) {
                    Log.d(KeyguardViewMediator.TAG, "keyguardDone");
                    KeyguardViewMediator.this.tryKeyguardDone();
                }
            }

            public void keyguardDoneDrawing() {
                Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDoneDrawing");
                KeyguardViewMediator.this.mHandler.sendEmptyMessage(8);
                Trace.endSection();
            }

            public void setNeedsInput(boolean z) {
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setNeedsInput(z);
            }

            public void keyguardDonePending(boolean z, int i) {
                Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDonePending");
                Log.d(KeyguardViewMediator.TAG, "keyguardDonePending");
                if (i != ActivityManager.getCurrentUser()) {
                    Trace.endSection();
                    return;
                }
                boolean unused = KeyguardViewMediator.this.mKeyguardDonePending = true;
                boolean unused2 = KeyguardViewMediator.this.mHideAnimationRun = true;
                boolean unused3 = KeyguardViewMediator.this.mHideAnimationRunning = true;
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).startPreHideAnimation(KeyguardViewMediator.this.mHideAnimationFinishedRunnable);
                KeyguardViewMediator.this.mHandler.sendEmptyMessageDelayed(13, 3000);
                Trace.endSection();
            }

            public void keyguardGone() {
                Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardGone");
                Log.d(KeyguardViewMediator.TAG, "keyguardGone");
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setKeyguardGoingAwayState(false);
                KeyguardViewMediator.this.mKeyguardDisplayManager.hide();
                Trace.endSection();
            }

            public void readyForKeyguardDone() {
                Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#readyForKeyguardDone");
                if (KeyguardViewMediator.this.mKeyguardDonePending) {
                    boolean unused = KeyguardViewMediator.this.mKeyguardDonePending = false;
                    KeyguardViewMediator.this.tryKeyguardDone();
                }
                Trace.endSection();
            }

            public void resetKeyguard() {
                KeyguardViewMediator.this.resetStateLocked();
            }

            public void onCancelClicked() {
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).onCancelClicked();
            }

            public void playTrustedSound() {
                KeyguardViewMediator.this.playTrustedSound();
            }

            public boolean isScreenOn() {
                return KeyguardViewMediator.this.mDeviceInteractive;
            }

            public int getBouncerPromptReason() {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                boolean isTrustUsuallyManaged = KeyguardViewMediator.this.mUpdateMonitor.isTrustUsuallyManaged(currentUser);
                boolean z = isTrustUsuallyManaged || KeyguardViewMediator.this.mUpdateMonitor.isUnlockingWithBiometricsPossible(currentUser);
                KeyguardUpdateMonitor.StrongAuthTracker strongAuthTracker = KeyguardViewMediator.this.mUpdateMonitor.getStrongAuthTracker();
                int strongAuthForUser = strongAuthTracker.getStrongAuthForUser(currentUser);
                if (z && !strongAuthTracker.hasUserAuthenticatedSinceBoot()) {
                    return 1;
                }
                if (z && (strongAuthForUser & 16) != 0) {
                    return 2;
                }
                if ((strongAuthForUser & 2) != 0) {
                    return 3;
                }
                if (isTrustUsuallyManaged && (strongAuthForUser & 4) != 0) {
                    return 4;
                }
                if (z && ((strongAuthForUser & 8) != 0 || KeyguardViewMediator.this.mUpdateMonitor.isFingerprintLockedOut())) {
                    return 5;
                }
                if (z && (strongAuthForUser & 64) != 0) {
                    return 6;
                }
                if (!z || (strongAuthForUser & 128) == 0) {
                    return 0;
                }
                return 7;
            }

            public CharSequence consumeCustomMessage() {
                CharSequence access$2600 = KeyguardViewMediator.this.mCustomMessage;
                CharSequence unused = KeyguardViewMediator.this.mCustomMessage = null;
                return access$2600;
            }
        };
        C21715 r6 = new ActivityLaunchAnimator.Controller() {
            public void onLaunchAnimationStart(boolean z) {
            }

            public void onLaunchAnimationCancelled() {
                Log.d(KeyguardViewMediator.TAG, "Occlude launch animation cancelled. Occluded state is now: " + KeyguardViewMediator.this.mOccluded);
            }

            public void onLaunchAnimationEnd(boolean z) {
                if (z) {
                    KeyguardViewMediator.this.mCentralSurfaces.instantCollapseNotificationPanel();
                }
            }

            public ViewGroup getLaunchContainer() {
                return (ViewGroup) ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).getViewRootImpl().getView();
            }

            public void setLaunchContainer(ViewGroup viewGroup) {
                Log.wtf(KeyguardViewMediator.TAG, "Someone tried to change the launch container for the ActivityLaunchAnimator, which should never happen.");
            }

            public LaunchAnimator.State createAnimatorState() {
                int width = getLaunchContainer().getWidth();
                int height = getLaunchContainer().getHeight();
                float f = ((float) height) / 3.0f;
                float f2 = (float) width;
                float f3 = f2 / 3.0f;
                if (KeyguardViewMediator.this.mUpdateMonitor.isSecureCameraLaunchedOverKeyguard()) {
                    float f4 = f / 2.0f;
                    return new LaunchAnimator.State((int) (KeyguardViewMediator.this.mPowerButtonY - f4), (int) (KeyguardViewMediator.this.mPowerButtonY + f4), (int) (f2 - f3), width, KeyguardViewMediator.this.mWindowCornerRadius, KeyguardViewMediator.this.mWindowCornerRadius);
                }
                int i = height / 2;
                int i2 = width / 2;
                return new LaunchAnimator.State(i, i, i2, i2, KeyguardViewMediator.this.mWindowCornerRadius, KeyguardViewMediator.this.mWindowCornerRadius);
            }
        };
        this.mOccludeAnimationController = r6;
        this.mOccludeAnimationRunner = new OccludeActivityLaunchRemoteAnimationRunner(r6);
        this.mUnoccludeAnimationRunner = new IRemoteAnimationRunner.Stub() {
            /* access modifiers changed from: private */
            public ValueAnimator mUnoccludeAnimator;
            private final Matrix mUnoccludeMatrix = new Matrix();

            public void onAnimationCancelled(boolean z) {
                ValueAnimator valueAnimator = this.mUnoccludeAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                KeyguardViewMediator.this.setOccluded(z, false);
                Log.d(KeyguardViewMediator.TAG, "Unocclude animation cancelled. Occluded state is now: " + KeyguardViewMediator.this.mOccluded);
            }

            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
                RemoteAnimationTarget remoteAnimationTarget;
                Log.d(KeyguardViewMediator.TAG, "UnoccludeAnimator#onAnimationStart. Set occluded = false.");
                KeyguardViewMediator.this.setOccluded(false, true);
                if (remoteAnimationTargetArr == null || remoteAnimationTargetArr.length == 0 || (remoteAnimationTarget = remoteAnimationTargetArr[0]) == null) {
                    Log.d(KeyguardViewMediator.TAG, "No apps provided to unocclude runner; skipping animation and unoccluding.");
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                    return;
                }
                KeyguardViewMediator.this.mContext.getMainExecutor().execute(new KeyguardViewMediator$6$$ExternalSyntheticLambda1(this, remoteAnimationTarget, new SyncRtSurfaceTransactionApplier(((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).getViewRootImpl().getView()), iRemoteAnimationFinishedCallback));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onAnimationStart$1$com-android-systemui-keyguard-KeyguardViewMediator$6 */
            public /* synthetic */ void mo33301x31a5aa94(RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier, final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
                ValueAnimator valueAnimator = this.mUnoccludeAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                this.mUnoccludeAnimator = ofFloat;
                ofFloat.setDuration(250);
                this.mUnoccludeAnimator.setInterpolator(Interpolators.TOUCH_RESPONSE);
                this.mUnoccludeAnimator.addUpdateListener(new KeyguardViewMediator$6$$ExternalSyntheticLambda0(this, remoteAnimationTarget, syncRtSurfaceTransactionApplier));
                this.mUnoccludeAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        try {
                            iRemoteAnimationFinishedCallback.onAnimationFinished();
                            ValueAnimator unused = C21726.this.mUnoccludeAnimator = null;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
                this.mUnoccludeAnimator.start();
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onAnimationStart$0$com-android-systemui-keyguard-KeyguardViewMediator$6 */
            public /* synthetic */ void mo33300x178a2bf5(RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier, ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                this.mUnoccludeMatrix.setTranslate(0.0f, (1.0f - floatValue) * ((float) remoteAnimationTarget.screenSpaceBounds.height()) * 0.1f);
                syncRtSurfaceTransactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget.leash).withMatrix(this.mUnoccludeMatrix).withCornerRadius(KeyguardViewMediator.this.mWindowCornerRadius).withAlpha(floatValue).build()});
            }
        };
        C21747 r62 = new KeyguardStateController.Callback() {
            public void onBouncerShowingChanged() {
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    keyguardViewMediator.adjustStatusBarLocked(keyguardViewMediator.mKeyguardStateController.isBouncerShowing(), false);
                }
            }
        };
        this.mKeyguardStateControllerCallback = r62;
        this.mDelayedLockBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (KeyguardViewMediator.DELAYED_KEYGUARD_ACTION.equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra("seq", 0);
                    Log.d(KeyguardViewMediator.TAG, "received DELAYED_KEYGUARD_ACTION with seq = " + intExtra + ", mDelayedShowingSequence = " + KeyguardViewMediator.this.mDelayedShowingSequence);
                    synchronized (KeyguardViewMediator.this) {
                        if (KeyguardViewMediator.this.mDelayedShowingSequence == intExtra) {
                            KeyguardViewMediator.this.doKeyguardLocked((Bundle) null);
                        }
                    }
                } else if (KeyguardViewMediator.DELAYED_LOCK_PROFILE_ACTION.equals(intent.getAction())) {
                    int intExtra2 = intent.getIntExtra("seq", 0);
                    int intExtra3 = intent.getIntExtra("android.intent.extra.USER_ID", 0);
                    if (intExtra3 != 0) {
                        synchronized (KeyguardViewMediator.this) {
                            if (KeyguardViewMediator.this.mDelayedProfileShowingSequence == intExtra2) {
                                KeyguardViewMediator.this.lockProfile(intExtra3);
                            }
                        }
                    }
                }
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                    synchronized (KeyguardViewMediator.this) {
                        boolean unused = KeyguardViewMediator.this.mShuttingDown = true;
                    }
                }
            }
        };
        this.mHandler = new Handler(Looper.myLooper(), (Handler.Callback) null, true) {
            public void handleMessage(Message message) {
                NTLogUtil.m1686d(KeyguardViewMediator.TAG, "handleMessage " + message.what);
                int i = message.what;
                boolean z = true;
                if (i == 1) {
                    KeyguardViewMediator.this.handleShow((Bundle) message.obj);
                } else if (i == 2) {
                    KeyguardViewMediator.this.handleHide();
                } else if (i == 3) {
                    KeyguardViewMediator.this.handleReset();
                } else if (i == 4) {
                    Trace.beginSection("KeyguardViewMediator#handleMessage VERIFY_UNLOCK");
                    KeyguardViewMediator.this.handleVerifyUnlock();
                    Trace.endSection();
                } else if (i == 5) {
                    KeyguardViewMediator.this.handleNotifyFinishedGoingToSleep();
                } else if (i != 603) {
                    switch (i) {
                        case 7:
                            Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE");
                            KeyguardViewMediator.this.handleKeyguardDone();
                            Trace.endSection();
                            return;
                        case 8:
                            Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_DRAWING");
                            KeyguardViewMediator.this.handleKeyguardDoneDrawing();
                            Trace.endSection();
                            return;
                        case 9:
                            Trace.beginSection("KeyguardViewMediator#handleMessage SET_OCCLUDED");
                            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                            boolean z2 = message.arg1 != 0;
                            if (message.arg2 == 0) {
                                z = false;
                            }
                            keyguardViewMediator.handleSetOccluded(z2, z);
                            Trace.endSection();
                            return;
                        case 10:
                            synchronized (KeyguardViewMediator.this) {
                                KeyguardViewMediator.this.doKeyguardLocked((Bundle) message.obj);
                            }
                            return;
                        case 11:
                            DismissMessage dismissMessage = (DismissMessage) message.obj;
                            KeyguardViewMediator.this.handleDismiss(dismissMessage.getCallback(), dismissMessage.getMessage());
                            return;
                        case 12:
                            Trace.beginSection("KeyguardViewMediator#handleMessage START_KEYGUARD_EXIT_ANIM");
                            ((NotificationShadeWindowController) KeyguardViewMediator.this.mNotificationShadeWindowControllerLazy.get()).batchApplyWindowLayoutParams(new KeyguardViewMediator$10$$ExternalSyntheticLambda0(this, (StartKeyguardExitAnimParams) message.obj));
                            Trace.endSection();
                            return;
                        case 13:
                            Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_PENDING_TIMEOUT");
                            Log.w(KeyguardViewMediator.TAG, "Timeout while waiting for activity drawn!");
                            Trace.endSection();
                            return;
                        case 14:
                            Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_STARTED_WAKING_UP");
                            KeyguardViewMediator.this.handleNotifyStartedWakingUp();
                            Trace.endSection();
                            return;
                        default:
                            switch (i) {
                                case 17:
                                    KeyguardViewMediator.this.handleNotifyStartedGoingToSleep();
                                    return;
                                case 18:
                                    KeyguardViewMediator.this.handleSystemReady();
                                    return;
                                case 19:
                                    Trace.beginSection("KeyguardViewMediator#handleMessage CANCEL_KEYGUARD_EXIT_ANIM");
                                    KeyguardViewMediator.this.handleCancelKeyguardExitAnimation();
                                    Trace.endSection();
                                    return;
                                default:
                                    ((KeyguardViewMediatorEx) NTDependencyEx.get(KeyguardViewMediatorEx.class)).handleMessage(message);
                                    return;
                            }
                    }
                } else {
                    KeyguardViewMediator.this.initFaceRecognition();
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$handleMessage$0$com-android-systemui-keyguard-KeyguardViewMediator$10 */
            public /* synthetic */ void mo33295x7a4b6ea8(StartKeyguardExitAnimParams startKeyguardExitAnimParams) {
                KeyguardViewMediator.this.handleStartKeyguardExitAnimation(startKeyguardExitAnimParams.startTime, startKeyguardExitAnimParams.fadeoutDuration, startKeyguardExitAnimParams.mApps, startKeyguardExitAnimParams.mWallpapers, startKeyguardExitAnimParams.mNonApps, startKeyguardExitAnimParams.mFinishedCallback);
                KeyguardViewMediator.this.mFalsingCollector.onSuccessfulUnlock();
            }
        };
        this.mKeyguardGoingAwayRunnable = new Runnable() {
            public void run() {
                Trace.beginSection("KeyguardViewMediator.mKeyGuardGoingAwayRunnable");
                Log.d(KeyguardViewMediator.TAG, "keyguardGoingAway");
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).keyguardGoingAway();
                int i = (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).shouldDisableWindowAnimationsForUnlock() || (KeyguardViewMediator.this.mWakeAndUnlocking && !KeyguardViewMediator.this.mWallpaperSupportsAmbientMode)) ? 2 : 0;
                if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).isGoingToNotificationShade() || (KeyguardViewMediator.this.mWakeAndUnlocking && KeyguardViewMediator.this.mWallpaperSupportsAmbientMode)) {
                    i |= 1;
                }
                if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).isUnlockWithWallpaper()) {
                    i |= 4;
                }
                if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).shouldSubtleWindowAnimationsForUnlock()) {
                    i |= 8;
                }
                if (KeyguardViewMediator.this.mWakeAndUnlocking && KeyguardUnlockAnimationController.Companion.isNexusLauncherUnderneath()) {
                    i |= 16;
                }
                KeyguardViewMediator.this.mUpdateMonitor.setKeyguardGoingAway(true);
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setKeyguardGoingAwayState(true);
                KeyguardViewMediator.this.mUiBgExecutor.execute(new KeyguardViewMediator$11$$ExternalSyntheticLambda0(i));
                Trace.endSection();
            }

            static /* synthetic */ void lambda$run$0(int i) {
                try {
                    ActivityTaskManager.getService().keyguardGoingAway(i);
                } catch (RemoteException e) {
                    Log.e(KeyguardViewMediator.TAG, "Error while calling WindowManager", e);
                }
            }
        };
        this.mHideAnimationFinishedRunnable = new KeyguardViewMediator$$ExternalSyntheticLambda11(this);
        this.mFalsingCollector = falsingCollector;
        this.mLockPatternUtils = lockPatternUtils;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardViewControllerLazy = lazy;
        this.mDismissCallbackRegistry = dismissCallbackRegistry;
        this.mNotificationShadeDepthController = lazy3;
        this.mUiBgExecutor = executor;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mPM = powerManager;
        this.mTrustManager = trustManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mKeyguardDisplayManager = keyguardDisplayManager;
        dumpManager.registerDumpable(getClass().getName(), this);
        this.mDeviceConfig = deviceConfigProxy2;
        this.mScreenOnCoordinator = screenOnCoordinator;
        this.mNotificationShadeWindowControllerLazy = lazy4;
        this.mShowHomeOverLockscreen = deviceConfigProxy2.getBoolean("systemui", "nav_bar_handle_show_over_lockscreen", true);
        DeviceConfigProxy deviceConfigProxy3 = this.mDeviceConfig;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        deviceConfigProxy3.addOnPropertiesChangedListener("systemui", new KeyguardViewMediator$$ExternalSyntheticLambda12(handler), r5);
        this.mInGestureNavigationMode = QuickStepContract.isGesturalMode(navigationModeController.addListener(new KeyguardViewMediator$$ExternalSyntheticLambda1(this)));
        this.mDozeParameters = dozeParameters;
        this.mStatusBarStateController = sysuiStatusBarStateController2;
        sysuiStatusBarStateController2.addCallback(this);
        this.mKeyguardStateController = keyguardStateController2;
        keyguardStateController2.addCallback(r62);
        this.mKeyguardUnlockAnimationControllerLazy = lazy2;
        this.mScreenOffAnimationController = screenOffAnimationController;
        this.mInteractionJankMonitor = interactionJankMonitor;
        this.mDreamOverlayStateController = dreamOverlayStateController;
        this.mActivityLaunchAnimator = lazy5;
        this.mPowerButtonY = (float) context.getResources().getDimensionPixelSize(C1894R.dimen.physical_power_button_center_screen_location_y);
        this.mWindowCornerRadius = ScreenDecorationsUtils.getWindowCornerRadius(context);
        ((KeyguardViewMediatorEx) NTDependencyEx.get(KeyguardViewMediatorEx.class)).init(this.mHandler, this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-keyguard-KeyguardViewMediator  reason: not valid java name */
    public /* synthetic */ void m2764lambda$new$0$comandroidsystemuikeyguardKeyguardViewMediator(int i) {
        this.mInGestureNavigationMode = QuickStepContract.isGesturalMode(i);
    }

    public void userActivity() {
        this.mPM.userActivity(SystemClock.uptimeMillis(), false);
    }

    /* access modifiers changed from: package-private */
    public boolean mustNotUnlockCurrentUser() {
        return UserManager.isSplitSystemUser() && KeyguardUpdateMonitor.getCurrentUser() == 0;
    }

    private void setupLocked() {
        PowerManager.WakeLock newWakeLock = this.mPM.newWakeLock(1, "show keyguard");
        this.mShowKeyguardWakeLock = newWakeLock;
        boolean z = false;
        newWakeLock.setReferenceCounted(false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(DELAYED_KEYGUARD_ACTION);
        intentFilter2.addAction(DELAYED_LOCK_PROFILE_ACTION);
        intentFilter2.setPriority(1000);
        this.mContext.registerReceiver(this.mDelayedLockBroadcastReceiver, intentFilter2, "com.android.systemui.permission.SELF", (Handler) null, 2);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService(NotificationCompat.CATEGORY_ALARM);
        KeyguardUpdateMonitor.setCurrentUser(ActivityManager.getCurrentUser());
        if (isKeyguardServiceEnabled()) {
            if (!shouldWaitForProvisioning() && !this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
                z = true;
            }
            setShowingLocked(z, true);
        } else {
            setShowingLocked(false, true);
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mDeviceInteractive = this.mPM.isInteractive();
        this.mLockSounds = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setContentType(4).build()).build();
        String string = Settings.Global.getString(contentResolver, "lock_sound");
        if (string != null) {
            this.mLockSoundId = this.mLockSounds.load(string, 1);
        }
        if (string == null || this.mLockSoundId == 0) {
            Log.w(TAG, "failed to load lock sound from " + string);
        }
        String string2 = Settings.Global.getString(contentResolver, "unlock_sound");
        if (string2 != null) {
            this.mUnlockSoundId = this.mLockSounds.load(string2, 1);
        }
        if (string2 == null || this.mUnlockSoundId == 0) {
            Log.w(TAG, "failed to load unlock sound from " + string2);
        }
        String string3 = Settings.Global.getString(contentResolver, "trusted_sound");
        if (string3 != null) {
            this.mTrustedSoundId = this.mLockSounds.load(string3, 1);
        }
        if (string3 == null || this.mTrustedSoundId == 0) {
            Log.w(TAG, "failed to load trusted sound from " + string3);
        }
        this.mLockSoundVolume = (float) Math.pow(10.0d, (double) (((float) this.mContext.getResources().getInteger(17694851)) / 20.0f));
        this.mHideAnimation = AnimationUtils.loadAnimation(this.mContext, 17432683);
        this.mWorkLockController = new WorkLockActivityController(this.mContext);
        if (this.mBootCompleted) {
            this.mHandler.sendEmptyMessage(603);
        }
    }

    public void start() {
        synchronized (this) {
            setupLocked();
        }
    }

    public void onSystemReady() {
        this.mHandler.obtainMessage(18).sendToTarget();
    }

    /* access modifiers changed from: private */
    public void handleSystemReady() {
        synchronized (this) {
            Log.d(TAG, "onSystemReady");
            this.mSystemReady = true;
            doKeyguardLocked((Bundle) null);
            this.mUpdateMonitor.registerCallback(this.mUpdateCallback);
            this.mDreamOverlayStateController.addCallback(this.mDreamOverlayStateCallback);
        }
        maybeSendUserPresentBroadcast();
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x004a A[Catch:{ RemoteException -> 0x0058 }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x006b A[Catch:{ RemoteException -> 0x0058 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x009d A[Catch:{ RemoteException -> 0x0058 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStartedGoingToSleep(int r9) {
        /*
            r8 = this;
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "onStartedGoingToSleep("
            r1.<init>((java.lang.String) r2)
            java.lang.StringBuilder r1 = r1.append((int) r9)
            java.lang.String r2 = ")"
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r0, r1)
            monitor-enter(r8)
            r0 = 0
            r8.mDeviceInteractive = r0     // Catch:{ all -> 0x00af }
            r8.mPowerGestureIntercepted = r0     // Catch:{ all -> 0x00af }
            r1 = 1
            r8.mGoingToSleep = r1     // Catch:{ all -> 0x00af }
            int r2 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()     // Catch:{ all -> 0x00af }
            com.android.internal.widget.LockPatternUtils r3 = r8.mLockPatternUtils     // Catch:{ all -> 0x00af }
            boolean r3 = r3.getPowerButtonInstantlyLocks(r2)     // Catch:{ all -> 0x00af }
            if (r3 != 0) goto L_0x003b
            com.android.internal.widget.LockPatternUtils r3 = r8.mLockPatternUtils     // Catch:{ all -> 0x00af }
            boolean r3 = r3.isSecure(r2)     // Catch:{ all -> 0x00af }
            if (r3 != 0) goto L_0x0039
            goto L_0x003b
        L_0x0039:
            r3 = r0
            goto L_0x003c
        L_0x003b:
            r3 = r1
        L_0x003c:
            int r4 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()     // Catch:{ all -> 0x00af }
            long r4 = r8.getLockTimeout(r4)     // Catch:{ all -> 0x00af }
            r8.mLockLater = r0     // Catch:{ all -> 0x00af }
            com.android.internal.policy.IKeyguardExitCallback r6 = r8.mExitSecureCallback     // Catch:{ all -> 0x00af }
            if (r6 == 0) goto L_0x006b
            java.lang.String r2 = "KeyguardViewMediator"
            java.lang.String r3 = "pending exit secure callback cancelled"
            android.util.Log.d(r2, r3)     // Catch:{ all -> 0x00af }
            com.android.internal.policy.IKeyguardExitCallback r2 = r8.mExitSecureCallback     // Catch:{ RemoteException -> 0x0058 }
            r2.onKeyguardExitResult(r0)     // Catch:{ RemoteException -> 0x0058 }
            goto L_0x0060
        L_0x0058:
            r2 = move-exception
            java.lang.String r3 = "KeyguardViewMediator"
            java.lang.String r4 = "Failed to call onKeyguardExitResult(false)"
            android.util.Slog.w(r3, r4, r2)     // Catch:{ all -> 0x00af }
        L_0x0060:
            r2 = 0
            r8.mExitSecureCallback = r2     // Catch:{ all -> 0x00af }
            boolean r2 = r8.mExternallyEnabled     // Catch:{ all -> 0x00af }
            if (r2 != 0) goto L_0x0099
            r8.hideLocked()     // Catch:{ all -> 0x00af }
            goto L_0x0099
        L_0x006b:
            boolean r6 = r8.mShowing     // Catch:{ all -> 0x00af }
            if (r6 == 0) goto L_0x007a
            com.android.systemui.statusbar.policy.KeyguardStateController r6 = r8.mKeyguardStateController     // Catch:{ all -> 0x00af }
            boolean r6 = r6.isKeyguardGoingAway()     // Catch:{ all -> 0x00af }
            if (r6 != 0) goto L_0x007a
            r8.mPendingReset = r1     // Catch:{ all -> 0x00af }
            goto L_0x0099
        L_0x007a:
            r6 = 3
            if (r9 != r6) goto L_0x0083
            r6 = 0
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 > 0) goto L_0x0088
        L_0x0083:
            r6 = 2
            if (r9 != r6) goto L_0x008e
            if (r3 != 0) goto L_0x008e
        L_0x0088:
            r8.doKeyguardLaterLocked(r4)     // Catch:{ all -> 0x00af }
            r8.mLockLater = r1     // Catch:{ all -> 0x00af }
            goto L_0x0099
        L_0x008e:
            com.android.internal.widget.LockPatternUtils r3 = r8.mLockPatternUtils     // Catch:{ all -> 0x00af }
            boolean r2 = r3.isLockScreenDisabled(r2)     // Catch:{ all -> 0x00af }
            if (r2 != 0) goto L_0x0099
            r8.setPendingLock(r1)     // Catch:{ all -> 0x00af }
        L_0x0099:
            boolean r2 = r8.mPendingLock     // Catch:{ all -> 0x00af }
            if (r2 == 0) goto L_0x00a0
            r8.playSounds(r1)     // Catch:{ all -> 0x00af }
        L_0x00a0:
            monitor-exit(r8)     // Catch:{ all -> 0x00af }
            com.android.keyguard.KeyguardUpdateMonitor r1 = r8.mUpdateMonitor
            r1.dispatchStartedGoingToSleep(r9)
            com.android.keyguard.KeyguardUpdateMonitor r9 = r8.mUpdateMonitor
            r9.dispatchKeyguardGoingAway(r0)
            r8.notifyStartedGoingToSleep()
            return
        L_0x00af:
            r9 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x00af }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.onStartedGoingToSleep(int):void");
    }

    public void onFinishedGoingToSleep(int i, boolean z) {
        Log.d(TAG, "onFinishedGoingToSleep(" + i + NavigationBarInflaterView.KEY_CODE_END);
        synchronized (this) {
            this.mDeviceInteractive = false;
            this.mGoingToSleep = false;
            this.mWakeAndUnlocking = false;
            this.mAnimatingScreenOff = this.mDozeParameters.shouldAnimateDozingChange();
            resetKeyguardDonePendingLocked();
            this.mHideAnimationRun = false;
            notifyFinishedGoingToSleep();
            if (z) {
                ((PowerManager) this.mContext.getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 5, "com.android.systemui:CAMERA_GESTURE_PREVENT_LOCK");
                setPendingLock(false);
                this.mPendingReset = false;
            }
            if (this.mPendingReset) {
                resetStateLocked();
                this.mPendingReset = false;
            }
            maybeHandlePendingLock();
            if (!this.mLockLater && !z) {
                doKeyguardForChildProfilesLocked();
            }
        }
        this.mUpdateMonitor.dispatchFinishedGoingToSleep(i);
    }

    public void maybeHandlePendingLock() {
        boolean z = true;
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_dismisses_keyguard", 0, -2) != 1) {
            z = false;
        }
        if (z && this.mUpdateMonitor.isFaceRecognitionEnable()) {
            boolean isFaceRecognitionSucceeded = this.mUpdateMonitor.isFaceRecognitionSucceeded();
        }
        if (!this.mPendingLock) {
            return;
        }
        if (this.mScreenOffAnimationController.isKeyguardShowDelayed()) {
            Log.d(TAG, "#maybeHandlePendingLock: not handling because the screen off animation's isKeyguardShowDelayed() returned true. This should be handled soon by #onStartedWakingUp, or by the end actions of the screen off animation.");
        } else if (this.mKeyguardStateController.isKeyguardGoingAway()) {
            Log.d(TAG, "#maybeHandlePendingLock: not handling because the keyguard is going away. This should be handled shortly by StatusBar#finishKeyguardFadingAway.");
        } else {
            Log.d(TAG, "#maybeHandlePendingLock: handling pending lock; locking keyguard.");
            doKeyguardLocked((Bundle) null);
            setPendingLock(false);
        }
    }

    private boolean isKeyguardServiceEnabled() {
        try {
            return this.mContext.getPackageManager().getServiceInfo(new ComponentName(this.mContext, KeyguardService.class), 0).isEnabled();
        } catch (PackageManager.NameNotFoundException unused) {
            return true;
        }
    }

    private long getLockTimeout(int i) {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        long j = (long) Settings.Secure.getInt(contentResolver, "lock_screen_lock_after_timeout", 5000);
        long maximumTimeToLock = this.mLockPatternUtils.getDevicePolicyManager().getMaximumTimeToLock((ComponentName) null, i);
        return maximumTimeToLock <= 0 ? j : Math.max(Math.min(maximumTimeToLock - Math.max((long) Settings.System.getInt(contentResolver, "screen_off_timeout", 30000), 0), j), 0);
    }

    private void doKeyguardLaterLocked() {
        long lockTimeout = getLockTimeout(KeyguardUpdateMonitor.getCurrentUser());
        if (lockTimeout == 0) {
            doKeyguardLocked((Bundle) null);
        } else {
            doKeyguardLaterLocked(lockTimeout);
        }
    }

    private void doKeyguardLaterLocked(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime() + j;
        Intent intent = new Intent(DELAYED_KEYGUARD_ACTION);
        intent.setPackage(this.mContext.getPackageName());
        intent.putExtra("seq", this.mDelayedShowingSequence);
        intent.addFlags(268435456);
        this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320));
        Log.d(TAG, "setting alarm to turn off keyguard, seq = " + this.mDelayedShowingSequence);
        doKeyguardLaterForChildProfilesLocked();
    }

    private void doKeyguardLaterForChildProfilesLocked() {
        for (int i : UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId())) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                long lockTimeout = getLockTimeout(i);
                if (lockTimeout == 0) {
                    doKeyguardForChildProfilesLocked();
                } else {
                    long elapsedRealtime = SystemClock.elapsedRealtime() + lockTimeout;
                    Intent intent = new Intent(DELAYED_LOCK_PROFILE_ACTION);
                    intent.setPackage(this.mContext.getPackageName());
                    intent.putExtra("seq", this.mDelayedProfileShowingSequence);
                    intent.putExtra("android.intent.extra.USER_ID", i);
                    intent.addFlags(268435456);
                    this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320));
                }
            }
        }
    }

    private void doKeyguardForChildProfilesLocked() {
        for (int i : UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId())) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                lockProfile(i);
            }
        }
    }

    private void cancelDoKeyguardLaterLocked() {
        this.mDelayedShowingSequence++;
    }

    private void cancelDoKeyguardForChildProfilesLocked() {
        this.mDelayedProfileShowingSequence++;
    }

    public void onStartedWakingUp(boolean z) {
        Trace.beginSection("KeyguardViewMediator#onStartedWakingUp");
        if (this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
            SystemUIEventUtils.collectUnLockResults(this.mContext, SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_SUCCESS, 0);
        }
        synchronized (this) {
            this.mDeviceInteractive = true;
            if (this.mPendingLock && !z) {
                doKeyguardLocked((Bundle) null);
            }
            this.mAnimatingScreenOff = false;
            cancelDoKeyguardLaterLocked();
            cancelDoKeyguardForChildProfilesLocked();
            Log.d(TAG, "onStartedWakingUp, seq = " + this.mDelayedShowingSequence);
            notifyStartedWakingUp();
        }
        this.mUpdateMonitor.dispatchStartedWakingUp();
        maybeSendUserPresentBroadcast();
        Trace.endSection();
    }

    public void onScreenTurnedOff() {
        this.mUpdateMonitor.dispatchScreenTurnedOff();
    }

    private void maybeSendUserPresentBroadcast() {
        if (this.mSystemReady && this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
            sendUserPresentBroadcast();
        } else if (this.mSystemReady && shouldWaitForProvisioning()) {
            this.mLockPatternUtils.userPresent(KeyguardUpdateMonitor.getCurrentUser());
        }
    }

    public void onDreamingStarted() {
        this.mUpdateMonitor.dispatchDreamingStarted();
        synchronized (this) {
            if (this.mDeviceInteractive && this.mLockPatternUtils.isSecure(KeyguardUpdateMonitor.getCurrentUser())) {
                doKeyguardLaterLocked();
            }
        }
    }

    public void onDreamingStopped() {
        this.mUpdateMonitor.dispatchDreamingStopped();
        synchronized (this) {
            if (this.mDeviceInteractive) {
                cancelDoKeyguardLaterLocked();
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:30|31|32|33|43|40|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a6, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x008e, code lost:
        continue;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:32:0x0096 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setKeyguardEnabled(boolean r4) {
        /*
            r3 = this;
            java.lang.String r0 = "setKeyguardEnabled("
            monitor-enter(r3)
            java.lang.String r1 = "KeyguardViewMediator"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a7 }
            r2.<init>((java.lang.String) r0)     // Catch:{ all -> 0x00a7 }
            java.lang.StringBuilder r0 = r2.append((boolean) r4)     // Catch:{ all -> 0x00a7 }
            java.lang.String r2 = ")"
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r2)     // Catch:{ all -> 0x00a7 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00a7 }
            android.util.Log.d(r1, r0)     // Catch:{ all -> 0x00a7 }
            r3.mExternallyEnabled = r4     // Catch:{ all -> 0x00a7 }
            r0 = 1
            if (r4 != 0) goto L_0x0043
            boolean r1 = r3.mShowing     // Catch:{ all -> 0x00a7 }
            if (r1 == 0) goto L_0x0043
            com.android.internal.policy.IKeyguardExitCallback r4 = r3.mExitSecureCallback     // Catch:{ all -> 0x00a7 }
            if (r4 == 0) goto L_0x0032
            java.lang.String r4 = "KeyguardViewMediator"
            java.lang.String r0 = "in process of verifyUnlock request, ignoring"
            android.util.Log.d(r4, r0)     // Catch:{ all -> 0x00a7 }
            monitor-exit(r3)     // Catch:{ all -> 0x00a7 }
            return
        L_0x0032:
            java.lang.String r4 = "KeyguardViewMediator"
            java.lang.String r1 = "remembering to reshow, hiding keyguard, disabling status bar expansion"
            android.util.Log.d(r4, r1)     // Catch:{ all -> 0x00a7 }
            r3.mNeedToReshowWhenReenabled = r0     // Catch:{ all -> 0x00a7 }
            r3.updateInputRestrictedLocked()     // Catch:{ all -> 0x00a7 }
            r3.hideLocked()     // Catch:{ all -> 0x00a7 }
            goto L_0x00a5
        L_0x0043:
            if (r4 == 0) goto L_0x00a5
            boolean r4 = r3.mNeedToReshowWhenReenabled     // Catch:{ all -> 0x00a7 }
            if (r4 == 0) goto L_0x00a5
            java.lang.String r4 = "KeyguardViewMediator"
            java.lang.String r1 = "previously hidden, reshowing, reenabling status bar expansion"
            android.util.Log.d(r4, r1)     // Catch:{ all -> 0x00a7 }
            r4 = 0
            r3.mNeedToReshowWhenReenabled = r4     // Catch:{ all -> 0x00a7 }
            r3.updateInputRestrictedLocked()     // Catch:{ all -> 0x00a7 }
            com.android.internal.policy.IKeyguardExitCallback r1 = r3.mExitSecureCallback     // Catch:{ all -> 0x00a7 }
            r2 = 0
            if (r1 == 0) goto L_0x0078
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r1 = "onKeyguardExitResult(false), resetting"
            android.util.Log.d(r0, r1)     // Catch:{ all -> 0x00a7 }
            com.android.internal.policy.IKeyguardExitCallback r0 = r3.mExitSecureCallback     // Catch:{ RemoteException -> 0x006a }
            r0.onKeyguardExitResult(r4)     // Catch:{ RemoteException -> 0x006a }
            goto L_0x0072
        L_0x006a:
            r4 = move-exception
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r1 = "Failed to call onKeyguardExitResult(false)"
            android.util.Slog.w(r0, r1, r4)     // Catch:{ all -> 0x00a7 }
        L_0x0072:
            r3.mExitSecureCallback = r2     // Catch:{ all -> 0x00a7 }
            r3.resetStateLocked()     // Catch:{ all -> 0x00a7 }
            goto L_0x00a5
        L_0x0078:
            r3.showLocked(r2)     // Catch:{ all -> 0x00a7 }
            r3.mWaitingUntilKeyguardVisible = r0     // Catch:{ all -> 0x00a7 }
            android.os.Handler r4 = r3.mHandler     // Catch:{ all -> 0x00a7 }
            r0 = 8
            r1 = 2000(0x7d0, double:9.88E-321)
            r4.sendEmptyMessageDelayed(r0, r1)     // Catch:{ all -> 0x00a7 }
            java.lang.String r4 = "KeyguardViewMediator"
            java.lang.String r0 = "waiting until mWaitingUntilKeyguardVisible is false"
            android.util.Log.d(r4, r0)     // Catch:{ all -> 0x00a7 }
        L_0x008e:
            boolean r4 = r3.mWaitingUntilKeyguardVisible     // Catch:{ all -> 0x00a7 }
            if (r4 == 0) goto L_0x009e
            r3.wait()     // Catch:{ InterruptedException -> 0x0096 }
            goto L_0x008e
        L_0x0096:
            java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x00a7 }
            r4.interrupt()     // Catch:{ all -> 0x00a7 }
            goto L_0x008e
        L_0x009e:
            java.lang.String r4 = "KeyguardViewMediator"
            java.lang.String r0 = "done waiting for mWaitingUntilKeyguardVisible"
            android.util.Log.d(r4, r0)     // Catch:{ all -> 0x00a7 }
        L_0x00a5:
            monitor-exit(r3)     // Catch:{ all -> 0x00a7 }
            return
        L_0x00a7:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x00a7 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.setKeyguardEnabled(boolean):void");
    }

    public void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) {
        Trace.beginSection("KeyguardViewMediator#verifyUnlock");
        synchronized (this) {
            Log.d(TAG, "verifyUnlock");
            if (shouldWaitForProvisioning()) {
                Log.d(TAG, "ignoring because device isn't provisioned");
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to call onKeyguardExitResult(false)", e);
                }
            } else if (this.mExternallyEnabled) {
                Log.w(TAG, "verifyUnlock called when not externally disabled");
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e2) {
                    Slog.w(TAG, "Failed to call onKeyguardExitResult(false)", e2);
                }
            } else if (this.mExitSecureCallback != null) {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e3) {
                    Slog.w(TAG, "Failed to call onKeyguardExitResult(false)", e3);
                }
            } else if (!isSecure()) {
                this.mExternallyEnabled = true;
                this.mNeedToReshowWhenReenabled = false;
                updateInputRestricted();
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(true);
                } catch (RemoteException e4) {
                    Slog.w(TAG, "Failed to call onKeyguardExitResult(false)", e4);
                }
            } else {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e5) {
                    Slog.w(TAG, "Failed to call onKeyguardExitResult(false)", e5);
                }
            }
        }
        Trace.endSection();
    }

    public boolean isShowingAndNotOccluded() {
        return this.mShowing && !this.mOccluded;
    }

    public void setOccluded(boolean z, boolean z2) {
        Log.d(TAG, "setOccluded(" + z + NavigationBarInflaterView.KEY_CODE_END);
        Trace.beginSection("KeyguardViewMediator#setOccluded");
        Log.d(TAG, "setOccluded " + z);
        this.mInteractionJankMonitor.cancel(23);
        this.mHandler.removeMessages(9);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(9, z ? 1 : 0, z2 ? 1 : 0));
        Trace.endSection();
    }

    public IRemoteAnimationRunner getOccludeAnimationRunner() {
        return this.mOccludeAnimationRunner;
    }

    public IRemoteAnimationRunner getUnoccludeAnimationRunner() {
        return this.mUnoccludeAnimationRunner;
    }

    public static int getUnlockTrackSimState(int i) {
        return mUnlockTrackSimStates.get(i);
    }

    public boolean isHiding() {
        return this.mHiding;
    }

    public boolean isAnimatingScreenOff() {
        return this.mAnimatingScreenOff;
    }

    /* access modifiers changed from: private */
    public void handleSetOccluded(boolean z, boolean z2) {
        boolean z3;
        Trace.beginSection("KeyguardViewMediator#handleSetOccluded");
        Log.d(TAG, "handleSetOccluded(" + z + NavigationBarInflaterView.KEY_CODE_END);
        synchronized (this) {
            if (this.mHiding && z) {
                startKeyguardExitAnimation(0, 0);
            }
            if (this.mOccluded != z) {
                this.mOccluded = z;
                this.mUpdateMonitor.setKeyguardOccluded(z);
                KeyguardViewController keyguardViewController = this.mKeyguardViewControllerLazy.get();
                if (!((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isSimPinSecure()) {
                    if (z2 && this.mDeviceInteractive) {
                        z3 = true;
                        keyguardViewController.setOccluded(z, z3);
                        adjustStatusBarLocked();
                    }
                }
                z3 = false;
                keyguardViewController.setOccluded(z, z3);
                adjustStatusBarLocked();
            }
        }
        Trace.endSection();
    }

    public void doKeyguardTimeout(Bundle bundle) {
        this.mHandler.removeMessages(10);
        this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(10, bundle));
    }

    public boolean isInputRestricted() {
        return this.mShowing || this.mNeedToReshowWhenReenabled;
    }

    private void updateInputRestricted() {
        synchronized (this) {
            updateInputRestrictedLocked();
        }
    }

    private void updateInputRestrictedLocked() {
        boolean isInputRestricted = isInputRestricted();
        if (this.mInputRestricted != isInputRestricted) {
            this.mInputRestricted = isInputRestricted;
            for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
                IKeyguardStateCallback iKeyguardStateCallback = this.mKeyguardStateCallbacks.get(size);
                try {
                    iKeyguardStateCallback.onInputRestrictedStateChanged(isInputRestricted);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to call onDeviceProvisioned", e);
                    if (e instanceof DeadObjectException) {
                        this.mKeyguardStateCallbacks.remove((Object) iKeyguardStateCallback);
                    }
                }
            }
        }
    }

    public void doKeyguardLocked(Bundle bundle) {
        if (KeyguardUpdateMonitor.CORE_APPS_ONLY) {
            Log.d(TAG, "doKeyguard: not showing because booting to cryptkeeper");
            return;
        }
        boolean z = true;
        if (!this.mExternallyEnabled) {
            Log.d(TAG, "doKeyguard: not showing because externally disabled");
            this.mNeedToReshowWhenReenabled = true;
        } else if (!this.mShowing || !this.mKeyguardViewControllerLazy.get().isShowing()) {
            if (!mustNotUnlockCurrentUser() || !this.mUpdateMonitor.isDeviceProvisioned()) {
                boolean z2 = this.mUpdateMonitor.isSimPinSecure() || ((SubscriptionManager.isValidSubscriptionId(this.mUpdateMonitor.getNextSubIdForState(1)) || SubscriptionManager.isValidSubscriptionId(this.mUpdateMonitor.getNextSubIdForState(7))) && (SystemProperties.getBoolean("keyguard.no_require_sim", false) ^ true));
                if (z2 || !shouldWaitForProvisioning()) {
                    if (bundle == null || !bundle.getBoolean(OPTION_FORCE_SHOW, false)) {
                        z = false;
                    }
                    if (this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser()) && !z2 && !z) {
                        Log.d(TAG, "doKeyguard: not showing because lockscreen is off");
                        return;
                    }
                } else {
                    Log.d(TAG, "doKeyguard: not showing because device isn't provisioned and the sim is not locked or missing");
                    return;
                }
            }
            Log.d(TAG, "doKeyguard: showing the lock screen");
            showLocked(bundle);
        } else {
            Log.d(TAG, "doKeyguard: not showing because it is already showing");
            resetStateLocked();
        }
    }

    /* access modifiers changed from: private */
    public void lockProfile(int i) {
        this.mTrustManager.setDeviceLockedForUser(i, true);
    }

    /* access modifiers changed from: private */
    public boolean shouldWaitForProvisioning() {
        return !this.mUpdateMonitor.isDeviceProvisioned() && !isSecure();
    }

    /* access modifiers changed from: private */
    public void handleDismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        if (this.mShowing) {
            if (iKeyguardDismissCallback != null) {
                this.mDismissCallbackRegistry.addCallback(iKeyguardDismissCallback);
            }
            this.mCustomMessage = charSequence;
            this.mKeyguardViewControllerLazy.get().dismissAndCollapse();
        } else if (iKeyguardDismissCallback != null) {
            new DismissCallbackWrapper(iKeyguardDismissCallback).notifyDismissError();
        }
    }

    public void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        this.mHandler.obtainMessage(11, new DismissMessage(iKeyguardDismissCallback, charSequence)).sendToTarget();
    }

    public void resetStateLocked() {
        Log.e(TAG, "resetStateLocked");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
        this.mUpdateMonitor.onFaceRecognitionSucceeded(false);
    }

    private void verifyUnlockLocked() {
        Log.d(TAG, "verifyUnlockLocked");
        this.mHandler.sendEmptyMessage(4);
    }

    private void notifyStartedGoingToSleep() {
        Log.d(TAG, "notifyStartedGoingToSleep");
        this.mHandler.sendEmptyMessage(17);
    }

    private void notifyFinishedGoingToSleep() {
        Log.d(TAG, "notifyFinishedGoingToSleep");
        this.mHandler.sendEmptyMessage(5);
    }

    private void notifyStartedWakingUp() {
        Log.d(TAG, "notifyStartedWakingUp");
        this.mHandler.sendEmptyMessage(14);
    }

    private void showLocked(Bundle bundle) {
        Trace.beginSection("KeyguardViewMediator#showLocked acquiring mShowKeyguardWakeLock");
        Log.d(TAG, "showLocked");
        this.mShowKeyguardWakeLock.acquire();
        this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(1, bundle));
        Trace.endSection();
    }

    private void hideLocked() {
        Trace.beginSection("KeyguardViewMediator#hideLocked");
        Log.d(TAG, "hideLocked");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2));
        Trace.endSection();
    }

    public void hideWithAnimation(IRemoteAnimationRunner iRemoteAnimationRunner) {
        if (this.mKeyguardDonePending) {
            this.mKeyguardExitAnimationRunner = iRemoteAnimationRunner;
            this.mViewMediatorCallback.readyForKeyguardDone();
        }
    }

    public void setBlursDisabledForAppLaunch(boolean z) {
        this.mNotificationShadeDepthController.get().setBlursDisabledForAppLaunch(z);
    }

    public boolean isSecure() {
        return isSecure(KeyguardUpdateMonitor.getCurrentUser());
    }

    public boolean isSecure(int i) {
        return this.mLockPatternUtils.isSecure(i) || this.mUpdateMonitor.isSimPinSecure();
    }

    public boolean isAnySimPinSecure() {
        for (int i = 0; i < this.mLastSimStates.size(); i++) {
            if (KeyguardUpdateMonitor.isSimPinSecure(this.mLastSimStates.get(this.mLastSimStates.keyAt(i)))) {
                return true;
            }
        }
        return false;
    }

    public void setSwitchingUser(boolean z) {
        this.mUpdateMonitor.setSwitchingUser(z);
    }

    public void setCurrentUser(int i) {
        KeyguardUpdateMonitor.setCurrentUser(i);
        synchronized (this) {
            notifyTrustedChangedLocked(this.mUpdateMonitor.getUserHasTrust(i));
        }
    }

    private void keyguardDone() {
        Trace.beginSection("KeyguardViewMediator#keyguardDone");
        Log.d(TAG, "keyguardDone()");
        userActivity();
        EventLog.writeEvent(70000, 2);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7));
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void tryKeyguardDone() {
        Log.d(TAG, "tryKeyguardDone: pending - " + this.mKeyguardDonePending + ", animRan - " + this.mHideAnimationRun + " animRunning - " + this.mHideAnimationRunning);
        if (!this.mKeyguardDonePending && this.mHideAnimationRun && !this.mHideAnimationRunning) {
            handleKeyguardDone();
        } else if (!this.mHideAnimationRun) {
            Log.d(TAG, "tryKeyguardDone: starting pre-hide animation");
            this.mHideAnimationRun = true;
            this.mHideAnimationRunning = true;
            this.mKeyguardViewControllerLazy.get().startPreHideAnimation(this.mHideAnimationFinishedRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void handleKeyguardDone() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDone");
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda9(this, currentUser));
        Log.d(TAG, "handleKeyguardDone");
        synchronized (this) {
            resetKeyguardDonePendingLocked();
        }
        if (this.mGoingToSleep) {
            this.mUpdateMonitor.clearBiometricRecognizedWhenKeyguardDone(currentUser);
            Log.i(TAG, "Device is going to sleep, aborting keyguardDone");
            return;
        }
        setPendingLock(false);
        IKeyguardExitCallback iKeyguardExitCallback = this.mExitSecureCallback;
        if (iKeyguardExitCallback != null) {
            try {
                iKeyguardExitCallback.onKeyguardExitResult(true);
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to call onKeyguardExitResult()", e);
            }
            this.mExitSecureCallback = null;
            this.mExternallyEnabled = true;
            this.mNeedToReshowWhenReenabled = false;
            updateInputRestricted();
        }
        handleHide();
        this.mUpdateMonitor.clearBiometricRecognizedWhenKeyguardDone(currentUser);
        Trace.endSection();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleKeyguardDone$1$com-android-systemui-keyguard-KeyguardViewMediator */
    public /* synthetic */ void mo33256x2f385c0f(int i) {
        if (this.mLockPatternUtils.isSecure(i)) {
            this.mLockPatternUtils.getDevicePolicyManager().reportKeyguardDismissed(i);
        }
    }

    /* access modifiers changed from: private */
    public void sendUserPresentBroadcast() {
        synchronized (this) {
            if (this.mBootCompleted) {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda6(this, (UserManager) this.mContext.getSystemService("user"), new UserHandle(currentUser), currentUser));
            } else {
                this.mBootSendUserPresent = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$sendUserPresentBroadcast$2$com-android-systemui-keyguard-KeyguardViewMediator */
    public /* synthetic */ void mo33264xe185ae85(UserManager userManager, UserHandle userHandle, int i) {
        for (int of : userManager.getProfileIdsWithDisabled(userHandle.getIdentifier())) {
            this.mContext.sendBroadcastAsUser(USER_PRESENT_INTENT, UserHandle.of(of));
        }
        this.mLockPatternUtils.userPresent(i);
    }

    /* access modifiers changed from: private */
    public void handleKeyguardDoneDrawing() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDoneDrawing");
        synchronized (this) {
            Log.d(TAG, "handleKeyguardDoneDrawing");
            if (this.mWaitingUntilKeyguardVisible) {
                Log.d(TAG, "handleKeyguardDoneDrawing: notifying mWaitingUntilKeyguardVisible");
                this.mWaitingUntilKeyguardVisible = false;
                notifyAll();
                this.mHandler.removeMessages(8);
            }
        }
        Trace.endSection();
    }

    private void playSounds(boolean z) {
        playSound(z ? this.mLockSoundId : this.mUnlockSoundId);
    }

    private void playSound(int i) {
        if (i != 0 && Settings.System.getInt(this.mContext.getContentResolver(), "lockscreen_sounds_enabled", 1) == 1) {
            this.mLockSounds.stop(this.mLockSoundStreamId);
            if (this.mAudioManager == null) {
                AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
                this.mAudioManager = audioManager;
                if (audioManager != null) {
                    this.mUiSoundsStreamType = audioManager.getUiSoundsStreamType();
                } else {
                    return;
                }
            }
            this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda3(this, i));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$playSound$3$com-android-systemui-keyguard-KeyguardViewMediator */
    public /* synthetic */ void mo33263x7dc6aa5c(int i) {
        if (!this.mAudioManager.isStreamMute(this.mUiSoundsStreamType)) {
            SoundPool soundPool = this.mLockSounds;
            float f = this.mLockSoundVolume;
            int play = soundPool.play(i, f, f, 1, 0, 1.0f);
            synchronized (this) {
                this.mLockSoundStreamId = play;
            }
        }
    }

    /* access modifiers changed from: private */
    public void playTrustedSound() {
        playSound(this.mTrustedSoundId);
    }

    private void updateActivityLockScreenState(boolean z, boolean z2) {
        this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda8(z, z2));
    }

    static /* synthetic */ void lambda$updateActivityLockScreenState$4(boolean z, boolean z2) {
        Log.d(TAG, "updateActivityLockScreenState(" + z + ", " + z2 + NavigationBarInflaterView.KEY_CODE_END);
        try {
            ActivityTaskManager.getService().setLockScreenShown(z, z2);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void handleShow(Bundle bundle) {
        Trace.beginSection("KeyguardViewMediator#handleShow");
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        if (this.mLockPatternUtils.isSecure(currentUser)) {
            this.mLockPatternUtils.getDevicePolicyManager().reportKeyguardSecured(currentUser);
        }
        synchronized (this) {
            if (!this.mSystemReady) {
                Log.d(TAG, "ignoring handleShow because system is not ready.");
                return;
            }
            Log.d(TAG, "handleShow");
            this.mHiding = false;
            this.mKeyguardExitAnimationRunner = null;
            this.mWakeAndUnlocking = false;
            setPendingLock(false);
            setShowingLocked(true);
            this.mKeyguardViewControllerLazy.get().show(bundle);
            resetKeyguardDonePendingLocked();
            this.mHideAnimationRun = false;
            adjustStatusBarLocked();
            userActivity();
            this.mUpdateMonitor.setKeyguardGoingAway(false);
            this.mKeyguardViewControllerLazy.get().setKeyguardGoingAwayState(false);
            this.mShowKeyguardWakeLock.release();
            this.mUpdateMonitor.onFaceRecognitionSucceeded(false);
            this.mKeyguardDisplayManager.show();
            this.mLockPatternUtils.scheduleNonStrongBiometricIdleTimeout(KeyguardUpdateMonitor.getCurrentUser());
            Trace.endSection();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$5$com-android-systemui-keyguard-KeyguardViewMediator  reason: not valid java name */
    public /* synthetic */ void m2765lambda$new$5$comandroidsystemuikeyguardKeyguardViewMediator() {
        Log.e(TAG, "mHideAnimationFinishedRunnable#run");
        this.mHideAnimationRunning = false;
        tryKeyguardDone();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005d, code lost:
        android.os.Trace.endSection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0060, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleHide() {
        /*
            r5 = this;
            java.lang.String r0 = "KeyguardViewMediator#handleHide"
            android.os.Trace.beginSection(r0)
            boolean r0 = r5.mAodShowing
            if (r0 != 0) goto L_0x000d
            boolean r0 = r5.mDreamOverlayShowing
            if (r0 == 0) goto L_0x0021
        L_0x000d:
            android.content.Context r0 = r5.mContext
            java.lang.Class<android.os.PowerManager> r1 = android.os.PowerManager.class
            java.lang.Object r0 = r0.getSystemService(r1)
            android.os.PowerManager r0 = (android.os.PowerManager) r0
            long r1 = android.os.SystemClock.uptimeMillis()
            r3 = 4
            java.lang.String r4 = "com.android.systemui:BOUNCER_DOZING"
            r0.wakeUp(r1, r3, r4)
        L_0x0021:
            monitor-enter(r5)
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r1 = "handleHide"
            android.util.Log.d(r0, r1)     // Catch:{ all -> 0x0061 }
            boolean r0 = r5.mustNotUnlockCurrentUser()     // Catch:{ all -> 0x0061 }
            if (r0 == 0) goto L_0x003b
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r1 = "Split system user, quit unlocking."
            android.util.Log.d(r0, r1)     // Catch:{ all -> 0x0061 }
            r0 = 0
            r5.mKeyguardExitAnimationRunner = r0     // Catch:{ all -> 0x0061 }
            monitor-exit(r5)     // Catch:{ all -> 0x0061 }
            return
        L_0x003b:
            r0 = 1
            r5.mHiding = r0     // Catch:{ all -> 0x0061 }
            boolean r0 = r5.mShowing     // Catch:{ all -> 0x0061 }
            if (r0 == 0) goto L_0x004c
            boolean r0 = r5.mOccluded     // Catch:{ all -> 0x0061 }
            if (r0 != 0) goto L_0x004c
            java.lang.Runnable r0 = r5.mKeyguardGoingAwayRunnable     // Catch:{ all -> 0x0061 }
            r0.run()     // Catch:{ all -> 0x0061 }
            goto L_0x005c
        L_0x004c:
            dagger.Lazy<com.android.systemui.statusbar.NotificationShadeWindowController> r0 = r5.mNotificationShadeWindowControllerLazy     // Catch:{ all -> 0x0061 }
            java.lang.Object r0 = r0.get()     // Catch:{ all -> 0x0061 }
            com.android.systemui.statusbar.NotificationShadeWindowController r0 = (com.android.systemui.statusbar.NotificationShadeWindowController) r0     // Catch:{ all -> 0x0061 }
            com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda5 r1 = new com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda5     // Catch:{ all -> 0x0061 }
            r1.<init>(r5)     // Catch:{ all -> 0x0061 }
            r0.batchApplyWindowLayoutParams(r1)     // Catch:{ all -> 0x0061 }
        L_0x005c:
            monitor-exit(r5)     // Catch:{ all -> 0x0061 }
            android.os.Trace.endSection()
            return
        L_0x0061:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0061 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.handleHide():void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleHide$6$com-android-systemui-keyguard-KeyguardViewMediator */
    public /* synthetic */ void mo33255xa6e6ae30() {
        handleStartKeyguardExitAnimation(SystemClock.uptimeMillis() + this.mHideAnimation.getStartOffset(), this.mHideAnimation.getDuration(), (RemoteAnimationTarget[]) null, (RemoteAnimationTarget[]) null, (RemoteAnimationTarget[]) null, (IRemoteAnimationFinishedCallback) null);
    }

    /* access modifiers changed from: private */
    public void handleStartKeyguardExitAnimation(long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        long j3 = j;
        long j4 = j2;
        RemoteAnimationTarget[] remoteAnimationTargetArr4 = remoteAnimationTargetArr;
        final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback2 = iRemoteAnimationFinishedCallback;
        Trace.beginSection("KeyguardViewMediator#handleStartKeyguardExitAnimation");
        Log.d(TAG, "handleStartKeyguardExitAnimation startTime=" + j + " fadeoutDuration=" + j4);
        synchronized (this) {
            if (this.mHiding || this.mSurfaceBehindRemoteAnimationRequested || this.mKeyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture()) {
                FaceRecognitionController faceRecognitionController = this.mFaceRecognitionController;
                if (faceRecognitionController != null) {
                    faceRecognitionController.postToStopCamera();
                    this.mFaceRecognitionController.resetAuthUnlockAttempt();
                }
                this.mHiding = false;
                IRemoteAnimationRunner iRemoteAnimationRunner = this.mKeyguardExitAnimationRunner;
                this.mKeyguardExitAnimationRunner = null;
                LatencyTracker.getInstance(this.mContext).onActionEnd(11);
                if (KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation && iRemoteAnimationRunner != null && iRemoteAnimationFinishedCallback2 != null) {
                    C216612 r7 = new IRemoteAnimationFinishedCallback() {
                        public void onAnimationFinished() throws RemoteException {
                            try {
                                iRemoteAnimationFinishedCallback2.onAnimationFinished();
                            } catch (RemoteException e) {
                                Slog.w(KeyguardViewMediator.TAG, "Failed to call onAnimationFinished", e);
                            }
                            KeyguardViewMediator.this.onKeyguardExitFinished();
                            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).hide(0, 0);
                            KeyguardViewMediator.this.mInteractionJankMonitor.end(29);
                        }

                        public IBinder asBinder() {
                            return iRemoteAnimationFinishedCallback2.asBinder();
                        }
                    };
                    try {
                        this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("RunRemoteAnimation"));
                        iRemoteAnimationRunner.onAnimationStart(7, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, r7);
                    } catch (RemoteException e) {
                        Slog.w(TAG, "Failed to call onAnimationStart", e);
                    }
                } else if (!KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation || this.mStatusBarStateController.leaveOpenOnKeyguardHide() || remoteAnimationTargetArr4 == null || remoteAnimationTargetArr4.length <= 0) {
                    this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("RemoteAnimationDisabled"));
                    this.mKeyguardViewControllerLazy.get().hide(j, j4);
                    this.mContext.getMainExecutor().execute(new KeyguardViewMediator$$ExternalSyntheticLambda10(this, iRemoteAnimationFinishedCallback2, remoteAnimationTargetArr4));
                    onKeyguardExitFinished();
                } else {
                    this.mSurfaceBehindRemoteAnimationFinishedCallback = iRemoteAnimationFinishedCallback2;
                    this.mSurfaceBehindRemoteAnimationRunning = true;
                    this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("DismissPanel"));
                    this.mKeyguardUnlockAnimationControllerLazy.get().notifyStartSurfaceBehindRemoteAnimation(remoteAnimationTargetArr4[0], j, this.mSurfaceBehindRemoteAnimationRequested);
                }
            } else {
                if (iRemoteAnimationFinishedCallback2 != null) {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException e2) {
                        Slog.w(TAG, "Failed to call onAnimationFinished", e2);
                    }
                }
                setShowingLocked(this.mShowing, true);
                return;
            }
        }
        Trace.endSection();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleStartKeyguardExitAnimation$8$com-android-systemui-keyguard-KeyguardViewMediator */
    public /* synthetic */ void mo33257x5fb004b2(final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (iRemoteAnimationFinishedCallback == null) {
            this.mKeyguardUnlockAnimationControllerLazy.get().notifyFinishedKeyguardExitAnimation(false);
            this.mInteractionJankMonitor.end(29);
            return;
        }
        SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView());
        RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr[0];
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(400);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new KeyguardViewMediator$$ExternalSyntheticLambda2(remoteAnimationTarget, syncRtSurfaceTransactionApplier));
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException unused) {
                    Slog.e(KeyguardViewMediator.TAG, "RemoteException");
                } catch (Throwable th) {
                    KeyguardViewMediator.this.mInteractionJankMonitor.end(29);
                    throw th;
                }
                KeyguardViewMediator.this.mInteractionJankMonitor.end(29);
            }

            public void onAnimationCancel(Animator animator) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException unused) {
                    Slog.e(KeyguardViewMediator.TAG, "RemoteException");
                } catch (Throwable th) {
                    KeyguardViewMediator.this.mInteractionJankMonitor.cancel(29);
                    throw th;
                }
                KeyguardViewMediator.this.mInteractionJankMonitor.cancel(29);
            }
        });
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public void onKeyguardExitFinished() {
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(this.mPhoneState)) {
            playSounds(false);
        }
        setShowingLocked(false);
        this.mWakeAndUnlocking = false;
        this.mDismissCallbackRegistry.notifyDismissSucceeded();
        resetKeyguardDonePendingLocked();
        this.mHideAnimationRun = false;
        adjustStatusBarLocked();
        sendUserPresentBroadcast();
    }

    private InteractionJankMonitor.Configuration.Builder createInteractionJankMonitorConf(String str) {
        return InteractionJankMonitor.Configuration.Builder.withView(29, this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView()).setTag(str);
    }

    public boolean isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe() {
        return this.mSurfaceBehindRemoteAnimationRunning || this.mKeyguardStateController.isFlingingToDismissKeyguard();
    }

    /* access modifiers changed from: private */
    public void handleCancelKeyguardExitAnimation() {
        showSurfaceBehindKeyguard();
        onKeyguardExitRemoteAnimationFinished(true);
    }

    public void onKeyguardExitRemoteAnimationFinished(boolean z) {
        if (this.mSurfaceBehindRemoteAnimationRunning || this.mSurfaceBehindRemoteAnimationRequested) {
            this.mKeyguardViewControllerLazy.get().blockPanelExpansionFromCurrentTouch();
            boolean z2 = this.mShowing;
            InteractionJankMonitor.getInstance().end(29);
            DejankUtils.postAfterTraversal(new KeyguardViewMediator$$ExternalSyntheticLambda7(this, z2, z));
            this.mKeyguardUnlockAnimationControllerLazy.get().notifyFinishedKeyguardExitAnimation(z);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onKeyguardExitRemoteAnimationFinished$9$com-android-systemui-keyguard-KeyguardViewMediator */
    public /* synthetic */ void mo33262xa210c104(boolean z, boolean z2) {
        onKeyguardExitFinished();
        if (this.mKeyguardStateController.isDismissingFromSwipe() || z) {
            this.mKeyguardUnlockAnimationControllerLazy.get().hideKeyguardViewAfterRemoteAnimation();
        }
        finishSurfaceBehindRemoteAnimation(z2);
        this.mSurfaceBehindRemoteAnimationRequested = false;
        this.mKeyguardStateController.notifyKeyguardGoingAway(false);
        this.mUpdateMonitor.dispatchKeyguardDismissAnimationFinished();
    }

    public void showSurfaceBehindKeyguard() {
        this.mSurfaceBehindRemoteAnimationRequested = true;
        try {
            ActivityTaskManager.getService().keyguardGoingAway(KeyguardUnlockAnimationController.Companion.isNexusLauncherUnderneath() ? 22 : 6);
            this.mKeyguardStateController.notifyKeyguardGoingAway(true);
        } catch (RemoteException e) {
            this.mSurfaceBehindRemoteAnimationRequested = false;
            e.printStackTrace();
        }
    }

    public void hideSurfaceBehindKeyguard() {
        this.mSurfaceBehindRemoteAnimationRequested = false;
        if (this.mShowing) {
            this.mKeyguardStateController.notifyKeyguardGoingAway(false);
            setShowingLocked(true, true);
        }
    }

    public boolean requestedShowSurfaceBehindKeyguard() {
        return this.mSurfaceBehindRemoteAnimationRequested;
    }

    public boolean isAnimatingBetweenKeyguardAndSurfaceBehind() {
        return this.mSurfaceBehindRemoteAnimationRunning;
    }

    /* access modifiers changed from: package-private */
    public void finishSurfaceBehindRemoteAnimation(boolean z) {
        if (this.mSurfaceBehindRemoteAnimationRunning) {
            this.mSurfaceBehindRemoteAnimationRunning = false;
            IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = this.mSurfaceBehindRemoteAnimationFinishedCallback;
            if (iRemoteAnimationFinishedCallback != null) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                    this.mSurfaceBehindRemoteAnimationFinishedCallback = null;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void adjustStatusBarLocked() {
        adjustStatusBarLocked(false, false);
    }

    /* access modifiers changed from: private */
    public void adjustStatusBarLocked(boolean z, boolean z2) {
        if (this.mStatusBarManager == null) {
            this.mStatusBarManager = (StatusBarManager) this.mContext.getSystemService("statusbar");
        }
        StatusBarManager statusBarManager = this.mStatusBarManager;
        if (statusBarManager == null) {
            Log.w(TAG, "Could not get status bar manager");
            return;
        }
        int i = 0;
        if (z2) {
            statusBarManager.disable(0);
        }
        if (z || isShowingAndNotOccluded()) {
            if (!this.mShowHomeOverLockscreen || !this.mInGestureNavigationMode) {
                i = 2097152;
            }
            i |= 16777216;
        }
        if (((KeyguardViewMediatorEx) NTDependencyEx.get(KeyguardViewMediatorEx.class)).isHighTemperature()) {
            i |= 18939904;
        }
        Log.d(TAG, "adjustStatusBarLocked: mShowing=" + this.mShowing + " mOccluded=" + this.mOccluded + " isSecure=" + isSecure() + " force=" + z + " --> flags=0x" + Integer.toHexString(i));
        this.mStatusBarManager.disable(i);
    }

    /* access modifiers changed from: private */
    public void handleReset() {
        synchronized (this) {
            Log.d(TAG, "handleReset");
            this.mKeyguardViewControllerLazy.get().reset(true);
        }
    }

    /* access modifiers changed from: private */
    public void handleVerifyUnlock() {
        Trace.beginSection("KeyguardViewMediator#handleVerifyUnlock");
        synchronized (this) {
            Log.d(TAG, "handleVerifyUnlock");
            setShowingLocked(true);
            this.mKeyguardViewControllerLazy.get().dismissAndCollapse();
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void handleNotifyStartedGoingToSleep() {
        synchronized (this) {
            Log.d(TAG, "handleNotifyStartedGoingToSleep");
            this.mKeyguardViewControllerLazy.get().onStartedGoingToSleep();
        }
    }

    /* access modifiers changed from: private */
    public void handleNotifyFinishedGoingToSleep() {
        synchronized (this) {
            Log.d(TAG, "handleNotifyFinishedGoingToSleep");
            this.mKeyguardViewControllerLazy.get().onFinishedGoingToSleep();
            if (this.mBootCompleted) {
                initFaceRecognition();
            }
            this.mFaceRecognitionController.setScreenOn(false);
            this.mUpdateMonitor.onFaceRecognitionSucceeded(false);
        }
    }

    /* access modifiers changed from: private */
    public void handleNotifyStartedWakingUp() {
        Trace.beginSection("KeyguardViewMediator#handleMotifyStartedWakingUp");
        synchronized (this) {
            Log.d(TAG, "handleNotifyWakingUp");
            this.mKeyguardViewControllerLazy.get().onStartedWakingUp();
            initFaceRecognition();
            this.mFaceRecognitionController.setScreenOn(true);
            this.mUpdateMonitor.onFaceRecognitionSucceeded(false);
        }
        ((KeyguardViewMediatorEx) NTDependencyEx.get(KeyguardViewMediatorEx.class)).handleNotifyStartedWakingUp();
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void resetKeyguardDonePendingLocked() {
        this.mKeyguardDonePending = false;
        this.mHandler.removeMessages(13);
    }

    public void onBootCompleted() {
        synchronized (this) {
            if (this.mContext.getResources().getBoolean(17891673)) {
                this.mUserSwitcherController.schedulePostBootGuestCreation();
            }
            this.mBootCompleted = true;
            adjustStatusBarLocked(false, true);
            if (this.mBootSendUserPresent) {
                sendUserPresentBroadcast();
            }
        }
    }

    public void onWakeAndUnlocking() {
        Trace.beginSection("KeyguardViewMediator#onWakeAndUnlocking");
        this.mWakeAndUnlocking = true;
        keyguardDone();
        Trace.endSection();
    }

    public KeyguardViewController registerCentralSurfaces(CentralSurfaces centralSurfaces, NotificationPanelViewController notificationPanelViewController, PanelExpansionStateManager panelExpansionStateManager, BiometricUnlockController biometricUnlockController, View view, KeyguardBypassController keyguardBypassController) {
        this.mCentralSurfaces = centralSurfaces;
        this.mKeyguardViewControllerLazy.get().registerCentralSurfaces(centralSurfaces, notificationPanelViewController, panelExpansionStateManager, biometricUnlockController, view, keyguardBypassController);
        return this.mKeyguardViewControllerLazy.get();
    }

    @Deprecated
    public void startKeyguardExitAnimation(long j, long j2) {
        startKeyguardExitAnimation(0, j, j2, (RemoteAnimationTarget[]) null, (RemoteAnimationTarget[]) null, (RemoteAnimationTarget[]) null, (IRemoteAnimationFinishedCallback) null);
    }

    public void startKeyguardExitAnimation(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        startKeyguardExitAnimation(i, 0, 0, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
    }

    private void startKeyguardExitAnimation(int i, long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        Trace.beginSection("KeyguardViewMediator#startKeyguardExitAnimation");
        this.mInteractionJankMonitor.cancel(23);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(12, new StartKeyguardExitAnimParams(i, j, j2, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback)));
        Trace.endSection();
    }

    public void cancelKeyguardExitAnimation() {
        Trace.beginSection("KeyguardViewMediator#cancelKeyguardExitAnimation");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(19));
        Trace.endSection();
    }

    public void onSystemKeyPressed(int i) {
        ((KeyguardViewMediatorEx) NTDependencyEx.get(KeyguardViewMediatorEx.class)).onSystemKeyPressed(i);
    }

    public ViewMediatorCallback getViewMediatorCallback() {
        return this.mViewMediatorCallback;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("  mSystemReady: ");
        printWriter.println(this.mSystemReady);
        printWriter.print("  mBootCompleted: ");
        printWriter.println(this.mBootCompleted);
        printWriter.print("  mBootSendUserPresent: ");
        printWriter.println(this.mBootSendUserPresent);
        printWriter.print("  mExternallyEnabled: ");
        printWriter.println(this.mExternallyEnabled);
        printWriter.print("  mShuttingDown: ");
        printWriter.println(this.mShuttingDown);
        printWriter.print("  mNeedToReshowWhenReenabled: ");
        printWriter.println(this.mNeedToReshowWhenReenabled);
        printWriter.print("  mShowing: ");
        printWriter.println(this.mShowing);
        printWriter.print("  mInputRestricted: ");
        printWriter.println(this.mInputRestricted);
        printWriter.print("  mOccluded: ");
        printWriter.println(this.mOccluded);
        printWriter.print("  mDelayedShowingSequence: ");
        printWriter.println(this.mDelayedShowingSequence);
        printWriter.print("  mExitSecureCallback: ");
        printWriter.println((Object) this.mExitSecureCallback);
        printWriter.print("  mDeviceInteractive: ");
        printWriter.println(this.mDeviceInteractive);
        printWriter.print("  mGoingToSleep: ");
        printWriter.println(this.mGoingToSleep);
        printWriter.print("  mHiding: ");
        printWriter.println(this.mHiding);
        printWriter.print("  mDozing: ");
        printWriter.println(this.mDozing);
        printWriter.print("  mAodShowing: ");
        printWriter.println(this.mAodShowing);
        printWriter.print("  mWaitingUntilKeyguardVisible: ");
        printWriter.println(this.mWaitingUntilKeyguardVisible);
        printWriter.print("  mKeyguardDonePending: ");
        printWriter.println(this.mKeyguardDonePending);
        printWriter.print("  mHideAnimationRun: ");
        printWriter.println(this.mHideAnimationRun);
        printWriter.print("  mPendingReset: ");
        printWriter.println(this.mPendingReset);
        printWriter.print("  mPendingLock: ");
        printWriter.println(this.mPendingLock);
        printWriter.print("  wakeAndUnlocking: ");
        printWriter.println(this.mWakeAndUnlocking);
    }

    public void setDozing(boolean z) {
        ((KeyguardViewMediatorEx) NTDependencyEx.get(KeyguardViewMediatorEx.class)).setDozing(z);
        if (z != this.mDozing) {
            this.mDozing = z;
            if (!z) {
                this.mAnimatingScreenOff = false;
            }
            if (this.mShowing || !this.mPendingLock || !this.mDozeParameters.canControlUnlockedScreenOff()) {
                setShowingLocked(this.mShowing);
            }
        }
    }

    public void onDozeAmountChanged(float f, float f2) {
        if (this.mAnimatingScreenOff && this.mDozing && f == 1.0f) {
            this.mAnimatingScreenOff = false;
            setShowingLocked(this.mShowing, true);
        }
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
    }

    private static class StartKeyguardExitAnimParams {
        long fadeoutDuration;
        RemoteAnimationTarget[] mApps;
        IRemoteAnimationFinishedCallback mFinishedCallback;
        RemoteAnimationTarget[] mNonApps;
        int mTransit;
        RemoteAnimationTarget[] mWallpapers;
        long startTime;

        private StartKeyguardExitAnimParams(int i, long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            this.mTransit = i;
            this.startTime = j;
            this.fadeoutDuration = j2;
            this.mApps = remoteAnimationTargetArr;
            this.mWallpapers = remoteAnimationTargetArr2;
            this.mNonApps = remoteAnimationTargetArr3;
            this.mFinishedCallback = iRemoteAnimationFinishedCallback;
        }
    }

    private void setShowingLocked(boolean z) {
        setShowingLocked(z, false);
    }

    private void setShowingLocked(boolean z, boolean z2) {
        boolean z3 = true;
        boolean z4 = this.mDozing && !this.mWakeAndUnlocking;
        boolean z5 = this.mShowing;
        boolean z6 = z != z5 || z2;
        if (z == z5 && z4 == this.mAodShowing && !z2) {
            z3 = false;
        }
        this.mShowing = z;
        this.mUpdateMonitor.setShowing(z);
        this.mAodShowing = z4;
        if (z6) {
            notifyDefaultDisplayCallbacks(z);
        }
        if (z3) {
            updateActivityLockScreenState(z, z4);
        }
    }

    private void notifyDefaultDisplayCallbacks(boolean z) {
        DejankUtils.whitelistIpcs((Runnable) new KeyguardViewMediator$$ExternalSyntheticLambda0(this, z));
        updateInputRestrictedLocked();
        this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda4(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$notifyDefaultDisplayCallbacks$10$com-android-systemui-keyguard-KeyguardViewMediator */
    public /* synthetic */ void mo33260x502711ad(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            IKeyguardStateCallback iKeyguardStateCallback = this.mKeyguardStateCallbacks.get(size);
            try {
                iKeyguardStateCallback.onShowingStateChanged(z, KeyguardUpdateMonitor.getCurrentUser());
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to call onShowingStateChanged", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove((Object) iKeyguardStateCallback);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$notifyDefaultDisplayCallbacks$11$com-android-systemui-keyguard-KeyguardViewMediator */
    public /* synthetic */ void mo33261x7dffac0c() {
        this.mTrustManager.reportKeyguardShowingChanged();
    }

    /* access modifiers changed from: private */
    public void notifyTrustedChangedLocked(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            try {
                this.mKeyguardStateCallbacks.get(size).onTrustedChanged(z);
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to call notifyTrustedChangedLocked", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(size);
                }
            }
        }
    }

    private void setPendingLock(boolean z) {
        this.mPendingLock = z;
        Trace.traceCounter(4096, "pendingLock", z ? 1 : 0);
    }

    public void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) {
        synchronized (this) {
            this.mKeyguardStateCallbacks.add(iKeyguardStateCallback);
            try {
                iKeyguardStateCallback.onSimSecureStateChanged(this.mUpdateMonitor.isSimPinSecure());
                iKeyguardStateCallback.onShowingStateChanged(this.mShowing, KeyguardUpdateMonitor.getCurrentUser());
                iKeyguardStateCallback.onInputRestrictedStateChanged(this.mInputRestricted);
                iKeyguardStateCallback.onTrustedChanged(this.mUpdateMonitor.getUserHasTrust(KeyguardUpdateMonitor.getCurrentUser()));
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to call to IKeyguardStateCallback", e);
            }
        }
    }

    private static class DismissMessage {
        private final IKeyguardDismissCallback mCallback;
        private final CharSequence mMessage;

        DismissMessage(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
            this.mCallback = iKeyguardDismissCallback;
            this.mMessage = charSequence;
        }

        public IKeyguardDismissCallback getCallback() {
            return this.mCallback;
        }

        public CharSequence getMessage() {
            return this.mMessage;
        }
    }

    private class ActivityLaunchRemoteAnimationRunner extends IRemoteAnimationRunner.Stub {
        private final ActivityLaunchAnimator.Controller mActivityLaunchController;
        private ActivityLaunchAnimator.Runner mRunner;

        ActivityLaunchRemoteAnimationRunner(ActivityLaunchAnimator.Controller controller) {
            this.mActivityLaunchController = controller;
        }

        public void onAnimationCancelled(boolean z) throws RemoteException {
            ActivityLaunchAnimator.Runner runner = this.mRunner;
            if (runner != null) {
                runner.onAnimationCancelled(z);
            }
        }

        public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
            ActivityLaunchAnimator.Runner createRunner = ((ActivityLaunchAnimator) KeyguardViewMediator.this.mActivityLaunchAnimator.get()).createRunner(this.mActivityLaunchController);
            this.mRunner = createRunner;
            createRunner.onAnimationStart(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
        }
    }

    private class OccludeActivityLaunchRemoteAnimationRunner extends ActivityLaunchRemoteAnimationRunner {
        OccludeActivityLaunchRemoteAnimationRunner(ActivityLaunchAnimator.Controller controller) {
            super(controller);
        }

        public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
            super.onAnimationStart(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
            Log.d(KeyguardViewMediator.TAG, "OccludeAnimator#onAnimationStart. Set occluded = true.");
            KeyguardViewMediator.this.setOccluded(true, false);
        }

        public void onAnimationCancelled(boolean z) throws RemoteException {
            super.onAnimationCancelled(z);
            Log.d(KeyguardViewMediator.TAG, "Occlude animation cancelled by WM. Setting occluded state to: " + z);
            KeyguardViewMediator.this.setOccluded(z, false);
        }
    }

    /* access modifiers changed from: private */
    public void initFaceRecognition() {
        if (this.mFaceRecognitionController == null) {
            Log.d(TAG, "initFaceRecognition", new Exception());
            FaceRecognitionController faceRecognitionController = (FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class);
            this.mFaceRecognitionController = faceRecognitionController;
            faceRecognitionController.initKeyguardFaceRecognition();
            this.mFaceRecognitionController.setStatusBarKeyguardViewManager(this.mKeyguardViewControllerLazy.get());
            this.mUpdateMonitor.setFaceRecognitionControllerRef(this.mFaceRecognitionController);
            this.mUpdateMonitor.updateFaceRecognitionState();
        }
    }
}
