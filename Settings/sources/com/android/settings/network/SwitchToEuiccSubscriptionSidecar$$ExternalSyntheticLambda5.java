package com.android.settings.network;

import android.telephony.UiccCardInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda5(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return SwitchToEuiccSubscriptionSidecar.lambda$isMultipleEnabledProfilesSupported$4(this.f$0, (UiccCardInfo) obj);
    }
}
