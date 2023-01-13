package com.android.keyguard;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.C1894R;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import javax.inject.Inject;

public abstract class KeyguardInputViewController<T extends KeyguardInputView> extends ViewController<T> implements KeyguardSecurityView {
    private final EmergencyButton mEmergencyButton;
    private final EmergencyButtonController mEmergencyButtonController;
    private final KeyguardSecurityCallback mKeyguardSecurityCallback;
    private KeyguardSecurityCallback mNullCallback = new KeyguardSecurityCallback() {
        public void dismiss(boolean z, int i, KeyguardSecurityModel.SecurityMode securityMode) {
        }

        public void dismiss(boolean z, int i, boolean z2, KeyguardSecurityModel.SecurityMode securityMode) {
        }

        public boolean isVerifyUnlockOnly() {
            return false;
        }

        public void onUserInput() {
        }

        public void reportUnlockAttempt(int i, boolean z, int i2) {
        }

        public void reset() {
        }

        public void userActivity() {
        }
    };
    private boolean mPaused;
    private final KeyguardSecurityModel.SecurityMode mSecurityMode;

    /* access modifiers changed from: protected */
    public void onViewAttached() {
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
    }

    public void reset() {
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
    }

    public void showPromptReason(int i) {
    }

    protected KeyguardInputViewController(T t, KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback, EmergencyButtonController emergencyButtonController) {
        super(t);
        EmergencyButton emergencyButton;
        this.mSecurityMode = securityMode;
        this.mKeyguardSecurityCallback = keyguardSecurityCallback;
        if (t == null) {
            emergencyButton = null;
        } else {
            emergencyButton = (EmergencyButton) t.findViewById(C1894R.C1898id.emergency_call_button);
        }
        this.mEmergencyButton = emergencyButton;
        this.mEmergencyButtonController = emergencyButtonController;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.mEmergencyButtonController.init();
    }

    /* access modifiers changed from: package-private */
    public KeyguardSecurityModel.SecurityMode getSecurityMode() {
        return this.mSecurityMode;
    }

    /* access modifiers changed from: protected */
    public KeyguardSecurityCallback getKeyguardSecurityCallback() {
        if (this.mPaused) {
            return this.mNullCallback;
        }
        return this.mKeyguardSecurityCallback;
    }

    public void onPause() {
        this.mPaused = true;
    }

    public void onResume(int i) {
        this.mPaused = false;
    }

    public void reloadColors() {
        EmergencyButton emergencyButton = this.mEmergencyButton;
        if (emergencyButton != null) {
            emergencyButton.reloadColors();
        }
    }

    public void startAppearAnimation() {
        ((KeyguardInputView) this.mView).startAppearAnimation();
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        return ((KeyguardInputView) this.mView).startDisappearAnimation(runnable);
    }

    public CharSequence getTitle() {
        return ((KeyguardInputView) this.mView).getTitle();
    }

    public int getIndexIn(KeyguardSecurityViewFlipper keyguardSecurityViewFlipper) {
        return keyguardSecurityViewFlipper.indexOfChild(this.mView);
    }

    public static class Factory {
        private final DevicePostureController mDevicePostureController;
        private final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
        private final FalsingCollector mFalsingCollector;
        private final InputMethodManager mInputMethodManager;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final KeyguardViewController mKeyguardViewController;
        private final LatencyTracker mLatencyTracker;
        private final LiftToActivateListener mLiftToActivateListener;
        private final LockPatternUtils mLockPatternUtils;
        private final DelayableExecutor mMainExecutor;
        private final KeyguardMessageAreaController.Factory mMessageAreaControllerFactory;
        private final Resources mResources;
        private final TelephonyManager mTelephonyManager;

        @Inject
        public Factory(KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, LatencyTracker latencyTracker, KeyguardMessageAreaController.Factory factory, InputMethodManager inputMethodManager, @Main DelayableExecutor delayableExecutor, @Main Resources resources, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController.Factory factory2, DevicePostureController devicePostureController, KeyguardViewController keyguardViewController) {
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mLockPatternUtils = lockPatternUtils;
            this.mLatencyTracker = latencyTracker;
            this.mMessageAreaControllerFactory = factory;
            this.mInputMethodManager = inputMethodManager;
            this.mMainExecutor = delayableExecutor;
            this.mResources = resources;
            this.mLiftToActivateListener = liftToActivateListener;
            this.mTelephonyManager = telephonyManager;
            this.mEmergencyButtonControllerFactory = factory2;
            this.mFalsingCollector = falsingCollector;
            this.mDevicePostureController = devicePostureController;
            this.mKeyguardViewController = keyguardViewController;
        }

        public KeyguardInputViewController create(KeyguardInputView keyguardInputView, KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback) {
            KeyguardInputView keyguardInputView2 = keyguardInputView;
            EmergencyButtonController create = this.mEmergencyButtonControllerFactory.create((EmergencyButton) keyguardInputView2.findViewById(C1894R.C1898id.emergency_call_button));
            if (keyguardInputView2 instanceof KeyguardPatternView) {
                return new KeyguardPatternViewController((KeyguardPatternView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mLatencyTracker, this.mFalsingCollector, create, this.mMessageAreaControllerFactory, this.mDevicePostureController);
            } else if (keyguardInputView2 instanceof KeyguardPasswordView) {
                return new KeyguardPasswordViewController((KeyguardPasswordView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mInputMethodManager, create, this.mMainExecutor, this.mResources, this.mFalsingCollector, this.mKeyguardViewController);
            } else if (keyguardInputView2 instanceof KeyguardPINView) {
                return new KeyguardPinViewController((KeyguardPINView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, create, this.mFalsingCollector, this.mDevicePostureController);
            } else if (keyguardInputView2 instanceof KeyguardSimPinView) {
                return new KeyguardSimPinViewController((KeyguardSimPinView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, this.mTelephonyManager, this.mFalsingCollector, create);
            } else if (keyguardInputView2 instanceof KeyguardSimPukView) {
                return new KeyguardSimPukViewController((KeyguardSimPukView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, this.mTelephonyManager, this.mFalsingCollector, create);
            } else {
                throw new RuntimeException("Unable to find controller for " + keyguardInputView2);
            }
        }
    }
}
