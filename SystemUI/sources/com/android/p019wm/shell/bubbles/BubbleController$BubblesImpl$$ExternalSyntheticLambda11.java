package com.android.p019wm.shell.bubbles;

import java.util.function.IntConsumer;

/* renamed from: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda11 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ IntConsumer f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda11(IntConsumer intConsumer, int i) {
        this.f$0 = intConsumer;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
