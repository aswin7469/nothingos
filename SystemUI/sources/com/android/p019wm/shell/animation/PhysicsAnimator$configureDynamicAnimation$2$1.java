package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0010\u0010\u0003\u001a\f0\u0004R\b\u0012\u0004\u0012\u0002H\u00020\u0005H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, mo65043d2 = {"<anonymous>", "", "T", "it", "Lcom/android/wm/shell/animation/PhysicsAnimator$InternalListener;", "Lcom/android/wm/shell/animation/PhysicsAnimator;", "invoke", "(Lcom/android/wm/shell/animation/PhysicsAnimator$InternalListener;)Ljava/lang/Boolean;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimator$configureDynamicAnimation$2$1 */
/* compiled from: PhysicsAnimator.kt */
final class PhysicsAnimator$configureDynamicAnimation$2$1 extends Lambda implements Function1<PhysicsAnimator<T>.InternalListener, Boolean> {
    final /* synthetic */ DynamicAnimation<?> $anim;
    final /* synthetic */ boolean $canceled;
    final /* synthetic */ FloatPropertyCompat<? super T> $property;
    final /* synthetic */ float $value;
    final /* synthetic */ float $velocity;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PhysicsAnimator$configureDynamicAnimation$2$1(FloatPropertyCompat<? super T> floatPropertyCompat, boolean z, float f, float f2, DynamicAnimation<?> dynamicAnimation) {
        super(1);
        this.$property = floatPropertyCompat;
        this.$canceled = z;
        this.$value = f;
        this.$velocity = f2;
        this.$anim = dynamicAnimation;
    }

    public final Boolean invoke(PhysicsAnimator<T>.InternalListener internalListener) {
        Intrinsics.checkNotNullParameter(internalListener, "it");
        return Boolean.valueOf(internalListener.onInternalAnimationEnd$WMShell_release(this.$property, this.$canceled, this.$value, this.$velocity, this.$anim instanceof FlingAnimation));
    }
}
