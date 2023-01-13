package com.android.systemui.doze;

import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.dagger.DozeScope;
import com.android.systemui.doze.dagger.WrappedService;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import javax.inject.Inject;
import javax.inject.Provider;

@DozeScope
public class DozeScreenState implements DozeMachine.Part {
    private static final boolean DEBUG = DozeService.DEBUG;
    private static final int ENTER_DOZE_DELAY = 4000;
    private static final int ENTER_DOZE_DELAY_BY_LANDSCAPE_SCREEN_OFF = SystemProperties.getInt("debug.system.landscape_doze_delay", 400);
    public static final int ENTER_DOZE_HIDE_WALLPAPER_DELAY = 2500;
    private static final int ENTER_SCREEN_OFF_WITH_ANIMATION_DELAY = 500;
    private static final String TAG = "DozeScreenState";
    public static final int UDFPS_DISPLAY_STATE_DELAY = 1200;
    private final Runnable mApplyPendingScreenState = new DozeScreenState$$ExternalSyntheticLambda0(this);
    private final AuthController mAuthController;
    private final AuthController.Callback mAuthControllerCallback;
    private final DozeHost mDozeHost;
    private final DozeLog mDozeLog;
    private final DozeScreenBrightness mDozeScreenBrightness;
    private final DozeMachine.Service mDozeService;
    private final Handler mHandler;
    private boolean mIsLandscapeScreenOff = false;
    private NotificationPanelViewController mNotificationPanelViewController;
    private final DozeParameters mParameters;
    private int mPendingScreenState = 0;
    private UdfpsController mUdfpsController;
    private final Provider<UdfpsController> mUdfpsControllerProvider;
    private SettableWakeLock mWakeLock;

    @Inject
    public DozeScreenState(@WrappedService DozeMachine.Service service, @Main Handler handler, DozeHost dozeHost, DozeParameters dozeParameters, WakeLock wakeLock, AuthController authController, Provider<UdfpsController> provider, DozeLog dozeLog, DozeScreenBrightness dozeScreenBrightness) {
        C20641 r0 = new AuthController.Callback() {
            public void onAllAuthenticatorsRegistered() {
                DozeScreenState.this.updateUdfpsController();
            }

            public void onEnrollmentsChanged() {
                DozeScreenState.this.updateUdfpsController();
            }
        };
        this.mAuthControllerCallback = r0;
        this.mDozeService = service;
        this.mHandler = handler;
        this.mParameters = dozeParameters;
        this.mDozeHost = dozeHost;
        this.mWakeLock = new SettableWakeLock(wakeLock, TAG);
        this.mAuthController = authController;
        this.mUdfpsControllerProvider = provider;
        this.mDozeLog = dozeLog;
        this.mDozeScreenBrightness = dozeScreenBrightness;
        updateUdfpsController();
        if (this.mUdfpsController == null) {
            authController.addCallback(r0);
        }
        this.mNotificationPanelViewController = ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).getNotificationPanelViewController();
    }

    /* access modifiers changed from: private */
    public void updateUdfpsController() {
        if (this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
            this.mUdfpsController = this.mUdfpsControllerProvider.get();
        } else {
            this.mUdfpsController = null;
        }
    }

    public void destroy() {
        this.mAuthController.removeCallback(this.mAuthControllerCallback);
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        UdfpsController udfpsController;
        int screenState = state2.screenState(this.mParameters);
        this.mDozeHost.cancelGentleSleep();
        if (state2 == DozeMachine.State.FINISH) {
            this.mPendingScreenState = 0;
            this.mHandler.removeCallbacks(this.mApplyPendingScreenState);
            m2740lambda$transitionTo$0$comandroidsystemuidozeDozeScreenState(screenState);
            this.mWakeLock.setAcquired(false);
        } else if (screenState != 0) {
            boolean hasCallbacks = this.mHandler.hasCallbacks(this.mApplyPendingScreenState);
            boolean z = state == DozeMachine.State.DOZE_PULSE_DONE && state2.isAlwaysOn();
            boolean z2 = (state == DozeMachine.State.DOZE_AOD_PAUSED || state == DozeMachine.State.DOZE) && state2.isAlwaysOn();
            boolean z3 = (state.isAlwaysOn() && state2 == DozeMachine.State.DOZE) || (state == DozeMachine.State.DOZE_AOD_PAUSING && state2 == DozeMachine.State.DOZE_AOD_PAUSED);
            boolean z4 = state == DozeMachine.State.INITIALIZED;
            if (z4) {
                this.mIsLandscapeScreenOff = false;
            }
            if (hasCallbacks || z4 || z || z2) {
                this.mPendingScreenState = screenState;
                boolean z5 = state2 == DozeMachine.State.DOZE_AOD && this.mParameters.shouldDelayDisplayDozeTransition() && !z2;
                boolean z6 = state2 == DozeMachine.State.DOZE_AOD && (udfpsController = this.mUdfpsController) != null && udfpsController.isFingerDown();
                boolean z7 = state2 == DozeMachine.State.DOZE && ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).shouldPlayOnOffAnimation();
                NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
                if (notificationPanelViewController != null) {
                    this.mIsLandscapeScreenOff = notificationPanelViewController.isLandscapeScreenOff();
                    this.mNotificationPanelViewController.setIsLandscapeOff(false);
                }
                if (!hasCallbacks) {
                    if (DEBUG) {
                        Log.d(TAG, "Display state changed to " + screenState + " delayed by " + (z5 ? ENTER_DOZE_DELAY : 1));
                    }
                    if (z5) {
                        if (z4) {
                            m2740lambda$transitionTo$0$comandroidsystemuidozeDozeScreenState(2);
                            this.mPendingScreenState = screenState;
                        }
                        this.mHandler.postDelayed(this.mApplyPendingScreenState, 4000);
                    } else if (z6) {
                        this.mDozeLog.traceDisplayStateDelayedByUdfps(this.mPendingScreenState);
                        this.mHandler.postDelayed(this.mApplyPendingScreenState, NotificationTapHelper.DOUBLE_TAP_TIMEOUT_MS);
                    } else if (z7 && !this.mIsLandscapeScreenOff) {
                        if (z4) {
                            m2740lambda$transitionTo$0$comandroidsystemuidozeDozeScreenState(2);
                            this.mPendingScreenState = screenState;
                        }
                        this.mHandler.postDelayed(this.mApplyPendingScreenState, 500);
                    } else if (this.mIsLandscapeScreenOff) {
                        this.mDozeService.setDozeScreenState(1);
                        this.mHandler.postDelayed(this.mApplyPendingScreenState, (long) ENTER_DOZE_DELAY_BY_LANDSCAPE_SCREEN_OFF);
                        this.mIsLandscapeScreenOff = false;
                    } else {
                        this.mHandler.post(this.mApplyPendingScreenState);
                    }
                } else if (DEBUG) {
                    Log.d(TAG, "Pending display state change to " + screenState);
                }
                if (z5 || z6 || this.mIsLandscapeScreenOff || z7) {
                    this.mWakeLock.setAcquired(true);
                }
            } else if (z3) {
                this.mDozeHost.prepareForGentleSleep(new DozeScreenState$$ExternalSyntheticLambda1(this, screenState));
            } else {
                m2740lambda$transitionTo$0$comandroidsystemuidozeDozeScreenState(screenState);
            }
        }
    }

    /* access modifiers changed from: private */
    public void applyPendingScreenState() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null || !udfpsController.isFingerDown()) {
            m2740lambda$transitionTo$0$comandroidsystemuidozeDozeScreenState(this.mPendingScreenState);
            this.mPendingScreenState = 0;
            return;
        }
        this.mDozeLog.traceDisplayStateDelayedByUdfps(this.mPendingScreenState);
        this.mHandler.postDelayed(this.mApplyPendingScreenState, NotificationTapHelper.DOUBLE_TAP_TIMEOUT_MS);
    }

    /* access modifiers changed from: private */
    /* renamed from: applyScreenState */
    public void m2740lambda$transitionTo$0$comandroidsystemuidozeDozeScreenState(int i) {
        if (i != 0) {
            if (DEBUG) {
                Log.d(TAG, "setDozeScreenState(" + i + NavigationBarInflaterView.KEY_CODE_END);
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
