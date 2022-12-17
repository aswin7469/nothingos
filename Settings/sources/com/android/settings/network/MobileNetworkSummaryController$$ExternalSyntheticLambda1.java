package com.android.settings.network;

import com.android.settings.network.helper.SubscriptionAnnotation;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MobileNetworkSummaryController$$ExternalSyntheticLambda1 implements ToIntFunction {
    public final int applyAsInt(Object obj) {
        return ((SubscriptionAnnotation) obj).getSubscriptionId();
    }
}
