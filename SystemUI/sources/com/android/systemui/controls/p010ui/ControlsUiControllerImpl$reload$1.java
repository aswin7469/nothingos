package com.android.systemui.controls.p010ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import androidx.constraintlayout.motion.widget.Key;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/controls/ui/ControlsUiControllerImpl$reload$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl$reload$1 */
/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$reload$1 extends AnimatorListenerAdapter {
    final /* synthetic */ ViewGroup $parent;
    final /* synthetic */ ControlsUiControllerImpl this$0;

    ControlsUiControllerImpl$reload$1(ControlsUiControllerImpl controlsUiControllerImpl, ViewGroup viewGroup) {
        this.this$0 = controlsUiControllerImpl;
        this.$parent = viewGroup;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        this.this$0.controlViewsById.clear();
        this.this$0.controlsById.clear();
        ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
        ViewGroup viewGroup = this.$parent;
        Runnable access$getOnDismiss$p = controlsUiControllerImpl.onDismiss;
        Context context = null;
        if (access$getOnDismiss$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
            access$getOnDismiss$p = null;
        }
        Context access$getActivityContext$p = this.this$0.activityContext;
        if (access$getActivityContext$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        } else {
            context = access$getActivityContext$p;
        }
        controlsUiControllerImpl.show(viewGroup, access$getOnDismiss$p, context);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.$parent, Key.ALPHA, new float[]{0.0f, 1.0f});
        ofFloat.setInterpolator(new DecelerateInterpolator(1.0f));
        ofFloat.setDuration(200);
        ofFloat.start();
    }
}
