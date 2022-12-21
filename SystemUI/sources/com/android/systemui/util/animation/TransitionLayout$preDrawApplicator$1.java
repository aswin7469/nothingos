package com.android.systemui.util.animation;

import android.view.ViewTreeObserver;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/util/animation/TransitionLayout$preDrawApplicator$1", "Landroid/view/ViewTreeObserver$OnPreDrawListener;", "onPreDraw", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TransitionLayout.kt */
public final class TransitionLayout$preDrawApplicator$1 implements ViewTreeObserver.OnPreDrawListener {
    final /* synthetic */ TransitionLayout this$0;

    TransitionLayout$preDrawApplicator$1(TransitionLayout transitionLayout) {
        this.this$0 = transitionLayout;
    }

    public boolean onPreDraw() {
        this.this$0.updateScheduled = false;
        this.this$0.getViewTreeObserver().removeOnPreDrawListener(this);
        this.this$0.isPreDrawApplicatorRegistered = false;
        this.this$0.applyCurrentState();
        return true;
    }
}
