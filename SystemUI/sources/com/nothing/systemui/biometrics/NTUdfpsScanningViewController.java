package com.nothing.systemui.biometrics;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.icu.lang.UCharacter;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;
import javax.inject.Provider;

public class NTUdfpsScanningViewController extends ViewController<LottieAnimationView> implements KeyguardStateController.Callback, WakefulnessLifecycle.Observer {
    private static final String TAG = "ScanningViewController";
    private AuthController mAuthController;
    private AuthController.Callback mAuthControllerCallback = new AuthController.Callback() {
        public void onAllAuthenticatorsRegistered() {
            NTLogUtil.m1682i(NTUdfpsScanningViewController.TAG, "onAllAuthenticatorsRegistered");
            NTUdfpsScanningViewController.this.updateSensorLocation();
            NTUdfpsScanningViewController.this.updateUdfpsDependentParams();
        }
    };
    /* access modifiers changed from: private */
    public PointF mFingerprintSensorLocation;
    private KeyguardStateController mKeyguardStateController;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
        }

        public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
            if (biometricSourceType == BiometricSourceType.FINGERPRINT && NTUdfpsScanningViewController.this.mFingerprintSensorLocation != null) {
                NTUdfpsScanningViewController.this.stopAnimation();
            }
        }
    };
    private int mSensorRadius;
    private UdfpsController mUdfpsController;
    private UdfpsController.Callback mUdfpsControllerCallback = new UdfpsController.Callback() {
        public void onFingerDown() {
            NTLogUtil.m1682i(NTUdfpsScanningViewController.TAG, "onFingerDown mFingerprintSensorLocation=" + NTUdfpsScanningViewController.this.mFingerprintSensorLocation);
            if (NTUdfpsScanningViewController.this.mFingerprintSensorLocation == null) {
                NTLogUtil.m1682i(NTUdfpsScanningViewController.TAG, "fingerprintSensorLocation=null onFingerDown. Skip showing dwell ripple");
                return;
            }
            ((LottieAnimationView) NTUdfpsScanningViewController.this.mView).setVisibility(0);
            ((LottieAnimationView) NTUdfpsScanningViewController.this.mView).playAnimation();
        }

        public void onFingerUp() {
            NTLogUtil.m1680d(NTUdfpsScanningViewController.TAG, "onFingerUp: ");
            NTUdfpsScanningViewController.this.stopAnimation();
        }
    };
    private Provider<UdfpsController> mUdfpsControllerProvider;
    private int mUdfpsScanningViewSize = UCharacter.UnicodeBlock.NYIAKENG_PUACHUE_HMONG_ID;
    private WakefulnessLifecycle mWakefulnessLifecycle;

    public void onStartedGoingToSleep() {
    }

    @Inject
    public NTUdfpsScanningViewController(Context context, AuthController authController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, WakefulnessLifecycle wakefulnessLifecycle, CommandRegistry commandRegistry, NotificationShadeWindowController notificationShadeWindowController, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController, Provider<UdfpsController> provider, StatusBarStateController statusBarStateController, LottieAnimationView lottieAnimationView) {
        super(lottieAnimationView);
        this.mAuthController = authController;
        this.mUdfpsControllerProvider = provider;
        updateUdfpsController();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
    }

    private void updateUdfpsController() {
        if (this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
            this.mUdfpsController = this.mUdfpsControllerProvider.get();
        } else {
            this.mUdfpsController = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        NTLogUtil.m1681e(TAG, "onInit=");
    }

    public void onViewAttached() {
        this.mAuthController.addCallback(this.mAuthControllerCallback);
        updateSensorLocation();
        updateUdfpsDependentParams();
        if (this.mUdfpsController != null) {
            NTLogUtil.m1682i(TAG, "mUdfpsController addCallback");
            this.mUdfpsController.addCallback(this.mUdfpsControllerCallback);
        }
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mKeyguardStateController.addCallback(this);
        this.mWakefulnessLifecycle.addObserver(this);
        ((LottieAnimationView) this.mView).setVisibility(8);
    }

    public void onViewDetached() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.removeCallback(this.mUdfpsControllerCallback);
        }
        this.mAuthController.removeCallback(this.mAuthControllerCallback);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mKeyguardStateController.removeCallback(this);
        this.mWakefulnessLifecycle.removeObserver(this);
    }

    public void onKeyguardFadingAwayChanged() {
        this.mKeyguardStateController.isKeyguardFadingAway();
    }

    /* access modifiers changed from: private */
    public void stopAnimation() {
        ((LottieAnimationView) this.mView).setVisibility(8);
        ((LottieAnimationView) this.mView).pauseAnimation();
        ((LottieAnimationView) this.mView).setProgress(0.0f);
    }

    /* access modifiers changed from: private */
    public void updateSensorLocation() {
        this.mFingerprintSensorLocation = this.mAuthController.getFingerprintSensorLocation();
        NTLogUtil.m1682i(TAG, "updateSensorLocation fingerprintSensorLocation= " + this.mFingerprintSensorLocation);
    }

    /* access modifiers changed from: private */
    public void updateUdfpsDependentParams() {
        NTLogUtil.m1682i(TAG, "updateUdfpsDependentParams");
    }

    private void updateViewLayoutParams() {
        NTLogUtil.m1682i(TAG, "updateViewLayoutParams fingerprintSensorLocation= " + this.mFingerprintSensorLocation + ", mUdfpsScanningViewRadius=" + this.mUdfpsScanningViewSize);
        ViewGroup.LayoutParams layoutParams = ((LottieAnimationView) this.mView).getLayoutParams();
        layoutParams.width = this.mUdfpsScanningViewSize;
        layoutParams.height = this.mUdfpsScanningViewSize;
        ((LottieAnimationView) this.mView).setLayoutParams(layoutParams);
        if (this.mFingerprintSensorLocation != null) {
            ((LottieAnimationView) this.mView).setTranslationX(this.mFingerprintSensorLocation.x - ((float) (this.mUdfpsScanningViewSize / 2)));
            ((LottieAnimationView) this.mView).setTranslationY(this.mFingerprintSensorLocation.y - ((float) (this.mUdfpsScanningViewSize / 2)));
            return;
        }
        ((LottieAnimationView) this.mView).setTranslationX((float) (540 - (this.mUdfpsScanningViewSize / 2)));
        ((LottieAnimationView) this.mView).setTranslationY((float) (2170 - (this.mUdfpsScanningViewSize / 2)));
    }
}
