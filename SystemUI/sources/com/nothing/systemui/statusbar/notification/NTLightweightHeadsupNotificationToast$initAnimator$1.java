package com.nothing.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationToast$initAnimator$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "onAnimationStart", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTLightweightHeadsupNotificationToast.kt */
public final class NTLightweightHeadsupNotificationToast$initAnimator$1 extends AnimatorListenerAdapter {
    final /* synthetic */ NTLightweightHeadsupNotificationToast this$0;

    NTLightweightHeadsupNotificationToast$initAnimator$1(NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast) {
        this.this$0 = nTLightweightHeadsupNotificationToast;
    }

    public void onAnimationStart(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        super.onAnimationStart(animator);
        Log.d(NTLightweightHeadsupNotificationToast.TAG, "inAnimatorSet onAnimationStart: mInset start");
        this.this$0.setAlpha(1.0f);
        this.this$0.setVisibility(0);
        this.this$0.getPopMessageLayout().setAlpha(0.0f);
        this.this$0.getTitleView().setText(C1893R.string.game_mode);
        this.this$0.getContentView().setText(C1893R.string.f263on);
    }

    public void onAnimationEnd(Animator animator) {
        super.onAnimationEnd(animator);
        this.this$0.getPopMessageLayout().setAlpha(1.0f);
    }
}
