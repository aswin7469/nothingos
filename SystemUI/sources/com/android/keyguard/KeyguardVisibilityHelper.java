package com.android.keyguard;

import android.view.View;
import android.view.ViewPropertyAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;

public class KeyguardVisibilityHelper {
    private final Runnable mAnimateKeyguardStatusViewGoneEndRunnable = new KeyguardVisibilityHelper$$ExternalSyntheticLambda1(this);
    private final Runnable mAnimateKeyguardStatusViewInvisibleEndRunnable = new KeyguardVisibilityHelper$$ExternalSyntheticLambda0(this);
    private final Runnable mAnimateKeyguardStatusViewVisibleEndRunnable = new KeyguardVisibilityHelper$$ExternalSyntheticLambda2(this);
    private boolean mAnimateYPos;
    private final AnimationProperties mAnimationProperties = new AnimationProperties();
    private final DozeParameters mDozeParameters;
    private final KeyguardStateController mKeyguardStateController;
    private boolean mKeyguardViewVisibilityAnimating;
    private boolean mLastOccludedState = false;
    private final ScreenOffAnimationController mScreenOffAnimationController;
    private View mView;

    public KeyguardVisibilityHelper(View view, KeyguardStateController keyguardStateController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController, boolean z) {
        this.mView = view;
        this.mKeyguardStateController = keyguardStateController;
        this.mDozeParameters = dozeParameters;
        this.mScreenOffAnimationController = screenOffAnimationController;
        this.mAnimateYPos = z;
    }

    public boolean isVisibilityAnimating() {
        return this.mKeyguardViewVisibilityAnimating;
    }

    public void setViewVisibility(int i, boolean z, boolean z2, int i2) {
        this.mView.animate().cancel();
        boolean isOccluded = this.mKeyguardStateController.isOccluded();
        this.mKeyguardViewVisibilityAnimating = false;
        if ((!z && i2 == 1 && i != 1) || z2) {
            this.mKeyguardViewVisibilityAnimating = true;
            this.mView.animate().alpha(0.0f).setStartDelay(0).setDuration(160).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(this.mAnimateKeyguardStatusViewGoneEndRunnable);
            if (z) {
                this.mView.animate().setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).setDuration(this.mKeyguardStateController.getShortenedFadingAwayDuration()).start();
            }
        } else if (i2 == 2 && i == 1) {
            this.mView.setVisibility(0);
            this.mKeyguardViewVisibilityAnimating = true;
            this.mView.setAlpha(0.0f);
            this.mView.animate().alpha(1.0f).setStartDelay(0).setDuration(320).setInterpolator(Interpolators.ALPHA_IN).withEndAction(this.mAnimateKeyguardStatusViewVisibleEndRunnable);
        } else if (i != 1) {
            this.mView.setVisibility(8);
            this.mView.setAlpha(1.0f);
        } else if (z) {
            this.mKeyguardViewVisibilityAnimating = true;
            ViewPropertyAnimator withEndAction = this.mView.animate().alpha(0.0f).setInterpolator(Interpolators.FAST_OUT_LINEAR_IN).withEndAction(this.mAnimateKeyguardStatusViewInvisibleEndRunnable);
            if (this.mAnimateYPos) {
                float y = this.mView.getY() - (((float) this.mView.getHeight()) * 0.05f);
                long j = (long) 125;
                long j2 = (long) 0;
                this.mAnimationProperties.setDuration(j).setDelay(j2);
                PropertyAnimator.cancelAnimation(this.mView, AnimatableProperty.f375Y);
                PropertyAnimator.setProperty(this.mView, AnimatableProperty.f375Y, y, this.mAnimationProperties, true);
                withEndAction.setDuration(j).setStartDelay(j2);
            }
            withEndAction.start();
        } else if (this.mScreenOffAnimationController.shouldAnimateInKeyguard()) {
            this.mKeyguardViewVisibilityAnimating = true;
            this.mScreenOffAnimationController.animateInKeyguard(this.mView, this.mAnimateKeyguardStatusViewVisibleEndRunnable);
        } else if (!this.mLastOccludedState || isOccluded) {
            this.mView.setVisibility(0);
            this.mView.setAlpha(1.0f);
        } else {
            this.mView.setVisibility(0);
            this.mView.setAlpha(0.0f);
            this.mView.animate().setDuration(500).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f).withEndAction(this.mAnimateKeyguardStatusViewVisibleEndRunnable).start();
        }
        this.mLastOccludedState = isOccluded;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardVisibilityHelper  reason: not valid java name */
    public /* synthetic */ void m2305lambda$new$0$comandroidkeyguardKeyguardVisibilityHelper() {
        this.mKeyguardViewVisibilityAnimating = false;
        this.mView.setVisibility(4);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-keyguard-KeyguardVisibilityHelper  reason: not valid java name */
    public /* synthetic */ void m2306lambda$new$1$comandroidkeyguardKeyguardVisibilityHelper() {
        this.mKeyguardViewVisibilityAnimating = false;
        this.mView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-keyguard-KeyguardVisibilityHelper  reason: not valid java name */
    public /* synthetic */ void m2307lambda$new$2$comandroidkeyguardKeyguardVisibilityHelper() {
        this.mKeyguardViewVisibilityAnimating = false;
    }
}
