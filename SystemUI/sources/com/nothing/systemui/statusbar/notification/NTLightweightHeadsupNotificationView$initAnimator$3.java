package com.nothing.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.nothing.systemui.NTDependencyEx;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView$initAnimator$3", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NTLightweightHeadsupNotificationView.kt */
public final class NTLightweightHeadsupNotificationView$initAnimator$3 extends AnimatorListenerAdapter {
    final /* synthetic */ NTLightweightHeadsupNotificationView this$0;

    NTLightweightHeadsupNotificationView$initAnimator$3(NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView) {
        this.this$0 = nTLightweightHeadsupNotificationView;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        super.onAnimationEnd(animator);
        if (this.this$0.row == null) {
            Intrinsics.throwUninitializedPropertyAccessException("row");
        }
        ((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).hidePopNotificationView();
        ExpandableNotificationRow access$getRow$p = this.this$0.row;
        if (access$getRow$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("row");
            access$getRow$p = null;
        }
        access$getRow$p.performClick();
        Log.d(NTLightweightHeadsupNotificationView.TAG, "clickOutAnimatorSet onAnimationEnd: performClick");
        this.this$0.setReleased(true);
    }
}
