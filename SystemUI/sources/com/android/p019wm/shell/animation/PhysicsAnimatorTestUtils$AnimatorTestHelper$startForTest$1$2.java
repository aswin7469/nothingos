package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import com.android.p019wm.shell.animation.PhysicsAnimatorTestUtils;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001JM\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u00002\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\bH\u0016¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, mo65043d2 = {"com/android/wm/shell/animation/PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$2", "Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "onAnimationEnd", "", "target", "property", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "wasFling", "", "canceled", "finalValue", "", "finalVelocity", "allRelevantPropertyAnimsEnded", "(Ljava/lang/Object;Landroidx/dynamicanimation/animation/FloatPropertyCompat;ZZFFZ)V", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$2 */
/* compiled from: PhysicsAnimatorTestUtils.kt */
public final class PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$2 implements PhysicsAnimator.EndListener<T> {
    final /* synthetic */ CountDownLatch $unblockLatch;
    final /* synthetic */ PhysicsAnimatorTestUtils.AnimatorTestHelper<T> this$0;

    PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$2(PhysicsAnimatorTestUtils.AnimatorTestHelper<T> animatorTestHelper, CountDownLatch countDownLatch) {
        this.this$0 = animatorTestHelper;
        this.$unblockLatch = countDownLatch;
    }

    public void onAnimationEnd(T t, FloatPropertyCompat<? super T> floatPropertyCompat, boolean z, boolean z2, float f, float f2, boolean z3) {
        FloatPropertyCompat<? super T> floatPropertyCompat2 = floatPropertyCompat;
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        Iterator it = this.this$0.testEndListeners.iterator();
        while (it.hasNext()) {
            ((PhysicsAnimator.EndListener) it.next()).onAnimationEnd(t, floatPropertyCompat, z, z2, f, f2, z3);
        }
        if (z3) {
            this.this$0.testEndListeners.clear();
            this.this$0.testUpdateListeners.clear();
            if (PhysicsAnimatorTestUtils.startBlocksUntilAnimationsEnd) {
                this.$unblockLatch.countDown();
            }
        }
    }
}
