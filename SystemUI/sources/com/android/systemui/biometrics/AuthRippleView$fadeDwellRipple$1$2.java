package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/biometrics/AuthRippleView$fadeDwellRipple$1$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "onAnimationStart", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthRippleView.kt */
public final class AuthRippleView$fadeDwellRipple$1$2 extends AnimatorListenerAdapter {
    final /* synthetic */ AuthRippleView this$0;

    AuthRippleView$fadeDwellRipple$1$2(AuthRippleView authRippleView) {
        this.this$0 = authRippleView;
    }

    public void onAnimationStart(Animator animator) {
        Animator access$getRetractDwellAnimator$p = this.this$0.retractDwellAnimator;
        if (access$getRetractDwellAnimator$p != null) {
            access$getRetractDwellAnimator$p.cancel();
        }
        Animator access$getDwellPulseOutAnimator$p = this.this$0.dwellPulseOutAnimator;
        if (access$getDwellPulseOutAnimator$p != null) {
            access$getDwellPulseOutAnimator$p.cancel();
        }
        this.this$0.drawDwell = true;
    }

    public void onAnimationEnd(Animator animator) {
        this.this$0.drawDwell = false;
        this.this$0.resetDwellAlpha();
    }
}
