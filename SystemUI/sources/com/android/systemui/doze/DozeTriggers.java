package com.android.systemui.doze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Debug;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.SurfaceControl;
import androidx.core.app.NotificationCompat;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.Dependency;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.dagger.DozeScope;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.biometrics.NTColorController;
import com.nothing.systemui.doze.AODController;
import com.nothing.systemui.keyguard.KeyguardUpdateMonitorEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javax.inject.Inject;

@DozeScope
public class DozeTriggers implements DozeMachine.Part {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = DozeService.DEBUG;
    public static final String NT_TURN_OFF_INVERT_OR_AOD = "turn_off_invert_or_aod";
    private static final int PROXIMITY_TIMEOUT_DELAY_MS = 500;
    private static final String PULSE_ACTION = "com.android.systemui.doze.pulse";
    private static final String TAG = "DozeTriggers";
    private static boolean sWakeDisplaySensorState = true;
    private final String DELAY_DISMISS_FINGERPRINT = "com.nothingos.aod.dismiss.fingerprint";
    private final String ENTER_SLEEP_MODE_ACTION = "com.nothingos.aod.sleep.enter";
    private final String EXIT_SLEEP_MODE_ACTION = "com.nothingos.aod.sleep.exit";
    private final int NO_CLOSE = 0;
    private final int TURN_OFF_INVERT = 1;
    private final int TURN_OFF_INVERT_AND_AOD = 2;
    private AlarmManager mAlarmManager;
    private final boolean mAllowPulseTriggers;
    /* access modifiers changed from: private */
    public Runnable mAodInterruptRunnable;
    /* access modifiers changed from: private */
    public final AuthController mAuthController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final TriggerReceiver mBroadcastReceiver = new TriggerReceiver();
    private final AmbientDisplayConfiguration mConfig;
    private final Context mContext;
    private PendingIntent mDelayDismissFpPendingIntent;
    private final DevicePostureController.Callback mDevicePostureCallback = new DozeTriggers$$ExternalSyntheticLambda6();
    private final DevicePostureController mDevicePostureController;
    private final DockEventListener mDockEventListener = new DockEventListener();
    private final DockManager mDockManager;
    private final DozeHost mDozeHost;
    private final DozeLog mDozeLog;
    private final DozeParameters mDozeParameters;
    /* access modifiers changed from: private */
    public final DozeSensors mDozeSensors;
    private PendingIntent mEnterSleepModePendingIntent;
    private PendingIntent mExitSleepModePendingIntent;
    private DozeHost.Callback mHostCallback = new DozeHost.Callback() {
        public void onNotificationAlerted(Runnable runnable) {
            DozeTriggers.this.onNotification(runnable);
        }

        public void onLiftWake() {
            Log.d(DozeTriggers.TAG, "onLiftWake: ");
            DozeTriggers.this.onSensor(3, -1.0f, -1.0f, new float[]{1.0f});
        }

        public void onMotion() {
            Log.d(DozeTriggers.TAG, "onMotion: ");
            DozeTriggers.this.onSensor(2, -1.0f, -1.0f, new float[]{1.0f});
        }

        public void onDozeFingerprintDown() {
            Log.d(DozeTriggers.TAG, "onDozeFingerprintDown: ");
            DozeMachine.State state = DozeTriggers.this.mMachine.getState();
            Log.d(DozeTriggers.TAG, "onDozeFingerprintDown: state = " + state);
            Log.d(DozeTriggers.TAG, "onDozeFingerprintDown: running = " + ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFingerprintDetectionRunning());
            if (!DozeTriggers.this.mAuthController.isFpIconVisible()) {
                DozeTriggers.this.mAuthController.showFingerprintIcon();
            }
            if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFingerprintDetectionRunning()) {
                DozeTriggers.this.cancelDelayDismissFingerprint();
                Runnable unused = DozeTriggers.this.mAodInterruptRunnable = new DozeTriggers$1$$ExternalSyntheticLambda0(this);
                if (state == DozeMachine.State.DOZE_AOD_PAUSING || state == DozeMachine.State.DOZE_AOD_PAUSED || state == DozeMachine.State.DOZE) {
                    DozeTriggers.this.switchAODPowerMode(1);
                    DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
                } else {
                    DozeTriggers.this.mAodInterruptRunnable.run();
                    Runnable unused2 = DozeTriggers.this.mAodInterruptRunnable = null;
                }
            }
            DozeTriggers.this.delayDismissFingerprint();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDozeFingerprintDown$0$com-android-systemui-doze-DozeTriggers$1 */
        public /* synthetic */ void mo32472x2e7930d9() {
            Log.d(DozeTriggers.TAG, "onDozeFingerprintDown: off finger auth");
            DozeTriggers.this.mAuthController.showDimLayer(true, "onDozeFingerprintDown");
            RectF sensorRectF = DozeTriggers.this.mAuthController.getSensorRectF();
            DozeTriggers.this.mAuthController.onAodInterrupt((int) sensorRectF.centerX(), (int) sensorRectF.centerY(), sensorRectF.width(), sensorRectF.height());
        }

        public void onDozeFingerprintUp() {
            Log.d(DozeTriggers.TAG, "onDozeFingerprintUp: ");
            DozeTriggers.this.mAuthController.onCancelUdfps();
            DozeTriggers.this.mAuthController.showDimLayer(false, "onDozeFingerprintUp");
        }

        public void onDozeFingerprintRunningStateChanged() {
            Log.d(DozeTriggers.TAG, "onDozeFingerprintRunningStateChanged: ");
            DozeTriggers.this.cancelDelayDismissFingerprint();
            DozeTriggers.this.delayDismissFingerprint();
        }

        public void onTapWakeUp() {
            Log.d(DozeTriggers.TAG, "onTapWakeUp: ");
            DozeTriggers.this.onSensor(9, -1.0f, -1.0f, new float[]{1.0f});
        }
    };
    private final KeyguardStateController mKeyguardStateController;
    /* access modifiers changed from: private */
    public DozeMachine mMachine;
    private final DelayableExecutor mMainExecutor;
    /* access modifiers changed from: private */
    public boolean mNotificationPulseRequested = false;
    private long mNotificationPulseTime;
    private final ProximityCheck mProxCheck;
    private boolean mPulsePending;
    private final AsyncSensorManager mSensorManager;
    private final UiEventLogger mUiEventLogger;
    private final WakeLock mWakeLock;
    private boolean mWantProxSensor;
    private boolean mWantSensors;
    private boolean mWantTouchScreenSensors;

    static /* synthetic */ void lambda$new$0(int i) {
    }

    public enum DozingUpdateUiEvent implements UiEventLogger.UiEventEnum {
        DOZING_UPDATE_NOTIFICATION(433),
        DOZING_UPDATE_SIGMOTION(434),
        DOZING_UPDATE_SENSOR_PICKUP(435),
        DOZING_UPDATE_SENSOR_DOUBLE_TAP(436),
        DOZING_UPDATE_SENSOR_LONG_SQUEEZE(437),
        DOZING_UPDATE_DOCKING(438),
        DOZING_UPDATE_SENSOR_WAKEUP(439),
        DOZING_UPDATE_SENSOR_WAKE_LOCKSCREEN(440),
        DOZING_UPDATE_SENSOR_TAP(441),
        DOZING_UPDATE_AUTH_TRIGGERED(657),
        DOZING_UPDATE_QUICK_PICKUP(708),
        DOZING_UPDATE_WAKE_TIMEOUT(794);
        
        private final int mId;

        private DozingUpdateUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static DozingUpdateUiEvent fromReason(int i) {
            switch (i) {
                case 1:
                    return DOZING_UPDATE_NOTIFICATION;
                case 2:
                    return DOZING_UPDATE_SIGMOTION;
                case 3:
                    return DOZING_UPDATE_SENSOR_PICKUP;
                case 4:
                    return DOZING_UPDATE_SENSOR_DOUBLE_TAP;
                case 5:
                    return DOZING_UPDATE_SENSOR_LONG_SQUEEZE;
                case 6:
                    return DOZING_UPDATE_DOCKING;
                case 7:
                    return DOZING_UPDATE_SENSOR_WAKEUP;
                case 8:
                    return DOZING_UPDATE_SENSOR_WAKE_LOCKSCREEN;
                case 9:
                    return DOZING_UPDATE_SENSOR_TAP;
                case 10:
                    return DOZING_UPDATE_AUTH_TRIGGERED;
                case 11:
                    return DOZING_UPDATE_QUICK_PICKUP;
                default:
                    return null;
            }
        }
    }

    @Inject
    public DozeTriggers(Context context, DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeParameters dozeParameters, AsyncSensorManager asyncSensorManager, WakeLock wakeLock, DockManager dockManager, ProximitySensor proximitySensor, ProximityCheck proximityCheck, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, AuthController authController, @Main DelayableExecutor delayableExecutor, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, DevicePostureController devicePostureController) {
        Context context2 = context;
        this.mContext = context2;
        this.mDozeHost = dozeHost;
        AmbientDisplayConfiguration ambientDisplayConfiguration2 = ambientDisplayConfiguration;
        this.mConfig = ambientDisplayConfiguration2;
        DozeParameters dozeParameters2 = dozeParameters;
        this.mDozeParameters = dozeParameters2;
        AsyncSensorManager asyncSensorManager2 = asyncSensorManager;
        this.mSensorManager = asyncSensorManager2;
        WakeLock wakeLock2 = wakeLock;
        this.mWakeLock = wakeLock2;
        this.mAllowPulseTriggers = true;
        DevicePostureController devicePostureController2 = devicePostureController;
        this.mDevicePostureController = devicePostureController2;
        this.mDozeSensors = new DozeSensors(context, asyncSensorManager2, dozeParameters2, ambientDisplayConfiguration2, wakeLock2, new DozeTriggers$$ExternalSyntheticLambda7(this), new DozeTriggers$$ExternalSyntheticLambda8(this), dozeLog, proximitySensor, secureSettings, authController, devicePostureController2);
        this.mDockManager = dockManager;
        this.mProxCheck = proximityCheck;
        this.mDozeLog = dozeLog;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mAuthController = authController;
        this.mMainExecutor = delayableExecutor;
        this.mUiEventLogger = uiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mAlarmManager = (AlarmManager) context2.getSystemService(NotificationCompat.CATEGORY_ALARM);
    }

    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    public void destroy() {
        this.mDozeSensors.destroy();
        this.mProxCheck.destroy();
    }

    /* access modifiers changed from: private */
    public void onNotification(Runnable runnable) {
        if (DozeMachine.DEBUG) {
            Log.d(TAG, "requestNotificationPulse");
        }
        if (!sWakeDisplaySensorState) {
            Log.d(TAG, "Wake display false. Pulse denied.");
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("wakeDisplaySensor");
            return;
        }
        this.mNotificationPulseTime = SystemClock.elapsedRealtime();
        if (!this.mConfig.pulseOnNotificationEnabled(-2)) {
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("pulseOnNotificationsDisabled");
        } else if (this.mDozeHost.isAlwaysOnSuppressed()) {
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("dozeSuppressed");
        } else {
            this.mNotificationPulseRequested = true;
            requestPulse(1, false, runnable);
            this.mDozeLog.traceNotificationPulse();
        }
    }

    private static void runIfNotNull(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    private void proximityCheckThenCall(Consumer<Boolean> consumer, boolean z, int i) {
        Boolean isProximityCurrentlyNear = this.mDozeSensors.isProximityCurrentlyNear();
        if (z) {
            consumer.accept(null);
        } else if (isProximityCurrentlyNear != null) {
            consumer.accept(isProximityCurrentlyNear);
        } else {
            this.mProxCheck.check(500, new DozeTriggers$$ExternalSyntheticLambda4(this, SystemClock.uptimeMillis(), i, consumer));
            this.mWakeLock.acquire(TAG);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$proximityCheckThenCall$1$com-android-systemui-doze-DozeTriggers */
    public /* synthetic */ void mo32469x64e5e29e(long j, int i, Consumer consumer, Boolean bool) {
        boolean z;
        long uptimeMillis = SystemClock.uptimeMillis();
        DozeLog dozeLog = this.mDozeLog;
        if (bool == null) {
            z = false;
        } else {
            z = bool.booleanValue();
        }
        dozeLog.traceProximityResult(z, uptimeMillis - j, i);
        consumer.accept(bool);
        this.mWakeLock.release(TAG);
    }

    /* access modifiers changed from: package-private */
    public void onSensor(int i, float f, float f2, float[] fArr) {
        int i2 = i;
        float[] fArr2 = fArr;
        boolean z = i2 == 4;
        boolean z2 = i2 == 9;
        boolean z3 = i2 == 3;
        boolean z4 = i2 == 5;
        boolean z5 = i2 == 7;
        boolean z6 = i2 == 8;
        boolean z7 = i2 == 10;
        boolean z8 = i2 == 11;
        boolean z9 = z8 || ((z5 || z6) && fArr2 != null && fArr2.length > 0 && fArr2[0] != 0.0f);
        boolean z10 = i2 == 2;
        DozeMachine.State state = null;
        if (z5) {
            if (!this.mMachine.isExecutingTransition()) {
                state = this.mMachine.getState();
            }
            onWakeScreen(z9, state, i2);
        } else if (z4) {
            requestPulse(i2, true, (Runnable) null);
        } else if (!z6 && !z8) {
            DozeTriggers$$ExternalSyntheticLambda5 dozeTriggers$$ExternalSyntheticLambda5 = r0;
            DozeTriggers$$ExternalSyntheticLambda5 dozeTriggers$$ExternalSyntheticLambda52 = new DozeTriggers$$ExternalSyntheticLambda5(this, i, z, z2, f, f2, fArr, z3, z7, z10);
            proximityCheckThenCall(dozeTriggers$$ExternalSyntheticLambda5, true, i2);
        } else if (z9) {
            requestPulse(i2, true, (Runnable) null);
        }
        if (z3 && !shouldDropPickupEvent()) {
            this.mDozeLog.tracePickupWakeUp(SystemClock.elapsedRealtime() - this.mNotificationPulseTime < ((long) this.mDozeParameters.getPickupVibrationThreshold()));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onSensor$3$com-android-systemui-doze-DozeTriggers  reason: not valid java name */
    public /* synthetic */ void m2739lambda$onSensor$3$comandroidsystemuidozeDozeTriggers(int i, boolean z, boolean z2, float f, float f2, float[] fArr, boolean z3, boolean z4, boolean z5, Boolean bool) {
        if (bool != null && bool.booleanValue()) {
            this.mDozeLog.traceSensorEventDropped(i, "prox reporting near");
        } else if (z || z2) {
            if (!(f == -1.0f || f2 == -1.0f)) {
                this.mDozeHost.onSlpiTap(f, f2);
            }
            if (getResolverInt(AODController.KEY_AOD_LIFT_WAKE_ENABLE, 1) == 1) {
                gentleWakeUp(i);
            } else {
                triggerFpMotion(fArr);
            }
        } else if (z3) {
            if (shouldDropPickupEvent()) {
                this.mDozeLog.traceSensorEventDropped(i, "keyguard occluded");
            } else if (getResolverInt(AODController.KEY_AOD_LIFT_WAKE_ENABLE, 1) == 1) {
                gentleWakeUp(i);
            } else {
                triggerFpMotion(fArr);
            }
        } else if (z4) {
            DozeMachine.State state = this.mMachine.getState();
            if (state == DozeMachine.State.DOZE_AOD || state == DozeMachine.State.DOZE) {
                this.mAodInterruptRunnable = new DozeTriggers$$ExternalSyntheticLambda3(this, f, f2, fArr);
            }
            requestPulse(10, true, (Runnable) null);
        } else if (z5) {
            triggerFpMotion(fArr);
        } else {
            this.mDozeHost.extendPulse(i);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onSensor$2$com-android-systemui-doze-DozeTriggers  reason: not valid java name */
    public /* synthetic */ void m2738lambda$onSensor$2$comandroidsystemuidozeDozeTriggers(float f, float f2, float[] fArr) {
        this.mAuthController.onAodInterrupt((int) f, (int) f2, fArr[3], fArr[4]);
    }

    private boolean shouldDropPickupEvent() {
        return this.mKeyguardStateController.isOccluded();
    }

    private void gentleWakeUp(int i) {
        Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        Objects.requireNonNull(uiEventLogger);
        ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda1(uiEventLogger));
        if (this.mDozeParameters.getDisplayNeedsBlanking()) {
            this.mDozeHost.setAodDimmingScrim(1.0f);
        }
        this.mMachine.wakeUp();
    }

    /* access modifiers changed from: private */
    public void onProximityFar(boolean z) {
        if (this.mMachine.isExecutingTransition()) {
            Log.w(TAG, "onProximityFar called during transition. Ignoring sensor response.");
            return;
        }
        boolean z2 = !z;
        DozeMachine.State state = this.mMachine.getState();
        boolean z3 = false;
        boolean z4 = state == DozeMachine.State.DOZE_AOD_PAUSED;
        boolean z5 = state == DozeMachine.State.DOZE_AOD_PAUSING;
        if (state == DozeMachine.State.DOZE_AOD) {
            z3 = true;
        }
        if (state == DozeMachine.State.DOZE_PULSING || state == DozeMachine.State.DOZE_PULSING_BRIGHT) {
            if (DEBUG) {
                Log.i(TAG, "Prox changed, ignore touch = " + z2);
            }
            this.mDozeHost.onIgnoreTouchWhilePulsing(z2);
        }
        if (z && (z4 || z5)) {
            if (DEBUG) {
                Log.i(TAG, "Prox FAR, unpausing AOD");
            }
            this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
        } else if (z2 && z3) {
            if (DEBUG) {
                Log.i(TAG, "Prox NEAR, pausing AOD");
            }
            this.mMachine.requestState(DozeMachine.State.DOZE_AOD_PAUSING);
        }
    }

    private void onWakeScreen(boolean z, DozeMachine.State state, int i) {
        this.mDozeLog.traceWakeDisplay(z, i);
        sWakeDisplaySensorState = z;
        boolean z2 = false;
        if (z) {
            proximityCheckThenCall(new DozeTriggers$$ExternalSyntheticLambda2(this, state, i), false, i);
            return;
        }
        boolean z3 = state == DozeMachine.State.DOZE_AOD_PAUSED;
        if (state == DozeMachine.State.DOZE_AOD_PAUSING) {
            z2 = true;
        }
        if (!z2 && !z3) {
            this.mMachine.requestState(DozeMachine.State.DOZE);
            this.mUiEventLogger.log(DozingUpdateUiEvent.DOZING_UPDATE_WAKE_TIMEOUT);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onWakeScreen$4$com-android-systemui-doze-DozeTriggers  reason: not valid java name */
    public /* synthetic */ void m2740lambda$onWakeScreen$4$comandroidsystemuidozeDozeTriggers(DozeMachine.State state, int i, Boolean bool) {
        if ((bool == null || !bool.booleanValue()) && state == DozeMachine.State.DOZE) {
            this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda1(uiEventLogger));
        }
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        closeDisplayInversion(state2);
        switch (C20692.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
                this.mAodInterruptRunnable = null;
                sWakeDisplaySensorState = true;
                this.mBroadcastReceiver.register(this.mBroadcastDispatcher);
                this.mDockManager.addListener(this.mDockEventListener);
                this.mDozeSensors.requestTemporaryDisable();
                this.mDozeHost.addCallback(this.mHostCallback);
                if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFingerprintDetectionRunning()) {
                    delayDismissFingerprint();
                    break;
                }
                break;
            case 2:
            case 3:
                this.mWantProxSensor = state2 == DozeMachine.State.DOZE || state2 == DozeMachine.State.DOZE_AOD;
                this.mWantSensors = true;
                this.mWantTouchScreenSensors = true;
                if (state2 == DozeMachine.State.DOZE_AOD && !sWakeDisplaySensorState) {
                    onWakeScreen(false, state2, 7);
                    break;
                }
                break;
            case 4:
            case 5:
                this.mWantProxSensor = true;
                break;
            case 6:
            case 7:
                this.mWantProxSensor = true;
                this.mWantTouchScreenSensors = false;
                break;
            case 8:
                this.mWantProxSensor = false;
                this.mWantTouchScreenSensors = false;
                break;
            case 9:
                this.mDozeSensors.requestTemporaryDisable();
                break;
            case 10:
                this.mBroadcastReceiver.unregister(this.mBroadcastDispatcher);
                this.mDozeHost.removeCallback(this.mHostCallback);
                this.mDockManager.removeListener(this.mDockEventListener);
                this.mDozeSensors.setListening(false, false);
                this.mDozeSensors.setProxListening(false);
                this.mWantSensors = false;
                this.mWantProxSensor = false;
                this.mWantTouchScreenSensors = false;
                cancelDelayDismissFingerprint();
                this.mAuthController.showFingerprintIcon();
                cancelDelayEnterAndExitSleepMode();
                break;
        }
        this.mDozeSensors.setListening(this.mWantSensors, this.mWantTouchScreenSensors);
    }

    /* renamed from: com.android.systemui.doze.DozeTriggers$2 */
    static /* synthetic */ class C20692 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|(3:19|20|22)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.systemui.doze.DozeMachine$State[] r0 = com.android.systemui.doze.DozeMachine.State.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$systemui$doze$DozeMachine$State = r0
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.INITIALIZED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_PAUSED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_PAUSING     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_PULSING     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_PULSING_BRIGHT     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0060 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_DOCKED     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x006c }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_PULSE_DONE     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0078 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.FINISH     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeTriggers.C20692.<clinit>():void");
        }
    }

    public void onScreenState(int i) {
        this.mDozeSensors.onScreenState(i);
        boolean z = false;
        boolean z2 = i == 3 || i == 4 || i == 1;
        DozeSensors dozeSensors = this.mDozeSensors;
        if (this.mWantProxSensor && z2) {
            z = true;
        }
        dozeSensors.setProxListening(z);
        this.mDozeSensors.setListening(this.mWantSensors, this.mWantTouchScreenSensors, z2);
        Log.d(TAG, "onScreenState:  state  = " + i + ", cb: " + Debug.getCallers(3));
        if (z2) {
            cancelDelayEnterAndExitSleepMode();
            delayEnterAndExitSleepMode();
        }
        ((KeyguardUpdateMonitorEx) NTDependencyEx.get(KeyguardUpdateMonitorEx.class)).setLowPowerState(z2);
        Runnable runnable = this.mAodInterruptRunnable;
        if (runnable != null && i == 3) {
            runnable.run();
            this.mAodInterruptRunnable = null;
        }
        Runnable runnable2 = this.mAodInterruptRunnable;
        if (runnable2 != null && i == 2) {
            runnable2.run();
            this.mAodInterruptRunnable = null;
        }
    }

    /* access modifiers changed from: private */
    public void requestPulse(int i, boolean z, Runnable runnable) {
        Assert.isMainThread();
        this.mDozeHost.extendPulse(i);
        DozeMachine.State state = this.mMachine.isExecutingTransition() ? null : this.mMachine.getState();
        if (state == DozeMachine.State.DOZE_PULSING && i == 8) {
            this.mMachine.requestState(DozeMachine.State.DOZE_PULSING_BRIGHT);
        } else if (this.mPulsePending || !this.mAllowPulseTriggers || !canPulse()) {
            if (this.mAllowPulseTriggers) {
                this.mDozeLog.tracePulseDropped(this.mPulsePending, state, this.mDozeHost.isPulsingBlocked());
            }
            runIfNotNull(runnable);
        } else {
            boolean z2 = true;
            this.mPulsePending = true;
            DozeTriggers$$ExternalSyntheticLambda0 dozeTriggers$$ExternalSyntheticLambda0 = new DozeTriggers$$ExternalSyntheticLambda0(this, runnable, i);
            if (this.mDozeParameters.getProxCheckBeforePulse() && !z) {
                z2 = false;
            }
            proximityCheckThenCall(dozeTriggers$$ExternalSyntheticLambda0, z2, i);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda1(uiEventLogger));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$requestPulse$5$com-android-systemui-doze-DozeTriggers  reason: not valid java name */
    public /* synthetic */ void m2741lambda$requestPulse$5$comandroidsystemuidozeDozeTriggers(Runnable runnable, int i, Boolean bool) {
        if (bool == null || !bool.booleanValue()) {
            continuePulseRequest(i);
            return;
        }
        this.mDozeLog.tracePulseDropped("inPocket");
        this.mPulsePending = false;
        runIfNotNull(runnable);
    }

    private boolean canPulse() {
        return this.mMachine.getState() == DozeMachine.State.DOZE || this.mMachine.getState() == DozeMachine.State.DOZE_AOD || this.mMachine.getState() == DozeMachine.State.DOZE_AOD_DOCKED;
    }

    private void continuePulseRequest(int i) {
        this.mPulsePending = false;
        if (this.mDozeHost.isPulsingBlocked() || !canPulse()) {
            this.mDozeLog.tracePulseDropped(this.mPulsePending, this.mMachine.getState(), this.mDozeHost.isPulsingBlocked());
            return;
        }
        this.mMachine.requestPulse(i);
        this.mAuthController.showFingerprintIcon();
        cancelDelayDismissFingerprint();
        delayDismissFingerprint();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(" mAodInterruptRunnable=" + this.mAodInterruptRunnable);
        printWriter.print(" notificationPulseTime=");
        printWriter.println(Formatter.formatShortElapsedTime(this.mContext, this.mNotificationPulseTime));
        printWriter.println(" pulsePending=" + this.mPulsePending);
        printWriter.println("DozeSensors:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        this.mDozeSensors.dump(indentingPrintWriter);
    }

    private class TriggerReceiver extends BroadcastReceiver {
        private boolean mRegistered;

        private TriggerReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (DozeTriggers.PULSE_ACTION.equals(intent.getAction())) {
                if (DozeMachine.DEBUG) {
                    Log.d(DozeTriggers.TAG, "Received pulse intent");
                }
                DozeTriggers.this.requestPulse(0, false, (Runnable) null);
            }
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                DozeTriggers.this.mDozeSensors.onUserSwitched();
            }
            Log.d(DozeTriggers.TAG, "Received  intent = " + intent.getAction() + " mRegistered " + this.mRegistered);
            if (this.mRegistered) {
                if ("com.nothingos.aod.sleep.enter".equals(intent.getAction())) {
                    DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD_PAUSING);
                }
                if ("com.nothingos.aod.sleep.exit".equals(intent.getAction())) {
                    DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
                    if (((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView()) {
                        ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewAlpha(1.0f);
                    }
                }
                if ("com.nothingos.aod.dismiss.fingerprint".equals(intent.getAction())) {
                    DozeTriggers.this.mAuthController.dismissFingerprintIcon();
                    if (((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView()) {
                        return;
                    }
                    if (DozeTriggers.this.mNotificationPulseRequested) {
                        ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewAlpha(0.0f);
                        boolean unused = DozeTriggers.this.mNotificationPulseRequested = false;
                        DozeTriggers.this.delayDismissFingerprint();
                        return;
                    }
                    DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE);
                }
            }
        }

        public void register(BroadcastDispatcher broadcastDispatcher) {
            if (!this.mRegistered) {
                IntentFilter intentFilter = new IntentFilter(DozeTriggers.PULSE_ACTION);
                intentFilter.addAction("android.intent.action.USER_SWITCHED");
                intentFilter.addAction("com.nothingos.aod.sleep.enter");
                intentFilter.addAction("com.nothingos.aod.sleep.exit");
                intentFilter.addAction("com.nothingos.aod.dismiss.fingerprint");
                broadcastDispatcher.registerReceiver(this, intentFilter);
                this.mRegistered = true;
            }
        }

        public void unregister(BroadcastDispatcher broadcastDispatcher) {
            if (this.mRegistered) {
                broadcastDispatcher.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }
    }

    private class DockEventListener implements DockManager.DockEventListener {
        private DockEventListener() {
        }

        public void onEvent(int i) {
            if (DozeTriggers.DEBUG) {
                Log.d(DozeTriggers.TAG, "dock event = " + i);
            }
            if (i == 0) {
                DozeTriggers.this.mDozeSensors.ignoreTouchScreenSensorsSettingInterferingWithDocking(false);
            } else if (i == 1 || i == 2) {
                DozeTriggers.this.mDozeSensors.ignoreTouchScreenSensorsSettingInterferingWithDocking(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void delayDismissFingerprint() {
        Log.d(TAG, "delayDismissFingerprint visible = " + this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser()));
        if (this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
            if (this.mDelayDismissFpPendingIntent == null) {
                this.mDelayDismissFpPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.nothingos.aod.dismiss.fingerprint"), 67108864);
            }
            this.mAlarmManager.cancel(this.mDelayDismissFpPendingIntent);
            this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + 5000, this.mDelayDismissFpPendingIntent);
        }
    }

    /* access modifiers changed from: private */
    public void cancelDelayDismissFingerprint() {
        Log.d(TAG, "cancelDelayDismissFingerprint");
        PendingIntent pendingIntent = this.mDelayDismissFpPendingIntent;
        if (pendingIntent != null) {
            this.mAlarmManager.cancel(pendingIntent);
        }
    }

    private void delayEnterAndExitSleepMode() {
        long j;
        long j2;
        if (!this.mDozeParameters.getAlwaysOn()) {
            return;
        }
        if (!this.mDozeParameters.getAlwaysOn() || !((AODController) NTDependencyEx.get(AODController.class)).isAllDay()) {
            Date time = Calendar.getInstance().getTime();
            int hours = time.getHours();
            int minutes = time.getMinutes();
            int seconds = time.getSeconds();
            int intValue = Integer.valueOf(((AODController) NTDependencyEx.get(AODController.class)).getAODEndTime().substring(0, 2)).intValue();
            int intValue2 = Integer.valueOf(((AODController) NTDependencyEx.get(AODController.class)).getAODEndTime().substring(2, 4)).intValue();
            int intValue3 = Integer.valueOf(((AODController) NTDependencyEx.get(AODController.class)).getAODStartTime().substring(0, 2)).intValue();
            int intValue4 = Integer.valueOf(((AODController) NTDependencyEx.get(AODController.class)).getAODStartTime().substring(2, 4)).intValue();
            if (((AODController) NTDependencyEx.get(AODController.class)).checkNightMode()) {
                Log.d(TAG, "delayEnterAndExitSleepMode: night mode");
                j2 = (long) (((((hours <= intValue3 ? intValue3 - hours : intValue3 + (24 - hours)) * 60 * 60) + ((intValue4 - minutes) * 60)) * 1000) + ((0 - seconds) * 1000));
                j = 0;
            } else {
                long j3 = (long) (((((intValue >= hours ? intValue - hours : (24 - hours) + intValue) * 60 * 60) + ((intValue2 - minutes) * 60)) * 1000) + ((0 - seconds) * 1000));
                Log.d(TAG, "delayEnterAndExitSleepMode: not night mode  duration = " + j3);
                j = j3 + 0;
                j2 = j3 + ((long) (((24 - intValue) + intValue3) * 60 * 60 * 1000));
            }
            Log.d(TAG, "delayEnterAndExitSleepMode now = " + time + ", enterSleepModeDelayMs = " + j + ", exitSleepModeDelayMs = " + j2);
            if (j > 0) {
                Log.d(TAG, "delay enter sleep mode enterSleepModeDelayMs = " + j);
                if (this.mEnterSleepModePendingIntent == null) {
                    this.mEnterSleepModePendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.nothingos.aod.sleep.enter"), 67108864);
                }
                this.mAlarmManager.cancel(this.mEnterSleepModePendingIntent);
                this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j, this.mEnterSleepModePendingIntent);
            }
            if (j2 > 0) {
                Log.d(TAG, "delay exit sleep mode exitSleepModeDelayMs = " + j2);
                if (this.mExitSleepModePendingIntent == null) {
                    this.mExitSleepModePendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.nothingos.aod.sleep.exit"), 67108864);
                }
                this.mAlarmManager.cancel(this.mExitSleepModePendingIntent);
                this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j2, this.mExitSleepModePendingIntent);
            }
        }
    }

    private void cancelDelayEnterAndExitSleepMode() {
        Log.d(TAG, "cancelDelayEnterSleepMode");
        PendingIntent pendingIntent = this.mEnterSleepModePendingIntent;
        if (pendingIntent != null) {
            this.mAlarmManager.cancel(pendingIntent);
        }
        PendingIntent pendingIntent2 = this.mExitSleepModePendingIntent;
        if (pendingIntent2 != null) {
            this.mAlarmManager.cancel(pendingIntent2);
        }
    }

    /* access modifiers changed from: private */
    public void switchAODPowerMode(int i) {
        Log.d(TAG, "switchAODPowerMode " + i);
        SurfaceControl.setDisplayPowerMode(SurfaceControl.getInternalDisplayToken(), i);
    }

    private void triggerFpMotion(float[] fArr) {
        if (fArr[0] == 1.0f && ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFingerprintDetectionRunning()) {
            if (this.mMachine.getState() != DozeMachine.State.DOZE_AOD) {
                this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
            }
            this.mAuthController.showFingerprintIcon();
            cancelDelayDismissFingerprint();
            delayDismissFingerprint();
        }
    }

    private void closeDisplayInversion(DozeMachine.State state) {
        NTLogUtil.m1680d(TAG, "closeDisplayInversion newState==" + state);
        int resolverInt = getResolverInt(NT_TURN_OFF_INVERT_OR_AOD, 0);
        if (state == DozeMachine.State.FINISH || state == DozeMachine.State.INITIALIZED) {
            if (resolverInt != 0) {
                if (resolverInt == 2) {
                    putResolverInt("doze_always_on", 1);
                }
                if (resolverInt == 1 || resolverInt == 2) {
                    KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class);
                    if (!keyguardUpdateMonitor.isKeyguardVisible() || !keyguardUpdateMonitor.isUdfpsEnrolled() || keyguardUpdateMonitor.isFingerprintLockedOut()) {
                        ((NTColorController) NTDependencyEx.get(NTColorController.class)).restoreInversion();
                    } else {
                        NTLogUtil.m1682i(TAG, "Don't restoreInversion!");
                    }
                }
                putResolverInt(NT_TURN_OFF_INVERT_OR_AOD, 0);
            }
        } else if (resolverInt == 0) {
            if (getResolverInt("accessibility_display_inversion_enabled", 0) == 1) {
                if (getResolverInt("doze_always_on", 0) == 1) {
                    putResolverInt("doze_always_on", 0);
                    resolverInt = 2;
                } else {
                    resolverInt = 1;
                }
                ((NTColorController) NTDependencyEx.get(NTColorController.class)).resetInversion();
            }
            putResolverInt(NT_TURN_OFF_INVERT_OR_AOD, resolverInt);
        }
    }

    private void putResolverInt(String str, int i) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), str, i);
    }

    private int getResolverInt(String str, int i) {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), str, i);
    }
}
