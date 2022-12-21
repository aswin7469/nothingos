package com.android.systemui.classifier;

import android.hardware.biometrics.BiometricSourceType;
import android.view.MotionEvent;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.android.systemui.util.time.SystemClock;
import java.util.Collections;
import java.util.Objects;
import javax.inject.Inject;

@SysUISingleton
class FalsingCollectorImpl implements FalsingCollector {
    private static final boolean DEBUG = false;
    private static final long GESTURE_PROCESSING_DELAY_MS = 100;
    private static final String PROXIMITY_SENSOR_TAG = "FalsingManager";
    private static final String TAG = "FalsingManager";
    private boolean mAvoidGesture;
    /* access modifiers changed from: private */
    public final BatteryController mBatteryController;
    private final BatteryController.BatteryStateChangeCallback mBatteryListener;
    private final DockManager.DockEventListener mDockEventListener;
    /* access modifiers changed from: private */
    public final DockManager mDockManager;
    /* access modifiers changed from: private */
    public final FalsingDataProvider mFalsingDataProvider;
    private final FalsingManager mFalsingManager;
    private final HistoryTracker mHistoryTracker;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateCallback;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final DelayableExecutor mMainExecutor;
    private MotionEvent mPendingDownEvent;
    /* access modifiers changed from: private */
    public final ProximitySensor mProximitySensor;
    private boolean mScreenOn;
    private final ThresholdSensor.Listener mSensorEventListener = new FalsingCollectorImpl$$ExternalSyntheticLambda0(this);
    private boolean mSessionStarted;
    private boolean mShowingAod;
    /* access modifiers changed from: private */
    public int mState;
    private final StatusBarStateController mStatusBarStateController;
    private final StatusBarStateController.StateListener mStatusBarStateListener;
    private final SystemClock mSystemClock;

    static void logDebug(String str, Throwable th) {
    }

    public boolean isReportingEnabled() {
        return false;
    }

    public void onAffordanceSwipingAborted() {
    }

    public void onAffordanceSwipingStarted(boolean z) {
    }

    public void onCameraHintStarted() {
    }

    public void onCameraOn() {
    }

    public void onExpansionFromPulseStopped() {
    }

    public void onLeftAffordanceHintStarted() {
    }

    public void onLeftAffordanceOn() {
    }

    public void onNotificationActive() {
    }

    public void onNotificationDismissed() {
    }

    public void onNotificationDoubleTap(boolean z, float f, float f2) {
    }

    public void onNotificationStartDismissing() {
    }

    public void onNotificationStartDraggingDown() {
    }

    public void onNotificationStopDismissing() {
    }

    public void onNotificationStopDraggingDown() {
    }

    public void onQsDown() {
    }

    public void onStartExpandingFromPulse() {
    }

    public void onTrackingStarted(boolean z) {
    }

    public void onTrackingStopped() {
    }

    public void onUnlockHintStarted() {
    }

    public void setNotificationExpanded() {
    }

    public boolean shouldEnforceBouncer() {
        return false;
    }

    @Inject
    FalsingCollectorImpl(FalsingDataProvider falsingDataProvider, FalsingManager falsingManager, KeyguardUpdateMonitor keyguardUpdateMonitor, HistoryTracker historyTracker, ProximitySensor proximitySensor, StatusBarStateController statusBarStateController, KeyguardStateController keyguardStateController, BatteryController batteryController, DockManager dockManager, @Main DelayableExecutor delayableExecutor, SystemClock systemClock) {
        C20081 r0 = new StatusBarStateController.StateListener() {
            public void onStateChanged(int i) {
                FalsingCollectorImpl.logDebug("StatusBarState=" + StatusBarState.toString(i));
                int unused = FalsingCollectorImpl.this.mState = i;
                FalsingCollectorImpl.this.updateSessionActive();
            }
        };
        this.mStatusBarStateListener = r0;
        C20092 r1 = new KeyguardUpdateMonitorCallback() {
            public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
                if (i == KeyguardUpdateMonitor.getCurrentUser() && biometricSourceType == BiometricSourceType.FACE) {
                    FalsingCollectorImpl.this.mFalsingDataProvider.setJustUnlockedWithFace(true);
                }
            }
        };
        this.mKeyguardUpdateCallback = r1;
        C20103 r2 = new BatteryController.BatteryStateChangeCallback() {
            public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            }

            public void onWirelessChargingChanged(boolean z) {
                if (z || FalsingCollectorImpl.this.mDockManager.isDocked()) {
                    FalsingCollectorImpl.this.mProximitySensor.pause();
                } else {
                    FalsingCollectorImpl.this.mProximitySensor.resume();
                }
            }
        };
        this.mBatteryListener = r2;
        C20114 r3 = new DockManager.DockEventListener() {
            public void onEvent(int i) {
                if (i != 0 || FalsingCollectorImpl.this.mBatteryController.isWirelessCharging()) {
                    FalsingCollectorImpl.this.mProximitySensor.pause();
                } else {
                    FalsingCollectorImpl.this.mProximitySensor.resume();
                }
            }
        };
        this.mDockEventListener = r3;
        this.mFalsingDataProvider = falsingDataProvider;
        this.mFalsingManager = falsingManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mHistoryTracker = historyTracker;
        this.mProximitySensor = proximitySensor;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mBatteryController = batteryController;
        this.mDockManager = dockManager;
        this.mMainExecutor = delayableExecutor;
        this.mSystemClock = systemClock;
        proximitySensor.setTag("FalsingManager");
        proximitySensor.setDelay(1);
        statusBarStateController.addCallback(r0);
        this.mState = statusBarStateController.getState();
        keyguardUpdateMonitor.registerCallback(r1);
        batteryController.addCallback(r2);
        dockManager.addListener(r3);
    }

    public void onSuccessfulUnlock() {
        this.mFalsingManager.onSuccessfulUnlock();
        sessionEnd();
    }

    public void setShowingAod(boolean z) {
        this.mShowingAod = z;
        updateSessionActive();
    }

    public void setQsExpanded(boolean z) {
        if (z) {
            unregisterSensors();
        } else if (this.mSessionStarted) {
            registerSensors();
        }
    }

    public void onScreenOnFromTouch() {
        onScreenTurningOn();
    }

    public void onScreenTurningOn() {
        this.mScreenOn = true;
        updateSessionActive();
    }

    public void onScreenOff() {
        this.mScreenOn = false;
        updateSessionActive();
    }

    public void onBouncerShown() {
        unregisterSensors();
    }

    public void onBouncerHidden() {
        if (this.mSessionStarted) {
            registerSensors();
        }
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        if (!this.mKeyguardStateController.isShowing() || (this.mStatusBarStateController.isDozing() && !this.mStatusBarStateController.isPulsing())) {
            avoidGesture();
        } else if (motionEvent.getActionMasked() != 4) {
            if (motionEvent.getActionMasked() == 0) {
                this.mPendingDownEvent = MotionEvent.obtain(motionEvent);
                this.mAvoidGesture = false;
            } else if (!this.mAvoidGesture) {
                MotionEvent motionEvent2 = this.mPendingDownEvent;
                if (motionEvent2 != null) {
                    this.mFalsingDataProvider.onMotionEvent(motionEvent2);
                    this.mPendingDownEvent.recycle();
                    this.mPendingDownEvent = null;
                }
                this.mFalsingDataProvider.onMotionEvent(motionEvent);
            }
        }
    }

    public void onMotionEventComplete() {
        DelayableExecutor delayableExecutor = this.mMainExecutor;
        FalsingDataProvider falsingDataProvider = this.mFalsingDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        delayableExecutor.executeDelayed(new FalsingCollectorImpl$$ExternalSyntheticLambda1(falsingDataProvider), 100);
    }

    public void avoidGesture() {
        this.mAvoidGesture = true;
        MotionEvent motionEvent = this.mPendingDownEvent;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mPendingDownEvent = null;
        }
    }

    public void cleanup() {
        unregisterSensors();
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mBatteryController.removeCallback(this.mBatteryListener);
        this.mDockManager.removeListener(this.mDockEventListener);
    }

    public void updateFalseConfidence(FalsingClassifier.Result result) {
        this.mHistoryTracker.addResults(Collections.singleton(result), this.mSystemClock.uptimeMillis());
    }

    private boolean shouldSessionBeActive() {
        return this.mScreenOn && this.mState == 1 && !this.mShowingAod;
    }

    /* access modifiers changed from: private */
    public void updateSessionActive() {
        if (shouldSessionBeActive()) {
            sessionStart();
        } else {
            sessionEnd();
        }
    }

    private void sessionStart() {
        if (!this.mSessionStarted && shouldSessionBeActive()) {
            logDebug("Starting Session");
            this.mSessionStarted = true;
            this.mFalsingDataProvider.setJustUnlockedWithFace(false);
            registerSensors();
            this.mFalsingDataProvider.onSessionStarted();
        }
    }

    private void sessionEnd() {
        if (this.mSessionStarted) {
            logDebug("Ending Session");
            this.mSessionStarted = false;
            unregisterSensors();
            this.mFalsingDataProvider.onSessionEnd();
        }
    }

    private void registerSensors() {
        this.mProximitySensor.register(this.mSensorEventListener);
    }

    private void unregisterSensors() {
        this.mProximitySensor.unregister(this.mSensorEventListener);
    }

    /* access modifiers changed from: private */
    public void onProximityEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mFalsingManager.onProximityEvent(new ProximityEventImpl(thresholdSensorEvent));
    }

    static void logDebug(String str) {
        logDebug(str, (Throwable) null);
    }

    private static class ProximityEventImpl implements FalsingManager.ProximityEvent {
        private ThresholdSensorEvent mThresholdSensorEvent;

        ProximityEventImpl(ThresholdSensorEvent thresholdSensorEvent) {
            this.mThresholdSensorEvent = thresholdSensorEvent;
        }

        public boolean getCovered() {
            return this.mThresholdSensorEvent.getBelow();
        }

        public long getTimestampNs() {
            return this.mThresholdSensorEvent.getTimestampNs();
        }
    }
}
