package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.util.MathUtils;
import android.view.MotionEvent;
import com.android.keyguard.BouncerPanelExpansionCalculator;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.C1893R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.time.SystemClock;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;

public class UdfpsKeyguardViewController extends UdfpsAnimationViewController<UdfpsKeyguardView> {
    public static final String TAG = "UdfpsKeyguardViewCtrl";
    private final ActivityLaunchAnimator mActivityLaunchAnimator;
    private final ActivityLaunchAnimator.Listener mActivityLaunchAnimatorListener = new ActivityLaunchAnimator.Listener() {
        public void onLaunchAnimationStart() {
            boolean unused = UdfpsKeyguardViewController.this.mIsLaunchingActivity = true;
            float unused2 = UdfpsKeyguardViewController.this.mActivityLaunchProgress = 0.0f;
            UdfpsKeyguardViewController.this.updateAlpha();
        }

        public void onLaunchAnimationEnd() {
            boolean unused = UdfpsKeyguardViewController.this.mIsLaunchingActivity = false;
            UdfpsKeyguardViewController.this.updateAlpha();
        }

        public void onLaunchAnimationProgress(float f) {
            float unused = UdfpsKeyguardViewController.this.mActivityLaunchProgress = f;
            UdfpsKeyguardViewController.this.updateAlpha();
        }
    };
    /* access modifiers changed from: private */
    public float mActivityLaunchProgress;
    private final StatusBarKeyguardViewManager.AlternateAuthInterceptor mAlternateAuthInterceptor = new StatusBarKeyguardViewManager.AlternateAuthInterceptor() {
        public boolean isAnimating() {
            return false;
        }

        public boolean showAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.showUdfpsBouncer(true);
        }

        public boolean hideAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.showUdfpsBouncer(false);
        }

        public boolean isShowingAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.mShowingUdfpsBouncer;
        }

        public void requestUdfps(boolean z, int i) {
            boolean unused = UdfpsKeyguardViewController.this.mUdfpsRequested = z;
            ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).requestUdfps(z, i);
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        public void setQsExpansion(float f) {
            float unused = UdfpsKeyguardViewController.this.mQsExpansion = f;
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        public boolean onTouch(MotionEvent motionEvent) {
            if (UdfpsKeyguardViewController.this.mTransitionToFullShadeProgress != 0.0f) {
                return false;
            }
            return UdfpsKeyguardViewController.this.mUdfpsController.onTouch(motionEvent);
        }

        public void setBouncerExpansionChanged(float f) {
            float unused = UdfpsKeyguardViewController.this.mInputBouncerHiddenAmount = f;
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        public void onBouncerVisibilityChanged() {
            UdfpsKeyguardViewController.this.updateGenericBouncerVisibility();
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        public void dump(PrintWriter printWriter) {
            printWriter.println(UdfpsKeyguardViewController.this.getTag());
        }
    };
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onUiModeChanged() {
            ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).updateColor();
        }

        public void onThemeChanged() {
            ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).updateColor();
        }

        public void onConfigChanged(Configuration configuration) {
            UdfpsKeyguardViewController.this.updateScaleFactor();
            ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).updatePadding();
            ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).updateColor();
        }
    };
    private boolean mFaceDetectRunning;
    /* access modifiers changed from: private */
    public float mInputBouncerHiddenAmount;
    private boolean mIsGenericBouncerShowing;
    /* access modifiers changed from: private */
    public boolean mIsLaunchingActivity;
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    private final KeyguardStateController.Callback mKeyguardStateControllerCallback = new KeyguardStateController.Callback() {
        public void onLaunchTransitionFadingAwayChanged() {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            boolean unused = udfpsKeyguardViewController.mLaunchTransitionFadingAway = udfpsKeyguardViewController.mKeyguardStateController.isLaunchTransitionFadingAway();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }
    };
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public final StatusBarKeyguardViewManager mKeyguardViewManager;
    /* access modifiers changed from: private */
    public float mLastDozeAmount;
    private long mLastUdfpsBouncerShowTime = -1;
    /* access modifiers changed from: private */
    public boolean mLaunchTransitionFadingAway;
    private final LockscreenShadeTransitionController mLockScreenShadeTransitionController;
    /* access modifiers changed from: private */
    public float mPanelExpansionFraction;
    private final PanelExpansionListener mPanelExpansionListener = new PanelExpansionListener() {
        public void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
            float fraction = panelExpansionChangeEvent.getFraction();
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            if (udfpsKeyguardViewController.mKeyguardViewManager.isBouncerInTransit()) {
                fraction = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(fraction);
            }
            float unused = udfpsKeyguardViewController.mPanelExpansionFraction = fraction;
            UdfpsKeyguardViewController.this.updateAlpha();
        }
    };
    /* access modifiers changed from: private */
    public float mQsExpansion;
    /* access modifiers changed from: private */
    public boolean mShowingUdfpsBouncer;
    private final StatusBarStateController.StateListener mStateListener = new StatusBarStateController.StateListener() {
        public void onDozeAmountChanged(float f, float f2) {
            if (UdfpsKeyguardViewController.this.mLastDozeAmount < f) {
                boolean unused = UdfpsKeyguardViewController.this.showUdfpsBouncer(false);
            }
            UdfpsKeyguardViewController.this.mUnlockedScreenOffDozeAnimator.cancel();
            if (!UdfpsKeyguardViewController.this.mUnlockedScreenOffAnimationController.isAnimationPlaying() || f == 0.0f) {
                ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).onDozeAmountChanged(f, f2, 1);
            } else {
                UdfpsKeyguardViewController.this.mUnlockedScreenOffDozeAnimator.start();
            }
            float unused2 = UdfpsKeyguardViewController.this.mLastDozeAmount = f;
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        public void onStateChanged(int i) {
            int unused = UdfpsKeyguardViewController.this.mStatusBarState = i;
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }
    };
    /* access modifiers changed from: private */
    public int mStatusBarState;
    private final SystemClock mSystemClock;
    /* access modifiers changed from: private */
    public float mTransitionToFullShadeProgress;
    /* access modifiers changed from: private */
    public final UdfpsController mUdfpsController;
    /* access modifiers changed from: private */
    public boolean mUdfpsRequested;
    /* access modifiers changed from: private */
    public final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    /* access modifiers changed from: private */
    public final ValueAnimator mUnlockedScreenOffDozeAnimator;

    /* access modifiers changed from: protected */
    public String getTag() {
        return "UdfpsKeyguardViewController";
    }

    public boolean listenForTouchesOutsideView() {
        return true;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected UdfpsKeyguardViewController(UdfpsKeyguardView udfpsKeyguardView, StatusBarStateController statusBarStateController, PanelExpansionStateManager panelExpansionStateManager, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ConfigurationController configurationController, SystemClock systemClock, KeyguardStateController keyguardStateController, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, SystemUIDialogManager systemUIDialogManager, UdfpsController udfpsController, ActivityLaunchAnimator activityLaunchAnimator) {
        super(udfpsKeyguardView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mUnlockedScreenOffDozeAnimator = ofFloat;
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockScreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mConfigurationController = configurationController;
        this.mSystemClock = systemClock;
        this.mKeyguardStateController = keyguardStateController;
        this.mUdfpsController = udfpsController;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        ofFloat.setDuration(360);
        ofFloat.setInterpolator(Interpolators.ALPHA_IN);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ((UdfpsKeyguardView) UdfpsKeyguardViewController.this.mView).onDozeAmountChanged(valueAnimator.getAnimatedFraction(), ((Float) valueAnimator.getAnimatedValue()).floatValue(), 2);
            }
        });
    }

    public void onInit() {
        super.onInit();
        this.mKeyguardViewManager.setAlternateAuthInterceptor(this.mAlternateAuthInterceptor);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        float dozeAmount = getStatusBarStateController().getDozeAmount();
        this.mLastDozeAmount = dozeAmount;
        this.mStateListener.onDozeAmountChanged(dozeAmount, dozeAmount);
        getStatusBarStateController().addCallback(this.mStateListener);
        this.mUdfpsRequested = false;
        this.mLaunchTransitionFadingAway = this.mKeyguardStateController.isLaunchTransitionFadingAway();
        this.mKeyguardStateController.addCallback(this.mKeyguardStateControllerCallback);
        this.mStatusBarState = getStatusBarStateController().getState();
        this.mQsExpansion = this.mKeyguardViewManager.getQsExpansion();
        updateGenericBouncerVisibility();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        getPanelExpansionStateManager().addExpansionListener(this.mPanelExpansionListener);
        updateScaleFactor();
        ((UdfpsKeyguardView) this.mView).updatePadding();
        updateAlpha();
        updatePauseAuth();
        this.mKeyguardViewManager.setAlternateAuthInterceptor(this.mAlternateAuthInterceptor);
        this.mLockScreenShadeTransitionController.setUdfpsKeyguardViewController(this);
        this.mActivityLaunchAnimator.addListener(this.mActivityLaunchAnimatorListener);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        super.onViewDetached();
        this.mFaceDetectRunning = false;
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateControllerCallback);
        getStatusBarStateController().removeCallback(this.mStateListener);
        this.mKeyguardViewManager.removeAlternateAuthInterceptor(this.mAlternateAuthInterceptor);
        this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(false);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        getPanelExpansionStateManager().removeExpansionListener(this.mPanelExpansionListener);
        if (this.mLockScreenShadeTransitionController.getUdfpsKeyguardViewController() == this) {
            this.mLockScreenShadeTransitionController.setUdfpsKeyguardViewController((UdfpsKeyguardViewController) null);
        }
        this.mActivityLaunchAnimator.removeListener(this.mActivityLaunchAnimatorListener);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
        printWriter.println("mShowingUdfpsBouncer=" + this.mShowingUdfpsBouncer);
        printWriter.println("mFaceDetectRunning=" + this.mFaceDetectRunning);
        printWriter.println("mStatusBarState=" + StatusBarState.toString(this.mStatusBarState));
        printWriter.println("mTransitionToFullShadeProgress=" + this.mTransitionToFullShadeProgress);
        printWriter.println("mQsExpansion=" + this.mQsExpansion);
        printWriter.println("mIsGenericBouncerShowing=" + this.mIsGenericBouncerShowing);
        printWriter.println("mInputBouncerHiddenAmount=" + this.mInputBouncerHiddenAmount);
        printWriter.println("mPanelExpansionFraction=" + this.mPanelExpansionFraction);
        printWriter.println("unpausedAlpha=" + ((UdfpsKeyguardView) this.mView).getUnpausedAlpha());
        printWriter.println("mUdfpsRequested=" + this.mUdfpsRequested);
        printWriter.println("mLaunchTransitionFadingAway=" + this.mLaunchTransitionFadingAway);
        printWriter.println("mLastDozeAmount=" + this.mLastDozeAmount);
        ((UdfpsKeyguardView) this.mView).dump(printWriter);
    }

    /* access modifiers changed from: private */
    public boolean showUdfpsBouncer(boolean z) {
        if (this.mShowingUdfpsBouncer == z) {
            return false;
        }
        boolean shouldPauseAuth = shouldPauseAuth();
        this.mShowingUdfpsBouncer = z;
        if (z) {
            this.mLastUdfpsBouncerShowTime = this.mSystemClock.uptimeMillis();
        }
        if (this.mShowingUdfpsBouncer) {
            if (shouldPauseAuth) {
                ((UdfpsKeyguardView) this.mView).animateInUdfpsBouncer((Runnable) null);
            }
            if (this.mKeyguardViewManager.isOccluded()) {
                this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(true);
            }
            ((UdfpsKeyguardView) this.mView).announceForAccessibility(((UdfpsKeyguardView) this.mView).getContext().getString(C1893R.string.accessibility_fingerprint_bouncer));
        } else {
            this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(false);
        }
        updateGenericBouncerVisibility();
        updateAlpha();
        updatePauseAuth();
        return true;
    }

    public boolean shouldPauseAuth() {
        if (this.mShowingUdfpsBouncer) {
            return false;
        }
        if (this.mUdfpsRequested && !getNotificationShadeVisible() && ((!this.mIsGenericBouncerShowing || this.mInputBouncerHiddenAmount != 0.0f) && this.mKeyguardStateController.isShowing())) {
            return false;
        }
        if (this.mLaunchTransitionFadingAway) {
            return true;
        }
        if ((this.mStatusBarState == 1 || this.mLastDozeAmount != 0.0f) && this.mInputBouncerHiddenAmount >= 0.5f && ((double) ((UdfpsKeyguardView) this.mView).getUnpausedAlpha()) >= 25.5d) {
            return false;
        }
        return true;
    }

    public void onTouchOutsideView() {
        maybeShowInputBouncer();
    }

    private void maybeShowInputBouncer() {
        if (this.mShowingUdfpsBouncer && hasUdfpsBouncerShownWithMinTime()) {
            this.mKeyguardViewManager.showBouncer(true);
        }
    }

    private boolean hasUdfpsBouncerShownWithMinTime() {
        return this.mSystemClock.uptimeMillis() - this.mLastUdfpsBouncerShowTime > 200;
    }

    public void setTransitionToFullShadeProgress(float f) {
        this.mTransitionToFullShadeProgress = f;
        updateAlpha();
    }

    public void updateAlpha() {
        int i;
        float f = this.mUdfpsRequested ? this.mInputBouncerHiddenAmount : this.mPanelExpansionFraction;
        if (this.mShowingUdfpsBouncer) {
            i = 255;
        } else {
            i = (int) MathUtils.constrain(MathUtils.map(0.5f, 0.9f, 0.0f, 255.0f, f), 0.0f, 255.0f);
        }
        if (!this.mShowingUdfpsBouncer) {
            int interpolation = (int) (((float) ((int) (((float) i) * (1.0f - Interpolators.EMPHASIZED_DECELERATE.getInterpolation(this.mQsExpansion))))) * (1.0f - this.mTransitionToFullShadeProgress));
            if (this.mIsLaunchingActivity && !this.mUdfpsRequested) {
                interpolation = (int) (((float) interpolation) * (1.0f - this.mActivityLaunchProgress));
            }
            i = (int) (((float) interpolation) * ((UdfpsKeyguardView) this.mView).getDialogSuggestedAlpha());
        }
        int unpausedAlpha = ((UdfpsKeyguardView) this.mView).getUnpausedAlpha();
        ((UdfpsKeyguardView) this.mView).setUnpausedAlpha(i);
        if (i != unpausedAlpha) {
            updatePauseAuth();
        }
    }

    /* access modifiers changed from: private */
    public void updateGenericBouncerVisibility() {
        this.mIsGenericBouncerShowing = this.mKeyguardViewManager.isBouncerShowing();
        if (this.mKeyguardViewManager.isShowingAlternateAuth() || !this.mKeyguardViewManager.bouncerIsOrWillBeShowing()) {
            this.mInputBouncerHiddenAmount = 1.0f;
        } else if (this.mIsGenericBouncerShowing) {
            this.mInputBouncerHiddenAmount = 0.0f;
        }
    }

    /* access modifiers changed from: private */
    public void updateScaleFactor() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null && udfpsController.mOverlayParams != null) {
            ((UdfpsKeyguardView) this.mView).setScaleFactor(this.mUdfpsController.mOverlayParams.getScaleFactor());
        }
    }

    public void startFpsBreatheAnim() {
        if (this.mView != null) {
            ((UdfpsKeyguardView) this.mView).startFpsBreatheAnim();
        }
    }

    public void stopFpsBreatheAnim() {
        if (this.mView != null) {
            ((UdfpsKeyguardView) this.mView).stopFpsBreatheAnim();
        }
    }

    public void dismissFingerprintIcon() {
        if (this.mView != null) {
            ((UdfpsKeyguardView) this.mView).setVisibility(4);
        }
    }

    public void showFingerprintIcon() {
        NTLogUtil.m1680d("ltf", "showFingerprintIcon: mView = " + this.mView);
        if (this.mView != null) {
            ((UdfpsKeyguardView) this.mView).setVisibility(0);
            updateAlpha();
        }
    }
}
