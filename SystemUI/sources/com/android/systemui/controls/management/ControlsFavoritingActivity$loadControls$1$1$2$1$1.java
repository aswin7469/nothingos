package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.systemui.C1894R;
import com.android.systemui.controls.TooltipManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsFavoritingActivity$loadControls$1$1$2$1$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$loadControls$1$1$2$1$1 extends AnimatorListenerAdapter {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    ControlsFavoritingActivity$loadControls$1$1$2$1$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public void onAnimationEnd(Animator animator) {
        ManagementPageIndicator access$getPageIndicator$p = this.this$0.pageIndicator;
        ManagementPageIndicator managementPageIndicator = null;
        if (access$getPageIndicator$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
            access$getPageIndicator$p = null;
        }
        if (access$getPageIndicator$p.getVisibility() == 0 && this.this$0.mTooltipManager != null) {
            int[] iArr = new int[2];
            ManagementPageIndicator access$getPageIndicator$p2 = this.this$0.pageIndicator;
            if (access$getPageIndicator$p2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                access$getPageIndicator$p2 = null;
            }
            access$getPageIndicator$p2.getLocationOnScreen(iArr);
            int i = iArr[0];
            ManagementPageIndicator access$getPageIndicator$p3 = this.this$0.pageIndicator;
            if (access$getPageIndicator$p3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                access$getPageIndicator$p3 = null;
            }
            int width = i + (access$getPageIndicator$p3.getWidth() / 2);
            int i2 = iArr[1];
            ManagementPageIndicator access$getPageIndicator$p4 = this.this$0.pageIndicator;
            if (access$getPageIndicator$p4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
            } else {
                managementPageIndicator = access$getPageIndicator$p4;
            }
            int height = i2 + managementPageIndicator.getHeight();
            TooltipManager access$getMTooltipManager$p = this.this$0.mTooltipManager;
            if (access$getMTooltipManager$p != null) {
                access$getMTooltipManager$p.show(C1894R.string.controls_structure_tooltip, width, height);
            }
        }
    }
}
