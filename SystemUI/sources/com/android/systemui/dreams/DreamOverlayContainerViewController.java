package com.android.systemui.dreams;

import android.content.res.Resources;
import android.os.Handler;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.android.keyguard.BouncerPanelExpansionCalculator;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.util.ViewController;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Named;

@DreamOverlayComponent.DreamOverlayScope
public class DreamOverlayContainerViewController extends ViewController<DreamOverlayContainerView> {
    private final BlurUtils mBlurUtils;
    /* access modifiers changed from: private */
    public boolean mBouncerAnimating;
    private final KeyguardBouncer.BouncerExpansionCallback mBouncerExpansionCallback = new KeyguardBouncer.BouncerExpansionCallback() {
        public void onStartingToShow() {
            boolean unused = DreamOverlayContainerViewController.this.mBouncerAnimating = true;
        }

        public void onStartingToHide() {
            boolean unused = DreamOverlayContainerViewController.this.mBouncerAnimating = true;
        }

        public void onFullyHidden() {
            boolean unused = DreamOverlayContainerViewController.this.mBouncerAnimating = false;
        }

        public void onFullyShown() {
            boolean unused = DreamOverlayContainerViewController.this.mBouncerAnimating = false;
        }

        public void onExpansionChanged(float f) {
            if (DreamOverlayContainerViewController.this.mBouncerAnimating) {
                DreamOverlayContainerViewController.this.updateTransitionState(f);
            }
        }

        public void onVisibilityChanged(boolean z) {
            if (!z) {
                DreamOverlayContainerViewController.this.updateTransitionState(1.0f);
            }
        }
    };
    private final long mBurnInProtectionUpdateInterval;
    private final ComplicationHostViewController mComplicationHostViewController;
    private final ViewGroup mDreamOverlayContentView;
    private final int mDreamOverlayMaxTranslationY;
    private final Handler mHandler;
    private long mJitterStartTimeMillis;
    private final int mMaxBurnInOffset;
    private final long mMillisUntilFullJitter;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final DreamOverlayStatusBarViewController mStatusBarViewController;

    @Inject
    public DreamOverlayContainerViewController(DreamOverlayContainerView dreamOverlayContainerView, ComplicationHostViewController complicationHostViewController, @Named("dream_overlay_content_view") ViewGroup viewGroup, DreamOverlayStatusBarViewController dreamOverlayStatusBarViewController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, BlurUtils blurUtils, @Main Handler handler, @Main Resources resources, @Named("max_burn_in_offset") int i, @Named("burn_in_protection_update_interval") long j, @Named("millis_until_full_jitter") long j2) {
        super(dreamOverlayContainerView);
        this.mDreamOverlayContentView = viewGroup;
        this.mStatusBarViewController = dreamOverlayStatusBarViewController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mBlurUtils = blurUtils;
        this.mComplicationHostViewController = complicationHostViewController;
        this.mDreamOverlayMaxTranslationY = resources.getDimensionPixelSize(C1894R.dimen.dream_overlay_y_offset);
        viewGroup.addView(complicationHostViewController.getView(), new ViewGroup.LayoutParams(-1, -1));
        this.mHandler = handler;
        this.mMaxBurnInOffset = i;
        this.mBurnInProtectionUpdateInterval = j;
        this.mMillisUntilFullJitter = j2;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.mStatusBarViewController.init();
        this.mComplicationHostViewController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mJitterStartTimeMillis = System.currentTimeMillis();
        this.mHandler.postDelayed(new DreamOverlayContainerViewController$$ExternalSyntheticLambda0(this), this.mBurnInProtectionUpdateInterval);
        KeyguardBouncer bouncer = this.mStatusBarKeyguardViewManager.getBouncer();
        if (bouncer != null) {
            bouncer.addBouncerExpansionCallback(this.mBouncerExpansionCallback);
        }
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mHandler.removeCallbacks(new DreamOverlayContainerViewController$$ExternalSyntheticLambda0(this));
        KeyguardBouncer bouncer = this.mStatusBarKeyguardViewManager.getBouncer();
        if (bouncer != null) {
            bouncer.removeBouncerExpansionCallback(this.mBouncerExpansionCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public View getContainerView() {
        return this.mView;
    }

    /* access modifiers changed from: private */
    public void updateBurnInOffsets() {
        int i = this.mMaxBurnInOffset;
        long currentTimeMillis = System.currentTimeMillis() - this.mJitterStartTimeMillis;
        long j = this.mMillisUntilFullJitter;
        if (currentTimeMillis < j) {
            i = Math.round(MathUtils.lerp(0.0f, (float) i, ((float) currentTimeMillis) / ((float) j)));
        }
        int i2 = i * 2;
        int burnInOffset = BurnInHelperKt.getBurnInOffset(i2, true) - i;
        int burnInOffset2 = BurnInHelperKt.getBurnInOffset(i2, false) - i;
        ((DreamOverlayContainerView) this.mView).setTranslationX((float) burnInOffset);
        ((DreamOverlayContainerView) this.mView).setTranslationY((float) burnInOffset2);
        this.mHandler.postDelayed(new DreamOverlayContainerViewController$$ExternalSyntheticLambda0(this), this.mBurnInProtectionUpdateInterval);
    }

    /* access modifiers changed from: private */
    public void updateTransitionState(float f) {
        for (Integer intValue : Arrays.asList(1, 2)) {
            int intValue2 = intValue.intValue();
            this.mComplicationHostViewController.getViewsAtPosition(intValue2).forEach(new DreamOverlayContainerViewController$$ExternalSyntheticLambda1(getAlpha(intValue2, f), getTranslationY(intValue2, f)));
        }
        this.mBlurUtils.applyBlur(((DreamOverlayContainerView) this.mView).getViewRootImpl(), (int) this.mBlurUtils.blurRadiusOfRatio(1.0f - BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f)), false);
    }

    static /* synthetic */ void lambda$updateTransitionState$0(float f, float f2, View view) {
        view.setAlpha(f);
        view.setTranslationY(f2);
    }

    private static float getAlpha(int i, float f) {
        float f2;
        Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        if (i == 1) {
            f2 = BouncerPanelExpansionCalculator.getDreamAlphaScaledExpansion(f);
        } else {
            f2 = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f + 0.03f);
        }
        return interpolator.getInterpolation(f2);
    }

    private float getTranslationY(int i, float f) {
        float f2;
        Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        if (i == 1) {
            f2 = BouncerPanelExpansionCalculator.getDreamYPositionScaledExpansion(f);
        } else {
            f2 = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f + 0.03f);
        }
        return MathUtils.lerp(-this.mDreamOverlayMaxTranslationY, 0, interpolator.getInterpolation(f2));
    }
}
