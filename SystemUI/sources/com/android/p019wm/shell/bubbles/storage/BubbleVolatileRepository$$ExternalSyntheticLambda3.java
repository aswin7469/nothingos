package com.android.p019wm.shell.bubbles.storage;

import java.util.List;
import java.util.function.Predicate;

/* renamed from: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleVolatileRepository$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ List f$0;

    public /* synthetic */ BubbleVolatileRepository$$ExternalSyntheticLambda3(List list) {
        this.f$0 = list;
    }

    public final boolean test(Object obj) {
        return BubbleVolatileRepository.m3442sanitizeBubbles$lambda6(this.f$0, (BubbleEntity) obj);
    }
}
