package com.android.p019wm.shell.bubbles;

import java.util.Set;
import java.util.function.Predicate;

/* renamed from: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda7 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda7 implements Predicate {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ Set f$1;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda7(String str, Set set) {
        this.f$0 = str;
        this.f$1 = set;
    }

    public final boolean test(Object obj) {
        return BubbleData.lambda$removeBubblesWithInvalidShortcuts$1(this.f$0, this.f$1, (Bubble) obj);
    }
}
