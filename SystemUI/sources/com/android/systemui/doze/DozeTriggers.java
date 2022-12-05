package com.android.systemui.doze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.Dependency;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozeSensors;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import com.nothingos.keyguard.NTColorController;
import com.nothingos.systemui.doze.AODController;
import com.nothingos.systemui.doze.LiftWakeGestureController;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class DozeTriggers implements DozeMachine.Part {
    private static final boolean DEBUG = DozeService.DEBUG;
    private static boolean sWakeDisplaySensorState = true;
    private AlarmManager mAlarmManager;
    private Runnable mAodInterruptRunnable;
    private final AuthController mAuthController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final AmbientDisplayConfiguration mConfig;
    private final Context mContext;
    private PendingIntent mDelayDismissFpPendingIntent;
    private final DockManager mDockManager;
    private final DozeHost mDozeHost;
    private final DozeLog mDozeLog;
    private final DozeParameters mDozeParameters;
    private final DozeSensors mDozeSensors;
    private PendingIntent mEnterSleepModePendingIntent;
    private PendingIntent mExitSleepModePendingIntent;
    private final KeyguardStateController mKeyguardStateController;
    private DozeMachine mMachine;
    private final DelayableExecutor mMainExecutor;
    private long mNotificationPulseTime;
    private final ProximitySensor.ProximityCheck mProxCheck;
    private boolean mPulsePending;
    private final AsyncSensorManager mSensorManager;
    private final UiEventLogger mUiEventLogger;
    private final UiModeManager mUiModeManager;
    private final WakeLock mWakeLock;
    private boolean mWantProxSensor;
    private boolean mWantSensors;
    private boolean mWantTouchScreenSensors;
    private final TriggerReceiver mBroadcastReceiver = new TriggerReceiver(this, null);
    private final DockEventListener mDockEventListener = new DockEventListener(this, null);
    private DozeHost.Callback mHostCallback = new AnonymousClass1();
    private final String ENTER_SLEEP_MODE_ACTION = "com.nothingos.aod.sleep.enter";
    private final String EXIT_SLEEP_MODE_ACTION = "com.nothingos.aod.sleep.exit";
    private final String DELAY_DISMISS_FINGERPRINT = "com.nothingos.aod.dismiss.fingerprint";
    private final int NO_CLOSE = 0;
    private final int TURN_OFF_INVERT = 1;
    private final int TURN_OFF_INVERT_AND_AOD = 2;
    private int mPulseReason = -1;
    private final boolean mAllowPulseTriggers = true;

    @VisibleForTesting
    /* loaded from: classes.dex */
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

        DozingUpdateUiEvent(int i) {
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

    public DozeTriggers(Context context, DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeParameters dozeParameters, AsyncSensorManager asyncSensorManager, WakeLock wakeLock, DockManager dockManager, ProximitySensor proximitySensor, ProximitySensor.ProximityCheck proximityCheck, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, AuthController authController, DelayableExecutor delayableExecutor, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController) {
        this.mContext = context;
        this.mDozeHost = dozeHost;
        this.mConfig = ambientDisplayConfiguration;
        this.mDozeParameters = dozeParameters;
        this.mSensorManager = asyncSensorManager;
        this.mWakeLock = wakeLock;
        this.mDozeSensors = new DozeSensors(context, asyncSensorManager, dozeParameters, ambientDisplayConfiguration, wakeLock, new DozeSensors.Callback() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0
            @Override // com.android.systemui.doze.DozeSensors.Callback
            public final void onSensorPulse(int i, float f, float f2, float[] fArr) {
                DozeTriggers.this.onSensor(i, f, f2, fArr);
            }
        }, new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DozeTriggers.this.onProximityFar(((Boolean) obj).booleanValue());
            }
        }, dozeLog, proximitySensor, secureSettings, authController);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mDockManager = dockManager;
        this.mProxCheck = proximityCheck;
        this.mDozeLog = dozeLog;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mAuthController = authController;
        this.mMainExecutor = delayableExecutor;
        this.mUiEventLogger = uiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void destroy() {
        this.mDozeSensors.destroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNotification(Runnable runnable) {
        if (DozeMachine.DEBUG) {
            Log.d("DozeTriggers", "requestNotificationPulse");
        }
        if (!sWakeDisplaySensorState) {
            Log.d("DozeTriggers", "Wake display false. Pulse denied.");
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("wakeDisplaySensor");
            return;
        }
        this.mNotificationPulseTime = SystemClock.elapsedRealtime();
        if (!this.mConfig.pulseOnNotificationEnabled(-2)) {
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("pulseOnNotificationsDisabled");
            Log.d("DozeTriggers", "onNotification: pulseOnNotificationsDisabled");
        } else if (this.mDozeHost.isDozeSuppressed()) {
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("dozeSuppressed");
            Log.d("DozeTriggers", "onNotification: dozeSuppressed");
        } else {
            this.mPulseReason = 1;
            requestPulse(1, false, runnable);
            this.mDozeLog.traceNotificationPulse();
        }
    }

    private static void runIfNotNull(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    private void proximityCheckThenCall(final Consumer<Boolean> consumer, boolean z, final int i) {
        Boolean isProximityCurrentlyNear = this.mDozeSensors.isProximityCurrentlyNear();
        if (z) {
            consumer.accept(null);
        } else if (isProximityCurrentlyNear != null) {
            consumer.accept(isProximityCurrentlyNear);
        } else {
            final long uptimeMillis = SystemClock.uptimeMillis();
            this.mProxCheck.check(500L, new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers.this.lambda$proximityCheckThenCall$0(uptimeMillis, i, consumer, (Boolean) obj);
                }
            });
            this.mWakeLock.acquire("DozeTriggers");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$proximityCheckThenCall$0(long j, int i, Consumer consumer, Boolean bool) {
        this.mDozeLog.traceProximityResult(bool == null ? false : bool.booleanValue(), SystemClock.uptimeMillis() - j, i);
        consumer.accept(bool);
        this.mWakeLock.release("DozeTriggers");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void onSensor(final int i, final float f, final float f2, final float[] fArr) {
        boolean z;
        final boolean z2 = i == 4;
        final boolean z3 = i == 9;
        boolean z4 = i == 3;
        boolean z5 = i == 5;
        boolean z6 = i == 7;
        boolean z7 = i == 8;
        final boolean z8 = i == 10;
        boolean z9 = i == 11;
        boolean z10 = z9 || ((z6 || z7) && fArr != null && fArr.length > 0 && fArr[0] != 0.0f);
        final boolean z11 = i == 2;
        DozeMachine.State state = null;
        if (z6) {
            if (!this.mMachine.isExecutingTransition()) {
                state = this.mMachine.getState();
            }
            onWakeScreen(z10, state, i);
        } else if (z5) {
            requestPulse(i, true, null);
        } else {
            if (z7 || z9) {
                z = true;
                if (z10) {
                    requestPulse(i, true, null);
                }
            } else {
                final boolean z12 = z4;
                proximityCheckThenCall(new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DozeTriggers.this.lambda$onSensor$2(i, z2, z3, f, f2, fArr, z12, z8, z11, (Boolean) obj);
                    }
                }, false, i);
                z = true;
            }
            if (z4 || shouldDropPickupEvent()) {
            }
            this.mDozeLog.tracePickupWakeUp(SystemClock.elapsedRealtime() - this.mNotificationPulseTime < ((long) this.mDozeParameters.getPickupVibrationThreshold()) ? z : false);
            return;
        }
        z = true;
        if (z4) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSensor$2(int i, boolean z, boolean z2, final float f, final float f2, final float[] fArr, boolean z3, boolean z4, boolean z5, Boolean bool) {
        Log.d("DozeTriggers", "onSensor: result = " + bool);
        if (bool != null && bool.booleanValue()) {
            this.mDozeLog.traceSensorEventDropped(i, "prox reporting near");
        } else if (z || z2) {
            if (f != -1.0f && f2 != -1.0f) {
                this.mDozeHost.onSlpiTap(f, f2);
            }
            if (getResolverInt("tap_gesture", 1) == 1) {
                gentleWakeUp(i);
            } else {
                triggerFpMotion(fArr);
            }
        } else if (z3) {
            if (shouldDropPickupEvent()) {
                this.mDozeLog.traceSensorEventDropped(i, "keyguard occluded");
            } else if (getResolverInt("wake_gesture_enabled", 1) == 1) {
                gentleWakeUp(i);
            } else {
                triggerFpMotion(fArr);
            }
        } else if (!z4) {
            if (z5) {
                triggerFpMotion(fArr);
            } else {
                this.mDozeHost.extendPulse(i);
            }
        } else {
            DozeMachine.State state = this.mMachine.getState();
            if (state == DozeMachine.State.DOZE_AOD || state == DozeMachine.State.DOZE) {
                this.mAodInterruptRunnable = new Runnable() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DozeTriggers.this.lambda$onSensor$1(f, f2, fArr);
                    }
                };
            }
            requestPulse(10, true, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSensor$1(float f, float f2, float[] fArr) {
        this.mAuthController.onAodInterrupt((int) f, (int) f2, fArr[3], fArr[4]);
    }

    private boolean shouldDropPickupEvent() {
        return this.mKeyguardStateController.isOccluded();
    }

    private void gentleWakeUp(int i) {
        Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        Objects.requireNonNull(uiEventLogger);
        ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda2(uiEventLogger));
        if (this.mDozeParameters.getDisplayNeedsBlanking()) {
            this.mDozeHost.setAodDimmingScrim(1.0f);
        }
        this.mMachine.wakeUp();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProximityFar(boolean z) {
        if (this.mMachine.isExecutingTransition()) {
            Log.w("DozeTriggers", "onProximityFar called during transition. Ignoring sensor response.");
            return;
        }
        boolean z2 = !z;
        DozeMachine.State state = this.mMachine.getState();
        boolean z3 = false;
        boolean z4 = state == DozeMachine.State.DOZE_AOD_PAUSED;
        DozeMachine.State state2 = DozeMachine.State.DOZE_AOD_PAUSING;
        boolean z5 = state == state2;
        DozeMachine.State state3 = DozeMachine.State.DOZE_AOD;
        if (state == state3) {
            z3 = true;
        }
        if (state == DozeMachine.State.DOZE_PULSING || state == DozeMachine.State.DOZE_PULSING_BRIGHT) {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox changed, ignore touch = " + z2);
            }
            this.mDozeHost.onIgnoreTouchWhilePulsing(z2);
        }
        if (z && ((z4 || z5) && ((AODController) Dependency.get(AODController.class)).shouldShowAODView())) {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox FAR, unpausing AOD");
            }
            this.mMachine.requestState(state3);
        } else if (!z2 || !z3) {
        } else {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox NEAR, pausing AOD");
            }
            this.mMachine.requestState(state2);
        }
    }

    private void onWakeScreen(boolean z, final DozeMachine.State state, final int i) {
        this.mDozeLog.traceWakeDisplay(z, i);
        sWakeDisplaySensorState = z;
        boolean z2 = false;
        if (z) {
            proximityCheckThenCall(new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers.this.lambda$onWakeScreen$3(state, i, (Boolean) obj);
                }
            }, false, i);
            return;
        }
        boolean z3 = state == DozeMachine.State.DOZE_AOD_PAUSED;
        if (state == DozeMachine.State.DOZE_AOD_PAUSING) {
            z2 = true;
        }
        if (z2 || z3) {
            return;
        }
        this.mMachine.requestState(DozeMachine.State.DOZE);
        this.mUiEventLogger.log(DozingUpdateUiEvent.DOZING_UPDATE_WAKE_TIMEOUT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWakeScreen$3(DozeMachine.State state, int i, Boolean bool) {
        if ((bool == null || !bool.booleanValue()) && state == DozeMachine.State.DOZE) {
            this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda2(uiEventLogger));
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        closeDisplayInversion(state2);
        switch (AnonymousClass2.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
                this.mAodInterruptRunnable = null;
                sWakeDisplaySensorState = true;
                this.mBroadcastReceiver.register(this.mBroadcastDispatcher);
                this.mDozeHost.addCallback(this.mHostCallback);
                this.mDockManager.addListener(this.mDockEventListener);
                this.mDozeSensors.requestTemporaryDisable();
                checkTriggersAtInit();
                if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFingerprintDetectionRunning()) {
                    delayDismissFingerprint();
                    break;
                }
                break;
            case 2:
            case 3:
                this.mWantProxSensor = state2 != DozeMachine.State.DOZE;
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

    /* renamed from: com.android.systemui.doze.DozeTriggers$2  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[DozeMachine.State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[DozeMachine.State.INITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_PAUSED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_PAUSING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSING_BRIGHT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_DOCKED.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSE_DONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.FINISH.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
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
        Log.d("DozeTriggers", "onScreenState:  state  = " + i, new Exception());
        if (z2) {
            cancelDelayEnterAndExitSleepMode();
            delayEnterAndExitSleepMode();
        }
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).setLowPowerState(z2);
        Runnable runnable = this.mAodInterruptRunnable;
        if (runnable != null && i == 3) {
            runnable.run();
            this.mAodInterruptRunnable = null;
        }
        Runnable runnable2 = this.mAodInterruptRunnable;
        if (runnable2 == null || i != 2) {
            return;
        }
        runnable2.run();
        this.mAodInterruptRunnable = null;
    }

    private void checkTriggersAtInit() {
        if (this.mUiModeManager.getCurrentModeType() == 3 || this.mDozeHost.isBlockingDoze() || !this.mDozeHost.isProvisioned()) {
            this.mMachine.requestState(DozeMachine.State.FINISH);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestPulse(final int i, boolean z, final Runnable runnable) {
        Log.d("DozeTriggers", "requestPulse: reason = " + i);
        Assert.isMainThread();
        this.mDozeHost.extendPulse(i);
        DozeMachine.State state = this.mMachine.isExecutingTransition() ? null : this.mMachine.getState();
        if (state == DozeMachine.State.DOZE_PULSING && i == 8) {
            this.mMachine.requestState(DozeMachine.State.DOZE_PULSING_BRIGHT);
        } else if (this.mPulsePending || !this.mAllowPulseTriggers || !canPulse()) {
            if (this.mAllowPulseTriggers) {
                this.mDozeLog.tracePulseDropped(this.mPulsePending, state, this.mDozeHost.isPulsingBlocked());
            }
            Log.d("DozeTriggers", "requestPulse: can not pulse mPulsePending = " + this.mPulsePending + "mAllowPulseTriggers =" + this.mAllowPulseTriggers + "canPulse =" + canPulse() + "state = " + this.mMachine.getState());
            runIfNotNull(runnable);
        } else {
            boolean z2 = true;
            this.mPulsePending = true;
            Consumer<Boolean> consumer = new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers.this.lambda$requestPulse$4(runnable, i, (Boolean) obj);
                }
            };
            if (this.mDozeParameters.getProxCheckBeforePulse() && !z) {
                z2 = false;
            }
            proximityCheckThenCall(consumer, z2, i);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda2(uiEventLogger));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestPulse$4(Runnable runnable, int i, Boolean bool) {
        if (bool != null && bool.booleanValue()) {
            this.mDozeLog.tracePulseDropped("inPocket");
            this.mPulsePending = false;
            runIfNotNull(runnable);
            return;
        }
        continuePulseRequest(i);
    }

    private boolean canPulse() {
        return this.mMachine.getState() == DozeMachine.State.DOZE || this.mMachine.getState() == DozeMachine.State.DOZE_AOD || this.mMachine.getState() == DozeMachine.State.DOZE_AOD_DOCKED || this.mMachine.getState() == DozeMachine.State.DOZE_AOD_PAUSING || this.mMachine.getState() == DozeMachine.State.DOZE_AOD_PAUSED;
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

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void dump(PrintWriter printWriter) {
        printWriter.println(" mAodInterruptRunnable=" + this.mAodInterruptRunnable);
        printWriter.print(" notificationPulseTime=");
        printWriter.println(Formatter.formatShortElapsedTime(this.mContext, this.mNotificationPulseTime));
        printWriter.println(" pulsePending=" + this.mPulsePending);
        printWriter.println("DozeSensors:");
        PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        this.mDozeSensors.dump(indentingPrintWriter);
    }

    /* loaded from: classes.dex */
    private class TriggerReceiver extends BroadcastReceiver {
        private boolean mRegistered;

        private TriggerReceiver() {
        }

        /* synthetic */ TriggerReceiver(DozeTriggers dozeTriggers, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.android.systemui.doze.pulse".equals(intent.getAction())) {
                if (DozeMachine.DEBUG) {
                    Log.d("DozeTriggers", "Received pulse intent");
                }
                DozeTriggers.this.requestPulse(0, false, null);
            }
            if (UiModeManager.ACTION_ENTER_CAR_MODE.equals(intent.getAction())) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.FINISH);
            }
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                DozeTriggers.this.mDozeSensors.onUserSwitched();
            }
            Log.d("DozeTriggers", "Received  intent = " + intent.getAction());
            if ("com.nothingos.aod.sleep.enter".equals(intent.getAction())) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD_PAUSING);
            }
            if ("com.nothingos.aod.sleep.exit".equals(intent.getAction())) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
                if (((AODController) Dependency.get(AODController.class)).shouldShowAODView()) {
                    ((StatusBar) Dependency.get(StatusBar.class)).setNotificationPanelViewAlpha(1.0f);
                }
            }
            if ("com.nothingos.aod.dismiss.fingerprint".equals(intent.getAction())) {
                DozeTriggers.this.mAuthController.dismissFingerprintIcon();
                ((LiftWakeGestureController) Dependency.get(LiftWakeGestureController.class)).requestMotionTrigger();
                if (DozeTriggers.this.mPulseReason == 1 && !((AODController) Dependency.get(AODController.class)).shouldShowAODView()) {
                    ((StatusBar) Dependency.get(StatusBar.class)).setNotificationPanelViewAlpha(0.0f);
                    DozeTriggers.this.mPulseReason = -1;
                }
                if (((AODController) Dependency.get(AODController.class)).shouldShowAODView()) {
                    return;
                }
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD_PAUSING);
            }
        }

        public void register(BroadcastDispatcher broadcastDispatcher) {
            if (this.mRegistered) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter("com.android.systemui.doze.pulse");
            intentFilter.addAction(UiModeManager.ACTION_ENTER_CAR_MODE);
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            intentFilter.addAction("com.nothingos.aod.sleep.enter");
            intentFilter.addAction("com.nothingos.aod.sleep.exit");
            intentFilter.addAction("com.nothingos.aod.dismiss.fingerprint");
            broadcastDispatcher.registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }

        public void unregister(BroadcastDispatcher broadcastDispatcher) {
            if (!this.mRegistered) {
                return;
            }
            broadcastDispatcher.unregisterReceiver(this);
            this.mRegistered = false;
        }
    }

    /* loaded from: classes.dex */
    private class DockEventListener implements DockManager.DockEventListener {
        private DockEventListener() {
        }

        /* synthetic */ DockEventListener(DozeTriggers dozeTriggers, AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.doze.DozeTriggers$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements DozeHost.Callback {
        AnonymousClass1() {
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onNotificationAlerted(Runnable runnable) {
            DozeTriggers.this.onNotification(runnable);
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onPowerSaveChanged(boolean z) {
            if (DozeTriggers.this.mDozeHost.isPowerSaveActive()) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE);
            } else if (DozeTriggers.this.mMachine.getState() != DozeMachine.State.DOZE || !DozeTriggers.this.mConfig.alwaysOnEnabled(-2)) {
            } else {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
            }
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onDozeSuppressedChanged(boolean z) {
            DozeMachine.State state;
            if (DozeTriggers.this.mConfig.alwaysOnEnabled(-2) && !z) {
                state = DozeMachine.State.DOZE_AOD;
            } else {
                state = DozeMachine.State.DOZE;
            }
            DozeTriggers.this.mMachine.requestState(state);
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onDozeFingerprintDown() {
            Log.d("DozeTriggers", "onDozeFingerprintDown: ");
            DozeMachine.State state = DozeTriggers.this.mMachine.getState();
            Log.d("DozeTriggers", "onDozeFingerprintDown: state = " + state);
            Log.d("DozeTriggers", "onDozeFingerprintDown: running = " + ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFingerprintDetectionRunning());
            if (!DozeTriggers.this.mAuthController.isFpIconVisible()) {
                DozeTriggers.this.mAuthController.showFingerprintIcon();
            }
            if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFingerprintDetectionRunning()) {
                DozeTriggers.this.cancelDelayDismissFingerprint();
                DozeTriggers.this.mAodInterruptRunnable = new Runnable() { // from class: com.android.systemui.doze.DozeTriggers$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DozeTriggers.AnonymousClass1.this.lambda$onDozeFingerprintDown$0();
                    }
                };
                if (state == DozeMachine.State.DOZE_AOD_PAUSING || state == DozeMachine.State.DOZE_AOD_PAUSED) {
                    DozeTriggers.this.switchAODPowerMode(1);
                    DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
                } else {
                    DozeTriggers.this.mAodInterruptRunnable.run();
                    DozeTriggers.this.mAodInterruptRunnable = null;
                }
            }
            DozeTriggers.this.delayDismissFingerprint();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDozeFingerprintDown$0() {
            Log.d("DozeTriggers", "onDozeFingerprintDown: off finger auth");
            DozeTriggers.this.mAuthController.doHBM(true);
            RectF sensorRectF = DozeTriggers.this.mAuthController.getSensorRectF();
            DozeTriggers.this.mAuthController.onAodInterrupt((int) sensorRectF.centerX(), (int) sensorRectF.centerY(), sensorRectF.width(), sensorRectF.height());
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onDozeFingerprintUp() {
            Log.d("DozeTriggers", "onDozeFingerprintUp: ");
            DozeTriggers.this.mAuthController.onCancelUdfps();
            DozeTriggers.this.mAuthController.doHBM(false);
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onDozeFingerprintRunningStateChanged() {
            Log.d("DozeTriggers", "onDozeFingerprintRunningStateChanged: ");
            DozeTriggers.this.cancelDelayDismissFingerprint();
            DozeTriggers.this.delayDismissFingerprint();
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onTapWake() {
            Log.d("DozeTriggers", "onTapWake: ");
            DozeTriggers.this.onSensor(9, -1.0f, -1.0f, new float[]{1.0f});
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onLiftWake() {
            Log.d("DozeTriggers", "onLiftWake: ");
            DozeTriggers.this.onSensor(3, -1.0f, -1.0f, new float[]{1.0f});
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onMotion() {
            Log.d("DozeTriggers", "onMotion: ");
            DozeTriggers.this.onSensor(2, -1.0f, -1.0f, new float[]{1.0f});
        }
    }

    private void delayEnterAndExitSleepMode() {
        long j;
        long j2;
        if (((AODController) Dependency.get(AODController.class)).isAllDay()) {
            return;
        }
        Date time = Calendar.getInstance().getTime();
        int hours = time.getHours();
        int minutes = time.getMinutes();
        int seconds = time.getSeconds();
        int intValue = Integer.valueOf(((AODController) Dependency.get(AODController.class)).getAODEndTime().substring(0, 2)).intValue();
        int intValue2 = Integer.valueOf(((AODController) Dependency.get(AODController.class)).getAODEndTime().substring(2, 4)).intValue();
        int intValue3 = Integer.valueOf(((AODController) Dependency.get(AODController.class)).getAODStartTime().substring(0, 2)).intValue();
        int intValue4 = Integer.valueOf(((AODController) Dependency.get(AODController.class)).getAODStartTime().substring(2, 4)).intValue();
        if (((AODController) Dependency.get(AODController.class)).checkNightMode()) {
            Log.d("DozeTriggers", "delayEnterAndExitSleepMode: night mode");
            j2 = ((((hours <= intValue3 ? intValue3 - hours : intValue3 + (24 - hours)) * 60 * 60) + ((intValue4 - minutes) * 60)) * 1000) + ((0 - seconds) * 1000);
            j = 0;
        } else {
            long j3 = ((((intValue >= hours ? intValue - hours : (24 - hours) + intValue) * 60 * 60) + ((intValue2 - minutes) * 60)) * 1000) + ((0 - seconds) * 1000);
            Log.d("DozeTriggers", "delayEnterAndExitSleepMode: not night mode  duration = " + j3);
            j = j3 + 0;
            j2 = j3 + ((long) (((24 - intValue) + intValue3) * 60 * 60 * 1000));
        }
        Log.d("DozeTriggers", "delayEnterAndExitSleepMode now = " + time + ", enterSleepModeDelayMs = " + j + ", exitSleepModeDelayMs = " + j2);
        if (j > 0) {
            Log.d("DozeTriggers", "delay enter sleep mode enterSleepModeDelayMs = " + j);
            if (this.mEnterSleepModePendingIntent == null) {
                this.mEnterSleepModePendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.nothingos.aod.sleep.enter"), 67108864);
            }
            this.mAlarmManager.cancel(this.mEnterSleepModePendingIntent);
            this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j, this.mEnterSleepModePendingIntent);
        }
        if (j2 <= 0) {
            return;
        }
        Log.d("DozeTriggers", "delay exit sleep mode exitSleepModeDelayMs = " + j2);
        if (this.mExitSleepModePendingIntent == null) {
            this.mExitSleepModePendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.nothingos.aod.sleep.exit"), 67108864);
        }
        this.mAlarmManager.cancel(this.mExitSleepModePendingIntent);
        this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j2, this.mExitSleepModePendingIntent);
    }

    private void cancelDelayEnterAndExitSleepMode() {
        Log.d("DozeTriggers", "cancelDelayEnterSleepMode");
        PendingIntent pendingIntent = this.mEnterSleepModePendingIntent;
        if (pendingIntent != null) {
            this.mAlarmManager.cancel(pendingIntent);
        }
        PendingIntent pendingIntent2 = this.mExitSleepModePendingIntent;
        if (pendingIntent2 != null) {
            this.mAlarmManager.cancel(pendingIntent2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void delayDismissFingerprint() {
        Log.d("DozeTriggers", "delayDismissFingerprint visible = " + this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser()));
        if (!this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
            return;
        }
        if (this.mDelayDismissFpPendingIntent == null) {
            this.mDelayDismissFpPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.nothingos.aod.dismiss.fingerprint"), 67108864);
        }
        this.mAlarmManager.cancel(this.mDelayDismissFpPendingIntent);
        this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + 5000, this.mDelayDismissFpPendingIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelDelayDismissFingerprint() {
        Log.d("DozeTriggers", "cancelDelayDismissFingerprint");
        PendingIntent pendingIntent = this.mDelayDismissFpPendingIntent;
        if (pendingIntent != null) {
            this.mAlarmManager.cancel(pendingIntent);
        }
    }

    private void triggerFpMotion(float[] fArr) {
        if (fArr[0] != 1.0f || !((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFingerprintDetectionRunning()) {
            return;
        }
        DozeMachine.State state = this.mMachine.getState();
        DozeMachine.State state2 = DozeMachine.State.DOZE_AOD;
        if (state != state2) {
            this.mMachine.requestState(state2);
        }
        this.mAuthController.showFingerprintIcon();
        cancelDelayDismissFingerprint();
        delayDismissFingerprint();
    }

    private void closeDisplayInversion(DozeMachine.State state) {
        Log.e("closeDisplayInversion", "newState==" + state);
        int resolverInt = getResolverInt("turn_off_invert_or_aod", 0);
        if (state != DozeMachine.State.FINISH && state != DozeMachine.State.INITIALIZED) {
            if (resolverInt != 0) {
                return;
            }
            if (getResolverInt("accessibility_display_inversion_enabled", 0) == 1) {
                if (getResolverInt("doze_always_on", 0) == 1) {
                    putResolverInt("doze_always_on", 0);
                    resolverInt = 2;
                } else {
                    resolverInt = 1;
                }
                ((NTColorController) Dependency.get(NTColorController.class)).resetInversion();
            }
            putResolverInt("turn_off_invert_or_aod", resolverInt);
        } else if (resolverInt == 0) {
        } else {
            if (resolverInt == 2) {
                putResolverInt("doze_always_on", 1);
            }
            if (resolverInt == 1 || resolverInt == 2) {
                if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isKeyguardVisible() && ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isUdfpsEnrolled()) {
                    Log.i("DozeTriggers", "Don't restoreInversion!");
                } else {
                    ((NTColorController) Dependency.get(NTColorController.class)).restoreInversion();
                }
            }
            putResolverInt("turn_off_invert_or_aod", 0);
        }
    }

    private void putResolverInt(String str, int i) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), str, i);
    }

    private int getResolverInt(String str, int i) {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), str, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchAODPowerMode(int i) {
        Log.d("DozeTriggers", "switchAODPowerMode " + i);
        SurfaceControl.setDisplayPowerMode(SurfaceControl.getInternalDisplayToken(), i);
    }
}
