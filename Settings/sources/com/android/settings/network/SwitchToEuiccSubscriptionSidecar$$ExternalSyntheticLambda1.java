package com.android.settings.network;

import android.telephony.UiccSlotMapping;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda1 implements ToIntFunction {
    public final int applyAsInt(Object obj) {
        return ((UiccSlotMapping) obj).getPortIndex();
    }
}
