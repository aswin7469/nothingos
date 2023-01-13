package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/keyguard/KeyguardUnlockAnimationController$1$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardUnlockAnimationController.kt */
public final class KeyguardUnlockAnimationController$1$2 extends AnimatorListenerAdapter {
    final /* synthetic */ KeyguardUnlockAnimationController this$0;

    KeyguardUnlockAnimationController$1$2(KeyguardUnlockAnimationController keyguardUnlockAnimationController) {
        this.this$0 = keyguardUnlockAnimationController;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        if (this.this$0.surfaceBehindAlpha == 0.0f) {
            ((KeyguardViewMediator) this.this$0.keyguardViewMediator.get()).finishSurfaceBehindRemoteAnimation(false);
        }
    }
}
