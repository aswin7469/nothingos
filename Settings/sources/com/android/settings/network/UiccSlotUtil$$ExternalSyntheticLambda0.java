package com.android.settings.network;

import android.telephony.UiccSlotMapping;
import java.util.Collection;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UiccSlotUtil$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ Collection f$0;

    public /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda0(Collection collection) {
        this.f$0 = collection;
    }

    public final boolean test(Object obj) {
        return UiccSlotUtil.lambda$getExcludedLogicalSlotIndex$4(this.f$0, (UiccSlotMapping) obj);
    }
}
