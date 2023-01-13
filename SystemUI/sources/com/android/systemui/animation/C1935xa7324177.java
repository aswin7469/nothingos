package com.android.systemui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.systemui.animation.AnimatedDialog;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/animation/AnimatedDialog$AnimatedBoundsLayoutListener$onLayoutChange$animator$1$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "animation_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.animation.AnimatedDialog$AnimatedBoundsLayoutListener$onLayoutChange$animator$1$1 */
/* compiled from: DialogLaunchAnimator.kt */
public final class C1935xa7324177 extends AnimatorListenerAdapter {
    final /* synthetic */ AnimatedDialog.AnimatedBoundsLayoutListener this$0;

    C1935xa7324177(AnimatedDialog.AnimatedBoundsLayoutListener animatedBoundsLayoutListener) {
        this.this$0 = animatedBoundsLayoutListener;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        this.this$0.currentAnimator = null;
    }
}
