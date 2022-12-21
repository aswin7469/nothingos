package com.android.p019wm.shell.bubbles;

import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda13 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda13 implements Runnable {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda13(Consumer consumer, String str) {
        this.f$0 = consumer;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
