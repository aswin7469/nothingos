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
import android.view.KeyEvent;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardDrawnCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.SystemUI;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.DeviceConfigProxy;
import com.nothingos.systemui.doze.AODController;
import com.nothingos.systemui.facerecognition.FaceRecognitionController;
import com.nothingos.utils.SystemUIEventUtils;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class KeyguardViewMediator extends SystemUI implements StatusBarStateController.StateListener {
    private PowerManager.WakeLock mAODFpAuthWakeLock;
    private AlarmManager mAlarmManager;
    private boolean mAnimatingScreenOff;
    private boolean mAodShowing;
    private AudioManager mAudioManager;
    private boolean mBootCompleted;
    private boolean mBootSendUserPresent;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private CharSequence mCustomMessage;
    private int mDelayedProfileShowingSequence;
    private int mDelayedShowingSequence;
    private DeviceConfigProxy mDeviceConfig;
    private boolean mDeviceInteractive;
    private final DismissCallbackRegistry mDismissCallbackRegistry;
    private DozeParameters mDozeParameters;
    private boolean mDozing;
    private IKeyguardDrawnCallback mDrawnCallback;
    private IKeyguardExitCallback mExitSecureCallback;
    private FaceRecognitionController mFaceRecognitionController;
    private final FalsingCollector mFalsingCollector;
    private boolean mGoingToSleep;
    private Animation mHideAnimation;
    private boolean mHiding;
    private boolean mInGestureNavigationMode;
    private boolean mInputRestricted;
    private final KeyguardDisplayManager mKeyguardDisplayManager;
    private IRemoteAnimationRunner mKeyguardExitAnimationRunner;
    private final KeyguardStateController mKeyguardStateController;
    private final Lazy<KeyguardUnlockAnimationController> mKeyguardUnlockAnimationControllerLazy;
    private final Lazy<KeyguardViewController> mKeyguardViewControllerLazy;
    private boolean mLockLater;
    private final LockPatternUtils mLockPatternUtils;
    private int mLockSoundId;
    private int mLockSoundStreamId;
    private float mLockSoundVolume;
    private SoundPool mLockSounds;
    private final Lazy<NotificationShadeDepthController> mNotificationShadeDepthController;
    private final DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener;
    private final PowerManager mPM;
    private boolean mPendingLock;
    private boolean mPendingReset;
    private boolean mPulsing;
    private boolean mShowHomeOverLockscreen;
    private PowerManager.WakeLock mShowKeyguardWakeLock;
    private boolean mShowing;
    private boolean mShuttingDown;
    private StatusBarManager mStatusBarManager;
    private final SysuiStatusBarStateController mStatusBarStateController;
    private IRemoteAnimationFinishedCallback mSurfaceBehindRemoteAnimationFinishedCallback;
    private boolean mSurfaceBehindRemoteAnimationRunning;
    private boolean mSystemReady;
    private final TrustManager mTrustManager;
    private int mTrustedSoundId;
    private final Executor mUiBgExecutor;
    private int mUiSoundsStreamType;
    private int mUnlockSoundId;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private final UserSwitcherController mUserSwitcherController;
    private boolean mWakeAndUnlocking;
    private boolean mWallpaperSupportsAmbientMode;
    private WorkLockActivityController mWorkLockController;
    private static SparseIntArray mUnlockTrackSimStates = new SparseIntArray();
    private static final Intent USER_PRESENT_INTENT = new Intent("android.intent.action.USER_PRESENT").addFlags(606076928);
    private boolean mExternallyEnabled = true;
    private boolean mNeedToReshowWhenReenabled = false;
    private boolean mOccluded = false;
    private final SparseIntArray mLastSimStates = new SparseIntArray();
    private final SparseBooleanArray mSimWasLocked = new SparseBooleanArray();
    private String mPhoneState = TelephonyManager.EXTRA_STATE_IDLE;
    private boolean mWaitingUntilKeyguardVisible = false;
    private boolean mKeyguardDonePending = false;
    private boolean mHideAnimationRun = false;
    private boolean mHideAnimationRunning = false;
    private final ArrayList<IKeyguardStateCallback> mKeyguardStateCallbacks = new ArrayList<>();
    private boolean mPowerGestureIntercepted = false;
    private boolean mSurfaceBehindRemoteAnimationRequested = false;
    KeyguardUpdateMonitorCallback mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.2
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserInfoChanged(int i) {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitching(int i) {
            Log.d("KeyguardViewMediator", String.format("onUserSwitching %d", Integer.valueOf(i)));
            synchronized (KeyguardViewMediator.this) {
                KeyguardViewMediator.this.resetKeyguardDonePendingLocked();
                if (!KeyguardViewMediator.this.mLockPatternUtils.isLockScreenDisabled(i)) {
                    KeyguardViewMediator.this.resetStateLocked();
                } else {
                    KeyguardViewMediator.this.dismiss(null, null);
                }
                KeyguardViewMediator.this.adjustStatusBarLocked();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitchComplete(int i) {
            UserInfo userInfo;
            Log.d("KeyguardViewMediator", String.format("onUserSwitchComplete %d", Integer.valueOf(i)));
            if (i == 0 || (userInfo = UserManager.get(((SystemUI) KeyguardViewMediator.this).mContext).getUserInfo(i)) == null || KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                return;
            }
            if (!userInfo.isGuest() && !userInfo.isDemo()) {
                return;
            }
            KeyguardViewMediator.this.dismiss(null, null);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onClockVisibilityChanged() {
            KeyguardViewMediator.this.adjustStatusBarLocked();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onDeviceProvisioned() {
            KeyguardViewMediator.this.sendUserPresentBroadcast();
            synchronized (KeyguardViewMediator.this) {
                if (KeyguardViewMediator.this.mustNotUnlockCurrentUser()) {
                    KeyguardViewMediator.this.doKeyguardLocked(null);
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:111:0x00ad  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0098  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00c7  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x0190 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onSimStateChanged(int i, int i2, int i3) {
            boolean z;
            int i4;
            Log.d("KeyguardViewMediator", "onSimStateChanged(subId=" + i + ", slotId=" + i2 + ",state=" + i3 + ")");
            int size = KeyguardViewMediator.this.mKeyguardStateCallbacks.size();
            boolean isSimPinSecure = KeyguardViewMediator.this.mUpdateMonitor.isSimPinSecure();
            for (int i5 = size - 1; i5 >= 0; i5--) {
                try {
                    ((IKeyguardStateCallback) KeyguardViewMediator.this.mKeyguardStateCallbacks.get(i5)).onSimSecureStateChanged(isSimPinSecure);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onSimSecureStateChanged", e);
                    if (e instanceof DeadObjectException) {
                        KeyguardViewMediator.this.mKeyguardStateCallbacks.remove(i5);
                    }
                }
            }
            synchronized (KeyguardViewMediator.this) {
                int i6 = KeyguardViewMediator.this.mLastSimStates.get(i2);
                if (i6 != 2 && i6 != 3) {
                    z = false;
                    KeyguardViewMediator.this.mLastSimStates.append(i2, i3);
                    i4 = KeyguardViewMediator.mUnlockTrackSimStates.get(i2, -1);
                    if (i3 != 5) {
                        if (i4 != 10) {
                            KeyguardViewMediator.mUnlockTrackSimStates.put(i2, i3);
                        } else {
                            Log.e("KeyguardViewMediator", "skip the redundant SIM_STATE_READY state");
                            return;
                        }
                    } else if (i3 != 2) {
                        KeyguardViewMediator.mUnlockTrackSimStates.put(i2, i3);
                    }
                    if (i4 != 5 && i3 == 2) {
                        Log.e("KeyguardViewMediator", "skip the unnecessary SIM_STATE_PIN_REQUIRED state");
                        return;
                    }
                    if (i3 != 1) {
                        if (i3 == 2 || i3 == 3) {
                            synchronized (KeyguardViewMediator.this) {
                                KeyguardViewMediator.this.mSimWasLocked.append(i2, true);
                                if (KeyguardViewMediator.this.mShowing) {
                                    KeyguardViewMediator.this.resetStateLocked();
                                } else {
                                    Log.d("KeyguardViewMediator", "INTENT_VALUE_ICC_LOCKED and keygaurd isn't showing; need to show keyguard so user can enter sim pin");
                                    KeyguardViewMediator.this.doKeyguardLocked(null);
                                }
                            }
                            return;
                        } else if (i3 == 5) {
                            synchronized (KeyguardViewMediator.this) {
                                Log.d("KeyguardViewMediator", "READY, reset state? " + KeyguardViewMediator.this.mShowing);
                                if (KeyguardViewMediator.this.mShowing && KeyguardViewMediator.this.mSimWasLocked.get(i2, false)) {
                                    Log.d("KeyguardViewMediator", "SIM moved to READY when the previously was locked. Reset the state.");
                                    KeyguardViewMediator.this.mSimWasLocked.append(i2, false);
                                    KeyguardViewMediator.this.resetStateLocked();
                                }
                            }
                            return;
                        } else if (i3 != 6) {
                            if (i3 == 7) {
                                synchronized (KeyguardViewMediator.this) {
                                    if (!KeyguardViewMediator.this.mShowing) {
                                        Log.d("KeyguardViewMediator", "PERM_DISABLED and keygaurd isn't showing.");
                                        KeyguardViewMediator.this.doKeyguardLocked(null);
                                    } else {
                                        Log.d("KeyguardViewMediator", "PERM_DISABLED, resetStateLocked toshow permanently disabled message in lockscreen.");
                                        KeyguardViewMediator.this.resetStateLocked();
                                    }
                                }
                                return;
                            }
                            Log.v("KeyguardViewMediator", "Unspecific state: " + i3);
                            return;
                        }
                    }
                    synchronized (KeyguardViewMediator.this) {
                        if (KeyguardViewMediator.this.shouldWaitForProvisioning()) {
                            if (KeyguardViewMediator.this.mShowing) {
                                KeyguardViewMediator.this.resetStateLocked();
                            } else {
                                Log.d("KeyguardViewMediator", "ICC_ABSENT isn't showing, we need to show the keyguard since the device isn't provisioned yet.");
                                KeyguardViewMediator.this.doKeyguardLocked(null);
                            }
                        }
                        if (i3 == 1) {
                            if (z) {
                                Log.d("KeyguardViewMediator", "SIM moved to ABSENT when the previous state was locked. Reset the state.");
                                KeyguardViewMediator.this.resetStateLocked();
                            }
                            KeyguardViewMediator.this.mSimWasLocked.append(i2, false);
                        }
                    }
                    return;
                }
                z = true;
                KeyguardViewMediator.this.mLastSimStates.append(i2, i3);
                i4 = KeyguardViewMediator.mUnlockTrackSimStates.get(i2, -1);
                if (i3 != 5) {
                }
                if (i4 != 5) {
                }
                if (i3 != 1) {
                }
                synchronized (KeyguardViewMediator.this) {
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
            int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(currentUser)) {
                KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportFailedBiometricAttempt(currentUser);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportSuccessfulBiometricAttempt(i);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTrustChanged(int i) {
            if (i == KeyguardUpdateMonitor.getCurrentUser()) {
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    keyguardViewMediator.notifyTrustedChangedLocked(keyguardViewMediator.mUpdateMonitor.getUserHasTrust(i));
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onHasLockscreenWallpaperChanged(boolean z) {
            synchronized (KeyguardViewMediator.this) {
                KeyguardViewMediator.this.notifyHasLockscreenWallpaperChanged(z);
            }
        }
    };
    ViewMediatorCallback mViewMediatorCallback = new ViewMediatorCallback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.3
        @Override // com.android.keyguard.ViewMediatorCallback
        public void userActivity() {
            KeyguardViewMediator.this.userActivity();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void keyguardDone(boolean z, int i) {
            if (i != ActivityManager.getCurrentUser()) {
                return;
            }
            Log.d("KeyguardViewMediator", "keyguardDone");
            KeyguardViewMediator.this.tryKeyguardDone();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void keyguardDoneDrawing() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDoneDrawing");
            KeyguardViewMediator.this.mHandler.sendEmptyMessage(8);
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void setNeedsInput(boolean z) {
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setNeedsInput(z);
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void keyguardDonePending(boolean z, int i) {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDonePending");
            Log.d("KeyguardViewMediator", "keyguardDonePending");
            if (i == ActivityManager.getCurrentUser()) {
                KeyguardViewMediator.this.mKeyguardDonePending = true;
                KeyguardViewMediator.this.mHideAnimationRun = true;
                KeyguardViewMediator.this.mHideAnimationRunning = true;
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).startPreHideAnimation(KeyguardViewMediator.this.mHideAnimationFinishedRunnable);
                KeyguardViewMediator.this.mHandler.sendEmptyMessageDelayed(13, 3000L);
                Trace.endSection();
                return;
            }
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void keyguardGone() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardGone");
            Log.d("KeyguardViewMediator", "keyguardGone");
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setKeyguardGoingAwayState(false);
            KeyguardViewMediator.this.mKeyguardDisplayManager.hide();
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void readyForKeyguardDone() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#readyForKeyguardDone");
            if (KeyguardViewMediator.this.mKeyguardDonePending) {
                KeyguardViewMediator.this.mKeyguardDonePending = false;
                KeyguardViewMediator.this.tryKeyguardDone();
            }
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void resetKeyguard() {
            KeyguardViewMediator.this.resetStateLocked();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void onCancelClicked() {
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).onCancelClicked();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void onBouncerVisiblityChanged(boolean z) {
            synchronized (KeyguardViewMediator.this) {
                KeyguardViewMediator.this.adjustStatusBarLocked(z, false);
            }
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void playTrustedSound() {
            KeyguardViewMediator.this.playTrustedSound();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public boolean isScreenOn() {
            return KeyguardViewMediator.this.mDeviceInteractive;
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public int getBouncerPromptReason() {
            int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            boolean isTrustUsuallyManaged = KeyguardViewMediator.this.mUpdateMonitor.isTrustUsuallyManaged(currentUser);
            boolean z = isTrustUsuallyManaged || KeyguardViewMediator.this.mUpdateMonitor.isUnlockingWithBiometricsPossible(currentUser);
            KeyguardUpdateMonitor.StrongAuthTracker strongAuthTracker = KeyguardViewMediator.this.mUpdateMonitor.getStrongAuthTracker();
            int strongAuthForUser = strongAuthTracker.getStrongAuthForUser(currentUser);
            if (!z || strongAuthTracker.hasUserAuthenticatedSinceBoot()) {
                if (z && (strongAuthForUser & 16) != 0) {
                    return 2;
                }
                if (z && (strongAuthForUser & 2) != 0) {
                    return 3;
                }
                if (isTrustUsuallyManaged && (strongAuthForUser & 4) != 0) {
                    return 4;
                }
                if (z && (strongAuthForUser & 8) != 0) {
                    return 5;
                }
                if (z && (strongAuthForUser & 64) != 0) {
                    return 6;
                }
                return (!z || (strongAuthForUser & 128) == 0) ? 0 : 7;
            }
            return 1;
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public CharSequence consumeCustomMessage() {
            CharSequence charSequence = KeyguardViewMediator.this.mCustomMessage;
            KeyguardViewMediator.this.mCustomMessage = null;
            return charSequence;
        }
    };
    private final BroadcastReceiver mDelayedLockBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("seq", 0);
                Log.d("KeyguardViewMediator", "received DELAYED_KEYGUARD_ACTION with seq = " + intExtra + ", mDelayedShowingSequence = " + KeyguardViewMediator.this.mDelayedShowingSequence);
                synchronized (KeyguardViewMediator.this) {
                    if (KeyguardViewMediator.this.mDelayedShowingSequence == intExtra) {
                        KeyguardViewMediator.this.doKeyguardLocked(null);
                    }
                }
            } else if (!"com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK".equals(intent.getAction())) {
            } else {
                int intExtra2 = intent.getIntExtra("seq", 0);
                int intExtra3 = intent.getIntExtra("android.intent.extra.USER_ID", 0);
                if (intExtra3 == 0) {
                    return;
                }
                synchronized (KeyguardViewMediator.this) {
                    if (KeyguardViewMediator.this.mDelayedProfileShowingSequence == intExtra2) {
                        KeyguardViewMediator.this.lockProfile(intExtra3);
                    }
                }
            }
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator.this.mShuttingDown = true;
                }
            }
        }
    };
    private Handler mHandler = new Handler(Looper.myLooper(), null, true) { // from class: com.android.systemui.keyguard.KeyguardViewMediator.6
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            switch (i) {
                case 1:
                    KeyguardViewMediator.this.handleShow((Bundle) message.obj);
                    return;
                case 2:
                    KeyguardViewMediator.this.handleHide();
                    return;
                case 3:
                    KeyguardViewMediator.this.handleReset();
                    return;
                case 4:
                    Trace.beginSection("KeyguardViewMediator#handleMessage VERIFY_UNLOCK");
                    KeyguardViewMediator.this.handleVerifyUnlock();
                    Trace.endSection();
                    return;
                case 5:
                    KeyguardViewMediator.this.handleNotifyFinishedGoingToSleep();
                    return;
                case 6:
                    Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_SCREEN_TURNING_ON");
                    KeyguardViewMediator.this.handleNotifyScreenTurningOn((IKeyguardDrawnCallback) message.obj);
                    Trace.endSection();
                    return;
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
                    boolean z = true;
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
                        KeyguardViewMediator keyguardViewMediator2 = KeyguardViewMediator.this;
                        keyguardViewMediator2.notifyDefaultDisplayCallbacks(keyguardViewMediator2.mShowing);
                    }
                    return;
                case 11:
                    DismissMessage dismissMessage = (DismissMessage) message.obj;
                    KeyguardViewMediator.this.handleDismiss(dismissMessage.getCallback(), dismissMessage.getMessage());
                    return;
                case 12:
                    Trace.beginSection("KeyguardViewMediator#handleMessage START_KEYGUARD_EXIT_ANIM");
                    StartKeyguardExitAnimParams startKeyguardExitAnimParams = (StartKeyguardExitAnimParams) message.obj;
                    KeyguardViewMediator.this.handleStartKeyguardExitAnimation(startKeyguardExitAnimParams.startTime, startKeyguardExitAnimParams.fadeoutDuration, startKeyguardExitAnimParams.mApps, startKeyguardExitAnimParams.mWallpapers, startKeyguardExitAnimParams.mNonApps, startKeyguardExitAnimParams.mFinishedCallback);
                    KeyguardViewMediator.this.mFalsingCollector.onSuccessfulUnlock();
                    Trace.endSection();
                    return;
                case 13:
                    Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_PENDING_TIMEOUT");
                    Log.w("KeyguardViewMediator", "Timeout while waiting for activity drawn!");
                    Trace.endSection();
                    return;
                case 14:
                    Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_STARTED_WAKING_UP");
                    KeyguardViewMediator.this.handleNotifyStartedWakingUp();
                    Trace.endSection();
                    return;
                case 15:
                    Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_SCREEN_TURNED_ON");
                    KeyguardViewMediator.this.handleNotifyScreenTurnedOn();
                    Trace.endSection();
                    return;
                case 16:
                    KeyguardViewMediator.this.handleNotifyScreenTurnedOff();
                    return;
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
                    switch (i) {
                        case 600:
                            KeyguardViewMediator.this.handleTapGestureDown();
                            return;
                        case 601:
                            KeyguardViewMediator.this.handleTapGestureUp();
                            return;
                        case 602:
                            KeyguardViewMediator.this.handleTapWake();
                            return;
                        case 603:
                            KeyguardViewMediator.this.initFaceRecognition();
                            return;
                        default:
                            return;
                    }
            }
        }
    };
    private final Runnable mKeyguardGoingAwayRunnable = new AnonymousClass7();
    private final Runnable mHideAnimationFinishedRunnable = new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardViewMediator.this.lambda$new$5();
        }
    };
    private boolean mIsAODAuth = false;

    public void onShortPowerPressedGoHome() {
    }

    public KeyguardViewMediator(Context context, FalsingCollector falsingCollector, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, Lazy<KeyguardViewController> lazy, DismissCallbackRegistry dismissCallbackRegistry, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, Executor executor, PowerManager powerManager, TrustManager trustManager, UserSwitcherController userSwitcherController, DeviceConfigProxy deviceConfigProxy, NavigationModeController navigationModeController, KeyguardDisplayManager keyguardDisplayManager, DozeParameters dozeParameters, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, Lazy<KeyguardUnlockAnimationController> lazy2, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Lazy<NotificationShadeDepthController> lazy3) {
        super(context);
        DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.1
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("nav_bar_handle_show_over_lockscreen")) {
                    KeyguardViewMediator.this.mShowHomeOverLockscreen = properties.getBoolean("nav_bar_handle_show_over_lockscreen", true);
                }
            }
        };
        this.mOnPropertiesChangedListener = onPropertiesChangedListener;
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
        this.mDeviceConfig = deviceConfigProxy;
        this.mShowHomeOverLockscreen = deviceConfigProxy.getBoolean("systemui", "nav_bar_handle_show_over_lockscreen", true);
        DeviceConfigProxy deviceConfigProxy2 = this.mDeviceConfig;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        deviceConfigProxy2.addOnPropertiesChangedListener("systemui", new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), onPropertiesChangedListener);
        this.mInGestureNavigationMode = QuickStepContract.isGesturalMode(navigationModeController.addListener(new NavigationModeController.ModeChangedListener() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda1
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i) {
                KeyguardViewMediator.this.lambda$new$0(i);
            }
        }));
        this.mDozeParameters = dozeParameters;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        sysuiStatusBarStateController.addCallback(this);
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardUnlockAnimationControllerLazy = lazy2;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        this.mInGestureNavigationMode = QuickStepContract.isGesturalMode(i);
    }

    public void userActivity() {
        this.mPM.userActivity(SystemClock.uptimeMillis(), false);
    }

    boolean mustNotUnlockCurrentUser() {
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
        intentFilter2.addAction("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD");
        intentFilter2.addAction("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK");
        this.mContext.registerReceiver(this.mDelayedLockBroadcastReceiver, intentFilter2, "com.android.systemui.permission.SELF", null);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
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
            Log.w("KeyguardViewMediator", "failed to load lock sound from " + string);
        }
        String string2 = Settings.Global.getString(contentResolver, "unlock_sound");
        if (string2 != null) {
            this.mUnlockSoundId = this.mLockSounds.load(string2, 1);
        }
        if (string2 == null || this.mUnlockSoundId == 0) {
            Log.w("KeyguardViewMediator", "failed to load unlock sound from " + string2);
        }
        String string3 = Settings.Global.getString(contentResolver, "trusted_sound");
        if (string3 != null) {
            this.mTrustedSoundId = this.mLockSounds.load(string3, 1);
        }
        if (string3 == null || this.mTrustedSoundId == 0) {
            Log.w("KeyguardViewMediator", "failed to load trusted sound from " + string3);
        }
        this.mLockSoundVolume = (float) Math.pow(10.0d, this.mContext.getResources().getInteger(17694841) / 20.0f);
        this.mHideAnimation = AnimationUtils.loadAnimation(this.mContext, 17432683);
        this.mWorkLockController = new WorkLockActivityController(this.mContext);
        if (this.mBootCompleted) {
            this.mHandler.sendEmptyMessage(603);
        }
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        synchronized (this) {
            setupLocked();
        }
    }

    public void onSystemReady() {
        this.mHandler.obtainMessage(18).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSystemReady() {
        synchronized (this) {
            Log.d("KeyguardViewMediator", "onSystemReady");
            this.mSystemReady = true;
            doKeyguardLocked(null);
            this.mUpdateMonitor.registerCallback(this.mUpdateCallback);
            if (this.mBootCompleted) {
                initFaceRecognition();
            }
            ((NavigationModeController) Dependency.get(NavigationModeController.class)).reloadNavOverlay();
        }
        maybeSendUserPresentBroadcast();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x004b A[Catch: all -> 0x00a7, TRY_LEAVE, TryCatch #0 {, blocks: (B:5:0x001d, B:7:0x0030, B:11:0x003c, B:13:0x004b, B:15:0x0052, B:16:0x0060, B:18:0x0067, B:19:0x008f, B:22:0x0095, B:23:0x0098, B:30:0x0059, B:31:0x006b, B:33:0x006f, B:38:0x007f, B:41:0x0085, B:43:0x008d), top: B:4:0x001d, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x006b A[Catch: all -> 0x00a7, TryCatch #0 {, blocks: (B:5:0x001d, B:7:0x0030, B:11:0x003c, B:13:0x004b, B:15:0x0052, B:16:0x0060, B:18:0x0067, B:19:0x008f, B:22:0x0095, B:23:0x0098, B:30:0x0059, B:31:0x006b, B:33:0x006f, B:38:0x007f, B:41:0x0085, B:43:0x008d), top: B:4:0x001d, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onStartedGoingToSleep(int i) {
        boolean z;
        Log.d("KeyguardViewMediator", "onStartedGoingToSleep(" + i + ")");
        synchronized (this) {
            this.mDeviceInteractive = false;
            this.mPowerGestureIntercepted = false;
            this.mGoingToSleep = true;
            int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (!this.mLockPatternUtils.getPowerButtonInstantlyLocks(currentUser) && this.mLockPatternUtils.isSecure(currentUser)) {
                z = false;
                long lockTimeout = getLockTimeout(KeyguardUpdateMonitor.getCurrentUser());
                this.mLockLater = false;
                if (this.mExitSecureCallback == null) {
                    Log.d("KeyguardViewMediator", "pending exit secure callback cancelled");
                    try {
                        this.mExitSecureCallback.onKeyguardExitResult(false);
                    } catch (RemoteException e) {
                        Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e);
                    }
                    this.mExitSecureCallback = null;
                    if (!this.mExternallyEnabled) {
                        hideLocked();
                    }
                } else if (this.mShowing) {
                    this.mPendingReset = true;
                } else if ((i == 3 && lockTimeout > 0) || (i == 2 && !z)) {
                    doKeyguardLaterLocked(lockTimeout);
                    this.mLockLater = true;
                } else if (!this.mLockPatternUtils.isLockScreenDisabled(currentUser)) {
                    this.mPendingLock = true;
                }
                if (this.mPendingLock && i == 2) {
                    playSounds(true);
                }
            }
            z = true;
            long lockTimeout2 = getLockTimeout(KeyguardUpdateMonitor.getCurrentUser());
            this.mLockLater = false;
            if (this.mExitSecureCallback == null) {
            }
            if (this.mPendingLock) {
                playSounds(true);
            }
        }
        this.mUpdateMonitor.dispatchStartedGoingToSleep(i);
        this.mUpdateMonitor.dispatchKeyguardGoingAway(false);
        notifyStartedGoingToSleep();
    }

    public void onFinishedGoingToSleep(int i, boolean z) {
        Log.d("KeyguardViewMediator", "onFinishedGoingToSleep(" + i + ")");
        synchronized (this) {
            this.mDeviceInteractive = false;
            this.mGoingToSleep = false;
            this.mWakeAndUnlocking = false;
            this.mAnimatingScreenOff = this.mDozeParameters.shouldControlUnlockedScreenOff();
            resetKeyguardDonePendingLocked();
            this.mHideAnimationRun = false;
            notifyFinishedGoingToSleep();
            if (z) {
                ((PowerManager) this.mContext.getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 5, "com.android.systemui:CAMERA_GESTURE_PREVENT_LOCK");
                this.mPendingLock = false;
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
        if (!this.mPendingLock || this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying()) {
            return;
        }
        doKeyguardLocked(null);
        this.mPendingLock = false;
    }

    private boolean isKeyguardServiceEnabled() {
        try {
            return this.mContext.getPackageManager().getServiceInfo(new ComponentName(this.mContext, KeyguardService.class), 0).isEnabled();
        } catch (PackageManager.NameNotFoundException unused) {
            return true;
        }
    }

    private long getLockTimeout(int i) {
        ContentResolver contentResolver;
        long j = Settings.Secure.getInt(this.mContext.getContentResolver(), "lock_screen_lock_after_timeout", 0);
        long maximumTimeToLock = this.mLockPatternUtils.getDevicePolicyManager().getMaximumTimeToLock(null, i);
        return maximumTimeToLock <= 0 ? j : Math.max(Math.min(maximumTimeToLock - Math.max(Settings.System.getInt(contentResolver, "screen_off_timeout", 30000), 0L), j), 0L);
    }

    private void doKeyguardLaterLocked() {
        long lockTimeout = getLockTimeout(KeyguardUpdateMonitor.getCurrentUser());
        if (lockTimeout == 0) {
            doKeyguardLocked(null);
        } else {
            doKeyguardLaterLocked(lockTimeout);
        }
    }

    private void doKeyguardLaterLocked(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime() + j;
        Intent intent = new Intent("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD");
        intent.putExtra("seq", this.mDelayedShowingSequence);
        intent.addFlags(268435456);
        this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320));
        Log.d("KeyguardViewMediator", "setting alarm to turn off keyguard, seq = " + this.mDelayedShowingSequence);
        doKeyguardLaterForChildProfilesLocked();
    }

    private void doKeyguardLaterForChildProfilesLocked() {
        int[] enabledProfileIds;
        for (int i : UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId())) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                long lockTimeout = getLockTimeout(i);
                if (lockTimeout == 0) {
                    doKeyguardForChildProfilesLocked();
                } else {
                    long elapsedRealtime = SystemClock.elapsedRealtime() + lockTimeout;
                    Intent intent = new Intent("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK");
                    intent.putExtra("seq", this.mDelayedProfileShowingSequence);
                    intent.putExtra("android.intent.extra.USER_ID", i);
                    intent.addFlags(268435456);
                    this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 301989888));
                }
            }
        }
    }

    private void doKeyguardForChildProfilesLocked() {
        int[] enabledProfileIds;
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
            SystemUIEventUtils.collectUnLockResults(this.mContext, "unlock_success", 0);
        }
        synchronized (this) {
            this.mDeviceInteractive = true;
            if (this.mPendingLock && !z) {
                doKeyguardLocked(null);
            }
            this.mAnimatingScreenOff = false;
            cancelDoKeyguardLaterLocked();
            cancelDoKeyguardForChildProfilesLocked();
            Log.d("KeyguardViewMediator", "onStartedWakingUp, seq = " + this.mDelayedShowingSequence);
            notifyStartedWakingUp();
        }
        this.mUpdateMonitor.dispatchStartedWakingUp();
        maybeSendUserPresentBroadcast();
        Trace.endSection();
    }

    public void onScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
        Trace.beginSection("KeyguardViewMediator#onScreenTurningOn");
        notifyScreenOn(iKeyguardDrawnCallback);
        Trace.endSection();
    }

    public void onScreenTurnedOn() {
        Trace.beginSection("KeyguardViewMediator#onScreenTurnedOn");
        notifyScreenTurnedOn();
        this.mUpdateMonitor.dispatchScreenTurnedOn();
        Trace.endSection();
    }

    public void onScreenTurnedOff() {
        notifyScreenTurnedOff();
        this.mUpdateMonitor.dispatchScreenTurnedOff();
    }

    private void maybeSendUserPresentBroadcast() {
        if (this.mSystemReady && this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
            sendUserPresentBroadcast();
        } else if (!this.mSystemReady || !shouldWaitForProvisioning()) {
        } else {
            getLockPatternUtils().userPresent(KeyguardUpdateMonitor.getCurrentUser());
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

    public void setKeyguardEnabled(boolean z) {
        synchronized (this) {
            Log.d("KeyguardViewMediator", "setKeyguardEnabled(" + z + ")");
            this.mExternallyEnabled = z;
            if (!z && this.mShowing) {
                if (this.mExitSecureCallback != null) {
                    Log.d("KeyguardViewMediator", "in process of verifyUnlock request, ignoring");
                    return;
                }
                Log.d("KeyguardViewMediator", "remembering to reshow, hiding keyguard, disabling status bar expansion");
                this.mNeedToReshowWhenReenabled = true;
                updateInputRestrictedLocked();
                hideLocked();
            } else if (z && this.mNeedToReshowWhenReenabled) {
                Log.d("KeyguardViewMediator", "previously hidden, reshowing, reenabling status bar expansion");
                this.mNeedToReshowWhenReenabled = false;
                updateInputRestrictedLocked();
                if (this.mExitSecureCallback != null) {
                    Log.d("KeyguardViewMediator", "onKeyguardExitResult(false), resetting");
                    try {
                        this.mExitSecureCallback.onKeyguardExitResult(false);
                    } catch (RemoteException e) {
                        Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e);
                    }
                    this.mExitSecureCallback = null;
                    resetStateLocked();
                } else {
                    showLocked(null);
                    this.mWaitingUntilKeyguardVisible = true;
                    this.mHandler.sendEmptyMessageDelayed(8, 2000L);
                    Log.d("KeyguardViewMediator", "waiting until mWaitingUntilKeyguardVisible is false");
                    while (this.mWaitingUntilKeyguardVisible) {
                        try {
                            wait();
                        } catch (InterruptedException unused) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    Log.d("KeyguardViewMediator", "done waiting for mWaitingUntilKeyguardVisible");
                }
            }
        }
    }

    public void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) {
        Trace.beginSection("KeyguardViewMediator#verifyUnlock");
        synchronized (this) {
            Log.d("KeyguardViewMediator", "verifyUnlock");
            if (shouldWaitForProvisioning()) {
                Log.d("KeyguardViewMediator", "ignoring because device isn't provisioned");
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e);
                }
            } else if (this.mExternallyEnabled) {
                Log.w("KeyguardViewMediator", "verifyUnlock called when not externally disabled");
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e2) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e2);
                }
            } else if (this.mExitSecureCallback != null) {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e3) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e3);
                }
            } else if (!isSecure()) {
                this.mExternallyEnabled = true;
                this.mNeedToReshowWhenReenabled = false;
                updateInputRestricted();
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(true);
                } catch (RemoteException e4) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e4);
                }
            } else {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e5) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e5);
                }
            }
        }
        Trace.endSection();
    }

    public boolean isShowingAndNotOccluded() {
        return this.mShowing && !this.mOccluded;
    }

    public void setOccluded(boolean z, boolean z2) {
        Trace.beginSection("KeyguardViewMediator#setOccluded");
        Log.d("KeyguardViewMediator", "setOccluded " + z);
        this.mHandler.removeMessages(9);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(9, z ? 1 : 0, z2 ? 1 : 0));
        Trace.endSection();
    }

    public static int getUnlockTrackSimState(int i) {
        return mUnlockTrackSimStates.get(i);
    }

    public boolean isHiding() {
        return this.mHiding;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetOccluded(boolean z, boolean z2) {
        boolean z3;
        Trace.beginSection("KeyguardViewMediator#handleSetOccluded");
        synchronized (this) {
            if (this.mHiding && z) {
                startKeyguardExitAnimation(0L, 0L);
            }
            if (this.mOccluded != z) {
                this.mOccluded = z;
                this.mUpdateMonitor.setKeyguardOccluded(z);
                KeyguardViewController keyguardViewController = this.mKeyguardViewControllerLazy.get();
                if (!((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isSimPinSecure() && z2 && this.mDeviceInteractive) {
                    z3 = true;
                    keyguardViewController.setOccluded(z, z3);
                    adjustStatusBarLocked();
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
                    Slog.w("KeyguardViewMediator", "Failed to call onDeviceProvisioned", e);
                    if (e instanceof DeadObjectException) {
                        this.mKeyguardStateCallbacks.remove(iKeyguardStateCallback);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doKeyguardLocked(Bundle bundle) {
        if (KeyguardUpdateMonitor.CORE_APPS_ONLY) {
            Log.d("KeyguardViewMediator", "doKeyguard: not showing because booting to cryptkeeper");
            return;
        }
        boolean z = true;
        if (!this.mExternallyEnabled) {
            Log.d("KeyguardViewMediator", "doKeyguard: not showing because externally disabled");
            this.mNeedToReshowWhenReenabled = true;
        } else if (this.mShowing && this.mKeyguardViewControllerLazy.get().isShowing()) {
            Log.d("KeyguardViewMediator", "doKeyguard: not showing because it is already showing");
            resetStateLocked();
        } else {
            if (!mustNotUnlockCurrentUser() || !this.mUpdateMonitor.isDeviceProvisioned()) {
                boolean z2 = this.mUpdateMonitor.isSimPinSecure() || ((SubscriptionManager.isValidSubscriptionId(this.mUpdateMonitor.getNextSubIdForState(1)) || SubscriptionManager.isValidSubscriptionId(this.mUpdateMonitor.getNextSubIdForState(7))) && (SystemProperties.getBoolean("keyguard.no_require_sim", false) ^ true));
                if (!z2 && shouldWaitForProvisioning()) {
                    Log.d("KeyguardViewMediator", "doKeyguard: not showing because device isn't provisioned and the sim is not locked or missing");
                    return;
                }
                if (bundle == null || !bundle.getBoolean("force_show", false)) {
                    z = false;
                }
                if (this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser()) && !z2 && !z) {
                    Log.d("KeyguardViewMediator", "doKeyguard: not showing because lockscreen is off");
                    return;
                } else if (this.mLockPatternUtils.checkVoldPassword(KeyguardUpdateMonitor.getCurrentUser())) {
                    Log.d("KeyguardViewMediator", "Not showing lock screen since just decrypted");
                    setShowingLocked(false);
                    hideLocked();
                    return;
                }
            }
            Log.d("KeyguardViewMediator", "doKeyguard: showing the lock screen");
            showLocked(bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lockProfile(int i) {
        this.mTrustManager.setDeviceLockedForUser(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldWaitForProvisioning() {
        return !this.mUpdateMonitor.isDeviceProvisioned() && !isSecure();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        if (!this.mShowing) {
            if (iKeyguardDismissCallback == null) {
                return;
            }
            new DismissCallbackWrapper(iKeyguardDismissCallback).notifyDismissError();
            return;
        }
        if (iKeyguardDismissCallback != null) {
            this.mDismissCallbackRegistry.addCallback(iKeyguardDismissCallback);
        }
        this.mCustomMessage = charSequence;
        this.mKeyguardViewControllerLazy.get().dismissAndCollapse();
    }

    public void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        this.mHandler.obtainMessage(11, new DismissMessage(iKeyguardDismissCallback, charSequence)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetStateLocked() {
        Log.e("KeyguardViewMediator", "resetStateLocked");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
    }

    private void notifyStartedGoingToSleep() {
        Log.d("KeyguardViewMediator", "notifyStartedGoingToSleep");
        this.mHandler.sendEmptyMessage(17);
    }

    private void notifyFinishedGoingToSleep() {
        Log.d("KeyguardViewMediator", "notifyFinishedGoingToSleep");
        this.mHandler.sendEmptyMessage(5);
    }

    private void notifyStartedWakingUp() {
        Log.d("KeyguardViewMediator", "notifyStartedWakingUp");
        this.mHandler.sendEmptyMessage(14);
    }

    private void notifyScreenOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
        Log.d("KeyguardViewMediator", "notifyScreenOn");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, iKeyguardDrawnCallback));
    }

    private void notifyScreenTurnedOn() {
        Log.d("KeyguardViewMediator", "notifyScreenTurnedOn");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(15));
    }

    private void notifyScreenTurnedOff() {
        Log.d("KeyguardViewMediator", "notifyScreenTurnedOff");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(16));
    }

    private void showLocked(Bundle bundle) {
        Trace.beginSection("KeyguardViewMediator#showLocked acquiring mShowKeyguardWakeLock");
        Log.d("KeyguardViewMediator", "showLocked");
        this.mShowKeyguardWakeLock.acquire();
        this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(1, bundle));
        Trace.endSection();
    }

    private void hideLocked() {
        Trace.beginSection("KeyguardViewMediator#hideLocked");
        Log.d("KeyguardViewMediator", "hideLocked");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2));
        Trace.endSection();
    }

    public void hideWithAnimation(IRemoteAnimationRunner iRemoteAnimationRunner) {
        if (!this.mKeyguardDonePending) {
            return;
        }
        this.mKeyguardExitAnimationRunner = iRemoteAnimationRunner;
        this.mViewMediatorCallback.readyForKeyguardDone();
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

    public void setSwitchingUser(boolean z) {
        this.mUpdateMonitor.setSwitchingUser(z);
    }

    public void setCurrentUser(int i) {
        KeyguardUpdateMonitor.setCurrentUser(i);
        synchronized (this) {
            notifyTrustedChangedLocked(this.mUpdateMonitor.getUserHasTrust(i));
        }
    }

    public void keyguardDone() {
        Trace.beginSection("KeyguardViewMediator#keyguardDone");
        Log.d("KeyguardViewMediator", "keyguardDone()");
        userActivity();
        EventLog.writeEvent(70000, 2);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7));
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryKeyguardDone() {
        Log.d("KeyguardViewMediator", "tryKeyguardDone: pending - " + this.mKeyguardDonePending + ", animRan - " + this.mHideAnimationRun + " animRunning - " + this.mHideAnimationRunning);
        if (!this.mKeyguardDonePending && this.mHideAnimationRun && !this.mHideAnimationRunning) {
            handleKeyguardDone();
        } else if (this.mHideAnimationRun) {
        } else {
            Log.d("KeyguardViewMediator", "tryKeyguardDone: starting pre-hide animation");
            this.mHideAnimationRun = true;
            this.mHideAnimationRunning = true;
            this.mKeyguardViewControllerLazy.get().startPreHideAnimation(this.mHideAnimationFinishedRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyguardDone() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDone");
        final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardViewMediator.this.lambda$handleKeyguardDone$1(currentUser);
            }
        });
        Log.d("KeyguardViewMediator", "handleKeyguardDone");
        synchronized (this) {
            resetKeyguardDonePendingLocked();
        }
        if (this.mGoingToSleep) {
            this.mUpdateMonitor.clearBiometricRecognized();
            Log.i("KeyguardViewMediator", "Device is going to sleep, aborting keyguardDone");
            return;
        }
        IKeyguardExitCallback iKeyguardExitCallback = this.mExitSecureCallback;
        if (iKeyguardExitCallback != null) {
            try {
                iKeyguardExitCallback.onKeyguardExitResult(true);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult()", e);
            }
            this.mExitSecureCallback = null;
            this.mExternallyEnabled = true;
            this.mNeedToReshowWhenReenabled = false;
            updateInputRestricted();
        }
        handleHide();
        this.mUpdateMonitor.clearBiometricRecognized();
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleKeyguardDone$1(int i) {
        if (this.mLockPatternUtils.isSecure(i)) {
            this.mLockPatternUtils.getDevicePolicyManager().reportKeyguardDismissed(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUserPresentBroadcast() {
        synchronized (this) {
            if (this.mBootCompleted) {
                final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                final UserHandle userHandle = new UserHandle(currentUser);
                final UserManager userManager = (UserManager) this.mContext.getSystemService("user");
                this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        KeyguardViewMediator.this.lambda$sendUserPresentBroadcast$2(userManager, userHandle, currentUser);
                    }
                });
            } else {
                this.mBootSendUserPresent = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendUserPresentBroadcast$2(UserManager userManager, UserHandle userHandle, int i) {
        for (int i2 : userManager.getProfileIdsWithDisabled(userHandle.getIdentifier())) {
            this.mContext.sendBroadcastAsUser(USER_PRESENT_INTENT, UserHandle.of(i2));
        }
        getLockPatternUtils().userPresent(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyguardDoneDrawing() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDoneDrawing");
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleKeyguardDoneDrawing");
            if (this.mWaitingUntilKeyguardVisible) {
                Log.d("KeyguardViewMediator", "handleKeyguardDoneDrawing: notifying mWaitingUntilKeyguardVisible");
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

    private void playSound(final int i) {
        if (i != 0 && Settings.System.getInt(this.mContext.getContentResolver(), "lockscreen_sounds_enabled", 1) == 1) {
            this.mLockSounds.stop(this.mLockSoundStreamId);
            if (this.mAudioManager == null) {
                AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
                this.mAudioManager = audioManager;
                if (audioManager == null) {
                    return;
                }
                this.mUiSoundsStreamType = audioManager.getUiSoundsStreamType();
            }
            this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardViewMediator.this.lambda$playSound$3(i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playSound$3(int i) {
        if (this.mAudioManager.isStreamMute(this.mUiSoundsStreamType)) {
            return;
        }
        SoundPool soundPool = this.mLockSounds;
        float f = this.mLockSoundVolume;
        int play = soundPool.play(i, f, f, 1, 0, 1.0f);
        synchronized (this) {
            this.mLockSoundStreamId = play;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playTrustedSound() {
        playSound(this.mTrustedSoundId);
    }

    private void updateActivityLockScreenState(final boolean z, final boolean z2) {
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardViewMediator.lambda$updateActivityLockScreenState$4(z, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateActivityLockScreenState$4(boolean z, boolean z2) {
        Log.d("KeyguardViewMediator", "updateActivityLockScreenState(" + z + ", " + z2 + ")");
        try {
            ActivityTaskManager.getService().setLockScreenShown(z, z2);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleShow(Bundle bundle) {
        Trace.beginSection("KeyguardViewMediator#handleShow");
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        if (this.mLockPatternUtils.isSecure(currentUser)) {
            this.mLockPatternUtils.getDevicePolicyManager().reportKeyguardSecured(currentUser);
        }
        synchronized (this) {
            if (!this.mSystemReady) {
                Log.d("KeyguardViewMediator", "ignoring handleShow because system is not ready.");
                return;
            }
            Log.d("KeyguardViewMediator", "handleShow");
            this.mHiding = false;
            this.mKeyguardExitAnimationRunner = null;
            this.mWakeAndUnlocking = false;
            this.mPendingLock = false;
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

    /* renamed from: com.android.systemui.keyguard.KeyguardViewMediator$7  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass7 implements Runnable {
        AnonymousClass7() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Trace.beginSection("KeyguardViewMediator.mKeyGuardGoingAwayRunnable");
            Log.d("KeyguardViewMediator", "keyguardGoingAway");
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).keyguardGoingAway();
            final int i = (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).shouldDisableWindowAnimationsForUnlock() || (KeyguardViewMediator.this.mWakeAndUnlocking && !KeyguardViewMediator.this.mWallpaperSupportsAmbientMode)) ? 2 : 0;
            if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).isGoingToNotificationShade() || (KeyguardViewMediator.this.mWakeAndUnlocking && KeyguardViewMediator.this.mWallpaperSupportsAmbientMode)) {
                i |= 1;
            }
            if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).isUnlockWithWallpaper()) {
                i |= 4;
            }
            if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).shouldSubtleWindowAnimationsForUnlock()) {
                i |= 8;
            }
            KeyguardViewMediator.this.mUpdateMonitor.setKeyguardGoingAway(true);
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setKeyguardGoingAwayState(true);
            KeyguardViewMediator.this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardViewMediator.AnonymousClass7.lambda$run$0(i);
                }
            });
            Trace.endSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$run$0(int i) {
            try {
                ActivityTaskManager.getService().keyguardGoingAway(i);
            } catch (RemoteException e) {
                Log.e("KeyguardViewMediator", "Error while calling WindowManager", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        Log.e("KeyguardViewMediator", "mHideAnimationFinishedRunnable#run");
        this.mHideAnimationRunning = false;
        tryKeyguardDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHide() {
        Trace.beginSection("KeyguardViewMediator#handleHide");
        if (this.mAodShowing) {
            ((PowerManager) this.mContext.getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:BOUNCER_DOZING");
        }
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleHide");
            if (mustNotUnlockCurrentUser()) {
                Log.d("KeyguardViewMediator", "Split system user, quit unlocking.");
                this.mKeyguardExitAnimationRunner = null;
                return;
            }
            this.mHiding = true;
            if (this.mShowing && !this.mOccluded) {
                this.mKeyguardGoingAwayRunnable.run();
            } else {
                handleStartKeyguardExitAnimation(SystemClock.uptimeMillis() + this.mHideAnimation.getStartOffset(), this.mHideAnimation.getDuration(), null, null, null, null);
            }
            Trace.endSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStartKeyguardExitAnimation(long j, long j2, final RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        Trace.beginSection("KeyguardViewMediator#handleStartKeyguardExitAnimation");
        Log.d("KeyguardViewMediator", "handleStartKeyguardExitAnimation startTime=" + j + " fadeoutDuration=" + j2);
        synchronized (this) {
            if (!this.mHiding && !this.mSurfaceBehindRemoteAnimationRequested && !this.mKeyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture()) {
                if (iRemoteAnimationFinishedCallback != null) {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException e) {
                        Slog.w("KeyguardViewMediator", "Failed to call onAnimationFinished", e);
                    }
                }
                setShowingLocked(this.mShowing, true);
                return;
            }
            FaceRecognitionController faceRecognitionController = this.mFaceRecognitionController;
            if (faceRecognitionController != null) {
                faceRecognitionController.postToStopCamera();
                this.mFaceRecognitionController.resetAuthUnlockAttempt();
            }
            this.mUpdateMonitor.resetFingerprintLockout();
            this.mUpdateMonitor.doHBM(false);
            this.mHiding = false;
            IRemoteAnimationRunner iRemoteAnimationRunner = this.mKeyguardExitAnimationRunner;
            this.mKeyguardExitAnimationRunner = null;
            if (this.mWakeAndUnlocking && this.mDrawnCallback != null) {
                this.mKeyguardViewControllerLazy.get().getViewRootImpl().setReportNextDraw();
                notifyDrawn(this.mDrawnCallback);
                this.mDrawnCallback = null;
            }
            LatencyTracker.getInstance(this.mContext).onActionEnd(11);
            boolean z = KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation;
            if (z && iRemoteAnimationRunner != null && iRemoteAnimationFinishedCallback != null) {
                IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback2 = new IRemoteAnimationFinishedCallback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.8
                    public void onAnimationFinished() throws RemoteException {
                        try {
                            iRemoteAnimationFinishedCallback.onAnimationFinished();
                        } catch (RemoteException e2) {
                            Slog.w("KeyguardViewMediator", "Failed to call onAnimationFinished", e2);
                        }
                        KeyguardViewMediator.this.onKeyguardExitFinished();
                        ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).hide(0L, 0L);
                        InteractionJankMonitor.getInstance().end(29);
                    }

                    public IBinder asBinder() {
                        return iRemoteAnimationFinishedCallback.asBinder();
                    }
                };
                try {
                    InteractionJankMonitor.getInstance().begin(createInteractionJankMonitorConf("RunRemoteAnimation"));
                    iRemoteAnimationRunner.onAnimationStart(7, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback2);
                } catch (RemoteException e2) {
                    Slog.w("KeyguardViewMediator", "Failed to call onAnimationStart", e2);
                }
            } else if (z && !this.mStatusBarStateController.leaveOpenOnKeyguardHide() && remoteAnimationTargetArr != null && remoteAnimationTargetArr.length > 0) {
                this.mSurfaceBehindRemoteAnimationFinishedCallback = iRemoteAnimationFinishedCallback;
                this.mSurfaceBehindRemoteAnimationRunning = true;
                InteractionJankMonitor.getInstance().begin(createInteractionJankMonitorConf("DismissPanel"));
                this.mKeyguardUnlockAnimationControllerLazy.get().notifyStartKeyguardExitAnimation(remoteAnimationTargetArr[0], j, this.mSurfaceBehindRemoteAnimationRequested);
            } else {
                InteractionJankMonitor.getInstance().begin(createInteractionJankMonitorConf("RemoteAnimationDisabled"));
                this.mKeyguardViewControllerLazy.get().hide(j, j2);
                this.mContext.getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        KeyguardViewMediator.this.lambda$handleStartKeyguardExitAnimation$7(iRemoteAnimationFinishedCallback, remoteAnimationTargetArr);
                    }
                });
                onKeyguardExitFinished();
                this.mKeyguardStateController.notifyKeyguardGoingAway(false);
                ((StatusBar) Dependency.get(StatusBar.class)).setLightRevealScrimValue(1.0f);
            }
            Trace.endSection();
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleStartKeyguardExitAnimation$7(final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (iRemoteAnimationFinishedCallback == null) {
            InteractionJankMonitor.getInstance().end(29);
            return;
        }
        final SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView());
        final RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr[0];
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(400L);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                KeyguardViewMediator.lambda$handleStartKeyguardExitAnimation$6(remoteAnimationTarget, syncRtSurfaceTransactionApplier, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                try {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException unused) {
                        Slog.e("KeyguardViewMediator", "RemoteException");
                    }
                } finally {
                    InteractionJankMonitor.getInstance().end(29);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                try {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException unused) {
                        Slog.e("KeyguardViewMediator", "RemoteException");
                    }
                } finally {
                    InteractionJankMonitor.getInstance().cancel(29);
                }
            }
        });
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleStartKeyguardExitAnimation$6(RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier, ValueAnimator valueAnimator) {
        syncRtSurfaceTransactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget.leash).withAlpha(valueAnimator.getAnimatedFraction()).build()});
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        return new InteractionJankMonitor.Configuration.Builder(29).setView(this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView()).setTag(str);
    }

    public boolean isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe() {
        return this.mSurfaceBehindRemoteAnimationRunning || this.mKeyguardStateController.isFlingingToDismissKeyguard();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCancelKeyguardExitAnimation() {
        showSurfaceBehindKeyguard();
        onKeyguardExitRemoteAnimationFinished(true);
    }

    public void onKeyguardExitRemoteAnimationFinished(boolean z) {
        if (this.mSurfaceBehindRemoteAnimationRunning || this.mSurfaceBehindRemoteAnimationRequested) {
            this.mKeyguardViewControllerLazy.get().blockPanelExpansionFromCurrentTouch();
            boolean z2 = this.mShowing;
            onKeyguardExitFinished();
            if (this.mKeyguardStateController.isDismissingFromSwipe() || !z2) {
                this.mKeyguardUnlockAnimationControllerLazy.get().hideKeyguardViewAfterRemoteAnimation();
            }
            finishSurfaceBehindRemoteAnimation(z);
            this.mSurfaceBehindRemoteAnimationRequested = false;
            this.mKeyguardUnlockAnimationControllerLazy.get().notifyFinishedKeyguardExitAnimation();
            InteractionJankMonitor.getInstance().end(29);
        }
    }

    public void showSurfaceBehindKeyguard() {
        this.mSurfaceBehindRemoteAnimationRequested = true;
        try {
            ActivityTaskManager.getService().keyguardGoingAway(6);
        } catch (RemoteException e) {
            this.mSurfaceBehindRemoteAnimationRequested = false;
            e.printStackTrace();
        }
    }

    public void hideSurfaceBehindKeyguard() {
        this.mSurfaceBehindRemoteAnimationRequested = false;
        if (this.mShowing) {
            setShowingLocked(true, true);
        }
    }

    public boolean requestedShowSurfaceBehindKeyguard() {
        return this.mSurfaceBehindRemoteAnimationRequested;
    }

    public void finishSurfaceBehindRemoteAnimation(boolean z) {
        this.mSurfaceBehindRemoteAnimationRunning = false;
        IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = this.mSurfaceBehindRemoteAnimationFinishedCallback;
        if (iRemoteAnimationFinishedCallback != null) {
            if (!z) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            }
            this.mSurfaceBehindRemoteAnimationFinishedCallback = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustStatusBarLocked() {
        adjustStatusBarLocked(false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustStatusBarLocked(boolean z, boolean z2) {
        if (this.mStatusBarManager == null) {
            this.mStatusBarManager = (StatusBarManager) this.mContext.getSystemService("statusbar");
        }
        StatusBarManager statusBarManager = this.mStatusBarManager;
        if (statusBarManager == null) {
            Log.w("KeyguardViewMediator", "Could not get status bar manager");
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
        Log.d("KeyguardViewMediator", "adjustStatusBarLocked: mShowing=" + this.mShowing + " mOccluded=" + this.mOccluded + " isSecure=" + isSecure() + " force=" + z + " --> flags=0x" + Integer.toHexString(i));
        this.mStatusBarManager.disable(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReset() {
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleReset");
            this.mKeyguardViewControllerLazy.get().reset(true);
            this.mUpdateMonitor.onFaceRecognitionSucceeded(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVerifyUnlock() {
        Trace.beginSection("KeyguardViewMediator#handleVerifyUnlock");
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleVerifyUnlock");
            setShowingLocked(true);
            this.mKeyguardViewControllerLazy.get().dismissAndCollapse();
        }
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyStartedGoingToSleep() {
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleNotifyStartedGoingToSleep");
            this.mKeyguardViewControllerLazy.get().onStartedGoingToSleep();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyFinishedGoingToSleep() {
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleNotifyFinishedGoingToSleep");
            this.mKeyguardViewControllerLazy.get().onFinishedGoingToSleep();
            if (this.mBootCompleted) {
                initFaceRecognition();
            }
            this.mFaceRecognitionController.setScreenOn(false);
            this.mUpdateMonitor.onFaceRecognitionSucceeded(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyStartedWakingUp() {
        Trace.beginSection("KeyguardViewMediator#handleMotifyStartedWakingUp");
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleNotifyWakingUp");
            this.mKeyguardViewControllerLazy.get().onStartedWakingUp();
            this.mFaceRecognitionController.setScreenOn(true);
            this.mUpdateMonitor.onFaceRecognitionSucceeded(false);
            if (!((StatusBar) Dependency.get(StatusBar.class)).isWakeAndUnlock()) {
                ((StatusBar) Dependency.get(StatusBar.class)).setNotificationPanelViewAlpha(1.0f);
            }
            this.mIsAODAuth = false;
            releaseAODWakeLock();
            Log.d("KeyguardViewMediator", "onStartedWakingUp: misAodAuth = " + this.mIsAODAuth);
        }
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
        Trace.beginSection("KeyguardViewMediator#handleNotifyScreenTurningOn");
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleNotifyScreenTurningOn");
            this.mKeyguardViewControllerLazy.get().onScreenTurningOn();
            if (iKeyguardDrawnCallback != null) {
                if (this.mWakeAndUnlocking) {
                    this.mDrawnCallback = iKeyguardDrawnCallback;
                } else {
                    notifyDrawn(iKeyguardDrawnCallback);
                }
            }
        }
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyScreenTurnedOn() {
        Trace.beginSection("KeyguardViewMediator#handleNotifyScreenTurnedOn");
        if (LatencyTracker.isEnabled(this.mContext)) {
            LatencyTracker.getInstance(this.mContext).onActionEnd(5);
        }
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleNotifyScreenTurnedOn");
            this.mKeyguardViewControllerLazy.get().onScreenTurnedOn();
        }
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyScreenTurnedOff() {
        synchronized (this) {
            Log.d("KeyguardViewMediator", "handleNotifyScreenTurnedOff");
            this.mDrawnCallback = null;
        }
    }

    private void notifyDrawn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
        Trace.beginSection("KeyguardViewMediator#notifyDrawn");
        try {
            iKeyguardDrawnCallback.onDrawn();
        } catch (RemoteException e) {
            Slog.w("KeyguardViewMediator", "Exception calling onDrawn():", e);
        }
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetKeyguardDonePendingLocked() {
        this.mKeyguardDonePending = false;
        this.mHandler.removeMessages(13);
    }

    @Override // com.android.systemui.SystemUI
    public void onBootCompleted() {
        synchronized (this) {
            if (this.mContext.getResources().getBoolean(17891567)) {
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

    @Deprecated
    public void startKeyguardExitAnimation(long j, long j2) {
        startKeyguardExitAnimation(0, j, j2, null, null, null, null);
    }

    public void startKeyguardExitAnimation(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        startKeyguardExitAnimation(i, 0L, 0L, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
    }

    private void startKeyguardExitAnimation(int i, long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        Trace.beginSection("KeyguardViewMediator#startKeyguardExitAnimation");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(12, new StartKeyguardExitAnimParams(i, j, j2, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback)));
        Trace.endSection();
    }

    public void cancelKeyguardExitAnimation() {
        Trace.beginSection("KeyguardViewMediator#cancelKeyguardExitAnimation");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(19));
        Trace.endSection();
    }

    public ViewMediatorCallback getViewMediatorCallback() {
        return this.mViewMediatorCallback;
    }

    public LockPatternUtils getLockPatternUtils() {
        return this.mLockPatternUtils;
    }

    @Override // com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
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
        printWriter.println(this.mExitSecureCallback);
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
        printWriter.print("  mWakeAndUnlocking: ");
        printWriter.println(this.mWakeAndUnlocking);
        printWriter.print("  mDrawnCallback: ");
        printWriter.println(this.mDrawnCallback);
    }

    public void setDozing(boolean z) {
        if (z == this.mDozing) {
            return;
        }
        this.mDozing = z;
        if (!z) {
            this.mAnimatingScreenOff = false;
        }
        if (!this.mShowing && this.mPendingLock && this.mDozeParameters.canControlUnlockedScreenOff()) {
            return;
        }
        setShowingLocked(this.mShowing);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozeAmountChanged(float f, float f2) {
        if (!this.mAnimatingScreenOff || !this.mDozing || f != 1.0f) {
            return;
        }
        this.mAnimatingScreenOff = false;
        setShowingLocked(this.mShowing, true);
    }

    public void setPulsing(boolean z) {
        this.mPulsing = z;
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class StartKeyguardExitAnimParams {
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

    void setShowingLocked(boolean z) {
        setShowingLocked(z, false);
    }

    private void setShowingLocked(boolean z, boolean z2) {
        boolean z3 = true;
        boolean z4 = this.mDozing && !this.mWakeAndUnlocking;
        if (z == this.mShowing && z4 == this.mAodShowing && !z2) {
            z3 = false;
        }
        this.mShowing = z;
        this.mAodShowing = z4;
        if (z3) {
            notifyDefaultDisplayCallbacks(z);
            updateActivityLockScreenState(z, z4);
        }
        this.mUpdateMonitor.setShowing(this.mShowing);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDefaultDisplayCallbacks(final boolean z) {
        DejankUtils.whitelistIpcs(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardViewMediator.this.lambda$notifyDefaultDisplayCallbacks$8(z);
            }
        });
        updateInputRestrictedLocked();
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardViewMediator.this.lambda$notifyDefaultDisplayCallbacks$9();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDefaultDisplayCallbacks$8(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            IKeyguardStateCallback iKeyguardStateCallback = this.mKeyguardStateCallbacks.get(size);
            try {
                iKeyguardStateCallback.onShowingStateChanged(z, KeyguardUpdateMonitor.getCurrentUser());
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call onShowingStateChanged", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(iKeyguardStateCallback);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDefaultDisplayCallbacks$9() {
        this.mTrustManager.reportKeyguardShowingChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyTrustedChangedLocked(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            try {
                this.mKeyguardStateCallbacks.get(size).onTrustedChanged(z);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call notifyTrustedChangedLocked", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(size);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyHasLockscreenWallpaperChanged(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            try {
                this.mKeyguardStateCallbacks.get(size).onHasLockscreenWallpaperChanged(z);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call onHasLockscreenWallpaperChanged", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(size);
                }
            }
        }
    }

    public void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) {
        synchronized (this) {
            this.mKeyguardStateCallbacks.add(iKeyguardStateCallback);
            try {
                iKeyguardStateCallback.onSimSecureStateChanged(this.mUpdateMonitor.isSimPinSecure());
                iKeyguardStateCallback.onShowingStateChanged(this.mShowing, KeyguardUpdateMonitor.getCurrentUser());
                iKeyguardStateCallback.onInputRestrictedStateChanged(this.mInputRestricted);
                iKeyguardStateCallback.onTrustedChanged(this.mUpdateMonitor.getUserHasTrust(KeyguardUpdateMonitor.getCurrentUser()));
                iKeyguardStateCallback.onHasLockscreenWallpaperChanged(this.mUpdateMonitor.hasLockscreenWallpaper());
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call to IKeyguardStateCallback", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DismissMessage {
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

    public void onKeyGestureTap(KeyEvent keyEvent) {
        Log.d("KeyguardViewMediator", "onKeyGestureTap:  keyevent = " + keyEvent);
        if (keyEvent.getKeyCode() == 626) {
            if (keyEvent.getAction() == 0) {
                Log.d("KeyguardViewMediator", "onKeyGestureTap: finger down aodAuth = " + this.mIsAODAuth);
                if (this.mIsAODAuth || !this.mUpdateMonitor.isFingerprintDetectionRunning()) {
                    return;
                }
                if (this.mAODFpAuthWakeLock == null) {
                    PowerManager.WakeLock newWakeLock = this.mPM.newWakeLock(1, "KeyguardViewMediator");
                    this.mAODFpAuthWakeLock = newWakeLock;
                    newWakeLock.setReferenceCounted(false);
                }
                aquireAODWakeLock();
                this.mIsAODAuth = true;
                this.mHandler.sendEmptyMessage(600);
            } else if (keyEvent.getAction() != 1) {
            } else {
                Log.d("KeyguardViewMediator", "onKeyGestureTap: finger up");
                this.mHandler.sendEmptyMessage(601);
            }
        } else if (keyEvent.getKeyCode() != 627) {
        } else {
            Log.d("KeyguardViewMediator", "onTapWake: ");
            this.mHandler.sendEmptyMessage(602);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTapWake() {
        Log.d("KeyguardViewMediator", "handleTapWake: = ");
        this.mUpdateMonitor.handleTapWake();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTapGestureDown() {
        Log.d("KeyguardViewMediator", "handleTapGestureDown: = " + this.mIsAODAuth + " ~~~~~~");
        ((StatusBar) Dependency.get(StatusBar.class)).setNotificationPanelViewAlpha(0.0f);
        this.mUpdateMonitor.handleTapGestureDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTapGestureUp() {
        Log.d("KeyguardViewMediator", "handleTapGestureUp: = " + this.mIsAODAuth + " ~~~~~~" + ((StatusBar) Dependency.get(StatusBar.class)).isWakeAndUnlock());
        this.mIsAODAuth = false;
        if (!((StatusBar) Dependency.get(StatusBar.class)).isWakeAndUnlock() && ((AODController) Dependency.get(AODController.class)).shouldShowAODView()) {
            ((StatusBar) Dependency.get(StatusBar.class)).setNotificationPanelViewAlpha(1.0f);
        }
        this.mUpdateMonitor.handleTapGestureUp();
    }

    private void aquireAODWakeLock() {
        Log.d("KeyguardViewMediator", "aquireAODWakeLock");
        PowerManager.WakeLock wakeLock = this.mAODFpAuthWakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.mAODFpAuthWakeLock.release();
        }
        PowerManager.WakeLock wakeLock2 = this.mAODFpAuthWakeLock;
        if (wakeLock2 != null) {
            wakeLock2.acquire(3000L);
        }
    }

    private void releaseAODWakeLock() {
        Log.d("KeyguardViewMediator", "releaseAODWakeLock");
        PowerManager.WakeLock wakeLock = this.mAODFpAuthWakeLock;
        if (wakeLock == null || !wakeLock.isHeld()) {
            return;
        }
        this.mAODFpAuthWakeLock.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initFaceRecognition() {
        if (this.mFaceRecognitionController == null) {
            Log.d("KeyguardViewMediator", "initFaceRecognition", new Exception());
            FaceRecognitionController faceRecognitionController = (FaceRecognitionController) Dependency.get(FaceRecognitionController.class);
            this.mFaceRecognitionController = faceRecognitionController;
            faceRecognitionController.initKeyguardFaceRecognition();
            this.mFaceRecognitionController.setStatusBarKeyguardViewManager(this.mKeyguardViewControllerLazy.get());
            this.mUpdateMonitor.setFaceRecognitionControllerRef(this.mFaceRecognitionController);
            this.mUpdateMonitor.updateFaceRecognitionState();
        }
    }
}
