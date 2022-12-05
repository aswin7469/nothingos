package com.android.systemui.doze;

import android.os.Handler;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import javax.inject.Provider;
/* loaded from: classes.dex */
public class DozeScreenState implements DozeMachine.Part {
    private static final boolean DEBUG = DozeService.DEBUG;
    private final AuthController mAuthController;
    private final DozeHost mDozeHost;
    private final DozeLog mDozeLog;
    private final DozeScreenBrightness mDozeScreenBrightness;
    private final DozeMachine.Service mDozeService;
    private final Handler mHandler;
    private final DozeParameters mParameters;
    private UdfpsController mUdfpsController;
    private final Provider<UdfpsController> mUdfpsControllerProvider;
    private SettableWakeLock mWakeLock;
    private final Runnable mApplyPendingScreenState = new Runnable() { // from class: com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            DozeScreenState.this.applyPendingScreenState();
        }
    };
    private int mPendingScreenState = 0;

    public DozeScreenState(DozeMachine.Service service, Handler handler, DozeHost dozeHost, DozeParameters dozeParameters, WakeLock wakeLock, AuthController authController, Provider<UdfpsController> provider, DozeLog dozeLog, DozeScreenBrightness dozeScreenBrightness) {
        this.mDozeService = service;
        this.mHandler = handler;
        this.mParameters = dozeParameters;
        this.mDozeHost = dozeHost;
        this.mWakeLock = new SettableWakeLock(wakeLock, "DozeScreenState");
        this.mAuthController = authController;
        this.mUdfpsControllerProvider = provider;
        this.mDozeLog = dozeLog;
        this.mDozeScreenBrightness = dozeScreenBrightness;
        updateUdfpsController();
        if (this.mUdfpsController == null) {
            authController.addCallback(new AuthController.Callback() { // from class: com.android.systemui.doze.DozeScreenState.1
                @Override // com.android.systemui.biometrics.AuthController.Callback
                public void onAllAuthenticatorsRegistered() {
                    DozeScreenState.this.updateUdfpsController();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUdfpsController() {
        if (this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
            this.mUdfpsController = this.mUdfpsControllerProvider.mo1933get();
        } else {
            this.mUdfpsController = null;
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        UdfpsController udfpsController;
        final int screenState = state2.screenState(this.mParameters);
        this.mDozeHost.cancelGentleSleep();
        boolean z = false;
        if (state2 == DozeMachine.State.FINISH) {
            this.mPendingScreenState = 0;
            this.mHandler.removeCallbacks(this.mApplyPendingScreenState);
            lambda$transitionTo$0(screenState);
            this.mWakeLock.setAcquired(false);
        } else if (screenState == 0) {
        } else {
            boolean hasCallbacks = this.mHandler.hasCallbacks(this.mApplyPendingScreenState);
            int i = 1;
            boolean z2 = state == DozeMachine.State.DOZE_PULSE_DONE && state2.isAlwaysOn();
            DozeMachine.State state3 = DozeMachine.State.DOZE_AOD_PAUSED;
            boolean z3 = (state == state3 || state == DozeMachine.State.DOZE) && state2.isAlwaysOn();
            boolean z4 = (state.isAlwaysOn() && state2 == DozeMachine.State.DOZE) || (state == DozeMachine.State.DOZE_AOD_PAUSING && state2 == state3);
            boolean z5 = state == DozeMachine.State.INITIALIZED;
            if (!hasCallbacks && !z5 && !z2 && !z3) {
                if (z4) {
                    this.mDozeHost.prepareForGentleSleep(new Runnable() { // from class: com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            DozeScreenState.this.lambda$transitionTo$0(screenState);
                        }
                    });
                    return;
                } else {
                    lambda$transitionTo$0(screenState);
                    return;
                }
            }
            this.mPendingScreenState = screenState;
            DozeMachine.State state4 = DozeMachine.State.DOZE_AOD;
            boolean z6 = state2 == state4 && this.mParameters.shouldControlScreenOff() && !z3;
            if (state2 == state4 && (udfpsController = this.mUdfpsController) != null && udfpsController.isFingerDown()) {
                z = true;
            }
            if (z6 || z) {
                this.mWakeLock.setAcquired(true);
            }
            if (!hasCallbacks) {
                if (DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Display state changed to ");
                    sb.append(screenState);
                    sb.append(" delayed by ");
                    if (z6) {
                        i = 4000;
                    }
                    sb.append(i);
                    Log.d("DozeScreenState", sb.toString());
                }
                if (z6) {
                    this.mHandler.postDelayed(this.mApplyPendingScreenState, 4000L);
                } else if (z) {
                    this.mDozeLog.traceDisplayStateDelayedByUdfps(this.mPendingScreenState);
                    this.mHandler.postDelayed(this.mApplyPendingScreenState, 1200L);
                } else {
                    this.mHandler.post(this.mApplyPendingScreenState);
                }
            } else if (!DEBUG) {
            } else {
                Log.d("DozeScreenState", "Pending display state change to " + screenState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyPendingScreenState() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null && udfpsController.isFingerDown()) {
            this.mDozeLog.traceDisplayStateDelayedByUdfps(this.mPendingScreenState);
            this.mHandler.postDelayed(this.mApplyPendingScreenState, 1200L);
            return;
        }
        lambda$transitionTo$0(this.mPendingScreenState);
        this.mPendingScreenState = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: applyScreenState */
    public void lambda$transitionTo$0(int i) {
        if (i != 0) {
            if (DEBUG) {
                Log.d("DozeScreenState", "setDozeScreenState(" + i + ")");
            }
            this.mDozeService.setDozeScreenState(i);
            if (i == 3) {
                this.mDozeScreenBrightness.updateBrightnessAndReady(false);
            }
            this.mPendingScreenState = 0;
            this.mWakeLock.setAcquired(false);
        }
    }
}
