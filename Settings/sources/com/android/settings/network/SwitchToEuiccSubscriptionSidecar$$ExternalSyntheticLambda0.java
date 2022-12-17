package com.android.settings.network;

import android.telephony.UiccSlotMapping;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda0(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return SwitchToEuiccSubscriptionSidecar.lambda$getTargetPortId$0(this.f$0, (UiccSlotMapping) obj);
    }
}
