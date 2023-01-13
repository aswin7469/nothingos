package com.android.systemui.biometrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.hardware.biometrics.BiometricFingerprintConstants;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.util.Log;
import android.util.RotationUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.util.LatencyTracker;
import com.android.internal.util.Preconditions;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.biometrics.BiometricDisplayListener;
import com.android.systemui.biometrics.UdfpsView;
import com.android.systemui.biometrics.dagger.BiometricsBackground;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.time.SystemClock;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.biometrics.NTColorController;
import com.nothing.systemui.biometrics.NTFingerprintBrightnessController;
import com.nothing.systemui.doze.AODController;
import com.nothing.systemui.keyguard.KeyguardUpdateMonitorEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Unit;

@SysUISingleton
public class UdfpsController implements DozeReceiver {
    private static final int AOD_FINGER_DOWN_DELAY = SystemProperties.getInt("persist.sysui.finger.down.delay", 50);
    private static final long AOD_INTERRUPT_TIMEOUT_MILLIS = 1000;
    private static final int BREATHE_ANIM_DELAY = 3000;
    private static final long DISABLE_ORIENTATION_LISTENER_TIME = 5000;
    public static final VibrationEffect EFFECT_CLICK = VibrationEffect.get(0);
    private static final int FPS_SCNNING_ANIM_SIZE = 295;
    private static final long MIN_TOUCH_LOG_INTERVAL = 50;
    private static final String TAG = "UdfpsController";
    public static final VibrationAttributes VIBRATION_ATTRIBUTES = new VibrationAttributes.Builder().setUsage(65).build();
    private final Runnable disableOrientationListenerRunnable;
    private final Runnable enableOrientationListenerRunnable;
    /* access modifiers changed from: private */
    public final AccessibilityManager mAccessibilityManager;
    /* access modifiers changed from: private */
    public boolean mAcquiredReceived;
    private int mActivePointerId = -1;
    /* access modifiers changed from: private */
    public final ActivityLaunchAnimator mActivityLaunchAnimator;
    private NTFingerprintBrightnessController.AlphaCallback mAlphaCallback;
    private final AlternateUdfpsTouchProvider mAlternateTouchProvider;
    /* access modifiers changed from: private */
    public Runnable mAodInterruptRunnable;
    private boolean mAttemptedToDismissKeyguard;
    private Runnable mAuthControllerUpdateUdfpsLocation;
    /* access modifiers changed from: private */
    public boolean mAuthFailed;
    private final Executor mBiometricExecutor;
    /* access modifiers changed from: private */
    public boolean mBouncerVisibility;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Set<Callback> mCallbacks = new HashSet();
    private Runnable mCancelAodTimeoutAction;
    /* access modifiers changed from: private */
    public final ConfigurationController mConfigurationController;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final SystemUIDialogManager mDialogManager;
    private final Runnable mDismissFingerprintIconRunnable;
    /* access modifiers changed from: private */
    public final DumpManager mDumpManager;
    private final Execution mExecution;
    private final FalsingManager mFalsingManager;
    /* access modifiers changed from: private */
    public final DelayableExecutor mFgExecutor;
    /* access modifiers changed from: private */
    public final FingerprintManager mFingerprintManager;
    /* access modifiers changed from: private */
    public boolean mHalControlsIllumination;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public final UdfpsHbmProvider mHbmProvider;
    /* access modifiers changed from: private */
    public final LayoutInflater mInflater;
    private boolean mIsAodInterruptActive;
    /* access modifiers changed from: private */
    public boolean mIsIconVisible;
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    /* access modifiers changed from: private */
    public final StatusBarKeyguardViewManager mKeyguardViewManager;
    private final LatencyTracker mLatencyTracker;
    private final Object mLock;
    /* access modifiers changed from: private */
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    private NTFingerprintBrightnessController mNTBrightnessController;
    private NTColorController mNTColorController;
    /* access modifiers changed from: private */
    public boolean mOnFingerDown;
    private boolean mOnFingerDownOver;
    final BiometricDisplayListener mOrientationListener;
    UdfpsControllerOverlay mOverlay;
    UdfpsOverlayParams mOverlayParams = new UdfpsOverlayParams();
    /* access modifiers changed from: private */
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    /* access modifiers changed from: private */
    public boolean mPendingShowDimlayer;
    /* access modifiers changed from: private */
    public long mPendingShowDimlayerTime;
    private final PowerManager mPowerManager;
    private final ScreenLifecycle.Observer mScreenObserver;
    /* access modifiers changed from: private */
    public boolean mScreenOn;
    int mSensorId;
    private final Runnable mShowFingerprintIconRunnable;
    /* access modifiers changed from: private */
    public final StatusBarStateController mStatusBarStateController;
    private StatusBarStateController.StateListener mStatusBarStateListener;
    /* access modifiers changed from: private */
    public final SystemClock mSystemClock;
    private long mTouchLogTime;
    /* access modifiers changed from: private */
    public int mUdfpsViewVisibility;
    /* access modifiers changed from: private */
    public final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private VelocityTracker mVelocityTracker;
    private final VibratorHelper mVibrator;
    private final WakefulnessLifecycle.Observer mWakefulnessObserver;
    /* access modifiers changed from: private */
    public final WindowManager mWindowManager;
    private final Runnable startAnimRunnable;
    private final Runnable stopAnimRunnable;

    public interface Callback {
        void onFingerDown();

        void onFingerUp();
    }

    public static boolean exceedsVelocityThreshold(float f) {
        return f > 750.0f;
    }

    public class UdfpsOverlayController extends IUdfpsOverlayController.Stub {
        public UdfpsOverlayController() {
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$showUdfpsOverlay$1$com-android-systemui-biometrics-UdfpsController$UdfpsOverlayController */
        public /* synthetic */ void mo30856x2b496fb9(long j, int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback2 = iUdfpsOverlayControllerCallback;
            UdfpsController udfpsController = UdfpsController.this;
            Context access$700 = UdfpsController.this.mContext;
            FingerprintManager access$800 = UdfpsController.this.mFingerprintManager;
            LayoutInflater access$900 = UdfpsController.this.mInflater;
            WindowManager access$1000 = UdfpsController.this.mWindowManager;
            AccessibilityManager access$1100 = UdfpsController.this.mAccessibilityManager;
            StatusBarStateController access$1200 = UdfpsController.this.mStatusBarStateController;
            PanelExpansionStateManager access$1300 = UdfpsController.this.mPanelExpansionStateManager;
            StatusBarKeyguardViewManager access$1400 = UdfpsController.this.mKeyguardViewManager;
            KeyguardUpdateMonitor access$500 = UdfpsController.this.mKeyguardUpdateMonitor;
            SystemUIDialogManager access$1500 = UdfpsController.this.mDialogManager;
            DumpManager access$1600 = UdfpsController.this.mDumpManager;
            LockscreenShadeTransitionController access$1700 = UdfpsController.this.mLockscreenShadeTransitionController;
            ConfigurationController access$1800 = UdfpsController.this.mConfigurationController;
            UdfpsController udfpsController2 = udfpsController;
            SystemClock access$1900 = UdfpsController.this.mSystemClock;
            UdfpsController udfpsController3 = udfpsController2;
            KeyguardStateController access$2000 = UdfpsController.this.mKeyguardStateController;
            UnlockedScreenOffAnimationController access$2100 = UdfpsController.this.mUnlockedScreenOffAnimationController;
            boolean access$2200 = UdfpsController.this.mHalControlsIllumination;
            UdfpsHbmProvider access$2300 = UdfpsController.this.mHbmProvider;
            UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4 udfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4 = r1;
            UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4 udfpsController$UdfpsOverlayController$$ExternalSyntheticLambda42 = new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4(this, j);
            Context context = access$700;
            FingerprintManager fingerprintManager = access$800;
            UdfpsControllerOverlay udfpsControllerOverlay = new UdfpsControllerOverlay(context, fingerprintManager, access$900, access$1000, access$1100, access$1200, access$1300, access$1400, access$500, access$1500, access$1600, access$1700, access$1800, access$1900, access$2000, access$2100, access$2200, access$2300, j, i, iUdfpsOverlayControllerCallback2, udfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4, UdfpsController.this.mActivityLaunchAnimator);
            udfpsController3.showUdfpsOverlay(udfpsControllerOverlay);
        }

        public void showUdfpsOverlay(long j, int i, int i2, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2(this, j, i2, iUdfpsOverlayControllerCallback));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$showUdfpsOverlay$0$com-android-systemui-biometrics-UdfpsController$UdfpsOverlayController */
        public /* synthetic */ Boolean mo30855xa8febada(long j, View view, MotionEvent motionEvent, Boolean bool) {
            return Boolean.valueOf(UdfpsController.this.onTouch(j, motionEvent, bool.booleanValue()));
        }

        public void hideUdfpsOverlay(int i) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$hideUdfpsOverlay$2$com-android-systemui-biometrics-UdfpsController$UdfpsOverlayController */
        public /* synthetic */ void mo30850x67755773() {
            if (UdfpsController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                Log.d(UdfpsController.TAG, "hiding udfps overlay when mKeyguardUpdateMonitor.isFingerprintDetectionRunning()=true");
            }
            UdfpsController.this.hideUdfpsOverlay(false);
        }

        public void onAcquired(int i, int i2) {
            if (BiometricFingerprintConstants.shouldTurnOffHbm(i2)) {
                UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda5(this, i, i2, i2 == 0));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAcquired$3$com-android-systemui-biometrics-UdfpsController$UdfpsOverlayController */
        public /* synthetic */ void mo30851xcb946e3d(int i, int i2, boolean z) {
            if (UdfpsController.this.mOverlay == null) {
                Log.e(UdfpsController.TAG, "Null request when onAcquired for sensorId: " + i + " acquiredInfo=" + i2);
                return;
            }
            boolean unused = UdfpsController.this.mAcquiredReceived = true;
            UdfpsView overlayView = UdfpsController.this.mOverlay.getOverlayView();
            if (overlayView != null) {
                overlayView.stopIllumination();
            }
            if (z) {
                UdfpsController.this.mOverlay.onAcquiredGood();
            }
        }

        public void onEnrollmentProgress(int i, int i2) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda3(this, i2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onEnrollmentProgress$4$com-android-systemui-biometrics-UdfpsController$UdfpsOverlayController */
        public /* synthetic */ void mo30853xfafc4e79(int i) {
            if (UdfpsController.this.mOverlay == null) {
                Log.e(UdfpsController.TAG, "onEnrollProgress received but serverRequest is null");
            } else {
                UdfpsController.this.mOverlay.onEnrollmentProgress(i);
            }
        }

        public void onEnrollmentHelp(int i) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda6(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onEnrollmentHelp$5$com-android-systemui-biometrics-UdfpsController$UdfpsOverlayController */
        public /* synthetic */ void mo30852xd165fcc4() {
            if (UdfpsController.this.mOverlay == null) {
                Log.e(UdfpsController.TAG, "onEnrollmentHelp received but serverRequest is null");
            } else {
                UdfpsController.this.mOverlay.onEnrollmentHelp();
            }
        }

        public void setDebugMessage(int i, String str) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda1(this, str));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setDebugMessage$6$com-android-systemui-biometrics-UdfpsController$UdfpsOverlayController */
        public /* synthetic */ void mo30854x1f7db2b(String str) {
            if (UdfpsController.this.mOverlay != null && !UdfpsController.this.mOverlay.isHiding()) {
                UdfpsController.this.mOverlay.getOverlayView().setDebugMessage(str);
            }
        }
    }

    public void updateOverlayParams(int i, UdfpsOverlayParams udfpsOverlayParams) {
        if (i != this.mSensorId) {
            this.mSensorId = i;
            Log.w(TAG, "updateUdfpsParams | sensorId has changed");
        }
        if (!this.mOverlayParams.equals(udfpsOverlayParams)) {
            this.mOverlayParams = udfpsOverlayParams;
            boolean isShowingAlternateAuth = this.mKeyguardViewManager.isShowingAlternateAuth();
            redrawOverlay();
            if (isShowingAlternateAuth) {
                this.mKeyguardViewManager.showGenericBouncer(true);
            }
        }
    }

    public void setAuthControllerUpdateUdfpsLocation(Runnable runnable) {
        this.mAuthControllerUpdateUdfpsLocation = runnable;
    }

    public void setHalControlsIllumination(boolean z) {
        this.mHalControlsIllumination = z;
    }

    public static float computePointerSpeed(VelocityTracker velocityTracker, int i) {
        return (float) Math.sqrt(Math.pow((double) velocityTracker.getXVelocity(i), 2.0d) + Math.pow((double) velocityTracker.getYVelocity(i), 2.0d));
    }

    public boolean onTouch(MotionEvent motionEvent) {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null || udfpsControllerOverlay.isHiding()) {
            return false;
        }
        return onTouch(this.mOverlay.getRequestId(), motionEvent, false);
    }

    private boolean isWithinSensorArea(UdfpsView udfpsView, float f, float f2, boolean z) {
        if (z) {
            return udfpsView.isWithinSensorArea(f, f2);
        }
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null || udfpsControllerOverlay.getAnimationViewController() == null || this.mOverlay.getAnimationViewController().shouldPauseAuth() || !this.mOverlayParams.getSensorBounds().contains((int) f, (int) f2)) {
            return false;
        }
        return true;
    }

    private Point getTouchInNativeCoordinates(MotionEvent motionEvent, int i) {
        Point point = new Point((int) motionEvent.getRawX(i), (int) motionEvent.getRawY(i));
        int rotation = this.mOverlayParams.getRotation();
        if (rotation == 1 || rotation == 3) {
            RotationUtils.rotatePoint(point, RotationUtils.deltaRotation(rotation, 0), this.mOverlayParams.getLogicalDisplayWidth(), this.mOverlayParams.getLogicalDisplayHeight());
        }
        float scaleFactor = this.mOverlayParams.getScaleFactor();
        point.x = (int) (((float) point.x) / scaleFactor);
        point.y = (int) (((float) point.y) / scaleFactor);
        return point;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0060, code lost:
        if (r4 != 10) goto L_0x01e9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(long r17, android.view.MotionEvent r19, boolean r20) {
        /*
            r16 = this;
            r7 = r16
            r1 = r17
            r0 = r19
            r8 = r20
            com.android.systemui.biometrics.UdfpsControllerOverlay r3 = r7.mOverlay
            java.lang.String r9 = "UdfpsController"
            r10 = 0
            if (r3 != 0) goto L_0x0015
            java.lang.String r0 = "ignoring onTouch with null overlay"
            android.util.Log.w(r9, r0)
            return r10
        L_0x0015:
            boolean r3 = r3.matchesRequestId(r1)
            if (r3 != 0) goto L_0x003e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r3 = "ignoring stale touch event: "
            r0.<init>((java.lang.String) r3)
            java.lang.StringBuilder r0 = r0.append((long) r1)
            java.lang.String r1 = " current: "
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)
            com.android.systemui.biometrics.UdfpsControllerOverlay r1 = r7.mOverlay
            long r1 = r1.getRequestId()
            java.lang.StringBuilder r0 = r0.append((long) r1)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r9, r0)
            return r10
        L_0x003e:
            com.android.systemui.biometrics.UdfpsControllerOverlay r3 = r7.mOverlay
            com.android.systemui.biometrics.UdfpsView r3 = r3.getOverlayView()
            int r4 = r19.getActionMasked()
            r5 = -1
            r11 = 1
            if (r4 == 0) goto L_0x01ec
            if (r4 == r11) goto L_0x01be
            r6 = 4
            r12 = 3
            r13 = 2
            if (r4 == r13) goto L_0x0068
            if (r4 == r12) goto L_0x01be
            if (r4 == r6) goto L_0x0064
            r14 = 7
            if (r4 == r14) goto L_0x0068
            r6 = 9
            if (r4 == r6) goto L_0x01ec
            r0 = 10
            if (r4 == r0) goto L_0x01be
            goto L_0x01e9
        L_0x0064:
            r3.onTouchOutsideView()
            return r11
        L_0x0068:
            java.lang.String r4 = "UdfpsController.onTouch.ACTION_MOVE"
            android.os.Trace.beginSection(r4)
            int r4 = r7.mActivePointerId
            if (r4 != r5) goto L_0x0076
            int r4 = r0.getPointerId(r10)
            goto L_0x007a
        L_0x0076:
            int r4 = r0.findPointerIndex(r4)
        L_0x007a:
            int r5 = r19.getActionIndex()
            if (r4 != r5) goto L_0x01b8
            float r5 = r0.getX(r4)
            float r14 = r0.getY(r4)
            boolean r5 = r7.isWithinSensorArea(r3, r5, r14, r8)
            if (r8 != 0) goto L_0x0090
            if (r5 == 0) goto L_0x00ac
        L_0x0090:
            boolean r8 = r16.shouldTryToDismissKeyguard()
            if (r8 == 0) goto L_0x00ac
            java.lang.String r0 = "onTouch | dismiss keyguard ACTION_MOVE"
            android.util.Log.v(r9, r0)
            boolean r0 = r7.mOnFingerDown
            if (r0 != 0) goto L_0x00a3
            r16.playStartHaptic()
        L_0x00a3:
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r7.mKeyguardViewManager
            r0.notifyKeyguardAuthenticated(r10)
            r7.mAttemptedToDismissKeyguard = r11
            goto L_0x01e9
        L_0x00ac:
            android.graphics.Point r8 = r7.getTouchInNativeCoordinates(r0, r4)
            if (r5 == 0) goto L_0x01a5
            android.view.VelocityTracker r3 = r7.mVelocityTracker
            if (r3 != 0) goto L_0x00bc
            android.view.VelocityTracker r3 = android.view.VelocityTracker.obtain()
            r7.mVelocityTracker = r3
        L_0x00bc:
            android.view.VelocityTracker r3 = r7.mVelocityTracker
            r3.addMovement(r0)
            android.view.VelocityTracker r3 = r7.mVelocityTracker
            r5 = 1000(0x3e8, float:1.401E-42)
            r3.computeCurrentVelocity(r5)
            android.view.VelocityTracker r3 = r7.mVelocityTracker
            int r5 = r7.mActivePointerId
            float r3 = computePointerSpeed(r3, r5)
            float r5 = r0.getTouchMinor(r4)
            float r0 = r0.getTouchMajor(r4)
            boolean r4 = exceedsVelocityThreshold(r3)
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.Float r14 = java.lang.Float.valueOf((float) r5)
            r6[r10] = r14
            java.lang.Float r14 = java.lang.Float.valueOf((float) r0)
            r6[r11] = r14
            java.lang.Float r3 = java.lang.Float.valueOf((float) r3)
            r6[r13] = r3
            java.lang.Boolean r3 = java.lang.Boolean.valueOf((boolean) r4)
            r6[r12] = r3
            java.lang.String r3 = "minor: %.1f, major: %.1f, v: %.1f, exceedsVelocityThreshold: %b"
            java.lang.String r12 = java.lang.String.format(r3, r6)
            com.android.systemui.util.time.SystemClock r3 = r7.mSystemClock
            long r14 = r3.elapsedRealtime()
            long r10 = r7.mTouchLogTime
            long r14 = r14 - r10
            boolean r3 = r7.mAcquiredReceived
            if (r3 != 0) goto L_0x0183
            if (r4 != 0) goto L_0x0183
            com.android.systemui.biometrics.UdfpsOverlayParams r3 = r7.mOverlayParams
            float r3 = r3.getScaleFactor()
            float r5 = r5 / r3
            float r6 = r0 / r3
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r3 = "ACTION_MOVE mOnFingerDown="
            r0.<init>((java.lang.String) r3)
            boolean r3 = r7.mOnFingerDown
            java.lang.StringBuilder r0 = r0.append((boolean) r3)
            java.lang.String r3 = ", mOnFingerDownOver="
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r3)
            boolean r3 = r7.mOnFingerDownOver
            java.lang.StringBuilder r0 = r0.append((boolean) r3)
            java.lang.String r3 = ", mAuthFailed="
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r3)
            boolean r3 = r7.mAuthFailed
            java.lang.StringBuilder r0 = r0.append((boolean) r3)
            java.lang.String r0 = r0.toString()
            com.nothing.systemui.util.NTLogUtil.m1688i(r9, r0)
            boolean r0 = r7.mOnFingerDown
            if (r0 != 0) goto L_0x015a
            boolean r0 = r7.mOnFingerDownOver
            if (r0 != 0) goto L_0x015a
            boolean r0 = r7.mAuthFailed
            if (r0 != 0) goto L_0x015a
            int r3 = r8.x
            int r4 = r8.y
            r0 = r16
            r1 = r17
            r0.mo30825xd7243b17(r1, r3, r4, r5, r6)
            r0 = 1
            r7.mOnFingerDownOver = r0
        L_0x015a:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "onTouch | finger down: "
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r12)
            java.lang.String r0 = r0.toString()
            android.util.Log.v(r9, r0)
            com.android.systemui.util.time.SystemClock r0 = r7.mSystemClock
            long r0 = r0.elapsedRealtime()
            r7.mTouchLogTime = r0
            android.os.PowerManager r0 = r7.mPowerManager
            com.android.systemui.util.time.SystemClock r1 = r7.mSystemClock
            long r1 = r1.uptimeMillis()
            r3 = 0
            r0.userActivity(r1, r13, r3)
            r10 = 1
            goto L_0x01b9
        L_0x0183:
            r0 = 50
            int r0 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x01b8
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "onTouch | finger move: "
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r12)
            java.lang.String r0 = r0.toString()
            android.util.Log.v(r9, r0)
            com.android.systemui.util.time.SystemClock r0 = r7.mSystemClock
            long r0 = r0.elapsedRealtime()
            r7.mTouchLogTime = r0
            goto L_0x01b8
        L_0x01a5:
            java.lang.String r0 = "onTouch | finger outside"
            android.util.Log.v(r9, r0)
            r7.onFingerUp(r1, r3)
            com.android.systemui.util.concurrency.DelayableExecutor r0 = r7.mFgExecutor
            com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda6 r1 = new com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda6
            r1.<init>(r7, r8)
            r0.execute(r1)
        L_0x01b8:
            r10 = 0
        L_0x01b9:
            android.os.Trace.endSection()
            goto L_0x02b9
        L_0x01be:
            java.lang.String r0 = "UdfpsController.onTouch.ACTION_UP"
            android.os.Trace.beginSection(r0)
            r7.mActivePointerId = r5
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            if (r0 == 0) goto L_0x01cf
            r0.recycle()
            r0 = 0
            r7.mVelocityTracker = r0
        L_0x01cf:
            java.lang.String r0 = "onTouch | finger up"
            android.util.Log.v(r9, r0)
            r0 = 0
            r7.mAttemptedToDismissKeyguard = r0
            r7.onFingerUp(r1, r3)
            com.android.systemui.plugins.FalsingManager r1 = r7.mFalsingManager
            r2 = 13
            r1.isFalseTouch(r2)
            android.os.Trace.endSection()
            r7.mOnFingerDownOver = r0
            r7.mAuthFailed = r0
        L_0x01e9:
            r10 = 0
            goto L_0x02b9
        L_0x01ec:
            java.lang.String r4 = "UdfpsController.onTouch.ACTION_DOWN"
            android.os.Trace.beginSection(r4)
            android.view.VelocityTracker r4 = r7.mVelocityTracker
            if (r4 != 0) goto L_0x01fc
            android.view.VelocityTracker r4 = android.view.VelocityTracker.obtain()
            r7.mVelocityTracker = r4
            goto L_0x01ff
        L_0x01fc:
            r4.clear()
        L_0x01ff:
            float r4 = r19.getX()
            float r6 = r19.getY()
            boolean r10 = r7.isWithinSensorArea(r3, r4, r6, r8)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "onTouch | action down | withinSensorArea="
            r3.<init>((java.lang.String) r4)
            java.lang.StringBuilder r3 = r3.append((boolean) r10)
            java.lang.String r4 = ", getX="
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            float r4 = r19.getX()
            java.lang.StringBuilder r3 = r3.append((float) r4)
            java.lang.String r4 = ", getY"
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            float r4 = r19.getY()
            java.lang.StringBuilder r3 = r3.append((float) r4)
            java.lang.String r4 = ", fromUdfpsView="
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            java.lang.StringBuilder r3 = r3.append((boolean) r8)
            java.lang.String r3 = r3.toString()
            com.nothing.systemui.util.NTLogUtil.m1689v(r9, r3)
            if (r10 == 0) goto L_0x0294
            java.lang.String r3 = "UdfpsController.e2e.onPointerDown"
            r4 = 0
            android.os.Trace.beginAsyncSection(r3, r4)
            java.lang.String r3 = "onTouch | action down"
            android.util.Log.v(r9, r3)
            int r3 = r0.getPointerId(r4)
            r7.mActivePointerId = r3
            android.view.VelocityTracker r3 = r7.mVelocityTracker
            r3.addMovement(r0)
            r7.mAcquiredReceived = r4
            int r3 = r7.mActivePointerId
            if (r3 != r5) goto L_0x0268
            int r3 = r0.getPointerId(r4)
            goto L_0x026c
        L_0x0268:
            int r3 = r0.findPointerIndex(r3)
        L_0x026c:
            int r4 = r19.getActionIndex()
            if (r3 != r4) goto L_0x0292
            android.graphics.Point r4 = r7.getTouchInNativeCoordinates(r0, r3)
            float r5 = r0.getTouchMinor(r3)
            float r0 = r0.getTouchMajor(r3)
            com.android.systemui.biometrics.UdfpsOverlayParams r3 = r7.mOverlayParams
            float r3 = r3.getScaleFactor()
            float r5 = r5 / r3
            float r6 = r0 / r3
            int r3 = r4.x
            int r4 = r4.y
            r0 = r16
            r1 = r17
            r0.mo30825xd7243b17(r1, r3, r4, r5, r6)
        L_0x0292:
            r0 = 1
            goto L_0x0295
        L_0x0294:
            r0 = 0
        L_0x0295:
            if (r10 != 0) goto L_0x0299
            if (r8 == 0) goto L_0x02b5
        L_0x0299:
            boolean r1 = r16.shouldTryToDismissKeyguard()
            if (r1 == 0) goto L_0x02b5
            java.lang.String r1 = "onTouch | dismiss keyguard ACTION_DOWN"
            android.util.Log.v(r9, r1)
            boolean r1 = r7.mOnFingerDown
            if (r1 != 0) goto L_0x02ac
            r16.playStartHaptic()
        L_0x02ac:
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r1 = r7.mKeyguardViewManager
            r2 = 0
            r1.notifyKeyguardAuthenticated(r2)
            r1 = 1
            r7.mAttemptedToDismissKeyguard = r1
        L_0x02b5:
            android.os.Trace.endSection()
            r10 = r0
        L_0x02b9:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsController.onTouch(long, android.view.MotionEvent, boolean):boolean");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onTouch$0$com-android-systemui-biometrics-UdfpsController  reason: not valid java name */
    public /* synthetic */ void m2589lambda$onTouch$0$comandroidsystemuibiometricsUdfpsController(Point point) {
        if (this.mOverlay == null) {
            Log.e(TAG, "touch outside sensor area receivedbut serverRequest is null");
            return;
        }
        float scaleFactor = this.mOverlayParams.getScaleFactor();
        this.mOverlay.onTouchOutsideOfSensorArea((float) point.x, (float) point.y, ((float) this.mOverlayParams.getSensorBounds().centerX()) / scaleFactor, ((float) this.mOverlayParams.getSensorBounds().centerY()) / scaleFactor, this.mOverlayParams.getRotation());
    }

    private boolean shouldTryToDismissKeyguard() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        return udfpsControllerOverlay != null && (udfpsControllerOverlay.getAnimationViewController() instanceof UdfpsKeyguardViewController) && this.mKeyguardStateController.canDismissLockScreen() && !this.mAttemptedToDismissKeyguard;
    }

    @Inject
    public UdfpsController(Context context, Execution execution, LayoutInflater layoutInflater, FingerprintManager fingerprintManager, WindowManager windowManager, StatusBarStateController statusBarStateController, @Main DelayableExecutor delayableExecutor, PanelExpansionStateManager panelExpansionStateManager, StatusBarKeyguardViewManager statusBarKeyguardViewManager, DumpManager dumpManager, KeyguardUpdateMonitor keyguardUpdateMonitor, FalsingManager falsingManager, PowerManager powerManager, AccessibilityManager accessibilityManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ScreenLifecycle screenLifecycle, VibratorHelper vibratorHelper, UdfpsHapticsSimulator udfpsHapticsSimulator, UdfpsShell udfpsShell, Optional<UdfpsHbmProvider> optional, KeyguardStateController keyguardStateController, DisplayManager displayManager, @Main Handler handler, ConfigurationController configurationController, SystemClock systemClock, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, SystemUIDialogManager systemUIDialogManager, LatencyTracker latencyTracker, ActivityLaunchAnimator activityLaunchAnimator, Optional<AlternateUdfpsTouchProvider> optional2, @BiometricsBackground Executor executor) {
        StatusBarStateController statusBarStateController2 = statusBarStateController;
        C19761 r3 = new ScreenLifecycle.Observer() {
            public void onScreenTurnedOn() {
                boolean unused = UdfpsController.this.mScreenOn = true;
                if (UdfpsController.this.mAodInterruptRunnable != null) {
                    UdfpsController.this.mAodInterruptRunnable.run();
                    Runnable unused2 = UdfpsController.this.mAodInterruptRunnable = null;
                }
                if (UdfpsController.this.shouldStartHBM()) {
                    UdfpsController.this.showDimLayer(true, "onScreenTurnedOn");
                }
            }

            public void onScreenTurnedOff() {
                boolean unused = UdfpsController.this.mScreenOn = false;
            }
        };
        this.mScreenObserver = r3;
        C19822 r4 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (UdfpsController.this.mOverlay != null && UdfpsController.this.mOverlay.getRequestReason() != 4 && "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    Log.d(UdfpsController.TAG, "ACTION_CLOSE_SYSTEM_DIALOGS received, mRequestReason: " + UdfpsController.this.mOverlay.getRequestReason());
                    UdfpsController.this.mOverlay.cancel();
                    UdfpsController.this.hideUdfpsOverlay(false);
                }
            }
        };
        this.mBroadcastReceiver = r4;
        this.mLock = new Object();
        this.mUdfpsViewVisibility = 0;
        this.mOnFingerDownOver = false;
        this.mAuthFailed = false;
        this.mPendingShowDimlayer = false;
        this.mPendingShowDimlayerTime = 0;
        this.startAnimRunnable = new Runnable() {
            public void run() {
                if (UdfpsController.this.mOverlay != null && UdfpsController.this.mOverlay.getOverlayView() != null) {
                    UdfpsView overlayView = UdfpsController.this.mOverlay.getOverlayView();
                    NTLogUtil.m1688i(UdfpsController.TAG, "handleStartAnim");
                    if (overlayView != null && overlayView.getAnimationViewController() != null && (overlayView.getAnimationViewController() instanceof UdfpsKeyguardViewController) && UdfpsController.this.mScreenOn) {
                        ((UdfpsKeyguardViewController) overlayView.getAnimationViewController()).startFpsBreatheAnim();
                    }
                }
            }
        };
        this.stopAnimRunnable = new Runnable() {
            public void run() {
                if (UdfpsController.this.mOverlay != null && UdfpsController.this.mOverlay.getOverlayView() != null) {
                    UdfpsView overlayView = UdfpsController.this.mOverlay.getOverlayView();
                    NTLogUtil.m1688i(UdfpsController.TAG, "handleStopAnim mView=" + overlayView);
                    if (overlayView != null && overlayView.getAnimationViewController() != null && (overlayView.getAnimationViewController() instanceof UdfpsKeyguardViewController)) {
                        ((UdfpsKeyguardViewController) overlayView.getAnimationViewController()).stopFpsBreatheAnim();
                    }
                }
            }
        };
        this.mStatusBarStateListener = new StatusBarStateController.StateListener() {
            public void onDozingChanged(boolean z) {
                NTLogUtil.m1688i(UdfpsController.TAG, "HBM  onDozingChanged dozing=" + z + ", shouldStartHBM=" + UdfpsController.this.shouldStartHBM());
                if (z) {
                    if (UdfpsController.this.mOverlay != null) {
                        UdfpsController.this.showDimLayer(false, "dozing");
                        UdfpsController.this.mOverlay.onDozingChanged(true);
                    }
                } else if (UdfpsController.this.shouldStartHBM()) {
                    UdfpsController.this.showDimLayer(true, "not dozing");
                }
            }
        };
        this.mDismissFingerprintIconRunnable = new Runnable() {
            public void run() {
                if (UdfpsController.this.mOverlay != null && UdfpsController.this.mOverlay.getOverlayView() != null) {
                    UdfpsView overlayView = UdfpsController.this.mOverlay.getOverlayView();
                    NTLogUtil.m1688i(UdfpsController.TAG, "hideUdfpsOverlay from mDismissFingerprintIconRunnable" + overlayView);
                    if (overlayView != null) {
                        boolean unused = UdfpsController.this.mIsIconVisible = false;
                        overlayView.setAlpha(0.0f);
                    }
                }
            }
        };
        this.mShowFingerprintIconRunnable = new Runnable() {
            public void run() {
                if (UdfpsController.this.mOverlay != null && UdfpsController.this.mOverlay.getOverlayView() != null) {
                    NTLogUtil.m1688i(UdfpsController.TAG, "updateOverlay from mShowFingerprintIconRunnable");
                    UdfpsView overlayView = UdfpsController.this.mOverlay.getOverlayView();
                    if (overlayView != null) {
                        boolean unused = UdfpsController.this.mIsIconVisible = true;
                        NTLogUtil.m1686d(UdfpsController.TAG, "alpha=" + overlayView.getAlpha() + ", setAlpha=1");
                        overlayView.setAlpha(1.0f);
                    }
                }
            }
        };
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
                NTLogUtil.m1688i(UdfpsController.TAG, "onBiometricAuthFailed biometricSourceType=" + biometricSourceType + ", mOnFingerDown=" + UdfpsController.this.mOnFingerDown);
                if (biometricSourceType == BiometricSourceType.FINGERPRINT && UdfpsController.this.mOnFingerDown) {
                    boolean unused = UdfpsController.this.mAuthFailed = true;
                    if (UdfpsController.this.mOverlay != null && UdfpsController.this.mOverlay.getOverlayView() != null) {
                        UdfpsController udfpsController = UdfpsController.this;
                        udfpsController.onFingerUp(udfpsController.mOverlay.getRequestId(), UdfpsController.this.mOverlay.getOverlayView());
                    }
                }
            }

            public void onKeyguardBouncerStateChanged(boolean z) {
                UdfpsController udfpsController = UdfpsController.this;
                boolean unused = udfpsController.mBouncerVisibility = udfpsController.mKeyguardViewManager.isBouncerShowing();
            }

            public void onKeyguardVisibilityChangedRaw(boolean z) {
                boolean z2 = System.currentTimeMillis() - UdfpsController.this.mPendingShowDimlayerTime < 1000;
                NTLogUtil.m1688i(UdfpsController.TAG, "onKeyguardVisibilityChangedRaw showing=" + z + ", shouldStartHBM=" + UdfpsController.this.shouldStartHBM());
                if (z && UdfpsController.this.mPendingShowDimlayer && z2) {
                    UdfpsController.this.showDimLayer(true, "onKeyguardVisibilityChangedRaw");
                    boolean unused = UdfpsController.this.mPendingShowDimlayer = false;
                    long unused2 = UdfpsController.this.mPendingShowDimlayerTime = 0;
                }
            }
        };
        this.mIsIconVisible = false;
        this.mAlphaCallback = new NTFingerprintBrightnessController.AlphaCallback() {
            public void onAlpha(float f) {
                UdfpsView overlayView;
                NTLogUtil.m1686d(UdfpsController.TAG, "onAlpha:" + f);
                if (UdfpsController.this.mOverlay != null && UdfpsController.this.mOverlay.getOverlayView() != null && (overlayView = UdfpsController.this.mOverlay.getOverlayView()) != null && overlayView.getAnimationViewController() != null) {
                    float f2 = 1.0f - f;
                    NTLogUtil.m1686d(UdfpsController.TAG, "updateAlpha:" + f2);
                    overlayView.getAnimationViewController().updateAlpha(f2);
                }
            }
        };
        this.disableOrientationListenerRunnable = new Runnable() {
            public void run() {
                NTLogUtil.m1688i(UdfpsController.TAG, "do disableOrientationListener");
                UdfpsController.this.mOrientationListener.disable();
            }
        };
        this.enableOrientationListenerRunnable = new Runnable() {
            public void run() {
                NTLogUtil.m1688i(UdfpsController.TAG, "do enableOrientationListener");
                UdfpsController.this.mOrientationListener.enable();
            }
        };
        C198114 r6 = new WakefulnessLifecycle.Observer() {
            public void onFinishedWakingUp() {
                NTLogUtil.m1686d(UdfpsController.TAG, "onFinishedWakingUp");
                UdfpsController.this.startFpsBreatheAnim();
            }

            public void onStartedWakingUp() {
                UdfpsView overlayView;
                if (UdfpsController.this.mOverlay != null && UdfpsController.this.mOverlay.getOverlayView() != null && (overlayView = UdfpsController.this.mOverlay.getOverlayView()) != null) {
                    NTLogUtil.m1686d(UdfpsController.TAG, "onStartedWakingUp,mView Visible:" + overlayView.getVisibility());
                    UdfpsController.this.resetDisplaySetting(overlayView.getVisibility());
                }
            }

            public void onFinishedGoingToSleep() {
                NTLogUtil.m1686d(UdfpsController.TAG, "onFinishedGoingToSleep");
                UdfpsController.this.stopFpsBreatheAnim();
            }
        };
        this.mWakefulnessObserver = r6;
        this.mContext = context;
        this.mExecution = execution;
        this.mVibrator = vibratorHelper;
        this.mInflater = layoutInflater;
        FingerprintManager fingerprintManager2 = (FingerprintManager) Preconditions.checkNotNull(fingerprintManager);
        this.mFingerprintManager = fingerprintManager2;
        this.mWindowManager = windowManager;
        this.mFgExecutor = delayableExecutor;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        this.mStatusBarStateController = statusBarStateController2;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mDumpManager = dumpManager;
        this.mDialogManager = systemUIDialogManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mFalsingManager = falsingManager;
        this.mPowerManager = powerManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mHbmProvider = optional.orElse(null);
        screenLifecycle.addObserver(r3);
        ((WakefulnessLifecycle) Dependency.get(WakefulnessLifecycle.class)).addObserver(r6);
        this.mScreenOn = screenLifecycle.getScreenState() == 2;
        this.mConfigurationController = configurationController;
        this.mSystemClock = systemClock;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mLatencyTracker = latencyTracker;
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        this.mAlternateTouchProvider = optional2.orElse(null);
        this.mBiometricExecutor = executor;
        this.mOrientationListener = new BiometricDisplayListener(context, displayManager, handler, BiometricDisplayListener.SensorType.UnderDisplayFingerprint.INSTANCE, new UdfpsController$$ExternalSyntheticLambda4(this));
        UdfpsOverlayController udfpsOverlayController = new UdfpsOverlayController();
        fingerprintManager2.setUdfpsOverlayController(udfpsOverlayController);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(r4, intentFilter, 2);
        udfpsHapticsSimulator.setUdfpsController(this);
        udfpsShell.setUdfpsOverlayController(udfpsOverlayController);
        this.mHandler = handler;
        statusBarStateController2.addCallback(this.mStatusBarStateListener);
        this.mBouncerVisibility = false;
        this.mHandler.post(new Runnable() {
            public void run() {
                UdfpsController.this.mKeyguardUpdateMonitor.registerCallback(UdfpsController.this.mKeyguardUpdateMonitorCallback);
            }
        });
        this.mNTColorController = (NTColorController) NTDependencyEx.get(NTColorController.class);
        NTFingerprintBrightnessController nTFingerprintBrightnessController = new NTFingerprintBrightnessController(context, this);
        this.mNTBrightnessController = nTFingerprintBrightnessController;
        nTFingerprintBrightnessController.setAlphaCallback(this.mAlphaCallback);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-biometrics-UdfpsController  reason: not valid java name */
    public /* synthetic */ Unit m2588lambda$new$1$comandroidsystemuibiometricsUdfpsController() {
        Runnable runnable = this.mAuthControllerUpdateUdfpsLocation;
        if (runnable != null) {
            runnable.run();
        }
        return Unit.INSTANCE;
    }

    public void playStartHaptic() {
        if (this.mAccessibilityManager.isTouchExplorationEnabled()) {
            this.mVibrator.vibrate(Process.myUid(), this.mContext.getOpPackageName(), EFFECT_CLICK, "udfps-onStart-click", VIBRATION_ATTRIBUTES);
        }
    }

    public void dozeTimeTick() {
        UdfpsView overlayView;
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null && (overlayView = udfpsControllerOverlay.getOverlayView()) != null) {
            overlayView.dozeTimeTick();
        }
    }

    private void redrawOverlay() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            hideUdfpsOverlay(true);
            showUdfpsOverlay(udfpsControllerOverlay);
        }
    }

    /* access modifiers changed from: private */
    public void showUdfpsOverlay(UdfpsControllerOverlay udfpsControllerOverlay) {
        this.mExecution.assertIsMainThread();
        this.mOverlay = udfpsControllerOverlay;
        final int requestReason = udfpsControllerOverlay.getRequestReason();
        if (requestReason == 4 && !this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
            Log.d(TAG, "Attempting to showUdfpsOverlay when fingerprint detection isn't running on keyguard. Skip show.");
            showDimLayer(false, "showUdfpsOverlay skip");
        } else if (udfpsControllerOverlay.show(this, this.mOverlayParams)) {
            Log.v(TAG, "showUdfpsOverlay | adding window reason=" + requestReason);
            this.mOnFingerDown = false;
            this.mAttemptedToDismissKeyguard = false;
            enableOrientationListener();
            resetDisplaySetting(0);
            this.mOnFingerDownOver = false;
            this.mAuthFailed = false;
            if (!this.mStatusBarStateController.isDozing()) {
                showDimLayer(true, "showUdfpsOverlay");
            }
            this.mUdfpsViewVisibility = 0;
            final UdfpsView overlayView = udfpsControllerOverlay.getOverlayView();
            if (overlayView != null) {
                overlayView.post(new Runnable() {
                    public void run() {
                        NTLogUtil.m1688i(UdfpsController.TAG, "HBM showDimLayer from reason=" + requestReason + ", shouldStartHBM=" + UdfpsController.this.shouldStartHBM());
                        int i = requestReason;
                        if (i == 2 || i == 3 || i == 4) {
                            overlayView.setVisibilityListener(new UdfpsController$4$$ExternalSyntheticLambda0(this));
                        }
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$run$0$com-android-systemui-biometrics-UdfpsController$4  reason: not valid java name */
                    public /* synthetic */ void m2590lambda$run$0$comandroidsystemuibiometricsUdfpsController$4(int i) {
                        NTLogUtil.m1688i(UdfpsController.TAG, "onVisibilityChanged=" + i);
                        UdfpsController.this.resetDisplaySetting(i);
                        int unused = UdfpsController.this.mUdfpsViewVisibility = i;
                    }
                });
            }
        } else {
            Log.v(TAG, "showUdfpsOverlay | the overlay is already showing");
        }
    }

    /* access modifiers changed from: private */
    public void hideUdfpsOverlay(boolean z) {
        this.mExecution.assertIsMainThread();
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            UdfpsView overlayView = udfpsControllerOverlay.getOverlayView();
            if (overlayView != null) {
                onFingerUp(this.mOverlay.getRequestId(), overlayView);
            }
            stopFpsBreatheAnim();
            NTLogUtil.m1688i(TAG, "HBM stopHbm from hideUdfpsOverlay");
            if (!z) {
                showDimLayer(false, "hideUdfpsOverlay");
            }
            this.mNTColorController.restoreDisplaySettingsIfNeeded();
            if (overlayView != null) {
                overlayView.setVisibilityListener((UdfpsView.UdfpsViewVisibilityListener) null);
            }
            boolean hide = this.mOverlay.hide();
            if (this.mKeyguardViewManager.isShowingAlternateAuth()) {
                this.mKeyguardViewManager.resetAlternateAuth(true);
            }
            Log.v(TAG, "hideUdfpsOverlay | removing window: " + hide);
        } else {
            Log.v(TAG, "hideUdfpsOverlay | the overlay is already hidden");
        }
        this.mOverlay = null;
        disableOrientationListener();
    }

    /* access modifiers changed from: package-private */
    public void onAodInterrupt(int i, int i2, float f, float f2) {
        if (!this.mIsAodInterruptActive) {
            if (!this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                this.mKeyguardViewManager.showBouncer(true);
                return;
            }
            UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
            UdfpsController$$ExternalSyntheticLambda5 udfpsController$$ExternalSyntheticLambda5 = new UdfpsController$$ExternalSyntheticLambda5(this, udfpsControllerOverlay != null ? udfpsControllerOverlay.getRequestId() : -1, i, i2, f2, f);
            this.mAodInterruptRunnable = udfpsController$$ExternalSyntheticLambda5;
            if (this.mScreenOn) {
                udfpsController$$ExternalSyntheticLambda5.run();
                this.mAodInterruptRunnable = null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onAodInterrupt$3$com-android-systemui-biometrics-UdfpsController */
    public /* synthetic */ void mo30826xd6add518(long j, int i, int i2, float f, float f2) {
        this.mIsAodInterruptActive = true;
        this.mCancelAodTimeoutAction = this.mFgExecutor.executeDelayed(new UdfpsController$$ExternalSyntheticLambda7(this), 1000);
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null) {
            NTLogUtil.m1686d(TAG, "Null request in onAodInterrupt");
            return;
        }
        UdfpsView overlayView = udfpsControllerOverlay.getOverlayView();
        if (overlayView != null) {
            overlayView.startIllumination((Runnable) null);
        }
        StringBuilder sb = new StringBuilder("onAodInterrupt onFingerDown delay ");
        int i3 = AOD_FINGER_DOWN_DELAY;
        NTLogUtil.m1686d(TAG, sb.append(i3).toString());
        this.mHandler.postDelayed(new UdfpsController$$ExternalSyntheticLambda8(this, j, i, i2, f, f2), (long) i3);
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    /* access modifiers changed from: package-private */
    public void onCancelUdfps() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (!(udfpsControllerOverlay == null || udfpsControllerOverlay.getOverlayView() == null)) {
            onFingerUp(this.mOverlay.getRequestId(), this.mOverlay.getOverlayView());
        }
        if (this.mIsAodInterruptActive) {
            Runnable runnable = this.mCancelAodTimeoutAction;
            if (runnable != null) {
                runnable.run();
                this.mCancelAodTimeoutAction = null;
            }
            this.mIsAodInterruptActive = false;
        }
    }

    public boolean isFingerDown() {
        return this.mOnFingerDown;
    }

    /* access modifiers changed from: private */
    /* renamed from: onFingerDown */
    public void mo30825xd7243b17(long j, int i, int i2, float f, float f2) {
        long j2 = j;
        this.mExecution.assertIsMainThread();
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null) {
            Log.w(TAG, "Null request in onFingerDown");
        } else if (!udfpsControllerOverlay.matchesRequestId(j2)) {
            Log.w(TAG, "Mismatched fingerDown: " + j2 + " current: " + this.mOverlay.getRequestId());
        } else {
            this.mLatencyTracker.onActionStart(14);
            if (!this.mOnFingerDown) {
                playStartHaptic();
                if (!this.mKeyguardUpdateMonitor.isFaceDetectionRunning()) {
                    this.mKeyguardUpdateMonitor.requestFaceAuth(false);
                }
            }
            this.mOnFingerDown = true;
            if (this.mAlternateTouchProvider != null) {
                this.mBiometricExecutor.execute(new UdfpsController$$ExternalSyntheticLambda0(this, j, i, i2, f, f2));
            } else {
                this.mFingerprintManager.onPointerDown(j, this.mSensorId, i, i2, f, f2);
            }
            Trace.endAsyncSection("UdfpsController.e2e.onPointerDown", 0);
            UdfpsView overlayView = this.mOverlay.getOverlayView();
            if (overlayView != null) {
                if (shouldDozingFingerDownEnableHBM()) {
                    ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewAlpha(0.0f);
                    showDimLayer(true, "onFingerDown");
                }
                overlayView.startIllumination(new UdfpsController$$ExternalSyntheticLambda1(this, j2));
            }
            for (Callback onFingerDown : this.mCallbacks) {
                onFingerDown.onFingerDown();
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFingerDown$4$com-android-systemui-biometrics-UdfpsController */
    public /* synthetic */ void mo30827x35f27137(long j, int i, int i2, float f, float f2) {
        this.mAlternateTouchProvider.onPointerDown(j, i, i2, f, f2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFingerDown$6$com-android-systemui-biometrics-UdfpsController */
    public /* synthetic */ void mo30829x3505a539(long j) {
        if (this.mAlternateTouchProvider != null) {
            this.mBiometricExecutor.execute(new UdfpsController$$ExternalSyntheticLambda3(this));
            return;
        }
        this.mFingerprintManager.onUiReady(j, this.mSensorId);
        this.mLatencyTracker.onActionEnd(14);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFingerDown$5$com-android-systemui-biometrics-UdfpsController */
    public /* synthetic */ void mo30828x357c0b38() {
        this.mAlternateTouchProvider.onUiReady();
        this.mLatencyTracker.onActionEnd(14);
    }

    /* access modifiers changed from: private */
    public void onFingerUp(long j, UdfpsView udfpsView) {
        this.mExecution.assertIsMainThread();
        this.mActivePointerId = -1;
        this.mAcquiredReceived = false;
        if (this.mOnFingerDown) {
            if (this.mAlternateTouchProvider != null) {
                this.mBiometricExecutor.execute(new UdfpsController$$ExternalSyntheticLambda2(this, j));
            } else {
                this.mFingerprintManager.onPointerUp(j, this.mSensorId);
            }
            for (Callback onFingerUp : this.mCallbacks) {
                onFingerUp.onFingerUp();
            }
        }
        this.mOnFingerDown = false;
        if (udfpsView.isIlluminationRequested()) {
            udfpsView.stopIllumination();
        }
        if (shouldDozingFingerDownEnableHBM()) {
            showDimLayer(false, "onFingerUp");
            if (!((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).isWakeAndUnlock() && ((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView()) {
                ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewAlpha(1.0f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFingerUp$7$com-android-systemui-biometrics-UdfpsController */
    public /* synthetic */ void mo30830x41374633(long j) {
        this.mAlternateTouchProvider.onPointerUp(j);
    }

    /* access modifiers changed from: private */
    public void startFpsBreatheAnim() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.startAnimRunnable);
            this.mHandler.postDelayed(this.startAnimRunnable, 3000);
        }
    }

    /* access modifiers changed from: private */
    public void stopFpsBreatheAnim() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.startAnimRunnable);
            this.mHandler.post(this.stopAnimRunnable);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void showDimLayer(boolean r5, java.lang.String r6) {
        /*
            r4 = this;
            java.lang.String r0 = "Show DimLayer open= "
            java.lang.Object r1 = r4.mLock
            monitor-enter(r1)
            com.nothing.systemui.biometrics.NTFingerprintBrightnessController r2 = r4.mNTBrightnessController     // Catch:{ all -> 0x003e }
            if (r2 != 0) goto L_0x0012
            java.lang.String r4 = "UdfpsController"
            java.lang.String r5 = "mNTBrightnessController is null"
            com.nothing.systemui.util.NTLogUtil.m1687e(r4, r5)     // Catch:{ all -> 0x003e }
            monitor-exit(r1)     // Catch:{ all -> 0x003e }
            return
        L_0x0012:
            java.lang.String r2 = "UdfpsController"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x003e }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x003e }
            java.lang.StringBuilder r0 = r3.append((boolean) r5)     // Catch:{ all -> 0x003e }
            java.lang.String r3 = " reason= "
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r3)     // Catch:{ all -> 0x003e }
            java.lang.StringBuilder r6 = r0.append((java.lang.String) r6)     // Catch:{ all -> 0x003e }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x003e }
            com.nothing.systemui.util.NTLogUtil.m1688i(r2, r6)     // Catch:{ all -> 0x003e }
            if (r5 == 0) goto L_0x0037
            com.nothing.systemui.biometrics.NTFingerprintBrightnessController r4 = r4.mNTBrightnessController     // Catch:{ all -> 0x003e }
            r5 = 1
            r4.drawDimLayer(r5)     // Catch:{ all -> 0x003e }
            goto L_0x003c
        L_0x0037:
            com.nothing.systemui.biometrics.NTFingerprintBrightnessController r4 = r4.mNTBrightnessController     // Catch:{ all -> 0x003e }
            r4.dismiss()     // Catch:{ all -> 0x003e }
        L_0x003c:
            monitor-exit(r1)     // Catch:{ all -> 0x003e }
            return
        L_0x003e:
            r4 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x003e }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsController.showDimLayer(boolean, java.lang.String):void");
    }

    /* access modifiers changed from: private */
    public boolean shouldStartHBM() {
        boolean isUdfpsEnrolled = this.mKeyguardUpdateMonitor.isUdfpsEnrolled();
        boolean isDozing = this.mStatusBarStateController.isDozing();
        boolean z = true;
        boolean z2 = this.mStatusBarStateController.getState() == 2;
        boolean isKeyguardVisible = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isKeyguardVisible();
        boolean isDeviceInteractive = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isDeviceInteractive();
        boolean isGoingToSleep = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isGoingToSleep();
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null || udfpsControllerOverlay.getOverlayView() == null || !isUdfpsEnrolled || isDozing || z2 || !isKeyguardVisible || !isDeviceInteractive || isGoingToSleep || this.mBouncerVisibility) {
            z = false;
        }
        NTLogUtil.m1688i(TAG, "HBM shouldStartHBM:" + z + ",  hasFingerprintEnrolled=" + isUdfpsEnrolled + ",getState=" + this.mStatusBarStateController.getState() + ", isDozing =" + isDozing + ", statusBarShadeLocked=" + z2 + ", keyguardVisible=" + isKeyguardVisible + ", deviceInteractive=" + isDeviceInteractive + ", isGoingToSleep=" + isGoingToSleep + ", mBouncerVisibility=" + this.mBouncerVisibility);
        return z;
    }

    public void dismissFingerprintIcon() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mShowFingerprintIconRunnable);
            this.mHandler.post(this.mDismissFingerprintIconRunnable);
        }
    }

    public void showFingerprintIcon() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mDismissFingerprintIconRunnable);
            this.mHandler.post(this.mShowFingerprintIconRunnable);
        }
    }

    public boolean isFpIconVisible() {
        return this.mIsIconVisible;
    }

    public View getView() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            return udfpsControllerOverlay.getOverlayView();
        }
        return null;
    }

    private boolean shouldDozingFingerDownEnableHBM() {
        return this.mStatusBarStateController.isDozing() && !((KeyguardUpdateMonitorEx) NTDependencyEx.get(KeyguardUpdateMonitorEx.class)).isLowPowerState();
    }

    private void disableOrientationListener() {
        if (this.mHandler != null) {
            NTLogUtil.m1688i(TAG, "disableOrientationListener");
            this.mHandler.removeCallbacks(this.startAnimRunnable);
            this.mHandler.removeCallbacks(this.disableOrientationListenerRunnable);
            this.mHandler.postDelayed(this.disableOrientationListenerRunnable, 5000);
        }
    }

    private void enableOrientationListener() {
        if (this.mHandler != null) {
            NTLogUtil.m1688i(TAG, "enableOrientationListener");
            this.mHandler.removeCallbacks(this.disableOrientationListenerRunnable);
            this.mHandler.post(this.enableOrientationListenerRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void resetDisplaySetting(int i) {
        if (i == 0) {
            this.mNTColorController.resetDisplaySettingsIfNeeded();
        }
    }
}
