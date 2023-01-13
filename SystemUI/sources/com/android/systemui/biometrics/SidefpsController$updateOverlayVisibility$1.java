package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/biometrics/SidefpsController$updateOverlayVisibility$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SidefpsController.kt */
public final class SidefpsController$updateOverlayVisibility$1 extends AnimatorListenerAdapter {
    final /* synthetic */ View $view;
    final /* synthetic */ SidefpsController this$0;

    SidefpsController$updateOverlayVisibility$1(View view, SidefpsController sidefpsController) {
        this.$view = view;
        this.this$0 = sidefpsController;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        this.$view.setVisibility(8);
        this.this$0.overlayHideAnimator = null;
    }
}
