package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import com.android.systemui.statusbar.LiftReveal;
import com.android.systemui.statusbar.LightRevealScrim;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/biometrics/AuthRippleController$onKeyguardFadingAwayChanged$1$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$onKeyguardFadingAwayChanged$1$2 extends AnimatorListenerAdapter {
    final /* synthetic */ LightRevealScrim $lightRevealScrim;
    final /* synthetic */ AuthRippleController this$0;

    AuthRippleController$onKeyguardFadingAwayChanged$1$2(LightRevealScrim lightRevealScrim, AuthRippleController authRippleController) {
        this.$lightRevealScrim = lightRevealScrim;
        this.this$0 = authRippleController;
    }

    public void onAnimationEnd(Animator animator) {
        this.$lightRevealScrim.setRevealAmount(1.0f);
        if (Intrinsics.areEqual((Object) this.$lightRevealScrim.getRevealEffect(), (Object) this.this$0.circleReveal)) {
            this.$lightRevealScrim.setRevealEffect(LiftReveal.INSTANCE);
        }
        this.this$0.setLightRevealScrimAnimator((ValueAnimator) null);
    }
}
