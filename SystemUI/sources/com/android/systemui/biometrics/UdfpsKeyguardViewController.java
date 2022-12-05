package com.android.systemui.biometrics;

import android.content.res.Configuration;
import android.util.MathUtils;
import android.view.MotionEvent;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.R$string;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class UdfpsKeyguardViewController extends UdfpsAnimationViewController<UdfpsKeyguardView> {
    private final ConfigurationController mConfigurationController;
    private boolean mFaceDetectRunning;
    private float mInputBouncerHiddenAmount;
    private boolean mIsBouncerVisible;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final StatusBarKeyguardViewManager mKeyguardViewManager;
    private float mLastDozeAmount;
    private boolean mLaunchTransitionFadingAway;
    private final LockscreenShadeTransitionController mLockScreenShadeTransitionController;
    private boolean mQsExpanded;
    private boolean mShowingUdfpsBouncer;
    private float mStatusBarExpansion;
    private int mStatusBarState;
    private final SystemClock mSystemClock;
    private float mTransitionToFullShadeProgress;
    private final UdfpsController mUdfpsController;
    private boolean mUdfpsRequested;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private long mLastUdfpsBouncerShowTime = -1;
    private final StatusBarStateController.StateListener mStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.1
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozeAmountChanged(float f, float f2) {
            if (UdfpsKeyguardViewController.this.mLastDozeAmount < f) {
                UdfpsKeyguardViewController.this.showUdfpsBouncer(false);
            }
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).onDozeAmountChanged(f, f2);
            UdfpsKeyguardViewController.this.mLastDozeAmount = f;
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onStateChanged(int i) {
            UdfpsKeyguardViewController.this.mStatusBarState = i;
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).setStatusBarState(i);
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }
    };
    private final StatusBarKeyguardViewManager.AlternateAuthInterceptor mAlternateAuthInterceptor = new StatusBarKeyguardViewManager.AlternateAuthInterceptor() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.2
        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean isAnimating() {
            return false;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean showAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.showUdfpsBouncer(true);
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean hideAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.showUdfpsBouncer(false);
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean isShowingAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.mShowingUdfpsBouncer;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void requestUdfps(boolean z, int i) {
            UdfpsKeyguardViewController.this.mUdfpsRequested = z;
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).requestUdfps(z, i);
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void setQsExpanded(boolean z) {
            UdfpsKeyguardViewController.this.mQsExpanded = z;
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean onTouch(MotionEvent motionEvent) {
            if (UdfpsKeyguardViewController.this.mTransitionToFullShadeProgress != 0.0f) {
                return false;
            }
            return UdfpsKeyguardViewController.this.mUdfpsController.onTouch(motionEvent);
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void setBouncerExpansionChanged(float f) {
            UdfpsKeyguardViewController.this.mInputBouncerHiddenAmount = f;
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void onBouncerVisibilityChanged() {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mIsBouncerVisible = udfpsKeyguardViewController.mKeyguardViewManager.isBouncerShowing();
            if (!UdfpsKeyguardViewController.this.mIsBouncerVisible) {
                UdfpsKeyguardViewController.this.mInputBouncerHiddenAmount = 1.0f;
            } else if (UdfpsKeyguardViewController.this.mKeyguardViewManager.isBouncerShowing()) {
                UdfpsKeyguardViewController.this.mInputBouncerHiddenAmount = 0.0f;
            }
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void dump(PrintWriter printWriter) {
            printWriter.println(UdfpsKeyguardViewController.this.getTag());
        }
    };
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.3
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onUiModeChanged() {
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onThemeChanged() {
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onOverlayChanged() {
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onConfigChanged(Configuration configuration) {
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).updateColor();
        }
    };
    private final StatusBar.ExpansionChangedListener mStatusBarExpansionChangedListener = new StatusBar.ExpansionChangedListener() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.4
        @Override // com.android.systemui.statusbar.phone.StatusBar.ExpansionChangedListener
        public void onExpansionChanged(float f, boolean z) {
            UdfpsKeyguardViewController.this.mStatusBarExpansion = f;
            UdfpsKeyguardViewController.this.updateAlpha();
        }
    };
    private final KeyguardStateController.Callback mKeyguardStateControllerCallback = new KeyguardStateController.Callback() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.5
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onLaunchTransitionFadingAwayChanged() {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mLaunchTransitionFadingAway = udfpsKeyguardViewController.mKeyguardStateController.isLaunchTransitionFadingAway();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }
    };
    private final UnlockedScreenOffAnimationController.Callback mUnlockedScreenOffCallback = new UnlockedScreenOffAnimationController.Callback() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController$$ExternalSyntheticLambda0
        @Override // com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController.Callback
        public final void onUnlockedScreenOffProgressUpdate(float f, float f2) {
            UdfpsKeyguardViewController.this.lambda$new$0(f, f2);
        }
    };

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    String getTag() {
        return "UdfpsKeyguardViewController";
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public boolean listenForTouchesOutsideView() {
        return true;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public /* bridge */ /* synthetic */ void updateAlpha(float f) {
        super.updateAlpha(f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UdfpsKeyguardViewController(UdfpsKeyguardView udfpsKeyguardView, StatusBarStateController statusBarStateController, StatusBar statusBar, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ConfigurationController configurationController, SystemClock systemClock, KeyguardStateController keyguardStateController, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, UdfpsController udfpsController) {
        super(udfpsKeyguardView, statusBarStateController, statusBar, dumpManager);
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockScreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mConfigurationController = configurationController;
        this.mSystemClock = systemClock;
        this.mKeyguardStateController = keyguardStateController;
        this.mUdfpsController = udfpsController;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        super.onInit();
        this.mKeyguardViewManager.setAlternateAuthInterceptor(this.mAlternateAuthInterceptor);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
        float dozeAmount = this.mStatusBarStateController.getDozeAmount();
        this.mLastDozeAmount = dozeAmount;
        this.mStateListener.onDozeAmountChanged(dozeAmount, dozeAmount);
        this.mStatusBarStateController.addCallback(this.mStateListener);
        this.mUdfpsRequested = false;
        this.mLaunchTransitionFadingAway = this.mKeyguardStateController.isLaunchTransitionFadingAway();
        this.mKeyguardStateController.addCallback(this.mKeyguardStateControllerCallback);
        this.mStatusBarState = this.mStatusBarStateController.getState();
        this.mQsExpanded = this.mKeyguardViewManager.isQsExpanded();
        this.mInputBouncerHiddenAmount = 1.0f;
        this.mIsBouncerVisible = this.mKeyguardViewManager.bouncerIsOrWillBeShowing();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mStatusBar.addExpansionChangedListener(this.mStatusBarExpansionChangedListener);
        updateAlpha();
        updatePauseAuth();
        this.mKeyguardViewManager.setAlternateAuthInterceptor(this.mAlternateAuthInterceptor);
        this.mLockScreenShadeTransitionController.setUdfpsKeyguardViewController(this);
        this.mUnlockedScreenOffAnimationController.addCallback(this.mUnlockedScreenOffCallback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.util.ViewController
    public void onViewDetached() {
        super.onViewDetached();
        this.mFaceDetectRunning = false;
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateControllerCallback);
        this.mStatusBarStateController.removeCallback(this.mStateListener);
        this.mKeyguardViewManager.removeAlternateAuthInterceptor(this.mAlternateAuthInterceptor);
        this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(false);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mStatusBar.removeExpansionChangedListener(this.mStatusBarExpansionChangedListener);
        if (this.mLockScreenShadeTransitionController.getUdfpsKeyguardViewController() == this) {
            this.mLockScreenShadeTransitionController.setUdfpsKeyguardViewController(null);
        }
        this.mUnlockedScreenOffAnimationController.removeCallback(this.mUnlockedScreenOffCallback);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("mShowingUdfpsBouncer=" + this.mShowingUdfpsBouncer);
        printWriter.println("mFaceDetectRunning=" + this.mFaceDetectRunning);
        printWriter.println("mStatusBarState=" + StatusBarState.toShortString(this.mStatusBarState));
        printWriter.println("mQsExpanded=" + this.mQsExpanded);
        printWriter.println("mIsBouncerVisible=" + this.mIsBouncerVisible);
        printWriter.println("mInputBouncerHiddenAmount=" + this.mInputBouncerHiddenAmount);
        printWriter.println("mStatusBarExpansion=" + this.mStatusBarExpansion);
        printWriter.println("unpausedAlpha=" + ((UdfpsKeyguardView) this.mView).getUnpausedAlpha());
        printWriter.println("mUdfpsRequested=" + this.mUdfpsRequested);
        printWriter.println("mView.mUdfpsRequested=" + ((UdfpsKeyguardView) this.mView).mUdfpsRequested);
        printWriter.println("mLaunchTransitionFadingAway=" + this.mLaunchTransitionFadingAway);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
                ((UdfpsKeyguardView) this.mView).animateInUdfpsBouncer(null);
            }
            if (this.mKeyguardViewManager.isOccluded()) {
                this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(true);
            }
            T t = this.mView;
            ((UdfpsKeyguardView) t).announceForAccessibility(((UdfpsKeyguardView) t).getContext().getString(R$string.accessibility_fingerprint_bouncer));
        } else {
            this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(false);
        }
        updateAlpha();
        updatePauseAuth();
        return true;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public boolean shouldPauseAuth() {
        if (this.mShowingUdfpsBouncer) {
            return false;
        }
        if (this.mUdfpsRequested && !this.mNotificationShadeExpanded && (!this.mIsBouncerVisible || this.mInputBouncerHiddenAmount != 0.0f)) {
            return false;
        }
        return this.mLaunchTransitionFadingAway || this.mStatusBarState != 1 || this.mQsExpanded || this.mInputBouncerHiddenAmount < 0.5f || this.mIsBouncerVisible;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public void onTouchOutsideView() {
        maybeShowInputBouncer();
    }

    private void maybeShowInputBouncer() {
        if (!this.mShowingUdfpsBouncer || !hasUdfpsBouncerShownWithMinTime()) {
            return;
        }
        this.mKeyguardViewManager.showBouncer(true);
        this.mKeyguardViewManager.resetAlternateAuth(false);
    }

    private boolean hasUdfpsBouncerShownWithMinTime() {
        return this.mSystemClock.uptimeMillis() - this.mLastUdfpsBouncerShowTime > 200;
    }

    public void setTransitionToFullShadeProgress(float f) {
        this.mTransitionToFullShadeProgress = f;
        updateAlpha();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAlpha() {
        int constrain = this.mShowingUdfpsBouncer ? 255 : (int) MathUtils.constrain(MathUtils.map(0.5f, 0.9f, 0.0f, 255.0f, this.mUdfpsRequested ? this.mInputBouncerHiddenAmount : this.mStatusBarExpansion), 0.0f, 255.0f);
        if (!this.mShowingUdfpsBouncer) {
            constrain = (int) (constrain * (1.0f - this.mTransitionToFullShadeProgress));
        }
        ((UdfpsKeyguardView) this.mView).setUnpausedAlpha(constrain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(float f, float f2) {
        this.mStateListener.onDozeAmountChanged(f, f2);
    }

    public void startFpsBreatheAnim() {
        T t = this.mView;
        if (t != 0) {
            ((UdfpsKeyguardView) t).startFpsBreatheAnim();
        }
    }

    public void stopFpsBreatheAnim() {
        T t = this.mView;
        if (t != 0) {
            ((UdfpsKeyguardView) t).stopFpsBreatheAnim();
        }
    }
}
