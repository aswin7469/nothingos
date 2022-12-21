package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016J\u0012\u0010\b\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016J\u0012\u0010\t\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000¨\u0006\n"}, mo64987d2 = {"com/android/systemui/media/MediaHierarchyManager$animator$1$2", "Landroid/animation/AnimatorListenerAdapter;", "cancelled", "", "onAnimationCancel", "", "animation", "Landroid/animation/Animator;", "onAnimationEnd", "onAnimationStart", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaHierarchyManager.kt */
public final class MediaHierarchyManager$animator$1$2 extends AnimatorListenerAdapter {
    private boolean cancelled;
    final /* synthetic */ MediaHierarchyManager this$0;

    MediaHierarchyManager$animator$1$2(MediaHierarchyManager mediaHierarchyManager) {
        this.this$0 = mediaHierarchyManager;
    }

    public void onAnimationCancel(Animator animator) {
        this.cancelled = true;
        this.this$0.animationPending = false;
        View access$getRootView$p = this.this$0.rootView;
        if (access$getRootView$p != null) {
            access$getRootView$p.removeCallbacks(this.this$0.startAnimation);
        }
    }

    public void onAnimationEnd(Animator animator) {
        this.this$0.isCrossFadeAnimatorRunning = false;
        if (!this.cancelled) {
            this.this$0.applyTargetStateIfNotAnimating();
        }
    }

    public void onAnimationStart(Animator animator) {
        this.cancelled = false;
        this.this$0.animationPending = false;
    }
}
