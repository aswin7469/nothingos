package com.nothingos.keyguard;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.Log;
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
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public class UdfpsScanningViewController extends ViewController<LottieAnimationView> implements KeyguardStateController.Callback, WakefulnessLifecycle.Observer {
    private AuthController mAuthController;
    private PointF mFingerprintSensorLocation;
    private KeyguardStateController mKeyguardStateController;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private int mSensorRadius;
    private UdfpsController mUdfpsController;
    private Provider<UdfpsController> mUdfpsControllerProvider;
    private WakefulnessLifecycle mWakefulnessLifecycle;
    private int mUdfpsScanningViewSize = 295;
    private KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.nothingos.keyguard.UdfpsScanningViewController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
            if (biometricSourceType != BiometricSourceType.FINGERPRINT || UdfpsScanningViewController.this.mFingerprintSensorLocation == null) {
                return;
            }
            UdfpsScanningViewController.this.stopAnimation();
        }
    };
    private UdfpsController.Callback mUdfpsControllerCallback = new UdfpsController.Callback() { // from class: com.nothingos.keyguard.UdfpsScanningViewController.2
        @Override // com.android.systemui.biometrics.UdfpsController.Callback
        public void onFingerDown() {
            Log.i("ScanningViewController", "onFingerDown mFingerprintSensorLocation=" + UdfpsScanningViewController.this.mFingerprintSensorLocation);
            if (UdfpsScanningViewController.this.mFingerprintSensorLocation != null) {
                ((LottieAnimationView) ((ViewController) UdfpsScanningViewController.this).mView).setVisibility(0);
                ((LottieAnimationView) ((ViewController) UdfpsScanningViewController.this).mView).playAnimation();
                return;
            }
            Log.i("ScanningViewController", "fingerprintSensorLocation=null onFingerDown. Skip showing dwell ripple");
        }

        @Override // com.android.systemui.biometrics.UdfpsController.Callback
        public void onFingerUp() {
            Log.d("ScanningViewController", "onFingerUp: ");
            UdfpsScanningViewController.this.stopAnimation();
        }
    };
    private AuthController.Callback mAuthControllerCallback = new AuthController.Callback() { // from class: com.nothingos.keyguard.UdfpsScanningViewController.3
        @Override // com.android.systemui.biometrics.AuthController.Callback
        public void onAllAuthenticatorsRegistered() {
            Log.i("ScanningViewController", "onAllAuthenticatorsRegistered");
            UdfpsScanningViewController.this.updateSensorLocation();
            UdfpsScanningViewController.this.updateUdfpsDependentParams();
        }
    };

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedGoingToSleep() {
    }

    public UdfpsScanningViewController(StatusBar statusBar, Context context, AuthController authController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, WakefulnessLifecycle wakefulnessLifecycle, CommandRegistry commandRegistry, NotificationShadeWindowController notificationShadeWindowController, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController, Provider<UdfpsController> provider, StatusBarStateController statusBarStateController, LottieAnimationView lottieAnimationView) {
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
            this.mUdfpsController = this.mUdfpsControllerProvider.mo1933get();
        } else {
            this.mUdfpsController = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        Log.e("ScanningViewController", "onInit=");
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        this.mAuthController.addCallback(this.mAuthControllerCallback);
        updateSensorLocation();
        updateUdfpsDependentParams();
        if (this.mUdfpsController != null) {
            Log.i("ScanningViewController", "mUdfpsController addCallback");
            this.mUdfpsController.addCallback(this.mUdfpsControllerCallback);
        }
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mKeyguardStateController.addCallback(this);
        this.mWakefulnessLifecycle.addObserver(this);
        ((LottieAnimationView) this.mView).setVisibility(8);
    }

    @Override // com.android.systemui.util.ViewController
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

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onKeyguardFadingAwayChanged() {
        this.mKeyguardStateController.isKeyguardFadingAway();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAnimation() {
        ((LottieAnimationView) this.mView).setVisibility(8);
        ((LottieAnimationView) this.mView).pauseAnimation();
        ((LottieAnimationView) this.mView).setProgress(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSensorLocation() {
        this.mFingerprintSensorLocation = this.mAuthController.getFingerprintSensorLocation();
        Log.i("ScanningViewController", "updateSensorLocation fingerprintSensorLocation= " + this.mFingerprintSensorLocation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUdfpsDependentParams() {
        Log.i("ScanningViewController", "updateUdfpsDependentParams");
        AuthController authController = this.mAuthController;
        if (authController == null || authController.getUdfpsProps().size() <= 0) {
            return;
        }
        FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mAuthController.getUdfpsProps().get(0);
        this.mSensorRadius = fingerprintSensorPropertiesInternal.sensorRadius;
        this.mUdfpsController = this.mUdfpsControllerProvider.mo1933get();
        PointF pointF = this.mFingerprintSensorLocation;
        pointF.x = fingerprintSensorPropertiesInternal.sensorLocationX;
        pointF.y = fingerprintSensorPropertiesInternal.sensorLocationY;
        updateViewLayoutParams();
        Log.i("ScanningViewController", "udfpsRadius= " + this.mSensorRadius + ", udfpsSensorLeft=" + fingerprintSensorPropertiesInternal.sensorLocationX + ", udfpsSensorTop=" + fingerprintSensorPropertiesInternal.sensorLocationY);
    }

    private void updateViewLayoutParams() {
        Log.i("ScanningViewController", "updateViewLayoutParams fingerprintSensorLocation= " + this.mFingerprintSensorLocation + ", mUdfpsScanningViewRadius=" + this.mUdfpsScanningViewSize);
        ViewGroup.LayoutParams layoutParams = ((LottieAnimationView) this.mView).getLayoutParams();
        int i = this.mUdfpsScanningViewSize;
        layoutParams.width = i;
        layoutParams.height = i;
        ((LottieAnimationView) this.mView).setLayoutParams(layoutParams);
        PointF pointF = this.mFingerprintSensorLocation;
        if (pointF != null) {
            ((LottieAnimationView) this.mView).setTranslationX(pointF.x - (this.mUdfpsScanningViewSize / 2));
            ((LottieAnimationView) this.mView).setTranslationY(this.mFingerprintSensorLocation.y - (this.mUdfpsScanningViewSize / 2));
            return;
        }
        ((LottieAnimationView) this.mView).setTranslationX(540 - (this.mUdfpsScanningViewSize / 2));
        ((LottieAnimationView) this.mView).setTranslationY(2170 - (this.mUdfpsScanningViewSize / 2));
    }
}
