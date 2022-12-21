package com.android.p019wm.shell.bubbles.storage;

import java.util.function.Predicate;

/* renamed from: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleVolatileRepository$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ BubbleVolatileRepository$$ExternalSyntheticLambda1(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return BubbleVolatileRepository.m3441removeBubblesForUserWithParent$lambda5(this.f$0, (BubbleEntity) obj);
    }
}
