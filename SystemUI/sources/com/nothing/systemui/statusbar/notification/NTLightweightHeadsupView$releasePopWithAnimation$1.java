package com.nothing.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016J\u0010\u0010\f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\r"}, mo64987d2 = {"com/nothing/systemui/statusbar/notification/NTLightweightHeadsupView$releasePopWithAnimation$1", "Landroid/animation/AnimatorListenerAdapter;", "skip", "", "getSkip", "()Z", "setSkip", "(Z)V", "onAnimationCancel", "", "animation", "Landroid/animation/Animator;", "onAnimationEnd", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTLightweightHeadsupView.kt */
public final class NTLightweightHeadsupView$releasePopWithAnimation$1 extends AnimatorListenerAdapter {
    final /* synthetic */ Runnable $runnable;
    private boolean skip;
    final /* synthetic */ NTLightweightHeadsupView this$0;

    NTLightweightHeadsupView$releasePopWithAnimation$1(NTLightweightHeadsupView nTLightweightHeadsupView, Runnable runnable) {
        this.this$0 = nTLightweightHeadsupView;
        this.$runnable = runnable;
    }

    public final boolean getSkip() {
        return this.skip;
    }

    public final void setSkip(boolean z) {
        this.skip = z;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "animation");
        super.onAnimationEnd(animator);
        if (!this.skip) {
            this.this$0.setVisibility(8);
            Log.d(NTLightweightHeadsupView.TAG, "onAnimationEnd: mAllout setOffset end");
            this.this$0.setReleased(true);
            this.$runnable.run();
        }
    }

    public void onAnimationCancel(Animator animator) {
        super.onAnimationCancel(animator);
        this.skip = true;
    }
}
