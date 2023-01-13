package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.FrameCallbackScheduler;
import androidx.dynamicanimation.animation.SpringAnimation;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001JM\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u00002\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\bH\u0016¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, mo65043d2 = {"com/android/wm/shell/animation/PhysicsAnimator$startInternal$3", "Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "onAnimationEnd", "", "target", "property", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "wasFling", "", "canceled", "finalValue", "", "finalVelocity", "allRelevantPropertyAnimsEnded", "(Ljava/lang/Object;Landroidx/dynamicanimation/animation/FloatPropertyCompat;ZZFFZ)V", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimator$startInternal$3 */
/* compiled from: PhysicsAnimator.kt */
public final class PhysicsAnimator$startInternal$3 implements PhysicsAnimator.EndListener<T> {
    final /* synthetic */ FloatPropertyCompat<? super T> $animatedProperty;
    final /* synthetic */ float $flingMax;
    final /* synthetic */ float $flingMin;
    final /* synthetic */ PhysicsAnimator.SpringConfig $springConfig;
    final /* synthetic */ PhysicsAnimator<T> this$0;

    PhysicsAnimator$startInternal$3(FloatPropertyCompat<? super T> floatPropertyCompat, float f, float f2, PhysicsAnimator.SpringConfig springConfig, PhysicsAnimator<T> physicsAnimator) {
        this.$animatedProperty = floatPropertyCompat;
        this.$flingMin = f;
        this.$flingMax = f2;
        this.$springConfig = springConfig;
        this.this$0 = physicsAnimator;
    }

    public void onAnimationEnd(T t, FloatPropertyCompat<? super T> floatPropertyCompat, boolean z, boolean z2, float f, float f2, boolean z3) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) this.$animatedProperty) && z && !z2) {
            boolean z4 = true;
            boolean z5 = Math.abs(f2) > 0.0f;
            boolean z6 = !(this.$flingMin <= f && f <= this.$flingMax);
            if (z5 || z6) {
                this.$springConfig.setStartVelocity$WMShell_release(f2);
                if (this.$springConfig.getFinalPosition$WMShell_release() != PhysicsAnimatorKt.UNSET) {
                    z4 = false;
                }
                if (z4) {
                    if (z5) {
                        this.$springConfig.setFinalPosition$WMShell_release(f2 < 0.0f ? this.$flingMin : this.$flingMax);
                    } else if (z6) {
                        PhysicsAnimator.SpringConfig springConfig = this.$springConfig;
                        float f3 = this.$flingMin;
                        if (f >= f3) {
                            f3 = this.$flingMax;
                        }
                        springConfig.setFinalPosition$WMShell_release(f3);
                    }
                }
                SpringAnimation access$getSpringAnimation = this.this$0.getSpringAnimation(this.$animatedProperty, t);
                FrameCallbackScheduler access$getCustomScheduler$p = this.this$0.customScheduler;
                if (access$getCustomScheduler$p == null) {
                    access$getCustomScheduler$p = access$getSpringAnimation.getScheduler();
                    Intrinsics.checkNotNullExpressionValue(access$getCustomScheduler$p, "springAnim.scheduler");
                }
                access$getSpringAnimation.setScheduler(access$getCustomScheduler$p);
                this.$springConfig.applyToAnimation$WMShell_release(access$getSpringAnimation);
                access$getSpringAnimation.start();
            }
        }
    }
}
