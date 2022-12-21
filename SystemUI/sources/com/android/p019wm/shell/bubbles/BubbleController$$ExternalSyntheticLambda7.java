package com.android.p019wm.shell.bubbles;

import com.android.p019wm.shell.onehanded.OneHandedController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda7 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ BubbleController f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda7(BubbleController bubbleController) {
        this.f$0 = bubbleController;
    }

    public final void accept(Object obj) {
        this.f$0.registerOneHandedState((OneHandedController) obj);
    }
}
