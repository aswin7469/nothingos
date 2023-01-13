package com.android.p019wm.shell.bubbles;

import android.util.ArrayMap;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import com.android.p019wm.shell.bubbles.animation.AnimatableScaleMatrix;

/* renamed from: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda13 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda13 implements PhysicsAnimator.UpdateListener {
    public final /* synthetic */ BubbleStackView f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda13(BubbleStackView bubbleStackView, boolean z, float f) {
        this.f$0 = bubbleStackView;
        this.f$1 = z;
        this.f$2 = f;
    }

    public final void onAnimationUpdateForProperty(Object obj, ArrayMap arrayMap) {
        this.f$0.mo48686x1174b7ab(this.f$1, this.f$2, (AnimatableScaleMatrix) obj, arrayMap);
    }
}
