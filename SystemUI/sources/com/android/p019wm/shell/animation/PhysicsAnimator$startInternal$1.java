package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.FrameCallbackScheduler;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "", "T", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimator$startInternal$1 */
/* compiled from: PhysicsAnimator.kt */
final class PhysicsAnimator$startInternal$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ FloatPropertyCompat<? super T> $animatedProperty;
    final /* synthetic */ float $currentValue;
    final /* synthetic */ PhysicsAnimator.FlingConfig $flingConfig;
    final /* synthetic */ T $target;
    final /* synthetic */ PhysicsAnimator<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PhysicsAnimator$startInternal$1(PhysicsAnimator.FlingConfig flingConfig, PhysicsAnimator<T> physicsAnimator, FloatPropertyCompat<? super T> floatPropertyCompat, T t, float f) {
        super(0);
        this.$flingConfig = flingConfig;
        this.this$0 = physicsAnimator;
        this.$animatedProperty = floatPropertyCompat;
        this.$target = t;
        this.$currentValue = f;
    }

    public final void invoke() {
        PhysicsAnimator.FlingConfig flingConfig = this.$flingConfig;
        float f = this.$currentValue;
        flingConfig.setMin(Math.min(f, flingConfig.getMin()));
        flingConfig.setMax(Math.max(f, flingConfig.getMax()));
        this.this$0.cancel(this.$animatedProperty);
        FlingAnimation access$getFlingAnimation = this.this$0.getFlingAnimation(this.$animatedProperty, this.$target);
        FrameCallbackScheduler access$getCustomScheduler$p = this.this$0.customScheduler;
        if (access$getCustomScheduler$p == null) {
            access$getCustomScheduler$p = access$getFlingAnimation.getScheduler();
            Intrinsics.checkNotNullExpressionValue(access$getCustomScheduler$p, "flingAnim.scheduler");
        }
        access$getFlingAnimation.setScheduler(access$getCustomScheduler$p);
        this.$flingConfig.applyToAnimation$WMShell_release(access$getFlingAnimation);
        access$getFlingAnimation.start();
    }
}
