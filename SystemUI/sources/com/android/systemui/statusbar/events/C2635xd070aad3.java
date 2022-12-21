package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.FrameLayout;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/events/SystemEventChipAnimationController$onSystemEventAnimationFinish$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.events.SystemEventChipAnimationController$onSystemEventAnimationFinish$1 */
/* compiled from: SystemEventChipAnimationController.kt */
public final class C2635xd070aad3 extends AnimatorListenerAdapter {
    final /* synthetic */ SystemEventChipAnimationController this$0;

    C2635xd070aad3(SystemEventChipAnimationController systemEventChipAnimationController) {
        this.this$0 = systemEventChipAnimationController;
    }

    public void onAnimationEnd(Animator animator) {
        FrameLayout access$getAnimationWindowView$p = this.this$0.animationWindowView;
        if (access$getAnimationWindowView$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            access$getAnimationWindowView$p = null;
        }
        BackgroundAnimatableView access$getCurrentAnimatedView$p = this.this$0.currentAnimatedView;
        Intrinsics.checkNotNull(access$getCurrentAnimatedView$p);
        access$getAnimationWindowView$p.removeView(access$getCurrentAnimatedView$p.getView());
    }
}
