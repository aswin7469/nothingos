package com.android.p019wm.shell.bubbles;

import com.android.p019wm.shell.bubbles.BubbleController;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ Consumer f$1;
    public final /* synthetic */ Executor f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda4(BubbleController.BubblesImpl bubblesImpl, Consumer consumer, Executor executor, String str) {
        this.f$0 = bubblesImpl;
        this.f$1 = consumer;
        this.f$2 = executor;
        this.f$3 = str;
    }

    public final void run() {
        this.f$0.mo48430xf75b9d06(this.f$1, this.f$2, this.f$3);
    }
}
