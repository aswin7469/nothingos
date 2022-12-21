package com.nothing.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.StatsManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView$startFlipOutAnimation$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTLightweightHeadsupNotificationView.kt */
public final class NTLightweightHeadsupNotificationView$startFlipOutAnimation$1 extends AnimatorListenerAdapter {
    final /* synthetic */ NTLightweightHeadsupNotificationView this$0;

    NTLightweightHeadsupNotificationView$startFlipOutAnimation$1(NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView) {
        this.this$0 = nTLightweightHeadsupNotificationView;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        super.onAnimationEnd(animator);
        this.this$0.setAlpha(0.0f);
        this.this$0.getPopMessageLayout().postDelayed(new C4221x13b23fa3(this.this$0), StatsManager.DEFAULT_TIMEOUT_MILLIS);
    }
}
