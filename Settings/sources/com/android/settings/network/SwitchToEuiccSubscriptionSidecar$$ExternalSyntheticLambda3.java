package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda3 implements ToIntFunction {
    public final int applyAsInt(Object obj) {
        return ((SubscriptionInfo) obj).getPortIndex();
    }
}