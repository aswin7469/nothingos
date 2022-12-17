package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda4(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return SwitchToEuiccSubscriptionSidecar.lambda$isEsimEnabledAtTargetSlotPort$3(this.f$0, (SubscriptionInfo) obj);
    }
}
