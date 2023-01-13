package com.android.systemui.statusbar.phone;

import android.content.res.Resources;
import android.hardware.biometrics.BiometricSourceType;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.nothing.systemui.util.NTLogUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.p026io.PrintWriter;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;

@SysUISingleton
public class BiometricUnlockController extends KeyguardUpdateMonitorCallback implements Dumpable {
    private static final float BIOMETRIC_COLLAPSE_SPEEDUP_FACTOR = 1.1f;
    private static final long BIOMETRIC_WAKELOCK_TIMEOUT_MS = 15000;
    private static final String BIOMETRIC_WAKE_LOCK_NAME = "wake-and-unlock:wakelock";
    private static final boolean DEBUG_BIO_WAKELOCK = true;
    private static final int FP_ATTEMPTS_BEFORE_SHOW_BOUNCER = 5;
    public static final int MODE_DISMISS_BOUNCER = 8;
    public static final int MODE_NONE = 0;
    public static final int MODE_ONLY_WAKE = 4;
    public static final int MODE_SHOW_BOUNCER = 3;
    public static final int MODE_UNLOCK_COLLAPSING = 5;
    public static final int MODE_UNLOCK_FADING = 7;
    public static final int MODE_WAKE_AND_UNLOCK = 1;
    public static final int MODE_WAKE_AND_UNLOCK_FROM_DREAM = 6;
    public static final int MODE_WAKE_AND_UNLOCK_PULSING = 2;
    private static final String TAG = "BiometricUnlockCtrl";
    private static final UiEventLogger UI_EVENT_LOGGER = new UiEventLoggerImpl();
    private final AuthController mAuthController;
    private BiometricModeListener mBiometricModeListener;
    private BiometricSourceType mBiometricType;
    private final int mConsecutiveFpFailureThreshold;
    private final DozeParameters mDozeParameters;
    private DozeScrimController mDozeScrimController;
    /* access modifiers changed from: private */
    public boolean mFadedAwayAfterWakeAndUnlock;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public boolean mHasScreenTurnedOnSinceAuthenticating;
    private final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardStateController mKeyguardStateController;
    private KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    private KeyguardViewController mKeyguardViewController;
    private KeyguardViewMediator mKeyguardViewMediator;
    private long mLastFpFailureUptimeMillis;
    private final LatencyTracker mLatencyTracker;
    private final NotificationMediaManager mMediaManager;
    private final MetricsLogger mMetricsLogger;
    private int mMode;
    /* access modifiers changed from: private */
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    private int mNumConsecutiveFpFailures;
    /* access modifiers changed from: private */
    public PendingAuthenticated mPendingAuthenticated = null;
    /* access modifiers changed from: private */
    public boolean mPendingShowBouncer;
    /* access modifiers changed from: private */
    public final PowerManager mPowerManager;
    private final Runnable mReleaseBiometricWakeLockRunnable = new Runnable() {
        public void run() {
            Log.i(BiometricUnlockController.TAG, "biometric wakelock: TIMEOUT!!");
            BiometricUnlockController.this.releaseBiometricWakeLock();
        }
    };
    private final ScreenLifecycle.Observer mScreenObserver;
    private final ScreenOffAnimationController mScreenOffAnimationController;
    private ScrimController mScrimController;
    private final SessionTracker mSessionTracker;
    private final ShadeController mShadeController;
    private final StatusBarStateController mStatusBarStateController;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private PowerManager.WakeLock mWakeLock;
    final WakefulnessLifecycle.Observer mWakefulnessObserver;

    public interface BiometricModeListener {
        void notifyBiometricAuthModeChanged();

        void onModeChanged(int i);

        void onResetMode();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WakeAndUnlockMode {
    }

    private static final class PendingAuthenticated {
        public final BiometricSourceType biometricSourceType;
        public final boolean isStrongBiometric;
        public final int userId;

        PendingAuthenticated(int i, BiometricSourceType biometricSourceType2, boolean z) {
            this.userId = i;
            this.biometricSourceType = biometricSourceType2;
            this.isStrongBiometric = z;
        }
    }

    public enum BiometricUiEvent implements UiEventLogger.UiEventEnum {
        BIOMETRIC_FINGERPRINT_SUCCESS(396),
        BIOMETRIC_FINGERPRINT_FAILURE(397),
        BIOMETRIC_FINGERPRINT_ERROR(398),
        BIOMETRIC_FACE_SUCCESS(399),
        BIOMETRIC_FACE_FAILURE(400),
        BIOMETRIC_FACE_ERROR(401),
        BIOMETRIC_IRIS_SUCCESS(402),
        BIOMETRIC_IRIS_FAILURE(HttpURLConnection.HTTP_FORBIDDEN),
        BIOMETRIC_IRIS_ERROR(HttpURLConnection.HTTP_NOT_FOUND),
        BIOMETRIC_BOUNCER_SHOWN(916);
        
        static final Map<BiometricSourceType, BiometricUiEvent> ERROR_EVENT_BY_SOURCE_TYPE = null;
        static final Map<BiometricSourceType, BiometricUiEvent> FAILURE_EVENT_BY_SOURCE_TYPE = null;
        static final Map<BiometricSourceType, BiometricUiEvent> SUCCESS_EVENT_BY_SOURCE_TYPE = null;
        private final int mId;

        static {
            BiometricUiEvent biometricUiEvent;
            BiometricUiEvent biometricUiEvent2;
            BiometricUiEvent biometricUiEvent3;
            BiometricUiEvent biometricUiEvent4;
            BiometricUiEvent biometricUiEvent5;
            BiometricUiEvent biometricUiEvent6;
            BiometricUiEvent biometricUiEvent7;
            BiometricUiEvent biometricUiEvent8;
            BiometricUiEvent biometricUiEvent9;
            ERROR_EVENT_BY_SOURCE_TYPE = Map.m1743of(BiometricSourceType.FINGERPRINT, biometricUiEvent3, BiometricSourceType.FACE, biometricUiEvent6, BiometricSourceType.IRIS, biometricUiEvent9);
            BiometricUiEvent biometricUiEvent10 = biometricUiEvent8;
            SUCCESS_EVENT_BY_SOURCE_TYPE = Map.m1743of(BiometricSourceType.FINGERPRINT, biometricUiEvent, BiometricSourceType.FACE, biometricUiEvent4, BiometricSourceType.IRIS, biometricUiEvent7);
            FAILURE_EVENT_BY_SOURCE_TYPE = Map.m1743of(BiometricSourceType.FINGERPRINT, biometricUiEvent2, BiometricSourceType.FACE, biometricUiEvent5, BiometricSourceType.IRIS, biometricUiEvent10);
        }

        private BiometricUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    @Inject
    public BiometricUnlockController(DozeScrimController dozeScrimController, KeyguardViewMediator keyguardViewMediator, ScrimController scrimController, ShadeController shadeController, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, @Main Resources resources, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters, MetricsLogger metricsLogger, DumpManager dumpManager, PowerManager powerManager, NotificationMediaManager notificationMediaManager, WakefulnessLifecycle wakefulnessLifecycle, ScreenLifecycle screenLifecycle, AuthController authController, StatusBarStateController statusBarStateController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SessionTracker sessionTracker, LatencyTracker latencyTracker, ScreenOffAnimationController screenOffAnimationController) {
        KeyguardBypassController keyguardBypassController2 = keyguardBypassController;
        C29154 r3 = new WakefulnessLifecycle.Observer() {
            public void onFinishedWakingUp() {
                if (BiometricUnlockController.this.mPendingShowBouncer) {
                    BiometricUnlockController.this.showBouncer();
                }
            }

            public void onStartedGoingToSleep() {
                BiometricUnlockController.this.resetMode();
                boolean unused = BiometricUnlockController.this.mFadedAwayAfterWakeAndUnlock = false;
                PendingAuthenticated unused2 = BiometricUnlockController.this.mPendingAuthenticated = null;
            }

            public void onFinishedGoingToSleep() {
                Trace.beginSection("BiometricUnlockController#onFinishedGoingToSleep");
                if (BiometricUnlockController.this.mPendingAuthenticated != null) {
                    BiometricUnlockController.this.mHandler.post(new BiometricUnlockController$4$$ExternalSyntheticLambda0(this, BiometricUnlockController.this.mPendingAuthenticated));
                    PendingAuthenticated unused = BiometricUnlockController.this.mPendingAuthenticated = null;
                }
                Trace.endSection();
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onFinishedGoingToSleep$0$com-android-systemui-statusbar-phone-BiometricUnlockController$4 */
            public /* synthetic */ void mo43717x7234ee5(PendingAuthenticated pendingAuthenticated) {
                BiometricUnlockController.this.onBiometricAuthenticated(pendingAuthenticated.userId, pendingAuthenticated.biometricSourceType, pendingAuthenticated.isStrongBiometric);
            }
        };
        this.mWakefulnessObserver = r3;
        C29165 r4 = new ScreenLifecycle.Observer() {
            public void onScreenTurnedOn() {
                boolean unused = BiometricUnlockController.this.mHasScreenTurnedOnSinceAuthenticating = true;
            }
        };
        this.mScreenObserver = r4;
        this.mPowerManager = powerManager;
        this.mShadeController = shadeController;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mDozeParameters = dozeParameters;
        keyguardUpdateMonitor.registerCallback(this);
        this.mMediaManager = notificationMediaManager;
        this.mLatencyTracker = latencyTracker;
        wakefulnessLifecycle.addObserver(r3);
        screenLifecycle.addObserver(r4);
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mDozeScrimController = dozeScrimController;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mScrimController = scrimController;
        this.mKeyguardStateController = keyguardStateController;
        this.mHandler = handler;
        Resources resources2 = resources;
        this.mConsecutiveFpFailureThreshold = resources.getInteger(C1894R.integer.fp_consecutive_failure_time_ms);
        this.mKeyguardBypassController = keyguardBypassController2;
        keyguardBypassController2.setUnlockController(this);
        this.mMetricsLogger = metricsLogger;
        this.mAuthController = authController;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mSessionTracker = sessionTracker;
        this.mScreenOffAnimationController = screenOffAnimationController;
        dumpManager.registerDumpable(getClass().getName(), this);
    }

    public void setKeyguardViewController(KeyguardViewController keyguardViewController) {
        this.mKeyguardViewController = keyguardViewController;
    }

    public void setBiometricModeListener(BiometricModeListener biometricModeListener) {
        this.mBiometricModeListener = biometricModeListener;
    }

    /* access modifiers changed from: private */
    public void releaseBiometricWakeLock() {
        if (this.mWakeLock != null) {
            this.mHandler.removeCallbacks(this.mReleaseBiometricWakeLockRunnable);
            Log.i(TAG, "releasing biometric wakelock");
            this.mWakeLock.release();
            this.mWakeLock = null;
        }
    }

    public void onBiometricAcquired(BiometricSourceType biometricSourceType, int i) {
        if (BiometricSourceType.FINGERPRINT == biometricSourceType && i != 0) {
            return;
        }
        if (BiometricSourceType.FACE != biometricSourceType || i == 0) {
            Trace.beginSection("BiometricUnlockController#onBiometricAcquired");
            releaseBiometricWakeLock();
            if (this.mStatusBarStateController.isDozing()) {
                if (this.mLatencyTracker.isEnabled()) {
                    this.mLatencyTracker.onActionStart(biometricSourceType == BiometricSourceType.FACE ? 7 : 2);
                }
                this.mWakeLock = this.mPowerManager.newWakeLock(1, BIOMETRIC_WAKE_LOCK_NAME);
                Trace.beginSection("acquiring wake-and-unlock");
                this.mWakeLock.acquire();
                Trace.endSection();
                Log.i(TAG, "biometric acquired, grabbing biometric wakelock");
                this.mHandler.postDelayed(this.mReleaseBiometricWakeLockRunnable, BIOMETRIC_WAKELOCK_TIMEOUT_MS);
            }
            Trace.endSection();
        }
    }

    private boolean pulsingOrAod() {
        ScrimState state = this.mScrimController.getState();
        return state == ScrimState.AOD || state == ScrimState.PULSING;
    }

    public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
        Trace.beginSection("BiometricUnlockController#onBiometricAuthenticated");
        if (this.mUpdateMonitor.isGoingToSleep()) {
            this.mPendingAuthenticated = new PendingAuthenticated(i, biometricSourceType, z);
            Trace.endSection();
            return;
        }
        this.mBiometricType = biometricSourceType;
        this.mMetricsLogger.write(new LogMaker(1697).setType(10).setSubtype(toSubtype(biometricSourceType)));
        Optional.ofNullable(BiometricUiEvent.SUCCESS_EVENT_BY_SOURCE_TYPE.get(biometricSourceType)).ifPresent(new BiometricUnlockController$$ExternalSyntheticLambda3(this));
        if (this.mKeyguardStateController.isOccluded() || this.mKeyguardBypassController.onBiometricAuthenticated(biometricSourceType, z)) {
            this.mKeyguardViewMediator.userActivity();
            startWakeAndUnlock(biometricSourceType, z);
            return;
        }
        Log.d(TAG, "onBiometricAuthenticated aborted by bypass controller");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onBiometricAuthenticated$0$com-android-systemui-statusbar-phone-BiometricUnlockController */
    public /* synthetic */ void mo43708xa5f887f2(BiometricUiEvent biometricUiEvent) {
        UI_EVENT_LOGGER.log(biometricUiEvent, getSessionId());
    }

    public void startWakeAndUnlock(BiometricSourceType biometricSourceType, boolean z) {
        startWakeAndUnlock(calculateMode(biometricSourceType, z));
    }

    public void startWakeAndUnlock(int i) {
        Log.v(TAG, "startWakeAndUnlock(" + i + NavigationBarInflaterView.KEY_CODE_END);
        boolean isDeviceInteractive = this.mUpdateMonitor.isDeviceInteractive();
        this.mMode = i;
        this.mHasScreenTurnedOnSinceAuthenticating = false;
        if (i == 2 && pulsingOrAod()) {
            this.mNotificationShadeWindowController.setForceDozeBrightness(true);
        }
        this.mDozeParameters.getAlwaysOn();
        BiometricUnlockController$$ExternalSyntheticLambda2 biometricUnlockController$$ExternalSyntheticLambda2 = new BiometricUnlockController$$ExternalSyntheticLambda2(this, isDeviceInteractive);
        if (this.mMode != 0) {
            biometricUnlockController$$ExternalSyntheticLambda2.run();
        }
        int i2 = this.mMode;
        switch (i2) {
            case 1:
            case 2:
            case 6:
                if (i2 == 2) {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK_PULSING");
                    this.mMediaManager.updateMediaMetaData(false, true);
                } else if (i2 == 1) {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK");
                } else {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK_FROM_DREAM");
                    this.mUpdateMonitor.awakenFromDream();
                }
                this.mNotificationShadeWindowController.setNotificationShadeFocusable(false);
                this.mKeyguardViewMediator.onWakeAndUnlocking();
                Trace.endSection();
                break;
            case 3:
                Trace.beginSection("MODE_SHOW_BOUNCER");
                if (!isDeviceInteractive) {
                    this.mPendingShowBouncer = true;
                } else {
                    showBouncer();
                }
                Trace.endSection();
                break;
            case 5:
                Trace.beginSection("MODE_UNLOCK_COLLAPSING");
                if (!isDeviceInteractive) {
                    this.mPendingShowBouncer = true;
                } else {
                    if (!this.mKeyguardUnlockAnimationController.willHandleUnlockAnimation()) {
                        this.mShadeController.animateCollapsePanels(0, true, false, BIOMETRIC_COLLAPSE_SPEEDUP_FACTOR);
                    }
                    this.mPendingShowBouncer = false;
                    this.mKeyguardViewController.notifyKeyguardAuthenticated(false);
                }
                Trace.endSection();
                break;
            case 7:
            case 8:
                Trace.beginSection("MODE_DISMISS_BOUNCER or MODE_UNLOCK_FADING");
                this.mKeyguardViewController.notifyKeyguardAuthenticated(false);
                Trace.endSection();
                break;
        }
        onModeChanged(this.mMode);
        BiometricModeListener biometricModeListener = this.mBiometricModeListener;
        if (biometricModeListener != null) {
            biometricModeListener.notifyBiometricAuthModeChanged();
        }
        Trace.endSection();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startWakeAndUnlock$1$com-android-systemui-statusbar-phone-BiometricUnlockController */
    public /* synthetic */ void mo43710x883d0792(boolean z) {
        if (!z) {
            Log.i(TAG, "bio wakelock: Authenticated, waking up...");
            this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "android.policy:BIOMETRIC");
        }
        Trace.beginSection("release wake-and-unlock");
        releaseBiometricWakeLock();
        Trace.endSection();
    }

    private void onModeChanged(int i) {
        BiometricModeListener biometricModeListener = this.mBiometricModeListener;
        if (biometricModeListener != null) {
            biometricModeListener.onModeChanged(i);
        }
    }

    /* access modifiers changed from: private */
    public void showBouncer() {
        if (this.mMode == 3) {
            this.mKeyguardViewController.showBouncer(true);
        }
        this.mShadeController.animateCollapsePanels(0, true, false, BIOMETRIC_COLLAPSE_SPEEDUP_FACTOR);
        this.mPendingShowBouncer = false;
    }

    public boolean hasPendingAuthentication() {
        PendingAuthenticated pendingAuthenticated = this.mPendingAuthenticated;
        return pendingAuthenticated != null && this.mUpdateMonitor.isUnlockingWithBiometricAllowed(pendingAuthenticated.isStrongBiometric) && this.mPendingAuthenticated.userId == KeyguardUpdateMonitor.getCurrentUser();
    }

    public int getMode() {
        return this.mMode;
    }

    private int calculateMode(BiometricSourceType biometricSourceType, boolean z) {
        if (biometricSourceType == BiometricSourceType.FACE || biometricSourceType == BiometricSourceType.IRIS) {
            return calculateModeForPassiveAuth(z);
        }
        return calculateModeForFingerprint(z);
    }

    private int calculateModeForFingerprint(boolean z) {
        boolean isUnlockingWithBiometricAllowed = this.mUpdateMonitor.isUnlockingWithBiometricAllowed(z);
        boolean isDreaming = this.mUpdateMonitor.isDreaming();
        if (!this.mUpdateMonitor.isDeviceInteractive()) {
            if (this.mKeyguardViewController.isShowing() || this.mScreenOffAnimationController.isKeyguardShowDelayed()) {
                if (this.mDozeScrimController.isPulsing() && isUnlockingWithBiometricAllowed) {
                    return 2;
                }
                if (isUnlockingWithBiometricAllowed || !this.mKeyguardStateController.isMethodSecure()) {
                    return 1;
                }
                return 3;
            } else if (this.mKeyguardStateController.isUnlocked()) {
                return 1;
            } else {
                return 4;
            }
        } else if (isUnlockingWithBiometricAllowed && isDreaming) {
            return 6;
        } else {
            if (!this.mKeyguardViewController.isShowing()) {
                return 0;
            }
            if (this.mKeyguardViewController.bouncerIsOrWillBeShowing() && isUnlockingWithBiometricAllowed) {
                return 8;
            }
            if (isUnlockingWithBiometricAllowed) {
                return 5;
            }
            if (!this.mKeyguardViewController.isBouncerShowing()) {
                return 3;
            }
            return 0;
        }
    }

    private int calculateModeForPassiveAuth(boolean z) {
        boolean isUnlockingWithBiometricAllowed = this.mUpdateMonitor.isUnlockingWithBiometricAllowed(z);
        boolean isDreaming = this.mUpdateMonitor.isDreaming();
        boolean z2 = this.mKeyguardBypassController.getBypassEnabled() || this.mKeyguardBypassController.getUserHasDeviceEntryIntent();
        if (!this.mUpdateMonitor.isDeviceInteractive()) {
            if (!this.mKeyguardViewController.isShowing()) {
                if (z2) {
                    return 1;
                }
                return 4;
            } else if (!isUnlockingWithBiometricAllowed) {
                if (z2) {
                    return 3;
                }
                return 0;
            } else if (this.mDozeScrimController.isPulsing()) {
                if (z2) {
                    return 2;
                }
                return 4;
            } else if (z2) {
                return 2;
            } else {
                return 4;
            }
        } else if (!isUnlockingWithBiometricAllowed || !isDreaming) {
            if (isUnlockingWithBiometricAllowed && this.mKeyguardStateController.isOccluded()) {
                return 5;
            }
            if (!this.mKeyguardViewController.isShowing()) {
                return 0;
            }
            if ((this.mKeyguardViewController.bouncerIsOrWillBeShowing() || this.mKeyguardBypassController.getAltBouncerShowing()) && isUnlockingWithBiometricAllowed) {
                if (!z2 || !this.mKeyguardBypassController.canPlaySubtleWindowAnimations()) {
                    return 8;
                }
                return 7;
            } else if (isUnlockingWithBiometricAllowed) {
                if (z2 || this.mAuthController.isUdfpsFingerDown()) {
                    return 7;
                }
                return 0;
            } else if (z2) {
                return 3;
            } else {
                return 0;
            }
        } else if (z2) {
            return 6;
        } else {
            return 4;
        }
    }

    public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
        this.mMetricsLogger.write(new LogMaker(1697).setType(11).setSubtype(toSubtype(biometricSourceType)));
        Optional.ofNullable(BiometricUiEvent.FAILURE_EVENT_BY_SOURCE_TYPE.get(biometricSourceType)).ifPresent(new BiometricUnlockController$$ExternalSyntheticLambda1(this));
        if (this.mLatencyTracker.isEnabled()) {
            this.mLatencyTracker.onActionCancel(biometricSourceType == BiometricSourceType.FACE ? 7 : 2);
        }
        if (biometricSourceType == BiometricSourceType.FINGERPRINT && this.mUpdateMonitor.isUdfpsSupported()) {
            long uptimeMillis = SystemClock.uptimeMillis();
            if (uptimeMillis - this.mLastFpFailureUptimeMillis < ((long) this.mConsecutiveFpFailureThreshold)) {
                this.mNumConsecutiveFpFailures++;
            } else {
                this.mNumConsecutiveFpFailures = 1;
            }
            this.mLastFpFailureUptimeMillis = uptimeMillis;
            if (this.mNumConsecutiveFpFailures >= 5) {
                startWakeAndUnlock(3);
                UI_EVENT_LOGGER.log(BiometricUiEvent.BIOMETRIC_BOUNCER_SHOWN, getSessionId());
                this.mNumConsecutiveFpFailures = 0;
            }
        }
        cleanup();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onBiometricAuthFailed$2$com-android-systemui-statusbar-phone-BiometricUnlockController */
    public /* synthetic */ void mo43707xf000d62(BiometricUiEvent biometricUiEvent) {
        UI_EVENT_LOGGER.log(biometricUiEvent, getSessionId());
    }

    public void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
        this.mMetricsLogger.write(new LogMaker(1697).setType(15).setSubtype(toSubtype(biometricSourceType)).addTaggedData(1741, Integer.valueOf(i)));
        Optional.ofNullable(BiometricUiEvent.ERROR_EVENT_BY_SOURCE_TYPE.get(biometricSourceType)).ifPresent(new BiometricUnlockController$$ExternalSyntheticLambda0(this));
        if (biometricSourceType == BiometricSourceType.FINGERPRINT && ((i == 7 || i == 9) && this.mUpdateMonitor.isUdfpsSupported() && (this.mStatusBarStateController.getState() == 0 || this.mStatusBarStateController.getState() == 2))) {
            startWakeAndUnlock(3);
            UI_EVENT_LOGGER.log(BiometricUiEvent.BIOMETRIC_BOUNCER_SHOWN, getSessionId());
        }
        cleanup();
        if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
            this.mAuthController.showDimLayer(false, "onBiometricError");
            if (i != 5) {
                showBouncer();
            }
            NTLogUtil.m1688i(TAG, "onBiometricError isInteractive=" + this.mPowerManager.isInteractive() + " errString:" + str + " msgId:" + i);
            if (!this.mPowerManager.isInteractive()) {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        BiometricUnlockController.this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 0, "com.android.systemui:BOUNCER");
                    }
                }, 260);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onBiometricError$3$com-android-systemui-statusbar-phone-BiometricUnlockController */
    public /* synthetic */ void mo43709x436e94d6(BiometricUiEvent biometricUiEvent) {
        UI_EVENT_LOGGER.log(biometricUiEvent, getSessionId());
    }

    private void cleanup() {
        releaseBiometricWakeLock();
    }

    public void startKeyguardFadingAway() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                BiometricUnlockController.this.mNotificationShadeWindowController.setForceDozeBrightness(false);
            }
        }, 96);
    }

    public void finishKeyguardFadingAway() {
        if (isWakeAndUnlock()) {
            this.mFadedAwayAfterWakeAndUnlock = true;
        }
        resetMode();
    }

    /* access modifiers changed from: private */
    public void resetMode() {
        this.mMode = 0;
        this.mBiometricType = null;
        this.mNotificationShadeWindowController.setForceDozeBrightness(false);
        BiometricModeListener biometricModeListener = this.mBiometricModeListener;
        if (biometricModeListener != null) {
            biometricModeListener.onResetMode();
            this.mBiometricModeListener.notifyBiometricAuthModeChanged();
        }
        this.mNumConsecutiveFpFailures = 0;
        this.mLastFpFailureUptimeMillis = 0;
    }

    public boolean hasScreenTurnedOnSinceAuthenticating() {
        return this.mHasScreenTurnedOnSinceAuthenticating;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(" BiometricUnlockController:");
        printWriter.print("   mMode=");
        printWriter.println(this.mMode);
        printWriter.print("   mWakeLock=");
        printWriter.println((Object) this.mWakeLock);
        if (this.mUpdateMonitor.isUdfpsSupported()) {
            printWriter.print("   mNumConsecutiveFpFailures=");
            printWriter.println(this.mNumConsecutiveFpFailures);
            printWriter.print("   time since last failure=");
            printWriter.println(SystemClock.uptimeMillis() - this.mLastFpFailureUptimeMillis);
        }
    }

    public boolean isWakeAndUnlock() {
        int i = this.mMode;
        return i == 1 || i == 2 || i == 6;
    }

    public boolean unlockedByWakeAndUnlock() {
        return isWakeAndUnlock() || this.mFadedAwayAfterWakeAndUnlock;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r1.mMode;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBiometricUnlock() {
        /*
            r1 = this;
            boolean r0 = r1.isWakeAndUnlock()
            if (r0 != 0) goto L_0x0011
            int r1 = r1.mMode
            r0 = 5
            if (r1 == r0) goto L_0x0011
            r0 = 7
            if (r1 != r0) goto L_0x000f
            goto L_0x0011
        L_0x000f:
            r1 = 0
            goto L_0x0012
        L_0x0011:
            r1 = 1
        L_0x0012:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.BiometricUnlockController.isBiometricUnlock():boolean");
    }

    public BiometricSourceType getBiometricType() {
        return this.mBiometricType;
    }

    private InstanceId getSessionId() {
        return this.mSessionTracker.getSessionId(1);
    }

    /* renamed from: com.android.systemui.statusbar.phone.BiometricUnlockController$6 */
    static /* synthetic */ class C29176 {
        static final /* synthetic */ int[] $SwitchMap$android$hardware$biometrics$BiometricSourceType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                android.hardware.biometrics.BiometricSourceType[] r0 = android.hardware.biometrics.BiometricSourceType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$hardware$biometrics$BiometricSourceType = r0
                android.hardware.biometrics.BiometricSourceType r1 = android.hardware.biometrics.BiometricSourceType.FINGERPRINT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$android$hardware$biometrics$BiometricSourceType     // Catch:{ NoSuchFieldError -> 0x001d }
                android.hardware.biometrics.BiometricSourceType r1 = android.hardware.biometrics.BiometricSourceType.FACE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$android$hardware$biometrics$BiometricSourceType     // Catch:{ NoSuchFieldError -> 0x0028 }
                android.hardware.biometrics.BiometricSourceType r1 = android.hardware.biometrics.BiometricSourceType.IRIS     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.BiometricUnlockController.C29176.<clinit>():void");
        }
    }

    private int toSubtype(BiometricSourceType biometricSourceType) {
        int i = C29176.$SwitchMap$android$hardware$biometrics$BiometricSourceType[biometricSourceType.ordinal()];
        if (i == 1) {
            return 0;
        }
        if (i != 2) {
            return i != 3 ? 3 : 2;
        }
        return 1;
    }
}
