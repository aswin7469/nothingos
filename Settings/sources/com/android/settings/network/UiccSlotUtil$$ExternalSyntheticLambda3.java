package com.android.settings.network;

import com.google.common.collect.ImmutableList;
import java.util.function.IntPredicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UiccSlotUtil$$ExternalSyntheticLambda3 implements IntPredicate {
    public final /* synthetic */ ImmutableList f$0;

    public /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda3(ImmutableList immutableList) {
        this.f$0 = immutableList;
    }

    public final boolean test(int i) {
        return UiccSlotUtil.lambda$getEsimSlotId$2(this.f$0, i);
    }
}
