package com.android.settings.network;

import com.android.settings.network.helper.SubscriptionAnnotation;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MobileNetworkSummaryStatus$$ExternalSyntheticLambda3 implements Predicate {
    public final boolean test(Object obj) {
        return ((SubscriptionAnnotation) obj).isDisplayAllowed();
    }
}
