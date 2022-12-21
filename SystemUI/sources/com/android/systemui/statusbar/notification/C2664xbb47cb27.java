package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/notification/ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2 */
/* compiled from: ViewGroupFadeHelper.kt */
public final class C2664xbb47cb27 extends AnimatorListenerAdapter {
    final /* synthetic */ Runnable $endRunnable;

    C2664xbb47cb27(Runnable runnable) {
        this.$endRunnable = runnable;
    }

    public void onAnimationEnd(Animator animator) {
        Runnable runnable = this.$endRunnable;
        if (runnable != null) {
            runnable.run();
        }
    }
}