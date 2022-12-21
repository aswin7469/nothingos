package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/keyguard/TextAnimator$setTextStyle$listener$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationCancel", "", "animation", "Landroid/animation/Animator;", "onAnimationEnd", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TextAnimator.kt */
public final class TextAnimator$setTextStyle$listener$1 extends AnimatorListenerAdapter {
    final /* synthetic */ Runnable $onAnimationEnd;
    final /* synthetic */ TextAnimator this$0;

    TextAnimator$setTextStyle$listener$1(Runnable runnable, TextAnimator textAnimator) {
        this.$onAnimationEnd = runnable;
        this.this$0 = textAnimator;
    }

    public void onAnimationEnd(Animator animator) {
        this.$onAnimationEnd.run();
        this.this$0.getAnimator$SystemUI_nothingRelease().removeListener(this);
    }

    public void onAnimationCancel(Animator animator) {
        this.this$0.getAnimator$SystemUI_nothingRelease().removeListener(this);
    }
}
