package com.android.p019wm.shell.animation;

import android.util.ArrayMap;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import java.util.concurrent.CountDownLatch;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J;\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u00002$\u0010\u0005\u001a \u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u0007\u0012\u0004\u0012\u00020\b0\u0006j\b\u0012\u0004\u0012\u00028\u0000`\tH\u0016¢\u0006\u0002\u0010\n¨\u0006\u000b"}, mo64987d2 = {"com/android/wm/shell/animation/PhysicsAnimatorTestUtils$blockUntilFirstAnimationFrameWhereTrue$1", "Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;", "onAnimationUpdateForProperty", "", "target", "values", "Landroid/util/ArrayMap;", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "Lcom/android/wm/shell/animation/UpdateMap;", "(Ljava/lang/Object;Landroid/util/ArrayMap;)V", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$blockUntilFirstAnimationFrameWhereTrue$1 */
/* compiled from: PhysicsAnimatorTestUtils.kt */
public final class C3357x4cc8474d implements PhysicsAnimator.UpdateListener<T> {
    final /* synthetic */ CountDownLatch $latch;
    final /* synthetic */ Function1<T, Boolean> $predicate;

    C3357x4cc8474d(Function1<? super T, Boolean> function1, CountDownLatch countDownLatch) {
        this.$predicate = function1;
        this.$latch = countDownLatch;
    }

    public void onAnimationUpdateForProperty(T t, ArrayMap<FloatPropertyCompat<? super T>, PhysicsAnimator.AnimationUpdate> arrayMap) {
        Intrinsics.checkNotNullParameter(t, "target");
        Intrinsics.checkNotNullParameter(arrayMap, "values");
        if (this.$predicate.invoke(t).booleanValue()) {
            this.$latch.countDown();
        }
    }
}
