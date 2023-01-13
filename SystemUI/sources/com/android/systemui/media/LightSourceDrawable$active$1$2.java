package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016J\u0012\u0010\f\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\r"}, mo65043d2 = {"com/android/systemui/media/LightSourceDrawable$active$1$2", "Landroid/animation/AnimatorListenerAdapter;", "cancelled", "", "getCancelled", "()Z", "setCancelled", "(Z)V", "onAnimationCancel", "", "animation", "Landroid/animation/Animator;", "onAnimationEnd", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LightSourceDrawable.kt */
public final class LightSourceDrawable$active$1$2 extends AnimatorListenerAdapter {
    private boolean cancelled;
    final /* synthetic */ LightSourceDrawable this$0;

    LightSourceDrawable$active$1$2(LightSourceDrawable lightSourceDrawable) {
        this.this$0 = lightSourceDrawable;
    }

    public final boolean getCancelled() {
        return this.cancelled;
    }

    public final void setCancelled(boolean z) {
        this.cancelled = z;
    }

    public void onAnimationCancel(Animator animator) {
        this.cancelled = true;
    }

    public void onAnimationEnd(Animator animator) {
        if (!this.cancelled) {
            this.this$0.rippleData.setProgress(0.0f);
            this.this$0.rippleData.setAlpha(0.0f);
            this.this$0.rippleAnimation = null;
            this.this$0.invalidateSelf();
        }
    }
}
