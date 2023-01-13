package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/statusbar/phone/UnlockedScreenOffAnimationController$animateInKeyguard$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationCancel", "", "animation", "Landroid/animation/Animator;", "onAnimationStart", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController$animateInKeyguard$2 extends AnimatorListenerAdapter {
    final /* synthetic */ View $keyguardView;
    final /* synthetic */ UnlockedScreenOffAnimationController this$0;

    UnlockedScreenOffAnimationController$animateInKeyguard$2(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, View view) {
        this.this$0 = unlockedScreenOffAnimationController;
        this.$keyguardView = view;
    }

    public void onAnimationCancel(Animator animator) {
        this.this$0.aodUiAnimationPlaying = false;
        this.this$0.decidedToAnimateGoingToSleep = null;
        this.$keyguardView.animate().setListener((Animator.AnimatorListener) null);
        this.this$0.interactionJankMonitor.cancel(41);
    }

    public void onAnimationStart(Animator animator) {
        InteractionJankMonitor access$getInteractionJankMonitor$p = this.this$0.interactionJankMonitor;
        CentralSurfaces access$getMCentralSurfaces$p = this.this$0.mCentralSurfaces;
        if (access$getMCentralSurfaces$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
            access$getMCentralSurfaces$p = null;
        }
        access$getInteractionJankMonitor$p.begin(access$getMCentralSurfaces$p.getNotificationShadeWindowView(), 41);
    }
}
