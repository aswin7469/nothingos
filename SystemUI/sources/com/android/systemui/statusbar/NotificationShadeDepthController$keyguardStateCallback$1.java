package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, mo64987d2 = {"com/android/systemui/statusbar/NotificationShadeDepthController$keyguardStateCallback$1", "Lcom/android/systemui/statusbar/policy/KeyguardStateController$Callback;", "onKeyguardFadingAwayChanged", "", "onKeyguardShowingChanged", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationShadeDepthController.kt */
public final class NotificationShadeDepthController$keyguardStateCallback$1 implements KeyguardStateController.Callback {
    final /* synthetic */ NotificationShadeDepthController this$0;

    NotificationShadeDepthController$keyguardStateCallback$1(NotificationShadeDepthController notificationShadeDepthController) {
        this.this$0 = notificationShadeDepthController;
    }

    public void onKeyguardFadingAwayChanged() {
        if (this.this$0.keyguardStateController.isKeyguardFadingAway() && this.this$0.biometricUnlockController.getMode() == 1) {
            Animator access$getKeyguardAnimator$p = this.this$0.keyguardAnimator;
            if (access$getKeyguardAnimator$p != null) {
                access$getKeyguardAnimator$p.cancel();
            }
            NotificationShadeDepthController notificationShadeDepthController = this.this$0;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            NotificationShadeDepthController notificationShadeDepthController2 = this.this$0;
            ofFloat.setDuration(notificationShadeDepthController2.dozeParameters.getWallpaperFadeOutDuration());
            ofFloat.setStartDelay(notificationShadeDepthController2.keyguardStateController.getKeyguardFadingAwayDelay());
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.addUpdateListener(new C2581x82488ea5(notificationShadeDepthController2));
            ofFloat.addListener(new C2583xd4eb1c55(notificationShadeDepthController2));
            ofFloat.start();
            notificationShadeDepthController.keyguardAnimator = ofFloat;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onKeyguardFadingAwayChanged$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3040onKeyguardFadingAwayChanged$lambda1$lambda0(NotificationShadeDepthController notificationShadeDepthController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(notificationShadeDepthController, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "animation");
        BlurUtils access$getBlurUtils$p = notificationShadeDepthController.blurUtils;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            notificationShadeDepthController.setWakeAndUnlockBlurRadius(access$getBlurUtils$p.blurRadiusOfRatio(((Float) animatedValue).floatValue()));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public void onKeyguardShowingChanged() {
        if (this.this$0.keyguardStateController.isShowing()) {
            Animator access$getKeyguardAnimator$p = this.this$0.keyguardAnimator;
            if (access$getKeyguardAnimator$p != null) {
                access$getKeyguardAnimator$p.cancel();
            }
            Animator access$getNotificationAnimator$p = this.this$0.notificationAnimator;
            if (access$getNotificationAnimator$p != null) {
                access$getNotificationAnimator$p.cancel();
            }
        }
    }
}
