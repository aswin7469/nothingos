package com.android.p019wm.shell.bubbles;

import java.util.function.Predicate;

/* renamed from: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda10 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda10 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda10(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return ((Bubble) obj).getPackageName().equals(this.f$0);
    }
}
