package com.android.keyguard;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.metrics.LogMaker;
import android.os.Debug;
import android.util.Log;
import android.util.Slog;
import android.view.MotionEvent;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.ActiveUnlockConfig;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.dagger.KeyguardBouncerScope;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1893R;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.GlobalSettings;
import javax.inject.Inject;

@KeyguardBouncerScope
public class KeyguardSecurityContainerController extends ViewController<KeyguardSecurityContainer> implements KeyguardSecurityView {
    private static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final String TAG = "KeyguardSecurityView";
    private final AdminSecondaryLockScreenController mAdminSecondaryLockScreenController;
    private final ConfigurationController mConfigurationController;
    private ConfigurationController.ConfigurationListener mConfigurationListener;
    private KeyguardSecurityModel.SecurityMode mCurrentSecurityMode;
    /* access modifiers changed from: private */
    public final FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    private final FeatureFlags mFeatureFlags;
    private final GlobalSettings mGlobalSettings;
    final Gefingerpoken mGlobalTouchListener;
    /* access modifiers changed from: private */
    public KeyguardSecurityCallback mKeyguardSecurityCallback;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private int mLastOrientation;
    /* access modifiers changed from: private */
    public final LockPatternUtils mLockPatternUtils;
    /* access modifiers changed from: private */
    public final MetricsLogger mMetricsLogger;
    /* access modifiers changed from: private */
    public final KeyguardSecurityContainer.SecurityCallback mSecurityCallback;
    private final KeyguardSecurityModel mSecurityModel;
    private final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
    private final SessionTracker mSessionTracker;
    private KeyguardSecurityContainer.SwipeListener mSwipeListener;
    /* access modifiers changed from: private */
    public final UiEventLogger mUiEventLogger;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mUpdateMonitor;
    private UserSwitcherController.UserSwitchCallback mUserSwitchCallback;
    private final UserSwitcherController mUserSwitcherController;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardSecurityContainerController */
    public /* synthetic */ void mo26061xff3ff0af() {
        showPrimarySecurityScreen(false);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    private KeyguardSecurityContainerController(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityContainer.SecurityCallback securityCallback, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, UserSwitcherController userSwitcherController, FeatureFlags featureFlags, GlobalSettings globalSettings, SessionTracker sessionTracker) {
        super(keyguardSecurityContainer);
        this.mLastOrientation = 0;
        this.mCurrentSecurityMode = KeyguardSecurityModel.SecurityMode.Invalid;
        this.mUserSwitchCallback = new KeyguardSecurityContainerController$$ExternalSyntheticLambda1(this);
        this.mGlobalTouchListener = new Gefingerpoken() {
            private MotionEvent mTouchDown;

            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                return false;
            }

            public boolean onTouchEvent(MotionEvent motionEvent) {
                boolean isOneHandedModeLeftAligned;
                if (motionEvent.getActionMasked() == 0) {
                    if (((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getMode() == 1 && ((isOneHandedModeLeftAligned && motionEvent.getX() > ((float) ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getWidth()) / 2.0f) || (!(isOneHandedModeLeftAligned = ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).isOneHandedModeLeftAligned()) && motionEvent.getX() <= ((float) ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getWidth()) / 2.0f))) {
                        KeyguardSecurityContainerController.this.mFalsingCollector.avoidGesture();
                    }
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
        this.mKeyguardSecurityCallback = new KeyguardSecurityCallback() {
            public boolean isVerifyUnlockOnly() {
                return false;
            }

            public void userActivity() {
                if (KeyguardSecurityContainerController.this.mSecurityCallback != null) {
                    KeyguardSecurityContainerController.this.mSecurityCallback.userActivity();
                }
            }

            public void onUserInput() {
                KeyguardSecurityContainerController.this.mUpdateMonitor.cancelFaceAuth();
            }

            public void dismiss(boolean z, int i, KeyguardSecurityModel.SecurityMode securityMode) {
                dismiss(z, i, false, securityMode);
            }

            public void dismiss(boolean z, int i, boolean z2, KeyguardSecurityModel.SecurityMode securityMode) {
                KeyguardSecurityContainerController.this.mSecurityCallback.dismiss(z, i, z2, securityMode);
            }

            public void reportUnlockAttempt(int i, boolean z, int i2) {
                KeyguardSecurityContainer.BouncerUiEvent bouncerUiEvent;
                int i3 = ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getMode() == 1 ? ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).isOneHandedModeLeftAligned() ? 1 : 2 : 0;
                if (z) {
                    SysUiStatsLog.write(64, 2, i3);
                    KeyguardSecurityContainerController.this.mLockPatternUtils.reportSuccessfulPasswordAttempt(i);
                    ThreadUtils.postOnBackgroundThread((Runnable) new KeyguardSecurityContainerController$2$$ExternalSyntheticLambda0());
                } else {
                    SysUiStatsLog.write(64, 1, i3);
                    KeyguardSecurityContainerController.this.reportFailedUnlockAttempt(i, i2);
                }
                KeyguardSecurityContainerController.this.mMetricsLogger.write(new LogMaker(197).setType(z ? 10 : 11));
                UiEventLogger access$1200 = KeyguardSecurityContainerController.this.mUiEventLogger;
                if (z) {
                    bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_PASSWORD_SUCCESS;
                } else {
                    bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_PASSWORD_FAILURE;
                }
                access$1200.log(bouncerUiEvent, KeyguardSecurityContainerController.this.getSessionId());
            }

            static /* synthetic */ void lambda$reportUnlockAttempt$0() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException unused) {
                }
                System.m1693gc();
                System.runFinalization();
                System.m1693gc();
            }

            public void reset() {
                KeyguardSecurityContainerController.this.mSecurityCallback.reset();
            }

            public void onCancelClicked() {
                KeyguardSecurityContainerController.this.mSecurityCallback.onCancelClicked();
            }
        };
        this.mSwipeListener = new KeyguardSecurityContainer.SwipeListener() {
            public void onSwipeUp() {
                if (!KeyguardSecurityContainerController.this.mUpdateMonitor.isFaceDetectionRunning()) {
                    KeyguardSecurityContainerController.this.mUpdateMonitor.requestFaceAuth(true);
                    KeyguardSecurityContainerController.this.mKeyguardSecurityCallback.userActivity();
                    KeyguardSecurityContainerController.this.showMessage((CharSequence) null, (ColorStateList) null);
                }
                if (KeyguardSecurityContainerController.this.mUpdateMonitor.isFaceEnrolled()) {
                    KeyguardSecurityContainerController.this.mUpdateMonitor.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.UNLOCK_INTENT, "swipeUpOnBouncer");
                }
            }
        };
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() {
            public void onThemeChanged() {
                KeyguardSecurityContainerController.this.reloadColors();
            }

            public void onUiModeChanged() {
                KeyguardSecurityContainerController.this.reloadColors();
            }
        };
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onDevicePolicyManagerStateChanged() {
                KeyguardSecurityContainerController.this.showPrimarySecurityScreen(false);
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
        AdminSecondaryLockScreenController.Factory factory2 = factory;
        this.mAdminSecondaryLockScreenController = factory.create(this.mKeyguardSecurityCallback);
        this.mConfigurationController = configurationController;
        this.mLastOrientation = getResources().getConfiguration().orientation;
        this.mFalsingCollector = falsingCollector;
        this.mFalsingManager = falsingManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mFeatureFlags = featureFlags;
        this.mGlobalSettings = globalSettings;
        this.mSessionTracker = sessionTracker;
    }

    public void onInit() {
        this.mSecurityViewFlipperController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        ((KeyguardSecurityContainer) this.mView).setSwipeListener(this.mSwipeListener);
        ((KeyguardSecurityContainer) this.mView).addMotionEventListener(this.mGlobalTouchListener);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mUserSwitcherController.addUserSwitchCallback(this.mUserSwitchCallback);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        ((KeyguardSecurityContainer) this.mView).removeMotionEventListener(this.mGlobalTouchListener);
        this.mUserSwitcherController.removeUserSwitchCallback(this.mUserSwitchCallback);
    }

    public void onPause() {
        this.mAdminSecondaryLockScreenController.hide();
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onPause();
        }
        ((KeyguardSecurityContainer) this.mView).onPause();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showPrimarySecurityScreen$1$com-android-keyguard-KeyguardSecurityContainerController */
    public /* synthetic */ KeyguardSecurityModel.SecurityMode mo26062xcda1679d() {
        return this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
    }

    public void showPrimarySecurityScreen(boolean z) {
        KeyguardSecurityModel.SecurityMode securityMode = (KeyguardSecurityModel.SecurityMode) DejankUtils.whitelistIpcs(new KeyguardSecurityContainerController$$ExternalSyntheticLambda0(this));
        if (DEBUG) {
            Log.v(TAG, "showPrimarySecurityScreen(turningOff=" + z + NavigationBarInflaterView.KEY_CODE_END);
        }
        showSecurityScreen(securityMode);
    }

    public void showPromptReason(int i) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            if (i != 0) {
                Log.i(TAG, "Strong auth required, reason: " + i);
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

    public void dismiss(boolean z, int i, KeyguardSecurityModel.SecurityMode securityMode) {
        this.mKeyguardSecurityCallback.dismiss(z, i, securityMode);
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
            SysUiStatsLog.write(63, ((KeyguardSecurityContainer) this.mView).getMode() == 1 ? ((KeyguardSecurityContainer) this.mView).isOneHandedModeLeftAligned() ? 3 : 4 : 2);
            getCurrentSecurityController().onResume(i);
        }
        ((KeyguardSecurityContainer) this.mView).onResume(this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser()), this.mKeyguardStateController.isFaceAuthEnabled());
    }

    public void startAppearAnimation() {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            ((KeyguardSecurityContainer) this.mView).startAppearAnimation(this.mCurrentSecurityMode);
            getCurrentSecurityController().startAppearAnimation();
        }
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        if (this.mCurrentSecurityMode == KeyguardSecurityModel.SecurityMode.None) {
            return false;
        }
        ((KeyguardSecurityContainer) this.mView).startDisappearAnimation(this.mCurrentSecurityMode);
        return getCurrentSecurityController().startDisappearAnimation(runnable);
    }

    public void onStartingToHide() {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onStartingToHide();
        }
    }

    public boolean showNextSecurityScreenOrFinish(boolean z, int i, boolean z2, KeyguardSecurityModel.SecurityMode securityMode) {
        int i2;
        boolean z3;
        Intent secondaryLockscreenRequirement;
        if (DEBUG) {
            Log.d(TAG, "showNextSecurityScreenOrFinish(" + z + NavigationBarInflaterView.KEY_CODE_END);
        }
        if (securityMode == KeyguardSecurityModel.SecurityMode.Invalid || securityMode == getCurrentSecurityMode()) {
            KeyguardSecurityContainer.BouncerUiEvent bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.UNKNOWN;
            boolean z4 = true;
            boolean z5 = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceRecognitionSucceeded() && (getCurrentSecurityMode() == KeyguardSecurityModel.SecurityMode.Pattern || getCurrentSecurityMode() == KeyguardSecurityModel.SecurityMode.PIN || getCurrentSecurityMode() == KeyguardSecurityModel.SecurityMode.Password);
            Log.d(TAG, "showNextSecurityScreenOrFinish: shouldFinishBecauseFaceUnlock = " + z5 + Debug.getCallers(6));
            if (this.mUpdateMonitor.getUserHasTrust(i)) {
                bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_EXTENDED_ACCESS;
                z3 = false;
                i2 = 3;
            } else if (this.mUpdateMonitor.getUserUnlockedWithBiometric(i) || z5) {
                bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_BIOMETRIC;
                z3 = false;
                i2 = 2;
            } else {
                if (KeyguardSecurityModel.SecurityMode.None == getCurrentSecurityMode()) {
                    KeyguardSecurityModel.SecurityMode securityMode2 = this.mSecurityModel.getSecurityMode(i);
                    if (KeyguardSecurityModel.SecurityMode.None == securityMode2) {
                        bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_NONE_SECURITY;
                        i2 = 0;
                    } else {
                        showSecurityScreen(securityMode2);
                        z4 = false;
                        i2 = -1;
                    }
                } else {
                    if (z) {
                        int i3 = C16316.f232xdc0e830a[getCurrentSecurityMode().ordinal()];
                        if (i3 == 1 || i3 == 2 || i3 == 3) {
                            bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_PASSWORD;
                            i2 = 1;
                            z3 = true;
                        } else if (i3 == 4 || i3 == 5) {
                            KeyguardSecurityModel.SecurityMode securityMode3 = this.mSecurityModel.getSecurityMode(i);
                            if (securityMode3 != KeyguardSecurityModel.SecurityMode.None || !this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
                                showSecurityScreen(securityMode3);
                            } else {
                                bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_SIM;
                                i2 = 4;
                            }
                        } else {
                            Log.v(TAG, "Bad security screen " + getCurrentSecurityMode() + ", fail safe");
                            showPrimarySecurityScreen(false);
                        }
                    }
                    z3 = false;
                    z4 = false;
                    i2 = -1;
                }
                z3 = false;
            }
            if (!z4 || z2 || (secondaryLockscreenRequirement = this.mUpdateMonitor.getSecondaryLockscreenRequirement(i)) == null) {
                if (i2 != -1) {
                    this.mMetricsLogger.write(new LogMaker(197).setType(5).setSubtype(i2));
                }
                if (bouncerUiEvent != KeyguardSecurityContainer.BouncerUiEvent.UNKNOWN) {
                    this.mUiEventLogger.log(bouncerUiEvent, getSessionId());
                }
                if (z4) {
                    this.mSecurityCallback.finish(z3, i);
                }
                return z4;
            }
            this.mAdminSecondaryLockScreenController.show(secondaryLockscreenRequirement);
            return false;
        }
        Log.w(TAG, "Attempted to invoke showNextSecurityScreenOrFinish with securityMode " + securityMode + ", but current mode is " + getCurrentSecurityMode());
        return false;
    }

    /* renamed from: com.android.keyguard.KeyguardSecurityContainerController$6 */
    static /* synthetic */ class C16316 {

        /* renamed from: $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode */
        static final /* synthetic */ int[] f232xdc0e830a;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.keyguard.KeyguardSecurityModel$SecurityMode[] r0 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f232xdc0e830a = r0
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Pattern     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f232xdc0e830a     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Password     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f232xdc0e830a     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.PIN     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f232xdc0e830a     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPin     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f232xdc0e830a     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPuk     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainerController.C16316.<clinit>():void");
        }
    }

    public boolean needsInput() {
        return getCurrentSecurityController().needsInput();
    }

    /* access modifiers changed from: package-private */
    public void showSecurityScreen(KeyguardSecurityModel.SecurityMode securityMode) {
        if (DEBUG) {
            Log.d(TAG, "showSecurityScreen(" + securityMode + NavigationBarInflaterView.KEY_CODE_END);
        }
        if (securityMode != KeyguardSecurityModel.SecurityMode.Invalid && securityMode != this.mCurrentSecurityMode) {
            KeyguardInputViewController<KeyguardInputView> currentSecurityController = getCurrentSecurityController();
            if (currentSecurityController != null) {
                currentSecurityController.onPause();
            }
            KeyguardInputViewController<KeyguardInputView> changeSecurityMode = changeSecurityMode(securityMode);
            if (changeSecurityMode != null) {
                changeSecurityMode.onResume(2);
                this.mSecurityViewFlipperController.show(changeSecurityMode);
                configureMode();
            }
            this.mSecurityCallback.onSecurityModeChanged(securityMode, changeSecurityMode != null && changeSecurityMode.needsInput());
        }
    }

    private boolean canUseOneHandedBouncer() {
        if (this.mCurrentSecurityMode == KeyguardSecurityModel.SecurityMode.Pattern || this.mCurrentSecurityMode == KeyguardSecurityModel.SecurityMode.PIN) {
            return getResources().getBoolean(C1893R.bool.can_use_one_handed_bouncer);
        }
        return false;
    }

    private boolean canDisplayUserSwitcher() {
        return this.mFeatureFlags.isEnabled(Flags.BOUNCER_USER_SWITCHER);
    }

    private void configureMode() {
        int i = 0;
        boolean z = this.mCurrentSecurityMode == KeyguardSecurityModel.SecurityMode.SimPin || this.mCurrentSecurityMode == KeyguardSecurityModel.SecurityMode.SimPuk;
        if (canDisplayUserSwitcher() && !z) {
            i = 2;
        } else if (canUseOneHandedBouncer()) {
            i = 1;
        }
        ((KeyguardSecurityContainer) this.mView).initMode(i, this.mGlobalSettings, this.mFalsingManager, this.mUserSwitcherController);
    }

    public void reportFailedUnlockAttempt(int i, int i2) {
        int i3 = 1;
        int currentFailedPasswordAttempts = this.mLockPatternUtils.getCurrentFailedPasswordAttempts(i) + 1;
        if (DEBUG) {
            Log.d(TAG, "reportFailedPatternAttempt: #" + currentFailedPasswordAttempts);
        }
        DevicePolicyManager devicePolicyManager = this.mLockPatternUtils.getDevicePolicyManager();
        int maximumFailedPasswordsForWipe = devicePolicyManager.getMaximumFailedPasswordsForWipe((ComponentName) null, i);
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
                Slog.i(TAG, "Too many unlock attempts; user " + profileWithMinimumFailedPasswordsForWipe + " will be wiped!");
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
            configureMode();
        }
    }

    /* access modifiers changed from: private */
    public InstanceId getSessionId() {
        return this.mSessionTracker.getSessionId(1);
    }

    public void updateKeyguardPosition(float f) {
        ((KeyguardSecurityContainer) this.mView).updatePositionByTouchX(f);
    }

    /* access modifiers changed from: private */
    public void reloadColors() {
        this.mSecurityViewFlipperController.reloadColors();
        ((KeyguardSecurityContainer) this.mView).reloadColors();
    }

    static class Factory {
        private final AdminSecondaryLockScreenController.Factory mAdminSecondaryLockScreenControllerFactory;
        private final ConfigurationController mConfigurationController;
        private final FalsingCollector mFalsingCollector;
        private final FalsingManager mFalsingManager;
        private final FeatureFlags mFeatureFlags;
        private final GlobalSettings mGlobalSettings;
        private final KeyguardSecurityModel mKeyguardSecurityModel;
        private final KeyguardStateController mKeyguardStateController;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final LockPatternUtils mLockPatternUtils;
        private final MetricsLogger mMetricsLogger;
        private final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
        private final SessionTracker mSessionTracker;
        private final UiEventLogger mUiEventLogger;
        private final UserSwitcherController mUserSwitcherController;
        private final KeyguardSecurityContainer mView;

        @Inject
        Factory(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, UserSwitcherController userSwitcherController, FeatureFlags featureFlags, GlobalSettings globalSettings, SessionTracker sessionTracker) {
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
            this.mFalsingCollector = falsingCollector;
            this.mFalsingManager = falsingManager;
            this.mFeatureFlags = featureFlags;
            this.mGlobalSettings = globalSettings;
            this.mUserSwitcherController = userSwitcherController;
            this.mSessionTracker = sessionTracker;
        }

        public KeyguardSecurityContainerController create(KeyguardSecurityContainer.SecurityCallback securityCallback) {
            return new KeyguardSecurityContainerController(this.mView, this.mAdminSecondaryLockScreenControllerFactory, this.mLockPatternUtils, this.mKeyguardUpdateMonitor, this.mKeyguardSecurityModel, this.mMetricsLogger, this.mUiEventLogger, this.mKeyguardStateController, securityCallback, this.mSecurityViewFlipperController, this.mConfigurationController, this.mFalsingCollector, this.mFalsingManager, this.mUserSwitcherController, this.mFeatureFlags, this.mGlobalSettings, this.mSessionTracker);
        }
    }
}
