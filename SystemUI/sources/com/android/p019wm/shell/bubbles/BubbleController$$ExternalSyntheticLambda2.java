package com.android.p019wm.shell.bubbles;

import com.android.p019wm.shell.bubbles.Bubbles;

/* renamed from: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda2 implements Bubbles.BubbleExpandListener {
    public final /* synthetic */ Bubbles.BubbleExpandListener f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda2(Bubbles.BubbleExpandListener bubbleExpandListener) {
        this.f$0 = bubbleExpandListener;
    }

    public final void onBubbleExpandChanged(boolean z, String str) {
        BubbleController.lambda$setExpandListener$7(this.f$0, z, str);
    }
}
