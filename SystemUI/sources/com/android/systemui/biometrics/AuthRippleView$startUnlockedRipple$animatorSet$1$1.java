package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/biometrics/AuthRippleView$startUnlockedRipple$animatorSet$1$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "onAnimationStart", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AuthRippleView.kt */
public final class AuthRippleView$startUnlockedRipple$animatorSet$1$1 extends AnimatorListenerAdapter {
    final /* synthetic */ Runnable $onAnimationEnd;
    final /* synthetic */ AuthRippleView this$0;

    AuthRippleView$startUnlockedRipple$animatorSet$1$1(AuthRippleView authRippleView, Runnable runnable) {
        this.this$0 = authRippleView;
        this.$onAnimationEnd = runnable;
    }

    public void onAnimationStart(Animator animator) {
        this.this$0.unlockedRippleInProgress = true;
        this.this$0.rippleShader.setShouldFadeOutRipple(true);
        this.this$0.drawRipple = true;
        this.this$0.setVisibility(0);
    }

    public void onAnimationEnd(Animator animator) {
        Runnable runnable = this.$onAnimationEnd;
        if (runnable != null) {
            runnable.run();
        }
        this.this$0.unlockedRippleInProgress = false;
        this.this$0.drawRipple = false;
        this.this$0.setVisibility(8);
    }
}
