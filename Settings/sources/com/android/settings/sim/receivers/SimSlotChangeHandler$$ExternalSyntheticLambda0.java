package com.android.settings.sim.receivers;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SimSlotChangeHandler$$ExternalSyntheticLambda0 implements Predicate {
    public final boolean test(Object obj) {
        return ((SubscriptionInfo) obj).isEmbedded();
    }
}
