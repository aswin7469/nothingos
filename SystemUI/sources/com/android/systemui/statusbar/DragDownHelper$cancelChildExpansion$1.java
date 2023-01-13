package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/statusbar/DragDownHelper$cancelChildExpansion$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenShadeTransitionController.kt */
public final class DragDownHelper$cancelChildExpansion$1 extends AnimatorListenerAdapter {
    final /* synthetic */ ExpandableView $child;
    final /* synthetic */ DragDownHelper this$0;

    DragDownHelper$cancelChildExpansion$1(DragDownHelper dragDownHelper, ExpandableView expandableView) {
        this.this$0 = dragDownHelper;
        this.$child = expandableView;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        this.this$0.getExpandCallback().setUserLockedChild(this.$child, false);
    }
}
