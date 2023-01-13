package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/statusbar/LockscreenShadeTransitionController$setDragDownAmountAnimated$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$setDragDownAmountAnimated$2 extends AnimatorListenerAdapter {
    final /* synthetic */ Function0<Unit> $endlistener;

    LockscreenShadeTransitionController$setDragDownAmountAnimated$2(Function0<Unit> function0) {
        this.$endlistener = function0;
    }

    public void onAnimationEnd(Animator animator) {
        this.$endlistener.invoke();
    }
}
