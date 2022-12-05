package com.android.systemui.biometrics;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.media.AudioAttributes;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.R$layout;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.biometrics.UdfpsView;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.time.SystemClock;
import com.nothingos.keyguard.NTColorController;
import com.nothingos.keyguard.fingerprint.NTFingerprintBrightnessController;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public class UdfpsController implements DozeReceiver {
    private final AccessibilityManager mAccessibilityManager;
    private Runnable mAodInterruptRunnable;
    private boolean mAttemptedToDismissKeyguard;
    private boolean mBouncerVisibility;
    private final BroadcastReceiver mBroadcastReceiver;
    private Runnable mCancelAodTimeoutAction;
    private final ConfigurationController mConfigurationController;
    private final Context mContext;
    private final WindowManager.LayoutParams mCoreLayoutParams;
    private final DumpManager mDumpManager;
    private final Execution mExecution;
    private final FalsingManager mFalsingManager;
    private final DelayableExecutor mFgExecutor;
    private final FingerprintManager mFingerprintManager;
    private boolean mGoodCaptureReceived;
    private Handler mHandler;
    private final UdfpsHbmProvider mHbmProvider;
    private final LayoutInflater mInflater;
    private boolean mIsAodInterruptActive;
    private final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final StatusBarKeyguardViewManager mKeyguardViewManager;
    private final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    private NTFingerprintBrightnessController mNTBrightnessController;
    private NTColorController mNTColorController;
    private boolean mOnFingerDown;
    @VisibleForTesting
    final BiometricOrientationEventListener mOrientationListener;
    private final PowerManager mPowerManager;
    private final ScreenLifecycle.Observer mScreenObserver;
    private boolean mScreenOn;
    @VisibleForTesting
    final FingerprintSensorPropertiesInternal mSensorProps;
    ServerRequest mServerRequest;
    private final StatusBar mStatusBar;
    private final StatusBarStateController mStatusBarStateController;
    private final SystemClock mSystemClock;
    private long mTouchLogTime;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private VelocityTracker mVelocityTracker;
    private final Vibrator mVibrator;
    private UdfpsView mView;
    private final WindowManager mWindowManager;
    @VisibleForTesting
    public static final AudioAttributes VIBRATION_SONIFICATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(11).build();
    public static final VibrationEffect EFFECT_CLICK = VibrationEffect.get(0);
    private int mActivePointerId = -1;
    private Set<Callback> mCallbacks = new HashSet();
    @SuppressLint({"ClickableViewAccessibility"})
    private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda1
        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            boolean lambda$new$0;
            lambda$new$0 = UdfpsController.this.lambda$new$0(view, motionEvent);
            return lambda$new$0;
        }
    };
    @SuppressLint({"ClickableViewAccessibility"})
    private final View.OnHoverListener mOnHoverListener = new View.OnHoverListener() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda0
        @Override // android.view.View.OnHoverListener
        public final boolean onHover(View view, MotionEvent motionEvent) {
            boolean lambda$new$1;
            lambda$new$1 = UdfpsController.this.lambda$new$1(view, motionEvent);
            return lambda$new$1;
        }
    };
    private final AccessibilityManager.TouchExplorationStateChangeListener mTouchExplorationStateChangeListener = new AccessibilityManager.TouchExplorationStateChangeListener() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda2
        @Override // android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
        public final void onTouchExplorationStateChanged(boolean z) {
            UdfpsController.this.lambda$new$2(z);
        }
    };
    private final Object mLock = new Object();
    private int mUdfpsViewVisibility = 0;
    private boolean mOnFingerDownOver = false;
    private boolean mAuthFailed = false;
    private boolean mPendingShowDimlayer = false;
    private long mPendingShowDimlayerTime = 0;
    private final Runnable startAnimRunnable = new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController.7
        @Override // java.lang.Runnable
        public void run() {
            Log.i("UdfpsController", "handleStartAnim");
            if (UdfpsController.this.mView == null || UdfpsController.this.mView.getAnimationViewController() == null || !(UdfpsController.this.mView.getAnimationViewController() instanceof UdfpsKeyguardViewController) || !UdfpsController.this.mScreenOn) {
                return;
            }
            ((UdfpsKeyguardViewController) UdfpsController.this.mView.getAnimationViewController()).startFpsBreatheAnim();
        }
    };
    private final Runnable stopAnimRunnable = new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController.8
        @Override // java.lang.Runnable
        public void run() {
            Log.i("UdfpsController", "handleStopAnim mView=" + UdfpsController.this.mView);
            if (UdfpsController.this.mView == null || UdfpsController.this.mView.getAnimationViewController() == null || !(UdfpsController.this.mView.getAnimationViewController() instanceof UdfpsKeyguardViewController)) {
                return;
            }
            ((UdfpsKeyguardViewController) UdfpsController.this.mView.getAnimationViewController()).stopFpsBreatheAnim();
        }
    };
    private StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.biometrics.UdfpsController.9
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozingChanged(boolean z) {
            Log.i("UdfpsController", "HBM  onDozingChanged dozing=" + z + ", shouldStartHBM=" + UdfpsController.this.shouldStartHBM());
            if (z) {
                if (UdfpsController.this.mView == null) {
                    return;
                }
                UdfpsController.this.hideDimLayer();
                if (UdfpsController.this.mView.getAnimationViewController() != null) {
                    Log.i("UdfpsController", "onDozingChanged update icon Alpha");
                    UdfpsController.this.mView.getAnimationViewController().updateAlpha(1.0f);
                }
                UdfpsController.this.mWindowManager.removeView(UdfpsController.this.mView);
                UdfpsController.this.mWindowManager.addView(UdfpsController.this.mView, UdfpsController.this.mCoreLayoutParams);
            } else if (!UdfpsController.this.shouldStartHBM()) {
            } else {
                UdfpsController.this.showDimLayer();
            }
        }
    };
    private final Runnable mDismissFingerprintIconRunnable = new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController.10
        @Override // java.lang.Runnable
        public void run() {
            Log.i("UdfpsController", "hideUdfpsOverlay from mDismissFingerprintIconRunnable" + UdfpsController.this.mView);
            if (UdfpsController.this.mView != null) {
                UdfpsController.this.mIsIconVisible = false;
                UdfpsController.this.mView.setAlpha(0.0f);
            }
        }
    };
    private final Runnable mShowFingerprintIconRunnable = new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController.11
        @Override // java.lang.Runnable
        public void run() {
            Log.i("UdfpsController", "updateOverlay from mShowFingerprintIconRunnable");
            if (UdfpsController.this.mView != null) {
                UdfpsController.this.mIsIconVisible = true;
                UdfpsController.this.mView.setAlpha(1.0f);
            }
            if (UdfpsController.this.mView != null) {
                Log.i("UdfpsController", "showDimLayer from mShowFingerprintIconRunnable");
                UdfpsController.this.showDimLayer();
            }
        }
    };
    private KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.biometrics.UdfpsController.12
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
            Log.i("UdfpsController", "onBiometricAuthFailed biometricSourceType=" + biometricSourceType + ", mOnFingerDown=" + UdfpsController.this.mOnFingerDown);
            if (biometricSourceType != BiometricSourceType.FINGERPRINT || !UdfpsController.this.mOnFingerDown) {
                return;
            }
            UdfpsController.this.mAuthFailed = true;
            UdfpsController.this.onFingerUp();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardBouncerChanged(boolean z) {
            Log.i("UdfpsController", "onKeyguardBouncerChanged bouncerVisibility=" + UdfpsController.this.mKeyguardViewManager.isBouncerShowing());
            UdfpsController udfpsController = UdfpsController.this;
            udfpsController.mBouncerVisibility = udfpsController.mKeyguardViewManager.isBouncerShowing();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChangedRaw(boolean z) {
            boolean z2 = System.currentTimeMillis() - UdfpsController.this.mPendingShowDimlayerTime < 1000;
            Log.i("UdfpsController", "onKeyguardVisibilityChangedRaw showing=" + z + ", shouldStartHBM=" + UdfpsController.this.shouldStartHBM());
            if (!z || !UdfpsController.this.mPendingShowDimlayer || !z2) {
                return;
            }
            Log.i("UdfpsController", "onKeyguardVisibilityChangedRaw showDimLayer");
            UdfpsController.this.showDimLayer();
            UdfpsController.this.mPendingShowDimlayer = false;
            UdfpsController.this.mPendingShowDimlayerTime = 0L;
        }
    };
    private boolean mIsIconVisible = false;
    private NTFingerprintBrightnessController.AlphaCallback mAlphaCallback = new NTFingerprintBrightnessController.AlphaCallback() { // from class: com.android.systemui.biometrics.UdfpsController.13
        @Override // com.nothingos.keyguard.fingerprint.NTFingerprintBrightnessController.AlphaCallback
        public void onAlpha(float f) {
            Log.d("UdfpsController", "onAlpha:" + f);
            if (UdfpsController.this.mView == null || UdfpsController.this.mView.getAnimationViewController() == null) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("updateAlpha:");
            float f2 = 1.0f - f;
            sb.append(f2);
            Log.d("UdfpsController", sb.toString());
            UdfpsController.this.mView.getAnimationViewController().updateAlpha(f2);
        }
    };

    /* loaded from: classes.dex */
    public interface Callback {
        void onFingerDown();

        void onFingerUp();
    }

    private int getCoreLayoutParamFlags() {
        return 16777512;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ServerRequest {
        final IUdfpsOverlayControllerCallback mCallback;
        final UdfpsEnrollHelper mEnrollHelper;
        final int mRequestReason;

        ServerRequest(int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback, UdfpsEnrollHelper udfpsEnrollHelper) {
            this.mRequestReason = i;
            this.mCallback = iUdfpsOverlayControllerCallback;
            this.mEnrollHelper = udfpsEnrollHelper;
        }

        void onEnrollmentProgress(int i) {
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper != null) {
                udfpsEnrollHelper.onEnrollmentProgress(i);
            }
        }

        void onAcquiredGood() {
            Log.d("UdfpsController", "onAcquiredGood");
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper != null) {
                udfpsEnrollHelper.animateIfLastStep();
            }
        }

        void onEnrollmentHelp() {
            Log.d("UdfpsController", "onEnrollmentHelp");
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper != null) {
                udfpsEnrollHelper.onEnrollmentHelp();
            }
        }

        void onUserCanceled() {
            try {
                this.mCallback.onUserCanceled();
            } catch (RemoteException e) {
                Log.e("UdfpsController", "Remote exception", e);
            }
        }
    }

    /* loaded from: classes.dex */
    public class UdfpsOverlayController extends IUdfpsOverlayController.Stub {
        public UdfpsOverlayController() {
        }

        public void showUdfpsOverlay(int i, final int i2, final IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController.this.lambda$showUdfpsOverlay$0(i2, iUdfpsOverlayControllerCallback);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showUdfpsOverlay$0(int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            UdfpsController.this.mServerRequest = new ServerRequest(i, iUdfpsOverlayControllerCallback, (i == 1 || i == 2) ? new UdfpsEnrollHelper(UdfpsController.this.mContext, UdfpsController.this.mFingerprintManager, i) : null);
            Log.i("UdfpsController", "showUdfpsOverlay from Framework");
            UdfpsController.this.updateOverlay();
        }

        public void hideUdfpsOverlay(int i) {
            Log.i("UdfpsController", "hideUdfpsOverlay from framework:" + i);
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController.this.lambda$hideUdfpsOverlay$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$hideUdfpsOverlay$1() {
            UdfpsController.this.mServerRequest = null;
            Log.i("UdfpsController", "hideUdfpsOverlay from Framework");
            UdfpsController.this.updateOverlay();
        }

        public void onAcquiredGood(final int i) {
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController.this.lambda$onAcquiredGood$2(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAcquiredGood$2(int i) {
            if (UdfpsController.this.mView != null) {
                UdfpsController.this.mGoodCaptureReceived = true;
                UdfpsController.this.mView.stopIlluminationNT();
                ServerRequest serverRequest = UdfpsController.this.mServerRequest;
                if (serverRequest != null) {
                    serverRequest.onAcquiredGood();
                    return;
                } else {
                    Log.e("UdfpsController", "Null serverRequest when onAcquiredGood");
                    return;
                }
            }
            Log.e("UdfpsController", "Null view when onAcquiredGood for sensorId: " + i);
        }

        public void onEnrollmentProgress(int i, final int i2) {
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController.this.lambda$onEnrollmentProgress$3(i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentProgress$3(int i) {
            ServerRequest serverRequest = UdfpsController.this.mServerRequest;
            if (serverRequest == null) {
                Log.e("UdfpsController", "onEnrollProgress received but serverRequest is null");
            } else {
                serverRequest.onEnrollmentProgress(i);
            }
        }

        public void onEnrollmentHelp(int i) {
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController.this.lambda$onEnrollmentHelp$4();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentHelp$4() {
            ServerRequest serverRequest = UdfpsController.this.mServerRequest;
            if (serverRequest == null) {
                Log.e("UdfpsController", "onEnrollmentHelp received but serverRequest is null");
            } else {
                serverRequest.onEnrollmentHelp();
            }
        }

        public void setDebugMessage(int i, final String str) {
            UdfpsController.this.mFgExecutor.execute(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.UdfpsOverlayController.this.lambda$setDebugMessage$5(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setDebugMessage$5(String str) {
            if (UdfpsController.this.mView == null) {
                return;
            }
            UdfpsController.this.mView.setDebugMessage(str);
        }
    }

    private static float computePointerSpeed(VelocityTracker velocityTracker, int i) {
        return (float) Math.sqrt(Math.pow(velocityTracker.getXVelocity(i), 2.0d) + Math.pow(velocityTracker.getYVelocity(i), 2.0d));
    }

    public boolean onTouch(MotionEvent motionEvent) {
        UdfpsView udfpsView = this.mView;
        if (udfpsView == null) {
            return false;
        }
        return onTouch(udfpsView, motionEvent, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
        return onTouch(view, motionEvent, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(View view, MotionEvent motionEvent) {
        return onTouch(view, motionEvent, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(boolean z) {
        updateTouchListener();
    }

    private boolean isWithinSensorArea(UdfpsView udfpsView, float f, float f2, boolean z) {
        if (z) {
            return udfpsView.isWithinSensorArea(f, f2);
        }
        UdfpsView udfpsView2 = this.mView;
        return udfpsView2 != null && udfpsView2.getAnimationViewController() != null && !this.mView.getAnimationViewController().shouldPauseAuth() && getSensorLocation().contains(f, f2);
    }

    private boolean onTouch(View view, MotionEvent motionEvent, boolean z) {
        boolean z2;
        int findPointerIndex;
        int findPointerIndex2;
        UdfpsView udfpsView = (UdfpsView) view;
        int actionMasked = motionEvent.getActionMasked();
        boolean z3 = true;
        boolean z4 = false;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked == 4) {
                            udfpsView.onTouchOutsideView();
                            return true;
                        } else if (actionMasked != 7) {
                            if (actionMasked != 9) {
                                if (actionMasked != 10) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                Trace.beginSection("UdfpsController.onTouch.ACTION_MOVE");
                int i = this.mActivePointerId;
                if (i == -1) {
                    findPointerIndex2 = motionEvent.getPointerId(0);
                } else {
                    findPointerIndex2 = motionEvent.findPointerIndex(i);
                }
                if (findPointerIndex2 == motionEvent.getActionIndex()) {
                    boolean isWithinSensorArea = isWithinSensorArea(udfpsView, motionEvent.getX(findPointerIndex2), motionEvent.getY(findPointerIndex2), z);
                    if ((z || isWithinSensorArea) && shouldTryToDismissKeyguard()) {
                        Log.v("UdfpsController", "onTouch | dismiss keyguard ACTION_MOVE");
                        if (!this.mOnFingerDown) {
                            playStartHaptic();
                        }
                        this.mKeyguardViewManager.notifyKeyguardAuthenticated(false);
                        this.mAttemptedToDismissKeyguard = true;
                        return false;
                    } else if (isWithinSensorArea) {
                        if (this.mVelocityTracker == null) {
                            this.mVelocityTracker = VelocityTracker.obtain();
                        }
                        this.mVelocityTracker.addMovement(motionEvent);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        float computePointerSpeed = computePointerSpeed(this.mVelocityTracker, this.mActivePointerId);
                        float touchMinor = motionEvent.getTouchMinor(findPointerIndex2);
                        float touchMajor = motionEvent.getTouchMajor(findPointerIndex2);
                        boolean z5 = computePointerSpeed > 750.0f;
                        String format = String.format("minor: %.1f, major: %.1f, v: %.1f, exceedsVelocityThreshold: %b", Float.valueOf(touchMinor), Float.valueOf(touchMajor), Float.valueOf(computePointerSpeed), Boolean.valueOf(z5));
                        long elapsedRealtime = this.mSystemClock.elapsedRealtime() - this.mTouchLogTime;
                        if (this.mGoodCaptureReceived || z5) {
                            if (elapsedRealtime >= 50) {
                                Log.v("UdfpsController", "onTouch | finger move: " + format);
                                this.mTouchLogTime = this.mSystemClock.elapsedRealtime();
                            }
                            z3 = false;
                        } else {
                            Log.i("UdfpsController", "ACTION_MOVE mOnFingerDown=" + this.mOnFingerDown + ", mOnFingerDownOver=" + this.mOnFingerDownOver + ", mAuthFailed=" + this.mAuthFailed);
                            if (!this.mOnFingerDown && !this.mOnFingerDownOver && !this.mAuthFailed) {
                                onFingerDown((int) motionEvent.getRawX(), (int) motionEvent.getRawY(), touchMinor, touchMajor);
                                this.mOnFingerDownOver = true;
                            }
                            Log.v("UdfpsController", "onTouch | finger down: " + format);
                            this.mTouchLogTime = this.mSystemClock.elapsedRealtime();
                            this.mPowerManager.userActivity(this.mSystemClock.uptimeMillis(), 2, 0);
                        }
                        z4 = z3;
                    } else {
                        Log.v("UdfpsController", "onTouch | finger outside");
                        onFingerUp();
                    }
                }
                Trace.endSection();
                return z4;
            }
            Trace.beginSection("UdfpsController.onTouch.ACTION_UP");
            this.mActivePointerId = -1;
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            Log.v("UdfpsController", "onTouch | finger up");
            this.mAttemptedToDismissKeyguard = false;
            onFingerUp();
            this.mFalsingManager.isFalseTouch(13);
            Trace.endSection();
            this.mOnFingerDownOver = false;
            this.mAuthFailed = false;
            return false;
        }
        Trace.beginSection("UdfpsController.onTouch.ACTION_DOWN");
        VelocityTracker velocityTracker2 = this.mVelocityTracker;
        if (velocityTracker2 == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker2.clear();
        }
        boolean isWithinSensorArea2 = isWithinSensorArea(udfpsView, motionEvent.getX(), motionEvent.getY(), z);
        Log.v("UdfpsController", "onTouch | action down | withinSensorArea=" + isWithinSensorArea2);
        if (isWithinSensorArea2) {
            Trace.beginAsyncSection("UdfpsController.e2e.onPointerDown", 0);
            Log.v("UdfpsController", "onTouch | action down");
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mVelocityTracker.addMovement(motionEvent);
            int i2 = this.mActivePointerId;
            if (i2 == -1) {
                findPointerIndex = motionEvent.getPointerId(0);
            } else {
                findPointerIndex = motionEvent.findPointerIndex(i2);
            }
            if (findPointerIndex == motionEvent.getActionIndex()) {
                onFingerDown((int) motionEvent.getRawX(), (int) motionEvent.getRawY(), motionEvent.getTouchMinor(findPointerIndex), motionEvent.getTouchMajor(findPointerIndex));
            }
            z2 = true;
        } else {
            Log.v("UdfpsController", "onTouch | action down | withinSensorArea=false , getX=" + motionEvent.getX() + ", getY" + motionEvent.getY() + ", fromUdfpsView=" + z);
            z2 = false;
        }
        if ((isWithinSensorArea2 || z) && shouldTryToDismissKeyguard()) {
            Log.v("UdfpsController", "onTouch | dismiss keyguard ACTION_DOWN");
            if (!this.mOnFingerDown) {
                playStartHaptic();
            }
            this.mKeyguardViewManager.notifyKeyguardAuthenticated(false);
            this.mAttemptedToDismissKeyguard = true;
        }
        Trace.endSection();
        return z2;
    }

    private boolean shouldTryToDismissKeyguard() {
        return this.mView.getAnimationViewController() != null && (this.mView.getAnimationViewController() instanceof UdfpsKeyguardViewController) && this.mKeyguardStateController.canDismissLockScreen() && !this.mAttemptedToDismissKeyguard;
    }

    public UdfpsController(Context context, Execution execution, LayoutInflater layoutInflater, FingerprintManager fingerprintManager, WindowManager windowManager, StatusBarStateController statusBarStateController, DelayableExecutor delayableExecutor, StatusBar statusBar, StatusBarKeyguardViewManager statusBarKeyguardViewManager, DumpManager dumpManager, KeyguardUpdateMonitor keyguardUpdateMonitor, FalsingManager falsingManager, PowerManager powerManager, AccessibilityManager accessibilityManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ScreenLifecycle screenLifecycle, Vibrator vibrator, UdfpsHapticsSimulator udfpsHapticsSimulator, Optional<UdfpsHbmProvider> optional, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, DisplayManager displayManager, Handler handler, ConfigurationController configurationController, SystemClock systemClock, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        ScreenLifecycle.Observer observer = new ScreenLifecycle.Observer() { // from class: com.android.systemui.biometrics.UdfpsController.1
            @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
            public void onScreenTurnedOn() {
                Log.i("UdfpsController", "HBM  onScreenTurnedOn shouldStartHBM()=" + UdfpsController.this.shouldStartHBM());
                UdfpsController.this.mScreenOn = true;
                if (UdfpsController.this.mAodInterruptRunnable != null) {
                    UdfpsController.this.mAodInterruptRunnable.run();
                    UdfpsController.this.mAodInterruptRunnable = null;
                }
                UdfpsController.this.startFpsBreatheAnim();
                if (UdfpsController.this.shouldStartHBM()) {
                    UdfpsController.this.showDimLayer();
                }
            }

            @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
            public void onScreenTurnedOff() {
                Log.i("UdfpsController", "HBM  onScreenTurnedOff shouldStartHBM()=" + UdfpsController.this.shouldStartHBM());
                UdfpsController.this.mScreenOn = false;
                UdfpsController.this.stopFpsBreatheAnim();
            }

            @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
            public void onScreenTurningOff() {
                Log.i("UdfpsController", "HBM stopHbm from onScreenTurningOff");
            }

            @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
            public void onScreenTurningOn() {
                Log.i("UdfpsController", "HBM  onScreenTurningOn shouldStartHBM()=" + UdfpsController.this.shouldStartHBM());
            }
        };
        this.mScreenObserver = observer;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.biometrics.UdfpsController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                ServerRequest serverRequest = UdfpsController.this.mServerRequest;
                if (serverRequest == null || serverRequest.mRequestReason == 4 || !"android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    return;
                }
                Log.d("UdfpsController", "ACTION_CLOSE_SYSTEM_DIALOGS received, mRequestReason: " + UdfpsController.this.mServerRequest.mRequestReason);
                UdfpsController.this.mServerRequest.onUserCanceled();
                UdfpsController.this.mServerRequest = null;
                Log.i("UdfpsController", "updateOverlay from ACTION_CLOSE_SYSTEM_DIALOGS");
                UdfpsController.this.updateOverlay();
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mContext = context;
        this.mExecution = execution;
        this.mVibrator = vibrator;
        this.mInflater = layoutInflater;
        FingerprintManager fingerprintManager2 = (FingerprintManager) Preconditions.checkNotNull(fingerprintManager);
        this.mFingerprintManager = fingerprintManager2;
        this.mWindowManager = windowManager;
        this.mFgExecutor = delayableExecutor;
        this.mStatusBar = statusBar;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mDumpManager = dumpManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mFalsingManager = falsingManager;
        this.mPowerManager = powerManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mHbmProvider = optional.orElse(null);
        screenLifecycle.addObserver(observer);
        boolean z = true;
        this.mScreenOn = screenLifecycle.getScreenState() == 2;
        this.mOrientationListener = new BiometricOrientationEventListener(context, new Function0() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function0
            /* renamed from: invoke */
            public final Object mo1951invoke() {
                Unit lambda$new$3;
                lambda$new$3 = UdfpsController.this.lambda$new$3();
                return lambda$new$3;
            }
        }, displayManager, handler);
        this.mKeyguardBypassController = keyguardBypassController;
        this.mConfigurationController = configurationController;
        this.mSystemClock = systemClock;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        FingerprintSensorPropertiesInternal findFirstUdfps = findFirstUdfps();
        this.mSensorProps = findFirstUdfps;
        Log.i("UdfpsController", "UdfpsController mSensorProps: sensorLocationX=" + findFirstUdfps.sensorLocationX + ", sensorLocationY=" + findFirstUdfps.sensorLocationY + ", sensorRadius=" + findFirstUdfps.sensorRadius);
        Preconditions.checkArgument(findFirstUdfps == null ? false : z);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2009, getCoreLayoutParamFlags(), -3);
        this.mCoreLayoutParams = layoutParams;
        layoutParams.setTitle("UdfpsController");
        layoutParams.setFitInsetsTypes(0);
        layoutParams.gravity = 51;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.privateFlags = 536870912;
        fingerprintManager2.setUdfpsOverlayController(new UdfpsOverlayController());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(broadcastReceiver, intentFilter);
        udfpsHapticsSimulator.setUdfpsController(this);
        this.mHandler = handler;
        statusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mBouncerVisibility = false;
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController.3
            @Override // java.lang.Runnable
            public void run() {
                UdfpsController.this.mKeyguardUpdateMonitor.registerCallback(UdfpsController.this.mKeyguardUpdateMonitorCallback);
            }
        });
        this.mNTColorController = (NTColorController) Dependency.get(NTColorController.class);
        NTFingerprintBrightnessController nTFingerprintBrightnessController = new NTFingerprintBrightnessController(context, this);
        this.mNTBrightnessController = nTFingerprintBrightnessController;
        nTFingerprintBrightnessController.setAlphaCallback(this.mAlphaCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$3() {
        onOrientationChanged();
        return Unit.INSTANCE;
    }

    @VisibleForTesting
    public void playStartHaptic() {
        Vibrator vibrator = this.mVibrator;
        if (vibrator != null) {
            vibrator.vibrate(Process.myUid(), this.mContext.getOpPackageName(), EFFECT_CLICK, "udfps-onStart", VIBRATION_SONIFICATION_ATTRIBUTES);
        }
    }

    private FingerprintSensorPropertiesInternal findFirstUdfps() {
        for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : this.mFingerprintManager.getSensorPropertiesInternal()) {
            if (fingerprintSensorPropertiesInternal.isAnyUdfpsType()) {
                return fingerprintSensorPropertiesInternal;
            }
        }
        return null;
    }

    @Override // com.android.systemui.doze.DozeReceiver
    public void dozeTimeTick() {
        UdfpsView udfpsView = this.mView;
        if (udfpsView != null) {
            udfpsView.dozeTimeTick();
        }
    }

    public RectF getSensorLocation() {
        FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mSensorProps;
        int i = fingerprintSensorPropertiesInternal.sensorLocationX;
        int i2 = fingerprintSensorPropertiesInternal.sensorRadius;
        int i3 = fingerprintSensorPropertiesInternal.sensorLocationY;
        return new RectF(i - i2, i3 - i2, i + i2, i3 + i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOverlay() {
        this.mExecution.assertIsMainThread();
        Log.d("UdfpsController", "updateOverlay:  mServerRequest = " + this.mServerRequest);
        ServerRequest serverRequest = this.mServerRequest;
        if (serverRequest != null) {
            showUdfpsOverlay(serverRequest);
        } else {
            hideUdfpsOverlay();
        }
    }

    private boolean shouldRotate(UdfpsAnimationViewController udfpsAnimationViewController) {
        if (!(udfpsAnimationViewController instanceof UdfpsKeyguardViewController)) {
            return true;
        }
        return !this.mKeyguardUpdateMonitor.isGoingToSleep() && this.mKeyguardStateController.isOccluded();
    }

    private WindowManager.LayoutParams computeLayoutParams(UdfpsAnimationViewController udfpsAnimationViewController) {
        int i = 0;
        int paddingX = udfpsAnimationViewController != null ? udfpsAnimationViewController.getPaddingX() : 0;
        if (udfpsAnimationViewController != null) {
            i = udfpsAnimationViewController.getPaddingY();
        }
        this.mCoreLayoutParams.flags = getCoreLayoutParamFlags();
        if (udfpsAnimationViewController != null && udfpsAnimationViewController.listenForTouchesOutsideView()) {
            this.mCoreLayoutParams.flags |= 262144;
        }
        Log.i("UdfpsController", "computeLayoutParams sensorLocationX=" + this.mSensorProps.sensorLocationX + ", sensorLocationY=" + this.mSensorProps.sensorLocationY + ", sensorRadius=" + this.mSensorProps.sensorRadius + ", padding=" + paddingX);
        WindowManager.LayoutParams layoutParams = this.mCoreLayoutParams;
        FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mSensorProps;
        layoutParams.x = fingerprintSensorPropertiesInternal.sensorLocationX + (-147);
        layoutParams.y = fingerprintSensorPropertiesInternal.sensorLocationY + (-147);
        layoutParams.height = 295;
        layoutParams.width = 295;
        View ghbmView = this.mView.getGhbmView();
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) ghbmView.getLayoutParams();
        int i2 = this.mSensorProps.sensorRadius;
        layoutParams2.width = (i2 * 2) + (paddingX * 2);
        layoutParams2.height = (i2 * 2) + (i * 2);
        layoutParams2.topMargin = 147 - i2;
        layoutParams2.setMarginStart(147 - i2);
        ghbmView.setLayoutParams(layoutParams2);
        Point point = new Point();
        this.mContext.getDisplay().getRealSize(point);
        int rotation = this.mContext.getDisplay().getRotation();
        if (rotation != 1) {
            if (rotation == 3) {
                if (!shouldRotate(udfpsAnimationViewController)) {
                    Log.v("UdfpsController", "skip rotating udfps location ROTATION_270");
                } else {
                    Log.v("UdfpsController", "rotate udfps location ROTATION_270");
                    WindowManager.LayoutParams layoutParams3 = this.mCoreLayoutParams;
                    int i3 = point.x;
                    FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal2 = this.mSensorProps;
                    layoutParams3.x = (i3 - fingerprintSensorPropertiesInternal2.sensorLocationY) - 147;
                    layoutParams3.y = fingerprintSensorPropertiesInternal2.sensorLocationX - 147;
                }
            }
        } else if (!shouldRotate(udfpsAnimationViewController)) {
            Log.v("UdfpsController", "skip rotating udfps location ROTATION_90");
        } else {
            Log.v("UdfpsController", "rotate udfps location ROTATION_90");
            WindowManager.LayoutParams layoutParams4 = this.mCoreLayoutParams;
            FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal3 = this.mSensorProps;
            layoutParams4.x = fingerprintSensorPropertiesInternal3.sensorLocationY - 147;
            layoutParams4.y = (point.y - fingerprintSensorPropertiesInternal3.sensorLocationX) - 147;
        }
        WindowManager.LayoutParams layoutParams5 = this.mCoreLayoutParams;
        layoutParams5.accessibilityTitle = " ";
        return layoutParams5;
    }

    private void onOrientationChanged() {
        boolean isShowingAlternateAuth = this.mKeyguardViewManager.isShowingAlternateAuth();
        hideUdfpsOverlay();
        Log.i("UdfpsController", "updateOverlay from onOrientationChanged");
        updateOverlay();
        if (isShowingAlternateAuth) {
            this.mKeyguardViewManager.showGenericBouncer(true);
        }
    }

    private void showUdfpsOverlay(ServerRequest serverRequest) {
        this.mExecution.assertIsMainThread();
        final int i = serverRequest.mRequestReason;
        if (this.mView == null) {
            try {
                Log.v("UdfpsController", "showUdfpsOverlay | adding window reason=" + i);
                UdfpsView udfpsView = (UdfpsView) this.mInflater.inflate(R$layout.udfps_view, (ViewGroup) null, false);
                this.mView = udfpsView;
                this.mOnFingerDown = false;
                udfpsView.setSensorProperties(this.mSensorProps);
                this.mView.setHbmProvider(this.mHbmProvider);
                UdfpsAnimationViewController inflateUdfpsAnimation = inflateUdfpsAnimation(i);
                this.mAttemptedToDismissKeyguard = false;
                inflateUdfpsAnimation.init();
                this.mView.setAnimationViewController(inflateUdfpsAnimation);
                this.mOrientationListener.enable();
                if (i == 1 || i == 2 || i == 3) {
                    this.mView.setImportantForAccessibility(2);
                }
                this.mNTColorController.resetDisplaySettingsIfNeeded();
                this.mOnFingerDownOver = false;
                this.mAuthFailed = false;
                this.mWindowManager.addView(this.mView, computeLayoutParams(inflateUdfpsAnimation));
                this.mUdfpsViewVisibility = 0;
                this.mView.postDelayed(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController.4
                    @Override // java.lang.Runnable
                    public void run() {
                        Log.i("UdfpsController", "HBM showDimLayer from reason=" + i + ", shouldStartHBM=" + UdfpsController.this.shouldStartHBM());
                        int i2 = i;
                        if (i2 == 2 || i2 == 3) {
                            UdfpsController.this.drawDimLayer(true);
                            if (UdfpsController.this.mView == null) {
                                return;
                            }
                            UdfpsController.this.mView.setVisibilityListener(new UdfpsView.UdfpsViewVisibilityListener() { // from class: com.android.systemui.biometrics.UdfpsController.4.1
                                @Override // com.android.systemui.biometrics.UdfpsView.UdfpsViewVisibilityListener
                                public void onVisibilityChanged(int i3) {
                                    Log.i("UdfpsController", "onVisibilityChanged=" + i3);
                                    if (UdfpsController.this.mUdfpsViewVisibility != 0 && i3 == 0) {
                                        UdfpsController.this.drawDimLayer(true);
                                    }
                                    if (i3 != 0) {
                                        UdfpsController.this.hideDimLayer();
                                    }
                                    UdfpsController.this.mUdfpsViewVisibility = i3;
                                }
                            });
                        } else if (i2 != 4) {
                        } else {
                            UdfpsController.this.showDimLayer();
                            if (UdfpsController.this.mView == null) {
                                return;
                            }
                            UdfpsController.this.mView.setVisibilityListener(new UdfpsView.UdfpsViewVisibilityListener() { // from class: com.android.systemui.biometrics.UdfpsController.4.2
                                @Override // com.android.systemui.biometrics.UdfpsView.UdfpsViewVisibilityListener
                                public void onVisibilityChanged(int i3) {
                                    Log.i("UdfpsController", "onVisibilityChanged=" + i3);
                                    if (UdfpsController.this.mUdfpsViewVisibility != 0 && i3 == 0) {
                                        UdfpsController.this.showDimLayer();
                                    }
                                    if (i3 != 0) {
                                        UdfpsController.this.hideDimLayer();
                                    }
                                    UdfpsController.this.mUdfpsViewVisibility = i3;
                                }
                            });
                        }
                    }
                }, 0L);
                this.mAccessibilityManager.addTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
                updateTouchListener();
                return;
            } catch (RuntimeException e) {
                Log.e("UdfpsController", "showUdfpsOverlay | failed to add window", e);
                return;
            }
        }
        Log.v("UdfpsController", "showUdfpsOverlay | the overlay is already showing");
    }

    private UdfpsAnimationViewController inflateUdfpsAnimation(int i) {
        if (i == 1 || i == 2) {
            UdfpsEnrollView udfpsEnrollView = (UdfpsEnrollView) this.mInflater.inflate(R$layout.udfps_enroll_view, (ViewGroup) null);
            this.mView.addView(udfpsEnrollView);
            Log.i("UdfpsController", "inflateUdfpsAnimation sensorLocationX=" + this.mSensorProps.sensorLocationX + ", sensorLocationY=" + this.mSensorProps.sensorLocationY + ", sensorRadius=" + this.mSensorProps.sensorRadius);
            updateInnerViewLayoutParams(udfpsEnrollView);
            this.mView.enableScanningAnim(false);
            udfpsEnrollView.updateSensorLocation(this.mSensorProps);
            return new UdfpsEnrollViewController(udfpsEnrollView, this.mServerRequest.mEnrollHelper, this.mStatusBarStateController, this.mStatusBar, this.mDumpManager);
        } else if (i == 3) {
            UdfpsBpView udfpsBpView = (UdfpsBpView) this.mInflater.inflate(R$layout.udfps_bp_view, (ViewGroup) null);
            this.mView.addView(udfpsBpView);
            updateInnerViewLayoutParams(udfpsBpView);
            this.mView.enableScanningAnim(false);
            return new UdfpsBpViewController(udfpsBpView, this.mStatusBarStateController, this.mStatusBar, this.mDumpManager);
        } else if (i == 4) {
            UdfpsKeyguardView udfpsKeyguardView = (UdfpsKeyguardView) this.mInflater.inflate(R$layout.udfps_keyguard_view, (ViewGroup) null);
            this.mView.addView(udfpsKeyguardView);
            updateInnerViewLayoutParams(udfpsKeyguardView);
            this.mView.enableScanningAnim(true);
            return new UdfpsKeyguardViewController(udfpsKeyguardView, this.mStatusBarStateController, this.mStatusBar, this.mKeyguardViewManager, this.mKeyguardUpdateMonitor, this.mDumpManager, this.mLockscreenShadeTransitionController, this.mConfigurationController, this.mSystemClock, this.mKeyguardStateController, this.mUnlockedScreenOffAnimationController, this);
        } else if (i == 5) {
            UdfpsFpmOtherView udfpsFpmOtherView = (UdfpsFpmOtherView) this.mInflater.inflate(R$layout.udfps_fpm_other_view, (ViewGroup) null);
            this.mView.addView(udfpsFpmOtherView);
            updateInnerViewLayoutParams(udfpsFpmOtherView);
            this.mView.enableScanningAnim(false);
            return new UdfpsFpmOtherViewController(udfpsFpmOtherView, this.mStatusBarStateController, this.mStatusBar, this.mDumpManager);
        } else {
            Log.d("UdfpsController", "Animation for reason " + i + " not supported yet");
            return null;
        }
    }

    private void hideUdfpsOverlay() {
        this.mExecution.assertIsMainThread();
        if (this.mView != null) {
            Log.v("UdfpsController", "hideUdfpsOverlay | removing window");
            onFingerUp();
            boolean isShowingAlternateAuth = this.mKeyguardViewManager.isShowingAlternateAuth();
            stopFpsBreatheAnim();
            Log.i("UdfpsController", "HBM stopHbm from hideUdfpsOverlay");
            this.mNTColorController.restoreDisplaySettingsIfNeeded();
            hideDimLayer();
            this.mView.setVisibilityListener(null);
            this.mWindowManager.removeView(this.mView);
            this.mView.setOnTouchListener(null);
            this.mView.setOnHoverListener(null);
            this.mView.setAnimationViewController(null);
            if (isShowingAlternateAuth) {
                this.mKeyguardViewManager.resetAlternateAuth(true);
            }
            this.mAccessibilityManager.removeTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
            this.mView = null;
        } else {
            Log.v("UdfpsController", "hideUdfpsOverlay | the overlay is already hidden");
        }
        this.mOrientationListener.disable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAodInterrupt(final int i, final int i2, final float f, final float f2) {
        Log.d("UdfpsController", "onAodInterrupt: mIsAodInterruptActive = " + this.mIsAodInterruptActive);
        if (!this.mIsAodInterruptActive && this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
            this.mAodInterruptRunnable = new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.this.lambda$onAodInterrupt$4(i, i2, f2, f);
                }
            };
            Log.d("UdfpsController", "onAodInterrupt: mScreenOn = " + this.mScreenOn);
            Runnable runnable = this.mAodInterruptRunnable;
            if (runnable == null) {
                return;
            }
            runnable.run();
            this.mAodInterruptRunnable = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAodInterrupt$4(int i, int i2, float f, float f2) {
        this.mIsAodInterruptActive = true;
        this.mCancelAodTimeoutAction = this.mFgExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                UdfpsController.this.onCancelUdfps();
            }
        }, 1000L);
        onFingerDown(i, i2, f, f2);
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCancelUdfps() {
        Log.d("UdfpsController", "onCancelUdfps: ");
        onFingerUp();
        if (!this.mIsAodInterruptActive) {
            return;
        }
        Runnable runnable = this.mCancelAodTimeoutAction;
        if (runnable != null) {
            runnable.run();
            this.mCancelAodTimeoutAction = null;
        }
        this.mIsAodInterruptActive = false;
    }

    public boolean isFingerDown() {
        return this.mOnFingerDown;
    }

    private void onFingerDown(final int i, final int i2, final float f, final float f2) {
        this.mExecution.assertIsMainThread();
        UdfpsView udfpsView = this.mView;
        if (udfpsView == null) {
            Log.w("UdfpsController", "Null view in onFingerDown");
            return;
        }
        boolean z = true;
        if ((udfpsView.getAnimationViewController() instanceof UdfpsKeyguardViewController) && !this.mStatusBarStateController.isDozing()) {
            this.mKeyguardBypassController.setUserHasDeviceEntryIntent(true);
        }
        if (!this.mOnFingerDown && !this.mKeyguardUpdateMonitor.isFaceDetectionRunning()) {
            this.mKeyguardUpdateMonitor.requestFaceAuth(false);
        }
        this.mOnFingerDown = true;
        Trace.endAsyncSection("UdfpsController.e2e.onPointerDown", 0);
        Trace.beginAsyncSection("UdfpsController.e2e.startIllumination", 0);
        final long currentTimeMillis = System.currentTimeMillis();
        Log.i("UdfpsController", "onFingerDown begin startT=" + currentTimeMillis);
        StringBuilder sb = new StringBuilder();
        sb.append("HBM isHbmEnabled=");
        UdfpsHbmProvider udfpsHbmProvider = this.mHbmProvider;
        if (udfpsHbmProvider == null || !udfpsHbmProvider.isHbmEnabled()) {
            z = false;
        }
        sb.append(z);
        Log.i("UdfpsController", sb.toString());
        Log.i("UdfpsController", "dozing = " + this.mStatusBarStateController.isDozing() + " screenOn = " + this.mScreenOn + " lowPowerState = " + ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isLowPowerState());
        if (shouldDozingFingerDownEnableHBM()) {
            ((StatusBar) Dependency.get(StatusBar.class)).setNotificationPanelViewAlpha(0.0f);
            this.mView.postDelayed(new AnonymousClass5(currentTimeMillis, i, i2, f, f2), 50L);
        } else {
            this.mView.startIlluminationNT(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.this.lambda$onFingerDown$5(currentTimeMillis, i, i2, f, f2);
                }
            });
        }
        for (Callback callback : this.mCallbacks) {
            callback.onFingerDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.biometrics.UdfpsController$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements Runnable {
        final /* synthetic */ float val$major;
        final /* synthetic */ float val$minor;
        final /* synthetic */ long val$startT;
        final /* synthetic */ int val$x;
        final /* synthetic */ int val$y;

        AnonymousClass5(long j, int i, int i2, float f, float f2) {
            this.val$startT = j;
            this.val$x = i;
            this.val$y = i2;
            this.val$minor = f;
            this.val$major = f2;
        }

        @Override // java.lang.Runnable
        public void run() {
            UdfpsView udfpsView = UdfpsController.this.mView;
            final long j = this.val$startT;
            final int i = this.val$x;
            final int i2 = this.val$y;
            final float f = this.val$minor;
            final float f2 = this.val$major;
            udfpsView.startIllumination(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.AnonymousClass5.this.lambda$run$0(j, i, i2, f, f2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(long j, int i, int i2, float f, float f2) {
            long currentTimeMillis = System.currentTimeMillis();
            Log.i("UdfpsController", "onFingerDown  endT=" + currentTimeMillis);
            Log.i("UdfpsController", "onFingerDown call onPointerDown===costTime=" + (currentTimeMillis - j));
            UdfpsController.this.mFingerprintManager.onPointerDown(UdfpsController.this.mSensorProps.sensorId, i, i2, f, f2);
            UdfpsController.this.mFingerprintManager.onUiReady(UdfpsController.this.mSensorProps.sensorId);
            Trace.endAsyncSection("UdfpsController.e2e.startIllumination", 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFingerDown$5(long j, int i, int i2, float f, float f2) {
        long currentTimeMillis = System.currentTimeMillis();
        Log.i("UdfpsController", "onFingerDown2  endT=" + currentTimeMillis);
        Log.i("UdfpsController", "onFingerDown2 call onPointerDown===costTime=" + (currentTimeMillis - j));
        int min = Math.min(i, i2);
        int max = Math.max(i, i2);
        Log.i("UdfpsController", "onFingerDown2 call onPointerDown===xx=" + min + ", yy=" + max);
        this.mFingerprintManager.onPointerDown(this.mSensorProps.sensorId, min, max, f, f2);
        this.mFingerprintManager.onUiReady(this.mSensorProps.sensorId);
        Trace.endAsyncSection("UdfpsController.e2e.startIllumination", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFingerUp() {
        this.mExecution.assertIsMainThread();
        this.mActivePointerId = -1;
        this.mGoodCaptureReceived = false;
        if (this.mView == null) {
            Log.w("UdfpsController", "Null view in onFingerUp");
            return;
        }
        if (this.mOnFingerDown) {
            Log.i("UdfpsController", "onFingerUp  endT=" + System.currentTimeMillis());
            this.mFingerprintManager.onPointerUp(this.mSensorProps.sensorId);
            for (Callback callback : this.mCallbacks) {
                callback.onFingerUp();
            }
        }
        this.mOnFingerDown = false;
        this.mView.stopIlluminationNT();
        if (!shouldDozingFingerDownEnableHBM()) {
            return;
        }
        this.mView.postDelayed(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController.6
            @Override // java.lang.Runnable
            public void run() {
                if (UdfpsController.this.mView != null) {
                    UdfpsController.this.mView.stopIllumination();
                }
                if (!((StatusBar) Dependency.get(StatusBar.class)).isWakeAndUnlock()) {
                    ((StatusBar) Dependency.get(StatusBar.class)).setNotificationPanelViewAlpha(1.0f);
                }
            }
        }, 50L);
    }

    private void updateTouchListener() {
        if (this.mView == null) {
            return;
        }
        if (this.mAccessibilityManager.isTouchExplorationEnabled()) {
            this.mView.setOnHoverListener(this.mOnHoverListener);
            this.mView.setOnTouchListener(null);
            return;
        }
        this.mView.setOnHoverListener(null);
        this.mView.setOnTouchListener(this.mOnTouchListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFpsBreatheAnim() {
        Handler handler = this.mHandler;
        if (handler == null) {
            return;
        }
        handler.removeCallbacks(this.startAnimRunnable);
        this.mHandler.postDelayed(this.startAnimRunnable, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopFpsBreatheAnim() {
        Handler handler = this.mHandler;
        if (handler == null) {
            return;
        }
        handler.removeCallbacks(this.startAnimRunnable);
        this.mHandler.post(this.stopAnimRunnable);
    }

    private void startHBM() {
        UdfpsHbmProvider udfpsHbmProvider = this.mHbmProvider;
        if (udfpsHbmProvider != null) {
            udfpsHbmProvider.enableHbm();
        }
    }

    private void stopHBM() {
        UdfpsHbmProvider udfpsHbmProvider = this.mHbmProvider;
        if (udfpsHbmProvider != null) {
            udfpsHbmProvider.disableHbm(null);
        }
    }

    public void closeHBM() {
        UdfpsHbmProvider udfpsHbmProvider = this.mHbmProvider;
        if (udfpsHbmProvider != null) {
            udfpsHbmProvider.disableHbm();
        }
    }

    public void doHBM(boolean z) {
        synchronized (this.mLock) {
            if (this.mHbmProvider != null) {
                Log.i("UdfpsController", "HBM doHBM open=" + z);
                if (z) {
                    startHBM();
                } else {
                    stopHBM();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldStartHBM() {
        boolean isUdfpsEnrolled = this.mKeyguardUpdateMonitor.isUdfpsEnrolled();
        boolean isDozing = this.mStatusBarStateController.isDozing();
        boolean z = true;
        boolean z2 = this.mStatusBarStateController.getState() == 2;
        boolean isKeyguardVisible = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isKeyguardVisible();
        boolean isDeviceInteractive = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isDeviceInteractive();
        boolean isGoingToSleep = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isGoingToSleep();
        if (this.mView == null || !isUdfpsEnrolled || isDozing || z2 || !isKeyguardVisible || !isDeviceInteractive || isGoingToSleep || this.mBouncerVisibility) {
            z = false;
        }
        Log.i("UdfpsController", "HBM shouldStartHBM:" + z + ",  hasFingerprintEnrolled=" + isUdfpsEnrolled + ",getState=" + this.mStatusBarStateController.getState() + ", isDozing =" + isDozing + ", statusBarShadeLocked=" + z2 + ", keyguardVisible=" + isKeyguardVisible + ", deviceInteractive=" + isDeviceInteractive + ", isGoingToSleep=" + isGoingToSleep + ", mBouncerVisibility=" + this.mBouncerVisibility + ", mView=" + this.mView);
        return z;
    }

    public void dismissFingerprintIcon() {
        Handler handler = this.mHandler;
        if (handler == null) {
            return;
        }
        handler.removeCallbacks(this.mShowFingerprintIconRunnable);
        this.mHandler.post(this.mDismissFingerprintIconRunnable);
    }

    public void showFingerprintIcon() {
        Handler handler = this.mHandler;
        if (handler == null) {
            return;
        }
        handler.removeCallbacks(this.mDismissFingerprintIconRunnable);
        this.mHandler.post(this.mShowFingerprintIconRunnable);
    }

    public boolean isFpIconVisible() {
        return this.mIsIconVisible;
    }

    public FingerprintSensorPropertiesInternal getSensorProps() {
        return this.mSensorProps;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDimLayer() {
        Log.i("UdfpsController", "showDimLayer mScreenOn=" + this.mScreenOn + ", isDozing=" + this.mStatusBarStateController.isDozing() + ", isGoingToSleep=" + this.mKeyguardUpdateMonitor.isGoingToSleep() + ", isKeyguardVisible=" + this.mKeyguardUpdateMonitor.isKeyguardVisible());
        if (!this.mScreenOn || this.mStatusBarStateController.isDozing()) {
            return;
        }
        if (!this.mKeyguardUpdateMonitor.isKeyguardVisible()) {
            this.mPendingShowDimlayer = true;
            this.mPendingShowDimlayerTime = System.currentTimeMillis();
            return;
        }
        drawDimLayer(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideDimLayer() {
        Log.i("UdfpsController", "hideDimLayer");
        this.mNTBrightnessController.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawDimLayer(boolean z) {
        Log.d("UdfpsController", "drawDimLayer, forceDrawDimlayer:" + z);
        this.mNTBrightnessController.drawDimLayer(z);
    }

    private void updateInnerViewLayoutParams(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        int i = this.mSensorProps.sensorRadius;
        layoutParams.width = i * 2;
        layoutParams.height = i * 2;
        layoutParams.topMargin = 147 - i;
        layoutParams.setMarginStart(147 - i);
        view.setLayoutParams(layoutParams);
    }

    public View getView() {
        return this.mView;
    }

    private boolean shouldDozingFingerDownEnableHBM() {
        return this.mStatusBarStateController.isDozing() && !((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isLowPowerState();
    }
}
