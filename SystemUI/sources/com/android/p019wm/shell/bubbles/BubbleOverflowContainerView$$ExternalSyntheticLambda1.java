package com.android.p019wm.shell.bubbles;

import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.bubbles.BubbleOverflowContainerView$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleOverflowContainerView$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ BubbleController f$0;

    public /* synthetic */ BubbleOverflowContainerView$$ExternalSyntheticLambda1(BubbleController bubbleController) {
        this.f$0 = bubbleController;
    }

    public final void accept(Object obj) {
        this.f$0.promoteBubbleFromOverflow((Bubble) obj);
    }
}
