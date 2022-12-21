package com.android.p019wm.shell.bubbles;

import java.util.function.Predicate;

/* renamed from: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda6 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda6 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda6(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return ((Bubble) obj).getKey().equals(this.f$0);
    }
}
