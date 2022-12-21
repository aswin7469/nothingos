package com.android.p019wm.shell.bubbles;

import android.view.ViewTreeObserver;

/* renamed from: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda35 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda35 implements ViewTreeObserver.OnDrawListener {
    public final /* synthetic */ BubbleStackView f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda35(BubbleStackView bubbleStackView) {
        this.f$0 = bubbleStackView;
    }

    public final void onDraw() {
        this.f$0.updateSystemGestureExcludeRects();
    }
}
