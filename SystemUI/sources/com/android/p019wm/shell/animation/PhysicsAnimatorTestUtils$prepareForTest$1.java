package com.android.p019wm.shell.animation;

import com.android.p019wm.shell.animation.PhysicsAnimatorTestUtils;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\u0010\u0000\u001a\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<no name provided>", "Lcom/android/wm/shell/animation/PhysicsAnimator;", "target", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$prepareForTest$1 */
/* compiled from: PhysicsAnimatorTestUtils.kt */
final class PhysicsAnimatorTestUtils$prepareForTest$1 extends Lambda implements Function1<Object, PhysicsAnimator<?>> {
    final /* synthetic */ Function1<Object, PhysicsAnimator<?>> $defaultConstructor;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PhysicsAnimatorTestUtils$prepareForTest$1(Function1<Object, ? extends PhysicsAnimator<?>> function1) {
        super(1);
        this.$defaultConstructor = function1;
    }

    public final PhysicsAnimator<?> invoke(Object obj) {
        Intrinsics.checkNotNullParameter(obj, "target");
        PhysicsAnimator<?> invoke = this.$defaultConstructor.invoke(obj);
        PhysicsAnimatorTestUtils.allAnimatedObjects.add(obj);
        PhysicsAnimatorTestUtils.animatorTestHelpers.put(invoke, new PhysicsAnimatorTestUtils.AnimatorTestHelper(invoke));
        return invoke;
    }
}
