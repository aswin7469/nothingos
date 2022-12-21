package com.android.p019wm.shell.animation;

import com.android.p019wm.shell.animation.PhysicsAnimator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, mo64987d2 = {"<anonymous>", "", "T", "", "update", "Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "invoke", "(Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$verifyAnimationUpdateFrames$2 */
/* compiled from: PhysicsAnimatorTestUtils.kt */
final class PhysicsAnimatorTestUtils$verifyAnimationUpdateFrames$2 extends Lambda implements Function1<PhysicsAnimator.AnimationUpdate, Boolean> {
    final /* synthetic */ Float $value;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PhysicsAnimatorTestUtils$verifyAnimationUpdateFrames$2(Float f) {
        super(1);
        this.$value = f;
    }

    public final Boolean invoke(PhysicsAnimator.AnimationUpdate animationUpdate) {
        Intrinsics.checkNotNullParameter(animationUpdate, "update");
        float value = animationUpdate.getValue();
        Float f = this.$value;
        Intrinsics.checkNotNullExpressionValue(f, "value");
        return Boolean.valueOf(value <= f.floatValue());
    }
}
