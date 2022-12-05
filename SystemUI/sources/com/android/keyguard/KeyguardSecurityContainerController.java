package com.android.keyguard;

import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.metrics.LogMaker;
import android.util.Log;
import android.util.Slog;
import android.view.MotionEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class KeyguardSecurityContainerController extends ViewController<KeyguardSecurityContainer> implements KeyguardSecurityView {
    private final AdminSecondaryLockScreenController mAdminSecondaryLockScreenController;
    private final ConfigurationController mConfigurationController;
    private ConfigurationController.ConfigurationListener mConfigurationListener;
    private KeyguardSecurityModel.SecurityMode mCurrentSecurityMode;
    private final Gefingerpoken mGlobalTouchListener;
    private KeyguardSecurityCallback mKeyguardSecurityCallback;
    private final KeyguardStateController mKeyguardStateController;
    private int mLastOrientation;
    private final LockPatternUtils mLockPatternUtils;
    private final MetricsLogger mMetricsLogger;
    private final KeyguardSecurityContainer.SecurityCallback mSecurityCallback;
    private final KeyguardSecurityModel mSecurityModel;
    private final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
    private KeyguardSecurityContainer.SwipeListener mSwipeListener;
    private final UiEventLogger mUiEventLogger;
    private final KeyguardUpdateMonitor mUpdateMonitor;

    /* renamed from: com.android.keyguard.KeyguardSecurityContainerController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass2 implements KeyguardSecurityCallback {
        AnonymousClass2() {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void userActivity() {
            if (KeyguardSecurityContainerController.this.mSecurityCallback != null) {
                KeyguardSecurityContainerController.this.mSecurityCallback.userActivity();
            }
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void onUserInput() {
            KeyguardSecurityContainerController.this.mUpdateMonitor.cancelFaceAuth();
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void dismiss(boolean z, int i) {
            dismiss(z, i, false);
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void dismiss(boolean z, int i, boolean z2) {
            KeyguardSecurityContainerController.this.mSecurityCallback.dismiss(z, i, z2);
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void reportUnlockAttempt(int i, boolean z, int i2) {
            if (z) {
                SysUiStatsLog.write(64, 2);
                KeyguardSecurityContainerController.this.mLockPatternUtils.reportSuccessfulPasswordAttempt(i);
                ThreadUtils.postOnBackgroundThread(KeyguardSecurityContainerController$2$$ExternalSyntheticLambda0.INSTANCE);
            } else {
                SysUiStatsLog.write(64, 1);
                KeyguardSecurityContainerController.this.reportFailedUnlockAttempt(i, i2);
            }
            KeyguardSecurityContainerController.this.mMetricsLogger.write(new LogMaker(197).setType(z ? 10 : 11));
            KeyguardSecurityContainerController.this.mUiEventLogger.log(z ? KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_PASSWORD_SUCCESS : KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_PASSWORD_FAILURE);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$reportUnlockAttempt$0() {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException unused) {
            }
            System.gc();
            System.runFinalization();
            System.gc();
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void reset() {
            KeyguardSecurityContainerController.this.mSecurityCallback.reset();
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void onCancelClicked() {
            KeyguardSecurityContainerController.this.mSecurityCallback.onCancelClicked();
        }
    }

    private KeyguardSecurityContainerController(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityContainer.SecurityCallback securityCallback, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController) {
        super(keyguardSecurityContainer);
        this.mLastOrientation = 0;
        this.mCurrentSecurityMode = KeyguardSecurityModel.SecurityMode.Invalid;
        this.mGlobalTouchListener = new Gefingerpoken() { // from class: com.android.keyguard.KeyguardSecurityContainerController.1
            private MotionEvent mTouchDown;

            @Override // com.android.systemui.Gefingerpoken
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                return false;
            }

            @Override // com.android.systemui.Gefingerpoken
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 0) {
                    MotionEvent motionEvent2 = this.mTouchDown;
                    if (motionEvent2 != null) {
                        motionEvent2.recycle();
                        this.mTouchDown = null;
                    }
                    this.mTouchDown = MotionEvent.obtain(motionEvent);
                    return false;
                } else if (this.mTouchDown == null) {
                    return false;
                } else {
                    if (motionEvent.getActionMasked() != 1 && motionEvent.getActionMasked() != 3) {
                        return false;
                    }
                    this.mTouchDown.recycle();
                    this.mTouchDown = null;
                    return false;
                }
            }
        };
        this.mKeyguardSecurityCallback = new AnonymousClass2();
        this.mSwipeListener = new KeyguardSecurityContainer.SwipeListener() { // from class: com.android.keyguard.KeyguardSecurityContainerController.3
            @Override // com.android.keyguard.KeyguardSecurityContainer.SwipeListener
            public void onSwipeUp() {
                if (!KeyguardSecurityContainerController.this.mUpdateMonitor.isFaceDetectionRunning()) {
                    KeyguardSecurityContainerController.this.mUpdateMonitor.requestFaceAuth(true);
                    KeyguardSecurityContainerController.this.mKeyguardSecurityCallback.userActivity();
                    KeyguardSecurityContainerController.this.showMessage(null, null);
                }
            }
        };
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.KeyguardSecurityContainerController.4
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                KeyguardSecurityContainerController.this.mSecurityViewFlipperController.reloadColors();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onUiModeChanged() {
                KeyguardSecurityContainerController.this.mSecurityViewFlipperController.reloadColors();
            }
        };
        this.mLockPatternUtils = lockPatternUtils;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mSecurityModel = keyguardSecurityModel;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mSecurityCallback = securityCallback;
        this.mSecurityViewFlipperController = keyguardSecurityViewFlipperController;
        this.mAdminSecondaryLockScreenController = factory.create(this.mKeyguardSecurityCallback);
        this.mConfigurationController = configurationController;
        this.mLastOrientation = getResources().getConfiguration().orientation;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mSecurityViewFlipperController.init();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        ((KeyguardSecurityContainer) this.mView).setSwipeListener(this.mSwipeListener);
        ((KeyguardSecurityContainer) this.mView).addMotionEventListener(this.mGlobalTouchListener);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        ((KeyguardSecurityContainer) this.mView).removeMotionEventListener(this.mGlobalTouchListener);
    }

    public void onPause() {
        this.mAdminSecondaryLockScreenController.hide();
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onPause();
        }
        ((KeyguardSecurityContainer) this.mView).onPause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ KeyguardSecurityModel.SecurityMode lambda$showPrimarySecurityScreen$0() {
        return this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
    }

    public void showPrimarySecurityScreen(boolean z) {
        Log.v("KeyguardSecurityView", "showPrimarySecurityScreen(turningOff=" + z + ")");
        showSecurityScreen((KeyguardSecurityModel.SecurityMode) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.keyguard.KeyguardSecurityContainerController$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                KeyguardSecurityModel.SecurityMode lambda$showPrimarySecurityScreen$0;
                lambda$showPrimarySecurityScreen$0 = KeyguardSecurityContainerController.this.lambda$showPrimarySecurityScreen$0();
                return lambda$showPrimarySecurityScreen$0;
            }
        }));
    }

    public void showPromptReason(int i) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            if (i != 0) {
                Log.i("KeyguardSecurityView", "Strong auth required, reason: " + i);
            }
            getCurrentSecurityController().showPromptReason(i);
        }
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().showMessage(charSequence, colorStateList);
        }
    }

    public KeyguardSecurityModel.SecurityMode getCurrentSecurityMode() {
        return this.mCurrentSecurityMode;
    }

    public void reset() {
        ((KeyguardSecurityContainer) this.mView).reset();
        this.mSecurityViewFlipperController.reset();
    }

    public CharSequence getTitle() {
        return ((KeyguardSecurityContainer) this.mView).getTitle();
    }

    public void onResume(int i) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onResume(i);
        }
        ((KeyguardSecurityContainer) this.mView).onResume(this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser()), this.mKeyguardStateController.isFaceAuthEnabled());
    }

    public void startAppearAnimation() {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().startAppearAnimation();
        }
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        ((KeyguardSecurityContainer) this.mView).startDisappearAnimation(getCurrentSecurityMode());
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            return getCurrentSecurityController().startDisappearAnimation(runnable);
        }
        return false;
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public void onStartingToHide() {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onStartingToHide();
        }
    }

    public boolean showNextSecurityScreenOrFinish(boolean z, int i, boolean z2) {
        KeyguardSecurityContainer.BouncerUiEvent bouncerUiEvent;
        boolean z3;
        int i2;
        Intent secondaryLockscreenRequirement;
        Log.d("KeyguardSecurityView", "showNextSecurityScreenOrFinish(" + z + ")");
        KeyguardSecurityContainer.BouncerUiEvent bouncerUiEvent2 = KeyguardSecurityContainer.BouncerUiEvent.UNKNOWN;
        boolean z4 = true;
        boolean z5 = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceRecognitionSucceeded() && (getCurrentSecurityMode() == KeyguardSecurityModel.SecurityMode.Pattern || getCurrentSecurityMode() == KeyguardSecurityModel.SecurityMode.PIN || getCurrentSecurityMode() == KeyguardSecurityModel.SecurityMode.Password);
        Log.d("KeyguardSecurityView", "showNextSecurityScreenOrFinish: shouldFinishBecauseFaceUnlock = " + z5);
        if (this.mUpdateMonitor.getUserHasTrust(i)) {
            bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_EXTENDED_ACCESS;
            z3 = false;
            i2 = 3;
        } else if (this.mUpdateMonitor.getUserUnlockedWithBiometric(i) || z5) {
            bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_BIOMETRIC;
            z3 = false;
            i2 = 2;
        } else {
            KeyguardSecurityModel.SecurityMode securityMode = KeyguardSecurityModel.SecurityMode.None;
            if (securityMode == getCurrentSecurityMode()) {
                KeyguardSecurityModel.SecurityMode securityMode2 = this.mSecurityModel.getSecurityMode(i);
                if (securityMode == securityMode2) {
                    bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_NONE_SECURITY;
                    i2 = 0;
                } else {
                    showSecurityScreen(securityMode2);
                    bouncerUiEvent = bouncerUiEvent2;
                    z4 = false;
                    i2 = -1;
                }
                z3 = false;
            } else {
                if (z) {
                    int i3 = AnonymousClass5.$SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[getCurrentSecurityMode().ordinal()];
                    if (i3 == 1 || i3 == 2 || i3 == 3) {
                        bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_PASSWORD;
                        i2 = 1;
                        z3 = true;
                    } else if (i3 == 4 || i3 == 5) {
                        KeyguardSecurityModel.SecurityMode securityMode3 = this.mSecurityModel.getSecurityMode(i);
                        if (securityMode3 == securityMode && this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
                            bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_SIM;
                            z3 = false;
                            i2 = 4;
                        } else {
                            showSecurityScreen(securityMode3);
                        }
                    } else {
                        Log.v("KeyguardSecurityView", "Bad security screen " + getCurrentSecurityMode() + ", fail safe");
                        showPrimarySecurityScreen(false);
                    }
                }
                bouncerUiEvent = bouncerUiEvent2;
                z3 = false;
                z4 = false;
                i2 = -1;
            }
        }
        if (z4 && !z2 && (secondaryLockscreenRequirement = this.mUpdateMonitor.getSecondaryLockscreenRequirement(i)) != null) {
            this.mAdminSecondaryLockScreenController.show(secondaryLockscreenRequirement);
            return false;
        }
        if (i2 != -1) {
            this.mMetricsLogger.write(new LogMaker(197).setType(5).setSubtype(i2));
        }
        if (bouncerUiEvent != bouncerUiEvent2) {
            this.mUiEventLogger.log(bouncerUiEvent);
        }
        if (z4) {
            this.mSecurityCallback.finish(z3, i);
        }
        return z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.keyguard.KeyguardSecurityContainerController$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode;

        static {
            int[] iArr = new int[KeyguardSecurityModel.SecurityMode.values().length];
            $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode = iArr;
            try {
                iArr[KeyguardSecurityModel.SecurityMode.Pattern.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.Password.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.PIN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.SimPin.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.SimPuk.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public boolean needsInput() {
        return getCurrentSecurityController().needsInput();
    }

    @VisibleForTesting
    void showSecurityScreen(KeyguardSecurityModel.SecurityMode securityMode) {
        Log.d("KeyguardSecurityView", "showSecurityScreen(" + securityMode + ")");
        if (securityMode == KeyguardSecurityModel.SecurityMode.Invalid || securityMode == this.mCurrentSecurityMode) {
            return;
        }
        KeyguardInputViewController<KeyguardInputView> currentSecurityController = getCurrentSecurityController();
        if (currentSecurityController != null) {
            currentSecurityController.onPause();
        }
        KeyguardInputViewController<KeyguardInputView> changeSecurityMode = changeSecurityMode(securityMode);
        if (changeSecurityMode != null) {
            changeSecurityMode.onResume(2);
            this.mSecurityViewFlipperController.show(changeSecurityMode);
            ((KeyguardSecurityContainer) this.mView).updateLayoutForSecurityMode(securityMode);
        }
        this.mSecurityCallback.onSecurityModeChanged(securityMode, changeSecurityMode != null && changeSecurityMode.needsInput());
    }

    public void reportFailedUnlockAttempt(int i, int i2) {
        int i3 = 1;
        int currentFailedPasswordAttempts = this.mLockPatternUtils.getCurrentFailedPasswordAttempts(i) + 1;
        Log.d("KeyguardSecurityView", "reportFailedPatternAttempt: #" + currentFailedPasswordAttempts);
        DevicePolicyManager devicePolicyManager = this.mLockPatternUtils.getDevicePolicyManager();
        int maximumFailedPasswordsForWipe = devicePolicyManager.getMaximumFailedPasswordsForWipe(null, i);
        int i4 = maximumFailedPasswordsForWipe > 0 ? maximumFailedPasswordsForWipe - currentFailedPasswordAttempts : Integer.MAX_VALUE;
        if (i4 < 5) {
            int profileWithMinimumFailedPasswordsForWipe = devicePolicyManager.getProfileWithMinimumFailedPasswordsForWipe(i);
            if (profileWithMinimumFailedPasswordsForWipe == i) {
                if (profileWithMinimumFailedPasswordsForWipe != 0) {
                    i3 = 3;
                }
            } else if (profileWithMinimumFailedPasswordsForWipe != -10000) {
                i3 = 2;
            }
            if (i4 > 0) {
                ((KeyguardSecurityContainer) this.mView).showAlmostAtWipeDialog(currentFailedPasswordAttempts, i4, i3);
            } else {
                Slog.i("KeyguardSecurityView", "Too many unlock attempts; user " + profileWithMinimumFailedPasswordsForWipe + " will be wiped!");
                ((KeyguardSecurityContainer) this.mView).showWipeDialog(currentFailedPasswordAttempts, i3);
            }
        }
        this.mLockPatternUtils.reportFailedPasswordAttempt(i);
        if (i2 > 0) {
            this.mLockPatternUtils.reportPasswordLockout(i2, i);
            ((KeyguardSecurityContainer) this.mView).showTimeoutDialog(i, i2, this.mLockPatternUtils, this.mSecurityModel.getSecurityMode(i));
        }
    }

    private KeyguardInputViewController<KeyguardInputView> getCurrentSecurityController() {
        return this.mSecurityViewFlipperController.getSecurityView(this.mCurrentSecurityMode, this.mKeyguardSecurityCallback);
    }

    private KeyguardInputViewController<KeyguardInputView> changeSecurityMode(KeyguardSecurityModel.SecurityMode securityMode) {
        this.mCurrentSecurityMode = securityMode;
        return getCurrentSecurityController();
    }

    public void updateResources() {
        int i = getResources().getConfiguration().orientation;
        if (i != this.mLastOrientation) {
            this.mLastOrientation = i;
            ((KeyguardSecurityContainer) this.mView).updateLayoutForSecurityMode(this.mCurrentSecurityMode);
        }
    }

    public void updateKeyguardPosition(float f) {
        ((KeyguardSecurityContainer) this.mView).updateKeyguardPosition(f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Factory {
        private final AdminSecondaryLockScreenController.Factory mAdminSecondaryLockScreenControllerFactory;
        private final ConfigurationController mConfigurationController;
        private final KeyguardSecurityModel mKeyguardSecurityModel;
        private final KeyguardStateController mKeyguardStateController;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final LockPatternUtils mLockPatternUtils;
        private final MetricsLogger mMetricsLogger;
        private final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
        private final UiEventLogger mUiEventLogger;
        private final KeyguardSecurityContainer mView;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Factory(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController) {
            this.mView = keyguardSecurityContainer;
            this.mAdminSecondaryLockScreenControllerFactory = factory;
            this.mLockPatternUtils = lockPatternUtils;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mKeyguardSecurityModel = keyguardSecurityModel;
            this.mMetricsLogger = metricsLogger;
            this.mUiEventLogger = uiEventLogger;
            this.mKeyguardStateController = keyguardStateController;
            this.mSecurityViewFlipperController = keyguardSecurityViewFlipperController;
            this.mConfigurationController = configurationController;
        }

        public KeyguardSecurityContainerController create(KeyguardSecurityContainer.SecurityCallback securityCallback) {
            return new KeyguardSecurityContainerController(this.mView, this.mAdminSecondaryLockScreenControllerFactory, this.mLockPatternUtils, this.mKeyguardUpdateMonitor, this.mKeyguardSecurityModel, this.mMetricsLogger, this.mUiEventLogger, this.mKeyguardStateController, securityCallback, this.mSecurityViewFlipperController, this.mConfigurationController);
        }
    }
}
