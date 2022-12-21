package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001JM\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u00002\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\bH\u0016¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, mo64987d2 = {"com/android/wm/shell/animation/PhysicsAnimatorTestUtils$blockUntilAnimationsEnd$1", "Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "onAnimationEnd", "", "target", "property", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "wasFling", "", "canceled", "finalValue", "", "finalVelocity", "allRelevantPropertyAnimsEnded", "(Ljava/lang/Object;Landroidx/dynamicanimation/animation/FloatPropertyCompat;ZZFFZ)V", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$blockUntilAnimationsEnd$1 */
/* compiled from: PhysicsAnimatorTestUtils.kt */
public final class PhysicsAnimatorTestUtils$blockUntilAnimationsEnd$1 implements PhysicsAnimator.EndListener<T> {
    final /* synthetic */ HashSet<FloatPropertyCompat<? super T>> $animatingProperties;
    final /* synthetic */ CountDownLatch $latch;

    PhysicsAnimatorTestUtils$blockUntilAnimationsEnd$1(HashSet<FloatPropertyCompat<? super T>> hashSet, CountDownLatch countDownLatch) {
        this.$animatingProperties = hashSet;
        this.$latch = countDownLatch;
    }

    public void onAnimationEnd(T t, FloatPropertyCompat<? super T> floatPropertyCompat, boolean z, boolean z2, float f, float f2, boolean z3) {
        Intrinsics.checkNotNullParameter(t, "target");
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        if (this.$animatingProperties.contains(floatPropertyCompat)) {
            this.$latch.countDown();
        }
    }
}
