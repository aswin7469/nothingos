package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.AnimationHandler;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.wm.shell.animation.PhysicsAnimator;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
public final class PhysicsAnimator$startInternal$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ FloatPropertyCompat<? super T> $animatedProperty;
    final /* synthetic */ float $currentValue;
    final /* synthetic */ PhysicsAnimator.FlingConfig $flingConfig;
    final /* synthetic */ T $target;
    final /* synthetic */ PhysicsAnimator<T> this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PhysicsAnimator$startInternal$1(PhysicsAnimator.FlingConfig flingConfig, PhysicsAnimator<T> physicsAnimator, FloatPropertyCompat<? super T> floatPropertyCompat, T t, float f) {
        super(0);
        this.$flingConfig = flingConfig;
        this.this$0 = physicsAnimator;
        this.$animatedProperty = floatPropertyCompat;
        this.$target = t;
        this.$currentValue = f;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        FlingAnimation flingAnimation;
        AnimationHandler animationHandler;
        PhysicsAnimator.FlingConfig flingConfig = this.$flingConfig;
        float f = this.$currentValue;
        flingConfig.setMin(Math.min(f, flingConfig.getMin()));
        flingConfig.setMax(Math.max(f, flingConfig.getMax()));
        this.this$0.cancel(this.$animatedProperty);
        flingAnimation = this.this$0.getFlingAnimation(this.$animatedProperty, this.$target);
        animationHandler = ((PhysicsAnimator) this.this$0).customAnimationHandler;
        if (animationHandler == null) {
            animationHandler = flingAnimation.getAnimationHandler();
            Intrinsics.checkNotNullExpressionValue(animationHandler, "flingAnim.animationHandler");
        }
        flingAnimation.setAnimationHandler(animationHandler);
        this.$flingConfig.applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(flingAnimation);
        flingAnimation.start();
    }
}
