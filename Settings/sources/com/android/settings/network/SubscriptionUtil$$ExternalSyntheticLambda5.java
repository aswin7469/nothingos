package com.android.settings.network;

import com.android.settings.network.SubscriptionUtil;
import java.util.Set;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda5(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return SubscriptionUtil.lambda$getNtUniqueSubscriptionDisplayNames$23(this.f$0, (SubscriptionUtil.AnonymousClass2DisplayInfo) obj);
    }
}
