package com.android.p019wm.shell.bubbles;

import android.util.ArrayMap;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import com.android.p019wm.shell.bubbles.animation.AnimatableScaleMatrix;

/* renamed from: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda3 implements PhysicsAnimator.UpdateListener {
    public final /* synthetic */ BubbleStackView f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda3(BubbleStackView bubbleStackView) {
        this.f$0 = bubbleStackView;
    }

    public final void onAnimationUpdateForProperty(Object obj, ArrayMap arrayMap) {
        this.f$0.mo48683x8db15add((AnimatableScaleMatrix) obj, arrayMap);
    }
}
