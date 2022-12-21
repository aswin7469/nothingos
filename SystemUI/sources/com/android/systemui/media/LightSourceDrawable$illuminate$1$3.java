package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/media/LightSourceDrawable$illuminate$1$3", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LightSourceDrawable.kt */
public final class LightSourceDrawable$illuminate$1$3 extends AnimatorListenerAdapter {
    final /* synthetic */ LightSourceDrawable this$0;

    LightSourceDrawable$illuminate$1$3(LightSourceDrawable lightSourceDrawable) {
        this.this$0 = lightSourceDrawable;
    }

    public void onAnimationEnd(Animator animator) {
        this.this$0.rippleData.setProgress(0.0f);
        this.this$0.rippleAnimation = null;
        this.this$0.invalidateSelf();
    }
}
