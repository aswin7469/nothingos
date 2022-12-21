package com.android.p019wm.shell.bubbles;

import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda15 implements Runnable {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda15(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void run() {
        this.f$0.accept(true);
    }
}
