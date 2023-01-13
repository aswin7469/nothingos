package com.android.systemui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/animation/ViewHierarchyAnimator$Companion$animateRemoval$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "animation_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ViewHierarchyAnimator.kt */
public final class ViewHierarchyAnimator$Companion$animateRemoval$2 extends AnimatorListenerAdapter {
    final /* synthetic */ long $duration;
    final /* synthetic */ ViewGroup $parent;
    final /* synthetic */ View $rootView;

    ViewHierarchyAnimator$Companion$animateRemoval$2(View view, long j, ViewGroup viewGroup) {
        this.$rootView = view;
        this.$duration = j;
        this.$parent = viewGroup;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        this.$rootView.animate().alpha(0.0f).setInterpolator(Interpolators.ALPHA_OUT).setDuration(this.$duration / ((long) 2)).withEndAction(new C1941xb0a19e6d(this.$parent, this.$rootView)).start();
    }

    /* access modifiers changed from: private */
    /* renamed from: onAnimationEnd$lambda-0  reason: not valid java name */
    public static final void m2554onAnimationEnd$lambda0(ViewGroup viewGroup, View view) {
        Intrinsics.checkNotNullParameter(viewGroup, "$parent");
        Intrinsics.checkNotNullParameter(view, "$rootView");
        viewGroup.getOverlay().remove(view);
    }
}
