package androidx.dynamicanimation.animation;

import android.animation.ValueAnimator;
import androidx.dynamicanimation.animation.AnimationHandler;

/* renamed from: androidx.dynamicanimation.animation.AnimationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0143xf577eeeb implements ValueAnimator.DurationScaleChangeListener {
    public final /* synthetic */ AnimationHandler.DurationScaleChangeListener33 f$0;

    public /* synthetic */ C0143xf577eeeb(AnimationHandler.DurationScaleChangeListener33 durationScaleChangeListener33) {
        this.f$0 = durationScaleChangeListener33;
    }

    public final void onChanged(float f) {
        this.f$0.lambda$register$0(f);
    }
}