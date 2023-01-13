package com.android.keyguard;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.hardware.biometrics.BiometricSourceType;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Process;
import android.os.VibrationAttributes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.nothing.keyguard.LockIconViewControllerEx;
import com.nothing.systemui.NTDependencyEx;
import java.p026io.PrintWriter;
import java.util.Objects;
import javax.inject.Inject;

@CentralSurfacesComponent.CentralSurfacesScope
public class LockIconViewController extends ViewController<LockIconView> implements Dumpable {
    private static final long LONG_PRESS_TIMEOUT = 200;
    private static final String TAG = "LockIconViewController";
    private static final VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(18);
    private static final float sDefaultDensity;
    private static final int sLockIconRadiusPx;
    private final View.OnClickListener mA11yClickListener = new LockIconViewController$$ExternalSyntheticLambda2(this);
    private final View.AccessibilityDelegate mAccessibilityDelegate = new View.AccessibilityDelegate() {
        private final AccessibilityNodeInfo.AccessibilityAction mAccessibilityAuthenticateHint;
        private final AccessibilityNodeInfo.AccessibilityAction mAccessibilityEnterHint;

        {
            this.mAccessibilityAuthenticateHint = new AccessibilityNodeInfo.AccessibilityAction(16, LockIconViewController.this.getResources().getString(C1894R.string.accessibility_authenticate_hint));
            this.mAccessibilityEnterHint = new AccessibilityNodeInfo.AccessibilityAction(16, LockIconViewController.this.getResources().getString(C1894R.string.accessibility_enter_hint));
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            if (!LockIconViewController.this.isActionable()) {
                return;
            }
            if (LockIconViewController.this.mShowLockIcon) {
                accessibilityNodeInfo.addAction(this.mAccessibilityAuthenticateHint);
            } else if (LockIconViewController.this.mShowUnlockIcon) {
                accessibilityNodeInfo.addAction(this.mAccessibilityEnterHint);
            }
        }
    };
    private final AccessibilityManager mAccessibilityManager;
    private final AccessibilityManager.AccessibilityStateChangeListener mAccessibilityStateChangeListener = new LockIconViewController$$ExternalSyntheticLambda3(this);
    private int mActivePointerId = -1;
    private final AuthController mAuthController;
    private final AuthController.Callback mAuthControllerCallback = new AuthController.Callback() {
        public void onAllAuthenticatorsRegistered() {
            LockIconViewController.this.updateUdfpsConfig();
        }

        public void onEnrollmentsChanged() {
            LockIconViewController.this.updateUdfpsConfig();
        }

        public void onUdfpsLocationChanged() {
            LockIconViewController.this.updateUdfpsConfig();
        }
    };
    private final AuthRippleController mAuthRippleController;
    private int mBottomPaddingPx;
    /* access modifiers changed from: private */
    public boolean mCanDismissLockScreen;
    /* access modifiers changed from: private */
    public Runnable mCancelDelayedUpdateVisibilityRunnable;
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onUiModeChanged() {
            LockIconViewController.this.updateColors();
        }

        public void onThemeChanged() {
            LockIconViewController.this.updateColors();
        }

        public void onConfigChanged(Configuration configuration) {
            LockIconViewController.this.updateConfiguration();
            LockIconViewController.this.updateColors();
        }
    };
    private int mDefaultPaddingPx;
    private boolean mDownDetected;
    /* access modifiers changed from: private */
    public final DelayableExecutor mExecutor;
    private final FalsingManager mFalsingManager;
    private float mHeightPixels;
    private final AnimatedStateListDrawable mIcon;
    /* access modifiers changed from: private */
    public float mInterpolatedDarkAmount;
    /* access modifiers changed from: private */
    public boolean mIsBouncerShowing;
    /* access modifiers changed from: private */
    public boolean mIsDozing;
    /* access modifiers changed from: private */
    public boolean mIsKeyguardShowing;
    private final KeyguardStateController.Callback mKeyguardStateCallback = new KeyguardStateController.Callback() {
        public void onUnlockedChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            boolean unused = lockIconViewController.mCanDismissLockScreen = lockIconViewController.mKeyguardStateController.canDismissLockScreen();
            if (LockIconViewController.this.mIsKeyguardShowing) {
                LockIconViewController lockIconViewController2 = LockIconViewController.this;
                boolean unused2 = lockIconViewController2.mUserUnlockedWithBiometric = lockIconViewController2.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            }
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController.this.updateVisibility();
        }

        public void onKeyguardShowingChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            boolean unused = lockIconViewController.mCanDismissLockScreen = lockIconViewController.mKeyguardStateController.canDismissLockScreen();
            LockIconViewController.this.updateKeyguardShowing();
            if (LockIconViewController.this.mIsKeyguardShowing) {
                LockIconViewController lockIconViewController2 = LockIconViewController.this;
                boolean unused2 = lockIconViewController2.mUserUnlockedWithBiometric = lockIconViewController2.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            }
            LockIconViewController.this.updateVisibility();
        }

        public void onKeyguardFadingAwayChanged() {
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController.this.updateVisibility();
        }
    };
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onKeyguardVisibilityChanged(boolean z) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            boolean unused = lockIconViewController.mIsBouncerShowing = lockIconViewController.mKeyguardViewController.isBouncerShowing();
            LockIconViewController.this.updateVisibility();
        }

        public void onKeyguardBouncerStateChanged(boolean z) {
            boolean unused = LockIconViewController.this.mIsBouncerShowing = z;
            LockIconViewController.this.updateVisibility();
        }

        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            boolean access$1200 = LockIconViewController.this.mRunningFPS;
            boolean access$1300 = LockIconViewController.this.mUserUnlockedWithBiometric;
            LockIconViewController lockIconViewController = LockIconViewController.this;
            boolean unused = lockIconViewController.mUserUnlockedWithBiometric = lockIconViewController.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
                boolean unused2 = LockIconViewController.this.mRunningFPS = z;
                if (access$1200 && !LockIconViewController.this.mRunningFPS) {
                    if (LockIconViewController.this.mCancelDelayedUpdateVisibilityRunnable != null) {
                        LockIconViewController.this.mCancelDelayedUpdateVisibilityRunnable.run();
                    }
                    LockIconViewController lockIconViewController2 = LockIconViewController.this;
                    Runnable unused3 = lockIconViewController2.mCancelDelayedUpdateVisibilityRunnable = lockIconViewController2.mExecutor.executeDelayed(new LockIconViewController$3$$ExternalSyntheticLambda0(this), 50);
                    return;
                }
            }
            if (access$1300 != LockIconViewController.this.mUserUnlockedWithBiometric || access$1200 != LockIconViewController.this.mRunningFPS) {
                LockIconViewController.this.updateVisibility();
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBiometricRunningStateChanged$0$com-android-keyguard-LockIconViewController$3 */
        public /* synthetic */ void mo26438x8e76945f() {
            LockIconViewController.this.updateVisibility();
        }
    };
    /* access modifiers changed from: private */
    public final KeyguardViewController mKeyguardViewController;
    private CharSequence mLockedLabel;
    private Runnable mLongPressCancelRunnable;
    private final int mMaxBurnInOffsetX;
    private final int mMaxBurnInOffsetY;
    private Runnable mOnGestureDetectedRunnable;
    /* access modifiers changed from: private */
    public boolean mRunningFPS;
    private final Rect mSensorTouchLocation = new Rect();
    private boolean mShowAodLockIcon;
    private boolean mShowAodUnlockedIcon;
    /* access modifiers changed from: private */
    public boolean mShowLockIcon;
    /* access modifiers changed from: private */
    public boolean mShowUnlockIcon;
    /* access modifiers changed from: private */
    public int mStatusBarState;
    private final StatusBarStateController mStatusBarStateController;
    private StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onDozeAmountChanged(float f, float f2) {
            float unused = LockIconViewController.this.mInterpolatedDarkAmount = f2;
            ((LockIconView) LockIconViewController.this.mView).setDozeAmount(f2);
            LockIconViewController.this.updateBurnInOffsets();
        }

        public void onDozingChanged(boolean z) {
            boolean unused = LockIconViewController.this.mIsDozing = z;
            LockIconViewController.this.updateBurnInOffsets();
            LockIconViewController.this.updateVisibility();
        }

        public void onStateChanged(int i) {
            int unused = LockIconViewController.this.mStatusBarState = i;
            LockIconViewController.this.updateVisibility();
        }
    };
    private boolean mUdfpsEnrolled;
    private boolean mUdfpsSupported;
    private CharSequence mUnlockedLabel;
    /* access modifiers changed from: private */
    public boolean mUserUnlockedWithBiometric;
    private VelocityTracker mVelocityTracker;
    private final VibratorHelper mVibrator;
    private float mWidthPixels;

    static {
        float f = ((float) DisplayMetrics.DENSITY_DEVICE_STABLE) / 160.0f;
        sDefaultDensity = f;
        sLockIconRadiusPx = (int) (f * 36.0f);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public LockIconViewController(LockIconView lockIconView, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewController keyguardViewController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, AuthController authController, DumpManager dumpManager, AccessibilityManager accessibilityManager, ConfigurationController configurationController, @Main DelayableExecutor delayableExecutor, VibratorHelper vibratorHelper, AuthRippleController authRippleController, @Main Resources resources) {
        super(lockIconView);
        Resources resources2 = resources;
        this.mStatusBarStateController = statusBarStateController;
        KeyguardUpdateMonitor keyguardUpdateMonitor2 = keyguardUpdateMonitor;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.mAuthController = authController;
        this.mKeyguardViewController = keyguardViewController;
        this.mKeyguardStateController = keyguardStateController;
        this.mFalsingManager = falsingManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mConfigurationController = configurationController;
        this.mExecutor = delayableExecutor;
        this.mVibrator = vibratorHelper;
        this.mAuthRippleController = authRippleController;
        this.mMaxBurnInOffsetX = resources2.getDimensionPixelSize(C1894R.dimen.udfps_burn_in_offset_x);
        this.mMaxBurnInOffsetY = resources2.getDimensionPixelSize(C1894R.dimen.udfps_burn_in_offset_y);
        AnimatedStateListDrawable animatedStateListDrawable = (AnimatedStateListDrawable) resources2.getDrawable(C1894R.C1896drawable.super_lock_icon, ((LockIconView) this.mView).getContext().getTheme());
        this.mIcon = animatedStateListDrawable;
        ((LockIconView) this.mView).setImageDrawable(animatedStateListDrawable);
        ((LockIconViewControllerEx) NTDependencyEx.get(LockIconViewControllerEx.class)).init(getContext(), (LockIconView) this.mView, resources, keyguardUpdateMonitor2, this);
        this.mUnlockedLabel = resources2.getString(C1894R.string.accessibility_unlock_button);
        this.mLockedLabel = resources2.getString(C1894R.string.accessibility_lock_icon);
        dumpManager.registerDumpable(TAG, this);
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        ((LockIconView) this.mView).setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        updateIsUdfpsEnrolled();
        updateConfiguration();
        updateKeyguardShowing();
        this.mUserUnlockedWithBiometric = false;
        this.mIsBouncerShowing = this.mKeyguardViewController.isBouncerShowing();
        this.mIsDozing = this.mStatusBarStateController.isDozing();
        this.mInterpolatedDarkAmount = this.mStatusBarStateController.getDozeAmount();
        this.mRunningFPS = this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning();
        this.mCanDismissLockScreen = this.mKeyguardStateController.canDismissLockScreen();
        this.mStatusBarState = this.mStatusBarStateController.getState();
        updateColors();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mAuthController.addCallback(this.mAuthControllerCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
        this.mDownDetected = false;
        updateBurnInOffsets();
        updateVisibility();
        this.mAccessibilityManager.addAccessibilityStateChangeListener(this.mAccessibilityStateChangeListener);
        updateAccessibility();
        ((LockIconViewControllerEx) NTDependencyEx.get(LockIconViewControllerEx.class)).onViewAttached();
    }

    private void updateAccessibility() {
        if (this.mAccessibilityManager.isEnabled()) {
            ((LockIconView) this.mView).setOnClickListener(this.mA11yClickListener);
        } else {
            ((LockIconView) this.mView).setOnClickListener((View.OnClickListener) null);
        }
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mAuthController.removeCallback(this.mAuthControllerCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateCallback);
        Runnable runnable = this.mCancelDelayedUpdateVisibilityRunnable;
        if (runnable != null) {
            runnable.run();
            this.mCancelDelayedUpdateVisibilityRunnable = null;
        }
        this.mAccessibilityManager.removeAccessibilityStateChangeListener(this.mAccessibilityStateChangeListener);
        ((LockIconViewControllerEx) NTDependencyEx.get(LockIconViewControllerEx.class)).onViewDetached();
    }

    public float getTop() {
        return ((LockIconView) this.mView).getLocationTop();
    }

    public void updateVisibility() {
        Runnable runnable = this.mCancelDelayedUpdateVisibilityRunnable;
        if (runnable != null) {
            runnable.run();
            this.mCancelDelayedUpdateVisibilityRunnable = null;
        }
        if (this.mIsKeyguardShowing || this.mIsDozing) {
            boolean z = this.mUdfpsEnrolled && !this.mShowUnlockIcon && !this.mShowLockIcon && !this.mShowAodUnlockedIcon && !this.mShowAodLockIcon;
            boolean z2 = this.mShowLockIcon;
            boolean z3 = this.mShowUnlockIcon;
            this.mShowLockIcon = !this.mCanDismissLockScreen && !this.mUserUnlockedWithBiometric && isLockScreen() && !this.mKeyguardUpdateMonitor.isFaceRecognitionSucceeded() && (!this.mUdfpsEnrolled || !this.mRunningFPS || this.mKeyguardUpdateMonitor.isFaceRecognitionEnable());
            this.mShowUnlockIcon = (this.mCanDismissLockScreen || this.mUserUnlockedWithBiometric || this.mKeyguardUpdateMonitor.isFaceRecognitionSucceeded()) && isLockScreen();
            this.mShowAodUnlockedIcon = false;
            this.mShowAodLockIcon = false;
            boolean z4 = this.mIsDozing || this.mKeyguardStateController.isOccluded();
            CharSequence contentDescription = ((LockIconView) this.mView).getContentDescription();
            ((LockIconViewControllerEx) NTDependencyEx.get(LockIconViewControllerEx.class)).updateVisibility(this.mShowLockIcon, this.mShowUnlockIcon, z2, z3, z, z4);
            if (!Objects.equals(contentDescription, ((LockIconView) this.mView).getContentDescription()) && ((LockIconView) this.mView).getContentDescription() != null) {
                ((LockIconView) this.mView).announceForAccessibility(((LockIconView) this.mView).getContentDescription());
                return;
            }
            return;
        }
        ((LockIconView) this.mView).setVisibility(4);
    }

    private boolean isLockScreen() {
        return !this.mIsDozing && !this.mIsBouncerShowing && this.mStatusBarState == 1;
    }

    /* access modifiers changed from: private */
    public void updateKeyguardShowing() {
        this.mIsKeyguardShowing = this.mKeyguardStateController.isShowing() && !this.mKeyguardStateController.isKeyguardGoingAway();
    }

    /* access modifiers changed from: private */
    public void updateColors() {
        ((LockIconView) this.mView).updateColorAndBackgroundVisibility();
    }

    /* access modifiers changed from: private */
    public void updateConfiguration() {
        ((LockIconViewControllerEx) NTDependencyEx.get(LockIconViewControllerEx.class)).updateConfiguration();
        this.mUnlockedLabel = ((LockIconView) this.mView).getContext().getResources().getString(C1894R.string.accessibility_unlock_button);
        this.mLockedLabel = ((LockIconView) this.mView).getContext().getResources().getString(C1894R.string.accessibility_lock_icon);
        updateLockIconLocation();
    }

    private void updateLockIconLocation() {
        ((LockIconViewControllerEx) NTDependencyEx.get(LockIconViewControllerEx.class)).updateLockIconLocation(this.mAuthController, this.mSensorTouchLocation);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("mUdfpsSupported: " + this.mUdfpsSupported);
        printWriter.println("mUdfpsEnrolled: " + this.mUdfpsEnrolled);
        printWriter.println("mIsKeyguardShowing: " + this.mIsKeyguardShowing);
        printWriter.println(" mIcon: ");
        int[] state = this.mIcon.getState();
        int length = state.length;
        for (int i = 0; i < length; i++) {
            printWriter.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + state[i]);
        }
        printWriter.println();
        printWriter.println(" mShowUnlockIcon: " + this.mShowUnlockIcon);
        printWriter.println(" mShowLockIcon: " + this.mShowLockIcon);
        printWriter.println(" mShowAodUnlockedIcon: " + this.mShowAodUnlockedIcon);
        printWriter.println("  mIsDozing: " + this.mIsDozing);
        printWriter.println("  mIsBouncerShowing: " + this.mIsBouncerShowing);
        printWriter.println("  mUserUnlockedWithBiometric: " + this.mUserUnlockedWithBiometric);
        printWriter.println("  mRunningFPS: " + this.mRunningFPS);
        printWriter.println("  mCanDismissLockScreen: " + this.mCanDismissLockScreen);
        printWriter.println("  mStatusBarState: " + StatusBarState.toString(this.mStatusBarState));
        printWriter.println("  mInterpolatedDarkAmount: " + this.mInterpolatedDarkAmount);
        printWriter.println("  mSensorTouchLocation: " + this.mSensorTouchLocation);
        if (this.mView != null) {
            ((LockIconView) this.mView).dump(printWriter, strArr);
        }
    }

    public void dozeTimeTick() {
        updateBurnInOffsets();
    }

    /* access modifiers changed from: private */
    public void updateBurnInOffsets() {
        float lerp = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetX * 2, true) - this.mMaxBurnInOffsetX), this.mInterpolatedDarkAmount);
        float lerp2 = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetY * 2, false) - this.mMaxBurnInOffsetY), this.mInterpolatedDarkAmount);
        MathUtils.lerp(0.0f, BurnInHelperKt.getBurnInProgressOffset(), this.mInterpolatedDarkAmount);
        ((LockIconView) this.mView).setTranslationX(lerp);
        ((LockIconView) this.mView).setTranslationY(lerp2);
    }

    private void updateIsUdfpsEnrolled() {
        boolean z = this.mUdfpsSupported;
        boolean z2 = this.mUdfpsEnrolled;
        this.mUdfpsSupported = this.mKeyguardUpdateMonitor.isUdfpsSupported();
        ((LockIconView) this.mView).setUseBackground(false);
        boolean isUdfpsEnrolled = this.mKeyguardUpdateMonitor.isUdfpsEnrolled();
        this.mUdfpsEnrolled = isUdfpsEnrolled;
        if (z != this.mUdfpsSupported || z2 != isUdfpsEnrolled) {
            updateVisibility();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0027, code lost:
        if (r12 != 10) goto L_0x00cc;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r11, java.lang.Runnable r12) {
        /*
            r10 = this;
            boolean r0 = r10.onInterceptTouchEvent(r11)
            r1 = 0
            if (r0 != 0) goto L_0x000b
            r10.cancelTouches()
            return r1
        L_0x000b:
            r10.mOnGestureDetectedRunnable = r12
            int r12 = r11.getActionMasked()
            r2 = 200(0xc8, double:9.9E-322)
            r0 = 1
            if (r12 == 0) goto L_0x0081
            if (r12 == r0) goto L_0x0072
            r4 = 2
            if (r12 == r4) goto L_0x002b
            r5 = 3
            if (r12 == r5) goto L_0x007d
            r5 = 7
            if (r12 == r5) goto L_0x002b
            r4 = 9
            if (r12 == r4) goto L_0x0081
            r11 = 10
            if (r12 == r11) goto L_0x007d
            goto L_0x00cc
        L_0x002b:
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            r12.addMovement(r11)
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            r1 = 1000(0x3e8, float:1.401E-42)
            r12.computeCurrentVelocity(r1)
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            int r1 = r10.mActivePointerId
            float r12 = com.android.systemui.biometrics.UdfpsController.computePointerSpeed(r12, r1)
            int r11 = r11.getClassification()
            if (r11 == r4) goto L_0x00cc
            boolean r11 = com.android.systemui.biometrics.UdfpsController.exceedsVelocityThreshold(r12)
            if (r11 == 0) goto L_0x00cc
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r1 = "lock icon long-press rescheduled due to high pointer velocity="
            r11.<init>((java.lang.String) r1)
            java.lang.StringBuilder r11 = r11.append((float) r12)
            java.lang.String r11 = r11.toString()
            java.lang.String r12 = "LockIconViewController"
            android.util.Log.v(r12, r11)
            java.lang.Runnable r11 = r10.mLongPressCancelRunnable
            r11.run()
            com.android.systemui.util.concurrency.DelayableExecutor r11 = r10.mExecutor
            com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1 r12 = new com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1
            r12.<init>(r10)
            java.lang.Runnable r11 = r11.executeDelayed(r12, r2)
            r10.mLongPressCancelRunnable = r11
            goto L_0x00cc
        L_0x0072:
            java.lang.Class<com.nothing.keyguard.LockIconViewControllerEx> r11 = com.nothing.keyguard.LockIconViewControllerEx.class
            java.lang.Object r11 = com.nothing.systemui.NTDependencyEx.get(r11)
            com.nothing.keyguard.LockIconViewControllerEx r11 = (com.nothing.keyguard.LockIconViewControllerEx) r11
            r11.onLockIconClick()
        L_0x007d:
            r10.cancelTouches()
            goto L_0x00cc
        L_0x0081:
            boolean r12 = r10.mDownDetected
            if (r12 != 0) goto L_0x00a4
            android.view.accessibility.AccessibilityManager r12 = r10.mAccessibilityManager
            boolean r12 = r12.isTouchExplorationEnabled()
            if (r12 == 0) goto L_0x00a4
            com.android.systemui.statusbar.VibratorHelper r4 = r10.mVibrator
            int r5 = android.os.Process.myUid()
            android.content.Context r12 = r10.getContext()
            java.lang.String r6 = r12.getOpPackageName()
            android.os.VibrationEffect r7 = com.android.systemui.biometrics.UdfpsController.EFFECT_CLICK
            java.lang.String r8 = "lock-icon-down"
            android.os.VibrationAttributes r9 = TOUCH_VIBRATION_ATTRIBUTES
            r4.vibrate(r5, r6, r7, r8, r9)
        L_0x00a4:
            int r12 = r11.getPointerId(r1)
            r10.mActivePointerId = r12
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            if (r12 != 0) goto L_0x00b5
            android.view.VelocityTracker r12 = android.view.VelocityTracker.obtain()
            r10.mVelocityTracker = r12
            goto L_0x00b8
        L_0x00b5:
            r12.clear()
        L_0x00b8:
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            r12.addMovement(r11)
            r10.mDownDetected = r0
            com.android.systemui.util.concurrency.DelayableExecutor r11 = r10.mExecutor
            com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1 r12 = new com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1
            r12.<init>(r10)
            java.lang.Runnable r11 = r11.executeDelayed(r12, r2)
            r10.mLongPressCancelRunnable = r11
        L_0x00cc:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.LockIconViewController.onTouchEvent(android.view.MotionEvent, java.lang.Runnable):boolean");
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!inLockIconArea(motionEvent) || !isActionable()) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            return true;
        }
        return this.mDownDetected;
    }

    /* access modifiers changed from: private */
    public void onLongPress() {
        AuthRippleController authRippleController;
        cancelTouches();
        if (this.mFalsingManager.isFalseTouch(14)) {
            Log.v(TAG, "lock icon long-press rejected by the falsing manager.");
            return;
        }
        this.mIsBouncerShowing = true;
        if (this.mUdfpsSupported && this.mShowUnlockIcon && (authRippleController = this.mAuthRippleController) != null) {
            authRippleController.showUnlockRipple(BiometricSourceType.FINGERPRINT);
        }
        updateVisibility();
        Runnable runnable = this.mOnGestureDetectedRunnable;
        if (runnable != null) {
            runnable.run();
        }
        this.mVibrator.vibrate(Process.myUid(), getContext().getOpPackageName(), UdfpsController.EFFECT_CLICK, "lock-icon-device-entry", TOUCH_VIBRATION_ATTRIBUTES);
        this.mKeyguardViewController.showBouncer(true);
    }

    private void cancelTouches() {
        this.mDownDetected = false;
        Runnable runnable = this.mLongPressCancelRunnable;
        if (runnable != null) {
            runnable.run();
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private boolean inLockIconArea(MotionEvent motionEvent) {
        ((LockIconView) this.mView).getHitRect(this.mSensorTouchLocation);
        return this.mSensorTouchLocation.contains((int) motionEvent.getX(), (int) motionEvent.getY()) && ((LockIconView) this.mView).getVisibility() == 0;
    }

    /* access modifiers changed from: private */
    public boolean isActionable() {
        return this.mUdfpsSupported || this.mShowUnlockIcon;
    }

    public void setAlpha(float f) {
        ((LockIconView) this.mView).setAlpha(f);
    }

    /* access modifiers changed from: private */
    public void updateUdfpsConfig() {
        this.mExecutor.execute(new LockIconViewController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateUdfpsConfig$0$com-android-keyguard-LockIconViewController */
    public /* synthetic */ void mo26431x13946920() {
        updateIsUdfpsEnrolled();
        updateConfiguration();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-keyguard-LockIconViewController  reason: not valid java name */
    public /* synthetic */ void m2308lambda$new$1$comandroidkeyguardLockIconViewController(View view) {
        onLongPress();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-keyguard-LockIconViewController  reason: not valid java name */
    public /* synthetic */ void m2309lambda$new$2$comandroidkeyguardLockIconViewController(boolean z) {
        updateAccessibility();
    }
}
