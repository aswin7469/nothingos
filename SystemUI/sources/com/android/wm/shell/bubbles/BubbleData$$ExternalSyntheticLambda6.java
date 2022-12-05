package com.android.wm.shell.bubbles;

import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda6 implements Function {
    public static final /* synthetic */ BubbleData$$ExternalSyntheticLambda6 INSTANCE = new BubbleData$$ExternalSyntheticLambda6();

    private /* synthetic */ BubbleData$$ExternalSyntheticLambda6() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        long sortKey;
        sortKey = BubbleData.sortKey((Bubble) obj);
        return Long.valueOf(sortKey);
    }
}
