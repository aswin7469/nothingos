package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/DisplayCutoutBaseView$enableShowProtection$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DisplayCutoutBaseView.kt */
public final class DisplayCutoutBaseView$enableShowProtection$2 extends AnimatorListenerAdapter {
    final /* synthetic */ DisplayCutoutBaseView this$0;

    DisplayCutoutBaseView$enableShowProtection$2(DisplayCutoutBaseView displayCutoutBaseView) {
        this.this$0 = displayCutoutBaseView;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        this.this$0.cameraProtectionAnimator = null;
        if (!this.this$0.showProtection) {
            this.this$0.requestLayout();
        }
    }
}
