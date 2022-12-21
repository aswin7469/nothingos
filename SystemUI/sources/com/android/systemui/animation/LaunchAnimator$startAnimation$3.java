package com.android.systemui.animation;

import android.animation.ValueAnimator;
import com.android.systemui.animation.LaunchAnimator;
import kotlin.Metadata;
import kotlin.jvm.internal.Ref;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/animation/LaunchAnimator$startAnimation$3", "Lcom/android/systemui/animation/LaunchAnimator$Animation;", "cancel", "", "animation_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LaunchAnimator.kt */
public final class LaunchAnimator$startAnimation$3 implements LaunchAnimator.Animation {
    final /* synthetic */ ValueAnimator $animator;
    final /* synthetic */ Ref.BooleanRef $cancelled;

    LaunchAnimator$startAnimation$3(Ref.BooleanRef booleanRef, ValueAnimator valueAnimator) {
        this.$cancelled = booleanRef;
        this.$animator = valueAnimator;
    }

    public void cancel() {
        this.$cancelled.element = true;
        this.$animator.cancel();
    }
}
